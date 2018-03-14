package io.giylmi.configuration.web.server;

import io.undertow.Undertow;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.websocket.servlet.UndertowWebSocketServletWebServerCustomizer;
import org.springframework.boot.web.embedded.undertow.UndertowReactiveWebServerFactory;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.boot.web.reactive.server.ReactiveWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ConditionalOnProperty(name = "server.type", havingValue = "undertow")
@Import({UndertowOnPropertyConfiguration.ServletConfiguration.class, UndertowOnPropertyConfiguration.ReactiveConfiguration.class})
public class UndertowOnPropertyConfiguration {

    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    @ConditionalOnClass(io.undertow.websockets.jsr.Bootstrap.class)
    static class ServletConfiguration {

        @Bean
        public UndertowServletWebServerFactory undertowServletWebServerFactory() {
            return new UndertowServletWebServerFactory();
        }

        @Bean
        @ConditionalOnMissingBean(name = "websocketServletWebServerCustomizer")
        public UndertowWebSocketServletWebServerCustomizer websocketContainerCustomizer() {
            return new UndertowWebSocketServletWebServerCustomizer();
        }
    }

    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
    @ConditionalOnMissingBean(ReactiveWebServerFactory.class)
    @ConditionalOnClass({ Undertow.class })
    static class ReactiveConfiguration {

        @Bean
        public UndertowReactiveWebServerFactory undertowReactiveWebServerFactory() {
            return new UndertowReactiveWebServerFactory();
        }

    }
}
