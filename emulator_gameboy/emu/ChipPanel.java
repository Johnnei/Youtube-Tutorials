package emu;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

import chip.Z80;

public class ChipPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	private Z80 chip;
	
	public ChipPanel(Z80 chip) {
		this.chip = chip;
	}
	
	public void paint(Graphics g) {
		final int SCALE = 2;
		final Color[] COLORS = { new Color(0x00, 0x00, 0x00), new Color(0x66, 0x66, 0x66), new Color(0xBB, 0xBB, 0xBB), new Color(0xFF, 0xFF, 0xFF) };
		byte[] display = chip.getDisplay();
		for(int i = 0; i < display.length; i++) {
			g.setColor(COLORS[display[i]]);
			
			int x = (i % 144);
			int y = (int)Math.floor(i / 144);
			
			g.fillRect(x * SCALE, y * SCALE, SCALE, SCALE);
		}
	}

}
