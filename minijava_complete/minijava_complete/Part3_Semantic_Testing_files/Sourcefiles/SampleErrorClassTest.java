class SampleErrorClassTest {
	public static void main(String[] a) {
		System.out.println(new SampleClassC().foo(10));
	}
}



class SampleAbstractClass {
	int s1;
	int[] s2;
	int s3;
	
	public int foo(boolean i) {
		s3 = i;
		return s3;
	}
	
}

// This is deliberately put before other class declarations
class SampleClassMain extends SampleErrorClassTest {
	
}

class SampleClassA extends SampleAbstractClass {
	
	public int foo(boolean i) {
		i = i + 1;
		return s3;
	}
}

class SampleClassB extends SampleAbstractClass {
	
}

class SampleClassC extends SampleClassA {
	
}
