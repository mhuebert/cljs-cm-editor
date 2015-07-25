(ns cljs-cm-editor.core
    (:require [reagent.core :as r :refer [cursor]]
              [CodeMirror]
              [CodeMirror-simple]
              [CodeMirror-overlay]
              #_[CodeMirror-deref]))

(defonce editor-index (r/atom {}))
(defonce editor-currently-focused (r/atom nil))

(defn cm-instance [editor]
  (-> editor r/state-atom deref :editor))

(defn focus-last-editor []
  (js/setTimeout #(-> @editor-index last last cm-instance .focus) 15))

(defn get-editor [id]
  (cm-instance (id @editor-index)))

(defn focus-editor [id]
  (.focus (get-editor id)))

(def cm-defaults {
                  :lineNumbers     false
                  :lineWrapping    true
                  :styleActiveLine true
                  #_:scrollbarStyle #_"null"
                  :theme           "solarized dark"

                  :mode            "clojure"})

(defn safe-set [editor value]
  (let [cursor-pos (.getCursor editor)]
    (.setValue editor value)
    (if (-> editor (aget "state") (aget "focused"))
      (.setCursor editor cursor-pos))))

(defn cm-editor-static
  ([a] (cm-editor-static a {}))
  ([a options]
    (r/create-class
      {:component-did-mount #(let [node (.getDOMNode %)
                                   config (clj->js (merge cm-defaults options))
                                   editor (.fromTextArea js/CodeMirror node config)
                                   coerce (fn [x]
                                            (if (string? x) x (str x)))
                                   val (coerce @a)]
                              (add-watch a nil (fn [_ _ _ source]
                                                 (let [source (coerce source)]
                                                   (if (not= source (.getValue editor))
                                                     (.setValue editor source)))))
                              (if (:on-click options)
                                (.on editor "mousedown" (:on-click options)))
                              (r/set-state % {:editor editor :a a})
                              (.setValue editor val))

       :reagent-render      (fn []
                              [:textarea {:style {:width "100%" :height "100%" :display "flex" :background "red" :flex 1}}])})))

(defn cm-editor
  ([a] (cm-editor a {}))
  ([a options]
   (r/create-class
     {:component-did-mount    #(let [node (.getDOMNode %)
                                     config (clj->js (merge cm-defaults options))
                                     editor (.fromTextArea js/CodeMirror node config)
                                     val (or @a "")
                                     id (or (:id options) (+ 1 (count @editor-index)))]
                                (r/set-state % {:editor editor :id id :a a})
                                (swap! editor-index merge {id %})
                                (.setValue editor val)
                                (add-watch a nil (fn [_ _ _ new-state]
                                                   (if (not= new-state (.getValue editor))
                                                     (safe-set editor (or new-state "")))))
                                (.on editor "change" (fn [_]
                                                       (let [value (.getValue editor)]
                                                         (reset! a value))))

                                (.on editor "focus"
                                     (fn [_] (reset! editor-currently-focused id)))

                                (if-not (empty? (:click-coords options))
                                  (let [[x y] (:click-coords options)
                                        pos (.coordsChar editor (clj->js {:left x :top y}))]
                                    (.focus editor)
                                    (.setCursor editor pos)
                                    )))

      :component-will-unmount #(let [{:keys [id editor]} (r/state %)]
                                (swap! editor-index dissoc id)
                                (.off editor))

      :reagent-render         (fn []
                                [:textarea {:style {:width "100%" :height "100%" :display "flex" :background "red" :flex 1}
                                            }])})))



(defn ^:export main []
  (let [content (r/atom "(+ 1 2 @3)")]
    (r/render [:div {:style {:margin 30}}
               [cm-editor content {:mode  "clojureDeref"
                                   :theme :3024-day}]]
              (.getElementById js/document "app"))))
