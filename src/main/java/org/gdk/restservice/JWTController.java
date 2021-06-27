package org.gdk.restservice;

import org.json.JSONObject;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

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

    private byte[] decode(String payload) throws IllegalArgumentException {
        return java.util.Base64.getUrlDecoder().decode(payload.getBytes(StandardCharsets.UTF_8));
    }
}
