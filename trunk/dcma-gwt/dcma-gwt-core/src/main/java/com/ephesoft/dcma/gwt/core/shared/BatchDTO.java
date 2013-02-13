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

package com.ephesoft.dcma.gwt.core.shared;

import java.util.List;
import java.util.Map;

import com.ephesoft.dcma.batch.schema.Batch;
import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.batch.schema.Page;
import com.ephesoft.dcma.core.common.BatchInstanceStatus;
import com.ephesoft.dcma.gwt.core.client.util.ReverseIterable;
import com.google.gwt.user.client.rpc.IsSerializable;

public class BatchDTO implements IsSerializable {

	private Batch batch;

	private BatchInstanceStatus batchInstanceStatus;

	private String baseHTTPUrl;

	private String isValidationScriptEnabled = BatchConstants.OFF;
	private String fieldValueChangeScriptSwitchState = BatchConstants.OFF;
	private String fuzzySearchSwitchState = "ON";
	private String suggestionBoxSwitchState = BatchConstants.OFF;
	private String externalApplicationSwitchState = BatchConstants.OFF;
	private Map<String, String> urlAndShortcutMap = null;
	private Map<String, String> dimensionsForPopUp = null;
	private Map<String, String> urlAndTitleMap = null;
	private String fuzzySearchPopUpXDimension = "500";
	private String fuzzySearchPopUpYDimension = "350";
	private Integer realUpdateInterval = 5;
	private Integer preloadedImageCount = 3;
	// value '1' signifies document to be displayed in tree view
	private int docDisplayName = 1;
	private boolean defaultReviewPanelStateOpen = true;

	private static final String PIXELS = "px";

	public BatchDTO() {
		//Empty constructor
	}

	public BatchDTO(Batch batch, String baseHTTPUrl) {
		this.batch = batch;
		this.baseHTTPUrl = baseHTTPUrl;
	}

	public BatchDTO(Batch batch, String baseHTTPUrl, String isValidationScriptEnabled, String isFieldValueChangeScriptEnabled,
			String isFuzzySearchEnabled, String suggestionBoxSwitchState, String externalApplicationSwitchState,
			Map<String, String> urlAndShortcutMap, Map<String, String> dimensionsForPopUp, Map<String, String> urlAndTitleMap,
			String fuzzySearchPopUpXDimension, String fuzzySearchPopUpYDimension, String updateIntervalInStringForm,
			String preloadedImageCountString, BatchInstanceStatus batchInstanceStatus, int docDisplayName,
			String defaultReviewPanelState) {
		this.batch = batch;
		this.baseHTTPUrl = baseHTTPUrl;
		this.setDocDisplayName(docDisplayName);
		this.batchInstanceStatus = batchInstanceStatus;
		if (null != isValidationScriptEnabled) {
			setIsValidationScriptEnabled(isValidationScriptEnabled);
		}
		if (null != isFuzzySearchEnabled) {
			setFuzzySearchSwitchState(isFuzzySearchEnabled);
		}
		if (null != isFieldValueChangeScriptEnabled) {
			setFieldValueChangeScriptSwitchState(isFieldValueChangeScriptEnabled);
		}
		if (null != suggestionBoxSwitchState) {
			setSuggestionBoxSwitchState(suggestionBoxSwitchState);
		}

		if (null != externalApplicationSwitchState) {
			setExternalApplicationSwitchState(externalApplicationSwitchState);
		}
		if (null != fuzzySearchPopUpXDimension && Integer.parseInt(fuzzySearchPopUpXDimension) > 0) {
			setFuzzySearchPopUpXDimension(fuzzySearchPopUpXDimension + PIXELS);
		}
		if (null != fuzzySearchPopUpYDimension && Integer.parseInt(fuzzySearchPopUpYDimension) > 0) {
			setFuzzySearchPopUpYDimension(fuzzySearchPopUpYDimension + PIXELS);
		}
		if (null != updateIntervalInStringForm) {
			int updateInterval = Integer.parseInt(updateIntervalInStringForm);
			if (updateInterval > 0) {
				setRealUpdateInterval(updateInterval);
			}
		}
		if (null != preloadedImageCountString) {
			int preloadedImageCount = Integer.parseInt(preloadedImageCountString);
			if (preloadedImageCount > 0) {
				setPreloadedImageCount(preloadedImageCount);
			}
		}
		if (null != defaultReviewPanelState && defaultReviewPanelState.equalsIgnoreCase(BatchConstants.FALSE)) {
			defaultReviewPanelStateOpen = false;
		}
		setUrlAndShortcutMap(urlAndShortcutMap);
		setDimensionsForPopUp(dimensionsForPopUp);
		setUrlAndTitleMap(urlAndTitleMap);
	}

