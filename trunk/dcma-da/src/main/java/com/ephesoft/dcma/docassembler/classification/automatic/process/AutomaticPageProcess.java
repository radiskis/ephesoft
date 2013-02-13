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

package com.ephesoft.dcma.docassembler.classification.automatic.process;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ephesoft.dcma.batch.constant.BatchConstants;
import com.ephesoft.dcma.batch.schema.Batch;
import com.ephesoft.dcma.batch.schema.DocField;
import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.batch.schema.Field;
import com.ephesoft.dcma.batch.schema.Page;
import com.ephesoft.dcma.batch.schema.Batch.DocumentClassificationTypes;
import com.ephesoft.dcma.batch.schema.DocField.AlternateValues;
import com.ephesoft.dcma.batch.schema.Document.Pages;
import com.ephesoft.dcma.batch.schema.Page.PageLevelFields;
import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.batch.service.PluginPropertiesService;
import com.ephesoft.dcma.core.EphesoftProperty;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.da.service.PageTypeService;
import com.ephesoft.dcma.docassembler.DocumentAssemblerProperties;
import com.ephesoft.dcma.docassembler.constant.DocumentAssemblerConstants;
import com.ephesoft.dcma.docassembler.constant.PlaceHolder;

/**
 * This class will process every page present at document type Unknown. This will read all the pages one by one and basis of the max of
 * other classification types it will create new documents and delete the current page from the document type Unknown.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.docassembler.DocumentAssembler
 */
public class AutomaticPageProcess {

	/**
	 * LOGGER to print the logging information.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(AutomaticPageProcess.class);

	/**
	 * pageTypeService PageTypeService.
	 */
	private PageTypeService pageTypeService;

	/**
	 * Instance of PluginPropertiesService.
	 */
	private PluginPropertiesService pluginPropertiesService;

	/**
	 * xmlDocuments List<DocumentType>.
	 */
	private List<Document> xmlDocuments;

	/**
	 * Batch instance ID.
	 */
	private String batchInstanceID;

	/**
	 * Reference of BatchSchemaService.
	 */
	private BatchSchemaService batchSchemaService;

	/**
	 * Reference of propertyMap.
	 */
	private Map<String, String> propertyMap;

	/**
	 * barcode Confidence score.
	 */
	private String barcodeConfidence;
	/**
	 * barcode Classification.
	 */
	private String barcodeClassification;

	/**
	 * To get Property Map.
	 * @return the propertyMap
	 */
	public Map<String, String> getPropertyMap() {
		return propertyMap;
	}

	/**
	 * To set Property Map.
	 * @param propertyMap Map<String, String>
	 */
	public void setPropertyMap(Map<String, String> propertyMap) {
		this.propertyMap = propertyMap;
	}

	/**
	 * To get Batch Schema Service.
	 * @return the batchSchemaService
	 */
	public final BatchSchemaService getBatchSchemaService() {
		return batchSchemaService;
	}

	/**
	 * To set Batch Schema Service.
	 * @param batchSchemaService BatchSchemaService
	 */
	public final void setBatchSchemaService(final BatchSchemaService batchSchemaService) {
		this.batchSchemaService = batchSchemaService;
	}

	/**
	 * To get Page Type Service.
	 * @return the pageTypeService
	 */
	public final PageTypeService getPageTypeService() {
		return pageTypeService;
	}

	/**
	 * To set Page Type Service.
	 * @param pageTypeService PageTypeService
	 */
	public final void setPageTypeService(final PageTypeService pageTypeService) {
		this.pageTypeService = pageTypeService;
	}

	/**
	 * To get Xml Documents.
	 * @return List<DocumentType>
	 */
	public final List<Document> getXmlDocuments() {
		return xmlDocuments;
	}

	/**
	 * To set Xml Documents.
	 * @param xmlDocuments List<DocumentType>
	 */
	public final void setXmlDocuments(final List<Document> xmlDocuments) {
		this.xmlDocuments = xmlDocuments;
	}

	/**
	 * To get Batch Instance ID.
	 * @return batchInstanceID
	 */
	public final String getBatchInstanceID() {
		return batchInstanceID;
	}

	/**
	 * To set Batch Instance ID.
	 * @param batchInstanceID String
	 */
	public final void setBatchInstanceID(final String batchInstanceID) {
		this.batchInstanceID = batchInstanceID;
	}

