import { assert } from "chai";
import * as api from "@/helpers/api-functions";
import { stations, destroyTables, createTables } from "./test-helpers";

describe("API station inserts", function () {
  // Insert stations before tests
  before(async function () {
    await destroyTables();
    await createTables();

    let response;
    for (const station of stations) {
      response = await api.insertStation(station);
      assert.equal(response.status, 200);
    }
  });

  describe("Station inserts", function () {
    it("Stations inserted", async function () {
      api.getAllStations().then((response) => {
        assert.equal(response.status, 200);
        assert.equal(response.data, stations);
      });
    });

    it("Station with taken ID rejected with 400", async function () {
      api.insertStation(stations[0]).then((response) => {
        assert.equal(response.status, 400);
      });
    });
  });
});
