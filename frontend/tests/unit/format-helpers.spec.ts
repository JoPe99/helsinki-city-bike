import { assert } from "chai";
import {
  formatDistance,
  formatSeconds,
  pageToOffset,
  timestampToDate,
} from "@/helpers/format-helpers";

describe("Format helpers", function () {
  describe("formatDistance", function () {
    it("Formats 0 to 0m", function () {
      const result = formatDistance(0);
      assert.equal(result, "0m");
    });

    it("Formats negative number to 0m", function () {
      const result = formatDistance(-50);
      assert.equal(result, "0m");
    });

    it("Formats distances under kilometer", function () {
      const result = formatDistance(50);
      assert.equal(result, "50m");
    });

    it("Formats distances over a kilometer but under ten", function () {
      const result = formatDistance(2500);
      assert.equal(result, "2.50km");
    });

    it("Formats distance rounding up to 2.50km", function () {
      const result = formatDistance(2496);
      assert.equal(result, "2.50km");
    });

    it("Formats distance rounding up to 10.0km", function () {
      const result = formatDistance(9999);
      assert.equal(result, "10.0km");
    });

    it("Formats distance rounding down to 10.0km", function () {
      const result = formatDistance(10049);
      assert.equal(result, "10.0km");
    });

    it("Formats distance over 10km", function () {
      const result = formatDistance(14342);
      assert.equal(result, "14.3km");
    });

    it("Formats distance over 100km", function () {
      const result = formatDistance(150000);
      assert.equal(result, "150.0km");
    });
  });
  describe("formatSeconds", function () {
    it("Formats zero correctly", function () {
      const result = formatSeconds(0);
      assert.equal(result, "0s");
    });

    it("Formats negative number to 0s", function () {
      const result = formatSeconds(0);
      assert.equal(result, "0s");
    });

    it("Formats time under a minute", function () {
      const result = formatSeconds(50);
      assert.equal(result, "50s");
    });

    it("Formats time over a minute", function () {
      const result = formatSeconds(948);
      assert.equal(result, "15min 48s");
    });

    it("Formats time over an hour", function () {
      const result = formatSeconds(7524);
      assert.equal(result, "2h 5min");
    });

    it("Formats time rounding up to day", function () {
      const result = formatSeconds(86399);
      assert.equal(result, "1d 0h");
    });

    it("Formats time over a day", function () {
      const result = formatSeconds(93600);
      assert.equal(result, "1d 2h");
    });
  });
});
