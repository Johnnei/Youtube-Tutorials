package chip;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Z80 {

	// FINAL data
	public static final int REGISTER_A = 0;
	public static final int REGISTER_B = 1;
	public static final int REGISTER_C = 2;
	public static final int REGISTER_D = 3;
	public static final int REGISTER_E = 4;
	public static final int REGISTER_H = 5;
	public static final int REGISTER_L = 6;
	public static final int REGISTER_F = 7;
	// END FINAL

	/**
	 * The 144x160 pixel 4 colors display
	 */
	private byte[] display;
	/**
	 * 64K Memory<br/>
	 * 0000-3FFF 16KB ROM Bank 00 (in cartridge, fixed at bank 00)<br/>
	 * 4000-7FFF 16KB ROM Bank 01..NN (in cartridge, switchable bank number)<br/>
	 * 8000-9FFF 8KB Video RAM (VRAM) (switchable bank 0-1 in CGB Mode)<br/>
	 * A000-BFFF 8KB External RAM (in cartridge, switchable bank, if any)<br/>
	 * C000-CFFF 4KB Work RAM Bank 0 (WRAM)<br/>
	 * D000-DFFF 4KB Work RAM Bank 1 (WRAM) (switchable bank 1-7 in CGB Mode)<br/>
	 * E000-FDFF Same as C000-DDFF (ECHO) (typically not used)<br/>
	 * FE00-FE9F Sprite Attribute Table (OAM)<br/>
	 * FEA0-FEFF Not Usable<br/>
	 * FF00-FF7F I/O Ports<br/>
	 * FF80-FFFE High RAM (HRAM)<br/>
	 * FFFF Interrupt Enable Register<br/>
	 */
	private char[] memory;
	/**
	 * 8 8-bit Registers<br/>
	 * They can be paired up to 16-bit registers<br/>
	 * Left 8-bit registers can be: A, B, D and H<br/>
	 * Register F is the "Flag Register"<br/>
	 * The flag register contains multiple flags<br/>
	 * Bit 7: Zero flag (Z)<br/>
	 * Bit 6: Subtract Flag (N)<br/>
	 * Bit 5: Half Carry Flag (H)<br/>
	 * Bit 4: Carry Flag (C)<br/>
	 */
	private char[] registers;
	/**
	 * The StackPointer
	 */
	private char sp;
	/**
	 * The ProgramCounter
	 */
	private char pc;

	private boolean needRedraw;

	/**
	 * Resets the Z80 processor
	 */
	public void reset() {
		display = new byte[23040];
		memory = new char[65535];
		registers = new char[8];
		pc = 0x0;
		sp = 0xFFFE;
	}

	/**
	 * Processes a single opcode and returns the amount of "cycles" it took to
	 * process it
	 * 
	 * @return The amount of cycles this operation took
	 */
	public int run() {
		// TODO Fetch opcode
		// TODO Parse opcode
		return 4; // A "NOP" Instruction takes 4 cycles, a NOP does nothing so
					// we will default it to 4 cycles
	}

	/**
	 * Returns the display data
	 * 
	 * @return Current state of the 64x32 display
	 */
	public byte[] getDisplay() {
		return display;
	}

	/**
	 * Checks if there is a redraw needed
	 * 
	 * @return If a redraw is needed
	 */
	public boolean needsRedraw() {
		return needRedraw;
	}

	/**
	 * Notify the chip that is has been redrawn
	 */
	public void removeDrawFlag() {
		needRedraw = false;
	}

	/**
	 * Sets the key data to the memory<br/>
	 * The keys are stored in a single byte as followed:<br/>
	 * Bit 7 - Not used<br/>
	 * Bit 6 - Not used<br/>
	 * Bit 5 - P15 Select Button Keys (0=Select)<br/>
	 * Bit 4 - P14 Select Direction Keys (0=Select)<br/>
	 * Bit 3 - P13 Input Down or Start (0=Pressed) (Read Only)<br/>
	 * Bit 2 - P12 Input Up or Select (0=Pressed) (Read Only)<br/>
	 * Bit 1 - P11 Input Left or Button B (0=Pressed) (Read Only)<br/>
	 * Bit 0 - P10 Input Right or Button A (0=Pressed) (Read Only)<br/>
	 * 
	 * @param b The key states
	 */
	public void setKeys(byte b) {
		memory[0xFF00] = (char) b;
	}

	/**
	 * Loads the cartridge into the memory
	 * 
	 * @param file
	 *            The location of the cartrigde
	 */
	public void loadCartridge(String file) {
		DataInputStream input = null;
		try {
			input = new DataInputStream(new FileInputStream(new File(file)));

			int offset = 0;
			while (input.available() > 0) {
				memory[offset] = (char) (input.readByte() & 0xFF);
				offset++;
			}

		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException ex) {
				}
			}
		}
	}
}
