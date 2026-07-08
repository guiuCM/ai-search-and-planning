(define (domain hotel_2)
  (:requirements :adl :typing :fluents)

  (:types
    habitacio reserva dia orientacio - object
  )           

  (:predicates
    (habitacio_ocupada ?h - habitacio ?d - dia)
    (reserva_servida ?r - reserva)
    (reserva_no_servida ?r - reserva)
    (dia_reserva ?r - reserva ?d - dia)
    (te_orientacio ?h - habitacio ?o - orientacio)
    (vol_orientacio ?r - reserva ?o - orientacio)
  )

  (:functions
    (capacitat_habitacio ?h - habitacio)
    (persones_reserva ?r - reserva)
    (total-cost)
  )

  ;... actions
  (:action assignar_reserva
      :parameters (?r - reserva ?h - habitacio)
      :precondition (and 
                        (not (reserva_servida ?r))
                        (not (reserva_no_servida ?r))
                        (>= (capacitat_habitacio ?h) (persones_reserva ?r))
                        (forall (?d - dia) (or (not (dia_reserva ?r ?d)) (not (habitacio_ocupada ?h ?d))))
                    )
      :effect (and
                (reserva_servida ?r)
                (forall (?d - dia) (when (dia_reserva ?r ?d) (habitacio_ocupada ?h ?d)))
                
                ;penalització per orientació
                (when (forall (?o - orientacio) 
                          (not (and (vol_orientacio ?r ?o) (te_orientacio ?h ?o)))
                      )
                      (increase (total-cost) 100)
                )
              )
  )


  (:action descartar_reserva
    :parameters (?r - reserva) 
    :precondition (not (reserva_servida ?r))
    :effect (and 
        (reserva_no_servida ?r)
        (increase (total-cost) 500000)
    )
  )

)