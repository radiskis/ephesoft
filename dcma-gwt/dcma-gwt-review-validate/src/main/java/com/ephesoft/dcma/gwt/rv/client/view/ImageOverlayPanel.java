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

package com.ephesoft.dcma.gwt.rv.client.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.ephesoft.dcma.batch.schema.Batch;
import com.ephesoft.dcma.batch.schema.Coordinates;
import com.ephesoft.dcma.batch.schema.Direction;
import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.batch.schema.Page;
import com.ephesoft.dcma.batch.schema.Field.CoordinatesList;
import com.ephesoft.dcma.core.common.BatchInstanceStatus;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.client.ui.RotatableImage;
import com.ephesoft.dcma.gwt.core.client.ui.ScreenMaskUtility;
import com.ephesoft.dcma.gwt.core.shared.BatchDTO;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialog;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialogUtil;
import com.ephesoft.dcma.gwt.core.shared.PointCoordinate;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialog.DialogListener;
import com.ephesoft.dcma.gwt.rv.client.event.PageChangeEvent;
import com.ephesoft.dcma.gwt.rv.client.event.RVKeyDownEvent;
import com.ephesoft.dcma.gwt.rv.client.event.RVKeyDownEventHandler;
import com.ephesoft.dcma.gwt.rv.client.event.RVKeyUpEvent;
import com.ephesoft.dcma.gwt.rv.client.event.RVKeyUpEventHandler;
import com.ephesoft.dcma.gwt.rv.client.event.ThumbnailSelectionEvent;
import com.ephesoft.dcma.gwt.rv.client.event.ThumbnailSelectionEventHandler;
import com.ephesoft.dcma.gwt.rv.client.event.TreeRefreshEvent;
import com.ephesoft.dcma.gwt.rv.client.i18n.ReviewValidateConstants;
import com.ephesoft.dcma.gwt.rv.client.i18n.ReviewValidateMessages;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.event.dom.client.ScrollHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.ScrollPanel;

/**
 * The ImageOberlayPanel {@codeImageOverlayPanel} class represents view for validating batch class image using overlay and handles image rotate, zoom in
 * and zoom out functionality.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.gwt.rv.client.view.RVBasePanel
 * @see com.ephesoft.dcma.gwt.rv.client.view
 */
public class ImageOverlayPanel extends RVBasePanel {

	interface Binder extends UiBinder<DockLayoutPanel, ImageOverlayPanel> {

	}

	@UiField
	protected ScrollPanel imageScrollPanel;
	@UiField
	protected RotatableImage pageImage;
	@UiField
	protected Button splitButton;
	@UiField
	protected Button deleteButton;
	@UiField
	protected Button duplicatePageButton;
	@UiField
	protected Button movePageButton;
	@UiField
	protected Button zoomin;
	@UiField
	protected Button zoomout;
	@UiField
	protected Button rotate;
	@UiField
	protected Button fitToPage;
	@UiField
	protected RotatableImage tempImage;
	@UiField
	protected DockLayoutPanel imageOverlayViewPanel;

	private Timer timeout = null;

	private boolean isRemoveOverlay = false;

	private CoordinatesList coordinatesTypeList;

	private List<Coordinates> coordinatesList;

	private double x0Coordinate;
	private double x1Coordinate;
	private double y0Coordinate;
	private double y1Coordinate;
	private Integer originalWidth;
	private static final double ZOOM_FACTOR = 1.25;
	private int zoomCount;
	private static final int ZOOM_COUNT_LIMIT = 6;
	private boolean fitToPageImage;
	private Integer heightBeforeFitToHeight;
	private Integer widthBeforeFitToWidth;
	private Integer clickCount = 0;
	private boolean shiftKeyPressed = false;
	private boolean ctrlKeyPressed = false;
	private boolean rightMouseFirstClickDone = false;
	private static final double X_FACTOR = 0.443;
	private static final double Y_FACTOR = 0.243;
	private List<PointCoordinate> pointCoordinates;
	private final PointCoordinate pointCoordinate1 = new PointCoordinate();
	private final PointCoordinate pointCoordinate2 = new PointCoordinate();
	private Integer coordinateIdentifier = 0;
	private final ZoomLock zoomLock;
	private boolean isFirstClick = true;
	private static final double CROSS_HAIR_REDUCTION_FACTOR = 5;
	private final Map<String, ArrayList<Integer>> initialPageDimensions = new HashMap<String, ArrayList<Integer>>();
	public static final String EMPTY_STRING = "";
	private static final Binder BINDER = GWT.create(Binder.class);

	/**
	 * The dimension image {@link RotatableImage} is for used for getting dimension of image to be loaded in case of IE.
	 */
	private final RotatableImage dimensionImage = new RotatableImage();

