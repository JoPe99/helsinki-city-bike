<template>
  <v-container fluid>
    <v-card color="primary">
      <v-card-title class="pt-2 pb-0"
        >Features of this application</v-card-title
      >
      <v-container fluid>
        <v-row wrap>
          <v-col fill-width>
            <v-card style="height: 100%" color="secondary">
              <v-card-title>Journeys</v-card-title>
              <v-card-text class="text-body-1">
                Journeys can be browsed in the Journey tab. Journeys are
                paginated, sorted and filtered on the server.
                <p></p>

                Journeys can be filtered by distance, duration, search, time of
                departure and time of return. The sliders have value intervals,
                so you can get as close to the desired values as you want!
                Search is case-sensitive, and it searches departure and return
                station names. Start date is from the start of the day, and end
                time from the end of the chosen end date.

                <p></p>

                The table can be fairly quickly sorted by any value visible, so
                feel free to play around with it.

                <p></p>

                By clicking a row on the table, the journey departure and return
                stations will be shown on the map, connected by a line. In case
                the journey returns to the departure station, there will be only
                one marker on the map.
              </v-card-text>
            </v-card>
          </v-col>
          <v-col fill-width>
            <v-card style="height: 100%" color="secondary">
              <v-card-title>Stations</v-card-title>
              <v-card-text class="text-body-1">
                Stations can be browsed from the Stations-tab.
                <p></p>
                Stations are fetched from server on app startup. All stations
                are shown on the map when no specific station is selected.
                Station can be selected/unselected by clicking either on a
                marker on the map, or a row in the list.
                <p></p>

                Stations can be filtered only by search, but the search searches
                all visible values of the station. The table can be sorted by
                all of the values.
                <p></p>
                In the top corner of the station list, there is a button which
                opens a modal containing very interesting data about the
                selected station. That includes average distance and duration
                for both depart and return journeys, and top 5 stations for
                departures and returns. All of these values can be filtered by
                the date pickers in the modal.
              </v-card-text>
            </v-card>
          </v-col>
          <v-col fill-width>
            <v-card style="height: 100%" color="secondary">
              <v-card-title>Insert data</v-card-title>
              <v-card-text class="text-body-1">
                Last tab is for inserting new data into the backend database.
                There is a submit form for both journeys and stations, so you
                can create your own stations and use them for adding new
                journeys! The data is validated both in frontend form submission
                and in the backend before inserting into database.

                <p></p>
                There is even a fun little location picker for you to play
                around with when inserting stations. When choosing station ID,
                keep in mind that the already existing stations don't have
                consecutive IDs. To help with that, the form will automagically
                default to a first available ID. The maximum ID value is 1999,
                so there can only be 1999 stations.
                <p></p>
                Also good to note is that in the case that you are connected to
                the server in cloud, everyone can see the stations you insert.
                So please don't put your credit card number and your favorite
                security questions into the data fields.
              </v-card-text>
            </v-card>
          </v-col>
        </v-row>
      </v-container>
    </v-card>
  </v-container>
</template>

<script lang="ts">
import { defineComponent } from "vue";
import { useStore } from "../../store/index";

export default defineComponent({
  name: "ActionsList",
  data: () => ({
    store: useStore(),
  }),
  computed: {},
  methods: {},
});
</script>
