package chip;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Random;

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
	
	private boolean needRedraw;
	
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
		
		needRedraw = false;
		loadFontset();
	}
	
	/**
	 * Executes a single Operation Code (Opcode)
	 */
	public void run() {
		//fetch Opcode
		char opcode = (char)((memory[pc] << 8) | memory[pc + 1]);
		System.out.print(Integer.toHexString(opcode).toUpperCase() + ": ");
		//decode opcode
		switch(opcode & 0xF000) {
		
		case 0x0000: //Multi-case
			switch(opcode & 0x00FF) {
			case 0x00E0: //00E0: Clear Screen
				System.err.println("Unsupported Opcode!");
				System.exit(0);
				break;
				
			case 0x00EE: //00EE: Returns from subroutine
				stackPointer--;
				pc = (char)(stack[stackPointer] + 2);
				System.out.println("Returning to " + Integer.toHexString(pc).toUpperCase());
				break;
				
			default: //0NNN: Calls RCA 1802 Program at address NNN
				System.err.println("Unsupported Opcode!");
				System.exit(0);
				break;
			}
			break;
		
		case 0x1000: //1NNN: Jumps to address NNN
			int nnn = opcode & 0x0FFF;
			pc = (char)nnn;
			System.out.println("Jumping to " + Integer.toHexString(pc).toUpperCase());
			break;
			
		case 0x2000: //2NNN: Calls subroutine at NNN
			stack[stackPointer] = pc;
			stackPointer++;
			pc = (char)(opcode & 0x0FFF);
			System.out.println("Calling " + Integer.toHexString(pc).toUpperCase() + " from " + Integer.toHexString(stack[stackPointer - 1]).toUpperCase());
			break;
			
		case 0x3000: { //3XNN: Skips the next instruction if VX equals NN
			int x = (opcode & 0x0F00) >> 8;
			int nn = (opcode & 0x00FF);
			if(V[x] == nn) {
				pc += 4;
				System.out.println("Skipping next instruction (V[" + x +"] == " + nn + ")");
			} else {
				pc += 2;
				System.out.println("Not skipping next instruction (V[" + x +"] != " + nn + ")");
			}
			break;
		}
			
		case 0x6000: { //6XNN: Set VX to NN
			int x = (opcode & 0x0F00) >> 8;
			V[x] = (char)(opcode & 0x00FF);
			pc += 2;
			System.out.println("Setting V[" + x + "] to " + (int)V[x]);
			break;
		}
			
		case 0x7000: { //7XNN: Adds NN to VX
			int x = (opcode & 0x0F00) >> 8;
			int nn = (opcode & 0x00FF);
			V[x] = (char)((V[x] + nn) & 0xFF);
			pc += 2;
			System.out.println("Adding " + nn + " to V["+ x + "] = " + (int)V[x]);
			break;
		}
		
		case 0x8000: //Contains more data in last nibble
			
			switch(opcode & 0x000F) {
				
			case 0x0000: //8XY0: Sets VX to the value of VY.
				default:
					System.err.println("Unsupported Opcode!");
					System.exit(0);
					break;
			}
				
			break;
			
		case 0xA000: //ANNN: Set I to NNN
			I = (char)(opcode & 0x0FFF);
			pc += 2;
			System.out.println("Set I to " + Integer.toHexString(I).toUpperCase());
			break;
			
		case 0xC000: { //CXNN: Set VX to a random number and NN
			int x = (opcode & 0x0F00) >> 8;
			int nn = (opcode & 0x00FF);
			int randomNumber = new Random().nextInt(256) & nn;
			System.out.println("V[" + x + "] has been set to (randomised) " + randomNumber);
			V[x] = (char)randomNumber;
			pc += 2;
		}
			
		case 0xD000: { //DXYN: Draw a sprite (X, Y) size (8, N). Sprite is located at I
			int x = V[(opcode & 0x0F00) >> 8];
			int y = V[(opcode & 0x00F0) >> 4];
			int height = opcode & 0x000F;
			
			V[0xF] = 0;
			
			for(int _y = 0; _y < height; _y++) {
				int line = memory[I + _y];
				for(int _x = 0; _x < 8; _x++) {
					int pixel = line & (0x80 >> _x);
					if(pixel != 0) {
						int totalX = x + _x;
						int totalY = y + _y;
						int index = totalY * 64 + totalX;
						
						if(display[index] == 1)
							V[0xF] = 1;
						
						display[index] ^= 1;
					}
				}
			}
			pc += 2;
			needRedraw = true;
			System.out.println("Drawing at V[" + ((opcode & 0x0F00) >> 8) + "] = " + x + ", V[" + ((opcode & 0x00F0) >> 4) + "] = " + y);
			break;
		}
		
		case 0xE000: {
			switch (opcode & 0x00FF) {
			case 0x009E: { //EX9E Skip the next instruction if the Key VX is pressed
				int key = (opcode & 0x0F00) >> 8;
				if(keys[key] == 1) {
					pc += 4;
				} else {
					pc += 2;
				}
				break;
			}
				
			case 0x00A1: { //EXA1 Skip the next instruction if the Key VX is NOT pressed
				int key = (opcode & 0x0F00) >> 8;
				if(keys[key] == 0) {
					pc += 4;
				} else {
					pc += 2;
				}
				break;
			}
				
				default:
					System.err.println("Unexisting opcode");
					System.exit(0);
					return;
			}
			break;
		}
		
		case 0xF000:
			
			switch(opcode & 0x00FF) {
			
			case 0x0007: { //FX07: Set VX to the value of delay_timer
				int x = (opcode & 0x0F00) >> 8;
				V[x] = (char)delay_timer;
				pc += 2;
				System.out.println("V[" + x + "] has been set to " + delay_timer);
			}
			
			case 0x0015: { //FX15: Set delay timer to V[x]
				int x = (opcode & 0x0F00) >> 8;
				delay_timer = V[x];
				pc += 2;
				System.out.println("Set delay_timer to V[" + x + "] = " + (int)V[x]);
			}
			
			case 0x0029: { //FX29: Sets I to the location of the sprite for the character VX (Fontset)
				int x = (opcode & 0x0F00) >> 8;
				int character = V[x];
				I = (char)(0x050 + (character * 5));
				System.out.println("Setting I to Character V[" + x + "] = " + (int)V[x] + " Offset to 0x" + Integer.toHexString(I).toUpperCase());
				pc += 2;
				break;
			}
			
			case 0x0033: { //FX33 Store a binary-coded decimal value VX in I, I + 1 and I + 2
				int x = (opcode & 0x0F00) >> 8;
				int value = V[x];
				int hundreds = (value - (value % 100)) / 100;
				value -= hundreds * 100;
				int tens = (value - (value % 10))/ 10;
				value -= tens * 10;
				memory[I] = (char)hundreds;
				memory[I + 1] = (char)tens;
				memory[I + 2] = (char)value;
				System.out.println("Storing Binary-Coded Decimal V[" + x + "] = " + (int)(V[(opcode & 0x0F00) >> 8]) + " as { " + hundreds+ ", " + tens + ", " + value + "}");
				pc += 2;
				break;
			}
			
			case 0x0065: { //FX65 Filss V0 to VX with values from I
				int x = (opcode & 0x0F00) >> 8;
				for(int i = 0; i < x; i++) {
					V[i] = memory[I + i];
				}
				System.out.println("Setting V[0] to V[" + x + "] to the values of merory[0x" + Integer.toHexString(I & 0xFFFF).toUpperCase() + "]");
				pc += 2;
				break;
			}
			
			default:
				System.err.println("Unsupported Opcode!");
				System.exit(0);
			}
			break;
		
			default:
				System.err.println("Unsupported Opcode!");
				System.exit(0);
		}
	}
	
	/**
	 * Returns the display data
	 * @return
	 * Current state of the 64x32 display
	 */
	public byte[] getDisplay() {
		return display;
	}

	/**
	 * Checks if there is a redraw needed
	 * @return
	 * If a redraw is needed
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
	 * Loads the program into the memory
	 * @param file
	 * The location of the program
	 */
	public void loadProgram(String file) {
		DataInputStream input = null;
		try {
			input = new DataInputStream(new FileInputStream(new File(file)));
			
			int offset = 0;
			while(input.available() > 0) {
				memory[0x200 + offset] = (char)(input.readByte() & 0xFF);
				offset++;
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		} finally {
			if(input != null) {
				try { input.close(); } catch (IOException ex) {}
			}
		}
	}
	
	/**
	 * Loads the fontset into the memory
	 */
	public void loadFontset() {
		for(int i = 0; i < ChipData.fontset.length; i++) {
			memory[0x50 + i] = (char)(ChipData.fontset[i] & 0xFF);
		}
	}
	
	public void setKeyBuffer(int[] keyBuffer) {
		for(int i = 0; i < keys.length; i++) {
			keys[i] = (byte)keyBuffer[i];
		}
	}

}
