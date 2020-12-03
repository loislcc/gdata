package edu.buaa.web.rest;

import java.util.ArrayList;
import java.util.Random;

public class NameNode {
    private int namenodeID;
    private int sendTime;
    private int receiveTime;

    public NameNode(int namenodeID) {
        this.namenodeID = namenodeID;
        this.receiveTime = 0;
        this.sendTime = 0;
    }

    public int getSendTime() {
        return sendTime;
    }

    public int getReceiveTime() {
        return receiveTime;
    }

    public void clc() {
        this.sendTime = 0;
        this.receiveTime = 0;
    }


    public void send(int persize, int num, ArrayList<DataNode> dataNodes, int mode) {
        switch (mode) {
            case 1: {
                sendmode1(persize, num, dataNodes);
                break;
            }
            case 2: {
                sendmode2(persize, num, dataNodes);
                break;
            }
            case 3: {
                sendmode3(persize, num, dataNodes);
                break;
            }
            case 4: {
                sendmode4(persize, num, dataNodes);
                break;
            }
            case 5: {
                sendmode5(persize, num, dataNodes);
                break;
            }
            case 6: {
                sendmode6(persize, num, dataNodes);
                break;
            }
        }
    }

    private void sendmode1(int persize, int num, ArrayList<DataNode> dataNodes){
        Random r = new Random();
        int choose;
        for (int i = 0; i < num; i++) {
            int load = 0;
            choose = r.nextInt(dataNodes.size());
            while (dataNodes.get(choose).getUsed()+persize > dataNodes.get(choose).getCapacity()) {
                choose = r.nextInt(dataNodes.size());
            }
            choose = r.nextInt(dataNodes.size());
            dataNodes.get(choose).addBlockNum(1);
            dataNodes.get(choose).addUsed(persize);
            sendTime = sendTime + dataNodes.get(choose).getDistance();
        }
    }

    private void sendmode2(int persize, int num, ArrayList<DataNode> dataNodes) {
        Random r = new Random();
        int choose;

        for (int i = 0; i < num; i++) {
            int load = 0;
            int avgload = 0;
            for (int j = 0; j < dataNodes.size(); j++) {
                load += dataNodes.get(j).getUsed();
            }
            avgload = load / dataNodes.size();
            choose = r.nextInt(dataNodes.size());
            while (dataNodes.get(choose).getUsed() > avgload) {
                choose = r.nextInt(dataNodes.size());
            }
            dataNodes.get(choose).addBlockNum(1);
            dataNodes.get(choose).addUsed(persize);
            sendTime = sendTime + dataNodes.get(choose).getDistance();
        }
    }

    private void sendmode3(int persize, int num, ArrayList<DataNode> dataNodes) {
        int choose;

        for (int i = 0; i < num; i++) {
            choose = 0;
            for (int j = 1; j < dataNodes.size(); j++) {
                if (dataNodes.get(j).value(0.5) < dataNodes.get(choose).value(0.5)) {
                    choose = j;
                }
            }
            dataNodes.get(choose).addBlockNum(1);
            dataNodes.get(choose).addUsed(persize);
            sendTime = sendTime + dataNodes.get(choose).getDistance();
        }
    }

    //RS(4.2)
    private void sendmode4(int persize, int num, ArrayList<DataNode> dataNodes) {
        for (int i = 0; i < num; i = i + 4) {
            ArrayList<Integer> chosen = new ArrayList<>();
            for (int j = 0; j < 4; j++) {
                chosen.add(chooseone(persize, dataNodes, chosen, true));
            }
            for (int j = 0; j < 2; j++) {
                chosen.add(chooseone(persize, dataNodes, chosen, false));
            }
        }
    }

