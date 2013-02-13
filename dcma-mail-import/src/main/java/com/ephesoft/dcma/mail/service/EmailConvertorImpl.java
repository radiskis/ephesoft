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

package com.ephesoft.dcma.mail.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;

import javax.mail.Multipart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.core.common.CustomMessage;
import com.ephesoft.dcma.core.common.FileType;
import com.ephesoft.dcma.core.common.MailMetaData;
import com.ephesoft.dcma.core.component.ICommonConstants;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.core.service.FileFormatConvertor;
import com.ephesoft.dcma.mail.constants.MailConstants;
import com.ephesoft.dcma.util.FileUtils;

/**
 * This Class converts the email into desired format of test or html.
 * 
 * @author Ephesoft
 *
 */
public class EmailConvertorImpl implements EmailConvertor {

	/**
	 * An instance of Logger for proper logging.
	 */
	protected static final Logger LOGGER = LoggerFactory.getLogger(EmailConvertorImpl.class);

	/**
	 * An instance of {@link FileFormatConvertor}.
	 */
	@Autowired
	@Qualifier("openOfficeConvertor")
	private FileFormatConvertor openOfficeConvertor;

	/**
	 * An instance of {@link BatchSchemaService}.
	 */
	@Autowired
	private BatchSchemaService batchSchemaService;

	@Override
	public void convert(CustomMessage customMsg, URI outputFileURI) throws DCMAApplicationException {
		convert(customMsg, outputFileURI.toString());
	}

	@Override
	public void convert(final CustomMessage customMessage, final String outputFilePath) throws DCMAApplicationException {
		FileOutputStream fileOutputStream = null;
		MailMetaData mail = null;
		String localOutputFilePath = outputFilePath;
		try {
			if (customMessage.getMessage().getContent() instanceof Multipart) {
				File createFolders = new File(localOutputFilePath + File.separator + ICommonConstants.DOWNLOAD_FOLDER_NAME);
				createFolders.mkdirs();
				mail = customMessage.getMailMetaData();
				File file = new File(localOutputFilePath + File.separator + ICommonConstants.DOWNLOAD_FOLDER_NAME + File.separator
						+ MailConstants.TEMP_FILE_NAME + FileType.TXT.getExtensionWithDot());
				fileOutputStream = new FileOutputStream(file);
				fileOutputStream.write(mail.toString().getBytes());
				localOutputFilePath = FileUtils.changeFileExtension(localOutputFilePath + File.separator + file.getName(),
						FileType.PDF.getExtension());
				fileOutputStream.close();
				// get the input file http url.
				File parentFile = file.getParentFile();
				String inputFileURL = batchSchemaService.getHttpEmailFolderPath() + '/' + parentFile.getParentFile().getName() + '/'
						+ parentFile.getName() + '/' + file.getName();
				openOfficeConvertor.convert(inputFileURL, localOutputFilePath, null, FileType.PDF);
				// Uncomment following line to delete the file after converting to 'pdf'.
				// file.delete();
			}
		} catch (Exception e) {
			LOGGER.error("Unable to convert Email into PDF file.", e);
			throw new DCMAApplicationException(e.getMessage(), e);

		} finally {
			try {
				if (fileOutputStream != null) {
					fileOutputStream.close();
				}
			} catch (IOException e) {
				LOGGER.error("Error in closing file :" + e.getMessage());
			}
		}
	}

}