	/**
	 * To get Plugin Properties Service.
	 * @return the pluginPropertiesService
	 */
	public PluginPropertiesService getPluginPropertiesService() {
		return pluginPropertiesService;
	}

	/**
	 * To set Plugin Properties Service.
	 * @param pluginPropertiesService PluginPropertiesService
	 */
	public void setPluginPropertiesService(PluginPropertiesService pluginPropertiesService) {
		this.pluginPropertiesService = pluginPropertiesService;
	}

	/**
	 * To get Barcode Confidence.
	 * @return the barcodeConfidence
	 */
	public final String getBarcodeConfidence() {
		return barcodeConfidence;
	}

	/**
	 * To set Barcode Confidence.
	 * @param barcodeConfidence String
	 */
	public final void setBarcodeConfidence(final String barcodeConfidence) {
		this.barcodeConfidence = barcodeConfidence;
	}

	/**
	 * To get Barcode Classification.
	 * @return the barcodeClassification
	 */
	public final String getBarcodeClassification() {
		return barcodeClassification;
	}

	/**
	 * To set Barcode Classification.
	 * @param barcodeClassification String
	 */
	public final void setBarcodeClassification(final String barcodeClassification) {
		this.barcodeClassification = barcodeClassification;
	}

	/**
	 * This method will set the document type name and ConfidenceThreshold.
	 * 
	 * @param docType {@link DocumentType}
	 * @param pageTypeName {@link String}
	 */
	public void setDocTypeNameAndConfThreshold(final Document docType, final String pageTypeName) {

		List<com.ephesoft.dcma.da.domain.DocumentType> docTypes = pluginPropertiesService.getDocTypeByPageTypeName(batchInstanceID,
				pageTypeName);

		String docTypeName = null;
		float minConfidenceThreshold = 0;

		if (null == docTypes || docTypes.isEmpty()) {
			LOGGER.info("No Document Type found for the input page type name.");
		} else {
			Iterator<com.ephesoft.dcma.da.domain.DocumentType> itr = docTypes.iterator();
			while (itr.hasNext()) {
				com.ephesoft.dcma.da.domain.DocumentType docTypeDB = itr.next();
				docTypeName = docTypeDB.getName();
				minConfidenceThreshold = docTypeDB.getMinConfidenceThreshold();
				LOGGER.debug("DocumentType name : " + docTypeName + "  minConfidenceThreshold : " + minConfidenceThreshold);
				if (null != docTypeName) {
					break;
				}
			}
		}

		if (null != docTypeName) {
			docType.setType(docTypeName);
			DecimalFormat twoDForm = new DecimalFormat("#.##");
			minConfidenceThreshold = Float.valueOf(twoDForm.format(minConfidenceThreshold));
			docType.setConfidenceThreshold(minConfidenceThreshold);
		} else {
			String errMsg = "DocumentType name is not found in the data base " + "for page type name: " + pageTypeName;
			LOGGER.info(errMsg);
		}
	}

