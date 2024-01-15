(define (domain Dominio1)

(:requirements :strips :typing)

(:types 
    posicionable localizacion - object
    Unidades Edificios TiposUnidades TiposEdificios Recursos - posicionable  
)

(:constants 
    CentroDeMando Barracones - TiposEdificios
    VCE - TiposUnidades
    Minerales Gas - Recursos 
)


(:predicates
    ; Nos indica si el 'posicionable' esta en una localizacion del mapa
    (esta ?p - posicionable ?x - localizacion)
    ; Nos indica si existe un camino entre 2 localizaciones del mapa
    (existeCamino ?x ?y - localizacion)
    ; Nos indica si el edificio está construido
    (construido ?e - Edificios)
    ; Nos indica si la unidad esta extrayendo un recurso
    (extrae ?vce - Unidades ?r - Recursos)
    ; Nos indica el tipo de edificio
    (esTipoEdificio ?e - Edificios ?te - TiposEdificios)
    ; Nos indica el tipo de unidad
    (esTipoUnidades ?u - Unidades ?tu - TiposUnidades)
)

; Acción para mover unidades por el mapa
(:action Navegar
    :parameters (?u - Unidades ?locOri ?locDest - localizacion)
    :precondition 
        (and 
            ; Comprobamos si hay camino entre el punto de origen y el punto destino
            (existeCamino ?locOri ?locDest)
            ; Comprobamos si la unidad esta en el punto origen
            (esta ?u ?locOri)
        )
    :effect 
        (and 
            ; Actualizamos la nueva posición
            (esta ?u ?locDest)
            (not (esta ?u ?locOri))
        )
)

; Acción para asignar a una unidad VCE a extraer un resurso
(:action Asignar
    :parameters (?u - Unidades ?x - localizacion ?r - Recursos)
    :precondition 
        (and 
            ; Comprobamos si la unidad esta en la localización 'x'
            (esta ?u ?x)
            ; Comprobamos si el recurso esta en la localización 'x'
            (esta ?r ?x)
        )
    :effect 
        (and 
            ; Le asignamos la extracción del recurso
            (extrae ?u ?r)
        )
)

)