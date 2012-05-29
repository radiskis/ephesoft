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

package com.ephesoft.dcma.gwt.customWorkflow.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.ClassPathResource;

import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.core.common.FileType;
import com.ephesoft.dcma.gwt.core.server.DCMAHttpServlet;
import com.ephesoft.dcma.gwt.customWorkflow.client.i18n.CustomWorkflowConstants;
import com.ephesoft.dcma.util.FileUtils;

public class ImportPluginUploadServlet extends DCMAHttpServlet {

	private static final String CAUSE = "cause:";

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String META_INF_APPLICATION_PROPERTIES = "META-INF\\application.properties";
	private static final String PLUGIN_UPLOAD_PROPERTY_NAME = "plugin_upload_folder_path";

	private static final String JAR_EXT = FileType.JAR.getExtensionWithDot();

	private static final String XML_EXT = FileType.XML.getExtensionWithDot();

	private boolean validZipContent = false;

	String tempOutputUnZipDir = "", jarFilePath = "", xmlFilePath = "", zipFileName = "", zipPathname = "";

	@Override
	public final void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		doPost(request, response);
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);
		String lastAttachedZipSourcePath = req.getParameter("lastAttachedZipSourcePath");
		if (lastAttachedZipSourcePath != null && !lastAttachedZipSourcePath.isEmpty()) {
			if (new File(lastAttachedZipSourcePath).exists()) {
				FileUtils.deleteDirectoryAndContentsRecursive(new File(lastAttachedZipSourcePath));
			}
		}
		attachFile(req, resp, batchSchemaService);
	}

	private void attachFile(HttpServletRequest req, HttpServletResponse resp, BatchSchemaService batchSchemaService)
			throws IOException {
		String errorMessageString = "";
		PrintWriter printWriter = resp.getWriter();
		File tempZipFile = null;
		InputStream instream = null;
		OutputStream out = null;
		ZipInputStream zipInputStream = null;

		List<ZipEntry> zipEntries = null;
		if (ServletFileUpload.isMultipartContent(req)) {
			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			String exportSerailizationFolderPath = fetchPropertyFromPropertiesFile(PLUGIN_UPLOAD_PROPERTY_NAME,
					META_INF_APPLICATION_PROPERTIES);
			File exportSerailizationFolder = new File(exportSerailizationFolderPath);
			if (!exportSerailizationFolder.exists()) {
				exportSerailizationFolder.mkdir();
			}

			List<FileItem> items;
			try {
				items = upload.parseRequest(req);
				for (FileItem item : items) {

					if (!item.isFormField() && "importFile".equals(item.getFieldName())) {
						zipFileName = item.getName();
						if (zipFileName != null) {
							zipFileName = zipFileName.substring(zipFileName.lastIndexOf(File.separator) + 1);
						}
						zipPathname = exportSerailizationFolderPath + File.separator + zipFileName;

						// get only the file name not whole path
						zipPathname = zipFileName;
						if (zipFileName != null) {
							zipFileName = FilenameUtils.getName(zipFileName);
						}
						try {
							instream = item.getInputStream();
							tempZipFile = new File(zipPathname);

							if (tempZipFile.exists()) {
								tempZipFile.delete();
							}
							out = new FileOutputStream(tempZipFile);
							byte buf[] = new byte[1024];
							int len;
							while ((len = instream.read(buf)) > 0) {
								out.write(buf, 0, len);
							}
						} catch (FileNotFoundException e) {
							log.error("Unable to create the export folder." + e, e);
							printWriter.write("Unable to create the export folder.Please try again.");

						} catch (IOException e) {
							log.error("Unable to read the file." + e, e);
							printWriter.write("Unable to read the file.Please try again.");
						} finally {
							if (out != null) {
								try {
									out.close();
								} catch (IOException ioe) {
									log.info("Could not close stream for file." + tempZipFile);
								}
							}
							if (instream != null) {
								try {
									instream.close();
								} catch (IOException ioe) {
									log.info("Could not close stream for file." + zipFileName);
								}
							}
						}
					}
				}
			} catch (FileUploadException e) {
				log.error("Unable to read the form contents." + e, e);
				printWriter.write("Unable to read the form contents.Please try again.");
			}

			tempOutputUnZipDir = exportSerailizationFolderPath + File.separator + zipFileName;
			File tempOutputUnZipDirFile = new File(tempOutputUnZipDir);
			try {
				FileUtils.copyFile(tempZipFile, tempOutputUnZipDirFile);

				// FileUtils.unzip(tempZipFile, tempOutputUnZipDir);
			} catch (Exception e) {
				log.error("Unable to copy the file." + e, e);
				printWriter.write("Unable to copy the file.Please try again.");
				tempZipFile.delete();
			}

			zipInputStream = new ZipInputStream(new FileInputStream(tempOutputUnZipDir));
			// new ZipInputStream(new FileInputStream(tempOutputUnZipDir)).getNextEntry();

			zipEntries = new ArrayList<ZipEntry>();
			ZipEntry nextEntry = zipInputStream.getNextEntry();
			while (nextEntry != null) {
				zipEntries.add(nextEntry);
				nextEntry = zipInputStream.getNextEntry();
			}
			errorMessageString = processZipFileContents(zipEntries, zipPathname);

		} else {
			log.error("Request contents type is not supported.");
			printWriter.write("Request contents type is not supported.");
		}
		if (tempZipFile != null) {
			tempZipFile.delete();
		}
		if (validZipContent) {
			String zipFileNameWithoutExtension = tempOutputUnZipDir.substring(0, tempOutputUnZipDir.lastIndexOf('.'));
			printWriter.write(CustomWorkflowConstants.PLUGIN_NAME + zipFileNameWithoutExtension);
			printWriter.append("|");
			printWriter.append(CustomWorkflowConstants.JAR_FILE_PATH).append(jarFilePath);
			printWriter.append("|");
			printWriter.append(CustomWorkflowConstants.XML_FILE_PATH).append(xmlFilePath);
			printWriter.append("|");
			printWriter.flush();
		} else {
			printWriter.write("Error while importing.Please try again." + CAUSE + errorMessageString);
		}

	}

	private String processZipFileContents(List<ZipEntry> zipEntries, String zipPathname) {

		String errorMessageString = "";
		boolean numberOfFilesValid = false;
		boolean jarFileExists = false;
		boolean xmlFileExists = false;
		boolean validName = false;

		if (zipEntries.size() == 2) {
			numberOfFilesValid = true;
		}
		if (numberOfFilesValid) {
			for (ZipEntry zipEntry : zipEntries) {
				String fileName = zipEntry.getName();
				String fileExtension = fileName.substring(fileName.lastIndexOf('.'));
				if (fileExtension.equals(XML_EXT) && !xmlFileExists) {
					xmlFileExists = true;
					xmlFilePath = fileName;
				} else if (fileExtension.equals(JAR_EXT) && !jarFileExists) {
					jarFileExists = true;
					jarFilePath = fileName;
				}
				if (xmlFileExists && jarFileExists) {
					break;
				}
			}
		}

		String zipFileNameWithoutExt = zipFileName.substring(0, zipFileName.lastIndexOf('.'));
		String jarFileNameWithoutExt = jarFilePath.substring(0, jarFilePath.lastIndexOf('.'));
		if (zipFileNameWithoutExt.equals(jarFileNameWithoutExt)) {
			validName = true;
		}else
		{
			validName = false;
		}
		if (numberOfFilesValid && jarFileExists && xmlFileExists && validName) {
			validZipContent = true;

		} else {
			validZipContent = false;
			if (!numberOfFilesValid) {
				errorMessageString = "Error: Selected zip file must contain two files only";
			} else if (!xmlFileExists) {
				errorMessageString = "Error: Selected zip file must contain a xml file";
			} else if (!jarFileExists) {
				errorMessageString = "Error: Selected zip file must contain a jar file";
			} else if (!validName) {
				errorMessageString = "Error: Selected zip file and jar file must have same name";
			} else {
				errorMessageString = "Error: Invalid Zip file content";
			}
		}

		return errorMessageString;
	}

	boolean isFileExists(String fileName, File parentFolder) {
		boolean isFileExists = false;

		for (String currentFileName : parentFolder.list()) {
			if (currentFileName.equals(fileName)) {
				isFileExists = true;
				break;
			}
		}
		return isFileExists;
	}

	/**
	 * API to fetch the specified property from the given property file
	 * 
	 * @param propertyName: {@link String}
	 * @param propertyFileName: {@link String}
	 * @return property {@link String}
	 */
	private String fetchPropertyFromPropertiesFile(String propertyName, String propertyFileName) {
		ClassPathResource classPathResource = new ClassPathResource(propertyFileName);

		FileInputStream fileInputStream = null;
		File propertyFile = null;
		String property = null;
		try {
			propertyFile = classPathResource.getFile();
			Properties properties = new Properties();
			fileInputStream = new FileInputStream(propertyFile);
			properties.load(fileInputStream);
			property = properties.getProperty(propertyName);
		} catch (IOException e) {
			log.error("An Exception occurred while executing the patch." + e.getMessage(), e);
		}

		return property;

	}
}
