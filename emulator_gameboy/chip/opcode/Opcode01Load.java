package chip.opcode;

import chip.Register;
import chip.Z80;

public class Opcode01Load implements IOpcode {

	@Override
	public void execute(Z80 gba) {
		gba.setRegister(Register.B, Register.C, gba.readMemory16bit(gba.getProgramCounter() + 1));
		gba.setProgramCounter(gba.getProgramCounter() + 3); //3 -> 2 read bytes + 1 to advance
	}

	@Override
	public int getCycleCount() {
		return 12;
	}

}
