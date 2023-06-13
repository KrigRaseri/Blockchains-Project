package com.umbrella.blockchains.blockchain;

import com.google.inject.Inject;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.umbrella.blockchains.blockchain.BlockChainUtil.*;


/*
 * The BlockChain class represents a blockchain and provides methods to create a blockchain
 * by generating blocks with the desired number of leading zeros in their hash.
 */

@AllArgsConstructor(onConstructor = @__({@Inject}))
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BlockChain {
    private BlockChainExecutor blockChainExecutor;


    /**
     * Creates a blockchain by generating blocks with the desired number of leading zeros in their hash.
     *
     * @return The created blockchain as a List of blocks.
     */
    public List<Block> createBlockChain() {
        List<Block> blockChain = new ArrayList<>();
        String leadingZeros = "0";

        Block block = new Block(null);
        for (int i = 0; i < 5; i++) {
            block = createBlock(block, leadingZeros);

            if (i == 0) {
                block.setChatLogs(Collections.emptyList());
            }

            blockChain.add(block);
            leadingZeros = adjustNumOfZeros(block, leadingZeros.length(), block.getTimeTaken());
            block = new Block(block);
        }

        blockChainExecutor.executorService.shutdownNow();
        return blockChain;
    }

    /**
     * Creates a block by mining a proved block and collecting chat logs concurrently.
     *
     * @param previousBlock The previous block in the blockchain.
     * @param leadingZeros  The desired number of leading zeros.
     * @return The created block.
     */
    private Block createBlock(Block previousBlock, String leadingZeros) {
        try {
            AtomicBoolean stopMessageCollection = new AtomicBoolean(false);
            Future<List<String>> messageCollectionFuture = blockChainExecutor.startMessageCollectionTask(stopMessageCollection);
            Block block = blockChainExecutor.executorService
                    .invokeAny(blockChainExecutor.createCallableList(previousBlock, leadingZeros));

            stopMessageCollection.set(true);
            block.setChatLogs(messageCollectionFuture.get());
            return block;

        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
