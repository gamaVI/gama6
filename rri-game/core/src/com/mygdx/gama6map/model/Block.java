package com.mygdx.gama6map.model;

public class Block {
    private String timestamp; //later change to Date/Long
    private int index;
    private String lastHash;
    private String hash;
    private BlockData data;
    private long nonce;
    private int difficulty;
    private long totalHashCount;


    public static class BlockData {
        private String type;
        private String message;


        public String getMessage() {
            return message;
        }
    }


    public int getIndex() {
        return index;
    }

    public BlockData getData() {
        return data;
    }

}
