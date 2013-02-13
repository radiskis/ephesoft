/********************************************************************************* 
* Ephesoft is a Intelligent Document Capture and Mailroom Automation program 
* developed by Ephesoft, Inc. Copyright (C) 2010-2012 Ephesoft Inc. 
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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Expand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ephesoft.dcma.constant.UtilConstants;

/**
 * This class is a utility file consisting of various APIs related to different functions that can be performed with a file.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see java.io.FileInputStream
 */
public class FileUtils implements IUtilCommonConstants {

	/**
	 * Logger for logging the messages.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(FileUtils.class);

	/**
	 * This method deletes a given directory with its content.
	 * 
	 * @param srcPath {@link File}
	 * @return true if successful false other wise.
	 */
	public static boolean deleteDirectoryAndContents(File srcPath) {

		String files[] = srcPath.list();

		boolean folderDelete = true;

		if (files != null) {
			for (int index = 0; index < files.length; index++) {

				String sFilePath = srcPath.getPath() + File.separator + files[index];
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
	 * @param dir {@link String}
	 * @param zipfile {@link String}
	 * @param excludeBatchXml boolean
	 * @throws IOException 
	 * @throws IllegalArgumentException in case of error
	 */
	public static void zipDirectory(final String dir, final String zipfile, final boolean excludeBatchXml) throws IOException,
			IllegalArgumentException {
		// Check that the directory is a directory, and get its contents
		File directory = new File(dir);
		if (!directory.isDirectory()) {
			throw new IllegalArgumentException("Not a directory:  " + dir);
		}
		String[] entries = directory.list();
		byte[] buffer = new byte[UtilConstants.BUFFER_CONST]; // Create a buffer for copying
		int bytesRead;

		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipfile));

		for (int index = 0; index < entries.length; index++) {
			if (excludeBatchXml && entries[index].contains(IUtilCommonConstants.BATCH_XML)) {
				continue;
			}
			File file = new File(directory, entries[index]);
			if (file.isDirectory()) {
				continue;// Ignore directory
			}
			FileInputStream input = new FileInputStream(file); // Stream to read file
			ZipEntry entry = new ZipEntry(file.getName()); // Make a ZipEntry
			out.putNextEntry(entry); // Store entry
			bytesRead = input.read(buffer);
			while (bytesRead != -1) {
				out.write(buffer, 0, bytesRead);
				bytesRead = input.read(buffer);
			}
			if (input != null) {
				input.close();
			}
		}
		if (out != null) {
			out.close();
		}
	}

	/**
	 * API to zip list of files to a desired file. Operation aborted if any file is invalid or a directory.
	 * 
	 * @param filePaths {@link List}< {@link String}>
	 * @param outputFilePath {@link String}
	 * @throws IOException in case of error
	 */
	public static void zipMultipleFiles(List<String> filePaths, String outputFilePath) throws IOException {
		LOGGER.info("Zipping files to " + outputFilePath + ".zip file");
		File outputFile = new File(outputFilePath);

		if (outputFile.exists()) {
			LOGGER.info(outputFilePath + " file already exists. Deleting existing and creating a new file.");
			outputFile.delete();
		}

		byte[] buffer = new byte[UtilConstants.BUFFER_CONST]; // Create a buffer for copying
		int bytesRead;
		ZipOutputStream out = null;
		FileInputStream input = null;
		try {
			out = new ZipOutputStream(new FileOutputStream(outputFilePath));
			for (String filePath : filePaths) {
				LOGGER.info("Writing file " + filePath + " into zip file.");

				File file = new File(filePath);
				if (!file.exists() || file.isDirectory()) {
					throw new Exception("Invalid file: " + file.getAbsolutePath()
							+ ". Either file does not exists or it is a directory.");
				}
				input = new FileInputStream(file); // Stream to read file
				ZipEntry entry = new ZipEntry(file.getName()); // Make a ZipEntry
				out.putNextEntry(entry); // Store entry
				bytesRead = input.read(buffer);
				while (bytesRead != -1) {
					out.write(buffer, 0, bytesRead);
					bytesRead = input.read(buffer);
				}

			}

		} catch (Exception e) {
			LOGGER.error("Exception occured while zipping file." + e.getMessage(), e);
		} finally {
			if (input != null) {
				input.close();
			}
			if (out != null) {
				out.close();
			}
		}
	}

	/**
	 * This method deletes a given directory with its content.
	 * 
	 * @param srcPath {@link String}
	 * @return true if successful false other wise.
	 */
	public static boolean deleteDirectoryAndContents(String sSrcPath) {
		return deleteContents(sSrcPath, true);
	}

	/**
	 * This method deletes a given directory with its content.
	 * 
	 * @param srcPath {@link String}
	 * @param folderDelete boolean
	 * @return true if successful false other wise.
	 */
	public static boolean deleteContents(final String sSrcPath, final boolean folderDelete) {
		File srcPath = new File(sSrcPath);
		boolean returnVal = folderDelete;
		if (null == srcPath || !srcPath.exists()) {
			returnVal = false;
		} else {
			String files[] = srcPath.list();
			if (files != null) {
				for (int index = 0; index < files.length; index++) {
					String sFilePath = srcPath.getPath() + File.separator + files[index];
					File fFilePath = new File(sFilePath);
					returnVal = returnVal & fFilePath.delete();
				}
				returnVal = returnVal & srcPath.delete();
			}
		}
		return returnVal;
	}

	/**
	 * This method deletes contents only.
	 * 
	 * @param srcPath {@link String}
	 * @return true if successful false other wise.
	 */
	public static boolean deleteContentsOnly(String srcPath) {
		boolean folderDelete = true;
		File srcPathFile = new File(srcPath);
		if (null == srcPathFile || !srcPathFile.exists()) {
			folderDelete = false;
		} else {
			String files[] = srcPathFile.list();
			if (files != null) {
				for (int index = 0; index < files.length; index++) {
					String sFilePath = srcPathFile.getPath() + File.separator + files[index];
					File fFilePath = new File(sFilePath);
					folderDelete = folderDelete & fFilePath.delete();
				}
			}
		}
		return folderDelete;
	}

	/**
	 * This method copies the src file to dest file.
	 * 
	 * @param srcFile {@link File}
	 * @param destFile {@link File}
	 * @throws IOException in case of error
	 */
	public static void copyFile(File srcFile, File destFile) throws IOException {
		InputStream input = null;
		OutputStream out = null;
		input = new FileInputStream(srcFile);
		out = new FileOutputStream(destFile);
		byte[] buf = new byte[UtilConstants.BUF_CONST];
		int len = input.read(buf);
		while (len > 0) {
			out.write(buf, 0, len);
			len = input.read(buf);
		}
		if (input != null) {
			input.close();
		}
		if (out != null) {
			out.close();
		}

	}

	/**
	 * This methods copies a directory with all its files.
	 * 
	 * @param srcPath {@link File}
	 * @param dstPath {@link File}
	 * @throws IOException in case of error
	 */
	public static void copyDirectoryWithContents(final File srcPath, final File dstPath) throws IOException {

		if (srcPath.isDirectory()) {
			if (!dstPath.exists()) {
				dstPath.mkdir();
			}

			String[] files = srcPath.list();
			if (files.length > 0) {
				Arrays.sort(files);

				for (int index = 0; index < files.length; index++) {

					copyDirectoryWithContents(new File(srcPath, files[index]), new File(dstPath, files[index]));
				}
			}

		} else {
			if (srcPath.exists()) {
				InputStream input = new FileInputStream(srcPath);
				OutputStream out = new FileOutputStream(dstPath);
				// Transfer bytes from in to out
				byte[] buf = new byte[UtilConstants.BUF_CONST];
				int len = input.read(buf);
				while (len > 0) {
					out.write(buf, 0, len);
					len = input.read(buf);
				}
				if (input != null) {
					input.close();
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
	 * @param sSrcPath {@link String}
	 * @param sDstPath {@link String}
	 * @throws IOException in case of error
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

				for (int index = 0; index < files.length; index++) {

					copyDirectoryWithContents(new File(srcPath, files[index]), new File(dstPath, files[index]));
				}
			}

		} else {
			if (srcPath.exists()) {
				InputStream input = new FileInputStream(srcPath);
				OutputStream out = new FileOutputStream(dstPath);
				// Transfer bytes from in to out
				byte[] buf = new byte[UtilConstants.BUF_CONST];
				int len = input.read(buf);
				while (len > 0) {
					out.write(buf, 0, len);
					len = input.read(buf);
				}
				if (input != null) {
					input.close();
				}
				if (out != null) {
					out.close();
				}
			}
		}

	}

	/**
	 * To delete all XML files.
	 * @param folderName {@link String}
	 */
	public static void deleteAllXMLs(String folderName) {
		File file = new File(folderName);
		if (file.isDirectory()) {
			File[] allFiles = file.listFiles();
			for (int index = 0; index < allFiles.length; index++) {
				if (allFiles[index].getName().endsWith(EXTENSION_XML)) {
					allFiles[index].delete();
				}
			}
		}
	}

	/**
	 * To delete all HOCR files.
	 * @param folderName {@link String}
	 */
	public static void deleteAllHocrFiles(String folderName) {
		File file = new File(folderName);
		if (file.isDirectory()) {
			File[] allFiles = file.listFiles();
			for (int index = 0; index < allFiles.length; index++) {
				if (allFiles[index].getName().endsWith(EXTENSION_HTML)) {
					allFiles[index].delete();
				}
			}
		}
	}

	/**
	 * To copy all XML files.
	 * @param fromLoc {@link String}
	 * @param toLoc {@link String}
	 */
	public static void copyAllXMLFiles(String fromLoc, String toLoc) {
		File inputFolder = new File(fromLoc);
		File outputFolder = new File(toLoc);
		File[] inputFiles = inputFolder.listFiles();
		for (int index = 0; index < inputFiles.length; index++) {
			if (inputFiles[index].getName().endsWith(EXTENSION_XML)) {
				FileReader input;
				FileWriter out;
				int character;
				try {
					input = new FileReader(inputFiles[index]);
					out = new FileWriter(outputFolder + File.separator + inputFiles[index].getName());
					character = input.read();
					while (character != -1) {
						out.write(character);
						character = input.read();
					}
					if (input != null) {
						input.close();
					}
					if (out != null) {
						out.close();
					}
				} catch (FileNotFoundException e) {
					LOGGER.error("Exception while reading files:" + e);
				} catch (IOException e) {
					LOGGER.error("Exception while copying files:" + e);
				}
			}
		}

	}

	/**
	 * To check the existence of Hocr files.
	 * @param folderLocation {@link String}
	 * @return boolean
	 */
	public static boolean checkHocrFileExist(String folderLocation) {
		boolean returnValue = false;
		File folderLoc = new File(folderLocation);
		File[] allFiles = folderLoc.listFiles();
		for (int index = 0; index < allFiles.length; index++) {
			if (allFiles[index].getName().endsWith(EXTENSION_HTML)) {
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
	public static void updateProperty(final File propertyFile, final Map<String, String> propertyMap, final String comments)
			throws IOException {
		if (null == propertyFile || null == propertyMap || propertyMap.isEmpty()) {
			throw new IOException("propertyFile/propertyMap is null or empty.");
		}
		String commentsToAdd = HASH_STRING + comments;
		FileInputStream fileInputStream = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader bufferedReader = null;
		List<String> propertiesToWrite = null;
		try {
			fileInputStream = new FileInputStream(propertyFile);
			inputStreamReader = new InputStreamReader(fileInputStream);
			bufferedReader = new BufferedReader(inputStreamReader);
			propertiesToWrite = new ArrayList<String>();
			propertiesToWrite.add(commentsToAdd);
			processPropertyFile(propertyMap, bufferedReader, propertiesToWrite);
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException exception) {
					LOGGER.error("Exception occured while closing bufferedReader :" + exception);
				}
			}
			if (inputStreamReader != null) {
				try {
					inputStreamReader.close();
				} catch (IOException exception) {
					LOGGER.error("Exception while closing input stream :" + exception);
				}
			}
		}
		writeToPropertyFile(propertyFile, propertiesToWrite);
	}

	/**
	 * API to write a list of Strings to a property file.
	 * 
	 * @param propertyFile {@link File}
	 * @param propertiesToWrite {@link List}
	 * @throws IOException
	 */
	private static void writeToPropertyFile(final File propertyFile, final List<String> propertiesToWrite) throws IOException {

		FileWriter fileWriter = null;
		BufferedWriter bufferedWriter = null;
		try {
			fileWriter = new FileWriter(propertyFile, false);
			bufferedWriter = new BufferedWriter(fileWriter);
			for (String lineToWrite : propertiesToWrite) {
				bufferedWriter.write(lineToWrite);
				bufferedWriter.newLine();
			}
		} finally {
			if (bufferedWriter != null) {
				try {
					bufferedWriter.close();
				} catch (IOException exception) {
					LOGGER.error("Exception occured while closing bufferedWriter : " + exception);
				}
			}
			if (fileWriter != null) {
				try {
					fileWriter.close();
				} catch (IOException exception) {
					LOGGER.error("Exception occured while closing fileWriter : " + exception);
				}
			}

		}

	}

	/**
	 * API to process A property file and add all properties along with comment to list.
	 * 
	 * @param propertyMap {@link Map}
	 * @param bufferedReader {@link BufferedReader}
	 * @param propertiesToWrite {@link List}
	 * @throws IOException
	 */
	private static void processPropertyFile(final Map<String, String> propertyMap, final BufferedReader bufferedReader,
			final List<String> propertiesToWrite) throws IOException {
		String strLine = null;
		String key = null;
		String value = null;
		while ((strLine = bufferedReader.readLine()) != null) {
		    strLine = strLine.trim();
			if (strLine.startsWith(HASH_STRING)) {
				propertiesToWrite.add(strLine);
			} else {
				int indexOfDelimeter = strLine.indexOf(EQUAL_TO);
				if (indexOfDelimeter > 0) {
					key = strLine.substring(0, indexOfDelimeter).trim();
					StringBuilder lineToWrite = new StringBuilder(key);
					lineToWrite.append(EQUAL_TO);
					value = propertyMap.get(key);
					if (value != null) {
						lineToWrite.append(value);
					} else {
						lineToWrite.append(strLine
								.substring(indexOfDelimeter + 1));
					}
					propertiesToWrite.add(lineToWrite.toString());
				}
			}
		}
	}

	/**
	 * To get Absolute File Path.
	 * @param pathname {@link String}
	 * @return {@link String}
	 */
	public static String getAbsoluteFilePath(String pathname) {
		assert pathname != null : "Path name is Null, pathname : " + pathname;
		File file = new File(pathname);
		return file.getAbsolutePath();
	}

	/**
	 * To change File Extension.
	 * 
	 * @param fileName {@link String}
	 * @param extension {@link String}
	 * @return {@link String}
	 */
	public static String changeFileExtension(String fileName, String extension) {
		String name = fileName;
		int indexOf = name.lastIndexOf(DOT);
		int endIndex = name.length();
		String substring = name.substring(indexOf + 1, endIndex);
		name = name.replace(substring, extension);
		return name;
	}

	/**
	 * This method zips the contents of Directory specified into a zip file whose name is provided.
	 * 
	 * @param dir2zip {@link String}
	 * @param zout {@link String}
	 * @param dir2zipName {@link String}
	 * @throws IOException in case of error
	 */
	public static void zipDirectory(String dir2zip, ZipOutputStream zout, String dir2zipName) throws IOException {
		File srcDir = new File(dir2zip);
		List<String> fileList = listDirectory(srcDir);
		for (String fileName : fileList) {
			File file = new File(srcDir.getParent(), fileName);
			String zipName = fileName;
			if (File.separatorChar != FORWARD_SLASH) {
				zipName = fileName.replace(File.separatorChar, FORWARD_SLASH);
			}
			zipName = zipName.substring(zipName.indexOf(dir2zipName + BACKWARD_SLASH) + 1 + (dir2zipName + BACKWARD_SLASH).length());

			ZipEntry zipEntry;
			if (file.isFile()) {
				zipEntry = new ZipEntry(zipName);
				zipEntry.setTime(file.lastModified());
				zout.putNextEntry(zipEntry);
				FileInputStream fin = new FileInputStream(file);
				byte[] buffer = new byte[UtilConstants.BUFFER_CONST];
				for (int n; (n = fin.read(buffer)) > 0;) {
					zout.write(buffer, 0, n);
				}
				if (fin != null) {
					fin.close();
				}
			} else {
				zipEntry = new ZipEntry(zipName + FORWARD_SLASH);
				zipEntry.setTime(file.lastModified());
				zout.putNextEntry(zipEntry);
			}
		}
		if (zout != null) {
			zout.close();
		}
	}

	/**
	 * List the contents of directory.
	 * @param directory {@link File}
	 * @return List<String> 
	 * @throws IOException in case of error
	 */
	public static List<String> listDirectory(File directory) throws IOException {
		return listDirectory(directory, true);
	}

	/**
	 * List the contents of directory.
	 * @param directory {@link File}
	 * @param includingDirectory boolean
	 * @return List<String> 
	 * @throws IOException in case of error
	 */
	public static List<String> listDirectory(File directory, boolean includingDirectory) throws IOException {

		Stack<String> stack = new Stack<String>();
		List<String> list = new ArrayList<String>();

		// If it's a file, just return itself
		if (directory.isFile()) {
			if (directory.canRead()) {
				list.add(directory.getName());
			}
		} else {

			// Traverse the directory in width-first manner, no-recursively
			String root = directory.getParent();
			stack.push(directory.getName());
			while (!stack.empty()) {
				String current = (String) stack.pop();
				File curDir = new File(root, current);
				String[] fileList = curDir.list();
				if (fileList != null) {
					for (String entry : fileList) {
						File file = new File(curDir, entry);
						if (file.isFile()) {
							if (file.canRead()) {
								list.add(current + File.separator + entry);
							} else {
								throw new IOException("Can't read file: " + file.getPath());
							}
						} else if (file.isDirectory()) {
							if (includingDirectory) {
								list.add(current + File.separator + entry);
							}
							stack.push(current + File.separator + file.getName());
						} else {
							throw new IOException("Unknown entry: " + file.getPath());
						}
					}
				}
			}
		}
		return list;
	}

	/**
	 * This method deletes a given directory with its content.
	 *  
	 * @param srcPath {@link File}
	 * @param deleteSrcDir boolean
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

	/**
	 * This method deletes a given directory with its content.
	 *  
	 * @param srcPath {@link File}
	 */
	public static void deleteDirectoryAndContentsRecursive(File srcPath) {
		deleteDirectoryAndContentsRecursive(srcPath, true);
	}

	/**
	 * This method unzips a given directory with its content.
	 * 
	 * @param zipFilepath {@link File}
	 * @param destinationDir {@link String}
	 */
	public static void unzip(File zipFile, String destinationDir) {
		File destinationFile = new File(destinationDir);
		if (destinationFile.exists()) {
			destinationFile.delete();
		}
		
		/**
		 * Expander class.
		 */
		final class Expander extends Expand {

			private static final String UNZIP = "unzip";

			public Expander() {
				super();
				setProject(new Project());
				getProject().init();
				setTaskType(UNZIP);
				setTaskName(UNZIP);
			}
		}
		Expander expander = new Expander();
		expander.setSrc(zipFile);
		expander.setDest(destinationFile);
		expander.execute();
	}

	/**
	 * To get File Name of required Type from Folder.
	 * 
	 * @param dirLocation {@link String}
	 * @param fileExtOrFolderName {@link String}
	 * @return {@link String}
	 */
	public static String getFileNameOfTypeFromFolder(String dirLocation, String fileExtOrFolderName) {
		String fileOrFolderName = EMPTY_STRING;
		File[] listFiles = new File(dirLocation).listFiles();
		if (listFiles != null) {
			for (int index = 0; index < listFiles.length; index++) {
				if (listFiles[index].getName().toLowerCase(Locale.getDefault()).indexOf(
						fileExtOrFolderName.toLowerCase(Locale.getDefault())) > -1) {
					fileOrFolderName = listFiles[index].getPath();
					break;
				}
			}
		}
		return fileOrFolderName;
	}

	/**
	 * To create Thread Pool Lock File.
	 * 
	 * @param batchInstanceIdentifier {@link String}
	 * @param lockFolderPath {@link String}
	 * @param pluginFolderName {@link String}
	 * @throws IOException in case of error
	 */
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

	/**
	 * To delete Thread Pool Lock File.
	 * 
	 * @param batchInstanceIdentifier {@link String}
	 * @param lockFolderPath {@link String}
	 * @param pluginFolderName {@link String}
	 * @throws IOException in case of error
	 */
	public static void deleteThreadPoolLockFile(String batchInstanceIdentifier, String lockFolderPath, String pluginFolderName)
			throws IOException {
		File _lockFile = new File(lockFolderPath + File.separator + pluginFolderName);
		boolean isDeleteSuccess = _lockFile.delete();
		if (!isDeleteSuccess) {
			LOGGER.error("Unable to delete lock file for threadpool for pluginName:" + pluginFolderName);
		}
	}

	/**
	 * To move Directory and Contents.
	 * @param sourceDirPath {@link String}
	 * @param destDirPath {@link String}
	 * @return boolean
	 */
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

	/**
	 * To delete Selected Files from Directory.
	 * @param directoryPath {@link String}
	 * @param filesList List<String>
	 */
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
	 * @param srcFile {@link File}
	 * @param destFile {@link File}
	 * @throws IOException in case of error
	 */
	public static void appendFile(File srcFile, File destFile) throws IOException {
		InputStream inputSream = null;
		FileOutputStream out = null;
		try {
			inputSream = new FileInputStream(srcFile);
			out = new FileOutputStream(destFile, true);
			byte[] buf = new byte[UtilConstants.BUF_CONST];
			int len = inputSream.read(buf);
			while (len > 0) {
				out.write(buf, 0, len);
				len = inputSream.read(buf);
			}
		} finally {
			IOUtils.closeQuietly(inputSream);
		}
	}

	/**
	 * This API merging the input files into single output file.
	 * 
	 * @param srcFiles {@link String}
	 * @param destFile {@link String}
	 * @return boolean
	 * @throws IOException in case of error
	 */
	public static boolean mergeFilesIntoSingleFile(List<String> srcFiles, String destFile) throws IOException {
		boolean isFileMerged = false;
		File outputFile = new File(destFile);
		for (String string : srcFiles) {
			File inputFile = new File(string);
			appendFile(inputFile, outputFile);
		}
		return isFileMerged;
	}

	/**
	 * To get output Stream from Zip.
	 * 
	 * @param zipName {@link String}
	 * @param fileName {@link String}
	 * @return {@link InputStream}
	 * @throws FileNotFoundException in case of error
	 * @throws IOException in case of error
	 */
	public static OutputStream getOutputStreamFromZip(final String zipName, final String fileName) throws FileNotFoundException,
			IOException {
		ZipOutputStream stream = null;
		stream = new ZipOutputStream(new FileOutputStream(new File(zipName + ZIP_FILE_EXT)));
		ZipEntry zipEntry = new ZipEntry(fileName);
		stream.putNextEntry(zipEntry);

		return stream;
	}

	/**
	 * To get Input Stream from Zip.
	 * 
	 * @param zipName {@link String}
	 * @param fileName {@link String}
	 * @return {@link InputStream}
	 * @throws FileNotFoundException in case of error
	 * @throws IOException in case of error
	 */
	public static InputStream getInputStreamFromZip(final String zipName, final String fileName) throws FileNotFoundException,
			IOException {
		ZipFile zipFile = new ZipFile(zipName + ZIP_FILE_EXT);
		// InputStream inputStream = zipFile.getInputStream(zipFile.getEntry(fileName));
		return zipFile.getInputStream(zipFile.getEntry(fileName));
	}

	/**
	 * This method checks for the existence of zip files.
	 * 
	 * @param zipFilePath {@link String}
	 * @return boolean
	 */
	public static boolean isZipFileExists(String zipFilePath) {
		File file = new File(zipFilePath + ZIP_FILE_EXT);
		return file.exists();
	}

	/**
	 * This method returns the updated file name if file with same already exists in the parent folder.
	 * 
	 * @param fileName {@link String}
	 * @param parentFolder {@link File}
	 * @return {@link String}
	 */
	public static String getUpdatedFileNameForDuplicateFile(String fileName, File parentFolder) {
		return getUpdatedFileNameForDuplicateFile(fileName, parentFolder, -1);
	}

	/**
	 * This method returns the updated file name if file with same already exists in the parent folder.
	 * 
	 * @param fileName {@link String}
	 * @param parentFolder {@link File}
	 * @param fileCount {@link Integer} Specifies the count to be appended in the file name if file with same name already exists.
	 *            (Initially -1 is passed when method is called first time for a file)
	 * @return {@link String}
	 */
	public static String getUpdatedFileNameForDuplicateFile(String fileName, File parentFolder, int fileCount) {
		String updatedFileName = null;
		if (fileCount < 0) {
			updatedFileName = fileName;
		} else {
			updatedFileName = fileName + UtilConstants.UNDERSCORE + fileCount;
		}
		if (isFileExists(updatedFileName, parentFolder)) {
			updatedFileName = getUpdatedFileNameForDuplicateFile(fileName, parentFolder, fileCount + 1);
		}
		return updatedFileName;
	}

	/**
	 * This method checks if the file with specific filer name already exists in the the parent folder.
	 * 
	 * @param fileName {@link String}
	 * @param parentFolder {@link File}
	 * @return boolean
	 */
	public static boolean isFileExists(String fileName, File parentFolder) {
		LOGGER.info("checking if file with name " + fileName + " exists in folder " + parentFolder.getAbsolutePath());
		File[] fileArray = parentFolder.listFiles();
		String existingFileName = "";
		int extensionIndex = 0;
		boolean fileExists = false;
		if (fileArray != null) {
			for (File file : fileArray) {
				existingFileName = file.getName();
				extensionIndex = existingFileName.indexOf(UtilConstants.DOT);
				extensionIndex = extensionIndex == -1 ? existingFileName.length() : extensionIndex;
				if (existingFileName.substring(0, extensionIndex).equals(fileName)) {
					LOGGER.info("file exists with name " + fileName + " in folder " + parentFolder.getAbsolutePath());
					fileExists = true;
					break;
				}
			}
		}
		return fileExists;
	}

	/**
	 * To clean up Directory.
	 * @param srcPath {@link File}
	 * @return boolean
	 */
	public static boolean cleanUpDirectory(File srcPath) {
		boolean isDeleted = true;
		if (srcPath.exists()) {
			File[] files = srcPath.listFiles();
			if (files != null) {
				for (int index = 0; index < files.length; index++) {
					if (files[index].isDirectory()) {
						isDeleted &= cleanUpDirectory(files[index]);
					}
					isDeleted &= files[index].delete();
					// files[index].
				}
			}
		}
		isDeleted &= srcPath.delete();
		return isDeleted;
	}

	/**
	 * To create OS Independent Path.
	 * @param path {@link String}
	 * @return {@link String}
	 */
	public static String createOSIndependentPath(String path) {

		StringTokenizer tokenizer = new StringTokenizer(path, "/\\");
		StringBuffer OSIndependentfilePath = new StringBuffer();
		boolean isFirst = true;
		while (tokenizer.hasMoreTokens()) {
			if (!isFirst) {
				OSIndependentfilePath.append(File.separator);
			}
			OSIndependentfilePath.append(tokenizer.nextToken());
			isFirst = false;
		}
		return OSIndependentfilePath.toString();
	}

	/**
	 * This API moves file from source path to destination path by creating destination path if it does not exists.
	 * 
	 * @param sourcePath source path of file to be moved
	 * @param destinationPath destination path where file has to be moved
	 * @return operation success
	 * @throws IOException if error occurs
	 * @throws Exception error occurred while copying file from source to destination path
	 */
	public static boolean moveFile(String sourcePath, String destinationPath) throws IOException {
		boolean success = false;
		if (null != sourcePath && null != destinationPath) {
			File sourceFile = new File(sourcePath);
			File destinationFile = new File(destinationPath);

			// Delete the file if already exists
			if (destinationFile.exists()) {
				deleteDirectoryAndContentsRecursive(destinationFile, true);
			}

			// Create directories for destination path
			if (destinationFile.getParentFile() != null) {
				destinationFile.getParentFile().mkdirs();
			}

			// Moving file from source path to destination path
			if (sourceFile.exists() && sourceFile.canWrite()) {
				success = sourceFile.renameTo(destinationFile);
			}

			// Copy file from source to destination path when the source file cannot be deleted
			if (!success && sourceFile.exists() && !sourceFile.canWrite()) {
				success = true;
				if (sourceFile.isDirectory()) {
					copyDirectoryWithContents(sourceFile, destinationFile);
				} else {
					copyFile(sourceFile, destinationFile);
				}
			}
		}
		return success;
	}

	/**
	 * This method checks whether the given file is a directory or file.
	 * 
	 * @param folderDetail - file to be checked
	 * @return boolean
	 */
	public static boolean checkForFile(String filePath) {
		boolean isFile = false;
		if (null != filePath) {
			File file = new File(filePath);
			isFile = file.isFile();
		}
		return isFile;
	}

	/**
	 * Method to replace invalid characters from file name {fileName} by the replace character specified by admin.
	 * 
	 * @param fileName name of the file from which invalid characters are to be replaced.
	 * @param invalidChars array of invalid characters which are to be replaced.
	 * @return {@link String}
	 */
	public static String replaceInvalidFileChars(final String fileName) {
		String finalReplaceChar = IUtilCommonConstants.UNDER_SCORE;
		String[] invalidChars = IUtilCommonConstants.INVALID_FILE_EXTENSIONS.split(IUtilCommonConstants.INVALID_CHAR_SEPARATOR);

		LOGGER.info("Entering removeInvalidFileChars method");
		String updatedFileName = fileName;
		if (fileName != null && !fileName.isEmpty() && invalidChars != null && invalidChars.length > UtilConstants.ZERO) {
			if (finalReplaceChar == null || finalReplaceChar.isEmpty()) {
				LOGGER.info("Replace character not specified. Using default character '-' as a replace character.");
				finalReplaceChar = DEFAULT_REPLACE_CHAR;
			}
			for (String invalidChar : invalidChars) {
				if (finalReplaceChar.equals(invalidChar)) {
					LOGGER
							.info("Replace character not specified or an invalid character. Using default character '-' as a replace character.");
					finalReplaceChar = DEFAULT_REPLACE_CHAR;
				}
				updatedFileName = updatedFileName.replace(invalidChar, finalReplaceChar);
			}
		}
		LOGGER.info("Exiting removeInvalidFileChars method");
		return updatedFileName;
	}

	/**
	 * API to take read write lock on the folder represented by the given folder path.
	 * 
	 * @param folderPath {@link String}
	 * @return true if lock is successfully acquired, else false
	 */
	public static boolean lockFolder(String folderPath) {
		String lockFileName = folderPath + File.separator + "_lock";
		boolean isLockAcquired = true;
		try {
			File lockFile = new File(lockFileName);
			FileChannel fileChannel = new RandomAccessFile(lockFile, "rw").getChannel();

			FileLock lock = fileChannel.lock();

			try {
				if (lock == null) {
					lock = fileChannel.tryLock();
				}
			} catch (OverlappingFileLockException e) {
				LOGGER.trace("File is already locked in this thread or virtual machine");
			}
			if (lock == null) {
				LOGGER.trace("File is already locked in this thread or virtual machine");
			}

		} catch (Exception e) {
			LOGGER.error("Unable to aquire lock on file : " + lockFileName, e);
			isLockAcquired = false;
		}
		return isLockAcquired;
	}

	/**
	 * API to get the size of a file in mega bytes.
	 * 
	 * @param file {@link File} file whose size is to be calculated
	 * @return
	 */
	public static double getSizeInMB(File file) {
		double sizeInMb = 0.0;
		if (file != null && file.exists()) {
			sizeInMb = file.length() * 1.0 / BYTES_IN_ONE_MB;
		}
		return sizeInMb;
	}
}
