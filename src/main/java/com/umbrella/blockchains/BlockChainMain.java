package com.umbrella.blockchains;

import com.esotericsoftware.kryo.Kryo;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.umbrella.blockchains.blockchain.Block;
import com.umbrella.blockchains.blockchain.BlockChain;
import com.umbrella.blockchains.kryo.KryoImpl;

import java.util.List;

/**
 The main class for the blockchain project.
 This class initializes and manages the blockchain system.
 It provides the entry point for the application and contains the main method to start the execution.
 The commented out portion can be uncommented for the use of Kryo.

 1. A block is created and tasks that change the hash are submitted to the ExecutionService until the zeros are found.
 2. Once found the block will be set with the new hash, magic num, time taken, and thread number that found the result.
 3. The amount of zeros will then be shifted depending on time taken. 10 sec or less it goes up, 60+ it goes up.
 4. The block is added to an array list.
 5. Finally, once done creating blocks the array list is returned, and each block is printed out.
 6. If kryo is enabled then it will be serialized to data.bin.
 */
public class BlockChainMain {
    public static void main(String[] args) {
        Injector injector = Guice.createInjector();
        BlockChain blockChain = injector.getInstance(BlockChain.class);

        blockChain.createBlockChain().stream().map(Block::toString).forEach(System.out::println);

        /*
        List<Block> blockChainList = blockChain.createBlockChain();
        blockChainList.stream().map(Block::toString).forEach(System.out::println);

        Kryo kryo = KryoImpl.runKryo();
        blockChainList.forEach(b -> KryoImpl.writeKryo(kryo, blockChainList));
        KryoImpl.readKryo(kryo).forEach(System.out::println);
        */
    }
}
