package nx.ese.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class NX_Encrypting {

    public static final String SHA1 = "SHA-1";

    public static final String SHA256 = "SHA-256";

    public static final String SHA384 = "SHA-384";

    public static final String SHA512 = "SHA-512";

    private String algorithm;
    
    private static final Logger logger = LoggerFactory.getLogger(NX_Encrypting.class);

    public NX_Encrypting(String algorithm) {
        this.algorithm = algorithm;
    }

    public NX_Encrypting() {
        this(SHA1);
    }

    public byte[] encrypt(String message) {
        byte[] digest = null;
        byte[] buffer = message.getBytes(StandardCharsets.UTF_8);
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            messageDigest.reset();
            messageDigest.update(buffer);
            digest = messageDigest.digest();
        } catch (NoSuchAlgorithmException nsae) {
        	logger.error("Error: " + nsae);
        }
        return digest;
    }

    public String encryptInBase64() {
        return Base64.getEncoder().encodeToString(this.encrypt(UUID.randomUUID().toString()));
    }

    public String encryptInBase64UrlSafe() {
        String code64Url = Base64.getUrlEncoder().encodeToString(this.encrypt(UUID.randomUUID().toString()));
        return code64Url.substring(0, code64Url.indexOf('='));
    }

}
