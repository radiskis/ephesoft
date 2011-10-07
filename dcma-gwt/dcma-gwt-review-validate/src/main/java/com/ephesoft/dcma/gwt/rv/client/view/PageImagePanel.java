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

import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.batch.schema.Page;
import com.ephesoft.dcma.gwt.core.client.ui.RotatableImage;
import com.ephesoft.dcma.gwt.rv.client.event.DocExpandEvent;
import com.ephesoft.dcma.gwt.rv.client.event.DocExpandEventHandler;
import com.ephesoft.dcma.gwt.rv.client.event.PageChangeEvent;
import com.ephesoft.dcma.gwt.rv.client.event.PageChangeEventHandler;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class PageImagePanel extends RVBasePanel {

	interface Binder extends UiBinder<DockLayoutPanel, PageImagePanel> {
	}

	@UiField
	RotatableImage currPageImage;

	@UiField
	RotatableImage previousPageImage;

	@UiField
	RotatableImage nextPageImage;

	@UiField
	RotatableImage previousPageImageButton;

	@UiField
	RotatableImage nextPageImageButton;

	Page nextPage;

	Page previousPage;

	Document previousDocument;

	Document nextDocument;

	@UiField
	HorizontalPanel imagesPanel;

	private static final Binder binder = GWT.create(Binder.class);

	private static final String CURSOR = "cursor_style";
	private static final String THUMBNAIL = "thumbnail";

	public PageImagePanel() {
		initWidget(binder.createAndBindUi(this));
		setImageStyle(previousPageImage, THUMBNAIL);
		setImageStyle(nextPageImage, THUMBNAIL);
		currPageImage.setUrl("images/Current.jpg");
		setButtonStyle(currPageImage, CURSOR);
		previousPageImageButton.setUrl("images/Prev_Button.jpg");
		setButtonStyle(previousPageImageButton, CURSOR);
		nextPageImageButton.setUrl("images/Next_Button.jpg");
		setButtonStyle(nextPageImageButton, CURSOR);
		imagesPanel.setStyleName("document-style");

		nextPageImage.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				showNextPage();
			}

		});
		nextPageImageButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent paramClickEvent) {
				showNextPage();

			}
		});
		previousPageImage.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				showPrevPage();
			}
		});
		previousPageImageButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent paramClickEvent) {
				showPrevPage();

			}
		});
	}

	private void showPrevPage() {
		if (previousDocument != null && previousPage != null) {
			presenter.getView().getDocTree().openDocument(previousDocument, previousPage);
		}
	}

	private void showNextPage() {
		if (nextDocument != null && nextPage != null) {
			presenter.getView().getDocTree().openDocument(nextDocument, nextPage);
		}
	}

	private void setImageStyle(RotatableImage image, String style) {
		image.setStyleName(style);
		image.setWidth("90px");
		image.setHeight("90px");

	}

	private void setButtonStyle(RotatableImage image, String style) {
		image.setStyleName(style);
		image.setWidth("20px");
		image.setHeight("20px");

	}

	@Override
	public void injectEvents(HandlerManager eventBus) {
		eventBus.addHandler(DocExpandEvent.TYPE, new DocExpandEventHandler() {

			@Override
			public void onExpand(DocExpandEvent event) {
				if (event.getDocument() != null) {
					previousDocument = presenter.getView().getDocTree().getPreviousDocument();
					previousPage = previousDocument.getPages().getPage().get(previousDocument.getPages().getPage().size() - 1);

					nextDocument = presenter.getView().getDocTree().getNextDocument();
					nextPage = nextDocument.getPages().getPage().get(0);

					if (nextPage != null) {
						if (!nextPage.isIsRotated()) {
							nextPageImage.setUrl(presenter.batchDTO.getAbsoluteURLFor(nextPage.getThumbnailFileName()), nextPage
									.getDirection());
						} else {
							nextPageImage.setUrl(presenter.batchDTO.getAbsoluteURLForRotatedImage(nextPage.getThumbnailFileName(),
									nextPage.getDirection().toString()), nextPage.getDirection());
						}

					} else {
						nextPageImage.setVisible(false);
					}
					if (previousPage != null) {
						if (!previousPage.isIsRotated()) {
							previousPageImage.setUrl(presenter.batchDTO.getAbsoluteURLFor(previousPage.getThumbnailFileName()),
									previousPage.getDirection());
						} else {
							previousPageImage.setUrl(presenter.batchDTO.getAbsoluteURLForRotatedImage(previousPage
									.getThumbnailFileName(), previousPage.getDirection().toString()), previousPage.getDirection());
						}

					} else {
						previousPageImage.setVisible(false);
					}
				}

			}
		});

		eventBus.addHandler(PageChangeEvent.TYPE, new PageChangeEventHandler() {

			@Override
			public void onPageChange(PageChangeEvent event) {
				if (event.getDocument() != null) {
					previousDocument = presenter.getView().getDocTree().getPreviousDocument();
					previousPage = previousDocument.getPages().getPage().get(previousDocument.getPages().getPage().size() - 1);

					nextDocument = presenter.getView().getDocTree().getNextDocument();
					nextPage = nextDocument.getPages().getPage().get(0);

					if (nextPage != null) {
						if (!nextPage.isIsRotated()) {
							nextPageImage.setUrl(presenter.batchDTO.getAbsoluteURLFor(nextPage.getThumbnailFileName()), nextPage
									.getDirection());
						} else {
							nextPageImage.setUrl(presenter.batchDTO.getAbsoluteURLForRotatedImage(nextPage.getThumbnailFileName(),
									nextPage.getDirection().toString()), nextPage.getDirection());
						}

					} else {
						nextPageImage.setVisible(false);
					}
					if (previousPage != null) {
						if (!previousPage.isIsRotated()) {
							previousPageImage.setUrl(presenter.batchDTO.getAbsoluteURLFor(previousPage.getThumbnailFileName()),
									previousPage.getDirection());
						} else {
							previousPageImage.setUrl(presenter.batchDTO.getAbsoluteURLForRotatedImage(previousPage
									.getThumbnailFileName(), previousPage.getDirection().toString()), previousPage.getDirection());
						}

					} else {
						previousPageImage.setVisible(false);
					}
				}

			}
		});
	}

	@Override
	public void initializeWidget() {
		// TODO Auto-generated method stub

	}

}
