package org.example.application.service;

import org.example.domain.model.Greeting;
import org.example.domain.port.in.GetGreetingsUseCase;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.IntStream;

@Service
public class GreetingService implements GetGreetingsUseCase {

    private static final int GREETING_COUNT = 10;
    private static final String GREETING_MESSAGE = "Hello World";

    @Override
    public List<Greeting> getGreetings() {
        return IntStream.range(0, GREETING_COUNT)
                .mapToObj(i -> new Greeting(GREETING_MESSAGE))
                .toList();
    }
}
