# xWCPS Definition

xWCPS (XPath Enabled WCPS), is a Query Language (QL) defined in the context of EarthServer 2 Project (http://wwww.earthserver.eu/) aiming to merge two widely adopted standards, namely XPath 2.0 because of it's potentials on XML metadata handling and WCPS's raster data processing abilities, into a new construct, which enables simultaneous search on both XML-encoded metadata and OGC coverages.

xWCPS enables search on both array data / coverages and metadata, allows filtering of coverages based on metadata and eliminates the WCPS requirement for explicitly declaring the
coverages’ IDs.

xWCPS follows FLWOR expression paradigm and provides appropriate place holders that enable any XPath or WCPS or combined query to be expressed in its syntax. XPath clauses that address specific parts of metadata stored in XML documents.

## xWCPS EBNF

The top level production rules of xWCPS, in terms of  EBNF, demonstrate the structural XPath 2.0 and WCPS elements that compose the combined language.

```
xwcps: (letClause)* wcpsQuery
		| xpath; 
		
wcpsQuery: (forClauseList) (letClause)* (whereClause)? (returnClause) ;

forClauseList: FOR (xwcpsforClause) (COMMA xwcpsforClause)*;

letClause: LET identifier ':=' letClauseExpression; 

whereClause: WHERE (booleanScalarExpression | booleanXpathClause );

returnClause: RETURN processingExpression;

processingExpression: xmlClause
					| xpathClause
					| wrapResultClause
                    | encodedCoverageExpression
                    | mixedClause
                    ;
```				
					
## xWCPS Grammar

Each xWCPS FLWOR expression is defined along the following principles:
* The first two xWCPS clauses for, let can appear any number of times in any order.
* Let, Where and Order By clauses are optional.
* Variable definition (referred as identifier) follows WCPS conventions, therefore variable names can start with or without $-sign
* Each variable defined in a for clause is bound either to an XPath (in order to identify coverages via metadata) or to a WCPS Coverage list.
* Where clause allows any boolean expression of XPath and WCPS conditions.
* Every valid boolean/logical XPath or WCPS expression is a valid xWCPS where expression.
* Results can be wrapped in custom xml, so as to address user needs for custom response-formatting, e.g. an HTML response.
* xWCPS permits the creation of mixed results, containing both array data and metadata, through a function, namely mixed().
* Metadata can be fetched through operator "::" applied in any variable. 
* xWCPS Queries must always have a “return” clause.
* The Return statement of a query can contain textual results, structured XML results, WCPS encoded (i.e. png, tiff, csv) results, or combinations of binary and textual results into various formats.


#### The For Clause

The for clause binds a variable to each item returned by the in expression. There are 3 options that can be used in a for clause:

* Use all available coverages: *
* Use all coverages of a specific service: *@"endpoint" (endpoint can be a url with double quotes or alias)
* Use specific coverages: coverageId or coverageId@"endpoint" (endpoint can be a url with double quotes or alias)

Thus returned items are either always coverage ids. The for clause results in iteration. There can be multiple for clauses in the same FLWOR expression.

##### For Clause EBNF

```
forClauseList: FOR (xwcpsforClause) (COMMA xwcpsforClause)*;

xwcpsforClause: forClause
				| xpathForClause
				;
                
forClause:  coverageVariableName IN
           (LEFT_PARANTHESIS)? (extendedIdentifier) (COMMA (extendedIdentifier))* (RIGHT_PARANTHESIS)?;
           
extendedIdentifier: identifier (AT) endpointIdentifier					#specificIdInServerLabel
					| (MULTIPLICATION) (AT) endpointIdentifier			#allCoveragesInServerLabel
					| (MULTIPLICATION)									#allCoveragesLabel
					| identifier										#specificIdLabel
					;
            
xpathForClause:  coverageVariableName IN xwcpsCoveragesClause;

xwcpsCoveragesClause: xpath;

```	

###### Examples

* WCPS like:

        for $cov in (coverage1, coverage2, coverage3)

* All coverages:

        for $cov in (*)

* All coverages in specific endpoint:

        for $cov in (*@endpoint1)

* Specific coverage in specific endpoint:

        for $cov in (coverage1@endpoint1)		 


#### The Where Clause

The where clause is used to specify one or more metadata or coverage related criteria for filtering down the returned result. Currently combined data and metadata join operations are not allowed in the context of xWCPS. Every XPath or WCPS expression evaluating to a boolean result is a valid xWCPS comparison expression. To declare an xPath expression the “::” notation should follow the variable.

##### Where Clause EBNF

```
whereClause: WHERE (booleanScalarExpression | booleanXpathClause );

booleanXpathClause : xpathClause;

xpathClause: metadataExpression (xpath)?
			| scalarExpression (xpath)?
			| functionName LEFT_PARANTHESIS scalarExpression xpath RIGHT_PARANTHESIS;
            
metadataExpression: coverageVariableName DOUBLE_COLON;

```

###### Examples

* WCPS like:

        where avg($c.red) > 200) 

