package br.dev.model;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.BitSet;

import javax.swing.JTextField;

public class KeyListenerLimit implements KeyListener {
	JTextField component;
	int predSize = 0;
	int nextPress = 1;
	boolean complete = false;

	public KeyListenerLimit(int size, JTextField next){
		component = next;
		predSize = size;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		if(nextPress == predSize){
			complete = true;
		}

	}

	@Override
	public void keyPressed(KeyEvent e) {

	}

	@Override
	public void keyReleased(KeyEvent e) {
		JTextField te =  (JTextField) e.getComponent();
		nextPress = te.getDocument().getLength() + 1;

		if(complete){
			component.requestFocus();
			complete = false;
			nextPress = 1;
		}
	}
}
