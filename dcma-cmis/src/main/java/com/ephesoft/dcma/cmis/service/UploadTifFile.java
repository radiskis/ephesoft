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

package com.ephesoft.dcma.cmis.service;

import java.io.File;

import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.exceptions.CmisObjectNotFoundException;

import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.cmis.CMISDocumentDetails;
import com.ephesoft.dcma.cmis.constant.CMISExportConstant;
import com.ephesoft.dcma.core.common.FileType;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;

/**
 * This class is used to upload tiff file.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.cmis.service.CMISExportServiceImpl
 *
 */
public class UploadTifFile extends AbstractUploadFile{
	/**
	 * This method performs the function of uploading file.
	 * @param batchInstanceIdentifier {@link String}
	 * @param rootFolder {@link String}
	 * @param alfrescoAspectSwitch {@link String}
	 * @param session {@link Session}
	 * @param batchInstanceFolder {@link Folder}
	 * @param sFolderToBeExported {@link String}
	 * @param batchClassIdentifier {@link String}
	 * @param document {@link Document}
	 * @param updatedCmisFileName {@link String}
	 * @param documentDetails {@link CMISDocumentDetails}
	 * @throws DCMAApplicationException if any exception occurs during uploading file. 
	 */
	@Override
	public void uploadFile(String batchInstanceIdentifier, String rootFolder, String alfrescoAspectSwitch, Session session,
			Folder batchInstanceFolder, String sFolderToBeExported, String batchClassIdentifier, Document document,
			String updatedCmisFileName, CMISDocumentDetails documentDetails) throws DCMAApplicationException {
		String sMultiPageTif = document.getMultiPageTiffFile();
		if (sMultiPageTif != null && !sMultiPageTif.isEmpty()) {

			File fSourceTifFile = new File(sFolderToBeExported + File.separator + sMultiPageTif);
			try {
				org.apache.chemistry.opencmis.client.api.Document doc = null;
				try {
					doc = (org.apache.chemistry.opencmis.client.api.Document) session
							.getObjectByPath(CMISExportConstant.FOLDER_SEPARATOR + rootFolder + CMISExportConstant.FOLDER_SEPARATOR
									+ fSourceTifFile.getName());
				} catch (CmisObjectNotFoundException e) {
					LOGGER.error(e.getMessage(), e);
				}

				String tifUpdatedFileName = null;

				if (updatedCmisFileName != null && !updatedCmisFileName.isEmpty()) {
					tifUpdatedFileName = updatedCmisFileName + FileType.TIF.getExtensionWithDot();
				}

				doc = uploadDocument(doc, session, batchInstanceFolder, fSourceTifFile, document, batchClassIdentifier,
						true, batchInstanceIdentifier, tifUpdatedFileName, documentDetails);

				if (null != alfrescoAspectSwitch && alfrescoAspectSwitch.equalsIgnoreCase(CMISExportConstant.ON_STRING)) {
					addAspectsToDocument(doc, document, batchClassIdentifier, batchInstanceIdentifier, documentDetails);
				}
			} catch (Exception e) {
				LOGGER.error("Problem uploading Tiff file : " + fSourceTifFile, e);
				throw new DCMAApplicationException(CMISExportConstant.UNABLE_TO_UPLOAD_THE_DOCUMENT + sMultiPageTif, e);
			}
		}
	}
	
}
