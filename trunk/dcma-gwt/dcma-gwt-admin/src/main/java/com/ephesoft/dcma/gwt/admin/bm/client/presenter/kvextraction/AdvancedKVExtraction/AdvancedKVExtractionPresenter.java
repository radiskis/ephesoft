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

import java.util.List;

import com.ephesoft.dcma.core.common.KVFetchValue;
import com.ephesoft.dcma.core.common.KVPageValue;
import com.ephesoft.dcma.gwt.admin.bm.client.AdminConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.BatchClassManagementController;
import com.ephesoft.dcma.gwt.admin.bm.client.MessageConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementMessages;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.AbstractBatchClassPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.view.kvextraction.AdvancedKVExtraction.AdvancedKVExtractionView;
import com.ephesoft.dcma.gwt.admin.bm.client.view.kvextraction.AdvancedKVExtraction.Coordinates;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.client.ui.ScreenMaskUtility;
import com.ephesoft.dcma.gwt.core.client.validator.EmptyStringValidator;
import com.ephesoft.dcma.gwt.core.client.validator.NumberValidator;
import com.ephesoft.dcma.gwt.core.shared.AdvancedKVExtractionDTO;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialogUtil;
import com.ephesoft.dcma.gwt.core.shared.KVExtractionDTO;
import com.ephesoft.dcma.gwt.core.shared.OutputDataCarrierDTO;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DialogBox;

public class AdvancedKVExtractionPresenter extends AbstractBatchClassPresenter<AdvancedKVExtractionView> {

	private static final String STYLE_WIDTH500PX = "width500px";
	private static final String EXTENSION_PNG = ".png";
	private static final char EXT_CHAR = '.';
	private static final String KV_EXTRACTION_RESULT = "KV Extraction Result";
	protected static final String EXTENSION_TIF = ".tif";

	boolean isEditAdvancedKV;

	public AdvancedKVExtractionPresenter(BatchClassManagementController controller, AdvancedKVExtractionView view) {
		super(controller, view);
	}

	@Override
	public void injectEvents(HandlerManager eventBus) {
		// TODO Auto-generated method stub

	}

