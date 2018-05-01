grammar try;

//parser

definition   :   (fundef | varsdef | classdef )* EOF ;

//classdefinition
classname
        :   ID
        ;

classdef:   'class' ID '{'
        (   fundef
            | varsdef
            )* '}';

//functiondefinition
funname :   ID;

fundef   :   type ID '(' params ')' block ;

//variabledefinition
varname :   ID;

varsdef :   type  ID ('='  expr)? ';' ;

vardef  :   type  ID ('='  expr)? ;

//parameter
params  :   (param  (',' param)*)? ;

param   :   type  ID ('='  expr)? ;

//typeType
typename:   ID;

basetype:   ('int' | 'bool' | 'void' | 'string') ;

arraytype:  (basetype | typename)  ('[' ']')* ;

type    :   (basetype | typename | arraytype);

//block
block   :   '{' stmt* '}' ;

//statement
stmt
        :   block                                                   #blockStmt
        |   varsdef                                                 #varDeclStmt
        |   'if' '(' expr ')' stmt ('else' stmt)?                   #ifStmt
        |   'for' '(' start = expr? ';'
                      condition = expr? ';'
                      update = expr? ')' stmt                       #forStmt
        |   'while' '(' expr? ')' stmt                              #whilStmt
        |   'return' expr? ';'                                      #returnStmt
        |   'break' ';'                                             #breakStmt
        |   'continue' ';'                                          #continueStmt
        |   ';'                                                     #emptyStmt
        |   expr ';'                                                #exprStmt
        ;

//expression
exprs   :   expr(',' expr)* ;

expr    :   funname '(' exprs? ')'                                              #callExpr
        |   'new' creator                                                       #newExpr
//      |   expr (opcom = '[' expr ']')+
        |   expr '[' expr ']'                                                   #arrayExpr
        |   '(' expr ')'                                                        #subExpr
//      |   expr op = '.' expr
        |   expr '.' (ID | funname '(' exprs? ')')                              #memberExpr
        |   op = ('++' | '--') expr                                             #prefixExpr
        |   op = ('-' | '!' | '~'| '+' ) expr                                   #prefixExpr
        |   expr op =('++'  | '--')                                             #suffixExpr
        |   expr ('*' | '/' | '%') expr                                         #binaryExpr
        |   expr op=('+' |'-' ) expr                                            #binaryExpr
        |   expr ('>>' | '<<') expr                                             #binaryExpr
        |   expr '&' expr                                                       #binaryExpr
        |   expr '^' expr                                                       #binaryExpr
        |   expr '|' expr                                                       #binaryExpr
        |   expr  op = ('>' | '<' | '>=' | '<=' | '==' | '!=') expr             #binaryExpr
        |   expr  op = '&&' expr                                                #andExpr
        |   expr  op ='||' expr                                                 #orExpr
//      |   expr '?' expr ':' expr
        |   varname                                                             #idExpr
        |   NUM                                                                 #intConstExpr
        |   STR                                                                 #stringConstExpr
        |   ('NULL' | 'null')                                                   #nullExpr
        |   (TRUE | FALSE)                               #boolConstExpr
        |   <assoc=right> expr '=' expr                                         #assignExpr
        |   'this'                                                              #thisExpr
        ;
creator :   classname                                                           #nonArrayCreator
        |   (classname | typename) ('[' expr ']')+ ('[' ']')+('[' expr ']')+    #wrongCreator
        |   (classname | typename) ('[' expr ']')+ ('[' ']')*                   #arrayCreator
        ;

//lexxer

STR  : '"' ('\\"' | '\\\\'|.)*? '"' ;

ID
    :	[a-zA-Z_] [a-zA-Z_0-9]*
    ;

NUM :  [1-9] [0-9]* | '0' ;

WS : ( ' ' | '\t' | '\n' | '\r' ) + -> skip;

BLOCK_COMMENT
    : '/*' .*? '*/' -> skip
    ;

LINE_COMMENT
    : '//' ~[\r\n]* -> skip
    ;

TRUE    : ('true' | 'TRUE');

FALSE   : ('false'|'FALSE');