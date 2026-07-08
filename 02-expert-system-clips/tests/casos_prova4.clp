(defmodule casos_prova
    (import MAIN ?ALL)
    (export ?ALL)
)


(definstances persones
    ([parella_gran] of Persona
          (Cotxe  "false")
          (Edat  72)
          (Nom  "Jordi")
          (Num_persones  2)
          (Preu_estricte  "true")
          (Preu_max  850)
          (Preu_min  400)
          (Punts_interes  "Farmacia" "Parque" "Supermercado" "Biblioteca")
          (Sexe  "Hombre")
          (Tipus_solicitant  "Pareja")
          (Transport  "Bus")
    )
)
