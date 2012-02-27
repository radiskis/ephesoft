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

package com.ephesoft.dcma.core.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ephesoft.dcma.core.common.FileType;
import com.ephesoft.dcma.util.FileUtils;

@Service
public class ZipServiceImpl implements ZipService {

	protected static final Logger LOG = LoggerFactory.getLogger(ZipServiceImpl.class);

	@Override
	public void unzipFiles(File fSourceZip, String destinationDir) throws FileNotFoundException, IOException {
		// Create destination directory if it doesn't exists
		File destDir = new File(destinationDir);
		if (!destDir.exists()) {
			destDir.mkdir();
			LOG.info("destination directory " + destinationDir + " created");
		}
		ZipFile zipFile = new ZipFile(fSourceZip);
		// Extract entries from zip file
		Enumeration<? extends ZipEntry> e = zipFile.entries();
		while (e.hasMoreElements()) {
			ZipEntry entry = (ZipEntry) e.nextElement();
			if (entry != null) {
				String outputFileName = entry.getName();
				// if the entry is directory, leave it.
				if (entry.isDirectory() || outputFileName.endsWith(FileType.ZIP.getExtensionWithDot())) {
					LOG.info("Cann't import entry within zip file. Entry is either a directory or a zip file. Entry : "
							+ outputFileName);
					continue;
				} else {
					File destinationFilePath = new File(destinationDir, outputFileName);
					String newFileName = updateFileName(destinationFilePath.getName(), destinationDir);
					destinationFilePath = new File(destinationDir, newFileName);

					LOG.info("Extracting " + destinationDir);

					// Get the InputStream for current entry of the zip file using getInputStream(Entry entry) method.
					BufferedInputStream bis = new BufferedInputStream(zipFile.getInputStream(entry));

					int data;
					byte buffer[] = new byte[1024];
					// read the current entry from the zip file, extract it and write the extracted file.
					FileOutputStream fos = new FileOutputStream(destinationFilePath);
					BufferedOutputStream bos = new BufferedOutputStream(fos, 1024);

					try {
						while ((data = bis.read(buffer, 0, 1024)) != -1) {
							bos.write(buffer, 0, data);
						}
					} finally {
						// flush the output stream and close it.
						bos.flush();
						bos.close();
						// close the input stream.
						bis.close();
					}
				}
			}
		}
	}

	private String updateFileName(String fileName, String folderPath) {
		if (fileName != null) {
			int extensionIndex = fileName.indexOf('.');
			extensionIndex = extensionIndex == -1 ? fileName.length() : extensionIndex;
			File parentFile = new File(folderPath);
			LOG.info("Updating file name if any file with the same name exists. File : " + fileName);
			fileName = FileUtils.getUpdatedFileNameForDuplicateFile(fileName.substring(0, extensionIndex), parentFile, -1)
					+ fileName.substring(extensionIndex);
			LOG.info("Updated file name : " + fileName);
		}
		return fileName;
	}

	public static void main(String[] args) throws FileNotFoundException, IOException {
		new ZipServiceImpl().unzipFiles(new File("C:\\email.zip"), "C:\\zzzzz");
	}

}
