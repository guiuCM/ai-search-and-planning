(define (problem hotel-problem)
    (:domain hotel_0)
    (:objects
        h1 h2 h3 - habitacio
        r1 r2 r3 r4 - reserva
        d1  d2  d3  d4  d5  d6  d7  d8  d9  d10
        d11 d12 d13 d14 d15 d16 d17 d18 d19 d20
        d21 d22 d23 d24 d25 d26 d27 d28 d29 d30 - dia
    )

    (:init
        (= (capacitat_habitacio h1) 2)
        (= (capacitat_habitacio h2) 4)
        (= (capacitat_habitacio h3) 1)

        (dia_reserva r1 d3)
        (dia_reserva r1 d4)
        (dia_reserva r1 d5)
        (dia_reserva r1 d6)
        (dia_reserva r1 d7)
        (= (persones_reserva r1) 3)

        (dia_reserva r2 d1)
        (dia_reserva r2 d2)
        (dia_reserva r2 d3)
        (dia_reserva r2 d4)
        (= (persones_reserva r2) 2)


        (dia_reserva r3 d3)
        (dia_reserva r3 d4)
        (dia_reserva r3 d5)
        (= (persones_reserva r3) 4)


        (dia_reserva r4 d5)
        (dia_reserva r4 d6)
        (dia_reserva r4 d7)
        (dia_reserva r4 d8)
        (dia_reserva r4 d9)
        (= (persones_reserva r4) 1)
    )

    (:goal 
        (forall (?r - reserva) 
            (reserva_servida ?r)
        )
    )
)