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
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.core.common.CustomMessage;
import com.ephesoft.dcma.core.common.FileType;
import com.ephesoft.dcma.core.common.UserType;
import com.ephesoft.dcma.core.component.ICommonConstants;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.core.service.FileFormatConvertor;
import com.ephesoft.dcma.core.service.ZipService;
import com.ephesoft.dcma.da.domain.BatchClass;
import com.ephesoft.dcma.da.domain.BatchClassCloudConfig;
import com.ephesoft.dcma.da.domain.BatchClassEmailConfiguration;
import com.ephesoft.dcma.da.domain.User;
import com.ephesoft.dcma.da.service.BatchClassCloudConfigService;
import com.ephesoft.dcma.da.service.BatchClassEmailConfigService;
import com.ephesoft.dcma.da.service.UserService;
import com.ephesoft.dcma.mail.constants.MailConstants;
import com.ephesoft.dcma.util.ApplicationConfigProperties;
import com.ephesoft.dcma.util.FileUtils;

/**
 * Service implementation class for email.
 * 
 * @author Ephesoft
 *
 */
public class EmailImporterServiceImpl implements EmailImporterService {

	/**
	 * An instance of {@link BatchClassEmailConfigService}.
	 */
	@Autowired
	private BatchClassEmailConfigService batConfigService;

	/**
	 * An instance of {@link FileFormatConvertor}.
	 */
	@Autowired
	@Qualifier("openOfficeConvertor")
	private FileFormatConvertor openOfficeConvertor;

	/**
	 * An instance of {@link ZipService}.
	 */
	@Autowired
	private ZipService zipService;

	/**
	 * An instance of {@link BatchSchemaService}.
	 */
	@Autowired
	private BatchSchemaService batchSchemaService;

	/**
	 * An instance of {@link BatchClassCloudConfig}.
	 */
	@Autowired
	private BatchClassCloudConfigService batchClassCloudService;

	/**
	 * An instance of {@link MailReceiverService}.
	 */
	@Autowired
	private MailReceiverService mailReceiverService;

	/**
	 * An instance of {@link EmailConvertor}.
	 */
	@Autowired
	private EmailConvertor emailConvertor;

	/**
	 * An instance of {@link UserService}.
	 */
	@Autowired
	private UserService userService;

	/**
	 * Map containing encoded characters.
	 */
	private Map<String, String> encodedCharacterMap;

	/**
	 * To store default folder locations.
	 */
	private String defaultFolderLocation;

	/**
	 * An instance of Logger for proper logging.
	 */
	protected static final Logger LOGGER = LoggerFactory.getLogger(EmailImporterServiceImpl.class);
	
	/**
	 * String containing supported attachment extensions.
	 */
	private String supportedAttachmentExtensions;

	/**
	 * The USER_TYPE {@link String} is a constant for Epehsoft Cloud user type
	 * defined in properties file.
	 */
	private static final String USER_TYPE = "user_type";

