package Type;

/*
 * Class represents base types in MiniJava
 * @Author Oleg Godunok, Changhao Han
 */
public class BaseType extends Type {
	// represents the 'int' type
	public static final BaseType INTEGER = new BaseType("int");
	// represents the 'boolean' type
	public static final BaseType BOOLEAN = new BaseType("boolean");
	// string representation of the type
	private String rep;

	private BaseType(String rep) {
		this.rep = rep;
	}

	@Override
	public String toString() {
		return rep;
	}

	@Override
	public boolean same(Type e) {
		// e is of type BaseType so the two are equal if the string
		// representations are the same. Another way to determine if
		// the two types are equal: equality. There is only one object
		// for each base type so return e == this; will do the trick
		return e == this;
	}

	@Override
	public boolean assignable(Type e) {
		return e == this;
	}
}
