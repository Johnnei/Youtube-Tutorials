package chip;

public enum Flag {
	
	/**
	 * The bit in {@link #REGISTER_F} that represents if the last math operation result was: 0
	 */
	Zero(7),
	/**
	 * The bit in {@link #REGISTER_F} that represents if the last math operation was a subtraction
	 */
	Subtract(6),
	/**
	 * The bit in {@link #REGISTER_F} that represents if the last math operation caused a carry on the lower nibble (result >= 16)
	 */
	HalfCarry(5),
	/**
	 * The bit in {@link #REGISTER_F} that represents if the last math operation caused a carry (result > 255)<Br/>
	 * This bit is also set of {@link #REGISTER_A} is the smaller value during the CP Instruction
	 */
	Carry(4);
	
	public final int bit;
	
	private Flag(int bit) {
		this.bit = bit;
	}

}
