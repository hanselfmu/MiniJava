/*
 *  boot.c: Main program for CSE minijava compiled code
 *          Ruth Anderson & Hal Perkins
 *
 *  Modified 11/11 to use long for 64-bit systems
 *
 *  Contents:
 *    Main program that calls the compiled code as a function
 *    Functions get and put that can be used by compiled code
 *      for integer I/O
 *    Function mjmalloc to allocate bytes for minijava's new operator
 *
 *  Additional functions used by compiled code can be added as desired.
 */

#include <stdio.h>
#include <stdlib.h>

extern void asm_main();   /* main function in compiled code */
                          /* change function name if your   */
                          /* compiled main has a different label */

/*
 *  Prompt for input, then return
 *  next integer from standard input.
 */
long get() {
  long k;
  printf("get: ");
  scanf("%ld", &k);
  return k;
}

/* Write x to standard output with a title */
void put(long x) {
  printf("put: %ld\n", x);

}


/*
 *  mjmalloc returns a pointer to a chunk of memory
 *  at least num_bytes large.  Returns NULL if there
 *  is insufficient memory available.
 */

void * mjmalloc(long num_bytes) {
  return (malloc(num_bytes));
}

/* Execute compiled program asm_main */
int main() {
  asm_main();
  return 0;
}

