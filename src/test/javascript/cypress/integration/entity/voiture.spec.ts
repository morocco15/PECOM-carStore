import { entityItemSelector } from '../../support/commands';
import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('Voiture e2e test', () => {
  const voiturePageUrl = '/voiture';
  const voiturePageUrlPattern = new RegExp('/voiture(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const voitureSample = {};

  let voiture: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/voitures+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/voitures').as('postEntityRequest');
    cy.intercept('DELETE', '/api/voitures/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (voiture) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/voitures/${voiture.id}`,
      }).then(() => {
        voiture = undefined;
      });
    }
  });

  it('Voitures menu should load Voitures page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('voiture');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Voiture').should('exist');
    cy.url().should('match', voiturePageUrlPattern);
  });

  describe('Voiture page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(voiturePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Voiture page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/voiture/new$'));
        cy.getEntityCreateUpdateHeading('Voiture');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', voiturePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/voitures',
          body: voitureSample,
        }).then(({ body }) => {
          voiture = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/voitures+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [voiture],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(voiturePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Voiture page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('voiture');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', voiturePageUrlPattern);
      });

      it('edit button click should load edit Voiture page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Voiture');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', voiturePageUrlPattern);
      });

      it('last delete button click should delete instance of Voiture', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('voiture').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', voiturePageUrlPattern);

        voiture = undefined;
      });
    });
  });

  describe('new Voiture page', () => {
    beforeEach(() => {
      cy.visit(`${voiturePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('Voiture');
    });

    it('should create an instance of Voiture', () => {
      cy.get(`[data-cy="model"]`).type('Bourgogne').should('have.value', 'Bourgogne');

      cy.get(`[data-cy="prix"]`).type('81363').should('have.value', '81363');

      cy.setFieldImageAsBytesOfEntity('image1', 'integration-test.png', 'image/png');

      cy.setFieldImageAsBytesOfEntity('image2', 'integration-test.png', 'image/png');

      cy.setFieldImageAsBytesOfEntity('image3', 'integration-test.png', 'image/png');

      cy.get(`[data-cy="statut"]`).select('LIBRE');

      cy.get(`[data-cy="miseEnVente"]`).type('2021-11-12T06:01').should('have.value', '2021-11-12T06:01');

      cy.get(`[data-cy="etat"]`).select('ENDOMMAGE');

      cy.get(`[data-cy="porte"]`).type('0').should('have.value', '0');

      cy.get(`[data-cy="boiteVitesse"]`).type('8598').should('have.value', '8598');

      cy.get(`[data-cy="co2"]`).type('58390').should('have.value', '58390');

      cy.get(`[data-cy="chevaux"]`).type('3894').should('have.value', '3894');

      cy.get(`[data-cy="carburant"]`).select('ELECTRIQUE');

      // since cypress clicks submit too fast before the blob fields are validated
      cy.wait(200); // eslint-disable-line cypress/no-unnecessary-waiting
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        voiture = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', voiturePageUrlPattern);
    });
  });
});
