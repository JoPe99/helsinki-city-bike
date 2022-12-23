#!/bin/bash

# This script loads all of the CSV files required for the application
# into the src/main/resources/ directory.
#
# Don't change the names or locations of the downloaded files.


curl -o ./src/main/resources/test.csv https://www.stats.govt.nz/assets/Uploads/Annual-enterprise-survey/Annual-enterprise-survey-2021-financial-year-provisional/Download-data/annual-enterprise-survey-2021-financial-year-provisional-size-bands-csv.csv
curl -o ./src/main/resources/test1.csv https://www.stats.govt.nz/assets/Uploads/Annual-enterprise-survey/Annual-enterprise-survey-2021-financial-year-provisional/Download-data/annual-enterprise-survey-2021-financial-year-provisional-size-bands-csv.csv