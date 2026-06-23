package pluginsystem;

import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.Base64;

/**
 * Generates RSA key pair files for plugin signing workflow.
 */
public final class PluginKeyPairGenerator {
    private PluginKeyPairGenerator() {
    }

    /**
     * Usage: PluginKeyPairGenerator [keysDirectory]
     */
    public static void main(String[] args) throws Exception {
        String targetDir = args.length > 0 ? args[0] : "keys";
        Path keysDir = Path.of(targetDir);
        Files.createDirectories(keysDir);

        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        String publicKey = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
        String privateKey = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());

        Files.writeString(keysDir.resolve("plugin-public.key"), publicKey);
        Files.writeString(keysDir.resolve("plugin-private.key"), privateKey);
    }
}
