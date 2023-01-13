/**
 * This file includes helpers for tests to reduce clutter inside those files.
 */

import { StationType } from "@/helpers/backend-data-types";
import axios from "axios";

// Table clear
export function destroyTables() {
  return axios.delete("http://localhost:8081/delete");
}

// Table
export function createTables() {
  return axios.post("http://localhost:8081/create");
}

// api-functions.spec.ts
// Insertable stations
export const stations: StationType[] = [
  // First station
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

  // Seconds station
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

  // Third station
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

  // Fourth station
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
