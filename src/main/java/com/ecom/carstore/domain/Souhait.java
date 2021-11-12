package com.ecom.carstore.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Souhait.
 */
@Entity
@Table(name = "souhait")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Souhait implements Serializable {

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

    @ManyToMany
    @JoinTable(
        name = "rel_souhait__voitures",
        joinColumns = @JoinColumn(name = "souhait_id"),
        inverseJoinColumns = @JoinColumn(name = "voitures_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "marque", "categories", "commande", "panier", "vendeur", "souhaits" }, allowSetters = true)
    private Set<Voiture> voitures = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Souhait id(Long id) {
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

    public Souhait utilisateur(Utilisateur utilisateur) {
        this.setUtilisateur(utilisateur);
        return this;
    }

    public Set<Voiture> getVoitures() {
        return this.voitures;
    }

    public void setVoitures(Set<Voiture> voitures) {
        this.voitures = voitures;
    }

    public Souhait voitures(Set<Voiture> voitures) {
        this.setVoitures(voitures);
        return this;
    }

    public Souhait addVoitures(Voiture voiture) {
        this.voitures.add(voiture);
        voiture.getSouhaits().add(this);
        return this;
    }

    public Souhait removeVoitures(Voiture voiture) {
        this.voitures.remove(voiture);
        voiture.getSouhaits().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Souhait)) {
            return false;
        }
        return id != null && id.equals(((Souhait) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Souhait{" +
            "id=" + getId() +
            "}";
    }
}
