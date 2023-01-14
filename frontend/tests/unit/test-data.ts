/**
 * This file includes test data for the tests
 */

import { StationType, InsertJourneyType } from "@/helpers/backend-data-types";

/**
 * Valid stations to be inserted. In total 4 stations.
 */
export const stations: StationType[] = [
  {
    id: 1,
    nameFi: "Ensimmäinen",
    nameSe: "Första",
    nameEn: "First",

    addressFi: "Ensimmäisenkatu 1",
    addressSe: "Förstagatan 1",

    cityFi: "Helsinki",
    citySe: "Helsingfors",

    operator: "First Bikes",
    capacity: 1,

    longitude: "24.937549",
    latitude: "60.156500",
  },

  {
    id: 2,
    nameFi: "Toinen",
    nameSe: "Sekunda",
    nameEn: "Second",

    addressFi: "Toisenkatu 2",
    addressSe: "Sekundagatan 2",

    cityFi: "Helsinki",
    citySe: "Helsingfors",

    operator: "Second Bikes",
    capacity: 2,

    longitude: "24.937549",
    latitude: "60.156500",
  } as StationType,

  {
    id: 3,
    nameFi: "Kolmas",
    nameSe: "Tres",
    nameEn: "Third",

    addressFi: "Kolmannenkatu 3",
    addressSe: "Tresgatan 3",

    cityFi: "Helsinki",
    citySe: "Helsingfors",

    operator: "First Bikes",
    capacity: 3,

    longitude: "24.937549",
    latitude: "60.156500",
  } as StationType,

  {
    id: 4,
    nameFi: "Neljäs",
    nameSe: "Quattro",
    nameEn: "Fourth",

    addressFi: "Neljäs katu 4",
    addressSe: "Quattrogatan 4",

    cityFi: "Helsinki",
    citySe: "Helsingfors",

    operator: "Second Bikes",
    capacity: 4,

    longitude: "24.937549",
    latitude: "60.156500",
  } as StationType,
];

/**
 * Stations which should be rejected by backend validation
 */
export const tooLongValueStation: StationType = {
  id: 5,
  nameFi: "Viides",
  nameSe: "Fem",
  nameEn: "Fifth",

  addressFi:
    "Liianpitkäkadunnimijokaeimilläänolealleviisikymmentämerkkiäpitkäjotenhylätään",
  addressSe: "Femgatan 5",

  cityFi: "Helsinki",
  citySe: "Helsingfors",

  operator: "Rejected Bikes",
  capacity: 20,

  longitude: "24.937549",
  latitude: "60.156500",
};

/**
 * Valid journeys to be inserted.
 *
 * In total 9 journeys, with 6 of the journeys starting at "Ensimmäinen."
 * "Ensimmäinen" -> "Ensimmäinen": 3 journeys
 * "Ensimmäinen" -> "Toinen", "Kolmas", "Neljäs": 1 each
 * "Toinen", "Kolmas", "Neljäs" -> "Ensimmäinen": 1 each
 *
 * Longest journey by distance is 150000 metres long.
 * Longest journey by duration is 2678400 seconds long.
 * Earliest journey starts at "2021-05-01T13:00:00" from "Ensimmäinen".
 * Latest journey starts at "2021-06-01T12:00:00" from "Ensimmäinen".
 *
 * Average departure distance from "Ensimmäinen" is 25133.3
 * Average return duration from "Ensimmäinen" is 448602.5
 *
 * Average return distance from "Ensimmäinen" is 25083.3
 * Average return duration from "Ensimmäinen" is 453800
 *
 * Top departure destinations from "Ensimmäinen":
 * "Ensimmäinen": 3
 * "Toinen", "Kolmas", "Neljäs": 1 each
 *
 * Top return destinations from "Ensimmäinen":
 * "Ensimmäinen": 3
 * "Toinen", "Kolmas", "Neljäs": 1 each
 */
