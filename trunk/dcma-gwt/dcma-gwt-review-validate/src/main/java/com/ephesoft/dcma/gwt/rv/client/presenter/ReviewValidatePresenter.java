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

package com.ephesoft.dcma.gwt.rv.client.presenter;

import java.util.List;
import java.util.Map;

import com.ephesoft.dcma.batch.schema.Batch;
import com.ephesoft.dcma.batch.schema.BatchStatus;
import com.ephesoft.dcma.batch.schema.Coordinates;
import com.ephesoft.dcma.batch.schema.DataTable;
import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.batch.schema.Page;
import com.ephesoft.dcma.batch.schema.Row;
import com.ephesoft.dcma.batch.schema.Field.CoordinatesList;
import com.ephesoft.dcma.batch.schema.HocrPages.HocrPage.Spans.Span;
import com.ephesoft.dcma.gwt.core.client.event.TabSelectionEvent;
import com.ephesoft.dcma.gwt.core.client.event.TabSelectionEventHandler;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.client.ui.ScreenMaskUtility;
import com.ephesoft.dcma.gwt.core.shared.BatchDTO;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialog;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialogUtil;
import com.ephesoft.dcma.gwt.core.shared.FunctionKeyDTO;
import com.ephesoft.dcma.gwt.core.shared.PointCoordinate;
import com.ephesoft.dcma.gwt.rv.client.ReviewValidateDocServiceAsync;
import com.ephesoft.dcma.gwt.rv.client.event.DocumentRefreshEvent;
import com.ephesoft.dcma.gwt.rv.client.event.RVKeyDownEvent;
import com.ephesoft.dcma.gwt.rv.client.event.RVKeyDownEventHandler;
import com.ephesoft.dcma.gwt.rv.client.event.RVKeyUpEvent;
import com.ephesoft.dcma.gwt.rv.client.event.RVKeyUpEventHandler;
import com.ephesoft.dcma.gwt.rv.client.event.TreeRefreshEvent;
import com.ephesoft.dcma.gwt.rv.client.i18n.ReviewValidateConstants;
import com.ephesoft.dcma.gwt.rv.client.i18n.ReviewValidateMessages;
import com.ephesoft.dcma.gwt.rv.client.view.ExternalAppDialogBox;
import com.ephesoft.dcma.gwt.rv.client.view.ReviewValidateView;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasWidgets;

public class ReviewValidatePresenter implements Presenter {

	public BatchDTO batchDTO;
	public Document document;
	public Page page;

	public final ReviewValidateDocServiceAsync rpcService;
	public final HandlerManager eventBus;

	public boolean batchSelected = false;

	private boolean isTableView = false;

	private boolean isManualTableExtraction = false;

	private final ReviewValidateView view;

	private ConfirmationDialog confirmationDialog = null;

	private String currentFieldName;

	private boolean isControlSorQPressed;

	public ReviewValidatePresenter(ReviewValidateDocServiceAsync rpcService, HandlerManager eventBus) {
		this.rpcService = rpcService;
		this.eventBus = eventBus;
		registerEventHandlers();
		this.batchDTO = new BatchDTO();
		this.view = new ReviewValidateView();
		this.view.setPresenter(this);
	}

	public void setFocus() {
		boolean focusSet = ReviewValidatePresenter.this.view.getRvPanel().getValidatePanel().setFocusAfterConformationDialog();
		if (!focusSet) {
			ReviewValidatePresenter.this.view.getRvPanel().getReviewPanel().setFocus();
		}
	}

