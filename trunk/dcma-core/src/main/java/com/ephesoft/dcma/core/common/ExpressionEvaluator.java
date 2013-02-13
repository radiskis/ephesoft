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

package com.ephesoft.dcma.core.common;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is for evaluating expressions.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see javax.script.ScriptEngineManager
 * @param <T>
 */
public class ExpressionEvaluator<T> {

	/**
	 * LOGGER to print the logging information.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(ExpressionEvaluator.class);

	/**
	 * expression String.
	 */
	private final String expression;
	
	/**
	 * mgr ScriptEngineManager.
	 */
	private final ScriptEngineManager mgr;
	
	/**
	 * engine ScriptEngine.
	 */
	private ScriptEngine engine;
	
	/**
	 * SCRIPT_ENGINE_NAME String.
	 */
	private static final String SCRIPT_ENGINE_NAME = "JavaScript";

	/**
	 * Constructor.
	 * 
	 * @param expression String
	 */
	public ExpressionEvaluator(final String expression) {
		this.expression = expression;
		this.mgr = new ScriptEngineManager();
		this.engine = mgr.getEngineByName(SCRIPT_ENGINE_NAME);
	}

	/**
	 * Evaluation method.
	 * @return T 
	 * @throws ScriptException in case of error
	 * @throws ClassCastException in case of error
	 */
	@SuppressWarnings("unchecked")
	public T eval() throws ScriptException, ClassCastException {
		//Object result = engine.eval(expression);
		return (T) engine.eval(expression);
	}

	/**
	 * To put the value.
	 * @param key String
	 * @param value Object
	 */
	public void putValue(final String key, final Object value) {
		engine.put(key, value);
	}

	/**
	 * To set Script Engine Name.
	 * @param engineName String
	 */
	public void setScriptEngineName(final String engineName) {
		if (engineName != null) {
			this.engine = mgr.getEngineByName(engineName);
			if (this.engine == null) {
				LOGGER.info("Cannot find any engine with name = " + engineName);
				this.engine = mgr.getEngineByName(SCRIPT_ENGINE_NAME);
				LOGGER.info("Engine created with : " + SCRIPT_ENGINE_NAME);
			}
		}
	}

}
