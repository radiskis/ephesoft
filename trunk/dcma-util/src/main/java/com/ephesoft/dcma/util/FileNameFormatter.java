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

import java.io.IOException;
import java.util.Properties;

/**
 * When an instance of the FileNameFormatter is created it loads the Properties file FileNameFormatter.properties to the field prop.
 * There are various methods in the Filename formatter which returns the File name based on the configuration in the properties file.
 * 
 * @author Ephesoft
 * 
 */
public class FileNameFormatter implements IUtilCommonConstants {

	private static final String SET_PROPERTY_NAME = "Property not set propertyname";
	private static Properties prop = new Properties();

	/**
	 * When an instance of the FileNameFormatter is created it loads the Properties file FileNameFormatter.properties to the field
	 * prop.
	 * 
	 * @throws Exception
	 */
	public FileNameFormatter() throws Exception {

		try {
			prop.load(FileNameFormatter.class.getClassLoader().getResourceAsStream(FILENAME_FORMATTER_PROPERTIES));

		} catch (IOException ioExp) {

			throw new Exception("Problem Reading the property file->" + "FileNameFormatter.properties" + ioExp);
		}

	}

	/**
	 * This method gives the name of multi page files based on the parameters. The format of the file name is configurable. The example
	 * entry in the properties file is: multiPageFileNameFormat=batchInstanceIdentifier;seperator ;document;documentId;extension
	 * multiPageFileNameSeperator=_
	 * 
	 * @param documentID
	 * @param batchInstanceIdentifier
	 * @param extension
	 * @return name of Multi page file.
	 * @throws Exception
	 */
	public String getMuliPageFileName(String documentID, String batchInstanceIdentifier, String extension) throws Exception {
		String multiPageFileNameFormat = prop.getProperty(MULIPAGE_FILENAME_FORMAT);
		String seperator = prop.getProperty(MULIPAGE_FILENAME_SEPERATOR);
		if (multiPageFileNameFormat == null || multiPageFileNameFormat.isEmpty()) {
			throw new Exception(SET_PROPERTY_NAME + '=' + MULIPAGE_FILENAME_FORMAT);
		}

		String[] fileNameComponents = multiPageFileNameFormat.split(FILE_NAME_FORMAT_DELIMITOR);
		StringBuffer documentFileNameBuffer = new StringBuffer();
		for (int index = 0; index < fileNameComponents.length; index++) {
			if (fileNameComponents[index].equals(BATCH_ID)) {
				documentFileNameBuffer.append(batchInstanceIdentifier);
			} else if (fileNameComponents[index].equals(DOCUMENT_STRING)) {
				documentFileNameBuffer.append(DOCUMENT);
			} else if (fileNameComponents[index].equals(BATCH_ID)) {
				documentFileNameBuffer.append(batchInstanceIdentifier);
			} else if (fileNameComponents[index].equals(SEPERATOR)) {
				documentFileNameBuffer.append(seperator);
			} else if (fileNameComponents[index].equalsIgnoreCase(EXTENSION)) {
				documentFileNameBuffer.append(extension);
			} else if (fileNameComponents[index].equals(DOCUMENT_ID)) {
				documentFileNameBuffer.append(documentID);
			} else {
				throw new Exception("The property " + MULIPAGE_FILENAME_FORMAT + " contains invalid element "
						+ fileNameComponents[index] + "refer the file" + FILENAME_FORMATTER_PROPERTIES);
			}
		}

		return documentFileNameBuffer.toString();

	}

