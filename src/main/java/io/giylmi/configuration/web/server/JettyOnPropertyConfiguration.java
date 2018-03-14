package io.giylmi.configuration.web.server;

import org.eclipse.jetty.websocket.jsr356.server.deploy.WebSocketServerContainerInitializer;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.boot.autoconfigure.websocket.servlet.JettyWebSocketServletWebServerCustomizer;
import org.springframework.boot.web.embedded.jetty.JettyReactiveWebServerFactory;
import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory;
import org.springframework.boot.web.reactive.server.ReactiveWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ConditionalOnProperty(name = "server.type", havingValue = "jetty")
@Import({JettyOnPropertyConfiguration.ServletConfiguration.class, JettyOnPropertyConfiguration.ReactiveConfiguration.class})
public class JettyOnPropertyConfiguration {

    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    @ConditionalOnClass(WebSocketServerContainerInitializer.class)
    static class ServletConfiguration {

        @Bean
        @ConditionalOnMissingBean(name = "websocketServletWebServerCustomizer")
        public JettyWebSocketServletWebServerCustomizer websocketContainerCustomizer() {
            return new JettyWebSocketServletWebServerCustomizer();
        }

        @Bean
        @ConditionalOnMissingBean(value = ServletWebServerFactory.class, search = SearchStrategy.CURRENT)
        public JettyServletWebServerFactory JettyServletWebServerFactory() {
            return new JettyServletWebServerFactory();
        }
    }

    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
    @ConditionalOnMissingBean(ReactiveWebServerFactory.class)
    @ConditionalOnClass({ org.eclipse.jetty.server.Server.class })
    static class ReactiveConfiguration {


        @Bean
        public JettyReactiveWebServerFactory jettyReactiveWebServerFactory() {
            return new JettyReactiveWebServerFactory();
        }

    }
}
