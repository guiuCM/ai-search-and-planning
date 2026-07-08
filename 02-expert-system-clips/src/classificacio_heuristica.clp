(defmodule classificacio_heuristica
    (import MAIN ?ALL)
    (import abstraccio ?ALL)
    (export ?ALL)
)

(deftemplate classificacio_heuristica::Candidatura
    (slot vivenda (type INSTANCE-NAME))
    (slot oferta (type STRING))
    (slot puntuacion (type INTEGER) (default 0))         
    (multislot requisitos_faltantes (type STRING))       
    (multislot extras_encontrados (type STRING))
    (multislot motius (type STRING))                     
    (slot estat (type SYMBOL) (default evaluando))       
)

;; Template para controlar el Top N
(deftemplate classificacio_heuristica::Control-Salida
    (slot cantidad-impresa (type INTEGER) (default 0))
    (slot limite (type INTEGER) (default 5)) ; <-- AQUÍ CAMBIAS SI QUIERES 5 o 10
)

(deftemplate classificacio_heuristica::veinat_rang
   (slot rang)
   (multislot veins) ;; rangs immediatament per sota i per sobre
)

(deffacts classificacio_heuristica::rangs_pressupost_definits
   (veinat_rang (rang "r0_200")     (veins "r200_400"))
   (veinat_rang (rang "r200_400")   (veins "r0_200" "r400_600"))
   (veinat_rang (rang "r400_600")   (veins "r200_400" "r600_800"))
   (veinat_rang (rang "r600_800")   (veins "r400_600" "r800_1000"))
   (veinat_rang (rang "r800_1000")  (veins "r600_800" "r1000_1200"))
   (veinat_rang (rang "r1000_1200") (veins "r800_1000" "r1200_1400"))
   (veinat_rang (rang "r1200_1400") (veins "r1000_1200" "r1400_1600"))
   (veinat_rang (rang "r1400_1600") (veins "r1200_1400" "r1600_1800"))
   (veinat_rang (rang "r1600_1800") (veins "r1400_1600" "r1800_2000"))
   (veinat_rang (rang "r1800_2000") (veins "r1600_1800" "r2000_2500"))
   (veinat_rang (rang "r2000_2500") (veins "r1800_2000" "r2500_3000"))
   (veinat_rang (rang "r2500_3000") (veins "r2000_2500" "r3000_3500"))
   (veinat_rang (rang "r3000_3500") (veins "r2500_3000" "r3500_4000"))
   (veinat_rang (rang "r3500_4000") (veins "r3000_3500" "r4000_4500"))
   (veinat_rang (rang "r4000_4500") (veins "r3500_4000" "r4500_5000"))
   (veinat_rang (rang "r4500_5000") (veins "r4000_4500"))
   ; afegir més?
)


; Iterar sobre cada rang de l'usuari
(defrule classificacio_heuristica::generar_candidats_valids

    (rangs_pressupost_usuari (rangs $? ?ru $?))
    (rang_num_persones_usuari (categoria ?cat-pers-usuari))
    (rangs_pressupost_vivenda (vivenda ?v) (rangs $? ?rv $?))
    (rang_num_persones_vivenda (vivenda ?v) (categoria ?cat-pers-casa))

    (veinat_rang
      (rang ?ru)
      (veins $?veins))

    (num_persones
      (nom ?cat-pers-usuari)
      (min ?u-min)
      (max ?u-max))

    (num_persones
      (nom ?cat-pers-casa)
      (min ?v-min)
      (max ?v-max))

    ; mateix rang preu o un per sobre/per sota
    (test (or (eq ?ru ?rv)
             (member$ ?rv $?veins)))

    ; mateix rang num persones o per sobre
    (test (>= ?v-max ?u-max))

    
    ;;Per no repetir
    (not (Candidatura (vivenda ?v)))
    =>
    (printout t "[Clasificacion] Vivienda aceptada: " ?v crlf)
    (assert (Candidatura (vivenda ?v) (puntuacion 0) ))
)

;;; Abans de puntuar, mirem si falta alguna cosa obligatòria

(defrule classificacio_heuristica::detectar_requisits_faltants
    (declare (salience 20)) ; Prioritat alta per detectar-ho al principi
    ?c <- (Candidatura (vivenda ?v) (requisitos_faltantes $?faltan) (estat evaluando))
    
    ;; L'usuari vol X (Interés o Transport)
    (or (object (is-a Persona) (Punts_interes $? ?req $?))
        (object (is-a Persona) (Transport $? ?req $?)))
    
    ;; La casa NO ho té (ni aprop ni accessible)
    (not (proximitat_interes (vivenda ?v) (tipus ?req)))
    
    ;; Encara no ho hem apuntat
    (test (not (member$ ?req $?faltan)))
    =>
    (modify ?c (requisitos_faltantes $?faltan ?req))
)


;Funciona assignant punts per cada condició extra i retorna la millor

;Un contador per fer iteracions
(deftemplate classificacio_heuristica::Control
    (slot impresos (type INTEGER))
)

;Comparació preu
(defrule classificacio_heuristica::sumar_punts_economicos
    ?c <- (Candidatura (vivenda ?v) (puntuacion ?pts) (motius $?m) (estat evaluando))
    (rang_pressupost (vivenda ?v) (tipus "dins_pressupost"))
    
    ;;Evita bucles
    (test (not (member$ "Precio dentro del limite estricto (+30)" $?m)))
    =>
    (modify ?c (puntuacion (+ ?pts 30)) (motius $?m "Precio dentro del limite estricto (+30)"))
)

