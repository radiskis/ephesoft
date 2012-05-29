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

package com.ephesoft.dcma.gwt.admin.bm.client.view.batch;

import java.util.Map.Entry;

import com.ephesoft.dcma.gwt.admin.bm.client.AdminConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.MessageConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.batch.SamplePatternPresenter;
import com.ephesoft.dcma.gwt.core.client.View;
import com.ephesoft.dcma.gwt.core.shared.SamplePatternDTO;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SamplePatternView extends View<SamplePatternPresenter> {

	interface Binder extends UiBinder<VerticalPanel, SamplePatternView> {
	}

	private static final Binder BINDER = GWT.create(Binder.class);

	private FlexTable samplePatternTable;

	@UiField
	protected FlowPanel patternListPanel;

	@UiField
	protected Button backButton;

	private DialogBox dialogBox;

	@UiField
	ScrollPanel scrollPanel;

	public SamplePatternView() {
		super();
		initWidget(BINDER.createAndBindUi(this));

		scrollPanel.setSize("390px", "395px");

		samplePatternTable = new FlexTable();
		samplePatternTable.setWidth("395px");
		samplePatternTable.setCellSpacing(0);
		samplePatternTable.addStyleName("border-result-table");
		backButton.setText(AdminConstants.CLOSE_BUTTON);
		patternListPanel.add(samplePatternTable);
		backButton.setFocus(true);
	}

	@UiHandler("backButton")
	public void onBackButtonClicked(ClickEvent e) {
		dialogBox.hide();
	}

	public void createSamplePatternList(SamplePatternDTO samplePatternDTO) {
		samplePatternTable.removeAllRows();
		setResultList(samplePatternDTO);
	}

	private void setResultList(SamplePatternDTO samplePatternDTO) {

		int rows = 1;
         
		if (samplePatternDTO != null && samplePatternDTO.getPatternValueMap() != null) {
			samplePatternTable.getCellFormatter().setWidth(0, 0, "20%");
			samplePatternTable.setText(0, 0, "Name");
			samplePatternTable.setText(0, 1, "Sample Regex Pattern");
			samplePatternTable.getRowFormatter().setStylePrimaryName(0, "header");
			for (Entry<String, String> entrySet : samplePatternDTO.getPatternValueMap().entrySet()) {
				samplePatternTable.setText(rows, 0, entrySet.getKey());
				samplePatternTable.setText(rows, 1, entrySet.getValue());
				rows++;

			}
		} else {
			samplePatternTable.getFlexCellFormatter().setColSpan(1, 0, 4);
			samplePatternTable.setText(0, 0, MessageConstants.MSG_NO_RESULTS_FOUND);

		}

	}

	public DialogBox getDialogBox() {
		return dialogBox;
	}

	public void setDialogBox(DialogBox dialogBox) {
		this.dialogBox = dialogBox;
	}

	public Button getBackButton() {
		return backButton;
	}

}
