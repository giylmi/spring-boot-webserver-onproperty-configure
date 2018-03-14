package io.giylmi.configuration.web.server;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.boot.web.reactive.server.ReactiveWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import reactor.ipc.netty.http.server.HttpServer;

@Configuration
@ConditionalOnProperty(name = "server.type", havingValue = "netty")
@Import(NettyOnPropertyConfiguration.ReactiveConfiguration.class)
public class NettyOnPropertyConfiguration {

    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
    @ConditionalOnMissingBean(ReactiveWebServerFactory.class)
    @ConditionalOnClass({ HttpServer.class })
    static class ReactiveConfiguration {
        @Bean
        public NettyReactiveWebServerFactory NettyReactiveWebServerFactory() {
            return new NettyReactiveWebServerFactory();
        }

    }
}
