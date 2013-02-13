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

package com.ephesoft.dcma.workflow.jbpm.pvm.internal.wire.descriptor;

import org.jbpm.pvm.internal.wire.WireContext;
import org.jbpm.pvm.internal.wire.descriptor.AbstractDescriptor;

import com.ephesoft.dcma.core.common.BatchInstanceStatus;

/**
 * This class gives description of the status of batch instance.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see org.jbpm.pvm.internal.wire.descriptor.AbstractDescriptor
 * 
 */
public class BatchInstanceStatusDescriptor extends AbstractDescriptor {

	/**
	 * serialVersionUID long.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * status {@link BatchInstanceStatus}.
	 */
	private final BatchInstanceStatus status;

	/**
	 * Constructor.
	 * 
	 * @param batchInstanceStatus {@link BatchInstanceStatus}
	 */
	public BatchInstanceStatusDescriptor(final BatchInstanceStatus batchInstanceStatus) {
		super();
		status = batchInstanceStatus;
	}

	/**
	 * Returns the status.
	 * 
	 * @param wireContext WireContext
	 * @return Object
	 */
	@Override
	public Object construct(WireContext wireContext) {
		return status;
	}

	/*
	 * <java expr="#{batchInstanceService}" g="81,90,207,60" method="updateBatchInstanceStatus" name="set-status-to-running"> <arg
	 * type="com.ephesoft.dcma.core.id.BatchInstanceID"> <object expr="#{batchIDD}"/> </arg> <arg> <batch-instance-status
	 * value="RUNNING"/> </arg> <transition name="to folder-import" to="folder-import" g="-80,-18"/> </java>
	 */
}
