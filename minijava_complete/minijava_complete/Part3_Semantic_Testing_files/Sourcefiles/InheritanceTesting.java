class InheritanceExample {
    public static void main(String[] a){
	System.out.println(new A().call());
    }
}

class A{
	int i;
	int j;
	int k;
	A var;

	public A call() {
		int x;
		int y;
		x = 1;
		y = 2;
		x = x + y;
		return var;
	}
}

class B extends A{
	int m;
	B v;
	public B call() {
		return v;
	}
}

class C extends B{
	int n;
	public int method(int x, boolean y) {
		return x;
	}
}