package ru.practicum;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class StatsClient {
    private final URI serverUrl = URI.create("http://stats-server:9090");

    private final String appName = "ewm-service";

    private final ObjectMapper objectMapper;


    private final RestTemplate restTemplate = new RestTemplateBuilder()
            .uriTemplateHandler(new DefaultUriBuilderFactory(String.valueOf(serverUrl)))
            .requestFactory(HttpComponentsClientHttpRequestFactory::new)
            .build();

    public void createHit(String uri, String ip) {
        String path = "/hit";
        EndpointHitDto hitDto = new EndpointHitDto();
        hitDto.setApp(appName);
        hitDto.setUri(uri);
        hitDto.setIp(ip);
        hitDto.setTimestamp(LocalDateTime.now());
        HttpEntity<Object> requestEntity = new HttpEntity<>(hitDto, defaultHeaders());
        restTemplate.exchange(path, HttpMethod.POST, requestEntity, Object.class);
    }

    private List<ViewStatsDto> sendStatsRequest(String path) {
        return restTemplate.exchange(path, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<ViewStatsDto>>() {
                }).getBody();
    }

    public List<ViewStatsDto> getViewsByUris(Set<String> uri) {
        List<String> uris = new ArrayList<>(uri);
        URIBuilder path = new URIBuilder().setPath("stats")
                .addParameter("start", "2000-01-01 00:00:00")
                .addParameter("end",
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .addParameter("unique", "true");
        for (String url : uris) {
            path.addParameter("uris", url);
        }
        return sendStatsRequest(URLDecoder.decode(path.toString(), StandardCharsets.UTF_8));
    }

    private HttpHeaders defaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }
}