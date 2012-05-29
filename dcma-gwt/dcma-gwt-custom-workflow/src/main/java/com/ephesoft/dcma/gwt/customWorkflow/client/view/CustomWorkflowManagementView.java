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

package com.ephesoft.dcma.gwt.customWorkflow.client.view;

import com.ephesoft.dcma.gwt.core.client.View;
import com.ephesoft.dcma.gwt.customWorkflow.client.presenter.CustomWorkflowManagementPresenter;
import com.ephesoft.dcma.gwt.customWorkflow.client.view.dependencies.DependencyManagementView;
import com.ephesoft.dcma.gwt.customWorkflow.client.view.dependencies.DependencyView;
import com.ephesoft.dcma.gwt.customWorkflow.client.view.dependencies.EditDependencyView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.LayoutPanel;

public class CustomWorkflowManagementView extends View<CustomWorkflowManagementPresenter> {

	@UiField
	DockLayoutPanel customWorkflowLayoutPanel;

	@UiField
	CustomWorkflowEntryView customWorkflowEntryView;

	@UiField
	DependencyManagementView dependencyManagementView;

	@UiField
	DependencyView dependencyView;

	@UiField
	EditDependencyView editDependencyView;

	@UiField
	LayoutPanel layoutPanel;

	/**
	 * @return the customWorkflowEntryView
	 */
	public CustomWorkflowEntryView getCustomWorkflowEntryView() {
		return customWorkflowEntryView;
	}

	interface Binder extends UiBinder<DockLayoutPanel, CustomWorkflowManagementView> {
	}

	private static final Binder BINDER = GWT.create(Binder.class);

	public CustomWorkflowManagementView() {

		initWidget(BINDER.createAndBindUi(this));
		// workflowBreadCrumbView.addStyleName("buttonsPanelLayout");
		init();
	}

	private void init() {
		showEntryView();
	}


	public void showEntryView() {
		layoutPanel.clear();
		getCustomWorkflowEntryView().setVisible(true);
		layoutPanel.add(getCustomWorkflowEntryView());
	}

	/**
	 * @return the dependencyView
	 */
	public DependencyView getDependencyView() {
		return dependencyView;
	}

	public void showDependencyManagementView() {
		layoutPanel.clear();
		dependencyManagementView.setVisible(true);
		dependencyManagementView.getLayoutPanel().clear();
		dependencyManagementView.getLayoutPanel().add(dependencyManagementView.getDependencyView());
		layoutPanel.add(dependencyManagementView);

	}

	/**
	 * @param customWorkflowEntryView the customWorkflowEntryView to set
	 */
	public void setCustomWorkflowEntryView(CustomWorkflowEntryView customWorkflowEntryView) {
		this.customWorkflowEntryView = customWorkflowEntryView;
	}

	/**
	 * @param dependencyView the dependencyView to set
	 */
	public void setDependencyView(DependencyView dependencyView) {
		this.dependencyView = dependencyView;
	}

	/**
	 * @return the dependencyManagementView
	 */
	public DependencyManagementView getDependencyManagementView() {
		return dependencyManagementView;
	}

}
