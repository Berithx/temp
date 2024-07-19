package com.sparta.test.webFlux.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class CustomWebSocketHandler implements WebSocketHandler {

    private static final Logger log = LoggerFactory.getLogger(CustomWebSocketHandler.class);
    private final WebClient webClient;

    public CustomWebSocketHandler() {
        this.webClient = WebClient.create("http://localhost:8080");
    }


    @Override
    public Mono<Void> handle(WebSocketSession session) {
        return session.send(
                session.receive()
                        .flatMap(msg -> {
                            // 원본 메시지를 그대로 반환
                            Mono<WebSocketMessage> echoMessage = Mono.just(session.textMessage("Echo: " + msg.getPayloadAsText()));
                            log.info(msg.getPayloadAsText());

                            // GreetingHandler의 결과를 호출하여 반환
                            Mono<WebSocketMessage> greetingMessage = webClient.get()
                                    .uri("/hello")
                                    .retrieve()
                                    .bodyToMono(String.class)
                                    .map(response -> session.textMessage("Greeting: " + response));

                            // 원본 메시지와 Greeting 메시지를 합쳐서 반환
                            return Flux.concat(echoMessage, greetingMessage);
                        })
        );
    }
}
