package com.umbrella.blockchains.blockchain;

import lombok.Data;

@Data
public class Block {
    private int id;
    private long timestamp;
    private String prevHash;
    private String currHash;

    public Block(int id, long timestamp, String prevHash, String currHash) {
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
                Hash of the previous block:
                %s
                Hash of the block:
                %s
                """, id,timestamp,prevHash,currHash);
    }
}
