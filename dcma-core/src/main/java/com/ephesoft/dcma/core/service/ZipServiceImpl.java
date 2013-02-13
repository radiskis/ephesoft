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

package com.ephesoft.dcma.core.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ephesoft.dcma.core.common.FileType;
import com.ephesoft.dcma.core.constant.CoreConstants;
import com.ephesoft.dcma.util.FileUtils;

/**
 * This class unzips files in specified destination directory.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.core.service.ZipService
 */
@Service
public class ZipServiceImpl implements ZipService {

	/**
	 * DEFAULT_LOCALE Locale.
	 */
	private static final Locale DEFAULT_LOCALE = Locale.getDefault();

	/**
	 * LOGGER to print the logging information.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ZipServiceImpl.class);

	/**
	 * This API unzips files in specified destination directory. Destination directory is created if it doesn't exist. If destination
	 * directory passed is null, zip will be extracted in its parent directory.
	 * 
	 * @param zipFile {@link File} Zip File to be unzipped.
	 * @param destinationDir {@link String} Directory where file is to be unzipped.
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	@Override
	public void unzipFiles(File fSourceZip, String destinationDir) throws FileNotFoundException, IOException {
		String finalDestinationDir = destinationDir;
		LOG.info("Extracting zip file = " + fSourceZip.getAbsolutePath());
		BufferedInputStream bis = null;
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		ZipFile zipFile = null;
		if (null == fSourceZip || !fSourceZip.getName().toLowerCase(DEFAULT_LOCALE).endsWith(FileType.ZIP.getExtensionWithDot())) {
			LOG.info("File is either null or not a valid zip file. File passed = " + fSourceZip);
		} else {
			// Create destination directory if it doesn't exists
			try {
				if (null == finalDestinationDir) {
					finalDestinationDir = fSourceZip.getParent();
					LOG.info("destination Directory null, extracting files to directory = " + finalDestinationDir);
				}
				File destDir = new File(finalDestinationDir);
				if (!destDir.exists()) {
					destDir.mkdir();
					LOG.info("destination directory " + finalDestinationDir + " created");
				}
				zipFile = new ZipFile(fSourceZip);

				// Extract entries from zip file
				Enumeration<? extends ZipEntry> zipEntry = zipFile.entries();
				while (zipEntry.hasMoreElements()) {

					ZipEntry entry = (ZipEntry) zipEntry.nextElement();
					if (entry != null) {
						String outputFileName = entry.getName();

						// if the entry is directory, leave it.
						if (entry.isDirectory() || outputFileName.toLowerCase(DEFAULT_LOCALE).endsWith(FileType.ZIP.getExtensionWithDot())) {
							LOG.info("Cann't import entry within zip file. Entry is either a directory or a zip file. Entry : "
									+ outputFileName);
							continue;

						} else {
							File destinationFilePath = new File(finalDestinationDir, outputFileName);
							String newFileName = updateFileName(destinationFilePath.getName(), finalDestinationDir);
							destinationFilePath = new File(finalDestinationDir, newFileName);

							LOG.info("Extracting file = " + outputFileName);

							// Get the InputStream for current entry of the zip file using getInputStream(Entry entry) method.
							bis = new BufferedInputStream(zipFile.getInputStream(entry));

							// read the current entry from the zip file, extract it and write the extracted file.
							fos = new FileOutputStream(destinationFilePath);
							bos = new BufferedOutputStream(fos, CoreConstants.BUFFER_BYTES);

							int data;
							byte buffer[] = new byte[CoreConstants.BUFFER_BYTES];
							boolean endOfFile = (data = bis.read(buffer, 0, CoreConstants.BUFFER_BYTES)) != -1;
							while (endOfFile) {
								bos.write(buffer, 0, data);
								endOfFile = (data = bis.read(buffer, 0, CoreConstants.BUFFER_BYTES)) != -1;
							}
							bos.flush();
						}
					}
				}
			} finally {
				try {
					LOG.info("Closing input and ouput streams...");
					// flush the output stream and close it.
					if (bos != null) {
						bos.flush();
						bos.close();
					}
					// close the input stream.
					if (bis != null) {
						bis.close();
					}
					if (null != zipFile) {
						zipFile.close();
					}
				} catch (Exception e) {
					LOG.info("Error occurred while closing the stream for extracting zip file....", e);
				}
			}
		}
	}

	private String updateFileName(String fileName, String folderPath) {
		String finalFileName = fileName;
		if (fileName != null) {
			int extensionIndex = fileName.indexOf(CoreConstants.DOT);
			extensionIndex = extensionIndex == -1 ? fileName.length() : extensionIndex;
			File parentFile = new File(folderPath);
			LOG.info("Updating file name if any file with the same name exists. File : " + fileName);
			finalFileName = FileUtils.getUpdatedFileNameForDuplicateFile(fileName.substring(0, extensionIndex), parentFile, -1)
					+ fileName.substring(extensionIndex);
			LOG.info("Updated file name : " + finalFileName);
		}
		return finalFileName;
	}

}
