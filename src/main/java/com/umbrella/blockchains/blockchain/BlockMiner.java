package com.umbrella.blockchains.blockchain;

import java.util.concurrent.ThreadLocalRandom;

public class BlockMiner {

    /**
     * Mines for a proved block by finding a hash with the desired number of leading zeros.
     *
     * @param block        The previous block in the blockchain.
     * @param leadingZeros The desired number of leading zeros.
     * @return The proved block.
     * @throws InterruptedException If the thread is interrupted.
     */
    protected Block mineProvedBlock(Block block, String leadingZeros) throws InterruptedException {
        Block blockCopy = BlockChainUtil.copyBlock(block);

        long start = System.currentTimeMillis();
        while (!blockCopy.getCurrHash().startsWith(leadingZeros)) {
            int randomMagicNum = ThreadLocalRandom.current().nextInt(Integer.MAX_VALUE);
            blockCopy.setMagicNumber(randomMagicNum);
            blockCopy.setCurrHash(BlockChainUtil.createNewHash(blockCopy));

            if (Thread.currentThread().isInterrupted()) {
                System.out.println("Block mining was interrupted.");
                throw new InterruptedException();
            }
        }
        long end = System.currentTimeMillis();

        blockCopy.setTimeTaken((int) ((end - start) / 1000.0));
        blockCopy.setMinerNum(Thread.currentThread().getId());
        return blockCopy;
    }
}
