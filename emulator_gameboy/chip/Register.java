package chip;

public enum Register {
	A(0),
	B(1),
	C(2),
	D(3),
	E(4),
	H(5),
	L(6),
	F(7);
	
	public final int index;
	
	private Register(int index) {
		this.index = index;
	}
}
