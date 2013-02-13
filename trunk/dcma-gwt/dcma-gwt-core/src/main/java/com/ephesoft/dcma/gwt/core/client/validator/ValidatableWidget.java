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

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class ValidatableWidget<W extends Widget> implements Validatable, HasValidators {

	private static final String DATE_BOX_FORMAT_ERROR = "dateBoxFormatError";

	private W widget;

	private boolean validateWidget;

	private boolean forcedReviewDone = true;

	public boolean isValidateWidget() {
		return validateWidget;
	}

	public void setValidateWidget(boolean validateWidget) {
		this.validateWidget = validateWidget;
	}

	public ValidatableWidget(W widget) {
		this(widget, true);
	}

	public ValidatableWidget(W widget, boolean validateWidget) {
		this.widget = widget;
		this.validateWidget = validateWidget;
		addValueChangeHandler();
	}

	private void addValueChangeHandler() {
		if (this.widget != null && this.widget instanceof TextBox) {
			((TextBox) widget).addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> val) {
					String value = val.getValue();
					if (value != null) {
						((TextBox) widget).setValue(value.trim());
					}
				}
			});
		}
	}

	private final Set<Validator> _validators = new HashSet<Validator>();

	@Override
	public void addValidator(Validator validator) {
		_validators.add(validator);
	}

	public W getWidget() {
		return widget;

	}

	@Override
	public void toggleValidDateBox() {
		validate();
	}

	public void toggleValidateBox(boolean valid) {
		if (valid){
			widget.removeStyleName(DATE_BOX_FORMAT_ERROR);
		}
		else {
			widget.addStyleName(DATE_BOX_FORMAT_ERROR);
		}
	}

	@Override
	public boolean validate() {
		boolean valid = forcedReviewDone;
		if (forcedReviewDone) {
			valid = validateThroughValidators();
		}
		toggleValidateBox(valid);
		return valid;
	}

	public boolean validateThroughValidators() {
		boolean valid = true;
		if (validateWidget) {
			for (Validator validator : _validators) {
				if (validator != null && !validator.validate()) {
						valid = false;
						break;
					}
			}
		}
		return valid;
	}

	public void setForcedReviewDone(boolean isForcedReviewDone) {
		this.forcedReviewDone = isForcedReviewDone;
		validate();
	}

	public boolean isForcedReviewDone() {
		return forcedReviewDone;
	}

}
