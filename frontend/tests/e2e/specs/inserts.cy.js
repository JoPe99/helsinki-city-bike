// https://docs.cypress.io/api/table-of-contents

describe("Journeys are visible", () => {
  it("Visits the app root url", () => {
    cy.viewport(1920, 1080);
    cy.visit("http://localhost:8080/");
    cy.wait(2000);
    cy.contains("header", "Helsinki City Bike");
    cy.get(".v-toolbar__content > :nth-child(3)").click();
  });
});
