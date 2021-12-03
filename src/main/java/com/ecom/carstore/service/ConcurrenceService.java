package com.ecom.carstore.service;

import com.ecom.carstore.domain.Voiture;
import com.ecom.carstore.repository.VoitureRepository;

public class ConcurrenceService {

    VoitureRepository voitureRepository;

    boolean statusModifiable(Voiture voiture){


        return true;
    }
}
