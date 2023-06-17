package com.umbrella.blockchains.messenger;

/**
 * A collection of custom exception classes used in the Messenger application.
 */
public class MessengerExceptions {

    //Exception thrown when there is an error during message signing.
    public static class MessageSigningException extends RuntimeException {
        public MessageSigningException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    //Exception thrown when there is an error loading a key.
    public static class KeyLoadingException extends RuntimeException {
        public KeyLoadingException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    //Exception thrown when there is an error writing a file.
    public static class FileWritingException extends RuntimeException {
        public FileWritingException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    //Exception thrown when there is an error during message decryption.
    public static class MessageDecryptionException extends RuntimeException {
        public MessageDecryptionException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    //Exception thrown when there is an error during signature verification.
    public static class SignatureVerificationException extends RuntimeException {
        public SignatureVerificationException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
