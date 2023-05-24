package com.umbrella.blockchains.blockchain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class BlockChain {

    public List<Block> createBlocks() {
        try (Scanner sc = new Scanner(System.in)) {
            List<Block> blockChain = new ArrayList<>();
            Block block = new Block(null);
            String input = "0".repeat(sc.nextInt());

            for (int i = 0; i < 5; i++) {
                long start = System.currentTimeMillis();
                while (!block.getCurrHash().startsWith(input)) {
                    block.setMagicNumber(ThreadLocalRandom.current().nextInt(100, 9999998 + 1));
                    block.setCurrHash(BlockChainUtil.applySha256(BlockChainUtil.createHashString(block)));
                }
                long end = System.currentTimeMillis();

                block.setTimeTaken((int) ((end-start) / 1000.0));
                blockChain.add(block);
                block = new Block(block);
            }
            return Collections.unmodifiableList(blockChain);
        }
    }
}
