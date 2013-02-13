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

package com.ephesoft.dcma.gwt.admin.bm.client.view.tablecolumninfo.advancedtableextraction;

import java.util.List;

import com.ephesoft.dcma.gwt.admin.bm.client.AdminConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.MessageConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementMessages;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.tablecolumninfo.advancedtableextraction.AdvancedTableExtractionPresenter;
import com.ephesoft.dcma.gwt.core.client.View;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.client.ui.RotatableImage;
import com.ephesoft.dcma.gwt.core.client.ui.ScreenMaskUtility;
import com.ephesoft.dcma.gwt.core.client.view.ContextMenuPanel;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialogUtil;
import com.ephesoft.dcma.gwt.core.shared.TableColumnInfoDTO;
import com.ephesoft.dcma.gwt.core.shared.TableInfoDTO;
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
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitHandler;

/**
 * This class provides functionality to edit advanced table extraction.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.gwt.core.client.View
 */
public class AdvancedTableExtractionView extends View<AdvancedTableExtractionPresenter> {

	/**
	 * UI binder.
	 */
	interface Binder extends UiBinder<DockLayoutPanel, AdvancedTableExtractionView> {
	}

	/**
	 * Instantiates a class via deferred binding.
	 */
	private static final Binder BINDER = GWT.create(Binder.class);

	/**
	 * advancedTELayoutPanel DockLayoutPanel.
	 */
	@UiField
	protected DockLayoutPanel advancedTELayoutPanel;

	/**
	 * tableColumnLabel Label.
	 */
	@UiField
	protected Label tableColumnLabel;

	/**
	 * tableColumnInfoList ListBox.
	 */
	@UiField
	protected ListBox tableColumnInfoList;

	/**
	 * colStartCoordLabel Label.
	 */
	@UiField
	protected Label colStartCoordLabel;

	/**
	 * colStartCoord TextBox.
	 */
	@UiField
	protected TextBox colStartCoord;

	/**
	 * colStartCoordStar Label.
	 */
	@UiField
	protected Label colStartCoordStar;

	/**
	 * colEndCoordLabel Label.
	 */
	@UiField
	protected Label colEndCoordLabel;

	/**
	 * colEndCoord TextBox.
	 */
	@UiField
	protected TextBox colEndCoord;

	/**
	 * colEndCoordStar Label.
	 */
	@UiField
	protected Label colEndCoordStar;

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
	 * tableColumnsInfoProperties VerticalPanel.
	 */
	@UiField
	protected VerticalPanel tableColumnsInfoProperties;

	/**
	 * groupingTabColInfoProperties DockLayoutPanel.
	 */
	@UiField
	protected DockLayoutPanel groupingTabColInfoProperties;

	/**
	 * dynamicTabColInfoProperties VerticalPanel.
	 */
	@UiField
	protected VerticalPanel dynamicTabColInfoProperties;

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
	 * isAdvancedTableInfo Hidden.
	 */
	@UiField
	protected Hidden isAdvancedTableInfo;

	/**
	 * pageImage RotatableImage.
	 */
	@UiField
	protected RotatableImage pageImage;

	/**
	 * tableColumnCoordinates TableColumnCoordinates.
	 */
	private final TableColumnCoordinates tableColumnCoordinates;

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
	 * dimensionImage RotatableImage.
	 */
	protected RotatableImage dimensionImage = new RotatableImage();

	/**
	 * clearButton Button.
	 */
	@UiField
	protected Button clearButton;

	/**
	 * clearAllButton Button.
	 */
	@UiField
	protected Button clearAllButton;

	/**
	 * contextMenu ContextMenuPanel.
	 */
	private final ContextMenuPanel contextMenu = new ContextMenuPanel();

	/**
	 * fileName String.
	 */
	private String fileName;

