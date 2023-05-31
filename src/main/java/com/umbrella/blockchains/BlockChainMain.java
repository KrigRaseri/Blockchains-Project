package com.umbrella.blockchains;

import com.esotericsoftware.kryo.Kryo;
import com.umbrella.blockchains.blockchain.Block;
import com.umbrella.blockchains.blockchain.BlockChain;
import com.umbrella.blockchains.kryo.KryoImpl;

import java.util.List;

/**
 The main class for the blockchain project.
 This class initializes and manages the blockchain system.
 It provides the entry point for the application and contains the main method to start the execution.
 The commented out portion can be uncommented for the use of Kryo.

 1. The number of leading zero's to find in a hash is chosen by the user.
 2. A block is created and tasks that change the hash are submitted to the ExecutionService until the zeros are found.
 3. Once found the block will be set with the new hash, magic num, time taken, and thread number that found the result.
 4. The amount of zeros will then be shifted depending on time taken. 10 sec or less it goes up, 60+ it goes up.
 5. The block is added to an array list.
 6. Finally, once done creating blocks the array list is returned, and each block is printed out.
 7. If kryo is enabled then it will be serialized to data.bin.
 */
public class BlockChainMain {
    public static void main(String[] args) {
        BlockChain.createBlockChain().stream().map(Block::toString).forEach(System.out::println);

        /*
        List<Block> blockChain = BlockChain.createBlockChain();
        blockChain.stream().map(Block::toString).forEach(System.out::println);

        Kryo kryo = KryoImpl.runKryo();
        blockChain.forEach(b -> KryoImpl.writeKryo(kryo, blockChain));
        KryoImpl.readKryo(kryo).forEach(System.out::println);
        */
    }
}
