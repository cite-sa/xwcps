grammar XWCPS;

import WCPS, WCPSLexerTokens;

xwcps : wcpsQuery
	| xquery
	; 
	
xquery: main;


/**
 * Example: 
 * for c in ( AvgLandTemp ) return <a>describeCoverage(c)//*[local-name()='domainSet']</a>
 */
xmlReturnClause: openXmlElement xwcpsReturnClause closeXmlElement
				| openXmlElement xmlReturnClause closeXmlElement ;

openXmlElement: LOWER_THAN (qName) (attribute)* GREATER_THAN; 

attribute: qName EQUAL quated 
		 | qName EQUAL xwcpsReturnClause
		 ;

quated: ( XPATH_LITERAL | STRING_LITERAL);
			
closeXmlElement: LOWER_THAN_SLASH qName GREATER_THAN;

/**
 * Example: 
 * for c in ( AvgLandTemp ) return describeCoverage(c)//*[local-name()='domainSet']
 * for c in ( AvgLandTemp ) return min(describeCoverage(c)//*[local-name()='domainSet']//@anyattr)
 */
xwcpsReturnClause: scalarExpression (xquery)?
				| functionName LEFT_PARANTHESIS scalarExpression xquery RIGHT_PARANTHESIS;

xpathForClause:  coverageVariableName IN xwcpsCoveragesClause;

xwcpsCoveragesClause: xquery;

/*
 * overrided wcps rules
 */
processingExpression: xmlReturnClause
					| xwcpsReturnClause
                    | encodedCoverageExpression;

forClauseList: FOR (xwcpsforClause) (COMMA xwcpsforClause)*;

xwcpsforClause: forClause
			| xpathForClause
			;

/* 
 * xquery grammar pasted bellow 
 * (the current version of antlr doen't flawlessly support multi-import)
 */
main  :  expr
  ;

locationPath 
  :  relativeLocationPath
  |  absoluteLocationPathNoroot
  ;

absoluteLocationPathNoroot
  :  '/' relativeLocationPath
  |  '//' relativeLocationPath
  ;

relativeLocationPath
  :  step (('/'|'//') step)*
  ;

step  :  axisSpecifier nodeTest predicate*
  |  abbreviatedStep
  ;

axisSpecifier
  :  AxisNameXpath '::'
  |  '@'?
  ;

nodeTest:  nameTest
  |  NodeType '(' ')'
  |  'processing-instruction' '(' ( XPATH_LITERAL | STRING_LITERAL) ')'
  ;

predicate
  :  '[' expr ']'
  ;

abbreviatedStep
  :  '.'
  |  '..'
  ;

expr  :  orExpr
  ;

primaryExpr
  :  variableReference
  |  '(' expr ')'
  |  ( XPATH_LITERAL | STRING_LITERAL)
  |  REAL_NUMBER_CONSTANT
  |  functionCall
  ;

functionCall
  :  functionName '(' ( expr ( ',' expr )* )? ')'
  ;

unionExprNoRoot
  :  pathExprNoRoot ('|' unionExprNoRoot)?
  |  '/' '|' unionExprNoRoot
  ;

pathExprNoRoot
  :  locationPath
  |  filterExpr (('/'|'//') relativeLocationPath)?
  ;

filterExpr
  :  primaryExpr predicate*
  ;

orExpr  :  andExpr ('or' andExpr)*
  ;

andExpr  :  equalityExpr ('and' equalityExpr)*
  ;

equalityExpr
  :  relationalExpr (('='|'!=') relationalExpr)*
  ;

relationalExpr
  :  additiveExpr ((LOWER_THAN | GREATER_THAN | LOWER_OR_EQUAL_THAN | GREATER_OR_EQUAL_THAN) additiveExpr)*
  ;

additiveExpr
  :  multiplicativeExpr (('+'|'-') multiplicativeExpr)*
  ;

multiplicativeExpr
  :  unaryExprNoRoot (('*'|'div'|'mod') multiplicativeExpr)?
  |  '/' (('div'|'mod') multiplicativeExpr)?
  ;

unaryExprNoRoot
  :  '-'* unionExprNoRoot
  ;

qName  :  nCName (':' nCName)?
  ;

functionName
  :  qName  // Does not match nodeType, as per spec.
  ;

variableReference
  :  '$' qName
  ;

nameTest:  '*'
  |  nCName ':' '*'
  |  qName
  ;

nCName  :  NCName | SIMPLE_IDENTIFIER_WITH_NUMBERS | SIMPLE_IDENTIFIER
  |  AxisNameXpath
  |  wcpsHotWords
  ;

wcpsHotWords:  FOR
	|  ABSOLUTE_VALUE
	|  ADD
	|  ALL
	|  AND
	|  ARCSIN
	|  ARCCOS
	|  ARCTAN
	|  AVG
	|  BIT
	|  CONDENSE
	|  COS
	|  COSH
	|  COUNT
	|  COVERAGE
	|  CRS_TRANSFORM
	|  DECODE
	|  DESCRIBE_COVERAGE
	|  ENCODE
	|  EXP
	|  EXTEND
	|  FALSE 
	|  IMAGINARY_PART
	|  ID
	|  IMGCRSDOMAIN
	|  IN
	|  LN
	|  LIST
	|  LOG
	|  MAX
	|  MIN
	|  NOT
	|  OR
	|  OVER
	|  OVERLAY
	|  POWER
	|  REAL_PART
	|  ROUND
	|  RETURN
	|  SCALE
	|  SIN
	|  SINH
	|  SLICE
	|  SOME
	|  SQUARE_ROOT
	|  STRUCT
	|  TAN
	|  TANH
	|  TRIM
	|  TRUE
	|  USING
	|  VALUE
	|  VALUES
	|  WHERE
	|  XOR
	;
