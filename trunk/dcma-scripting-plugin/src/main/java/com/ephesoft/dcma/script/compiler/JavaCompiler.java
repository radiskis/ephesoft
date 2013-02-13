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

package com.ephesoft.dcma.script.compiler;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import com.ephesoft.dcma.script.constant.ScriptConstants;

/**
 * This class will compile the scripts at run time and execute it. A wrapper to ease the use of com.sun.tools.javac.Main.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.script.service.ScriptService
 */
public final class JavaCompiler {

	/**
	 * To store the classpath.
	 */
	private String classpath;

	/**
	 * To store the output directory.
	 */
	private String outputdir;

	/**
	 * To store the source path of class file.
	 */
	private String sourcepath;

	/**
	 * To store the bootable class path.
	 */
	private String bootclasspath;

	/**
	 * The external directories.
	 */
	private String extdirs;

	/**
	 * To store the encoding.
	 */
	private String encoding;

	/**
	 * To store the target location.
	 */
	private String target;

	/**
	 * Constructor.
	 * 
	 * @param classpath String
	 * @param outputdir String
	 */
	public JavaCompiler(final String classpath, final String outputdir) {
		this.classpath = classpath;
		this.outputdir = outputdir;
	}

	/**
	 * Compile the given source files.
	 * 
	 * @param srcFiles String
	 * @return null if success; or compilation errors
	 */
	public String compile(final String srcFiles[]) {
		String errorStr = null;
		final StringWriter err = new StringWriter();
		final PrintWriter errPrinter = new PrintWriter(err);

		final String args[] = buildJavacArgs(srcFiles);
		final int resultCode = com.sun.tools.javac.Main.compile(args, errPrinter);

		errPrinter.close();

		if (resultCode != 0) {
			errorStr = err.toString();
		}
		return errorStr;
	}

	/**
	 * Compile the given source files.
	 * 
	 * @param srcFiles File
	 * @return null if success; or compilation errors
	 */
	public String compile(final File srcFiles[]) {
		String paths[] = new String[srcFiles.length];
		for (int i = 0; i < paths.length; i++) {
			paths[i] = srcFiles[i].getAbsolutePath();
		}
		return compile(paths);
	}

	private String[] buildJavacArgs(final String srcFiles[]) {
		final List<String> args = new ArrayList<String>();

		if (classpath != null) {
			args.add(ScriptConstants.HYPHEN_CLASSPATH);
			args.add(classpath);
		}
		if (outputdir != null) {
			args.add(ScriptConstants.HYPHEN_D);
			args.add(outputdir);
		}
		if (sourcepath != null) {
			args.add(ScriptConstants.HYPHEN_SOURCEPATH);
			args.add(sourcepath);
		}
		if (bootclasspath != null) {
			args.add(ScriptConstants.HYPHEN_BOOTCLASSPATH);
			args.add(bootclasspath);
		}
		if (extdirs != null) {
			args.add(ScriptConstants.HYPHEN_EXTDIRS);
			args.add(extdirs);
		}
		if (encoding != null) {
			args.add(ScriptConstants.HYPHEN_ENCODING);
			args.add(encoding);
		}
		if (target != null) {
			args.add(ScriptConstants.HYPHEN_TARGET);
			args.add(target);
		}

		for (int i = 0; i < srcFiles.length; i++) {
			args.add(srcFiles[i]);
		}

		return (String[]) args.toArray(new String[args.size()]);
	}

	/**
	 * To get Boot classpath.
	 * 
	 * @return String
	 */
	public String getBootclasspath() {
		return bootclasspath;
	}

	/**
	 * To set Boot classpath.
	 * 
	 * @param bootclasspath String
	 */
	public void setBootclasspath(final String bootclasspath) {
		this.bootclasspath = bootclasspath;
	}

	/**
	 * To get Classpath.
	 * 
	 * @return String
	 */
	public String getClasspath() {
		return classpath;
	}

	/**
	 * To set Classpath.
	 * 
	 * @param classpath String
	 */
	public void setClasspath(final String classpath) {
		this.classpath = classpath;
	}

	/**
	 * To get Encoding.
	 * 
	 * @return String
	 */
	public String getEncoding() {
		return encoding;
	}

	/**
	 * To set Encoding.
	 * 
	 * @param encoding String
	 */
	public void setEncoding(final String encoding) {
		this.encoding = encoding;
	}

	/**
	 * To get Extdirs.
	 * 
	 * @return String
	 */
	public String getExtdirs() {
		return extdirs;
	}

	/**
	 * To set Extdirs.
	 * 
	 * @param extdirs String
	 */
	public void setExtdirs(final String extdirs) {
		this.extdirs = extdirs;
	}

	/**
	 * To get Outputdir.
	 * 
	 * @return String
	 */
	public String getOutputdir() {
		return outputdir;
	}

	/**
	 * To set Outputdir.
	 * 
	 * @param outputdir String
	 */
	public void setOutputdir(final String outputdir) {
		this.outputdir = outputdir;
	}

	/**
	 * To get Source path.
	 * 
	 * @return String
	 */
	public String getSourcepath() {
		return sourcepath;
	}

	/**
	 * To set Source path.
	 * 
	 * @param sourcepath String
	 */
	public void setSourcepath(final String sourcepath) {
		this.sourcepath = sourcepath;
	}

	/**
	 * To get Target.
	 * 
	 * @return String
	 */
	public String getTarget() {
		return target;
	}

	/**
	 * To set Target.
	 * 
	 * @param target String
	 */
	public void setTarget(final String target) {
		this.target = target;
	}

}
