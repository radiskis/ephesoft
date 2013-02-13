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

package com.ephesoft.dcma.gwt.admin.bm.client.presenter.scanner;

import java.util.Collection;

import com.ephesoft.dcma.gwt.admin.bm.client.BatchClassManagementController;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.AbstractBatchClassPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.view.scanner.ScannerDetailView;
import com.ephesoft.dcma.gwt.core.shared.WebScannerConfigurationDTO;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;

/**
 * The presenter for view that shows the scanner details.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.gwt.admin.bm.client.presenter.AbstractBatchClassPresenter
 */
public class ScannerDetailPresenter extends AbstractBatchClassPresenter<ScannerDetailView> {

	/**
	 * dataTable FlexTable.
	 */
	private FlexTable dataTable;

	/**
	 * Constructor.
	 * 
	 * @param controller BatchClassManagementController
	 * @param view ScannerDetailView
	 */
	public ScannerDetailPresenter(BatchClassManagementController controller, ScannerDetailView view) {
		super(controller, view);

	}

	/**
	 * Processing to be done on load of this presenter.
	 */
	@Override
	public void bind() {
		if (controller.getSelectedWebScannerConfiguration() != null) {
			view.setView();
			this.dataTable = view.getDataTable();
			dataTable.getColumnFormatter().setWidth(0, "40%");
			dataTable.getColumnFormatter().setWidth(1, "1%");
			dataTable.getColumnFormatter().setWidth(2, "59%");
			dataTable.getCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_TOP);
			setProperties();
		}
	}

	private void setProperties() {
		int row = 0;
		Collection<WebScannerConfigurationDTO> values = controller.getSelectedWebScannerConfiguration().getChildren();
		if (values != null) {

			// handling for profile name field
			dataTable.setWidget(row, 0, new Label(controller.getSelectedWebScannerConfiguration().getDescription()
					+ BatchClassManagementConstants.COLON));
			dataTable.setWidget(row, 2, new Label(controller.getSelectedWebScannerConfiguration().getValue()));
			dataTable.getFlexCellFormatter().addStyleName(row, 0, "bold_text");
			dataTable.getFlexCellFormatter().setAlignment(row, 0, HasHorizontalAlignment.ALIGN_RIGHT,
					HasVerticalAlignment.ALIGN_MIDDLE);
			row++;

			for (WebScannerConfigurationDTO dto : values) {
				dataTable.setWidget(row, 0, new Label(dto.getDescription() + BatchClassManagementConstants.COLON));
				dataTable.setWidget(row, 2, new Label(dto.getValue()));
				dataTable.getFlexCellFormatter().addStyleName(row, 0, "bold_text");
				dataTable.getFlexCellFormatter().setAlignment(row, 0, HasHorizontalAlignment.ALIGN_RIGHT,
						HasVerticalAlignment.ALIGN_MIDDLE);
				row++;
			}
		}
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
}
