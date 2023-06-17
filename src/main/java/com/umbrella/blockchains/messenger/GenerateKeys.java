package com.umbrella.blockchains.messenger;

import java.io.*;
import java.security.*;

import com.umbrella.blockchains.messenger.MessengerExceptions.*;

/**
 * The GenerateKeys class provides methods to generate and manage RSA key pairs.
 */
public class GenerateKeys {
    private KeyPairGenerator keyGen;
    private KeyPair pair;
    private PrivateKey privateKey;
    private PublicKey publicKey;

    /**
     * Constructs a GenerateKeys object with the specified key length.
     *
     * @param keyLength the length of the key pair
     * @throws NoSuchAlgorithmException if the RSA algorithm is not available
     */
    public GenerateKeys(int keyLength) throws NoSuchAlgorithmException {
        this.keyGen = KeyPairGenerator.getInstance("RSA");
        this.keyGen.initialize(keyLength);
    }

    /**
     * Generates the key pair.
     */
    public void createKeys() {
        this.pair = this.keyGen.generateKeyPair();
        this.privateKey = pair.getPrivate();
        this.publicKey = pair.getPublic();
    }

    public PrivateKey getPrivateKey() {
        return this.privateKey;
    }

    public PublicKey getPublicKey() {
        return this.publicKey;
    }

    /**
     * Writes the key to a file.
     *
     * @param fileName the name of the file to write the key
     * @param key      the key data to be written
     * @throws FileWritingException if an exception happens while writing to the file.
     */
    public void writeKeysToFile(String fileName, byte[] key) {
        File file = new File(fileName);
        file.getParentFile().mkdirs();

        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(key);
        } catch (IOException e) {
            throw new FileWritingException("Exception occurred while writing to file.", e);
        }
    }
}
