;;; ---------------------------------------------------------
;;; ontologia_prac2.clp
;;; Translated by owl2clips
;;; Translated to CLIPS from ontology ontologia_prac2.rdf
;;; :Date 30/11/2025 12:48:03

(defclass Vivenda
    (is-a USER)
    (role concrete)
    (pattern-match reactive)
    (multislot distancia
        (type INSTANCE)
        (create-accessor read-write))
    (slot Preu
        (type INTEGER)
        (create-accessor read-write))
    (slot Superficie
        (type INTEGER)
        (create-accessor read-write))
    (slot balco
        (type SYMBOL)
        (create-accessor read-write))
    (slot coord_x
        (type INTEGER)
        (create-accessor read-write))
    (slot coord_y
        (type INTEGER)
        (create-accessor read-write))
    (slot n_dormitoris_dobles
        (type INTEGER)
        (create-accessor read-write))
    (slot n_dormitoris_simples
        (type INTEGER)
        (create-accessor read-write))
    (slot terrassa
        (type SYMBOL)
        (create-accessor read-write))
)

(defclass Unifamiliar
    (is-a Vivenda)
    (role concrete)
    (pattern-match reactive)
)

(defclass Pis
    (is-a Vivenda)
    (role concrete)
    (pattern-match reactive)
)

(defclass Duplex
    (is-a Pis)
    (role concrete)
    (pattern-match reactive)
)

(defclass Pis_normal
    (is-a Pis)
    (role concrete)
    (pattern-match reactive)
)

(defclass Barcelona
    (is-a USER)
    (role concrete)
    (pattern-match reactive)
    (slot Nom
        (type SYMBOL)
        (create-accessor read-write))
    ;;; El nom del tipus del punt d'interes e.x. supermercat
    (multislot Punts_interes
        (type STRING)
        (create-accessor read-write))
    (slot coord_x
        (type INTEGER)
        (create-accessor read-write))
    (slot coord_y
        (type INTEGER)
        (create-accessor read-write))
)

(defclass Persona
    (is-a USER)
    (role concrete)
    (pattern-match reactive)
    (multislot oferta_adecuada
        (type INSTANCE)
        (create-accessor read-write))
    (slot Cotxe
        (type SYMBOL)
        (create-accessor read-write))
    (slot Edat
        (type INTEGER)
        (create-accessor read-write))
    (slot Nom
        (type SYMBOL)
        (create-accessor read-write))
    (slot Num_persones
        (type INTEGER)
        (create-accessor read-write))
    (slot Preu_estricte
        (type SYMBOL)
        (create-accessor read-write))
    (slot Preu_max
        (type INTEGER)
        (create-accessor read-write))
    (slot Preu_min
        (type INTEGER)
        (create-accessor read-write))
    ;;; El nom del tipus del punt d'interes e.x. supermercat
    (multislot Punts_interes
        (type STRING)
        (create-accessor read-write))
    (slot Sexe
        (type STRING)
        (create-accessor read-write))
    (slot Tipus_solicitant
        (type STRING)
        (create-accessor read-write))
    (multislot Transport
        (type STRING)
        (create-accessor read-write))
)

(definstances instances
    ([Escola] of Barcelona
         (Punts_interes  "escola")
         (coord_x  20)
         (coord_y  20)
    )

    ([Super] of Barcelona
         (Punts_interes  "supermercat")
         (coord_x  15)
         (coord_y  15)
    )

    ([casa1] of Pis_normal
         (Preu  1800)
         (Superficie  300)
         (balco  "true")
         (coord_x  10)
         (coord_y  10)
         (n_dormitoris_dobles  1)
         (n_dormitoris_simples  2)
         (terrassa  "true")
    )

    ([duplex1] of Duplex
         (Preu  2000)
         (Superficie  200)
         (balco  "true")
         (n_dormitoris_dobles  3)
         (n_dormitoris_simples  1)
         (terrassa  "true")
    )

    ([pis1] of Pis_normal
         (Preu  900)
         (Superficie  100)
         (balco  "true")
         (coord_x  7)
         (coord_y  10)
         (n_dormitoris_dobles  1)
         (n_dormitoris_simples  2)
         (terrassa  "false")
    )

    ([pis2] of Pis_normal
         (Preu  500)
         (Superficie  70)
         (balco  "false")
         (coord_x  25)
         (coord_y  25)
         (n_dormitoris_dobles  1)
         (n_dormitoris_simples  0)
         (terrassa  "true")
    )

    ([pis3] of Pis_normal
         (Preu  1600)
         (Superficie  150)
         (balco  "true")
         (coord_x  49)
         (coord_y  49)
         (n_dormitoris_dobles  0)
         (n_dormitoris_simples  4)
         (terrassa  "false")
    )

    ([pis4] of Pis_normal
         (Preu  300)
         (Superficie  40)
         (balco  "false")
         (coord_x  35)
         (coord_y  25)
         (n_dormitoris_dobles  0)
         (n_dormitoris_simples  1)
         (terrassa  "true")
    )

    ([uni] of Barcelona
         (Punts_interes  "universitat")
         (coord_x  50)
         (coord_y  50)
    )

)
