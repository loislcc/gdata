package edu.buaa.web.rest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

import static edu.buaa.domain.Constants.ca;
import static edu.buaa.domain.Constants.dis;

public class Main {
    public static void main(String args[]){
        //创建datanode集群
        Random r = new Random();
        ArrayList<DataNode> dataNodes = new ArrayList<>();
//        int[] dis= {1,2,3,4,5,6,7,8,9,10,
//                        10,9,8,7,6,5,4,3,2,1};
//        int[] ca= {1024,1024,1024,1024,1024,1024,1024,1024,1024,1024,
//                521,512,512,512,512,512,512,512,512,512};
        for(int i=0;i<20;i++){
            int dist = r.nextInt(10)+1;
            DataNode dataNode = new DataNode(i,ca[i],0,0,dis[i]);
            dataNodes.add(dataNode);
        }
        Collections.sort(dataNodes, new Comparator<DataNode>() {
            @Override
            public int compare(DataNode dataNode, DataNode t1) {
                return dataNode.getDistance()-t1.getDistance();
            }
        });
        //创建Namenode
        NameNode nameNode = new NameNode(1);
        //分发数据,回收数据
//        nameNode.send(10,100,dataNodes,1);
//        output(dataNodes, nameNode);
//
//        nameNode.send(10,100,dataNodes,2);
//        output(dataNodes, nameNode);
        int num = 500;
        nameNode.send(10,num,dataNodes,5);
        output(dataNodes, nameNode);

        nameNode.send(10,num,dataNodes,4);
        output(dataNodes, nameNode);

        nameNode.send(10,num,dataNodes,6);
        output(dataNodes, nameNode);
    }

    private static void output(ArrayList<DataNode> dataNodes, NameNode nameNode) {
        analyze(dataNodes);
        nameNode.receive(dataNodes);
        System.out.println(nameNode.getSendTime());
        System.out.println(nameNode.getReceiveTime());
        nameNode.clc();
    }

    private static void analyze(ArrayList<DataNode> dataNodes){
        //System.out.println("datanodeId\tdistance");
        System.out.println();
        for(DataNode dataNode : dataNodes){
            System.out.println(//dataNode.getDatanodeID()+"\t"+
                    //dataNode.getDistance()+"\t"+
                    //dataNode.getUsed()+"\t");//+
                    //dataNode.getBlockNum());
                    dataNode.getkey());
        }
    }
}
