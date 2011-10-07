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

package com.ephesoft.dcma.gwt.rv.client.view;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.ephesoft.dcma.batch.schema.Batch;
import com.ephesoft.dcma.batch.schema.Coordinates;
import com.ephesoft.dcma.batch.schema.Direction;
import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.batch.schema.Page;
import com.ephesoft.dcma.batch.schema.Field.CoordinatesList;
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
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ScrollPanel;

public class ImageOverlayPanel extends RVBasePanel {

	interface Binder extends UiBinder<DockLayoutPanel, ImageOverlayPanel> {

	}

	@UiField
	ScrollPanel imageScrollPanel;
	@UiField
	RotatableImage pageImage;
	@UiField
	Button splitButton;
	@UiField
	Button deleteButton;
	@UiField
	Button duplicatePageButton;
	@UiField
	Button movePageButton;
	@UiField
	Button zoomin;
	@UiField
	Button zoomout;
	@UiField
	Button rotate;
	@UiField
	Button fitToPage;
	@UiField
	RotatableImage tempImage;
	@UiField
	DockLayoutPanel imageOverlayViewPanel;

	private Timer timeout = null;

	private boolean isRemoveOverlay = false;

	private CoordinatesList coordinatesTypeList;

	private List<Coordinates> coordinatesList;

	private double x0, x1, y0, y1;
	private Integer originalWidth;
	private double zoomFactor = 1.25;
	private int zoomCount = 1;
	private int zoomCountLimit = 3;
	private boolean fitToPageImage;
	private Integer heightBeforeFitToHeight;
	private Integer widthBeforeFitToWidth;
	private Integer clickCount = 0;
	private boolean shiftKeyPressed = false;
	private boolean ctrlKeyPressed = false;
	private double xFactor = 0.443;
	private double yFactor = 0.243;
	private List<PointCoordinate> pointCoordinates;
	private PointCoordinate pointCoordinate1 = new PointCoordinate();
	private PointCoordinate pointCoordinate2 = new PointCoordinate();
	private Integer coordinateIdentifier = 0;
	private ZoomLock zoomLock;
	private boolean isFirstClick = true;
	private double crosshairReductionFactor = 5;

	private static final Binder BINDER = GWT.create(Binder.class);

