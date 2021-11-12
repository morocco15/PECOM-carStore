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

describe('Souhait e2e test', () => {
  const souhaitPageUrl = '/souhait';
  const souhaitPageUrlPattern = new RegExp('/souhait(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const souhaitSample = {};

  let souhait: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/souhaits+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/souhaits').as('postEntityRequest');
    cy.intercept('DELETE', '/api/souhaits/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (souhait) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/souhaits/${souhait.id}`,
      }).then(() => {
        souhait = undefined;
      });
    }
  });

  it('Souhaits menu should load Souhaits page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('souhait');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Souhait').should('exist');
    cy.url().should('match', souhaitPageUrlPattern);
  });

  describe('Souhait page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(souhaitPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Souhait page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/souhait/new$'));
        cy.getEntityCreateUpdateHeading('Souhait');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', souhaitPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/souhaits',
          body: souhaitSample,
        }).then(({ body }) => {
          souhait = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/souhaits+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [souhait],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(souhaitPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Souhait page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('souhait');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', souhaitPageUrlPattern);
      });

      it('edit button click should load edit Souhait page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Souhait');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', souhaitPageUrlPattern);
      });

      it('last delete button click should delete instance of Souhait', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('souhait').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', souhaitPageUrlPattern);

        souhait = undefined;
      });
    });
  });

  describe('new Souhait page', () => {
    beforeEach(() => {
      cy.visit(`${souhaitPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('Souhait');
    });

    it('should create an instance of Souhait', () => {
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        souhait = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', souhaitPageUrlPattern);
    });
  });
});
