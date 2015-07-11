(ns cljs-cm-editor.core
    (:require [reagent.core :as r :refer [cursor]]))

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
                  :extraKeys       (.-subparKeymap js/window)
                  :matchBrackets   true
                  :mode            "clojure"})

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
                                                     (.setValue editor (or new-state "")))))
                                (.focus editor)
                                (.on editor "change" (fn [_]
                                                       (let [value (.getValue editor)]
                                                         (reset! a value))))
                                (.on editor "focus"
                                     (fn [_] (reset! editor-currently-focused id)))

                                (if-not (empty? (:click-coords options))
                                  (let [[x y] (:click-coords options)
                                        pos (.coordsChar editor (clj->js {:left x :top y}))]
                                    (.setCursor editor pos)
                                    )))

      :component-will-unmount #(let [{:keys [id editor]} (r/state %)]
                                (swap! editor-index dissoc id)
                                (.off editor))

      :reagent-render         (fn []
                                [:textarea {:style {:width "100%" :height "100%" :display "flex" :background "red" :flex 1}}])})))



(defn ^:export main []
  (let [content (r/atom "Editor content")]
    (r/render [cm-editor content]
              (.getElementById js/document "app"))))