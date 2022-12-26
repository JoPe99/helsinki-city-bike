/**
 *  This module includes the data types coming from the server.
 *
 */

/**
 * Journeys come in this format from the server.
 */
export type JourneyType = {
  departureTime: string;
  departureStationName: string;
  departureStationId: number;
  returnTime: string;
  returnStationName: string;
  returnStationId: number;
  distanceCovered: number;
  durationSeconds: number;
};
