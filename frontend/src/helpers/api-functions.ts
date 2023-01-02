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
 * sortBy, SortDesc, and search as parameters.
 * as parameters.
 *
 * Example:
 *  localhost:8081/journeys?pageSize=5&offset=0&sortBy="departureTime"
 *
 * @param pageSize
 * @param offset
 * @param sortBy
 * @param sortDesc
 * @param search
 * @returns JourneyType[]
 */
export function getJourneys(
  pageSize: number,
  offset: number,
  sortBy: string,
  sortDesc: boolean,
  search: string
) {
  const url = `${SERVER_IP}/journeys`;
  return axios.get<JourneyType[]>(url, {
    params: {
      pageSize: pageSize,
      offset: offset,
      sortBy: sortBy,
      sortDesc: sortDesc,
      search: search,
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
