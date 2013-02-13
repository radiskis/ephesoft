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

package com.ephesoft.dcma.gwt.core.client.validator;

import java.util.HashSet;
import java.util.Set;

import com.google.gwt.user.client.ui.Widget;

public class RegExValidatableWidget<W extends Widget> implements Validatable, HasValidators {

	private static final String DATE_BOX_FORMAT_ERROR = "dateBoxFormatError";
	private final W widget;

	public RegExValidatableWidget(W widget) {
		this.widget = widget;
	}

	public RegExValidatableWidget(W widget, boolean valid) {
		this.widget = widget;
		this.valid = valid;
	}

	private final Set<Validator> _validators = new HashSet<Validator>();

	private boolean valid;

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
		toggleValidateBox(valid);
	}

	@Override
	public void toggleValidDateBox() {
		validate();
	}

	public boolean validateThroughValidators() {
		boolean valid = true;
		for (Validator validator : _validators) {
			if (validator != null && !validator.validate()) {
					valid = false;
					break;
			}
		}
		return valid;
	}

	@Override
	public boolean validate() {
		valid = validateThroughValidators();
		toggleValidateBox(valid);
		return valid;
	}

	public void toggleValidateBox(boolean valid) {
		if (valid){
			widget.removeStyleName(DATE_BOX_FORMAT_ERROR);
		}
		else{
			widget.addStyleName(DATE_BOX_FORMAT_ERROR);
		}
	}

	@Override
	public void addValidator(Validator validator) {
		_validators.add(validator);
	}

	public W getWidget() {
		return widget;
	}

}
