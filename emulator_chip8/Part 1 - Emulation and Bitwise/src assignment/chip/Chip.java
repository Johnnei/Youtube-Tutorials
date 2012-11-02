package chip;

public class Chip {
	
	/**
	 * 4kB of 8-bit memory<br/>
	 * At position 0x50: The "bios" fontset
	 * At position 0x200: The start of every program
	 */
	private char[] memory;
	/**
	 * 16 8-bit registers.<br/>
	 * They will be used to store data which is used in several operation<br/>
	 * Register 0xF is used for Carry, Borrow and collision detection
	 */
	private char[] V;
	/**
	 * 16-bit (only 12 are used) to point to a specific point in the memory
	 */
	private char I;
	/**
	 * The 16-bit (only 12 are used) to point to the current operation
	 */
	private char pc;
	
	/**
	 * Subroutine callstack<br/>
	 * Allows up to 16 levels of nesting
	 */
	private char stack[];
	/**
	 * Points to the next free slot int the stack
	 */
	private int stackPointer;
	
	/**
	 * This timer is used to delay events in programs/games
	 */
	private int delay_timer;
	/**
	 * This timer is used to make a beeping sound
	 */
	private int sound_timer;
	
	/**
	 * This array will be our keyboard state
	 */
	private byte[] keys;
	
	/**
	 * The 64x32 pixel monochrome (black/white) display
	 */
	private byte[] display;
	
	/**
	 * Reset the Chip 8 memory and pointers
	 */
	public void init() {
		memory = new char[4096];
		V = new char[16];
		I = 0x0;
		pc = 0x200;
		
		stack = new char[16];
		stackPointer = 0;
		
		delay_timer = 0;
		sound_timer = 0;
		
		keys = new byte[16];
		
		display = new byte[64 * 32];
	}
	
	/**
	 * Executes a single Operation Code (Opcode)
	 */
	public void run() {
		//fetch Opcode
		char opcode = (char)((memory[pc] << 8) | memory[pc + 1]);
		System.out.print(Integer.toHexString(opcode) + ": ");
		//decode opcode
		switch(opcode & 0xF000) {
		
		case 0x8000: //Contains more data in last nibble
			
			switch(opcode & 0x000F) {
				
			case 0x0000: //8XY0: Sets VX to the value of VY.
				default:
					System.err.println("Unsupported Opcode!");
					System.exit(0);
					break;
			}
				
			break;
		
			default:
				System.err.println("Unsupported Opcode!");
				System.exit(0);
		}
			//execute opcode
	}
	
	/**
	 * Returns the display data
	 * @return
	 * Current state of the 64x32 display
	 */
	public byte[] getDisplay() {
		return display;
	}

}
