;; This Source Code Form is subject to the terms of the Mozilla Public
;; License, v. 2.0. If a copy of the MPL was not distributed with this
;; file, You can obtain one at http://mozilla.org/MPL/2.0/.
;;
;; This Source Code Form is "Incompatible With Secondary Licenses", as
;; defined by the Mozilla Public License, v. 2.0.
;;
;; Copyright (c) 2020 UXBOX Labs SL

(ns app.main.ui.settings.feedback
  "Feedback form."
  (:require
   [app.common.spec :as us]
   [app.main.data.messages :as dm]
   [app.main.data.users :as du]
   [app.main.refs :as refs]
   [app.main.store :as st]
   [app.main.ui.components.forms :as fm]
   [app.main.ui.icons :as i]
   [app.util.dom :as dom]
   [app.util.i18n :as i18n :refer [tr]]
   [cljs.spec.alpha :as s]
   [rumext.alpha :as mf]))

(s/def ::feedback-form
  (s/keys :opt-un [::lang ::theme]))

(defn- on-error
  [form error]
  (st/emit! (dm/error (tr "errors.generic"))))

(defn- on-success
  [form]
  (st/emit! (dm/success (tr "notifications.profile-saved"))))

(defn- on-submit
  [form event]
  (let [data  (:clean-data @form)
        mdata {:on-success (partial on-success form)
               :on-error (partial on-error form)}]
    (st/emit! (du/update-profile (with-meta data mdata)))))

(mf/defc options-form
  []
  (let [profile (mf/deref refs/profile)
        form    (fm/use-form :spec ::feedback-form
                             :initial profile)]
    [:& fm/form {:class "feedback-form"
                 :on-submit on-submit
                 :form form}

     ;; --- Feedback section
     [:h2 (tr "feedback.title")]
     [:p (tr "feedback.subtitle")]

     [:div.fields-row
      [:& fm/input {:label (tr "feedback.subject")
                    :name :subject}]]
     [:div.fields-row
      [:& fm/textarea
       {:label (tr "feedback.description")
        :name :description
        :rows 5}]]

     [:& fm/submit-button
      {:label (tr "labels.send")}]

     [:hr]

     [:h2 (tr "feedback.discussions-title")]
     [:p (tr "feedback.discussions-subtitle1")]
     [:p (tr "feedback.discussions-subtitle2")]

     [:a.btn-secondary.btn-large
      {:href "https://github.com/penpot/penpot/discussions" :target "_blank"}
      (tr "feedback.discussions-go-to")]

     [:hr]

     [:h2 "Gitter"]
     [:p (tr "feedback.chat-subtitle")]
     [:a.btn-secondary.btn-large
      {:href "https://gitter.im/penpot/community" :target "_blank"}
      (tr "feedback.chat-start")]

     ]))

(mf/defc feedback-page
  []
  [:div.dashboard-settings
   [:div.form-container
    [:& options-form]]])
