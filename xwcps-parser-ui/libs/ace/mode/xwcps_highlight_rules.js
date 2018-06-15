define(function(require, exports, module) {
"use strict";

var oop = require("lib/oop");
var TextHighlightRules = require("mode/text_highlight_rules").TextHighlightRules;

var XwcpsHighlightRules = function() {

    var keywords = (
		"add|and|condense|coverage|div|for|in|let|list|max|min|mod|" +
	    "or|over|overlay|pow|return|struct|using|value|values|where|xor|@|::"
    );
    

    var builtinConstants = (
        "true|false"
    );

    var builtinFunctions = (
        "abs|all|arccos|arcsin|arctan|avg|bit|cos|cosh|count|crstransform|" +
        "decode|describe_coverage|encode|exp|extend|id|im|imgcrsdomain|ln|log|" +
        "mixed|not|re|round|scale|sin|sinh|slice|some|sqrt|tan|tanh|trim|wrap-result"
    );

    var dataTypes = (
        "int|numeric|decimal|date|varchar|char|bigint|float|double|bit|binary|text|set|timestamp|" +
        "real|number|integer"
    );

    var keywordMapper = this.createKeywordMapper({
        "support.function": builtinFunctions,
        "keyword": keywords,
        "constant.language": builtinConstants,
        "storage.type": dataTypes
    }, "identifier", true);

    this.$rules = {
        "start" : [ {
            token : "comment",
            regex : "--.*$"
        }, {
            token : "string",           // " string
            regex : '".*?"'
        }, {
            token : "string",           // ' string
            regex : "'.*?'"
        }, {
            token : "constant.numeric", // float
            regex : "[+-]?\\d+(?:(?:\\.\\d*)?(?:[eE][+-]?\\d+)?)?\\b"
        }, {
            token : keywordMapper,
            regex : "[a-zA-Z_$][a-zA-Z0-9_$]*\\b"
        }, {
            token : "keyword.operator",
            regex : "\\+|\\-|\\/|\\/\\/|%|<@>|@>|<@|&|\\^|~|<|>|<=|=>|==|!=|<>|="
        }, {
            token : "paren.lparen",
            regex : "[\\(]"
        }, {
            token : "paren.rparen",
            regex : "[\\)]"
        }, {
            token : "text",
            regex : "\\s+"
        } ]
    };
    this.normalizeRules();
};

oop.inherits(XwcpsHighlightRules, TextHighlightRules);

exports.XwcpsHighlightRules = XwcpsHighlightRules;
});

