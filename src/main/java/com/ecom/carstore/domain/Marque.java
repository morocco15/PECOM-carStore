package com.ecom.carstore.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Marque.
 */
@Entity
@Table(name = "marque")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Marque implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "marque")
    private String marque;

    @OneToMany(mappedBy = "marque")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "marque", "categories", "commande", "panier", "vendeur", "souhaits" }, allowSetters = true)
    private Set<Voiture> voitures = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Marque id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMarque() {
        return this.marque;
    }

    public Marque marque(String marque) {
        this.setMarque(marque);
        return this;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public Set<Voiture> getVoitures() {
        return this.voitures;
    }

    public void setVoitures(Set<Voiture> voitures) {
        if (this.voitures != null) {
            this.voitures.forEach(i -> i.setMarque(null));
        }
        if (voitures != null) {
            voitures.forEach(i -> i.setMarque(this));
        }
        this.voitures = voitures;
    }

    public Marque voitures(Set<Voiture> voitures) {
        this.setVoitures(voitures);
        return this;
    }

    public Marque addVoitures(Voiture voiture) {
        this.voitures.add(voiture);
        voiture.setMarque(this);
        return this;
    }

    public Marque removeVoitures(Voiture voiture) {
        this.voitures.remove(voiture);
        voiture.setMarque(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Marque)) {
            return false;
        }
        return id != null && id.equals(((Marque) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Marque{" +
            "id=" + getId() +
            ", marque='" + getMarque() + "'" +
            "}";
    }
}
