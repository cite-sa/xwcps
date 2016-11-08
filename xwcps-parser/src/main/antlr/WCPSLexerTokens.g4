/**
 * This file contains all the tokens needed in the wcps grammar.
 * To add a new token, use the grammar provided by antlr and place the token in its corresponding place in alphabetical order.
 *
 * @author Alex Dumitru <alex@flanche.net>
 * @author Vlad Merticariu <vlad@flanche.net>
 */
lexer grammar WCPSLexerTokens;

NodeType:  'comment'
  |  'text'
  |  'processing-instruction'
  |  'node'
  ;
  
AxisNameXpath:  'ancestor'
  |  'ancestor-or-self'
  |  'attribute'
  |  'child'
  |  'descendant'
  |  'descendant-or-self'
  |  'following'
  |  'following-sibling'
  |  'namespace'
  |  'parent'
  |  'preceding'
  |  'preceding-sibling'
  |  'self'
  ;
  
//ENDPOINT_LITERAL: START_CHARS* ENDPOINT_PART*;

/**
 * This file contains all the tokens needed in the wcps grammar.
 * To add a new token, use the grammar provided by antlr and place the token in its corresponding place in alphabetical order.
 *
 * @author Alex Dumitru <alex@flanche.net>
 * @author Vlad Merticariu <vlad@flanche.net>
 */
//lexer grammar WCPSLexerTokens;
FOR: ('f'|'F')('o'|'O')('r'|'R');
ABSOLUTE_VALUE: ('a'|'A')('b'|'B')('s'|'S');
ADD: ('a'|'A')('d'|'D')('d'|'D');
ALL: ('a'|'A')('l'|'L')('l'|'L');
AND: ('a'|'A')('n'|'N')('d'|'D');
AT: '@';
ARCSIN: ('a'|'A')('r'|'R')('c'|'C')('s'|'S')('i'|'I')('n'|'N');
ARCCOS: ('a'|'A')('r'|'R')('c'|'C')('c'|'C')('o'|'O')('s'|'S');
ARCTAN: ('a'|'A')('r'|'R')('c'|'C')('t'|'T')('a'|'A')('n'|'N');
AVG: ('a'|'A')('v'|'V')('g'|'G');
BIT: ('b'|'B')('i'|'I')('t'|'T');
COLON : ':';
COMMA : ',';
CONDENSE: ('c'|'C')('o'|'O')('n'|'N')('d'|'D')('e'|'E')('n'|'N')('s'|'S')('e'|'E');
COS: ('c'|'C')('o'|'O')('s'|'S');
COSH: ('c'|'C')('o'|'O')('s'|'S')('h'|'H');
COUNT:('c'|'C')('o'|'O')('u'|'U')('n'|'N')('t'|'T');
COVERAGE: ('c'|'C')('o'|'O')('v'|'V')('e'|'E')('r'|'R')('a'|'A')('g'|'G')('e'|'E');
COVERAGE_VARIABLE_NAME_PREFIX: '$';
CRS_TRANSFORM: ('c' | 'C')('r' | 'R')('s' | 'S')('t' | 'T')('r' | 'R')('a' | 'A')('n' | 'N')('s' | 'S')('f' | 'F')('o' | 'O')('r' | 'R')('m' | 'M');
DECODE: ('d' | 'D')('e' | 'E')('c' | 'C')('o' | 'O')('d' | 'D')('e' | 'E');
DESCRIBE_COVERAGE: ('d' | 'D')('e' | 'E')('s' | 'S')('c' | 'C')('r' | 'R')('i' | 'I')('b' | 'B')('e' | 'E')('c' | 'C')('o' | 'O')('v' | 'V')('e' | 'E')('r' | 'R')('a' | 'A')('g' | 'G')('e' | 'E');
DIV: ('d' | 'D')('i' | 'I')('v' | 'V');
DIVISION: '/';
DOT: '.';
DOUBLE_COLON: '::';
ENCODE: ('e' | 'E')('n' | 'N')('c' | 'C')('o' | 'O')('d' | 'D')('e' | 'E');
EQUAL: '=';
EXP: ('e'|'E')('x'|'X')('p'|'P');
EXTEND: ('e' | 'E')('x' | 'X')('t' | 'T')('e' | 'E')('n' | 'N')('d' | 'D');
FALSE : ('F' | 'f')('A' | 'a')('L' | 'l')('S' | 's')('E' | 'e');
GREATER_THAN: '>';
GREATER_THAN_SLASH: '/>';
GREATER_OR_EQUAL_THAN: '>=';
IMAGINARY_PART: ('i'|'I')('m'|'M');
ID:	('i'|'I')('d'|'D');
IMGCRSDOMAIN: ('i'|'I')('m'|'M')('g'|'G')('c'|'C')('r'|'R')('s'|'S')('d'|'D')('o'|'O')('m'|'M')('a'|'A')('i'|'I')('n'|'N');
IN:	('i'|'I')('n'|'N');
LEFT_BRACE: '{';
LEFT_BRACKET: '[';
LEFT_PARANTHESIS: '(';
LET: ('l'|'L')('e' | 'E')('t'|'T');
LN: ('l'|'L')('n'|'N');
LIST: ('l'|'L')('i'|'I')('s'|'S')('t'|'T');
LOG: ('l'|'L')('o'|'O')('g'|'G');
LOWER_THAN: '<';
LOWER_THAN_SLASH: '</';
LOWER_OR_EQUAL_THAN: '<=';
MAX:('m'|'M')('a'|'A')('x'|'X');
METADATA: ('m' | 'M')('e' | 'E')('t' | 'T')('a' | 'A')('d' | 'D')('a' | 'A')('t' | 'T')('a' | 'A');
MIN: ('m'|'M')('i'|'I')('n'|'N');
MINUS: '-';
MIXED:('m'|'M')('i'|'I')('x'|'X')('e'|'E')('d'|'D');
MOD: ('m'|'M')('o'|'O')('d'|'D');
MULTIPLICATION: '*';
NOT: ('n'|'N')('o'|'O')('t'|'T');
NOT_EQUAL: '!=';
OR: ('o'|'O')('r'|'R');
OVER:('o'|'O')('v'|'V')('e'|'E')('r'|'R');
OVERLAY: ('o'|'O')('v'|'V')('e'|'E')('r'|'R')('l'|'L')('a'|'A')('y'|'Y');
PLUS: '+';
POWER: ('p'|'P')('o'|'O')('w'|'W');
REAL_PART: ('r'|'R')('e'|'E');
ROUND: ('r'|'R')('o'|'O')('u'|'U')('n'|'N')('d'|'D');
RETURN: ('r'|'R')('e'|'E')('t'|'T')('u'|'U')('r'|'R')('n'|'N');
RIGHT_BRACE: '}';
RIGHT_BRACKET: ']';
RIGHT_PARANTHESIS: ')';
SCALE: ('s'|'S')('c'|'C')('a'|'A')('l'|'L')('e'|'E');
SEMICOLON: ';';
SIN: ('s'|'S')('i'|'I')('n'|'N');
SINH: ('s'|'S')('i'|'I')('n'|'N')('h'|'H');
SLICE: ('s'|'S')('l'|'L')('i'|'I')('c'|'C')('e'|'E');
SOME:('s'|'S')('o'|'O')('m'|'M')('e'|'E');
SQUARE_ROOT: ('s'|'S')('q'|'Q')('r'|'R')('t'|'T');
STRUCT: ('s'|'S')('t'|'T')('r'|'R')('u'|'U')('c'|'C')('t'|'T');
TAN: ('t'|'T')('a'|'A')('n'|'N');
TANH: ('t'|'T')('a'|'A')('n'|'N')('h'|'H');
TRIM: ('T' | 't')('R' | 'r')('I' | 'i')('M' | 'm');
TRUE: ('T' | 't')('R' | 'r')('U' | 'u')('E' | 'e');
USING: ('u'|'U')('s'|'S')('i'|'I')('n'|'N')('g'|'G');
VALUE:('v'|'V')('a'|'A')('l'|'L')('u'|'U')('e'|'E');
VALUES:('v'|'V')('a'|'A')('l'|'L')('u'|'U')('e'|'E')('s'|'S');
WHERE: ('w'|'W')('h'|'H')('e'|'E')('r'|'R')('e'|'E');
WRAP_RESULT: ('w'|'W')('r'|'R')('a'|'A')('p'|'P')'-'('r'|'R')('e'|'E')('s'|'S')('u'|'U')('l'|'L')('t'|'T');
XOR: ('x'|'X')('o'|'O')('r'|'R');
REAL_NUMBER_CONSTANT:'-'?NUMBERS+('.'NUMBERS*)?;
GGG: ('g'|'G')('g'|'G')('g'|'G');

