
(defmodule entrada
    (import MAIN ?ALL)
    (export ?ALL)
)

(defrule MAIN::inicio 
    (declare (salience 20)) 
    => 
    (printout t "------------------------------------------------" crlf)
    (printout t "Bienvenido al buscador de viviendas de Barcelona" crlf)
    (printout t "------------------------------------------------" crlf)
    (focus entrada)
)

(deffunction entrada::obtener_texto (?pregunta)
    (printout t ?pregunta " ")
    (bind ?res (read))
    (return ?res)
)

(deffunction entrada::obtener_numero (?pregunta ?min ?max)
    (printout t ?pregunta " (" ?min "-" ?max "): ")
    (bind ?num (read))
    (while (or (not (numberp ?num)) (< ?num ?min) (> ?num ?max)) do
        (printout t "Valor inválido. Por favor, introduzca un número entre " ?min " y " ?max ": ")
        (bind ?num (read))
    )
    (return ?num)
)

(deffunction entrada::selecciona_una_opcion (?question $?opcions)
    (printout t ?question)
    (printout t " Las opciones son: " $?opcions crlf)
    (bind ?response (read))
    (while (not (member$ ?response $?opcions)) do 
        (printout t "Opción no válida. Inténtelo de nuevo: " $?opcions crlf)
        (bind ?response (read))
    )
    (return ?response)
)

(deffunction entrada::selecciona_multiples_opciones (?titulo $?opciones_validas)
    (printout t crlf)
    (printout t ?titulo crlf)
    (printout t "Opciones disponibles: " $?opciones_validas crlf)
    (printout t "Escriba una opción y pulse enter. Para terminar escriba FIN." crlf)
    
    (bind ?response (read))
    (bind $?lista_retorno (create$))
    
    (while (neq ?response FIN) do
        ;; Convertimos a string para comparar si es necesario, o mantenemos símbolo
        (if (and (member$ ?response $?opciones_validas) (not (member$ ?response $?lista_retorno))) then
            (bind $?lista_retorno (insert$ $?lista_retorno (+ (length$ $?lista_retorno) 1) ?response))
        else
            (if (neq ?response FIN) then
                (printout t "Opción no reconocida o ya añadida." crlf)
            )
        )
        (bind ?response (read))
    )
    (return $?lista_retorno)
)

;Funció per convertir llista a llista de strings
(deffunction llista-a-strings (?llista)
   (bind ?nova-llista (create$))
   (foreach ?elem ?llista                  
      (bind ?nova-llista (create$ ?nova-llista (str-cat ?elem)))
   )
   (return ?nova-llista)
)

(deffunction entrada::instanciacion_persona ()
    (bind ?nom (obtener_texto "Introduzca su Nombre:"))
    (bind ?edat (obtener_numero "Introduzca su edad" 18 100))
    (bind ?sexe (selecciona_una_opcion "Introduzca su sexo:" Hombre Mujer Otro))
    (bind ?tipo (selecciona_una_opcion "Tipo de solicitante:" Estudiante Familia Pareja Soltero))
    (bind ?num_pers (obtener_numero "¿Cuántas personas vivirán en la casa?" 1 10))

    (printout t crlf "--- Datos Económicos ---" crlf)
    (bind ?p_min (obtener_numero "Precio Mínimo (€)" 0 500))
    (bind ?p_max (obtener_numero "Precio Máximo (€)" ?p_min 10000)) ; El max debe ser mayor que el min
    (bind ?estricte (selecciona_una_opcion "¿Es el presupuesto estricto?" Si No))

    (printout t crlf "--- Movilidad ---" crlf)
    (bind ?cotxe (selecciona_una_opcion "¿Dispone de coche propio?" true false))
    
    (bind ?transporte (selecciona_multiples_opciones "¿Qué transporte público necesita cerca?" 
                      Metro Bus Tren Tranvia Bicing Ninguno))

    (bind ?interes (selecciona_multiples_opciones "¿Qué puntos de interés necesita cerca?" 
                   Supermercado Colegio Parque Hospital Gimnasio Farmacia Centro_Comercial Universidad Cine Bar Biblioteca))

    (make-instance usuario of Persona 
        (Nom ?nom)
        (Edat ?edat)
        (Sexe (str-cat ?sexe))
        (Tipus_solicitant (str-cat ?tipo))
        (Num_persones ?num_pers)
        (Preu_min ?p_min)
        (Preu_max ?p_max)
        (Preu_estricte ?estricte)
        (Cotxe ?cotxe)
        (Transport (llista-a-strings ?transporte)) 
        (Punts_interes (llista-a-strings ?interes))
    )
    
    (printout t crlf ">> Perfil de usuario creado correctamente." crlf)
)

(defrule entrada::crear-perfil
    =>
    (instanciacion_persona)
)