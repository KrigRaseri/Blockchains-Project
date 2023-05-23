package com.umbrella.blockchains.blockchain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BlockChain {

    public List<Block> createBlocks() {
        List<Block> blockChain = new ArrayList<>();
        Block block = new Block(null);

        for (int i = 0; i < 5; i++) {
            blockChain.add(block);
            block = new Block(block);
        }
        return Collections.unmodifiableList(blockChain);
    }
}
