(define (domain Dominio7)

(:requirements :strips :typing  :disjunctive-preconditions :negative-preconditions :conditional-effects :fluents)

(:types 
    posicionable localizacion - object
    Unidades Edificios Recursos - posicionable 
    TiposUnidades - Unidades
    TiposEdificios - Edificios
    TiposRecursos - Recursos
)

(:constants 
    CentroDeMando Barracones Extractor - TiposEdificios
    VCE Marine Soldado - TiposUnidades
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
    ; Nos indica si la unidad está libre
    (libre ?u - Unidades)
    ; Nos indica si un recurso se está extrayendo
    (hayRecurso ?r - TiposRecursos)
    ; Nos indica si la unidad ha sido reclutada
    (reclutada ?u - Unidades)
)

(:functions
    ; Almacena la cantidad de recursos recolectados de cada tipo
    (AlmacenRecursos ?r - TiposRecursos)
    ; Almacena el número de unidades que están extrayendo un tipo de recurso
    (unidadesNodo ?r - TiposRecursos)
    ; Almacena los recursos necesarios para reclutar un tipo de unidad o para la construccion de un edificio
    (necesitaRecurso ?p - posicionable ?r - TiposRecursos)
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
            ; Comprobamos que la unidad es de tipo VCE
            (esTipoUnidades ?u VCE)
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
            ; Si es Gas establecemos que se está extrayendo
            (when (esTipoRecursos ?r Gas)
                (and
                    (hayRecurso Gas)
                    ; Incrementamos el número de VCEs que extraen el recurso
                    (increase (unidadesNodo Gas) 1) 
                )
            )
            ; Si es Mineral establecemos que se está extrayendo
            (when (esTipoRecursos ?r Minerales) 
                (and
                    (hayRecurso Minerales)
                    ; Incrementamos el número de VCEs que extraen el recurso
                    (increase (unidadesNodo Minerales) 1)
                )
            )
        )
)

; Acción para construir un edificio en una posición
(:action Construir
    :parameters (?u - Unidades ?e - Edificios ?x - localizacion)
    :precondition 
        (and 
            ; La unidad encargada debe se de tipo VCE
            (esTipoUnidades ?u VCE)
            ; El VCE debe de estar libre
            (libre ?u)
            ; Comprobamos si el VCE se sitúa en la localización donde se construirá el edificio
            (esta ?u ?x)
            ; Comprobamos si el edificio no está construido y no hay ningún otro construido en la localizacion 'x'
            (not (construido ?e))
            (not (exists (?ed - Edificios) (esta ?ed ?x)))

            ; Comprobamos el tipo de edificio
            (exists (?te - TiposEdificios)
                (and
                    (esTipoEdificio ?e ?te)
                    ; Comprobamos los recursos necesarios para su construcción
                    (forall (?r - TiposRecursos)
                        (>=
                            (AlmacenRecursos ?r)
                            (necesitaRecurso ?te ?r)
                        )
                    )
                )
            )
        )
    :effect 
        (and 
            ; Establecemos la posición del edificio
            (esta ?e ?x)
            ; Marcamos el edificio como construido
            (construido ?e)

            ; Actualizamos el almacen segun el tipo de edificio
            (when (esTipoEdificio ?e Extractor) 
                (decrease (AlmacenRecursos Minerales) (necesitaRecurso Extractor Minerales))
                
            )
            (when (esTipoEdificio ?e Barracones) 
                (and
                    (decrease (AlmacenRecursos Minerales) (necesitaRecurso Barracones Minerales))
                    (decrease (AlmacenRecursos Gas) (necesitaRecurso Barracones Gas))
                )
            )

        )
)

; Acción para reclutar una unidad la cual necesitará algún recurso
(:action Reclutar
    :parameters (?e - Edificios ?u - Unidades ?x - localizacion)
    :precondition 
        (and 
            ; Comprobamos si no esta reclutada
            (not (reclutada ?u))
            ; Comprobamos si el edificio donde se recluta la unidad esta construido
            (construido ?e)
            ; Comprobamos si el edificio se situa en 'x'
            (esta ?e ?x)
            ; Comprobamos que el edificio no sea un Extractor
            (not (esTipoEdificio ?e Extractor))

            ; Si el edificio es un CentroDeMando la unidad será un VCE
            (imply (esTipoEdificio ?e CentroDeMando)
                (esTipoUnidades ?u VCE)
            )
            ; Si el edificio es un Barracon la unidad será un Marine o un Soldado
            (imply (esTipoEdificio ?e Barracones)
                (or 
                    (esTipoUnidades ?u Soldado)
                    (esTipoUnidades ?u Marine)
                )
            )

            ; Comprobamos el tipo de unidad a reclutar
            (exists (?tu - TiposUnidades)
                (and
                    (esTipoUnidades ?u ?tu)
                    ; Comprobamos que tengamos los recursos suficientes en el almacén de los recursos necesarios para el reclutamiento de la unidad 
                    (forall (?tr - TiposRecursos)
                        (>=
                            (AlmacenRecursos ?tr)
                            (necesitaRecurso ?tu ?tr)
                        )
                    )
                )
            )

        )
    :effect 
        (and 
            ; Identificamos la unidad como reclutada
            (reclutada ?u)
            ; La unidad está libre
            (libre ?u)
            ; La unidad se situa en 'x'
            (esta ?u ?x)
            
            ; Comprobamos el tipo de unidad y actualizamos los almacenes de recursos
            (when (esTipoUnidades ?u VCE) 
                (and
                    (decrease (AlmacenRecursos Minerales) (necesitaRecurso VCE Minerales))
                    (decrease (AlmacenRecursos Gas) (necesitaRecurso VCE Gas))
                )
            )
            (when (esTipoUnidades ?u Marine) 
                (and
                    (decrease (AlmacenRecursos Minerales) (necesitaRecurso Marine Minerales))
                    (decrease (AlmacenRecursos Gas) (necesitaRecurso Marine Gas))
                )
            )
            (when (esTipoUnidades ?u Soldado) 
                (and
                    (decrease (AlmacenRecursos Minerales) (necesitaRecurso Soldado Minerales))
                    (decrease (AlmacenRecursos Gas) (necesitaRecurso Soldado Gas))
                )
            )

        )
)

; Acción para recolectar recursos
(:action Recolectar
    :parameters (?r - Recursos ?x - localizacion)
    :precondition 
        (and 
            ; En 'x' debe haber el recurso 'r'
            (esta ?r ?x)

            ; Comprobamos que exista al menos una unidad VCE que esté extrayendo el recurso
            (exists (?u - Unidades) 
                (and
                    (esTipoUnidades ?u VCE)
                    (extrae ?u ?r)
                )
            )

            ; Comprobamos que no sobrepase el limite del almacen
            (exists (?tr - TiposRecursos)
                (and
                    (esTipoRecursos ?r ?tr)
                    (<=
                        (+
                            (AlmacenRecursos ?tr)
                            (*
                                10
                                (unidadesNodo ?tr)
                            )
                        )
                        60
                    )
                )
            )

        )
    :effect 
        (and
            ; Añadimos al respectivo almacén el recurso recolectado 
            (when (esTipoRecursos ?r Minerales) 
                (increase (AlmacenRecursos Minerales) (* 10 (unidadesNodo Minerales)))
            )
            (when (esTipoRecursos ?r Gas) 
                (increase (AlmacenRecursos Gas) (* 10 (unidadesNodo Gas)))
            )
        )
)




)