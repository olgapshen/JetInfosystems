> Powered by Koshka!!!

# Test task for JetInfosystems

## Configuration

Create `config.js` file in `/src/main/webapp/js` folder.  
Set `SERVER_API` variable pointing to web application root.  

Example:  

```
var SERVER_API = "http://localhost:8070/jetinfosystems/";
```

Server path can be follow for different environment:

1. Run frontend stand alone, request backend from the server: 
"http://localhost:8070/jetinfosystems"
2. Run frontend stand alone, request backend from the debugger: 
"http://localhost:8888"
3. Run frontend and backend from deployed server or debugger: ".";

Create folder `jetinfosystems` in root of your web server installation.

## Build

For build, from the project root:

```
$ mvn package
$ cd src/main/webapp
$ npm install
$ cd -
```

## Deploy

Deploy (Jetty9 preferred):  

```
$ cp target/jetinfosystems.war [jetty9 root path]/webapps/
```