(define (problem Problema5) (:domain Dominio5)

(:objects 
    CentroDeMando1 Extractor1 Barracones1 BahiaIngenieria1 - Edificios
    VCE1 VCE2 VCE3 Marine1 Marine2 Soldado1 - Unidades
    mineral1 mineral2 gas1 - Recursos
    SoldadoUniversal1 - Investigacion
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
    (esTipoEdificio BahiaIngenieria1 BahiaIngenieria)

    (esTipoInvestigacion SoldadoUniversal1 SoldadoUniversal)

    (esTipoUnidades VCE1 VCE)
    (esTipoUnidades VCE2 VCE)
    (esTipoUnidades VCE3 VCE)
    (esTipoUnidades Marine1 Marine)
    (esTipoUnidades Marine2 Marine)
    (esTipoUnidades Soldado1 Soldado)

    (construido CentroDeMando1)
    (esta CentroDeMando1 loc11)
    
    (esta VCE1 loc11)
    (reclutada VCE1)
    (libre VCE1)
    

    (esTipoRecursos mineral1 Minerales)
    (esTipoRecursos mineral2 Minerales)
    (esTipoRecursos gas1 Gas)
    (esta mineral1 loc22)
    (esta mineral2 loc32)
    (esta gas1 loc44)

    (necesitaRecurso Extractor Minerales)
    (necesitaRecurso Barracones Minerales)
    (necesitaRecurso Barracones Gas)
    (necesitaRecurso BahiaIngenieria Minerales)
    (necesitaRecurso BahiaIngenieria Gas)
    (InvestigacionNecesitaRecurso SoldadoUniversal Minerales)
    (InvestigacionNecesitaRecurso SoldadoUniversal Gas)
    
)

(:goal 
    (and
        (esta Barracones1 loc32)
        (esta BahiaIngenieria1 loc12)
        (esta Marine1 loc31)
        (esta Marine2 loc24)
        (esta Soldado1 loc12)
    )
    
)
 
)