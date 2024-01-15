(define (problem Problema3) (:domain Dominio3)

(:objects 
    CentroDeMando1 Extractor1 Barracones1 - Edificios
    VCE1 VCE2 VCE3 - Unidades
    mineral1 mineral2 gas1 - Recursos
    loc11 loc12 loc21 loc22 loc31 loc32 loc23 loc33 loc13 loc14 loc24 loc34 loc44 - localizacion
)

(:init
    (existeCamino loc11 loc12)
    (existeCamino loc11 loc21)
    (existeCamino loc12 loc11)
    (existeCamino loc12 loc22)
    (existeCamino loc21 loc11)
    (existeCamino loc21 loc31)
    (existeCamino loc31 loc32)
    (existeCamino loc31 loc21)
    (existeCamino loc32 loc31)
    (existeCamino loc32 loc22)
    (existeCamino loc22 loc12)
    (existeCamino loc22 loc32)
    (existeCamino loc22 loc23)
    (existeCamino loc23 loc13)
    (existeCamino loc23 loc33)
    (existeCamino loc23 loc22)
    (existeCamino loc13 loc23)
    (existeCamino loc13 loc14)
    (existeCamino loc14 loc13)
    (existeCamino loc14 loc24)
    (existeCamino loc24 loc14)
    (existeCamino loc24 loc34)
    (existeCamino loc34 loc24)
    (existeCamino loc34 loc44)
    (existeCamino loc34 loc33)
    (existeCamino loc44 loc34)
    (existeCamino loc33 loc34)
    (existeCamino loc33 loc23)   

    (esTipoEdificio CentroDeMando1 CentroDeMando)
    (esTipoEdificio Extractor1 Extractor)
    (esTipoEdificio Barracones1 Barracones)
    (esTipoUnidades VCE1 VCE)
    (esTipoUnidades VCE2 VCE)
    (esTipoUnidades VCE3 VCE)

    (construido CentroDeMando1)
    (esta CentroDeMando1 loc11)
    
    (esta VCE1 loc11)
    (esta VCE2 loc11)
    (esta VCE3 loc11)
    (libre VCE1)
    (libre VCE2)
    (libre VCE3)

    (esTipoRecursos mineral1 Minerales)
    (esTipoRecursos mineral2 Minerales)
    (esTipoRecursos gas1 Gas)
    (esta mineral1 loc22)
    (esta mineral2 loc32)
    (esta gas1 loc44)

    (necesitaRecurso Extractor Minerales)
    (necesitaRecurso Barracones Minerales)
    (necesitaRecurso Barracones Gas)
    
)

(:goal 
    (and
        (esta Barracones1 loc33)
        (construido Barracones1)
    )
    
)
 
)