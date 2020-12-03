package edu.buaa.web.rest;

public class DataNode {
    private int datanodeID;
    private int capacity;//GB
    private int used;//GB
    private int blockNum;
    private int distance;//跳数
    private int recNum;

    public DataNode(int datanodeID, int capacity, int used, int blockNum, int distance) {
        this.datanodeID = datanodeID;
        this.capacity = capacity;
        this.used = used;
        this.blockNum = blockNum;
        this.distance = distance;
    }

    public void clc(){
        used = 0;
        blockNum = 0;
    }

    public double value(double key){
        return key*distance + (1-key)*used;
    }

    public double value2(double key){
        return (key)*(double)used/capacity+(1-key)*distance;
    }

    public void setUsed(int used) {
        this.used = used;
    }

    public void addUsed(int used) {
        this.used += used;
    }

    public void setBlockNum(int blockNum) {
        this.blockNum = blockNum;
    }

    public void addBlockNum(int blockNum) {
        this.blockNum += blockNum;
    }

    public void addRecNum(int recNum) {
        this.recNum += recNum;
    }

    public int getDatanodeID() {
        return datanodeID;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getUsed() {
        return used;
    }

    public int getBlockNum() {
        return blockNum;
    }

    public int getDistance() {
        return distance;
    }

    public double getkey(){
        return (double)used/capacity;
    }
}
