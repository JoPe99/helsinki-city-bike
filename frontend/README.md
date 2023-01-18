# Instructions

#### Requirements
- NodeJS (Version 14 or later required)

#### Configuration

To configure server IP, adding an .env-file to the root containing:

```
VUE_APP_SERVER_IP=server IP here
```

If not configured, defaults to "localhost:8081".



#### Run in browser

To run the frontend app, in frontend directory run:

```
npm install
npm run lint
npm run serve
```

#### Run with Electron

```
npm install
npm run lint
npm run electron:serve
```

# Bugs

- Refreshing the page breaks the application due to the store not being ready on it.
    - Could be fixed by for example showing a loading screen on refresh

# Improvements to make

- Standardize insert error messages
- Add a server status icon to the app bar
- Add scrolling and mobile support
- Design is pretty bad, lots of empty space when window is larger