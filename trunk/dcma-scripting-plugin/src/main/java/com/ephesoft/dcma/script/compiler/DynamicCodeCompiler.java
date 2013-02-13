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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ephesoft.dcma.core.common.DCMABusinessException;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.script.constant.ScriptConstants;

/**
 * This class will compile the scripts at run time and execute it.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.script.service.ScriptService
 */
public final class DynamicCodeCompiler {

	/**
	 * LOGGER to print the logging information.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(DynamicCodeCompiler.class);

	/**
	 * String to store compile class path.
	 */
	private final String compileClasspath;

	/**
	 * String to store the parent class loader value.
	 */
	private final ClassLoader parentClassLoader;

	/**
	 * List to store the source directories.
	 */
	final private List<SourceDirectory> sourceDirectories = new ArrayList<SourceDirectory>();

	/**
	 * Map type variable to store loaded classes.
	 */
	final private Map<String, LoadedClass> loadedClasses = new HashMap<String, LoadedClass>();

	/**
	 * Constructor.
	 */
	public DynamicCodeCompiler() {
		this(Thread.currentThread().getContextClassLoader());
	}

	/**
	 * Constructor.
	 * 
	 * @param parentClassLoader {@link ClassLoader}
	 */
	public DynamicCodeCompiler(final ClassLoader parentClassLoader) {
		this(extractClasspath(parentClassLoader), parentClassLoader);

	}

	/**
	 * Constructor.
	 * 
	 * @param compileClasspath {@link String} used to compile dynamic classes
	 * @param parentClassLoader {@link ClassLoader} the parent of the class loader that loads all the dynamic classes
	 */
	public DynamicCodeCompiler(final String compileClasspath, final ClassLoader parentClassLoader) {
		this.compileClasspath = compileClasspath;
		this.parentClassLoader = parentClassLoader;
	}

	/**
	 * Add a directory that contains the source of dynamic java code.
	 * 
	 * @param srcDirectory {@link File}
	 * @return true if the add is successful
	 */
	public boolean addSourceDir(final File srcDirectory) {
		File srcDir = srcDirectory;
		boolean isSourceDirAdded = true;
		try {
			srcDir = srcDir.getCanonicalFile();
		} catch (final IOException e) {
			LOGGER.error(e.getMessage());
		}

		synchronized (sourceDirectories) {

			// check existence
			for (int i = 0; i < sourceDirectories.size(); i++) {
				final SourceDirectory src = (SourceDirectory) sourceDirectories.get(i);
				if (src.srcDir.equals(srcDir)) {
					isSourceDirAdded = false;
				}
			}

			// add new
			final SourceDirectory src = new SourceDirectory(srcDir);
			sourceDirectories.add(src);

			LOGGER.info("Add source dir " + srcDir);
		}

		return isSourceDirAdded;
	}

	/**
	 * Returns the up-to-date dynamic class by name.
	 * 
	 * @param className {@link String}
	 * @return Class<?>
	 * @throws ClassNotFoundException if source file not found or compilation error
	 * @throws DCMAApplicationException in case of error
	 */
	public Class<?> loadClass(final String className) throws DCMAApplicationException, ClassNotFoundException {

		Class<?> returnVal;
		boolean isReturn = false;
		LoadedClass loadedClass = null;
		synchronized (loadedClasses) {
			loadedClass = (LoadedClass) loadedClasses.get(className);
		}

		// first access of a class
		if (loadedClass == null) {

			final String resource = className.replace(ScriptConstants.FULL_STOP, ScriptConstants.SLASH) + ScriptConstants.DOT_JAVA;
			final SourceDirectory src = locateResource(resource);
			if (src == null) {
				throw new ClassNotFoundException("DynamicCodeCompiler class not found " + className);
			}

			synchronized (this) {

				// compile and load class
				loadedClass = new LoadedClass(className, src);

				synchronized (loadedClasses) {
					loadedClasses.put(className, loadedClass);
				}
			}

			returnVal = loadedClass.clazz;
			isReturn = true;
		}

		// subsequent access
		if (loadedClass.isChanged() && !isReturn) {
			// unload and load again
			unload(loadedClass.srcDir);
			returnVal = loadClass(className);
		} else {
			returnVal = loadedClass.clazz;
		}
		return returnVal;
	}

