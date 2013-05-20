package chip.opcode;

import chip.Z80;

public class Opcode00NOP implements IOpcode {

	@Override
	public void execute(Z80 gba) {
		gba.incrementProgramCounter();
	}

	@Override
	public int getCycleCount() {
		return 4;
	}

}
