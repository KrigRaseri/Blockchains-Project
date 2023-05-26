package com.umbrella.blockchains;

import com.esotericsoftware.kryo.Kryo;
import com.umbrella.blockchains.blockchain.Block;
import com.umbrella.blockchains.blockchain.BlockChain;
import com.umbrella.blockchains.kryo.KryoImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class BlockChainMain {
    public static void main(String[] args) {
        ArrayList<Block> blockChain = BlockChain.createBlockChain();
        blockChain.stream().map(Block::toString).forEach(System.out::println);

        Kryo kryo = KryoImpl.runKryo();
        blockChain.forEach(b -> KryoImpl.writeKryo(kryo, blockChain));
        KryoImpl.readKryo(kryo).forEach(System.out::println);
    }
}
