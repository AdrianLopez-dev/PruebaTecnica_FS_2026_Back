package org.example.domain.port.in;

import org.example.domain.model.Greeting;

import java.util.List;

public interface GetGreetingsUseCase {

    List<Greeting> getGreetings();
}
