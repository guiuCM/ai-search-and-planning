(defmodule refinament
    (import MAIN ?ALL)
    (import classificacio_heuristica ?ALL)
    (export ?ALL)
)

(defrule refinament::descartar_per_preu
    (declare (salience 100))
    
    ?c <- (Candidatura (vivenda ?v))
    
    ;; Si no es compara amb el punter, troba noms diferents
    ?obj <- (object (is-a Vivenda) (Preu ?preu-casa))
    (test (eq ?v ?obj))
    
    (object (is-a Persona) (Preu_max ?p-max) (Preu_estricte ?estricte))
    
    (test 
        (if (eq ?estricte "true") then
            (> ?preu-casa ?p-max)
        else
            (> ?preu-casa (* ?p-max 1.05))
        )
    )
    =>
    (retract ?c)
)

(defrule refinament::descartar_per_habitacions
    (declare (salience 100))
    
    ?c <- (Candidatura (vivenda ?v))
    
    (object (is-a Vivenda) (name ?v) 
            (n_dormitoris_dobles ?nd) 
            (n_dormitoris_simples ?ns))
            
    (object (is-a Persona) (Num_persones ?n-pers))

    (test (< (+ (* ?nd 2) ?ns) ?n-pers))
    =>
    (retract ?c)
)


(defrule refinament::triar_top_n
    (not (Control-Salida))
    =>
    (assert (Control-Salida (cantidad-impresa 0) (limite 5))) ; TOP 5
)
