package com.umbrella.blockchains;

import com.umbrella.blockchains.blockchain.Block;
import com.umbrella.blockchains.blockchain.BlockChain;

import java.util.List;


public class BlockChainMain {
    public static void main(String[] args) {
        BlockChain blockChain = new BlockChain();
        List<Block> blockChainList = blockChain.createBlocks();
        blockChainList.stream().map(Block::toString).forEach(System.out::println);
    }
}
