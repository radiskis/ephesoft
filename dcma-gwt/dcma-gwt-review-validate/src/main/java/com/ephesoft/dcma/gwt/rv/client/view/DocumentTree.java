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

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.ephesoft.dcma.batch.schema.Batch;
import com.ephesoft.dcma.batch.schema.Coordinates;
import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.batch.schema.Field;
import com.ephesoft.dcma.batch.schema.Page;
import com.ephesoft.dcma.core.common.BatchInstanceStatus;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.client.ui.RotatableImage;
import com.ephesoft.dcma.gwt.core.client.ui.ScreenMaskUtility;
import com.ephesoft.dcma.gwt.core.shared.BatchDTO;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialogUtil;
import com.ephesoft.dcma.gwt.rv.client.event.DocExpandEvent;
import com.ephesoft.dcma.gwt.rv.client.event.DocTypeChangeEvent;
import com.ephesoft.dcma.gwt.rv.client.event.DocTypeChangeEventHandler;
import com.ephesoft.dcma.gwt.rv.client.event.DocumentRefreshEvent;
import com.ephesoft.dcma.gwt.rv.client.event.DocumentRefreshHandler;
import com.ephesoft.dcma.gwt.rv.client.event.IconRefreshEvent;
import com.ephesoft.dcma.gwt.rv.client.event.IconRefreshEventHandler;
import com.ephesoft.dcma.gwt.rv.client.event.PageChangeEvent;
import com.ephesoft.dcma.gwt.rv.client.event.PageChangeEventHandler;
import com.ephesoft.dcma.gwt.rv.client.event.RVKeyDownEvent;
import com.ephesoft.dcma.gwt.rv.client.event.RVKeyDownEventHandler;
import com.ephesoft.dcma.gwt.rv.client.event.RVKeyUpEvent;
import com.ephesoft.dcma.gwt.rv.client.event.RVKeyUpEventHandler;
import com.ephesoft.dcma.gwt.rv.client.event.ThumbnailSelectionEvent;
import com.ephesoft.dcma.gwt.rv.client.event.TreeRefreshEvent;
import com.ephesoft.dcma.gwt.rv.client.event.TreeRefreshEventHandler;
import com.ephesoft.dcma.gwt.rv.client.event.ValidationFieldChangeEvent;
import com.ephesoft.dcma.gwt.rv.client.event.ValidationFieldChangeEventHandler;
import com.ephesoft.dcma.gwt.rv.client.i18n.ReviewValidateConstants;
import com.ephesoft.dcma.gwt.rv.client.i18n.ReviewValidateMessages;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;

public class DocumentTree extends RVBasePanel {

	interface Binder extends UiBinder<DockLayoutPanel, DocumentTree> {
	}

	@UiField
	protected Tree docTree;

	private TreeItem selectedDocItem;
	private RotatableImage selectedImage;
	@UiField
	protected ScrollPanel scrollPanel;
	@UiField
	protected RotatableImage tempImage;

	private static final Binder BINDER = GWT.create(Binder.class);

	public DocumentTree() {
		super();
		initWidget(BINDER.createAndBindUi(this));
		tempImage.setVisible(false);
	}

	public RotatableImage getTempImage() {
		return tempImage;
	}

