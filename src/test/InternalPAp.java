package test;

import javax.swing.JFrame;

import processing.core.PApplet;

public class InternalPAp extends JFrame {

	PApplet ap;

	public InternalPAp() {
		setSize(600, 800);
		ap = new TAp(300, 300, this);
		getContentPane().add(ap);
		setVisible(true);
		ap.init();
		((TAp) ap).b = 0;
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		try {
			Thread.sleep(2000);
			removeAndNew();
		} catch (Exception exc) {

		}
	}

	public void removeAndNew() {
		getContentPane().remove(ap);
		ap = new TAp(300, 300, this);
		ap.init();
		getContentPane().add(ap);
		((TAp) ap).b = 200;
	}

	public static void main(String[] args) {
		new InternalPAp();
	}

}
