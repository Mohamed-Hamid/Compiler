# Compiler
Just Another Compiler

STEPS:
1- Parsing of input. Input: TextFile, Comments on the places that will call methods from point 2 (Implemenation of NFA) and ();
Symbol table feeding
2 - Implementation of NFA
  Base Case
    1 character => NFA 
    1 string => (String string)
    | or - => or(NFA first, NFA second, NFA ...)
    * => (NFA nfa)
    + => (NFA nfa)
    c"concatenation" => (NFA first, NFA second, NFA ...)
3- NFA -> DFA implement Transition Table
4- DFA -> minimum DFA
5- Parse examples
