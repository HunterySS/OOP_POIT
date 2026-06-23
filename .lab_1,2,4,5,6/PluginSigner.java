package pluginsystem;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.HexFormat;
import java.util.Properties;

/**
 * Signs plugin jars and produces a .sig metadata file.
 */
public final class PluginSigner {
    private static final String PRIVATE_KEY_PATH = "keys/plugin-private.key";

    private PluginSigner() {
    }

    /**
     * Usage: PluginSigner <pluginJarPath> [validDays]
     */
    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            throw new IllegalArgumentException("Usage: PluginSigner <pluginJarPath> [validDays]");
        }

        Path pluginPath = Path.of(args[0]);
        if (!Files.exists(pluginPath)) {
            throw new IllegalArgumentException("Plugin file not found: " + pluginPath);
        }

        long validDays = args.length > 1 ? Long.parseLong(args[1]) : 30L;
        long activateFrom = Instant.now().getEpochSecond();
        long activateTo = Instant.now().plus(validDays, ChronoUnit.DAYS).getEpochSecond();
        String hash = calculateSha256(pluginPath);
        String payload = PluginSignatureVerifier.buildPayload(
                pluginPath.getFileName().toString(),
                hash,
                activateFrom,
                activateTo
        );

        PrivateKey privateKey = readPrivateKey(Path.of(PRIVATE_KEY_PATH));
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(payload.getBytes(StandardCharsets.UTF_8));
        String signatureBase64 = Base64.getEncoder().encodeToString(signature.sign());

        Properties properties = new Properties();
        properties.setProperty("hash", hash);
        properties.setProperty("activateFromEpoch", String.valueOf(activateFrom));
        properties.setProperty("activateToEpoch", String.valueOf(activateTo));
        properties.setProperty("signature", signatureBase64);

        Path signaturePath = pluginPath.resolveSibling(pluginPath.getFileName() + ".sig");
        try (var outputStream = Files.newOutputStream(signaturePath)) {
            properties.store(outputStream, "Plugin signature metadata");
        }
    }

    private static String calculateSha256(Path path) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = Files.readAllBytes(path);
        return HexFormat.of().formatHex(digest.digest(bytes));
    }

    private static PrivateKey readPrivateKey(Path path) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(Files.readString(path).trim());
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        return KeyFactory.getInstance("RSA").generatePrivate(keySpec);
    }
}
