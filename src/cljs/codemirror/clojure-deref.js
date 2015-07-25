
/* Example definition of a simple mode that understands a subset of
 * JavaScript:
 */

CodeMirror.defineSimpleMode("simplemode", {
  // The start state contains the rules that are intially used
  start: [
    // The regex matches the token, the token property contains the type
    {regex: /"@"/, token: "deref"},
//    // You can match multiple tokens at once. Note that the captured
//    // groups must span the whole string in this case
//    {regex: /(function)(\s+)([a-z$][\w$]*)/,
//     token: ["keyword", null, "variable-2"]},
//    // Rules are matched in the order in which they appear, so there is
//    // no ambiguity between this one and the one above
//    {regex: /(?:function|var|return|if|for|while|else|do|this)\b/,
//     token: "keyword"},
//    {regex: /true|false|null|undefined/, token: "atom"},
//    {regex: /0x[a-f\d]+|[-+]?(?:\.\d+|\d+\.?\d*)(?:e[-+]?\d+)?/i,
//     token: "number"},
//    {regex: /\/\/.*/, token: "comment"},
//    {regex: /\/(?:[^\\]|\\.)*?\//, token: "variable-3"},
//    // A next property will cause the mode to move to a different state
//    {regex: /\/\*/, token: "comment", next: "comment"},
//    {regex: /[-+\/*=<>!]+/, token: "operator"},
//    // indent and dedent properties guide autoindentation
//    {regex: /[\{\[\(]/, indent: true},
//    {regex: /[\}\]\)]/, dedent: true},
//    {regex: /[a-z$][\w$]*/, token: "variable"},
//    // You can embed other modes with the mode property. This rule
//    // causes all code between << and >> to be highlighted with the XML
//    // mode.
//    {regex: /<</, token: "meta", mode: {spec: "xml", end: />>/}}
  ]
});

CodeMirror.overlayMode(CodeMirror.getMode(config, parserConfig.backdrop || "text/html"), derefOverlay)
//
//CodeMirror.defineMode("clj-deref", function(config, parserConfig) {
//  var derefOverlay = {
//    token: function(stream, state) {
//      var ch;
//      if (stream.match(/@[\W+]/)) {
//        while ((ch = stream.next()) != null)
//        stream.eat(/@[\W+]/);
//        return "clj-deref";
//      }
//      while (stream.next() != null && !stream.match("@", false)) {}
//      return null;
//    }
//  };
//  return CodeMirror.overlayMode(CodeMirror.getMode(config, parserConfig.backdrop || "text/html"), derefOverlay);
//});