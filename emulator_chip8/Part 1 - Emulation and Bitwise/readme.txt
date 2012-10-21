Part 1 - Emulation and Bitwise Operations
- Coverage

In this part we'll start by putting together all information of the chipset into an chip class.
This class will be the location in which we will emulate the chip.

- Assignement for part 2

Make a Frame to display the display array visually. This could be a JFrame or OpenGL (LWJGL advised) or whatever.
I'll show my solution in part 2.
Hint: Scale the display up to 640x320 or you'll have a really small screen.

- Binary (Base 2) and Hexadecimal (Base 16)

There are many ways to display an decimal number (1, 2, 3, 3.14, etc).
A computer is working with "bits", which is binary.
A bit will represent a "bool" value: It's either on or off
All the values are made up by bits and the amount of available bits will limit the maximums.
The values of binary are all powers of 2 and the lowest value bit will be on the most right side.
The maximum value will be reached with all bits "on" (set to 1) and the lowest with all bits off.
To get an idea of how large each type is: the smallest type we can work with is Byte (byte) which is 8-bit (0 - 255),
Short (short) is 16-bit (0 - 65'535), Integer (int) is 32-bit (0 - 4'294'967'295), Long (long) is 64-bit (0 - 18'446'744'073'709'551'615).
The maximum value which you can reach with x bits is: 2^x - 1 (-1 because 0 is the first value).
To display a value in binary you can use this method (ex. with putting 67 into a byte)
Well start with noting the values of that bit above the row
1
2 6 3 1 8 4 2 1
8 4 2 6 
---------
0 0 0 0 0 0 0 0
but for readability the standard is: group in four-bit (nibble)
0000 0000
so well start checking our value:
Does 128 fit in 67? No: We'll write down a zero: 0xxx xxxx
Does 64 fit in 67?: Yes: We'll write down an one: 01xx xxxx, We'll also subtract 64 from 67: remaining with 3
Does 32 fit in 3?: No: We'll write down a zero: 010x xxxx
Does 16 fit in 3?: No: We'll write down a zero: 0100 xxxx
Does 8 fit in 3?: No: We'll write down a zero: 0100 0xxx
Does 4 fit in 3?: No: We'll write down a zero: 0100 00xx
Does 2 fit in 3?: Yes: We'll write down an one: 0100 001x, We'll also subtract 2 from 3: remaining with 1
Does 1 fit in 1?: Yes: We'll write down an one: 0100 0011

so lets do some simple binary counting, and to keep it simple we'll use an example with nibbles (4 bit) and one with bytes.
0101 = 5 in decimal
1010 = 10 in decimal
---- +
1111 = 15 in decimal
To make it easy you could translate the binary into decimal and then add up the decimals and then back into binary or you can use shifting of bits, but you'll get that after you work with bits a bit.
The basis of that is 1 (1 in decimal) and 1 (1 in decimal) will become 10 (2 in decimal) and work with that on, it sounds maybe complicated but you'll get it in time.

Ok so on to the hexadecimal: I can be a lot shorter about this.
In hexadecimal we use 1 digit to represent 0 up to and including 15: 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, A (10), B (11), C (12), D (13), E (14), F (15).
So with 1 digit we can represent 1 nibble (4 bit), that means that we can write down a full byte in just 2 characters.
The rule to let people know you are working in hexadecimal is: add 0x infront of the number.
So we'll rewrite the values of the type ranges: Byte (0x00 - 0xFF), Short (0x0000, 0xFFFF), Integer (0x00000000, 0xFFFFFFFF), Long (0x0000000000000000 - 0xFFFFFFFFFFFFFFFF).
It is used to have to write down less and it is a bit more clear in bitwise operations.
How to translate hexadecimal into decimal:
0xFE - (F * 16) + (E * 1) = 254
0x102 - (1 * 16 * 16) + (0 * 16) + (2 * 1) = 258
etc.

[Continue the video from here]

- Bitwise Operations

This chapter is only used as reference.

Legenda: [NAME (JAVA OPERATOR)]

[Left Shift (<<)]
0000 0001 (1)
---- ---- 3 (Left shift 3 positions)
0000 1000 (8)

[Right Shift (>>)]
0010 0010 (34)
---- ---- 1 (Right Shift 1 position)
0001 0001 (17)

[OR Operation (|)]
0010 1001 (41)
0101 1010 (90)
---- ---- OR
0111 1011 (123)

True/False Schema:
A | B | Result
0 | 0 | 0
1 | 0 | 1
0 | 1 | 1
1 | 1 | 1

[AND Operation (&)]
0010 1001 (41)
0101 1010 (90)
---- ---- AND
0000 1000 (8)

True/False Schema
A | B | Result
0 | 0 | 0
1 | 0 | 0
0 | 1 | 0
1 | 1 | 1

[XOR (Exclusive OR) Operation (^)]
0010 1001 (41)
0101 1010 (90)
---- ---- XOR
0111 0011 (115)

True/False Schema
A | B | Result
0 | 0 | 0
1 | 0 | 1
0 | 1 | 1
1 | 1 | 0

[Unary (~)] (Invert)
0010 1001 (41)
---- ---- UNARY
1101 0110 (214)