package com.ecom.carstore.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Panier.
 */
@Entity
@Table(name = "panier")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Panier implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @JsonIgnoreProperties(value = { "idcompte", "idPaiment", "panier", "souhait", "commandes" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Utilisateur utilisateur;

    @OneToMany(mappedBy = "panier")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "marque", "categories", "commande", "panier", "vendeur", "souhaits" }, allowSetters = true)
    private Set<Voiture> voitures = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Panier id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Utilisateur getUtilisateur() {
        return this.utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public Panier utilisateur(Utilisateur utilisateur) {
        this.setUtilisateur(utilisateur);
        return this;
    }

    public Set<Voiture> getVoitures() {
        return this.voitures;
    }

    public void setVoitures(Set<Voiture> voitures) {
        if (this.voitures != null) {
            this.voitures.forEach(i -> i.setPanier(null));
        }
        if (voitures != null) {
            voitures.forEach(i -> i.setPanier(this));
        }
        this.voitures = voitures;
    }

    public Panier voitures(Set<Voiture> voitures) {
        this.setVoitures(voitures);
        return this;
    }

    public Panier addVoitures(Voiture voiture) {
        this.voitures.add(voiture);
        voiture.setPanier(this);
        return this;
    }

    public Panier removeVoitures(Voiture voiture) {
        this.voitures.remove(voiture);
        voiture.setPanier(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Panier)) {
            return false;
        }
        return id != null && id.equals(((Panier) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Panier{" +
            "id=" + getId() +
            "}";
    }
}