* Xpath:
      
        $c:://*[local-name()='RectifiedGrid'][@dimension=2]


#### The Return Clause

The role of the return clause is twofold. The return clause from the one hand specifies what is to be returned and from the other hand the way that this result should be represented. Thus we can have the following options:

* Use the encode function of WCPS -> WCPS result
* Use "::" operator -> XML result
* Use an xPath 2.0 expression / function -> XML result
* Use the new “mixed” function to combine both -> multipart result

##### Return Clause EBNF

```
returnClause: RETURN processingExpression;

processingExpression: xmlClause
					| xpathClause
					| wrapResultClause
                    | encodedCoverageExpression
                    | mixedClause
                    ;
                    
xmlClause: openXmlElement xmlPayload closeXmlElement
			| openXmlElement (quated)? (xmlClauseWithQuate)* closeXmlElement 
            | (openXmlWithClose) + 
            ;
                
xpathClause: metadataExpression (xpath)?
			| scalarExpression (xpath)?
			| functionName LEFT_PARANTHESIS scalarExpression xpath RIGHT_PARANTHESIS;
            
wrapResultClause: 	WRAP_RESULT LEFT_PARANTHESIS
					processingExpression COMMA  openXmlElement ( wrapResultSubElement )*
					RIGHT_PARANTHESIS;
                    
encodedCoverageExpression: ENCODE LEFT_PARANTHESIS
                           coverageExpression COMMA /* FORMAT_NAME */ STRING_LITERAL (COMMA STRING_LITERAL)*
                           RIGHT_PARANTHESIS;

mixedClause: MIXED LEFT_PARANTHESIS encodedCoverageExpression COMMA (xmlClause | xpathClause) RIGHT_PARANTHESIS;

```

###### Examples

* WCPS like:

        for $c1 in (*@endpoint1)
        return  <result>
        		    <coverageDescription>
                		$c:://*[local-name()='lowerCorner']
                	</coverageDescription>
                </result>

* XPath like:

        for $c1 in (coverage1, coverage2, coverage3)
        return encode($c1, "csv")

* Combined:

        for $c1 in (*@endpointAlias1)
        return mixed(encode($c1, "csv"), $c1::)

#### The Let Clause

Let clause is for variable assignment and re-use.

#### The Order by Clause

Order By clause is for results' ordering.

					   
## xWCPS XPath Examples

In the context of xWCPS, XPath is mostly employed for the following use cases

###### Examples

* Return the metadata of the coverage with ID: mslp

		for $c in ( mslp ) return $c::

* Return the metadata of all available coverages of the service with alias name: ECMWF

		for $c in *@ECMWF return $c::
        
* Filter coverages through an attribute:

		for $c in * 
		where $c:://*[local-name()='RectifiedGrid'][@dimension=2] 
		return encode($c, "csv")

* Returns both data and metadata of a specific coverage ID:

		for $c in (CCI_V2_monthly_chlor_a)
		return mixed(encode ($c[ansi("2001-07-31T23:59:00")] * 1000 , "png"), $c:://wcs:CoverageId)

* Return the coverage IDs of the PlanetService service which are 2D and of type RectifiedGrid:

		for $c in *@PlanetServer 
        where $c:://*[localname()='RectifiedGrid'][@dimension=2] 
        return $c:://*[local-name()='RectifiedGrid']

* Retrieve specific metadata from coverages:
	
        for $cov in * 
        return $c:://*[local-name()='lowerCorner']
