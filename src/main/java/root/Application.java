package root;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import root.entities.User;

import java.net.URI;
import java.util.Optional;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        String url = "http://94.198.50.185:7081/api/users";
        RestTemplate restTemplate = new RestTemplate();
        StringBuilder result = new StringBuilder();
        User user = new User(3L, "James", "Brown",  (byte) 0);

        try (var context = SpringApplication.run(Application.class, args)) {

            // cookie
            HttpHeaders headers = new HttpHeaders();
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            String cookie = Optional.ofNullable(response.getHeaders().get("set-cookie"))
                    .orElseThrow().get(0);
            headers.set("cookie", cookie);

            System.out.println("Cookie: " + cookie);


            // First part -----------------------
            RequestEntity<User> request = new RequestEntity<>(user, headers, HttpMethod.POST, URI.create(url));
            response = restTemplate.exchange(request, String.class);

            result.append(response.getBody());


            // Second part ----------------------
            user.setName("Thomas");
            user.setLastName("Shelby");

            request = new RequestEntity<>(user, headers, HttpMethod.PUT, URI.create(url));
            response = restTemplate.exchange(request, String.class);

            result.append(response.getBody());


            // Third part -----------------------
            request = new RequestEntity<>(headers, HttpMethod.DELETE, URI.create(url + "/3"));
            response = restTemplate.exchange(request, String.class);

            result.append(response.getBody());

            System.out.println(result);
        }
    }

}
