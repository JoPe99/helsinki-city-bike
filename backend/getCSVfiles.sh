#!/bin/bash

# This script loads all of the CSV files required for the application
# into the src/main/resources/ directory.
#
# Don't change the names or locations of the downloaded files.


curl -o ./src/main/resources/data/stations.csv https://opendata.arcgis.com/datasets/726277c507ef4914b0aec3cbcfcbfafc_0.csv
#curl -o ./src/main/resources/data/05_2021.csv https://dev.hsl.fi/citybikes/od-trips-2021/2021-05.csv
#curl -o ./src/main/resources/data/06_2021.csv https://dev.hsl.fi/citybikes/od-trips-2021/2021-06.csv
#curl -o ./src/main/resources/data/07_2021.csv https://dev.hsl.fi/citybikes/od-trips-2021/2021-07.csv