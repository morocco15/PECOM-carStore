package com.ecom.carstore.domain;

import com.ecom.carstore.domain.enumeration.BoiteDeVitesse;
import com.ecom.carstore.domain.enumeration.Carburant;
import com.ecom.carstore.domain.enumeration.Etat;
import com.ecom.carstore.domain.enumeration.Statut;
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
 * A Voiture.
 */
@Entity
@Table(name = "voiture")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Voiture implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id", unique = true)
    private Long id;

    @Column(name = "model")
    private String model;

    @Column(name = "prix")
    private Long prix;

    @Column(name = "image_1")
    private String image1;

    @Column(name = "image_2")
    private String image2;

    @Column(name = "image_3")
    private String image3;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut")
    private Statut statut;

    @Column(name = "version")
    private Integer version;

    @Column(name = "mise_en_vente")
    private ZonedDateTime miseEnVente;

    @Enumerated(EnumType.STRING)
    @Column(name = "etat")
    private Etat etat;

    @Max(value = 5)
    @Column(name = "porte")
    private Integer porte;

    @Enumerated(EnumType.STRING)
    @Column(name = "boite_vitesse")
    private BoiteDeVitesse boiteVitesse;

    @Column(name = "co_2")
    private Integer co2;

    @Column(name = "chevaux")
    private Integer chevaux;

    @Enumerated(EnumType.STRING)
    @Column(name = "carburant")
    private Carburant carburant;

    @Column(name = "annees")
    private ZonedDateTime annees;

    @Column(name = "ville")
    private String ville;

    @Column(name = "code_postal")
    private Integer codePostal;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JsonIgnoreProperties(value = { "voitures" }, allowSetters = true)
    private Marque marque;

    @ManyToMany
    @JoinTable(
        name = "rel_voiture__categories",
        joinColumns = @JoinColumn(name = "voiture_id"),
        inverseJoinColumns = @JoinColumn(name = "categories_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "voitures" }, allowSetters = true)
    private Set<Categorie> categories = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "voitures", "acheteur" }, allowSetters = true)
    private Commande commande;

    @ManyToOne
    @JsonIgnoreProperties(value = { "utilisateur", "voitures" }, allowSetters = true)
    private Panier panier;

    @ManyToOne
    @JsonIgnoreProperties(value = { "voitures" }, allowSetters = true)
    private Vendeur vendeur;

    @ManyToMany(mappedBy = "voitures")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "utilisateur", "voitures" }, allowSetters = true)
    private Set<Souhait> souhaits = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Voiture id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModel() {
        return this.model;
    }

    public Voiture model(String model) {
        this.setModel(model);
        return this;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Long getPrix() {
        return this.prix;
    }

    public Voiture prix(Long prix) {
        this.setPrix(prix);
        return this;
    }

    public void setPrix(Long prix) {
        this.prix = prix;
    }

    public String getImage1() {
        return this.image1;
    }

    public Voiture image1(String image1) {
        this.setImage1(image1);
        return this;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return this.image2;
    }

    public Voiture image2(String image2) {
        this.setImage2(image2);
        return this;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage3() {
        return this.image3;
    }

    public Voiture image3(String image3) {
        this.setImage3(image3);
        return this;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }

    public Statut getStatut() {
        return this.statut;
    }

    public Voiture statut(Statut statut) {
        this.setStatut(statut);
        return this;
    }

    public void setStatut(Statut statut) {
        this.statut = statut;
    }

    public Integer getVersion() {
        return this.version;
    }

    public Voiture version(Integer version) {
        this.setVersion(version);
        return this;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public ZonedDateTime getMiseEnVente() {
        return this.miseEnVente;
    }

    public Voiture miseEnVente(ZonedDateTime miseEnVente) {
        this.setMiseEnVente(miseEnVente);
        return this;
    }

    public void setMiseEnVente(ZonedDateTime miseEnVente) {
        this.miseEnVente = miseEnVente;
    }

    public Etat getEtat() {
        return this.etat;
    }

    public Voiture etat(Etat etat) {
        this.setEtat(etat);
        return this;
    }

    public void setEtat(Etat etat) {
        this.etat = etat;
    }

    public Integer getPorte() {
        return this.porte;
    }

    public Voiture porte(Integer porte) {
        this.setPorte(porte);
        return this;
    }

    public void setPorte(Integer porte) {
        this.porte = porte;
    }

    public BoiteDeVitesse getBoiteVitesse() {
        return this.boiteVitesse;
    }

    public Voiture boiteVitesse(BoiteDeVitesse boiteVitesse) {
        this.setBoiteVitesse(boiteVitesse);
        return this;
    }

    public void setBoiteVitesse(BoiteDeVitesse boiteVitesse) {
        this.boiteVitesse = boiteVitesse;
    }

    public Integer getCo2() {
        return this.co2;
    }

    public Voiture co2(Integer co2) {
        this.setCo2(co2);
        return this;
    }

    public void setCo2(Integer co2) {
        this.co2 = co2;
    }

    public Integer getChevaux() {
        return this.chevaux;
    }

    public Voiture chevaux(Integer chevaux) {
        this.setChevaux(chevaux);
        return this;
    }

    public void setChevaux(Integer chevaux) {
        this.chevaux = chevaux;
    }

    public Carburant getCarburant() {
        return this.carburant;
    }

    public Voiture carburant(Carburant carburant) {
        this.setCarburant(carburant);
        return this;
    }

    public void setCarburant(Carburant carburant) {
        this.carburant = carburant;
    }

    public ZonedDateTime getAnnees() {
        return this.annees;
    }

    public Voiture annees(ZonedDateTime annees) {
        this.setAnnees(annees);
        return this;
    }

    public void setAnnees(ZonedDateTime annees) {
        this.annees = annees;
    }

    public String getVille() {
        return this.ville;
    }

    public Voiture ville(String ville) {
        this.setVille(ville);
        return this;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public Integer getCodePostal() {
        return this.codePostal;
    }

    public Voiture codePostal(Integer codePostal) {
        this.setCodePostal(codePostal);
        return this;
    }

    public void setCodePostal(Integer codePostal) {
        this.codePostal = codePostal;
    }

    public String getDescription() {
        return this.description;
    }

    public Voiture description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Marque getMarque() {
        return this.marque;
    }

    public void setMarque(Marque marque) {
        this.marque = marque;
    }

    public Voiture marque(Marque marque) {
        this.setMarque(marque);
        return this;
    }

    public Set<Categorie> getCategories() {
        return this.categories;
    }

    public void setCategories(Set<Categorie> categories) {
        this.categories = categories;
    }

    public Voiture categories(Set<Categorie> categories) {
        this.setCategories(categories);
        return this;
    }

    public Voiture addCategories(Categorie categorie) {
        this.categories.add(categorie);
        categorie.getVoitures().add(this);
        return this;
    }

    public Voiture removeCategories(Categorie categorie) {
        this.categories.remove(categorie);
        categorie.getVoitures().remove(this);
        return this;
    }

    public Commande getCommande() {
        return this.commande;
    }

    public void setCommande(Commande commande) {
        this.commande = commande;
    }

    public Voiture commande(Commande commande) {
        this.setCommande(commande);
        return this;
    }

    public Panier getPanier() {
        return this.panier;
    }

    public void setPanier(Panier panier) {
        this.panier = panier;
    }

    public Voiture panier(Panier panier) {
        this.setPanier(panier);
        return this;
    }

    public Vendeur getVendeur() {
        return this.vendeur;
    }

    public void setVendeur(Vendeur vendeur) {
        this.vendeur = vendeur;
    }

    public Voiture vendeur(Vendeur vendeur) {
        this.setVendeur(vendeur);
        return this;
    }

    public Set<Souhait> getSouhaits() {
        return this.souhaits;
    }

    public void setSouhaits(Set<Souhait> souhaits) {
        if (this.souhaits != null) {
            this.souhaits.forEach(i -> i.removeVoitures(this));
        }
        if (souhaits != null) {
            souhaits.forEach(i -> i.addVoitures(this));
        }
        this.souhaits = souhaits;
    }

    public Voiture souhaits(Set<Souhait> souhaits) {
        this.setSouhaits(souhaits);
        return this;
    }

    public Voiture addSouhaits(Souhait souhait) {
        this.souhaits.add(souhait);
        souhait.getVoitures().add(this);
        return this;
    }

    public Voiture removeSouhaits(Souhait souhait) {
        this.souhaits.remove(souhait);
        souhait.getVoitures().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Voiture)) {
            return false;
        }
        return id != null && id.equals(((Voiture) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Voiture{" +
            "id=" + getId() +
            ", model='" + getModel() + "'" +
            ", prix=" + getPrix() +
            ", image1='" + getImage1() + "'" +
            ", image2='" + getImage2() + "'" +
            ", image3='" + getImage3() + "'" +
            ", statut='" + getStatut() + "'" +
            ", version=" + getVersion() +
            ", miseEnVente='" + getMiseEnVente() + "'" +
            ", etat='" + getEtat() + "'" +
            ", porte=" + getPorte() +
            ", boiteVitesse='" + getBoiteVitesse() + "'" +
            ", co2=" + getCo2() +
            ", chevaux=" + getChevaux() +
            ", carburant='" + getCarburant() + "'" +
            ", annees='" + getAnnees() + "'" +
            ", ville='" + getVille() + "'" +
            ", codePostal=" + getCodePostal() +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
