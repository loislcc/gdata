package edu.buaa.service;


import edu.buaa.domain.Maprelation;
import edu.buaa.domain.Notification;
import edu.buaa.service.message.ToConsoleProducer;
import edu.buaa.web.rest.MaprelationResource;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
public class CheckEdge {

    private final MaprelationResource maprelationResource;

    private final ToConsoleProducer toConsoleProducer;

    public CheckEdge(MaprelationResource maprelationResource, ToConsoleProducer toConsoleProducer) {
        this.maprelationResource = maprelationResource;
        this.toConsoleProducer = toConsoleProducer;
    }

    //3.添加定时任务

    @Scheduled(cron = "0/5 * * * * ?")  //或直接指定时间间隔，例如：5秒//@Scheduled(fixedRate=5000)
    private void configureTasks() throws ParseException, URISyntaxException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        List<Maprelation> maprelationList = maprelationResource.getMaprelations();
        String pre = "";
        for(int i = 0; i < maprelationList.size();i++){
            Maprelation map = maprelationList.get(i);
            if(map.getStatus().equals("up")) {
                Date last = df.parse(map.getLastime());
                Date now = new Date();
                long diff = now.getTime() - last.getTime();
                if(diff > 15000) {
                    map.setStatus("down");
                    maprelationResource.updateMaprelation(map);
                    if(!map.getRnode().equals(pre)){
                        String mess = "*** " +map.getRnode()+" ***" + " is offline ";
                        toConsoleProducer.sendMsgToGatewayConsole(mess);
                        pre = map.getRnode();
                    }
                }
            }

        }
    }

}
