define(function(require, exports, module) {
"use strict";

var oop = require("../lib/oop");
var TextMode = require("./text").Mode;
var XwcpsHighlightRules = require("./xwcps_highlight_rules").XwcpsHighlightRules;
var Range = require("../range").Range;

var Mode = function() {
    this.HighlightRules = XwcpsHighlightRules;
};
oop.inherits(Mode, TextMode);

(function() {

    this.lineCommentStart = "--";

    this.$id = "mode/xwcps";
}).call(Mode.prototype);

exports.Mode = Mode;

});