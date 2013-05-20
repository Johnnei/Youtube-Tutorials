package emu;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

import chip.Z80;

public class ChipFrame extends JFrame implements KeyListener {
	
	private static final int KEY_LEFT = 37;
	private static final int KEY_UP = 38;
	private static final int KEY_RIGHT = 39;
	private static final int KEY_DOWN = 40;
	
	public static final int WIDTH = 160;
	public static final int HEIGHT = 144;
	
	private static final long serialVersionUID = 1L;
	private ChipPanel panel;
	private int[] keyBuffer;
	private int[] keyIdToKey;

	public ChipFrame(Z80 c) {
		setPreferredSize(new Dimension(WIDTH * ChipPanel.SCALE, HEIGHT * ChipPanel.SCALE));
		setResizable(false);
		pack();
		setPreferredSize(new Dimension(WIDTH * ChipPanel.SCALE + getInsets().left + getInsets().right, HEIGHT * ChipPanel.SCALE + getInsets().top + getInsets().bottom));
		panel = new ChipPanel(c);
		setLayout(new BorderLayout());
		add(panel, BorderLayout.CENTER);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Gameboy Emulator");
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
		addKeyListener(this);
		
		keyIdToKey = new int[256];
		keyBuffer = new int[16];
		fillKeyIds();
	}
	
	private void fillKeyIds() {
		for(int i = 0; i < keyIdToKey.length; i++) {
			keyIdToKey[i] = -1;
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		System.out.println("KeyPressed: " + e.getKeyCode());
		if(keyIdToKey[e.getKeyCode()] != -1) {
			keyBuffer[keyIdToKey[e.getKeyCode()]] = 1;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(keyIdToKey[e.getKeyCode()] != -1) {
			keyBuffer[keyIdToKey[e.getKeyCode()]] = 0;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {	
	}
	
	public int[] getKeyBuffer() {
		return keyBuffer;
	}
	
}
