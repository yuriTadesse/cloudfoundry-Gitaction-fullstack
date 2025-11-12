package com.example.demo;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import java.util.Map;
@RestController
@CrossOrigin(origins = "*")
public class HelloController {
    private final Counter helloCounter;
    public HelloController(MeterRegistry meterRegistry) {
        this.helloCounter = Counter.builder("app_hello_requests_total")
            .description("Total /api/hello requests")
            .register(meterRegistry);
    }
    @GetMapping("/api/hello")
    public Map<String, String> hello() {
        helloCounter.increment();
        return Map.of("message", "Hello from Spring Boot on Cloud Foundry!");
    }
}
