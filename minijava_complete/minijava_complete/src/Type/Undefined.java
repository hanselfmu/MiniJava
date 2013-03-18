package Type;

/*
 * Class represents undefined types (unknown types)
 * @Author Oleg Godunok, Changhao Han
 */
public class Undefined extends Type {
	// fields
	public static final Undefined UNDEFINED = new Undefined("undefined");
	// string representation of Undefined
	private String rep;

	// private constructor for 'this'
	private Undefined(String rep) {
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