	private boolean setDocumentSelected(TreeItem selectedItem) {
		TreeItem item = selectedItem;
		boolean isAlreadySelected = false;
		if (item == null) {
			item = docTree.getItem(0);
		}
		if (selectedDocItem != null && selectedDocItem.getTitle().equals(item.getTitle())) {
			if (!item.getState()) {
				selectedDocItem.setState(true);
				selectedDocItem.setSelected(true);
			} else {
				isAlreadySelected = true;
			}
			DocumentTreeItem documentTreeItem = getdocTreeItemByTitle(item.getTitle());

			documentTreeItem.icon.setStyleName(ReviewValidateConstants.NO_ERROR_ICON);
			if (presenter.batchDTO.isErrorContained(documentTreeItem.document)) {
				documentTreeItem.icon.setStyleName(ReviewValidateConstants.ERROR_ICON);
			}
		} else {
			if (selectedDocItem != null) {
				selectedDocItem.setState(false);
				selectedDocItem.setSelected(false);
				DocumentTreeItem previousDocumentTreeItem = getdocTreeItemByTitle(selectedDocItem.getTitle());
				previousDocumentTreeItem.treeItem.removeStyleName(ReviewValidateConstants.DOCUMENT_STYLE);

			}

			selectedDocItem = item;
			selectedDocItem.setState(true);
			selectedDocItem.setSelected(true);
			DocumentTreeItem treeItem = getdocTreeItemByTitle(selectedDocItem.getTitle());
			presenter.document = treeItem.document;
			selectedDocItem.addStyleName(ReviewValidateConstants.DOCUMENT_STYLE);
			if (presenter.batchDTO.isErrorContained(treeItem.document)) {
				treeItem.icon.setStyleName(ReviewValidateConstants.ERROR_ICON);
			}
			// treeItem.docTitleLabel.setStyleName("highlight_documentTitle");

			// setting the scroll
			int count = 0;
			for (Document docType : presenter.batchDTO.getBatch().getDocuments().getDocument()) {
				if (docType.getIdentifier().equals(presenter.document.getIdentifier())) {
					count++;
					break;
				}
				count++;
			}
		}
		return isAlreadySelected;
	}

	private native void scrollIntoView(Element element) /*-{
						element.scrollIntoView(true);
						}-*/;

	public void setPageSelected(final RotatableImage image, boolean fireEvent) {
		setPageSelected(image, null, null, fireEvent, false);
	}

	private void setPageSelected(final RotatableImage image, Field field, List<Coordinates> coordinatesList, boolean fireEvent,
			boolean removeOverlay) {
		if (image == null) {
			DocumentTree.this.fireEvent(new ThumbnailSelectionEvent(null));
			return;
		}
		if (selectedImage != null) {
			selectedImage.removeStyleName(ReviewValidateConstants.THUMBNAIL_HIGHLIGHTED);
			selectedImage.setStyleName(ReviewValidateConstants.THUMBNAIL_DEFAULT);
		}
		selectedImage = image;
		selectedImage.setStyleName(ReviewValidateConstants.THUMBNAIL_HIGHLIGHTED);

		ThumbnailSelectionEvent thumbnailSelectionEvent = new ThumbnailSelectionEvent(pageImageMap.get(image.getTitle()).page,
				coordinatesList, removeOverlay);
		if (field != null) {
			thumbnailSelectionEvent = new ThumbnailSelectionEvent(pageImageMap.get(image.getTitle()).page, field, coordinatesList,
					removeOverlay);
		}
		if (fireEvent) {
			DocumentTree.this.fireEvent(thumbnailSelectionEvent);
		}
	}

	private void createTree(boolean fireEvent) {
		clearView();
		List<Document> docBeans = presenter.batchDTO.getBatch().getDocuments().getDocument();

		int counter = 0;
		TreeItem item = null;
		boolean isThumbNailLoaded = false;
		boolean isFirstThumbNail = false;
		for (final Document docBean : docBeans) {

			VerticalPanel docTitleVerticalPanel = new VerticalPanel();
			HorizontalPanel docTitlePanel = new HorizontalPanel();
			docTitlePanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
			docTitleVerticalPanel.add(docTitlePanel);
			Label docTitleLabel = new Label();
			Label docType = new Label();
			docTitlePanel.add(docTitleLabel);

			Label icon = new Label();
			icon.setStyleName(ReviewValidateConstants.NO_ERROR_ICON);

			docTitlePanel.add(icon);
			docTitleVerticalPanel.add(docType);
			final TreeItem docItem = docTree.addItem(docTitleVerticalPanel);

			addDocDisplayItemsAndHandlers(docBean, docTitleLabel, docType, icon, docItem);

			if (presenter.batchDTO.isErrorContained(docBean)) {
				isThumbNailLoaded = true;
				icon.setStyleName(ReviewValidateConstants.ERROR_ICON);
				if (counter == 0) {
					item = docItem;
					counter++;
				}
			}

			Iterator<Page> iter = docBean.getPages().getPage().iterator();
			for (int i = 0; i < Math.ceil(docBean.getPages().getPage().size() / 2.0); i++) {
				FlexTable flexTable = new FlexTable();
				for (int j = 0; j < 2; j++) {
					if (iter.hasNext()) {
						final Page pageTypeBean = iter.next();
						final RotatableImage image = new RotatableImage();
						if (isThumbNailLoaded && !isFirstThumbNail) {
							isThumbNailLoaded = false;
							isFirstThumbNail = true;
							if (!pageTypeBean.isIsRotated()) {
								image.setUrl(presenter.batchDTO.getAbsoluteURLFor(pageTypeBean.getThumbnailFileName()), pageTypeBean
										.getDirection());
							} else {
								image.setUrl(presenter.batchDTO.getAbsoluteURLForRotatedImage(pageTypeBean.getThumbnailFileName(),
										pageTypeBean.getDirection().toString()), pageTypeBean.getDirection());
							}
						}
						image.addStyleName(ReviewValidateConstants.THUMBNAIL_DEFAULT);

						image.addClickHandler(new ClickHandler() {

							@Override
							public void onClick(ClickEvent arg0) {
								setPageSelected(image, true);
							}
						});

						VerticalPanel imagePanel = setImagesForTree(pageTypeBean, image);
						flexTable.setWidget(0, j, imagePanel);
					}
					docItem.addItem(flexTable);

				}
			}

		}

		if (fireEvent) {
			setDocumentSelected(item);
			if (selectedDocItem != null) {
				selectedDocItem.setState(true);
			}
		}
	}