	public Map<String, String> getUrlAndTitleMap() {
		return urlAndTitleMap;
	}

	public final void setUrlAndTitleMap(Map<String, String> urlAndTitleMap) {
		this.urlAndTitleMap = urlAndTitleMap;
	}

	public Batch getBatch() {
		return batch;
	}

	public String getAbsoluteURLFor(String fileName) {
		return this.baseHTTPUrl + "/" + fileName;
	}

	public String getAbsoluteURLForRotatedImage(String fileName, String direction) {
		return this.baseHTTPUrl + "/" + direction + "/" + fileName;
	}

	public String getDocDisplayNameByDocId(String docId) {
		Document document = getDocumentById(docId);
		return document.getType();
	}

	public Document getDocumentById(String documentId) {
		List<Document> docs = batch.getDocuments().getDocument();
		Document document = null;
		for (Document doc : docs) {
			if (doc.getIdentifier().equals(documentId)) {
				document = doc;
				break;
			}
		}
		return document;
	}

	public Page getUpdatedPageInDocument(Document document, Page page) {
		Page newPage = null;
		List<Page> pagesInDoc = document.getPages().getPage();
		for (Page pageType : pagesInDoc) {
			if (pageType.getIdentifier().equals(page.getIdentifier())) {
				newPage = pageType;
				break;
			}
		}
		return newPage;
	}

	public Document getDocumentForPage(Page page) {
		Document document = null;
		Batch batch = this.getBatch();
		List<Document> documents = batch.getDocuments().getDocument();
		List<Page> pageTypes = null;
		for (Document doc : documents) {
			pageTypes = doc.getPages().getPage();
			for (Page pageType : pageTypes) {
				if (pageType.getIdentifier().equals(page.getIdentifier())) {
					document = doc;
					break;
				}
			}
		}
		return document;
	}

	public Document getNextDocumentTo(Document document, boolean isError) {
		Document doc = null;
		List<Document> documents = batch.getDocuments().getDocument();
		if (documents != null && !documents.isEmpty()) {
			Boolean reachedToInput = Boolean.FALSE;
			for (Document documentType : documents) {
				if (documentType.getIdentifier().equals(document.getIdentifier())) {
					reachedToInput = Boolean.TRUE;
					continue;
				}
				if (reachedToInput && (!isError || isErrorContained(documentType))) {
					doc = documentType;
					break;
				}
			}
			if (doc == null) {
				doc = getDocumentType(doc, documents);
			}
			if (doc == null) {
				doc = document;
			}
		}
		return doc;
	}

	private Document getDocumentType(Document doc, List<Document> documents) {
		Document docTemp = doc;
		for (Document documentType : documents) {
			if (isErrorContained(documentType)) {
				docTemp = documentType;
				break;
			}
		}
		return docTemp;
	}

	public Document getPreviousDocumentTo(Document document, boolean isError) {
		List<Document> documents = batch.getDocuments().getDocument();
		Document doc = null;
		Boolean reachedToInput = Boolean.FALSE;
		ReverseIterable<Document> iter = new ReverseIterable<Document>(documents);
		for (Document documentType : iter) {
			if (documentType.getIdentifier().equals(document.getIdentifier())) {
				reachedToInput = Boolean.TRUE;
				if (iter.iterator().hasNext()) {
					continue;
				}
			}
			if (reachedToInput && (!isError || isErrorContained(documentType))
					&& !documentType.getIdentifier().equalsIgnoreCase(document.getIdentifier())) {
				doc = documentType;
				break;
			}
		}
		if (doc == null) {
			iter = new ReverseIterable<Document>(documents);
			for (Document documentType : iter) {
				if (reachedToInput && (!isError || isErrorContained(documentType))) {
					doc = documentType;
					break;
				}
			}
		}
		return doc;
	}

	public boolean isErrorContained(Document document) {
		boolean error = true;
		switch (batchInstanceStatus) {
			case READY_FOR_REVIEW:
				error = !document.isReviewed();
				break;
			case READY_FOR_VALIDATION:
				error = !document.isReviewed() || !document.isValid();
				break;
			default:
				error = true;
		}
		return error;
	}

