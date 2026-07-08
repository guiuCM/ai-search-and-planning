;modul abstraccio
(defmodule abstraccio
    (import MAIN ?ALL)
    (export ?ALL)
)

(deffacts abstraccio::variablitat
    (percentatge 0.05) ;;permet pagar 5% mes del preu max
)

;; rang presupost es vivena - tipus press
(deftemplate abstraccio::rang_pressupost
   (slot vivenda (type INSTANCE))
    (slot tipus (type STRING))
)

(deftemplate abstraccio::proximitat_interes
    (slot vivenda (type INSTANCE))
    (multislot tipus (type STRING))
    (slot dist (type STRING))
)

(deftemplate abstraccio::rang_edat
    (slot tipus (type STRING))
)

(deftemplate abstraccio::tipus_perfil
    (slot perfil (type STRING))
)

(deftemplate abstraccio::tipus_rangs
   (slot nom (type STRING))   ; p.ex. "r200_400"
   (slot min (type INTEGER))
   (slot max (type INTEGER))
)

(deftemplate abstraccio::rangs_pressupost_usuari
    (multislot rangs (type STRING))
)

(deftemplate abstraccio::rangs_pressupost_vivenda
    (slot vivenda (type INSTANCE))
    (multislot rangs (type STRING))
)

(deffacts abstraccio::init-rangs-usuari
   (rangs_pressupost_usuari (rangs))
)

(defrule abstraccio::init-rangs-pressupost-vivenda
   ;; Hi ha una vivenda
   ?v <- (object (is-a Vivenda))

   ;; Encara NO hi ha fact de rangs per a aquesta vivenda
   (not (rangs_pressupost_vivenda (vivenda ?v)))
   =>
   ;; Creem el fact amb la llista de rangs buida
   (assert (rangs_pressupost_vivenda
              (vivenda ?v)
              (rangs)))
)

(deffacts abstraccio::rangs_pressupost_definits
   (tipus_rangs (nom "r0_200")       (min 0)    (max 200))
   (tipus_rangs (nom "r200_400")     (min 200)  (max 400))
   (tipus_rangs (nom "r400_600")     (min 400)  (max 600))
   (tipus_rangs (nom "r600_800")     (min 600)  (max 800))
   (tipus_rangs (nom "r800_1000")    (min 800)  (max 1000))
   (tipus_rangs (nom "r1000_1200")   (min 1000) (max 1200))
   (tipus_rangs (nom "r1200_1400")   (min 1200) (max 1400))
   (tipus_rangs (nom "r1400_1600")   (min 1400) (max 1600))
   (tipus_rangs (nom "r1600_1800")   (min 1600) (max 1800))
   (tipus_rangs (nom "r1800_2000")   (min 1800) (max 2000))
   (tipus_rangs (nom "r2000_2500")   (min 2000) (max 2500))
   (tipus_rangs (nom "r2500_3000")   (min 2500) (max 3000))
   (tipus_rangs (nom "r3000_3500")   (min 3000) (max 3500))
   (tipus_rangs (nom "r3500_4000")   (min 3500) (max 4000))
   (tipus_rangs (nom "r4000_4500")   (min 4000) (max 4500))
   (tipus_rangs (nom "r4500_5000")   (min 4500) (max 5000))
   (tipus_rangs (nom "r5000_6000")   (min 5000) (max 6000))
   (tipus_rangs (nom "r6000_7000")   (min 6000) (max 7000))
   (tipus_rangs (nom "r7000_8000")   (min 7000) (max 8000))
   (tipus_rangs (nom "r8000_9000")   (min 8000) (max 9000))
   (tipus_rangs (nom "r9000_10000")  (min 9000) (max 10000))
)

(deftemplate abstraccio::num_persones
   (slot nom (type STRING))   ; p.ex. "3_4pers"
   (slot min (type INTEGER))
   (slot max (type INTEGER))
)

(deffacts abstraccio::categories_num_persones
    (num_persones (nom "1pers") (min 1) (max 1))
    (num_persones (nom "2pers") (min 2) (max 2))
    (num_persones (nom "3_4pers") (min 3) (max 4))
    (num_persones (nom "5_6pers") (min 5) (max 6))
    (num_persones (nom "7+pers") (min 7) (max 15))
)