;Comparació proximitat
(defrule classificacio_heuristica::sumar_punts_ubicacio_interes_aprop
    (declare (salience 10))
    ?c <- (Candidatura (vivenda ?v) (puntuacion ?pts) (motius $?m) (estat evaluando))
    (proximitat_interes (vivenda ?v) (tipus ?lloc) (dist "aprop"))
    (object (is-a Persona) (Punts_interes $? ?lloc $?))
    
    ;;Evita bucles
    (test (not (member$ (str-cat ?lloc " muy cerca (+20) (interes)") $?m)))
    =>
    (modify ?c (puntuacion (+ ?pts 20)) (motius $?m (str-cat ?lloc " muy cerca (+20) (interes)")))
)

;Comparació proximitat
(defrule classificacio_heuristica::sumar_punts_transporte_interes_aprop
    (declare (salience 10))
    ?c <- (Candidatura (vivenda ?v) (puntuacion ?pts) (motius $?m) (estat evaluando))
    (proximitat_interes (vivenda ?v) (tipus ?lloc) (dist "aprop"))
    (object (is-a Persona) (Transport $? ?lloc $?))
    
    ;;Evita bucles
    (test (not (member$ (str-cat ?lloc " muy cerca (+20) (transporte)") $?m)))
    =>
    (modify ?c (puntuacion (+ ?pts 20)) (motius $?m (str-cat ?lloc " muy cerca (+20) (transporte)")))
)

;Comparació proximitat
(defrule classificacio_heuristica::sumar_punts_ubicacio_interes_accessible
    ?c <- (Candidatura (vivenda ?v) (puntuacion ?pts) (motius $?m) (estat evaluando))
    (proximitat_interes (vivenda ?v) (tipus ?lloc) (dist "accessible"))
    (object (is-a Persona) (Punts_interes $? ?lloc $?))
    
    ;;Evita bucles
    (test (not (member$ (str-cat ?lloc " muy cerca (+20) (interes)") $?m)))
    (test (not (member$ (str-cat ?lloc " accessible (+10) (interes)") $?m)))
    =>
    (modify ?c (puntuacion (+ ?pts 10)) (motius $?m (str-cat ?lloc " accessible (+10) (interes)")))
)

;Comparació proximitat
(defrule classificacio_heuristica::sumar_punts_ubicacio_aprop
    ?c <- (Candidatura (vivenda ?v) (puntuacion ?pts) (motius $?m) (estat evaluando))
    (proximitat_interes (vivenda ?v) (tipus ?lloc) (dist "aprop"))
    (not (object (is-a Persona) (Punts_interes $? ?lloc $?)))
    
    ;;Evita bucles
    (test (not (member$ (str-cat ?lloc " muy cerca (+5)") $?m)))
    (test (not (member$ (str-cat ?lloc " muy cerca (+20) (transporte)") $?m)))
    =>
    (modify ?c (puntuacion (+ ?pts 5)) (motius $?m (str-cat ?lloc " muy cerca (+5)")))
)

;;Comparació perfil
(defrule classificacio_heuristica::bonus_estudiants
    ?c <- (Candidatura (vivenda ?v) (puntuacion ?pts) (motius $?m) (estat evaluando))
    (tipus_perfil (perfil "grup_estudiants"))
    (proximitat_interes (vivenda ?v) (tipus "Universidad") (dist ?d&:(neq ?d "lluny")))
    
    ;;Evita bucles
    (test (not (member$ "Ideal para estudiantes: Universidad cerca (+50)" $?m)))
    =>
    (modify ?c (puntuacion (+ ?pts 50)) (motius $?m "Ideal para estudiantes: Universidad cerca (+50)"))
)


(defrule classificacio_heuristica::detectar_extras_no_pedidos
    (declare (salience 5)) ; Se ejecuta después de puntuar
    ?c <- (Candidatura (vivenda ?v) (extras_encontrados $?extras) (estat evaluando))
    
    ;; La casa tiene ALGO muy cerca
    (proximitat_interes (vivenda ?v) (tipus ?algo) (dist "aprop"))
    
    ;; Comprobamos que el usuario NO lo pidió (ni en Punts_interes ni en Transport)
    (not (object (is-a Persona) (Punts_interes $? ?algo $?)))
    (not (object (is-a Persona) (Transport $? ?algo $?)))
    
    ;; Evitar duplicados en la lista
    (test (not (member$ ?algo $?extras)))
    =>
    (modify ?c (extras_encontrados $?extras ?algo))
)

;; MUY RECOMENDABLE
(defrule classificacio_heuristica::classificar_molt_recomanable
    (declare (salience -10))
    ?c <- (Candidatura (requisitos_faltantes $?f) (extras_encontrados $?e) (estat evaluando))
    
    (test (= (length$ $?f) 0))  ; Cumple todo lo obligatorio
    (test (> (length$ $?e) 0))  ; Tiene algún regalo extra
    =>
    (modify ?c (estat clasificado) (oferta "Muy recomendable"))
)

;; ADECUADO
(defrule classificacio_heuristica::classificar_adequat
    (declare (salience -10))
    ?c <- (Candidatura (requisitos_faltantes $?f) (extras_encontrados $?e) (estat evaluando))
    
    (test (= (length$ $?f) 0))
    (test (= (length$ $?e) 0)) ; No extras
    =>
    (modify ?c (estat clasificado) (oferta "Adecuado"))
)

;; PARCIALMENTE ADECUADO (1 o 2 fallos)
(defrule classificacio_heuristica::classificar_parcial
    (declare (salience -10))
    ?c <- (Candidatura (requisitos_faltantes $?f) (estat evaluando))
    (test (< (length$ $?f) 3))
    (test (> (length$ $?f) 0))
    =>
    (modify ?c (estat clasificado) (oferta "Parcialmente adecuado"))
)

