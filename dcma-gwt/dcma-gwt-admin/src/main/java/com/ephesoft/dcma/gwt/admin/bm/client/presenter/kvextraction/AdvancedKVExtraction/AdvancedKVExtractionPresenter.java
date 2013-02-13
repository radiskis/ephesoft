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

package com.ephesoft.dcma.gwt.admin.bm.client.presenter.kvextraction.advancedkvextraction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.ephesoft.dcma.batch.schema.HocrPages.HocrPage.Spans.Span;
import com.ephesoft.dcma.core.common.FileType;
import com.ephesoft.dcma.core.common.KVFetchValue;
import com.ephesoft.dcma.core.common.KVPageValue;
import com.ephesoft.dcma.gwt.admin.bm.client.AdminConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.BatchClassManagementController;
import com.ephesoft.dcma.gwt.admin.bm.client.MessageConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementMessages;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.AbstractBatchClassPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.view.kvextraction.advancedkvextraction.AdvancedKVExtractionView;
import com.ephesoft.dcma.gwt.admin.bm.client.view.kvextraction.advancedkvextraction.Coordinates;
import com.ephesoft.dcma.gwt.core.client.EphesoftAsyncCallback;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.client.ui.ScreenMaskUtility;
import com.ephesoft.dcma.gwt.core.client.validator.EmptyStringValidator;
import com.ephesoft.dcma.gwt.core.client.validator.NumberValidator;
import com.ephesoft.dcma.gwt.core.client.validator.RegExValidator;
import com.ephesoft.dcma.gwt.core.shared.AdvancedKVExtractionDTO;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialogUtil;
import com.ephesoft.dcma.gwt.core.shared.DocumentTypeDTO;
import com.ephesoft.dcma.gwt.core.shared.FieldTypeDTO;
import com.ephesoft.dcma.gwt.core.shared.KVExtractionDTO;
import com.ephesoft.dcma.gwt.core.shared.OutputDataCarrierDTO;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.DialogBox;

/**
 * The presenter for view that shows the advanced KV extraction view details.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.gwt.admin.bm.client.presenter.AbstractBatchClassPresenter
 */
public class AdvancedKVExtractionPresenter extends AbstractBatchClassPresenter<AdvancedKVExtractionView> {

	/**
	 * STYLE_WIDTH500PX String.
	 */
	private static final String STYLE_WIDTH500PX = "width500px";

	/**
	 * KV_EXTRACTION_RESULT String.
	 */
	private static final String KV_EXTRACTION_RESULT = "KV Extraction Result";

	/**
	 * EXTENSION_TIF String.
	 */
	protected static final String EXTENSION_TIF = ".tif";

	/**
	 * editAdvancedKV boolean.
	 */
	private boolean editAdvancedKV;

	/**
	 * advancedKVExtractionDTO AdvancedKVExtractionDTO.
	 */
	private AdvancedKVExtractionDTO advancedKVExtractionDTO;

	/**
	 * Constructor.
	 * 
	 * @param controller BatchClassManagementController
	 * @param view AdvancedKVExtractionView
	 */
	public AdvancedKVExtractionPresenter(BatchClassManagementController controller, AdvancedKVExtractionView view) {
		super(controller, view);
	}

	/**
	 * To handle events.
	 * 
	 * @param eventBus HandlerManager
	 */
	@Override
	public void injectEvents(HandlerManager eventBus) {

	}