	public ImageOverlayPanel() {
		DockLayoutPanel layoutPanel = BINDER.createAndBindUi(this);
		initWidget(layoutPanel);
		movePageButton.setStyleName("move_button");
		splitButton.setStyleName("split_button");
		splitButton.setTitle(LocaleDictionary.get().getConstantValue(ReviewValidateConstants.tooltip_split));
		deleteButton.setStyleName("delete_button");
		deleteButton.setTitle(LocaleDictionary.get().getConstantValue(ReviewValidateConstants.tooltip_delete));
		duplicatePageButton.setStyleName("duplicate_button");
		duplicatePageButton.setTitle(LocaleDictionary.get().getConstantValue(ReviewValidateConstants.tooltip_duplicate));
		movePageButton.setStyleName("move_page_button");
		movePageButton.setTitle(LocaleDictionary.get().getConstantValue(ReviewValidateConstants.tooltip_move));
		zoomin.setStyleName("zoom_in");
		zoomin.setTitle(LocaleDictionary.get().getConstantValue(ReviewValidateConstants.tooltip_zoom_in));
		zoomout.setStyleName("zoom_out");
		zoomout.setTitle(LocaleDictionary.get().getConstantValue(ReviewValidateConstants.tooltip_zoom_out));
		rotate.setStyleName("rotate_button");
		fitToPage.setStyleName("fit_to_page");
		fitToPage.setTitle(LocaleDictionary.get().getConstantValue(ReviewValidateConstants.tooltip_fit_to_page));
		rotate.setTitle(LocaleDictionary.get().getConstantValue(ReviewValidateConstants.tooltip_rotate));
		tempImage.setVisible(false);
		zoomLock = new ZoomLock(this);
		pageImage.setUrl("");
		tempImage.addLoadHandler(new LoadHandler() {

			@Override
			public void onLoad(LoadEvent arg0) {
				int width = tempImage.getWidth();
				String currentBrowser = getUserAgent();
				if (currentBrowser != null && currentBrowser.length() > 0 && currentBrowser.contains("msie")) {
					if (width <= 0) {
						Double screenWidth = 0.53 * getViewPortWidth();
						width = screenWidth.intValue();
					}
				}
				pageImage.setWidth(String.valueOf(width));
				int height = tempImage.getHeight();
				pageImage.setHeight(String.valueOf(height));
				pageImage.setVisible(false);
				removeOverlays();
				pageImage.setUrl(tempImage.getUrl().substring(0, tempImage.getUrl().indexOf("?")), presenter.page.getDirection());
				pageImage.setStyleName("thumbnailHighlighted");
			}
		});
		pageImage.addMouseUpHandler(new MouseUpHandler() {

			@Override
			public void onMouseUp(MouseUpEvent mouseDown) {
				double aspectRatio = (double) (pageImage.getWidth()) / (double) (originalWidth);
				int xCoordinate = (int) Math.round(mouseDown.getX() / aspectRatio);
				int yCoordinate = (int) Math.round(mouseDown.getY() / aspectRatio);
				if (presenter.isTableView() && presenter.isManualTableExtraction()) {
					if (isFirstClick) {
						pageImage.setStyleName("pointer");
						pointCoordinate1.setxCoordinate(xCoordinate);
						pointCoordinate1.setyCoordinate(yCoordinate);
						isFirstClick = false;
					} else {
						pageImage.removeStyleName("pointer");
						if (xCoordinate < pointCoordinate1.getxCoordinate()) {
							int temp = pointCoordinate1.getxCoordinate();
							pointCoordinate1.setxCoordinate(xCoordinate);
							xCoordinate = temp;
						}
						if (yCoordinate < pointCoordinate1.getyCoordinate()) {
							int temp = pointCoordinate1.getyCoordinate();
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
					if (shiftKeyPressed) {
						if (clickCount == 0) {
							pointCoordinate1.setxCoordinate(xCoordinate);
							pointCoordinate1.setyCoordinate(yCoordinate);
							clickCount = 1;
						} else {
							pointCoordinate2.setxCoordinate(xCoordinate);
							pointCoordinate2.setyCoordinate(yCoordinate);
							presenter.getHOCRContent(pointCoordinate1, pointCoordinate2);
						}
					} else if (ctrlKeyPressed) {
						if (clickCount == 0 || pointCoordinates == null) {
							pointCoordinates = new ArrayList<PointCoordinate>();
						}
						PointCoordinate pointCoordinate = new PointCoordinate();
						pointCoordinate.setxCoordinate(xCoordinate);
						pointCoordinate.setyCoordinate(yCoordinate);
						pointCoordinates.add(pointCoordinate);
						presenter.getHOCRContent(pointCoordinates);
						clickCount++;
					} else {
						pointCoordinates = new ArrayList<PointCoordinate>(1);
						PointCoordinate pointCoordinate = new PointCoordinate();
						pointCoordinate.setxCoordinate(xCoordinate);
						pointCoordinate.setyCoordinate(yCoordinate);
						pointCoordinates.add(pointCoordinate);
						presenter.getHOCRContent(pointCoordinates);
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
			public void onLoad(LoadEvent arg0) {
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
			public void onScroll(ScrollEvent arg0) {
				processOverlay(coordinatesTypeList, pageImage, imageScrollPanel.getHorizontalScrollPosition(), imageScrollPanel
						.getScrollPosition(), 1);
			}
		});

		imageOverlayViewPanel.addStyleName("right1");

		pageImage.addMouseMoveHandler(new MouseMoveHandler() {

			@Override
			public void onMouseMove(final MouseMoveEvent paramMouseMoveEvent) {
				if (presenter.isManualTableExtraction()) {
					double aspectRatio = (double) (pageImage.getWidth()) / (double) (originalWidth);
					int xCoordinate = (int) Math.round(paramMouseMoveEvent.getX() / aspectRatio);
					int yCoordinate = (int) Math.round(paramMouseMoveEvent.getY() / aspectRatio);
					pointCoordinate2.setxCoordinate(xCoordinate);
					pointCoordinate2.setyCoordinate(yCoordinate);
					if (!isFirstClick) {
						// pageImage.addStyleName("pointer");
						doOverlay(pointCoordinate1, pointCoordinate2);
					} else {
						// pageImage.removeStyleName("pointer");
					}
				}
			}
		});
	}

	private void processOverlay(CoordinatesList coordinatesTypeList, Image image, int xScrollPosition, int yScrollPosition,
			float zoomFactor) {
		removeOverlays();
		if (fitToPageImage) {
			return;
		}
		if (isRemoveOverlay) {
			return;
		}
		coordinateIdentifier = 0;
		if (coordinatesTypeList != null && !coordinatesTypeList.getCoordinates().isEmpty()) {
			for (Coordinates coordinatesType : coordinatesTypeList.getCoordinates()) {
				processOverlay(coordinateIdentifier++, coordinatesType, pageImage, xScrollPosition, yScrollPosition, zoomFactor);
			}
		}
		if (coordinatesList != null) {
			processOverlay(coordinatesList, xScrollPosition, yScrollPosition, zoomFactor);
		}

	}

	private void processOverlay(Integer id, Coordinates coordinatesType, Image image, int xScrollPosition, int yScrollPosition,
			float zoomFactor) {
		double aspectRatio = (double) (pageImage.getWidth()) / (double) (originalWidth);
		StringBuffer coordinateIdentifier = new StringBuffer();
		coordinateIdentifier.append(id);
		if (coordinatesType != null) {
			x0 = coordinatesType.getX0().intValue();
			x1 = coordinatesType.getX1().intValue();
			y0 = coordinatesType.getY0().intValue();
			y1 = coordinatesType.getY1().intValue();

			x0 = (double) (x0 * aspectRatio) + (double) (xFactor * getViewPortWidth());
			x1 = (double) (x1 * aspectRatio) + (double) (xFactor * getViewPortWidth());
			y0 = (double) (y0 * aspectRatio) + (double) (yFactor * getViewPortHeight());
			y1 = (double) (y1 * aspectRatio) + (double) (yFactor * getViewPortHeight());
			if (yScrollPosition > (y1 - (0.255 * getViewPortHeight())) || xScrollPosition > (x0 - (0.443 * getViewPortWidth()))
					|| x1 - xScrollPosition > getViewPortWidth() || y1 - yScrollPosition > getViewPortHeight()) {
				removeOverlayById(coordinateIdentifier.toString());
				return;
			}
			if (xScrollPosition == 0 && yScrollPosition == 0 && zoomFactor == 1) {
				removeOverlayById(coordinateIdentifier.toString());
				doOverlay(coordinateIdentifier.toString(), x0, x1, y0, y1, zoomFactor);
			} else if (xScrollPosition <= x1 && yScrollPosition <= y1) {
				x0 -= xScrollPosition;
				x1 -= xScrollPosition;
				y0 -= yScrollPosition;
				y1 -= yScrollPosition;
				removeOverlayById(coordinateIdentifier.toString());
				doOverlay(coordinateIdentifier.toString(), x0, x1, y0, y1, zoomFactor);
			} else {
				removeOverlayById(coordinateIdentifier.toString());
			}
		}
	}

	public void processOverlay(List<Coordinates> coordinatesList, int xScrollPosition, int yScrollPosition, float zoomFactor) {
		for (Coordinates coordinates : coordinatesList) {
			double aspectRatio = (double) (pageImage.getWidth()) / (double) (originalWidth);
			if (coordinates != null) {
				x0 = coordinates.getX0().intValue();
				x1 = coordinates.getX1().intValue();
				y0 = coordinates.getY0().intValue();
				y1 = coordinates.getY1().intValue();
				x0 = (double) (x0 * aspectRatio) + (double) (xFactor * getViewPortWidth());
				x1 = (double) (x1 * aspectRatio) + (double) (xFactor * getViewPortWidth());
				y0 = (double) (y0 * aspectRatio) + (double) (yFactor * getViewPortHeight());
				y1 = (double) (y1 * aspectRatio) + (double) (yFactor * getViewPortHeight());
				if (yScrollPosition > (y1 - (0.24 * getViewPortHeight())) || xScrollPosition > (x0 - (0.443 * getViewPortWidth()))
						|| x1 - xScrollPosition > getViewPortWidth() || y1 - yScrollPosition > getViewPortHeight()) {
					return;
				}
				if (xScrollPosition == 0 && yScrollPosition == 0 && zoomFactor == 1) {
					doOverlayForRow(x0, x1, y0, y1, zoomFactor);
				} else if (xScrollPosition <= x1 && yScrollPosition <= y1) {
					x0 -= xScrollPosition;
					x1 -= xScrollPosition;
					y0 -= yScrollPosition;
					y1 -= yScrollPosition;
					doOverlayForRow(x0, x1, y0, y1, zoomFactor);
				}
			}
		}
	}

	private native int getViewPortHeightForFirefox() /*-{
														return $wnd.getViewPortHeight();
														}-*/;

	private int getViewPortHeight() {
		int viewPortHeight = 0;
		String currentBrowser = getUserAgent();
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

	private native void doOverlay(String id, double x0, double x1, double y0, double y1, double zoomFactor) /*-{
																											return $wnd.doOverlayById(id,x0, x1, y0, y1, zoomFactor);
																											}-*/;

	private native void doOverlayForRow(double x0, double x1, double y0, double y1, double zoomFactor) /*-{
																										return $wnd.doOverlayForRow(x0, x1, y0, y1, zoomFactor);
																										}-*/;

	private native void removeAllOverlays() /*-{
											return $wnd.removeOverlay();
											}-*/;

	private void removeOverlays() {
		removeAllOverlays();
		for (int i = 0; i < coordinateIdentifier; i++) {
			StringBuffer id = new StringBuffer();
			id.append(i);
			removeOverlayById(id.toString());
		}
	}

	private native void removeOverlayById(String id) /*-{
														return $wnd.removeOverlayById(id);
														}-*/;

	public static native String getUserAgent() /*-{
												return navigator.userAgent.toLowerCase();
												}-*/;

	@UiHandler("duplicatePageButton")
	public void OnDuplicateButtonClicked(ClickEvent event) {
		final ConfirmationDialog confirmationDialog = new ConfirmationDialog();
		confirmationDialog.setMessage(LocaleDictionary.get().getMessageValue(ReviewValidateMessages.msg_overlayPanel_duplicate,
				presenter.page.getIdentifier()));
		confirmationDialog.setDialogTitle(LocaleDictionary.get().getConstantValue(ReviewValidateConstants.title_duplicate));
		confirmationDialog.addDialogListener(new DialogListener() {

			@Override
			public void onOkClick() {
				Batch batch = presenter.batchDTO.getBatch();
				ScreenMaskUtility.maskScreen();
				presenter.rpcService.duplicatePageOfDocument(batch, presenter.document.getIdentifier(),
						presenter.page.getIdentifier(), new AsyncCallback<BatchDTO>() {

							@Override
							public void onSuccess(BatchDTO batchDTO) {
								presenter.batchDTO = batchDTO;
								presenter.document = batchDTO.getDocumentById(presenter.document.getIdentifier());
								presenter.page = presenter.document.getPages().getPage().get(
										presenter.document.getPages().getPage().size() - 1);
								fireEvent(new TreeRefreshEvent(batchDTO, presenter.document, presenter.page));
								ScreenMaskUtility.unmaskScreen();
							}

							@Override
							public void onFailure(Throwable arg0) {
								ScreenMaskUtility.unmaskScreen();
								ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
										ReviewValidateMessages.msg_overlayPanel_duplicate_error, presenter.page.getIdentifier()));
							}
						});
			}

			@Override
			public void onCancelClick() {
				confirmationDialog.hide();
				presenter.setFocus();
			}
		});
		confirmationDialog.show();
		confirmationDialog.center();
		confirmationDialog.okButton.setFocus(true);

	}

	@UiHandler("deleteButton")
	public void onDeletButtonClicked(ClickEvent event) {
		final Batch batch = presenter.batchDTO.getBatch();
		List<Document> documents = batch.getDocuments().getDocument();
		if (documents.size() <= 1 && (documents.get(0).getPages().getPage().size() <= 1)) {
			ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
					ReviewValidateMessages.msg_overlayPanel_last_page_delete_error));
			return;
		}
		ConfirmationDialog confirmationDialog = new ConfirmationDialog();
		confirmationDialog.setMessage(LocaleDictionary.get().getMessageValue(ReviewValidateMessages.msg_overlayPanel_delete,
				presenter.page.getIdentifier()));
		confirmationDialog.setDialogTitle(LocaleDictionary.get().getConstantValue(ReviewValidateConstants.title_delete));
		confirmationDialog.addDialogListener(new DialogListener() {

			@Override
			public void onOkClick() {
				ScreenMaskUtility.maskScreen();

				presenter.rpcService.deletePageOfDocument(batch, presenter.document.getIdentifier(), presenter.page.getIdentifier(),
						new AsyncCallback<BatchDTO>() {

							@Override
							public void onFailure(Throwable caught) {
								ScreenMaskUtility.unmaskScreen();
								ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
										ReviewValidateMessages.msg_overlayPanel_delete_error, presenter.page.getIdentifier(),
										caught.getMessage()));
							}

							@Override
							public void onSuccess(BatchDTO batchDTO) {
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
		confirmationDialog.show();
		confirmationDialog.center();
		confirmationDialog.okButton.setFocus(true);
	}

	@UiHandler("splitButton")
	public void onSplitButtonClicked(ClickEvent event) {
		boolean isFirstPage = isFirstPage(presenter.batchDTO.getDocumentForPage(presenter.page));
		if (isFirstPage) {
			final ConfirmationDialog confirmationDialog = new ConfirmationDialog(true);
			confirmationDialog.setMessage(LocaleDictionary.get().getMessageValue(
					ReviewValidateMessages.msg_overlayPanel_split_fst_page));
			confirmationDialog.setDialogTitle(LocaleDictionary.get().getConstantValue(ReviewValidateConstants.title_split));
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
			confirmationDialog.center();
			confirmationDialog.show();
			confirmationDialog.okButton.setFocus(true);
			return;
		}
		ConfirmationDialog confirmationDialog = new ConfirmationDialog();
		confirmationDialog.setMessage(LocaleDictionary.get().getMessageValue(ReviewValidateMessages.msg_overlayPanel_split,
				presenter.document.getIdentifier(), presenter.page.getIdentifier()));
		confirmationDialog.setDialogTitle(LocaleDictionary.get().getConstantValue(ReviewValidateConstants.title_split_doc));
		confirmationDialog.addDialogListener(new DialogListener() {

			@Override
			public void onOkClick() {
				Batch batch = presenter.batchDTO.getBatch();
				ScreenMaskUtility.maskScreen();
				presenter.rpcService.splitDocument(batch, presenter.document.getIdentifier(), presenter.page.getIdentifier(),
						new AsyncCallback<BatchDTO>() {

							@Override
							public void onFailure(Throwable caught) {
								ScreenMaskUtility.unmaskScreen();
								ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
										ReviewValidateMessages.msg_overlayPanel_split_error, presenter.document.getIdentifier(),
										presenter.page.getIdentifier(), caught.getMessage()));
							}

							@Override
							public void onSuccess(BatchDTO batchDTO) {
								presenter.batchDTO = batchDTO;
								List<Document> docs = batchDTO.getBatch().getDocuments().getDocument();
								int docIndex = getDocumentIndex(docs, presenter.document.getIdentifier());
								presenter.document = docs.get(docIndex + 1);
								presenter.page = presenter.document.getPages().getPage().get(0);
								fireEvent(new TreeRefreshEvent(batchDTO, presenter.document, presenter.page));
								ScreenMaskUtility.unmaskScreen();
							}

							private int getDocumentIndex(List<Document> docs, String identifier) {
								int index = 0;
								for (Document document : docs) {
									if (document.getIdentifier().equalsIgnoreCase(identifier)) {
										return index;
									}
									index++;
								}
								return -1;
							}
						});
			}

			@Override
			public void onCancelClick() {
				presenter.setFocus();
				// confirmationDialog.hide();
			}
		});
		confirmationDialog.show();
		confirmationDialog.center();
		confirmationDialog.okButton.setFocus(true);

	}

	private boolean isFirstPage(Document documentType) {
		return documentType.getPages().getPage().get(0).getIdentifier().equals(presenter.page.getIdentifier());
	}

	@UiHandler("movePageButton")
	public void onMoveButtonClicked(ClickEvent event) {
		PageMoveDialog dialogBox = new PageMoveDialog();
		dialogBox.setDialogTitle(LocaleDictionary.get().getConstantValue(ReviewValidateConstants.title_move));
		// List<String> listOfDocumentId = getAllDocumentIdForMove(presenter.document.getIdentifier());
		List<String> listOfDocumentId = getAllDocumentIdForMove();
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
		Batch batch = presenter.batchDTO.getBatch();
		List<Document> documents = batch.getDocuments().getDocument();
		List<String> list = new LinkedList<String>();
		for (int i = 0; i < documents.size(); i++) {
			list.add(documents.get(i).getIdentifier());
		}
		return list;
	}

	private void loadImage() {
		int screenWidth = getViewPortWidth();
		Integer imageWidth = screenWidth * 53 / 100;
		removeOverlays();
		originalWidth = pageImage.getWidth();
		Integer imageHeight = pageImage.getHeight() * imageWidth / pageImage.getWidth();
		ImageOverlayPanel.this.pageImage.setVisible(true);
		setZoom(imageWidth, imageHeight);
	}

	@UiHandler("fitToPage")
	public void onFitToPageClicked(ClickEvent event) {
		removeOverlays();
		if (!fitToPageImage) {
			int viewPortHeight = getViewPortHeight();
			int viewPortWidth = getViewPortWidth();
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
	public void onZoomIn(ClickEvent event) {
		if (!fitToPageImage) {
			zoomCount++;
			if (!zoomout.isEnabled()) {
				zoomout.setEnabled(Boolean.TRUE);
			}
			pageImage.setSize("" + pageImage.getWidth() * zoomFactor, "" + pageImage.getHeight() * zoomFactor);
			processOverlay(coordinatesTypeList, pageImage, 0, 0, (float) (zoomCount * zoomFactor));
			setScroll();
			if (zoomCount == zoomCountLimit) {
				zoomin.setEnabled(Boolean.FALSE);
				return;
			}
		}
	}

	private void setScroll() {
		// Overlay to be removed in case of drop down fields where no
		// corresponding match is found
		if (isRemoveOverlay) {
			return;
		}
		if (x0 - 10 > (0.45 * getViewPortWidth())) {
			imageScrollPanel.setHorizontalScrollPosition((int) (x0 - (0.45 * getViewPortWidth()) - 10));
		} else {
			imageScrollPanel.setHorizontalScrollPosition((int) (x0 - (0.45 * getViewPortWidth())));
		}
		if (y0 - 10 > (0.25 * getViewPortHeight())) {
			imageScrollPanel.setScrollPosition((int) (y0 - (0.25 * getViewPortHeight()) - 10));
		} else {
			imageScrollPanel.setScrollPosition((int) (y0 - (0.25 * getViewPortHeight())));
		}
	}

	@UiHandler("zoomout")
	public void onZoomOut(ClickEvent event) {
		if (!fitToPageImage) {
			zoomCount--;
			if (!zoomin.isEnabled()) {
				zoomin.setEnabled(Boolean.TRUE);
			}
			pageImage.setSize("" + pageImage.getWidth() / zoomFactor, "" + pageImage.getHeight() / zoomFactor);
			processOverlay(coordinatesTypeList, pageImage, 0, 0, (float) (1 / ((zoomCountLimit - zoomCount) * zoomFactor)));
			setScroll();
			if (zoomCount == 1) {
				zoomout.setEnabled(Boolean.FALSE);
				return;
			}
		}
	}

	@UiHandler("rotate")
	public void rotateImage(ClickEvent event) {
		rotate.setEnabled(false);
		ScreenMaskUtility.maskScreen();
		presenter.rpcService.roatateImage(presenter.batchDTO.getBatch(), presenter.page, presenter.document.getIdentifier(),
				new AsyncCallback<Page>() {

					@Override
					public void onFailure(Throwable arg0) {
						ScreenMaskUtility.unmaskScreen();
						rotate.setEnabled(true);
						ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
								ReviewValidateMessages.msg_overlayPanel_rotate_error));
					}

					@Override
					public void onSuccess(Page arg0) {
						presenter.page.setDirection(arg0.getDirection());
						presenter.page.setIsRotated(arg0.isIsRotated());
						List<Document> docList = presenter.batchDTO.getBatch().getDocuments().getDocument();
						for (Document doc : docList) {
							if (presenter.document.getIdentifier().equals(doc.getIdentifier())) {
								List<Page> pageList = doc.getPages().getPage();
								int index = 0;
								for (Page pg : pageList) {
									if (pg.getIdentifier().equals(presenter.page.getIdentifier())) {
										pageList.set(index, presenter.page);// presenter.page;
									}
									index++;
								}
								doc = presenter.document;
							}
						}
						Direction direction = presenter.page.getDirection();
						String absoluteURLFor = presenter.batchDTO.getAbsoluteURLForRotatedImage(presenter.page.getDisplayFileName(),
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
	public void injectEvents(HandlerManager eventBus) {
		eventBus.addHandler(ThumbnailSelectionEvent.TYPE, new ThumbnailSelectionEventHandler() {

			@Override
			public void onThumbnailSelection(ThumbnailSelectionEvent event) {
				// Overlay to be removed in case of drop down fields
				// where no corresponding match is found
				isRemoveOverlay = event.isRemoveOverlay();

				removeOverlays();
				coordinatesTypeList = null;
				coordinatesList = null;
				if (event.getPage() == null) {
					int index = pageImage.getUrl().indexOf("?");
					if (index == -1) {
						index = pageImage.getUrl().length() - 1;
					}
					String url = pageImage.getUrl().substring(0, index);
					if (!presenter.page.isIsRotated()) {
						if (!presenter.batchDTO.getAbsoluteURLFor(presenter.page.getDisplayFileName()).trim().equals(url)) {
							tempImage
									.setUrl(presenter.batchDTO.getAbsoluteURLFor(presenter.page.getDisplayFileName()).trim(), "North");
						}
					} else {
						if (!presenter.batchDTO.getAbsoluteURLForRotatedImage(presenter.page.getDisplayFileName(),
								presenter.page.getDirection().toString()).trim().equals(url)) {
							tempImage.setUrl(presenter.batchDTO.getAbsoluteURLForRotatedImage(presenter.page.getDisplayFileName(),
									presenter.page.getDirection().toString()).trim(), "North");
						}
					}
					return;
				}
				// fitToPageImage = false;
				presenter.page = event.getPage();

				splitButton.setEnabled(true);
				deleteButton.setEnabled(true);
				duplicatePageButton.setEnabled(true);
				movePageButton.setEnabled(true);
				rotate.setEnabled(true);
				fitToPage.setEnabled(true);

				if (event.getField() != null && event.getField().getCoordinatesList() != null) {
					coordinatesTypeList = event.getField().getCoordinatesList();
					List<Page> pagesInSelectedDocument = presenter.document.getPages().getPage();
					for (Page page : pagesInSelectedDocument) {
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
					String url = pageImage.getUrl().substring(0, index);
					if (!presenter.page.isIsRotated()) {
						if (presenter.batchDTO.getAbsoluteURLFor(presenter.page.getDisplayFileName()).trim().equals(url)) {
							if (zoomLock.isLocked()) {
								processOverlay(coordinatesTypeList, pageImage, 0, 0, (float) (zoomCount * zoomFactor));
								if (coordinatesTypeList != null) {
									setScroll();
								}
								zoomLock.lockPosition();
							} else {
								processOverlay(coordinatesTypeList, pageImage, imageScrollPanel.getHorizontalScrollPosition(),
										imageScrollPanel.getScrollPosition(), zoomLock.getZoomFactor());
								if (coordinatesTypeList != null) {
									setScroll();
								}
							}
							return;
						} else {
							tempImage.setUrl(presenter.batchDTO.getAbsoluteURLFor(presenter.page.getDisplayFileName()).trim(),
									presenter.page.getDirection());
						}
					} else {
						if (presenter.batchDTO.getAbsoluteURLForRotatedImage(presenter.page.getDisplayFileName(),
								presenter.page.getDirection().toString()).trim().equals(url)) {
							if (zoomLock.isLocked()) {
								processOverlay(coordinatesTypeList, pageImage, 0, 0, (float) (zoomCount * zoomFactor));
								if (coordinatesTypeList != null) {
									setScroll();
								}
								zoomLock.lockPosition();
							} else {
								processOverlay(coordinatesTypeList, pageImage, imageScrollPanel.getHorizontalScrollPosition(),
										imageScrollPanel.getScrollPosition(), zoomLock.getZoomFactor());
								if (coordinatesTypeList != null) {
									setScroll();
								}
							}
							return;
						} else {
							tempImage.setUrl(presenter.batchDTO.getAbsoluteURLForRotatedImage(presenter.page.getDisplayFileName(),
									presenter.page.getDirection().toString()).trim(), presenter.page.getDirection());
						}
					}
				}
			}
		});

		eventBus.addHandler(RVKeyDownEvent.TYPE, new RVKeyDownEventHandler() {

			@Override
			public void onKeyDown(RVKeyDownEvent event) {
				if (event.getEvent().isShiftKeyDown()) {
					shiftKeyPressed = true;
					// clickCount = 0;
				}
				if (event.getEvent().isControlKeyDown()) {
					ctrlKeyPressed = true;

					switch (event.getEvent().getNativeEvent().getKeyCode()) {

						// Ctrl + t or T
						case 't':
						case 'T':
							event.getEvent().getNativeEvent().preventDefault();
							onSplitButtonClicked(null);
							break;

						// Ctrl + m or M
						case 'm':
						case 'M':
							event.getEvent().getNativeEvent().preventDefault();
							onMoveButtonClicked(null);
							break;

						// Ctrl + d or D
						case 'd':
						case 'D':
							event.getEvent().getNativeEvent().preventDefault();
							OnDuplicateButtonClicked(null);
							break;

						// Ctrl + 1 or !
						case '1':
						case '!':
							event.getEvent().getNativeEvent().preventDefault();
							if (zoomin.isEnabled()) {
								onZoomIn(null);
							}
							break;

						// Ctrl + 2 or @
						case '2':
						case '@':
							event.getEvent().getNativeEvent().preventDefault();
							if (zoomout.isEnabled()) {
								onZoomOut(null);
							}
							break;

						// Ctrl + r or R
						case 'r':
						case 'R':
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
						case 'l':
						case 'L':
							event.getEvent().getNativeEvent().preventDefault();
							if (zoomLock.isLocked()) {
								zoomLock.unlock();
								getConformationDialog(LocaleDictionary.get().getConstantValue(ReviewValidateConstants.ZOOM_UNLOCKED),
										LocaleDictionary.get().getMessageValue(ReviewValidateMessages.ZOOM_UNLOCKED_SUCCESSFUL));
							} else {
								zoomLock.setLockPosition(imageScrollPanel.getHorizontalScrollPosition(), imageScrollPanel
										.getScrollPosition(), zoomCount, fitToPageImage);
								zoomLock.setLocked(true);
								getConformationDialog(LocaleDictionary.get().getConstantValue(ReviewValidateConstants.ZOOM_LOCKED),
										LocaleDictionary.get().getMessageValue(ReviewValidateMessages.ZOOM_LOCKED_SUCCESSFUL));
							}
							break;
						default:
							break;
					}
				} else {
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
					}
				}
			}
		});
		eventBus.addHandler(RVKeyUpEvent.TYPE, new RVKeyUpEventHandler() {

			@Override
			public void onKeyUp(RVKeyUpEvent event) {
				shiftKeyPressed = false;
				ctrlKeyPressed = false;
				clickCount = 0;
			}
		});
	}

	private void getConformationDialog(String title, String message) {
		ScreenMaskUtility.maskScreen();
		final ConfirmationDialog confirmationDialog = new ConfirmationDialog(true);
		confirmationDialog.setMessage(message);
		confirmationDialog.setDialogTitle(title);
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
		confirmationDialog.show();
		confirmationDialog.center();
		confirmationDialog.okButton.setFocus(true);
	}

	public ScrollPanel getImageScrollPanel() {
		return imageScrollPanel;
	}

	public int getZoomCount() {
		return zoomCount;
	}

	private void setZoom(Integer width, Integer height) {

		int zoomLockingFactor = zoomLock.getZoomFactor();
		if (zoomLock.isLocked()) {
			if (zoomLockingFactor != 1) {
				this.zoomCount = zoomLockingFactor;
				if (!zoomout.isEnabled()) {
					zoomout.setEnabled(Boolean.TRUE);
				}
				pageImage.setSize("" + width * Math.pow(zoomFactor, (zoomCount - 1)), "" + height
						* Math.pow(zoomFactor, (zoomCount - 1)));
				processOverlay(coordinatesTypeList, pageImage, 0, 0, (float) (zoomCount * zoomFactor));
				if (coordinatesTypeList != null) {
					setScroll();
				}
				if (zoomCount == zoomCountLimit) {
					zoomin.setEnabled(Boolean.FALSE);
				}
			} else {
				pageImage.setSize(width.toString(), height.toString());
				processOverlay(coordinatesTypeList, pageImage, imageScrollPanel.getHorizontalScrollPosition(), imageScrollPanel
						.getScrollPosition(), 1);
				if (coordinatesTypeList != null) {
					setScroll();
				}
				zoomin.setEnabled(Boolean.TRUE);
				zoomout.setEnabled(Boolean.FALSE);
			}
		} else {
			pageImage.setSize(width.toString(), height.toString());
			processOverlay(coordinatesTypeList, pageImage, 0, 0, 1);
			setScroll();
			zoomin.setEnabled(Boolean.TRUE);
			zoomout.setEnabled(Boolean.FALSE);
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
		double aspectRatio = (double) (pageImage.getWidth()) / (double) (originalWidth);
		double xScrollPosition = imageScrollPanel.getHorizontalScrollPosition();
		double yScrollPosition = imageScrollPanel.getScrollPosition();
		// double yScrollingAdder = 75;
		x0 = (double) (x0Coord * aspectRatio) + (double) (xFactor * getViewPortWidth());
		x1 = (double) (x1Coord * aspectRatio) + (double) (xFactor * getViewPortWidth());
		y0 = (double) (y0Coord * aspectRatio) + (double) (yFactor * getViewPortHeight());
		y1 = (double) (y1Coord * aspectRatio) + (double) (yFactor * getViewPortHeight());
		if (yScrollPosition > (y1 - (yFactor * getViewPortHeight())) || xScrollPosition > (x0 - (xFactor * getViewPortWidth()))
				|| x1 - xScrollPosition > getViewPortWidth() || y1 - yScrollPosition > getViewPortHeight()) {
			return;
		}
		if (xScrollPosition == 0 && yScrollPosition == 0 && zoomFactor == 1) {
			doOverlay(x0, x1, y0, y1, zoomFactor);
		} else if (xScrollPosition <= x1 && yScrollPosition <= y1) {
			x0 -= xScrollPosition;
			x1 -= xScrollPosition;
			y0 -= yScrollPosition;
			y1 -= yScrollPosition;
			doOverlay(x0, x1, y0, y1, zoomFactor);
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
		double initialX = initialXCoor;
		double initialY = initialYCoor;
		double finalX = finalXCoor;
		double finalY = finalYCoor;
		if (initialX <= finalX && initialY <= finalY) {
			// it is in the III quadrant.
			if (zoomCount == 2) {
				finalX = finalXCoor - crosshairReductionFactor - 8;
			} else if (zoomCount == 3) {
				finalX = finalXCoor - crosshairReductionFactor - 7;
			}
			processOverlay(initialX, initialY, finalX, finalY - crosshairReductionFactor, 1);

		} else if (initialX >= finalX && initialY <= finalY) {
			// it is in IV quadrant.
			processOverlay(finalX, initialY, initialX, finalY, 1);
		} else if (initialX <= finalX && initialY >= finalY) {
			// it is in II quadrant.
			if (zoomCount == 2) {
				finalX = finalX - crosshairReductionFactor - 5;
			}
			processOverlay(initialX, finalY, finalX - crosshairReductionFactor, initialYCoor - crosshairReductionFactor, 1);
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

	public native void doOverlay(final double x0, final double x1, final double y0, final double y1, final double zoomFactor) /*-{
																																return $wnd.doOverlay(x0, x1, y0, y1, zoomFactor);
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

}
