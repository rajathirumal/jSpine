package com.spine.core.runntime;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLClassLoader;

import com.spine.common.classLoader.SpineClassLoader;
import com.spine.common.configuration.SpineConfiguration;

public class SpineRun {
	private URLClassLoader sessionWithClasses = null;
	private String defaultPackageName = null;

	/**
	 * Initializes the application by setting the default package name and creating
	 * a URLClassLoader to load classes from JARs based on configuration.
	 */
	private void init() {
		this.defaultPackageName = SpineConfiguration.DEFAULT_PACKAGE;
		SpineClassLoader afClassLoader = new SpineClassLoader();
		this.sessionWithClasses = afClassLoader.loadClass();
	}

	/**
	 * Executes the flow by loading the worker class based on its name in the first
	 * argument, finding the "processor" method using reflection, and invoking it on
	 * a new instance of the class.
	 *
	 * @param args Command-line arguments passed to the application. The first
	 *             argument is expected to be the name of the worker class.
	 */
	private void run(String[] args) {
		// Was the program name passed in the argument ?
		if (args.length < 1) {
			System.err.println("No program specified for runnung");
		}
		String workerName = args[0];
		String fullyQualifiedWorkerName = this.defaultPackageName + "." + workerName;
		Method processMethod = null;

		try {
			// Load worker class using reflection
			Class<?> workerClass = this.sessionWithClasses.loadClass(fullyQualifiedWorkerName);
			// Find "processor" method
			processMethod = workerClass.getDeclaredMethod("processor");
			// Get constructor and make it accessible
			Constructor<?> constructor = workerClass.getDeclaredConstructor();
			constructor.setAccessible(true);
			// Create an instance of the worker class
			Object instance = constructor.newInstance();
			// Invoke the "processor" method on the instance
			processMethod.invoke(instance);
			System.out.println("Execution successful");
		} catch (ClassNotFoundException classNotFoundException) {
			System.err.println("Worker class not found: " + classNotFoundException.getMessage());
		} catch (NoSuchMethodException noSuchMethodException) {
			System.err.println("Processor method not found: " + noSuchMethodException.getMessage());
		} catch (SecurityException securityException) {
			System.err.println("Security exception: " + securityException.getMessage());
		} catch (IllegalAccessException illegalAccessException) {
			System.err.println("Illegal access exception: " + illegalAccessException.getMessage());
		} catch (InvocationTargetException invocationTargetException) {
			invocationTargetException.printStackTrace();
			System.err.println("Invocation target exception: " + invocationTargetException.getMessage());
		} catch (InstantiationException instantiationException) {
			System.err.println("Instantiation exception: " + instantiationException.getMessage());
		} catch (IllegalArgumentException illegalArgumentException) {
			System.err.println("Illegal argument exception: " + illegalArgumentException.getMessage());
		}

	}

	/**
	 * Main entry point for the application. This method takes command-line
	 * arguments, loads the worker class using reflection, and executes its
	 * "processor" method.
	 *
	 * @param args Command-line arguments passed to the application.
	 */
	public static void main(String[] args) {
		SpineRun afRun = new SpineRun();
		afRun.init();
		afRun.run(args);
	}
}