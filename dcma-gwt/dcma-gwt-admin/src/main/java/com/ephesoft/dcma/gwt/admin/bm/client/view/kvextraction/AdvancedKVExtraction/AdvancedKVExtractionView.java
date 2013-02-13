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

package com.ephesoft.dcma.gwt.admin.bm.client.view.kvextraction.advancedkvextraction;

import java.util.Arrays;
import java.util.List;

import com.ephesoft.dcma.batch.schema.HocrPages.HocrPage.Spans.Span;
import com.ephesoft.dcma.core.common.KVFetchValue;
import com.ephesoft.dcma.core.common.KVPageValue;
import com.ephesoft.dcma.gwt.admin.bm.client.AdminConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.MessageConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementMessages;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.kvextraction.advancedkvextraction.AdvancedKVExtractionPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.view.fieldtype.KVExtractionTestResultView;
import com.ephesoft.dcma.gwt.core.client.View;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.client.ui.RotatableImage;
import com.ephesoft.dcma.gwt.core.client.ui.ScreenMaskUtility;
import com.ephesoft.dcma.gwt.core.client.validator.RegExValidatableWidget;
import com.ephesoft.dcma.gwt.core.client.validator.ValidatableWidget;
import com.ephesoft.dcma.gwt.core.client.view.ContextMenuPanel;
import com.ephesoft.dcma.gwt.core.client.view.SlidingPanel;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialogUtil;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ErrorEvent;
import com.google.gwt.event.dom.client.ErrorHandler;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.event.dom.client.ScrollHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitHandler;

/**
 * This class shows the advanced KV extraction view details.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.gwt.core.client.View
 */
public class AdvancedKVExtractionView extends View<AdvancedKVExtractionPresenter> {

	/**
	 * EXTENSION_PNG String.
	 */
	private static final String EXTENSION_PNG = "png";

	/**
	 * EXTENSION_CHAR String.
	 */
	private static final String EXTENSION_CHAR = ".";

	/**
	 * UI binder.
	 */
	interface Binder extends UiBinder<DockLayoutPanel, AdvancedKVExtractionView> {
	}

	/**
	 * Instantiates a class via deferred binding.
	 */
	private static final Binder BINDER = GWT.create(Binder.class);

	/**
	 * advancedKVLayoutPanel DockLayoutPanel.
	 */
	@UiField
	protected DockLayoutPanel advancedKVLayoutPanel;

	/**
	 * useExistingKey CheckBox.
	 */
	@UiField
	protected CheckBox useExistingKey;

	/**
	 * useExistingKeyLabel Label.
	 */
	@UiField
	protected Label useExistingKeyLabel;

	/**
	 * keyPatternLabel Label.
	 */
	@UiField
	protected Label keyPatternLabel;

	/**
	 * To get Key Pattern Label.
	 * 
	 * @return Label
	 */
	public Label getKeyPatternLabel() {
		return keyPatternLabel;
	}

	/**
	 * To set Key Pattern Label.
	 * 
	 * @param keyPatternLabel Label
	 */
	public void setKeyPatternLabel(Label keyPatternLabel) {
		this.keyPatternLabel = keyPatternLabel;
	}

	/**
	 * keyPatternStar Label.
	 */
	@UiField
	protected Label keyPatternStar;

	/**
	 * keyPatternPanel HorizontalPanel.
	 */
	@UiField
	protected HorizontalPanel keyPatternPanel;

	/**
	 * keyPatternValidateButton Button.
	 */
	@UiField
	protected Button keyPatternValidateButton;

	/**
	 * keyPatternField ListBox.
	 */
	protected ListBox keyPatternField;

	/**
	 * keyPatternText TextBox.
	 */
	protected TextBox keyPatternText;

	/**
	 * valuePatternLabel Label.
	 */
	@UiField
	protected Label valuePatternLabel;

	/**
	 * To get Value Pattern Label.
	 * 
	 * @return Label
	 */
	public Label getValuePatternLabel() {
		return valuePatternLabel;
	}

	/**
	 * To set Value Pattern Label.
	 * 
	 * @param valuePatternLabel Label
	 */
	public void setValuePatternLabel(Label valuePatternLabel) {
		this.valuePatternLabel = valuePatternLabel;
	}

	/**
	 * valuePatternStar Label.
	 */
	@UiField
	protected Label valuePatternStar;

	/**
	 * valuePattern TextBox.
	 */
	@UiField
	protected TextBox valuePattern;

	/**
	 * valuePatternValidateButton Button.
	 */
	@UiField
	protected Button valuePatternValidateButton;

	/**
	 * fetchValueLabel Label.
	 */
	@UiField
	protected Label fetchValueLabel;

	/**
	 * fetchValue ListBox.
	 */
	@UiField
	protected ListBox fetchValue;

	/**
	 * fetchValueStar Label.
	 */
	@UiField
	protected Label fetchValueStar;

	/**
	 * kvPageValueLabel Label.
	 */
	@UiField
	protected Label kvPageValueLabel;

	/**
	 * kvPageValue ListBox.
	 */
	@UiField
	protected ListBox kvPageValue;

	/**
	 * kvPageValueStar Label.
	 */
	@UiField
	protected Label kvPageValueStar;

	/**
	 * lengthLabel Label.
	 */
	@UiField
	protected Label lengthLabel;

	/**
	 * length TextBox.
	 */
	@UiField
	protected TextBox length;

	/**
	 * lengthStar Label.
	 */
	@UiField
	protected Label lengthStar;

	/**
	 * widthLabel Label.
	 */
	@UiField
	protected Label widthLabel;

	/**
	 * width TextBox.
	 */
	@UiField
	protected TextBox width;

	/**
	 * widthStar Label.
	 */
	@UiField
	protected Label widthStar;

	/**
	 * xOffsetLabel Label.
	 */
	@UiField
	protected Label xOffsetLabel;

	/**
	 * xOffset TextBox.
	 */
	@UiField
	protected TextBox xOffset;

	/**
	 * xOffsetStar Label.
	 */
	@UiField
	protected Label xOffsetStar;

	/**
	 * yOffsetLabel Label.
	 */
	@UiField
	protected Label yOffsetLabel;

	/**
	 * yOffset TextBox.
	 */
	@UiField
	protected TextBox yOffset;

	/**
	 * yOffsetStar Label.
	 */
	@UiField
	protected Label yOffsetStar;

	/**
	 * multiplierLabel Label.
	 */
	@UiField
	protected Label multiplierLabel;

	/**
	 * multiplier TextBox.
	 */
	@UiField
	protected TextBox multiplier;

	/**
	 * saveButton Button.
	 */
	@UiField
	protected Button saveButton;

	/**
	 * cancelButton Button.
	 */
	@UiField
	protected Button cancelButton;

	/**
	 * samplePatternButton Button.
	 */
	@UiField
	protected Button samplePatternButton;

	/**
	 * oldKVExtractionProperties VerticalPanel.
	 */
	@UiField
	protected VerticalPanel oldKVExtractionProperties;

	/**
	 * groupingKVExtractionProperties DockLayoutPanel.
	 */
	@UiField
	protected DockLayoutPanel groupingKVExtractionProperties;

	/**
	 * newKVExtractionProperties VerticalPanel.
	 */
	@UiField
	protected VerticalPanel newKVExtractionProperties;