	/**
	 * Constructor.
	 */
	public AdvancedTableExtractionView() {
		super();
		initWidget(BINDER.createAndBindUi(this));
		colStartCoord.addStyleName(AdminConstants.WIDTH_COORDINATE_LABELS);
		colEndCoord.addStyleName(AdminConstants.WIDTH_COORDINATE_LABELS);
		saveButton.setText(AdminConstants.OK_BUTTON);
		cancelButton.setText(AdminConstants.CANCEL_BUTTON);
		saveButton.setHeight(AdminConstants.BUTTON_HEIGHT);
		cancelButton.setHeight(AdminConstants.BUTTON_HEIGHT);
		clearButton.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.CLEAR_BUTTON));
		clearAllButton.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.CLEAR_ALL_BUTTON));

		colStartCoordLabel.setText(LocaleDictionary.get()
				.getConstantValue(BatchClassManagementConstants.COLUMN_START_COORDINATE_LABEL)
				+ AdminConstants.COLON);
		colStartCoordLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		colStartCoordStar.setText(AdminConstants.STAR);
		colStartCoordStar.setStyleName(AdminConstants.FONT_RED_STYLE);

		colEndCoordLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.COLUMN_END_COORDINATE_LABEL)
				+ AdminConstants.COLON);
		colEndCoordLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		colEndCoordStar.setText(AdminConstants.STAR);
		colEndCoordStar.setStyleName(AdminConstants.FONT_RED_STYLE);

		tableColumnLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.CHOOSE_TABLE_COLUMN)
				+ AdminConstants.COLON);
		tableColumnLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);

		advancedTELayoutPanel.setStyleName(AdminConstants.BORDER_STYLE);
		advancedTELayoutPanel.setWidth("100%");
		tableColumnsInfoProperties.setSpacing(BatchClassManagementConstants.SIX);
		tableColumnsInfoProperties.addStyleName("background_group");
		dynamicTabColInfoProperties.setSpacing(BatchClassManagementConstants.SIX);
		dynamicTabColInfoProperties.addStyleName("background_group");

		groupingTabColInfoProperties.addStyleName("right_border");
		imageUpload.addStyleName(AdminConstants.BUTTON_PADDING_STYLE);
		imageUpload.setEncoding(FormPanel.ENCODING_MULTIPART);
		imageUpload.setMethod(FormPanel.METHOD_POST);
		imageUpload.setAction("dcma-gwt-admin/uploadImageFile");

		colStartCoord.setReadOnly(true);
		colEndCoord.setReadOnly(true);

		tempImage.setVisible(false);
		dimensionImage.setVisible(false);
		isAdvancedTableInfo.setValue(String.valueOf(Boolean.TRUE));

		tableColumnCoordinates = new TableColumnCoordinates(this);

		tableColumnInfoList.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent arg0) {
				presenter.setSelectedTableColumnInfoDTO(tableColumnInfoList.getValue(tableColumnInfoList.getSelectedIndex()));
			}
		});

		dimensionImage.addLoadHandler(new LoadHandler() {

			@Override
			public void onLoad(LoadEvent arg0) {
				// TODO Auto-generated method stub
			}
		});

		tempImage.addLoadHandler(new LoadHandler() {

			@Override
			public void onLoad(LoadEvent arg0) {
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
				ScreenMaskUtility.maskScreen();
				String fileName = importFile.getFilename();
				if (!fileName.toLowerCase().endsWith(AdminConstants.EXTENSION_TIFF)
						&& !fileName.toLowerCase().endsWith(AdminConstants.EXTENSION_TIF)) {
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
					ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
							BatchClassManagementMessages.ERROR_UPLOAD_IMAGE));
					return;
				}
				String fileName = importFile.getFilename();
				String fileSeperatorParam = AdminConstants.FILE_SEPARATOR_PARAM;
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
				String pngFileName = fileName.substring(0, fileName.lastIndexOf(AdminConstants.EXTENSION_CHAR) + 1)
						+ AdminConstants.EXTENSION_PNG;
				presenter.getPageImageUrl(batchClassID.getValue(), docName.getValue(), pngFileName);
			}
		});

		pageImage.addStyleName("pointer");

		pageImage.addMouseDownHandler(new MouseDownHandler() {

			@Override
			public void onMouseDown(MouseDownEvent paramMouseDownEvent) {
				int xVal = paramMouseDownEvent.getX();
				int yVal = paramMouseDownEvent.getY();
				int nativeButton = paramMouseDownEvent.getNativeButton();
				if (NativeEvent.BUTTON_LEFT == nativeButton) {
					tableColumnCoordinates.mouseDownat(xVal, yVal);
				}
			}
		});

		pageImage.addMouseMoveHandler(new MouseMoveHandler() {

			@Override
			public void onMouseMove(MouseMoveEvent paramMouseMoveEvent) {
				int xVal = paramMouseMoveEvent.getX();
				int yVal = paramMouseMoveEvent.getY();
				tableColumnCoordinates.mouseMoveat(xVal, yVal);
			}

		});

		imageScroll.addScrollHandler(new ScrollHandler() {

			@Override
			public void onScroll(ScrollEvent paramScrollEvent) {
				tableColumnCoordinates.createNewOverlay();
				contextMenu.hide();
			}
		});

		pageImage.setVisible(false);

	}

	/**
	 * To clear coordinates for selected column.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("clearButton")
	public void onClearButtonClicked(ClickEvent clickEvent) {
		presenter.clearCoordinatesForSelectedColumn();
	}

	/**
	 * To clear coordinates for selected column.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("clearAllButton")
	public void onClearAllButtonClicked(ClickEvent clickEvent) {
		presenter.clearCoordinatesForAllColumns();
	}

	/**
	 * To create Overlay.
	 * 
	 * @param x0Coordinate double
	 * @param x1Coordinate double
	 * @param y0Coordinate double
	 * @param y1Coordinate double
	 * @param zoomFactor double
	 */
	public void createOverlay(double x0Coordinate, double x1Coordinate, double y0Coordinate, double y1Coordinate, double zoomFactor) {
		double localX0Coordinate = x0Coordinate;
		double localX1Coordinate = x1Coordinate;
		double localY0Coordinate = y0Coordinate;
		double localY1Coordinate = y1Coordinate;

		double aspectWidthRatio = (double) (pageImage.getWidth()) / (double) (originalWidth);
		double aspectHeightRatio = (double) (pageImage.getHeight()) / (double) (originalHeight);
		localX0Coordinate = localX0Coordinate * aspectWidthRatio;
		localX1Coordinate = localX1Coordinate * aspectWidthRatio;
		localY0Coordinate = localY0Coordinate * aspectHeightRatio;
		localY1Coordinate = localY1Coordinate * aspectHeightRatio;
		createOverlay(localX0Coordinate, localX1Coordinate, localY0Coordinate, localY1Coordinate, zoomFactor, true);
	}

	/**
	 * To create Overlay.
	 * 
	 * @param x0Coordinate double
	 * @param x1Coordinate double
	 * @param y0Coordinate double
	 * @param y1Coordinate double
	 * @param zoomFactor double
	 * @param columnFinalized boolean
	 */
	public void createOverlay(double x0Coordinate, double x1Coordinate, double y0Coordinate, double y1Coordinate, double zoomFactor,
			boolean columnFinalized) {
		double localX0Coordinate = x0Coordinate;
		double localX1Coordinate = x1Coordinate;
		double localY0Coordinate = y0Coordinate;
		double localY1Coordinate = y1Coordinate;
		if (localX0Coordinate == 0 && localX1Coordinate == 0 && localY0Coordinate == 0 && localY1Coordinate == 0) {
			return;
		}

		double xFactor = BatchClassManagementConstants.X_FACTOR;
		double yFactor = BatchClassManagementConstants.Y_FACTOR;
		double yScrollingAdder = BatchClassManagementConstants.SEVENTY_FIVE;

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

			return;
		}
		if (xScrollPosition == 0 && yScrollPosition == 0 && zoomFactor == 1) {
			if (columnFinalized) {
				doOverlayForKey(localX0Coordinate, localX1Coordinate, localY0Coordinate, localY1Coordinate, zoomFactor);
			} else {
				doOverlay(localX0Coordinate, localX1Coordinate, localY0Coordinate, localY1Coordinate, zoomFactor);
			}
		} else if (xScrollPosition <= localX1Coordinate && yScrollPosition <= localY1Coordinate) {
			localX0Coordinate -= xScrollPosition;
			localX1Coordinate -= xScrollPosition;
			localY0Coordinate -= yScrollPosition;
			localY1Coordinate -= yScrollPosition;
			if (columnFinalized) {
				doOverlayForKey(localX0Coordinate, localX1Coordinate, localY0Coordinate, localY1Coordinate, zoomFactor);
			} else {
				doOverlay(localX0Coordinate, localX1Coordinate, localY0Coordinate, localY1Coordinate, zoomFactor);
			}
		}

	}

	/**
	 * To do overlay.
	 * 
	 * @param x0Coordinate double
	 * @param x1Coordinate double
	 * @param y0Coordinate double
	 * @param y1Coordinate double
	 * @param zoomFactor double
	 */
	public native void doOverlay(double x0Coordinate, double x1Coordinate, double y0Coordinate, double y1Coordinate, double zoomFactor) /*-{
																																		return $wnd.doOverlay(x0, x1, y0, y1, zoomFactor);
																																		}-*/;

	/**
	 * To do overlay for key.
	 * 
	 * @param x0Coordinate double
	 * @param x1Coordinate double
	 * @param y0Coordinate double
	 * @param y1Coordinate double
	 * @param zoomFactor double
	 */
	public native void doOverlayForKey(double x0Coordinate, double x1Coordinate, double y0Coordinate, double y1Coordinate,
			double zoomFactor) /*-{
								return $wnd.doOverlayForKey(x0, x1, y0, y1, zoomFactor);
								}-*/;

	/**
	 * To do overlay for value.
	 * 
	 * @param x0Coordinate double
	 * @param x1Coordinate double
	 * @param y0Coordinate double
	 * @param y1Coordinate double
	 * @param zoomFactor double
	 */
	public native void doOverlayForValue(double x0Coordinate, double x1Coordinate, double y0Coordinate, double y1Coordinate,
			double zoomFactor) /*-{
								return $wnd.doOverlayForValue(x0, x1, y0, y1, zoomFactor);
								}-*/;

	/**
	 * To remove Overlay.
	 */
	public native void removeOverlay() /*-{
										return $wnd.removeOverlay();
										}-*/;

	private void loadImage() {
		removeAllOverlays();
		this.pageImage.setVisible(true);
		String url = this.tempImage.getUrl();
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
		if (url != null && !url.isEmpty()) {
			int indexOf = url.lastIndexOf('/');
			if (indexOf != -1 && indexOf < url.length()) {
				presenter.setDisplayImageName(url.substring(indexOf + 1));
				presenter.setColumnCoordAndCreateOverlay();
			}
		}
		this.pageImage.setWidth(imageWidth.toString() + "px");
		this.pageImage.setHeight(imageHeight.toString() + "px");
		ScreenMaskUtility.unmaskScreen();
	}

	public void removeAllOverlays() {
		removeOverlay();
		tableColumnCoordinates.initialize();
	}

	/**
	 * To get View Port Width.
	 * 
	 * @return int
	 */
	private native int getViewPortWidth() /*-{
											return $wnd.getViewPortWidth();
											}-*/;

	/**
	 * To get View Port Height For Firefox.
	 * 
	 * @return int
	 */
	private native int getViewPortHeightForFirefox() /*-{
														return $wnd.getViewPortHeight();
														}-*/;

	/**
	 * To get View Port Height.
	 * 
	 * @return int
	 */
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

	/**
	 * To get View Port Height For IE.
	 * 
	 * @return int
	 */
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
	 * To get Column End Coordinate.
	 * 
	 * @return String
	 */
	public String getColEndCoord() {
		return colEndCoord.getText();
	}

	/**
	 * To set Column End Coordinate.
	 * 
	 * @param colEndCoord String
	 */
	public void setColEndCoord(String colEndCoord) {
		this.colEndCoord.setText(colEndCoord);
	}

	/**
	 * To get Column Start Coordinate.
	 * 
	 * @return String
	 */
	public String getColStartCoord() {
		return colStartCoord.getText();
	}

	/**
	 * To set Column Start Coordinate.
	 * 
	 * @param colStartCoord String
	 */
	public void setColStartCoord(String colStartCoord) {
		this.colStartCoord.setText(colStartCoord);
	}

	/**
	 * To perform operations on save click.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("saveButton")
	public void onSaveButtonClicked(ClickEvent clickEvent) {
		presenter.onSaveButtonClicked();
	}

	/**
	 * To perform operations on cancel click.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("cancelButton")
	public void onCancelButtonClicked(ClickEvent clickEvent) {
		tableColumnCoordinates.clearFinalizeValues();
		presenter.onCancelButtonClicked();
	}

	/**
	 * To clear Image Upload.
	 */
	public void clearImageUpload() {
		imageUpload.reset();
		pageImage.setUrl(BatchClassManagementConstants.EMPTY_STRING);
		tableColumnCoordinates.clearFinalizeValues();
	}

	/**
	 * To toggle Page Image Show Hide.
	 * 
	 * @param visibile boolean
	 */
	public void togglePageImageShowHide(boolean visibile) {
		pageImage.setVisible(visibile);
	}

	/**
	 * To get File Name.
	 * 
	 * @return String
	 */
	public String getFileName() {
		return fileName;
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
	 * To set Table Column Info List.
	 * 
	 * @param tableInfoDTO TableInfoDTO
	 */
	public void setTableColumnInfoList(final TableInfoDTO tableInfoDTO) {
		if (tableInfoDTO != null) {
			List<TableColumnInfoDTO> tableColumnInfoList = tableInfoDTO.getTableColumnInfoList(false);
			if (tableColumnInfoList != null) {
				this.tableColumnInfoList.clear();
				presenter.clearColumnDTOMap();
				for (TableColumnInfoDTO tableColumnInfoDTO : tableColumnInfoList) {
					String columnName = tableColumnInfoDTO.getColumnName();
					String columnIdentifier = tableColumnInfoDTO.getIdentifier();
					if (columnName != null && !columnName.isEmpty() && columnIdentifier != null && !columnIdentifier.isEmpty()) {
						presenter.addToDtoMap(columnName, tableColumnInfoDTO);
						this.tableColumnInfoList.addItem(columnName, columnIdentifier);
					}
				}
			}
		}
	}

	/**
	 * To set Selected Table Column.
	 * 
	 * @param selColumnName String
	 */
	public void setSelectedTableColumn(final String selColumnName) {
		final int itemCount = this.tableColumnInfoList.getItemCount();
		if (selColumnName != null && !selColumnName.isEmpty() && itemCount != 0) {
			for (int itemIndex = 0; itemIndex < itemCount; itemIndex++) {
				String itemText = this.tableColumnInfoList.getItemText(itemIndex);
				if (itemText != null && !itemText.isEmpty() && itemText.equalsIgnoreCase(selColumnName)) {
					this.tableColumnInfoList.setSelectedIndex(itemIndex);
					break;
				}
			}
		}
	}

	/**
	 * To set Column Coordinates.
	 * 
	 * @param xCoordinate0 String
	 * @param xCoordinate1 String
	 * @param yCoordinate0 String
	 * @param yCoordinate1 String
	 */
	public void setColCoordinates(final String xCoordinate0, final String xCoordinate1, final String yCoordinate0,
			final String yCoordinate1) {

		double aspectWidthRatio = (double) (pageImage.getWidth()) / (double) (originalWidth);
		double aspectHeightRatio = (double) (pageImage.getHeight()) / (double) (originalHeight);
		int x0Coord = (int) Math.round(Integer.parseInt(xCoordinate0) / aspectWidthRatio);
		int x1Coord = (int) Math.round(Integer.parseInt(xCoordinate1) / aspectWidthRatio);
		int y0Coord = (int) Math.round(Integer.parseInt(yCoordinate0) / aspectHeightRatio);
		int y1Coord = (int) Math.round(Integer.parseInt(yCoordinate1) / aspectHeightRatio);

		setColStartCoord(String.valueOf(x0Coord));
		setColEndCoord(String.valueOf(x1Coord));

		presenter.setColumnCoordinatesInDTO(String.valueOf(x0Coord), String.valueOf(x1Coord), String.valueOf(y0Coord), String
				.valueOf(y1Coord));
	}

	/**
	 * To set Overlay Coordinates.
	 * 
	 * @param xCoordinate0 int
	 * @param xCoordinate1 int
	 * @param yCoordinate0 int
	 * @param yCoordinate1 int
	 */
	public void setOverlayCoordinates(int xCoordinate0, int xCoordinate1, int yCoordinate0, int yCoordinate1) {
		int localX0Coordinate = xCoordinate0;
		int localX1Coordinate = xCoordinate1;
		int localY0Coordinate = yCoordinate0;
		int localY1Coordinate = yCoordinate1;
		final Coordinates coordinates = tableColumnCoordinates.getCoordinates();
		tableColumnCoordinates.setColumnFinalized(true);
		double aspectWidthRatio = (double) (pageImage.getWidth()) / (double) (originalWidth);
		double aspectHeightRatio = (double) (pageImage.getHeight()) / (double) (originalHeight);
		localX0Coordinate = (int) Math.round(localX0Coordinate * aspectWidthRatio);
		localX1Coordinate = (int) Math.round(localX1Coordinate * aspectWidthRatio);
		localY0Coordinate = (int) Math.round(localY0Coordinate * aspectHeightRatio);
		localY1Coordinate = (int) Math.round(localY1Coordinate * aspectHeightRatio);
		coordinates.setX0(localX0Coordinate);
		coordinates.setX1(localX1Coordinate);
		coordinates.setY0(localY0Coordinate);
		coordinates.setY1(localY1Coordinate);
	}

}