	/**
	 * Returns the Hocr File name. Example configuration is hocrFileNameFormat=batchInstanceIdentifier
	 * ;seperator;oldFileNameWOExt;extension hocrFileNameSeperator=_
	 * 
	 * @param batchInstanceIdentifier
	 * @param oldFileName
	 * @param newFileName
	 * @param OCRInputFileName
	 * @param extension
	 * @return name of hocr file
	 * @throws Exception
	 */
	public String getHocrFileName(String batchInstanceIdentifier, String oldFileName, String newFileName, String OCRInputFileName,
			String extension, String pageId, boolean forExtraction) throws Exception {
		String hocrFileNameFormat = prop.getProperty(HOCR_FILENAME_FORMAT);
		String seperator = prop.getProperty(HOCR_FILENAME_SEPERATOR);
		if (hocrFileNameFormat == null || hocrFileNameFormat.isEmpty()) {
			throw new Exception(SET_PROPERTY_NAME + '=' + HOCR_FILENAME_FORMAT);
		}

		String[] fileNameComponents = hocrFileNameFormat.split(FILE_NAME_FORMAT_DELIMITOR);
		StringBuffer hocrFileNameBuffer = new StringBuffer();
		for (int index = 0; index < fileNameComponents.length; index++) {
			if (fileNameComponents[index].equals(BATCH_ID)) {
				hocrFileNameBuffer.append(batchInstanceIdentifier);
			} else if (fileNameComponents[index].equals(SEPERATOR)) {
				hocrFileNameBuffer.append(seperator);
			} else if (fileNameComponents[index].equals(OLD_FILE_NAME_WITH_EXT)) {
				hocrFileNameBuffer.append(oldFileName);
			} else if (fileNameComponents[index].equals(NEW_FILE_NAME_WITH_EXT)) {
				hocrFileNameBuffer.append(newFileName);
			} else if (fileNameComponents[index].equals(OCR_INPUT_FILE_NAME_WITH_EXT)) {
				hocrFileNameBuffer.append(OCRInputFileName);
			} else if (fileNameComponents[index].equals(OLD_FILENAME_WO_EXT)) {
				hocrFileNameBuffer.append(removeExtension(oldFileName));
			} else if (fileNameComponents[index].equals(NEW_FILE_NAME_WO_EXT)) {
				hocrFileNameBuffer.append(removeExtension(newFileName));
			} else if (fileNameComponents[index].equals(OCR_INPUT_FILE_NAME_WO_EXT)) {
				hocrFileNameBuffer.append(removeExtension(OCRInputFileName));
			} else if (fileNameComponents[index].equalsIgnoreCase(EXTENSION)) {
				if (forExtraction) {
					hocrFileNameBuffer.append(seperator);
					hocrFileNameBuffer.append(EXTRACTION);
					hocrFileNameBuffer.append(extension);
				} else {
					hocrFileNameBuffer.append(extension);
				}
			} else if (fileNameComponents[index].equals(PAGE_ID)) {
				hocrFileNameBuffer.append(pageId);
			}

			else {
				throw new Exception("The property " + HOCR_FILENAME_FORMAT + " contains invalid element " + fileNameComponents[index]
						+ "refer the file" + FILENAME_FORMATTER_PROPERTIES);
			}
		}

		return hocrFileNameBuffer.toString();

	}

	/**
	 * Removes the extension from the file name.
	 * 
	 * @param fileName
	 * @return file name without the ext.
	 */
	private String removeExtension(String fileName) {
		String[] strArr;
		String returnValue = null;
		strArr = fileName.split("\\.");
		if (strArr.length == 2) {
			returnValue = strArr[0];
		} else {
			if (strArr.length > 2) {
				StringBuffer fileNameBuff = new StringBuffer();
				for (int index = 0; index <= strArr.length - 3; index++) {
					fileNameBuff.append(strArr[index]);
					fileNameBuff.append(DOT);
				}
				fileNameBuff.append(strArr[strArr.length - 2]);
				returnValue = fileNameBuff.toString();
			} else {
				returnValue = fileName;
			}
		}
		return returnValue;
	}

