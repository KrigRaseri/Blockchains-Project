package com.umbrella.blockchains.blockchain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class Block {
    private long minerNum;
    private int id;
    private long timeStamp;
    private int magicNumber;
    private String prevHash;
    private String currHash;
    private List<String> chatLogs;
    private int timeTaken;
    private String numOfZerosChange;

    public Block(Block previousBlock) {
        this.id = previousBlock == null ? 1 : previousBlock.getId() + 1;
        this.timeStamp = new Date().getTime();
        this.magicNumber = 0;
        this.prevHash = previousBlock == null ? "0" : previousBlock.getCurrHash();
        this.currHash = BlockChainUtil.applySha256(prevHash + timeStamp + id);
    }

    @Override
    public String toString() {
        return  String.format("""
                Block:
                Created by miner # %d
                Id: %d
                Timestamp: %s
                Magic number: %s
                Hash of the previous block:
                %s
                Hash of the block:
                %s
                Block data: %s
                Block was generating for %d seconds
                %s
                """, minerNum, id,timeStamp, magicNumber, prevHash, currHash
                ,chatBlock(chatLogs), timeTaken, numOfZerosChange);
    }


    private static String chatBlock(List<String> li) {
        StringBuilder chatLogs = new StringBuilder();

        if (li.isEmpty()) {
            chatLogs = new StringBuilder("no messages");
            return chatLogs.toString();
        }

        for (int i = 0; i < li.size(); i++) {
            chatLogs.append("\n" + li.get(i));

        }
        return chatLogs.toString();
    }
}
