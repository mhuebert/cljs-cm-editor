{:foreign-libs
          [{:file "codemirror/codemirror-min.js"
            :provides ["CodeMirror"]}
           {:file "codemirror/codemirror-all.js"
            :provides ["CodeMirror-all"]}
           {:file "codemirror/overlay.js"
            :provides ["CodeMirror-overlay"]
            :requires ["CodeMirror"]}
           {:file "codemirror/clojure-deref.js"
            :provides ["CodeMirror-deref"]
            :requires ["CodeMirror" "CodeMirror-overlay" "CodeMirror-simple"]}
           {:file "codemirror/subpar-with-keymap.js"
            :provides ["CodeMirror-subpar"]
            :requires ["CodeMirror"]}
           {:file "codemirror/match-brackets.js"
            :provides ["CodeMirror-match-brackets"]
            :requires ["CodeMirror"]}
           {:file "codemirror/simple-mode.js"
            :provides ["CodeMirror-simple"]
            :requires ["CodeMirror"]}]
 :externs ["codemirror/codemirror-externs.js"]}