	@Override
	public void processMails() {
		populateEncodedCharMap();
		final List<BatchClassEmailConfiguration> batchClassEmailConfigList = batConfigService.getAllEmailConfigs();
		final StringBuffer errorMsg = new StringBuffer("Error while processing mails from ");
		for (final BatchClassEmailConfiguration emailConfiguration : batchClassEmailConfigList) {
			String batchClassId = emailConfiguration.getBatchClass().getIdentifier();

				errorMsg.append(emailConfiguration.getUserName());
				errorMsg.append(MailConstants.SPACE);
				errorMsg.append(emailConfiguration.getServerName());
				errorMsg.append(MailConstants.SPACE);
				errorMsg.append(emailConfiguration.getServerType());
				errorMsg.append(MailConstants.SPACE);
				errorMsg.append(emailConfiguration.getFolderName());
				if (emailConfiguration != null && emailConfiguration.getBatchClass() != null) {
					try {
						final List<CustomMessage> messages = mailReceiverService.readMails(emailConfiguration);
						if (messages == null || messages.size() <= 0) {
							continue;
						}
						for (final CustomMessage message : messages) {
							try {
									if (checkForBatchClassLimit(batchClassId)) {
										logBatchClassLimitError(batchClassId,
												emailConfiguration);
									} else {
										updateBatchCounter(batchClassId);
										final String folderPath = message.getFolderPath();
										final String folderName = message.getFolderName();
										if (isZipValidExtension()) {
											extractZipFiles(folderPath + File.separator + ICommonConstants.DOWNLOAD_FOLDER_NAME);
										}
										downloadMail(message, folderPath);
										moveToUNCFolder(emailConfiguration.getBatchClass(), folderName);
									}
							} catch (final Exception e) {
								LOGGER.error(e.getMessage(), e);
							}
						}
					} catch (final Exception e) {
						LOGGER.error(errorMsg + e.getMessage(), e);
					}
				}
			}
	}	
	/**
	 * The <code>logBatchClassLimitError</code> is a method that logs the batch class
	 * limit error.
	 * 
	 * 
	 * @param batchClassId {@link String} batch class identifier
	 * @param emailConfiguration {@link BatchClassEmailConfiguration} email configuration object
	 */
	private void logBatchClassLimitError(final String batchClassId, final BatchClassEmailConfiguration emailConfiguration) {
		final User user = userService.getUserByBatchClassId(batchClassId);
		if (null == user) {
			LOGGER
			.error("Exceeded batch instance limit for batch class: "
					+ batchClassId
					+ " , email id: "
					+ emailConfiguration.getUserName());
		} else {
			LOGGER
			.error("Exceeded batch instance limit for batch class: "
					+ batchClassId
					+ " , email id: "
					+ emailConfiguration.getUserName()
					+ " and user: " + user.getEmail());
		}
	}
	
	private boolean isZipValidExtension() {
		final StringTokenizer tokenizer = new StringTokenizer(supportedAttachmentExtensions,
				MailConstants.EXTENSION_SEPARATOR_CONSTANT);
		boolean isFileValid = false;
		while (tokenizer.hasMoreTokens()) {
			final String validExtension = tokenizer.nextToken();
			if (validExtension.toLowerCase(Locale.getDefault()).equals(FileType.ZIP.getExtension())) {
				isFileValid = true;
				break;
			}
		}
		return isFileValid;
	}
	private void extractZipFiles(final String folderPath) throws FileNotFoundException, IOException {
		final File parentFile = new File(folderPath);
		final FilenameFilter filter = new FilenameFilter() {

			public boolean accept(final File dir, final String name) {
				return name.endsWith(FileType.ZIP.getExtensionWithDot());
			}
		};
		final File zipFile[] = parentFile.listFiles(filter);
		if (null != zipFile) {
			for (final File file : zipFile) {
				zipService.unzipFiles(file, folderPath);
			}
		}
	}
	private void populateEncodedCharMap() {
		encodedCharacterMap = new HashMap<String, String>();
		encodedCharacterMap.put(MailConstants.CHARCTER_PLUS, MailConstants.ENCODED_STRING_FOR_PLUS);
		defaultFolderLocation = batchSchemaService.getEmailFolderPath();
	}
	@Override
	public void downloadMail(final CustomMessage customMessage, final String folderPath) throws DCMAApplicationException {
		try {
			if (customMessage != null && customMessage.getMessage() != null) {
				emailConvertor.convert(customMessage, folderPath);
				final File file = new File(folderPath + File.separator + ICommonConstants.DOWNLOAD_FOLDER_NAME);
				// This code does not return any files that is being named as start with `.'.
				final FilenameFilter filter = new FilenameFilter() {

					public boolean accept(final File dir, final String name) {
						return !name.equals(MailConstants.TEMP_FILE_NAME + MailConstants.DOT_SEPARATOR + FileType.PDF.getExtension());
					}
				};
				final String wantedAttachments[] = file.list(filter);
				if (wantedAttachments != null && wantedAttachments.length > 0) {
					for (final String fileName : wantedAttachments) {
						checkExtensionsAndConvertAttachments(fileName, folderPath);
					}
				}
			}
		} catch (final Exception e) {
			LOGGER.error("Unable to read contents of the downloaded message", e);
			throw new DCMAApplicationException(e.getMessage(), e);
		}
	}