	@Override
	public void go(HasWidgets container) {
		final String batchId = com.google.gwt.user.client.Window.Location.getParameter("batch_id");
		ScreenMaskUtility.maskScreen();
		if (batchId == null) {
			getHighestPriorityBatch();
			batchSelected = true;
		} else {
			batchSelected = false;
			rpcService.getBatch(batchId, new AsyncCallback<BatchDTO>() {

				@Override
				public void onSuccess(final BatchDTO batchDTO) {

					if (!(batchDTO.getBatch().getBatchStatus().name().equalsIgnoreCase(BatchStatus.READY_FOR_REVIEW.name()) || batchDTO
							.getBatch().getBatchStatus().name().equalsIgnoreCase(BatchStatus.READY_FOR_VALIDATION.name()))) {
						ConfirmationDialog confirmationDialog = ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
								ReviewValidateMessages.error_batch_status, batchId, batchDTO.getBatch().getBatchStatus()));
						confirmationDialog.okButton.addClickHandler(new ClickHandler() {

							@Override
							public void onClick(ClickEvent arg0) {
								// getHighestPriorityBatch();
								moveToLandingPage();
							}
						});
						//return;
					}

					final String batchID2 = batchDTO.getBatch().getBatchInstanceIdentifier();
					rpcService.acquireLock(batchID2, new AsyncCallback<Void>() {

						@Override
						public void onFailure(Throwable arg0) {
							ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
									ReviewValidateMessages.error_batch_already_locked,
									batchDTO.getBatch().getBatchInstanceIdentifier())).okButton.addClickHandler(new ClickHandler() {

								@Override
								public void onClick(ClickEvent arg0) {
									moveToLandingPage();
								}
							});
						}

						@Override
						public void onSuccess(Void arg0) {
							ReviewValidatePresenter.this.batchDTO = batchDTO;
							ReviewValidatePresenter.this.view.initializeWidget();
							ScreenMaskUtility.unmaskScreen();

						}

					});
				}

