package chip.opcode;

import chip.Register;
import chip.Z80;

public class Opcode02Load implements IOpcode {

	@Override
	public void execute(Z80 gba) {
		gba.setRegister(Register.B, 0);
		gba.setRegister(Register.C, gba.getRegister(Register.A));
		gba.incrementProgramCounter();
	}

	@Override
	public int getCycleCount() {
		return 8;
	}

}
