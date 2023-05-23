package com.umbrella.blockchains.blockchain;

import lombok.Data;

@Data
public class Block {
    private int id;
    private long timeStamp;
    private String prevHash;
    private String currHash;

    public Block(int id, long timeStamp, String prevHash) {
        this.id = id;
        this.timeStamp = timeStamp;
        this.prevHash = prevHash;
        this.currHash = BlockChainUtil.applySha256(prevHash + timeStamp + id);;
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
                """, id,timeStamp,prevHash,currHash);
    }
}
