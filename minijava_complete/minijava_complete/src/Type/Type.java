package Type;

/*
 * Abstract class which denotes the type system in java. 
 * @Author Oleg Godunok, Changhao Han
 */
public abstract class Type {
	// Checks if 'this' and other are of the same types
	// Returns true if it is else false
	public abstract boolean same(Type other);

	// Checks if other is assignable to 'this'
	// If true, then it is, else false
	public abstract boolean assignable(Type other);

	// Returns a string representation of 'this'
	public abstract String toString();
	// Returns true if two Types are the same during runtime
}
