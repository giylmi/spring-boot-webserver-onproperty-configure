### Simple configuration to allow switch between embedded servers by system property instead of Spring Boot autodetecting

#### Build

`./gradlew build`

Output is artifact will be in `build/libs`

#### Run

`java -jar build/libs/ybootserver-0.0.1-SNAPSHOT.jar --server.type=<server-type>`

server-type = 'tomcat' | 'jetty' | 'undertow'

By default this will start servlet server.
If you want run reactive server change `spring-boot-starter-web` to `spring-boot-starter-webflux` in `build.gradle`.

This will add one option to `server-type` - `netty`.

