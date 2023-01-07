/**
 *  This module includes the data types coming from the server.
 */

import { formattedDateTime } from "./list-view-helpers";

/**
 * Journeys come in this format from the server.
 */
export type JourneyAPIResult = {
  length: number;
  journeys: JourneyType[];
};

/**
 * Journeys come from backend with departure and return
 * station coordinates. JourneyType is then formatted into
 * FormattedJourneyType.
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
 * This type is used for inserting into the backend database.
 */
export type InsertJourneyType = {
  departureTime: string;
  returnTime: string;

  departureStationName: string;
  departureStationId: number;

  returnStationName: string;
  returnStationId: number;

  distanceCovered: number;
  durationSeconds: number;
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
 * Station details, used combined with StationType
 */
export type StationDetails = {
  totalDepartJourneys: number;
  totalReturnJourneys: number;

  averageDepartDistance: number;
  averageReturnDistance: number;

  averageDepartDuration: number;
  averageReturnDuration: number;

  topDepartStations: Array<{ name: string; count: number }>;
  topReturnStations: Array<{ name: string; count: number }>;
};

/**
 * Single station data is a combination of types
 * StationType & StationDetails
 */
export type SingleStationType = StationType & StationDetails;
