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
 * Get trips from the backend. Takes page size, current offset, and sortBy
 * as parameters.
 *
 * Example:
 *  localhost:8081/trips?pageSize=5&offset=0&sortBy="departureTime"
 *
 * @param pageSize
 * @param offset
 * @param sortBy
 */
export function getJourneys(pageSize: number, offset: number, sortBy: string) {
  const url = `${SERVER_IP}/trips?pageSize=${pageSize}&offset=${offset}&sortBy=${sortBy}`;
  return axios.get<JourneyType[]>(url);
}

/**
 * Get all stations from the backend.
 */
export function getAllStations() {
  const url = `${SERVER_IP}/stations/all`;
  return axios.get<SingleStationType[]>(url);
}
