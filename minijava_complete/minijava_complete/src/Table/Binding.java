package Table;

import Type.Type;

/*
 * This class functions as a Wrapper for identifiers -
 * contains useful information such as Type (for now)
 * @Author Oleg Godunok, Changhao Han
 */
public class Binding {
	// fields
	public String id;
	public String belonging;
	// the type of the variable
	public Type type;
	// the offset of the variable or method. Will be -1 if not set
	public int offset;

	public Binding(String id, String belonging, Type type) {
		this.id = id;
		this.belonging = belonging;
		this.type = type;
		this.offset = -1;
	}

	// returns the variable id
	public String getID() {
		return id;
	}

	// returns the type of the variable id
	public Type getType() {
		return type;
	}

	public String toString() {
		return "id= " + id + "|type= " + type + "|belonging= " + belonging
				+ "|offset= " + offset;
	}
}
