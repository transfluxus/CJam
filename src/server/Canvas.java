package server;

import java.io.File;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.URL;
import java.net.URLClassLoader;

import javax.swing.JFrame;

import processing.core.PApplet;

public class Canvas extends JFrame {

	MainCanvasAdd ap;

	public Canvas() {
		setSize(800, 800);
		setVisible(true);
		updateMCA();
		CJamServer.MCRunning = true;
	}

	public void updateMCA() {
		if (ap != null)
			getContentPane().remove(ap);
		ap = loadnewAp();
		ap.init();
		getContentPane().add(ap);
		if (!isVisible())
			setVisible(true);
		toFront();
	}

	private MainCanvasAdd loadnewAp() {
		File classesDir = new File(CJamServer.mainPath + "bin/server/");
		// The parent classloader
		ClassLoader parentLoader = PApplet.class.getClassLoader();
		URLClassLoader loader1;
		try {
			// System.out.println(classesDir.toURI().toURL());
			loader1 = new URLClassLoader(
					new URL[] { classesDir.toURI().toURL() }, parentLoader);
			Class cls1 = loader1.loadClass("server.MainCanvasAdd");

			// InvocationHandler handler = new InvocationHandler() {
			// @Override
			// public Object invoke(Object proxy, Method method, Object[] args)
			// throws Throwable {
			//
			// // Get an instance of the up-to-date dynamic class
			// Object dynacode = getUpToDateInstance();
			//
			// // Forward the invocation
			// return method.invoke(dynacode, args);
			// }
			// };
			// MainCanvasAdd MainCanvasAdd = (MainCanvasAdd) Proxy
			// .newProxyInstance(MainCanvasAdd.class.getClassLoader(),
			// new Class[] { MainCanvasAdd.class }, handler);

			MainCanvasAdd mcA = (MainCanvasAdd) cls1.newInstance();
			return mcA;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}