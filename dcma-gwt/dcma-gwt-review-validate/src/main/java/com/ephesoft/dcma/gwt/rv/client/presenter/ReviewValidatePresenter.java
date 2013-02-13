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

package com.ephesoft.dcma.gwt.rv.client.presenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ephesoft.dcma.batch.schema.Batch;
import com.ephesoft.dcma.batch.schema.Coordinates;
import com.ephesoft.dcma.batch.schema.DataTable;
import com.ephesoft.dcma.batch.schema.DocField;
import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.batch.schema.Field;
import com.ephesoft.dcma.batch.schema.Page;
import com.ephesoft.dcma.batch.schema.Row;
import com.ephesoft.dcma.batch.schema.DocField.AlternateValues;
import com.ephesoft.dcma.batch.schema.Document.DocumentLevelFields;
import com.ephesoft.dcma.batch.schema.Document.Pages;
import com.ephesoft.dcma.batch.schema.Field.CoordinatesList;
import com.ephesoft.dcma.batch.schema.HocrPages.HocrPage.Spans.Span;
import com.ephesoft.dcma.core.common.BatchInstanceStatus;
import com.ephesoft.dcma.gwt.core.client.EphesoftAsyncCallback;
import com.ephesoft.dcma.gwt.core.client.event.TabSelectionEvent;
import com.ephesoft.dcma.gwt.core.client.event.TabSelectionEventHandler;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.client.ui.ScreenMaskUtility;
import com.ephesoft.dcma.gwt.core.shared.BatchDTO;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialog;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialogUtil;
import com.ephesoft.dcma.gwt.core.shared.FunctionKeyDTO;
import com.ephesoft.dcma.gwt.core.shared.PointCoordinate;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialog.DialogListener;
import com.ephesoft.dcma.gwt.rv.client.ReviewValidateDocServiceAsync;
import com.ephesoft.dcma.gwt.rv.client.event.IconRefreshEvent;
import com.ephesoft.dcma.gwt.rv.client.event.RVKeyDownEvent;
import com.ephesoft.dcma.gwt.rv.client.event.RVKeyDownEventHandler;
import com.ephesoft.dcma.gwt.rv.client.event.RVKeyUpEvent;
import com.ephesoft.dcma.gwt.rv.client.event.RVKeyUpEventHandler;
import com.ephesoft.dcma.gwt.rv.client.event.TreeRefreshEvent;
import com.ephesoft.dcma.gwt.rv.client.i18n.ReviewValidateConstants;
import com.ephesoft.dcma.gwt.rv.client.i18n.ReviewValidateMessages;
import com.ephesoft.dcma.gwt.rv.client.view.ExternalAppDialogBox;
import com.ephesoft.dcma.gwt.rv.client.view.ReviewValidateView;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasWidgets;

/**
 * The <code>ReviewValidatePresenter</code> class provides functionality for showing and handling events on review and validation
 * screens.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.gwt.rv.client.presenter.Presenter
 */
public class ReviewValidatePresenter implements Presenter {

	public BatchDTO batchDTO;
	public Document document;
	public Page page;
	private Integer realUpdateInterval;

	public final ReviewValidateDocServiceAsync rpcService;
	public final HandlerManager eventBus;

	public boolean batchSelected = false;

	private boolean tableView = false;

	private boolean manualTableExtraction = false;

	private final ReviewValidateView view;

	private ConfirmationDialog confirmationDialog = null;

	private String currentFieldName;

	private boolean controlSorQPressed;

	private final PreImageLoadingPresenter preImageLoadingPresenter;

	private Integer virtualUpdateCounter = 0;

	/**
	 * The isScriptExecuted is a check for executing field value script.
	 */
	private boolean scriptExecuted = false;

	/**
	 * The fieldValueChangeName {@link String} is for document field name on which script has to be executed.
	 */
	private String fieldValueChangeName = null;

	/**
	 * The currentDocumentFieldName {@link String} is for document field name on which has last focus.
	 */
	private String currentDocumentFieldName = null;

	public Integer getVirtualUpdateCounter() {
		return virtualUpdateCounter++;
	}

	/**
	 * @return the currentDocumentFieldName
	 */
	public String getCurrentDocumentFieldName() {
		return currentDocumentFieldName;
	}

	/**
	 * @param currentDocumentFieldName the lastDocumentFieldName to set
	 */
	public void setCurrentDocumentFieldName(final String currentDocumentFieldName) {
		this.currentDocumentFieldName = currentDocumentFieldName;
	}

	public ReviewValidatePresenter(final ReviewValidateDocServiceAsync rpcService, final HandlerManager eventBus) {
		this.rpcService = rpcService;
		this.eventBus = eventBus;
		registerEventHandlers();
		this.batchDTO = new BatchDTO();
		this.view = new ReviewValidateView();
		this.view.setPresenter(this);
		preImageLoadingPresenter = new PreImageLoadingPresenter(rpcService, eventBus, this);
	}

	public void setFocus() {
		final boolean focusSet = ReviewValidatePresenter.this.view.getRvPanel().getValidatePanel().setFocusAfterConformationDialog();
		if (!focusSet) {
			ReviewValidatePresenter.this.view.getRvPanel().getReviewPanel().setFocus();
		}
	}

	@Override
	public void onPresenterLoad(final HasWidgets container) {
		final String batchId = com.google.gwt.user.client.Window.Location.getParameter(ReviewValidateConstants.BATCH_ID);
		ScreenMaskUtility.maskScreen();
		// setting the default zoom count value from properties file
		setZoomCount();
		if (batchId == null) {
			getHighestPriorityBatch();
			batchSelected = true;
		} else {
			batchSelected = false;
			rpcService.getBatch(batchId, new EphesoftAsyncCallback<BatchDTO>() {

				@Override
				public void onSuccess(final BatchDTO batchDTO) {
					final BatchInstanceStatus batchInstanceStatus = batchDTO.getBatchInstanceStatus();
					recordReviewOrValidateDuration(batchId, batchInstanceStatus);
					if (!(batchInstanceStatus.equals(BatchInstanceStatus.READY_FOR_REVIEW) || batchInstanceStatus
							.equals(BatchInstanceStatus.READY_FOR_VALIDATION))) {
						final ConfirmationDialog confirmationDialog = ConfirmationDialogUtil
								.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
										ReviewValidateMessages.ERROR_BATCH_STATUS, batchId, batchInstanceStatus));
						confirmationDialog.okButton.addClickHandler(new ClickHandler() {

							@Override
							public void onClick(final ClickEvent arg0) {
								// getHighestPriorityBatch();
								moveToLandingPage();
							}
						});
						// return;
					}

					final String batchID2 = batchDTO.getBatch().getBatchInstanceIdentifier();
					rpcService.acquireLock(batchID2, new EphesoftAsyncCallback<Void>() {

						@Override
						public void customFailure(Throwable arg0) {
							ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
									ReviewValidateMessages.ERROR_BATCH_ALREADY_LOCKED,
									batchDTO.getBatch().getBatchInstanceIdentifier())).okButton.addClickHandler(new ClickHandler() {

								@Override
								public void onClick(final ClickEvent arg0) {
									moveToLandingPage();
								}
							});
						}

						@Override
						public void onSuccess(final Void arg0) {
							onLockAcquired(batchDTO);

						}
					});
			
				}
			