(deftemplate abstraccio::rang_num_persones_usuari
    (slot categoria (type STRING))
)

(deftemplate abstraccio::rang_num_persones_vivenda
    (slot vivenda (type INSTANCE))
    (slot categoria (type STRING))
)

(defrule abstraccio::assigna_num_persones_usuari
    (object (is-a Persona) (Num_persones ?n))

   ;; Un rang definit
   (num_persones
      (nom ?nom)      ; STRING: "r200_400", etc.
      (min ?rmin)
      (max ?rmax))

   (test (and (>= ?n ?rmin)
              (<= ?n ?rmax)))
             
   =>
   ;; Afegeix el rang al multislot
   (assert (rang_num_persones_usuari (categoria ?nom)))
)

(defrule abstraccio::assigna_num_persones_vivenda
    ?v <- (object (is-a Vivenda) (n_dormitoris_dobles ?nd) (n_dormitoris_simples ?ns))

   ;; Un rang definit
   (num_persones
      (nom ?nom)      ; STRING: "r200_400", etc.
      (min ?rmin)
      (max ?rmax))

   (test (and (>= (+ ?ns (* 2 ?nd)) ?rmin)
              (<= (+ ?ns (* 2 ?nd)) ?rmax)))
             
   =>
   ;; Afegeix el rang al multislot
   (assert (rang_num_persones_vivenda (vivenda ?v) (categoria ?nom)))
)

(defrule abstraccio::assigna-rangs-pressupost
   ;; Persona i els seus límits
   (object (is-a Persona) (Preu_max ?pmax) (Preu_min ?pmin))

   ;; Fet dels rangs assignats a l'usuari
   ?ru <- (rangs_pressupost_usuari
              (rangs $?llista))

   ;; Un rang definit
   (tipus_rangs
      (nom ?nom)      ; STRING: "r200_400", etc.
      (min ?rmin)
      (max ?rmax))

   (test (and (> ?rmax ?pmin)
              (< ?rmin ?pmax)))
             

   ;; Evitar duplicats
   (test (not (member$ ?nom $?llista)))
   =>
   ;; Afegeix el rang al multislot
   (modify ?ru (rangs $?llista ?nom))
)

(defrule abstraccio::assigna-rangs-preu_vivendes
   ?v <- (object (is-a Vivenda) (Preu ?p))

   ;; Fet dels rangs assignats a l'usuari
   ?ru <- (rangs_pressupost_vivenda
              (vivenda ?v)
              (rangs $?llista))

   ;; Un rang definit
   (tipus_rangs
      (nom ?nom)      ; STRING: "r200_400", etc.
      (min ?rmin)
      (max ?rmax))

   (test (and (>= ?p ?rmin)
              (< ?p ?rmax)))
             

   ;; Evitar duplicats
   (test (not (member$ ?nom $?llista)))
   =>
   ;; Afegeix el rang al multislot
   (modify ?ru (rangs $?llista ?nom))
)



(deffunction abstraccio::dist_2p (?x1 ?y1 ?x2 ?y2)
    (return (+ (abs (- ?x1 ?x2)) (abs (- ?y1 ?y2))))
)

(defrule abstraccio::distancies_punts
    (object (is-a Barcelona) (coord_x ?x1) (coord_y ?y1) (Punts_interes ?t))
    ?v <- (object (is-a Vivenda) (coord_x ?x2) (coord_y ?y2))
    =>
    (bind ?d (dist_2p ?x1 ?y1 ?x2 ?y2))
    (if (< ?d 30) then 
        (if (< ?d 15) then
            (assert (proximitat_interes (vivenda ?v) (tipus ?t) (dist "aprop")))
        else 
            (assert (proximitat_interes (vivenda ?v) (tipus ?t) (dist "accessible")))
        )
    else
        (assert (proximitat_interes (vivenda ?v) (tipus ?t) (dist "lluny")))
    )
)

;; ------------------ ABSTRACCIO EDAT ---------------- ;;

