;;; ---------------------------------------------------------
;;; ontologia_v2.clp
;;; Translated by owl2clips
;;; Translated to CLIPS from ontology ontologia_v2.rdf
;;; :Date 10/12/2025 17:27:36

(defclass Vivenda
    (is-a USER)
    (role concrete)
    (pattern-match reactive)
    (multislot distancia
        (type INSTANCE)
        (create-accessor read-write))
    (multislot Ascensor
        (type SYMBOL)
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

(defclass Unifamiliar
    (is-a Vivenda)
    (role concrete)
    (pattern-match reactive)
)

(defclass Barcelona
    (is-a USER)
    (role concrete)
    (pattern-match reactive)
    (slot Nom
        (type STRING)
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
        (type STRING)
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

