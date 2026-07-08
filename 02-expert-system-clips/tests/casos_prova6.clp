(defmodule casos_prova
    (import MAIN ?ALL)
    (export ?ALL)
)


(definstances persones
    ([parella_jove] of Persona
          (Cotxe  "true")
          (Edat  26)
          (Nom  "Anna")
          (Num_persones  2)
          (Preu_estricte  "true")
          (Preu_max  1100)
          (Preu_min  600)
          (Punts_interes  "Cine" "Supermercado" "Universidad" "Bar")
          (Sexe  "Mujer")
          (Tipus_solicitant  "Pareja")
          (Transport  "Bus" "Tren" "Metro")
    )
)
