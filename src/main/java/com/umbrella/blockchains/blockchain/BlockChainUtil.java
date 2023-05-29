package com.umbrella.blockchains.blockchain;

import java.security.MessageDigest;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public interface BlockChainUtil {
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

    static String createHashString(Block block) {
        return block.getId()
                + block.getTimeStamp()
                + block.getMagicNumber()
                + block.getPrevHash();
    }

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

    static String generateHash(Block block) {
        return applySha256( BlockChainUtil.createHashString(block));
    }

    static Block copyBlock(Block block) {
        Block blockCopy = new Block();
        blockCopy.setCurrHash(block.getCurrHash());
        blockCopy.setId(block.getId());
        blockCopy.setPrevHash(block.getPrevHash());
        blockCopy.setTimeStamp(block.getTimeStamp());
        return blockCopy;
    }
}
