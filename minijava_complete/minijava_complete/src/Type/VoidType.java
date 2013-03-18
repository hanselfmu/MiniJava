package Type;

/*
 * Used to represent the 'special' type void in MiniJava
 * @Author Oleg Godunok, Changhao Han 
 */
public class VoidType extends Type {
	// fields
	public static final VoidType VOID = new VoidType("void");
	private String rep;

	private VoidType(String rep) {
		this.rep = rep;
	}

	@Override
	public String toString() {
		return rep;
	}

	@Override
	public boolean same(Type e) {
		return e == this;
	}

	@Override
	public boolean assignable(Type e) {
		return e == this;
	}
}
