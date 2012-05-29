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

package com.ephesoft.dcma.gwt.admin.bm.client.view.kvextraction.AdvancedKVExtraction;

import java.util.Arrays;
import java.util.List;

import com.ephesoft.dcma.core.common.KVFetchValue;
import com.ephesoft.dcma.core.common.KVPageValue;
import com.ephesoft.dcma.core.common.LocationType;
import com.ephesoft.dcma.gwt.admin.bm.client.AdminConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.MessageConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementMessages;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.kvextraction.AdvancedKVExtraction.AdvancedKVExtractionPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.view.fieldtype.KVExtractionTestResultView;
import com.ephesoft.dcma.gwt.core.client.View;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.client.ui.RotatableImage;
import com.ephesoft.dcma.gwt.core.client.ui.ScreenMaskUtility;
import com.ephesoft.dcma.gwt.core.client.validator.ValidatableWidget;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialogUtil;
import com.google.gwt.core.client.GWT;
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
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitHandler;

public class AdvancedKVExtractionView extends View<AdvancedKVExtractionPresenter> {

	private static final String EXTENSION_PNG = "png";

	private static final String EXTENSION_CHAR = ".";

	interface Binder extends UiBinder<DockLayoutPanel, AdvancedKVExtractionView> {
	}

	private static final Binder BINDER = GWT.create(Binder.class);

	@UiField
	protected DockLayoutPanel advancedKVLayoutPanel;

	@UiField
	protected Label keyPatternLabel;
	@UiField
	protected Label keyPatternStar;
	@UiField
	protected TextBox keyPattern;

	@UiField
	protected Label valuePatternLabel;
	@UiField
	protected Label valuePatternStar;
	@UiField
	protected TextBox valuePattern;

	@UiField
	protected Label locationLabel;
	@UiField
	protected Label locationStar;
	@UiField
	protected TextBox location;

	@UiField
	protected Label fetchValueLabel;
	@UiField
	protected ListBox fetchValue;
	@UiField
	protected Label fetchValueStar;

	@UiField
	protected Label kvPageValueLabel;
	@UiField
	protected ListBox kvPageValue;
	@UiField
	protected Label kvPageValueStar;

	@UiField
	protected Label lengthLabel;
	@UiField
	protected TextBox length;
	@UiField
	protected Label lengthStar;

	@UiField
	protected Label widthLabel;
	@UiField
	protected TextBox width;
	@UiField
	protected Label widthStar;

	@UiField
	protected Label xOffsetLabel;
	@UiField
	protected TextBox xOffset;
	@UiField
	protected Label xOffsetStar;

	@UiField
	protected Label yOffsetLabel;
	@UiField
	protected TextBox yOffset;
	@UiField
	protected Label yOffsetStar;

	@UiField
	protected Label multiplierLabel;
	@UiField
	protected TextBox multiplier;

	@UiField
	protected Button saveButton;
	@UiField
	protected Button cancelButton;

	@UiField
	protected VerticalPanel oldKVExtractionProperties;

	@UiField
	protected DockLayoutPanel groupingKVExtractionProperties;

	@UiField
	protected VerticalPanel newKVExtractionProperties;

	@UiField
	protected FormPanel imageUpload;

	@UiField
	protected FileUpload importFile;

	@UiField
	protected Hidden batchClassID;

	@UiField
	protected Hidden docName;

	@UiField
	protected RotatableImage pageImage;

	@UiField
	protected Button captureKey;

	@UiField
	protected Button captureValue;

	@UiField
	protected Button clearButton;

	@UiField
	protected Button testAdvKvButton;

	@UiField
	protected HorizontalPanel captureValues;

	private KeyValueCoordinates keyValueCoordinates = null;

	private final KVExtractionTestResultView kvExtractionTestResultView;

	private Integer originalWidth = 0;
	private Integer originalHeight = 0;

	@UiField
	protected ScrollPanel imageScroll;

	@UiField
	protected RotatableImage tempImage;

