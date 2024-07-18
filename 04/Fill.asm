// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/4/Fill.asm

// Runs an infinite loop that listens to the keyboard input. 
// When a key is pressed (any key), the program blackens the screen,
// i.e. writes "black" in every pixel. When no key is pressed, 
// the screen should be cleared.

//// SOLUTION:
////
//// while(true)
////    addr=SCREEN
////    if key=1            (key pressed)
////        color=1         (set color to black)   
////        goto LOOP
////    else                (no key pressed)
////        color=0         (set color to white)
////    LOOP:   
////        for(int i=0; i<8192; i++){
////          SCREEN[addr]=color
////          addr++
////        }   

    // set color
    @color
    M=0
    // set 8192 registers
    @8192
    D=A
    @n 
    M=D

(LISTEN)
    // set screen address
    @SCREEN
    D=A
    @addr
    M=D
    // set i
    @i
    M=0
    // if keyboard is pressed
    @KBD
    D=M
    // go to BLACK
    @BLACK
    D;JGT

    @WHITE
    0;JMP

(BLACK)
    @color
    M=-1
    @LOOP
    0;JMP

(WHITE)
    @color
    M=0

(LOOP)
    @n
    D=M
    @i
    D=D-M
    // go to LISTEN if i>8192
    @LISTEN
    D;JEQ

    // RAM[addr]=color
    @color
    D=M
    @addr
    A=M
    M=D
    @addr
    M=M+1

    @i
    M=M+1
    @LOOP
    0;JMP