	/**
	 * This method will create new document for pages that was found in the batch.xml file for Unknown type document.
	 * 
	 * @param docPageInfo {@link List<PageType>}
	 * @throws DCMAApplicationException {@link DCMAApplicationException} Check for input parameters, create new documents for page found in document type Unknown.
	 */
	public final void createDocForPages(final List<Page> docPageInfo) throws DCMAApplicationException {
		String errMsg = null;
		if (null == this.xmlDocuments) {
			throw new DCMAApplicationException("Unable to write pages for the document.");
		}
		try {
			List<Document> insertAllDocument = new ArrayList<Document>();
			List<Integer> removeIndexList = new ArrayList<Integer>();
			Document document = null;
			Long idGenerator = 0L;
			boolean isLast = true;
			boolean isFirst = true;

			for (int index = 0; index < docPageInfo.size(); index++) {
				Page pgType = docPageInfo.get(index);
				DocField docFieldType = getPgLevelField(pgType);
				if (null == docFieldType) {
					errMsg = "Invalid format of page level fields. DocFieldType found for "
							+ getPropertyMap().get(DocumentAssemblerConstants.AUTOMATIC_CLASSIFICATION) + " classification is null.";
					throw new DCMAApplicationException(errMsg);
				}
				String value = docFieldType.getValue();
				float confidenceScore = docFieldType.getConfidence();
				if (null == value) {
					errMsg = "Invalid format of page level fields. Value found for "
							+ getPropertyMap().get(DocumentAssemblerConstants.AUTOMATIC_CLASSIFICATION) + " classification is null.";
					throw new DCMAApplicationException(errMsg);
				}
				// check for zero confidence score value
				// for zero value just leave the page to unknown type.
				if (confidenceScore == 0) {
					document = new Document();
					Pages pages = new Pages();
					idGenerator++;
					document.setIdentifier(EphesoftProperty.DOCUMENT.getProperty() + idGenerator);
					document.setPages(pages);
					document.setDocumentDisplayInfo(BatchConstants.EMPTY);
					insertAllDocument.add(document);
					document.getPages().getPage().add(pgType);
					document.setType(EphesoftProperty.UNKNOWN.getProperty());
					removeIndexList.add(index);
					continue;
				}
				if (isLast) {
					document = new Document();
					Pages pages = new Pages();
					idGenerator++;
					document.setIdentifier(EphesoftProperty.DOCUMENT.getProperty() + idGenerator);
					document.setPages(pages);
					document.setDocumentDisplayInfo(BatchConstants.EMPTY);
					insertAllDocument.add(document);
					isLast = false;
					isFirst = false;
				}
				if (value.contains(getPropertyMap().get(DocumentAssemblerConstants.CHECK_FIRST_PAGE))) {
					if (isFirst) {
						document = new Document();
						Pages pages = new Pages();
						idGenerator++;
						document.setIdentifier(EphesoftProperty.DOCUMENT.getProperty() + idGenerator);
						document.setPages(pages);
						document.setDocumentDisplayInfo(BatchConstants.EMPTY);
						insertAllDocument.add(document);
					}
					isFirst = true;
					document.getPages().getPage().add(pgType);
					removeIndexList.add(index);
				} else {
					if (value.contains(getPropertyMap().get(DocumentAssemblerConstants.CHECK_MIDDLE_PAGE))) {
						document.getPages().getPage().add(pgType);
						removeIndexList.add(index);
						isFirst = true;
					} else {
						if (value.contains(getPropertyMap().get(DocumentAssemblerConstants.CHECK_LAST_PAGE))) {
							document.getPages().getPage().add(pgType);
							removeIndexList.add(index);
							isLast = true;
						} else {
							errMsg = "For page type value: " + value + "  and page ID : " + pgType.getIdentifier()
									+ " , Data format is not correct in the batch.xml file. "
									+ getPropertyMap().get(DocumentAssemblerConstants.CHECK_FIRST_PAGE) + " , "
									+ getPropertyMap().get(DocumentAssemblerConstants.CHECK_MIDDLE_PAGE) + " and "
									+ getPropertyMap().get(DocumentAssemblerConstants.CHECK_LAST_PAGE)
									+ " any of the three are not present to the name <Value> tag.";
							LOGGER.error(errMsg);
						}
					}
				}
			}
			// update the xml file.
			updateBatchXML(insertAllDocument, removeIndexList);

		} catch (Exception e) {
			errMsg = "Unable to write pages for the document. " + e.getMessage();
			LOGGER.error(errMsg);
			throw new DCMAApplicationException(errMsg, e);
		}
	}

