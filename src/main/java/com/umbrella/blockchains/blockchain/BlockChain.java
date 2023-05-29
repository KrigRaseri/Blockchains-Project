package com.umbrella.blockchains.blockchain;

import java.util.ArrayList;
import java.util.List;
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
            for (int i = 0; i < 2; i++) {
                block = executorService.invokeAny( createCallableList(block, input) );
                blockChain.add(block);
                input = adjustNumOfZeros(input.length(), block.getTimeTaken());
                block = new Block(block);
            }
            executorService.shutdownNow();
            return blockChain;

        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static Block generateProvedBlock(Block block, String numOfZeros) {
        long start = System.currentTimeMillis();
        Block b = new Block();
        b.setCurrHash(block.getCurrHash());
        b.setId(block.getId());
        b.setPrevHash(block.getPrevHash());
        b.setTimeStamp(block.getTimeStamp());

        String curHash;
        int magicNum;

        while (!b.getCurrHash().startsWith(numOfZeros)) {
            magicNum = ThreadLocalRandom.current().nextInt(Integer.MAX_VALUE);;
            b.setMagicNumber(magicNum);
            curHash = BlockChainUtil.applySha256( BlockChainUtil.createHashString(b) );
            b.setCurrHash(curHash);
        }
        long end = System.currentTimeMillis();
        b.setTimeTaken((int) ((end-start) / 1000.0));
        return b;
    }

    private static List<Callable<Block>> createCallableList(Block block, String numOfZeros) {
        List<Callable<Block>> callableList = new ArrayList<>();
        for (int j = 0; j < 10; j++) {
            callableList.add(() -> generateProvedBlock(block, numOfZeros));
        }
        return callableList;
    }

    public static String adjustNumOfZeros(int numOfZeros, int timeTaken) {
        if (timeTaken <= 10) {
            return "0".repeat(numOfZeros + 1);
        } else if (timeTaken >= 60) {
            return "0".repeat(numOfZeros - 1);
        }
        return "0".repeat(numOfZeros);
    }

    private static Callable<Block> newCallable(Block block, String numOfZeros) {
        return new Callable<Block>() {
            @Override
            public Block call() throws Exception {
                return generateProvedBlock(block, numOfZeros);
            }
        };
    }
}