				@Override
				public void onFailure(Throwable throwable) {
					ScreenMaskUtility.unmaskScreen();
					ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
							ReviewValidateMessages.error_ret_specific_batch, batchId));
					moveToLandingPage();
				}
			});
		}
	}

	private void getHighestPriorityBatch() {
		rpcService.getHighestPriortyBatch(new AsyncCallback<BatchDTO>() {

			@Override
			public void onSuccess(final BatchDTO batchDTO) {
				final String batchID2 = batchDTO.getBatch().getBatchInstanceIdentifier();
				rpcService.acquireLock(batchID2, new AsyncCallback<Void>() {

					@Override
					public void onFailure(Throwable arg0) {
						ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
								ReviewValidateMessages.error_batch_already_locked, batchDTO.getBatch().getBatchInstanceIdentifier()));

					}

					@Override
					public void onSuccess(Void arg0) {
						ReviewValidatePresenter.this.batchDTO = batchDTO;
						ReviewValidatePresenter.this.view.initializeWidget();
						ScreenMaskUtility.unmaskScreen();

					}
				});
			}

			@Override
			public void onFailure(Throwable throwable) {
				ScreenMaskUtility.unmaskScreen();
				ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
						ReviewValidateMessages.error_ret_batch));
				moveToLandingPage();
			}
		});
	}

	public ReviewValidateView getView() {
		return view;
	}

	private void registerEventHandlers() {

		eventBus.addHandler(RVKeyDownEvent.TYPE, new RVKeyDownEventHandler() {

			@Override
			public void onKeyDown(RVKeyDownEvent event) {
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
						default:
							break;
					}
				} else {
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
				}
			}
		});

		eventBus.addHandler(RVKeyUpEvent.TYPE, new RVKeyUpEventHandler() {

			@Override
			public void onKeyUp(RVKeyUpEvent event) {
				if (event.getEvent().isControlKeyDown()) {
					switch (event.getEvent().getNativeEvent().getKeyCode()) {
						case 's':
						case 'S':
							event.getEvent().getNativeEvent().preventDefault();
							performOperationsOnCtrlSPress();
							break;

						// shortcut for the keyboard shortcuts pop-up
						case 'E':
						case 'e':
							view.getTopPanel().showKeyboardShortctPopUp();
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
							break;

						default:
							break;
					}// end Switch
				} else {
					if (batchDTO.getBatch().getBatchStatus().equals(BatchStatus.READY_FOR_VALIDATION)) {
					
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

		eventBus.addHandler(TabSelectionEvent.TYPE, new TabSelectionEventHandler() {

			@Override
			public void onTabSelection(final TabSelectionEvent event) {
				rpcService.updateBatch(batchDTO.getBatch(), new AsyncCallback<BatchStatus>() {

					@Override
					public void onFailure(Throwable arg0) {
						ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
								ReviewValidateMessages.error_topPanel_ok_failure, batchDTO.getBatch().getBatchInstanceIdentifier(),
								arg0.getMessage()));
					}

					@Override
					public void onSuccess(BatchStatus arg0) {
						rpcService.cleanup(new AsyncCallback<Void>() {

							@Override
							public void onFailure(Throwable arg0) {
								ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
										ReviewValidateMessages.error_topPanel_ok_failure,
										batchDTO.getBatch().getBatchInstanceIdentifier()));
							}

							@Override
							public void onSuccess(Void arg0) {
								String htmlPattern = event.getHtmlPattern();
								moveToTab(htmlPattern);
							}
						});
					}
				});
			}
		});
	}

	private void executeScript(String shortcutKeyName) {
		if (document.getType().equals("Unknown")) {
			document.setReviewed(false);
		} else {
			document.setReviewed(true);
		}
		ScreenMaskUtility.maskScreen("Executing Script....");
		rpcService.executeScript(shortcutKeyName, batchDTO.getBatch(), document, new AsyncCallback<BatchDTO>() {

			@Override
			public void onFailure(Throwable arg0) {
				ScreenMaskUtility.unmaskScreen();
			}

			@Override
			public void onSuccess(final BatchDTO arg0) {
				batchDTO = arg0;
				List<Document> documents = arg0.getBatch().getDocuments().getDocument();
				for (final Document doc : documents) {
					if (doc.getIdentifier().equalsIgnoreCase(document.getIdentifier())) {
						document = doc;
						if (doc.isValid()) {
							document = batchDTO.getNextDocumentTo(doc, true);
						}
						eventBus.fireEvent(new TreeRefreshEvent(arg0, document, document.getPages().getPage().get(0)));
					}
				}
				ScreenMaskUtility.unmaskScreen();
				updateBatch(batchDTO.getBatch());
				setFocus();
			}
		});

	}

	public ConfirmationDialog updateBatch(final Batch batch) {
		confirmationDialog = null;
		rpcService.updateBatch(batch, new AsyncCallback<BatchStatus>() {

			@Override
			public void onFailure(Throwable arg0) {
				setControlSorQPressed(false);
				ScreenMaskUtility.unmaskScreen();
				ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
						ReviewValidateMessages.error_save_batch, batch.getBatchInstanceIdentifier()));
			}

			@Override
			public void onSuccess(BatchStatus arg0) {
				if (null != arg0) {
					confirmationDialog = new ConfirmationDialog();
					confirmationDialog.addDialogListener(new com.ephesoft.dcma.gwt.core.shared.ConfirmationDialog.DialogListener() {

						@Override
						public void onOkClick() {
							rpcService.signalWorkflow(batch, new AsyncCallback<BatchStatus>() {

								@Override
								public void onFailure(Throwable paramThrowable) {
									setControlSorQPressed(false);
									ScreenMaskUtility.unmaskScreen();
									ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
											ReviewValidateMessages.error_save_batch, batch.getBatchInstanceIdentifier())
											+ paramThrowable.getMessage());
								}

								@Override
								public void onSuccess(BatchStatus batchStatus) {
									setControlSorQPressed(false);
									ScreenMaskUtility.unmaskScreen();
									String href = Window.Location.getHref();
									String baseUrl = href.substring(0, href.lastIndexOf('/'));
									if (!batchSelected) {
										Window.Location.assign(baseUrl + ReviewValidateConstants.FILE_SEPARATOR
												+ ReviewValidateConstants.BATCH_LIST_HTML);
									} else {
										Window.Location.assign(baseUrl + ReviewValidateConstants.FILE_SEPARATOR
												+ ReviewValidateConstants.REVIEW_VALIDATE_HTML);
									}

								}
							});
						}

						@Override
						public void onCancelClick() {
							setControlSorQPressed(false);
							confirmationDialog.hide();
							ScreenMaskUtility.unmaskScreen();
							setFocus();
							eventBus.fireEvent(new TreeRefreshEvent(batchDTO, document, page));
						}

					});
					switch (arg0) {
						case REVIEWED:
							confirmationDialog.setDialogTitle(LocaleDictionary.get().getConstantValue(
									(ReviewValidateConstants.title_review_done)));
							confirmationDialog.setMessage(LocaleDictionary.get().getMessageValue(
									ReviewValidateMessages.msg_review_confirm));
							confirmationDialog.show();
							confirmationDialog.center();
							confirmationDialog.okButton.setFocus(true);
							break;
						case VALIDATED:
							confirmationDialog.setDialogTitle(LocaleDictionary.get().getConstantValue(
									(ReviewValidateConstants.title_validation_done)));
							confirmationDialog.setMessage(LocaleDictionary.get().getMessageValue(
									ReviewValidateMessages.msg_validation_confirm));
							confirmationDialog.show();
							confirmationDialog.center();
							confirmationDialog.okButton.setFocus(true);
							break;
						default:
							ScreenMaskUtility.unmaskScreen();
							setControlSorQPressed(false);
							break;
					}
				} else {
					setControlSorQPressed(false);
					ScreenMaskUtility.unmaskScreen();
				}
			}
		});
		return confirmationDialog;
	}

	private boolean checkIfManualStageCompleted(Document selectedDoc) {
		Batch batch = batchDTO.getBatch();
		boolean returnStatus = true;
		if (batch.getBatchStatus().name().equalsIgnoreCase(BatchStatus.READY_FOR_REVIEW.name())) {
			for (Document doc : batch.getDocuments().getDocument()) {
				if (doc.getIdentifier().equals(selectedDoc.getIdentifier())) {
					if (!doc.isReviewed()) {
						document = doc;
						returnStatus = false;
						break;
					}
				}
			}
		} else {
			for (Document doc : batch.getDocuments().getDocument()) {
				if (doc.getIdentifier().equals(selectedDoc.getIdentifier())) {
					if (!doc.isValid()) {
						document = doc;
						returnStatus = false;
						break;
					}
				}
			}
		}
		return returnStatus;
	}

	private void moveToTab(String htmlPattern) {
		String href = Window.Location.getHref();
		String baseUrl = href.substring(0, href.lastIndexOf('/'));
		Window.Location.assign(baseUrl + ReviewValidateConstants.FILE_SEPARATOR + htmlPattern);
	}

	private void moveToLandingPage() {
		String href = Window.Location.getHref();
		String baseUrl = href.substring(0, href.lastIndexOf('/'));
		Window.Location.assign(baseUrl + ReviewValidateConstants.FILE_SEPARATOR + ReviewValidateConstants.BATCH_LIST_HTML);
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

	private String addValues(List<Span> spanList) {
		StringBuffer value = new StringBuffer();
		if (spanList == null || !spanList.isEmpty()) {

			CoordinatesList coordinatesList = new CoordinatesList();
			int counter = 0;
			for (Span foundSpan : spanList) {

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

		String batchInstanceIdentifier = batchDTO.getBatch().getBatchInstanceIdentifier();
		ScreenMaskUtility.maskScreen();
		rpcService.getHOCRContent(pointCoordinate1, pointCoordinate2, batchInstanceIdentifier, this.page.getIdentifier(),
				rectangularCoordinateSet, new AsyncCallback<List<Span>>() {

					@Override
					public void onFailure(Throwable paramThrowable) {
						ScreenMaskUtility.unmaskScreen();
						ConfirmationDialogUtil.showConfirmationDialog(LocaleDictionary.get().getConstantValue(
								ReviewValidateConstants.UNABLE_TO_FIND), LocaleDictionary.get().getMessageValue(
								ReviewValidateMessages.ERROR_MESSAGE));
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

	public void getHOCRContent(List<PointCoordinate> pointCoordinates) {
		String batchInstanceIdentifier = batchDTO.getBatch().getBatchInstanceIdentifier();
		ScreenMaskUtility.maskScreen();
		rpcService.getHOCRContent(pointCoordinates, batchInstanceIdentifier, this.page.getIdentifier(),
				new AsyncCallback<List<Span>>() {

					@Override
					public void onFailure(Throwable paramThrowable) {
						ScreenMaskUtility.unmaskScreen();
						ConfirmationDialogUtil.showConfirmationDialog(LocaleDictionary.get().getConstantValue(
								ReviewValidateConstants.UNABLE_TO_FIND), LocaleDictionary.get().getMessageValue(
								ReviewValidateMessages.ERROR_MESSAGE));

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
		if (batchDTO.getBatch().getBatchStatus().equals(BatchStatus.READY_FOR_REVIEW)
				|| batchDTO.getBatch().getBatchStatus().equals(BatchStatus.READY_FOR_VALIDATION)) {
			ScreenMaskUtility.maskScreen();
			rpcService.getFunctionKeyDTOs(document.getType(), batchDTO.getBatch().getBatchInstanceIdentifier(),
					new AsyncCallback<List<FunctionKeyDTO>>() {

						@Override
						public void onFailure(final Throwable arg0) {
							ScreenMaskUtility.unmaskScreen();
							ConfirmationDialogUtil.showConfirmationDialog(LocaleDictionary.get().getConstantValue(
									ReviewValidateConstants.UNABLE_TO_FIND), LocaleDictionary.get().getMessageValue(
									ReviewValidateMessages.ERROR_MESSAGE));
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
									Button button = new Button(functionKeyDTO.getShortcutKeyName(), new ClickHandler() {

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
											Button nextButton = new Button(">", new ClickHandler() {

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
											Button previousButton = new Button("<", new ClickHandler() {

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
		this.isTableView = isTableView;
	}

	public boolean isTableView() {
		return isTableView;
	}

	public boolean isManualTableExtraction() {
		return isManualTableExtraction;
	}

	public void setManualTableExtraction(final boolean isManualTableExtraction) {
		this.isManualTableExtraction = isManualTableExtraction;
	}

	public void performOperationsOnCtrlSPress() {
		if (batchDTO.getFieldValueChangeScriptSwitchState().equals(ReviewValidateConstants.ON)
				&& batchDTO.getBatch().getBatchStatus().equals(BatchStatus.READY_FOR_VALIDATION)) {
			ScreenMaskUtility.maskScreen("Executing Script and Saving....");
			String fieldName = view.getRvPanel().getValidatePanel().getCurrentDocFieldWidgetName();
			rpcService.executeScriptOnFieldChange(batchDTO.getBatch(), document, fieldName, new AsyncCallback<BatchDTO>() {

				@Override
				public void onFailure(Throwable arg0) {
					ScreenMaskUtility.unmaskScreen();
					ConfirmationDialogUtil.showConfirmationDialog(LocaleDictionary.get().getConstantValue(
							ReviewValidateMessages.UNABLE_TO_EXECUTE_SCRIPT_AND_SAVE), LocaleDictionary.get().getMessageValue(
							ReviewValidateMessages.ERROR_MESSAGE));
				}

				@Override
				public void onSuccess(final BatchDTO arg0) {
					batchDTO = arg0;
					List<Document> documents = arg0.getBatch().getDocuments().getDocument();
					for (final Document doc : documents) {
						if (doc.getIdentifier().equalsIgnoreCase(document.getIdentifier())) {
							document = doc;
						}
					}
					validateBatch();
				}
			});
		} else {
			ScreenMaskUtility.maskScreen("Saving...");
			validateBatch();
		}
	}

	public void performOperationsOnCtrlQPress() {
		if (batchDTO.getFieldValueChangeScriptSwitchState().equals(ReviewValidateConstants.ON)
				&& batchDTO.getBatch().getBatchStatus().equals(BatchStatus.READY_FOR_VALIDATION)) {
			ScreenMaskUtility.maskScreen("Executing Script and Saving....");
			String fieldName = view.getRvPanel().getValidatePanel().getCurrentDocFieldWidgetName();
			rpcService.executeScriptOnFieldChange(batchDTO.getBatch(), document, fieldName, new AsyncCallback<BatchDTO>() {

				@Override
				public void onFailure(Throwable arg0) {
					ScreenMaskUtility.unmaskScreen();
					ConfirmationDialogUtil.showConfirmationDialog(LocaleDictionary.get().getConstantValue(
							ReviewValidateMessages.UNABLE_TO_EXECUTE_SCRIPT_AND_SAVE), LocaleDictionary.get().getMessageValue(
							ReviewValidateMessages.ERROR_MESSAGE));
				}

				@Override
				public void onSuccess(final BatchDTO arg0) {
					batchDTO = arg0;
					List<Document> documents = arg0.getBatch().getDocuments().getDocument();
					for (final Document doc : documents) {
						if (doc.getIdentifier().equalsIgnoreCase(document.getIdentifier())) {
							document = doc;
						}
					}
					reviewBatch();
				}
			});
		} else {
			ScreenMaskUtility.maskScreen("Saving...");
			reviewBatch();
		}
	}

	public void getTableHOCRContent(final PointCoordinate initialCoordinate, final PointCoordinate finalCoordinate) {
		String batchInstanceIdentifier = batchDTO.getBatch().getBatchInstanceIdentifier();
		ScreenMaskUtility.maskScreen();
		rpcService.getTableHOCRContent(initialCoordinate, finalCoordinate, batchInstanceIdentifier, this.page.getIdentifier(),
				new AsyncCallback<List<Span>>() {

					@Override
					public void onFailure(final Throwable paramThrowable) {
						ScreenMaskUtility.unmaskScreen();
						ConfirmationDialogUtil.showConfirmationDialog(LocaleDictionary.get().getMessageValue(
								ReviewValidateMessages.UNABLE_TO_FETCH_DATA), LocaleDictionary.get().getMessageValue(
								ReviewValidateMessages.ERROR_MESSAGE));
						getView().getTableExtractionView().undoManualExtractionChanges();
					}

					@Override
					public void onSuccess(final List<Span> spanList) {
						ScreenMaskUtility.unmaskScreen();
						if (spanList != null && !spanList.isEmpty() && isTableView) {
							getView().getTableExtractionView().setSelectedValue(spanList, initialCoordinate, finalCoordinate);
						}
					}
				});
	}

	public void getTableData(final Map<Integer, Coordinates> columnVsCoordinates, final String documentTypeName,
			DataTable selectedDataTable) {
		String batchInstanceIdentifier = batchDTO.getBatch().getBatchInstanceIdentifier();
		String batchClassIdentifier = batchDTO.getBatch().getBatchClassIdentifier();
		ScreenMaskUtility.maskScreen();
		rpcService.getTableData(columnVsCoordinates, documentTypeName, selectedDataTable, batchClassIdentifier,
				batchInstanceIdentifier, this.page.getIdentifier(), new AsyncCallback<List<Row>>() {

					@Override
					public void onFailure(final Throwable paramThrowable) {
						ScreenMaskUtility.unmaskScreen();
						ConfirmationDialogUtil.showConfirmationDialog(LocaleDictionary.get().getMessageValue(
								ReviewValidateMessages.UNABLE_TO_FETCH_DATA), LocaleDictionary.get().getMessageValue(
								ReviewValidateMessages.ERROR_MESSAGE));
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

	public void displayExternalApp(String newUrl, String title) {
		Map<String, String> dimensionsOfPopUpMap = batchDTO.getDimensionsForPopUp();
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
		Batch batch = batchDTO.getBatch();
		StringBuffer pathOfBatchXmlStringBuffer = new StringBuffer();
		pathOfBatchXmlStringBuffer.append(batch.getBatchLocalPath()).append("\\").append(
				batch.getBatchInstanceIdentifier() + "\\" + batch.getBatchInstanceIdentifier() + "_batch.xml");
		rpcService.getEncodedString(pathOfBatchXmlStringBuffer.toString(), new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable arg0) {
				ConfirmationDialogUtil.showConfirmationDialog(LocaleDictionary.get().getMessageValue(
						ReviewValidateMessages.UNABLE_TO_DISPLAY_EXTERNAL_APP), LocaleDictionary.get().getMessageValue(
						ReviewValidateMessages.ERROR_MESSAGE));
			}

			@Override
			public void onSuccess(final String pathOfBatchXml) {

				rpcService.getGeneratedSecurityTokenForExternalApp(new AsyncCallback<String>() {

					@Override
					public void onFailure(Throwable arg0) {
						ConfirmationDialogUtil.showConfirmationDialog(LocaleDictionary.get().getMessageValue(
								ReviewValidateMessages.UNABLE_TO_DISPLAY_EXTERNAL_APP), LocaleDictionary.get().getMessageValue(
								ReviewValidateMessages.ERROR_MESSAGE));

					}

					@Override
					public void onSuccess(String securityTokenString) {
						StringBuffer newUrl = new StringBuffer();
						String documentId = document.getIdentifier();
						newUrl.append(htmlPattern);
						if (htmlPattern.indexOf("?") != -1) {
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
									.getMessageValue(ReviewValidateMessages.ERROR_MESSAGE));
						}
						displayExternalApp(newUrl.toString(), title);
					}
				});
			}
		});

	}

	private void getUpdatedBatchDTO() {
		ScreenMaskUtility.maskScreen();
		rpcService.getBatch(batchDTO.getBatch().getBatchInstanceIdentifier(), new AsyncCallback<BatchDTO>() {

			@Override
			public void onFailure(Throwable arg0) {
				ScreenMaskUtility.unmaskScreen();
				ConfirmationDialogUtil.showConfirmationDialog(LocaleDictionary.get().getMessageValue(
						ReviewValidateMessages.UNABLE_TO_FETCH_BATCH_XML), LocaleDictionary.get().getMessageValue(
						ReviewValidateMessages.ERROR_MESSAGE));
			}

			@Override
			public void onSuccess(BatchDTO arg0) {
				batchDTO = arg0;
				Document updatedDocument = batchDTO.getDocumentById(document.getIdentifier());
				eventBus.fireEvent(new TreeRefreshEvent(batchDTO, updatedDocument, page));
				ScreenMaskUtility.unmaskScreen();
			}
		});

	}

	public void executeScriptOnFieldChange(String fieldName) {

		if (!isControlSorQPressed()) {
			if (document.getType().equals("Unknown")) {
				document.setReviewed(false);
			} else {
				document.setReviewed(true);
			}
			ScreenMaskUtility.maskScreen("Executing Script....");
			view.getRvPanel().getValidatePanel().setCurrentFieldSet(false);
			rpcService.executeScriptOnFieldChange(batchDTO.getBatch(), document, fieldName, new AsyncCallback<BatchDTO>() {

				@Override
				public void onFailure(Throwable arg0) {
					ScreenMaskUtility.unmaskScreen();
				}

				@Override
				public void onSuccess(final BatchDTO arg0) {
					batchDTO = arg0;
					List<Document> documents = arg0.getBatch().getDocuments().getDocument();
					for (final Document doc : documents) {
						if (doc.getIdentifier().equalsIgnoreCase(document.getIdentifier())) {
							document = doc;
							view.getRvPanel().getValidatePanel().setFieldAlreadySelected(true);
							if (doc.isValid()) {
								view.getRvPanel().getValidatePanel().setFieldAlreadySelected(false);
								document = batchDTO.getNextDocumentTo(doc, true);
							}
							eventBus.fireEvent(new TreeRefreshEvent(arg0, document, document.getPages().getPage().get(0)));
						}
					}
					updateBatch(batchDTO.getBatch());
				}
			});
		}
	}

	public void setCurrentFieldName(String currentFieldName) {
		this.currentFieldName = currentFieldName;
	}

	public String getCurrentFieldName() {
		return currentFieldName;
	}

	public boolean isControlSorQPressed() {
		return isControlSorQPressed;
	}

	public void setControlSorQPressed(boolean isControlSorQPressed) {
		this.isControlSorQPressed = isControlSorQPressed;
	}

	public void validateBatch() {
		if (checkDocumentType()) {
			if (document.getType().equals("Unknown")) {
				document.setReviewed(false);
			} else {
				document.setReviewed(true);
			}
			if (batchDTO.getBatch().getBatchStatus().equals(BatchStatus.READY_FOR_VALIDATION)
					&& ReviewValidateConstants.ON.equalsIgnoreCase(batchDTO.getIsValidationScriptEnabled())) {
				rpcService.executeScript(batchDTO.getBatch(), new AsyncCallback<BatchDTO>() {

					@Override
					public void onFailure(Throwable arg0) {
						ScreenMaskUtility.unmaskScreen();
						setControlSorQPressed(false);
					}

					@Override
					public void onSuccess(final BatchDTO arg0) {
						batchDTO = arg0;
						List<Document> documents = arg0.getBatch().getDocuments().getDocument();
						for (final Document doc : documents) {
							if (doc.getIdentifier().equalsIgnoreCase(document.getIdentifier())) {
								document = doc;
								if (doc.isValid()) {
									document = batchDTO.getNextDocumentTo(doc, true);
								}
								eventBus.fireEvent(new TreeRefreshEvent(arg0, document, document.getPages().getPage().get(0)));
								break;
							}
						}
						updateBatch(batchDTO.getBatch());
					}
				});
			} else if (batchDTO.getBatch().getBatchStatus().equals(BatchStatus.READY_FOR_VALIDATION)) {
				List<Document> documents = batchDTO.getBatch().getDocuments().getDocument();
				for (final Document doc : documents) {
					if (doc.getIdentifier().equalsIgnoreCase(document.getIdentifier())) {
						document = doc;
						if (doc.isValid()) {
							document = batchDTO.getNextDocumentTo(doc, true);
						}
						eventBus.fireEvent(new TreeRefreshEvent(batchDTO, document, document.getPages().getPage().get(0)));
						break;
					}
				}
				updateBatch(batchDTO.getBatch());
			} else {
				eventBus.fireEvent(new DocumentRefreshEvent(document));
				updateBatch(batchDTO.getBatch());
			}
		} else {
			errorMessage();
			updateBatch(batchDTO.getBatch());
		}
		setFocus();
	}

	public void reviewBatch() {
		if (checkDocumentType()) {
			if (document.getType().equals("Unknown")) {
				document.setReviewed(false);
			} else {
				document.setReviewed(true);
			}
			if (batchDTO.getBatch().getBatchStatus().equals(BatchStatus.READY_FOR_VALIDATION)
					&& ReviewValidateConstants.ON.equalsIgnoreCase(batchDTO.getIsValidationScriptEnabled())) {

				rpcService.executeScript(batchDTO.getBatch(), new AsyncCallback<BatchDTO>() {

					@Override
					public void onFailure(Throwable arg0) {
						ScreenMaskUtility.unmaskScreen();
						setControlSorQPressed(false);
					}

					@Override
					public void onSuccess(final BatchDTO arg0) {
						batchDTO = arg0;
						List<Document> documents = arg0.getBatch().getDocuments().getDocument();
						for (final Document doc : documents) {
							if (doc.getIdentifier().equalsIgnoreCase(document.getIdentifier())) {
								document = doc;
								if (checkIfManualStageCompleted(document)) {
									document = batchDTO.getNextDocumentTo(doc, true);
								}
								eventBus.fireEvent(new TreeRefreshEvent(arg0, document, document.getPages().getPage().get(0)));
								break;
							}
						}
						updateBatch(batchDTO.getBatch());
					}
				});
			} else {
				eventBus.fireEvent(new DocumentRefreshEvent(document));
				updateBatch(batchDTO.getBatch());
			}
		} else {
			errorMessage();
			updateBatch(batchDTO.getBatch());
		}
		setFocus();
	}
}