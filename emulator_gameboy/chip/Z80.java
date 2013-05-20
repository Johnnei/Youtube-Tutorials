package chip;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import chip.opcode.IOpcode;
import chip.opcode.Opcode00NOP;
import chip.opcode.Opcode1000Stop;

public class Z80 {

	/**
	 * The 160x144 pixel 4 colors display
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
		display = new byte[160 * 144];
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

	/**
	 * A big Factory-Method to create the correct Opcode for the current byte found in the memory at the current programcounter
	 * @return The opcode to process
	 */
	private IOpcode getOpcode() {
		switch (memory[pc]) {
			case 0x00:
				return new Opcode00NOP();
			case 0x01:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x02:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x03:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x04:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x05:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x06:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x07:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x08:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x09:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x0A:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x0B:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x0C:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x0D:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x0E:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x0F:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x10:
				// 16 Bit Opcode

				switch (memory[pc + 1]) {
					case 0x00: // STOP
						return new Opcode1000Stop();

					default:
						throw new RuntimeException("Invalid ROM (0x10 Case) (0x" + Integer.toHexString(pc) + ")");
				}
			case 0x11:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x12:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x13:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x14:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x15:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x16:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x17:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x18:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x19:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x1A:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x1B:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x1C:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x1D:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x1E:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x1F:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x20:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x21:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x22:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x23:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x24:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x25:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x26:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x27:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x28:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x29:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x2A:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x2B:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x2C:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x2D:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x2E:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x2F:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x30:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x31:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x32:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x33:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x34:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x35:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x36:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x37:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x38:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x39:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x3A:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x3B:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x3C:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x3D:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x3E:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x3F:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x40:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x41:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x42:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x43:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x44:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x45:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x46:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x47:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x48:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x49:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x4A:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x4B:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x4C:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x4D:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x4E:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x4F:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x50:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x51:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x52:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x53:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x54:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x55:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x56:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x57:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x58:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x59:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x5A:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x5B:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x5C:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x5D:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x5E:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x5F:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x60:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x61:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x62:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x63:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x64:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x65:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x66:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x67:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x68:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x69:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x6A:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x6B:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x6C:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x6D:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x6E:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x6F:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x70:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x71:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x72:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x73:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x74:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x75:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x76:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x77:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x78:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x79:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x7A:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x7B:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x7C:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x7D:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x7E:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x7F:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x80:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x81:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x82:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x83:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x84:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x85:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x86:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x87:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x88:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x89:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x8A:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x8B:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x8C:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x8D:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x8E:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x8F:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x90:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x91:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x92:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x93:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x94:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x95:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x96:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x97:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x98:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x99:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x9A:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x9B:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x9C:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x9D:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x9E:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0x9F:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0xA0:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0xA1:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0xA2:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0xA3:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0xA4:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0xA5:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0xA6:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0xA7:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0xA8:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0xA9:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0xAA:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0xAB:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0xAC:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0xAD:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0xAE:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0xAF:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0xB0:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0xB1:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0xB2:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0xB3:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0xB4:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0xB5:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0xB6:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0xB7:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0xB8:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0xB9:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0xBA:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0xBB:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0xBC:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0xBD:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0xBE:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0xBF:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0xC0:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0xC1:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0xC2:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0xC3:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0xC4:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0xC5:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0xC6:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0xC7:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0xC8:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0xC9:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0xCA:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0xCB:
				// 16 Bit Opcode

				switch (memory[pc + 1]) {
				// TODO Implement 0xCB Opcodes
				}

				throw new RuntimeException("Invalid ROM (0xCB Case) (0x" + Integer.toHexString(pc) + ")");
			case 0xCC:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0xCD:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0xCE:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0xCF:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0xD0:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0xD1:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0xD2:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0xD4:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0xD5:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0xD6:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0xD7:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0xD8:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0xD9:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0xDA:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0xDC:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0xDF:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0xE0:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0xE1:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0xE2:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0xE5:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0xE6:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0xE7:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0xE8:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0xE9:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0xEA:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0xEE:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0xEF:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0xF0:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0xF1:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0xF2:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0xF3:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0xF5:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0xF6:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0xF7:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0xF8:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0xF9:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0xFA:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0xFB:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0xFE:
				// TODO Implement opcode
				return new Opcode00NOP();
			case 0xFF:
				// TODO Implement opcode
				return new Opcode00NOP();

			default:
				System.out.println("Unprocessed opcode 0x" + Integer.toHexString(memory[pc]).toUpperCase() + ", Processing as NOP");
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
	public int registerPair(Register leftRegister, Register rightRegister) {
		return (register[leftRegister.index] << 8) | register[rightRegister.index];
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
	 * 
	 * @return
	 */
	public int getStackPointer() {
		return sp & 0xFFFF;
	}

	/**
	 * Gets the program counter
	 * 
	 * @return
	 */
	public int getProgramCounter() {
		return pc & 0xFFFF;
	}

	/**
	 * Read data from the memory
	 * 
	 * @param address
	 * @return
	 */
	public int readMemory(int address) {
		return memory[address] & 0xFFFF;
	}

	/**
	 * Set the stackpointer
	 * 
	 * @param sp
	 */
	public void setStackPointer(int sp) {
		this.sp = (char)(sp & 0xFFFF);
	}

	/**
	 * Set the program counter
	 * 
	 * @param pc
	 */
	public void setProgramCounter(int pc) {
		this.pc = (char)(pc & 0xFFFF);
	}

	/**
	 * Write data to the memory
	 * 
	 * @param address
	 * @param value
	 */
	public void writeMemory(int address, int value) {
		memory[address] = (char)(value & 0xFF);
	}

	/**
	 * Increments the stackpointer by 1
	 */
	public void incrementStackPointer() {
		++sp;
	}

	/**
	 * Decrements the stack pointer by 1
	 */
	public void decrementStackPointer() {
		--sp;
	}

	/**
	 * Increments the program counter by 1
	 */
	public void incrementProgramCounter() {
		++pc;
	}

	/**
	 * Returns the display data
	 * 
	 * @return Current state of the 160x144 display
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
