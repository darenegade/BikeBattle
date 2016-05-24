# BikeBattleBackend

##Starten
1. Mit Intellij das Repo importieren

2. Lombok Plugin installieren und als Annotation Processors für das Projekt aktivieren (https://github.com/mplushnikov/lombok-intellij-plugin)

3a. BackendApplication.main als Spring Application starten

3b. Mit Gradle WAR File bauen und in Tomcat deployn

##RESTful
Erlaubt sind alle CRUD Operationen und es ist eine Suche implementiert. Hateos ist integriert.
Übermittlung der Daten im JSON Format.
Schnittstellen:
* /users
* /routes
* /measurements
* /drives
