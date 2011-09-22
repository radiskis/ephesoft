/********************************************************************************* 
* Ephesoft is a Intelligent Document Capture and Mailroom Automation program 
* developed by Ephesoft, Inc. Copyright (C) 2010-2011 Ephesoft Inc. 
* 
* This program is free software; you can redistribute it and/or modify it under 
* the terms of the GNU Affero General Public License version 3 as published by the 
* Free Software Foundation with the addition of the following permission added 
* to Section 15 as permitted in Section 7(a): FOR ANY PART OF THE COVERED WORK 
* IN WHICH THE COPYRIGHT IS OWNED BY EPHESOFT, EPHESOFT DISCLAIMS THE WARRANTY 
* OF NON INFRINGEMENT OF THIRD PARTY RIGHTS. 
* 
* This program is distributed in the hope that it will be useful, but WITHOUT 
* ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS 
* FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more 
* details. 
* 
* You should have received a copy of the GNU Affero General Public License along with 
* this program; if not, see http://www.gnu.org/licenses or write to the Free 
* Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 
* 02110-1301 USA. 
* 
* You can contact Ephesoft, Inc. headquarters at 111 Academy Way, 
* Irvine, CA 92617, USA. or at email address info@ephesoft.com. 
* 
* The interactive user interfaces in modified source and object code versions 
* of this program must display Appropriate Legal Notices, as required under 
* Section 5 of the GNU Affero General Public License version 3. 
* 
* In accordance with Section 7(b) of the GNU Affero General Public License version 3, 
* these Appropriate Legal Notices must retain the display of the "Ephesoft" logo. 
* If the display of the logo is not reasonably feasible for 
* technical reasons, the Appropriate Legal Notices must display the words 
* "Powered by Ephesoft". 
********************************************************************************/ 

