package com.ecom.carstore.service;

import com.ecom.carstore.domain.Categorie;
import com.ecom.carstore.repository.CategorieRepository;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CategorieService {

    private final Logger log = LoggerFactory.getLogger(CategorieService.class);

    private final CategorieRepository categorieRepository;

    public CategorieService(CategorieRepository categorieRepository) {
        this.categorieRepository = categorieRepository;
    }

    public List<String> listCategorie() {
        return categorieRepository.listCategorie();
    }
}
