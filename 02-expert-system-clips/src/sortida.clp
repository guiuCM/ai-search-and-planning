(defmodule sortida
    (import refinament ?ALL)
    (export ?ALL)
)


;; Aquesta regla imprimeix la millor oferta restant (màxima puntuació)
(defrule sortida::imprimir_top_n

    ?ctrl <- (Control-Salida (cantidad-impresa ?n) (limite ?lim))
    (test (< ?n ?lim))
    
    ;Triem la màxima puntuació
    ?c_best <- (Candidatura (vivenda ?v) (oferta ?oferta) (puntuacion ?max) (estat clasificado) (motius $?m) (requisitos_faltantes $?faltan))
    (not (Candidatura (estat clasificado) (puntuacion ?p&:(> ?p ?max))))
    
    =>
    (bind ?nombre (instance-name ?v))
    (bind ?preu (send ?v get-Preu))
    (printout t "" crlf)
    (printout t "" crlf)
    (printout t "TOP " (+ ?n 1) " | " ?oferta " | " ?nombre " (" ?max " pts) | (" ?preu " EUR)" crlf)
    
    (if (> (length$ $?faltan) 0) then
        (printout t "       FALTAN: " $?faltan crlf)
    )
    (printout t "       Motivos: " $?m crlf)
    
    (modify ?c_best (estat impreso))
    (modify ?ctrl (cantidad-impresa (+ ?n 1)))
)


;No hi ha pisos
(defrule sortida::fi_sense_resultats
    (Control-Salida (cantidad-impresa ?n))
    (not (Candidatura (estat clasificado)))
    =>
    (if (= ?n 0) then
        (printout t "" crlf)
        (printout t "Lo sentimos mucho, pero no tenemos viviendas que complen los requisitos pedidos" crlf)
        (printout t "" crlf)
    )  
)