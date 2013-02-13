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

package com.ephesoft.dcma.webapp;

import java.io.IOException;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ephesoft.dcma.constant.WebAppConstants;

/**
 * This class is to execute a chain of filters.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see javax.servlet.FilterChain
 */
public class HTTPHeaderFilter implements Filter {

	/**
	 * Destroy method.
	 */
	@Override
	public void destroy() {
		// No implementation
	}

	/**
	 * To make the application compatible with IE-8 and to prevent caching in the browser.
	 * 
	 * @param request ServletRequest
	 * @param response ServletResponse
	 * @param chain FilterChain
	 * @throws IOException in case of error
	 * @throws ServletException in case of error
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		if (response instanceof HttpServletResponse) {
			((HttpServletResponse) response).setHeader(WebAppConstants.X_UA_COMPATIBLE, WebAppConstants.IE_8);
		}
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		String requestURI = httpRequest.getRequestURI();

		if (requestURI.contains(WebAppConstants.NOCACHE) || requestURI.contains(WebAppConstants.CSS_EXTENSION)) {

			Date now = new Date();
			HttpServletResponse httpResponse = (HttpServletResponse) response;
			httpResponse.setDateHeader(WebAppConstants.DATE, now.getTime());
			httpResponse.setDateHeader(WebAppConstants.EXPIRES, now.getTime() - WebAppConstants.TIME_CONST);
			httpResponse.setHeader(WebAppConstants.PRAGMA, WebAppConstants.NO_CACHE);
			httpResponse.setHeader(WebAppConstants.CACHE_CONTROL, WebAppConstants.NO_CACHE_NO_STORE_MUST_REVALIDATE);
		}
		chain.doFilter(request, response);
	}

	/**
	 * Init method.
	 * 
	 * @param arg0 FilterConfig
	 * @throws ServletException in case of error
	 */
	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// No implementation
	}

}
