package com.umbrella.blockchains;

import com.umbrella.blockchains.blockchain.Block;
import com.umbrella.blockchains.blockchain.BlockChain;

public class BlockChainMain {
    public static void main(String[] args) {
        BlockChain blockChain = new BlockChain();
        blockChain.createBlocks().stream().map(Block::toString).forEach(System.out::println);
    }
}
