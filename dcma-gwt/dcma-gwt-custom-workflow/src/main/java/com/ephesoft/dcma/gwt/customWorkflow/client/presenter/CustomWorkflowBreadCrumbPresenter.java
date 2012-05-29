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

package com.ephesoft.dcma.gwt.customWorkflow.client.presenter;

import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.customWorkflow.client.CustomWorkflowController;
import com.ephesoft.dcma.gwt.customWorkflow.client.ViewType;
import com.ephesoft.dcma.gwt.customWorkflow.client.i18n.CustomWorkflowMessages;
import com.ephesoft.dcma.gwt.customWorkflow.client.view.CustomWorkflowBreadCrumbView;
import com.google.gwt.event.shared.HandlerManager;

public class CustomWorkflowBreadCrumbPresenter extends AbstractCustomWorkflowPresenter<CustomWorkflowBreadCrumbView> {

	public CustomWorkflowBreadCrumbPresenter(CustomWorkflowController controller, CustomWorkflowBreadCrumbView view) {
		super(controller, view);
		initializeBreadCrumb();
	}

	@Override
	public void bind() {
		// Processing to be done when this presenter loads.
	}

	public void createBreadCrumbForDependenciesList() {
		view.create(new CustomWorkflowBreadCrumbView.BreadCrumbView(ViewType.ENTRY_VIEW, LocaleDictionary.get().getMessageValue(
				CustomWorkflowMessages.PLUGINS_LIST), ViewType.ENTRY_VIEW.getValue()),
				new CustomWorkflowBreadCrumbView.BreadCrumbView(ViewType.DEPENDENCIES_VIEW, LocaleDictionary.get().getMessageValue(
						CustomWorkflowMessages.DEPENDENCIES), ViewType.DEPENDENCIES_VIEW.getValue()));
		view.createBreadCrumbString();
	}

	public void createBreadCrumbForDependenciesAddView(String viewName) {
		view.create(new CustomWorkflowBreadCrumbView.BreadCrumbView(ViewType.ENTRY_VIEW, LocaleDictionary.get().getMessageValue(
				CustomWorkflowMessages.PLUGINS_LIST), ViewType.ENTRY_VIEW.getValue()),
				new CustomWorkflowBreadCrumbView.BreadCrumbView(ViewType.DEPENDENCIES_VIEW, LocaleDictionary.get().getMessageValue(
						CustomWorkflowMessages.DEPENDENCIES), ViewType.DEPENDENCIES_VIEW.getValue()),
				new CustomWorkflowBreadCrumbView.BreadCrumbView(ViewType.ADD_DEPENDENCY_VIEW, viewName, ViewType.ADD_DEPENDENCY_VIEW
						.getValue()));
		view.createBreadCrumbString();
	}

	public void createBreadCrumbForEntryView() {
		view.create(new CustomWorkflowBreadCrumbView.BreadCrumbView(ViewType.ENTRY_VIEW, LocaleDictionary.get().getMessageValue(
				CustomWorkflowMessages.PLUGINS_LIST), ViewType.ENTRY_VIEW.getValue()));
		view.createBreadCrumbString();
	}

	public void initializeBreadCrumb() {

		createBreadCrumbForEntryView();
	}

	@Override
	public void injectEvents(HandlerManager eventBus) {

	}

	public void setbackButtonVisibility(boolean visibility) {
		view.getPreviousButton().setEnabled(visibility);
	}

}
