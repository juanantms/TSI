(define (domain Dominio5)

(:requirements :strips :typing  :disjunctive-preconditions :negative-preconditions :conditional-effects)

(:types 
    posicionable localizacion - object
    Unidades Edificios Recursos Investigacion - posicionable 
    TiposUnidades - Unidades
    TiposEdificios - Edificios
    TiposRecursos - Recursos
    TiposInvestigacion - Investigacion
)

(:constants 
    CentroDeMando Barracones Extractor BahiaIngenieria - TiposEdificios
    VCE Marine Soldado - TiposUnidades
    Minerales Gas - TiposRecursos 
    SoldadoUniversal - TiposInvestigacion
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
    ; Nos indica el tipo de investigación
    (esTipoInvestigacion ?i - Investigacion ?ti - TiposInvestigacion)
    ; Nos indica el recurso que el edificio necesita para su construcción
    (necesitaRecurso ?e - TiposEdificios ?r - TiposRecursos)
    ; Nos indica el recurso que la investigación necesita
    (InvestigacionNecesitaRecurso ?e - TiposInvestigacion ?r - TiposRecursos)
    ; Nos indica si la unidad está libre
    (libre ?u - Unidades)
    ; Nos indica si un recurso se está extrayendo
    (hayRecurso ?r - TiposRecursos)
    ; Nos indica si la unidad ha sido reclutada
    (reclutada ?u - Unidades)
    ; Nos indica que la investigación ya se ha investigado
    (investigado ?i - Investigacion)
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
                (hayRecurso Gas)
            )
            ; Si es Mineral establecemos que se está extrayendo
            (when (esTipoRecursos ?r Minerales) 
                (hayRecurso Minerales)
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
            ; Comprobamos si el edificio no está construido
            (not (construido ?e))

            ; Comprobamos si el VCE se sitúa en la localización donde se construirá el edificio
            (esta ?u ?x)

            ; Comprobamos el tipo de edificio
            (exists (?te - TiposEdificios)
                (and
                    (esTipoEdificio ?e ?te)
                    ; Comprobamos los recursos necesarios para su construcción
                    (forall (?r - TiposRecursos)
                        (imply (necesitaRecurso ?te ?r)
                            (hayRecurso ?r)
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

            ; Si la unidad es VCE, se reclutará en el Centro de Mando y tendrá un coste de minerales
            (imply (esTipoUnidades ?u VCE)
                (and 
                    (esTipoEdificio ?e CentroDeMando)
                    ; Comprobamos si hay minerales 
                    (hayRecurso Minerales)
                )
            )
            ; Si la unidad es Marine, se reclutará en los Barracones y tendrá un coste de minerales
            (imply (esTipoUnidades ?u Marine)
                (and
                    (esTipoEdificio ?e Barracones)
                    ; Comprobamos si hay minerales 
                    (hayRecurso Minerales)  
                )
            )
            ; Si la unidad es Soldado, se reclutará en los Barracones y tendrá un coste de minerales y de gas vespeno
            ; Además reclutar un Soldado requiere la investigación de SoldadoUniversal
            (imply (esTipoUnidades ?u Soldado)
                (and
                    ; Comprobamos si hay una investigación para reclutar Soldados
                    (exists (?i - Investigacion)
                        (and 
                            (esTipoInvestigacion ?i SoldadoUniversal)
                            (investigado ?i)
                        )
                    )

                    (esTipoEdificio ?e Barracones)
                    ; Comprobamos si hay minerales 
                    (hayRecurso Minerales)
                    ; Comprobamos si hay gas 
                    (hayRecurso Gas)
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
        )
)

; Acción para realizar una investigación en el edificio Bahia de Ingenieria
(:action Investigar
    :parameters (?e - Edificios ?i - Investigacion)
    :precondition 
        (and 
            ; Comprobamos que el edificio es Bahia de Ingenieria
            (esTipoEdificio ?e BahiaIngenieria)
            ; Comprobamos si está construido
            (construido ?e)
            
            ; Comprobamos si no se ha investigado la investigación 'i'
            (not (investigado ?i))

            ; Comprobamos los recursos que necesita el tipo de investigación
            (exists (?ti - TiposInvestigacion)
                (and
                    (esTipoInvestigacion ?i ?ti)
                    (forall (?r - TiposRecursos)
                        (imply (InvestigacionNecesitaRecurso ?ti ?r)
                            (hayRecurso ?r)
                        )
                    )    
                )
            )
        )
    :effect 
        (and 
            ; Indicamos como investigado 'i'   
            (investigado ?i)
        )
)

)