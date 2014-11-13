package server;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

import sun.awt.WindowClosingListener;

public class Canvas extends JFrame implements WindowListener {

	MainCanvasAdd ap;

	public Canvas() {
		setSize(800, 800);
		// setUndecorated(true);
		setVisible(true);
		updateMCA();
		CJamServer.MCRunning = true;
		addWindowListener(this);
	}

	public void updateMCA() {
		if (ap != null)
			getContentPane().remove(ap);
		ap = (MainCanvasAdd) Compiler.getAp();// loadnewAp();
		ap.init();
		getContentPane().add(ap);
		if (!isVisible())
			setVisible(true);
		toFront();
	}

	@Override
	public void windowOpened(WindowEvent e) {
	}

	@Override
	public void windowClosing(WindowEvent e) {
		CJamServer.MCRunning = false;
	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}

}