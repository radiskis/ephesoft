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

package com.ephesoft.dcma.workflow.aspects;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ClassUtils;

import com.ephesoft.dcma.core.DCMAException;
import com.ephesoft.dcma.core.annotation.PostProcess;
import com.ephesoft.dcma.da.id.BatchInstanceID;

/**
 * This class performs some post-processing in executing the batch.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see org.aspectj.lang.annotation.AfterReturning
 */
@Aspect
public class DCMAPostProcessAspect {

	/**
	 * LOGGER to print the logging information.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(DCMAPostProcessAspect.class);

	/**
	 * To perform post-processing.
	 * 
	 * @param joinPoint JoinPoint
	 * @throws DCMAException in case of error
	 */
	@AfterReturning("execution(* com.ephesoft.dcma.*.service.*.*(..)) " + "&& !within(com.ephesoft.dcma.da.service.*) "
			+ "&& !within(com.ephesoft.dcma.workflows.service.*)")
	public void postprocess(JoinPoint joinPoint) throws DCMAException {

		try {
			Object target = joinPoint.getTarget();
			if (target == null) {
				return;
			}

			Class<?> clazz = ClassUtils.getUserClass(target);
			Method[] methods = clazz.getMethods();
			if (methods != null && methods.length > 0) {
				for (int i = 0; i < methods.length; i++) {
					Annotation annotation = methods[i].getAnnotation(PostProcess.class);
					if (annotation != null) {
						if (joinPoint.getArgs().length >= 1 && (joinPoint.getArgs()[0] instanceof BatchInstanceID)) {
							methods[i].invoke(target, joinPoint.getArgs()[0], joinPoint.getArgs()[1]);
						} else {
							LOGGER.info("Method " + methods[i] + " does not comply to Post-process agreement. So.. not invoked.");
						}
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("Exception in Post-processing", e);
			throw new DCMAException("Exception in Post-processing", e);
		}
	}
}
