package com.umbrella.blockchains.blockchain;

import lombok.Data;

import java.util.Date;

@Data
public class Block {
    private int id;
    private long timeStamp;
    private String prevHash;
    private String currHash;

    public Block(Block previousBlock) {
        this.id = previousBlock == null ? 1 : previousBlock.getId() + 1;
        this.timeStamp = new Date().getTime();
        this.prevHash = previousBlock == null ? "0" : previousBlock.getCurrHash();
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
