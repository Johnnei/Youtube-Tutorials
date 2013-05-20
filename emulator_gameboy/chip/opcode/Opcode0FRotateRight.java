package chip.opcode;

import chip.Flag;
import chip.Register;
import chip.Z80;

public class Opcode0FRotateRight implements IOpcode {

	@Override
	public void execute(Z80 gba) {
		int flagA = gba.getRegister(Register.A);
		int bitZero = flagA & 0x1;
		gba.resetFlagRegister();
		gba.setFlagRegister(Flag.Carry, bitZero == 1);
		flagA = flagA >>> 1;
		flagA |= bitZero << 7;
		gba.setFlagRegister(Flag.Zero, flagA == 0);
		gba.setRegister(Register.A, flagA);
	}

	@Override
	public int getCycleCount() {
		return 4;
	}

}
