package chip.opcode;

import chip.Z80;

public interface IOpcode {
	
	public void execute(Z80 gba);
	
	public int getCycleCount();

}
