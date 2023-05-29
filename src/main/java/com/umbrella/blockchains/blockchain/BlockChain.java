package com.umbrella.blockchains.blockchain;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.*;

public class BlockChain {
    private static final int THREAD_POOL_SIZE = 10;
    private static ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

    public static ArrayList<Block> createBlockChain() {
        try (Scanner sc = new Scanner(System.in)) {
            ArrayList<Block> blockChain = new ArrayList<>();
            System.out.println("Enter how many zeros the hash must start with:");
            String input = "0".repeat(sc.nextInt());

            Block block = new Block(null);
            for (int i = 0; i < 5; i++) {
                block = executorService.invokeAny( BlockChainUtil.createCallableList(block, input));
                blockChain.add(block);
                input = BlockChainUtil.adjustNumOfZeros(block, input.length(), block.getTimeTaken());
                block = new Block(block);
            }
            executorService.shutdownNow();
            return blockChain;

        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    static Block generateProvedBlock(Block block, String numOfZeros) {
        Block blockCopy = BlockChainUtil.copyBlock(block);

        long start = System.currentTimeMillis();
        while (!blockCopy.getCurrHash().startsWith(numOfZeros)) {
            blockCopy.setMagicNumber(ThreadLocalRandom.current().nextInt(Integer.MAX_VALUE));
            blockCopy.setCurrHash( BlockChainUtil.applySha256( BlockChainUtil.createHashString(blockCopy)));
        }
        long end = System.currentTimeMillis();
        blockCopy.setTimeTaken((int) ((end-start) / 1000.0));
        blockCopy.setMinerNum(Thread.currentThread().getId());
        return blockCopy;
    }
}
