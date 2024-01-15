(define (problem Problema1) (:domain Dominio1)

(:objects 
    CentroDeMando1 - Edificios
    VCE1 - Unidades
    mineral - Recursos
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
    (esTipoUnidades VCE1 VCE)
    (construido CentroDeMando1)
    (esta CentroDeMando1 loc11)
    (esta VCE1 loc11)
    (esta mineral loc22)
    (esta mineral loc32)
)

(:goal 
    (and
        (extrae VCE1 mineral)
    )
)
)