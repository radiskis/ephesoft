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

import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.client.view.SlidingPanel;
import com.ephesoft.dcma.gwt.rv.client.i18n.ReviewValidateConstants;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockLayoutPanel;

public class ReviewValidateView extends RVBasePanel {

	interface Binder extends UiBinder<DockLayoutPanel, ReviewValidateView> {
	}

	private static final Binder BINDER = GWT.create(Binder.class);

	@UiField
	protected TopPanel topPanel;
	@UiField
	protected ReviewValidatePanel rvPanel;
	@UiField
	protected ImageOverlayPanel imgOverlayPanel;
	@UiField
	protected DocumentTree docTree;
	@UiField
	protected DockLayoutPanel reviewValidateViewPanel;
	@UiField
	protected DockLayoutPanel tableView;
	@UiField
	protected DockLayoutPanel rVView;
	@UiField
	protected SlidingPanel slidingPanel;
	@UiField
	protected Button rvView;
	@UiField
	protected TableExtractionView tableExtractionView;

	private final DockLayoutPanel outer;

	public ReviewValidateView() {
		super();
		outer = BINDER.createAndBindUi(this);

		Window.enableScrolling(true);
		Window.setMargin("0px");

		Element topElem = outer.getWidgetContainerElement(topPanel);
		topElem.getStyle().setZIndex(2);
		topElem.getStyle().setOverflow(Overflow.VISIBLE);
		reviewValidateViewPanel.addStyleName("left10");
		slidingPanel.setWidget(rVView);
		rvView.setText(LocaleDictionary.get().getConstantValue(ReviewValidateConstants.MSG_BACK));
		rvView.addStyleName("margin");
	}

	public void initializeWidget() {
		rvPanel.setPresenter(this.presenter);
		rvPanel.initializeWidget();
		rvPanel.addStyleName("review_validate_panel");

		topPanel.setPresenter(presenter);
		topPanel.initializeWidget();
		topPanel.getTopHorizontalPanel().addStyleName("top_Panel");

		imgOverlayPanel.setPresenter(this.presenter);
		imgOverlayPanel.initializeWidget();
		imgOverlayPanel.addStyleName("image_overlay_panel");

		tableExtractionView.setPresenter(this.presenter);
		tableExtractionView.initializeWidget();

		docTree.setPresenter(this.presenter);
		docTree.initializeWidget();
		docTree.addStyleName("doc_tree");

		slidingPanel.setEventBus(eventBus);

		this.presenter.setBatchListScreenTab();
	}

	public DockLayoutPanel getOuter() {
		return outer;
	}

	@Override
	public void injectEvents(HandlerManager eventBus) {
		// no need to do anything on injection of events on this view
	}

	@UiHandler("rvView")
	protected void onReviewValidateViewClicked(ClickEvent clickEvent) {
		presenter.setTableView(Boolean.FALSE);
		slidingPanel.setWidget(rVView);
	}

	public SlidingPanel getSlidingPanel() {
		return slidingPanel;
	}

	public DockLayoutPanel getTableView() {
		return tableView;
	}

	public TopPanel getTopPanel() {
		return topPanel;
	}

	public DockLayoutPanel getReviewValidateView() {
		return rVView;
	}

	public void setReviewValidateView() {
		slidingPanel.setWidget(rVView);
	}

	public ReviewValidatePanel getRvPanel() {
		return rvPanel;
	}

	public DocumentTree getDocTree() {
		return docTree;
	}

	public TableExtractionView getTableExtractionView() {
		return tableExtractionView;
	}

	public ImageOverlayPanel getImgOverlayPanel() {
		return imgOverlayPanel;
	}
}
