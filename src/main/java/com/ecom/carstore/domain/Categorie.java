package com.ecom.carstore.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Categorie.
 */
@Entity
@Table(name = "categorie")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Categorie implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "categorie")
    private String categorie;

    @ManyToMany(mappedBy = "categories")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "marque", "categories", "commande", "panier", "vendeur", "souhaits" }, allowSetters = true)
    private Set<Voiture> voitures = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Categorie id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategorie() {
        return this.categorie;
    }

    public Categorie categorie(String categorie) {
        this.setCategorie(categorie);
        return this;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public Set<Voiture> getVoitures() {
        return this.voitures;
    }

    public void setVoitures(Set<Voiture> voitures) {
        if (this.voitures != null) {
            this.voitures.forEach(i -> i.removeCategories(this));
        }
        if (voitures != null) {
            voitures.forEach(i -> i.addCategories(this));
        }
        this.voitures = voitures;
    }

    public Categorie voitures(Set<Voiture> voitures) {
        this.setVoitures(voitures);
        return this;
    }

    public Categorie addVoitures(Voiture voiture) {
        this.voitures.add(voiture);
        voiture.getCategories().add(this);
        return this;
    }

    public Categorie removeVoitures(Voiture voiture) {
        this.voitures.remove(voiture);
        voiture.getCategories().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Categorie)) {
            return false;
        }
        return id != null && id.equals(((Categorie) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Categorie{" +
            "id=" + getId() +
            ", categorie='" + getCategorie() + "'" +
            "}";
    }
}
