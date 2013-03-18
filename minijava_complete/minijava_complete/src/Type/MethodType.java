package Type;

import java.util.List;

/*
 * A MethodType in MiniJava
 * @Author Oleg Godunok, Changhao Han
 */
public class MethodType extends Type {
	// fields
	public String name; // method name
	public Type rType; // return type
	public List<Type> params; // list of parameters types

	public MethodType(String name, Type rType, List<Type> params) {
		this.name = name;
		this.rType = rType;
		this.params = params;
	}

	public void addParameter(Type t) {
		params.add(t);
	}

	// Two methods are the same if the types of the parameters
	// are the same, the return types are the same, and the names
	// are the same
	@Override
	public boolean same(Type other) {
		if (!(other instanceof MethodType))
			return false;
		MethodType o = (MethodType) other;
		if (params.size() != o.params.size())
			return false;
		for (int i = 0; i < params.size(); i++) {
			if (!params.get(i).same(o.params.get(i)))
				return false;
		}
		if (!(rType.same(o.rType)))
			return false;
		return true;
	}

	// method m1 is assignable to method m2 iff the parameters
	// are of the same type, the two methods have the same name,
	// and the return type of m1 is assignable to m2
	@Override
	public boolean assignable(Type other) {
		if (!(other instanceof MethodType))
			return false;
		MethodType o = (MethodType) other;
		if (params.size() != o.params.size())
			return false;
		for (int i = 0; i < params.size(); i++) {
			if (!params.get(i).same(o.params.get(i)))
				return false;
		}
		if (!(rType.assignable(o.rType)))
			return false;
		return true;
	}

	@Override
	public String toString() {
		String res = "MethodType-(return type: " + rType;
		if (params == null)
			return res + ")";
		if (params.size() == 0)
			return res + ")";
		res += ", Param1: " + params.get(0);
		for (int i = 1; i < params.size(); i++) {
			res += ", " + "Param" + (i + 1) + ": " + params.get(i);
		}
		return res + ")";
	}

	// returns the number of parameters for 'this'
	public int numParams() {
		return params.size();
	}
}
