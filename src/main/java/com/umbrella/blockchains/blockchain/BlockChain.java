package com.umbrella.blockchains.blockchain;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.umbrella.blockchains.blockchain.BlockChainUtil.generateHash;


public class BlockChain {
    protected static ExecutorService executorService = Executors.newFixedThreadPool(10);

    public static ArrayList<Block> createBlockChain() {
        try (Scanner sc = new Scanner(System.in)) {
            ArrayList<Block> blockChain = new ArrayList<>();
            System.out.println("Enter how many zeros the hash must start with:");
            String input = "0".repeat(sc.nextInt());

            Block block = new Block(null);
            for (int i = 0; i < 5; i++) {
                block = executorService.invokeAny( createCallableList(block, input));
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

    private static Block generateProvedBlock(Block block, String numOfZeros) throws InterruptedException {
        Block blockCopy = BlockChainUtil.copyBlock(block);

        long start = System.currentTimeMillis();
        while (!blockCopy.getCurrHash().startsWith(numOfZeros)) {
            int randomMagicNum = ThreadLocalRandom.current().nextInt(Integer.MAX_VALUE);
            blockCopy.setMagicNumber(randomMagicNum);
            blockCopy.setCurrHash( generateHash(blockCopy));
            if (Thread.currentThread().isInterrupted()) {
                throw new InterruptedException();
            }
        }
        long end = System.currentTimeMillis();
        blockCopy.setTimeTaken((int) ((end-start) / 1000.0));
        blockCopy.setMinerNum(Thread.currentThread().getId());
        return blockCopy;
    }

    private static List<Callable<Block>> createCallableList(Block block, String numOfZeros) {
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
}
