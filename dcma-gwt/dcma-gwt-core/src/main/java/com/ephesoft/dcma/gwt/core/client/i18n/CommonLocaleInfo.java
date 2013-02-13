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

package com.ephesoft.dcma.gwt.core.client.i18n;

import java.util.ArrayList;
import java.util.List;

public final class CommonLocaleInfo extends LocaleInfo {

	private CommonLocaleInfo(String locale) {
		super(locale, "commonConstants", null);
		availableConstants.add(LocaleCommonConstants.HEADER_LABEL_HI);
		availableConstants.add(LocaleCommonConstants.HEADER_LABEL_SIGN_OUT);
		availableConstants.add(LocaleCommonConstants.HEADER_LABEL_HELP);
		availableConstants.add(LocaleCommonConstants.MSG_MASK_WAIT);
		availableConstants.add(LocaleCommonConstants.EXC_CONSUMER_AMOUNT_IS_NOT_POSITIVE);
		availableConstants.add(LocaleCommonConstants.EXC_CONSUMER_TYPE_IS_NULL);
		availableConstants.add(LocaleCommonConstants.EXC_CPU_COUNT_VOILATION);
		availableConstants.add(LocaleCommonConstants.EXC_HOLDER_IS_NULL);
		availableConstants.add(LocaleCommonConstants.EXC_INVALID_SUBJECT);
		availableConstants.add(LocaleCommonConstants.EXC_ISSUED_IS_NULL);
		availableConstants.add(LocaleCommonConstants.EXC_ISSUER_IS_NULL);
		availableConstants.add(LocaleCommonConstants.EXC_LICENSE_HAS_EXPIRED);
		availableConstants.add(LocaleCommonConstants.EXC_LICENSE_IS_NOT_YET_VALID);
		availableConstants.add(LocaleCommonConstants.EXC_SYSTEM_DATE_VOILATION);
		availableConstants.add(LocaleCommonConstants.EXC_SYSTEM_MAC_ADDRESS_VOILATION);
		availableConstants.add(LocaleCommonConstants.TITLE_CONFIRMATION_OK);
		availableConstants.add(LocaleCommonConstants.TITLE_CONFIRMATION_CANCEL);
		availableConstants.add(LocaleCommonConstants.DIALOG_BOX_TITLE);
		availableConstants.add(LocaleCommonConstants.EXC_EPHESOFT_LICENSE_CPU_LIMIT_EXHAUSTED);
		availableConstants.add(LocaleCommonConstants.EXC_EPHESOFT_LICENSE_SERVER_ERROR);
		availableConstants.add(LocaleCommonConstants.TITLE_GO_TO_PAGE);
		availableConstants.add(LocaleCommonConstants.TITLE_PREVIOUS);
		availableConstants.add(LocaleCommonConstants.TITLE_NEXT);
		availableConstants.add(LocaleCommonConstants.TITLE_DISPLAYING);
		availableConstants.add(LocaleCommonConstants.TITLE_CONFIRMATION_SAVE);
		availableConstants.add(LocaleCommonConstants.TITLE_CONFIRMATION_DISCARD);
		availableConstants.add(LocaleCommonConstants.HELP_URK_ERROR_MESSAGE);
		availableConstants.add(LocaleCommonConstants.ERROR_TITLE);
		availableConstants.add(LocaleCommonConstants.UP_RECORD);
		availableConstants.add(LocaleCommonConstants.DOWN_RECORD);
		availableConstants.add(LocaleCommonConstants.SERVER_DISCONNECTED);
	}

	public static CommonLocaleInfo get(String locale) {
		return new CommonLocaleInfo(locale);
	}

	private final List<String> availableConstants = new ArrayList<String>(3);

	public boolean isExist(String key) {
		return availableConstants.contains(key);
	}
}
