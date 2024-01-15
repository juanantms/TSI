(define (domain Dominio2)

(:requirements :strips :typing :negative-preconditions :disjunctive-preconditions)

(:types 
    posicionable localizacion - object
    Unidades Edificios Recursos TiposUnidades TiposEdificios TiposRecursos  - posicionable 
)

(:constants 
    CentroDeMando Barracones Extractor - TiposEdificios
    VCE - TiposUnidades
    Minerales Gas - TiposRecursos 
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
    ; Nos indica el tipo de recurso
    (esTipoRecursos ?r - Recursos ?tr - TiposRecursos)
    ; Nos indica el recurso que el edificio necesita para su construcción
    (necesitaRecurso ?e - TiposEdificios ?r - TiposRecursos)
    ; Nos indica si la unidad está libre
    (libre ?u - Unidades)
)

; Acción para mover unidades por el mapa
(:action Navegar
    :parameters (?u - Unidades ?locOri ?locDest - localizacion)
    :precondition 
        (and 
            ; Comprobamos si la unidad esta libre
            (libre ?u)
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
            ; Comprobamos si la unidad esta libre
            (libre ?u)
            ; Comprobamos si la unidad esta en la localización 'x'
            (esta ?u ?x)
            ; Comprobamos si el recurso esta en la localización 'x'
            (esta ?r ?x)
            
            ; Si el recurso es Gas
            (imply (esTipoRecursos ?r Gas)
                (and
                    ; Debe de haber un Extractor construido en la posición donde hay Gas
                    (exists (?e - Edificios)
                        (and
                            (esta ?e ?x)
                            (esTipoEdificio ?e Extractor)
                        )
                    )
                )
            )
        )
    :effect 
        (and 
            ; Le asignamos la extracción del recurso
            (extrae ?u ?r)
            ; La unidad deja de estar libre
            (not (libre ?u))
        )
)

; Acción para construir un edificio en una posición
(:action Construir
    :parameters (?u - Unidades ?e - Edificios ?x - localizacion ?r - Recursos)
    :precondition 
        (and 
            ; La unidad encargada debe se de tipo VCE
            (esTipoUnidades ?u VCE)
            ; El VCE debe de estar libre
            (libre ?u)
            ; Comprobamos si el edificio no está construido
            (not (construido ?e))

            ; Buscamos una unidad que extraiga el recurso que se necesita para la construcción del edificio
            (exists (?u2 - Unidades) 
                (and 
                    (extrae ?u2 ?r)
                )
            )

            ; Comprobamos si el VCE se sitúa en la localización donde se construirá el edificio
            (esta ?u ?x)
        )
    :effect 
        (and 
            ; Establecemos la posición del edificio
            (esta ?e ?x)
            ; Marcamos el edificio como construido
            (construido ?e)
        )
)

)