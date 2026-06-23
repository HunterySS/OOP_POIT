package pluginsystem;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.util.Base64;
import java.util.HexFormat;
import java.util.Properties;

/**
 * Verifies plugin integrity, activation window, and author signature.
 */
public final class PluginSignatureVerifier {
    private static final String PUBLIC_KEY_PATH = "keys/plugin-public.key";

    private PluginSignatureVerifier() {
    }

    /**
     * Validates .sig metadata and verifies plugin jar signature.
     */
    public static boolean verify(Path pluginJarPath) {
        try {
            Path signaturePath = pluginJarPath.resolveSibling(pluginJarPath.getFileName() + ".sig");
            if (!Files.exists(signaturePath)) {
                return false;
            }

            Properties signatureProperties = readProperties(signaturePath);
            String expectedHash = signatureProperties.getProperty("hash");
            long activateFrom = Long.parseLong(signatureProperties.getProperty("activateFromEpoch"));
            long activateTo = Long.parseLong(signatureProperties.getProperty("activateToEpoch"));
            String signatureBase64 = signatureProperties.getProperty("signature");

            long now = Instant.now().getEpochSecond();
            if (now < activateFrom || now > activateTo) {
                return false;
            }

            String actualHash = calculateSha256(pluginJarPath);
            if (!actualHash.equalsIgnoreCase(expectedHash)) {
                return false;
            }

            String payload = buildPayload(pluginJarPath.getFileName().toString(), expectedHash, activateFrom, activateTo);
            PublicKey publicKey = readPublicKey(Path.of(PUBLIC_KEY_PATH));
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initVerify(publicKey);
            signature.update(payload.getBytes(StandardCharsets.UTF_8));
            return signature.verify(Base64.getDecoder().decode(signatureBase64));
        } catch (Exception exception) {
            return false;
        }
    }

    /**
     * Creates canonical payload that both signer and verifier use.
     */
    static String buildPayload(String pluginFileName, String hash, long activateFrom, long activateTo) {
        return pluginFileName + "|" + hash + "|" + activateFrom + "|" + activateTo;
    }

    private static Properties readProperties(Path path) throws IOException {
        Properties properties = new Properties();
        try (InputStream inputStream = Files.newInputStream(path)) {
            properties.load(inputStream);
        }
        return properties;
    }

    private static String calculateSha256(Path path) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = Files.readAllBytes(path);
        return HexFormat.of().formatHex(digest.digest(bytes));
    }

    private static PublicKey readPublicKey(Path path) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(Files.readString(path).trim());
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        return KeyFactory.getInstance("RSA").generatePublic(keySpec);
    }
}
