package chip;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import chip.opcode.IOpcode;
import chip.opcode.Opcode00NOP;

public class Z80 {

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
	private char[] register;
	/**
	 * The StackPointer
	 */
	private char sp;
	/**
	 * The ProgramCounter<br/>
	 * The PC is being initiated on startup to the position: 0x100<br/>
	 * At that location is commenly a jump and a nop
	 */
	private char pc;

	private boolean needRedraw;

	/**
	 * Resets the Z80 processor
	 */
	public void reset() {
		display = new byte[23040];
		memory = new char[65535];
		register = new char[8];
		pc = 0x100;
		sp = 0xFFFE;
	}

	/**
	 * Processes a single opcode and returns the amount of "cycles" it took to process it
	 * 
	 * @return The amount of cycles this operation took
	 */
	public int run() {
		IOpcode opcode = getOpcode();
		opcode.execute(this);
		return opcode.getCycleCount();
	}

	private IOpcode getOpcode() {
		switch (memory[pc]) {
			case 0x00:
				return new Opcode00NOP();
				
				default:
					System.out.println("Unprocessed opcode " + Integer.toHexString(memory[pc]).toUpperCase() + ", Processing as NOP");
					return new Opcode00NOP();
		}
	}

	/**
	 * Merges two registers into the 16-bit value
	 * 
	 * @param leftRegister The index of the left byte (upper 8 bits) register (A, B, D or H)
	 * @param rightRegister The index of the right byte (lower 8 bits) register
	 * @return The 16 bit register value
	 */
	public int registerPair(int leftRegister, int rightRegister) {
		return (register[leftRegister] << 8) | register[rightRegister];
	}

	/**
	 * The possible conditions:<br/>
	 * cc = NZ, if Z flag is reset.<br/>
	 * cc = Z, if Z flag is set.<br/>
	 * cc = NC, if C flag is reset.<br/>
	 * cc = C, if C flag is set.
	 * 
	 * @param cc
	 * @return
	 */
	public boolean passesCondition(Condition cc) {
		switch (cc) {
			case NZ:
				return (register[Register.F.index] >>> Flag.Zero.bit) == 0;
			case Z:
				return (register[Register.F.index] >>> Flag.Zero.bit) == 1;
			case NC:
				return (register[Register.F.index] >>> Flag.Carry.bit) == 0;
			case C:
				return (register[Register.F.index] >>> Flag.Carry.bit) == 1;
				
				default:
					throw new RuntimeException("This Exception won't be thrown as all Condition are being checked");
		}
	}
	
	/**
	 * Gets the stack pointer
	 * @return
	 */
	public char getStackPointer() {
		return sp;
	}
	
	/**
	 * Gets the program counter
	 * @return
	 */
	public char getProgramCounter() {
		return pc;
	}
	
	/**
	 * Read data from the memory
	 * @param address
	 * @return
	 */
	public char readMemory(int address) {
		return memory[address];
	}
	
	/**
	 * Set the stackpointer
	 * @param sp
	 */
	public void setStackPointer(char sp) {
		this.sp = sp;
	}
	
	/**
	 * Set the program counter
	 * @param pc
	 */
	public void setProgramCounter(char pc) {
		this.pc = pc;
	}
	
	/**
	 * Write data to the memory
	 * @param address
	 * @param value
	 */
	public void writeMemory(int address, char value) {
		memory[address] = value;
	}
	
	public void incrementStackPointer() {
		++sp;
	}
	
	public void decrementStackPointer() {
		--sp;
	}
	
	public void incrementProgramCounter() {
		++pc;
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
	 * @param file The location of the cartrigde
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
