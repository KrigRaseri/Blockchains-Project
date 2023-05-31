package com.umbrella.blockchains.blockchain;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.umbrella.blockchains.blockchain.BlockChainUtil.*;

/**
 * A simple implementation of a blockchain.
 */
public class BlockChain {
    private static final int THREAD_POOL_SIZE = 20;
    private static final  ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

    /**
     * Creates a blockchain by generating blocks with the desired number of leading zeros in their hash.
     *
     * @return The created blockchain as a List of blocks.
     */
    public static List<Block> createBlockChain() {
        try {
            List<Block> blockChain = new ArrayList<>();
            String numOfZeros = "0";

            Block block = new Block(null);
            for (int i = 0; i < 5; i++) {
                block = executorService.invokeAny( createCallableList(block, numOfZeros));
                blockChain.add(block);
                numOfZeros = adjustNumOfZeros(block, numOfZeros.length(), block.getTimeTaken());
                block = new Block(block);
            }
            executorService.shutdownNow();
            return blockChain;

        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Generates a proved block by finding a hash with the desired number of leading zeros.
     *
     * @param block      The previous block in the blockchain.
     * @param numOfZeros The desired number of leading zeros.
     * @return The proved block.
     * @throws InterruptedException If the thread is interrupted.
     */
    private static Block generateProvedBlock(Block block, String numOfZeros) throws InterruptedException {
        Block blockCopy = BlockChainUtil.copyBlock(block);

        long start = System.currentTimeMillis();
        while (!blockCopy.getCurrHash().startsWith(numOfZeros)) {
            int randomMagicNum = ThreadLocalRandom.current().nextInt(Integer.MAX_VALUE);
            blockCopy.setMagicNumber(randomMagicNum);
            blockCopy.setCurrHash( createNewHash(blockCopy));
            if (Thread.currentThread().isInterrupted()) {
                throw new InterruptedException();
            }
        }
        long end = System.currentTimeMillis();
        blockCopy.setTimeTaken((int) ((end-start) / 1000.0));
        blockCopy.setMinerNum(Thread.currentThread().getId());
        return blockCopy;
    }

    /**
     * Creates a list of Callable objects to generate proved blocks.
     *
     * @param block      The previous block in the blockchain.
     * @param numOfZeros The desired number of leading zeros.
     * @return The list of Callable objects.
     */
    private static List<Callable<Block>> createCallableList(Block block, String numOfZeros) {
        return IntStream.range(0, 20)
                .mapToObj(j -> newCallable(block, numOfZeros))
                .collect(Collectors.toList());
    }

    /**
     * Creates a new Callable object for generating proved blocks.
     *
     * @param block      The previous block in the blockchain.
     * @param numOfZeros The desired number of leading zeros.
     * @return The Callable object.
     */
    private static Callable<Block> newCallable(Block block, String numOfZeros) {
        return new Callable<Block>() {
            @Override
            public Block call() throws Exception {
                return BlockChain.generateProvedBlock(block, numOfZeros);
            }
        };
    }
}