	private SourceDirectory locateResource(final String resource) {
		SourceDirectory src = null;
		for (int i = 0; i < sourceDirectories.size(); i++) {
			src = (SourceDirectory) sourceDirectories.get(i);
			if (new File(src.srcDir, resource).exists()) {
				break;
			}
		}
		return src;
	}

	private void unload(final SourceDirectory src) {
		// clear loaded classes
		synchronized (loadedClasses) {
			for (final Iterator<LoadedClass> iter = loadedClasses.values().iterator(); iter.hasNext();) {
				final LoadedClass loadedClass = iter.next();
				if (loadedClass.srcDir == src) {
					iter.remove();
				}
			}
		}

		// create new class loader
		src.recreateClassLoader();
	}

	/**
	 * Get a resource from added source directories.
	 * 
	 * @param resource {@link String}
	 * @return URL
	 */
	@SuppressWarnings("deprecation")
	public URL getResource(final String resource) {
		URL returnValue = null;
		try {

			final SourceDirectory src = locateResource(resource);
			final URL url = new File(src.srcDir, resource).toURL();
			returnValue = src == null ? null : url;

		} catch (final MalformedURLException e) {
			LOGGER.error(e.getMessage(), e);
		}
		return returnValue;
	}

	/**
	 * Get a resource stream from added source directories.
	 * 
	 * @param resource {@link String}
	 * @return InputStream
	 */
	public InputStream getResourceAsStream(final String resource) {
		InputStream returnValue = null;
		try {

			final SourceDirectory src = locateResource(resource);
			returnValue = src == null ? null : new FileInputStream(new File(src.srcDir, resource));

		} catch (final FileNotFoundException e) {
			LOGGER.error(e.getMessage(), e);
		}
		return returnValue;
	}

