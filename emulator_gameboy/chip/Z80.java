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

	public static final int NZ = 0;
	public static final int Z = 1;
	public static final int NC = 2;
	public static final int C = 3;

	/**
	 * The bit in {@link #REGISTER_F} that represents if the last math operation result was: 0
	 */
	public static final int FLAG_ZERO = 7;
	/**
	 * The bit in {@link #REGISTER_F} that represents if the last math operation was a subtraction
	 */
	public static final int FLAG_SUBTRACT = 6;
	/**
	 * The bit in {@link #REGISTER_F} that represents if the last math operation caused a carry on the lower nibble (result >= 16)
	 */
	public static final int FLAG_HALF_CARRY = 5;
	/**
	 * The bit in {@link #REGISTER_F} that represents if the last math operation caused a carry (result > 255)<Br/>
	 * This bit is also set of {@link #REGISTER_A} is the smaller value during the CP Instruction
	 */
	public static final int FLAG_CARRY = 4;
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
		int opcode = memory[pc];
		switch (opcode) {
		// PUSH n
			case 0xF5: { // PUSH AF
				push((byte) register[REGISTER_A]);
				push((byte) register[REGISTER_F]);
				pc += 1;
				return 16;
			}

			case 0xC5: { // PUSH BC
				push((byte) register[REGISTER_B]);
				push((byte) register[REGISTER_C]);
				pc += 1;
				return 16;
			}

			case 0xD5: { // PUSH DE
				push((byte) register[REGISTER_D]);
				push((byte) register[REGISTER_E]);
				pc += 1;
				return 16;
			}

			case 0xE5: { // PUSH HL
				push((byte) register[REGISTER_H]);
				push((byte) register[REGISTER_L]);
				pc += 1;
				return 16;
			}

			// POP nn
			case 0xF1: { // POP AF
				register[REGISTER_F] = (char) pop();
				register[REGISTER_A] = (char) pop();
				pc += 1;
				return 12;
			}

			case 0xC1: { // POP BC
				register[REGISTER_C] = (char) pop();
				register[REGISTER_B] = (char) pop();
				pc += 1;
				return 12;
			}

			case 0xD1: { // POP DE
				register[REGISTER_E] = (char) pop();
				register[REGISTER_D] = (char) pop();
				pc += 1;
				return 12;
			}

			case 0xE1: { // POP HL
				register[REGISTER_L] = (char) pop();
				register[REGISTER_H] = (char) pop();
				pc += 1;
				return 12;
			}

			// CALL
			case 0xCD: { // CALL nn
				int nn = memory[pc + 1] | (memory[pc + 2] << 8);
				call(nn);
				return 12;
			}

			// JUMP
			case 0xC3: { // JMP nn
				int nn = memory[pc + 1] | (memory[pc + 2] << 8);
				jump(nn);
				return 12;
			}

			case 0xC2: { // JMP NZ, nn
				int nn = memory[pc + 1] | (memory[pc + 2] << 8);
				jump(NZ, nn);
				return 12;
			}

			case 0xCA: { // JMP Z, nn
				int nn = memory[pc + 1] | (memory[pc + 2] << 8);
				jump(Z, nn);
				return 12;
			}

			case 0xD2: { // JMP NC, nn
				int nn = memory[pc + 1] | (memory[pc + 2] << 8);
				jump(NC, nn);
				return 12;
			}

			case 0xDA: { // JMP C, nn
				int nn = memory[pc + 1] | (memory[pc + 2] << 8);
				jump(C, nn);
				return 12;
			}

			case 0xE9: { // JMP (HL)
				int nn = registerPair(REGISTER_H, REGISTER_L);
				jump(nn);
				return 4;
			}

			case 0x18: { // JR n
				byte n = (byte) memory[pc + 1];
				int nn = pc + n;
				jump(nn);
				return 8;
			}
			
			// LOAD
			case 0x7F: { //LD A, A
				load(REGISTER_A, REGISTER_A);
				return 4;
			}
			
			case 0x7E: { //LD A, HL 
				load(REGISTER_A, REGISTER_H, REGISTER_L);
				return 8;
			}
			
			// ADD
			case 0x80: {
				add(REGISTER_B);
				return 4;
			}
			
			case 0x81: {
				add(REGISTER_C);
				return 4;
			}
			case 0x87: { //ADD A,n
				add(REGISTER_A);
				return 4;
			}

			// RETURN
			case 0xC9: { // RET
				int nn = pop() | (pop() << 8);
				jump(nn);
				return 8;
			}

			default:
				return 4;
		}
	}
	
	/**
	 * Adds the value from <tt>register[r]</tt> to {@link #REGISTER_A} and stores it into {@link #REGISTER_A}<br/>
	 * {@link #FLAG_SUBTRACT} will be reset as we dont subtract<br/>
	 * {@link #FLAG_HALF_CARRY} Will be set if half carry occurs<br/>
	 * {@link #FLAG_CARRY} Will be set if full carry occurs<br/>
	 * {@link #FLAG_ZERO} Will be set if the result is zero
	 * @param r The register to add to {@link #REGISTER_A}
	 */
	private void add(int r) {
		register[REGISTER_A] += register[r];
		register[REGISTER_F] &= ~(1 << FLAG_SUBTRACT);
		if(register[REGISTER_A] == 0) {
			register[REGISTER_F] |= (1 << FLAG_ZERO);
		}
		if(register[REGISTER_A] > 15) {
			register[REGISTER_F] |= (1 << FLAG_HALF_CARRY);
		}
		if(register[REGISTER_A] > 255) {
			register[REGISTER_F] |= (1 << FLAG_CARRY);
		}
	}
	
	/**
	 * Puts the value into A
	 * @param value The value to put into a
	 */
	private void load(int value) {
		register[REGISTER_A] = (char)value;
	}
	
	/**
	 * Puts the value from combined register <tt>register[r2left << 8 | r2right]</tt> into <tt>register[r1]</tt>
	 * @param r1 The register to put the value into 
	 * @param r2left The register for the upper 8 bits of the value
	 * @param r2right The register for the lower 8 bits of the value
	 */
	private void load(int r1, int r2left, int r2right) {
		register[r1] = (char)registerPair(r2left, r2right);
	}
	
	/**
	 * Puts the value from <tt>register[r2]</tt> into <tt>register[r1]</tt>
	 * @param r1 The register to put the value into
	 * @param r2 The register to read the value from
	 */
	private void load(int r1, int r2) {
		register[r1] = register[r2];
	}

	/**
	 * Merges two registers into the 16-bit value
	 * 
	 * @param leftRegister The index of the left byte (upper 8 bits) register (A, B, D or H)
	 * @param rightRegister The index of the right byte (lower 8 bits) register
	 * @return The 16 bit register value
	 */
	private int registerPair(int leftRegister, int rightRegister) {
		return (register[leftRegister] << 8) | register[rightRegister];
	}

	/**
	 * Call a specific address<br/>
	 * Stores the next operation on the stack
	 * 
	 * @param nn The address to jump to
	 */
	private void call(int nn) {
		int newAddress = pc + 1;
		push((byte) (newAddress >> 8));
		push((byte) (newAddress));
		jump(nn);
	}

	/**
	 * Jumps to a given address if the condition is true<br/>
	 * <br/>
	 * The possible conditions:<br/>
	 * cc = NZ, Jump if Z flag is reset.<br/>
	 * cc = Z, Jump if Z flag is set.<br/>
	 * cc = NC, Jump if C flag is reset.<br/>
	 * cc = C, Jump if C flag is set.
	 * 
	 * @param cc Defines which condition we are being told to check
	 * @param nn The location to jump to
	 */
	private void jump(int cc, int nn) {
		if (passesCondition(cc)) {
			jump(nn);
		}
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
	private boolean passesCondition(int cc) {
		switch (cc) {
			case NZ:
				return (register[REGISTER_F] >>> FLAG_ZERO) == 0;
			case Z:
				return (register[REGISTER_F] >>> FLAG_ZERO) == 1;
			case NC:
				return (register[REGISTER_F] >>> FLAG_CARRY) == 0;
			case C:
				return (register[REGISTER_F] >>> FLAG_CARRY) == 1;
		}
		throw new RuntimeException("Condition is not a valid condition: " + cc);
	}

	/**
	 * Jumps to the given address
	 * 
	 * @param nn The address to jump to
	 */
	private void jump(int nn) {
		pc = (char) nn;
	}

	/**
	 * Pops a byte from the stack
	 * 
	 * @return The next byte on the stack
	 */
	private int pop() {
		sp++;
		return memory[sp];
	}

	/**
	 * Pushes a byte onto the stack
	 * 
	 * @param n The byte to be pushed
	 */
	private void push(byte n) {
		memory[sp] = (char) n;
		--sp;
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
