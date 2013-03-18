	.text
	.globl asm_main
asm_main:
	pushq %rbp
	movq %rsp,%rbp
	pushq %rdi
	movq %rdi,%rcx
	call ExampleClass$ExampleClass
	movq %rax,%rdi
	movq $6,%rax
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
RunClass$$: .quad 0

ExampleClass$compute:
	pushq %rbp
	movq %rsp,%rbp
	subq $16,%rsp
	movq $2,%rax
	pushq %rax
	movq $1,%rax
	popq %rdx
	cmpq %rax,%rdx
	jl genfalse1
	cmpq $0,%rax
	je genfalse1
	movq $1,%rax
	cmpq $0,%rax
	je genfalse1
	movq $1,%rax
	cmpq $0,%rax
	je genfalse1
	cmpq $0,%rax
	je genfalse1
	cmpq $0,%rax
	je genfalse1
	movq $0,%rax
	pushq %rax
	movq 16(%rbp),%rax
	popq %rdx
	cmpq %rax,%rdx
	jge genfalse1
	cmpq $0,%rax
	je genfalse1
	movq $0,%rax
	xor $1,%rax
	cmpq $0,%rax
	je genfalse1
	cmpq $0,%rax
	je genfalse1
	movq $1,%rax
	jmp storeit1
genfalse1:
	movq $0,%rax
storeit1:
	movq %rax,-8(%rbp)
	movq -8(%rbp),%rax
	pushq %rdi
	movq %rax,%rdi
	call put
	popq %rdi
	movq $0,%rax
	movq %rax,-16(%rbp)
test1:
	movq -8(%rbp),%rax
	cmpq $0,%rax
	je done1
	movq -16(%rbp),%rax
	pushq %rax
	movq $1,%rax
	popq %rdx
	addq %rdx,%rax
	movq %rax,-16(%rbp)
	movq $2,%rax
	pushq %rax
	movq $1,%rax
	popq %rdx
	cmpq %rax,%rdx
	jl genfalse4
	cmpq $0,%rax
	je genfalse4
	movq $1,%rax
	cmpq $0,%rax
	je genfalse4
	movq $1,%rax
	cmpq $0,%rax
	je genfalse4
	cmpq $0,%rax
	je genfalse4
	cmpq $0,%rax
	je genfalse4
	movq -16(%rbp),%rax
	pushq %rax
	movq 16(%rbp),%rax
	popq %rdx
	cmpq %rax,%rdx
	jge genfalse4
	cmpq $0,%rax
	je genfalse4
	movq $0,%rax
	xor $1,%rax
	cmpq $0,%rax
	je genfalse4
	cmpq $0,%rax
	je genfalse4
	movq $1,%rax
	jmp storeit4
genfalse4:
	movq $0,%rax
storeit4:
	movq %rax,-8(%rbp)
	movq -16(%rbp),%rax
	pushq %rdi
	movq %rax,%rdi
	call put
	popq %rdi
	jmp test1
done1:
	movq -16(%rbp),%rax
	movq %rbp,%rsp
	popq %rbp
	ret 
ExampleClass$ExampleClass:
	pushq %rbp
	movq %rsp,%rbp
	pushq %rdi
	movq $8,%rdi
	call mjmalloc
	popq %rdi
	leaq ExampleClass$$,%rdx
	movq %rdx,(%rax)
	movq %rbp,%rsp
	popq %rbp
	ret 

	.data
ExampleClass$$: .quad 0
	.quad ExampleClass$ExampleClass
	.quad ExampleClass$compute
