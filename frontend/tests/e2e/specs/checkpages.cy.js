
describe("Journeys are visible", () => {
  it("Visits the app root url", () => {
    cy.viewport(1920, 1080);
    cy.visit("http://localhost:8080/");

    // Wait for the store to load
    cy.wait(2000);
    cy.contains("Helsinki City Bike");

    // Check journeys page
    cy.get(".v-toolbar__content > :nth-child(3)").click();

    cy.contains("Start date");
    cy.contains("End date");
    cy.contains("Distance filter");
    cy.contains("Duration filter");
    
    // Map is available
    cy.get('.vue2leaflet-map');

    // Check that list is ordered correctly and the first item is starting at 12:00:00
    cy.get(':nth-child(1) > :nth-child(1) > .v-list-item__subtitle').contains("12:00:00");

    
    // Check stations page
    cy.get(".v-toolbar__content > :nth-child(4)").click();

    // Check that list is ordered correctly
    cy.get('tbody > :nth-child(1) > :nth-child(1)').contains("1");
    cy.get('tbody > :nth-child(2) > :nth-child(1)').contains("2");

    // Map is available
    cy.get('.vue2leaflet-map');

    // Check insert page
    cy.get(".v-toolbar__content > :nth-child(5)").click();

    cy.contains("Insert journey");

    cy.contains("Insert station");

  });
});