	/**
	 * Processing to be done on load of this presenter.
	 */
	@Override
	public void bind() {
		KVExtractionDTO selectedKVExtraction = controller.getSelectedKVExtraction();
		this.advancedKVExtractionDTO = new AdvancedKVExtractionDTO();
		if (selectedKVExtraction != null) {
			AdvancedKVExtractionDTO advancedKVExtractionDTO = selectedKVExtraction.getAdvancedKVExtractionDTO();
			if (advancedKVExtractionDTO != null) {
				mergeAdvancedKVExtractionDTO(this.advancedKVExtractionDTO, advancedKVExtractionDTO);
			}
			
			view.setUseExistingKey(selectedKVExtraction.isUseExistingKey());

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
				float pVarLocal = (float) Math.pow(BatchClassManagementConstants.TEN, Rpl);
				Rval = Rval * pVarLocal;
				float tmp = Math.round(Rval);
				Float fVarLocal = (float) tmp / pVarLocal;
				view.setMultiplier(String.valueOf(fVarLocal.toString()));
			} else {
				view.setMultiplier(AdminConstants.EMPTY_STRING);
			}
			view.getValidateKeyPatternTextBox().addValidator(new EmptyStringValidator(view.getKeyPatternTextBox()));
			view.getValidateKeyPatternTextBox().addValidator(
					new RegExValidator(view.getValidateKeyPatternTextBox(), view.getKeyPatternTextBox(), true, false, true, null,
							controller.getRpcService()));
			view.getValidateKeyPatternTextBox().toggleValidDateBox();
			view.getValidateValuePatternTextBox().addValidator(new EmptyStringValidator(view.getValuePatternTextBox()));
			view.getValidateValuePatternTextBox().addValidator(
					new RegExValidator(view.getValidateValuePatternTextBox(), view.getValuePatternTextBox(), true, false, true, null,
							controller.getRpcService()));
			view.getValidateValuePatternTextBox().toggleValidDateBox();
			view.getValidateMultiplierTextBox().addValidator(new NumberValidator(view.getMultiplierTextBox(), true, true));
			view.getValidateMultiplierTextBox().toggleValidDateBox();
			view.setSpanList(null);
			view.clearImageUpload();
		}
	}

	private void mergeAdvancedKVExtractionDTO(AdvancedKVExtractionDTO selAdvKVExtractionDTO, AdvancedKVExtractionDTO advKVExtractionDTO) {
		if (selAdvKVExtractionDTO != null && advKVExtractionDTO != null) {
			selAdvKVExtractionDTO.setDisplayImageName(advKVExtractionDTO.getDisplayImageName());
			selAdvKVExtractionDTO.setImageName(advKVExtractionDTO.getImageName());
			selAdvKVExtractionDTO.setKeyX0Coord(advKVExtractionDTO.getKeyX0Coord());
			selAdvKVExtractionDTO.setKeyX1Coord(advKVExtractionDTO.getKeyX1Coord());
			selAdvKVExtractionDTO.setKeyY0Coord(advKVExtractionDTO.getKeyY0Coord());
			selAdvKVExtractionDTO.setKeyY1Coord(advKVExtractionDTO.getKeyY1Coord());
			selAdvKVExtractionDTO.setValueX0Coord(advKVExtractionDTO.getValueX0Coord());
			selAdvKVExtractionDTO.setValueX1Coord(advKVExtractionDTO.getValueX1Coord());
			selAdvKVExtractionDTO.setValueY0Coord(advKVExtractionDTO.getValueY0Coord());
			selAdvKVExtractionDTO.setValueY1Coord(advKVExtractionDTO.getValueY1Coord());
		}
	}

	/**
	 * To check whether advanced KV is edited or not.
	 * 
	 * @return boolean
	 */
	public boolean isEditAdvancedKV() {
		return this.editAdvancedKV;
	}

	/**
	 * To set Edit Advanced KV.
	 * 
	 * @param isEditAdvancedKV boolean
	 */
	public void setEditAdvancedKV(boolean isEditAdvancedKV) {
		this.editAdvancedKV = isEditAdvancedKV;
	}

	/**
	 * To create Key Value Overlay.
	 */
	public void createKeyValueOverlay() {
		AdvancedKVExtractionDTO advancedKVExtraction = controller.getSelectedKVExtraction().getAdvancedKVExtractionDTO();
		Coordinates keyCoordinates = new Coordinates();
		Coordinates valueCoordinates = new Coordinates();
		double aspectWidthRatio = view.getAspectRatioWidth();
		double aspectHeightRatio = view.getAspectRatioHeight();
		keyCoordinates.setX0((int) Math.round(advancedKVExtraction.getKeyX0Coord() * aspectWidthRatio));
		keyCoordinates.setY0((int) Math.round(advancedKVExtraction.getKeyY0Coord() * aspectHeightRatio));
		keyCoordinates.setX1((int) Math.round(advancedKVExtraction.getKeyX1Coord() * aspectWidthRatio));
		keyCoordinates.setY1((int) Math.round(advancedKVExtraction.getKeyY1Coord() * aspectHeightRatio));
		valueCoordinates.setX0((int) Math.round(advancedKVExtraction.getValueX0Coord() * aspectWidthRatio));
		valueCoordinates.setY0((int) Math.round(advancedKVExtraction.getValueY0Coord() * aspectHeightRatio));
		valueCoordinates.setX1((int) Math.round(advancedKVExtraction.getValueX1Coord() * aspectWidthRatio));
		valueCoordinates.setY1((int) Math.round(advancedKVExtraction.getValueY1Coord() * aspectHeightRatio));
		view.createKeyValueOverlay(keyCoordinates, valueCoordinates);
	}

	/**
	 * To get Page Image Url.
	 * 
	 * @param batchClassId String
	 * @param documentName String
	 * @param imageName String
	 */
	public void getPageImageUrl(final String batchClassId, final String documentName, final String imageName) {
		controller.getRpcService().getAdvancedKVImageUploadPath(batchClassId, documentName, imageName,
				new EphesoftAsyncCallback<String>() {

					@Override
					public void customFailure(Throwable paramThrowable) {
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
							view.setPageImageUrl(pathUrl);
						}
					}
				});
	}

	/**
	 * To perform operations in case of save button clicked.
	 */
	public void onSaveButtonClicked() {
		boolean validFlag = validateFields();
		KVExtractionDTO selectedKVExtraction = controller.getSelectedKVExtraction();

		if (validFlag) {
			int length = BatchClassManagementConstants.ZERO_CONST, width = BatchClassManagementConstants.ZERO_CONST;
			if (view.getLength() != null && !view.getLength().isEmpty()) {
				length = Integer.parseInt(view.getLength());
			}
			if (view.getWidth() != null && !view.getWidth().isEmpty()) {
				width = Integer.parseInt(view.getWidth());
			}
			if (length == BatchClassManagementConstants.ZERO_CONST || width == BatchClassManagementConstants.ZERO_CONST) {
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
		selectedKVExtraction.setUseExistingKey(view.isUseExistingKey());
		if (selectedKVExtraction.isUseExistingKey()) {
			selectedKVExtraction.setKeyPattern(view.getKeyPatternField());
		} else {
			selectedKVExtraction.setKeyPattern(view.getKeyPattern());
		}
		selectedKVExtraction.setValuePattern(view.getValuePattern());
		// selectedKVExtraction.setLocationType(view.getLocation());
		selectedKVExtraction.setNoOfWords(null);
		selectedKVExtraction.setUseExistingKey(view.isUseExistingKey());
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
		if (!view.getKeyCoordinates().isEmpty() && !view.getValueCoordinates().isEmpty()) {
			setKeyValueCoordinates(view.getKeyCoordinates(), view.getValueCoordinates());
		}
		AdvancedKVExtractionDTO selectedAdvKvExtractionDTO = selectedKVExtraction.getAdvancedKVExtractionDTO();
		if (selectedAdvKvExtractionDTO == null) {
			selectedAdvKvExtractionDTO = new AdvancedKVExtractionDTO();
			selectedKVExtraction.setAdvancedKVExtractionDTO(selectedAdvKvExtractionDTO);
		}
		mergeAdvancedKVExtractionDTO(selectedAdvKvExtractionDTO, this.advancedKVExtractionDTO);
	}

	private boolean validateFields() {
		boolean validFlag = true;
		if (validFlag && view.isUseExistingKey()) {
			if (!view.isKeyFieldSelected()) {
				ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
						BatchClassManagementMessages.NO_KEY_FIELD_SELECTED));
				validFlag = false;
			}
		} else if (validFlag
				&& (view.getValidateKeyPatternTextBox().getWidget().getText().isEmpty() || view.getValidateValuePatternTextBox()
						.getWidget().getText().isEmpty())) {
			ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
					BatchClassManagementMessages.MANDATORY_FIELDS_BLANK));
			validFlag = false;
		}

		else if (validFlag && !view.getValidateKeyPatternTextBox().isValid()) {
			String label = view.getKeyPatternLabel().getText();
			ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
					BatchClassManagementMessages.VALIDATE_THE_REGEX_PATTERN,
					label.subSequence(BatchClassManagementConstants.ZERO_CONST, label.length() - 1)));
			validFlag = false;
		}

		if (validFlag && !view.getValidateValuePatternTextBox().isValid()) {
			String label = view.getValuePatternLabel().getText();
			ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
					BatchClassManagementMessages.VALIDATE_THE_REGEX_PATTERN,
					label.subSequence(BatchClassManagementConstants.ZERO_CONST, label.length() - 1)));
			validFlag = false;
		}

		if (validFlag && !view.getValidateMultiplierTextBox().validate()) {
			if (view.getMultiplier() != null && !view.getMultiplier().isEmpty()) {
				ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
						BatchClassManagementMessages.MULTIPLIER_ERROR));
			}
			validFlag = false;
		}
		if (validFlag && view.getMultiplier() != null && !view.getMultiplier().isEmpty()) {
			Integer mult = (int) (Float.parseFloat(view.getMultiplier()) * BatchClassManagementConstants.HUNDRED);
			if (mult > BatchClassManagementConstants.HUNDRED || mult < BatchClassManagementConstants.ZERO_CONST) {
				ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
						BatchClassManagementMessages.MULTIPLIER_ERROR));
				validFlag = false;
			}
		}
		return validFlag;
	}

	/**
	 * This method sets the Key and Value coordinates in the advanced KV DTO.
	 * 
	 * @param keyCoord Coordinates
	 * @param valueCoord Coordinates
	 */
	private void setKeyValueCoordinates(final Coordinates keyCoord, final Coordinates valueCoord) {
		double aspectWidthRatio = view.getAspectRatioWidth();
		double aspectHeightRatio = view.getAspectRatioHeight();
		advancedKVExtractionDTO.setKeyX0Coord((int) Math.round(keyCoord.getX0() / aspectWidthRatio));
		advancedKVExtractionDTO.setKeyX1Coord((int) Math.round(keyCoord.getX1() / aspectWidthRatio));
		advancedKVExtractionDTO.setKeyY0Coord((int) Math.round(keyCoord.getY0() / aspectHeightRatio));
		advancedKVExtractionDTO.setKeyY1Coord((int) Math.round(keyCoord.getY1() / aspectHeightRatio));
		advancedKVExtractionDTO.setValueX0Coord((int) Math.round(valueCoord.getX0() / aspectWidthRatio));
		advancedKVExtractionDTO.setValueX1Coord((int) Math.round(valueCoord.getX1() / aspectWidthRatio));
		advancedKVExtractionDTO.setValueY0Coord((int) Math.round(valueCoord.getY0() / aspectHeightRatio));
		advancedKVExtractionDTO.setValueY1Coord((int) Math.round(valueCoord.getY1() / aspectHeightRatio));
	}

	/**
	 * To show view in case of cancel click.
	 */
	public void onCancelButtonClicked() {
		if (controller.isAdd()) {
			controller.setAdd(false);
		}
		view.removeAllOverlays();
		controller.getView().toggleBottomPanelShowHide(true);
		controller.getMainPresenter().showFieldTypeView(false);

	}

	/**
	 * To clear values.
	 */
	public void clearValues() {
		if (controller.getSelectedKVExtraction() != null) {
			view.setUseExistingKey(false);
			view.setLengthOfRect(String.valueOf(BatchClassManagementConstants.ZERO_CONST));
			view.setWidthOfRect(String.valueOf(BatchClassManagementConstants.ZERO_CONST));
			view.setxOffset(String.valueOf(BatchClassManagementConstants.ZERO_CONST));
			view.setyOffset(String.valueOf(BatchClassManagementConstants.ZERO_CONST));
			view.setMultiplier(AdminConstants.EMPTY_STRING);
			view.setKeyPatternText(AdminConstants.EMPTY_STRING);
			view.setValuePattern(AdminConstants.EMPTY_STRING);
			view.setFetchValue(KVFetchValue.ALL);
		}
	}

	/**
	 * To set image url and coordinates.
	 */
	public void setImageUrlAndCoordinates() {
		KVExtractionDTO selectedKVExtraction = controller.getSelectedKVExtraction();
		if (selectedKVExtraction != null) {
			AdvancedKVExtractionDTO advancedKVExtractionDTO = selectedKVExtraction.getAdvancedKVExtractionDTO();
			if (null != advancedKVExtractionDTO && null != advancedKVExtractionDTO.getDisplayImageName()) {
				view.setBatchClassID(controller.getBatchClass().getIdentifier());
				view.setDocName(controller.getSelectedDocument().getName());
				getPageImageUrl(view.getBatchClassID(), view.getDocName(), advancedKVExtractionDTO.getDisplayImageName());
			}
		}
	}

	/**
	 * To perform operations on Test Advanced KV Button click.
	 */
	public void onTestAdvancedKvButtonClicked() {
		final String fileName = advancedKVExtractionDTO.getImageName();
		if (validateFields()) {
			ScreenMaskUtility.maskScreen();
			saveDataInDTO(controller.getSelectedKVExtraction());
			controller.getRpcService().testAdvancedKVExtraction(controller.getBatchClass(), controller.getSelectedKVExtraction(),
					view.getDocName(), fileName, new EphesoftAsyncCallback<List<OutputDataCarrierDTO>>() {

						@Override
						public void customFailure(Throwable throwable) {
							ScreenMaskUtility.unmaskScreen();
							ConfirmationDialogUtil.showConfirmationDialog(throwable.getMessage(), MessageConstants.TITLE_TEST_FAILURE,
									Boolean.TRUE);
						}

						@Override
						public void onSuccess(List<OutputDataCarrierDTO> outputDtos) {
							ScreenMaskUtility.unmaskScreen();
							getUpdatedFileName(fileName);
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

						private void getUpdatedFileName(final String fileName) {
							controller.getRpcService().getUpdatedTestFileName(view.getBatchClassID(), view.getDocName(), fileName,
									new EphesoftAsyncCallback<String>() {

										@Override
										public void customFailure(Throwable arg0) {

										}

										@Override
										public void onSuccess(String updatedFileName) {
											advancedKVExtractionDTO.setImageName(updatedFileName);
										}
									});
						}
					});
		}
	}

	/**
	 * To set Image Name in DTO.
	 * 
	 * @param imageName String
	 * @param displayImageName String
	 */
	public void setImageNameInDTO(final String imageName, final String displayImageName) {
		advancedKVExtractionDTO.setImageName(imageName);
		advancedKVExtractionDTO.setDisplayImageName(displayImageName);
	}

	/**
	 * To get Span List.
	 * 
	 * @param coordinateX int
	 * @param coordinateY int
	 * @param clientCoordinateX int
	 * @param clientCoordinateY int
	 */
	public void getSpanList(final int coordinateX, final int coordinateY, final int clientCoordinateX, final int clientCoordinateY) {
		String imageFileName = advancedKVExtractionDTO.getImageName();
		String hocrFileName = imageFileName.substring(BatchClassManagementConstants.ZERO_CONST, imageFileName
				.lastIndexOf(BatchClassManagementConstants.DOT))
				+ FileType.HTML.getExtensionWithDot();
		controller.getRpcService().getSpanList(controller.getBatchClass().getIdentifier(), controller.getSelectedDocument().getName(),
				hocrFileName, new EphesoftAsyncCallback<List<Span>>() {

					@Override
					public void customFailure(Throwable arg0) {

					}

					@Override
					public void onSuccess(List<Span> arg0) {
						view.setSpanList(arg0);
						view.extractSpanValue(coordinateX, coordinateY, clientCoordinateX, clientCoordinateY);
					}
				});
	}

	/**
	 * To set Key Pattern Fields.
	 */
	public void setKeyPatternFields() {

		DocumentTypeDTO docTypeDTO = controller.getSelectedDocument();
		String fieldOrderNumber = controller.getSelectedDocumentLevelField().getFieldOrderNumber();
		List<String> fieldTypeNames = new ArrayList<String>();
		if (docTypeDTO != null && fieldOrderNumber != null && !fieldOrderNumber.isEmpty()) {
			Collection<FieldTypeDTO> fieldTypeDTOs = docTypeDTO.getFields(false);
			if (fieldTypeDTOs != null && !fieldTypeDTOs.isEmpty()) {
				for (FieldTypeDTO fieldTypeDTO : fieldTypeDTOs) {
					if (Integer.parseInt(fieldTypeDTO.getFieldOrderNumber()) < Integer.parseInt(fieldOrderNumber)) {
						fieldTypeNames.add(fieldTypeDTO.getName());
					}
				}
			}
			view.setKeyFieldList(fieldTypeNames);
		}
	}

	/**
	 * To perform operations on key pattern validate button click.
	 */
	public void onKeyPatternValidateButtonClicked() {
		view.getValidateKeyPatternTextBox().validate();

	}

	/**
	 * To perform operations on value pattern validate button click.
	 */
	public void onValuePatternValidateButtonClicked() {
		view.getValidateValuePatternTextBox().validate();
	}
}