	public String getIsValidationScriptEnabled() {
		return isValidationScriptEnabled;
	}

	public final void setIsValidationScriptEnabled(String isValidationScriptEnabled) {
		this.isValidationScriptEnabled = isValidationScriptEnabled;
	}

	public boolean isBatchValidated() {
		boolean valid = true;
		List<Document> documents = batch.getDocuments().getDocument();
		if (batchInstanceStatus.equals(BatchInstanceStatus.READY_FOR_REVIEW)) {
			for (Document document : documents) {
				if (!document.isReviewed()) {
					valid = false;
					break;
				}
			}
		} else {
			for (Document document : documents) {
				if (!document.isReviewed() || !document.isValid()) {
					valid = false;
					break;
				}
			}
		}
		return valid;
	}

	public BatchInstanceStatus getBatchInstanceStatus() {
		return batchInstanceStatus;
	}

	public void setBatchInstanceStatus(BatchInstanceStatus batchInstanceStatus) {
		this.batchInstanceStatus = batchInstanceStatus;
	}

	public final void setFuzzySearchSwitchState(String fuzzySearchSwitchState) {
		this.fuzzySearchSwitchState = fuzzySearchSwitchState;
	}

	public String getFuzzySearchSwitchState() {
		return fuzzySearchSwitchState;
	}

	public final void setFieldValueChangeScriptSwitchState(String fieldValueChangeScriptSwitchState) {
		this.fieldValueChangeScriptSwitchState = fieldValueChangeScriptSwitchState;
	}

	public String getFieldValueChangeScriptSwitchState() {
		return fieldValueChangeScriptSwitchState;
	}

	public String getSuggestionBoxSwitchState() {
		return this.suggestionBoxSwitchState;
	}

	public final void setSuggestionBoxSwitchState(String suggestionBoxSwitchState) {
		this.suggestionBoxSwitchState = suggestionBoxSwitchState;
	}

	public final void setExternalApplicationSwitchState(String externalApplicationSwitchState) {
		this.externalApplicationSwitchState = externalApplicationSwitchState;
	}

	public String getExternalApplicationSwitchState() {
		return externalApplicationSwitchState;
	}

	public final void setUrlAndShortcutMap(Map<String, String> urlAndShortcutMap) {
		this.urlAndShortcutMap = urlAndShortcutMap;
	}

	public Map<String, String> getUrlAndShortcutMap() {
		return urlAndShortcutMap;
	}

	public final void setDimensionsForPopUp(Map<String, String> dimensionsForPopUp) {
		this.dimensionsForPopUp = dimensionsForPopUp;
	}

	public Map<String, String> getDimensionsForPopUp() {
		return dimensionsForPopUp;
	}

	public String getFuzzySearchPopUpXDimension() {
		return fuzzySearchPopUpXDimension;
	}

	public final void setFuzzySearchPopUpXDimension(String fuzzySearchPopUpXDimension) {
		this.fuzzySearchPopUpXDimension = fuzzySearchPopUpXDimension;
	}

	public String getFuzzySearchPopUpYDimension() {
		return fuzzySearchPopUpYDimension;
	}

	public final void setFuzzySearchPopUpYDimension(String fuzzySearchPopUpYDimension) {
		this.fuzzySearchPopUpYDimension = fuzzySearchPopUpYDimension;
	}

	public final void setRealUpdateInterval(Integer realUpdateInterval) {
		this.realUpdateInterval = realUpdateInterval;
	}

	public Integer getRealUpdateInterval() {
		return realUpdateInterval;
	}

	public Integer getPreloadedImageCount() {
		return preloadedImageCount;
	}

	public final void setPreloadedImageCount(Integer preloadedImageCount) {
		this.preloadedImageCount = preloadedImageCount;
	}

	public final void setDocDisplayName(int docDisplayName) {
		this.docDisplayName = docDisplayName;
	}

	public int getDocDisplayName() {
		return docDisplayName;
	}

	public boolean isDefaultReviewPanelStateOpen() {
		return defaultReviewPanelStateOpen;
	}

	public void setDefaultReviewPanelStateOpen(boolean defaultReviewPanelStateOpen) {
		this.defaultReviewPanelStateOpen = defaultReviewPanelStateOpen;
	}

}