				@Override
				public void customFailure(Throwable throwable) {
					ScreenMaskUtility.unmaskScreen();
					if (!displayErrorMessage(throwable)) {
						ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
								ReviewValidateMessages.ERROR_RET_SPECIFIC_BATCH, batchId)).okButton
								.addClickHandler(new ClickHandler() {

									@Override
									public void onClick(final ClickEvent arg0) {
										moveToLandingPage();
									}
								});
					}
				}
				
			});
		}
		}

	private void onLockAcquired(final BatchDTO batchDTO) {
		ReviewValidatePresenter.this.batchDTO = batchDTO;
		ReviewValidatePresenter.this.view.initializeWidget();
		realUpdateInterval = batchDTO.getRealUpdateInterval();
		final int preloadedImageCount = batchDTO.getPreloadedImageCount();
		final Batch batch = batchDTO.getBatch();
		if (batch != null) {
			if (preImageLoadingPresenter != null) {
				preImageLoadingPresenter.loadInvalidDocuments(batch.getBatchInstanceIdentifier(), preloadedImageCount);
			}
			setTitleOfTab(batch.getBatchInstanceIdentifier());
		}
		ScreenMaskUtility.unmaskScreen();
	}

	private void getHighestPriorityBatch() {
		rpcService.getHighestPriortyBatch(new EphesoftAsyncCallback<BatchDTO>() {

			@Override
			public void onSuccess(final BatchDTO batchDTO) {
				onLockAcquired(batchDTO);
			}

			@Override
			public void customFailure(Throwable throwable) {
				ScreenMaskUtility.unmaskScreen();
				final String errorMessage = throwable.getLocalizedMessage();
				if (errorMessage.contains(ReviewValidateConstants.ERROR_TYPE_5)) {
					final int indexOfError = errorMessage.indexOf(ReviewValidateConstants.ERROR_TYPE_5);
					final int errorMessageLength = ReviewValidateConstants.ERROR_TYPE_5.length();
					final String batchID = errorMessage.substring(indexOfError + errorMessageLength + 1);
					ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
							ReviewValidateMessages.ERROR_BATCH_ALREADY_LOCKED, batchID));

				} else if (!displayErrorMessage(throwable)) {
					final ConfirmationDialog confirmationDialog = ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary
							.get().getMessageValue(ReviewValidateMessages.NO_BATCH_IN_REVIEW_VALIDATION), true);
					confirmationDialog.addDialogListener(new DialogListener() {

						@Override
						public void onOkClick() {
							moveToLandingPage();
						}

						@Override
						public void onCancelClick() {
							// no need to do anything on cancel click as it is anyway not visible
						}
					});

				}
			}
		});
	}

	public void setTitleOfTab(final String batchInstanceIdentifier) {
		if (batchInstanceIdentifier != null) {
			final String defaultTitle = LocaleDictionary.get().getConstantValue(ReviewValidateConstants.RV_TITLE);
			final String newTitle = defaultTitle + ReviewValidateConstants.SPACE + batchInstanceIdentifier;
			Window.setTitle(newTitle);
		}
	}

	public ReviewValidateView getView() {
		return view;
	}

	private final void registerEventHandlers() {

		registerKeyDownEvents();

		registerKeyUpEvents();

		registerTabSelectionEvents();
	}

	/**
	 * 
	 */
	private final void registerTabSelectionEvents() {
		eventBus.addHandler(TabSelectionEvent.type, new TabSelectionEventHandler() {

			@Override
			public void onTabSelection(final TabSelectionEvent event) {
				if (event.getSaveState()) {

					rpcService.saveBatch(batchDTO.getBatch(), new EphesoftAsyncCallback<Void>() {

						@Override
						public void customFailure(final Throwable arg0) {
							if (!displayErrorMessage(arg0)) {
								ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
										ReviewValidateMessages.ERROR_TOPPANEL_OK_FAILURE,
										batchDTO.getBatch().getBatchInstanceIdentifier(), arg0.getMessage()));
							}
						}

						@Override
						public void onSuccess(final Void arg0) {
							final String htmlPattern = event.getHtmlPattern();
							moveToTab(htmlPattern);
						}
					});

				} else {
					final String htmlPattern = event.getHtmlPattern();
					moveToTab(htmlPattern);
				}
			}
		});
	}

	/**
	 * 
	 */
	private final void registerKeyUpEvents() {
		eventBus.addHandler(RVKeyUpEvent.type, new RVKeyUpEventHandler() {

			@Override
			public void onKeyUp(final RVKeyUpEvent event) {
				if (event.getEvent().isControlKeyDown()) {
					switch (event.getEvent().getNativeEvent().getKeyCode()) {
						case 's':
						case 'S':
							event.getEvent().getNativeEvent().preventDefault();
							performOperationsOnCtrlSPress();
							ConfirmationDialogUtil.getConfirmationDialog().okButton.setFocus(true);
							break;

						// shortcut for the keyboard shortcuts pop-up
						case 'E':
						case 'e':
							event.getEvent().getNativeEvent().preventDefault();
							view.getTopPanel().showKeyboardShortctPopUp();
							view.getTopPanel().setFocusForOkButtonOnShortcutPopup(true);
							break;
						case 'q':
						case 'Q':
							event.getEvent().getNativeEvent().preventDefault();
							performOperationsOnCtrlQPress();
							// if document is unknown then set review to false else use below condition.

							break;
						case 'g':
						case 'G':
							event.getEvent().preventDefault();
							if (view.getRvPanel().getReviewDisclosurePanel().isOpen()) {
								view.getRvPanel().getReviewDisclosurePanel().setOpen(false);
							} else {
								view.getRvPanel().getReviewDisclosurePanel().setOpen(true);
							}
							view.getRvPanel().getFocusPanel().setFocus(true);
							break;

						default:
							break;
					}// end Switch
				} else {
					if (batchDTO.getBatchInstanceStatus().equals(BatchInstanceStatus.READY_FOR_VALIDATION)) {

						switch (event.getEvent().getNativeKeyCode()) {
							case 112:
								event.getEvent().getNativeEvent().preventDefault();
								executeScript("F1");
								break;
							case 113:
								event.getEvent().getNativeEvent().preventDefault();
								executeScript("F2");
								break;
							case 114:
								event.getEvent().getNativeEvent().preventDefault();
								executeScript("F3");
								break;
							case 115:
								event.getEvent().getNativeEvent().preventDefault();
								executeScript("F4");
								break;
							case 117:
								event.getEvent().getNativeEvent().preventDefault();
								executeScript("F6");
								break;
							case 118:
								event.getEvent().getNativeEvent().preventDefault();
								executeScript("F7");
								break;
							case 119:
								event.getEvent().getNativeEvent().preventDefault();
								executeScript("F8");
								break;
							case 120:
								event.getEvent().getNativeEvent().preventDefault();
								executeScript("F9");
								break;
							case 121:
								event.getEvent().getNativeEvent().preventDefault();
								executeScript("F10");
								break;
							case 122:
								event.getEvent().getNativeEvent().preventDefault();
								executeScript("F11");
								break;
							default:
								break;
						}
					}
				}
			}

		});
	}

	/**
	 * 
	 */
	private final void registerKeyDownEvents() {
		eventBus.addHandler(RVKeyDownEvent.type, new RVKeyDownEventHandler() {

			@Override
			public void onKeyDown(final RVKeyDownEvent event) {
				if (event.getEvent().isControlKeyDown()) {
					switch (event.getEvent().getNativeEvent().getKeyCode()) {
						case 's':
						case 'S':
							event.getEvent().getNativeEvent().preventDefault();
							setControlSorQPressed(true);
							break;
						case 'q':
						case 'Q':
							event.getEvent().getNativeEvent().preventDefault();
							setControlSorQPressed(true);
							break;
						case 'g':
						case 'G':
							event.getEvent().getNativeEvent().preventDefault();
							break;
						case 'E':
						case 'e':
							event.getEvent().getNativeEvent().preventDefault();
							view.getTopPanel().showKeyboardShortctPopUp();
							view.getTopPanel().setFocusForOkButtonOnShortcutPopup(true);
							break;
						default:
							break;
					}
				}	 else {
					switch (event.getEvent().getNativeKeyCode()) {
						case 112:
						case 113:
						case 114:
						case 115:
						case 117:
						case 118:
						case 119:
						case 120:
						case 121:
						case 122:
						case 123:
							event.getEvent().getNativeEvent().preventDefault();
							break;
						default:
							break;
					}
				}}

		});
	}

	private final void executeScript(final String shortcutKeyName) {
		if (document.getType().equals(ReviewValidateConstants.UNKNOWN_DOC_TYPE)) {
			document.setReviewed(false);
		} else {
			document.setReviewed(true);
		}
		ScreenMaskUtility.maskScreen(LocaleDictionary.get().getMessageValue(ReviewValidateMessages.EXECUTING_SCRIPT));
		rpcService.executeScript(shortcutKeyName, batchDTO.getBatch(), document, new EphesoftAsyncCallback<BatchDTO>() {

			@Override
			public void customFailure(Throwable arg0) {
				ScreenMaskUtility.unmaskScreen();
				displayErrorMessage(arg0);
			}

			@Override
			public void onSuccess(final BatchDTO arg0) {
				batchDTO = arg0;
				final List<Document> documents = arg0.getBatch().getDocuments().getDocument();
				for (final Document doc : documents) {
					if (doc.getIdentifier().equalsIgnoreCase(document.getIdentifier())) {
						document = doc;
						break;
					}
				}
				if (!checkAndSignalWorkflow(batchDTO.getBatch(), batchDTO.getBatchInstanceStatus(), true)) {
					refreshDocumentChanges();
					ScreenMaskUtility.unmaskScreen();
					setControlSorQPressed(false);
				}
				setFocus();
			}
		});

	}

	public final void moveToTab(final String htmlPattern) {
		if (batchDTO != null && batchDTO.getBatch() != null) {
			rpcService.cleanUpCurrentBatch(batchDTO.getBatch().getBatchInstanceIdentifier(), new EphesoftAsyncCallback<Void>() {

				@Override
				public void customFailure(Throwable arg0) {
					moveToUrl(htmlPattern);
				}

				@Override
				public void onSuccess(final Void arg0) {
					moveToUrl(htmlPattern);
				}
			});
		} else {
			moveToUrl(htmlPattern);
		}
	}

	private final void moveToUrl(final String htmlPattern) {
		final String href = Window.Location.getHref();
		final String baseUrl = href.substring(0, href.lastIndexOf('/'));
		Window.Location.assign(baseUrl + ReviewValidateConstants.FILE_SEPARATOR + htmlPattern);
	}

	public final void moveToLandingPage() {
		if (batchDTO != null && batchDTO.getBatch() != null) {
			rpcService.cleanUpCurrentBatch(batchDTO.getBatch().getBatchInstanceIdentifier(), new EphesoftAsyncCallback<Void>() {

				@Override
				public void customFailure(Throwable arg0) {
					moveToUrl(ReviewValidateConstants.BATCH_LIST_HTML);
				}

				@Override
				public void onSuccess(final Void arg0) {
					moveToUrl(ReviewValidateConstants.BATCH_LIST_HTML);
				}
			});
		} else {
			moveToUrl(ReviewValidateConstants.BATCH_LIST_HTML);
		}
	}

	public void onTableViewButtonClicked() {
		setTableView(Boolean.TRUE);
		view.getSlidingPanel().setWidget(view.getTableView());
	}

	public void onTableViewBackButtonClicked() {
		setTableView(Boolean.FALSE);
		view.setReviewValidateView();
	}

	public boolean checkDocumentType() {
		return view.getRvPanel().getReviewPanel().isSuggestBoxValid();
	}

	public void errorMessage() {
		ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
				ReviewValidateMessages.DOCUMENT_TYPE_ERROR));
	}

	private String addValues(final List<Span> spanList) {
		final StringBuffer value = new StringBuffer();
		if (spanList == null || !spanList.isEmpty()) {

			final CoordinatesList coordinatesList = new CoordinatesList();
			int counter = 0;
			for (final Span foundSpan : spanList) {

				if (foundSpan.getCoordinates() != null && foundSpan.getValue() != null) {
					coordinatesList.getCoordinates().add(foundSpan.getCoordinates());
					if (counter == 0) {
						counter++;
					} else {
						value.append(' ');
					}
					value.append(foundSpan.getValue());
				}
			}
			this.getView().getRvPanel().getValidatePanel().setSelectedValues(value.toString(), coordinatesList,
					this.page.getIdentifier());
		}
		return value.toString();
	}

	public void getHOCRContent(final PointCoordinate pointCoordinate1, final PointCoordinate pointCoordinate2,
			final boolean rectangularCoordinateSet) {

		final String batchInstanceIdentifier = batchDTO.getBatch().getBatchInstanceIdentifier();
		ScreenMaskUtility.maskScreen();
		rpcService.getHOCRContent(pointCoordinate1, pointCoordinate2, batchInstanceIdentifier, this.page.getIdentifier(),
				rectangularCoordinateSet, new EphesoftAsyncCallback<List<Span>>() {

					@Override
					public void customFailure(Throwable paramThrowable) {
						ScreenMaskUtility.unmaskScreen();
						ConfirmationDialogUtil.showConfirmationDialog(LocaleDictionary.get().getConstantValue(
								ReviewValidateConstants.UNABLE_TO_FIND), LocaleDictionary.get().getMessageValue(
								ReviewValidateMessages.ERROR_MESSAGE), Boolean.TRUE);
					}

					@Override
					public void onSuccess(final List<Span> spanList) {
						ScreenMaskUtility.unmaskScreen();
						if (spanList == null || spanList.isEmpty()) {
							return;
						}
						if (isTableView()) {
							getView().getTableExtractionView().insertDataInTable(spanList);
						} else {
							addValues(spanList);
						}
					}
				});
	}

	public void getHOCRContent(final List<PointCoordinate> pointCoordinates) {
		final String batchInstanceIdentifier = batchDTO.getBatch().getBatchInstanceIdentifier();
		ScreenMaskUtility.maskScreen();
		rpcService.getHOCRContent(pointCoordinates, batchInstanceIdentifier, this.page.getIdentifier(),
				new EphesoftAsyncCallback<List<Span>>() {

					@Override
					public void customFailure(Throwable paramThrowable) {
						ScreenMaskUtility.unmaskScreen();
						ConfirmationDialogUtil.showConfirmationDialog(LocaleDictionary.get().getConstantValue(
								ReviewValidateConstants.UNABLE_TO_FIND), LocaleDictionary.get().getMessageValue(
								ReviewValidateMessages.ERROR_MESSAGE), Boolean.TRUE);

					}

					@Override
					public void onSuccess(final List<Span> foundSpans) {
						ScreenMaskUtility.unmaskScreen();
						if (foundSpans == null || foundSpans.isEmpty()) {
							return;
						}
						if (isTableView()) {
							getView().getTableExtractionView().insertDataInTable(foundSpans);
						} else {
							addValues(foundSpans);
						}
					}
				});
	}

	public void setCustomizedShortcutPanels() {
		if (document != null
				&& (batchDTO.getBatchInstanceStatus().equals(BatchInstanceStatus.READY_FOR_REVIEW) || batchDTO
						.getBatchInstanceStatus().equals(BatchInstanceStatus.READY_FOR_VALIDATION))) {
			ScreenMaskUtility.maskScreen();
			rpcService.getFunctionKeyDTOs(document.getType(), batchDTO.getBatch().getBatchInstanceIdentifier(),
					new EphesoftAsyncCallback<List<FunctionKeyDTO>>() {

						@Override
						public void customFailure(final Throwable arg0) {
							ScreenMaskUtility.unmaskScreen();
							ConfirmationDialogUtil.showConfirmationDialog(LocaleDictionary.get().getConstantValue(
									ReviewValidateConstants.UNABLE_TO_FIND), LocaleDictionary.get().getMessageValue(
									ReviewValidateMessages.ERROR_MESSAGE), Boolean.TRUE);
						}

						@Override
						public void onSuccess(final List<FunctionKeyDTO> functionKeyDTOs) {
							ScreenMaskUtility.unmaskScreen();
							view.getRvPanel().getFirstShortcutsPanel().clear();
							view.getRvPanel().getLastShortcutsPanel().clear();
							if (functionKeyDTOs != null && !functionKeyDTOs.isEmpty()) {
								view.getRvPanel().getFunctionKeyLabel().setVisible(true);
								int count = 0;
								for (final FunctionKeyDTO functionKeyDTO : functionKeyDTOs) {
									final Button button = new Button(functionKeyDTO.getShortcutKeyName(), new ClickHandler() {

										@Override
										public void onClick(final ClickEvent arg0) {
											executeScript(functionKeyDTO.getShortcutKeyName());
										}
									});
									button.setVisible(true);
									button.setSize("30px", "25px");
									button.setTitle(functionKeyDTO.getMethodDescription());
									count++;
									if (count <= 6) {
										view.getRvPanel().getFirstShortcutsPanel().add(button);
									} else {
										if (count == 7) {
											final Button nextButton = new Button(">", new ClickHandler() {

												@Override
												public void onClick(final ClickEvent arg0) {
													view.getRvPanel().getSlidingPanel().setWidget(
															view.getRvPanel().getLastShortcutsPanel());
													view.getRvPanel().getSlidingPanel().setVisible(true);
												}
											});
											nextButton.setVisible(true);
											nextButton.setHeight("25px");
											nextButton.setTitle("More Buttons");
											view.getRvPanel().getFirstShortcutsPanel().add(nextButton);
											final Button previousButton = new Button("<", new ClickHandler() {

												@Override
												public void onClick(final ClickEvent arg0) {
													view.getRvPanel().getSlidingPanel().setWidget(
															view.getRvPanel().getFirstShortcutsPanel());
													view.getRvPanel().getSlidingPanel().setVisible(true);
												}
											});
											previousButton.setVisible(true);
											previousButton.setHeight("25px");
											previousButton.setTitle("Previous Buttons");
											view.getRvPanel().getLastShortcutsPanel().add(previousButton);
										}
										view.getRvPanel().getLastShortcutsPanel().add(button);
									}
								}
								view.getRvPanel().getSlidingPanel().setWidget(view.getRvPanel().getFirstShortcutsPanel());
								view.getRvPanel().getSlidingPanel().setVisible(true);
								view.getRvPanel().getFunctionKeyLabel().setVisible(true);
							} else {
								view.getRvPanel().getFunctionKeyLabel().setVisible(false);
							}
						}

					});
		} else {
			view.getRvPanel().getFirstShortcutsPanel().clear();
			view.getRvPanel().getLastShortcutsPanel().clear();
			view.getRvPanel().getFunctionKeyLabel().setVisible(false);
		}
	}

	public void setTableView(final boolean isTableView) {
		this.tableView = isTableView;
	}

	public boolean isTableView() {
		return tableView;
	}

	public boolean isManualTableExtraction() {
		return manualTableExtraction;
	}

	public void setManualTableExtraction(final boolean isManualTableExtraction) {
		this.manualTableExtraction = isManualTableExtraction;
	}

	public final void performOperationsOnCtrlSPress() {
		if (document.getType().equals(ReviewValidateConstants.UNKNOWN_DOC_TYPE)) {

			ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
					ReviewValidateMessages.SELECT_DOC_TYPE)).okButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(final ClickEvent arg0) {
					setFocus();
				}
			});
		} else {
			if (batchDTO.getBatchInstanceStatus().equals(BatchInstanceStatus.READY_FOR_VALIDATION)) {
				view.getRvPanel().getValidatePanel().setForceReviewDoneForDocFieldWidgets();
				if (batchDTO.getFieldValueChangeScriptSwitchState().equals(ReviewValidateConstants.ON_SWITCH)) {
					ScreenMaskUtility.maskScreen(LocaleDictionary.get().getMessageValue(ReviewValidateMessages.EXECUTING_AND_SAVING));
					final String fieldName = view.getRvPanel().getValidatePanel().getCurrentDocFieldWidgetName();
					rpcService.executeScriptOnFieldChange(batchDTO.getBatch(), document, fieldName, new EphesoftAsyncCallback<BatchDTO>() {


						@Override
						public void customFailure(Throwable arg0) {
							ScreenMaskUtility.unmaskScreen();
							setControlSorQPressed(false);
							if (!displayErrorMessage(arg0)) {
								ConfirmationDialogUtil.showConfirmationDialog(LocaleDictionary.get().getConstantValue(
										ReviewValidateMessages.UNABLE_TO_EXECUTE_SCRIPT_AND_SAVE), LocaleDictionary.get()
										.getMessageValue(ReviewValidateMessages.ERROR_MESSAGE), Boolean.TRUE);
							}
						}

						@Override
						public void onSuccess(final BatchDTO arg0) {
							batchDTO = arg0;
							final List<Document> documents = arg0.getBatch().getDocuments().getDocument();
							for (final Document doc : documents) {
								if (doc.getIdentifier().equalsIgnoreCase(document.getIdentifier())) {
									document = doc;
									break;
								}
							}
							validateBatch(true);
						}
					});
				} else {
					ScreenMaskUtility.maskScreen(LocaleDictionary.get().getMessageValue(ReviewValidateMessages.SAVING));
					validateBatch(false);
				}
			} else {
				ScreenMaskUtility.maskScreen(LocaleDictionary.get().getMessageValue(ReviewValidateMessages.SAVING));
				validateBatch(false);
			}
		}
	}
		

	public final void performOperationsOnCtrlQPress() {
		if (document.getType().equals(ReviewValidateConstants.UNKNOWN_DOC_TYPE)) {
			ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
					ReviewValidateMessages.SELECT_DOC_TYPE)).okButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(final ClickEvent arg0) {
					setFocus();
				}
			});
		} else {
			if (batchDTO.getBatchInstanceStatus().equals(BatchInstanceStatus.READY_FOR_VALIDATION)) {
				view.getRvPanel().getValidatePanel().setForceReviewDoneForDocFieldWidgets();
				if (batchDTO.getFieldValueChangeScriptSwitchState().equals(ReviewValidateConstants.ON_SWITCH)) {
					ScreenMaskUtility.maskScreen(LocaleDictionary.get().getMessageValue(ReviewValidateMessages.EXECUTING_AND_SAVING));
					final String fieldName = view.getRvPanel().getValidatePanel().getCurrentDocFieldWidgetName();
					rpcService.executeScriptOnFieldChange(batchDTO.getBatch(), document, fieldName, new EphesoftAsyncCallback<BatchDTO>() {

						@Override
						public void customFailure(Throwable arg0) {
							ScreenMaskUtility.unmaskScreen();
							if (!displayErrorMessage(arg0)) {
								ConfirmationDialogUtil.showConfirmationDialog(LocaleDictionary.get().getConstantValue(
										ReviewValidateMessages.UNABLE_TO_EXECUTE_SCRIPT_AND_SAVE), LocaleDictionary.get()
										.getMessageValue(ReviewValidateMessages.ERROR_MESSAGE), Boolean.TRUE);
							}
						}

						@Override
						public void onSuccess(final BatchDTO arg0) {
							batchDTO = arg0;
							final List<Document> documents = arg0.getBatch().getDocuments().getDocument();
							for (final Document doc : documents) {
								if (doc.getIdentifier().equalsIgnoreCase(document.getIdentifier())) {
									document = doc;
									break;
								}
							}
							reviewBatch(true);
						}
					});
				} else {
					ScreenMaskUtility.maskScreen(LocaleDictionary.get().getMessageValue(ReviewValidateMessages.SAVING));
					reviewBatch(false);
				}
			} else {
				ScreenMaskUtility.maskScreen(LocaleDictionary.get().getMessageValue(ReviewValidateMessages.SAVING));
				reviewBatch(false);
			}
		}
	}

	public void getTableHOCRContent(final PointCoordinate initialCoordinate, final PointCoordinate finalCoordinate) {
		final String batchInstanceIdentifier = batchDTO.getBatch().getBatchInstanceIdentifier();
		ScreenMaskUtility.maskScreen();
		rpcService.getTableHOCRContent(initialCoordinate, finalCoordinate, batchInstanceIdentifier, this.page.getIdentifier(),
				new EphesoftAsyncCallback<List<Span>>() {

					@Override
					public void customFailure(final Throwable paramThrowable) {
						ScreenMaskUtility.unmaskScreen();
						ConfirmationDialogUtil.showConfirmationDialog(LocaleDictionary.get().getMessageValue(
								ReviewValidateMessages.UNABLE_TO_FETCH_DATA), LocaleDictionary.get().getMessageValue(
								ReviewValidateMessages.ERROR_MESSAGE), Boolean.TRUE);
						getView().getTableExtractionView().undoManualExtractionChanges();
					}

					@Override
					public void onSuccess(final List<Span> spanList) {
						ScreenMaskUtility.unmaskScreen();
						if (spanList != null && !spanList.isEmpty() && tableView) {
							getView().getTableExtractionView().setSelectedValue(spanList, initialCoordinate, finalCoordinate);
						}
					}
				});
	}

	public void getTableData(final Map<Integer, Coordinates> columnVsCoordinates, final String documentTypeName,
			final DataTable selectedDataTable) {
		final String batchInstanceIdentifier = batchDTO.getBatch().getBatchInstanceIdentifier();
		final String batchClassIdentifier = batchDTO.getBatch().getBatchClassIdentifier();
		ScreenMaskUtility.maskScreen();
		rpcService.getTableData(columnVsCoordinates, documentTypeName, selectedDataTable, batchClassIdentifier,
				batchInstanceIdentifier, this.page.getIdentifier(), new EphesoftAsyncCallback<List<Row>>() {

					@Override
					public void customFailure(final Throwable paramThrowable) {
						ScreenMaskUtility.unmaskScreen();
						ConfirmationDialogUtil.showConfirmationDialog(LocaleDictionary.get().getMessageValue(
								ReviewValidateMessages.UNABLE_TO_FETCH_DATA), LocaleDictionary.get().getMessageValue(
								ReviewValidateMessages.ERROR_MESSAGE), Boolean.TRUE);
						getView().getTableExtractionView().undoManualExtractionChanges();
					}

					public void onSuccess(final List<Row> rowList) {
						ScreenMaskUtility.unmaskScreen();
						if (rowList == null || rowList.isEmpty()) {
							setManualTableExtraction(Boolean.FALSE);
							return;
						}
						getView().getTableExtractionView().mergeRows(rowList);
					}
				});
	}

	public void showExternalAppForHtmlPattern(final String htmlPattern, final String title) {
		if (htmlPattern != null && !htmlPattern.isEmpty()) {
			generateUrlForHtmlPatternAndDisplayApp(htmlPattern, title);
		}
	}

	public void displayExternalApp(final String newUrl, final String title) {
		final Map<String, String> dimensionsOfPopUpMap = batchDTO.getDimensionsForPopUp();
		final ExternalAppDialogBox externalAppDialogBox = new ExternalAppDialogBox(newUrl, dimensionsOfPopUpMap);
		externalAppDialogBox.setDialogTitle(title);
		externalAppDialogBox.addDialogBoxListener(new com.ephesoft.dcma.gwt.rv.client.view.ExternalAppDialogBox.DialogBoxListener() {

			@Override
			public void onOkClick() {
				externalAppDialogBox.hide();
				getUpdatedBatchDTO();
				setFocus();
			}

			@Override
			public void onCloseClick() {
				externalAppDialogBox.hide();
				setFocus();
			}
		});

	}

	private void generateUrlForHtmlPatternAndDisplayApp(final String htmlPattern, final String title) {
		final Batch batch = batchDTO.getBatch();
		final StringBuffer pathOfBatchXmlStringBuffer = new StringBuffer();
		pathOfBatchXmlStringBuffer.append(batch.getBatchLocalPath()).append(ReviewValidateConstants.SLASHES).append(
				batch.getBatchInstanceIdentifier()).append(ReviewValidateConstants.SLASHES).append(batch.getBatchInstanceIdentifier());
		rpcService.getEncodedStringForXMLPath(pathOfBatchXmlStringBuffer.toString(), new EphesoftAsyncCallback<String>() {

			@Override
			public void customFailure(Throwable arg0) {
				ConfirmationDialogUtil.showConfirmationDialog(LocaleDictionary.get().getMessageValue(
						ReviewValidateMessages.UNABLE_TO_DISPLAY_EXTERNAL_APP), LocaleDictionary.get().getMessageValue(
						ReviewValidateMessages.ERROR_MESSAGE), Boolean.TRUE);
			}

			@Override
			public void onSuccess(final String pathOfBatchXml) {
				rpcService.getGeneratedSecurityTokenForExternalApp(new EphesoftAsyncCallback<String>() {

					@Override
					public void customFailure(Throwable arg0) {
						ConfirmationDialogUtil.showConfirmationDialog(LocaleDictionary.get().getMessageValue(
								ReviewValidateMessages.UNABLE_TO_DISPLAY_EXTERNAL_APP), LocaleDictionary.get().getMessageValue(
								ReviewValidateMessages.ERROR_MESSAGE), Boolean.TRUE);

					}

					@Override
					public void onSuccess(final String securityTokenString) {
						final StringBuffer newUrl = new StringBuffer();
						String documentId = ReviewValidateConstants.EMPTY_STRING;
						if (document != null) {
							documentId = document.getIdentifier();
						}
						newUrl.append(htmlPattern);
						if (htmlPattern.indexOf('?') != -1) {
							newUrl.append("&document_id=").append(documentId);
						} else {
							newUrl.append("?document_id=").append(documentId);
						}
						newUrl.append("&batch_xml_path=").append(pathOfBatchXml);
						if (securityTokenString != null) {
							newUrl.append("&ticket=").append(securityTokenString);
						} else {
							ConfirmationDialogUtil.showConfirmationDialog(LocaleDictionary.get().getMessageValue(
									ReviewValidateMessages.UNABLE_TO_CREATE_AUTHENTICATION_FOR_EXTERNAL_APP), LocaleDictionary.get()
									.getMessageValue(ReviewValidateMessages.ERROR_MESSAGE), Boolean.TRUE);
						}
						displayExternalApp(newUrl.toString(), title);
					}
				});
			}
		});

	}

	private void getUpdatedBatchDTO() {
		ScreenMaskUtility.maskScreen();
		rpcService.getBatch(batchDTO.getBatch().getBatchInstanceIdentifier(), new EphesoftAsyncCallback<BatchDTO>() {

			@Override
			public void customFailure(Throwable arg0) {
				ScreenMaskUtility.unmaskScreen();
				if (!displayErrorMessage(arg0)) {
					ConfirmationDialogUtil.showConfirmationDialog(LocaleDictionary.get().getMessageValue(
							ReviewValidateMessages.UNABLE_TO_FETCH_BATCH_XML), LocaleDictionary.get().getMessageValue(
							ReviewValidateMessages.ERROR_MESSAGE), Boolean.TRUE);

				}
			}

			@Override
			public void onSuccess(final BatchDTO arg0) {
				batchDTO = arg0;
				final Document updatedDocument = batchDTO.getDocumentById(document.getIdentifier());
				eventBus.fireEvent(new TreeRefreshEvent(batchDTO, updatedDocument, page));
				ScreenMaskUtility.unmaskScreen();
			}
		});

	}

	public final boolean displayErrorMessage(final Throwable arg0) {
		final String errorMessage = arg0.getLocalizedMessage();
		String batchInstanceIdentifier = "";
		if (batchDTO != null && batchDTO.getBatch() != null) {
			batchInstanceIdentifier = batchDTO.getBatch().getBatchInstanceIdentifier();
		} else {
			batchInstanceIdentifier = Window.Location.getParameter(ReviewValidateConstants.BATCH_ID);
		}
		boolean errorCauseFound = false;
		if (errorMessage.equals(ReviewValidateConstants.ERROR_TYPE_1)) {
			errorCauseFound = true;
			final ConfirmationDialog confirmationDialog = ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get()
					.getMessageValue(ReviewValidateMessages.SESSION_TIME_OUT), true);
			confirmationDialog.okButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(final ClickEvent arg0) {
					Window.Location.reload();
				}
			});
		} else if (errorMessage.equals(ReviewValidateConstants.ERROR_TYPE_2)) {
			errorCauseFound = true;
			final ConfirmationDialog confirmationDialog = ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get()
					.getMessageValue(ReviewValidateMessages.ERROR_BATCH_ALREADY_LOCKED, batchInstanceIdentifier), true);
			confirmationDialog.okButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(final ClickEvent arg0) {
					moveToLandingPage();
				}
			});
		} else if (errorMessage.equals(ReviewValidateConstants.ERROR_TYPE_3)) {
			errorCauseFound = true;
			final ConfirmationDialog confirmationDialog = ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get()
					.getMessageValue(ReviewValidateMessages.NO_RIGHT_TO_OPEN_BATCH), true);
			confirmationDialog.okButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(final ClickEvent arg0) {
					moveToLandingPage();
				}
			});

		} else if (errorMessage.equals(ReviewValidateConstants.ERROR_TYPE_4)) {
			errorCauseFound = true;
			final ConfirmationDialog confirmationDialog = ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get()
					.getMessageValue(ReviewValidateMessages.ERROR_RET_SPECIFIC_BATCH, batchInstanceIdentifier), true);
			confirmationDialog.okButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(final ClickEvent arg0) {
					moveToLandingPage();
				}
			});

		}
		return errorCauseFound;
	}

	/**
	 * The executeScriptOnFieldChange method executes field value change script on the field name passed.
	 * 
	 * @param fieldName {@link String} document field name.
	 */
	public void executeScriptOnFieldChange(final String fieldName) {

		if (!isControlSorQPressed()) {
			if (document.getType().equals(ReviewValidateConstants.UNKNOWN_DOC_TYPE)) {
				document.setReviewed(false);
			} else {
				document.setReviewed(true);
			}
			scriptExecuted = false;
			fieldValueChangeName = ReviewValidateConstants.EMPTY_STRING;

			ScreenMaskUtility.maskScreen(LocaleDictionary.get().getMessageValue(ReviewValidateMessages.EXECUTING_SCRIPT));
			rpcService.executeScriptOnFieldChange(batchDTO.getBatch(), document, fieldName, new EphesoftAsyncCallback<BatchDTO>() {

				@Override
				public void customFailure(Throwable arg0) {
					ScreenMaskUtility.unmaskScreen();
					displayErrorMessage(arg0);
				}

				@Override
				public void onSuccess(final BatchDTO arg0) {
					batchDTO = arg0;
					final List<Document> documents = arg0.getBatch().getDocuments().getDocument();
					for (final Document doc : documents) {
						if (doc.getIdentifier().equalsIgnoreCase(document.getIdentifier())) {
							document = doc;
							view.getRvPanel().getValidatePanel().setFieldAlreadySelected(true);
							if (!batchDTO.isErrorContained(document)) {
								view.getRvPanel().getValidatePanel().setFieldAlreadySelected(false);
								validateBatch(true);
							} else {
								refreshDocumentChanges(page);
								ScreenMaskUtility.unmaskScreen();
								setControlSorQPressed(false);
							}
							break;
						}
					}

				}
			});
		}
	}

	public void setCurrentFieldName(final String currentFieldName) {
		this.currentFieldName = currentFieldName;
	}

	public String getCurrentFieldName() {
		return currentFieldName;
	}

	public boolean isControlSorQPressed() {
		return controlSorQPressed;
	}

	public final void setControlSorQPressed(final boolean controlSorQPressed) {
		this.controlSorQPressed = controlSorQPressed;
	}

	public final void validateBatch(final boolean isFieldChangeScriptExecuted) {
		if (checkDocumentType()) {
			if (document.getType().equals(ReviewValidateConstants.UNKNOWN_DOC_TYPE)) {
				document.setReviewed(false);
			} else {
				document.setReviewed(true);
			}
			if (batchDTO.getBatchInstanceStatus().equals(BatchInstanceStatus.READY_FOR_VALIDATION)
					&& ReviewValidateConstants.ON_SWITCH.equalsIgnoreCase(batchDTO.getIsValidationScriptEnabled())) {
				rpcService.executeScript(batchDTO.getBatch(), document, new EphesoftAsyncCallback<BatchDTO>() {

					@Override
					public void customFailure(Throwable arg0) {
						ScreenMaskUtility.unmaskScreen();
						setControlSorQPressed(false);
						displayErrorMessage(arg0);
					}

					@Override
					public void onSuccess(final BatchDTO arg0) {
						batchDTO = arg0;
						final List<Document> documents = arg0.getBatch().getDocuments().getDocument();
						for (final Document doc : documents) {
							if (doc.getIdentifier().equalsIgnoreCase(document.getIdentifier())) {
								document = doc;
								break;
							}
						}
						if (!checkAndSignalWorkflow(batchDTO.getBatch(), batchDTO.getBatchInstanceStatus(), true)) {
							refreshDocumentChanges();
							ScreenMaskUtility.unmaskScreen();
							setControlSorQPressed(false);
						}
					}
				});
			} else {
				performUpdateOperations(batchDTO.getBatch(), batchDTO.getBatchInstanceStatus(), isFieldChangeScriptExecuted, false);
			}
		} else {
			errorMessage();
			performUpdateOperations(batchDTO.getBatch(), batchDTO.getBatchInstanceStatus(), isFieldChangeScriptExecuted, false);
		}
	}

	private final void refreshDocumentChanges() {
		refreshDocumentChanges(null);
	}

	private final void refreshDocumentChanges(final Page page) {
		Page refreshPage = null;
		// if (!batchDTO.isErrorContained(document)) {
		// document = batchDTO.getNextDocumentTo(document, true);
		// refreshPage = null;
		// }
		if (batchDTO.isErrorContained(document)) {

			refreshPage = page;
		} else {
			document = batchDTO.getNextDocumentTo(document, true);
		}
		eventBus.fireEvent(new TreeRefreshEvent(batchDTO, document, refreshPage));
	}

	public void reviewBatch(final boolean isFieldChangeScriptExecuted) {
		if (checkDocumentType()) {
			if (document.getType().equals(ReviewValidateConstants.UNKNOWN_DOC_TYPE)) {
				document.setReviewed(false);
			} else {
				document.setReviewed(true);
			}
			if (batchDTO.getBatchInstanceStatus().equals(BatchInstanceStatus.READY_FOR_VALIDATION)
					&& ReviewValidateConstants.ON_SWITCH.equalsIgnoreCase(batchDTO.getIsValidationScriptEnabled())) {

				rpcService.executeScript(batchDTO.getBatch(), document, new EphesoftAsyncCallback<BatchDTO>() {

					@Override
					public void customFailure(Throwable arg0) {
						ScreenMaskUtility.unmaskScreen();
						setControlSorQPressed(false);
						displayErrorMessage(arg0);
					}

					@Override
					public void onSuccess(final BatchDTO arg0) {
						batchDTO = arg0;
						final List<Document> documents = arg0.getBatch().getDocuments().getDocument();
						for (final Document doc : documents) {
							if (doc.getIdentifier().equalsIgnoreCase(document.getIdentifier())) {
								document = doc;
								break;
							}
						}
						if (!checkAndSignalWorkflow(batchDTO.getBatch(), batchDTO.getBatchInstanceStatus(), true)) {
							refreshDocumentChanges();
							ScreenMaskUtility.unmaskScreen();
							setControlSorQPressed(false);
						}
					}
				});
			} else {
				performUpdateOperations(batchDTO.getBatch(), batchDTO.getBatchInstanceStatus(), isFieldChangeScriptExecuted, true);
			}
		} else {
			errorMessage();
			performUpdateOperations(batchDTO.getBatch(), batchDTO.getBatchInstanceStatus(), isFieldChangeScriptExecuted, true);
		}
	}

	private final void performUpdateOperations(final Batch batch, final BatchInstanceStatus batchInstanceStatus,
			final Boolean isFieldChangeScriptExecuted, final boolean ctrlQPressed) {
		if (!checkAndSignalWorkflow(batch, batchInstanceStatus, isFieldChangeScriptExecuted)) {
			refreshDocumentChanges();
			if (!isFieldChangeScriptExecuted) {
				if (ctrlQPressed) {
					updateBatchVirtually(batch);
				} else {
					updateBatch(batch);
				}
			} else {
				ScreenMaskUtility.unmaskScreen();
				setControlSorQPressed(false);
			}
		}
	}

	private final void updateBatchVirtually(final Batch batch) {
		if (getVirtualUpdateCounter() >= realUpdateInterval) {
			updateBatch(batch);
			resetVirtualUpdateCounter();
		} else {
			ScreenMaskUtility.unmaskScreen();
			setControlSorQPressed(false);
		}
	}

	private final void resetVirtualUpdateCounter() {
		virtualUpdateCounter = 0;
	}

	private Boolean checkAndSignalWorkflow(final Batch batch, final BatchInstanceStatus batchInstanceStatus,
			final Boolean isAnyScriptExecuted) {
		Boolean workflowSignalled = Boolean.FALSE;
		switch (batchInstanceStatus) {
			case READY_FOR_REVIEW: {
				if (checkIfAllDocumentsReviewed()) {
					workflowSignalled = Boolean.TRUE;
					signalWorkflow(batch, batchInstanceStatus, isAnyScriptExecuted);
				}
				break;
			}
			case READY_FOR_VALIDATION:
				if (checkIfAllDocumentsValidated()) {
					workflowSignalled = Boolean.TRUE;
					signalWorkflow(batch, batchInstanceStatus, isAnyScriptExecuted);
				}
				break;
			default:
				break;
		}
		return workflowSignalled;

	}

	private final void updateBatch(final Batch batch) {
		updateBatch(batch, false);
	}

	private final void updateBatch(final Batch batch, final boolean refreshDocument) {
		rpcService.saveBatch(batch, new EphesoftAsyncCallback<Void>() {

			@Override
			public void customFailure(Throwable paramThrowable) {
				if (!displayErrorMessage(paramThrowable)) {
					ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
							ReviewValidateMessages.ERROR_SAVE_BATCH, batch.getBatchInstanceIdentifier())
							+ paramThrowable.getMessage(), Boolean.TRUE);
				}
				ScreenMaskUtility.unmaskScreen();
				setControlSorQPressed(false);
			}

			@Override
			public void onSuccess(final Void arg0) {
				ScreenMaskUtility.unmaskScreen();
				setControlSorQPressed(false);
				if (refreshDocument) {
					setFocus();
					eventBus.fireEvent(new TreeRefreshEvent(batchDTO, document, page));
				}
			}
		});

	}

	private void signalWorkflow(final Batch batch, final BatchInstanceStatus batchInstanceStatus, final Boolean isAnyScriptExecuted) {
		Boolean showConfirmationDialog = Boolean.TRUE;
		refreshDocumentIcon();
		switch (batchInstanceStatus) {
			case READY_FOR_REVIEW:
				confirmationDialog = ConfirmationDialogUtil.showConfirmationDialog(LocaleDictionary.get().getMessageValue(
						ReviewValidateMessages.MSG_REVIEW_CONFIRM), LocaleDictionary.get().getConstantValue(
						ReviewValidateConstants.TITLE_REVIEW_DONE), Boolean.FALSE);

				break;
			case READY_FOR_VALIDATION:
				confirmationDialog = ConfirmationDialogUtil.showConfirmationDialog(LocaleDictionary.get().getMessageValue(
						ReviewValidateMessages.MSG_VALIDATION_CONFIRM), LocaleDictionary.get().getConstantValue(
						ReviewValidateConstants.TITLE_VALIDATION_DONE), Boolean.FALSE);

				break;
			default:
				showConfirmationDialog = Boolean.FALSE;
				confirmationDialog = null;
				ScreenMaskUtility.unmaskScreen();
				setControlSorQPressed(false);
				break;
		}
		if (showConfirmationDialog) {
			confirmationDialog.setPerformCancelOnEscape(true);
			confirmationDialog.addDialogListener(new com.ephesoft.dcma.gwt.core.shared.ConfirmationDialog.DialogListener() {

				@Override
				public void onOkClick() {
					rpcService.signalWorkflow(batch, new EphesoftAsyncCallback<Void>() {

						@Override
						public void customFailure(Throwable paramThrowable) {
							ScreenMaskUtility.unmaskScreen();
							setControlSorQPressed(false);
							if (!displayErrorMessage(paramThrowable)) {
								ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
										ReviewValidateMessages.ERROR_SAVE_BATCH, batch.getBatchInstanceIdentifier())
										+ paramThrowable.getMessage(), Boolean.TRUE);
							}
						}

						@Override
						public void onSuccess(final Void arg0) {

							rpcService.updateEndTimeAndCalculateDuration(batch.getBatchInstanceIdentifier(),
									new EphesoftAsyncCallback<Void>() {

										@Override
										public void customFailure(Throwable arg0) {
											// no need to do anything here as we are only firing a call to server and not looking for
											// the call back
										}

										@Override
										public void onSuccess(final Void arg0) {
											// no need to do anything here as we are only firing a call to server and not looking for
											// the call back

										}
									});

							ScreenMaskUtility.unmaskScreen();
							setControlSorQPressed(false);
							if (!batchSelected) {
								moveToLandingPage();
							} else {
								moveToTab(ReviewValidateConstants.REVIEW_VALIDATE_HTML);
							}
						}
					});
				}

				@Override
				public void onCancelClick() {
					if (isAnyScriptExecuted) {
						setControlSorQPressed(false);
						confirmationDialog.hide();
						ScreenMaskUtility.unmaskScreen();
						setFocus();
						eventBus.fireEvent(new TreeRefreshEvent(batchDTO, document, page));
					} else {
						updateBatch(batch, true);
					}

				}
			});
			confirmationDialog.okButton.setFocus(true);
		}
	}

	private void refreshDocumentIcon() {
		eventBus.fireEvent(new IconRefreshEvent(batchDTO, document));
	}

	private boolean checkIfAllDocumentsReviewed() {
		Boolean isBatchReviewed = Boolean.TRUE;
		final List<Document> documents = batchDTO.getBatch().getDocuments().getDocument();
		for (final Document document : documents) {
			if (!document.isReviewed()) {
				isBatchReviewed = Boolean.FALSE;
				break;
			}
		}
		return isBatchReviewed;
	}

	private boolean checkIfAllDocumentsValidated() {
		Boolean isBatchValidated = Boolean.TRUE;
		final List<Document> documents = batchDTO.getBatch().getDocuments().getDocument();
		for (final Document document : documents) {
			if (!document.isValid() || !document.isReviewed()) {
				isBatchValidated = Boolean.FALSE;
				break;
			}
		}
		return isBatchValidated;
	}

	private String getNewDocumentTypeID(final List<Document> docTypesList) {
		Long maxDocumentTypeID = 1L;
		for (final Document docType : docTypesList) {
			String curDocID = docType.getIdentifier();
			curDocID = curDocID.replaceAll(ReviewValidateConstants.DOCUMENT, ReviewValidateConstants.EMPTY_STRING);
			final Long curPageIDLg = Long.parseLong(curDocID, ReviewValidateConstants.RADIX_BASE);
			if (curPageIDLg > maxDocumentTypeID) {
				maxDocumentTypeID = curPageIDLg;
			}
		}
		return Long.toString(maxDocumentTypeID + 1L);
	}

	public void mergeDocument(final Batch batch, final String docIDOne, final String mergeDocID) {
		final List<Document> docTypesList = batch.getDocuments().getDocument();
		final Integer mergeDocIndexOne = getDocumentTypeIndex(docTypesList, docIDOne);
		final Integer mergeDocIndexTwo = getDocumentTypeIndex(docTypesList, mergeDocID);
		final Document mergeDocTypeOne = docTypesList.get(mergeDocIndexOne);
		final Pages mergePagesOne = mergeDocTypeOne.getPages();
		final List<Page> mergePageTypeListOne = mergePagesOne.getPage();
		final Document mergeDocTypeTwo = docTypesList.get(mergeDocIndexTwo);
		final Pages mergePagesTwo = mergeDocTypeTwo.getPages();
		final List<Page> pageTypeListTwo = mergePagesTwo.getPage();
		mergePageTypeListOne.addAll(pageTypeListTwo);
		docTypesList.remove(mergeDocTypeTwo);
		document = batchDTO.getDocumentById(docIDOne);
	}

	private int getDocumentTypeIndex(final List<Document> docTypesList, final String docID) {
		int docIndex = 0;
		for (final Document docType : docTypesList) {
			final String curDocID = docType.getIdentifier();
			if (curDocID.equals(docID)) {
				break;
			}
			docIndex++;
		}
		return docIndex;
	}

	private int getPageTypeIndex(final List<Document> docTypesList, final int docIndex, final String pageID) {
		final Pages pages = docTypesList.get(docIndex).getPages();
		final List<Page> pageTypeList = pages.getPage();
		int pageIndex = 0;
		for (final Page pageType : pageTypeList) {
			final String curPageID = pageType.getIdentifier();
			if (curPageID.equals(pageID)) {
				break;
			}
			pageIndex++;
		}
		return pageIndex;
	}

	public void splitDocument(final Batch batch, final String docID, final String pageID) {
		final List<Document> docTypesList = batch.getDocuments().getDocument();
		final Integer docIndex = getDocumentTypeIndex(docTypesList, docID);
		final Integer pageIndex = getPageTypeIndex(docTypesList, docIndex, pageID);
		final Document docType = docTypesList.get(docIndex);
		final Pages pages = docType.getPages();
		final List<Page> pageTypeList = pages.getPage();
		// create new document.
		final String newDocID = getNewDocumentTypeID(docTypesList);
		final Document newDocType = new Document();
		newDocType.setIdentifier(ReviewValidateConstants.DOCUMENT + newDocID);
		newDocType.setType(docType.getType());
		newDocType.setConfidence(docType.getConfidence());
		newDocType.setMultiPagePdfFile(docType.getMultiPagePdfFile());
		newDocType.setMultiPageTiffFile(docType.getMultiPageTiffFile());
		newDocType.setValid(docType.isValid());
		newDocType.setReviewed(docType.isReviewed());
		newDocType.setErrorMessage(ReviewValidateConstants.EMPTY_STRING);
		newDocType.setDocumentDisplayInfo(ReviewValidateConstants.EMPTY_STRING);

		final Pages newPages = new Pages();
		final List<Page> listOfNewPages = newPages.getPage();
		final List<String> listOfNewPageIdentifiers = new ArrayList<String>();
		// create the new document for split pages.
		for (int index = pageIndex; index < pageTypeList.size(); index++) {
			final Page pgType = pageTypeList.get(index);
			listOfNewPages.add(pgType);
			listOfNewPageIdentifiers.add(pgType.getIdentifier());
		}
		newDocType.setPages(newPages);
		// remove the pages from the older document.
		for (int index = pageTypeList.size() - 1; index >= pageIndex; index--) {
			pageTypeList.remove(index);
		}
		final DocumentLevelFields documentLevelFields = docType.getDocumentLevelFields();
		if (documentLevelFields != null) {
			final List<DocField> docFields = documentLevelFields.getDocumentLevelField();
			final DocumentLevelFields documentLevelFieldsForNewDoc = new DocumentLevelFields();
			final List<DocField> docFieldsForNewDoc = documentLevelFieldsForNewDoc.getDocumentLevelField();
			if (docFields != null) {
				for (final DocField docField : docFields) {
					final DocField docFieldForNewDoc = new DocField();
					docFieldForNewDoc.setConfidence(docField.getConfidence());
					docFieldForNewDoc.setFieldOrderNumber(docField.getFieldOrderNumber());
					docFieldForNewDoc.setFieldValueOptionList(docField.getFieldValueOptionList());
					docFieldForNewDoc.setName(docField.getName());
					docFieldForNewDoc.setType(docField.getType());
					docFieldForNewDoc.setValue(docField.getValue());
					if (listOfNewPageIdentifiers.contains(docField.getPage())) {
						docFieldForNewDoc.setCoordinatesList(docField.getCoordinatesList());
						docFieldForNewDoc.setOverlayedImageFileName(docField.getOverlayedImageFileName());
						docFieldForNewDoc.setPage(docField.getPage());
						docField.setPage(pageTypeList.get(0).getIdentifier());
						docField.setCoordinatesList(null);
						docField.setOverlayedImageFileName(null);
					} else {
						docFieldForNewDoc.setCoordinatesList(null);
						docFieldForNewDoc.setOverlayedImageFileName(null);
						if (docField.getPage() != null && !docField.getPage().isEmpty()) {
							docFieldForNewDoc.setPage(listOfNewPages.get(0).getIdentifier());
						} else {
							docFieldForNewDoc.setPage(docField.getPage());
						}
					}
					final AlternateValues alternateValuesForNewDoc = new AlternateValues();
					final List<Field> fieldsForNewDoc = alternateValuesForNewDoc.getAlternateValue();
					final AlternateValues alternateValues = docField.getAlternateValues();
					if (alternateValues != null) {
						createAlternateValues(pageTypeList, listOfNewPages, listOfNewPageIdentifiers, fieldsForNewDoc, alternateValues);
					}
					docFieldForNewDoc.setAlternateValues(alternateValuesForNewDoc);
					docFieldsForNewDoc.add(docFieldForNewDoc);
				}
			}

			newDocType.setDocumentLevelFields(documentLevelFieldsForNewDoc);
		} else {
			newDocType.setDocumentLevelFields(null);
		}
		docTypesList.add(docIndex + 1, newDocType);
		document = newDocType;
	}

	private void createAlternateValues(final List<Page> pageTypeList, final List<Page> listOfNewPages,
			final List<String> listOfNewPageIdentifiers, final List<Field> fieldsForNewDoc, final AlternateValues alternateValues) {
		final List<Field> alternateValueFields = alternateValues.getAlternateValue();
		if (alternateValueFields != null) {
			for (final Field field : alternateValueFields) {
				final Field fieldForNewDoc = new Field();
				fieldForNewDoc.setConfidence(field.getConfidence());
				fieldForNewDoc.setFieldOrderNumber(field.getFieldOrderNumber());
				fieldForNewDoc.setFieldValueOptionList(field.getFieldValueOptionList());
				fieldForNewDoc.setName(field.getName());
				fieldForNewDoc.setType(field.getType());
				fieldForNewDoc.setValue(field.getValue());
				if (listOfNewPageIdentifiers.contains(field.getPage())) {
					fieldForNewDoc.setCoordinatesList(field.getCoordinatesList());
					fieldForNewDoc.setOverlayedImageFileName(field.getOverlayedImageFileName());
					fieldForNewDoc.setPage(field.getPage());
					field.setCoordinatesList(null);
					field.setOverlayedImageFileName(null);
					field.setPage(pageTypeList.get(0).getIdentifier());
				} else {
					fieldForNewDoc.setCoordinatesList(null);
					fieldForNewDoc.setOverlayedImageFileName(null);
					if (field.getPage() != null && !field.getPage().isEmpty()) {
						fieldForNewDoc.setPage(listOfNewPages.get(0).getIdentifier());
					} else {
						fieldForNewDoc.setPage(field.getPage());
					}
				}
				fieldsForNewDoc.add(fieldForNewDoc);
			}
		}
	}

	public void setBatchListScreenTab() {
		final BatchInstanceStatus batchInstanceStatus = batchDTO.getBatchInstanceStatus();
		rpcService.getUserName(new EphesoftAsyncCallback<String>() {

			@Override
			public void customFailure(Throwable arg0) {
				// no need to do anything as we dont need to convey any failure
			}

			@Override
			public void onSuccess(final String arg0) {
				rpcService.setBatchListScreenTab(arg0, batchInstanceStatus, new EphesoftAsyncCallback<Void>() {

					@Override
					public void customFailure(Throwable arg0) {
						// no need to do anything as we dont require a callback from this server hit
					}

					@Override
					public void onSuccess(final Void arg0) {
						// no need to do anything as we dont require a callback from this server hit
					}
				});
			}
		});
	}

	public void setZoomCount() {
		this.rpcService.getZoomCount(new EphesoftAsyncCallback<String>() {

			@Override
			public void customFailure(Throwable arg0) {
				ScreenMaskUtility.unmaskScreen();
				ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
						ReviewValidateMessages.UNABLE_TO_GET_ZOOM_COUNT), Boolean.TRUE);

			}

			@Override
			public void onSuccess(final String zoomCountValue) {
				final int zoomCount = Integer.parseInt(zoomCountValue);
				view.getImgOverlayPanel().setZoomCount(zoomCount);
			}
		});
	}

	private void recordReviewOrValidateDuration(final String batchInstanceId, final BatchInstanceStatus batchInstanceStatus) {
		this.rpcService.recordReviewOrValidateDuration(batchInstanceId, batchInstanceStatus, new EphesoftAsyncCallback<Void>() {

			@Override
			public void customFailure(Throwable arg0) {
				// no need to do anything as we dont require a callback from this server hit
			}

			@Override
			public void onSuccess(final Void arg0) {
				// no need to do anything as we dont require a callback from this server hit

			}
		});

	}

	/**
	 * The getFieldValueChangeName method returns the document field name.
	 * 
	 * @return the fieldValueChangeName {@link String} document field name.
	 */
	public String getFieldValueChangeName() {
		return fieldValueChangeName;
	}

	/**
	 * The setFieldValueChangeName sets the name for document field for which script has to be executed.
	 * 
	 * @param fieldValueChangeName {@link String} document field name.
	 */
	public void setFieldValueChangeName(final String fieldValueChangeName) {
		this.fieldValueChangeName = fieldValueChangeName;
	}

	/**
	 * The isScriptExecuted method returns whether field value change script has to be executed or not.
	 * 
	 * @return the isScriptExecuted.
	 */
	public boolean isScriptExecuted() {
		return scriptExecuted;
	}

	/**
	 * The setFieldValueChangeScriptExecuted method sets indicator for executing field value change script.
	 * 
	 * @param scriptExecuted the isScriptExecuted to set.
	 */
	public void setScriptExecuted(final boolean isFieldValueChangeScriptExecuted) {
		this.scriptExecuted = isFieldValueChangeScriptExecuted;
	}

	public native void preventDefaultBehaviour(NativeEvent event, String functionKey)/*-{
																						event.returnValue = false; 
																						event.keyCode = 0;
																						}-*/;

}
