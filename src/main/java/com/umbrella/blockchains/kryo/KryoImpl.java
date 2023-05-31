package com.umbrella.blockchains.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.umbrella.blockchains.blockchain.Block;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 The KryoImpl class provides an implementation of the Kryo serialization library for serializing and deserializing
 Block objects and List<Block> instances.
 */
public class KryoImpl {
    private static File file = new File("src/main/resources/data.bin");
    private static Kryo kryoInstance;

    private KryoImpl() {}

    /**
     Retrieves an instance of the Kryo serializer. If no instance exists, a new instance of Kryo is created.
     The method registers the Block class and List class with the Kryo instance and returns it.
     @return The Kryo instance with registered classes.
     */
    public static Kryo runKryo() {
        Kryo kryo = kryoInstance == null ? new Kryo() : kryoInstance;
        kryo.register(Block.class);
        kryo.register(ArrayList.class);
        return kryo;
    }

    /**
     Serializes the provided List<Block> object using the given Kryo instance.
     The serialized data is written to a file specified by file.
     @param kryo The Kryo instance for serialization.
     @param li The List<Block> object to be serialized.
     @throws RuntimeException If an IOException occurs during the serialization process.
     */
    public static void writeKryo(Kryo kryo, List<Block> li) {
        try (Output fos = new Output(new FileOutputStream(file))) {
            kryo.writeObject(fos, li);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     Deserializes the List object from the file specified by file using the provided Kryo instance.
     @param kryo The Kryo instance for deserialization.
     @return The deserialized ArrayList<Block> object.
     @throws RuntimeException If the file is not found or an error occurs during deserialization.
     */
    public static List readKryo(Kryo kryo) {
        try (Input fis = new Input(new FileInputStream(file))) {
            return kryo.readObject(fis, ArrayList.class);

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
