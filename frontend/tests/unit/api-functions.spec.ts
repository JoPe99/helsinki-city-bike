import { assert } from "chai";
import * as api from "@/helpers/api-functions";
import { destroyTables, createTables } from "./test-helpers";
import {
  stations,
  journeys,
  tooLongValueStation,
  invalidDepartJourney,
  invalidReturnJourney,
  invalidDistanceJourney,
  invalidDurationJourney,
} from "./test-data";
import { JourneyAPIResult, JourneyType } from "@/helpers/backend-data-types";

// Populate database before doing tests
before(async function () {
  // Clean up database before doing tests
  await destroyTables();
  await createTables();

  // Insert stations stations and journeys before tests
  let response;

  for (const station of stations) {
    response = await api.insertStation(station);
  }

  for (const journey of journeys) {
    response = await api.insertJourney(journey);
  }
});

describe("Stations from API", function () {
  it("Station with taken ID rejected with 400", async function () {
    api.insertStation(stations[0]).then((response) => {
      assert.equal(response.status, 400);
    });
  });

  it("Station with property value length over 50 chars rejected with 400", async function () {
    api.insertStation(stations[0]).then((response) => {
      assert.equal(response.status, 400);
    });
  });

  it("Correct amount of stations inserted", async function () {
    api.getAllStations().then((response) => {
      assert.equal(response.status, 200);
      assert.equal(response.data, stations);
    });
  });
});

describe("Journeys from API", function () {
  it("Rejects journey with invalid departure station", async function () {
    api.insertJourney(invalidDepartJourney).then((response) => {
      assert.equal(response.status, 400);
    });
  });

  it("Rejects journey with invalid return station", async function () {
    api.insertJourney(invalidReturnJourney).then((response) => {
      assert.equal(response.status, 400);
    });
  });

  it("Rejects journey with distance under 10 meters", async function () {
    api.insertJourney(invalidDistanceJourney).then((response) => {
      assert.equal(response.status, 400);
    });
  });

  it("Rejects journey with duration under 10 seconds", async function () {
    api.insertJourney(invalidDurationJourney).then((response) => {
      assert.equal(response.status, 400);
    });
  });

  it("Correct amount of stations inserted", async function () {
    api.getJourneysCount().then((response) => {
      assert.equal(response.status, 200);

      const journeyCount = response.data;
      assert.equal(9, journeyCount);
    });
  });

  it("Gets correct longest journey by distance", async function () {
    api.getLongestDistance().then((response) => {
      assert.equal(response.status, 200);

      const apiResult: JourneyAPIResult = response.data;
      assert.equal(1, apiResult.length);

      const journey: JourneyType = apiResult.journeys[0];

      assert.equal(150000, journey.distanceCovered);
    });
  });

  it("Gets correct longest journey by duration", async function () {
    api.getLongestDistance().then((response) => {
      assert.equal(response.status, 200);

      const apiResult: JourneyAPIResult = response.data;
      assert.equal(1, apiResult.length);

      const journey: JourneyType = apiResult.journeys[0];

      assert.equal(2678400, journey.durationSeconds);
    });
  });

  it("Gets correct earliest journey", async function () {
    api.getEarliestJourney().then((response) => {
      assert.equal(response.status, 200);

      const apiResult: JourneyAPIResult = response.data;
      assert.equal(1, apiResult.length);

      const journey: JourneyType = apiResult.journeys[0];

      assert.equal("2021-05-01T13:00:00", journey.departureTime);
    });
  });

  it("Gets correct latest journey", async function () {
    api.getLatestJourney().then((response) => {
      assert.equal(response.status, 200);

      const apiResult: JourneyAPIResult = response.data;
      assert.equal(1, apiResult.length);

      const journey: JourneyType = apiResult.journeys[0];

      assert.equal("2021-06-01T12:00:00", journey.departureTime);
    });
  });
});
