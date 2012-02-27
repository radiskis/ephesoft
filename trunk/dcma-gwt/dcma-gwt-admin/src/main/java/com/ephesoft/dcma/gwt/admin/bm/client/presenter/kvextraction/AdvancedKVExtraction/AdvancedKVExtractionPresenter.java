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

package com.ephesoft.dcma.gwt.admin.bm.client.presenter.kvextraction.AdvancedKVExtraction;

import com.ephesoft.dcma.core.common.KVFetchValue;
import com.ephesoft.dcma.gwt.admin.bm.client.AdminConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.BatchClassManagementController;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementMessages;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.AbstractBatchClassPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.view.kvextraction.AdvancedKVExtraction.AdvancedKVExtractionView;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.client.validator.EmptyStringValidator;
import com.ephesoft.dcma.gwt.core.client.validator.NumberValidator;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialogUtil;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class AdvancedKVExtractionPresenter extends AbstractBatchClassPresenter<AdvancedKVExtractionView> {

	public AdvancedKVExtractionPresenter(BatchClassManagementController controller, AdvancedKVExtractionView view) {
		super(controller, view);
	}

	@Override
	public void injectEvents(HandlerManager eventBus) {
		// TODO Auto-generated method stub

	}

	@Override
	public void bind() {
		if (controller.getSelectedKVExtraction() != null) {
			view.setLocation(controller.getSelectedKVExtraction().getLocationType());
			view.setKeyPattern(controller.getSelectedKVExtraction().getKeyPattern());
			view.setValuePattern(controller.getSelectedKVExtraction().getValuePattern());
			if (controller.getSelectedKVExtraction().getFetchValue() != null) {
				view.setFetchValue(controller.getSelectedKVExtraction().getFetchValue());
			} else {
				view.setFetchValue(KVFetchValue.ALL);
			}
			if (controller.getSelectedKVExtraction().getXoffset() != null) {
				view.setxOffset(String.valueOf(controller.getSelectedKVExtraction().getXoffset()));
			} else {
				view.setxOffset(AdminConstants.EMPTY_STRING);
			}
			if (controller.getSelectedKVExtraction().getYoffset() != null) {
				view.setyOffset(String.valueOf(controller.getSelectedKVExtraction().getYoffset()));
			} else {
				view.setyOffset(AdminConstants.EMPTY_STRING);
			}
			if (controller.getSelectedKVExtraction().getWidth() != null) {
				view.setWidthOfRect(String.valueOf(controller.getSelectedKVExtraction().getWidth()));
			} else {
				view.setWidthOfRect(AdminConstants.EMPTY_STRING);
			}
			if (controller.getSelectedKVExtraction().getLength() != null) {
				view.setLengthOfRect(String.valueOf(controller.getSelectedKVExtraction().getLength()));
			} else {
				view.setLengthOfRect(AdminConstants.EMPTY_STRING);
			}
			if (controller.getSelectedKVExtraction().getMultiplier() != null) {
				float Rval = controller.getSelectedKVExtraction().getMultiplier();
				int Rpl = 2;
				float pVarLocal = (float) Math.pow(10, Rpl);
				Rval = Rval * pVarLocal;
				float tmp = Math.round(Rval);
				Float fVarLocal = (float) tmp / pVarLocal;
				view.setMultiplier(String.valueOf(fVarLocal.toString()));
			} else {
				view.setMultiplier(AdminConstants.EMPTY_STRING);
			}
			view.getValidateKeyPatternTextBox().addValidator(new EmptyStringValidator(view.getKeyPatternTextBox()));
			view.getValidateKeyPatternTextBox().toggleValidDateBox();
			view.getValidateValuePatternTextBox().addValidator(new EmptyStringValidator(view.getValuePatternTextBox()));
			view.getValidateValuePatternTextBox().toggleValidDateBox();
			view.getValidateMultiplierTextBox().addValidator(new NumberValidator(view.getMultiplierTextBox(), true, true));
			view.getValidateMultiplierTextBox().toggleValidDateBox();
			view.clearImageUpload();
		}
	}

	public void getPageImageUrl(String batchClassId, String imageName) {
		controller.getRpcService().getAdvancedKVImageUploadPath(batchClassId, imageName, new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable paramThrowable) {
				ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(BatchClassManagementMessages.ERROR_RETRIEVING_PATH));
			}

			@Override
			public void onSuccess(String pathUrl) {
				view.setPageImageUrl(pathUrl);
			}
		});
	}

	public void onSaveButtonClicked() {
		boolean validFlag = true;
		if (validFlag && (!view.getValidateKeyPatternTextBox().validate())) {
			ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(BatchClassManagementMessages.BLANK_ERROR));
			validFlag = false;
		}

		if (validFlag && (!view.getValidateValuePatternTextBox().validate())) {
			ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(BatchClassManagementMessages.BLANK_ERROR));
			validFlag = false;
		}

		if (validFlag && !view.getValidateMultiplierTextBox().validate()) {
			if (view.getMultiplier() != null && !view.getMultiplier().isEmpty()) {
				ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(BatchClassManagementMessages.MULTIPLIER_ERROR));
			}
			validFlag = false;
		}
		if (validFlag) {
			if (view.getMultiplier() != null && !view.getMultiplier().isEmpty()) {
				Integer mult = (int)(Float.parseFloat(view.getMultiplier()) * 100);
				if (mult > 100 || mult < 0) {
					ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(BatchClassManagementMessages.MULTIPLIER_ERROR));
					validFlag = false;
				}
			}
		}

