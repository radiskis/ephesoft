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
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.ephesoft.dcma.batch.schema.Batch;
import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.core.common.BatchInstanceStatus;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.client.ui.ScreenMaskUtility;
import com.ephesoft.dcma.gwt.core.client.ui.SuggestionBox;
import com.ephesoft.dcma.gwt.core.client.validator.SuggestBoxValidator;
import com.ephesoft.dcma.gwt.core.client.validator.ValidatableWidget;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialog;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialogUtil;
import com.ephesoft.dcma.gwt.core.shared.DocumentTypeDBBean;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialog.DialogListener;
import com.ephesoft.dcma.gwt.rv.client.event.DocExpandEvent;
import com.ephesoft.dcma.gwt.rv.client.event.DocExpandEventHandler;
import com.ephesoft.dcma.gwt.rv.client.event.DocTypeChangeEvent;
import com.ephesoft.dcma.gwt.rv.client.event.RVKeyUpEvent;
import com.ephesoft.dcma.gwt.rv.client.event.RVKeyUpEventHandler;
import com.ephesoft.dcma.gwt.rv.client.event.ThumbnailSelectionEvent;
import com.ephesoft.dcma.gwt.rv.client.event.TreeRefreshEvent;
import com.ephesoft.dcma.gwt.rv.client.event.TreeRefreshEventHandler;
import com.ephesoft.dcma.gwt.rv.client.i18n.ReviewValidateConstants;
import com.ephesoft.dcma.gwt.rv.client.i18n.ReviewValidateMessages;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;

/**
 * The <code>ReviewPanel</code> class provides functionality to show review panel view shown to user.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.gwt.rv.client.view
 * 
 */
public class ReviewPanel extends RVBasePanel {

	@UiField
	protected ListBox documentList;
	@UiField
	protected Label docTypeText;
	@UiField
	protected Label mergeDocText;
	@UiField
	protected VerticalPanel documentTypePanel;

	private SuggestionBox documentTypeSuggestBox;

	protected ListBox documentTypes;

	protected ValidatableWidget<SuggestBox> validatableSuggestBox;

	private final Map<Integer, Document> indexedDocumentMap = new LinkedHashMap<Integer, Document>();

	private static final String LIST_VIEW = "dropdown_list";

	private static final String SUGGEST_BOX_VIEW = "suggest_box";

	private static final String EMPTY_STRING = "";

	private String currentDocTypeView = null;

	interface Binder extends UiBinder<VerticalPanel, ReviewPanel> {
	}

	private static final Binder BINDER = GWT.create(Binder.class);
	private boolean keyPressed = true;
	private boolean keyPressedForDocList = true;

	private void fireDocTypeChangeEvent() {
		ScreenMaskUtility.maskScreen();
		final String docType = documentTypes.getValue(documentTypes.getSelectedIndex());
		onDocumentTypeChange(docType);
		keyPressed = true;
	}