SIMPLE_IDENTIFIER: START_CHARS + ;

SIMPLE_IDENTIFIER_WITH_NUMBERS: (START_CHARS | NUMBERS)+;

//COVERAGE_VARIABLE_NAME: '$'[a-zA-Z0-9_]+; disabled for backwards compatibility with WCPS1
IDENTIFIER: '$' SIMPLE_IDENTIFIER_WITH_NUMBERS; // added $ for backwards compatibility with WCPS1
NAME: [a-z|A-Z]+;
//FORMAT_NAME: replaced with STRING_LITERAL for backward compatibility with WCPS1. The regex for a valid mime type is: '"'[a-zA-Z0-9!#$&.+-^_]+'/'[a-zA-Z0-9!#$&.+-^_]+'"'
STRING_LITERAL: '"'[a-zA-Z0-9!#$&.+-^_]+'"';
WS: [ \n\t\r]+ -> skip;

XPATH_LITERAL  :  '"' ~'"'* '"'
  |  '\'' ~'\''* '\''
  ;
    
// xpath 
//NCName  :  NCNameStartChar NCNameChar*; 
NCName  :  START_CHARS [a-zA-Z0-9_\-]*; // removed '.' -- START_CHARS [a-zA-Z0-9_\-.]*

fragment NUMBERS: [0-9];

fragment START_CHARS: [a-zA-Z_];

