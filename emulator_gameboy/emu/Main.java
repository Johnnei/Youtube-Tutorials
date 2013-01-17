package emu;

import chip.Z80;

public class Main extends Thread {
	
	private Z80 z80;
	private ChipFrame frame;
	
	public Main() {
		z80 = new Z80();
		z80.reset();
		z80.loadCartridge("./tetris.gb");
		frame = new ChipFrame(z80);
	}
	
	public void run() {
		//4194304 Hz (roughly 4.2 mhz), 1 hz is about 23.84 nanoseconds worth of sleep
		while(true) {
			long startTime = System.nanoTime();
			int cyclesUsed = z80.run();
			long usedTime = System.nanoTime() - startTime;
			if(z80.needsRedraw()) {
				frame.repaint();
				z80.removeDrawFlag();
			}
			try {
				double nanosSleep = (cyclesUsed * 23.84D) - usedTime;
				if(nanosSleep > 0D) {
					Thread.sleep(0, (int)nanosSleep);
				}
			} catch (InterruptedException e) {
				//Unthrown exception
			}
		}
	}
	
	public static void main(String[] args) {
		Main main = new Main();
		main.start();
	}

}
