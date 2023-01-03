/**
 *  This file includes functions for interacting with the API.
 */

// Axios used for API interactions
import axios from "axios";

import { JourneyType, SingleStationType } from "./backend-data-types";

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
 * @returns JourneyType[]
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
  return axios.get<JourneyType[]>(url, {
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
 * Returns all stations from the backend.
 * @returns SingleStationType[]
 */
export function getAllStations() {
  const url = `${SERVER_IP}/stations/all`;
  return axios.get<SingleStationType[]>(url);
}

/**
 * Returns journey with longest duration from the backend.
 * @returns JourneyType[]
 */
export function getLongestDuration() {
  const url = `${SERVER_IP}/journeys/longestDuration`;
  return axios.get<JourneyType[]>(url);
}

/**
 * Returns journey with longest distance from the backend.
 * @returns JourneyType[]
 */
export function getLongestDistance() {
  const url = `${SERVER_IP}/journeys/longestDistance`;
  return axios.get<JourneyType[]>(url);
}

/**
 * Returns earliest journey by departure time
 * @returns JourneyType[]
 */
export function getEarliestJourney() {
  const url = `${SERVER_IP}/journeys/earliest`;
  return axios.get<JourneyType[]>(url);
}

/**
 * Returns latest journey by departure time
 * @returns JourneyType[]
 */
export function getLatestJourney() {
  const url = `${SERVER_IP}/journeys/latest`;
  return axios.get<JourneyType[]>(url);
}
