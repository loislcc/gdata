package edu.buaa.service.message;

import com.alibaba.fastjson.JSONObject;
import edu.buaa.domain.Maprelation;
import edu.buaa.domain.Notification;
import edu.buaa.web.rest.MaprelationResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class ShareNotiConsumer {
    private final Logger log = LoggerFactory.getLogger(ShareNotiConsumer.class);

    private final MaprelationResource maprelationResource;

    private final ToConsoleProducer toConsoleProducer;

    public ShareNotiConsumer(MaprelationResource maprelationResource, ToConsoleProducer toConsoleProducer) {
        this.maprelationResource = maprelationResource;
        this.toConsoleProducer = toConsoleProducer;
    }

    @StreamListener(ShareChannel.CHANNELIN)
    public void listen(Notification msg) throws URISyntaxException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            log.debug("listen Notification from edge device*: {}", msg.toString());
            if (msg.getType().equals("heart")) {  // 心跳信息，用于记录邻居
                List<Maprelation> maprelationList = maprelationResource.getMaprelations();
                boolean flag = false;
                String pre = "";
                for (Maprelation one: maprelationList) {
                    if(one.getRnode().equals(msg.getOwner())){
                        flag = true;
                        if(one.getStatus().equals("down")){
                            one.setStatus("up");                           // 更新状态
                            one.setLastime(df.format(new Date()));
                            maprelationResource.updateMaprelation(one);   // 更新时间
                            if(!one.getRnode().equals(pre)){
                                String mess = "*** " +msg.getOwner()+" ***" + " is online again";
                                toConsoleProducer.sendMsgToGatewayConsole(mess);
                                pre = one.getRnode();
                            }
                        }else {
                            one.setLastime(df.format(new Date()));
                            maprelationResource.updateMaprelation(one);   // 更新时间
                        }


                    }
                }
                if(!flag){
                    for(int i=0; i<4; i++) {
                        // 计算虚拟节点
                        String vitualNode = msg.getBody().split("!")[1] + msg.getOwner() + "vn" + i;  // body存的是ip
                        int hashCode = getHashCode(vitualNode);
                        Maprelation maprelation = new Maprelation();
                        maprelation.setRnode(msg.getOwner());
                        maprelation.setStatus("up");
                        maprelation.setVnode(String.valueOf(hashCode));   // 虚拟节点的hash值作为唯一标识
                        maprelation.setIp(msg.getBody().split("!")[1]);
                        maprelation.setLastime(df.format(new Date()));
                        maprelationResource.createMaprelation(maprelation);
                    }
                    String mess = "*** " +msg.getOwner()+" ***" + " is first online ";
                    toConsoleProducer.sendMsgToGatewayConsole(mess);
                }
            }

            // 处理其他类型信息

    }

    private static int getHashCode(String node) {
        final int p = 16777619;
        int hash = (int)2166136261L;
        for (int i = 0; i < node.length(); i++)
            hash = (hash ^ node.charAt(i)) * p;
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;
        // 如果算出来的值为负数则取其绝对值
        if (hash < 0)
            hash = Math.abs(hash);
        return hash;
    }

}