	/**
	 * The <code>ImageOverlayPanel</code> method is a default constructor which
	 * initializes default view for overlay on validation screen shown to user.
	 */
	public ImageOverlayPanel() {
		super();
		final DockLayoutPanel layoutPanel = BINDER.createAndBindUi(this);
		initWidget(layoutPanel);
		movePageButton.setStyleName("move_button");
		splitButton.setStyleName("split_button");
		splitButton.setTitle(LocaleDictionary.get().getConstantValue(ReviewValidateConstants.TOOLTIP_SPLIT));
		deleteButton.setStyleName("delete_button");
		deleteButton.setTitle(LocaleDictionary.get().getConstantValue(ReviewValidateConstants.TOOLTIP_DELETE));
		duplicatePageButton.setStyleName("duplicate_button");
		duplicatePageButton.setTitle(LocaleDictionary.get().getConstantValue(ReviewValidateConstants.TOOLTIP_DUPLICATE));
		movePageButton.setStyleName("move_page_button");
		movePageButton.setTitle(LocaleDictionary.get().getConstantValue(ReviewValidateConstants.TOOLTIP_MOVE));
		zoomin.setStyleName("zoom_in");
		zoomin.setTitle(LocaleDictionary.get().getConstantValue(ReviewValidateConstants.TOOLTIP_ZOOM_IN));
		zoomout.setStyleName("zoom_out");
		zoomout.setTitle(LocaleDictionary.get().getConstantValue(ReviewValidateConstants.TOOLTIP_ZOOM_OUT));
		rotate.setStyleName("rotate_button");
		fitToPage.setStyleName("fit_to_page");
		fitToPage.setTitle(LocaleDictionary.get().getConstantValue(ReviewValidateConstants.TOOLTIP_FIT_TO_PAGE));
		rotate.setTitle(LocaleDictionary.get().getConstantValue(ReviewValidateConstants.TOOLTIP_ROTATE));
		tempImage.setVisible(false);
		zoomLock = new ZoomLock(this);
		pageImage.setUrl(EMPTY_STRING);
		dimensionImage.setVisible(false);

		dimensionImage.addLoadHandler(new LoadHandler() {

			@Override
			public void onLoad(final LoadEvent arg0) {
				// Do nothing
			}
		});

		tempImage.addLoadHandler(new LoadHandler() {

			@Override
			public void onLoad(final LoadEvent loadEvent) {
				int width = tempImage.getWidth();
				int height = tempImage.getHeight();
				
				// For 3rd panel image load issue on IE
				DOM.setElementAttribute(dimensionImage.getElement(), ReviewValidateConstants.SOURCE
							, (DOM.getElementAttribute(tempImage.getElement(), ReviewValidateConstants.SOURCE)));
				final String currentBrowser = getUserAgent();

				// If current browser is IE then set image width and height from dimension image.
				if (currentBrowser != null && currentBrowser.length() > 0
						&& currentBrowser.contains(ReviewValidateConstants.IE_BROWSER)) {
					if (width <= 0) {
						width = dimensionImage.getWidth();
					}
					if (height <= 0) {
						height = dimensionImage.getHeight();
					}
				}

				if (!(initialPageDimensions.containsKey(presenter.page.getIdentifier()))) {
					final ArrayList<Integer> dimensions = new ArrayList<Integer>();
					dimensions.add(width);
					dimensions.add(height);

					initialPageDimensions.put(presenter.page.getIdentifier(), dimensions);
				}
				pageImage.setVisible(false);
				removeOverlays();
				pageImage.setUrl(tempImage.getUrl().substring(0, tempImage.getUrl().indexOf(ReviewValidateConstants.QUESTION_MARK)),
						presenter.page.getDirection());
				pageImage.setStyleName(ReviewValidateConstants.CSS_THUMBNAIL_HIGHLIGHTED);
			}
		});
		pageImage.addMouseUpHandler(new MouseUpHandler() {

			@Override
			public void onMouseUp(final MouseUpEvent mouseUp) {
				if (presenter.batchDTO.getBatchInstanceStatus().equals(BatchInstanceStatus.READY_FOR_VALIDATION)) {
					final double aspectRatio = (double) (pageImage.getWidth()) / (double) (originalWidth);
					int xCoordinate = (int) Math.round(mouseUp.getX() / aspectRatio);
					int yCoordinate = (int) Math.round(mouseUp.getY() / aspectRatio);
					if (presenter.isTableView() && presenter.isManualTableExtraction()) {
						if (isFirstClick) {
							pageImage.setStyleName(ReviewValidateConstants.POINTER);
							pointCoordinate1.setxCoordinate(xCoordinate);
							pointCoordinate1.setyCoordinate(yCoordinate);
							isFirstClick = false;
						} else {
							pageImage.removeStyleName(ReviewValidateConstants.POINTER);
							if (xCoordinate < pointCoordinate1.getxCoordinate()) {
								final int temp = pointCoordinate1.getxCoordinate();
								pointCoordinate1.setxCoordinate(xCoordinate);
								xCoordinate = temp;
							}
							if (yCoordinate < pointCoordinate1.getyCoordinate()) {
								final int temp = pointCoordinate1.getyCoordinate();
								pointCoordinate1.setyCoordinate(yCoordinate);
								yCoordinate = temp;
							}
							pointCoordinate2.setxCoordinate(xCoordinate);
							pointCoordinate2.setyCoordinate(yCoordinate);

							if (presenter.getView().getTableExtractionView().isValidCoordinate(pointCoordinate1, pointCoordinate2)) {
								presenter.getTableHOCRContent(pointCoordinate1, pointCoordinate2);
							}
							isFirstClick = true;
						}
					} else if (!presenter.isManualTableExtraction()) {
						if (mouseUp.getNativeButton() == Event.BUTTON_RIGHT) {
							clickCount = 0;
							ctrlKeyPressed = false;
							shiftKeyPressed = false;
							if (!rightMouseFirstClickDone) {
								pageImage.addStyleName(ReviewValidateConstants.POINTER);
								pointCoordinate1.setxCoordinate(xCoordinate);
								pointCoordinate1.setyCoordinate(yCoordinate);
								rightMouseFirstClickDone = true;
							} else {
								if (xCoordinate < pointCoordinate1.getxCoordinate()) {
									final int temp = pointCoordinate1.getxCoordinate();
									pointCoordinate1.setxCoordinate(xCoordinate);
									xCoordinate = temp;
								}
								if (yCoordinate < pointCoordinate1.getyCoordinate()) {
									final int temp = pointCoordinate1.getyCoordinate();
									pointCoordinate1.setyCoordinate(yCoordinate);
									yCoordinate = temp;
								}
								pageImage.removeStyleName(ReviewValidateConstants.POINTER);
								pointCoordinate2.setxCoordinate(xCoordinate);
								pointCoordinate2.setyCoordinate(yCoordinate);
								rightMouseFirstClickDone = false;
								presenter.getHOCRContent(pointCoordinate1, pointCoordinate2, true);
							}
						}

						else {
							rightMouseFirstClickDone = false;
							if (shiftKeyPressed) {
								if (clickCount == 0) {
									pointCoordinate1.setxCoordinate(xCoordinate);
									pointCoordinate1.setyCoordinate(yCoordinate);
									clickCount = 1;
								} else {
									pointCoordinate2.setxCoordinate(xCoordinate);
									pointCoordinate2.setyCoordinate(yCoordinate);
									presenter.getHOCRContent(pointCoordinate1, pointCoordinate2, false);
								}
							} else if (ctrlKeyPressed) {
								if (clickCount == 0 || pointCoordinates == null) {
									pointCoordinates = new ArrayList<PointCoordinate>();
								}
								final PointCoordinate pointCoordinate = new PointCoordinate();
								pointCoordinate.setxCoordinate(xCoordinate);
								pointCoordinate.setyCoordinate(yCoordinate);
								pointCoordinates.add(pointCoordinate);
								presenter.getHOCRContent(pointCoordinates);
								clickCount++;
							} else {
								pointCoordinates = new ArrayList<PointCoordinate>(1);
								final PointCoordinate pointCoordinate = new PointCoordinate();
								pointCoordinate.setxCoordinate(xCoordinate);
								pointCoordinate.setyCoordinate(yCoordinate);
								pointCoordinates.add(pointCoordinate);
								presenter.getHOCRContent(pointCoordinates);
							}
						}
					}
				}
			}
		});
		splitButton.setEnabled(false);
		deleteButton.setEnabled(false);
		duplicatePageButton.setEnabled(false);
		movePageButton.setEnabled(false);

		zoomin.setEnabled(false);
		zoomout.setEnabled(false);
		rotate.setEnabled(false);
		fitToPage.setEnabled(false);

		pageImage.addLoadHandler(new LoadHandler() {

			@Override
			public void onLoad(final LoadEvent arg0) {
				splitButton.setEnabled(true);
				deleteButton.setEnabled(true);
				duplicatePageButton.setEnabled(true);
				movePageButton.setEnabled(true);

				zoomin.setEnabled(true);
				zoomout.setEnabled(true);
				rotate.setEnabled(true);
				fitToPage.setEnabled(true);

				loadImage();
				pageImage.setVisible(true);
			}
		});

		imageScrollPanel.addScrollHandler(new ScrollHandler() {

			@Override
			public void onScroll(final ScrollEvent arg0) {
				processOverlay(coordinatesTypeList, imageScrollPanel.getHorizontalScrollPosition(), imageScrollPanel
						.getScrollPosition(), 1);
			}
		});

		imageOverlayViewPanel.addStyleName("right1");

		pageImage.addMouseMoveHandler(new MouseMoveHandler() {

			@Override
			public void onMouseMove(final MouseMoveEvent paramMouseMoveEvent) {
				if (presenter.batchDTO.getBatchInstanceStatus().equals(BatchInstanceStatus.READY_FOR_VALIDATION)) {
					if (presenter.isManualTableExtraction() || (!presenter.isManualTableExtraction() && rightMouseFirstClickDone)) {
						final double aspectRatio = (double) (pageImage.getWidth()) / (double) (originalWidth);
						final int xCoordinate = (int) Math.round(paramMouseMoveEvent.getX() / aspectRatio);
						final int yCoordinate = (int) Math.round(paramMouseMoveEvent.getY() / aspectRatio);
						pointCoordinate2.setxCoordinate(xCoordinate);
						pointCoordinate2.setyCoordinate(yCoordinate);
						if ((presenter.isManualTableExtraction() && !isFirstClick)
								|| (rightMouseFirstClickDone && !presenter.isManualTableExtraction())) {
							// pageImage.addStyleName("pointer");
							doOverlay(pointCoordinate1, pointCoordinate2);
						}
					} else {
						pageImage.removeStyleName(ReviewValidateConstants.POINTER);
					}
				}

			}
		});
	}

