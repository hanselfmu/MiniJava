class RunClass{
    public static void main(String[] a){
	System.out.println(new Example1().ComputeFac(10));
    }
}


class Example1{
    int[] b;
    int num_aux;
    int num;

    public Example1 ComputeFac(Example1 t){
	int num_aux ;
	int[] arr;
	if (num < 1)
	    num_aux = 1 ;
	else 
	    num_aux = num * (this.ComputeFac(true)) ;
	return num_aux ;
    }

    public boolean test(int[] s) {
    	while (s.length < 0) {
    		num = (new Example1()).caller(num_aux);
    	}
    	return true&&false;
    }

    public int[] caller(int x) {
    	return b;
    }
}

class Example2 extends Example1 {
	int j;
	boolean k;
	Example1 p;
	Example2 fac;
	public int run(int x, int x, int z, int t) {
		return fac.testing(fac, new Example2(), 20) ;
	}
	public Example1 ComputeFac(Example1 t) {
		return true;
	}
	public boolean testing(Example1 a, Example2 b, int c) {
		return false;
	}
}


class Example3 extends Example1 {
	public Example2 ComputeFac(Example1 t) {
		return false;
	}
}