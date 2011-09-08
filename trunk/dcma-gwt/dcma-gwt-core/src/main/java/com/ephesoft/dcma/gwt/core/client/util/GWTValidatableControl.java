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

package com.ephesoft.dcma.gwt.core.client.util;


import java.util.List;

import com.ephesoft.dcma.gwt.core.client.validator.PatternValidator;
import com.ephesoft.dcma.gwt.core.client.validator.ValidatableWidget;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;

public enum GWTValidatableControl {

	DATE, LONG, STRING, DOUBLE, INTEGER, FLOAT;

	public static ValidatableWidget<SuggestBox> createGWTControl(String type, String actualValue, final String fieldName,
			List<String> alternateValues, List<String> patternStr, final String sampleValue) {
		
		//GWTValidatableControl control = GWTValidatableControl.valueOf(type);

		MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
		actualValue = null == actualValue ? "" : actualValue;
		oracle.add(actualValue);
		for (String altValue : alternateValues) {
			oracle.add(altValue);
		}

		SuggestBox suggestBox = new SuggestBox(oracle);
		suggestBox.setValue(actualValue, true);

		final ValidatableWidget<SuggestBox> validatableSBox = new ValidatableWidget<SuggestBox>(suggestBox);

		validatableSBox.getWidget().addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				validatableSBox.toggleValidDateBox();
			}
		});

		if (patternStr != null)
			validatableSBox.addValidator(new PatternValidator(suggestBox, patternStr));
		validatableSBox.toggleValidDateBox();
		return validatableSBox;
	}

	public static ValidatableWidget<SuggestBox> createGWTControl(String type, String actualValue, List<String> alternateValues) {
		return createGWTControl(type, actualValue, null, alternateValues, null, null);
	}

}
