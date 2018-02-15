> Powered by Koshka!!!

# Test task for JetInfosystems

For build, from the project root:

```
$ mvn package
$ cd src/main/webapp
$ npm install
$ cd -
```

Deploy (Jetty9 preferred):  

```
$ cp target/jetinfosystems.war [jetty9 root path]/webapps/
```