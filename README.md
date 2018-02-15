> Powered by Koshka!!!

# Test task for JetInfosystems

## Configuration

Create `config.js` file in `/src/main/webapp/js` folder.  
Set `SERVER_API` variable pointing to web application root.  

Example:  

```
var SERVER_API = "http://localhost:8070/jetinfosystems/";
```

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