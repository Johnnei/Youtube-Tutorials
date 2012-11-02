package emu;

import chip.Chip;

public class Main {
	
	public static void main(String[] args) {
		Chip c = new Chip();
		c.init();
		//c.run();
		ChipFrame frame = new ChipFrame(c);
	}

}
