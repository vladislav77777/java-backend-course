package edu.java.controller;

import edu.java.client.StackOverflowClient;
import edu.java.entity.dto.StackOverflowResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Check http://localhost:8080/stackoverflow/question/16047306
@RestController
@RequestMapping("/stackoverflow")
public class StackOverflowController {

    private final StackOverflowClient stackOverflowClient;

    @Autowired
    public StackOverflowController(StackOverflowClient stackOverflowClient) {
        this.stackOverflowClient = stackOverflowClient;
    }

    @GetMapping("/question/{questionId}")
    public StackOverflowResponse getQuestion(@PathVariable long questionId) {
        return stackOverflowClient.fetchQuestion(questionId);
    }
}
