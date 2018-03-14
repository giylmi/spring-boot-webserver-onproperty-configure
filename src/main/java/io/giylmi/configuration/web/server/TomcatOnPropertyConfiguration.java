package io.giylmi.configuration.web.server;

import org.apache.catalina.startup.Tomcat;
import org.apache.coyote.UpgradeProtocol;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.TomcatServletWebServerFactoryCustomizer;
import org.springframework.boot.autoconfigure.websocket.reactive.TomcatWebSocketReactiveWebServerCustomizer;
import org.springframework.boot.autoconfigure.websocket.servlet.TomcatWebSocketServletWebServerCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatReactiveWebServerFactory;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.reactive.server.ReactiveWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.servlet.Servlet;

@Configuration
@ConditionalOnProperty(name = "server.type", havingValue = "tomcat")
@Import({TomcatOnPropertyConfiguration.ServletConfiguration.class, TomcatOnPropertyConfiguration.ReactiveConfiguration.class})
public class TomcatOnPropertyConfiguration {

    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    static class ServletConfiguration {

        @Bean
        @ConditionalOnClass({ Servlet.class, Tomcat.class, UpgradeProtocol.class })
        @ConditionalOnMissingBean(value = ServletWebServerFactory.class, search = SearchStrategy.CURRENT)
        public TomcatServletWebServerFactory tomcatServletWebServerFactory() {
            return new TomcatServletWebServerFactory();
        }

        @Bean
        @ConditionalOnMissingBean(name = "websocketServletWebServerCustomizer")
        @ConditionalOnClass(name = "org.apache.tomcat.websocket.server.WsSci", value = Tomcat.class)
        public TomcatWebSocketServletWebServerCustomizer websocketContainerCustomizer() {
            return new TomcatWebSocketServletWebServerCustomizer();
        }

        @Bean
        @ConditionalOnClass(name = "org.apache.catalina.startup.Tomcat")
        public TomcatServletWebServerFactoryCustomizer tomcatServletWebServerFactoryCustomizer(
                ServerProperties serverProperties) {
            return new TomcatServletWebServerFactoryCustomizer(serverProperties);
        }

    }

    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
    @ConditionalOnMissingBean(ReactiveWebServerFactory.class)
    @ConditionalOnClass({org.apache.catalina.startup.Tomcat.class})
    static class ReactiveConfiguration {
        @Bean
        public TomcatReactiveWebServerFactory tomcatReactiveWebServerFactory() {
            return new TomcatReactiveWebServerFactory();
        }

        @Bean
        @ConditionalOnClass(name = "org.apache.tomcat.websocket.server.WsSci", value = Tomcat.class)
        @ConditionalOnMissingBean(name = "websocketReactiveWebServerCustomizer")
        public TomcatWebSocketReactiveWebServerCustomizer websocketContainerCustomizer() {
            return new TomcatWebSocketReactiveWebServerCustomizer();
        }

    }
}
