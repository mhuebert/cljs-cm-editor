
CodeMirror.defineSimpleMode("derefOverlay", {
  start: [
    {regex: /@[^@\s(){}\[\]]+/, token: "deref"},
    {regex: /[^@]+/, token: null}
  ]
});

CodeMirror.defineMode("clojureDeref", function(config, parserConfig) {
  return CodeMirror.overlayMode(CodeMirror.getMode(config, "clojure"), CodeMirror.getMode(config, "derefOverlay"))
})