	private void checkExtensionsAndConvertAttachments(final String fileName, final String outputFilePath)
			throws UnsupportedEncodingException, IOException {
		boolean isFileValid = true;
		String localOutputFilePath = outputFilePath;
		if (fileName != null && !fileName.isEmpty()) {
			isFileValid = fileValidityCheck(fileName);
			if (!isFileValid
					&& !(fileName.toLowerCase(Locale.getDefault()).endsWith(FileType.TIF.getExtension()))
					&& !(fileName.toLowerCase(Locale.getDefault()).endsWith(FileType.PDF.getExtension()))
					&& !(fileName.toLowerCase(Locale.getDefault()).equalsIgnoreCase(MailConstants.TEMP_FILE_NAME
							+ FileType.TXT.getExtensionWithDot()))) {
				final File errorPDFFile = new File(defaultFolderLocation + File.separator + MailConstants.ERROR_FILE);
				try {
					final String newErrorPdfFilePath = localOutputFilePath + File.separator + MailConstants.ZZZZZ_ATTACHMENT
							+ fileName + FileType.PDF.getExtensionWithDot();
					FileUtils.copyFile(errorPDFFile, new File(newErrorPdfFilePath));
				} catch (final Exception e) {
					LOGGER.error("Unable to copy file.", e);
				}
			}
		}
		String encodedFileName = URLEncoder.encode(fileName, MailConstants.UTF_8_ENCODING);
		final Set<String> characterSet = encodedCharacterMap.keySet();
		for (final String key : characterSet) {
			encodedFileName = encodedFileName.replace(key, encodedCharacterMap.get(key));
		}
		final File file = new File(localOutputFilePath + File.separator + fileName);
		final String newFileName = fileName.toLowerCase(Locale.getDefault());
		if (!(newFileName.endsWith(FileType.TIFF.getExtensionWithDot()) || newFileName.endsWith(FileType.TIF.getExtensionWithDot())
				|| newFileName.endsWith(FileType.PDF.getExtensionWithDot()) || newFileName
				.endsWith(FileType.ZIP.getExtensionWithDot()))) {
			if (isFileValid) {
				localOutputFilePath = file.getPath();
				localOutputFilePath = FileUtils.changeFileExtension(localOutputFilePath, FileType.PDF.getExtension());
				final String inputFileURL = batchSchemaService.getHttpEmailFolderPath() + '/' + file.getParentFile().getName() + '/'
						+ ICommonConstants.DOWNLOAD_FOLDER_NAME + '/' + encodedFileName;
				openOfficeConvertor.convert(inputFileURL, localOutputFilePath, encodedFileName, FileType.PDF);
				// Uncomment following line to delete the file after converting to 'pdf'.
				// file.delete();
			}
		} else if (!newFileName.endsWith(FileType.ZIP.getExtensionWithDot())) {
			FileUtils.copyFile(new File(file.getParent() + File.separator + ICommonConstants.DOWNLOAD_FOLDER_NAME + File.separator
					+ fileName), file);
		}
	}

	private void moveToUNCFolder(final BatchClass batchClass, final String folderName) throws DCMAApplicationException {
		// Call convertPdfOrMultiPageTiffToTiff method to convert pdf to tiff before folder import module in download email folder.
		// convertPdfOrMultiPageTiffToTiff(batchClass, folderPath);
		batchSchemaService.copyEmailFolderToUNC(defaultFolderLocation, folderName, batchClass.getIdentifier());
	}

