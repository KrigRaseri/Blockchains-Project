package com.umbrella.blockchains.block;

import lombok.Data;
import java.util.Date;

@Data
public class Block {
    private int id;
    private Date timestamp;
    private byte prevHash;
    private byte currHash;

    public Block(int id, Date timestamp, byte prevHash, byte currHash) {
        this.id = id;
        this.timestamp = timestamp;
        this.prevHash = prevHash;
        this.currHash = currHash;
    }

    @Override
    public String toString() {
        return  String.format("""
                Block:
                Id: %d
                Timestamp: %s
                Hash of the previous block:\u0020
                %s
                Hash of the block:\u0020
                %s
                """, id,timestamp,prevHash,currHash);
    }
}