	/**
	 * This method returns the Display thumbnail file name. eg. of configuration displayThumbNailFileNameFormat
	 * =batchInstanceIdentifier;seperator;oldFileNameWOExt ;seperator;displayThumb;extension thumbNailFileNameSeperator=_
	 * 
	 * @param batchInstanceIdentifier
	 * @param oldFileName
	 * @param newFileName
	 * @param OCRInputFileName
	 * @param extension
	 * @return name of thumbnail file
	 * @throws Exception
	 */
	public String getDisplayThumbnailFileName(String batchInstanceIdentifier, String oldFileName, String newFileName,
			String OCRInputFileName, String extension, String pageId) throws Exception {
		String thumbNailFileNameFormat = prop.getProperty(DISPLAY_THUMBNAIL_FILENAME_FORMAT);
		String seperator = prop.getProperty(THUMBNAIL_FILENAME_SEPERATOR);
		if (thumbNailFileNameFormat == null || thumbNailFileNameFormat.isEmpty()) {
			throw new Exception(SET_PROPERTY_NAME + '=' + DISPLAY_THUMBNAIL_FILENAME_FORMAT);
		}

		String[] fileNameComponents = thumbNailFileNameFormat.split(FILE_NAME_FORMAT_DELIMITOR);
		StringBuffer dispThumbFileNameBuffer = new StringBuffer();
		for (int index = 0; index < fileNameComponents.length; index++) {
			if (fileNameComponents[index].equals(BATCH_ID)) {
				dispThumbFileNameBuffer.append(batchInstanceIdentifier);
			} else if (fileNameComponents[index].equals(SEPERATOR)) {
				dispThumbFileNameBuffer.append(seperator);
			} else if (fileNameComponents[index].equals(OLD_FILE_NAME_WITH_EXT)) {
				dispThumbFileNameBuffer.append(oldFileName);
			} else if (fileNameComponents[index].equals(NEW_FILE_NAME_WITH_EXT)) {
				dispThumbFileNameBuffer.append(newFileName);
			} else if (fileNameComponents[index].equals(OCR_INPUT_FILE_NAME_WITH_EXT)) {
				dispThumbFileNameBuffer.append(OCRInputFileName);
			} else if (fileNameComponents[index].equals(OLD_FILENAME_WO_EXT)) {
				dispThumbFileNameBuffer.append(removeExtension(oldFileName));
			} else if (fileNameComponents[index].equals(NEW_FILE_NAME_WO_EXT)) {
				dispThumbFileNameBuffer.append(removeExtension(newFileName));
			} else if (fileNameComponents[index].equals(OCR_INPUT_FILE_NAME_WO_EXT)) {
				dispThumbFileNameBuffer.append(removeExtension(OCRInputFileName));
			} else if (fileNameComponents[index].equalsIgnoreCase(EXTENSION)) {
				dispThumbFileNameBuffer.append(extension);
			} else if (fileNameComponents[index].equals(THUMB_STRING)) {
				dispThumbFileNameBuffer.append(THUMB_STRING);
			} else if (fileNameComponents[index].equals(DISP_THUMB_STRING)) {
				dispThumbFileNameBuffer.append(DISP_THUMB_STRING);
			} else if (fileNameComponents[index].equals(PAGE_ID)) {
				dispThumbFileNameBuffer.append(pageId);
			}

			else {
				throw new Exception("The property " + DISPLAY_THUMBNAIL_FILENAME_FORMAT + " contains invalid element "
						+ fileNameComponents[index] + "refer the file" + FILENAME_FORMATTER_PROPERTIES);
			}
		}

		return dispThumbFileNameBuffer.toString();

	}

