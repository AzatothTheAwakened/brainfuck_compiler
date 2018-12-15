grammar Brainfuck;

/*
    Parser
*/

prog : statementlist;

statementlist : statement+;

statement : INC #increase
          | DEC #decrease
          | LEFT #lshift
          | RIGHT #rshift
          | PUT #put
          | GET #get
          | LOOPB statements=statementlist LOOPE #loop
          ;

/*
    Tokenizer
*/

INC : '+';

DEC : '-';

LEFT : '<';

RIGHT : '>';

LOOPB : '[';

LOOPE : ']';

PUT : '.';

GET : ',';

IGNORED : (~('+'|'-'|'<'|'>'|'['|']'|'.'|','))+ -> skip;