export const journeys: InsertJourneyType[] = [
  // Journeys departing from "Ensimmäinen"
  {
    departureTime: "2021-05-01T12:00:00",
    returnTime: "2021-05-01T13:00:15",

    departureStationName: "Ensimmäinen",
    departureStationId: 1,

    returnStationName: "Toinen",
    returnStationId: 2,

    distanceCovered: 200,
    durationSeconds: 15,
  },

  {
    departureTime: "2021-05-01T13:00:00",
    returnTime: "2021-05-01T13:10:00",

    departureStationName: "Ensimmäinen",
    departureStationId: 1,

    returnStationName: "Kolmas",
    returnStationId: 3,

    distanceCovered: 200,
    durationSeconds: 600,
  },

  {
    departureTime: "2021-05-01T13:00:00",
    returnTime: "2021-05-01T13:10:00",

    departureStationName: "Ensimmäinen",
    departureStationId: 1,

    returnStationName: "Neljäs",
    returnStationId: 4,

    distanceCovered: 200,
    durationSeconds: 600,
  },

  // Journeys returning to Ensimmäinen

  {
    departureTime: "2021-05-01T13:00:00",
    returnTime: "2021-05-01T16:00:00",

    departureStationName: "Ensimmäinen",
    departureStationId: 1,

    returnStationName: "Ensimmäinen",
    returnStationId: 1,

    distanceCovered: 100,
    durationSeconds: 10800,
  },

  {
    departureTime: "2021-05-01T13:00:00",
    returnTime: "2021-05-01T16:00:00",

    departureStationName: "Toinen",
    departureStationId: 2,

    returnStationName: "Ensimmäinen",
    returnStationId: 1,

    distanceCovered: 100,
    durationSeconds: 10800,
  },

  {
    departureTime: "2021-05-01T13:00:00",
    returnTime: "2021-05-01T16:00:00",

    departureStationName: "Kolmas",
    departureStationId: 3,

    returnStationName: "Ensimmäinen",
    returnStationId: 1,

    distanceCovered: 100,
    durationSeconds: 10800,
  },

  {
    departureTime: "2021-05-01T13:00:00",
    returnTime: "2021-05-01T16:00:00",

    departureStationName: "Neljäs",
    departureStationId: 4,

    returnStationName: "Ensimmäinen",
    returnStationId: 1,

    distanceCovered: 100,
    durationSeconds: 10800,
  },

  // Earliest & longest journey by duration
  {
    departureTime: "2021-05-01T13:00:00",
    returnTime: "2021-06-01T13:00:00",

    departureStationName: "Ensimmäinen",
    departureStationId: 1,

    returnStationName: "Ensimmäinen",
    returnStationId: 1,

    distanceCovered: 100,
    durationSeconds: 2678400,
  },

  // Latest & longest journey by distance
  {
    departureTime: "2021-06-01T12:00:00",
    returnTime: "2021-06-01T12:20:00",

    departureStationName: "Ensimmäinen",
    departureStationId: 1,

    returnStationName: "Ensimmäinen",
    returnStationId: 1,

    distanceCovered: 150000,
    durationSeconds: 1200,
  },
];

/**
 * Journeys to be rejected by backend validation
 */

// Journey departing from non-existing station
export const invalidDepartJourney = {
  departureTime: "2021-05-01T12:00:00",
  returnTime: "2021-05-01T13:00:15",

  departureStationName: "Viides",
  departureStationId: 5,

  returnStationName: "Ensimmäinen",
  returnStationId: 1,

  distanceCovered: 400,
  durationSeconds: 15,
};

// Journey returning to non-existing station
export const invalidReturnJourney = {
  departureTime: "2021-05-01T12:00:00",
  returnTime: "2021-05-01T13:00:15",

  departureStationName: "Ensimmäinen",
  departureStationId: 1,

  returnStationName: "Viides",
  returnStationId: 5,

  distanceCovered: 400,
  durationSeconds: 15,
};

// Journey with distance under 10 meters
export const invalidDistanceJourney = {
  departureTime: "2021-05-01T12:00:00",
  returnTime: "2021-05-01T13:00:15",

  departureStationName: "Ensimmäinen",
  departureStationId: 1,

  returnStationName: "Ensimmäinen",
  returnStationId: 1,

  distanceCovered: 9,
  durationSeconds: 15,
};

// Journey with duration under 10 seconds
export const invalidDurationJourney = {
  departureTime: "2021-05-01T12:00:00",
  returnTime: "2021-05-01T13:00:15",

  departureStationName: "Ensimmäinen",
  departureStationId: 1,

  returnStationName: "Ensimmäinen",
  returnStationId: 1,

  distanceCovered: 1500,
  durationSeconds: 9,
};
