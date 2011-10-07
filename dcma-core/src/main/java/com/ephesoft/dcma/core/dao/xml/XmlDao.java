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

package com.ephesoft.dcma.core.dao.xml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamResult;

import org.hibernate.criterion.DetachedCriteria;

import com.ephesoft.dcma.core.common.DCMABusinessException;
import com.ephesoft.dcma.core.component.JAXB2Template;
import com.ephesoft.dcma.core.dao.Dao;
import com.ephesoft.dcma.util.XMLUtil;

/**
 * @author Ephesoft
 * @version 1.0
 * 
 * @param <T>
 */
public abstract class XmlDao<T> implements Dao<T> {

	public void create(T object, Serializable identifier) {
		create(object, identifier, null, "_batch.xml");
	}

	public void create(T object, Serializable identifier, String pageId, String fileName) {
		FileOutputStream stream = null;
		try {
			String filePath = null;
			if (null == pageId) {
				filePath = getJAXB2Template().getLocalFolderLocation() + File.separator + identifier + File.separator + identifier
						+ fileName;
			} else {
				filePath = getJAXB2Template().getLocalFolderLocation() + File.separator + identifier + File.separator + identifier
						+ "_" + pageId + fileName;
			}
			stream = new FileOutputStream(new File(filePath));
			StreamResult result = new StreamResult(stream);
			getJAXB2Template().getJaxb2Marshaller().marshal(object, result);
		} catch (FileNotFoundException e) {
			throw new DCMABusinessException(e.getMessage(), e);
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public T get(Serializable identifier) {
		return get(identifier, null, "_batch.xml");
	}

	@SuppressWarnings("unchecked")
	public T get(Serializable identifier, String pageId, String fileName) {
		try {
			String filePath = null;

			if (null == pageId) {
				filePath = getJAXB2Template().getLocalFolderLocation() + File.separator + identifier + File.separator + identifier
						+ fileName;
			} else {
				filePath = getJAXB2Template().getLocalFolderLocation() + File.separator + identifier + File.separator + identifier
						+ "_" + pageId + fileName;
			}

			File xmlFile = new File(filePath);
			Source source = XMLUtil.createSourceFromFile(xmlFile);
			return (T) getJAXB2Template().getJaxb2Marshaller().unmarshal(source);
		} catch (Exception e) {
			throw new DCMABusinessException(e.getMessage(), e);
		}
	}

	@Override
	public List<T> getAll() {
		throw new UnsupportedOperationException("Operation not supported.");
	}

	@Override
	public T merge(T object) {
		throw new UnsupportedOperationException("Operation not supported.");
	}

	@Override
	public void remove(T object) {
		// TODO Auto-generated method stub
	}

	@Override
	public void saveOrUpdate(T object) {
		throw new UnsupportedOperationException("Operation not supported.");
	}

	@Override
	public void create(T object) {
		throw new UnsupportedOperationException("Operation not supported.");
	}

	@Override
	public void evict(Object object) {
		throw new UnsupportedOperationException("Operation not supported.");
	}

	public void update(T object, Serializable identifier, String fileName) {
		FileOutputStream stream = null;
		try {
			stream = new FileOutputStream(new File(getJAXB2Template().getLocalFolderLocation() + File.separator + identifier
					+ File.separator + identifier + fileName));
			StreamResult result = new StreamResult(stream);
			getJAXB2Template().getJaxb2Marshaller().marshal(object, result);
		} catch (FileNotFoundException e) {
			throw new DCMABusinessException(e.getMessage(), e);
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
				}
			}
		}
	}

	@Override
	public int countAll() {
		return 0;
	}

	@Override
	public int count(DetachedCriteria criteria) {
		return 0;
	}

	public void update(T object, Serializable identifier) {
		update(object, identifier, "_batch.xml");
	}

	public abstract JAXB2Template getJAXB2Template();

}
