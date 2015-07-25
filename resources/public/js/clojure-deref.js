
CodeMirror.defineSimpleMode("derefOverlay", {
  start: [
    {regex: /@\d/, token: "deref", next: "start"},
    {regex: /[^@]*/, token: null}
  ]
});

CodeMirror.defineMode("clojureDeref", function(config, parserConfig) {
  return CodeMirror.overlayMode(CodeMirror.getMode(config, "clojure"), CodeMirror.getMode(config, "derefOverlay"))
})
