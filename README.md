# GPS Hawk

GPS Hawk is a GPS Tracking App for Android. It is part of a project of the University of Applied Sciences Vorarlberg. 
Please check out [FHVGIS](https://github.com/Lucasvo1/FHVGIS) for the server part.

## Documentation

### Server Communication
Check out the [Webservice of FHVGIS](https://github.com/Lucasvo1/FHVGIS/tree/master/fhvgis/webservice/src/main/java/at/fhv/itm14/fhvgis/webservice/app) for the interfaces.

### Exporting the data
All data will be automatically exported to the server.

#### Modes
All data can be exported to the server in two modes.

##### Exporting continously
In this mode all captured data will be automatically send to the server. This is an ongoing process that requires more mobile traffic than the other mode.
You can enable this mode in the settings.

##### Exporting in junks
In this mode all data will just be captured incremently without exporting them directly. As soon as you'll have a WLAN connection it will automatically upload all captured data in junks.
You can always upload all data manually in the export activity.

### Reserved Tracks
Because you might don't have mobile data enabled, the tracks will be pregenerated on the server before you can actually start to use them. This means that they will actually be persisted in the database on the server but they won't have any waypoints and also the start date of the track entity will be `null`.

The app will automatically ask for new tracks when it has an internet connection and the number of reserved tracks falls below a predefined threshold.

When you completely run out of tracks the app will try to reserve new tracks. If this fails the tracking cannot be started and an error message will be displayed.
