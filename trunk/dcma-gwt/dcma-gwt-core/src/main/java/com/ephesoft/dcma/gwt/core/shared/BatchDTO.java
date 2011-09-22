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

package com.ephesoft.dcma.gwt.core.shared;

import java.util.List;

import com.ephesoft.dcma.batch.schema.Batch;
import com.ephesoft.dcma.batch.schema.BatchStatus;
import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.batch.schema.Page;
import com.ephesoft.dcma.gwt.core.client.util.ReverseIterable;
import com.google.gwt.user.client.rpc.IsSerializable;

public class BatchDTO implements IsSerializable {
	
	private Batch batch;
	
	private String baseHTTPUrl;
	
	private String isScriptEnabled = "OFF";
	
	public BatchDTO() {
	}
	
	public BatchDTO(Batch batch, String baseHTTPUrl) {
		this.batch = batch;
		this.baseHTTPUrl = baseHTTPUrl;
	}
	
	public BatchDTO(Batch batch, String baseHTTPUrl, String isScriptEnabled) {
		this.batch = batch;
		this.baseHTTPUrl = baseHTTPUrl;
		this.isScriptEnabled = isScriptEnabled;
	}
	
	public Batch getBatch() {
		return batch;
	}
	
	public String getAbsoluteURLFor(String fileName) {
		return this.baseHTTPUrl + "/" + fileName;
	}
	public String getAbsoluteURLForRotatedImage(String fileName,String direction) {
		return this.baseHTTPUrl +"/"+ direction+ "/" + fileName;
	}
	public Document getDocumentById(String id) {
		List<Document> docs = batch.getDocuments().getDocument();
		for (Document document : docs) {
			if(document.getIdentifier().equals(id)) {
				return document;
			}
		}
		return null;
	}
	
	public Page getUpdatedPageInDocument(Document document, Page page) {
		List<Page> pagesInDoc = document.getPages().getPage();
		for (Page pageType : pagesInDoc) {
			if(pageType.getIdentifier().equals(page.getIdentifier())) {
				return pageType;
			}
		}
		return null;
	}
	
	public Document getDocumentForPage(Page page) {
		Batch batch = this.getBatch();
		List<Document> documents = batch.getDocuments().getDocument();
		List<Page> pageTypes = null;
		for (Document document : documents) {
			pageTypes = document.getPages().getPage();
			for (Page pageType : pageTypes) {
				if (pageType.getIdentifier().equals(page.getIdentifier())) {
					return document;
				}
			}
		}
		return null;
	}
	
	public Document getNextDocumentTo(Document document, boolean isError) {
		List<Document> documents = batch.getDocuments().getDocument();
		if(documents != null && !documents.isEmpty()){
			Boolean reachedToInput = Boolean.FALSE;
			for (Document documentType : documents) {
				if(documentType.getIdentifier().equals(document.getIdentifier())) {
					reachedToInput = Boolean.TRUE;
					continue;
				}
				if(reachedToInput && (!isError || isErrorContained(documentType))) {
					return documentType;
				}
			}
			for (Document documentType : documents) {
				if(isErrorContained(documentType)) {
					return documentType;
				}
			}
			
			return document;
		}
		return null;
	}
	
	public Document getPreviousDocumentTo(Document document, boolean isError) {
		List<Document> documents = batch.getDocuments().getDocument();
		
		Boolean reachedToInput = Boolean.FALSE;
		ReverseIterable<Document> iter = new ReverseIterable<Document>(documents);
		int count = 0;
		for (Document documentType : iter) {
			count++;
			if(documentType.getIdentifier().equals(document.getIdentifier())) {
				reachedToInput = Boolean.TRUE;
				if(iter.iterator().hasNext()){
					continue;
				}
			}
			if(reachedToInput && (!isError || isErrorContained(documentType)) && !documentType.getIdentifier().equalsIgnoreCase(document.getIdentifier())) {
				return documentType;
			}
		}
		iter = new ReverseIterable<Document>(documents);
		for (Document documentType : iter) {
			if(reachedToInput && (!isError || isErrorContained(documentType))) {
				return documentType;
			}
		}
		return null;
	}
	
	public boolean isErrorContained(Document document) {
		switch (batch.getBatchStatus()) {
			case READY_FOR_REVIEW:
				return !document.isReviewed();
			case READY_FOR_VALIDATION:
				return !document.isReviewed() || !document.isValid();
		}
		return true;
	}
	
	public String getIsScriptEnabled() {
		return isScriptEnabled;
	}

	public void setIsScriptEnabled(String isScriptEnabled) {
		this.isScriptEnabled = isScriptEnabled;
	}
	
	public boolean isBatchValidated() {
		boolean valid = true;
		List<Document> documents = batch.getDocuments().getDocument();
		if(batch.getBatchStatus().equals(BatchStatus.READY_FOR_REVIEW)) {
			for(Document document : documents) {
				if(!document.isReviewed()) {
					valid = false;
					break;
				}
			}
		} else {
			for(Document document : documents) {
				if(!document.isReviewed() || !document.isValid()) {
					valid = false;
					break;
				}
			}
		}
		return valid;
	}
	
}
