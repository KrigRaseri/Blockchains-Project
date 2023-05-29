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

    static String adjustNumOfZeros(int numOfZeros, int timeTaken) {
        if (timeTaken <= 10) {
            return "0".repeat(numOfZeros + 1);
        } else if (timeTaken >= 60) {
            return "0".repeat(numOfZeros - 1);
        }
        return "0".repeat(numOfZeros);
    }

    static List<Callable<Block>> createCallableList(Block block, String numOfZeros) {
        return IntStream.range(0, 20)
                .mapToObj(j -> newCallable(block, numOfZeros))
                .collect(Collectors.toList());
    }

    private static Callable<Block> newCallable(Block block, String numOfZeros) {
        return new Callable<Block>() {
            @Override
            public Block call() throws Exception {
                return BlockChain.generateProvedBlock(block, numOfZeros);
            }
        };
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
