(defmodule casos_prova
    (import MAIN ?ALL)
    (export ?ALL)
)


(definstances persones
    ([familia_garcia] of Persona
          (Cotxe  "true")
          (Edat  35)
          (Nom  "Montse")
          (Num_persones  4)
          (Preu_estricte  "true")
          (Preu_max  2000)
          (Preu_min  800)
          (Punts_interes  "Colegio" "Parque" "Supermercado" "Farmacia")
          (Sexe  "Mujer")
          (Tipus_solicitant  "Familia")
          (Transport  "Metro" "Bus")
    )
)
