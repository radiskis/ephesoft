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
						ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
								ReviewValidateMessages.error_batch_status, batchId, batchDTO.getBatch().getBatchStatus()));
						getHighestPriorityBatch();
						return;
					}

					final String batchID2 = batchDTO.getBatch().getBatchInstanceIdentifier();
					rpcService.acquireLock(batchID2, new AsyncCallback<Void>() {

						@Override
						public void onFailure(Throwable arg0) {
							ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
									ReviewValidateMessages.error_batch_already_locked,
									batchDTO.getBatch().getBatchInstanceIdentifier()));
							moveToLandingPage();
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
							ScreenMaskUtility.maskScreen("Please wait...");
							if (checkDocumentType()) {
								if (document.getType().equals("Unknown")) {
									document.setReviewed(false);
								} else {
									document.setReviewed(true);
								}
								if (batchDTO.getBatch().getBatchStatus().equals(BatchStatus.READY_FOR_VALIDATION)
										&& "ON".equalsIgnoreCase(batchDTO.getIsScriptEnabled())) {
									rpcService.executeScript(batchDTO.getBatch(), new AsyncCallback<BatchDTO>() {

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
													if (!doc.isValid()) {
														eventBus.fireEvent(new TreeRefreshEvent(arg0, document, document.getPages()
																.getPage().get(0)));
													} else {
														eventBus.fireEvent(new DocumentRefreshEvent(document));
													}
													break;
												}
											}
											updateBatch(batchDTO.getBatch());
											ScreenMaskUtility.unmaskScreen();
										}
									});
								} else if (batchDTO.getBatch().getBatchStatus().equals(BatchStatus.READY_FOR_VALIDATION)) {
									List<Document> documents = batchDTO.getBatch().getDocuments().getDocument();
									for (final Document doc : documents) {
										if (doc.getIdentifier().equalsIgnoreCase(document.getIdentifier())) {
											document = doc;
											if (!doc.isValid()) {
												eventBus.fireEvent(new TreeRefreshEvent(batchDTO, document, document.getPages()
														.getPage().get(0)));
											} else {
												eventBus.fireEvent(new DocumentRefreshEvent(document));
											}
											break;
										}
									}
									updateBatch(batchDTO.getBatch());
									ScreenMaskUtility.unmaskScreen();
								} else {
									eventBus.fireEvent(new DocumentRefreshEvent(document));
									updateBatch(batchDTO.getBatch());
								}
							} else {
								errorMessage();
							}
							setFocus();
							break;
						case 'q':
						case 'Q':
							event.getEvent().getNativeEvent().preventDefault();
							ScreenMaskUtility.maskScreen("Please wait...");
							// if document is unknown then set review to false else use below condition.
							if (checkDocumentType()) {
								if (document.getType().equals("Unknown")) {
									document.setReviewed(false);
								} else {
									document.setReviewed(true);
								}
								if (batchDTO.getBatch().getBatchStatus().equals(BatchStatus.READY_FOR_VALIDATION)
										&& "ON".equalsIgnoreCase(batchDTO.getIsScriptEnabled())) {

									rpcService.executeScript(batchDTO.getBatch(), new AsyncCallback<BatchDTO>() {

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
													if (!checkIfManualStageCompleted(document)) {
														eventBus.fireEvent(new TreeRefreshEvent(arg0, document, document.getPages()
																.getPage().get(0)));
													} else {
														eventBus.fireEvent(new DocumentRefreshEvent(document));
													}
													break;
												}
											}
											updateBatch(batchDTO.getBatch());
											ScreenMaskUtility.unmaskScreen();
										}
									});
								} else {
									eventBus.fireEvent(new DocumentRefreshEvent(document));
									updateBatch(batchDTO.getBatch());
								}
							} else {
								errorMessage();
							}
							setFocus();
							break;

						default:
							break;
					}
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
						if (!doc.isValid()) {
							eventBus.fireEvent(new TreeRefreshEvent(arg0, document, document.getPages().getPage().get(0)));
						} else {
							eventBus.fireEvent(new DocumentRefreshEvent(document));
						}
						break;
					}
				}
				ScreenMaskUtility.unmaskScreen();
				setFocus();
			}
		});

	}

	private ConfirmationDialog updateBatch(final Batch batch) {
		ScreenMaskUtility.maskScreen();
		confirmationDialog = null;
		rpcService.updateBatch(batch, new AsyncCallback<BatchStatus>() {

			@Override
			public void onFailure(Throwable arg0) {
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
									ScreenMaskUtility.unmaskScreen();
									ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
											ReviewValidateMessages.error_save_batch, batch.getBatchInstanceIdentifier())
											+ paramThrowable.getMessage());
								}

								@Override
								public void onSuccess(BatchStatus batchStatus) {
									ScreenMaskUtility.unmaskScreen();
									String href = Window.Location.getHref();
									String baseUrl = href.substring(0, href.lastIndexOf('/'));
									if (!batchSelected) {
										Window.Location.assign(baseUrl + "/" + "BatchList.html");
									} else {
										Window.Location.assign(baseUrl + "/" + "ReviewValidate.html");
									}

								}
							});
						}

						@Override
						public void onCancelClick() {
							confirmationDialog.hide();
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
							break;
					}
				}
				ScreenMaskUtility.unmaskScreen();
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
		Window.Location.assign(baseUrl + "/" + htmlPattern);
	}

	private void moveToLandingPage() {
		String href = Window.Location.getHref();
		String baseUrl = href.substring(0, href.lastIndexOf('/'));
		Window.Location.assign(baseUrl + "/" + "BatchList.html");
	}

	public void onTableViewButtonClicked() {
		setTableView(Boolean.TRUE);
		view.getSlidingPanel().setWidget(view.getTableView());
	}

	public void onTableViewBackButtonClicked() {
		setTableView(Boolean.FALSE);
		view.setReviewValidateView();
	}

	private boolean checkDocumentType() {
		return view.getRvPanel().getReviewPanel().isSuggestBoxValid();
	}

	private void errorMessage() {
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

	public void getHOCRContent(PointCoordinate pointCoordinate1, PointCoordinate pointCoordinate2) {

		String batchInstanceIdentifier = batchDTO.getBatch().getBatchInstanceIdentifier();
		ScreenMaskUtility.maskScreen();
		rpcService.getHOCRContent(pointCoordinate1, pointCoordinate2, batchInstanceIdentifier, this.page.getIdentifier(),
				new AsyncCallback<List<Span>>() {

					@Override
					public void onFailure(Throwable paramThrowable) {
						ScreenMaskUtility.unmaskScreen();
						ConfirmationDialogUtil.showConfirmationDialog(LocaleDictionary.get().getConstantValue(
								ReviewValidateConstants.UNABLE_TO_FOUND), LocaleDictionary.get().getMessageValue(
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
								ReviewValidateConstants.UNABLE_TO_FOUND), LocaleDictionary.get().getMessageValue(
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
		if (batchDTO.getBatch().getBatchStatus().equals(BatchStatus.READY_FOR_VALIDATION)) {
			ScreenMaskUtility.maskScreen();
			rpcService.getFunctionKeyDTOs(document.getType(), batchDTO.getBatch().getBatchInstanceIdentifier(),
					new AsyncCallback<List<FunctionKeyDTO>>() {

						@Override
						public void onFailure(final Throwable arg0) {
							ScreenMaskUtility.unmaskScreen();
							ConfirmationDialogUtil.showConfirmationDialog(LocaleDictionary.get().getConstantValue(
									ReviewValidateConstants.UNABLE_TO_FOUND), LocaleDictionary.get().getMessageValue(
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
											if (document.isReviewed()) {
												executeScript(functionKeyDTO.getShortcutKeyName());
											}
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

	public void getAllUrlShortcuts() {
		ScreenMaskUtility.maskScreen();
		rpcService.getUrlsOfExternalAppByShortcuts(batchDTO.getBatch().getBatchInstanceIdentifier(),
				new AsyncCallback<Map<String, String>>() {

					@Override
					public void onFailure(Throwable arg0) {
						ScreenMaskUtility.unmaskScreen();
						ConfirmationDialogUtil.showConfirmationDialog(LocaleDictionary.get().getMessageValue(
								ReviewValidateMessages.UNABLE_TO_FETCH_EXTERNAL_APP_INFO), LocaleDictionary.get().getMessageValue(
								ReviewValidateMessages.ERROR_MESSAGE));

					}

					@Override
					public void onSuccess(Map<String, String> shortcutUrlMap) {
						ScreenMaskUtility.unmaskScreen();
						view.getRvPanel().setButtonsForUrls(shortcutUrlMap);

					}

				});
	}

	public void displayExternalApp(final String htmlPattern) {
		if (htmlPattern != null && !htmlPattern.isEmpty()) {
			rpcService.getDimensionsForPopUp(batchDTO.getBatch().getBatchInstanceIdentifier(),
					new AsyncCallback<Map<String, String>>() {

						@Override
						public void onFailure(Throwable arg0) {
							ScreenMaskUtility.unmaskScreen();
							ConfirmationDialogUtil.showConfirmationDialog(LocaleDictionary.get().getMessageValue(
									ReviewValidateMessages.UNABLE_TO_FETCH_EXTERNAL_APP_INFO), LocaleDictionary.get().getMessageValue(
									ReviewValidateMessages.ERROR_MESSAGE));
						}

						@Override
						public void onSuccess(Map<String, String> dimensionsOfPopUpMap) {
							StringBuffer newUrl = new StringBuffer();
							StringBuffer pathOfBatchXml = new StringBuffer();
							Batch batch = batchDTO.getBatch();
							pathOfBatchXml.append(batch.getBatchLocalPath()).append("\\").append(
									batch.getBatchInstanceIdentifier() + "\\" + batch.getBatchInstanceIdentifier() + "_batch.xml");
							String documentId = document.getIdentifier();
							newUrl.append(htmlPattern);
							newUrl.append("?document_id=").append(documentId);
							newUrl.append("&batch_xml_path=").append(pathOfBatchXml);

							final ExternalAppDialogBox externalAppDialogBox = new ExternalAppDialogBox(newUrl.toString(),
									dimensionsOfPopUpMap);
							externalAppDialogBox.setDialogTitle(htmlPattern);
							externalAppDialogBox
									.addDialogBoxListener(new com.ephesoft.dcma.gwt.rv.client.view.ExternalAppDialogBox.DialogBoxListener() {

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
					});
		}
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

}
