(defmodule casos_prova
    (import MAIN ?ALL)
    (export ?ALL)
)


(definstances persones
    ([executiva] of Persona
          (Cotxe  "true")
          (Edat  42)
          (Nom  "Sofia")
          (Num_persones  1)
          (Preu_estricte  "false")
          (Preu_max  2500)
          (Preu_min  1200)
          (Punts_interes  "Parque")
          (Sexe  "Mujer")
          (Tipus_solicitant  "Soltero")
          (Transport  "Tranvia" "Bus")
    )
)
