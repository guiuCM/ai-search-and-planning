(defmodule casos_prova
    (import MAIN ?ALL)
    (export ?ALL)
)


(definstances persones
    ([treballador_it] of Persona
          (Cotxe  "false")
          (Edat  29)
          (Nom  "Marc")
          (Num_persones  1)
          (Preu_estricte  "false")
          (Preu_max  950)
          (Preu_min  500)
          (Punts_interes  "Gimnasio" "Hospital" "Centro_Comercial")
          (Sexe  "Hombre")
          (Tipus_solicitant  "Soltero")
          (Transport  "Metro" "Bicing")
    )
)
