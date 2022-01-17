package org.gdk.restservice;

import org.json.JSONObject;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;

@RestController
public class JWTController {
    private static final Logger logger = LoggerFactory.getLogger(JWTController.class);

    @GetMapping("/jwt")
    public ResponseEntity<String> backendJWT(@RequestHeader("X-JWT-Assertion") String jwt) {
        // code that uses the jwt
        logger.info("jwt is " + jwt);
        try {
            JSONObject payload = null;
            String splitToken[] = jwt.split("\\.");
            payload = new JSONObject(new String(decode(splitToken[1])));
            long expiryTime = payload.getLong("exp") * 1000;
            long currentTime = System.currentTimeMillis();
            if (currentTime > expiryTime) {
                logger.info("currentTime > expiryTime is true. exp=" + expiryTime + "  currentTime=" + currentTime);
            } else {
                logger.info("(expiryTime - currentTime) = " + (expiryTime-currentTime)/1000 + " seconds");
            }
        } catch (Exception e) {
            return new ResponseEntity<String>("Error while processing JWT", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<String>("jwt received", HttpStatus.OK);
    }

    @PostMapping(
            value = "/rest-auth",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<CreateResponse> postBody(@RequestBody Person person, @RequestHeader("Accept") String accept) {
        logger.info("Username/Password of the request is " + person.getCredentials().getUsername() +
                " " + person.getCredentials().getPassword());
        CreateResponse response = new CreateResponse();
        response.setResponse(person.getCredentials().getPassword());
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(accept)).body(response);
    }

    private byte[] decode(String payload) throws IllegalArgumentException {
        return java.util.Base64.getUrlDecoder().decode(payload.getBytes(StandardCharsets.UTF_8));
    }
}
