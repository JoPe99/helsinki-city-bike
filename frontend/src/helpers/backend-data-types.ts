/**
 *  This module includes the data types coming from the server.
 */

import { formattedDateTime } from "./list-view-helpers";

/**
 * Journeys come in this format from the server.
 */
export type JourneyType = {
  id: number;

  departureTime: string;
  returnTime: string;

  departureStationName: string;
  departureStationId: number;
  departureStationX: number;
  departureStationY: number;

  returnStationName: string;
  returnStationId: number;
  returnStationX: number;
  returnStationY: number;

  distanceCovered: number;
  durationSeconds: number;
};

/**
 * This app formats the journey types from server to this type,
 * which includes separate dates and times, and formatted
 * distance to kilometers and time to minutes.
 */

export type FormattedJourneyType = {
  id: number;

  departureDateTime: formattedDateTime;
  returnDateTime: formattedDateTime;

  departureStationName: string;
  departureStationId: number;
  departureStationX: number;
  departureStationY: number;

  returnStationName: string;
  returnStationId: number;
  returnStationX: number;
  returnStationY: number;

  distance: string;
  duration: string;
};

/**
 * Stations come in this format from the server.
 */
export type StationType = {
  id: number;

  nameFi: string;
  nameSe: string;
  nameEn: string;

  addressFi: string;
  addressSe: string;

  cityFi: string;
  citySe: string;

  operator: string;
  capacity: number;

  longitude: string;
  latitude: string;
};

/**
 * Stations come in this format from the server.
 */
export type StationDetails = {
  totalDepartJourneys: number;
  totalReturnJourneys: number;

  averageDepartDistance: number;
  averageReturnDistance: number;

  topDepartStations: Array<Map<string, number>>;
  topReturnStations: Array<Map<string, number>>;
};

/**
 * Single station data is a combination of types
 * StationType & StationDetails
 */
export type SingleStationType = StationType & StationDetails;
