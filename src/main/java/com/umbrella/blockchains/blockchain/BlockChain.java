package com.umbrella.blockchains.blockchain;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

public class BlockChain {
    private static ExecutorService executorService = Executors.newFixedThreadPool(5);

    public static ArrayList<Block> createBlockChain() {
        try (Scanner sc = new Scanner(System.in)) {
            ArrayList<Block> blockChain = new ArrayList<>();
            System.out.println("Enter how many zeros the hash must start with:");
            String input = "0".repeat(sc.nextInt());

            Block block = new Block(null);
            for (int i = 0; i < 5; i++) {
                blockChain.add(generateProvedBlock(block, input));
                input = adjustNumOfZeros(input.length(), block.getTimeTaken());
                block = new Block(block);
            }
            return blockChain;
        }
    }

    private static Block generateProvedBlock(Block block, String numOfZeros) {
        long start = System.currentTimeMillis();
        while (!block.getCurrHash().startsWith(numOfZeros)) {
            block.setMagicNumber(ThreadLocalRandom.current().nextInt(100, 9999998 + 1));
            block.setCurrHash(BlockChainUtil.applySha256( BlockChainUtil.createHashString(block) ));
        }
        long end = System.currentTimeMillis();
        block.setTimeTaken((int) ((end-start) / 1000.0));
        return block;
    }

    public static String adjustNumOfZeros(int numOfZeros, int timeTaken) {
        if (timeTaken <= 10) {
            return "0".repeat(numOfZeros + 1);
        } else if (timeTaken >= 60) {
            return "0".repeat(numOfZeros - 1);
    }
        return "0".repeat(numOfZeros);
    }
}
