package com.ecom.carstore.service;

import com.ecom.carstore.domain.Categorie;
import com.ecom.carstore.domain.Panier;
import com.ecom.carstore.domain.User;
import com.ecom.carstore.domain.Voiture;
import com.ecom.carstore.domain.enumeration.Etat;
import com.ecom.carstore.domain.enumeration.Statut;
import com.ecom.carstore.repository.PanierRepository;
import com.ecom.carstore.repository.UserRepository;
import com.ecom.carstore.repository.VoitureRepository;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class VoitureService {

    private final Logger log = LoggerFactory.getLogger(VoitureService.class);

    @Autowired
    private VoitureRepository voitureRepository;

    public VoitureService(VoitureRepository voitureRepository) {
        this.voitureRepository = voitureRepository;
    }

    public List<Voiture> getModelRecent(int debut, int fin) {
        Page<Voiture> v = voitureRepository.derniereVoitureAjouter(PageRequest.of(debut, fin));
        return v.getContent();
    }

    public Voiture findOneById(Long id) {
        return voitureRepository.getById(id);
    }

    private boolean statusModifiable(Long id, int version) {
        return version == voitureRepository.getVoitureVersion(id);
    }

    public boolean reserverVoiture(Long id, int version) {
        Voiture voiture = voitureRepository.getById(id);
        if (voiture.getStatut() == Statut.LIBRE && statusModifiable(id, version)) {
            voiture.setStatut(Statut.RESERVER);
            voiture.setVersion(voiture.getVersion() + 1);
            voitureRepository.save(voiture);
            return true;
        } else {
            return false;
        }
    }

    public void libererVoiture(Voiture voiture) {
        if (voiture.getStatut() == Statut.RESERVER) {
            voiture.setStatut(Statut.LIBRE);
            voiture.setVersion(voiture.getVersion() + 1);
            voitureRepository.save(voiture);
        }
    }

    public List<Voiture> maxPrix(Long max) {
        return voitureRepository.maxPrix(max);
    }

    public List<Voiture> minPrix(Long min) {
        return voitureRepository.minPrix(min);
    }

    public List<Voiture> limitePrix(Long min, Long max) {
        return voitureRepository.limitePrix(min, max);
    }

    public List<Voiture> limiteEtat(Etat etat) {
        return voitureRepository.limiteEtat(etat);
    }
    /*public List<Voiture> limiteCategorie(Categorie categorie) {
        return voitureRepository.limiteCategorie(categorie);
    }*/
}
