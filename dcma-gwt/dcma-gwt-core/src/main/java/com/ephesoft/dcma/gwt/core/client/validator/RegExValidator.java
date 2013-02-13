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

import com.ephesoft.dcma.gwt.core.client.DCMARemoteServiceAsync;
import com.ephesoft.dcma.gwt.core.client.EphesoftAsyncCallback;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.client.ui.ScreenMaskUtility;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialogUtil;
import com.google.gwt.user.client.ui.HasValue;

public class RegExValidator implements Validator {

	protected static final String INVALID_REGEX_PATTERN = "invalid_regex_pattern";
	private final HasValue<String> _value;
	private String pattern;
	private final boolean isOnlyPatternValidator;
	private final boolean isMandatory;
	private final boolean isMultiplePattern;
	private String patternDelimiter;
	private final DCMARemoteServiceAsync remoteService;
	private RegExValidatableWidget<?> validatableWidget;

	public RegExValidator(final RegExValidatableWidget<?> validatableWidget, final String pattern, final HasValue<String> value,
			final DCMARemoteServiceAsync remoteServiceAsync) {
		this(validatableWidget, value, true, false, false, null, remoteServiceAsync);
		this.pattern = pattern;
	}

	public RegExValidator(final RegExValidatableWidget<?> validatableWidget, final HasValue<String> value, final boolean isMandatory,
			final boolean isMultiplePattern, final boolean isPatternValidator, final String patternDelimiter,
			final DCMARemoteServiceAsync remoteServiceAsync) {
		this._value = value;
		this.validatableWidget = validatableWidget;
		this.isMandatory = isMandatory;
		this.isMultiplePattern = isMultiplePattern;
		this.isOnlyPatternValidator = isPatternValidator;
		this.patternDelimiter = patternDelimiter;
		this.remoteService = remoteServiceAsync;
	}

	@Override
	public boolean validate() {
		boolean isPatternValid = true;
		if (isOnlyPatternValidator) {
			final String pattern = _value.getValue();
			if (pattern.isEmpty() && isMandatory) {
				isPatternValid = false;
			} else {
				if (isMultiplePattern) {
					isPatternValid = validateMultiplePattern(pattern);
				} else {
					isPatternValid = validatePatternOnServer(pattern);
				}
			}
		} else {
			if (_value.getValue() == null || _value.getValue().trim().isEmpty()) {
				isPatternValid = false;
			} else {
				isPatternValid = validateValueWithPatternOnServer(_value.getValue(), pattern);
			}
		}
		return isPatternValid;
	}

	public boolean validateMultiplePattern(final String multiplePattern) {
		boolean isPatternValid = true;
		String[] patternArr = null;
		if (multiplePattern.contains(patternDelimiter)) {
			patternArr = multiplePattern.split(patternDelimiter);
			for (final String pattern : patternArr) {
				if (!validatePatternOnServer(pattern)) {
					isPatternValid = false;
					break;
				}
			}
		} else {
			isPatternValid = validatePatternOnServer(multiplePattern);
		}
		return isPatternValid;
	}

	private boolean validateValueWithPatternOnServer(final String value, final String pattern) {
		ScreenMaskUtility.maskScreen();
		remoteService.validateValueWithRegEx(value, pattern, new EphesoftAsyncCallback<Boolean>() {

			@Override
			public void onSuccess(final Boolean isValidInput) {
				ScreenMaskUtility.unmaskScreen();
				validatableWidget.toggleValidateBox(isValidInput);
			}

			@Override
			public void customFailure(final Throwable arg0) {
				ScreenMaskUtility.unmaskScreen();
				validatableWidget.toggleValidateBox(false);
			}
		});
		return false;
	}

	private boolean validatePatternOnServer(final String pattern) {
		ScreenMaskUtility.maskScreen();
		remoteService.validateRegEx(pattern, new EphesoftAsyncCallback<Boolean>() {

			@Override
			public void onSuccess(final Boolean isPatternValid) {
				validatableWidget.setValid(isPatternValid);
				ScreenMaskUtility.unmaskScreen();
				if (!isPatternValid) {
					ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(INVALID_REGEX_PATTERN));
				}
			}

			@Override
			public void customFailure(final Throwable arg0) {
				validatableWidget.setValid(false);
				ScreenMaskUtility.unmaskScreen();
				ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(INVALID_REGEX_PATTERN));
			}
		});
		return false;
	}
}