(defrule abstraccio::filtrar_edat
    (object (is-a Persona) (Edat ?e))
    =>
    (if (< ?e 30) then
        (assert (rang_edat (tipus "jove")))
    else
        (if (< ?e 65) then
            (assert (rang_edat (tipus "adult")))
        else
            (assert (rang_edat (tipus "3a_edat")))
        )
    )
)

;; ----------------- ABSTRACCIO PERFIL PERSONA -------- ;;
;; - interessos
;; - tipus perfil
;; 


(deftemplate abstraccio::interes_persona
    (multislot punts_interes (type STRING))
)

(defrule abstraccio::r_interes_pers
    (object (is-a Persona) (Punts_interes $?ll))
    =>
    (assert (interes_persona (punts_interes $?ll)))
)

;(defrule abstraccio::r1_tipus_perfil
;    (object (is-a Persona) ()) ;; falta preguntar que fa
;)

;; NO utilitzar
(defrule abstraccio::filtrar_perfil
    (object (is-a Persona) (Num_persones ?n) (Punts_interes $?ll))
    (rang_edat (tipus ?t))
    =>
    (if (eq ?t "jove") then
        (if (member$ "Universidad" $?ll) then
            (if (> ?n 1) then
                (assert (tipus_perfil (perfil "grup_estudiants")))
            else
                (assert (tipus_perfil (perfil "estudiant_sol")))
            )
        else
            (if (> ?n 2) then
                (assert (tipus_perfil (perfil "grup_joves")))
            else
                (if (= ?n 2) then
                    (assert (tipus_perfil (perfil "parella_joves")))
                else
                    (assert (tipus_perfil (perfil "jove_sol")))
                )
            )
        )
    else
        (if (eq ?t "adult") then
            (if (= ?n 1) then
                (assert (tipus_perfil (perfil "adult_sol")))
            else
                (if (= ?n 2) then
                    (assert (tipus_perfil (perfil "parella_adults")))
                else
                    (assert (tipus_perfil (perfil "familia")))
                )
            )
        else
            (if (member$ "hospital" $?ll) then
                (if (= ?n 1) then
                    (assert (tipus_perfil (perfil "3a_edat_sol_malalt")))
                else
                    (assert (tipus_perfil (perfil "3a_edat_parella_malalt")))
                )
            else
                (if (= ?n 1) then
                    (assert (tipus_perfil (perfil "3a_edat_sol")))
                else
                    (assert (tipus_perfil (perfil "3a_edat_parella")))
                )
            )
        )
    )

    
;; --------------------------- ABSTRACCIO TRANSPORTS ---------------------- ;;
;; TURE o FALS si te transport privat
(deftemplate abstraccio::mobilitat_privada
    (slot te_cotxe (type SYMBOL))
)


; Quins transports publics vol
; [llista transports]
; res
(deftemplate abstraccio::necessitat_transport
    (multislot tipus_t (type STRING))
)

(defrule abstraccio::abs_mobilitat_persona
    (object (is-a Persona) (Cotxe ?c))
    =>
    (assert (mobilitat_privada (te_cotxe ?c)))
)

(defrule abstraccio::necessitat_t_public
    (object (is-a Persona) (Transport $?ts))
    (test (> (length$ $?ts) 0))
    =>
    (assert (necessitat_transport (tipus_t "res")))
)

(defrule abstraccio::necessitat_t_public_res
    (object (is-a Persona) (Transport $?ts))
    (test (= (length$ $?ts) 0))
    =>
    (assert (necessitat_transport (tipus_t $?ts)))
)


;; ------------------- ABSTRACCIO GARATJE VIVENDES ---------------------------------- ;;

(deftemplate abstraccio::te_parking
    (slot vivenda (type INSTANCE))
    (slot garatje (type SYMBOL))
)

(defrule abstraccio::garatje_pis
    ?v <- (object (is-a Pis))
    =>
    (assert (te_parking (vivenda ?v) (garatje false)))
)

(defrule abstraccio::garatje_casa
    ?v <- (object (is-a Unifamiliar))
    =>
    (assert (te_parking (vivenda ?v) (garatje true)))
)


)
