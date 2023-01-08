/**
 *  This file includes functions for interacting with the API.
 */

// Axios used for API interactions
import axios from "axios";

import {
  StationType,
  SingleStationType,
  JourneyAPIResult,
  InsertJourneyType,
} from "./backend-data-types";

// If you want to use a self-hosted backend for testing,
// you can change this to "localhost:8081"
// TODO: Move to root dir?
const SERVER_IP = "http://localhost:8081";

/**
 * Get journeys from the backend. Takes page size, current offset,
 * sortBy, SortDesc, and search, start/end date, min/max duration/distance
 * as parameters.
 *
 * Example:
 *  localhost:8081/journeys?pageSize=5&offset=0&sortBy=departureTime \
 *  &sortDesc=true&search=Arabia&minDistance=1300&maxDistance=6000 \
 *  &minDuration=400&maxDuration=900
 *
 * @param pageSize
 * @param offset
 * @param sortBy
 * @param sortDesc
 * @param search
 * @param startDate
 * @param endDate
 * @param minDistance
 * @param maxDistance
 * @param minDuration
 * @param maxDuration
 * @returns JourneyAPIResult
 */
export function getJourneys(
  pageSize: number,
  offset: number,
  sortBy: string,
  sortDesc: boolean,
  search: string,
  startDate: string,
  endDate: string,
  minDistance: number,
  maxDistance: number,
  minDuration: number,
  maxDuration: number
) {
  const url = `${SERVER_IP}/journeys`;
  return axios.get<JourneyAPIResult>(url, {
    params: {
      pageSize: pageSize,
      offset: offset,
      sortBy: sortBy,
      sortDesc: sortDesc,
      search: search,
      startDate: startDate,
      endDate: endDate,
      minDistance: minDistance,
      maxDistance: maxDistance,
      minDuration: minDuration,
      maxDuration: maxDuration,
    },
  });
}

/**
 * Returns total number of journeys stored in the server.
 *
 * @returns number
 */
export function getJourneysCount() {
  const url = `${SERVER_IP}/journeys/count`;
  return axios.get<number>(url);
}

/**
 * Returns all stations.
 * @returns StationType[]
 */
export function getAllStations() {
  const url = `${SERVER_IP}/stations/all`;
  return axios.get<StationType[]>(url);
}

/**
 * Returns single station with details.
 * @returns SingleStationType
 */
export function getSingleStation(
  stationId: number,
  startDate: string | null,
  endDate: string | null
) {
  const url = `${SERVER_IP}/stations/${stationId}`;

  return axios.get<SingleStationType>(url, {
    params: {
      startDate: startDate,
      endDate: endDate,
    },
  });
}

/**
 * Returns journey with longest duration from the backend.
 * @returns JourneyType[]
 */
export function getLongestDuration() {
  const url = `${SERVER_IP}/journeys/longestDuration`;
  return axios.get<JourneyAPIResult>(url);
}

/**
 * Returns journey with longest distance from the backend.
 * @returns JourneyType[]
 */
export function getLongestDistance() {
  const url = `${SERVER_IP}/journeys/longestDistance`;
  return axios.get<JourneyAPIResult>(url);
}

/**
 * Returns earliest journey by departure time
 * @returns JourneyType[]
 */
export function getEarliestJourney() {
  const url = `${SERVER_IP}/journeys/earliest`;
  return axios.get<JourneyAPIResult>(url);
}

/**
 * Returns latest journey by departure time
 * @returns JourneyType[]
 */
export function getLatestJourney() {
  const url = `${SERVER_IP}/journeys/latest`;
  return axios.get<JourneyAPIResult>(url);
}

/**
 * Inserts a station into backend database.
 * @param station
 */
export function insertStation(station: StationType) {
  console.log("inserting station", station);
  const url = `${SERVER_IP}/insert/station`;
  return axios.post<StationType>(url, station);
}

/**
 * Inserts journey into backend database.
 * @param journey InsertJourneyType
 */
export function insertJourney(journey: InsertJourneyType) {
  console.log("inserting journey", journey);
  const url = `${SERVER_IP}/insert/journey`;
  return axios.post<InsertJourneyType>(url, journey);
}
