grammar XWCPS;

import WCPS, XPath, WCPSLexerTokens;

xwcps : (letClause)* wcpsQuery
	| xpath
	; 
	
xpath: main;

letClause: LET identifier ':=' letClauseExpression; 

letClauseExpression : coverageExpression | processingExpression;

/**
 * Example: 
 * for c in ( AvgLandTemp ) return <a>describeCoverage(c)//*[local-name()='domainSet']</a>
 */
xmlClause: openXmlElement xmlPayload closeXmlElement
				| openXmlElement (quated)? (xmlClauseWithQuate)* closeXmlElement 
				| (openXmlWithClose) + 
				;
xmlPayload: 
		  xpathClause
		| coverageExpression
		;

xmlClauseWithQuate: xmlClause (quated)?;

openXmlElement: xmlElement GREATER_THAN; 

openXmlWithClose: xmlElement GREATER_THAN_SLASH;

xmlElement: LOWER_THAN (qName) (attribute)*;

attribute: qName EQUAL quated 
		 | qName EQUAL xpathClause
		 ;

quated: ( XPATH_LITERAL | STRING_LITERAL);
			
closeXmlElement: LOWER_THAN_SLASH qName GREATER_THAN;

/**
 * Example: 
 * for c in ( AvgLandTemp ) return describeCoverage(c)//*[local-name()='domainSet']
 * for c in ( AvgLandTemp ) return min(describeCoverage(c)//*[local-name()='domainSet']//@anyattr)
 */
xpathClause: scalarExpression (xpath)?
				| identifier // TODO remove from this rule
				| functionName LEFT_PARANTHESIS scalarExpression xpath RIGHT_PARANTHESIS;

wrapResultClause: WRAP_RESULT LEFT_PARANTHESIS
					processingExpression COMMA  openXmlElement ( wrapResultSubElement )*
					RIGHT_PARANTHESIS;
					
wrapResultSubElement: openXmlElement | xmlClause ;

xpathForClause:  coverageVariableName IN xwcpsCoveragesClause;

xwcpsCoveragesClause: xpath;

/*
 * overrided wcps rules
 */

// on return
processingExpression: xmlClause
					| xpathClause
					| wrapResultClause
                    | encodedCoverageExpression;

wcpsQuery : (forClauseList) (letClause)* (whereClause)? (returnClause) ;

forClauseList: FOR (xwcpsforClause) (COMMA xwcpsforClause)*;

xwcpsforClause: forClause
			| xpathForClause
			;

