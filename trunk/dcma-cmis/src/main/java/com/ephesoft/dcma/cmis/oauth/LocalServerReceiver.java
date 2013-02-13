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

/*
 * Copyright (c) 2011 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.ephesoft.dcma.cmis.oauth;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Request;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.AbstractHandler;
import org.mortbay.jetty.nio.SelectChannelConnector;

import com.ephesoft.dcma.core.component.ICommonConstants;

/**
 * This class is implemented the VerificationCodeRecevier.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.cmis.oauth.VerificationCodeReceiver
 */
public final class LocalServerReceiver implements VerificationCodeReceiver {

	/**
	 * Constant for http string variable.
	 */
	private static final String HTTP = "http";

	/**
	 * Instance for callbackPath.
	 */
	private String callbackPath;

	/**
	 * Instance for host address.
	 */
	private String host;

	/**
	 * Instance for port address.
	 */
	private int port;

	/** Server or {@code null} before {@link #getRedirectUri()}. */
	private Server server;

	/** Verification code or {@code null} before received. */
	volatile String code;
	
	/**
	 * Constructor for LocalServerRecevier.
	 * 
	 * @param host {@link String}
	 * @param port {@link Integer}
	 * @param callbackPath {@link String}
	 */
	public LocalServerReceiver(String host, int port, String callbackPath) {
		this.host = host;
		this.port = port;
		this.callbackPath = ICommonConstants.FORWARD_SLASH + callbackPath;
	}

	@Override
	public String getRedirectUri() throws Exception {
		server = new Server();
		SelectChannelConnector connector = new SelectChannelConnector();
		connector.setPort(port);
		server.addConnector(connector);
		server.addHandler(new CallbackHandler());
		server.start();
		return HTTP + ICommonConstants.COLON + ICommonConstants.FORWARD_SLASH + ICommonConstants.FORWARD_SLASH + host
				+ ICommonConstants.COLON + port + callbackPath;
	}

	@Override
	public synchronized String waitForCode() {
		try {
			this.wait();
		} catch (InterruptedException exception) {
		}
		return code;
	}

	@Override
	public void stop() throws Exception {
		if (server != null) {
			for (Connector connector : server.getConnectors()) {
				connector.stop();
				connector.close();
			}
			server.stop();
			server.destroy();
		}
	}

	/**
	 * Jetty handler that takes the verifier token passed over from the OAuth provider and stashes it where {@link #waitForCode} will
	 * find it.
	 */
	class CallbackHandler extends AbstractHandler {

		@Override
		public void handle(String target, HttpServletRequest request, HttpServletResponse response, int dispatch) throws IOException {
			if (!callbackPath.equals(target)) {
				return;
			}
			writeLandingHtml(response);
			response.flushBuffer();
			((Request) request).setHandled(true);
			String error = request.getParameter("error");
			if (error != null) {
				throw new IOException("Unable to get the authentication code");
			}
			code = request.getParameter("code");
			synchronized (LocalServerReceiver.this) {
				LocalServerReceiver.this.notify();
			}
		}

		private void writeLandingHtml(HttpServletResponse response) throws IOException {
			response.setStatus(HttpServletResponse.SC_OK);
			response.setContentType("text/html");

			PrintWriter doc = response.getWriter();
			doc.println("<html>");
			doc.println("<head><title>OAuth 2.0 Authentication Token Recieved</title></head>");
			doc.println("<body>");
			doc.println("Received verification code. Closing...");
			doc.println("<script type='text/javascript'>");
			// We open "" in the same window to trigger JS ownership of it, which lets
			// us then close it via JS, at least in Chrome.
			doc.println("window.setTimeout(function() {");
			doc.println("    window.open('', '_self', ''); window.close(); }, 1000);");
			doc.println("if (window.opener) { window.opener.checkToken(); }");
			doc.println("</script>");
			doc.println("</body>");
			doc.println("</HTML>");
			doc.flush();
		}
	}
}
