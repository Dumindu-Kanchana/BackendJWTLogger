package org.gdk.restservice;

import org.json.JSONObject;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;

import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1OutputStream;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@RestController
public class JWTController {
    private static final Logger logger = LoggerFactory.getLogger(JWTController.class);

    @PostMapping("/pem-cert")
    public ResponseEntity<String> backendJWT(@RequestBody String jwksResponse) {
        String pemPublicKey = "";
        try {
            JSONObject jwks = getJWKS(jwksResponse);
            PublicKey publicKey = extractRSAPublicKey(jwks);
            pemPublicKey = publicKeyToPEM(publicKey);
            System.out.println(pemPublicKey);
        } catch (Exception e) {
            logger.error("Error while processing request. ", e);
            return new ResponseEntity<String>("Error while processing request", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<String>("cert: " + pemPublicKey, HttpStatus.OK);
    }

    public static JSONObject getJWKS(String jwksResponse) throws IOException {
        //String jwksResponse = "{\"keys\":[{\"kty\":\"RSA\",\"e\":\"AQAB\",\"use\":\"sig\",\"kid\":\"ZWQxZjAyMzZlMmMzYTVMWQ3M2Y1MjQwNzVlZmQ0NGE4M2ZkZQ\",\"alg\":\"RS256\",\"n\":\"zC3JHRTr1UHxtCfyin_30hH4SxSE2WmYuRZIGCUwlCErweoj5swPx4_WaW12mtt5NsAlr7GzZunkK2UgZFia8OQHIKfnBYlsPPA_jx1nF0ASFh9bAc-71cVbirNHiTIjsUpiRyuzUkkfh3euNhipJnNLKD9wLaWSwO_ziGmjp3cVbAcCD2bkKvwRLldA3cMmxhhgkfZzw7pkOvJ8blZxpMZBfigxmWm14xVrJCWC7MBJ7tWXsprVj8lxPBTiHDnPgPD1sjW9TtI5UrdBT30lV7hV22cXUCCQ\"}]}";
        return new JSONObject(jwksResponse);
    }

    // Method to extract RSA public key from JWKS response
    public static PublicKey extractRSAPublicKey(JSONObject jwks) throws NoSuchAlgorithmException, InvalidKeySpecException {
        JSONArray keys = jwks.getJSONArray("keys");
        // Assuming you're interested in the first key in the array
        JSONObject key = keys.getJSONObject(0);
        BigInteger modulus = new BigInteger(1, base64UrlDecode(key.getString("n")));
        BigInteger exponent = new BigInteger(1, base64UrlDecode(key.getString("e")));

        RSAPublicKeySpec spec = new RSAPublicKeySpec(modulus, exponent);
        KeyFactory factory = KeyFactory.getInstance("RSA");
        return factory.generatePublic(spec);
    }

    // Method to decode base64 URL encoded string
    private static byte[] base64UrlDecode(String input) {
        String base64 = input.replace('-', '+').replace('_', '/');
        return java.util.Base64.getDecoder().decode(base64);
    }

    public static String publicKeyToPEM(PublicKey publicKey) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        RSAPublicKey rsaPublicKey = (RSAPublicKey) publicKey;
        byte[] publicKeyEncoded = rsaPublicKey.getEncoded();
        return "-----BEGIN PUBLIC KEY-----\n" + Base64.getMimeEncoder().encodeToString(publicKeyEncoded) + "\n-----END PUBLIC KEY-----";
    }
}