	private void processOverlay(final CoordinatesList coordinatesTypeList, final int xScrollPosition, final int yScrollPosition,
			final float zoomFactor) {
		removeOverlays();
		if (!fitToPageImage && !isRemoveOverlay) {
			coordinateIdentifier = 0;
			if (coordinatesTypeList != null && !coordinatesTypeList.getCoordinates().isEmpty()) {
				for (final Coordinates coordinatesType : coordinatesTypeList.getCoordinates()) {
					processOverlay(coordinateIdentifier++, coordinatesType, xScrollPosition, yScrollPosition, zoomFactor);
				}
			}
			if (coordinatesList != null) {
				processOverlay(coordinatesList, xScrollPosition, yScrollPosition, zoomFactor);
			}
		}
	}

	private void processOverlay(final Integer identifier, final Coordinates coordinatesType, final int xScrollPosition,
			final int yScrollPosition, final float zoomFactor) {
		final double aspectRatio = (double) (pageImage.getWidth()) / (double) (originalWidth);
		final StringBuffer coordinateIdentifier = new StringBuffer();
		coordinateIdentifier.append(identifier);
		if (coordinatesType != null) {
			x0Coordinate = coordinatesType.getX0().intValue();
			x1Coordinate = coordinatesType.getX1().intValue();
			y0Coordinate = coordinatesType.getY0().intValue();
			y1Coordinate = coordinatesType.getY1().intValue();

			x0Coordinate = (double) (x0Coordinate * aspectRatio) + (double) (X_FACTOR * getViewPortWidth());
			x1Coordinate = (double) (x1Coordinate * aspectRatio) + (double) (X_FACTOR * getViewPortWidth());
			y0Coordinate = (double) (y0Coordinate * aspectRatio) + (double) (Y_FACTOR * getViewPortHeight());
			y1Coordinate = (double) (y1Coordinate * aspectRatio) + (double) (Y_FACTOR * getViewPortHeight());
			if (xScrollPosition > (x0Coordinate - (0.443 * getViewPortWidth())) || x1Coordinate - xScrollPosition > getViewPortWidth()) {
				removeOverlayById(coordinateIdentifier.toString());
				return;
			}
			if (xScrollPosition <= x1Coordinate && yScrollPosition <= y1Coordinate) {
				x0Coordinate -= xScrollPosition;
				x1Coordinate -= xScrollPosition;
				if (yScrollPosition > (y0Coordinate - (0.24 * getViewPortHeight()))) {
					y0Coordinate = (0.24 * getViewPortHeight());
				} else if (y0Coordinate - yScrollPosition > (0.96 * getViewPortHeight())) {
					y0Coordinate = 0.96 * getViewPortHeight();
				} else {
					y0Coordinate -= yScrollPosition;
				}
				if (yScrollPosition > (y1Coordinate - (0.24 * getViewPortHeight()))) {
					y1Coordinate = (0.24 * getViewPortHeight());
				} else if (y1Coordinate - yScrollPosition > (0.96 * getViewPortHeight())) {
					y1Coordinate = 0.96 * getViewPortHeight();
				} else {
					y1Coordinate -= yScrollPosition;
				}
				removeOverlayById(coordinateIdentifier.toString());
				doOverlay(coordinateIdentifier.toString(), x0Coordinate, x1Coordinate, y0Coordinate, y1Coordinate, zoomFactor);
			} else {
				removeOverlayById(coordinateIdentifier.toString());
			}
		}
	}

	public void processOverlay(final List<Coordinates> coordinatesList, final int xScrollPosition, final int yScrollPosition,
			final float zoomFactor) {
		for (final Coordinates coordinates : coordinatesList) {
			final double aspectRatio = (double) (pageImage.getWidth()) / (double) (originalWidth);
			if (coordinates != null) {
				x0Coordinate = coordinates.getX0().intValue();
				x1Coordinate = coordinates.getX1().intValue();
				y0Coordinate = coordinates.getY0().intValue();
				y1Coordinate = coordinates.getY1().intValue();
				x0Coordinate = (double) (x0Coordinate * aspectRatio) + (double) (X_FACTOR * getViewPortWidth());
				x1Coordinate = (double) (x1Coordinate * aspectRatio) + (double) (X_FACTOR * getViewPortWidth());
				y0Coordinate = (double) (y0Coordinate * aspectRatio) + (double) (Y_FACTOR * getViewPortHeight());
				y1Coordinate = (double) (y1Coordinate * aspectRatio) + (double) (Y_FACTOR * getViewPortHeight());
				if (yScrollPosition > (y1Coordinate - (0.24 * getViewPortHeight()))
						|| xScrollPosition > (x0Coordinate - (0.443 * getViewPortWidth()))
						|| x1Coordinate - xScrollPosition > getViewPortWidth() || y1Coordinate - yScrollPosition > getViewPortHeight()) {
					return;
				}
				if (xScrollPosition == 0 && yScrollPosition == 0 && zoomFactor == 1) {
					doOverlayForRow(x0Coordinate, x1Coordinate, y0Coordinate, y1Coordinate, zoomFactor);
				} else if (xScrollPosition <= x1Coordinate && yScrollPosition <= y1Coordinate) {
					x0Coordinate -= xScrollPosition;
					x1Coordinate -= xScrollPosition;
					y0Coordinate -= yScrollPosition;
					y1Coordinate -= yScrollPosition;
					doOverlayForRow(x0Coordinate, x1Coordinate, y0Coordinate, y1Coordinate, zoomFactor);
				}
			}
		}
	}

	private native int getViewPortHeightForFirefox() /*-{
														return $wnd.getViewPortHeight();
														}-*/;

	private int getViewPortHeight() {
		int viewPortHeight = 0;
		final String currentBrowser = getUserAgent();
		if (currentBrowser != null && currentBrowser.length() > 0 && currentBrowser.contains("msie")) {
			viewPortHeight = getViewPortHeightForIE();
		} else {
			viewPortHeight = getViewPortHeightForFirefox();
		}
		return viewPortHeight;
	}

	private native int getViewPortHeightForIE() /*-{
												return $wnd.getViewPortHeightForIE();
												}-*/;

	private native int getViewPortWidth() /*-{
											return $wnd.getViewPortWidth();
											}-*/;

