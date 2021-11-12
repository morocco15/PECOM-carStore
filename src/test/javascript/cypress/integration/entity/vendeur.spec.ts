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

describe('Vendeur e2e test', () => {
  const vendeurPageUrl = '/vendeur';
  const vendeurPageUrlPattern = new RegExp('/vendeur(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const vendeurSample = {};

  let vendeur: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/vendeurs+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/vendeurs').as('postEntityRequest');
    cy.intercept('DELETE', '/api/vendeurs/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (vendeur) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/vendeurs/${vendeur.id}`,
      }).then(() => {
        vendeur = undefined;
      });
    }
  });

  it('Vendeurs menu should load Vendeurs page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('vendeur');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Vendeur').should('exist');
    cy.url().should('match', vendeurPageUrlPattern);
  });

  describe('Vendeur page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(vendeurPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Vendeur page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/vendeur/new$'));
        cy.getEntityCreateUpdateHeading('Vendeur');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', vendeurPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/vendeurs',
          body: vendeurSample,
        }).then(({ body }) => {
          vendeur = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/vendeurs+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [vendeur],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(vendeurPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Vendeur page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('vendeur');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', vendeurPageUrlPattern);
      });

      it('edit button click should load edit Vendeur page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Vendeur');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', vendeurPageUrlPattern);
      });

      it('last delete button click should delete instance of Vendeur', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('vendeur').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', vendeurPageUrlPattern);

        vendeur = undefined;
      });
    });
  });

  describe('new Vendeur page', () => {
    beforeEach(() => {
      cy.visit(`${vendeurPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('Vendeur');
    });

    it('should create an instance of Vendeur', () => {
      cy.get(`[data-cy="login"]`).type('Technicien').should('have.value', 'Technicien');

      cy.get(`[data-cy="coordonnee"]`).type('Bretagne Nam navigate').should('have.value', 'Bretagne Nam navigate');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        vendeur = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', vendeurPageUrlPattern);
    });
  });
});