	/**
	 * Update Batch XML file.
	 * 
	 * @param insertAllDocument {@link List<DocumentType>}
	 * @param removeIndexList {@link List<Integer>}
	 * @throws DCMAApplicationException {@link DCMAApplicationException} Check for input parameters, update the batch xml.
	 */
	private void updateBatchXML(final List<Document> insertAllDocument, final List<Integer> removeIndexList)
			throws DCMAApplicationException {

		String errMsg = null;

		Batch batch = batchSchemaService.getBatch(batchInstanceID);

		List<Document> xmlDocuments = batch.getDocuments().getDocument();
		if (null != xmlDocuments && null != insertAllDocument && !insertAllDocument.isEmpty()) {
			xmlDocuments.addAll(insertAllDocument);
		}

		List<Page> docPageInfo = null;

		int indexDocType = -1;

		for (int i = 0; i < xmlDocuments.size(); i++) {
			final Document document = xmlDocuments.get(i);
			String docType = document.getType();
			if (null != docType && docType.equals(EphesoftProperty.UNKNOWN.getProperty())) {
				docPageInfo = document.getPages().getPage();
				indexDocType = i;
				break;
			}
		}

		if (!removeIndexList.isEmpty()) {
			for (int index = removeIndexList.size() - 1; index >= 0; index--) {
				int rIndex = removeIndexList.get(index);
				if (rIndex < docPageInfo.size()) {
					docPageInfo.remove(rIndex);
				} else {
					errMsg = "Invalid argument for removal of pages for document type : " + EphesoftProperty.UNKNOWN.getProperty();
					LOGGER.error(errMsg);
					throw new DCMAApplicationException(errMsg);
				}
			}
		}

		// Delete the document type "Unknown" if no more pages are present.
		if (indexDocType != -1 && null != docPageInfo && docPageInfo.isEmpty()) {
			xmlDocuments.remove(indexDocType);
		}

		// set the confidence score and document type name on the basis of
		// defined rules.
		setDocConfAndDocType(xmlDocuments);

		// merge the unknown documents on the basis of a check
		String mergeSwitch = pluginPropertiesService.getPropertyValue(batchInstanceID,
				DocumentAssemblerConstants.DOCUMENT_ASSEMBLER_PLUGIN, DocumentAssemblerProperties.DA_MERGE_UNKNOWN_DOCUMENT_SWITCH);
		int xmlDocSize = xmlDocuments.size();
		if (mergeSwitch != null && mergeSwitch.equalsIgnoreCase(DocumentAssemblerConstants.DA_MERGE_SWITCH_ON) && xmlDocSize > 1) {
			for (int i = 1; i < xmlDocuments.size();) {
				final Document document = xmlDocuments.get(i);
				String docType = document.getType();
				if (null != docType && docType.equals(EphesoftProperty.UNKNOWN.getProperty())) {
					final Document prevDocument = xmlDocuments.get(i - 1);
					if (null != prevDocument.getType() && !prevDocument.getType().equals(EphesoftProperty.UNKNOWN.getProperty())) {
						prevDocument.getPages().getPage().addAll(document.getPages().getPage());
						xmlDocuments.remove(i);
					} else {
						i++;
					}
				} else {
					i++;
				}
			}
		}
		// setting the batch level documentClassificationTypes.
		DocumentClassificationTypes documentClassificationTypes = new DocumentClassificationTypes();
		List<String> documentClassificationType = documentClassificationTypes.getDocumentClassificationType();
		documentClassificationType.add(getPropertyMap().get(DocumentAssemblerConstants.FACTORY_CLASSIFICATION));
		batch.setDocumentClassificationTypes(documentClassificationTypes);

		// Set the error message explicitly to blank to display the node in batch xml
		for (int i = 0; i < xmlDocuments.size(); i++) {
			final Document document = xmlDocuments.get(i);
			document.setErrorMessage(DocumentAssemblerConstants.EMPTY);
		}
		// now write the state of the object to the xml file.
		batchSchemaService.updateBatch(batch);

		LOGGER.info("updateBatchXML done.");

	}

	/**
	 * This method will retrieve the page level field type docFieldType for any input page type for automatic classification. It
	 * fetches the type with a max confidence score.In case of equal max scores , the precedence is given to the order as defined in
	 * the include list in the properties file.
	 * 
	 * @param pgType {@link PageType}
	 * @return docFieldType {@link DocFieldType}
	 */
	private DocField getPgLevelField(final Page pgType) {
		StringTokenizer tokenList = new StringTokenizer(getPropertyMap().get(DocumentAssemblerConstants.AUTOMATIC_INCLUDE_LIST), ";=");
		ArrayList<String> includeList = new ArrayList<String>();
		while (tokenList.hasMoreElements()) {
			String includeListElem = (String) tokenList.nextElement();
			includeList.add(includeListElem);
		}
		String name = DocumentAssemblerConstants.EMPTY;
		int lastIncludeListIndex = -1, includeListIndex = -1;
		DocField docFieldType = null;
		Float confidence, maxConfidence = -1f;
		if (null != pgType) {
			PageLevelFields pageLevelFields = pgType.getPageLevelFields();
			if (null != pageLevelFields) {
				List<DocField> pageLevelField = pageLevelFields.getPageLevelField();
				for (DocField docFdType : pageLevelField) {
					if (null != docFdType) {
						name = docFdType.getName();
						includeListIndex = contains(includeList, name);
						if (includeListIndex == -1) {
							continue;
						}
						confidence = docFdType.getConfidence();
						if (maxConfidence == -1 || null == docFieldType || confidence >= maxConfidence) {
							if (confidence > maxConfidence) {
								maxConfidence = confidence;
								docFieldType = docFdType;
								lastIncludeListIndex = includeListIndex;
							} else if (includeListIndex < lastIncludeListIndex) {
								maxConfidence = confidence;
								docFieldType = docFdType;
								lastIncludeListIndex = includeListIndex;
							}
						}
					}
				}
			}
		}

		return docFieldType;

	}

