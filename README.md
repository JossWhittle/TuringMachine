# Java Turing Machine

An engine for running turing machines written in java. I'm very VERY bored over the summer here in Oxfordshire so this was just a fun little project to keep my mind at work, and prevent me from getting any research done for my dissertation.

## Usage

	Usage: Turing Machine [{-f,--file} path]         Path to a file describing a Turing Machine
	                      [{-t,--timeout} duration]  Time in milliseconds to wait after each instruction
	                      [{-z,--zero}]              Fill the tape with 0's rather than blanks
	                      [{-q,--quiet}]             Only print the final tape value on HALT
	                      [{-h,--help}]              Display this message...

## Turing Machine Markup

Turing machines are defined as 5-Tuples consisting of:
* State ID       (digit)
* Expected Value (B/0/1)
* Print Value    (B/0/1)
* Move Command   (L/N/R)
* Next State ID  (digit)

Optionally, the first line of a Turing Machine file can be a String of B's, 0's, and 1's to define the initial state of the tape.

## An example Turing Machine

Here we have a simple turing machine consisting of four states. When computed it produces and infinitely long string of 0 space 1 space 0 space 1...

	(1,B,0,R,2)
	(2,B,B,R,3)
	(3,B,1,R,4)
	(4,B,B,R,1)

The machine assumes and infinite tape which is blank filled. Thus we would run this turing machine on the command line with

	java TuringMachine < Simple.tur -t 100

Another example machine is the 3-State Busy Beaver.

	(1,0,1,R,2)
	(1,1,1,L,3)
	(2,0,1,L,1)
	(2,1,1,R,2)
	(3,0,1,L,2)
	(3,1,1,N,H)

This machine imagines a infinite tape which is zero filled. To run it would type

	java TuringMachine < Beaver.tur -t 100 -z 

## Requirements

The code in this project requires the JArgs file CmdLineParser.java, which can be acquired here https://github.com/purcell/jargs