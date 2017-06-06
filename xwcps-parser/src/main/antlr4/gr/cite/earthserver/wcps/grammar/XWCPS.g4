grammar XWCPS;

import WCPS, XPath, WCPSLexerTokens;

xwcps : (letClause)* wcpsQuery
	| xpath
	; 
	
xpath: main;

orderByClause: ORDERBY (identifier | xpathClause) (ASC|DESC)?;

letClause: LET identifier ':=' letClauseExpression SEMICOLON;

letClauseExpression : arithmeticExpression
					| processingExpression // TODO can I remove this from here?
					;

/**
 * Example: 
 * for c in ( AvgLandTemp ) return <a>describeCoverage(c)//*[local-name()='domainSet']</a>
 */
xmlClause: openXmlElement xmlPayload closeXmlElement
				| openXmlElement (quated)? (xmlClauseWithQuate)* closeXmlElement 
				| (openXmlWithClose) + 
				;

xmlPayload: 
		  arithmeticExpression
		;

arithmeticExpression :  
				  arithmeticExpression booleanOperator arithmeticExpression
				| arithmeticExpression coverageArithmeticOperator arithmeticExpression
				| coverageExpression coverageArithmeticOperator coverageExpression
				| arithmeticExpression numericalComparissonOperator arithmeticExpression
				| LEFT_PARANTHESIS arithmeticExpression RIGHT_PARANTHESIS 
				| xpathClause
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
xpathClause: metadataExpression (xpath)?
			| scalarExpression (xpath)?
			//| metadataClause (xpath)?
			| functionName LEFT_PARANTHESIS scalarExpression xpath RIGHT_PARANTHESIS;
			
wrapResultClause: WRAP_RESULT LEFT_PARANTHESIS
					processingExpression COMMA  openXmlElement ( wrapResultSubElement )*
					RIGHT_PARANTHESIS;
					
wrapResultSubElement: openXmlElement | xmlClause ;

xpathForClause:  coverageVariableName IN xwcpsCoveragesClause;

xwcpsCoveragesClause: xpath;

mixedClause: MIXED LEFT_PARANTHESIS encodedCoverageExpression COMMA (xmlClause | xpathClause) RIGHT_PARANTHESIS;

//metadataClause: METADATA LEFT_PARANTHESIS coverageVariableName RIGHT_PARANTHESIS;

metadataExpression: coverageVariableName DOUBLE_COLON;

//////overrided wcps rules//////

whereClause: WHERE (booleanScalarExpression | booleanXpathClause );

booleanXpathClause : xpathClause;

// on return
processingExpression: identifier
                    | xmlClause
					| xpathClause
					| wrapResultClause
                    | encodedCoverageExpression
                    | mixedClause
                    ;

wcpsQuery : (forClauseList) (letClause)* (whereClause)? (orderByClause)? (returnClause);

forClauseList: FOR (xwcpsforClause) (COMMA xwcpsforClause)*;

xwcpsforClause: forClause
			| xpathForClause
			;
			
endpointIdentifier: identifier | STRING_LITERAL | XPATH_LITERAL;
						
extendedIdentifier: identifier (AT) endpointIdentifier			#specificIdInServerLabel
			| (MULTIPLICATION) (AT) endpointIdentifier			#allCoveragesInServerLabel
			| (MULTIPLICATION)									#allCoveragesLabel
			| identifier										#specificIdLabel
			;
			
forClause:  coverageVariableName IN
           (LEFT_PARANTHESIS)? (extendedIdentifier) (COMMA (extendedIdentifier))* (RIGHT_PARANTHESIS)?;