	/**
	 * This method will set the confidence score of every document on the basis of average of all the page confidence score.
	 * 
	 * @param xmlDocuments {@link List<DocumentType>}
	 * @throws DCMAApplicationException {@link DCMAApplicationException} Check for input parameters, update the batch xml.
	 */
	@SuppressWarnings("unchecked")
	private void setDocConfAndDocType(final List<Document> xmlDocuments) throws DCMAApplicationException {

		Map<String, Object[]> docConfidence = null;
		float confidenceScoreMax = 0.0f;
		String pageTypeName = null;
		float localSum = 0.0f;
		String classificationType = null;
		for (Document docType : xmlDocuments) {

			classificationType = checkBarcodeDAClassification(docType);

			if (classificationType != null && classificationType.indexOf(getBarcodeClassification()) > -1) {
				continue;
			}

			String type = docType.getType();
			if (null != type && type.equals(EphesoftProperty.UNKNOWN.getProperty())) {
				continue;
			}

			docConfidence = new HashMap<String, Object[]>();
			confidenceScoreMax = 0.0f;

			Pages pages = docType.getPages();
			if (null != pages) {
				List<Page> pageList = pages.getPage();
				for (Page pgType : pageList) {
					if (null != pgType) {
						PageLevelFields pageLevelFields = pgType.getPageLevelFields();
						if (null != pageLevelFields) {
							traversePgFds(pageLevelFields, docConfidence, classificationType);
						}
					}
				}
			}

			Set<String> set = docConfidence.keySet();
			Iterator<String> itr = set.iterator();
			while (itr.hasNext()) {
				String key = itr.next();
				Object[] objArr = docConfidence.get(key);
				List<Float> conFloatList = (List<Float>) objArr[1];
				localSum = 0.0f;
				for (Float f : conFloatList) {
					localSum += f;
				}
				if (confidenceScoreMax < localSum) {
					confidenceScoreMax = localSum;
					pageTypeName = key;
				}
			}

			Object[] objArr = docConfidence.get(pageTypeName);
			List<Float> conFloatList = (List<Float>) objArr[1];
			List<String> checkTypeList = (List<String>) objArr[0];

			float multiplyingFactor = 1.00f;
			try {
				multiplyingFactor = multiplyingFactor(checkTypeList);
			} catch (Exception e) {
				LOGGER.error("Invalid multiplyingFactor. " + e.getMessage());
			}

			float confidenceScore = (multiplyingFactor * confidenceScoreMax) / conFloatList.size();
			LOGGER.info("multiplyingFactor : " + multiplyingFactor + "  confidence score : " + confidenceScore
					+ " for document Type ID : " + docType.getIdentifier());

			DecimalFormat twoDForm = new DecimalFormat("#.##");
			confidenceScore = Float.valueOf(twoDForm.format(confidenceScore));

			docType.setConfidence(confidenceScore);
			if (null != checkTypeList && !checkTypeList.isEmpty()) {
				StringBuilder stringBuilder = new StringBuilder("");
				stringBuilder.append(pageTypeName);
				stringBuilder.append(checkTypeList.get(0));
				pageTypeName = stringBuilder.toString();
			} else {
				StringBuilder stringBuilder = new StringBuilder("");
				stringBuilder.append(pageTypeName);
				stringBuilder.append(getPropertyMap().get(DocumentAssemblerConstants.CHECK_FIRST_PAGE));
				pageTypeName = stringBuilder.toString();
			}

			setDocTypeNameAndConfThreshold(docType, pageTypeName);

		}

	}