    private void sendmode6(int persize, int num, ArrayList<DataNode> dataNodes) {
        for (int i = 0; i < num; i = i + 4) {
            ArrayList<Integer> chosen = new ArrayList<>();
            for (int j = 0; j < 4; j++) {
                chosen.add(chooseone2(persize, dataNodes, chosen, true));
            }
            for (int j = 0; j < 2; j++) {
                chosen.add(chooseone2(persize, dataNodes, chosen, false));
            }
        }
    }
    private int chooseone2(int persize, ArrayList<DataNode> dataNodes, ArrayList<Integer> chosen, boolean isblock) {
        int choose = 0;
        while (chosen.contains(choose)) {
            choose = choose + 1;
        }
        for (int j = choose; j < dataNodes.size(); j++) {
            if (chosen.contains(j)) {
                continue;
            }
            if (dataNodes.get(j).value2(0.99) < dataNodes.get(choose).value2(0.99) && dataNodes.get(j).getUsed()+persize < dataNodes.get(j).getCapacity()) {
                choose = j;
            }
        }
        if (isblock)
            dataNodes.get(choose).addBlockNum(1);
        else
            dataNodes.get(choose).addRecNum(1);
        dataNodes.get(choose).addUsed(persize);
        sendTime = sendTime + dataNodes.get(choose).getDistance();
        return choose;
    }

    private int chooseone(int persize, ArrayList<DataNode> dataNodes, ArrayList<Integer> chosen, boolean isblock) {
        int choose = 0;
        while (chosen.contains(choose)) {
            choose = choose + 1;
        }
        for (int j = choose; j < dataNodes.size(); j++) {
            if (chosen.contains(j)) {
                continue;
            }
            if (dataNodes.get(j).value(0.5) < dataNodes.get(choose).value(0.5)) {
                choose = j;
            }
        }
        if (isblock)
            dataNodes.get(choose).addBlockNum(1);
        else
            dataNodes.get(choose).addRecNum(1);
        dataNodes.get(choose).addUsed(persize);
        sendTime = sendTime + dataNodes.get(choose).getDistance();
        return choose;
    }


    //使用3副本
    private void sendmode5(int persize, int num, ArrayList<DataNode> dataNodes) {
        int choose1;
        int choose2;
        int choose3;
        Random r = new Random();

        for (int i = 0; i < num; i++) {
            choose1 = r.nextInt(dataNodes.size());
            while (dataNodes.get(choose1).getUsed()+persize >= dataNodes.get(choose1).getCapacity()) {
                choose1 = r.nextInt(dataNodes.size());
            }
            dataNodes.get(choose1).addBlockNum(1);
            dataNodes.get(choose1).addUsed(persize);

            sendTime = sendTime + dataNodes.get(choose1).getDistance();

            choose2 = r.nextInt(dataNodes.size());
            while (dataNodes.get(choose2).getUsed()+persize >= dataNodes.get(choose2).getCapacity() || choose2 == choose1) {
                choose2 = r.nextInt(dataNodes.size());
            }
            dataNodes.get(choose2).addRecNum(1);


            dataNodes.get(choose2).addUsed(persize);
            sendTime = sendTime + dataNodes.get(choose2).getDistance();

            choose3 = r.nextInt(dataNodes.size());
            while (dataNodes.get(choose3).getUsed()+persize >= dataNodes.get(choose3).getCapacity() || choose3 == choose1 || choose2 == choose3) {
                choose3 = r.nextInt(dataNodes.size());
            }
            dataNodes.get(choose3).addRecNum(1);
            dataNodes.get(choose3).addUsed(persize);
            sendTime = sendTime + dataNodes.get(choose3).getDistance();
        }

    }

    private void sendmode7(int persize, int num, ArrayList<DataNode> dataNodes) {
        virtual(dataNodes);
        for (int i = 0; i < num; i = i + 4) {
            ArrayList<Integer> chosen = new ArrayList<>();
            for (int j = 0; j < 4; j++) {
                chosen.add(chooseone2(persize, dataNodes, chosen, true));
            }
            for (int j = 0; j < 2; j++) {
                chosen.add(chooseone2(persize, dataNodes, chosen, false));
            }
        }
    }

    int standard;
    private void virtual(ArrayList<DataNode> dataNodes) {
        ArrayList<DataNode> newdatanode = new ArrayList<>();
        for(DataNode node:dataNodes){
            int num = node.getCapacity()/standard;
            for(int i=0;i<num;i++){
                DataNode dataNode = new DataNode(node.getDatanodeID(),standard,0,0,node.getDistance());
                newdatanode.add(dataNode);
            }
        }
        dataNodes.clear();
        dataNodes.addAll(newdatanode);


    }

    public void receive(ArrayList<DataNode> dataNodes) {
        for (int i = 0; i < dataNodes.size(); i++) {
            receiveTime += dataNodes.get(i).getBlockNum() * dataNodes.get(i).getDistance();
            dataNodes.get(i).clc();
        }
    }

}
