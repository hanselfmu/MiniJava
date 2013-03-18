	.text
	.globl asm_main
asm_main:
	pushq %rbp
	movq %rsp,%rbp
	pushq %rdi
	movq %rdi,%rcx
	call Fac$Fac
	movq %rax,%rdi
	movq $10,%rax
	pushq %rax
	movq (%rdi),%rax
	lea 16(%rax),%rax
	call *(%rax)
	popq %rdx
	popq %rdi
	pushq %rdi
	movq %rax,%rdi
	call put
	popq %rdi
	movq %rbp,%rsp
	popq %rbp
	ret 

	.data
Factorial$$: .quad 0

Fac$ComputeFac:
	pushq %rbp
	movq %rsp,%rbp
	subq $8,%rsp
	movq 16(%rbp),%rax
	pushq %rax
	movq $1,%rax
	popq %rdx
	cmpq %rax,%rdx
	jge else1
	movq $1,%rax
	movq %rax,-8(%rbp)
	jmp done1
else1:
	movq 16(%rbp),%rax
	pushq %rax
	pushq %rdi
	movq %rdi,%rcx
	movq %rdi,%rax
	movq 16(%rbp),%rax
	pushq %rax
	movq $1,%rax
	popq %rdx
	negq %rax
	addq %rdx,%rax
	pushq %rax
	movq (%rdi),%rax
	lea 16(%rax),%rax
	call *(%rax)
	popq %rdx
	popq %rdi
	popq %rdx
	imulq %rdx,%rax
	movq %rax,-8(%rbp)
done1:
	movq -8(%rbp),%rax
	movq %rbp,%rsp
	popq %rbp
	ret 
Fac$Fac:
	pushq %rbp
	movq %rsp,%rbp
	pushq %rdi
	movq $8,%rdi
	call mjmalloc
	popq %rdi
	leaq Fac$$,%rdx
	movq %rdx,(%rax)
	movq %rbp,%rsp
	popq %rbp
	ret 

	.data
Fac$$: .quad 0
	.quad Fac$Fac
	.quad Fac$ComputeFac
