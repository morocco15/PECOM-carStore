package com.ecom.carstore.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Utilisateur.
 */
@Entity
@Table(name = "utilisateur")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Utilisateur implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(unique = true)
    private User idcompte;

    @OneToOne
    @JoinColumn(unique = true)
    private CarteBancaire idPaiment;

    @JsonIgnoreProperties(value = { "utilisateur", "voitures" }, allowSetters = true)
    @OneToOne(mappedBy = "utilisateur")
    private Panier panier;

    @JsonIgnoreProperties(value = { "utilisateur", "voitures" }, allowSetters = true)
    @OneToOne(mappedBy = "utilisateur")
    private Souhait souhait;

    @OneToMany(mappedBy = "acheteur")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "voitures", "acheteur" }, allowSetters = true)
    private Set<Commande> commandes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Utilisateur id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getIdcompte() {
        return this.idcompte;
    }

    public void setIdcompte(User user) {
        this.idcompte = user;
    }

    public Utilisateur idcompte(User user) {
        this.setIdcompte(user);
        return this;
    }

    public CarteBancaire getIdPaiment() {
        return this.idPaiment;
    }

    public void setIdPaiment(CarteBancaire carteBancaire) {
        this.idPaiment = carteBancaire;
    }

    public Utilisateur idPaiment(CarteBancaire carteBancaire) {
        this.setIdPaiment(carteBancaire);
        return this;
    }

    public Panier getPanier() {
        return this.panier;
    }

    public void setPanier(Panier panier) {
        if (this.panier != null) {
            this.panier.setUtilisateur(null);
        }
        if (panier != null) {
            panier.setUtilisateur(this);
        }
        this.panier = panier;
    }

    public Utilisateur panier(Panier panier) {
        this.setPanier(panier);
        return this;
    }

    public Souhait getSouhait() {
        return this.souhait;
    }

    public void setSouhait(Souhait souhait) {
        if (this.souhait != null) {
            this.souhait.setUtilisateur(null);
        }
        if (souhait != null) {
            souhait.setUtilisateur(this);
        }
        this.souhait = souhait;
    }

    public Utilisateur souhait(Souhait souhait) {
        this.setSouhait(souhait);
        return this;
    }

    public Set<Commande> getCommandes() {
        return this.commandes;
    }

    public void setCommandes(Set<Commande> commandes) {
        if (this.commandes != null) {
            this.commandes.forEach(i -> i.setAcheteur(null));
        }
        if (commandes != null) {
            commandes.forEach(i -> i.setAcheteur(this));
        }
        this.commandes = commandes;
    }

    public Utilisateur commandes(Set<Commande> commandes) {
        this.setCommandes(commandes);
        return this;
    }

    public Utilisateur addCommandes(Commande commande) {
        this.commandes.add(commande);
        commande.setAcheteur(this);
        return this;
    }

    public Utilisateur removeCommandes(Commande commande) {
        this.commandes.remove(commande);
        commande.setAcheteur(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Utilisateur)) {
            return false;
        }
        return id != null && id.equals(((Utilisateur) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Utilisateur{" +
            "id=" + getId() +
            "}";
    }
}
