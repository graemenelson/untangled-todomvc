(ns untangled-todomvc.ui
  (:require [om.next :as om :refer-macros [defui]]
            [untangled.client.mutations :as mut]
            [om.dom :as dom]))

(defui TodoItem
  static om/IQuery
  (query [_] [:id :text :completed :editing])
  static om/Ident
  (ident [_ props] [:todo/by-id (:id props)])
  Object
  (render [this]
    (let [{:keys [text completed]} (om/props this)]
      (dom/li #js {:className (if completed "completed" "")}
        (dom/div #js {:className "view"}
          (dom/input #js {:className "toggle"
                          :type      "checkbox"
                          :onChange  #(mut/toggle! this :completed)})
          (dom/label nil text)
          (dom/button #js {:className "destroy"}))
        (dom/input #js {:className "edit"})))))

(def ui-todo-item (om/factory TodoItem {:keyfn :id}))

(defui TodoList
  static om/IQuery
  (query [this] [{:todos (om/get-query TodoItem)}])
  Object
  (render [this]
    (let [{:keys [todos]} (om/props this)]
      (dom/div nil
        (dom/section #js {:className "todoapp"}

          (.header this)

          (dom/section #js {:className "main"}
            (dom/input #js {:className "toggle-all" :type "checkbox"})
            (dom/label #js {:htmlFor "toggle-all"} "Mark all as complete")
            (dom/ul #js {:className "todo-list"}
              (map ui-todo-item todos)))

          (.filter-footer this))


        (.footer-info this))))

  (header [this]
    (dom/header #js {:className "header"}
      (dom/h1 nil "todos")
      (dom/input #js {:className   "new-todo"
                      :placeholder "What needs to be done?"
                      :autoFocus   true})))

  (filter-footer [this]
    (let [{:keys [todos]} (om/props this)]
      (dom/footer #js {:className "footer"}
        (dom/span #js {:className "todo-count"}
          (dom/strong nil (count todos)) " items left")
        (dom/ul #js {:className "filters"}
          (dom/li nil
            (dom/a #js {:className "selected" :href "#"} "All")
            (dom/a #js {:href "#"} "Active")
            (dom/a #js {:href "#"} "Completed")))
        (dom/button #js {:className "clear-completed"} "Clear Completed"))))


  (footer-info [this]
    (dom/footer #js {:className "info"}
      (dom/p nil "Double-click to edit a todo")
      (dom/p nil "Created by "
        (dom/a #js {:href   "http://www.thenavisway.com"
                    :target "_blank"} "NAVIS"))
      (dom/p nil "Part of "
        (dom/a #js {:href   "http://todomvc.com"
                    :target "_blank"} "TodoMVC")))))