	private ValidatableWidget<TextBox> validateKeyPatternTextBox;
	private ValidatableWidget<TextBox> validateValuePatternTextBox;
	private ValidatableWidget<TextBox> validateMultiplierTextBox;

	private String fileName;

	public AdvancedKVExtractionView() {
		super();
		initWidget(BINDER.createAndBindUi(this));
		this.kvExtractionTestResultView = new KVExtractionTestResultView();
		saveButton.setText(AdminConstants.OK_BUTTON);
		cancelButton.setText(AdminConstants.CANCEL_BUTTON);

		keyPatternLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.KEY_PATTERN)
				+ AdminConstants.COLON);
		keyPatternLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		keyPatternStar.setText(AdminConstants.STAR);
		keyPatternStar.setStyleName(AdminConstants.FONT_RED_STYLE);

		valuePatternLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.VALUE_PATTERN)
				+ AdminConstants.COLON);
		valuePatternLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		valuePatternStar.setText(AdminConstants.STAR);
		valuePatternStar.setStyleName(AdminConstants.FONT_RED_STYLE);

		locationLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.LOCATION) + AdminConstants.COLON);
		locationLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		locationStar.setText(AdminConstants.STAR);
		locationStar.setStyleName(AdminConstants.FONT_RED_STYLE);

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
		oldKVExtractionProperties.setSpacing(6);
		oldKVExtractionProperties.addStyleName("background_group");
		newKVExtractionProperties.setSpacing(6);
		newKVExtractionProperties.addStyleName("background_group");

		groupingKVExtractionProperties.addStyleName("right_border");

		imageUpload.setEncoding(FormPanel.ENCODING_MULTIPART);
		imageUpload.setMethod(FormPanel.METHOD_POST);
		imageUpload.setAction("dcma-gwt-admin/uploadImageFile");

		captureKey.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.CAPTURE_KEY_BUTTON));
		captureValue.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.CAPTURE_VALUE_BUTTON));
		clearButton.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.CLEAR_BUTTON));
		testAdvKvButton.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.TEST_ADV_KV_LABEL));

		validateKeyPatternTextBox = new ValidatableWidget<TextBox>(keyPattern);
		validateKeyPatternTextBox.getWidget().addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				validateKeyPatternTextBox.toggleValidDateBox();
			}
		});

		validateValuePatternTextBox = new ValidatableWidget<TextBox>(valuePattern);
		validateValuePatternTextBox.getWidget().addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				validateValuePatternTextBox.toggleValidDateBox();
			}
		});

		validateMultiplierTextBox = new ValidatableWidget<TextBox>(multiplier);
		validateMultiplierTextBox.getWidget().addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> arg0) {
				validateMultiplierTextBox.toggleValidDateBox();

			}
		});

		location.setReadOnly(true);
		length.setReadOnly(true);
		width.setReadOnly(true);
		xOffset.setReadOnly(true);
		yOffset.setReadOnly(true);

		captureValues.setSpacing(3);

		tempImage.setVisible(false);

		keyValueCoordinates = new KeyValueCoordinates(this);
		tempImage.addLoadHandler(new LoadHandler() {

			@Override
			public void onLoad(LoadEvent arg0) {
				pageImage.setVisible(true);
				ScreenMaskUtility.unmaskScreen();
				DOM.setElementAttribute(pageImage.getElement(), "src", (DOM.getElementAttribute(tempImage.getElement(), "src")));
				pageImage.setUrl(tempImage.getUrl());
				if (tempImage.getHeight() != 0) {
					originalWidth = tempImage.getWidth();
					originalHeight = tempImage.getHeight();
				} else {
					originalWidth = pageImage.getWidth();
					originalHeight = pageImage.getHeight();
				}
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
				setFileName(fileName);
				String pngFileName = fileName.substring(0, fileName.lastIndexOf(EXTENSION_CHAR) + 1) + EXTENSION_PNG;
				presenter.setEditAdvancedKV(false);
				presenter.getPageImageUrl(batchClassID.getValue(), docName.getValue(), pngFileName);
			}
		});

		pageImage.addStyleName("pointer");

		pageImage.addMouseDownHandler(new MouseDownHandler() {

			@Override
			public void onMouseDown(MouseDownEvent paramMouseDownEvent) {
				keyValueCoordinates.mouseDownat(paramMouseDownEvent.getX(), paramMouseDownEvent.getY());
			}
		});

		pageImage.addMouseMoveHandler(new MouseMoveHandler() {

			@Override
			public void onMouseMove(MouseMoveEvent paramMouseMoveEvent) {
				keyValueCoordinates.mouseMoveat(paramMouseMoveEvent.getX(), paramMouseMoveEvent.getY());

			}
		});
		imageScroll.addScrollHandler(new ScrollHandler() {

			@Override
			public void onScroll(ScrollEvent paramScrollEvent) {
				keyValueCoordinates.createNewOverlay();

			}
		});

		pageImage.setVisible(false);

	}

	private void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void createOverlay(double x0, double x1, double y0, double y1, double zoomFactor) {
		createOverlay(x0, x1, y0, y1, zoomFactor, false, false);
	}

	public void createOverlay(double x0, double x1, double y0, double y1, double zoomFactor, boolean forKey, boolean forValue) {
		if (x0 == 0 && x1 == 0 && y0 == 0 && y1 == 0) {
			return;
		}

		double xFactor = 0.263;
		double yFactor = 0.25;
		double yScrollingAdder = 75;

		x0 = (double) (x0) + (double) (xFactor * getViewPortWidth());
		x1 = (double) (x1) + (double) (xFactor * getViewPortWidth());
		y0 = (double) (y0) + (double) (yFactor * getViewPortHeight());
		y1 = (double) (y1) + (double) (yFactor * getViewPortHeight());
		int yScrollPosition = imageScroll.getScrollPosition();
		int xScrollPosition = imageScroll.getHorizontalScrollPosition();
		if (yScrollPosition > (y0 - (yFactor * getViewPortHeight())) || xScrollPosition > (x0 - (xFactor * getViewPortWidth()))
				|| x1 - xScrollPosition > getViewPortWidth() || y1 - yScrollPosition + yScrollingAdder > getViewPortHeight()) {
			// removeOverlay();
			return;
		}
		if (xScrollPosition == 0 && yScrollPosition == 0 && zoomFactor == 1) {
			if (forKey) {
				doOverlayForKey(x0, x1, y0, y1, zoomFactor);
			} else if (forValue) {
				doOverlayForValue(x0, x1, y0, y1, zoomFactor);
			} else {
				doOverlay(x0, x1, y0, y1, zoomFactor);
			}
		} else if (xScrollPosition <= x1 && yScrollPosition <= y1) {
			x0 -= xScrollPosition;
			x1 -= xScrollPosition;
			y0 -= yScrollPosition;
			y1 -= yScrollPosition;
			if (forKey) {
				doOverlayForKey(x0, x1, y0, y1, zoomFactor);
			} else if (forValue) {
				doOverlayForValue(x0, x1, y0, y1, zoomFactor);
			} else {
				doOverlay(x0, x1, y0, y1, zoomFactor);
			}
		}
		// doOverlay(x0, x1, y0, y1, zoomFactor);
	}

	public native void doOverlay(double x0, double x1, double y0, double y1, double zoomFactor) /*-{
																								return $wnd.doOverlay(x0, x1, y0, y1, zoomFactor);
																								}-*/;

	public native void doOverlayForKey(double x0, double x1, double y0, double y1, double zoomFactor) /*-{
																										return $wnd.doOverlayForKey(x0, x1, y0, y1, zoomFactor);
																										}-*/;

	public native void doOverlayForValue(double x0, double x1, double y0, double y1, double zoomFactor) /*-{
																										return $wnd.doOverlayForValue(x0, x1, y0, y1, zoomFactor);
																										}-*/;

	public native void removeOverlay() /*-{
										return $wnd.removeOverlay();
										}-*/;

	private void loadImage() {
		removeAllOverlays();
		pageImage.setVisible(true);
		int screenWidth = getViewPortWidth();
		Integer imageWidth = screenWidth * 70 / 100;
		Integer imageHeight = Integer.valueOf(0);

		if (originalWidth != 0) {
			imageHeight = originalHeight * imageWidth / originalWidth;
		}
		if (imageHeight == 0) {
			int screenHeight = getViewPortHeight();
			imageHeight = screenHeight * 70 / 100;
		}
		pageImage.setWidth(imageWidth.toString() + "px");
		pageImage.setHeight(imageHeight.toString() + "px");
		// presenter.setEditAdvancedKV(false);
		if (presenter.isEditAdvancedKV()) {
			presenter.createKeyValueOverlay();
		}
		ScreenMaskUtility.unmaskScreen();
	}

	public void removeAllOverlays() {
		removeOverlay();
		keyValueCoordinates.initialize();
	}

	private native int getViewPortWidth() /*-{
											return $wnd.getViewPortWidth();
											}-*/;

	private native int getViewPortHeightForFirefox() /*-{
														return $wnd.getViewPortHeight();
														}-*/;

	private int getViewPortHeight() {
		String currentBrowser = getUserAgent();
		if (currentBrowser != null && currentBrowser.length() > 0 && currentBrowser.contains("msie")) {
			return getViewPortHeightForIE();
		} else {
			return getViewPortHeightForFirefox();
		}
	}

	public static native String getUserAgent() /*-{
												return navigator.userAgent.toLowerCase();
												}-*/;

	private native int getViewPortHeightForIE() /*-{
												return $wnd.getViewPortHeightForIE();
												}-*/;

	public void setPageImageUrl(final String pageImageUrl) {
		DOM.setElementAttribute(this.tempImage.getElement(), "src", pageImageUrl);
		this.tempImage.setUrl(pageImageUrl);
	}

	public String getPageImageUrl() {
		return this.tempImage.getUrl();
	}

	public String getMultiplier() {
		return multiplier.getText();
	}

	public void setMultiplier(String multiplier) {
		this.multiplier.setText(multiplier);
	}

	public String getyOffset() {
		return yOffset.getText();
	}

	public void setyOffset(String yOffset) {
		this.yOffset.setText(yOffset);
	}

	public String getxOffset() {
		return xOffset.getText();
	}

	public void setxOffset(String xOffset) {
		this.xOffset.setText(xOffset);
	}

	public String getWidth() {
		return width.getText();
	}

	public void setWidthTextBox(String width) {
		this.width.setText(width);
	}

	public String getLength() {
		return length.getText();
	}

	public void setLengthOfRect(String length) {
		this.length.setText(length);
	}

	public void setWidthOfRect(String width) {
		this.width.setText(width);
	}

	public ValidatableWidget<TextBox> getValidateKeyPatternTextBox() {
		return validateKeyPatternTextBox;
	}

	public ValidatableWidget<TextBox> getValidateValuePatternTextBox() {
		return validateValuePatternTextBox;
	}

	public void setValidateMultiplierTextBox(ValidatableWidget<TextBox> validateMultiplierTextBox) {
		this.validateMultiplierTextBox = validateMultiplierTextBox;
	}

	public ValidatableWidget<TextBox> getValidateMultiplierTextBox() {
		return validateMultiplierTextBox;
	}

	public void setValidateKeyPatternTextBox(ValidatableWidget<TextBox> validateKeyPatternTextBox) {
		this.validateKeyPatternTextBox = validateKeyPatternTextBox;
	}

	public void setValidateValuePatternTextBox(ValidatableWidget<TextBox> validateValuePatternTextBox) {
		this.validateValuePatternTextBox = validateValuePatternTextBox;
	}

	public KVFetchValue getFetchValue() {
		String selected = this.fetchValue.getItemText(this.fetchValue.getSelectedIndex());
		KVFetchValue[] allKVFetchValue = KVFetchValue.values();
		for (KVFetchValue kvFetchValue : allKVFetchValue) {
			if (kvFetchValue.name().equals(selected)) {
				return kvFetchValue;
			}
		}
		return allKVFetchValue[0];
	}

	public void setFetchValue() {
		this.fetchValue.setVisibleItemCount(1);
		KVFetchValue[] kvFetchValues = KVFetchValue.values();
		for (KVFetchValue kvFetchValue2 : kvFetchValues) {
			this.fetchValue.addItem(kvFetchValue2.name());
		}
	}

	// public LocationType getLocation() {
	// String selected = this.location.getItemText(this.location.getSelectedIndex());
	// LocationType[] allLocationTypes = LocationType.values();
	// for (LocationType locationType : allLocationTypes) {
	// if (locationType.name().equals(selected))
	// return locationType;
	// }
	// return allLocationTypes[0];
	// }
	public LocationType getLocation() {
		String selected = this.location.getValue();
		LocationType[] allLocationTypes = LocationType.values();
		for (LocationType locationType : allLocationTypes) {
			if (locationType.name().equals(selected)) {
				return locationType;
			}
		}
		return allLocationTypes[0];
	}

	// public void setLocation() {
	// this.location.setVisibleItemCount(1);
	// LocationType[] allLocationTypes = LocationType.values();
	// for (LocationType locationType2 : allLocationTypes) {
	// this.location.addItem(locationType2.name());
	// }
	// }

	public void setKeyPattern(String keyPattern) {
		this.keyPattern.setText(keyPattern);
	}

	public String getKeyPattern() {
		return keyPattern.getText();
	}

	public void setValuePattern(String valuePattern) {
		this.valuePattern.setText(valuePattern);
	}

	public String getValuePattern() {
		return valuePattern.getText();
	}

	// public void setLocation(LocationType locationType) {
	// if (this.location.getItemCount() == 0)
	// setLocation();
	// this.location.setSelectedIndex(findIndex(locationType));
	// }
	public void setLocation(LocationType locationType) {
		if (locationType == null || locationType.name().isEmpty()) {
			this.location.setValue(LocationType.TOP.name());
		} else {
			this.location.setValue(locationType.name());
		}
	}

	public void setFetchValue(KVFetchValue kvFetchValue) {
		if (this.fetchValue.getItemCount() == 0) {
			setFetchValue();
		}
		this.fetchValue.setSelectedIndex(findKVIndex(kvFetchValue));
	}

	private int findKVIndex(KVFetchValue kvFetchValue) {
		if (kvFetchValue == null) {
			return 0;
		}
		KVFetchValue[] allLocationTypes = KVFetchValue.values();
		List<KVFetchValue> tempList = Arrays.asList(allLocationTypes);
		return tempList.indexOf(kvFetchValue);
	}

	// private int findIndex(LocationType locationType) {
	// if (locationType == null)
	// return 0;
	// LocationType[] allLocationTypes = LocationType.values();
	// List<LocationType> tempList = Arrays.asList(allLocationTypes);
	// return tempList.indexOf(locationType);
	// }

	@UiHandler("captureKey")
	public void captureKeyClicked(ClickEvent clickEvent) {
		if (keyValueCoordinates.isMouseStatus()) {
			ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
					BatchClassManagementMessages.MOUSE_NOT_CLICK_ERROR));
			return;
		}
		keyValueCoordinates.finalizeKey();
		keyValueCoordinates.createNewOverlay();
	}

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
		keyValueCoordinates.finalizeValue();
		keyValueCoordinates.createNewOverlay();
	}

	@UiHandler("clearButton")
	public void clearButtonClicked(ClickEvent clickEvent) {
		removeAllOverlays();
		keyValueCoordinates.clearFinalizeValues();
		presenter.clearValues();
	}

	@UiHandler("testAdvKvButton")
	public void testAdvKvButtonClicked(ClickEvent clickEvent) {
		presenter.onTestAdvancedKvButtonClicked();
	}

	public void findLocation(Coordinates keyCoordinates, Coordinates valueCoordinates) {

		if (keyCoordinates.getY0() >= valueCoordinates.getY1()) {
			// it is in top of key
			if (keyCoordinates.getX0() >= valueCoordinates.getX1()) {
				// it is in top left
				setValues(LocationType.TOP_LEFT, keyCoordinates, valueCoordinates);
			} else if (keyCoordinates.getX1() <= valueCoordinates.getX0()) {
				// it is in top right
				setValues(LocationType.TOP_RIGHT, keyCoordinates, valueCoordinates);
			} else if (keyCoordinates.getX0() <= valueCoordinates.getX0() && keyCoordinates.getX1() >= valueCoordinates.getX1()) {
				// it is in top
				setValues(LocationType.TOP, keyCoordinates, valueCoordinates);
			} else if (keyCoordinates.getX0() >= valueCoordinates.getX0() && keyCoordinates.getX1() >= valueCoordinates.getX1()) {
				// it is in top left
				setValues(LocationType.TOP_LEFT, keyCoordinates, valueCoordinates);
			} else if (keyCoordinates.getX0() <= valueCoordinates.getX0() && keyCoordinates.getX1() <= valueCoordinates.getX1()) {
				// it is top right
				setValues(LocationType.TOP_RIGHT, keyCoordinates, valueCoordinates);
			} else {
				// it spans all three quadrants
				double x0diff = valueCoordinates.getX0() - keyCoordinates.getX0();
				double x1diff = valueCoordinates.getX1() - keyCoordinates.getX1();
				if (x0diff < x1diff) {
					setValues(LocationType.TOP_RIGHT, keyCoordinates, valueCoordinates);
				} else {
					setValues(LocationType.TOP_LEFT, keyCoordinates, valueCoordinates);
				}
			}
		} else if (keyCoordinates.getY1() <= valueCoordinates.getY0()) {
			// it is in bottom of key
			if (keyCoordinates.getX0() >= valueCoordinates.getX1()) {
				// it is in bottom left
				setValues(LocationType.BOTTOM_LEFT, keyCoordinates, valueCoordinates);
			} else if (keyCoordinates.getX1() <= valueCoordinates.getX0()) {
				// it is in bottom right
				setValues(LocationType.BOTTOM_RIGHT, keyCoordinates, valueCoordinates);
			} else if (keyCoordinates.getX0() <= valueCoordinates.getX0() && keyCoordinates.getX1() >= valueCoordinates.getX1()) {
				// it is in bottom
				setValues(LocationType.BOTTOM, keyCoordinates, valueCoordinates);
			} else if (keyCoordinates.getX0() >= valueCoordinates.getX0() && keyCoordinates.getX1() >= valueCoordinates.getX1()) {
				// it is in bottom left
				setValues(LocationType.BOTTOM_LEFT, keyCoordinates, valueCoordinates);
			} else if (keyCoordinates.getX0() <= valueCoordinates.getX0() && keyCoordinates.getX1() <= valueCoordinates.getX1()) {
				// it is bottom right
				setValues(LocationType.BOTTOM_RIGHT, keyCoordinates, valueCoordinates);
			} else {
				// it spans all three quadrants
				double x0diff = valueCoordinates.getX0() - keyCoordinates.getX0();
				double x1diff = valueCoordinates.getX1() - keyCoordinates.getX1();
				if (x0diff < x1diff) {
					setValues(LocationType.BOTTOM_RIGHT, keyCoordinates, valueCoordinates);
				} else {
					setValues(LocationType.BOTTOM_LEFT, keyCoordinates, valueCoordinates);
				}
			}
		} else if (keyCoordinates.getY0() >= valueCoordinates.getY0() && keyCoordinates.getY1() >= valueCoordinates.getY1()) {
			// it is in line with key
			if (keyCoordinates.getX0() >= valueCoordinates.getX1()) {
				// it is in left
				setValues(LocationType.LEFT, keyCoordinates, valueCoordinates);
			} else if (keyCoordinates.getX1() <= valueCoordinates.getX0()) {
				// it is in right
				setValues(LocationType.RIGHT, keyCoordinates, valueCoordinates);
			} else if (keyCoordinates.getX0() <= valueCoordinates.getX0() && keyCoordinates.getX1() >= valueCoordinates.getX1()) {
				// it overlaps with key
				errorInKey();
			} else if (keyCoordinates.getX0() >= valueCoordinates.getX0() && keyCoordinates.getX1() >= valueCoordinates.getX1()) {
				// it is in left
				setValues(LocationType.LEFT, keyCoordinates, valueCoordinates);
			} else if (keyCoordinates.getX0() <= valueCoordinates.getX0() && keyCoordinates.getX1() <= valueCoordinates.getX1()) {
				// it is right
				setValues(LocationType.RIGHT, keyCoordinates, valueCoordinates);
			} else {
				// it spans all three quadrants
				double x0diff = valueCoordinates.getX0() - keyCoordinates.getX0();
				double x1diff = valueCoordinates.getX1() - keyCoordinates.getX1();
				if (x0diff < x1diff) {
					setValues(LocationType.TOP_RIGHT, keyCoordinates, valueCoordinates);
				} else {
					setValues(LocationType.TOP_LEFT, keyCoordinates, valueCoordinates);
				}
			}
		} else {
			// it spans more than two quadrants
			double y0diff = valueCoordinates.getY0() - keyCoordinates.getY0();
			double y1diff = valueCoordinates.getY1() - keyCoordinates.getY1();
			if (y0diff < y1diff) {
				// it is in bottom
				double x0diff = valueCoordinates.getX0() - keyCoordinates.getX0();
				double x1diff = valueCoordinates.getX1() - keyCoordinates.getX1();
				if (x0diff < x1diff) {
					setValues(LocationType.BOTTOM_RIGHT, keyCoordinates, valueCoordinates);
				} else {
					setValues(LocationType.BOTTOM_LEFT, keyCoordinates, valueCoordinates);
				}
			} else {
				// it is in top
				double x0diff = valueCoordinates.getX0() - keyCoordinates.getX0();
				double x1diff = valueCoordinates.getX1() - keyCoordinates.getX1();
				if (x0diff < x1diff) {
					setValues(LocationType.TOP_RIGHT, keyCoordinates, valueCoordinates);
				} else {
					setValues(LocationType.TOP_LEFT, keyCoordinates, valueCoordinates);
				}
			}
		}
	}

	private void setValues(LocationType locationType, Coordinates keyCoordinates, Coordinates valueCoordinates) {
		double aspectRatio = (double) (originalWidth) / (double) (pageImage.getWidth());
		double lengthOfBox = (valueCoordinates.getX1() - valueCoordinates.getX0()) * aspectRatio;
		double widthOfBox = (valueCoordinates.getY1() - valueCoordinates.getY0()) * aspectRatio;
		int lengthOfBoxInInt = (int) Math.round(lengthOfBox);
		int widthOfBoxInInt = (int) Math.round(widthOfBox);
		length.setValue(String.valueOf(lengthOfBoxInInt));
		width.setValue(String.valueOf(widthOfBoxInInt));
		setLocation(locationType);
		double xOffset = 0;
		double yOffset = 0;

		switch (locationType) {
			case TOP_LEFT:
				xOffset = keyCoordinates.getX0() - valueCoordinates.getX1();
				yOffset = keyCoordinates.getY0() - valueCoordinates.getY1();
				break;
			case TOP_RIGHT:
				xOffset = valueCoordinates.getX0() - keyCoordinates.getX1();
				yOffset = keyCoordinates.getY0() - valueCoordinates.getY1();
				break;
			case TOP:
				xOffset = 0;
				yOffset = keyCoordinates.getY0() - valueCoordinates.getY1();
				break;
			case LEFT:
				yOffset = 0;
				xOffset = keyCoordinates.getX0() - valueCoordinates.getX1();
				break;
			case RIGHT:
				yOffset = 0;
				xOffset = valueCoordinates.getX0() - keyCoordinates.getX1();
				break;
			case BOTTOM_LEFT:
				xOffset = keyCoordinates.getX0() - valueCoordinates.getX1();
				yOffset = valueCoordinates.getY0() - keyCoordinates.getY1();
				break;
			case BOTTOM_RIGHT:
				xOffset = valueCoordinates.getX0() - keyCoordinates.getX1();
				yOffset = valueCoordinates.getY0() - keyCoordinates.getY1();
				break;
			case BOTTOM:
				xOffset = 0;
				yOffset = valueCoordinates.getY0() - keyCoordinates.getY1();
				break;

			default:
				errorInFindingLocation();
				break;
		}
		xOffset *= aspectRatio;
		int xOffsetInInt = (int) Math.round(xOffset);
		yOffset *= aspectRatio;
		int yOffsetInInt = (int) Math.round(yOffset);
		this.xOffset.setValue(String.valueOf(xOffsetInInt));
		this.yOffset.setValue(String.valueOf(yOffsetInInt));
	}

	public Coordinates getKeyCoordinates() {
		Coordinates coordinates = new Coordinates();
		Coordinates keyCoordinates = keyValueCoordinates.getKeyCoordinates();
		if (null != keyCoordinates) {
			coordinates.set(keyCoordinates.getX0(), keyCoordinates.getY0(), keyCoordinates.getX1(), keyCoordinates.getY1());
		}
		return coordinates;
	}

	private void errorInFindingLocation() {
		ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
				BatchClassManagementMessages.UNABLE_TO_DETERMINE_LOCATION));

	}

	private void errorInKey() {
		ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
				BatchClassManagementMessages.VALUE_OVERLAPS_WITH_KEY));

	}

	@UiHandler("saveButton")
	public void onSaveButtonClicked(ClickEvent clickEvent) {
		presenter.onSaveButtonClicked();
	}

	@UiHandler("cancelButton")
	public void onCancelButtonClicked(ClickEvent clickEvent) {
		presenter.onCancelButtonClicked();
	}

	public void clearImageUpload() {
		imageUpload.reset();
		pageImage.setUrl("");
		keyValueCoordinates.clearFinalizeValues();
	}

	public TextBox getKeyPatternTextBox() {
		return this.keyPattern;
	}

	public TextBox getValuePatternTextBox() {
		return this.valuePattern;
	}

	public TextBox getMultiplierTextBox() {
		return this.multiplier;
	}

	public void togglePageImageShowHide(boolean visibile) {
		pageImage.setVisible(visibile);
	}

	public String getFileName() {
		return fileName;
	}

	public void createKeyValueOverlay(Coordinates keyCoordinates, Coordinates valueCoordinates) {
		keyValueCoordinates.createKeyValueOverlay(keyCoordinates, valueCoordinates);
	}

	public Coordinates getValueCoordinates() {
		Coordinates coordinates = new Coordinates();
		Coordinates valueCoordinates = keyValueCoordinates.getValueCoordinates();
		if (null != valueCoordinates) {
			coordinates.set(valueCoordinates.getX0(), valueCoordinates.getY0(), valueCoordinates.getX1(), valueCoordinates.getY1());
		}
		return coordinates;
	}

	public KVExtractionTestResultView getKvExtractionTestResultView() {
		return kvExtractionTestResultView;
	}

	public void setDocName(String docName) {
		this.docName.setValue(docName);
	}

	public String getDocName() {
		return this.docName.getValue();
	}

	public String getBatchClassID() {
		return this.batchClassID.getValue();
	}

	public void setBatchClassID(String batchClassID) {
		this.batchClassID.setValue(batchClassID);
	}

	public void setKVPageValue(final KVPageValue kvPageValue) {
		if (this.kvPageValue.getItemCount() == 0) {
			setKVPageValues();
		}
		int kvIndex = findKVIndex(kvPageValue);
		this.kvPageValue.setSelectedIndex(kvIndex);
	}

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

}
