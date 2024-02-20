package edu.java.client;

import org.springframework.web.reactive.function.client.WebClient;

public class Client {
    protected WebClient webClient;

    public Client(String url) {
        this.webClient = WebClient.create(url);
    }
}
