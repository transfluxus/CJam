package server;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import processing.core.PApplet;

public class Compiler {

	private final DiagnosticCollector<JavaFileObject> diagnostics;
	private final JavaCompiler compiler;
	private final StandardJavaFileManager fileManager;
	private final List<String> optionList;

	public Compiler() {
		diagnostics = new DiagnosticCollector<JavaFileObject>();
		compiler = ToolProvider.getSystemJavaCompiler();
		fileManager = compiler.getStandardFileManager(diagnostics, null, null);

		// This sets up the class path that the compiler will use.
		// I've added the .jar file that contains the DoStuff interface within
		// in it...
		optionList = new ArrayList<String>();
		optionList.add("-d");
		optionList.add(CJamServer.mainPath + "bin");
	}

	public boolean compile(List<File> files) {
		Iterable<? extends JavaFileObject> compilationUnit = fileManager
				.getJavaFileObjectsFromFiles(files);
		JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager,
				diagnostics, optionList, null, compilationUnit);
		boolean success = task.call();
		for (Diagnostic<?> diagnostic : diagnostics.getDiagnostics())
			System.out.format("Error on line %d in %s%n",
					diagnostic.getLineNumber(), diagnostic.getSource());
		try {
			fileManager.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return success;
	}

	public static Object getAp() {
		File classesDir = new File(CJamServer.mainPath + "bin/server/");
		// The parent classloader
		ClassLoader parentLoader = PApplet.class.getClassLoader();
		URLClassLoader loader;
		try {
			// System.out.println(classesDir.toURI().toURL());
			loader = new URLClassLoader(
					new URL[] { classesDir.toURI().toURL() }, parentLoader);
			Class<?> clazz = loader.loadClass("server.MainCanvasAdd");
			loader.close();

			MainCanvasAdd mcA = (MainCanvasAdd) clazz.newInstance();
			return mcA;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

}