	/**
	 * This method returns the Compare thumbnail file name. eg. of configuration compareThumbNailFileNameFormat
	 * =batchInstanceIdentifier;seperator;oldFileNameWOExt ;seperator;compareThumb;extension thumbNailFileNameSeperator=_
	 * 
	 * @param batchInstanceIdentifier
	 * @param oldFileName
	 * @param newFileName
	 * @param OCRInputFileName
	 * @param extension
	 * @return name of thumbnail file
	 * @throws Exception
	 */
	public String getCompareThumbnailFileName(String batchInstanceIdentifier, String oldFileName, String newFileName,
			String OCRInputFileName, String extension, String pageId) throws Exception {
		String thumbNailFileNameFormat = prop.getProperty(COMPARE_THUMBNAIL_FILENAME_FORMAT);
		String seperator = prop.getProperty(THUMBNAIL_FILENAME_SEPERATOR);
		if (thumbNailFileNameFormat == null || thumbNailFileNameFormat.isEmpty()) {
			throw new Exception(SET_PROPERTY_NAME + '=' + COMPARE_THUMBNAIL_FILENAME_FORMAT);
		}

		String[] fileNameComponents = thumbNailFileNameFormat.split(FILE_NAME_FORMAT_DELIMITOR);
		StringBuffer dispThumbFileNameBuffer = new StringBuffer();
		for (int index = 0; index < fileNameComponents.length; index++) {
			if (fileNameComponents[index].equals(BATCH_ID)) {
				dispThumbFileNameBuffer.append(batchInstanceIdentifier);
			} else if (fileNameComponents[index].equals(SEPERATOR)) {
				dispThumbFileNameBuffer.append(seperator);
			} else if (fileNameComponents[index].equals(OLD_FILE_NAME_WITH_EXT)) {
				dispThumbFileNameBuffer.append(oldFileName);
			} else if (fileNameComponents[index].equals(NEW_FILE_NAME_WITH_EXT)) {
				dispThumbFileNameBuffer.append(newFileName);
			} else if (fileNameComponents[index].equals(OCR_INPUT_FILE_NAME_WITH_EXT)) {
				dispThumbFileNameBuffer.append(OCRInputFileName);
			} else if (fileNameComponents[index].equals(OLD_FILENAME_WO_EXT)) {
				dispThumbFileNameBuffer.append(removeExtension(oldFileName));
			} else if (fileNameComponents[index].equals(NEW_FILE_NAME_WO_EXT)) {
				dispThumbFileNameBuffer.append(removeExtension(newFileName));
			} else if (fileNameComponents[index].equals(OCR_INPUT_FILE_NAME_WO_EXT)) {
				dispThumbFileNameBuffer.append(removeExtension(OCRInputFileName));
			} else if (fileNameComponents[index].equalsIgnoreCase(EXTENSION)) {
				dispThumbFileNameBuffer.append(extension);
			} else if (fileNameComponents[index].equals(THUMB_STRING)) {
				dispThumbFileNameBuffer.append(THUMB_STRING);
			} else if (fileNameComponents[index].equals(COMPARE_THUMB_STRING)) {
				dispThumbFileNameBuffer.append(COMPARE_THUMB_STRING);
			} else if (fileNameComponents[index].equals(PAGE_ID)) {
				dispThumbFileNameBuffer.append(pageId);
			}

			else {
				throw new Exception("The property " + COMPARE_THUMBNAIL_FILENAME_FORMAT + " contains invalid element "
						+ fileNameComponents[index] + "refer the file" + FILENAME_FORMATTER_PROPERTIES);
			}
		}

		return dispThumbFileNameBuffer.toString();

	}