	/**
	 * @param docBean
	 * @param docTitleLabel
	 * @param docType
	 * @param icon
	 * @param docItem
	 */
	private void addDocDisplayItemsAndHandlers(final Document docBean, Label docTitleLabel, Label docType, Label icon,
			final TreeItem docItem) {
		String docDisplayProperty = getDocDisplayProperty(docBean, presenter.batchDTO.getDocDisplayName());
		DocumentTreeItem documentTreeItem = new DocumentTreeItem(docItem, docBean, icon, docDisplayProperty);
		addDocTreeItem(documentTreeItem);

		docItem.setTitle(documentTreeItem.documentTitle);
		docTitleLabel.setText(documentTreeItem.documentTitle);
		docType.setText(documentTreeItem.displayName);
		docTitleLabel.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				if (docItem.getState()) {
					docItem.setState(Boolean.FALSE);
				} else {
					OpenEvent.fire(docTree, docItem);
				}
			}
		});
	}

	/**
	 * @param pageTypeBean
	 * @param image
	 * @return
	 */
	private VerticalPanel setImagesForTree(final Page pageTypeBean, final RotatableImage image) {
		PageImage pageImage = new PageImage(pageTypeBean, image);
		addPageImage(pageImage);

		Label pageId = new Label(pageImage.pageTitle);
		pageId.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

		VerticalPanel imagePanel = new VerticalPanel();
		imagePanel.setWidth("70px");
		imagePanel.setHeight("82px");
		image.setWidth("66px");
		image.setHeight("66px");

		image.setTitle(pageImage.pageTitle);

		pageId.setWidth("66px");
		pageId.setHeight("12px");

		imagePanel.add(image);
		imagePanel.add(pageId);
		return imagePanel;
	}

	public String getDocDisplayProperty(Document document, int displayName) {
		String docDisplayName = "";
		if (null != document.getDocumentDisplayInfo() && !document.getDocumentDisplayInfo().isEmpty()) {
			docDisplayName = document.getDocumentDisplayInfo();
		} else {
			switch (displayName) {
				case 1: // display document type
					docDisplayName = document.getType();
					break;
				case 2: // display document size
					docDisplayName = document.getSize();
					break;

				case 3: // display document confidence
					docDisplayName = String.valueOf(document.getConfidence());
					break;
				case 4: // display document confidence threshold
					docDisplayName = String.valueOf(document.getConfidenceThreshold());
					break;

				default:

			}
		}
		return docDisplayName;
	}

	public void setView(BatchDTO batchDTO) {
		presenter.batchDTO = batchDTO;
		createTree(true);
	}

	public void refreshView(BatchDTO batchDTO) {
		clearView();
		setView(batchDTO);
	}

	public void clearView() {
		this.docTree.clear();
		this.selectedDocItem = null;
		this.selectedImage = null;

		docTreeItemMap.clear();
		pageImageMap.clear();
	}

	private void handleRefreshEvent(BatchDTO batchDTO, Document document, Page page) {
		presenter.batchDTO = batchDTO;
		presenter.document = document;
		presenter.page = page;
		createTree(false);
		if (document != null) {
			setDocumentSelected(getdocTreeItemById(document.getIdentifier()).treeItem);
			if (page != null) {
				setPageSelected(getPageImageById(page.getIdentifier()).image, true);
			}
		} else {
			createTree(true);
		}
	}

	private static class DocumentTreeItem {

		protected String documentTitle;
		protected String displayName;
		protected Document document;
		protected TreeItem treeItem;
		protected Label icon;

		// Label docTitleLabel;

		DocumentTreeItem(TreeItem treeItem, Document document, Label icon, String docDisplayName) {
			this.document = document;
			this.displayName = docDisplayName;
			this.treeItem = treeItem;
			this.icon = icon;
			this.documentTitle = createDocumentTitle(document.getIdentifier());
			// this.docTitleLabel = docTitleLabel;
		}

		public static String createDocumentTitle(String identifier) {
			return identifier;
		}

		@Override
		public int hashCode() {

			return Integer.parseInt(document.getIdentifier());
		}

		@Override
		public boolean equals(Object obj) {
			return ((DocumentTreeItem) obj).document.getIdentifier().equals(this.document.getIdentifier());
		}

	}

	private final Map<String, DocumentTreeItem> docTreeItemMap = new LinkedHashMap<String, DocumentTreeItem>();

	private DocumentTreeItem getdocTreeItemById(String identifier) {
		return docTreeItemMap.get(DocumentTreeItem.createDocumentTitle(identifier));
	}

	private DocumentTreeItem getdocTreeItemByTitle(String title) {
		return docTreeItemMap.get(title);
	}

	private void addDocTreeItem(DocumentTreeItem item) {
		docTreeItemMap.put(item.documentTitle, item);
	}

	public Document getNextDocument() {
		return getNextDocTreeItem().document;
	}

	public Document getPreviousDocument() {
		return getPreviousDocTreeItem().document;
	}

	private DocumentTreeItem getNextDocTreeItem() {
		DocumentTreeItem returnValue = null;
		Iterator<Entry<String, DocumentTreeItem>> iterator = docTreeItemMap.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, DocumentTreeItem> docTreeMapItem = iterator.next();
			if (docTreeMapItem.getKey().equalsIgnoreCase(selectedDocItem.getTitle())) {
				if (iterator.hasNext()) {
					returnValue = iterator.next().getValue();
				} else {
					Iterator<Entry<String, DocumentTreeItem>> secondIterator = docTreeItemMap.entrySet().iterator();
					if (secondIterator.hasNext()) {
						returnValue = secondIterator.next().getValue();
					}
				}
				break;
			}
		}
		return returnValue;
	}

	private DocumentTreeItem getPreviousDocTreeItem() {
		DocumentTreeItem returnValue = null;
		int index = 0;
		Iterator<Entry<String, DocumentTreeItem>> iterator = docTreeItemMap.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, DocumentTreeItem> docTreeMapItem = iterator.next();
			if (docTreeMapItem.getKey().equalsIgnoreCase(selectedDocItem.getTitle())) {
				break;
			}
			index++;
		}
		Iterator<Entry<String, DocumentTreeItem>> secondIterator = docTreeItemMap.entrySet().iterator();
		if (index == 0) {
			index = docTreeItemMap.size();
		}
		index--;
		int counter = 0;
		while (secondIterator.hasNext()) {
			Entry<String, DocumentTreeItem> docTreeMapItem = secondIterator.next();
			if (index == counter) {
				returnValue = docTreeMapItem.getValue();
				break;
			}
			counter++;
		}
		return returnValue;
	}

	private static class PageImage {

		protected String pageTitle;
		protected Page page;
		protected RotatableImage image;

		public PageImage(Page page, RotatableImage image) {
			this.page = page;
			this.image = image;
			this.pageTitle = this.page.getIdentifier();
		}

		public RotatableImage getImage() {
			return image;
		}

		@Override
		public int hashCode() {
			return Integer.parseInt(page.getIdentifier());
		}

		@Override
		public boolean equals(Object obj) {
			return ((PageImage) obj).page.getIdentifier().equals(this.page.getIdentifier());
		}
	}

	private final Map<String, PageImage> pageImageMap = new LinkedHashMap<String, PageImage>();

	private PageImage getPageImageById(String identifier) {
		return pageImageMap.get(identifier);
	}

	private PageImage getPageImageByTitle(String title) {
		return pageImageMap.get(title);
	}

	private void addPageImage(PageImage pageImage) {
		pageImageMap.put(pageImage.pageTitle, pageImage);
	}

	private int getCurrentPageIndex() {
		int index = 0;
		List<Page> pageList = presenter.document.getPages().getPage();
		for (Page page : pageList) {
			if (page.getIdentifier().equalsIgnoreCase(presenter.page.getIdentifier())) {
				break;
			}
			index++;
		}
		return index;
	}

	private PageImage getNextPageImage() {
		List<Page> pageList = presenter.document.getPages().getPage();
		int currIndex = getCurrentPageIndex();
		int nextIndex = currIndex + 1;
		if (nextIndex == pageList.size()) {
			nextIndex = 0;
		}
		String nextPageTitle = presenter.document.getPages().getPage().get(nextIndex).getIdentifier();
		return getPageImageByTitle(nextPageTitle);
	}

	private PageImage getPreviousPageImage() {
		List<Page> pageList = presenter.document.getPages().getPage();
		int currIndex = getCurrentPageIndex();
		int prevIndex = currIndex - 1;
		if (prevIndex < 0) {
			prevIndex = pageList.size() - 1;
		}
		String nextPageTitle = presenter.document.getPages().getPage().get(prevIndex).getIdentifier();
		return getPageImageByTitle(nextPageTitle);
	}

	@Override
	public void initializeWidget() {
		createTree(true);
		// docTree.getItem(0).setState(true, true);
	}

	@Override
	public void injectEvents(HandlerManager eventBus) {

		addDocTreeOpenHandler();

		addTreeRefreshHandler(eventBus);

		addValidationFieldChange(eventBus);

		addIconRefreshEvent(eventBus);
		addPageChangeHandler(eventBus);

		addDocTypeChangeHandler(eventBus);

		addDocumentRefreshHandler(eventBus);

		addRVKeyDownEvent(eventBus);

		addRVKeyUpEvent(eventBus);
	}

	/**
	 * @param eventBus
	 */
	private void addRVKeyUpEvent(HandlerManager eventBus) {
		eventBus.addHandler(RVKeyUpEvent.type, new RVKeyUpEventHandler() {

			@Override
			public void onKeyUp(RVKeyUpEvent event) {
				KeyUpEvent keyEvent = event.getEvent();
				if (keyEvent.isControlKeyDown()) {
					int keyCode = keyEvent.getNativeEvent().getKeyCode();
					switch (keyCode) {
						case 'n':
						case 'N':
							keyNUpEvent(keyEvent);
							break;
						case 'p':
						case 'P':
							keyPUpEvent(keyEvent);
							break;
						default:
							break;
					}
				}
			}

		});
	}

	/**
	 * @param keyEvent
	 */
	private void keyPUpEvent(KeyUpEvent keyEvent) {
		if (!presenter.isTableView()) {
			if (!keyEvent.isShiftKeyDown()) {
				keyEvent.getNativeEvent().preventDefault();
				openNextPage();
			} else {
				keyEvent.getNativeEvent().preventDefault();
				openPreviousPage();
			}
		}
	}

	/**
	 * @param keyEvent
	 */
	private void keyNUpEvent(KeyUpEvent keyEvent) {
		if (!presenter.isTableView()) {

			if (!keyEvent.isShiftKeyDown()) {
				keyNShiftDownHandler(keyEvent);
			} else {
				keyNShiftUpHandler(keyEvent);
			}
		}
	}

	/**
	 * @param keyEvent
	 */
	private void keyNShiftUpHandler(KeyUpEvent keyEvent) {
		keyEvent.getNativeEvent().preventDefault();
		if (presenter.batchDTO.getBatchInstanceStatus().equals(BatchInstanceStatus.READY_FOR_VALIDATION)
				&& "ON".equalsIgnoreCase(presenter.batchDTO.getIsValidationScriptEnabled())) {
			Batch batch = presenter.batchDTO.getBatch();
			ScreenMaskUtility.maskScreen("Executing Script.....");
			presenter.rpcService.executeScript(batch, presenter.document, new AsyncCallback<BatchDTO>() {

				@Override
				public void onSuccess(final BatchDTO batchDTO) {
					List<Document> documents = batchDTO.getBatch().getDocuments().getDocument();
					for (Document doc : documents) {
						final DocumentTreeItem docTreeItem = docTreeItemMap.get(selectedDocItem.getTitle());
						if (docTreeItem != null && doc.getIdentifier().equalsIgnoreCase(docTreeItem.document.getIdentifier())) {
							if (!doc.isValid()) {
								docTreeItem.document = doc;
								DocumentTree.this.eventBus.fireEvent(new TreeRefreshEvent(batchDTO, docTreeItem.document,
										docTreeItem.document.getPages().getPage().get(0)));
							} else {
								openPreviousDocument();
							}
							break;
						}
					}
					ScreenMaskUtility.unmaskScreen();
				}

				@Override
				public void onFailure(Throwable paramThrowable) {
					if (!presenter.displayErrorMessage(paramThrowable)) {
						ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
								ReviewValidateMessages.SCRIPT_EXECUTION_ERROR));
					}
					ScreenMaskUtility.unmaskScreen();

				}
			});
		} else {
			openPreviousDocument();
		}
	}

	/**
	 * @param keyEvent
	 */
	private void keyNShiftDownHandler(KeyUpEvent keyEvent) {
		final DocumentTreeItem nextDocTreeItem = getNextDocTreeItem();
		keyEvent.getNativeEvent().preventDefault();
		if (presenter.batchDTO.getBatchInstanceStatus().equals(BatchInstanceStatus.READY_FOR_VALIDATION)
				&& "ON".equalsIgnoreCase(presenter.batchDTO.getIsValidationScriptEnabled())) {
			Batch batch = presenter.batchDTO.getBatch();
			ScreenMaskUtility.maskScreen("Executing Script.....");
			presenter.rpcService.executeScript(batch, presenter.document, new AsyncCallback<BatchDTO>() {

				@Override
				public void onSuccess(final BatchDTO batchDTO) {
					List<Document> documents = batchDTO.getBatch().getDocuments().getDocument();
					for (Document doc : documents) {
						final DocumentTreeItem docTreeItem = docTreeItemMap.get(selectedDocItem.getTitle());
						if (docTreeItem != null && doc.getIdentifier().equalsIgnoreCase(docTreeItem.document.getIdentifier())) {
							if (!doc.isValid()) {
								docTreeItem.document = doc;
								DocumentTree.this.eventBus.fireEvent(new TreeRefreshEvent(batchDTO, docTreeItem.document,
										docTreeItem.document.getPages().getPage().get(0)));
							} else {
								openDocument(nextDocTreeItem);
							}
							break;
						}
					}
					ScreenMaskUtility.unmaskScreen();
				}

				@Override
				public void onFailure(Throwable paramThrowable) {
					if (!presenter.displayErrorMessage(paramThrowable)) {
						ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
								ReviewValidateMessages.SCRIPT_EXECUTION_ERROR));
					}
					ScreenMaskUtility.unmaskScreen();

				}
			});
		} else {
			openDocument(nextDocTreeItem);
		}
	}
	/**
	 * @param eventBus
	 */
	private void addRVKeyDownEvent(HandlerManager eventBus) {
		eventBus.addHandler(RVKeyDownEvent.type, new RVKeyDownEventHandler() {

			@Override
			public void onKeyDown(RVKeyDownEvent event) {
				if (event.getEvent().isControlKeyDown()) {
					switch (event.getEvent().getNativeEvent().getKeyCode()) {
						case 'n':
						case 'N':
							event.getEvent().getNativeEvent().preventDefault();
							break;
						case 'p':
						case 'P':
							event.getEvent().getNativeEvent().preventDefault();
							break;
						default:
							break;
					}
				}

			}
		});
	}

	/**
	 * @param eventBus
	 */
	private void addDocumentRefreshHandler(HandlerManager eventBus) {
		eventBus.addHandler(DocumentRefreshEvent.type, new DocumentRefreshHandler() {

			@Override
			public void onUpdate(DocumentRefreshEvent event) {
				setDocumentSelected(getdocTreeItemById(event.getDocument().getIdentifier()).treeItem);
				if (!presenter.batchDTO.isErrorContained(presenter.document)) {

					if (presenter.batchDTO.getBatchInstanceStatus().equals(BatchInstanceStatus.READY_FOR_VALIDATION)) {
						for (Document doc : presenter.batchDTO.getBatch().getDocuments().getDocument()) {
							if (!doc.isValid()) {
								presenter.document = presenter.batchDTO.getNextDocumentTo(presenter.document, true);
								break;
							}
						}
					}
					if (presenter.batchDTO.getBatchInstanceStatus().equals(BatchInstanceStatus.READY_FOR_REVIEW)) {
						for (Document doc : presenter.batchDTO.getBatch().getDocuments().getDocument()) {
							if (!doc.isReviewed()) {
								presenter.document = presenter.batchDTO.getNextDocumentTo(presenter.document, true);
								break;
							}
						}
					}
					if (presenter.document != null) {
						setDocumentSelected(getdocTreeItemById(presenter.document.getIdentifier()).treeItem);
					}
				}
			}
		});
	}

	/**
	 * @param eventBus
	 */
	private void addDocTypeChangeHandler(HandlerManager eventBus) {
		eventBus.addHandler(DocTypeChangeEvent.type, new DocTypeChangeEventHandler() {

			@Override
			public void onDocumentTypeChange(DocTypeChangeEvent event) {
				handleRefreshEvent(event.getBatchDTO(), event.getDocumentType(), event.getDocumentType().getPages().getPage().get(0));
			}
		});
	}

	/**
	 * @param eventBus
	 */
	private void addPageChangeHandler(HandlerManager eventBus) {
		eventBus.addHandler(PageChangeEvent.type, new PageChangeEventHandler() {

			@Override
			public void onPageChange(PageChangeEvent event) {
				PageImage pageImage = getPageImageById(event.getPage().getIdentifier());
				if (!event.getPage().isIsRotated()) {
					pageImage.image.setUrl(presenter.batchDTO.getAbsoluteURLFor(event.getPage().getThumbnailFileName()), event
							.getPage().getDirection());
				} else {
					pageImage.image.setUrl(presenter.batchDTO.getAbsoluteURLForRotatedImage(event.getPage().getThumbnailFileName(),
							event.getPage().getDirection().toString()), event.getPage().getDirection());
				}
			}
		});
	}

	/**
	 * @param eventBus
	 */
	private void addIconRefreshEvent(HandlerManager eventBus) {
		eventBus.addHandler(IconRefreshEvent.type, new IconRefreshEventHandler() {

			@Override
			public void refresh(IconRefreshEvent iconRefreshEvent) {
				TreeItem item = getdocTreeItemById(iconRefreshEvent.getDocument().getIdentifier()).treeItem;
				selectedDocItem = item;
				selectedDocItem.setState(false);
				selectedDocItem.setSelected(false);
				DocumentTreeItem treeItem = getdocTreeItemByTitle(selectedDocItem.getTitle());
				selectedDocItem.addStyleName(ReviewValidateConstants.DOCUMENT_STYLE);
				treeItem.icon.setStyleName(ReviewValidateConstants.NO_ERROR_ICON);
				if (presenter.batchDTO.isErrorContained(presenter.document)) {
					treeItem.icon.setStyleName(ReviewValidateConstants.ERROR_ICON);
				}
			}
		});
	}

	/**
	 * @param eventBus
	 */
	private void addValidationFieldChange(HandlerManager eventBus) {
		eventBus.addHandler(ValidationFieldChangeEvent.type, new ValidationFieldChangeEventHandler() {

			@Override
			public void onFieldChange(ValidationFieldChangeEvent event) {
				String page = event.getField().getPage();
				if (page != null && getPageImageById(page) != null) {
					setPageSelected(getPageImageById(page).image, event.getField(), event.getCoordinatesList(), true, event
							.isRemoveOverlay());
				} else {
					setPageSelected(null, event.getField(), event.getCoordinatesList(), true, event.isRemoveOverlay());
				}
			}

			@Override
			public void onValueChange(ValidationFieldChangeEvent event) {
				/*
				 * Add value change handling code
				 */
			}
		});
	}

	/**
	 * @param eventBus
	 */
	private void addTreeRefreshHandler(HandlerManager eventBus) {
		eventBus.addHandler(TreeRefreshEvent.type, new TreeRefreshEventHandler() {

			@Override
			public void refresh(TreeRefreshEvent event) {
				handleRefreshEvent(event.getBatchDTO(), event.getDocument(), event.getPage());
			}
		});
	}

	/**
	 * 
	 */
	private void addDocTreeOpenHandler() {
		docTree.addOpenHandler(new OpenHandler<TreeItem>() {

			@Override
			public void onOpen(OpenEvent<TreeItem> arg0) {
				TreeItem target = arg0.getTarget();
				String title = target.getTitle();
				String text = target.getText();
				presenter.document = getdocTreeItemByTitle(arg0.getTarget().getTitle()).document;
				boolean isAlreadySelected = setDocumentSelected(arg0.getTarget());

				List<Page> pageList = presenter.document.getPages().getPage();
				for (Page page : pageList) {
					String identifier = page.getIdentifier();
					PageImage pageImage = pageImageMap.get(identifier);
					RotatableImage image = pageImage.getImage();
					if (!page.isIsRotated()) {
						image.setUrl(presenter.batchDTO.getAbsoluteURLFor(page.getThumbnailFileName()), page.getDirection());
					} else {
						image.setUrl(presenter.batchDTO.getAbsoluteURLForRotatedImage(page.getThumbnailFileName(), page.getDirection()
								.toString()), page.getDirection());
					}
				}

				presenter.page = presenter.document.getPages().getPage().get(0);

				setPageSelected(getPageImageById(presenter.page.getIdentifier()).image, false);
				int itemCount = presenter.getView().getRvPanel().getReviewPanel().documentTypes.getItemCount();
				if (target.getChildCount() > 0 && !(title.isEmpty() && text.isEmpty())) {
					scrollIntoView(target.getElement());
				}
				if (!isAlreadySelected || itemCount == 0) {
					DocumentTree.this.fireEvent(new DocExpandEvent(presenter.document));
				} else {
					DocumentTree.this.fireEvent(new DocExpandEvent(presenter.document, false));
				}
			}
		});
	}

	private void openDocument(DocumentTreeItem docTreeItem) {
		openDocument(docTreeItem, docTreeItem.document.getPages().getPage().get(0));
	}

	private void openDocument(DocumentTreeItem docTreeItem, Page page) {
		presenter.document = docTreeItem.document;
		setDocumentSelected(docTreeItem.treeItem);

		presenter.page = page;
		setPageSelected(getPageImageById(presenter.page.getIdentifier()).image, false);

		DocumentTree.this.fireEvent(new DocExpandEvent(presenter.document));
	}

	public void openDocument(Document document, Page page) {
		DocumentTreeItem docTreeItem = getdocTreeItemById(document.getIdentifier());
		openDocument(docTreeItem, page);
	}

	private void openPreviousDocument() {
		DocumentTreeItem docTreeItem = getPreviousDocTreeItem();
		presenter.document = docTreeItem.document;
		setDocumentSelected(docTreeItem.treeItem);

		presenter.page = presenter.document.getPages().getPage().get(0);
		setPageSelected(getPageImageById(presenter.page.getIdentifier()).image, false);
		DocumentTree.this.fireEvent(new DocExpandEvent(presenter.document));
	}

	private void openNextPage() {
		PageImage pageImage = getNextPageImage();
		presenter.page = pageImage.page;
		setPageSelected(pageImage.image, true);
	}

	private void openPreviousPage() {
		PageImage pageImage = getPreviousPageImage();
		presenter.page = pageImage.page;
		setPageSelected(pageImage.image, true);
	}
}
