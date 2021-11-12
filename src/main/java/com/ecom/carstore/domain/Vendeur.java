package com.ecom.carstore.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Vendeur.
 */
@Entity
@Table(name = "vendeur")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Vendeur implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id", unique = true)
    private Long id;

    @Column(name = "login")
    private String login;

    @Column(name = "coordonnee")
    private String coordonnee;

    @OneToMany(mappedBy = "vendeur")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "marque", "categories", "commande", "panier", "vendeur", "souhaits" }, allowSetters = true)
    private Set<Voiture> voitures = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Vendeur id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return this.login;
    }

    public Vendeur login(String login) {
        this.setLogin(login);
        return this;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getCoordonnee() {
        return this.coordonnee;
    }

    public Vendeur coordonnee(String coordonnee) {
        this.setCoordonnee(coordonnee);
        return this;
    }

    public void setCoordonnee(String coordonnee) {
        this.coordonnee = coordonnee;
    }

    public Set<Voiture> getVoitures() {
        return this.voitures;
    }

    public void setVoitures(Set<Voiture> voitures) {
        if (this.voitures != null) {
            this.voitures.forEach(i -> i.setVendeur(null));
        }
        if (voitures != null) {
            voitures.forEach(i -> i.setVendeur(this));
        }
        this.voitures = voitures;
    }

    public Vendeur voitures(Set<Voiture> voitures) {
        this.setVoitures(voitures);
        return this;
    }

    public Vendeur addVoitures(Voiture voiture) {
        this.voitures.add(voiture);
        voiture.setVendeur(this);
        return this;
    }

    public Vendeur removeVoitures(Voiture voiture) {
        this.voitures.remove(voiture);
        voiture.setVendeur(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Vendeur)) {
            return false;
        }
        return id != null && id.equals(((Vendeur) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Vendeur{" +
            "id=" + getId() +
            ", login='" + getLogin() + "'" +
            ", coordonnee='" + getCoordonnee() + "'" +
            "}";
    }
}
