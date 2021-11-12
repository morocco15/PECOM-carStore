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

describe('CarteBancaire e2e test', () => {
  const carteBancairePageUrl = '/carte-bancaire';
  const carteBancairePageUrlPattern = new RegExp('/carte-bancaire(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const carteBancaireSample = {};

  let carteBancaire: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/carte-bancaires+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/carte-bancaires').as('postEntityRequest');
    cy.intercept('DELETE', '/api/carte-bancaires/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (carteBancaire) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/carte-bancaires/${carteBancaire.id}`,
      }).then(() => {
        carteBancaire = undefined;
      });
    }
  });

  it('CarteBancaires menu should load CarteBancaires page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('carte-bancaire');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CarteBancaire').should('exist');
    cy.url().should('match', carteBancairePageUrlPattern);
  });

  describe('CarteBancaire page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(carteBancairePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CarteBancaire page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/carte-bancaire/new$'));
        cy.getEntityCreateUpdateHeading('CarteBancaire');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', carteBancairePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/carte-bancaires',
          body: carteBancaireSample,
        }).then(({ body }) => {
          carteBancaire = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/carte-bancaires+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [carteBancaire],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(carteBancairePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details CarteBancaire page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('carteBancaire');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', carteBancairePageUrlPattern);
      });

      it('edit button click should load edit CarteBancaire page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CarteBancaire');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', carteBancairePageUrlPattern);
      });

      it('last delete button click should delete instance of CarteBancaire', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('carteBancaire').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', carteBancairePageUrlPattern);

        carteBancaire = undefined;
      });
    });
  });

  describe('new CarteBancaire page', () => {
    beforeEach(() => {
      cy.visit(`${carteBancairePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('CarteBancaire');
    });

    it('should create an instance of CarteBancaire', () => {
      cy.get(`[data-cy="code"]`).type('7975').should('have.value', '7975');

      cy.get(`[data-cy="expiration"]`).type('2021-11-12T03:14').should('have.value', '2021-11-12T03:14');

      cy.get(`[data-cy="prenom"]`).type('Franche-Comté digital').should('have.value', 'Franche-Comté digital');

      cy.get(`[data-cy="nom"]`).type('Grocery c haptic').should('have.value', 'Grocery c haptic');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        carteBancaire = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', carteBancairePageUrlPattern);
    });
  });
});
