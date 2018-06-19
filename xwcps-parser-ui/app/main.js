editor = undefined;

define(["jquery", "ace"], function($, ace) {
	editor = ace.edit("queryInput");
	editor.setTheme("./theme/chrome");
	editor.session.setMode("./mode/xwcps");
	
	editor.setOptions({
		highlightActiveLine: true,
		highlightSelectedWord: true,
		enableBasicAutocompletion: true,
		enableLiveAutocompletion: false,
		enableSnippets: false
	});
});