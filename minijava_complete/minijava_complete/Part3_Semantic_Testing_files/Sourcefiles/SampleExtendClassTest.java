//The classes are basically the same as the BinaryTree 
//file except the visitor classes and the accept method
//in the Tree class

class SampleExtendClassTest {
	public static void main(String[] a) {
		System.out.println(new SampleClassD().foo(10));
	}
}


class SampleAbstractClass {
	int s1;
	int[] s2;
	int s3;
	
	public int foo(int i) {
		s3 = i;
		return s3;
	}
}

// This is deliberately put before other class declarations
class SampleClassD extends SampleClassC {
	
}

class SampleClassA extends SampleAbstractClass {
	
}

class SampleClassB extends SampleAbstractClass {
	
}

class SampleClassC extends SampleClassA {
	
}

