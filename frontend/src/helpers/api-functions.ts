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
 * Get journeys from the backend. Takes page size, current offset, and sortBy
 * as parameters.
 *
 * Example:
 *  localhost:8081/journeys?pageSize=5&offset=0&sortBy="departureTime"
 *
 * @param pageSize
 * @param offset
 * @param sortBy
 * @returns JourneyType[]
 */
export function getJourneys(pageSize: number, offset: number, sortBy: string) {
  const url = `${SERVER_IP}/journeys?pageSize=${pageSize}&offset=${offset}&sortBy=${sortBy}`;
  return axios.get<JourneyType[]>(url);
}

/**
 * Returns total number of journeys stored in the server.
 *
 * Example:
 * localhost:8081/journeys/count
 *
 * @returns number
 */
export function getJourneysCount() {
  const url = `${SERVER_IP}/journeys/count`;
  return axios.get<number>(url);
}

/**
 * Get all stations from the backend.
 * @returns SingleStationType[]
 */
export function getAllStations() {
  const url = `${SERVER_IP}/stations/all`;
  return axios.get<SingleStationType[]>(url);
}
