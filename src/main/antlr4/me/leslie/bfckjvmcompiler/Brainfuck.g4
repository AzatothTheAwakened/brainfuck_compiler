grammar Brainfuck;

/*
    Parser
*/

prog : statement+
     | math
     ;

statement : LEFT #lshift
          | RIGHT #rshift
          | PUT #put
          | GET #get
          | LOOPB statements=(statement|math)+ LOOPE #loop
          ;

math : INC+ #increase
     | DEC+ #decrease
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