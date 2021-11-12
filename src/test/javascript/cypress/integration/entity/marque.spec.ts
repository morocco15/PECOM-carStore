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

describe('Marque e2e test', () => {
  const marquePageUrl = '/marque';
  const marquePageUrlPattern = new RegExp('/marque(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const marqueSample = {};

  let marque: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/marques+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/marques').as('postEntityRequest');
    cy.intercept('DELETE', '/api/marques/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (marque) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/marques/${marque.id}`,
      }).then(() => {
        marque = undefined;
      });
    }
  });

  it('Marques menu should load Marques page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('marque');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Marque').should('exist');
    cy.url().should('match', marquePageUrlPattern);
  });

  describe('Marque page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(marquePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Marque page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/marque/new$'));
        cy.getEntityCreateUpdateHeading('Marque');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', marquePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/marques',
          body: marqueSample,
        }).then(({ body }) => {
          marque = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/marques+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [marque],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(marquePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Marque page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('marque');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', marquePageUrlPattern);
      });

      it('edit button click should load edit Marque page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Marque');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', marquePageUrlPattern);
      });

      it('last delete button click should delete instance of Marque', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('marque').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', marquePageUrlPattern);

        marque = undefined;
      });
    });
  });

  describe('new Marque page', () => {
    beforeEach(() => {
      cy.visit(`${marquePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('Marque');
    });

    it('should create an instance of Marque', () => {
      cy.get(`[data-cy="marque"]`).type('Profit-focused withdrawal').should('have.value', 'Profit-focused withdrawal');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        marque = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', marquePageUrlPattern);
    });
  });
});
