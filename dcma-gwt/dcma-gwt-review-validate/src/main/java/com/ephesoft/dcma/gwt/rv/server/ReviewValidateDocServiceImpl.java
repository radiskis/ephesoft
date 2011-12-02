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

package com.ephesoft.dcma.gwt.rv.server;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.core.io.ClassPathResource;

import com.ephesoft.dcma.batch.schema.Batch;
import com.ephesoft.dcma.batch.schema.BatchStatus;
import com.ephesoft.dcma.batch.schema.Column;
import com.ephesoft.dcma.batch.schema.Coordinates;
import com.ephesoft.dcma.batch.schema.DataTable;
import com.ephesoft.dcma.batch.schema.Direction;
import com.ephesoft.dcma.batch.schema.DocField;
import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.batch.schema.HocrPages;
import com.ephesoft.dcma.batch.schema.Page;
import com.ephesoft.dcma.batch.schema.Row;
import com.ephesoft.dcma.batch.schema.Document.DocumentLevelFields;
import com.ephesoft.dcma.batch.schema.Field.CoordinatesList;
import com.ephesoft.dcma.batch.schema.HocrPages.HocrPage;
import com.ephesoft.dcma.batch.schema.HocrPages.HocrPage.Spans.Span;
import com.ephesoft.dcma.batch.service.BatchInstancePluginPropertiesService;
import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.batch.service.PluginPropertiesService;
import com.ephesoft.dcma.core.DCMAException;
import com.ephesoft.dcma.core.common.BatchInstanceStatus;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.da.domain.BatchClass;
import com.ephesoft.dcma.da.domain.BatchInstance;
import com.ephesoft.dcma.da.domain.DocumentType;
import com.ephesoft.dcma.da.domain.FieldType;
import com.ephesoft.dcma.da.domain.FunctionKey;
import com.ephesoft.dcma.da.domain.RegexValidation;
import com.ephesoft.dcma.da.domain.TableColumnsInfo;
import com.ephesoft.dcma.da.domain.TableInfo;
import com.ephesoft.dcma.da.id.BatchInstanceID;
import com.ephesoft.dcma.da.service.BatchClassService;
import com.ephesoft.dcma.da.service.BatchInstanceService;
import com.ephesoft.dcma.da.service.TableColumnsInfoService;
import com.ephesoft.dcma.da.service.TableInfoService;
import com.ephesoft.dcma.encryption.security.SecurityTokenHandler;
import com.ephesoft.dcma.fuzzydb.service.FuzzyDBSearchService;
import com.ephesoft.dcma.gwt.core.server.DCMARemoteServiceServlet;
import com.ephesoft.dcma.gwt.core.shared.BatchClassBean;
import com.ephesoft.dcma.gwt.core.shared.BatchDTO;
import com.ephesoft.dcma.gwt.core.shared.DocumentTypeDBBean;
import com.ephesoft.dcma.gwt.core.shared.FieldTypeDTO;
import com.ephesoft.dcma.gwt.core.shared.FunctionKeyDTO;
import com.ephesoft.dcma.gwt.core.shared.PointCoordinate;
import com.ephesoft.dcma.gwt.core.shared.exception.GWTException;
import com.ephesoft.dcma.gwt.rv.client.ReviewValidateDocService;
import com.ephesoft.dcma.gwt.rv.client.constant.ValidateProperties;
import com.ephesoft.dcma.imagemagick.service.ImageProcessService;
import com.ephesoft.dcma.script.service.ScriptService;
import com.ephesoft.dcma.tablefinder.service.TableFinderService;
import com.ephesoft.dcma.util.FileUtils;
import com.ephesoft.dcma.workflow.service.common.WorkflowService;

public class ReviewValidateDocServiceImpl extends DCMARemoteServiceServlet implements ReviewValidateDocService {

	private static final long serialVersionUID = 440658407072287974L;

	private static final String META_INF = "META-INF";

	private static final String PROPERTY_FILE_NAME = "application.properties";

	private static final String LIST_VIEW = "dropdown_list";

	private static final String DOCUMENT_DEFAULT_VIEW_PROPERTY_KEY = "document.default_doc_type_view";

	private static final String VALIDATE_DOCUMENT_PLUGIN = "VALIDATE_DOCUMENT";

	private static final String DYNAMIC_FUNCTION_KEY_SCRIPT_NAME = "function_key_script_name";

	private static final String SCRIPT_TEST = "ScriptFunctionKey";

	private static final String FIELD_VALUE_CHANGE_SCRIPT_NAME = "field_value_change_script_name";

	private static final String DEFAULT_SCRIPT_FOR_FIELD_VALUE_CHANGE = "ScriptFieldValueChange";

	public BatchDTO getHighestPriortyBatch() {
		BatchInstanceService batchInstanceService = this.getSingleBeanOfType(BatchInstanceService.class);
		Set<String> allBatchClassByUserRoles = getAllBatchClassByUserRoles();
		BatchInstance batch = batchInstanceService.getHighestPriorityBatchInstance(allBatchClassByUserRoles);
		return getBatch(batch.getIdentifier());
	}

	public BatchDTO getBatch(String batchInstanceIdentifier) {

		BatchInstanceService batchInstanceService = this.getSingleBeanOfType(BatchInstanceService.class);
		BatchInstance batchInstance = batchInstanceService.getBatchInstanceByIdentifier(batchInstanceIdentifier);

		BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);
		Batch batch = batchSchemaService.getBatch(batchInstanceIdentifier);

