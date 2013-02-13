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

package com.ephesoft.dcma.da.common;

import java.util.Iterator;
import java.util.Map;

import org.hibernate.LockMode;
import org.hibernate.dialect.SQLServerDialect;

import com.ephesoft.dcma.da.constant.DataAccessConstant;

/**
 * This class applies lock to ephesoft SQL server dialect.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see org.hibernate.dialect.SQLServerDialect
 */
public class EphesoftSQLServerDialect extends SQLServerDialect {
	
	/**
	 * This method appends lock hint.
	 * 
	 * @param mode LockMode 
	 * @param tableName {@link String}
	 * @return {@link String}
	 */
	public String appendLockHint(LockMode mode, String tableName) {
		if ( mode.greaterThan( LockMode.READ ) ) {
			// does this need holdlock also? : return tableName + " with (updlock, rowlock, holdlock)";
			return tableName + " with (updlock, rowlock)";
		} else {
			return tableName + " with (READCOMMITTED)";
		}
	}
	
	/**
	 * This method applies lock to sql.
	 * 
	 * @param sql {@link String}
	 * @param aliasedLockModes {@link Map}
	 * @param keyColumnNames {@link Map}
	 * @return {@link String}
	 */
	public String applyLocksToSql(String sql, Map aliasedLockModes, Map keyColumnNames) {
		Iterator itr = aliasedLockModes.entrySet().iterator();
		StringBuffer buffer = new StringBuffer( sql );
		int correction = 0;
		while ( itr.hasNext() ) {
			final Map.Entry entry = ( Map.Entry ) itr.next();
			final LockMode lockMode = ( LockMode ) entry.getValue();

			final String alias = ( String ) entry.getKey();
			int start = -1, end = -1;
			if ( sql.endsWith(DataAccessConstant.SPACE + alias ) ) {
				start = ( sql.length() - alias.length() ) + correction;
				end = start + alias.length();
			}
			else {
				int position = sql.indexOf( DataAccessConstant.SPACE + alias + DataAccessConstant.SPACE );
				if ( position <= -1 ) {
					position = sql.indexOf( DataAccessConstant.SPACE + alias + DataAccessConstant.COMMA);
				}
				if ( position > -1 ) {
					start = position + correction + 1;
					end = start + alias.length();
				}
			}

			if ( start > -1 ) {
				final String lockHint = appendLockHint( lockMode, alias );
				buffer.replace( start, end, lockHint );
				correction += ( lockHint.length() - alias.length() );
			}
		
		}
		return buffer.toString();
	}
	
}
