package Type;

/*
 * Represents the type of arrays in MiniJava
 * @Author Oleg Godunok, Changhao Han
 */
public class ArrayType extends Type {
	// fields
	private int nDims; // # dimensions
	public Type elementType; // represents type of array

	public ArrayType(int nDims, Type elementType) {
		this.nDims = nDims;
		this.elementType = elementType;
	}

	// returns the number of dimension for 'this'
	public int getDim() {
		return nDims;
	}

	// Returns the type of the array
	public Type getType() {
		return elementType;
	}

	// Arrays in Java (and MiniJava) are structurally equivalent
	@Override
	public boolean same(Type other) {
		// other must be of type ArrayType
		if (!(other instanceof ArrayType))
			return false;
		ArrayType o = (ArrayType) other;
		if (nDims != o.getDim())
			return false;
		return elementType.same(o.getType());
	}

	@Override
	public boolean assignable(Type other) {
		if (!(other instanceof ArrayType))
			return false;
		ArrayType o = (ArrayType) other;
		if (nDims != o.getDim())
			return false;
		return elementType.assignable(o.elementType);
	}

	@Override
	public String toString() {
		return "ArrayType-(" + nDims + ", " + elementType + ")";
	}
}
