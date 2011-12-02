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

package com.ephesoft.dcma.mail.service;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.core.DCMAException;
import com.ephesoft.dcma.core.common.CustomMessage;
import com.ephesoft.dcma.core.common.FileType;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.core.service.FileFormatConvertor;
import com.ephesoft.dcma.core.threadpool.BatchInstanceThread;
import com.ephesoft.dcma.da.domain.BatchClass;
import com.ephesoft.dcma.da.domain.BatchClassEmailConfiguration;
import com.ephesoft.dcma.da.service.BatchClassEmailConfigService;
import com.ephesoft.dcma.imagemagick.service.ImageProcessService;
import com.ephesoft.dcma.mail.constants.MailConstants;
import com.ephesoft.dcma.util.CustomFileFilter;
import com.ephesoft.dcma.util.FileUtils;

public class EmailImporterServiceImpl implements EmailImporterService {

	@Autowired
	private BatchClassEmailConfigService batConfigService;

	@Autowired
	@Qualifier("openOfficeConvertor")
	private FileFormatConvertor openOfficeConvertor;

	@Autowired
	private ImageProcessService imageProcessService;

	@Autowired
	private BatchSchemaService batchSchemaService;

	@Autowired
	private MailReceiverService mailReceiverService;

	@Autowired
	private EmailConvertor emailConvertor;

	private String defaultFolderLocation;

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	private String supportedAttachmentExtensions;

	@Override
	public void processMails() {
		defaultFolderLocation = batchSchemaService.getEmailFolderPath();
		List<BatchClassEmailConfiguration> batchClassEmailConfigList = batConfigService.getAllEmailConfigs();

		for (BatchClassEmailConfiguration emailConfiguration : batchClassEmailConfigList) {
			if (emailConfiguration != null && emailConfiguration.getBatchClass() != null) {
				try {
					List<CustomMessage> messages = mailReceiverService.readMails(emailConfiguration);
					if (messages != null && messages.size() > 0) {
						for (CustomMessage message : messages) {
							try {
								String folderPath = message.getFolderPath();
								String folderName = message.getFolderName();
								downloadMail(message, folderPath);
								moveToUNCFolder(emailConfiguration.getBatchClass(), folderPath, folderName);
							} catch (Exception e) {
								logger.error(e.getMessage(), e);
							}
						}
					}
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
	}

	@Override
	public void downloadMail(CustomMessage cm, String folderPath) throws DCMAApplicationException {
		try {
			if (cm != null && cm.getMessage() != null) {
				emailConvertor.convert(cm, folderPath);
				File file = new File(folderPath);
				// This code does not return any files that is being named as start with `.'.
				FilenameFilter filter = new FilenameFilter() {

					public boolean accept(File dir, String name) {
						return !name.equals(MailConstants.TEMP_FILE_NAME + MailConstants.DOT_SEPARATOR + FileType.PDF.getExtension());
					}
				};
				String wantedAttachments[] = file.list(filter);
				if (wantedAttachments != null && wantedAttachments.length > 0) {
					for (String str : wantedAttachments) {
						String fileName = str;
						checkExtensionsAndConvertAttachments(fileName, folderPath);
					}
				}
			}
		} catch (Exception e) {
			logger.error("Unable to read contents of the downloaded message", e);
			throw new DCMAApplicationException(e.getMessage(), e);
		}
	}

	private void checkExtensionsAndConvertAttachments(String fileName, String outputFilePath) throws Exception {
		boolean isFileValid = true;
		if (fileName != null && !fileName.isEmpty()) {
			isFileValid = fileValidityCheck(fileName);
			if (!isFileValid
					&& (!fileName.toLowerCase(Locale.getDefault()).contains(FileType.TIF.getExtension()))
					&& !(fileName.toLowerCase(Locale.getDefault()).contains(FileType.PDF.getExtension()))
					&& !(fileName.toLowerCase(Locale.getDefault()).equalsIgnoreCase(MailConstants.TEMP_FILE_NAME
							+ FileType.TXT.getExtensionWithDot()))) {
				File errorPDFFile = new File(defaultFolderLocation + File.separator + MailConstants.ERROR_FILE);
				try {
					FileUtils.copyFile(errorPDFFile, new File(outputFilePath + File.separator + MailConstants.ZZZZZ_ATTACHMENT_PDF));
				} catch (Exception e) {
					logger.error("Unable to copy file.", e);
				}
			}
		}
		File file = new File(outputFilePath + File.separator + fileName);
		if (!(fileName.toLowerCase(Locale.getDefault()).contains(FileType.TIFF.getExtension())
				|| fileName.toLowerCase(Locale.getDefault()).contains(FileType.TIF.getExtension()) || fileName.toLowerCase().contains(
				FileType.PDF.getExtension()))) {
			outputFilePath = file.getPath();
			if (isFileValid) {
				outputFilePath = FileUtils.changeFileExtension(outputFilePath, FileType.PDF.getExtension());
				String inputFileURL = batchSchemaService.getHttpEmailFolderPath() + "/" + file.getParentFile().getName() + "/"
						+ file.getName();
				openOfficeConvertor.convert(inputFileURL, outputFilePath, FileType.PDF);
				// Uncomment following line to delete the file after converting to 'pdf'.
				// file.delete();
			}
		}
	}

	private void convertPdfOrMultiPageTiffToTiff(BatchClass batchClass, String folderPath) throws DCMAException {
		String filelist[] = null;
		File dir = new File(folderPath);
		filelist = dir.list(new CustomFileFilter(false, FileType.PDF.getExtensionWithDot(), FileType.TIF.getExtensionWithDot(),
				FileType.TIFF.getExtensionWithDot()));
		List<File> deleteFileList = new ArrayList<File>();
		BatchInstanceThread batchInstanceThread = new BatchInstanceThread();
		for (int counter = 0; counter < filelist.length; counter++) {
			File imagePath = new File(folderPath + File.separator + filelist[counter]);
			imageProcessService.convertPdfOrMultiPageTiffToTiff(batchClass, imagePath, batchInstanceThread, true);
			deleteFileList.add(imagePath);
		}
		try {
			logger.info("Converting multi page email tiffs using thread pool");
			batchInstanceThread.execute();
			logger.info("Completed multi page email tiffs conversion using thread pool");
		} catch (DCMAApplicationException e) {
			throw new DCMAException(e.getLocalizedMessage(), e);
		}
		for (File file : deleteFileList) {
			logger.info("Performing clean up");
			file.delete();
		}
	}

	private void moveToUNCFolder(BatchClass batchClass, String folderPath, String folderName) throws Exception {
		convertPdfOrMultiPageTiffToTiff(batchClass, folderPath);
		batchSchemaService.copyEmailFolderToUNC(defaultFolderLocation, folderName, batchClass.getIdentifier());
	}

	private boolean fileValidityCheck(String fileName) {
		StringTokenizer tokenizer = new StringTokenizer(supportedAttachmentExtensions, MailConstants.EXTENSION_SEPARATOR_CONSTANT);
		String attachmentExtension = fileName.substring(fileName.lastIndexOf(MailConstants.DOT_SEPARATOR) + 1);
		boolean isFileValid = false;
		while (tokenizer.hasMoreTokens()) {
			String inValidExtension = tokenizer.nextToken();
			if (attachmentExtension.equalsIgnoreCase(inValidExtension)) {
				isFileValid = true;
				break;
			}
		}

		return isFileValid;
	}

	public void setSupportedAttachmentExtensions(String supportedAttachmentExtensions) {
		this.supportedAttachmentExtensions = supportedAttachmentExtensions;
	}

}
