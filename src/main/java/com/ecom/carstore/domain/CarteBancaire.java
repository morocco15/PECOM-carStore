package com.ecom.carstore.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CarteBancaire.
 */
@Entity
@Table(name = "carte_bancaire")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CarteBancaire implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id", unique = true)
    private Long id;

    @Column(name = "code")
    private Long code;

    @Column(name = "expiration")
    private ZonedDateTime expiration;

    @Column(name = "prenom")
    private String prenom;

    @Column(name = "nom")
    private String nom;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CarteBancaire id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCode() {
        return this.code;
    }

    public CarteBancaire code(Long code) {
        this.setCode(code);
        return this;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public ZonedDateTime getExpiration() {
        return this.expiration;
    }

    public CarteBancaire expiration(ZonedDateTime expiration) {
        this.setExpiration(expiration);
        return this;
    }

    public void setExpiration(ZonedDateTime expiration) {
        this.expiration = expiration;
    }

    public String getPrenom() {
        return this.prenom;
    }

    public CarteBancaire prenom(String prenom) {
        this.setPrenom(prenom);
        return this;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNom() {
        return this.nom;
    }

    public CarteBancaire nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CarteBancaire)) {
            return false;
        }
        return id != null && id.equals(((CarteBancaire) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CarteBancaire{" +
            "id=" + getId() +
            ", code=" + getCode() +
            ", expiration='" + getExpiration() + "'" +
            ", prenom='" + getPrenom() + "'" +
            ", nom='" + getNom() + "'" +
            "}";
    }
}
