package com.ecom.carstore.domain;

import com.ecom.carstore.domain.enumeration.Livraison;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Commande.
 */
@Entity
@Table(name = "commande")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Commande implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id", unique = true)
    private Long id;

    @Column(name = "date_commande")
    private ZonedDateTime dateCommande;

    @Enumerated(EnumType.STRING)
    @Column(name = "mode_livraison")
    private Livraison modeLivraison;

    @OneToMany(mappedBy = "commande")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "marque", "categories", "commande", "panier", "vendeur", "souhaits" }, allowSetters = true)
    private Set<Voiture> voitures = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "idcompte", "idPaiment", "panier", "souhait", "commandes" }, allowSetters = true)
    private Utilisateur acheteur;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Commande id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDateCommande() {
        return this.dateCommande;
    }

    public Commande dateCommande(ZonedDateTime dateCommande) {
        this.setDateCommande(dateCommande);
        return this;
    }

    public void setDateCommande(ZonedDateTime dateCommande) {
        this.dateCommande = dateCommande;
    }

    public Livraison getModeLivraison() {
        return this.modeLivraison;
    }

    public Commande modeLivraison(Livraison modeLivraison) {
        this.setModeLivraison(modeLivraison);
        return this;
    }

    public void setModeLivraison(Livraison modeLivraison) {
        this.modeLivraison = modeLivraison;
    }

    public Set<Voiture> getVoitures() {
        return this.voitures;
    }

    public void setVoitures(Set<Voiture> voitures) {
        if (this.voitures != null) {
            this.voitures.forEach(i -> i.setCommande(null));
        }
        if (voitures != null) {
            voitures.forEach(i -> i.setCommande(this));
        }
        this.voitures = voitures;
    }

    public Commande voitures(Set<Voiture> voitures) {
        this.setVoitures(voitures);
        return this;
    }

    public Commande addVoitures(Voiture voiture) {
        this.voitures.add(voiture);
        voiture.setCommande(this);
        return this;
    }

    public Commande removeVoitures(Voiture voiture) {
        this.voitures.remove(voiture);
        voiture.setCommande(null);
        return this;
    }

    public Utilisateur getAcheteur() {
        return this.acheteur;
    }

    public void setAcheteur(Utilisateur utilisateur) {
        this.acheteur = utilisateur;
    }

    public Commande acheteur(Utilisateur utilisateur) {
        this.setAcheteur(utilisateur);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Commande)) {
            return false;
        }
        return id != null && id.equals(((Commande) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Commande{" +
            "id=" + getId() +
            ", dateCommande='" + getDateCommande() + "'" +
            ", modeLivraison='" + getModeLivraison() + "'" +
            "}";
    }
}