		PluginPropertiesService pluginPropertiesService = this.getBeanByName("batchInstancePluginPropertiesService",
				BatchInstancePluginPropertiesService.class);
		String validateScriptSwitch = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, VALIDATE_DOCUMENT_PLUGIN,
				ValidateProperties.VAILDATE_DOCUMENT_SCRIPTING_SWITCH);

		String fieldValueChangeScriptSwitch = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier,
				VALIDATE_DOCUMENT_PLUGIN, ValidateProperties.FIELD_VALUE_CHANGE_SCRIPT_SWITCH);

		String fuzzySearchSwitch = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, VALIDATE_DOCUMENT_PLUGIN,
				ValidateProperties.FUZZY_SEARCH_SWITCH);

		String suggestionBoxSwitchState = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, VALIDATE_DOCUMENT_PLUGIN,
				ValidateProperties.SUGGESTION_BOX_SWITCH);

		String externalApplicationSwitchState = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier,
				VALIDATE_DOCUMENT_PLUGIN, ValidateProperties.EXTERNAL_APP_SWITCH);
		Map<String, String> urlAndShortcutMap = null;
		Map<String, String> dimensionsForPopUp = null;
		Map<String, String> urlAndTitleMap = null;
		if (null != externalApplicationSwitchState && externalApplicationSwitchState.equals("ON")) {
			dimensionsForPopUp = new HashMap<String, String>();
			urlAndShortcutMap = new LinkedHashMap<String, String>();
			urlAndTitleMap = new LinkedHashMap<String, String>();
			getPropertiesOfExternalApplication(pluginPropertiesService, batchInstanceIdentifier, urlAndShortcutMap,
					dimensionsForPopUp, urlAndTitleMap);
		}

		switch (batchInstance.getStatus()) {
			case READY_FOR_REVIEW:
				batch.setBatchStatus(BatchStatus.READY_FOR_REVIEW);
				break;
			case READY_FOR_VALIDATION:
				batch.setBatchStatus(BatchStatus.READY_FOR_VALIDATION);
				break;
			default:
				break;
		}
		URL batchURL = batchSchemaService.getBatchContextURL(batchInstanceIdentifier);
		return new BatchDTO(batch, batchURL.toString(), validateScriptSwitch, fieldValueChangeScriptSwitch, fuzzySearchSwitch,
				suggestionBoxSwitchState, externalApplicationSwitchState, urlAndShortcutMap, dimensionsForPopUp, urlAndTitleMap);
	}

	private void getPropertiesOfExternalApplication(PluginPropertiesService pluginPropertiesService, String batchInstanceIdentifier,
			Map<String, String> urlAndShortcutMap, Map<String, String> dimensionsForPopUp, Map<String, String> urlAndTitleMap) {
		String xDimension = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, "VALIDATE_DOCUMENT",
				ValidateProperties.EXTERNAL_APP_X_DIMENSION);
		String yDimension = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, "VALIDATE_DOCUMENT",
				ValidateProperties.EXTERNAL_APP_Y_DIMENSION);

		dimensionsForPopUp.put(ValidateProperties.EXTERNAL_APP_X_DIMENSION.getPropertyKey(), xDimension);
		dimensionsForPopUp.put(ValidateProperties.EXTERNAL_APP_Y_DIMENSION.getPropertyKey(), yDimension);

		String url1 = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, "VALIDATE_DOCUMENT",
				ValidateProperties.EXTERNAL_APP_URL1);
		String url2 = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, "VALIDATE_DOCUMENT",
				ValidateProperties.EXTERNAL_APP_URL2);
		String url3 = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, "VALIDATE_DOCUMENT",
				ValidateProperties.EXTERNAL_APP_URL3);
		String url4 = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, "VALIDATE_DOCUMENT",
				ValidateProperties.EXTERNAL_APP_URL4);

		String titleForUrl1 = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, "VALIDATE_DOCUMENT",
				ValidateProperties.TITLE_EXTERNAL_APP_URL1);
		String titleForUrl2 = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, "VALIDATE_DOCUMENT",
				ValidateProperties.TITLE_EXTERNAL_APP_URL2);
		String titleForUrl3 = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, "VALIDATE_DOCUMENT",
				ValidateProperties.TITLE_EXTERNAL_APP_URL3);
		String titleForUrl4 = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, "VALIDATE_DOCUMENT",
				ValidateProperties.TITLE_EXTERNAL_APP_URL4);

		if (null != url1 && !url1.isEmpty()) {
			urlAndShortcutMap.put(ValidateProperties.EXTERNAL_APP_URL1.getPropertyKey(), url1);
		}
		if (null != url2 && !url2.isEmpty()) {
			urlAndShortcutMap.put(ValidateProperties.EXTERNAL_APP_URL2.getPropertyKey(), url2);
		}
		if (null != url3 && !url3.isEmpty()) {
			urlAndShortcutMap.put(ValidateProperties.EXTERNAL_APP_URL3.getPropertyKey(), url3);
		}
		if (null != url4 && !url4.isEmpty()) {
			urlAndShortcutMap.put(ValidateProperties.EXTERNAL_APP_URL4.getPropertyKey(), url4);
		}

		if (null != titleForUrl1 && !titleForUrl1.isEmpty()) {
			urlAndTitleMap.put(ValidateProperties.EXTERNAL_APP_URL1.getPropertyKey(), titleForUrl1);
		}
		if (null != titleForUrl2 && !titleForUrl2.isEmpty()) {
			urlAndTitleMap.put(ValidateProperties.EXTERNAL_APP_URL2.getPropertyKey(), titleForUrl2);
		}
		if (null != titleForUrl3 && !titleForUrl3.isEmpty()) {
			urlAndTitleMap.put(ValidateProperties.EXTERNAL_APP_URL3.getPropertyKey(), titleForUrl3);
		}
		if (null != titleForUrl4 && !titleForUrl4.isEmpty()) {
			urlAndTitleMap.put(ValidateProperties.EXTERNAL_APP_URL4.getPropertyKey(), titleForUrl4);
		}
	}

	@Override
	public BatchStatus updateBatch(Batch batch) {
		WorkflowService workflowService = this.getSingleBeanOfType(WorkflowService.class);
		return workflowService.updateBatch(batch, getUserName());
		// BatchStatus returnStatus = workflowService.updateBatch(batch, getUserName());
		// return returnStatus;
	}

	@Override
	public BatchStatus signalWorkflow(Batch batch) {
		WorkflowService workflowService = this.getSingleBeanOfType(WorkflowService.class);
		return workflowService.updateBatchAndSignalWorkflow(batch, getUserName());
		// BatchStatus returnStatus = workflowService.updateBatchAndSignalWorkflow(batch, getUserName());
		// return returnStatus;
	}

	@Override
	public List<DocumentTypeDBBean> getDocTypeByBatchInstanceID(String batchInstanceID) {
		// DocumentTypeService documentTypeService =
		// this.getSingleBeanOfType(DocumentTypeService.class);
		/*
		 * List<com.ephesoft.dcma.da.domain.DocumentType> documentTypes = documentTypeService
		 * .getDocTypeByBatchInstanceID(batchInstanceID);
		 */
		// PluginPropertiesService pluginPropertiesService = this
		// .getSingleBeanOfType(BatchInstancePluginPropertiesService.class);
		PluginPropertiesService pluginPropertiesService = this.getBeanByName("batchInstancePluginPropertiesService",
				BatchInstancePluginPropertiesService.class);
		List<com.ephesoft.dcma.da.domain.DocumentType> documentTypes = pluginPropertiesService.getDocumentTypes(batchInstanceID);

		List<DocumentTypeDBBean> documentTypeBeans = new ArrayList<DocumentTypeDBBean>();
		for (com.ephesoft.dcma.da.domain.DocumentType documentType : documentTypes) {
			DocumentTypeDBBean bean = new DocumentTypeDBBean();
			bean.setDescription(documentType.getDescription());
			bean.setRspProjectFileName(documentType.getRspProjectFileName());
			bean.setMinConfidenceThreshold(documentType.getMinConfidenceThreshold());
			bean.setName(documentType.getName());
			bean.setPriority(documentType.getPriority());
			bean.setHidden(documentType.isHidden());

			BatchClassBean classBean = new BatchClassBean();
			classBean.setDescription(documentType.getBatchClass().getDescription());
			classBean.setName(documentType.getBatchClass().getName());
			classBean.setPriority(documentType.getBatchClass().getPriority());
			classBean.setProcessName(documentType.getBatchClass().getProcessName());
			classBean.setUncFolder(documentType.getBatchClass().getUncFolder());
			classBean.setVersion(documentType.getBatchClass().getVersion());
			bean.setBatchClass(classBean);
			if (!bean.isHidden()) {
				documentTypeBeans.add(bean);
			}
		}

		return documentTypeBeans;

	}

	@Override
	public BatchDTO mergeDocument(Batch batch, String documentId, String documentIdToBeMerged) {
		BatchDTO batchDTO = null;
		BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);
		try {
			batchSchemaService.updateBatch(batch);
			Batch rtbatch = batchSchemaService.mergeDocuments(batch.getBatchInstanceIdentifier(), documentId, documentIdToBeMerged);
			URL batchURL = batchSchemaService.getBatchContextURL(rtbatch.getBatchInstanceIdentifier());
			PluginPropertiesService pluginPropertiesService = this.getBeanByName("batchInstancePluginPropertiesService",
					BatchInstancePluginPropertiesService.class);
			String batchInstanceIdentifier = batch.getBatchInstanceIdentifier();
			String validateScriptSwitch = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, VALIDATE_DOCUMENT_PLUGIN,
					ValidateProperties.VAILDATE_DOCUMENT_SCRIPTING_SWITCH);

			String fieldValueChangeScriptSwitch = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier,
					VALIDATE_DOCUMENT_PLUGIN, ValidateProperties.FIELD_VALUE_CHANGE_SCRIPT_SWITCH);

			String fuzzySearchSwitch = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, VALIDATE_DOCUMENT_PLUGIN,
					ValidateProperties.FUZZY_SEARCH_SWITCH);
			String suggestionBoxSwitchState = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier,
					VALIDATE_DOCUMENT_PLUGIN, ValidateProperties.SUGGESTION_BOX_SWITCH);
			String externalApplicationSwitchState = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier,
					VALIDATE_DOCUMENT_PLUGIN, ValidateProperties.EXTERNAL_APP_SWITCH);
			Map<String, String> urlAndShortcutMap = null;
			Map<String, String> dimensionsForPopUp = null;
			Map<String, String> urlAndTitleMap = null;
			if (null != externalApplicationSwitchState && externalApplicationSwitchState.equals("ON")) {
				getPropertiesOfExternalApplication(pluginPropertiesService, batchInstanceIdentifier, urlAndShortcutMap,
						dimensionsForPopUp, urlAndTitleMap);
			}

			batchDTO = new BatchDTO(rtbatch, batchURL.toString(), validateScriptSwitch, fieldValueChangeScriptSwitch,
					fuzzySearchSwitch, suggestionBoxSwitchState, externalApplicationSwitchState, urlAndShortcutMap,
					dimensionsForPopUp, urlAndTitleMap);

		} catch (DCMAApplicationException e) {
			log.error(e.getMessage(), e);
		}
		return batchDTO;
	}

	@Override
	public Document getFdTypeByDocTypeName(String batchInstanceIdentifier, String docTypeName) {
		// FieldTypeService fieldTypeService =
		// this.getSingleBeanOfType(FieldTypeService.class);
		// List<FieldType> listFieldTypes =
		// fieldTypeService.getFdTypeByDocTypeNameForBatchInstance(docTypeName,
		// Long.parseLong(batchID));
		// PluginPropertiesService pluginPropertiesService = this
		// .getSingleBeanOfType(BatchInstancePluginPropertiesService.class);
		PluginPropertiesService pluginPropertiesService = this.getBeanByName("batchInstancePluginPropertiesService",
				BatchInstancePluginPropertiesService.class);
		List<FieldType> listFieldTypes = pluginPropertiesService.getFieldTypes(batchInstanceIdentifier, docTypeName);

		Document documentType = new Document();
		DocumentLevelFields documentLevelFields = new DocumentLevelFields();
		List<DocField> documentLevelField = documentLevelFields.getDocumentLevelField();
		for (FieldType fieldType : listFieldTypes) {
			DocField dDocFieldType = new DocField();
			dDocFieldType.setConfidence(0);
			dDocFieldType.setCoordinatesList(null);
			dDocFieldType.setAlternateValues(null);
			dDocFieldType.setName(fieldType.getName());
			dDocFieldType.setFieldOrderNumber(fieldType.getFieldOrderNumber());
			dDocFieldType.setOverlayedImageFileName(null);
			dDocFieldType.setPage(null);
			dDocFieldType.setType(fieldType.getDataType().name());
			dDocFieldType.setValue(null);
			dDocFieldType.setFieldOrderNumber(fieldType.getFieldOrderNumber());
			dDocFieldType.setFieldValueOptionList(fieldType.getFieldOptionValueList());
			documentLevelField.add(dDocFieldType);
		}
		documentType.setDocumentLevelFields(documentLevelFields);
		documentType.setConfidence(0);
		documentType.setErrorMessage("");
		return documentType;
	}

	public BatchDTO duplicatePageOfDocument(Batch batch, String docID, String duplicatePageID) {
		String batchInstanceIdentifier = batch.getBatchInstanceIdentifier();
		try {
			BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);
			batchSchemaService.updateBatch(batch);
			batchSchemaService.duplicatePageOfDocument(batchInstanceIdentifier, docID, duplicatePageID);
		} catch (Exception e) {
			// TODO: handle exception
			// For now eat me.
		}

		return getBatch(batchInstanceIdentifier);
	}

	@Override
	public BatchDTO deletePageOfDocument(Batch batch, String docID, String pageId) {
		BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);
		String batchInstanceId = batch.getBatchInstanceIdentifier();
		try {
			batchSchemaService.updateBatch(batch);
			batchSchemaService.removePageOfDocument(batchInstanceId, docID, pageId);
			// return true;
		} catch (NumberFormatException e) {
			e.printStackTrace();
			// return false;
		} catch (DCMAApplicationException e) {
			e.printStackTrace();
			// return false;
		}
		return getBatch(batchInstanceId);
	}

	@Override
	public List<List<String>> fuzzyTextSearch(Batch batch, String searchText) {
		FuzzyDBSearchService fuzzyDBSearchService = this.getSingleBeanOfType(FuzzyDBSearchService.class);
		String batchInstanceIdentifier = batch.getBatchInstanceIdentifier();
		List<List<String>> result = null;
		try {
			result = fuzzyDBSearchService.fuzzyTextSearch(new BatchInstanceID(batchInstanceIdentifier), searchText);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (DCMAException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public BatchDTO splitDocument(Batch batch, String docID, String pageId) {
		BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);
		String batchInstanceIdentifier = batch.getBatchInstanceIdentifier();
		try {
			batchSchemaService.updateBatch(batch);
			batchSchemaService.splitDocument(batchInstanceIdentifier, docID, pageId);
			// return true;
		} catch (NumberFormatException e) {
			e.printStackTrace();
			// return false;
		} catch (DCMAApplicationException e) {
			e.printStackTrace();
			// return false;
		}
		return getBatch(batchInstanceIdentifier);
	}

	@Override
	public BatchDTO movePageOfDocument(Batch batch, String fromPageID, String fromDocID, String toDocId, String toPageID,
			Boolean moveAfterchecked) {
		BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);
		String batchInstanceID = batch.getBatchInstanceIdentifier();
		try {
			batchSchemaService.updateBatch(batch);
			batchSchemaService.movePageOfDocument(batchInstanceID, fromDocID, fromPageID, toDocId, toPageID, moveAfterchecked
					.booleanValue());
		} catch (DCMAApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return getBatch(batchInstanceID);

	}

	@Override
	public Page roatateImage(Batch batch, Page page, String documentId) {

		ImageProcessService imageProcessService = this.getSingleBeanOfType(ImageProcessService.class);
		try {
			String batchInstanceIdentifier = batch.getBatchInstanceIdentifier();
			String pageId = page.getIdentifier();
			BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);
			batchSchemaService.updateBatch(batch);
			imageProcessService.rotateImage(new BatchInstanceID(batchInstanceIdentifier), documentId, pageId);
			Direction direction = page.getDirection();
			if (null == direction) {
				direction = Direction.NORTH;
				page.setDirection(direction);
			}
			Direction newDirection = null;
			switch (direction) {
				case EAST:
					newDirection = Direction.SOUTH;
					break;
				case SOUTH:
					newDirection = Direction.WEST;
					break;
				case WEST:
					newDirection = Direction.NORTH;
					break;
				case NORTH:
					newDirection = Direction.EAST;
					break;
				default:
					newDirection = Direction.NORTH;
					break;
			}
			page.setDirection(newDirection);
			page.setIsRotated(true);
			String batchFolderName = batch.getBatchLocalPath() + File.separator + batch.getBatchInstanceIdentifier();
			File outputFolder = new File(batchFolderName + File.separator + newDirection.toString());
			if (!outputFolder.exists()) {
				outputFolder.mkdir();
			}
			File inputImage = new File(batchFolderName + File.separator + page.getDisplayFileName());
			File inputThumbNailImage = new File(batchFolderName + File.separator + page.getThumbnailFileName());
			File outputImage = new File(batchFolderName + File.separator + newDirection.toString() + File.separator
					+ page.getDisplayFileName());
			File outputThumbNailImage = new File(batchFolderName + File.separator + newDirection.toString() + File.separator
					+ page.getThumbnailFileName());
			try {
				FileUtils.copyFile(inputImage, outputImage);
				FileUtils.copyFile(inputThumbNailImage, outputThumbNailImage);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DCMAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return page;
	}

	@Override
	public int getRowsCount() {
		List<BatchInstanceStatus> statusList = new ArrayList<BatchInstanceStatus>();
		statusList.add(BatchInstanceStatus.READY_FOR_REVIEW);
		statusList.add(BatchInstanceStatus.READY_FOR_VALIDATION);
		int rowCount = 0;
		BatchInstanceService batchClassService = this.getSingleBeanOfType(BatchInstanceService.class);
		Set<String> allBatchClassByUserRoles = getAllBatchClassByUserRoles();
		rowCount = batchClassService.getCount(statusList, null, allBatchClassByUserRoles);
		return rowCount;
	}

	@Override
	public List<FieldTypeDTO> getFieldTypeDTOs(String documentName, String batchInstanceIdentifier) {

		PluginPropertiesService pluginPropertiesService = this.getBeanByName("batchInstancePluginPropertiesService",
				BatchInstancePluginPropertiesService.class);
		List<FieldType> allFdTypes = pluginPropertiesService.getFieldTypes(batchInstanceIdentifier, documentName);

		List<FieldTypeDTO> fieldTypeDTOs = new ArrayList<FieldTypeDTO>();

		if (null != allFdTypes && !allFdTypes.isEmpty()) {

			for (com.ephesoft.dcma.da.domain.FieldType fdType : allFdTypes) {

				if (null == fdType) {
					continue;
				}

				final String dbFdName = fdType.getName();

				final String sampleValue = fdType.getSampleValue();

				if (null == dbFdName || dbFdName.isEmpty()) {
					continue;
				}
				final List<RegexValidation> regexValidationList = fdType.getRegexValidation();

				if (null == regexValidationList || regexValidationList.isEmpty()) {
					FieldTypeDTO fieldTypeDTO = new FieldTypeDTO();
					fieldTypeDTO.setName(dbFdName);
					fieldTypeDTO.setRegexPatternList(new ArrayList<String>());
					fieldTypeDTO.setSampleValue(sampleValue);
					fieldTypeDTO.setFieldOptionValueList(fdType.getFieldOptionValueList());
					fieldTypeDTO.setDescription(fdType.getDescription());
					fieldTypeDTO.setHidden(fdType.isHidden());
					fieldTypeDTOs.add(fieldTypeDTO);
					continue;
				}

				List<String> patternList = new ArrayList<String>();
				for (RegexValidation regexValidation : regexValidationList) {
					if (null == regexValidation) {
						continue;
					}
					String pattern = regexValidation.getPattern();
					if (null == pattern || pattern.isEmpty()) {
						continue;
					}
					patternList.add(pattern);
				}

				if (!patternList.isEmpty()) {
					FieldTypeDTO fieldTypeDTO = new FieldTypeDTO();
					fieldTypeDTO.setName(dbFdName);
					fieldTypeDTO.setRegexPatternList(patternList);
					fieldTypeDTO.setSampleValue(sampleValue);
					fieldTypeDTO.setFieldOptionValueList(fdType.getFieldOptionValueList());
					fieldTypeDTO.setDescription(fdType.getDescription());
					fieldTypeDTO.setHidden(fdType.isHidden());
					fieldTypeDTOs.add(fieldTypeDTO);
				}
			}
		}

		return fieldTypeDTOs;
	}

	@Override
	public void saveBatch(BatchDTO batchDTO) {
		BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);
		batchSchemaService.updateBatch(batchDTO.getBatch());
	}

	@Override
	public List<String> getColumnRegexPattern(String documentName, String tableName) {

		List<String> columnPatternList = null;
		TableColumnsInfoService tableColumnsInfoService = this.getSingleBeanOfType(TableColumnsInfoService.class);
		final List<TableColumnsInfo> tableColumnsInfoList = tableColumnsInfoService.getTableColumnsInfo(documentName, tableName);

		if (null != tableColumnsInfoList && !tableColumnsInfoList.isEmpty()) {
			columnPatternList = new ArrayList<String>();
			for (TableColumnsInfo tableColumnsInfo : tableColumnsInfoList) {
				if (null == tableColumnsInfo) {
					columnPatternList.add("");
				} else {
					columnPatternList.add(tableColumnsInfo.getColumnPattern());
				}
			}
		}
		return columnPatternList;
	}

	private String getTableEndPattern(String documentTypeName, String tableName, String batchClassIdentifier) {
		String tableEndPattern = null;
		TableInfoService tableInfoService = this.getSingleBeanOfType(TableInfoService.class);
		final List<TableInfo> tableInfoList = tableInfoService.getTableInfoByDocTypeName(documentTypeName, batchClassIdentifier);

		if (null != tableInfoList && !tableInfoList.isEmpty()) {
			for (TableInfo tableInfo : tableInfoList) {
				if (null != tableInfo) {
					if (tableInfo.getName().equalsIgnoreCase(tableName)) {
						tableEndPattern = tableInfo.getEndPattern();
						break;
					}
				}
			}
		}
		return tableEndPattern;
	}

	@Override
	public String getDefaultDocTypeView() {
		String filePath = META_INF + File.separator + PROPERTY_FILE_NAME;
		String default_view = null;
		try {
			InputStream propertyInStream = new ClassPathResource(filePath).getInputStream();
			Properties properties = new Properties();
			properties.load(propertyInStream);
			String docView = properties.getProperty(DOCUMENT_DEFAULT_VIEW_PROPERTY_KEY);
			if (docView == null) {
				default_view = LIST_VIEW;
			} else {
				default_view = docView;
			}

		} catch (IOException e) {
			default_view = LIST_VIEW;
		}
		return default_view;
	}

	@Override
	public BatchDTO executeScript(Batch batch) throws GWTException {
		ScriptService scriptService = this.getSingleBeanOfType(ScriptService.class);
		BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);
		batchSchemaService.updateBatch(batch);
		BatchInstanceID batchInstanceID = new BatchInstanceID(batch.getBatchInstanceIdentifier());
		String nameOfPluginScript = batchSchemaService.getValidationScriptName();
		try {
			scriptService.executeScript(batchInstanceID, null, nameOfPluginScript, null, null);
		} catch (DCMAException e) {
			throw new GWTException(e.getMessage());
		}
		return getBatch(batch.getBatchInstanceIdentifier());
	}

	@Override
	public List<Span> getHOCRContent(PointCoordinate pointCoordinate1, PointCoordinate pointCoordinate2,
			String batchInstanceIdentifier, String pageID, boolean rectangularCoordinateSet) {
		List<Span> spanSelectedList = null;
		boolean valid = true;
		if (batchInstanceIdentifier == null) {
			valid = false;
		}
		if (pageID == null) {
			valid = false;
		}
		if (valid) {
			BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);
			HocrPages hocrPages = batchSchemaService.getHocrPages(batchInstanceIdentifier, pageID);
			if (hocrPages != null && hocrPages.getHocrPage() != null) {
				List<HocrPage> hocrPageList = hocrPages.getHocrPage();
				if (!hocrPageList.isEmpty()) {
					HocrPage hocrPage = hocrPageList.get(0);
					List<Span> spanList = new ArrayList<Span>();
					if (hocrPage.getSpans() != null) {
						spanList = hocrPage.getSpans().getSpan();
					}
					Integer firstSpanIndex = null;
					Integer lastSpanIndex = null;

					Integer x0Coordinate = pointCoordinate1.getxCoordinate();
					Integer y0Coordinate = pointCoordinate1.getyCoordinate();
					Integer x1Coordinate = pointCoordinate2.getxCoordinate();
					Integer y1Coordinate = pointCoordinate2.getyCoordinate();
					List<Span> spanSortedList = getSortedList(spanList);
					if (!rectangularCoordinateSet) {

						int counter = 0;

						for (Span span : spanSortedList) {
							long spanX0 = span.getCoordinates().getX0().longValue();
							long spanY0 = span.getCoordinates().getY0().longValue();
							long spanX1 = span.getCoordinates().getX1().longValue();
							long spanY1 = span.getCoordinates().getY1().longValue();
							if (spanX0 < x0Coordinate && spanX1 > x0Coordinate && spanY0 < y0Coordinate && spanY1 > y0Coordinate) {
								firstSpanIndex = counter;
							}
							if (spanX0 < x1Coordinate && spanX1 > x1Coordinate && spanY0 < y1Coordinate && spanY1 > y1Coordinate) {
								lastSpanIndex = counter;
							}
							if (firstSpanIndex != null && lastSpanIndex != null) {
								break;
							}
							counter++;
						}
						if (firstSpanIndex != null && lastSpanIndex != null) {
							counter = 0;
							for (Span span : spanSortedList) {
								if ((counter >= firstSpanIndex && counter <= lastSpanIndex)
										|| (counter <= firstSpanIndex && counter >= lastSpanIndex)) {
									if (spanSelectedList == null) {
										spanSelectedList = new ArrayList<Span>();
									}
									spanSelectedList.add(span);
								}
								counter++;
							}
						}
					} else {
						boolean isValidSpan = false;
						int defaultvalue = 20;
						int counter = 0;
						StringBuffer valueStringBuffer = new StringBuffer();
						long currentYCoor = spanSortedList.get(0).getCoordinates().getY1().longValue();
						for (Span span : spanSortedList) {
							isValidSpan = false;
							long spanX0 = span.getCoordinates().getX0().longValue();
							long spanY0 = span.getCoordinates().getY0().longValue();
							long spanX1 = span.getCoordinates().getX1().longValue();
							long spanY1 = span.getCoordinates().getY1().longValue();
							if ((spanY1 - currentYCoor) > defaultvalue) {
								currentYCoor = spanY1;
								if (spanSelectedList != null && spanSelectedList.size() > 0) {
									break;
								}
							}
							if (((spanX1 >= x0Coordinate && spanX1 <= x1Coordinate) || (spanX0 >= x0Coordinate && spanX0 <= x1Coordinate))
									&& ((spanY1 <= y1Coordinate && spanY1 >= y0Coordinate) || (spanY0 <= y1Coordinate && spanY0 >= y0Coordinate))) {
								isValidSpan = true;
							} else if (((x0Coordinate <= spanX0 && x1Coordinate >= spanX0) || (x0Coordinate >= spanX1 && x1Coordinate <= spanX1))
									&& ((y0Coordinate >= spanY0 && y0Coordinate <= spanY1) || (y1Coordinate >= spanY0 && y1Coordinate <= spanY1))
									|| ((y0Coordinate <= spanY0 && y1Coordinate >= spanY0) || (y0Coordinate >= spanY1 && y1Coordinate <= spanY1))
									&& ((x0Coordinate >= spanX0 && x0Coordinate <= spanX1) || (x1Coordinate >= spanX0 && x1Coordinate <= spanX1))) {
								isValidSpan = true;
							} else {
								if (((x0Coordinate > spanX0 && x0Coordinate < spanX1) || (x1Coordinate > spanX0 && x1Coordinate < spanX1))
										&& ((y0Coordinate > spanY0 && y0Coordinate < spanY1) || (y1Coordinate > spanY0 && y1Coordinate < spanY1))) {
									isValidSpan = true;
								}
							}
							if (isValidSpan) {
								if (counter != 0) {
									valueStringBuffer.append(' ');
								}
								valueStringBuffer.append(span.getValue());
								counter++;
							}

						}
						if (spanSelectedList == null) {
							spanSelectedList = new ArrayList<Span>();
						}
						Span span = new Span();
						Coordinates coordinates = new Coordinates();
						coordinates.setX0(BigInteger.valueOf(x0Coordinate));
						coordinates.setX1(BigInteger.valueOf(x1Coordinate));
						coordinates.setY0(BigInteger.valueOf(y0Coordinate));
						coordinates.setY1(BigInteger.valueOf(y1Coordinate));
						span.setCoordinates(coordinates);
						span.setValue(valueStringBuffer.toString());
						spanSelectedList.add(span);

					}
				}
			}
		}

		return spanSelectedList;
	}

	private List<Span> getSortedList(List<Span> spanList) {
		TableFinderService tableFinderService = this.getSingleBeanOfType(TableFinderService.class);
		int defaultvalue = 20;
		if (tableFinderService != null) {
			try {
				defaultvalue = Integer.parseInt(tableFinderService.getWidthOfLine());
			} catch (NumberFormatException nfe) {
				defaultvalue = 20;
			}
		}

		final int deafultGap = defaultvalue;
		// TODO optimize the set creation for document level fields.
		final Set<Span> set = new TreeSet<Span>(new Comparator<Span>() {

			public int compare(final Span firstSpan, final Span secSpan) {
				BigInteger s1Y1 = firstSpan.getCoordinates().getY1();
				BigInteger s2Y1 = secSpan.getCoordinates().getY1();
				int returnValue = 0;
				int compare = s1Y1.intValue() - s2Y1.intValue();

				if (compare >= -deafultGap && compare <= deafultGap) {
					BigInteger s1X1 = firstSpan.getCoordinates().getX1();
					BigInteger s2X1 = secSpan.getCoordinates().getX1();
					returnValue = s1X1.compareTo(s2X1);
				} else {
					returnValue = s1Y1.compareTo(s2Y1);
				}
				return returnValue;
			}
		});

		set.addAll(spanList);
		final List<Span> spanSortedList = new LinkedList<Span>();
		spanSortedList.addAll(set);

		// TODO add the clear method to remove all elements of set since it not
		// required after adding it to linked list.
		// set.clear();

		return spanSortedList;

	}

	@Override
	public List<Span> getHOCRContent(List<PointCoordinate> pointCoordinates, String batchInstanceIdentifier, String pageID) {

		List<Span> selectedSpanList = null;
		for (PointCoordinate pointCoordinate : pointCoordinates) {
			Integer xCoordinate = pointCoordinate.getxCoordinate();
			Integer yCoordinate = pointCoordinate.getyCoordinate();
			boolean valid = true;
			if (batchInstanceIdentifier == null) {
				valid = false;
			}
			if (pageID == null) {
				valid = false;
			}
			if (valid) {
				BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);
				HocrPages hocrPages = batchSchemaService.getHocrPages(batchInstanceIdentifier, pageID);
				if (hocrPages != null && hocrPages.getHocrPage() != null) {
					List<HocrPage> hocrPageList = hocrPages.getHocrPage();
					if (!hocrPageList.isEmpty()) {
						HocrPage hocrPage = hocrPageList.get(0);
						List<Span> spanList = new ArrayList<Span>();
						if (hocrPage.getSpans() != null) {
							spanList = hocrPage.getSpans().getSpan();
						}
						for (Span span1 : spanList) {
							long spanX0 = span1.getCoordinates().getX0().longValue();
							long spanY0 = span1.getCoordinates().getY0().longValue();
							long spanX1 = span1.getCoordinates().getX1().longValue();
							long spanY1 = span1.getCoordinates().getY1().longValue();
							if (spanX0 < xCoordinate && spanX1 > xCoordinate && spanY0 < yCoordinate && spanY1 > yCoordinate) {
								if (selectedSpanList == null) {
									selectedSpanList = new ArrayList<Span>();
								}
								selectedSpanList.add(span1);
								break;
							}
						}
					}
				}
			}
		}
		return selectedSpanList;
	}

	@Override
	public BatchDTO executeAddNewTable(Batch batch, String documentIdentifier) throws GWTException {
		ScriptService scriptService = this.getSingleBeanOfType(ScriptService.class);
		BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);
		batchSchemaService.updateBatch(batch);
		BatchInstanceID batchInstanceID = new BatchInstanceID(batch.getBatchInstanceIdentifier());
		String nameOfTableScript = batchSchemaService.getAddNewTableScriptName();
		try {
			scriptService.executeScript(batchInstanceID, null, nameOfTableScript, documentIdentifier, null);
		} catch (DCMAException e) {
			throw new GWTException(e.getMessage());
		}
		return getBatch(batch.getBatchInstanceIdentifier());
	}

	@Override
	public BatchDTO executeScript(String shortcutKeyName, Batch batch, Document document) throws GWTException {
		BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);
		batchSchemaService.updateBatch(batch);
		BatchInstanceID batchInstanceID = new BatchInstanceID(batch.getBatchInstanceIdentifier());
		String nameOfPluginScript = getDynamicFunctionKeyScriptName();
		BatchClassService batchClassService = this.getSingleBeanOfType(BatchClassService.class);
		BatchClass batchClass = batchClassService.getBatchClassByIdentifier(batch.getBatchClassIdentifier());
		if (batchClass.getDocumentTypes() != null && !batchClass.getDocumentTypes().isEmpty()) {
			for (DocumentType docType : batchClass.getDocumentTypes()) {
				if (docType.getName().equals(document.getType())) {
					if (docType.getFunctionKeys() != null && !docType.getFunctionKeys().isEmpty()) {
						for (FunctionKey functionKey : docType.getFunctionKeys()) {
							if (functionKey.getShortcutKeyname().equals(shortcutKeyName)) {
								executeScript(batchInstanceID, nameOfPluginScript, functionKey.getMethodName(), document
										.getIdentifier());
								break;
							}
						}
					}
				}
			}
		}
		return getBatch(batch.getBatchInstanceIdentifier());
	}

	private String getDynamicFunctionKeyScriptName() {
		String filePath = META_INF + File.separator + PROPERTY_FILE_NAME;
		String scriptName = null;
		try {
			InputStream propertyInStream = new ClassPathResource(filePath).getInputStream();
			Properties properties = new Properties();
			properties.load(propertyInStream);
			scriptName = properties.getProperty(DYNAMIC_FUNCTION_KEY_SCRIPT_NAME);
			if (scriptName == null) {
				scriptName = SCRIPT_TEST;
			}
		} catch (IOException e) {
			scriptName = SCRIPT_TEST;
		}
		return scriptName;
	}

	private void executeScript(BatchInstanceID batchInstanceID, String nameOfPluginScript, String methodName, String docIdentifier)
			throws GWTException {
		ScriptService scriptService = this.getSingleBeanOfType(ScriptService.class);
		try {
			scriptService.executeScript(batchInstanceID, null, nameOfPluginScript, docIdentifier, methodName);
		} catch (DCMAException e) {
			throw new GWTException(e.getMessage());
		}
	}

	@Override
	public List<FunctionKeyDTO> getFunctionKeyDTOs(String documentTypeName, String batchInstanceIdentifier) {
		List<FunctionKeyDTO> functionKeyDTOs = new ArrayList<FunctionKeyDTO>();
		PluginPropertiesService pluginPropertiesService = this.getBeanByName("batchInstancePluginPropertiesService",
				BatchInstancePluginPropertiesService.class);
		List<FunctionKey> allFunctionkeys = pluginPropertiesService.getFunctionKeys(batchInstanceIdentifier, documentTypeName);

		if (allFunctionkeys != null && !allFunctionkeys.isEmpty()) {
			for (FunctionKey functionKey : allFunctionkeys) {
				FunctionKeyDTO functionKeyDTO = new FunctionKeyDTO();
				functionKeyDTO.setMethodName(functionKey.getMethodName());
				functionKeyDTO.setMethodDescription(functionKey.getUiLabel());
				functionKeyDTO.setShortcutKeyName(functionKey.getShortcutKeyname());
				functionKeyDTOs.add(functionKeyDTO);
			}
		}

		return getFunctionKeyDTOSortedList(functionKeyDTOs);
	}

	private List<FunctionKeyDTO> getFunctionKeyDTOSortedList(final List<FunctionKeyDTO> functionKeyDTOs) {
		final Set<FunctionKeyDTO> set = new TreeSet<FunctionKeyDTO>(new Comparator<FunctionKeyDTO>() {

			public int compare(final FunctionKeyDTO firstFunctionKeyDTO, final FunctionKeyDTO secFunctionKeyDTO) {
				if (firstFunctionKeyDTO.getShortcutKeyName().length() < secFunctionKeyDTO.getShortcutKeyName().length()) {
					return -1;
				} else if (firstFunctionKeyDTO.getShortcutKeyName().length() > secFunctionKeyDTO.getShortcutKeyName().length()) {
					return 1;
				} else {
					return firstFunctionKeyDTO.getShortcutKeyName().compareTo(secFunctionKeyDTO.getShortcutKeyName());
				}
			}
		});
		List<FunctionKeyDTO> fKDtos = null;
		if (functionKeyDTOs != null) {
			set.addAll(functionKeyDTOs);
			fKDtos = new LinkedList<FunctionKeyDTO>();
			fKDtos.addAll(set);
		}
		return fKDtos;
	}

	@Override
	public List<Span> getTableHOCRContent(final PointCoordinate initialCoordinate, final PointCoordinate finalCoordinate,
			final String batchInstanceIdentifier, final String pageID) {
		List<Span> spanSelectedList = null;
		boolean valid = true;
		if (batchInstanceIdentifier == null) {
			valid = false;
		}
		if (pageID == null) {
			valid = false;
		}
		if (valid) {
			BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);
			TableFinderService tableFinderService = this.getSingleBeanOfType(TableFinderService.class);
			HocrPages hocrPages = batchSchemaService.getHocrPages(batchInstanceIdentifier, pageID);
			if (hocrPages != null && hocrPages.getHocrPage() != null) {
				List<HocrPage> hocrPageList = hocrPages.getHocrPage();
				if (!hocrPageList.isEmpty()) {
					HocrPage hocrPage = hocrPageList.get(0);
					List<Span> spanList = new ArrayList<Span>();
					if (hocrPage.getSpans() != null) {
						spanList = hocrPage.getSpans().getSpan();
					}
					List<Span> spanSortedList = getSortedList(spanList);
					Integer x0Coordinate = initialCoordinate.getxCoordinate();
					Integer y0Coordinate = initialCoordinate.getyCoordinate();
					Integer x1Coordinate = finalCoordinate.getxCoordinate();
					Integer y1Coordinate = finalCoordinate.getyCoordinate();
					boolean isValidSpan = false;
					int defaultvalue = 20;
					if (tableFinderService != null) {
						try {
							defaultvalue = Integer.parseInt(tableFinderService.getWidthOfLine());
						} catch (NumberFormatException nfe) {
							defaultvalue = 20;
						}
					}
					long currentYCoor = spanSortedList.get(0).getCoordinates().getY1().longValue();
					for (Span span : spanSortedList) {
						isValidSpan = false;
						long spanX0 = span.getCoordinates().getX0().longValue();
						long spanY0 = span.getCoordinates().getY0().longValue();
						long spanX1 = span.getCoordinates().getX1().longValue();
						long spanY1 = span.getCoordinates().getY1().longValue();
						if ((spanY1 - currentYCoor) > defaultvalue) {
							currentYCoor = spanY1;
							if (spanSelectedList != null && spanSelectedList.size() > 0) {
								break;
							}
						}
						if (((spanX1 >= x0Coordinate && spanX1 <= x1Coordinate) || (spanX0 >= x0Coordinate && spanX0 <= x1Coordinate))
								&& ((spanY1 <= y1Coordinate && spanY1 >= y0Coordinate) || (spanY0 <= y1Coordinate && spanY0 >= y0Coordinate))) {
							isValidSpan = true;
						} else if (((x0Coordinate <= spanX0 && x1Coordinate >= spanX0) || (x0Coordinate >= spanX1 && x1Coordinate <= spanX1))
								&& ((y0Coordinate >= spanY0 && y0Coordinate <= spanY1) || (y1Coordinate >= spanY0 && y1Coordinate <= spanY1))
								|| ((y0Coordinate <= spanY0 && y1Coordinate >= spanY0) || (y0Coordinate >= spanY1 && y1Coordinate <= spanY1))
								&& ((x0Coordinate >= spanX0 && x0Coordinate <= spanX1) || (x1Coordinate >= spanX0 && x1Coordinate <= spanX1))) {
							isValidSpan = true;
						} else {
							if (((x0Coordinate > spanX0 && x0Coordinate < spanX1) || (x1Coordinate > spanX0 && x1Coordinate < spanX1))
									&& ((y0Coordinate > spanY0 && y0Coordinate < spanY1) || (y1Coordinate > spanY0 && y1Coordinate < spanY1))) {
								isValidSpan = true;
							}
						}
						if (isValidSpan) {
							if (spanSelectedList == null) {
								spanSelectedList = new ArrayList<Span>();
							}
							spanSelectedList.add(span);
						}
					}
				}
			}
		}
		return spanSelectedList;
	}

	@Override
	public List<Row> getTableData(final Map<Integer, Coordinates> columnVsCoordinates, final String documentTypeName,
			final DataTable selectedDataTable, final String batchClassIdentifier, final String batchInstanceIdentifier,
			final String pageID) {
		List<Row> rowList = null;
		List<Span> spanSortedList = null;
		List<Column> columnList = selectedDataTable.getHeaderRow().getColumns().getColumn();
		String tableName = selectedDataTable.getName();
		boolean valid = true;
		if (batchInstanceIdentifier == null) {
			valid = false;
		}
		if (pageID == null) {
			valid = false;
		}
		String tableEndPattern = getTableEndPattern(documentTypeName, tableName, batchClassIdentifier);
		if (valid) {
			BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);
			HocrPages hocrPages = batchSchemaService.getHocrPages(batchInstanceIdentifier, pageID);
			if (hocrPages != null && hocrPages.getHocrPage() != null) {
				List<HocrPage> hocrPageList = hocrPages.getHocrPage();
				if (!hocrPageList.isEmpty()) {
					HocrPage hocrPage = hocrPageList.get(0);
					List<Span> spanList = new ArrayList<Span>();
					if (hocrPage.getSpans() != null) {
						spanList = hocrPage.getSpans().getSpan();
					}
					spanSortedList = getSortedList(spanList);
					rowList = addColumnData(spanSortedList, columnList, columnVsCoordinates, tableEndPattern);
				}
			}
		}
		return rowList;
	}

	private List<Row> addColumnData(final List<Span> spanSortedList, final List<Column> columnList,
			final Map<Integer, Coordinates> columnVsCoordinates, final String tableEndPattern) {
		TableFinderService tableFinderService = this.getSingleBeanOfType(TableFinderService.class);
		Integer minX = 0;
		Integer minY = 0;
		Integer maxX = 0;
		Integer maxY = 0;
		List<Row> rowList = new ArrayList<Row>();
		int counter = 1;
		for (Integer colNo : columnVsCoordinates.keySet()) {
			Coordinates coor = columnVsCoordinates.get(colNo);
			if (counter++ == 1) {
				minX = coor.getX0().intValue();
				minY = coor.getY0().intValue();
				maxX = coor.getX1().intValue();
				maxY = coor.getY1().intValue();
			} else {
				if (minX > coor.getX0().intValue()) {
					minX = coor.getX0().intValue();
				}
				if (minY > coor.getY0().intValue()) {
					minY = coor.getY0().intValue();
				}
				if (maxX < coor.getX1().intValue()) {
					maxX = coor.getX1().intValue();
				}
				if (maxY < coor.getY1().intValue()) {
					maxY = coor.getY1().intValue();
				}
			}
		}
		int defaultvalue = 20;
		if (tableFinderService != null) {
			try {
				defaultvalue = Integer.parseInt(tableFinderService.getWidthOfLine());
			} catch (NumberFormatException nfe) {
				defaultvalue = 20;
			}
		}
		counter = 1;
		long currentYCoor = 0;
		StringBuffer[] colValue = new StringBuffer[columnList.size()];
		for (int i = 0; i < colValue.length; i++) {
			colValue[i] = new StringBuffer("");
		}
		Coordinates rowCoordinates = new Coordinates();
		Row row = createNewRow(columnList);
		boolean isValidSpan = false;
		Pattern pattern = null;
		Matcher matcher = null;
		if (tableEndPattern != null) {
			pattern = Pattern.compile(tableEndPattern);
		}
		for (Span span : spanSortedList) {
			long spanX0Coor = span.getCoordinates().getX0().longValue();
			long spanY0Coor = span.getCoordinates().getY0().longValue();
			long spanX1Coor = span.getCoordinates().getX1().longValue();
			long spanY1Coor = span.getCoordinates().getY1().longValue();
			if (pattern != null) {
				matcher = pattern.matcher(span.getValue());
			}
			if (spanY0Coor < minY && spanY1Coor < minY) {
				if (matcher != null && matcher.find()) {
					break;
				}
				continue;
			}
			if (counter == 1) {
				currentYCoor = spanY1Coor;
				rowCoordinates.setX0(span.getCoordinates().getX0());
				rowCoordinates.setY0(span.getCoordinates().getY0());
				counter++;
			}
			if ((spanY1Coor - currentYCoor) > defaultvalue) {
				isValidSpan = false;
				for (int i = 0; i < colValue.length; i++) {
					if (colValue[i] != null && !colValue[i].toString().trim().isEmpty()) {
						isValidSpan = true;
					}
				}
				if (isValidSpan) {
					List<Column> rowColumnList = row.getColumns().getColumn();
					for (Integer colNo : columnVsCoordinates.keySet()) {
						rowColumnList.get(colNo).setValue(colValue[colNo].toString().trim());
					}
					row.setRowCoordinates(rowCoordinates);
					rowList.add(row);
					row = createNewRow(columnList);
				}
				for (int i = 0; i < colValue.length; i++) {
					colValue[i] = new StringBuffer("");
				}
				currentYCoor = spanY1Coor;
				rowCoordinates = new Coordinates();
				rowCoordinates.setX0(span.getCoordinates().getX0());
				rowCoordinates.setY0(span.getCoordinates().getY0());
			} else {
				rowCoordinates.setX1(span.getCoordinates().getX1());
				rowCoordinates.setY1(span.getCoordinates().getY1());
			}

			for (Integer colNo : columnVsCoordinates.keySet()) {
				Coordinates coor = columnVsCoordinates.get(colNo);
				long coorX0 = coor.getX0().longValue();
				long coorX1 = coor.getX1().longValue();
				if ((spanX0Coor > coorX0 && spanX0Coor < coorX1) || (spanX1Coor > coorX0 && spanX1Coor < coorX1)
						|| (spanX0Coor < coorX0 && spanX1Coor > coorX1)) {
					row.getColumns().getColumn().get(colNo).getCoordinatesList().getCoordinates().add(span.getCoordinates());
					colValue[colNo].append(span.getValue().toString() + " ");
					break;
				}
			}
			if ((tableEndPattern != null && !tableEndPattern.isEmpty()) && matcher.find()) {
				break;
			}
		}

		// setAlternateValues(rowList, columnList.size());
		return rowList;
	}

	private Row createNewRow(final List<Column> columnList) {
		Row row = new Row();

		Row.Columns columnsRow = row.getColumns();
		if (null == columnsRow) {
			columnsRow = new Row.Columns();
			row.setColumns(columnsRow);
		}
		List<Column> columnRowList = columnsRow.getColumn();
		for (int count = 0; count < columnList.size(); count++) {
			Column column = new Column();
			column.setName(null);
			column.setValue(null);
			column.setConfidence(0);
			column.setCoordinatesList(new CoordinatesList());
			column.setPage(null);
			column.setValid(true);
			column.setAlternateValues(new Column.AlternateValues());
			columnRowList.add(column);
		}
		return row;
	}

	@Override
	public BatchDTO executeScriptOnFieldChange(Batch batch, Document document, String fieldName) throws GWTException {
		ScriptService scriptService = this.getSingleBeanOfType(ScriptService.class);

		BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);
		batchSchemaService.updateBatch(batch);
		BatchInstanceID batchInstanceID = new BatchInstanceID(batch.getBatchInstanceIdentifier());
		String nameOfPluginScript = getFieldValueChangeScriptName();
		try {
			scriptService.executeScript(batchInstanceID, null, nameOfPluginScript, document.getIdentifier(), fieldName);
		} catch (DCMAException e) {
			throw new GWTException(e.getMessage());
		}
		return getBatch(batch.getBatchInstanceIdentifier());
	}

	private String getFieldValueChangeScriptName() {

		String filePath = META_INF + File.separator + PROPERTY_FILE_NAME;
		String scriptName = null;
		try {
			InputStream propertyInStream = new ClassPathResource(filePath).getInputStream();
			Properties properties = new Properties();
			properties.load(propertyInStream);
			scriptName = properties.getProperty(FIELD_VALUE_CHANGE_SCRIPT_NAME);
			if (scriptName == null) {
				scriptName = DEFAULT_SCRIPT_FOR_FIELD_VALUE_CHANGE;
			}
		} catch (IOException e) {
			scriptName = DEFAULT_SCRIPT_FOR_FIELD_VALUE_CHANGE;
		}
		return scriptName;
	}

	@Override
	public String getGeneratedSecurityTokenForExternalApp() {
		return SecurityTokenHandler.generateToken();
	}

	@Override
	public String getEncodedString(String toEncodeString) {
		return SecurityTokenHandler.getEncodedString(toEncodeString);
	}
}