package com.ephesoft.dcma.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Stack;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Expand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(FileUtils.class);

	/**
	 * This method deletes a given directory with its content.
	 * 
	 * @param srcPath
	 * @return true if successful false other wise.
	 */
	public static boolean deleteDirectoryAndContents(File srcPath) {

		String files[] = srcPath.list();

		boolean folderDelete = true;

		if (files != null) {
			for (int i = 0; i < files.length; i++) {

				String sFilePath = srcPath.getPath() + File.separator + files[i];
				File fFilePath = new File(sFilePath);
				folderDelete = folderDelete & fFilePath.delete();
			}
		}
		folderDelete = folderDelete & srcPath.delete();
		return folderDelete;
	}

	/**
	 * This method zips the contents of Directory specified into a zip file whose name is provided.
	 * 
	 * @param dir
	 * @param zipfile
	 * @throws IOException
	 * @throws IllegalArgumentException
	 */
	public static void zipDirectory(final String dir, final String zipfile, final boolean excludeBatchXml) throws IOException,
			IllegalArgumentException {
		// Check that the directory is a directory, and get its contents
		File d = new File(dir);
		if (!d.isDirectory())
			throw new IllegalArgumentException("Not a directory:  " + dir);
		String[] entries = d.list();
		byte[] buffer = new byte[4096]; // Create a buffer for copying
		int bytesRead;

		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipfile));

		for (int i = 0; i < entries.length; i++) {
			if (excludeBatchXml && entries[i].contains("batch.xml")) {
				continue;
			}
			File f = new File(d, entries[i]);
			if (f.isDirectory())
				continue;// Ignore directory
			FileInputStream in = new FileInputStream(f); // Stream to read file
			ZipEntry entry = new ZipEntry(f.getName()); // Make a ZipEntry
			out.putNextEntry(entry); // Store entry
			while ((bytesRead = in.read(buffer)) != -1)
				out.write(buffer, 0, bytesRead);
			if (in != null) {
				in.close();
			}
		}
		if (out != null) {
			out.close();
		}
	}

	/**
	 * This method deletes a given directory with its content.
	 * 
	 * @param srcPath
	 * @return true if successful false other wise.
	 */
	public static boolean deleteDirectoryAndContents(String sSrcPath) {
		return deleteContents(sSrcPath, true);
	}

	/**
	 * This method deletes a given directory with its content.
	 * 
	 * @param srcPath
	 * @return true if successful false other wise.
	 */
	public static boolean deleteContents(String sSrcPath, boolean folderDelete) {
		File srcPath = new File(sSrcPath);

		if (null == srcPath || !srcPath.exists()) {
			return false;
		}

		String files[] = srcPath.list();
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				String sFilePath = srcPath.getPath() + File.separator + files[i];
				File fFilePath = new File(sFilePath);
				folderDelete = folderDelete & fFilePath.delete();
			}
			folderDelete = folderDelete & srcPath.delete();
		}
		return folderDelete;
	}

	public static boolean deleteContentsOnly(String srcPath) {
		boolean folderDelete = true;
		File srcPathFile = new File(srcPath);

		if (null == srcPathFile || !srcPathFile.exists()) {
			return false;
		}

		String files[] = srcPathFile.list();
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				String sFilePath = srcPathFile.getPath() + File.separator + files[i];
				File fFilePath = new File(sFilePath);
				folderDelete = folderDelete & fFilePath.delete();
			}
		}
		return folderDelete;
	}

	/**
	 * This method copies the src file to dest file.
	 * 
	 * @param srcFile
	 * @param destFile
	 * @throws Exception
	 */
	public static void copyFile(File srcFile, File destFile) throws Exception {
		InputStream in = null;
		in = new FileInputStream(srcFile);
		OutputStream out = null;
		out = new FileOutputStream(destFile);
		byte[] buf = new byte[1024];
		int len;
		while ((len = in.read(buf)) > 0) {
			out.write(buf, 0, len);
		}
		if (in != null) {
			in.close();
		}
		if (out != null) {
			out.close();
		}

	}

	/**
	 * This methods copies a directory with all its files.
	 * 
	 * @param srcPath
	 * @param dstPath
	 * @throws IOException
	 */
	public static void copyDirectoryWithContents(final File srcPath, final File dstPath) throws IOException {

		if (srcPath.isDirectory()) {
			if (!dstPath.exists()) {
				dstPath.mkdir();
			}

			String[] files = srcPath.list();
			if (files.length > 0) {
				Arrays.sort(files);

				for (int i = 0; i < files.length; i++) {

					copyDirectoryWithContents(new File(srcPath, files[i]), new File(dstPath, files[i]));
				}
			}

		} else {
			if (!srcPath.exists()) {

			} else {
				InputStream in = new FileInputStream(srcPath);
				OutputStream out = new FileOutputStream(dstPath);
				// Transfer bytes from in to out
				byte[] buf = new byte[1024];
				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				if (in != null) {
					in.close();
				}
				if (out != null) {
					out.close();
				}
			}
		}

	}

	/**
	 * This methods copies a directory with all its files.
	 * 
	 * @param srcPath
	 * @param dstPath
	 * @throws IOException
	 */
	public static void copyDirectoryWithContents(final String sSrcPath, final String sDstPath) throws IOException {
		File srcPath = new File(sSrcPath);
		File dstPath = new File(sDstPath);

		if (srcPath.isDirectory()) {
			if (!dstPath.exists()) {
				dstPath.mkdir();
			}

			String[] files = srcPath.list();
			if (files.length > 0) {
				Arrays.sort(files);

				for (int i = 0; i < files.length; i++) {

					copyDirectoryWithContents(new File(srcPath, files[i]), new File(dstPath, files[i]));
				}
			}

		} else {
			if (!srcPath.exists()) {

			} else {
				InputStream in = new FileInputStream(srcPath);
				OutputStream out = new FileOutputStream(dstPath);
				// Transfer bytes from in to out
				byte[] buf = new byte[1024];
				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				if (in != null) {
					in.close();
				}
				if (out != null) {
					out.close();
				}
			}
		}

	}

	public static void deleteAllXMLs(String folderName) {
		File file = new File(folderName);
		if (file.isDirectory()) {
			File[] allFiles = file.listFiles();
			for (int i = 0; i < allFiles.length; i++) {
				if (allFiles[i].getName().endsWith(".xml")) {
					allFiles[i].delete();
				}
			}
		}
	}

	public static void deleteAllHocrFiles(String folderName) {
		File file = new File(folderName);
		if (file.isDirectory()) {
			File[] allFiles = file.listFiles();
			for (int i = 0; i < allFiles.length; i++) {
				if (allFiles[i].getName().endsWith(".html")) {
					allFiles[i].delete();
				}
			}
		}
	}

	public static void copyAllXMLFiles(String fromLoc, String toLoc) {
		File inputFolder = new File(fromLoc);
		File outputFolder = new File(toLoc);
		File[] inputFiles = inputFolder.listFiles();
		for (int i = 0; i < inputFiles.length; i++) {
			if (inputFiles[i].getName().endsWith(".xml")) {
				FileReader in;
				FileWriter out;
				int c;
				try {
					in = new FileReader(inputFiles[i]);
					out = new FileWriter(outputFolder + File.separator + inputFiles[i].getName());
					while ((c = in.read()) != -1) {
						out.write(c);
					}
					if (in != null) {
						in.close();
					}
					if (out != null) {
						out.close();
					}
				} catch (FileNotFoundException e) {
					System.out.println("Exception while reading files:" + e);
				} catch (IOException e) {
					System.out.println("Exception while copying files:" + e);
				}
			}
		}

	}

	public static boolean checkHocrFileExist(String folderLocation) {
		boolean returnValue = false;
		File folderLoc = new File(folderLocation);
		File[] allFiles = folderLoc.listFiles();
		for (int i = 0; i < allFiles.length; i++) {
			if (allFiles[i].getName().endsWith(".html")) {
				returnValue = true;
			}
		}
		return returnValue;
	}

	/**
	 * An utility method to update the properties file.
	 * 
	 * @param propertyFile File
	 * @param propertyMap Map<String, String>
	 * @param comments String
	 * @throws IOException If any of the parameter is null or input property file is not found.
	 */
	public static void updateProperty(File propertyFile, Map<String, String> propertyMap, String comments) throws IOException {

		if (null == propertyFile || null == propertyMap || propertyMap.isEmpty()) {
			throw new IOException("propertyFile/propertyMap is null or empty.");
		}

		Properties properties = new Properties();
		FileInputStream fileInputStream = new FileInputStream(propertyFile);
		properties.load(fileInputStream);

		Set<String> set = propertyMap.keySet();
		Iterator<String> itr = set.iterator();

		while (itr.hasNext()) {
			String key = itr.next();
			String value = propertyMap.get(key);
			properties.setProperty(key, value);
		}

		FileOutputStream fileOutputStream = new FileOutputStream(propertyFile);
		properties.store(fileOutputStream, comments);

	}

	public static String getAbsoluteFilePath(String pathname) {
		assert pathname != null : "Path name is Null, pathname : " + pathname;
		File file = new File(pathname);
		return file.getAbsolutePath();
	}

	public static String changeFileExtension(String fileName, String extension) {
		int indexOf = fileName.lastIndexOf(".");
		int endIndex = fileName.length();
		String substring = fileName.substring(indexOf + 1, endIndex);
		fileName = fileName.replace(substring, extension);
		return fileName;
	}

	/**
	 * This method zips the contents of Directory specified into a zip file whose name is provided.
	 * 
	 * @param dir
	 * @param zipfile
	 * @throws IOException
	 */

	public static void zipDirectory(String dir2zip, ZipOutputStream zout, String dir2zipName) throws IOException {
		File srcDir = new File(dir2zip);
		List<String> fileList = listDirectory(srcDir);
		for (String fileName : fileList) {
			File file = new File(srcDir.getParent(), fileName);
			String zipName = fileName;
			if (File.separatorChar != '/')
				zipName = fileName.replace(File.separatorChar, '/');
			zipName = zipName.substring(zipName.indexOf(dir2zipName + "\\") + 1 + (dir2zipName + "\\").length());

			ZipEntry ze;
			if (file.isFile()) {
				ze = new ZipEntry(zipName);
				ze.setTime(file.lastModified());
				zout.putNextEntry(ze);
				FileInputStream fin = new FileInputStream(file);
				byte[] buffer = new byte[4096];
				for (int n; (n = fin.read(buffer)) > 0;)
					zout.write(buffer, 0, n);
				if (fin != null) {
					fin.close();
				}
			} else {
				ze = new ZipEntry(zipName + '/');
				ze.setTime(file.lastModified());
				zout.putNextEntry(ze);
			}
		}
		if (zout != null) {
			zout.close();
		}
	}

	public static List<String> listDirectory(File directory) throws IOException {

		Stack<String> stack = new Stack<String>();
		List<String> list = new ArrayList<String>();

		// If it's a file, just return itself
		if (directory.isFile()) {
			if (directory.canRead())
				list.add(directory.getName());
			return list;
		}

		// Traverse the directory in width-first manner, no-recursively
		String root = directory.getParent();
		stack.push(directory.getName());
		while (!stack.empty()) {
			String current = (String) stack.pop();
			File curDir = new File(root, current);
			String[] fileList = curDir.list();
			if (fileList != null) {
				for (String entry : fileList) {
					File f = new File(curDir, entry);
					if (f.isFile()) {
						if (f.canRead()) {
							list.add(current + File.separator + entry);
						} else {
							throw new IOException("Can't read file: " + f.getPath());
						}
					} else if (f.isDirectory()) {
						list.add(current + File.separator + entry);
						stack.push(current + File.separator + f.getName());
					} else {
						throw new IOException("Unknown entry: " + f.getPath());
					}
				}
			}
		}
		return list;
	}

	/**
	 * This method deletes a given directory with its content.
	 * 
	 * @param srcPath
	 */
	public static void deleteDirectoryAndContentsRecursive(File srcPath, boolean deleteSrcDir) {
		if (srcPath.exists()) {
			File[] files = srcPath.listFiles();
			if (files != null) {
				for (int index = 0; index < files.length; index++) {
					if (files[index].isDirectory()) {
						deleteDirectoryAndContentsRecursive(files[index], true);
					}
					files[index].delete();
				}
			}
		}
		if (deleteSrcDir) {
			srcPath.delete();
		}
	}

	public static void deleteDirectoryAndContentsRecursive(File srcPath) {
		deleteDirectoryAndContentsRecursive(srcPath, true);
	}

	/**
	 * This method unzips a given directory with its content.
	 * 
	 * @param zipFilepath
	 * @param destinationDir
	 */
	public static void unzip(File zipFile, String destinationDir) {
		File destinationFile = new File(destinationDir);
		if (destinationFile.exists()) {
			destinationFile.delete();
		}
		final class Expander extends Expand {

			public Expander() {
				setProject(new Project());
				getProject().init();
				setTaskType("unzip");
				setTaskName("unzip");
			}
		}
		Expander expander = new Expander();
		expander.setSrc(zipFile);
		expander.setDest(destinationFile);
		expander.execute();
	}

	public static String getFileNameOfTypeFromFolder(String dirLocation, String fileExtOrFolderName) {
		String fileOrFolderName = "";
		File[] listFiles = new File(dirLocation).listFiles();
		if (listFiles != null) {
			for (int index = 0; index < listFiles.length; index++) {
				if (listFiles[index].getName().toLowerCase().indexOf(fileExtOrFolderName.toLowerCase()) > -1) {
					fileOrFolderName = listFiles[index].getPath();
					break;
				}
			}
		}
		return fileOrFolderName;
	}

	public static void createThreadPoolLockFile(String batchInstanceIdentifier, String lockFolderPath, String pluginFolderName)
			throws IOException {
		File lockFolder = new File(lockFolderPath);
		if (!lockFolder.exists()) {
			lockFolder.mkdir();
		}
		File _lockFile = new File(lockFolderPath + File.separator + pluginFolderName);
		boolean isCreateSuccess = _lockFile.createNewFile();
		if (!isCreateSuccess) {
			LOGGER.error("Unable to create lock file for threadpool for pluginName:" + pluginFolderName);
		}
	}

	public static void deleteThreadPoolLockFile(String batchInstanceIdentifier, String lockFolderPath, String pluginFolderName)
			throws IOException {
		File _lockFile = new File(lockFolderPath + File.separator + pluginFolderName);
		boolean isDeleteSuccess = _lockFile.delete();
		if (!isDeleteSuccess) {
			LOGGER.error("Unable to delete lock file for threadpool for pluginName:" + pluginFolderName);
		}
	}

	public static boolean moveDirectoryAndContents(String sourceDirPath, String destDirPath) {
		boolean success = true;
		File sourceDir = new File(sourceDirPath);
		File destDir = new File(destDirPath);
		if (sourceDir.exists() && destDir.exists()) {
			// delete the directory if it already exists
			deleteDirectoryAndContentsRecursive(destDir);
			success = sourceDir.renameTo(destDir);
		}
		return success;
	}

	public static void deleteSelectedFilesFromDirectory(String directoryPath, List<String> filesList) {
		File directory = new File(directoryPath);
		if (directory != null && directory.exists()) {
			for (File file : directory.listFiles()) {
				if (filesList == null || filesList.isEmpty() || !filesList.contains(file.getName())) {
					file.delete();
				}
			}
		}
	}

	/**
	 * This API is creating file if not exists.
	 * 
	 * @param filePath {@link String}
	 * @return isFileCreated
	 */
	public static boolean createFile(String filePath) {
		boolean isFileCreated = false;
		File file = new File(filePath);
		if (file.exists()) {
			isFileCreated = true;
		} else {
			try {
				isFileCreated = file.createNewFile();
			} catch (IOException e) {
				LOGGER.error("Unable to create file" + e.getMessage(), e);
			}
		}
		return isFileCreated;
	}

	/**
	 * This method append the src file to dest file.
	 * 
	 * @param srcFile
	 * @param destFile
	 * @throws Exception
	 */
	public static void appendFile(File srcFile, File destFile) throws Exception {
		InputStream in = null;
		in = new FileInputStream(srcFile);
		FileOutputStream out = new FileOutputStream(destFile, true);
		byte[] buf = new byte[1024];
		int len;
		while ((len = in.read(buf)) > 0) {
			out.write(buf, 0, len);
		}
		if (in != null) {
			in.close();
		}
		if (out != null) {
			out.close();
		}

	}

	/**
	 * This API merging the input files into single output file.
	 * 
	 * @param srcFiles {@link String}
	 * @param destFile {@link String}
	 * @return
	 */
	public static boolean mergeFilesIntoSingleFile(List<String> srcFiles, String destFile) {
		boolean isFileMerged = false;
		File outputFile = new File(destFile);
		for (String string : srcFiles) {
			File inputFile = new File(string);
			try {
				appendFile(inputFile, outputFile);
			} catch (Exception e) {
				LOGGER.error("Error in copying file" + e.getMessage(), e);
			}
		}
		return isFileMerged;
	}
}
