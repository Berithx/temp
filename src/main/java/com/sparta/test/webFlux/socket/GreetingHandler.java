package com.sparta.test.webFlux.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicLong;

@Component
public class GreetingHandler {

    private final AtomicLong counter = new AtomicLong();
    private static final Logger log = LoggerFactory.getLogger(GreetingHandler.class);

    public Mono<ServerResponse> hello(ServerRequest request) {
        log.info(String.valueOf(counter.incrementAndGet()));
        log.info(String.valueOf(Thread.currentThread()));
        return ServerResponse.ok().body(Mono.just("Hello, WebFlux!"), String.class);
    }
}