	/**
	 * This method will check the barcode classification.
	 * 
	 * @param docType {@link Document}
	 * @return {@link String}
	 * @throws DCMAApplicationException {@link DCMAApplicationException} Check for input parameters, update the batch xml.
	 */
	private String checkBarcodeDAClassification(Document docType) throws DCMAApplicationException {

		List<Page> pages = docType.getPages().getPage();
		String errMsg = null;
		String name = null;
		String value = DocumentAssemblerConstants.EMPTY;
		for (int index = 0; index < pages.size(); index++) {

			Page pgType = pages.get(index);
			DocField docField = getPgLevelField(pgType);
			value = docField.getValue();

			if (null == value) {
				errMsg = "Invalid format of page level fields. Value found for " + getBarcodeClassification()
						+ " classification is null.";
				throw new DCMAApplicationException(errMsg);
			}
			name = docField.getName();
			if (docField.getName().indexOf(getBarcodeClassification()) > -1) {
				setDocTypeNameAndConfThreshold(docType, value);
				int confidenceValue = 0;
				try {
					confidenceValue = Integer.parseInt(getBarcodeConfidence());
				} catch (NumberFormatException nfe) {
					errMsg = "Invalid integer for barcode confidence score in properties file." + nfe.getMessage();
					LOGGER.error(errMsg);
					throw new DCMAApplicationException(errMsg, nfe);
				}
				docType.setConfidence(confidenceValue);
				break;
			}
		}
		return name;
	}

	/**
	 * This method will apply the rule to calculate the confidence score.
	 * 
	 * @param checkTypeList {@link List<String>}
	 * @return multiplyingFactor float
	 */
	private float multiplyingFactor(List<String> checkTypeList) {

		float multiplyingFactor = 1.00f;
		float intialFactor = 1.00f;
		String checkFirstPage = getPropertyMap().get(DocumentAssemblerConstants.CHECK_FIRST_PAGE);
		String checkMiddlePage = getPropertyMap().get(DocumentAssemblerConstants.CHECK_MIDDLE_PAGE);
		String checkLastPage = getPropertyMap().get(DocumentAssemblerConstants.CHECK_LAST_PAGE);
		int fmlPage = Integer.parseInt(getPropertyMap().get(DocumentAssemblerConstants.RULE_FML_PAGE));
		int fPage = Integer.parseInt(getPropertyMap().get(DocumentAssemblerConstants.RULE_F_PAGE));
		int mPage = Integer.parseInt(getPropertyMap().get(DocumentAssemblerConstants.RULE_M_PAGE));
		int lPage = Integer.parseInt(getPropertyMap().get(DocumentAssemblerConstants.RULE_L_PAGE));
		int fmPage = Integer.parseInt(getPropertyMap().get(DocumentAssemblerConstants.RULE_FM_PAGE));
		int flPage = Integer.parseInt(getPropertyMap().get(DocumentAssemblerConstants.RULE_FL_PAGE));
		int mlPage = Integer.parseInt(getPropertyMap().get(DocumentAssemblerConstants.RULE_ML_PAGE));

		if (null != checkTypeList) {
			int placeHolder = 0;

			if (checkTypeList.contains(checkFirstPage)) {
				placeHolder = placeHolder | PlaceHolder.FP.getValue();
			}

			if (checkTypeList.contains(checkMiddlePage)) {
				placeHolder = placeHolder | PlaceHolder.MP.getValue();
			}

			if (checkTypeList.contains(checkLastPage)) {
				placeHolder = placeHolder | PlaceHolder.LP.getValue();
			}

			switch (PlaceHolder.getPlaceHolder(placeHolder)) {
				case FP:
					// multiplyingFactor = 0.50f;
					multiplyingFactor = (fPage * intialFactor) / fmlPage;
					break;
				case MP:
					// multiplyingFactor = 0.25f;
					multiplyingFactor = (mPage * intialFactor) / fmlPage;
					break;
				case FMP:
					// multiplyingFactor = 0.50f;
					multiplyingFactor = (fmPage * intialFactor) / fmlPage;
					break;
				case LP:
					// multiplyingFactor = 0.50f;
					multiplyingFactor = (lPage * intialFactor) / fmlPage;
					break;
				case FLP:
					// multiplyingFactor = 0.75f;
					multiplyingFactor = (flPage * intialFactor) / fmlPage;
					break;
				case MLP:
					// multiplyingFactor = 0.50f;
					multiplyingFactor = (mlPage * intialFactor) / fmlPage;
					break;
				case FMLP:
					// multiplyingFactor = 1.00f;
					multiplyingFactor = (fmlPage * intialFactor) / fmlPage;
					break;
				default:
					// multiplyingFactor = 0.50f;
					multiplyingFactor = (fPage * intialFactor) / fmlPage;
					break;
			}
		}

		return multiplyingFactor;
	}

