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

	private String compileClasspath;

	private ClassLoader parentClassLoader;

	final private List<SourceDirectory> sourceDirectories = new ArrayList<SourceDirectory>();

	// class name => LoadedClass
	final private Map<String, LoadedClass> loadedClasses = new HashMap<String, LoadedClass>();

	/**
	 * 
	 */
	public DynamicCodeCompiler() {
		this(Thread.currentThread().getContextClassLoader());
	}

	/**
	 * @param parentClassLoader ClassLoader
	 */
	public DynamicCodeCompiler(final ClassLoader parentClassLoader) {
		this(extractClasspath(parentClassLoader), parentClassLoader);

	}

	/**
	 * @param compileClasspath used to compile dynamic classes
	 * @param parentClassLoader the parent of the class loader that loads all the dynamic classes
	 */
	public DynamicCodeCompiler(final String compileClasspath, final ClassLoader parentClassLoader) {
		this.compileClasspath = compileClasspath;
		this.parentClassLoader = parentClassLoader;
	}

	/**
	 * Add a directory that contains the source of dynamic java code.
	 * 
	 * @param srcDir
	 * @return true if the add is successful
	 */
	public boolean addSourceDir(final File srcDirectory) {
		File srcDir = srcDirectory;
		boolean isSourceDirAdded = true;
		try {
			srcDir = srcDir.getCanonicalFile();
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
		}

		synchronized (sourceDirectories) {

			// check existence
			for (int i = 0; i < sourceDirectories.size(); i++) {
				final SourceDirectory src = (SourceDirectory) sourceDirectories.get(i);
				if (src.srcDir.equals(srcDir)) {
					isSourceDirAdded =  false;
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
	 * @param className
	 * @return
	 * @throws ClassNotFoundException if source file not found or compilation error
	 */
	public Class<?> loadClass(final String className) throws ClassNotFoundException {

		LoadedClass loadedClass = null;
		synchronized (loadedClasses) {
			loadedClass = (LoadedClass) loadedClasses.get(className);
		}

		// first access of a class
		if (loadedClass == null) {

			final String resource = className.replace('.', '/') + ".java";
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

			return loadedClass.clazz;
		}

		// subsequent access
		if (loadedClass.isChanged()) {
			// unload and load again
			unload(loadedClass.srcDir);
			return loadClass(className);
		}

		return loadedClass.clazz;
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

	private void unload(SourceDirectory src) {
		// clear loaded classes
		synchronized (loadedClasses) {
			for (Iterator<LoadedClass> iter = loadedClasses.values().iterator(); iter.hasNext();) {
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
	 * @param resource
	 * @return the resource URL, or null if resource not found
	 */
	@SuppressWarnings("deprecation")
	public URL getResource(final String resource) {
		try {

			final SourceDirectory src = locateResource(resource);
			final URL url = new File(src.srcDir, resource).toURL();
			return src == null ? null : url;

		} catch (MalformedURLException e) {
			LOGGER.error(e.getMessage(), e);
			// should not happen
			return null;
		}
	}

	/**
	 * Get a resource stream from added source directories.
	 * 
	 * @param resource
	 * @return the resource stream, or null if resource not found
	 */
	public InputStream getResourceAsStream(String resource) {
		try {

			SourceDirectory src = locateResource(resource);
			return src == null ? null : new FileInputStream(new File(src.srcDir, resource));

		} catch (FileNotFoundException e) {
			// should not happen
			LOGGER.error(e.getMessage(), e);
			return null;
		}
	}

	/**
	 * Create a proxy instance that implements the specified access interface and delegates incoming invocations to the specified
	 * dynamic implementation. The dynamic implementation may change at run-time, and the proxy will always delegates to the up-to-date
	 * implementation.
	 * 
	 * @param interfaceClass the access interface
	 * @param implClassName the backend dynamic implementation
	 * @return
	 * @throws RuntimeException if an instance cannot be created, because of class not found for example
	 */
	public Object newProxyInstance(Class<?> interfaceClass, String implClassName) throws RuntimeException {
		MyInvocationHandler handler = new MyInvocationHandler(implClassName);
		return Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[] {interfaceClass}, handler);
	}

	private class SourceDirectory {

		final private File srcDir;

		final private File binDir;

		final private JavaCompiler javaCompiler;

		private URLClassLoader urlClassLoader;

		SourceDirectory(File srcDir) {
			this.srcDir = srcDir;

			String subdir = srcDir.getAbsolutePath().replace(':', '_').replace('/', '_').replace('\\', '_');
			this.binDir = new File(System.getProperty("java.io.tmpdir"), "DynamicCodeCompiler/" + subdir);
			this.binDir.mkdirs();

			// prepare compiler
			this.javaCompiler = new JavaCompiler(compileClasspath, binDir.getAbsolutePath());

			// class loader
			recreateClassLoader();
		}

		@SuppressWarnings("deprecation")
		final void recreateClassLoader() {
			try {
				urlClassLoader = new URLClassLoader(new URL[] {binDir.toURL()}, parentClassLoader);
			} catch (MalformedURLException e) {
				// should not happen
				LOGGER.error(e.getMessage(), e);
			}
		}

	}

	private static class LoadedClass {

		final private String className;

		final private SourceDirectory srcDir;

		File srcFile;

	//	File binFile;

		Class<?> clazz;

		long lastModified;

		LoadedClass(String className, SourceDirectory src) {
			this.className = className;
			this.srcDir = src;

			String path = className.replace('.', '/');
			this.srcFile = new File(src.srcDir, path + ".java");
//			this.binFile = new File(src.binDir, path + ".class");

			compileAndLoadClass();
		}

		boolean isChanged() {
			return srcFile.lastModified() != lastModified;
		}

		final void compileAndLoadClass() {

			if (clazz != null) {
				return; // class already loaded
			}

			// compile, if required
			String error = null;
//			if (binFile.lastModified() < srcFile.lastModified()) {
				error = srcDir.javaCompiler.compile(new File[] {srcFile});
//			}

			if (error != null) {
				throw new RuntimeException("Failed to compile " + srcFile.getAbsolutePath() + ". Error: " + error);
			}

			try {
				// load class
				clazz = srcDir.urlClassLoader.loadClass(className);

				// load class success, remember time stamp
				lastModified = srcFile.lastModified();

			} catch (ClassNotFoundException e) {
				throw new RuntimeException("Failed to load DynaCode class " + srcFile.getAbsolutePath(), e);
			}

			LOGGER.info("Init " + clazz);
		}
	}

	private class MyInvocationHandler implements InvocationHandler {

		String backendClassName;

		Object backend;

		MyInvocationHandler(String className) {
			backendClassName = className;

			try {
				Class<?> clz = loadClass(backendClassName);
				backend = newDynaCodeInstance(clz);

			} catch (ClassNotFoundException e) {
				throw new RuntimeException(e);
			}
		}

		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

			// check if class has been updated
			Class<?> clz = loadClass(backendClassName);
			if (backend.getClass() != clz) {
				backend = newDynaCodeInstance(clz);
			}

			try {
				// invoke on backend
				return method.invoke(backend, args);

			} catch (InvocationTargetException e) {
				throw e.getTargetException();
			}
		}

		private Object newDynaCodeInstance(Class<?> clz) {
			try {
				return clz.newInstance();
			} catch (Exception e) {
				throw new RuntimeException("Failed to new instance of Dynamic code class " + clz.getName(), e);
			}
		}

	}

	/**
	 * Extracts a classpath string from a given class loader. Recognizes only URLClassLoader.
	 */
	private static String extractClasspath(final ClassLoader clazzParam) { 
		ClassLoader clazz = clazzParam;
		StringBuffer buf = new StringBuffer();

		while (clazz != null) {
			if (clazz instanceof URLClassLoader) {
				URL urls[] = ((URLClassLoader) clazz).getURLs();
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