//		if (validFlag
//				&& (controller.isAdd() && controller.getSelectedDocumentLevelField().checkKVExtractionDetails(view.getKeyPattern(),
//						view.getValuePattern(), view.getLocation()))) {
//			ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue((BatchClassManagementMessages.NAME_COMMON_ERROR)));
//			validFlag = false;
//		}
		
		if (validFlag) {
			int length = 0, width=0;
			if (view.getLength() != null && !view.getLength().isEmpty()) {
				length = Integer.parseInt(view.getLength());
			}
			if (view.getWidth() != null && !view.getWidth().isEmpty()) {
				width = Integer.parseInt(view.getWidth());
			}
			if (length==0 || width==0){
				ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(BatchClassManagementMessages.ADVANCED_KV_ERROR));
				validFlag=false;
			}
		}

		if (validFlag) {
			if (controller.isAdd()) {
				controller.getSelectedDocumentLevelField().addKvExtraction(controller.getSelectedKVExtraction());
				controller.setAdd(false);
			}

			controller.getSelectedKVExtraction().setKeyPattern(view.getKeyPattern());
			controller.getSelectedKVExtraction().setValuePattern(view.getValuePattern());
			controller.getSelectedKVExtraction().setLocationType(view.getLocation());
			controller.getSelectedKVExtraction().setNoOfWords(null);
			if (view.getLength() != null && !view.getLength().isEmpty()) {
				Integer length = Integer.parseInt(view.getLength());
				controller.getSelectedKVExtraction().setLength(length);
			}
			if (view.getWidth() != null && !view.getWidth().isEmpty()) {
				Integer width = Integer.parseInt(view.getWidth());
				controller.getSelectedKVExtraction().setWidth(width);
			}
			if (view.getxOffset() != null && !view.getxOffset().isEmpty()) {
				Integer xoffset = Integer.parseInt(view.getxOffset());
				controller.getSelectedKVExtraction().setXoffset(xoffset);
			}
			if (view.getyOffset() != null && !view.getyOffset().isEmpty()) {
				Integer yoffset = Integer.parseInt(view.getyOffset());
				controller.getSelectedKVExtraction().setYoffset(yoffset);
			}
			if (view.getFetchValue() != null) {
				controller.getSelectedKVExtraction().setFetchValue(view.getFetchValue());
			}
			if (view.getMultiplier() != null) {
				if(!view.getMultiplier().isEmpty()) {
					Float multiplier = Float.parseFloat(view.getMultiplier());
					controller.getSelectedKVExtraction().setMultiplier(multiplier);
				} else {
					controller.getSelectedKVExtraction().setMultiplier(null);
				}
			}

			view.removeAllOverlays();
			controller.getView().toggleBottomPanelShowHide(true);
			controller.getMainPresenter().showFieldTypeView(false);

		}
	}

	public void onCancelButtonClicked() {
		if (controller.isAdd()) {
			controller.setAdd(false);
		}
		view.removeAllOverlays();
		controller.getView().toggleBottomPanelShowHide(true);
		controller.getMainPresenter().showFieldTypeView(false);

	}

	public void clearValues() {
		if (controller.getSelectedKVExtraction() != null) {
			view.setLengthOfRect(String.valueOf(0));
			view.setWidthOfRect(String.valueOf(0));
			view.setxOffset(String.valueOf(0));
			view.setyOffset(String.valueOf(0));
			view.setMultiplier(AdminConstants.EMPTY_STRING);
			view.setKeyPattern(AdminConstants.EMPTY_STRING);
			view.setValuePattern(AdminConstants.EMPTY_STRING);
			view.setLocation(null);
			view.setFetchValue(KVFetchValue.ALL);
		}
	}
}
