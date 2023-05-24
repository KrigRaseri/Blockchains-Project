package com.umbrella.blockchains;

import com.umbrella.blockchains.blockchain.Block;
import com.umbrella.blockchains.blockchain.BlockChain;

public class BlockChainMain {
    public static void main(String[] args) {
        BlockChain.createBlockChain().stream().map(Block::toString).forEach(System.out::println);
    }
}
