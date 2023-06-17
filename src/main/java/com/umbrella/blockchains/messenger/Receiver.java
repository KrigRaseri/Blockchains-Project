package com.umbrella.blockchains.messenger;

import java.io.*;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.List;

import com.umbrella.blockchains.messenger.MessengerExceptions.*;

/**
 * The Receiver class is responsible for decrypting a message and verifying its signature.
 */
public class Receiver {

    /**
     * Decrypts a message from a data file and verifies its signature using the provided public key.
     *
     * @param pathToData     the path to the data file containing the encrypted message and signature
     * @param publicKeyPath  the path to the public key file used for signature verification
     * @return the decrypted message if the signature is valid, or an empty string  otherwise
     * @throws MessageDecryptionException if an error occurs during message decryption
     */
    @SuppressWarnings("unchecked")
    public String decryptMessage(String pathToData, String publicKeyPath) throws MessageDecryptionException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(pathToData))) {
            List<byte[]> list = (List<byte[]>) in.readObject();

            //Element 0 of the list is the message, while element 1 of the list is the signature.
            if (verifySignature(list.get(0), list.get(1), publicKeyPath)) {
                return new String(list.get(0));
            } else {
                System.err.println("Verification of signature failed.");
                return "";
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new MessageDecryptionException("Failed to decrypt the message from the data file at " + pathToData, e);
        }
    }

    /**
     * Verifies the signature of the provided data using the public key from the specified file.
     *
     * @param data      the data to verify the signature of
     * @param signature the signature to be verified
     * @param keyFile   the path to the public key file
     * @return true if the signature is valid, false otherwise
     * @throws SignatureVerificationException if an error occurs during signature verification
     */
    private boolean verifySignature(byte[] data, byte[] signature, String keyFile) throws SignatureVerificationException {
        try {
            Signature sig = Signature.getInstance("SHA1withRSA");
            sig.initVerify(getPublic(keyFile));
            sig.update(data);
            return sig.verify(signature);
        } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
            throw new SignatureVerificationException("Signature verification failed.", e);
        }
    }

    /**
     * Reads the public key from the specified file and returns the corresponding PublicKey object.
     *
     * @param filename the path to the public key file
     * @return the PublicKey object
     * @throws KeyLoadingException if an error occurs during public key loading
     */
    private PublicKey getPublic(String filename) throws KeyLoadingException {
        try {
            byte[] keyBytes = Files.readAllBytes(new File(filename).toPath());
            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePublic(spec);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new KeyLoadingException("Failed to load the public key.", e);
        }
    }
}