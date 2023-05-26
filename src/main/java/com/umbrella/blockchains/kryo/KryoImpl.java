package com.umbrella.blockchains.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.umbrella.blockchains.blockchain.Block;
import com.umbrella.blockchains.blockchain.BlockChain;

import java.io.*;
import java.util.ArrayList;

public class KryoImpl {
    private static File file = new File("src/main/resources/data.bin");

    public static Kryo runKryo() {
        Kryo kryo = new Kryo();
        kryo.register(Block.class);
        kryo.register(BlockChain.class);
        kryo.register(ArrayList.class);
        return kryo;
    }

    public static void writeKryo(Kryo kryo, ArrayList<Block> li) {
        try (Output fos = new Output(new FileOutputStream(file))) {
            kryo.writeObject(fos, li);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static ArrayList readKryo(Kryo kryo) {
        try (Input fis = new Input(new FileInputStream(file))) {
            return kryo.readObject(fis, ArrayList.class);

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