	/**
	 * imageUpload FormPanel.
	 */
	@UiField
	protected FormPanel imageUpload;

	/**
	 * importFile FileUpload.
	 */
	@UiField
	protected FileUpload importFile;

	/**
	 * batchClassID Hidden.
	 */
	@UiField
	protected Hidden batchClassID;

	/**
	 * docName Hidden.
	 */
	@UiField
	protected Hidden docName;

	/**
	 * pageImage RotatableImage.
	 */
	@UiField
	protected RotatableImage pageImage;

	/**
	 * captureKey Button.
	 */
	@UiField
	protected Button captureKey;

	/**
	 * captureValue Button.
	 */
	@UiField
	protected Button captureValue;

	/**
	 * clearButton Button.
	 */
	@UiField
	protected Button clearButton;

	/**
	 * testAdvKvButton Button.
	 */
	@UiField
	protected Button testAdvKvButton;

	/**
	 * initialOptionsPanel HorizontalPanel.
	 */
	@UiField
	protected HorizontalPanel initialOptionsPanel;

	/**
	 * secondOptionsPanel HorizontalPanel.
	 */
	@UiField
	protected HorizontalPanel secondOptionsPanel;

	/**
	 * editKey Button.
	 */
	@UiField
	protected Button editKey;

	/**
	 * editValue Button.
	 */
	@UiField
	protected Button editValue;

	/**
	 * nextOptions Button.
	 */
	@UiField
	protected Button nextOptions;

	/**
	 * previousOptions Button.
	 */
	@UiField
	protected Button previousOptions;

	/**
	 * optionsSlidingPanel SlidingPanel.
	 */
	@UiField
	protected SlidingPanel optionsSlidingPanel;

	/**
	 * keyValueCoordinates KeyValueCoordinates.
	 */
	private final KeyValueCoordinates keyValueCoordinates;

	/**
	 * kvExtractionTestResultView KVExtractionTestResultView.
	 */
	private final KVExtractionTestResultView kvExtractionTestResultView;

	/**
	 * editableCoordinate EditableCoordinate.
	 */
	private EditableCoordinate editableCoordinate = EditableCoordinate.NONE;

	/**
	 * originalWidth Integer.
	 */
	private Integer originalWidth = 0;

	/**
	 * originalHeight Integer.
	 */
	private Integer originalHeight = 0;

	/**
	 * imageScroll ScrollPanel.
	 */
	@UiField
	protected ScrollPanel imageScroll;

	/**
	 * tempImage RotatableImage.
	 */
	@UiField
	protected RotatableImage tempImage;

	/**
	 * dimensionImage -An unloaded copy of the image uploaded. Useful in case where IE is used as browser to access correct width and
	 * height of the image.
	 */
	protected RotatableImage dimensionImage = new RotatableImage();

	/**
	 * validateKeyPatternTextBox RegExValidatableWidget<TextBox>.
	 */
	private RegExValidatableWidget<TextBox> validateKeyPatternTextBox;

	/**
	 * validateValuePatternTextBox RegExValidatableWidget<TextBox>.
	 */
	private RegExValidatableWidget<TextBox> validateValuePatternTextBox;

	/**
	 * validateMultiplierTextBox ValidatableWidget<TextBox>.
	 */
	private ValidatableWidget<TextBox> validateMultiplierTextBox;

	/**
	 * lastOperation EditOperation.
	 */
	private EditOperation lastOperation = EditOperation.NONE;

	/**
	 * contextMenu ContextMenuPanel.
	 */
	private final ContextMenuPanel contextMenu = new ContextMenuPanel();

	/**
	 * spanList List.
	 */
	private List<Span> spanList;

	/**
	 * To get Span List.
	 * 
	 * @return List
	 */
	public List<Span> getSpanList() {
		return spanList;
	}

	/**
	 * To set Span List.
	 * 
	 * @param spanList List
	 */
	public void setSpanList(List<Span> spanList) {
		this.spanList = spanList;
	}

