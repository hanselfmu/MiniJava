class RunClass {
	public static void main(String[] a) {

		// System.out.println(new int[10].length);
		System.out.println(new ExampleClass().compute(6));
	}
}

class ExampleClass {

	public int compute(int num) {
		boolean x;
		int y;
		// !((!F && T) && (T && !F)
		x = (!(2 < 1) && (true && true)) && ((0 < num) && !false);
		System.out.println(x); // 1
		y = 0;

		// should print
		// 1
		// 2
		// 3
		// 4
		// 5
		// 6
		while (x) {
			y = y + 1;
			x = (!(2 < 1) && (true && true)) && ((y < num) && !false);
			System.out.println(y);
		}
		// over
		return y;
	}
}