	/**
	 * Create a proxy instance that implements the specified access interface and delegates incoming invocations to the specified
	 * dynamic implementation. The dynamic implementation may change at run-time, and the proxy will always delegates to the up-to-date
	 * implementation.
	 * 
	 * @param interfaceClass {@link Class<?>} the access interface
	 * @param implClassName {@link String} the back end dynamic implementation
	 * @return Object
	 * @throws RuntimeException if an instance cannot be created, because of class not found for example
	 * @throws DCMAApplicationException in case of error
	 */
	public Object newProxyInstance(final Class<?> interfaceClass, final String implClassName) throws DCMAApplicationException,
			RuntimeException {
		final MyInvocationHandler handler = new MyInvocationHandler(implClassName);
		return Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[] {interfaceClass}, handler);
	}

	/**
	 * Class for source directory.
	 * 
	 * @author Ephesoft
	 * @version 1.0
	 */
	private class SourceDirectory {

		/**
		 * To store the source directory.
		 */
		final private File srcDir;

		/**
		 * To store the bin directory.
		 */
		final private File binDir;

		/**
		 * JavaCompiler type variable.
		 */
		private final JavaCompiler javaCompiler;

		/**
		 * A URLClassLoader type variable.
		 */
		private URLClassLoader urlClassLoader;

		/**
		 * Constructor.
		 * 
		 * @param srcDir File
		 */
		SourceDirectory(final File srcDir) {
			this.srcDir = srcDir;

			final String subdir = srcDir.getAbsolutePath().replace(ScriptConstants.COLON, ScriptConstants.UNDERSCORE).replace(
					ScriptConstants.SLASH, ScriptConstants.UNDERSCORE).replace(ScriptConstants.DOUBLE_SLASH,
					ScriptConstants.UNDERSCORE);
			this.binDir = new File(System.getProperty("java.io.tmpdir"), "DynamicCodeCompiler/" + subdir);
			this.binDir.mkdirs();

			// prepare compiler
			this.javaCompiler = new JavaCompiler(compileClasspath, binDir.getAbsolutePath());

			// class loader
			recreateClassLoader();
		}

		@SuppressWarnings("deprecation")
		final private void recreateClassLoader() {
			try {
				urlClassLoader = new URLClassLoader(new URL[] {binDir.toURL()}, parentClassLoader);
			} catch (final MalformedURLException e) {
				// should not happen
				LOGGER.error(e.getMessage(), e);
			}
		}

	}

	/**
	 * Class for loading.
	 * 
	 * @author Ephesoft
	 * @version 1.0
	 */
	private static class LoadedClass {

		/**
		 * To store the class name.
		 */
		final private String className;

		/**
		 * To store the source directory.
		 */
		final private SourceDirectory srcDir;

		/**
		 * File type variable to store source file.
		 */
		final private File srcFile;
		
		/**
		 * File type variable to store bin file.
		 */
		File binFile;

		/**
		 * clazz Class<?>.
		 */
		private Class<?> clazz;

		/**
		 * To store the value regarding last modification.
		 */
		private long lastModified;

		/**
		 * Constructor.
		 * 
		 * @param className {@link String}
		 * @param src {@link SourceDirectory}
		 * @throws DCMAApplicationException in case of error
		 */
		LoadedClass(final String className, final SourceDirectory src) throws DCMAApplicationException {
			this.className = className;
			this.srcDir = src;

			final String path = className.replace(ScriptConstants.FULL_STOP, ScriptConstants.SLASH);
			this.srcFile = new File(src.srcDir, path + ScriptConstants.DOT_JAVA);
			this.binFile = new File(src.binDir, path + ScriptConstants.DOT_CLASS);

			compileAndLoadClass();
		}

		private boolean isChanged() {
			return srcFile.lastModified() != lastModified;
		}

		private final void compileAndLoadClass() throws DCMAApplicationException {

			if (clazz != null) {
				return; // class already loaded
			}

			// compile, if required
			String error = null;
			if (binFile.lastModified() < srcFile.lastModified()) {
				if(srcFile.exists()) {
					error = srcDir.javaCompiler.compile(new File[] {srcFile});
				}
			}

			if (error != null) {
				throw new DCMAApplicationException("Failed to compile " + srcFile.getAbsolutePath() + ". Error: " + error);
			}

			try {
				// load class
				clazz = srcDir.urlClassLoader.loadClass(className);

				// load class success, remember time stamp
				lastModified = srcFile.lastModified();

			} catch (final ClassNotFoundException e) {
				throw new DCMABusinessException("Failed to load DynaCode class " + srcFile.getAbsolutePath(), e);
			}

			LOGGER.info("Init " + clazz);
		}
	}

	/**
	 * Class to handle invocation.
	 * 
	 * @author Ephesoft
	 * @version 1.0
	 */
	private class MyInvocationHandler implements InvocationHandler {

		/**
		 * backendClassName String.
		 */
		final private String backendClassName;

		/**
		 * back end Object.
		 */
		private Object backend;

		/**
		 * Constructor.
		 * 
		 * @param className String
		 * @throws DCMAApplicationException in case of error
		 */
		MyInvocationHandler(final String className) throws DCMAApplicationException {
			backendClassName = className;

			try {
				final Class<?> clz = loadClass(backendClassName);
				backend = newDynaCodeInstance(clz);

			} catch (final ClassNotFoundException e) {
				throw new DCMABusinessException(e);
			}
		}

		/**
		 * Invoke method.
		 * 
		 * @param proxy Object
		 * @param method Method
		 * @param args Object[]
		 * @throws ClassNotFoundException
		 * @throws IllegalArgumentException
		 * @throws IllegalAccessException
		 * @throws InvocationTargetException
		 * @throws DCMAApplicationException
		 */
		public Object invoke(final Object proxy, final Method method, final Object[] args) throws ClassNotFoundException,
				IllegalArgumentException, IllegalAccessException, InvocationTargetException, DCMAApplicationException {

			// check if class has been updated
			final Class<?> clz = loadClass(backendClassName);
			if (backend.getClass() != clz) {
				backend = newDynaCodeInstance(clz);
			}

			// invoke on back end
			return method.invoke(backend, args);
		}

		private Object newDynaCodeInstance(final Class<?> clz) {
			try {
				return clz.newInstance();
			} catch (final Exception e) {
				throw new DCMABusinessException("Failed to new instance of Dynamic code class " + clz.getName(), e);
			}
		}

	}

	private static String extractClasspath(final ClassLoader clazzParam) {
		ClassLoader clazz = clazzParam;
		final StringBuffer buf = new StringBuffer();

		while (clazz != null) {
			if (clazz instanceof URLClassLoader) {
				final URL urls[] = ((URLClassLoader) clazz).getURLs();
				for (int i = 0; i < urls.length; i++) {
					if (buf.length() > 0) {
						buf.append(File.pathSeparatorChar);
					}
					buf.append(urls[i].getFile().toString());
				}
			}
			clazz = clazz.getParent();
		}

		return buf.toString();
	}

}
