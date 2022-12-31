import "datejs";
import { FormattedJourneyType, JourneyType } from "./backend-data-types";

export function pageToOffset(page: number, pageSize: number) {
  if (page == 0) {
    return 0;
  } else {
    return (page - 1) * pageSize;
  }
}

export function sortByArrayToString(sortBy: string[]) {
  return sortBy[0];
}

export function sortDescArrayToString(sortDesc: boolean[]) {
  return sortDesc[0];
}

export function formatJourneyTypeArray(journeys: JourneyType[]) {
  const formattedJourneyArray: FormattedJourneyType[] = [];
  let currentJourney = {} as FormattedJourneyType;

  for (const journey of journeys) {
    currentJourney.id = journey.id;

    currentJourney.departureDateTime = timestampToDate(journey.departureTime);
    currentJourney.returnDateTime = timestampToDate(journey.returnTime);

    currentJourney.departureStationName = journey.departureStationName;
    currentJourney.departureStationId = journey.departureStationId;
    currentJourney.departureStationX = journey.departureStationX;
    currentJourney.departureStationY = journey.departureStationY;

    currentJourney.returnStationName = journey.returnStationName;
    currentJourney.returnStationId = journey.returnStationId;
    currentJourney.returnStationX = journey.returnStationX;
    currentJourney.returnStationY = journey.returnStationY;

    currentJourney.distance = formatDistance(journey.distanceCovered);
    currentJourney.duration = formatSeconds(journey.durationSeconds);

    // Add created type to array
    formattedJourneyArray.push(currentJourney);
    // Empty current journey for the next iteration
    currentJourney = {} as FormattedJourneyType;
  }

  return formattedJourneyArray;
}

/**
 * Formats the server timestamp to
 * formattedDateTime type.
 *
 * Takes "YYYY-MM-DD'T'HH:MM:SS" as timestamp
 *
 * @param timestamp
 * @returns formattedDateTime
 */
export function timestampToDate(timestamp: string) {
  const date = Date.parse(timestamp);

  const formattedDateTime = {} as formattedDateTime;

  formattedDateTime.year = date.getFullYear();
  formattedDateTime.month = date.getMonth() + 1;
  formattedDateTime.day = date.getDate();

  formattedDateTime.hours = date.getHours();
  formattedDateTime.minutes = date.getMinutes();
  formattedDateTime.seconds = date.getSeconds();

  return formattedDateTime;
}

/**
 * Formats the covered distance in meters to readable format as a string.
 *
 * Examples:
 *  14342 is formatted to "14.3km"
 *  1245 is formatted to "1.25km"
 *  540 is formatted to "540m"
 *
 * @param meters
 * @returns string
 */
export function formatDistance(meters: number) {
  let ret = "";

  if (meters >= 10000) {
    ret = `${(meters / 1000).toFixed(1)}km`;
  }

  if (meters < 10000 && meters >= 1000) {
    ret = `${(meters / 1000).toFixed(2)}km`;
  }

  if (meters < 1000) {
    ret = `${meters}m`;
  }

  return ret;
}

/**
 * Formats seconds to more readable format.
 *
 * Examples:
 *  86540 is formatted to 1d 2h
 *  7524 is formatted to 2h 5min
 *  948 is formatted to 15min 48s
 *  50 is formatted to 50s
 *
 * @param seconds
 * @returns string
 */
export function formatSeconds(seconds: number) {
  let ret = "";

  if (seconds >= 86400) {
    const days = seconds / 86400;
    const hours = (seconds % 86400) / 3600;
    ret = `${Math.floor(days)}d ${hours.toFixed()}h`;
  }

  if (seconds < 86400) {
    const hours = seconds / 3600;
    const minutes = (seconds % 3600) / 60;
    ret = `${Math.floor(hours)}h ${minutes.toFixed()}min`;
  }

  if (seconds < 3600 && seconds >= 60) {
    const minutes = seconds / 60;
    const sec = seconds % 60;
    ret = `${Math.floor(minutes)}min ${sec}s`;
  }

  if (seconds < 60) {
    ret = `${seconds}s`;
  }

  return ret;
}

export type formattedDateTime = {
  hours: number;
  minutes: number;
  seconds: number;

  year: number;
  month: number;
  day: number;
};

export type StationLocation = {
  id: number;
  name: string;
  latLong: number[];
};
