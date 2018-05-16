(ns gateworld.rules.core
  (:require
   [gateworld.utils :refer [vec-with-item-removed]]))


;;


(def combat-move-types
  #{:activate-ability
    :pass-priority
    :pass-turn})


(def effect-types
  #{:sacrifice})


;;


(def empty-char-state
  {:permanents []
   :cards-in-hand []})


(def empty-combat-state
  {:chars []
   :active-char 0
   :npcs []
   :fx []})


;;


(defn add-char
  [state char]
  (as-> state $
        (update-in $ [:chars] conj char)))


(defn add-npc
  [state npc]
  (as-> state $
        (update-in $ [:npcs] conj npc)))


(defn add-effect
  [state effect]
  (update-in state [:fx] conj effect))


(defn sacrifice-permanent
  [state effect]
  (let [pid (:pid effect)
        permanent-index (:permanent-index effect)]
    (update-in state
               [:chars pid :permanents]
               vec-with-item-removed
               permanent-index)))


(defn resolve-next-effect
  [state]
  (let [effect (-> state :fx peek)
        popped-state (update-in state [:fx] pop)]
    (condp = (:type effect)
           :sacrifice (sacrifice-permanent popped-state effect))))
