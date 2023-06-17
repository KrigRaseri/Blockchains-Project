package com.umbrella.blockchains;

import com.esotericsoftware.kryo.Kryo;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.umbrella.blockchains.blockchain.Block;
import com.umbrella.blockchains.blockchain.BlockChain;
import com.umbrella.blockchains.kryo.KryoImpl;
import com.umbrella.blockchains.messenger.GenerateKeys;

import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 The main class for the blockchain project.
 This class initializes and manages the blockchain system.
 It provides the entry point for the application and contains the main method to start the execution.
 The commented out portion can be uncommented for the use of Kryo.

 1. If not already, a public/private keyPair will be created in resources/KeyPair.
 2. A block is created and tasks that change the hash are submitted to the ExecutionService until the zeros are found.
 3. While mining random pre-made messages will be chosen then encrypted and "sent" with a signature. For the sake of demo
    it is written to resources/MyData/SignedData.txt.
 4. The message will be decrypted and the signature validated. If true it will be added to the chat logs.
 5. Once found the block will be set with the new hash, magic num, time taken, the thread number that found the result,
    and the entire chat logs that were recorded while mining.
 6. The amount of zeros will then be shifted depending on time taken. 10 sec or less it goes up, 60+ it goes up.
 7. The block is added to an array list.
 8. Finally, once done creating blocks the array list is returned, and each block is printed out.
 9. If kryo is enabled then it will be serialized to data.bin.
 */
public class BlockChainMain {
    public static void main(String[] args) {
        Injector injector = Guice.createInjector();
        BlockChain blockChain = injector.getInstance(BlockChain.class);

        GenerateKeys generateKeys;

        try {
            generateKeys = new GenerateKeys(1024);
            generateKeys.createKeys();

            generateKeys.writeKeysToFile("./src/main/resources/KeyPair/publicKey",
                    generateKeys.getPublicKey().getEncoded());

            generateKeys.writeKeysToFile("./src/main/resources/KeyPair/privateKey",
                    generateKeys.getPrivateKey().getEncoded());

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

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
