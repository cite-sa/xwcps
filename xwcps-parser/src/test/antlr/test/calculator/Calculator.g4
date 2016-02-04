grammar Calculator;

c: e;

e: term subexpression;

subexpression: opsum term subexpression
	|;
	
term: factor subterm;

subterm: opfact factor subterm
	|;

factor: NUMBER
	| '(' e ')';

	
opsum: '+'
	| '-';

opfact: '*'
	| '/';

NUMBER: [0-9]+;

WS : [ \t\r\n]+ -> skip ;

