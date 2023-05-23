package com.umbrella.blockchains.block;

import lombok.Data;
import java.util.Date;

@Data
public class Block {
    private int id;
    private Date date;
    private byte prevHash;
    private byte currHash;

    public Block(int id, Date date, byte prevHash, byte currHash) {
        this.id = id;
        this.date = date;
        this.prevHash = prevHash;
        this.currHash = currHash;
    }
}
