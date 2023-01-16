import "datejs";
import { FormattedJourneyType, JourneyType } from "../types/backend-data-types";

/**
 * Returns an offset for working with database. Vuetify table uses
 * page number and size for pagination, and backend needs a number of where
 * to start.
 *
 * Examples:
 *
 * First page with page size of 25:
 * page = 0, pageSize = 25
 * returns 0
 *
 * Second page with same size:
 * page = 1, pageSize = 25
 * return 25
 *
 * Fifth page with page size of 50:
 * page = 5, pageSize = 50
 * returns 200
 *
 * @param page
 * @param pageSize
 * @returns offset as number
 */
export function pageToOffset(page: number, pageSize: number) {
  if (page == 0) {
    return 0;
  } else {
    return (page - 1) * pageSize;
  }
}

/**
 * Vuetify data table puts sort parameters in an array.
 * This function returns the string in the array.
 *
 * @param sortBy
 * @returns string
 */
export function sortByArrayToString(sortBy: string[]) {
  return sortBy[0];
}

/**
 * Vuetify data table puts sort order in an array.
 * This function returns the boolean in the array.
 *
 * @param sortDesc
 * @returns string
 */
export function sortDescArrayToString(sortDesc: boolean[]) {
  return sortDesc[0];
}

/**
 * Formats a backend raw data journey into more readable
 * format to display in backend. Takes an array of
 * JourneyType as parameter, and for each one creates a
 * formatted version of the type in an array.
 * Changes made are backend time strings to a FormattedDateTime,
 * and distance and duration are formatted by formatDistance
 * and formatSeconds.
 *
 * @param JourneyType[]
 * @returns FormattedJourneyType[]
 */
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
 * Example:
 *
 * Timestamp: "2021-05-30T20:44:37"
 * Returns a FormattedDateTime of
 * {year: 2021,
 * month: 5,
 * day: 30,
 * hours: 20,
 * minutes: 44,
 * seconds: 37}
 *
 * @param timestamp
 * @returns formattedDateTime
 */
export function timestampToDate(timestamp: string) {
  const date = Date.parse(timestamp);

  const formattedDateTime = {} as FormattedDateTime;

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

  if (meters < 0) {
    return "0m";
  }

  if (meters >= 10000) {
    ret = `${(meters / 1000).toFixed(1)}km`;
  }

  if (meters < 10000 && meters >= 1000) {
    ret = `${(meters / 1000).toFixed(2)}km`;
    // Format in line with over 10km formatting if
    // rounded to 10km
    if (ret == "10.00km") {
      ret = "10.0km";
    }
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
 *  93600 is formatted to 1d 2h
 *  86399 is formatted to 1d 0h
 *  7524 is formatted to 2h 5min
 *  948 is formatted to 15min 48s
 *  50 is formatted to 50s
 *
 * @param seconds
 * @returns string
 */
export function formatSeconds(seconds: number) {
  let ret = "";

  if (seconds < 0) {
    return "0s";
  }

  // Day or over
  if (seconds >= 86400) {
    let days = seconds / 86400;
    let hours = (seconds % 86400) / 3600;

    // If hour amount rounds to 24 hours, hours to 0
    // and extra day to days.
    if (Number(hours.toFixed()) == 24) {
      days++;
      hours = 0;
    }
    ret = `${Math.floor(days)}d ${hours.toFixed()}h`;
  }

  // Over an hour
  if (seconds < 86400) {
    let hours = seconds / 3600;
    let minutes = (seconds % 3600) / 60;

    // If rounds to 60, then add one to hours,
    // and reset minutes.
    if (minutes.toFixed() == "60") {
      hours++;
      minutes = 0;
      if (hours >= 24) {
        return "1d 0h";
      }
    }

    ret = `${Math.floor(hours)}h ${minutes.toFixed()}min`;
  }

  // Under an hour but over or exactly minute
  if (seconds < 3600 && seconds >= 60) {
    const minutes = seconds / 60;
    const sec = seconds % 60;
    ret = `${Math.floor(minutes)}min ${sec.toFixed()}s`;
  }

  // Under a minute
  if (seconds < 60) {
    ret = `${seconds}s`;
  }

  return ret;
}

export type FormattedDateTime = {
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