	/**
	 * Retturns name of overlayed files. eg. of configuration overlayFileNameFormat
	 * =batchInstanceIdentifier;seperator;oldFileNameWOExt;seperator;fieldName ;alternateValueIndex;extension
	 * overlayFileNameSeperator=_
	 * 
	 * @param batchInstanceIdentifier
	 * @param documentId
	 * @param ocrFileName
	 * @param fieldName
	 * @param alternateValueIndex
	 * @param alternateValue
	 * @param extension
	 * @return overlayed file name
	 * @throws Exception
	 */
	public String getOverlayedFileName(String batchInstanceIdentifier, String documentId, String ocrFileName, String fieldName,
			int alternateValueIndex, boolean alternateValue, String extension) throws Exception {

		String overlayedFileNameFormat = prop.getProperty(HOCR_FILENAME_FORMAT);
		String seperator = prop.getProperty(OVERLAY_FILENAME_SEPERATOR);
		if (overlayedFileNameFormat == null || overlayedFileNameFormat.isEmpty()) {
			throw new Exception(SET_PROPERTY_NAME + '=' + OVERLAY_FILENAME_FORMAT);
		}

		String[] fileNameComponents = overlayedFileNameFormat.split(FILE_NAME_FORMAT_DELIMITOR);
		StringBuffer overlayFileNameBuffer = new StringBuffer();
		for (int index = 0; index < fileNameComponents.length; index++) {
			if (fileNameComponents[index].equals(BATCH_ID)) {
				overlayFileNameBuffer.append(batchInstanceIdentifier);
			} else if (fileNameComponents[index].equals(SEPERATOR)) {
				overlayFileNameBuffer.append(seperator);
			} else if (fileNameComponents[index].equals(OLD_FILE_NAME_WITH_EXT)) {
				// hocrFileNameBuffer.append(oldFileName);
			} else if (fileNameComponents[index].equals(NEW_FILE_NAME_WITH_EXT)) {
				// hocrFileNameBuffer.append(newFileName);
			} else if (fileNameComponents[index].equals(OCR_INPUT_FILE_NAME_WITH_EXT)) {
				overlayFileNameBuffer.append(ocrFileName);
			} else if (fileNameComponents[index].equals(OLD_FILENAME_WO_EXT)) {
				// hocrFileNameBuffer.append(removeExtension(ocrFileName));
			} else if (fileNameComponents[index].equals(NEW_FILE_NAME_WO_EXT)) {
				// hocrFileNameBuffer.append(removeExtension(newFileName));
			} else if (fileNameComponents[index].equals(OCR_INPUT_FILE_NAME_WO_EXT)) {
				overlayFileNameBuffer.append(removeExtension(ocrFileName));
			} else if (fileNameComponents[index].equalsIgnoreCase(EXTENSION)) {
				overlayFileNameBuffer.append(extension);
			} else if (fileNameComponents[index].equals(FIELD_NAME)) {
				overlayFileNameBuffer.append(fieldName);
			} else if (fileNameComponents[index].equals(ALTERNATE_VALUE_INDEX)) {
				if (alternateValue) {
					overlayFileNameBuffer.append(seperator);
					overlayFileNameBuffer.append(alternateValueIndex);
				}
			}

			else {
				throw new Exception("The property " + OVERLAY_FILENAME_FORMAT + " contains invalid element "
						+ fileNameComponents[index] + "refer the file" + FILENAME_FORMATTER_PROPERTIES);
			}
		}

		String fileName;
		if (alternateValue) {
			fileName = batchInstanceIdentifier + UNDER_SCORE + documentId + UNDER_SCORE + ocrFileName + UNDER_SCORE + fieldName
					+ UNDER_SCORE + alternateValueIndex + EXTENSION_PNG;
		} else {
			fileName = batchInstanceIdentifier + UNDER_SCORE + documentId + UNDER_SCORE + ocrFileName + UNDER_SCORE + fieldName
					+ EXTENSION_PNG;
		}
		return fileName;

	}

