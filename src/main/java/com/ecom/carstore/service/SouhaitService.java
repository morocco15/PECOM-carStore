package com.ecom.carstore.service;

import com.ecom.carstore.domain.Souhait;
import com.ecom.carstore.domain.User;
import com.ecom.carstore.domain.Utilisateur;
import com.ecom.carstore.domain.Voiture;
import com.ecom.carstore.repository.SouhaitRepository;
import com.ecom.carstore.repository.UserRepository;
import com.ecom.carstore.repository.UtilisateurRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class SouhaitService {

    private final Logger log = LoggerFactory.getLogger(SouhaitService.class);

    private final SouhaitRepository souhaitRepository;
    private final UserRepository userRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final VoitureService voitureService;
    public SouhaitService(SouhaitRepository souhaitRepository,
                           UserRepository userRepository,
                           UtilisateurRepository utilisateurRepository,
                           VoitureService voitureService) {
        this.souhaitRepository = souhaitRepository;
        this.userRepository = userRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.voitureService = voitureService;
    }

    public boolean ajouterVoitureDansSouhait(String username,Long idVoiture){

        User user = userRepository.findOneByUsername(username);
        if(user!=null){
            Utilisateur utilisateur = utilisateurRepository.getByidcompte(user);
            Souhait souhait = utilisateur.getSouhait();
            Voiture voiture = voitureService.findOneById(idVoiture);
            if(souhait!=null && voiture!=null){
                if(souhait.getVoitures().contains(voiture)){
                    return false;
                }else {
                    souhait.addVoitures(voiture);
                    return true;
                }
            }else {
                return false;
            }
        }
        return false;
    }

    public boolean supprimerVoitureDuSouhait(String username,Long idVoiture){
        User user = userRepository.findOneByUsername(username);
        if(user!=null) {
            Utilisateur utilisateur = utilisateurRepository.getByidcompte(user);
            Souhait souhait = utilisateur.getSouhait();
            //Voiture voiture = souhaitContains(souhait,idVoiture);
            Voiture voiture = voitureService.findOneById(idVoiture);//il faut modifier
            if(souhait.getVoitures().contains(voiture)){
                souhait.removeVoitures(voiture);
                return true;
            }else {
                return false;
            }
        }
        return false;
    }
    public Voiture souhaitContains(Souhait souhait,Long idVoiture){
        for(Voiture voiture:souhait.getVoitures()){
            if(voiture.getId()==idVoiture)
                return voiture;
        }
        return null;
    }

    public List<Voiture> getSouhait(String username){
        List<Voiture> voitures = null;
        User user = userRepository.findOneByUsername(username);
        if(user!=null){
            Utilisateur utilisateur = utilisateurRepository.getByidcompte(user);
            Souhait souhait = utilisateur.getSouhait();
            if(souhait==null){
                return voitures;
            }else {
                voitures = new ArrayList<Voiture>(souhait.getVoitures());
                return voitures;
            }
        }
        return voitures;
    }

}
