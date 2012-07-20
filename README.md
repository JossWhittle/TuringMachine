# Java Turing Machine

An engine for running turing machines written in java. I'm very VERY bored over the summer here in Oxfordshire so this was just a fun little project to keep my mind at work, and prevent me from getting any research done for my dissertation.

## Usage

> Usage: Turing Machine [{-f,--file} path]         Path to a file describing a Turing Machine
>                       [{-t,--timeout} duration]  Time in milliseconds to wait after each instruction
>                       [{-z,--zero}]              Fill the tape with 0's rather than blanks
>                       [{-q,--quiet}]             Only print the final tape value on HALT
>                       [{-h,--help}]              Display this message...

## Turing Machine Markup

Turing machines are defined as 5-Tuples consisting of:
* State ID       (digit)
* Expected Value (B/0/1)
* Print Value    (B/0/1)
* Move Command   (L/N/R)
* Next State ID  (digit)

## An example Turing Machine