	@Override
	public void bind() {
		KVExtractionDTO selectedKVExtraction = controller.getSelectedKVExtraction();
		if (selectedKVExtraction != null) {
			view.setLocation(selectedKVExtraction.getLocationType());
			view.setKeyPattern(selectedKVExtraction.getKeyPattern());
			view.setValuePattern(selectedKVExtraction.getValuePattern());
			if (selectedKVExtraction.getFetchValue() != null) {
				view.setFetchValue(selectedKVExtraction.getFetchValue());
			} else {
				view.setFetchValue(KVFetchValue.ALL);
			}
			if (selectedKVExtraction.getKvPageValue() != null) {
				view.setKVPageValue(selectedKVExtraction.getKvPageValue());
			} else {
				view.setKVPageValue(KVPageValue.ALL);
			}
			if (selectedKVExtraction.getXoffset() != null) {
				view.setxOffset(String.valueOf(selectedKVExtraction.getXoffset()));
			} else {
				view.setxOffset(AdminConstants.EMPTY_STRING);
			}
			if (selectedKVExtraction.getYoffset() != null) {
				view.setyOffset(String.valueOf(selectedKVExtraction.getYoffset()));
			} else {
				view.setyOffset(AdminConstants.EMPTY_STRING);
			}
			if (selectedKVExtraction.getWidth() != null) {
				view.setWidthOfRect(String.valueOf(selectedKVExtraction.getWidth()));
			} else {
				view.setWidthOfRect(AdminConstants.EMPTY_STRING);
			}
			if (selectedKVExtraction.getLength() != null) {
				view.setLengthOfRect(String.valueOf(selectedKVExtraction.getLength()));
			} else {
				view.setLengthOfRect(AdminConstants.EMPTY_STRING);
			}
			if (selectedKVExtraction.getMultiplier() != null) {
				float Rval = selectedKVExtraction.getMultiplier();
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

	public boolean isEditAdvancedKV() {
		return this.isEditAdvancedKV;
	}

	public void setEditAdvancedKV(boolean isEditAdvancedKV) {
		this.isEditAdvancedKV = isEditAdvancedKV;
	}

	public void createKeyValueOverlay() {
		AdvancedKVExtractionDTO advancedKVExtraction = controller.getSelectedKVExtraction().getAdvancedKVExtractionDTO();
		Coordinates keyCoordinates = new Coordinates();
		Coordinates valueCoordinates = new Coordinates();
		keyCoordinates.setX0(advancedKVExtraction.getKeyX0Coord());
		keyCoordinates.setY0(advancedKVExtraction.getKeyY0Coord());
		keyCoordinates.setX1(advancedKVExtraction.getKeyX1Coord());
		keyCoordinates.setY1(advancedKVExtraction.getKeyY1Coord());
		valueCoordinates.setX0(advancedKVExtraction.getValueX0Coord());
		valueCoordinates.setY0(advancedKVExtraction.getValueY0Coord());
		valueCoordinates.setX1(advancedKVExtraction.getValueX1Coord());
		valueCoordinates.setY1(advancedKVExtraction.getValueY1Coord());
		view.createKeyValueOverlay(keyCoordinates, valueCoordinates);
	}

	public void getPageImageUrl(final String batchClassId, final String documentName, final String imageName) {
		controller.getRpcService().getAdvancedKVImageUploadPath(batchClassId, documentName, imageName, new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable paramThrowable) {
				setEditAdvancedKV(false);
				ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
						BatchClassManagementMessages.ERROR_RETRIEVING_PATH));
			}

			@Override
			public void onSuccess(String pathUrl) {
				if (null == pathUrl || pathUrl.isEmpty()) {
					setEditAdvancedKV(false);
					ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
							BatchClassManagementMessages.ERROR_RETRIEVING_PATH));
				} else {
					if (view.getFileName() != null && !view.getFileName().isEmpty()) {
						setImageNameInDTO(view.getFileName());
					}
					view.setPageImageUrl(pathUrl);
				}
			}
		});
	}

	public void onSaveButtonClicked() {
		boolean validFlag = validateFields();
		KVExtractionDTO selectedKVExtraction = controller.getSelectedKVExtraction();

		if (validFlag) {
			int length = 0, width = 0;
			if (view.getLength() != null && !view.getLength().isEmpty()) {
				length = Integer.parseInt(view.getLength());
			}
			if (view.getWidth() != null && !view.getWidth().isEmpty()) {
				width = Integer.parseInt(view.getWidth());
			}
			if (length == 0 || width == 0) {
				ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
						BatchClassManagementMessages.ADVANCED_KV_ERROR));
				validFlag = false;
			}
		}

		if (validFlag) {
			if (controller.isAdd()) {
				controller.getSelectedDocumentLevelField().addKvExtraction(selectedKVExtraction);
				controller.setAdd(false);
			}
			saveDataInDTO(selectedKVExtraction);
			view.removeAllOverlays();
			controller.getView().toggleBottomPanelShowHide(true);
			controller.getMainPresenter().showFieldTypeView(false);
		}
	}

	private void saveDataInDTO(final KVExtractionDTO selectedKVExtraction) {
		selectedKVExtraction.setKeyPattern(view.getKeyPattern());
		selectedKVExtraction.setValuePattern(view.getValuePattern());
		selectedKVExtraction.setLocationType(view.getLocation());
		selectedKVExtraction.setNoOfWords(null);
		if (view.getLength() != null && !view.getLength().isEmpty()) {
			Integer length = Integer.parseInt(view.getLength());
			selectedKVExtraction.setLength(length);
		}
		if (view.getWidth() != null && !view.getWidth().isEmpty()) {
			Integer width = Integer.parseInt(view.getWidth());
			selectedKVExtraction.setWidth(width);
		}
		if (view.getxOffset() != null && !view.getxOffset().isEmpty()) {
			Integer xoffset = Integer.parseInt(view.getxOffset());
			selectedKVExtraction.setXoffset(xoffset);
		}
		if (view.getyOffset() != null && !view.getyOffset().isEmpty()) {
			Integer yoffset = Integer.parseInt(view.getyOffset());
			selectedKVExtraction.setYoffset(yoffset);
		}
		if (view.getFetchValue() != null) {
			selectedKVExtraction.setFetchValue(view.getFetchValue());
		}
		if (view.getKVPageValue() != null) {
			selectedKVExtraction.setKvPageValue(view.getKVPageValue());
		}
		if (view.getMultiplier() != null) {
			if (!view.getMultiplier().isEmpty()) {
				Float multiplier = Float.parseFloat(view.getMultiplier());
				selectedKVExtraction.setMultiplier(multiplier);
			} else {
				selectedKVExtraction.setMultiplier(null);
			}
		}
		setAdvKVExtractionDTO(view.getKeyCoordinates(), view.getValueCoordinates(), selectedKVExtraction.getAdvancedKVExtractionDTO());
	}

	private boolean validateFields() {
		boolean validFlag = true;
		if (validFlag && (!view.getValidateKeyPatternTextBox().validate())) {
			ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
					BatchClassManagementMessages.BLANK_ERROR));
			validFlag = false;
		}

		if (validFlag && (!view.getValidateValuePatternTextBox().validate())) {
			ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
					BatchClassManagementMessages.BLANK_ERROR));
			validFlag = false;
		}

		if (validFlag && !view.getValidateMultiplierTextBox().validate()) {
			if (view.getMultiplier() != null && !view.getMultiplier().isEmpty()) {
				ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
						BatchClassManagementMessages.MULTIPLIER_ERROR));
			}
			validFlag = false;
		}
		if (validFlag) {
			if (view.getMultiplier() != null && !view.getMultiplier().isEmpty()) {
				Integer mult = (int) (Float.parseFloat(view.getMultiplier()) * 100);
				if (mult > 100 || mult < 0) {
					ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
							BatchClassManagementMessages.MULTIPLIER_ERROR));
					validFlag = false;
				}
			}
		}
		return validFlag;
	}

	private AdvancedKVExtractionDTO setAdvKVExtractionDTO(final Coordinates keyCoord, final Coordinates valueCoord,
			AdvancedKVExtractionDTO advancedKVExtractionDTO) {
		String fileName = view.getFileName();
		if (null != fileName && !fileName.isEmpty()) {
			String pngFileName = fileName.substring(0, fileName.lastIndexOf(EXT_CHAR)) + EXTENSION_PNG;
			advancedKVExtractionDTO.setDisplayImageName(pngFileName);
			setKeyValueCoordinates(keyCoord, valueCoord, advancedKVExtractionDTO);
		} else if (!keyCoord.isEmpty() && !valueCoord.isEmpty()) {
			setKeyValueCoordinates(keyCoord, valueCoord, advancedKVExtractionDTO);
		}
		return advancedKVExtractionDTO;
	}

	/**
	 * This method sets the Key and Value coordinates in the advanced KV DTO.
	 * 
	 * @param keyCoord
	 * @param valueCoord
	 * @param advKVExtractionDTO
	 */
	private void setKeyValueCoordinates(final Coordinates keyCoord, final Coordinates valueCoord,
			AdvancedKVExtractionDTO advKVExtractionDTO) {
		advKVExtractionDTO.setKeyX0Coord(keyCoord.getX0());
		advKVExtractionDTO.setKeyX1Coord(keyCoord.getX1());
		advKVExtractionDTO.setKeyY0Coord(keyCoord.getY0());
		advKVExtractionDTO.setKeyY1Coord(keyCoord.getY1());
		advKVExtractionDTO.setValueX0Coord(valueCoord.getX0());
		advKVExtractionDTO.setValueX1Coord(valueCoord.getX1());
		advKVExtractionDTO.setValueY0Coord(valueCoord.getY0());
		advKVExtractionDTO.setValueY1Coord(valueCoord.getY1());
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

	public void setImageUrlAndCoordinates() {
		KVExtractionDTO selectedKVExtraction = controller.getSelectedKVExtraction();
		if (selectedKVExtraction != null) {
			AdvancedKVExtractionDTO advancedKVExtractionDTO = selectedKVExtraction.getAdvancedKVExtractionDTO();
			if (null != advancedKVExtractionDTO && null != advancedKVExtractionDTO.getDisplayImageName()) {
				// advancedKVExtractionDTO.setImageName(view.getFileName());
				view.setBatchClassID(controller.getBatchClass().getIdentifier());
				view.setDocName(controller.getSelectedDocument().getName());
				getPageImageUrl(view.getBatchClassID(), view.getDocName(), advancedKVExtractionDTO.getDisplayImageName());
			}
		}
	}

	public void onTestAdvancedKvButtonClicked() {
		final AdvancedKVExtractionDTO advancedKVExtractionDTO = controller.getSelectedKVExtraction().getAdvancedKVExtractionDTO();
		final String fileName = advancedKVExtractionDTO.getImageName();
		if (validateFields()) {
			ScreenMaskUtility.maskScreen();
			saveDataInDTO(controller.getSelectedKVExtraction());
			controller.getRpcService().testAdvancedKVExtraction(controller.getBatchClass(), controller.getSelectedKVExtraction(),
					view.getDocName(), fileName, new AsyncCallback<List<OutputDataCarrierDTO>>() {

						@Override
						public void onFailure(Throwable throwable) {
							ScreenMaskUtility.unmaskScreen();
							ConfirmationDialogUtil.showConfirmationDialog(throwable.getMessage(), MessageConstants.TITLE_TEST_FAILURE,
									Boolean.TRUE);
						}

						@Override
						public void onSuccess(List<OutputDataCarrierDTO> outputDtos) {
							ScreenMaskUtility.unmaskScreen();
							getUpdatedFileName(advancedKVExtractionDTO, fileName);
							DialogBox dialogBox = new DialogBox();
							dialogBox.addStyleName(STYLE_WIDTH500PX);
							dialogBox.setHeight("200px");
							view.getKvExtractionTestResultView().createKVFieldList(outputDtos);
							view.getKvExtractionTestResultView().setDialogBox(dialogBox);
							dialogBox.setText(KV_EXTRACTION_RESULT);
							dialogBox.setWidget(view.getKvExtractionTestResultView());
							dialogBox.center();
							view.getKvExtractionTestResultView().getBackButton().setFocus(true);
							dialogBox.show();
						}

						private void getUpdatedFileName(final AdvancedKVExtractionDTO advancedKVExtractionDTO, final String fileName) {
							controller.getRpcService().getUpdatedTestFileName(view.getBatchClassID(), view.getDocName(), fileName,
									new AsyncCallback<String>() {

										@Override
										public void onFailure(Throwable arg0) {
											// TODO
										}

										@Override
										public void onSuccess(String updatedFileName) {
											setImageNameInDTO(updatedFileName);
										}
									});
						}
					});
		}
	}

	public void setImageNameInDTO(final String imageName) {
		AdvancedKVExtractionDTO advancedKVExtractionDTO = controller.getSelectedKVExtraction().getAdvancedKVExtractionDTO();
		if (advancedKVExtractionDTO != null) {
			advancedKVExtractionDTO.setImageName(imageName);
		}
	}
}