	private native void doOverlay(String identifier, double x0Coordinate, double x1Coordinate, double y0Coordinate,
			double y1Coordinate, double zoomFactor) /*-{
													return $wnd.doOverlayById(identifier,x0Coordinate, x1Coordinate, y0Coordinate, y1Coordinate, zoomFactor);
													}-*/;

	private native void doOverlayForRow(double x0Coordinate, double x1Coordinate, double y0Coordinate, double y1Coordinate,
			double zoomFactor) /*-{
								return $wnd.doOverlayForRow(x0Coordinate, x1Coordinate, y0Coordinate, y1Coordinate, zoomFactor);
								}-*/;

	private native void removeAllOverlays() /*-{
											return $wnd.removeOverlay();
											}-*/;

	private void removeOverlays() {
		removeAllOverlays();
		for (int i = 0; i < coordinateIdentifier; i++) {
			final StringBuffer identifier = new StringBuffer();
			identifier.append(i);
			removeOverlayById(identifier.toString());
		}
	}

	private native void removeOverlayById(String identifier) /*-{
																return $wnd.removeOverlayById(identifier);
																}-*/;

	public static native String getUserAgent() /*-{
												return navigator.userAgent.toLowerCase();
												}-*/;

	@UiHandler("duplicatePageButton")
	public void onDuplicateButtonClicked(final ClickEvent event) {
		final ConfirmationDialog confirmationDialog = ConfirmationDialogUtil.showConfirmationDialog(LocaleDictionary.get()
				.getMessageValue(ReviewValidateMessages.MSG_OVERLAYPANEL_DUPLICATE, presenter.page.getIdentifier()), LocaleDictionary
				.get().getConstantValue(ReviewValidateConstants.TITLE_DUPLICATE), Boolean.FALSE);

		confirmationDialog.addDialogListener(new DialogListener() {

			@Override
			public void onOkClick() {
				final Batch batch = presenter.batchDTO.getBatch();
				ScreenMaskUtility.maskScreen();
				presenter.rpcService.duplicatePageOfDocument(batch, presenter.document.getIdentifier(),
						presenter.page.getIdentifier(), new AsyncCallback<BatchDTO>() {

							@Override
							public void onSuccess(final BatchDTO batchDTO) {
								presenter.batchDTO = batchDTO;
								presenter.document = batchDTO.getDocumentById(presenter.document.getIdentifier());
								presenter.page = presenter.document.getPages().getPage().get(
										presenter.document.getPages().getPage().size() - 1);
								fireEvent(new TreeRefreshEvent(batchDTO, presenter.document, presenter.page));
								ScreenMaskUtility.unmaskScreen();
							}

							@Override
							public void onFailure(final Throwable arg0) {
								ScreenMaskUtility.unmaskScreen();
								if (!presenter.displayErrorMessage(arg0)) {
									ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
											ReviewValidateMessages.MSG_OVERLAYPANEL_DUPLICATE_ERROR, presenter.page.getIdentifier()),
											Boolean.TRUE);
								}
							}
						});
			}

			@Override
			public void onCancelClick() {
				confirmationDialog.hide();
				presenter.setFocus();
			}
		});

	}

	@UiHandler("deleteButton")
	public void onDeletButtonClicked(final ClickEvent event) {
		final Batch batch = presenter.batchDTO.getBatch();
		final List<Document> documents = batch.getDocuments().getDocument();
		if (documents.size() <= 1 && (documents.get(0).getPages().getPage().size() <= 1)) {
			ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
					ReviewValidateMessages.MSG_OVERLAYPANEL_LAST_PAGE_DELETE_ERROR));
			return;
		}
		final ConfirmationDialog confirmationDialog = ConfirmationDialogUtil.showConfirmationDialog(LocaleDictionary.get()
				.getMessageValue(
				ReviewValidateMessages.MSG_OVERLAYPANEL_DELETE, presenter.page.getIdentifier()), LocaleDictionary.get()
				.getConstantValue(ReviewValidateConstants.TITLE_DELETE), Boolean.FALSE);

		confirmationDialog.addDialogListener(new DialogListener() {

			@Override
			public void onOkClick() {
				ScreenMaskUtility.maskScreen();

				presenter.rpcService.deletePageOfDocument(batch, presenter.document.getIdentifier(), presenter.page.getIdentifier(),
						new AsyncCallback<BatchDTO>() {

							@Override
							public void onFailure(final Throwable caught) {
								ScreenMaskUtility.unmaskScreen();
								if (!presenter.displayErrorMessage(caught)) {
									ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
											ReviewValidateMessages.MSG_OVERLAYPANEL_DELETE_ERROR, presenter.page.getIdentifier(),
											caught.getMessage()), Boolean.TRUE);
								}
							}

							@Override
							public void onSuccess(final BatchDTO batchDTO) {
								presenter.batchDTO = batchDTO;
								presenter.document = batchDTO.getDocumentById(presenter.document.getIdentifier());
								if (presenter.document != null) {
									presenter.page = presenter.document.getPages().getPage().get(0);
								}
								fireEvent(new TreeRefreshEvent(batchDTO, presenter.document, presenter.page));
								ScreenMaskUtility.unmaskScreen();
							}
						});
			}

			@Override
			public void onCancelClick() {
				presenter.setFocus();
			}
		});

	}

	@UiHandler("splitButton")
	public void onSplitButtonClicked(final ClickEvent event) {
		final boolean isFirstPage = isFirstPage(presenter.batchDTO.getDocumentForPage(presenter.page));
		if (isFirstPage) {
			final ConfirmationDialog confirmationDialog = ConfirmationDialogUtil.showConfirmationDialog(LocaleDictionary.get()
					.getMessageValue(ReviewValidateMessages.MSG_OVERLAYPANEL_SPLIT_FST_PAGE), LocaleDictionary.get().getConstantValue(
					ReviewValidateConstants.TITLE_SPLIT), Boolean.TRUE);

			confirmationDialog.addDialogListener(new DialogListener() {

				@Override
				public void onOkClick() {
					confirmationDialog.hide();
					presenter.setFocus();
				}

				@Override
				public void onCancelClick() {
					presenter.setFocus();
				}
			});

			return;
		}
		final ConfirmationDialog confirmationDialog = ConfirmationDialogUtil.showConfirmationDialog(LocaleDictionary.get()
				.getMessageValue(ReviewValidateMessages.MSG_OVERLAYPANEL_SPLIT, presenter.document.getIdentifier(),
						presenter.page.getIdentifier()), LocaleDictionary.get().getConstantValue(
				ReviewValidateConstants.TITLE_SPLIT_DOC), Boolean.FALSE);

		confirmationDialog.addDialogListener(new DialogListener() {

			@Override
			public void onOkClick() {
				final Batch batch = presenter.batchDTO.getBatch();
				presenter.splitDocument(batch, presenter.document.getIdentifier(), presenter.page.getIdentifier());
				confirmationDialog.hide();
				fireEvent(new TreeRefreshEvent(presenter.batchDTO, presenter.document, presenter.page));
			}

			@Override
			public void onCancelClick() {
				confirmationDialog.hide();
				presenter.setFocus();
			}
		});

	}

	private boolean isFirstPage(final Document documentType) {
		return documentType.getPages().getPage().get(0).getIdentifier().equals(presenter.page.getIdentifier());
	}

	@UiHandler("movePageButton")
	public void onMoveButtonClicked(final ClickEvent event) {
		final PageMoveDialog dialogBox = new PageMoveDialog();
		dialogBox.setDialogTitle(LocaleDictionary.get().getConstantValue(ReviewValidateConstants.TITLE_MOVE));
		// List<String> listOfDocumentId =
		// getAllDocumentIdForMove(presenter.document.getIdentifier());
		final List<String> listOfDocumentId = getAllDocumentIdForMove();
		final PageMovePanel pageMovePanel = new PageMovePanel(presenter.page.getIdentifier(), presenter.document.getIdentifier(),
				listOfDocumentId, presenter.batchDTO, dialogBox, presenter);
		dialogBox.setWidth("100%");
		pageMovePanel.setWidth("100%");
		pageMovePanel.setListener(new DialogListener() {

			@Override
			public void onOkClick() {
				presenter.batchDTO = pageMovePanel.getBatch();
				presenter.document = presenter.batchDTO.getDocumentById(pageMovePanel.getMoveToDocumentId());
				presenter.page = presenter.batchDTO.getUpdatedPageInDocument(presenter.document, presenter.page);
				fireEvent(new TreeRefreshEvent(presenter.batchDTO, presenter.document, presenter.page));
			}

			@Override
			public void onCancelClick() {
				presenter.setFocus();
			}
		});
		dialogBox.add(pageMovePanel);
		dialogBox.center();
		dialogBox.show();
		pageMovePanel.movePageButton.setFocus(true);

	}

	private List<String> getAllDocumentIdForMove() {
		final Batch batch = presenter.batchDTO.getBatch();
		final List<Document> documents = batch.getDocuments().getDocument();
		final List<String> list = new LinkedList<String>();
		for (int i = 0; i < documents.size(); i++) {
			list.add(documents.get(i).getIdentifier());
		}
		return list;
	}

	private void loadImage() {
		final int screenWidth = getViewPortWidth();
		final Integer imageWidth = screenWidth * 53 / 100;
		removeOverlays();
		final Integer height = initialPageDimensions.get(presenter.page.getIdentifier()).get(1);
		final Integer width = initialPageDimensions.get(presenter.page.getIdentifier()).get(0);
		pageImage.setHeight(height.toString());
		pageImage.setWidth(width.toString());
		originalWidth = width;
		final Integer imageHeight = height * imageWidth / width;
		ImageOverlayPanel.this.pageImage.setVisible(true);
		setZoom(imageWidth, imageHeight);
	}

	@UiHandler("fitToPage")
	public void onFitToPageClicked(final ClickEvent event) {
		removeOverlays();
		if (!fitToPageImage) {
			final int viewPortHeight = getViewPortHeight();
			final int viewPortWidth = getViewPortWidth();
			heightBeforeFitToHeight = pageImage.getHeight();
			widthBeforeFitToWidth = pageImage.getWidth();
			double imageWidth = 0;
			double imageHeight = getViewPortHeight();
			float count = 53.0f;
			while (imageHeight > viewPortHeight * 72 / 100) {
				imageWidth = viewPortWidth * count / 100;
				imageHeight = pageImage.getHeight() * imageWidth / pageImage.getWidth();
				count -= 0.1f;
			}
			pageImage.setWidth(Double.toString(imageWidth));
			pageImage.setHeight(Double.toString(imageHeight));
			fitToPageImage = Boolean.TRUE;
		} else {
			fitToPageImage = Boolean.FALSE;
			pageImage.setHeight(heightBeforeFitToHeight.toString());
			pageImage.setWidth(widthBeforeFitToWidth.toString());
		}
	}

	@UiHandler("zoomin")
	public void onZoomIn(final ClickEvent event) {
		if (!fitToPageImage) {

			if (zoomCount >= ZOOM_COUNT_LIMIT) {
				zoomin.setEnabled(Boolean.FALSE);
			} else {
				zoomCount++;

				pageImage.setSize(EMPTY_STRING + pageImage.getWidth() * ZOOM_FACTOR, EMPTY_STRING + pageImage.getHeight()
						* ZOOM_FACTOR);
				processOverlay(coordinatesTypeList, 0, 0, (float) (zoomCount * ZOOM_FACTOR));
				setScroll();
			}
			if (!zoomout.isEnabled()) {
				zoomout.setEnabled(Boolean.TRUE);
			}

			return;
		}
	}

	private void setScroll() {
		// Overlay to be removed in case of drop down fields where no
		// corresponding match is found
		if (isRemoveOverlay) {
			return;
		}
		if (x0Coordinate - 10 > (0.45 * getViewPortWidth())) {
			imageScrollPanel.setHorizontalScrollPosition((int) (x0Coordinate - (0.45 * getViewPortWidth()) - 10));
		} else {
			imageScrollPanel.setHorizontalScrollPosition((int) (x0Coordinate - (0.45 * getViewPortWidth())));
		}
		if (y0Coordinate - 10 > (0.25 * getViewPortHeight())) {
			imageScrollPanel.setScrollPosition((int) (y0Coordinate - (0.25 * getViewPortHeight()) - 10));
		} else {
			imageScrollPanel.setScrollPosition((int) (y0Coordinate - (0.25 * getViewPortHeight())));
		}
	}

	@UiHandler("zoomout")
	public void onZoomOut(final ClickEvent event) {
		if (!fitToPageImage) {
			if (zoomCount <= 1) {
				zoomout.setEnabled(Boolean.FALSE);
			} else {
				zoomCount--;

				pageImage.setSize(EMPTY_STRING + pageImage.getWidth() / ZOOM_FACTOR, EMPTY_STRING + pageImage.getHeight()
						/ ZOOM_FACTOR);
				processOverlay(coordinatesTypeList,

				0, 0, (float) (1 / ((ZOOM_COUNT_LIMIT - zoomCount) * ZOOM_FACTOR)));
				setScroll();
			}
			if (!zoomin.isEnabled()) {
				zoomin.setEnabled(Boolean.TRUE);
			}
			return;
		}
	}

	@UiHandler("rotate")
	public void rotateImage(final ClickEvent event) {
		fitToPageImage = Boolean.FALSE;
		rotate.setEnabled(false);
		ScreenMaskUtility.maskScreen();
		presenter.rpcService.rotateImage(presenter.batchDTO.getBatch(), presenter.page, presenter.document.getIdentifier(),
				new AsyncCallback<Page>() {

					@Override
					public void onFailure(final Throwable arg0) {
						ScreenMaskUtility.unmaskScreen();
						rotate.setEnabled(true);
						if (!presenter.displayErrorMessage(arg0)) {
							ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
									ReviewValidateMessages.MSG_OVERLAYPANEL_ROTATE_ERROR));
						}
					}

					@Override
					public void onSuccess(final Page arg0) {
						presenter.page.setDirection(arg0.getDirection());
						presenter.page.setIsRotated(arg0.isIsRotated());
						final List<Document> docList = presenter.batchDTO.getBatch().getDocuments().getDocument();
						for (Document doc : docList) {
							if (presenter.document.getIdentifier().equals(doc.getIdentifier())) {
								final List<Page> pageList = doc.getPages().getPage();
								int index = 0;
								for (final Page pg : pageList) {
									if (pg.getIdentifier().equals(presenter.page.getIdentifier())) {
										pageList.set(index, presenter.page);// presenter.page;
									}
									index++;
								}
								doc = presenter.document;
							}
						}
						final Direction direction = presenter.page.getDirection();
						final String absoluteURLFor = presenter.batchDTO.getAbsoluteURLForRotatedImage(presenter.page
								.getDisplayFileName(),
								direction.toString());
						pageImage.setUrl(absoluteURLFor, direction);
						pageImage.setStyleName("thumbnailHighlighted");
						ImageOverlayPanel.this.fireEvent(new PageChangeEvent(presenter.document, presenter.page));
						timeout = new Timer() {

							public void run() {
								ScreenMaskUtility.unmaskScreen();
								timeout = null;
							}
						};

						timeout.schedule(1000);
						rotate.setEnabled(true);
					}
				});
	}

	@Override
	public void initializeWidget() {
		this.pageImage.setVisible(false);
	}

	@Override
	public void injectEvents(final HandlerManager eventBus) {
		addThumbnailSelectionHandler(eventBus);

		addRVKeyDownHandler(eventBus);
		addRVKeyUpEventHandler(eventBus);
	}

	/**
	 * @param eventBus
	 */
	private void addRVKeyUpEventHandler(final HandlerManager eventBus) {
		eventBus.addHandler(RVKeyUpEvent.type, new RVKeyUpEventHandler() {

			@Override
			public void onKeyUp(final RVKeyUpEvent event) {
				shiftKeyPressed = false;
				ctrlKeyPressed = false;
				clickCount = 0;
			}
		});
	}

	/**
	 * @param eventBus
	 */
	private void addRVKeyDownHandler(final HandlerManager eventBus) {
		eventBus.addHandler(RVKeyDownEvent.type, new RVKeyDownEventHandler() {

			@Override
			public void onKeyDown(final RVKeyDownEvent event) {
				if (event.getEvent().isShiftKeyDown()) {
					rightMouseFirstClickDone = false;
					shiftKeyPressed = true;
				}
				if (event.getEvent().isControlKeyDown()) {
					addCtrlKeyDownEventHandlers(event);
				} else {
					addCtrlKeyUpEventHandler(event);
				}
			}

			/**
			 * @param event
			 */
			private void addCtrlKeyUpEventHandler(final RVKeyDownEvent event) {
				switch (event.getEvent().getNativeEvent().getKeyCode()) {

					// Ctrl + f or F
					case '{':
						event.getEvent().getNativeEvent().preventDefault();
						onFitToPageClicked(null);
						break;

					// Shift + delete
					case '.':
						if (!event.getEvent().isShiftKeyDown()) {
							break;
						}
						event.getEvent().getNativeEvent().preventDefault();
						onDeletButtonClicked(null);
						break;
					default:
						break;
				}
			}

		});
	}

	/**
	 * @param event
	 */
	private void addCtrlKeyDownEventHandlers(final RVKeyDownEvent event) {
		rightMouseFirstClickDone = false;
		ctrlKeyPressed = true;
		final int keyCode = event.getEvent().getNativeEvent().getKeyCode();
		switch (keyCode) {
			// Ctrl + t or T
			case 84:
				event.getEvent().getNativeEvent().preventDefault();
				onSplitButtonClicked(null);
				break;

			// Ctrl + m or M
			case 77:
				event.getEvent().getNativeEvent().preventDefault();
				onMoveButtonClicked(null);
				break;

			// Ctrl + d or D
			case 68:
				event.getEvent().getNativeEvent().preventDefault();
				onDuplicateButtonClicked(null);
				break;

			// Ctrl + 1 or !
			case 97: // for numpad key 1
			case 49: // for standard key 1 and !
				event.getEvent().getNativeEvent().preventDefault();
				if (zoomin.isEnabled()) {
					onZoomIn(null);
				}
				break;

			// Ctrl + 2 or @
			case 98: // for numpad key 2
			case 50: // for standard key 2 and @
				event.getEvent().getNativeEvent().preventDefault();
				if (zoomout.isEnabled()) {
					onZoomOut(null);
				}
				break;

			// Ctrl + r or R
			case 82:
				event.getEvent().getNativeEvent().preventDefault();
				rotateImage(null);
				break;

			// Ctrl + left arrow
			case 37:
				if (!event.getEvent().isShiftKeyDown()) {
					event.getEvent().getNativeEvent().preventDefault();
					imageScrollPanel.setHorizontalScrollPosition(imageScrollPanel.getHorizontalScrollPosition() - 20);
					break;
				}
				// Ctrl + right arrow
			case 39:
				event.getEvent().getNativeEvent().preventDefault();
				imageScrollPanel.setHorizontalScrollPosition(imageScrollPanel.getHorizontalScrollPosition() + 20);
				break;

			// Ctrl + up arrow
			case 38:
				event.getEvent().getNativeEvent().preventDefault();
				imageScrollPanel.setScrollPosition(imageScrollPanel.getScrollPosition() - 20);
				break;

			// Ctrl + down arrow
			case 40:
				event.getEvent().getNativeEvent().preventDefault();
				imageScrollPanel.setScrollPosition(imageScrollPanel.getScrollPosition() + 20);
				break;

			// CTRL + l or L

			case 76:
				event.getEvent().getNativeEvent().preventDefault();
				if (zoomLock.isLocked()) {
					zoomLock.unlock();
					getConformationDialog(LocaleDictionary.get().getConstantValue(ReviewValidateConstants.ZOOM_UNLOCKED),
							LocaleDictionary.get().getMessageValue(ReviewValidateMessages.ZOOM_UNLOCKED_SUCCESSFUL));
				} else {
					zoomLock.setLockPosition(imageScrollPanel.getHorizontalScrollPosition(), imageScrollPanel.getScrollPosition(),
							zoomCount, fitToPageImage);
					zoomLock.setLocked(true);
					getConformationDialog(LocaleDictionary.get().getConstantValue(ReviewValidateConstants.ZOOM_LOCKED),
							LocaleDictionary.get().getMessageValue(ReviewValidateMessages.ZOOM_LOCKED_SUCCESSFUL));
				}
				break;
			default:
				break;
		}
	}
	/**
	 * @param eventBus
	 */
	private void addThumbnailSelectionHandler(final HandlerManager eventBus) {
		eventBus.addHandler(ThumbnailSelectionEvent.type, new ThumbnailSelectionEventHandler() {
			@Override
			public void onThumbnailSelection(final ThumbnailSelectionEvent event) {
				// Overlay to be removed in case of drop down fields
				// where no corresponding match is found
				isRemoveOverlay = event.isRemoveOverlay();
				fitToPageImage = Boolean.FALSE;
				removeOverlays();
				coordinatesTypeList = null;
				coordinatesList = null;
				imageScrollPanel.scrollToTop();
				if (event.getPage() == null) {
					addNullPageHandler();
					return;
				}
				presenter.page = event.getPage();
				splitButton.setEnabled(true);
				deleteButton.setEnabled(true);
				duplicatePageButton.setEnabled(true);
				movePageButton.setEnabled(true);
				rotate.setEnabled(true);
				fitToPage.setEnabled(true);
				if (event.getField() != null && event.getField().getCoordinatesList() != null) {
					coordinatesTypeList = event.getField().getCoordinatesList();
					final List<Page> pagesInSelectedDocument = presenter.document.getPages().getPage();
					for (final Page page : pagesInSelectedDocument) {
						if (page.getIdentifier().equals(event.getField().getPage())) {
							presenter.page = page;
							break;
						}
					}
					if (event.getCoordinatesList() != null) {
						coordinatesList = event.getCoordinatesList();
					}
				}
				if (pageImage.getUrl() != null) {
					int index = pageImage.getUrl().indexOf("?");
					if (index == -1) {
						index = pageImage.getUrl().length() - 1;
					}
					final String url = pageImage.getUrl().substring(0, index);
					if (!presenter.page.isIsRotated()) {
						if (presenter.batchDTO.getAbsoluteURLFor(presenter.page.getDisplayFileName()).trim().equals(url)) {
							if (zoomLock.isLocked()) {
								processOverlay(coordinatesTypeList,

								0, 0, (float) (zoomCount * ZOOM_FACTOR));
								if (coordinatesTypeList != null) {
									setScroll();
								}
								zoomLock.lockPosition();
							} else {
								processOverlay(coordinatesTypeList,

								imageScrollPanel.getHorizontalScrollPosition(), imageScrollPanel.getScrollPosition(), zoomLock
										.getZoomFactor());
								if (coordinatesTypeList != null) {
									setScroll();
								}
							}
							return;
						} else {
							dimensionImage.setUrl(presenter.batchDTO.getAbsoluteURLFor(presenter.page.getDisplayFileName()).trim(),
									presenter.page.getDirection());
							tempImage.setUrl(presenter.batchDTO.getAbsoluteURLFor(presenter.page.getDisplayFileName()).trim(),
									presenter.page.getDirection());
						}
					} else {
						if (presenter.batchDTO.getAbsoluteURLForRotatedImage(presenter.page.getDisplayFileName(),
								presenter.page.getDirection().toString()).trim().equals(url)) {
							if (zoomLock.isLocked()) {
								processOverlay(coordinatesTypeList, 0, 0, (float) (zoomCount * ZOOM_FACTOR));
								if (coordinatesTypeList != null) {
									setScroll();
								}
								zoomLock.lockPosition();
							} else {
								processOverlay(coordinatesTypeList, imageScrollPanel.getHorizontalScrollPosition(), imageScrollPanel
										.getScrollPosition(), zoomLock.getZoomFactor());
								if (coordinatesTypeList != null) {
									setScroll();
								}
							}
							return;
						} else {
							dimensionImage.setUrl(presenter.batchDTO.getAbsoluteURLForRotatedImage(
									presenter.page.getDisplayFileName(), presenter.page.getDirection().toString()).trim(),
									presenter.page.getDirection());
							tempImage.setUrl(presenter.batchDTO.getAbsoluteURLForRotatedImage(presenter.page.getDisplayFileName(),
									presenter.page.getDirection().toString()).trim(), presenter.page.getDirection());
						}
					}
				}
			}
		});
	}

	/**
	 * 
	 */
	private void addNullPageHandler() {
		int index = pageImage.getUrl().indexOf("?");
		if (index == -1) {
			index = pageImage.getUrl().length() - 1;
		}
		final String url = pageImage.getUrl().substring(0, index);
		if (!presenter.page.isIsRotated()) {
			if (!presenter.batchDTO.getAbsoluteURLFor(presenter.page.getDisplayFileName()).trim().equals(url)) {
				dimensionImage.setUrl(presenter.batchDTO.getAbsoluteURLFor(presenter.page.getDisplayFileName()).trim(),
						ReviewValidateConstants.DIRECTION_NORTH);
				tempImage.setUrl(presenter.batchDTO.getAbsoluteURLFor(presenter.page.getDisplayFileName()).trim(),
						ReviewValidateConstants.DIRECTION_NORTH);
			}
		} else {
			if (!presenter.batchDTO.getAbsoluteURLForRotatedImage(presenter.page.getDisplayFileName(),
					presenter.page.getDirection().toString()).trim().equals(url)) {
				dimensionImage.setUrl(presenter.batchDTO.getAbsoluteURLForRotatedImage(presenter.page.getDisplayFileName(),
						presenter.page.getDirection().toString()).trim(), ReviewValidateConstants.DIRECTION_NORTH);
				tempImage.setUrl(presenter.batchDTO.getAbsoluteURLForRotatedImage(presenter.page.getDisplayFileName(),
						presenter.page.getDirection().toString()).trim(), ReviewValidateConstants.DIRECTION_NORTH);
			}
		}
	}
	private void getConformationDialog(final String title, final String message) {
		ScreenMaskUtility.maskScreen();
		final ConfirmationDialog confirmationDialog = ConfirmationDialogUtil.showConfirmationDialog(message, title, Boolean.TRUE);
		confirmationDialog.setPerformCancelOnEscape(true);
		confirmationDialog.addDialogListener(new DialogListener() {

			@Override
			public void onCancelClick() {
				confirmationDialog.hide();
				ScreenMaskUtility.unmaskScreen();
				presenter.setFocus();
			}

			@Override
			public void onOkClick() {
				confirmationDialog.hide();
				ScreenMaskUtility.unmaskScreen();
				presenter.setFocus();
			}
		});

	}

	public ScrollPanel getImageScrollPanel() {
		return imageScrollPanel;
	}

	public int getZoomCount() {
		return zoomCount;
	}

	private void setZoom(final Integer width, final Integer height) {

		final int zoomLockingFactor = zoomLock.getZoomFactor();
		if (zoomLock.isLocked()) {
			if (zoomLockingFactor != 1) {
				zoomCount = zoomLockingFactor;
				if (!zoomout.isEnabled()) {
					zoomout.setEnabled(Boolean.TRUE);
				}
				pageImage.setSize(EMPTY_STRING + width * Math.pow(ZOOM_FACTOR, (zoomCount - 1)), EMPTY_STRING + height
						* Math.pow(ZOOM_FACTOR, (zoomCount - 1)));
				processOverlay(coordinatesTypeList, 0, 0, (float) (zoomCount * ZOOM_FACTOR));
				if (coordinatesTypeList != null) {
					setScroll();
				}
				if (zoomCount >= ZOOM_COUNT_LIMIT) {
					zoomin.setEnabled(Boolean.FALSE);
				}
			} else {
				pageImage.setSize(width.toString(), height.toString());
				processOverlay(coordinatesTypeList, imageScrollPanel.getHorizontalScrollPosition(), imageScrollPanel
						.getScrollPosition(), 1);
				if (coordinatesTypeList != null) {
					setScroll();
				}
				zoomin.setEnabled(Boolean.TRUE);
				zoomout.setEnabled(Boolean.FALSE);
			}
		} else {

			double pageWidth = width;
			double pageHeight = height;
			for (int tempZoomCount = 2; tempZoomCount <= zoomCount; tempZoomCount++) {
				pageWidth = pageWidth * ZOOM_FACTOR;
				pageHeight = pageHeight * ZOOM_FACTOR;
			}
			pageImage.setSize(EMPTY_STRING + pageWidth, EMPTY_STRING + pageHeight);
			if (zoomCount == 1) {
				processOverlay(coordinatesTypeList, 0, 0, 1);
			} else {
				processOverlay(coordinatesTypeList, 0, 0, (float) (zoomCount * ZOOM_FACTOR));
			}
			if (coordinatesTypeList != null) {
				setScroll();
			}

			if (zoomCount >= ZOOM_COUNT_LIMIT) {
				zoomin.setEnabled(Boolean.FALSE);

			} else {
				zoomin.setEnabled(Boolean.TRUE);
			}
			if (zoomCount <= 1) {
				zoomout.setEnabled(Boolean.FALSE);
			} else {
				zoomout.setEnabled(Boolean.TRUE);
			}
		}
		fitToPage.setEnabled(true);
		zoomLock.lockPosition();
	}

	/**
	 * This method removes existing overlay and calls getQuadrant method to find direction of mouse movement.
	 * 
	 * @param initialCoordinate {@link PointCoordinate}
	 * @param finalCoordinate {@link PointCoordinate}
	 */
	public void doOverlay(final PointCoordinate initialCoordinate, final PointCoordinate finalCoordinate) {
		removeOverlay();
		getQuadrantValues(initialCoordinate.getxCoordinate(), initialCoordinate.getyCoordinate(), finalCoordinate.getxCoordinate(),
				finalCoordinate.getyCoordinate());
	}

	/**
	 * This method draws an overlay on screen as the mouse is moved on image.
	 * 
	 * @param x0Coord {@link Double} initial X coordinate
	 * @param y0Coord {@link Double} final Y coordinate
	 * @param x1Coord {@link Double} initial X coordinate
	 * @param y1Coord {@link Double} final Y coordinate
	 * @param zoomFactor {@link Double}
	 */
	public void processOverlay(final double x0Coord, final double y0Coord, final double x1Coord, final double y1Coord,
			final double zoomFactor) {
		final double aspectRatio = (double) (pageImage.getWidth()) / (double) (originalWidth);
		final double xScrollPosition = imageScrollPanel.getHorizontalScrollPosition();
		final double yScrollPosition = imageScrollPanel.getScrollPosition();
		// double yScrollingAdder = 75;
		x0Coordinate = (double) (x0Coord * aspectRatio) + (double) (X_FACTOR * getViewPortWidth());
		x1Coordinate = (double) (x1Coord * aspectRatio) + (double) (X_FACTOR * getViewPortWidth());
		y0Coordinate = (double) (y0Coord * aspectRatio) + (double) (Y_FACTOR * getViewPortHeight());
		y1Coordinate = (double) (y1Coord * aspectRatio) + (double) (Y_FACTOR * getViewPortHeight());
		if (xScrollPosition > (x0Coordinate - (X_FACTOR * getViewPortWidth())) || x1Coordinate - xScrollPosition > getViewPortWidth()) {
			return;
		}
		if (xScrollPosition <= x1Coordinate && yScrollPosition <= y1Coordinate) {
			x0Coordinate -= xScrollPosition;
			x1Coordinate -= xScrollPosition;
			if (yScrollPosition > (y0Coordinate - (0.24 * getViewPortHeight()))) {
				y0Coordinate = (0.24 * getViewPortHeight());
			} else if (y0Coordinate - yScrollPosition > (0.96 * getViewPortHeight())) {
				y0Coordinate = 0.96 * getViewPortHeight();
			} else {
				y0Coordinate -= yScrollPosition;
			}
			if (yScrollPosition > (y1Coordinate - (0.24 * getViewPortHeight()))) {
				y1Coordinate = (0.24 * getViewPortHeight());
			} else if (y1Coordinate - yScrollPosition > (0.96 * getViewPortHeight())) {
				y1Coordinate = 0.96 * getViewPortHeight();
			} else {
				y1Coordinate -= yScrollPosition;
			}
			removeOverlay();
			doOverlay(x0Coordinate, x1Coordinate, y0Coordinate, y1Coordinate, zoomFactor);
		}
	}

	/**
	 * This method finds the quadrant according to direction in which mouse is moved and calls processOverlay to draw ocerlay on image.
	 * 
	 * @param initialXCoor {@link Integer}
	 * @param initialYCoor {@link Integer}
	 * @param finalXCoor {@link Integer}
	 * @param finalYCoor {@link Integer}
	 */
	public void getQuadrantValues(final int initialXCoor, final int initialYCoor, final int finalXCoor, final int finalYCoor) {
		final double initialX = initialXCoor;
		final double initialY = initialYCoor;
		double finalX = finalXCoor;
		double finalY = finalYCoor;
		if (initialX <= finalX && initialY <= finalY) {
			// it is in the III quadrant.
			if (zoomCount == 1) {
				finalX = finalXCoor - CROSS_HAIR_REDUCTION_FACTOR;
				finalY = finalY - CROSS_HAIR_REDUCTION_FACTOR - 7;
			}
			if (zoomCount == 2) {
				finalX = finalXCoor - CROSS_HAIR_REDUCTION_FACTOR - 8;
			} else if (zoomCount == 3) {
				finalX = finalXCoor - CROSS_HAIR_REDUCTION_FACTOR - 7;
			}
			processOverlay(initialX, initialY, finalX, finalY - CROSS_HAIR_REDUCTION_FACTOR, 1);

		} else if (initialX >= finalX && initialY <= finalY) {
			// it is in IV quadrant.
			processOverlay(finalX, initialY, initialX, finalY, 1);
		} else if (initialX <= finalX && initialY >= finalY) {
			// it is in II quadrant.
			if (zoomCount == 2) {
				finalX = finalX - CROSS_HAIR_REDUCTION_FACTOR - 5;
			}
			processOverlay(initialX, finalY, finalX - CROSS_HAIR_REDUCTION_FACTOR, initialYCoor - CROSS_HAIR_REDUCTION_FACTOR, 1);
		} else if (initialX >= finalX && initialY >= finalY) {
			// it is in I quadrant.
			processOverlay(finalX, finalY, initialX, initialY, 1);
		} else {
			processOverlay(initialX, initialY, finalX, finalY, 1);
		}
	}

	public void setZoomCount(final int zoomCount) {
		this.zoomCount = zoomCount;
	}

	public void setFitToPageImage(final boolean fitToPageImage) {
		this.fitToPageImage = fitToPageImage;
	}

	public native void doOverlay(final double x0Coordinate, final double x1Coordinate, final double y0Coordinate,
			final double y1Coordinate, final double zoomFactor) /*-{
																return $wnd.doOverlay(x0Coordinate, x1Coordinate, y0Coordinate, y1Coordinate, zoomFactor);
																}-*/;

	public native void removeOverlay() /*-{
										return $wnd.removeOverlay();
										}-*/;

	public void clearPanel() {
		splitButton.setEnabled(false);
		deleteButton.setEnabled(false);
		duplicatePageButton.setEnabled(false);
		movePageButton.setEnabled(false);
		rotate.setEnabled(false);
		fitToPage.setEnabled(false);
		this.pageImage.setVisible(false);
	}
	
	/**
	 * The <code>clearInitialPageDimensionsMap</code> method clears the initial dimension
	 * map.
	 */
	public void clearInitialPageDimensionsMap() {
		if (null != initialPageDimensions) {
			initialPageDimensions.clear();
		}
	}

}
