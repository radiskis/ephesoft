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

package com.ephesoft.dcma.gwt.admin.bm.client.presenter.regex;

import com.ephesoft.dcma.gwt.admin.bm.client.BatchClassManagementController;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.AbstractBatchClassPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.view.regex.RegexView;
import com.google.gwt.event.shared.HandlerManager;

/**
 * The presenter for view that shows the regex details.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.gwt.admin.bm.client.presenter.AbstractBatchClassPresenter
 */
public class RegexPresenter extends AbstractBatchClassPresenter<RegexView> {

	/**
	 * regexDetailPresenter RegexDetailPresenter.
	 */
	private final RegexDetailPresenter regexDetailPresenter;

	/**
	 * editRegexPresenter EditRegexPresenter.
	 */
	private final EditRegexPresenter editRegexPresenter;

	/**
	 * Constructor.
	 * 
	 * @param controller BatchClassManagementController
	 * @param view RegexView
	 */
	public RegexPresenter(BatchClassManagementController controller, RegexView view) {

		super(controller, view);
		this.regexDetailPresenter = new RegexDetailPresenter(controller, view.getRegexDetailView());
		this.editRegexPresenter = new EditRegexPresenter(controller, view.getEditRegexView());
	}

	/**
	 * Processing to be done on load of this presenter.
	 */
	@Override
	public void bind() {
		regexDetailPresenter.bind();
		editRegexPresenter.bind();
	}

	/**
	 * To show Regex Detail View.
	 */
	public void showRegexDetailView() {
		view.getRegexVerticalPanel().setVisible(Boolean.TRUE);
		view.getRegexConfigVerticalPanel().setVisible(Boolean.FALSE);
	}

	/**
	 * To show Edit Regex View.
	 */
	public void showEditRegexView() {
		view.getRegexVerticalPanel().setVisible(Boolean.FALSE);
		view.getRegexConfigVerticalPanel().setVisible(Boolean.TRUE);
	}

	/**
	 * To perform operations on Edit Function Key Properties button click.
	 */
	public void onEditRegexPropertiesBtnClicked() {
		controller.setAdd(false);
		showEditRegexView();
		controller.getBatchClass().setDirty(Boolean.TRUE);
		editRegexPresenter.bind();
	}

	/**
	 * To handle events.
	 * 
	 * @param eventBus HandlerManager
	 */
	@Override
	public void injectEvents(HandlerManager eventBus) {
		// event handling code goes here.
	}
}
