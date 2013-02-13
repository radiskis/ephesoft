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

package com.ephesoft.dcma.gwt.admin.bm.client.presenter.plugin;

import com.ephesoft.dcma.gwt.admin.bm.client.BatchClassManagementController;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.PluginNameConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.AbstractBatchClassPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.view.plugin.FuzzyDBPluginView;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerManager;

/**
 * The presenter for view that shows the Fuzzy DB plugin.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.gwt.admin.bm.client.presenter.AbstractBatchClassPresenter
 */
public class FuzzyDBPluginPresenter extends AbstractBatchClassPresenter<FuzzyDBPluginView> {

	/**
	 * fuzzyDBPropertiesViewPresenter FuzzyDBPropertiesViewPresenter.
	 */
	private final FuzzyDBPropertiesViewPresenter fuzzyDBPropertiesViewPresenter;

	/**
	 * editFuzzyDBPropertiesPresenter EditFuzzyDBPropertiesPresenter.
	 */
	private final EditFuzzyDBPropertiesPresenter editFuzzyDBPropertiesPresenter;

	/**
	 * pluginDataPresenter PluginDataPresenter.
	 */
	private final PluginDataPresenter pluginDataPresenter;

	/**
	 * Constructor.
	 * 
	 * @param controller BatchClassManagementController
	 * @param view FuzzyDBPluginView
	 */
	public FuzzyDBPluginPresenter(BatchClassManagementController controller, FuzzyDBPluginView view) {
		super(controller, view);

		this.pluginDataPresenter = new PluginDataPresenter(controller, view.getPluginDataView());
		this.fuzzyDBPropertiesViewPresenter = new FuzzyDBPropertiesViewPresenter(controller, view.getFuzzyDBPropertiesView());
		this.editFuzzyDBPropertiesPresenter = new EditFuzzyDBPropertiesPresenter(controller, view.getEditFuzzyDBPropertiesView());
	}

	/**
	 * Processing to be done on load of this presenter.
	 */
	@Override
	public void bind() {
		if (controller.getSelectedPlugin() != null
				&& controller.getSelectedPlugin().getPlugin().getPluginName().equals(PluginNameConstants.FUZZYDB_PLUGIN)) {
			this.fuzzyDBPropertiesViewPresenter.bind();
			this.editFuzzyDBPropertiesPresenter.bind();
			this.pluginDataPresenter.bind();

			view.getPropertyDetailsViewPanel().setVisible(true);
			view.getEditPropertyDetailPanel().setVisible(false);

			view.getFuzzyDBPropertiesView().getEditButton().addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent arg0) {
					view.getPropertyDetailsViewPanel().setVisible(false);
					view.getEditPropertyDetailPanel().setVisible(true);
					controller.getBatchClass().setDirty(Boolean.TRUE);
					editFuzzyDBPropertiesPresenter.bind();
				}
			});
		}
	}

	/**
	 * To handle events.
	 * 
	 * @param eventBus HandlerManager
	 */
	@Override
	public void injectEvents(HandlerManager eventBus) {
	}

	/**
	 * To get Fuzzy DB Properties View Presenter.
	 * 
	 * @return FuzzyDBPropertiesViewPresenter
	 */
	public FuzzyDBPropertiesViewPresenter getFuzzyDBPropertiesViewPresenter() {
		return fuzzyDBPropertiesViewPresenter;
	}

	/**
	 * To get edit Fuzzy DB Properties Presenter.
	 * 
	 * @return EditFuzzyDBPropertiesPresenter
	 */
	public EditFuzzyDBPropertiesPresenter getEditFuzzyDBPropertiesPresenter() {
		return editFuzzyDBPropertiesPresenter;
	}

}
