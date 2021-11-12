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

describe('Utilisateur e2e test', () => {
  const utilisateurPageUrl = '/utilisateur';
  const utilisateurPageUrlPattern = new RegExp('/utilisateur(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const utilisateurSample = {};

  let utilisateur: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/utilisateurs+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/utilisateurs').as('postEntityRequest');
    cy.intercept('DELETE', '/api/utilisateurs/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (utilisateur) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/utilisateurs/${utilisateur.id}`,
      }).then(() => {
        utilisateur = undefined;
      });
    }
  });

  it('Utilisateurs menu should load Utilisateurs page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('utilisateur');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Utilisateur').should('exist');
    cy.url().should('match', utilisateurPageUrlPattern);
  });

  describe('Utilisateur page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(utilisateurPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Utilisateur page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/utilisateur/new$'));
        cy.getEntityCreateUpdateHeading('Utilisateur');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', utilisateurPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/utilisateurs',
          body: utilisateurSample,
        }).then(({ body }) => {
          utilisateur = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/utilisateurs+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [utilisateur],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(utilisateurPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Utilisateur page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('utilisateur');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', utilisateurPageUrlPattern);
      });

      it('edit button click should load edit Utilisateur page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Utilisateur');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', utilisateurPageUrlPattern);
      });

      it('last delete button click should delete instance of Utilisateur', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('utilisateur').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', utilisateurPageUrlPattern);

        utilisateur = undefined;
      });
    });
  });

  describe('new Utilisateur page', () => {
    beforeEach(() => {
      cy.visit(`${utilisateurPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('Utilisateur');
    });

    it('should create an instance of Utilisateur', () => {
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        utilisateur = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', utilisateurPageUrlPattern);
    });
  });
});
