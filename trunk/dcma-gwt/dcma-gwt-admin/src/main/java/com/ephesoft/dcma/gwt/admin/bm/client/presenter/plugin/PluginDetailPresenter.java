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

import java.util.Collection;
import java.util.Map;

import com.ephesoft.dcma.gwt.admin.bm.client.AdminConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.BatchClassManagementController;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementMessages;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.PluginNameConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.AbstractBatchClassPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.view.plugin.PluginDetailView;
import com.ephesoft.dcma.gwt.core.client.EphesoftAsyncCallback;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.client.view.ExternalAppDialogBox;
import com.ephesoft.dcma.gwt.core.client.view.ExternalAppDialogBox.DialogBoxListener;
import com.ephesoft.dcma.gwt.core.shared.BatchClassPluginConfigDTO;
import com.ephesoft.dcma.gwt.core.shared.BatchClassPluginDTO;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialogUtil;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;

/**
 * The presenter for view that shows the plugin details.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.gwt.admin.bm.client.presenter.AbstractBatchClassPresenter
 */
public class PluginDetailPresenter extends AbstractBatchClassPresenter<PluginDetailView> {

	/**
	 * viewTable FlexTable.
	 */
	private FlexTable viewTable;

	/**
	 * dataTable FlexTable.
	 */
	private FlexTable dataTable;

	/**
	 * Constructor.
	 * 
	 * @param controller BatchClassManagementController
	 * @param view PluginDetailView
	 */
	public PluginDetailPresenter(BatchClassManagementController controller, PluginDetailView view) {
		super(controller, view);
	}

	/**
	 * To set properties.
	 */
	public void setProperties() {
		int row = 0;
		Collection<BatchClassPluginConfigDTO> values = controller.getSelectedPlugin().getBatchClassPluginConfigs();
		if (values != null) {
			for (BatchClassPluginConfigDTO pluginDTO : values) {
				viewTable.setWidget(row, 0, new Label(pluginDTO.getDescription() + BatchClassManagementConstants.COLON));
				viewTable.setWidget(row, 2, new Label(pluginDTO.getValue()));
				viewTable.getFlexCellFormatter().addStyleName(row, 0, "bold_text");
				viewTable.getFlexCellFormatter().setAlignment(row, 0, HasHorizontalAlignment.ALIGN_RIGHT,
						HasVerticalAlignment.ALIGN_MIDDLE);
				row++;
			}
		}
	}

	/**
	 * Processing to be done on load of this presenter.
	 */
	@Override
	public void bind() {
		if (controller.getSelectedPlugin() != null) {
			this.dataTable = view.getViewTable();
			viewTable = new FlexTable();
			viewTable.setWidth("80%");
			viewTable.setCellSpacing(0);
			viewTable.getColumnFormatter().setWidth(0, "40%");
			viewTable.getColumnFormatter().setWidth(1, "1%");
			viewTable.getColumnFormatter().setWidth(2, "59%");
			dataTable.setWidget(0, 0, viewTable);
			dataTable.getCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_TOP);
			setProperties();
			if (controller.getSelectedPlugin().getPlugin().getPluginName().equalsIgnoreCase(PluginNameConstants.CMIS_EXPORT_PLUGIN)) {
				view.getTestCMIS().setVisible(Boolean.TRUE);
			} else {
				view.getTestCMIS().setVisible(Boolean.FALSE);
			}
			enableCMISGetTokenButton();
		}
	}


	private void enableCMISGetTokenButton() {
		if (controller.getSelectedPlugin().getPlugin().getPluginName().equals(PluginNameConstants.CMIS_EXPORT_PLUGIN)) {
			view.getCmisInfo().setVisible(true);
		}
	}

	/**
	 * To get data table.
	 * @return FlexTable
	 */
	public FlexTable getDataTable() {
		return dataTable;
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
	 * To get RPC service on clicking test Cmis button.
	 */
	public void onTestCmisButtonClicked() {
		controller.getRpcService().checkCmisConnection(controller.getSelectedPlugin().getBatchClassPluginConfigs(),
				new EphesoftAsyncCallback<Map<String, String>>() {

					@Override
					public void onSuccess(Map<String, String> arg0) {
						if (arg0 != null && !arg0.isEmpty()) {
							BatchClassPluginDTO batchClassPluginDTO = controller.getSelectedPlugin();
							for (BatchClassPluginConfigDTO batchClassPluginConfigDTO : batchClassPluginDTO
									.getBatchClassPluginConfigs()) {
								if (arg0.containsKey(batchClassPluginConfigDTO.getName())) {
									batchClassPluginConfigDTO.setValue(arg0.get(batchClassPluginConfigDTO.getName()));
								}
							}
							controller.setSelectedPlugin(batchClassPluginDTO);
							bind();

						}
						ConfirmationDialogUtil.showConfirmationDialogSuccess(LocaleDictionary.get().getMessageValue(
								BatchClassManagementMessages.CMIS_CONNECTION_SUCCESSFUL), true);
					}

					@Override
					public void customFailure(Throwable arg0) {
						ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(arg0.getMessage()),
								true);
					}
				});
	}

	/**
	 * API for getting the OAuth token using OAuth authentication.
	 */
	public void onGetTokenButtonClicked() {
		if (controller.getSelectedPlugin().getPlugin().getPluginName().equals(PluginNameConstants.CMIS_EXPORT_PLUGIN)) {
			final ExternalAppDialogBox externalAppDialogBox = new ExternalAppDialogBox(AdminConstants.EMPTY_STRING, 600, 425, false,
					true, true, false);

			externalAppDialogBox.addDialogBoxListener(new DialogBoxListener() {

				@Override
				public void onOkClick() {
				}

				@Override
				public void onCloseClick() {
					externalAppDialogBox.hide();
				}
			});

			controller.getRpcService().getCmisConfiguration(controller.getSelectedPlugin().getBatchClassPluginConfigs(),
					new EphesoftAsyncCallback<Map<String, String>>() {

						@Override
						public void customFailure(Throwable throwable) {
							externalAppDialogBox.hide();
						}

						@Override
						public void onSuccess(Map<String, String> arg0) {
							externalAppDialogBox.hide();
							if (arg0 != null && !arg0.isEmpty()) {
								BatchClassPluginDTO batchClassPluginDTO = controller.getSelectedPlugin();
								for (BatchClassPluginConfigDTO batchClassPluginConfigDTO : batchClassPluginDTO
										.getBatchClassPluginConfigs()) {
									if (arg0.containsKey(batchClassPluginConfigDTO.getName())) {
										batchClassPluginConfigDTO.setValue(arg0.get(batchClassPluginConfigDTO.getName()));
									}
								}
								controller.setSelectedPlugin(batchClassPluginDTO);
								bind();
							}
						}
					});

			controller.getRpcService().getAuthenticationURL(controller.getSelectedPlugin().getBatchClassPluginConfigs(),
					new EphesoftAsyncCallback<String>() {

						@Override
						public void customFailure(Throwable throwable) {

						}

						@Override
						public void onSuccess(String arg0) {
							externalAppDialogBox.getFrame().setUrl(arg0);
							externalAppDialogBox.show();
						}
					});
		}
	}
}