	private boolean fileValidityCheck(final String fileName) {
		final StringTokenizer tokenizer = new StringTokenizer(supportedAttachmentExtensions,
				MailConstants.EXTENSION_SEPARATOR_CONSTANT);
		final String attachmentExtension = fileName.substring(fileName.lastIndexOf(MailConstants.DOT_SEPARATOR) + 1);
		boolean isFileValid = false;
		while (tokenizer.hasMoreTokens()) {
			final String inValidExtension = tokenizer.nextToken();
			if (attachmentExtension.equalsIgnoreCase(inValidExtension)) {
				isFileValid = true;
				break;
			}
		}

		return isFileValid;
	}

	public void setSupportedAttachmentExtensions(final String supportedAttachmentExtensions) {
		this.supportedAttachmentExtensions = supportedAttachmentExtensions;
	}

	/**
	 * The <code>checkForBatchClassLimit</code> method is used for checking
	 * whether the given batch class has reached instance limit for the day.
	 * 
	 * @param identifier
	 *            {@link String} batch class identifier
	 * @return true/false
	 */
	private boolean checkForBatchClassLimit(final String identifier) {
		boolean isLimitExceeded = false;
		Integer userType = getUserType();
		if (userType.intValue() == UserType.LIMITED.getUserType()
				&& null != identifier && !identifier.isEmpty()) {
			BatchClassCloudConfig batchClassCloudConfig = batchClassCloudService
					.getBatchClassCloudConfigByBatchClassIdentifier(identifier);
			if (null != batchClassCloudConfig) {
				Integer batchInstanceLimit = batchClassCloudConfig
						.getBatchInstanceLimit();
				Integer batchInstanceCounter = batchClassCloudConfig
						.getCurrentCounter();
				isLimitExceeded = (null != batchInstanceLimit
						&& null != batchInstanceCounter && batchInstanceCounter >= batchInstanceLimit);
			}
		}
		return isLimitExceeded;
	}
	
	/**
	 * The <code>updateBatchCounter</code> method is used to update batch count for
	 * Freemium user type.
	 * 
	 * @param batchClassID {@link String} batch class identifier
	 * @param userType {@link Integer} Ephesoft Cloud user type
	 */
	private void updateBatchCounter(String batchClassID) {
		Integer userType = getUserType();
		if (userType.intValue() == UserType.LIMITED.getUserType()) {
			BatchClassCloudConfig batchClassCloudConfig = batchClassCloudService.
															getBatchClassCloudConfigByBatchClassIdentifier(batchClassID);
			if (null != batchClassCloudConfig) {
				Integer batchInstanceCount = batchClassCloudConfig.getBatchInstanceLimit();
				Integer currentCounter = batchClassCloudConfig.getCurrentCounter();
				if (null != currentCounter && null != batchInstanceCount && ++currentCounter <= batchInstanceCount) {
					batchClassCloudConfig.setCurrentCounter(currentCounter);
					batchClassCloudService.updateBatchClassCloudConfig(batchClassCloudConfig);
				}
			}
		}
	}

	/**
	 * The <code>getUserType</code> method is used to get Ephesoft Cloud user
	 * type from properties file.
	 * 
	 * @return {@link Integer} Ephesoft Cloud user type
	 */
	private Integer getUserType() {
		Integer userType = null;
		try {
			userType = Integer.parseInt(ApplicationConfigProperties
					.getApplicationConfigProperties().getProperty(USER_TYPE));
		} catch (NumberFormatException numberFormatException) {
			userType = UserType.OTHERS.getUserType();
			LOGGER.error("user type property is in wrong format in property file");
		} catch (IOException ioException) {
			userType = UserType.OTHERS.getUserType();
			LOGGER.error("user type property is missing from property file");
		}
		return userType;
	}

}