	/**
	 * Eg. of configuration ocrInputFileNameFormat=batchInstanceIdentifier;seperator ;oldFileNameWOExt;extension
	 * ocrInputFileNameSeperator=_
	 * 
	 * @param batchInstanceIdentifier
	 * @param oldFileName
	 * @param newFileName
	 * @param extension
	 * @return
	 * @throws Exception
	 */
	public String getOCRInputFileName(String batchInstanceIdentifier, String oldFileName, String newFileName, String extension,
			String pageId) throws Exception {
		String ocrInputFileNameFormat = prop.getProperty(OCR_INPUT_FILENAME_FORMAT);
		String seperator = prop.getProperty(OCR_INPUT_FILENAME_SEPERATOR);
		if (ocrInputFileNameFormat == null || ocrInputFileNameFormat.isEmpty()) {
			throw new Exception(SET_PROPERTY_NAME + '=' + OCR_INPUT_FILENAME_FORMAT);
		}

		String[] fileNameComponents = ocrInputFileNameFormat.split(FILE_NAME_FORMAT_DELIMITOR);
		StringBuffer ocrInputFileNameBuffer = new StringBuffer();
		for (int index = 0; index < fileNameComponents.length; index++) {
			if (fileNameComponents[index].equals(BATCH_ID)) {
				ocrInputFileNameBuffer.append(batchInstanceIdentifier);
			} else if (fileNameComponents[index].equals(SEPERATOR)) {
				ocrInputFileNameBuffer.append(seperator);
			} else if (fileNameComponents[index].equals(OLD_FILE_NAME_WITH_EXT)) {
				ocrInputFileNameBuffer.append(oldFileName);
			} else if (fileNameComponents[index].equals(NEW_FILE_NAME_WITH_EXT)) {
				ocrInputFileNameBuffer.append(newFileName);
			} else if (fileNameComponents[index].equals(OLD_FILENAME_WO_EXT)) {
				ocrInputFileNameBuffer.append(removeExtension(oldFileName));
			} else if (fileNameComponents[index].equals(NEW_FILE_NAME_WO_EXT)) {
				ocrInputFileNameBuffer.append(removeExtension(newFileName));
			} else if (fileNameComponents[index].equalsIgnoreCase(EXTENSION)) {
				ocrInputFileNameBuffer.append(extension);
			} else if (fileNameComponents[index].equals(PAGE_ID)) {
				ocrInputFileNameBuffer.append(pageId);
			}

			else {
				throw new Exception("The property " + OCR_INPUT_FILENAME_FORMAT + " contains invalid element "
						+ fileNameComponents[index] + "refer the file" + FILENAME_FORMATTER_PROPERTIES);
			}
		}

		return ocrInputFileNameBuffer.toString();

	}

	public String getNewFileName(String batchInstanceIdentifier, String oldFileName, String pageId, String extension) throws Exception {
		String newFileFileNameFormat = prop.getProperty(NEW_FILE_FILENAME_FORMAT);
		String seperator = prop.getProperty(NEW_FILE_FILENAME_SEPERATOR);
		if (newFileFileNameFormat == null || newFileFileNameFormat.isEmpty()) {
			throw new Exception(SET_PROPERTY_NAME + '=' + NEW_FILE_FILENAME_FORMAT);
		}

		String[] fileNameComponents = newFileFileNameFormat.split(FILE_NAME_FORMAT_DELIMITOR);
		StringBuffer newFileFileNameBuffer = new StringBuffer();
		for (int index = 0; index < fileNameComponents.length; index++) {
			if (fileNameComponents[index].equals(BATCH_ID)) {
				newFileFileNameBuffer.append(batchInstanceIdentifier);
			} else if (fileNameComponents[index].equals(SEPERATOR)) {
				newFileFileNameBuffer.append(seperator);
			} else if (fileNameComponents[index].equals(OLD_FILE_NAME_WITH_EXT)) {
				newFileFileNameBuffer.append(oldFileName);
			} else if (fileNameComponents[index].equals(OLD_FILENAME_WO_EXT)) {
				newFileFileNameBuffer.append(removeExtension(oldFileName));
			} else if (fileNameComponents[index].equalsIgnoreCase(EXTENSION)) {
				newFileFileNameBuffer.append(extension);
			} else if (fileNameComponents[index].equals(PAGE_ID)) {
				newFileFileNameBuffer.append(pageId);
			}

			else {
				throw new Exception("The property " + OCR_INPUT_FILENAME_FORMAT + " contains invalid element "
						+ fileNameComponents[index] + "refer the file" + FILENAME_FORMATTER_PROPERTIES);
			}
		}

		return newFileFileNameBuffer.toString();
	}

}
