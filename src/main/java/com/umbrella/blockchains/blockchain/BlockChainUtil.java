package com.umbrella.blockchains.blockchain;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public interface BlockChainUtil {

    /**
     * Applies the SHA-256 hashing algorithm to the given input string.
     *
     * @param input The input string to hash.
     * @return The hashed output as a hexadecimal string.
     */
    static String applySha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            /* Applies sha256 to our input */
            byte[] hash = digest.digest(input.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for (byte elem: hash) {
                String hex = Integer.toHexString(0xff & elem);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates a hash string for a given block by concatenating its attributes.
     *
     * @param block The block to create the hash string for.
     * @return The hash string.
     */
    private static String createHashString(Block block) {
        return block.getId()
                + block.getTimeStamp()
                + block.getMagicNumber()
                + block.getPrevHash();
    }

    /**
     * Adjusts the number of leading zeros based on the time taken to mine the block.
     *
     * @param block      The block to adjust.
     * @param numOfZeros The current number of leading zeros.
     * @param timeTaken  The time taken to mine the block.
     * @return The adjusted number of leading zeros.
     */
    static String adjustNumOfZeros(Block block, int numOfZeros, int timeTaken) {
        String numZeros;
        if (timeTaken <= 10) {
            numZeros = "0".repeat(numOfZeros + 1);
            block.setNumOfZerosChange(String.format("N was increased to %d", numZeros.length()));
            return numZeros;

        } else if (timeTaken >= 60) {
            numZeros = "0".repeat(numOfZeros - 1);
            block.setNumOfZerosChange(String.format("N was decreased by %d", numZeros.length()));
            return numZeros;
        }
        block.setNumOfZerosChange("N stays the same");
        return "0".repeat(numOfZeros);
    }

    /**
     * Takes an existing block and creates and returns a new hash string.
     *
     * @param block The block to create the hash string for.
     * @return The new hash string.
     */
    static String createNewHash(Block block) {
        return applySha256( BlockChainUtil.createHashString(block));
    }

    /**
     * Creates a copy of a given block.
     *
     * @param block The block to copy.
     * @return The copied block.
     */
    static Block copyBlock(Block block) {
        Block blockCopy = new Block();
        blockCopy.setCurrHash(block.getCurrHash());
        blockCopy.setId(block.getId());
        blockCopy.setPrevHash(block.getPrevHash());
        blockCopy.setTimeStamp(block.getTimeStamp());
        return blockCopy;
    }

    /**
     * Generates a random chat message.
     *
     * @return The random chat message.
     */
    static String getRandomMessage() {
        List<String> randomWords = new ArrayList<>(Arrays.asList("Tom: Hey, I'm first!",
                "Sarah: It's not fair!", "Sarah: You always will be first because it is your blockchain!",
                "Sarah: Anyway, thank you for this amazing chat.", "Tom: You're welcome :)",
                "Nick: Hey Tom, nice chat"));

        return randomWords.get(ThreadLocalRandom.current().nextInt(randomWords.size()));
    }
}
