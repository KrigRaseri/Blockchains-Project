package com.umbrella.blockchains.messenger;

import java.io.*;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.List;

import com.umbrella.blockchains.messenger.MessengerExceptions.*;

/**
 * Represents a message that includes the text message, signature, and key pair information.
 */
public class Message {
    private List<byte[]> list;

    /**
     * Constructs a Message object with the given text message and key pair file path.
     *
     * @param textMessage      The text message to be included in the message.
     * @param keyPairFilePath  The file path to the key pair used for signing.
     */
    public Message(String textMessage, String keyPairFilePath) {
        this.list = new ArrayList<>();
        this.list.add(textMessage.getBytes());
        this.list.add(sign(textMessage, keyPairFilePath));
    }

    /**
     * Signs the given data using the private key stored in the specified key file.
     *
     * @param data     The data to be signed.
     * @param keyFile  The file path to the private key.
     * @return The signature of the data.
     * @throws MessageSigningException if an error occurs during the signing process.
     */
    private byte[] sign(String data, String keyFile) {
        try {
            Signature rsa = Signature.getInstance("SHA1withRSA");
            rsa.initSign(getPrivate(keyFile));
            rsa.update(data.getBytes());
            return rsa.sign();
        } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
            throw new MessageSigningException("Failed to sign the message", e);
        }
    }

    /**
     * Retrieves the private key from the specified file.
     *
     * @param filename  The file path to the private key.
     * @return The PrivateKey object.
     * @throws KeyLoadingException if an error occurs during the key retrieval process.
     */
    private PrivateKey getPrivate(String filename) {
        try {
            byte[] keyBytes = Files.readAllBytes(new File(filename).toPath());
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePrivate(spec);
        } catch (IOException e) {
            throw new KeyLoadingException("Failed to read the private key file", e);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new KeyLoadingException("Failed to generate the private key", e);
        }
    }

    /**
     * Writes the message to the specified file path using object serialization.
     *
     * @param filePath  The file path to write the message.
     * @throws FileWritingException if an error occurs during the file write process.
     */
    public void writeToFile(String filePath) {
        File f = new File(filePath);
        f.getParentFile().mkdirs();

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath))) {
            out.writeObject(this.list);
        } catch (IOException e) {
            throw new FileWritingException("Failed to write the message to a file", e);
        }
    }
}
