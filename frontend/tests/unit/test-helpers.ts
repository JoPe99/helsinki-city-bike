/**
 * This file includes helpers for tests to reduce clutter inside those files.
 */

import axios from "axios";

// Table clear
export function destroyTables() {
  return axios.delete("http://localhost:8081/delete");
}

// Table create
export function createTables() {
  return axios.post("http://localhost:8081/create");
}

export const topDeparturesForEnsimmainen = [
  { name: "Ensimmäinen", count: 3 },
  { name: "Toinen", count: 1 },
  { name: "Kolmas", count: 1 },
  { name: "Neljäs", count: 1 },
];

export const topReturnsForEnsimmainen = [
  { name: "Ensimmäinen", count: 3 },
  { name: "Toinen", count: 1 },
  { name: "Kolmas", count: 1 },
  { name: "Neljäs", count: 1 },
];
