package com.ecom.carstore.service;

import com.ecom.carstore.domain.Voiture;
import com.ecom.carstore.repository.VoitureRepository;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class VoitureService {

    private final Logger log = LoggerFactory.getLogger(VoitureService.class);

    private VoitureRepository voitureRepository;

    public VoitureService(VoitureRepository voitureRepository) {
        this.voitureRepository = voitureRepository;
    }

    public Page<Voiture> getModelRecent(int debut, int fin) {
        return voitureRepository.derniereVoitureAjouter(debut, fin);
    }
}
