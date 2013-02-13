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

package com.ephesoft.dcma.gwt.admin.bm.client.presenter.module;

import com.ephesoft.dcma.gwt.admin.bm.client.BatchClassManagementController;
import com.ephesoft.dcma.gwt.admin.bm.client.event.batch.ModuleEvent;
import com.ephesoft.dcma.gwt.admin.bm.client.event.batch.PluginEvent;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.AbstractBatchClassPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.view.module.ModuleView;
import com.ephesoft.dcma.gwt.core.client.util.Action;
import com.ephesoft.dcma.gwt.core.shared.ModuleDTO;
import com.ephesoft.dcma.gwt.core.shared.PluginDetailsDTO;
import com.google.gwt.event.shared.HandlerManager;

/**
 * The presenter for view that shows the module view details.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.gwt.admin.bm.client.presenter.AbstractBatchClassPresenter
 */
public class ModuleViewPresenter extends AbstractBatchClassPresenter<ModuleView> {

	/**
	 * moduleDetailPresenter ModuleDetailPresenter.
	 */
	private final ModuleDetailPresenter moduleDetailPresenter;

	/**
	 * editModulePresenter EditModulePresenter.
	 */
	private final EditModulePresenter editModulePresenter;

	/**
	 * Constructor.
	 * 
	 * @param controller BatchClassManagementController
	 * @param view ModuleView
	 */
	public ModuleViewPresenter(final BatchClassManagementController controller, ModuleView view) {
		super(controller, view);

		this.moduleDetailPresenter = new ModuleDetailPresenter(controller, view.getModuleDetailView());
		this.editModulePresenter = new EditModulePresenter(controller, view.getEditModuleView());
	}

	/**
	 * To perform binding in case of detail view clicked.
	 */
	public void onDetailViewClicked() {
		moduleDetailPresenter.bind();
	}

	/**
	 * To get Module Detail Presenter.
	 * 
	 * @return ModuleDetailPresenter
	 */
	public ModuleDetailPresenter getModuleDetailPresenter() {
		return moduleDetailPresenter;
	}

	/**
	 * To fire event on module selection.
	 * 
	 * @param module ModuleDTO
	 */
	public void onModuleSelection(ModuleDTO module) {
		controller.getEventBus().fireEvent(new ModuleEvent(Action.SELECT, module));
	}

	/**
	 * To fire event on plugin selection.
	 * 
	 * @param plugin PluginDetailsDTO
	 */
	public void onPluginSelection(PluginDetailsDTO plugin) {
		controller.getEventBus().fireEvent(new PluginEvent(Action.SELECT, plugin));
	}

	/**
	 * Processing to be done on load of this presenter.
	 */
	@Override
	public void bind() {
		this.moduleDetailPresenter.bind();
		if (controller.getSelectedModule() != null) {
			view.createPluginList(controller.getSelectedModule().getBatchClassPlugins());
		}
		this.editModulePresenter.bind();
	}

	/**
	 * To handle events.
	 * 
	 * @param eventBus HandlerManager
	 */
	@Override
	public void injectEvents(HandlerManager eventBus) {
		// TODO Auto-generated method stub

	}

	/**
	 * To show Module Detail View.
	 */
	public void showModuleDetailView() {
		view.getEditModuleViewPanel().setVisible(Boolean.FALSE);
		view.getModuleDetailViewPanel().setVisible(Boolean.TRUE);
	}

	/**
	 * To do binding in case of edit module button click.
	 */
	public void onEditModuleButtonClick() {
		view.getModuleDetailViewPanel().setVisible(Boolean.FALSE);
		controller.getBatchClass().setDirty(Boolean.TRUE);
		view.getEditModuleViewPanel().setVisible(Boolean.TRUE);
		editModulePresenter.bind();
	}

	/**
	 * To get edit module presenter.
	 * 
	 * @return EditModulePresenter
	 */
	public EditModulePresenter getEditModulePresenter() {
		return editModulePresenter;
	}

	/**
	 * To set add button enable.
	 * 
	 * @param enable boolean
	 */
	public void setAddButtonEnable(boolean enable) {
	
	}

}
