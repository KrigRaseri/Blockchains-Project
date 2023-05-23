package com.umbrella.blockchains.blockchain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class BlockChain {

    public List<Block> createBlocks() {
        List<Block> blockChain = new ArrayList<>();
        Block block;
        String hashOfPrevBlock = "0";

        for (int i = 1; i <= 5; i++) {
            block = new Block(i, new Date().getTime(), hashOfPrevBlock);
            blockChain.add(block);
            hashOfPrevBlock = block.getCurrHash();
        }
        return Collections.unmodifiableList(blockChain);
    }
}