	/**
	 * This method will traverse the page level fields.
	 * 
	 * @param pageLevelFields {@link PageLevelFields}
	 * @param classificationType {@link String}
	 * @param docConfidence Map<String, List<Float>>
	 */
	@SuppressWarnings("unchecked")
	private void traversePgFds(PageLevelFields pageLevelFields, Map<String, Object[]> docConfidence, String classificationType) {

		String value = null;
		String checkType = null;
		String name = null;
		List<DocField> pageLevelField = pageLevelFields.getPageLevelField();
		for (DocField docFdType : pageLevelField) {
			if (null != docFdType) {
				name = docFdType.getName();
				if (null != name && name.contains(classificationType)) {
					AlternateValues alternateValues = docFdType.getAlternateValues();
					if (null != alternateValues) {
						List<Field> alternateValue = alternateValues.getAlternateValue();
						value = docFdType.getValue();
						if (value.contains(getPropertyMap().get(DocumentAssemblerConstants.CHECK_FIRST_PAGE))) {
							checkType = getPropertyMap().get(DocumentAssemblerConstants.CHECK_FIRST_PAGE);
						} else if (value.contains(getPropertyMap().get(DocumentAssemblerConstants.CHECK_MIDDLE_PAGE))) {
							checkType = getPropertyMap().get(DocumentAssemblerConstants.CHECK_MIDDLE_PAGE);
						} else if (value.contains(getPropertyMap().get(DocumentAssemblerConstants.CHECK_LAST_PAGE))) {
							checkType = getPropertyMap().get(DocumentAssemblerConstants.CHECK_LAST_PAGE);
						} else {
							LOGGER.info("In valid page level value tag.");
						}
						for (Field fdType : alternateValue) {
							String val = fdType.getValue();
							float confidence = fdType.getConfidence();
							if (null != val && val.contains(checkType)) {
								String[] arr = val.split(checkType);
								if (null != arr && arr.length > 0) {
									Object[] objArr = docConfidence.get(arr[0]);
									List<Float> conFloatList = null;
									List<String> checkTypeList = null;
									if (null == objArr) {
										objArr = new Object[2];
										conFloatList = new ArrayList<Float>();
										checkTypeList = new ArrayList<String>();
									} else {
										checkTypeList = (List<String>) objArr[0];
										conFloatList = (List<Float>) objArr[1];
									}
									checkTypeList.add(checkType);
									conFloatList.add(confidence);
									objArr[0] = checkTypeList;
									objArr[1] = conFloatList;
									docConfidence.put(arr[0], objArr);
								}
							}
						}
					}
				}
			}
		}
	}

	/**
	 * This method will read all the pages of the document for document type Unknown.
	 * 
	 * @return docPageInfo {@link List<PageType>}
	 * @throws DCMAApplicationException {@link DCMAApplicationException} Check for input parameters and read all pages of the document.
	 */
	public final List<Page> readAllPages() throws DCMAApplicationException {
		LOGGER.info("Reading the document for Document Assembler.");
		List<Page> docPageInfo = null;

		final Batch batch = batchSchemaService.getBatch(this.batchInstanceID);

		this.xmlDocuments = batch.getDocuments().getDocument();

		for (int i = 0; i < this.xmlDocuments.size(); i++) {
			final Document document = this.xmlDocuments.get(i);
			String docType = document.getType();
			if (null != docType && docType.equals(EphesoftProperty.UNKNOWN.getProperty())) {
				docPageInfo = document.getPages().getPage();
				break;
			}
		}

		return docPageInfo;
	}

	/**
	 * Method to check whether one of the include list String as mentioned in the properties file , is contained in the xml.
	 * 
	 * @param list include list from the properties file
	 * @param str {@link String} to be checked
	 * @return index at which the string is present in the include list
	 */
	private int contains(List<String> list, String str) {
		int isFound = -1;
		if (str == null) {
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i) == null) {
					isFound = i;
				}
			}
		} else {
			for (int i = 0; i < list.size(); i++) {
				if (str.equalsIgnoreCase(list.get(i)) || str.indexOf(list.get(i)) > -1) {
					isFound = i;
				}
			}
		}
		return isFound;

	}
}
