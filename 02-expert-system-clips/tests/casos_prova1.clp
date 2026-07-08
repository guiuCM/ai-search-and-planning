(defmodule casos_prova
    (import MAIN ?ALL)
    (export ?ALL)
)


(definstances persones
    ([estudiants] of Persona
         (Cotxe  "false")
         (Edat  24)
         (Nom  "Manolo")
         (Num_persones  4)
         (Preu_estricte  "true")
         (Preu_max  1000)
         (Preu_min  200)
         (Punts_interes  "Supermercado" "Centro_Comercial" "Universidad")
         (Sexe  "Hombre")
         (Tipus_solicitant  "Soltero")
         (Transport  "Bus" "Metro")
    )
)