	public ReviewPanel() {
		super();
		initWidget(BINDER.createAndBindUi(this));
		documentList.addStyleName(ReviewValidateConstants.DROPBOX_STYLE);

		docTypeText.setText(LocaleDictionary.get().getConstantValue(ReviewValidateConstants.TITLE_REVIEWPANEL_DOCTYPE));
		mergeDocText.setText(LocaleDictionary.get().getConstantValue(ReviewValidateConstants.TITLE_REVIEWPANEL_MERGEDOCWITH));
		docTypeText.addStyleName("bold_text");
		mergeDocText.addStyleName("bold_text");
		documentTypes = new ListBox();
		documentTypes.setVisible(true);
		documentTypes.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent arg0) {
				if (!keyPressed) {
					fireDocTypeChangeEvent();
					keyPressed = true;
				}
			}
		});

		documentTypes.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				keyPressed = false;
			}
		});

		documentTypes.addKeyDownHandler(new KeyDownHandler() {

			@Override
			public void onKeyDown(KeyDownEvent arg0) {
				keyPressed = true;
				if (arg0.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					arg0.preventDefault();
					fireDocTypeChangeEvent();
				}

			}
		});

		documentList.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent arg0) {
				if (!keyPressedForDocList) {
					fireDocListChangeEvent();
					keyPressedForDocList = true;
				}

			}
		});
		documentList.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				keyPressedForDocList = false;
			}
		});
		documentList.addKeyDownHandler(new KeyDownHandler() {

			@Override
			public void onKeyDown(KeyDownEvent arg0) {
				keyPressedForDocList = true;
				if (arg0.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					arg0.preventDefault();
					fireDocListChangeEvent();
				}
			}
		});

	}

	private void fireDocListChangeEvent() {
		if (documentList.getSelectedIndex() != 0) {
			final Document selectedDoc = indexedDocumentMap.get(documentList.getSelectedIndex());
			mergeDocument(selectedDoc);
			keyPressedForDocList = false;
		}
	}

	@Override
	public void initializeWidget() {
		// no need to do anything while initializing widget
	}

	public void clearPanel() {
		documentTypes.clear();
		documentList.setEnabled(false);

	}

	@Override
	public void injectEvents(HandlerManager eventBus) {
		addDocExpandEventHandler(eventBus);

		addTreeRefreshEventHandler(eventBus);

		addRVKeyUpEventHandler(eventBus);
	}

	/**
	 * @param eventBus
	 */
	private void addRVKeyUpEventHandler(HandlerManager eventBus) {
		eventBus.addHandler(RVKeyUpEvent.type, new RVKeyUpEventHandler() {

			@Override
			public void onKeyUp(RVKeyUpEvent event) {
				if (event.getEvent().isControlKeyDown()) {
					switch (event.getEvent().getNativeKeyCode()) {
						// CTRL + ;
						case 59:
						case 186:
							if (!presenter.isTableView()) {
								event.getEvent().getNativeEvent().preventDefault();
								setFocus();
							}
							break;
						// CTRL + /
						case 191:
							if (!presenter.isTableView()) {
								getAndMergeDocumentListInBatch(event);
							}
							break;
						case '0':
							if (!presenter.isTableView()) {
								toggleView();
								presenter.getView().getRvPanel().getValidatePanel().resetFocusToDocumentField(
										presenter.getCurrentDocumentFieldName());
							}
							break;
						default:
							break;
					}
				}
			}

			private void getAndMergeDocumentListInBatch(RVKeyUpEvent event) {
				event.getEvent().getNativeEvent().preventDefault();
				List<Document> documentListInBatch = presenter.batchDTO.getBatch().getDocuments().getDocument();
				for (int index = 0; index < documentListInBatch.size(); index++) {
					if (documentListInBatch.get(index).getIdentifier().equals(presenter.document.getIdentifier())
							&& index > 0) {
						mergeDocument(documentListInBatch.get(index - 1));
						break;
					}
				}
			}

		});
	}

	/**
	 * @param eventBus
	 */
	private void addTreeRefreshEventHandler(HandlerManager eventBus) {
		eventBus.addHandler(TreeRefreshEvent.type, new TreeRefreshEventHandler() {

			@Override
			public void refresh(TreeRefreshEvent treeRefreshEvent) {
				// If any of the following entities is null... this means there is no page or document left in the batch.
				// Set the validate panel visibility to false.
				Batch batch = presenter.batchDTO.getBatch();
				if (batch == null || batch.getDocuments() == null || batch.getDocuments().getDocument() == null
						|| batch.getDocuments().getDocument().size() < 1) {
					documentTypes.clear();
					documentList.setEnabled(Boolean.FALSE);
					documentTypes.setEnabled(Boolean.FALSE);
					return;
				}
				setFocus();
			}
		});
	}

	/**
	 * @param eventBus
	 */
	private void addDocExpandEventHandler(HandlerManager eventBus) {
		eventBus.addHandler(DocExpandEvent.type, new DocExpandEventHandler() {

			@Override
			public void onExpand(DocExpandEvent event) {
				presenter.document = event.getDocument();

				presenter.rpcService.getDocTypeByBatchInstanceID(presenter.batchDTO.getBatch().getBatchInstanceIdentifier(),
						new AsyncCallback<List<DocumentTypeDBBean>>() {

							@Override
							public void onSuccess(List<DocumentTypeDBBean> documentTypesList) {
								Collections.sort(documentTypesList, new DocumentTypesComparator());
								if (documentTypes == null) {
									documentTypes = new ListBox();
								}
								documentTypes.clear();
								documentTypes.addStyleName(ReviewValidateConstants.DROPBOX_STYLE);
								int index = 0;
								int indexUnknown = 0;
								boolean docSelected = false;
								String actualValue = null;
								for (DocumentTypeDBBean bean : documentTypesList) {
									if (bean.getName().equalsIgnoreCase("Unknown")) {
										documentTypes.addItem(LocaleDictionary.get().getConstantValue(
												ReviewValidateConstants.DOCUMENT_TYPE_UNKNOWN), bean.getName());
										indexUnknown = index;
									} else {
										documentTypes.addItem(bean.getDescription(), bean.getName());
									}
									if (bean.getName().equals(presenter.document.getType())) {
										documentTypes.setItemSelected(index, true);
										actualValue = bean.getDescription();
										docSelected = true;
									}

									index++;
								}
								if (!docSelected) {
									documentTypes.setSelectedIndex(indexUnknown);
									actualValue = "Unknown";
								}
								documentTypes.setVisible(true);
								if (documentTypeSuggestBox != null) {
									documentTypeSuggestBox.hideSuggestionList();

								}
								MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
								documentTypeSuggestBox = new SuggestionBox(oracle);
								documentTypeSuggestBox.hideSuggestionList();
								documentTypeSuggestBox.addStyleName(ReviewValidateConstants.INPUTBOX_STYLE);

								setHandlerForSuggestBox(documentTypeSuggestBox);
								if (actualValue == null) {
									actualValue = EMPTY_STRING;
								}
								oracle.add(actualValue);
								for (int i = 0; i < documentTypes.getItemCount(); i++) {
									oracle.add(documentTypes.getItemText(i));
								}
								documentTypeSuggestBox.setValue(actualValue, true);
								setDocTypeView();
								ReviewPanel.this.fireEvent(new ThumbnailSelectionEvent(presenter.page));

							}

							@Override
							public void onFailure(Throwable arg0) {
								// do nothing if unable to get document type by batch instance id
							}
						});

				documentList.clear();
				documentList.addItem(LocaleDictionary.get().getConstantValue(ReviewValidateConstants.TITLE_SELECT_DOC));
				documentList.setEnabled(Boolean.TRUE);

				indexedDocumentMap.clear();

				List<Document> documentListInBatch = presenter.batchDTO.getBatch().getDocuments().getDocument();
				if (documentListInBatch.size() == 1) {
					documentList.setEnabled(Boolean.FALSE);
				}

				int index = 1;
				for (Document document : documentListInBatch) {
					if (!document.getIdentifier().equals(presenter.document.getIdentifier())) {
						indexedDocumentMap.put(index, document);
						documentList.addItem(document.getIdentifier());
						index++;
					}
				}
				setFocus();
			}
		});
	}

	private void mergeDocument(final Document selectedDoc) {
		final ConfirmationDialog confirmationDialog = ConfirmationDialogUtil.showConfirmationDialog(LocaleDictionary.get()
				.getMessageValue(ReviewValidateMessages.MSG_TREE_MERGE_DOC, presenter.document.getIdentifier(),
						selectedDoc.getIdentifier()), LocaleDictionary.get().getConstantValue(
				ReviewValidateConstants.TITLE_MERGE_CONFIRM), Boolean.FALSE);

		confirmationDialog.addDialogListener(new DialogListener() {

			@Override
			public void onOkClick() {
				presenter
						.mergeDocument(presenter.batchDTO.getBatch(), selectedDoc.getIdentifier(), presenter.document.getIdentifier());
				confirmationDialog.hide();
				ReviewPanel.this.fireEvent(new TreeRefreshEvent(presenter.batchDTO, presenter.document, null));
			}

			@Override
			public void onCancelClick() {
				documentList.setItemSelected(0, true);
				confirmationDialog.hide();
				presenter.setFocus();
			}
		});

	}

	class DocumentTypesComparator implements Comparator<DocumentTypeDBBean> {

		@Override
		public int compare(DocumentTypeDBBean firstObject, DocumentTypeDBBean secondObject) {
			int comparisonValue = 0;
			if (firstObject instanceof DocumentTypeDBBean && secondObject instanceof DocumentTypeDBBean) {
				String name1 = ((DocumentTypeDBBean) firstObject).getDescription();
				String name2 = ((DocumentTypeDBBean) secondObject).getDescription();
				comparisonValue = name1.compareTo(name2);
			}
			return comparisonValue;
		}

	}

	public final void setFocus() {
		if (presenter.batchDTO.getBatchInstanceStatus().equals(BatchInstanceStatus.READY_FOR_REVIEW)) {
			if (documentTypes.isVisible()) {
				documentTypes.setFocus(true);
			} else if (documentTypeSuggestBox.isVisible()) {
				documentTypeSuggestBox.setFocus(true);
			}
		}
	}

	private void setFocus(boolean isSuggestBox) {
		if (presenter.batchDTO.getBatchInstanceStatus().equals(BatchInstanceStatus.READY_FOR_REVIEW)) {
			if (isSuggestBox) {
				documentTypeSuggestBox.setFocus(true);
			} else {
				documentTypes.setFocus(true);
			}
		}
	}

	private void enableSuggestBox() {
		documentTypePanel.clear();
		documentTypePanel.add(documentTypeSuggestBox);
		documentTypeSuggestBox.setVisible(true);
		documentTypeSuggestBox.setText(documentTypes.getItemText(documentTypes.getSelectedIndex()));
		documentTypes.setVisible(false);
		currentDocTypeView = SUGGEST_BOX_VIEW;
		documentTypeSuggestBox.getTextBox().selectAll();
		setFocus(true);
	}

	private void enableListBox() {

		documentTypePanel.clear();
		documentTypePanel.add(documentTypes);
		documentTypes.setVisible(true);
		documentTypeSuggestBox.setVisible(false);
		currentDocTypeView = LIST_VIEW;
		setFocus(false);
	}

	private void toggleView() {
		if (documentTypes.isVisible()) {
			enableSuggestBox();
		} else if (documentTypeSuggestBox.isVisible()) {
			enableListBox();
		}
	}

	private void onDocumentTypeChange(final String docType) {
		presenter.rpcService.getFdTypeByDocTypeName(presenter.batchDTO.getBatch().getBatchInstanceIdentifier(), docType,
				new AsyncCallback<Document>() {

					@Override
					public void onFailure(Throwable arg0) {
						ScreenMaskUtility.unmaskScreen();
					}

					@Override
					public void onSuccess(Document documentType) {

						if (docType.equals(LocaleDictionary.get().getConstantValue(ReviewValidateConstants.DOCUMENT_TYPE_UNKNOWN))) {
							documentType.setReviewed(false);
							documentType.setType("Unknown");
						} else {
							documentType.setType(docType);
						}
						documentType.setDocumentTypeChanged(true);
						documentType.setIdentifier(presenter.document.getIdentifier());
						documentType.setPages(presenter.document.getPages());
						documentType.setDocumentDisplayInfo(EMPTY_STRING);
						List<Document> listDocumentTypes = presenter.batchDTO.getBatch().getDocuments().getDocument();
						int index = 0;
						for (Document documentType2 : listDocumentTypes) {
							if (documentType2.getIdentifier().equals(presenter.document.getIdentifier())) {
								listDocumentTypes.remove(documentType2);
								listDocumentTypes.add(index, documentType);
								break;
							}
							index++;
						}

						ReviewPanel.this.fireEvent(new DocTypeChangeEvent(documentType, presenter.batchDTO));

						ScreenMaskUtility.unmaskScreen();
					}
				});
	}

	private void setHandlerForSuggestBox(final SuggestBox suggestBox) {
		suggestBox.addSelectionHandler(new SelectionHandler<Suggestion>() {

			@Override
			public void onSelection(SelectionEvent<Suggestion> arg0) {
				String value = suggestBox.getText();
				for (int i = 0; i < documentTypes.getItemCount(); i++) {
					if (documentTypes.getItemText(i).equalsIgnoreCase(value)) {
						ScreenMaskUtility.maskScreen();
						onDocumentTypeChange(documentTypes.getValue(i));
						break;
					}
				}
			}
		});
		validatableSuggestBox = new ValidatableWidget<SuggestBox>(suggestBox);

		validatableSuggestBox.getWidget().addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				validatableSuggestBox.toggleValidDateBox();
			}
		});

		List<String> altValues = new ArrayList<String>();
		for (int i = 0; i < documentTypes.getItemCount(); i++) {
			altValues.add(documentTypes.getItemText(i));
		}
		validatableSuggestBox.addValidator(new SuggestBoxValidator(suggestBox, altValues));
		validatableSuggestBox.toggleValidDateBox();
	}

	public boolean isSuggestBoxValid() {
		boolean isValidated = true;
		if (documentTypeSuggestBox.isVisible()) {
			isValidated = validatableSuggestBox.validate();
		}
		return isValidated;
	}

	private void setDocumentView() {
		if (currentDocTypeView.equalsIgnoreCase(LIST_VIEW)) {
			enableListBox();
		} else {
			enableSuggestBox();
		}
	}

	/**
	 * 
	 */
	private void setDocTypeView() {
		if (currentDocTypeView == null) {
			presenter.rpcService.getDefaultDocTypeView(new AsyncCallback<String>() {

				@Override
				public void onFailure(Throwable paramThrowable) {
					ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
							ReviewValidateMessages.DEFAULT_DOC_TYPE_VIEW_FAILURE)
							+ paramThrowable.getMessage());
					enableListBox();
				}

				@Override
				public void onSuccess(String docType) {
					if (docType.equalsIgnoreCase(SUGGEST_BOX_VIEW)) {
						enableSuggestBox();
					} else {
						enableListBox();
					}
				}
			});
		} else {
			setDocumentView();
		}
	}
}
