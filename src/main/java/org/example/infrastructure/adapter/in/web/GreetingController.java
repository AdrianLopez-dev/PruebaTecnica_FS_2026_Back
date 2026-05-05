package org.example.infrastructure.adapter.in.web;

import org.example.domain.model.Greeting;
import org.example.domain.port.in.GetGreetingsUseCase;
import org.example.infrastructure.adapter.in.web.dto.GreetingResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class GreetingController {

    private final GetGreetingsUseCase getGreetingsUseCase;

    public GreetingController(GetGreetingsUseCase getGreetingsUseCase) {
        this.getGreetingsUseCase = getGreetingsUseCase;
    }

    @GetMapping("/service-one")
    public List<GreetingResponse> getServiceOne() {
        return toResponse(getGreetingsUseCase.getGreetings());
    }

    @GetMapping("/service-two")
    public List<GreetingResponse> getServiceTwo() {
        return toResponse(getGreetingsUseCase.getGreetings());
    }

    @GetMapping("/service-three")
    public List<GreetingResponse> getServiceThree() {
        return toResponse(getGreetingsUseCase.getGreetings());
    }

    private List<GreetingResponse> toResponse(List<Greeting> greetings) {
        return greetings.stream()
                .map(g -> new GreetingResponse(g.message()))
                .toList();
    }
}
