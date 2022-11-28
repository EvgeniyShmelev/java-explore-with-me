package ru.practicum.ewmmain.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import ru.practicum.ewmmain.events.model.Hit;
import ru.practicum.ewmmain.events.model.ViewStats;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@RestController
public class RestClient {
    private static final String FALSE_STRING_VALUE = "false";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Value("${stats-server-post.path}")
    private String hitPostPath;
    @Value("${stats-server-get.path}")
    private String hitGetPath;

    private final RestTemplate rest;

    public RestClient(RestTemplate rest) {
        this.rest = rest;
    }

    public void postHit(Hit hit) {
        log.info("Sending package to statistics server: {}", hit);
        rest.postForEntity(hitPostPath, hit, Hit.class);
    }

    public ViewStats getStats(int eventId, LocalDateTime start, LocalDateTime end) {
        log.info("Requesting stats fro statistics server");
        ViewStats[] stats = rest.getForObject(
                String.format(
                        hitGetPath,
                        start.format(formatter),
                        end.format(formatter),
                        List.of(URLEncoder.encode("/events/" + eventId, StandardCharsets.UTF_8)),
                        FALSE_STRING_VALUE),
                ViewStats[].class
        );
        return stats != null ? stats[0] : null;
    }
}