	/**
	 * Constructor.
	 */
	public AdvancedKVExtractionView() {
		super();
		initWidget(BINDER.createAndBindUi(this));
		this.kvExtractionTestResultView = new KVExtractionTestResultView();
		valuePattern.addStyleName(AdminConstants.GWT_ADVANCED_KV_TEXT_BOX);
		editKey.setText(AdminConstants.EDIT_KEY);
		editValue.setText(AdminConstants.EDIT_VALUE);

		previousOptions.setText(AdminConstants.PREVIOUS);
		nextOptions.setText(AdminConstants.NEXT);
		samplePatternButton.setText(AdminConstants.SAMPLE_REGEX_BUTTON);
		keyPatternValidateButton.setTitle(AdminConstants.VALIDATE_BUTTON);
		valuePatternValidateButton.setTitle(AdminConstants.VALIDATE_BUTTON);
		keyPatternValidateButton.setStyleName(AdminConstants.VALIDATE_BUTTON_IMAGE);
		valuePatternValidateButton.setStyleName(AdminConstants.VALIDATE_BUTTON_IMAGE);
		saveButton.setText(AdminConstants.OK_BUTTON);
		cancelButton.setText(AdminConstants.CANCEL_BUTTON);
		saveButton.setHeight(AdminConstants.BUTTON_HEIGHT);
		cancelButton.setHeight(AdminConstants.BUTTON_HEIGHT);
		useExistingKeyLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.USE_EXISTING_KEY_LABEL));
		useExistingKeyLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);

		keyPatternLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.KEY_PATTERN)
				+ AdminConstants.COLON);
		keyPatternLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		keyPatternStar.setText(AdminConstants.STAR);
		keyPatternStar.setStyleName(AdminConstants.FONT_RED_STYLE);
		setKeyPatternPanelView();

		valuePatternLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.VALUE_PATTERN)
				+ AdminConstants.COLON);
		valuePatternLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		valuePatternStar.setText(AdminConstants.STAR);
		valuePatternStar.setStyleName(AdminConstants.FONT_RED_STYLE);

		fetchValueLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.FETCH_VALUE)
				+ AdminConstants.COLON);
		fetchValueLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		fetchValueStar.setText(AdminConstants.STAR);
		fetchValueStar.setStyleName(AdminConstants.FONT_RED_STYLE);

		kvPageValueLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.KV_PAGE_VALUE_LABEL)
				+ AdminConstants.COLON);
		kvPageValueLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		kvPageValueStar.setText(AdminConstants.STAR);
		kvPageValueStar.setStyleName(AdminConstants.FONT_RED_STYLE);

		lengthLabel
				.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.LENGTH_LABEL) + AdminConstants.COLON);
		lengthLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		lengthStar.setText(AdminConstants.STAR);
		lengthStar.setStyleName(AdminConstants.FONT_RED_STYLE);

		widthLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.WIDTH_LABEL) + AdminConstants.COLON);
		widthLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		widthStar.setText(AdminConstants.STAR);
		widthStar.setStyleName(AdminConstants.FONT_RED_STYLE);

		xOffsetLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.XOFFSET_LABEL)
				+ AdminConstants.COLON);
		xOffsetLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		xOffsetStar.setText(AdminConstants.STAR);
		xOffsetStar.setStyleName(AdminConstants.FONT_RED_STYLE);

		yOffsetLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.YOFFSET_LABEL)
				+ AdminConstants.COLON);
		yOffsetLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		yOffsetStar.setText(AdminConstants.STAR);
		yOffsetStar.setStyleName(AdminConstants.FONT_RED_STYLE);

		multiplierLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.MULTIPLIER_LABEL)
				+ AdminConstants.COLON);
		multiplierLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);

		advancedKVLayoutPanel.setStyleName(AdminConstants.BORDER_STYLE);
		advancedKVLayoutPanel.setWidth("100%");
		oldKVExtractionProperties.setSpacing(BatchClassManagementConstants.SIX);
		oldKVExtractionProperties.addStyleName("background_group");
		newKVExtractionProperties.setSpacing(BatchClassManagementConstants.SIX);
		newKVExtractionProperties.addStyleName("background_group");

		groupingKVExtractionProperties.addStyleName("right_border");

		imageUpload.addStyleName(AdminConstants.BUTTON_PADDING_STYLE);
		imageUpload.setEncoding(FormPanel.ENCODING_MULTIPART);
		imageUpload.setMethod(FormPanel.METHOD_POST);
		imageUpload.setAction("dcma-gwt-admin/uploadImageFile");

		captureKey.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.CAPTURE_KEY_BUTTON));
		captureValue.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.CAPTURE_VALUE_BUTTON));
		clearButton.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.CLEAR_BUTTON));
		testAdvKvButton.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.TEST_ADV_KV_LABEL));

		validateKeyPatternTextBox = new RegExValidatableWidget<TextBox>(keyPatternText);
		validateKeyPatternTextBox.getWidget().addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				validateKeyPatternTextBox.setValid(false);
			}
		});

		validateValuePatternTextBox = new RegExValidatableWidget<TextBox>(valuePattern);
		validateValuePatternTextBox.getWidget().addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				validateValuePatternTextBox.setValid(false);
			}
		});

		validateMultiplierTextBox = new ValidatableWidget<TextBox>(multiplier);
		validateMultiplierTextBox.getWidget().addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> arg0) {
				validateMultiplierTextBox.toggleValidDateBox();

			}
		});

		length.setReadOnly(true);
		width.setReadOnly(true);
		xOffset.setReadOnly(true);
		yOffset.setReadOnly(true);
		initialOptionsPanel.setSpacing(BatchClassManagementConstants.THREE);
		secondOptionsPanel.setSpacing(BatchClassManagementConstants.THREE);
		optionsSlidingPanel.setWidget(initialOptionsPanel);

		tempImage.setVisible(false);

		keyValueCoordinates = new KeyValueCoordinates(this);
		disableAllButtons();

		dimensionImage.setVisible(false);

		dimensionImage.addLoadHandler(new LoadHandler() {

			@Override
			public void onLoad(final LoadEvent arg0) {
				// Do nothing
			}
		});

		tempImage.addLoadHandler(new LoadHandler() {

			@Override
			public void onLoad(LoadEvent arg0) {
				resetOperations();
				pageImage.setVisible(true);
				ScreenMaskUtility.unmaskScreen();

				DOM.setElementAttribute(pageImage.getElement(), "src", (DOM.getElementAttribute(dimensionImage.getElement(), "src")));
				pageImage.setUrl(dimensionImage.getUrl());
				originalWidth = dimensionImage.getWidth();
				originalHeight = dimensionImage.getHeight();
				loadImage();
			}
		});

		tempImage.addErrorHandler(new ErrorHandler() {

			@Override
			public void onError(ErrorEvent arg0) {
				ScreenMaskUtility.unmaskScreen();
				removeAllOverlays();
				presenter.setEditAdvancedKV(false);
				ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
						BatchClassManagementMessages.ERROR_UPLOAD_IMAGE));
			}
		});

		pageImage.addLoadHandler(new LoadHandler() {

			@Override
			public void onLoad(LoadEvent arg0) {
				// loadImage();
			}
		});
		importFile.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent arg0) {
				imageUpload.submit();
			}
		});
		imageUpload.addSubmitHandler(new SubmitHandler() {

			@Override
			public void onSubmit(SubmitEvent event) {
				String fileName = importFile.getFilename();
				String fileExt = fileName.substring(fileName.lastIndexOf('.') + 1);
				if (!fileExt.equalsIgnoreCase("tiff") && !fileExt.equalsIgnoreCase("tif")) {
					ScreenMaskUtility.unmaskScreen();
					ConfirmationDialogUtil.showConfirmationDialogError(MessageConstants.UPLOAD_IMAGE_INVALID_TYPE);
					event.cancel();
				}
				batchClassID.setValue(presenter.getController().getBatchClass().getIdentifier());
				docName.setValue(presenter.getController().getSelectedDocument().getName());
			}
		});
		imageUpload.addSubmitCompleteHandler(new SubmitCompleteHandler() {

			@Override
			public void onSubmitComplete(SubmitCompleteEvent arg0) {
				String result = arg0.getResults();
				if (result.toLowerCase().indexOf(AdminConstants.ERROR_CODE_TEXT) > -1) {
					ScreenMaskUtility.unmaskScreen();
					presenter.setEditAdvancedKV(false);
					ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
							BatchClassManagementMessages.ERROR_UPLOAD_IMAGE));
					return;
				}
				String fileName = importFile.getFilename();
				String fileSeperatorParam = "file_seperator:";
				String fileSeparator = null;
				int index = result.indexOf(fileSeperatorParam);
				if (index != -1) {
					int endIndex = result.indexOf('|', index);
					if (endIndex != -1) {
						fileSeparator = result.substring(index + fileSeperatorParam.length(), endIndex);
					} else {
						fileSeparator = result.substring(index + fileSeperatorParam.length());
					}
				}
				if (fileName != null && fileSeparator != null) {
					fileName = fileName.substring(fileName.lastIndexOf(fileSeparator) + 1);
				}
				String pngFileName = fileName.substring(0, fileName.lastIndexOf(EXTENSION_CHAR) + 1) + EXTENSION_PNG;
				presenter.setImageNameInDTO(fileName, pngFileName);
				presenter.setEditAdvancedKV(false);
				presenter.getPageImageUrl(batchClassID.getValue(), docName.getValue(), pngFileName);
			}
		});

		pageImage.addStyleName("pointer");

		pageImage.addMouseDownHandler(new MouseDownHandler() {

			@Override
			public void onMouseDown(MouseDownEvent paramMouseDownEvent) {
				int xVal = paramMouseDownEvent.getX();
				int yVal = paramMouseDownEvent.getY();
				switch (lastOperation) {
					case KEY:
						Coordinates keyCoordinates = keyValueCoordinates.getKeyCoordinates();
						findAndSetEditableCoordinate(xVal, yVal, keyCoordinates, true);
						break;
					case VALUE:
						Coordinates valueCoordinates = keyValueCoordinates.getValueCoordinates();
						findAndSetEditableCoordinate(xVal, yVal, valueCoordinates, false);
						break;
					case NONE:
						int nativeButton = paramMouseDownEvent.getNativeButton();
						if (NativeEvent.BUTTON_LEFT == nativeButton) {
							setEditValueAndTestAdvKvButton();
							keyValueCoordinates.mouseDownat(xVal, yVal);
						} else if (NativeEvent.BUTTON_RIGHT == nativeButton) {
							pageImage.setTitle(null);
							int clientY = paramMouseDownEvent.getClientY();
							int clientX = paramMouseDownEvent.getClientX();
							if (spanList == null) {
								presenter.getSpanList(xVal, yVal, clientX, clientY);
							} else {
								extractSpanValue(xVal, yVal, clientX, clientY);
							}
						}
						break;
					default:
						break;
				}
			}
		});

		pageImage.addMouseMoveHandler(new MouseMoveHandler() {

			@Override
			public void onMouseMove(MouseMoveEvent paramMouseMoveEvent) {
				int xVal = paramMouseMoveEvent.getX();
				int yVal = paramMouseMoveEvent.getY();
				Coordinates valueCoordinates = keyValueCoordinates.getValueCoordinates();
				Coordinates keyCoordinates = keyValueCoordinates.getKeyCoordinates();
				switch (lastOperation) {
					case KEY:
						moveMouseAt(xVal, yVal, keyCoordinates);
						keyCoordinates.doOverlay();
						createOverlay(valueCoordinates.getX0(), valueCoordinates.getX1(), valueCoordinates.getY0(), valueCoordinates
								.getY1(), 1, false, true);
						break;
					case VALUE:
						moveMouseAt(xVal, yVal, valueCoordinates);
						valueCoordinates.doOverlay();
						createOverlay(keyCoordinates.getX0(), keyCoordinates.getX1(), keyCoordinates.getY0(), keyCoordinates.getY1(),
								1, true, false);
						break;
					case NONE: {
						keyValueCoordinates.mouseMoveat(xVal, yVal);
						break;
					}
					default:
						break;
				}
			}

			private void moveMouseAt(int xVal, int yVal, Coordinates coordinates) {
				switch (editableCoordinate) {
					case X0:
						if (xVal < coordinates.getX1()) {
							coordinates.setX0(xVal);
						}
						break;
					case Y0:
						if (yVal < coordinates.getY1()) {
							coordinates.setY0(yVal);
						}
						break;
					case X1:
						if (xVal > coordinates.getX0()) {
							coordinates.setX1(xVal);
						}
						break;
					case Y1:
						if (yVal > coordinates.getY0()) {
							coordinates.setY1(yVal);
						}
						break;
					default:
						break;
				}
			}
		});

		imageScroll.addScrollHandler(new ScrollHandler() {

			@Override
			public void onScroll(ScrollEvent paramScrollEvent) {
				keyValueCoordinates.createNewOverlay();
				contextMenu.hide();
			}
		});

		pageImage.setVisible(false);

	}

	/**
	 * To find and set editable coordinate.
	 * 
	 * @param xVal int
	 * @param yVal int
	 * @param coordinates Coordinates
	 * @param isKey boolean
	 */
	public void findAndSetEditableCoordinate(int xVal, int yVal, Coordinates coordinates, boolean isKey) {
		if (EditableCoordinate.NONE.equals(editableCoordinate)) {
			int X0Key = coordinates.getX0();
			int Y0Key = coordinates.getY0();
			int X1Key = coordinates.getX1();
			int Y1Key = coordinates.getY1();
			if (X1Key < X0Key) {
				coordinates.setX0(X1Key);
				coordinates.setX1(X0Key);
			}
			if (Y1Key < Y0Key) {
				coordinates.setY0(Y1Key);
				coordinates.setY1(Y0Key);
			}
			X0Key = coordinates.getX0();
			Y0Key = coordinates.getY0();
			X1Key = coordinates.getX1();
			Y1Key = coordinates.getY1();
			if (xVal > X0Key && xVal < X1Key) {
				if (yVal - Y0Key < Y1Key - yVal) {
					editableCoordinate = EditableCoordinate.Y0;
				} else {
					editableCoordinate = EditableCoordinate.Y1;
				}
			} else if (yVal > Y0Key && yVal < Y1Key) {
				if (xVal - X0Key < X1Key - xVal) {
					editableCoordinate = EditableCoordinate.X0;
				} else {
					editableCoordinate = EditableCoordinate.X1;
				}
			}

		} else {
			if (isKey) {
				captureKeyInfo();
			} else {
				captureValueInfo();
			}
			resetOperations();
		}
	}

	private void resetOperations() {
		lastOperation = EditOperation.NONE;
		editableCoordinate = EditableCoordinate.NONE;
	}

	/**
	 * To extractSpanValue.
	 * 
	 * @param coordinateX int
	 * @param coordinateY int
	 * @param clientCoordinateX int
	 * @param clientCoordinateY int
	 */
	public void extractSpanValue(int coordinateX, int coordinateY, int clientCoordinateX, int clientCoordinateY) {
		if (spanList != null) {
			double aspectWidthRatio = (double) (pageImage.getWidth()) / (double) (originalWidth);
			double aspectHeightRatio = (double) (pageImage.getHeight()) / (double) (originalHeight);
			int xCoordinate = (int) Math.round(coordinateX / aspectWidthRatio);
			int yCoordinate = (int) Math.round(coordinateY / aspectHeightRatio);
			for (Span span : spanList) {
				int spanX0 = span.getCoordinates().getX0().intValue();
				int spanY0 = span.getCoordinates().getY0().intValue();
				int spanX1 = span.getCoordinates().getX1().intValue();
				int spanY1 = span.getCoordinates().getY1().intValue();
				if (spanX0 <= xCoordinate && spanX1 >= xCoordinate && spanY0 <= yCoordinate && spanY1 >= yCoordinate) {
					MenuBar menuBar = new MenuBar(true);
					MenuItem menuItem = new MenuItem(span.getValue(), new Command() {

						@Override
						public void execute() {
							contextMenu.hide();
						}
					});
					menuBar.addItem(menuItem);
					contextMenu.setWidget(menuBar);
					contextMenu.setPopupPosition(clientCoordinateX, clientCoordinateY);
					contextMenu.show();
					break;
				}
			}
		}
	}

	private void setKeyPatternPanelView() {
		keyPatternText = new TextBox();
		keyPatternText.addStyleName(AdminConstants.GWT_ADVANCED_KV_TEXT_BOX);
		keyPatternField = new ListBox();
		keyPatternField.setWidth("98%");
		keyPatternPanel.add(keyPatternText);
		useExistingKey.setValue(Boolean.FALSE);
		useExistingKey.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> arg0) {
				if (arg0.getValue().equals(Boolean.TRUE)) {
					keyPatternValidateButton.setEnabled(Boolean.FALSE);
					keyPatternPanel.remove(keyPatternText);
					keyPatternPanel.add(keyPatternField);
				} else {
					keyPatternValidateButton.setEnabled(Boolean.TRUE);
					keyPatternPanel.remove(keyPatternField);
					keyPatternPanel.add(keyPatternText);
				}
			}
		});
	}

	/**
	 * To create overlays.
	 * 
	 * @param x0Coordinate double
	 * @param x1Coordinate double
	 * @param y0Coordinate double
	 * @param y1Coordinate double
	 * @param zoomFactor double
	 */
	public void createOverlay(double x0Coordinate, double x1Coordinate, double y0Coordinate, double y1Coordinate, double zoomFactor) {
		createOverlay(x0Coordinate, x1Coordinate, y0Coordinate, y1Coordinate, zoomFactor, false, false);
	}

	/**
	 * To create overlays.
	 * 
	 * @param x0Coordinate double
	 * @param x1Coordinate double
	 * @param y0Coordinate double
	 * @param y1Coordinate double
	 * @param zoomFactor double
	 * @param forKey boolean
	 * @param forValue boolean
	 */
	public void createOverlay(double x0Coordinate, double x1Coordinate, double y0Coordinate, double y1Coordinate, double zoomFactor,
			boolean forKey, boolean forValue) {
		double localX0Coordinate = x0Coordinate;
		double localX1Coordinate = x1Coordinate;
		double localY0Coordinate = y0Coordinate;
		double localY1Coordinate = y1Coordinate;

		if (localX0Coordinate == 0 && localX1Coordinate == 0 && localY0Coordinate == 0 && localY1Coordinate == 0) {
			return;
		}

		double xFactor = BatchClassManagementConstants.X_FACTOR;
		double yFactor = BatchClassManagementConstants.Y_FACTOR;
		double yScrollingAdder = BatchClassManagementConstants.Y_SCROLLING_ADDER;

		localX0Coordinate = (double) (localX0Coordinate) + (double) (xFactor * getViewPortWidth());
		localX1Coordinate = (double) (localX1Coordinate) + (double) (xFactor * getViewPortWidth());
		localY0Coordinate = (double) (localY0Coordinate) + (double) (yFactor * getViewPortHeight());
		localY1Coordinate = (double) (localY1Coordinate) + (double) (yFactor * getViewPortHeight());
		int yScrollPosition = imageScroll.getScrollPosition();
		int xScrollPosition = imageScroll.getHorizontalScrollPosition();
		if (yScrollPosition > (localY0Coordinate - (yFactor * getViewPortHeight()))
				|| xScrollPosition > (localX0Coordinate - (xFactor * getViewPortWidth()))
				|| localX1Coordinate - xScrollPosition > getViewPortWidth()
				|| localY1Coordinate - yScrollPosition + yScrollingAdder > getViewPortHeight()) {
			// removeOverlay();
			return;
		}
		if (xScrollPosition == 0 && yScrollPosition == 0 && zoomFactor == 1) {
			if (forKey) {
				doOverlayForKey(localX0Coordinate, localX1Coordinate, localY0Coordinate, localY1Coordinate, zoomFactor);
			} else if (forValue) {
				doOverlayForValue(localX0Coordinate, localX1Coordinate, localY0Coordinate, localY1Coordinate, zoomFactor);
			} else {
				doOverlay(localX0Coordinate, localX1Coordinate, localY0Coordinate, localY1Coordinate, zoomFactor);
			}
		} else if (xScrollPosition <= localX1Coordinate && yScrollPosition <= localY1Coordinate) {
			localX0Coordinate -= xScrollPosition;
			localX1Coordinate -= xScrollPosition;
			localY0Coordinate -= yScrollPosition;
			localY1Coordinate -= yScrollPosition;
			if (forKey) {
				doOverlayForKey(localX0Coordinate, localX1Coordinate, localY0Coordinate, localY1Coordinate, zoomFactor);
			} else if (forValue) {
				doOverlayForValue(localX0Coordinate, localX1Coordinate, localY0Coordinate, localY1Coordinate, zoomFactor);
			} else {
				doOverlay(localX0Coordinate, localX1Coordinate, localY0Coordinate, localY1Coordinate, zoomFactor);
			}
		}
		// doOverlay(x0, x1, y0, y1, zoomFactor);
	}

	/**
	 * To do overlay.
	 * 
	 * @param x0Value double
	 * @param x1Value double
	 * @param y0Value double
	 * @param y1Value double
	 * @param zoomFactor double
	 */
	public native void doOverlay(double x0Value, double x1Value, double y0Value, double y1Value, double zoomFactor) /*-{
																													return $wnd.doOverlay(x0Value, x1Value, y0Value, y1Value, zoomFactor);
																													}-*/;

	/**
	 * To do overlay for key.
	 * 
	 * @param x0Value double
	 * @param x1Value double
	 * @param y0Value double
	 * @param y1Value double
	 * @param zoomFactor double
	 */
	public native void doOverlayForKey(double x0Value, double x1Value, double y0Value, double y1Value, double zoomFactor) /*-{
																															return $wnd.doOverlayForKey(x0Value, x1Value, y0Value, y1Value, zoomFactor);
																															}-*/;

	/**
	 * To do overlay for value.
	 * 
	 * @param x0Value double
	 * @param x1Value double
	 * @param y0Value double
	 * @param y1Value double
	 * @param zoomFactor double
	 */
	public native void doOverlayForValue(double x0Value, double x1Value, double y0Value, double y1Value, double zoomFactor) /*-{
																															return $wnd.doOverlayForValue(x0Value, x1Value, y0Value, y1Value, zoomFactor);
																															}-*/;

	/**
	 * To remove Overlay.
	 */
	public native void removeOverlay() /*-{
										return $wnd.removeOverlay();
										}-*/;

	private void loadImage() {
		removeAllOverlays();
		pageImage.setVisible(true);
		enableDisableButtons();
		int screenWidth = getViewPortWidth();
		Integer imageWidth = screenWidth * BatchClassManagementConstants.SEVENTY / BatchClassManagementConstants.HUNDRED;
		Integer imageHeight = Integer.valueOf(0);

		if (originalWidth != 0) {
			imageHeight = originalHeight * imageWidth / originalWidth;
		}
		if (imageHeight == 0) {
			int screenHeight = getViewPortHeight();
			imageHeight = screenHeight * BatchClassManagementConstants.SEVENTY / BatchClassManagementConstants.HUNDRED;
		}
		pageImage.setWidth(imageWidth.toString() + "px");
		pageImage.setHeight(imageHeight.toString() + "px");
		// presenter.setEditAdvancedKV(false);
		if (presenter.isEditAdvancedKV()) {
			presenter.createKeyValueOverlay();
		}
		spanList = null;
		ScreenMaskUtility.unmaskScreen();
	}

	/**
	 * To remove all overlays.
	 */
	public void removeAllOverlays() {
		removeOverlay();
		keyValueCoordinates.clearFinalizeValues();
		keyValueCoordinates.initialize();
	}

	private native int getViewPortWidth() /*-{
											return $wnd.getViewPortWidth();
											}-*/;

	private native int getViewPortHeightForFirefox() /*-{
														return $wnd.getViewPortHeight();
														}-*/;

	private int getViewPortHeight() {
		int height = getViewPortHeightForFirefox();
		String currentBrowser = getUserAgent();
		if (currentBrowser != null && currentBrowser.length() > 0 && currentBrowser.contains("msie")) {
			height = getViewPortHeightForIE();
		}
		return height;
	}

	/**
	 * To get User Agent.
	 * 
	 * @return String
	 */
	public static native String getUserAgent() /*-{
												return navigator.userAgent.toLowerCase();
												}-*/;

	private native int getViewPortHeightForIE() /*-{
												return $wnd.getViewPortHeightForIE();
												}-*/;

	/**
	 * To set Page Image Url.
	 * 
	 * @param pageImageUrl String
	 */
	public void setPageImageUrl(final String pageImageUrl) {
		DOM.setElementAttribute(this.tempImage.getElement(), "src", pageImageUrl);
		this.tempImage.setUrl(pageImageUrl);
		DOM.setElementAttribute(this.dimensionImage.getElement(), "src", pageImageUrl);
		this.dimensionImage.setUrl(pageImageUrl);
	}

	/**
	 * To get Page Image Url.
	 * 
	 * @return String
	 */
	public String getPageImageUrl() {
		return this.tempImage.getUrl();
	}

	/**
	 * To get Multiplier.
	 * 
	 * @return String
	 */
	public String getMultiplier() {
		return multiplier.getText();
	}

	/**
	 * To set Multiplier.
	 * 
	 * @param multiplier String
	 */
	public void setMultiplier(String multiplier) {
		this.multiplier.setText(multiplier);
	}

	/**
	 * To get y-Offset.
	 * 
	 * @return String
	 */
	public String getyOffset() {
		return yOffset.getText();
	}

	/**
	 * To set y-Offset.
	 * 
	 * @param yOffset String
	 */
	public void setyOffset(String yOffset) {
		this.yOffset.setText(yOffset);
	}

	/**
	 * To get x-Offset.
	 * 
	 * @return String
	 */
	public String getxOffset() {
		return xOffset.getText();
	}

	/**
	 * To set x-Offset.
	 * 
	 * @param xOffset String
	 */
	public void setxOffset(String xOffset) {
		this.xOffset.setText(xOffset);
	}

	/**
	 * To get Width.
	 * 
	 * @return String
	 */
	public String getWidth() {
		return width.getText();
	}

	/**
	 * To set Width Text Box.
	 * 
	 * @param width String
	 */
	public void setWidthTextBox(String width) {
		this.width.setText(width);
	}

	/**
	 * To get Length.
	 * 
	 * @return String
	 */
	public String getLength() {
		return length.getText();
	}

	/**
	 * To set Length Of Rectangle.
	 * 
	 * @param length String
	 */
	public void setLengthOfRect(String length) {
		this.length.setText(length);
	}

	/**
	 * To set width Of Rectangle.
	 * 
	 * @param width String
	 */
	public void setWidthOfRect(String width) {
		this.width.setText(width);
	}

	/**
	 * To get Validate Key Pattern Text Box.
	 * 
	 * @return RegExValidatableWidget<TextBox>
	 */
	public RegExValidatableWidget<TextBox> getValidateKeyPatternTextBox() {
		return validateKeyPatternTextBox;
	}

	/**
	 * To get Validate value Pattern Text Box.
	 * 
	 * @return RegExValidatableWidget<TextBox>
	 */
	public RegExValidatableWidget<TextBox> getValidateValuePatternTextBox() {
		return validateValuePatternTextBox;
	}

	/**
	 * To set Validate Multiplier Text Box.
	 * 
	 * @param validateMultiplierTextBox ValidatableWidget<TextBox>
	 */
	public void setValidateMultiplierTextBox(ValidatableWidget<TextBox> validateMultiplierTextBox) {
		this.validateMultiplierTextBox = validateMultiplierTextBox;
	}

	/**
	 * To get Validate Multiplier Text Box.
	 * 
	 * @return ValidatableWidget<TextBox>
	 */
	public ValidatableWidget<TextBox> getValidateMultiplierTextBox() {
		return validateMultiplierTextBox;
	}

	/**
	 * To set Validate Key Pattern Text Box.
	 * 
	 * @param validateKeyPatternTextBox RegExValidatableWidget<TextBox>
	 */
	public void setValidateKeyPatternTextBox(RegExValidatableWidget<TextBox> validateKeyPatternTextBox) {
		this.validateKeyPatternTextBox = validateKeyPatternTextBox;
	}

	/**
	 * To set Validate Value Pattern Text Box.
	 * 
	 * @param validateValuePatternTextBox RegExValidatableWidget<TextBox>
	 */
	public void setValidateValuePatternTextBox(RegExValidatableWidget<TextBox> validateValuePatternTextBox) {
		this.validateValuePatternTextBox = validateValuePatternTextBox;
	}

	/**
	 * To get Fetch Value.
	 * 
	 * @return KVFetchValue
	 */
	public KVFetchValue getFetchValue() {
		String selected = this.fetchValue.getItemText(this.fetchValue.getSelectedIndex());
		KVFetchValue[] allKVFetchValue = KVFetchValue.values();
		KVFetchValue kvFetchValue = allKVFetchValue[0];
		for (KVFetchValue fetchValue : allKVFetchValue) {
			if (fetchValue.name().equals(selected)) {
				kvFetchValue = fetchValue;
				break;
			}
		}
		return kvFetchValue;
	}

	/**
	 * To set Fetch Value.
	 */
	public void setFetchValue() {
		this.fetchValue.setVisibleItemCount(1);
		KVFetchValue[] kvFetchValues = KVFetchValue.values();
		for (KVFetchValue kvFetchValue2 : kvFetchValues) {
			this.fetchValue.addItem(kvFetchValue2.name());
		}
	}

	/**
	 * To set Key Pattern Text.
	 * 
	 * @param keyPattern String
	 */
	public void setKeyPatternText(String keyPattern) {
		this.keyPatternText.setText(keyPattern);
	}

	/**
	 * To get Key Pattern.
	 * 
	 * @return String
	 */
	public String getKeyPattern() {
		return keyPatternText.getText();
	}

	/**
	 * To set Value Pattern.
	 * 
	 * @param valuePattern String
	 */
	public void setValuePattern(String valuePattern) {
		this.valuePattern.setText(valuePattern);
	}

	/**
	 * To get Value Pattern.
	 * 
	 * @return String
	 */
	public String getValuePattern() {
		return valuePattern.getText();
	}

	/**
	 * To set Fetch Value.
	 * 
	 * @param kvFetchValue KVFetchValue
	 */
	public void setFetchValue(KVFetchValue kvFetchValue) {
		if (this.fetchValue.getItemCount() == 0) {
			setFetchValue();
		}
		this.fetchValue.setSelectedIndex(findKVIndex(kvFetchValue));
	}

	private int findKVIndex(KVFetchValue kvFetchValue) {
		int index = 0;
		if (kvFetchValue != null) {
			KVFetchValue[] allLocationTypes = KVFetchValue.values();
			List<KVFetchValue> tempList = Arrays.asList(allLocationTypes);
			index = tempList.indexOf(kvFetchValue);
		}
		return index;
	}

	/**
	 * To set widget on next option click.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("nextOptions")
	public void nextOptionsClicked(ClickEvent clickEvent) {
		optionsSlidingPanel.setWidget(secondOptionsPanel);
	}

	/**
	 * To set widget on previous option click.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("previousOptions")
	public void previousOptionsClicked(ClickEvent clickEvent) {
		optionsSlidingPanel.setWidget(initialOptionsPanel);
	}

	/**
	 * To capture Key Clicked.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("captureKey")
	public void captureKeyClicked(ClickEvent clickEvent) {
		if (keyValueCoordinates.isMouseStatus()) {
			ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
					BatchClassManagementMessages.MOUSE_NOT_CLICK_ERROR));
			return;
		}
		keyValueCoordinates.onKeyCapture();
		captureKeyInfo();
	}

	private void captureKeyInfo() {
		keyValueCoordinates.setKeyFinalized(true);
		keyValueCoordinates.createNewOverlay();
		enableDisableButtons();
	}

	/**
	 * To disable all buttons on edit key clicked.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("editKey")
	public void editKeyClicked(ClickEvent clickEvent) {
		lastOperation = EditOperation.KEY;
		disableAllButtons();
	}

	/**
	 * To capture Value Clicked.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("captureValue")
	public void captureValueClicked(ClickEvent clickEvent) {
		if (keyValueCoordinates.isMouseStatus()) {
			ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
					BatchClassManagementMessages.MOUSE_NOT_CLICK_ERROR));
			return;
		}
		if (!keyValueCoordinates.isKeyFinalized()) {
			ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
					BatchClassManagementMessages.KEY_NOT_FINAL_ERROR));
			return;
		}
		captureValueInfo();
	}

	private void captureValueInfo() {
		keyValueCoordinates.finalizeValue();
		keyValueCoordinates.createNewOverlay();
		enableDisableButtons();
	}

	/**
	 * To disable all buttons on edit value clicked.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("editValue")
	public void editValueClicked(ClickEvent clickEvent) {
		lastOperation = EditOperation.VALUE;
		disableAllButtons();
	}

	/**
	 * To clear all values.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("clearButton")
	public void clearButtonClicked(ClickEvent clickEvent) {
		removeAllOverlays();
		keyValueCoordinates.clearFinalizeValues();
		enableDisableButtons();
		presenter.clearValues();
	}

	/**
	 * To perform operations on test advanced KV button clicked.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("testAdvKvButton")
	public void testAdvKvButtonClicked(ClickEvent clickEvent) {
		presenter.onTestAdvancedKvButtonClicked();
	}

	/**
	 * To get sample patterns on Sample Pattern Button Clicked.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("samplePatternButton")
	public void onSamplePatternButtonClicked(ClickEvent clickEvent) {
		presenter.getController().getMainPresenter().getSamplePatterns();
	}

	/**
	 * To perform operations on Key Pattern Validate Button Clicked.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("keyPatternValidateButton")
	public void onKeyPatternValidateButtonClicked(ClickEvent clickEvent) {
		presenter.onKeyPatternValidateButtonClicked();
	}

	/**
	 * To perform operations on value Pattern Validate Button Clicked.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("valuePatternValidateButton")
	public void onValuePatternValidateButtonClicked(ClickEvent clickEvent) {
		presenter.onValuePatternValidateButtonClicked();
	}

	/**
	 * To set values of x & y offset.
	 * 
	 * @param keyCoordinates Coordinates
	 * @param valueCoordinates Coordinates
	 */
	public void setValues(Coordinates keyCoordinates, Coordinates valueCoordinates) {
		double aspectRatio = (double) (originalWidth) / (double) (pageImage.getWidth());
		double lengthOfBox = (valueCoordinates.getX1() - valueCoordinates.getX0()) * aspectRatio;
		double widthOfBox = (valueCoordinates.getY1() - valueCoordinates.getY0()) * aspectRatio;
		int lengthOfBoxInInt = (int) Math.round(lengthOfBox);
		int widthOfBoxInInt = (int) Math.round(widthOfBox);
		length.setValue(String.valueOf(lengthOfBoxInInt));
		width.setValue(String.valueOf(widthOfBoxInInt));
		double xOffset = valueCoordinates.getX0() - keyCoordinates.getX1();
		double yOffset = valueCoordinates.getY0() - keyCoordinates.getY1();

		xOffset *= aspectRatio;
		yOffset *= aspectRatio;

		this.xOffset.setValue(String.valueOf((int) Math.round(xOffset)));
		this.yOffset.setValue(String.valueOf((int) Math.round(yOffset)));
	}

	/**
	 * To get Key Coordinates.
	 * 
	 * @return Coordinates
	 */
	public Coordinates getKeyCoordinates() {
		Coordinates coordinates = new Coordinates();
		Coordinates keyCoordinates = keyValueCoordinates.getKeyCoordinates();
		if (null != keyCoordinates) {
			coordinates.set(keyCoordinates.getX0(), keyCoordinates.getY0(), keyCoordinates.getX1(), keyCoordinates.getY1());
		}
		return coordinates;
	}

	/**
	 * To save the changes on save click.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("saveButton")
	public void onSaveButtonClicked(ClickEvent clickEvent) {
		presenter.onSaveButtonClicked();
	}

	/**
	 * To cancel the chnages on cancel click.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("cancelButton")
	public void onCancelButtonClicked(ClickEvent clickEvent) {
		presenter.onCancelButtonClicked();
	}

	/**
	 * To clear uploaded image.
	 */
	public void clearImageUpload() {
		imageUpload.reset();
		pageImage.setUrl("");
		keyValueCoordinates.clearFinalizeValues();
		resetOperations();
		disableAllButtons();
	}

	/**
	 * To get Key Pattern Text Box.
	 * 
	 * @return TextBox
	 */
	public TextBox getKeyPatternTextBox() {
		return this.keyPatternText;
	}

	/**
	 * To get value Pattern Text Box.
	 * 
	 * @return TextBox
	 */
	public TextBox getValuePatternTextBox() {
		return this.valuePattern;
	}

	/**
	 * To get Multiplier Text Box.
	 * 
	 * @return TextBox
	 */
	public TextBox getMultiplierTextBox() {
		return this.multiplier;
	}

	/**
	 * To toggle the image visibility.
	 * 
	 * @param visibile boolean
	 */
	public void togglePageImageShowHide(boolean visibile) {
		pageImage.setVisible(visibile);
	}

	/**
	 * To create Key Value Overlay.
	 * 
	 * @param keyCoordinates Coordinates
	 * @param valueCoordinates Coordinates
	 */
	public void createKeyValueOverlay(Coordinates keyCoordinates, Coordinates valueCoordinates) {
		keyValueCoordinates.createKeyValueOverlay(keyCoordinates, valueCoordinates);
		enableDisableButtons();
	}

	/**
	 * To get Value Coordinates.
	 * 
	 * @return Coordinates
	 */
	public Coordinates getValueCoordinates() {
		Coordinates coordinates = new Coordinates();
		Coordinates valueCoordinates = keyValueCoordinates.getValueCoordinates();
		if (null != valueCoordinates) {
			coordinates.set(valueCoordinates.getX0(), valueCoordinates.getY0(), valueCoordinates.getX1(), valueCoordinates.getY1());
		}
		return coordinates;
	}

	/**
	 * To get KV Extraction Test Result View.
	 * 
	 * @return KVExtractionTestResultView
	 */
	public KVExtractionTestResultView getKvExtractionTestResultView() {
		return kvExtractionTestResultView;
	}

	/**
	 * Enum for edit operations.
	 * 
	 * @author Ephesoft
	 * @version 1.0
	 */
	private enum EditOperation {
		/**
		 * Key.
		 */
		KEY,
		/**
		 * Value.
		 */
		VALUE,
		/**
		 * None.
		 */
		NONE;
	}

	/**
	 * Enum for editable coordinates.
	 * 
	 * @author Ephesoft
	 * @version 1.0
	 */
	private enum EditableCoordinate {
		/**
		 * X0 coordinate.
		 */
		X0,
		/**
		 * Y0 coordinate.
		 */
		Y0,
		/**
		 * X1 coordinate.
		 */
		X1,
		/**
		 * Y1 coordinate.
		 */
		Y1,
		/**
		 * None.
		 */
		NONE;
	}

	private void enableDisableButtons() {
		String url = pageImage.getUrl();
		if (url == null || url.isEmpty() || !pageImage.isVisible()) {
			disableAllButtons();
		} else {
			captureKey.setEnabled(true);
			captureValue.setEnabled(true);
			clearButton.setEnabled(true);

			boolean keyFinalized = keyValueCoordinates.isKeyFinalized();
			editKey.setEnabled(keyFinalized);
			setEditValueAndTestAdvKvButton();
		}
	}

	private void disableAllButtons() {
		captureKey.setEnabled(false);
		captureValue.setEnabled(false);
		testAdvKvButton.setEnabled(false);
		editKey.setEnabled(false);
		editValue.setEnabled(false);
		clearButton.setEnabled(false);
	}

	private void setEditValueAndTestAdvKvButton() {
		boolean valueFinalized = keyValueCoordinates.isValueFinalized();
		testAdvKvButton.setEnabled(valueFinalized);
		editValue.setEnabled(valueFinalized);
	}

	/**
	 * To set Doc Name.
	 * 
	 * @param docName String
	 */
	public void setDocName(String docName) {
		this.docName.setValue(docName);
	}

	/**
	 * To get Doc Name.
	 * 
	 * @return String
	 */
	public String getDocName() {
		return this.docName.getValue();
	}

	/**
	 * To get Batch Class ID.
	 * 
	 * @return String
	 */
	public String getBatchClassID() {
		return this.batchClassID.getValue();
	}

	/**
	 * To set Batch Class ID.
	 * 
	 * @param batchClassID String
	 */
	public void setBatchClassID(String batchClassID) {
		this.batchClassID.setValue(batchClassID);
	}

	/**
	 * To set KV Page Value.
	 * 
	 * @param kvPageValue KVPageValue
	 */
	public void setKVPageValue(final KVPageValue kvPageValue) {
		if (this.kvPageValue.getItemCount() == 0) {
			setKVPageValues();
		}
		int kvIndex = findKVIndex(kvPageValue);
		this.kvPageValue.setSelectedIndex(kvIndex);
	}

	/**
	 * To set KV Page Values.
	 */
	public void setKVPageValues() {
		this.kvPageValue.setVisibleItemCount(1);
		KVPageValue[] kvPageValues = KVPageValue.values();
		for (KVPageValue kvPageValue : kvPageValues) {
			this.kvPageValue.addItem(kvPageValue.name());
		}
	}

	private int findKVIndex(final KVPageValue kvPageRange) {
		int kvIndex = 0;
		if (kvPageRange != null) {
			KVPageValue[] allLocationTypes = KVPageValue.values();
			List<KVPageValue> tempList = Arrays.asList(allLocationTypes);
			kvIndex = tempList.indexOf(kvPageRange);
		}
		return kvIndex;
	}

	/**
	 * To get KV Page Value.
	 * 
	 * @return KVPageValue
	 */
	public KVPageValue getKVPageValue() {
		KVPageValue selectedKVPageValue = null;
		String selected = this.kvPageValue.getItemText(this.kvPageValue.getSelectedIndex());
		KVPageValue[] allKVPageValue = KVPageValue.values();
		for (KVPageValue kvPageValue : allKVPageValue) {
			if (kvPageValue.name().equals(selected)) {
				selectedKVPageValue = kvPageValue;
			}
		}
		if (selectedKVPageValue == null) {
			selectedKVPageValue = allKVPageValue[0];
		}
		return selectedKVPageValue;
	}

	/**
	 * To set Key Field List.
	 * 
	 * @param fieldTypeNames List<String>
	 */
	public void setKeyFieldList(final List<String> fieldTypeNames) {
		this.keyPatternField.clear();
		if (fieldTypeNames != null && !fieldTypeNames.isEmpty()) {
			for (String fieldTypeName : fieldTypeNames) {
				if (fieldTypeName != null && !fieldTypeName.isEmpty()) {
					this.keyPatternField.addItem(fieldTypeName);
				}
			}
		} else {
			this.keyPatternField.addItem(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.NO_FIELD_EXISTS));
		}
	}

	/**
	 * To check whether is Key Field Selected.
	 * 
	 * @return boolean
	 */
	public boolean isKeyFieldSelected() {
		boolean isFieldSelected = false;
		if (this.keyPatternField != null && this.keyPatternField.getItemCount() != 0) {
			String selectedField = this.keyPatternField.getItemText(this.keyPatternField.getSelectedIndex());
			if (selectedField != null
					&& !selectedField.isEmpty()
					&& !selectedField.equalsIgnoreCase(LocaleDictionary.get().getConstantValue(
							BatchClassManagementConstants.NO_FIELD_EXISTS))) {
				isFieldSelected = true;
			}
		}
		return isFieldSelected;
	}

	/**
	 * To get Key Pattern Field.
	 * 
	 * @return String
	 */
	public String getKeyPatternField() {
		return keyPatternField.getItemText(keyPatternField.getSelectedIndex());
	}

	/**
	 * To check whether is use key existing.
	 * 
	 * @return boolean
	 */
	public boolean isUseExistingKey() {
		return useExistingKey.getValue();
	}

	/**
	 * To set Use Existing Key.
	 * 
	 * @param useExistingKey boolean
	 */
	public void setUseExistingKey(boolean useExistingKey) {
		this.useExistingKey.setValue(useExistingKey);
	}

	/**
	 * To set Key Field.
	 * 
	 * @param keyField String
	 */
	public void setKeyField(final String keyField) {
		int totalFieldCount = keyPatternField.getItemCount();
		for (int index = 0; index < totalFieldCount; index++) {
			if (keyPatternField.getItemText(index).equalsIgnoreCase(keyField)) {
				keyPatternField.setSelectedIndex(index);
				break;
			}
		}
	}

	/**
	 * To set Key Pattern.
	 * 
	 * @param keyPattern String
	 */
	public void setKeyPattern(final String keyPattern) {
		ValueChangeEvent.fire(useExistingKey, isUseExistingKey());
		presenter.setKeyPatternFields();
		if (isUseExistingKey()) {
			setKeyField(keyPattern);
		} else {
			setKeyPatternText(keyPattern);
		}
	}

	/**
	 * To disable all buttons while initializing.
	 */
	public void initialize() {
		disableAllButtons();
		optionsSlidingPanel.setWidget(initialOptionsPanel);
	}

	/**
	 * To enable Save Button.
	 * 
	 * @param isEnable boolean
	 */
	public void setSaveButtonEnable(boolean isEnable) {
		saveButton.setEnabled(isEnable);
	}

	/**
	 * To get Aspect Ratio Width.
	 * 
	 * @return double
	 */
	public double getAspectRatioWidth() {
		return (double) (pageImage.getWidth()) / (double) (originalWidth);
	}

	/**
	 * To get Aspect Ratio Height.
	 * 
	 * @return double
	 */
	public double getAspectRatioHeight() {
		return (double) (pageImage.getHeight()) / (double) (originalHeight);
	}
}
