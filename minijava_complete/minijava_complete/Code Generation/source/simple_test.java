class RunClass{
    public static void main(String[] a){
	System.out.println(new A().test(10));
    }
}

class A{
    int m;
    int n;
    int[] array;
    public int test(int num){
	int x;
        int y;
        int z;
        int init;
        int[] arr;
        int other;
       
        A b;
        A var;
        x = 10;
        y = 20;
        arr = new int[30];
        arr[0] = 40; 
        arr[5] = 50;
        arr[6] = 60; 
        init = this.Init(1000, 2000, 10); //this.m = 1000, this.n = 2000
   
        // z = 10 + 20 + ... + 60 = 210
        
	z = x + y + arr.length; // + arr[0] + arr[5] + arr[6]; // 2000
        System.out.println(this.getFirst());
        System.out.println(this.getSecond());
       
        b = new A(); 
        var = new B();
        System.out.println(this.getFirst());
        System.out.println(this.getSecond());
        other = var.Init(10, 10, 10);
        System.out.println(var.getFirst());
        System.out.println(var.getSecond());
        System.out.println(this.getFirst());
        System.out.println(this.getSecond());
        // 210 + this.test2(10, 10, 20, 210)
        other = var.getFirst() + var.getSecond(); // 20
	System.out.println(0);
        System.out.println(z);
        System.out.println(init);
        System.out.println(this.getFirst());
        System.out.println(this.getSecond());
        System.out.println(other);
        return z + this.test2(x, x, y, z) + init + this.getFirst() + this.getSecond() + other;
        // return 210 + 10 + 10 + 20 + 210 + 40 + 3000 = 3520 
   }
   
   public int Init(int var1, int var2, int len) {
     m = var1;
     n = var2;
     array = new int[len];
     return 0;
   }

   public int test2(int a, int b, int c, int d) {
     int s;
     int t;
     s = 10;
     t = s + 20;
     return a + b + c + d + s + t;
   }

   public int getFirst() {
     return m;
   }

   public int getSecond() {
     return n;
   } 

}

class B extends A {


}

