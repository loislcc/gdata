package edu.buaa.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import edu.buaa.domain.*;
import edu.buaa.repository.TaskRepository;
import edu.buaa.rsutils.jerasure.Decoder;
import edu.buaa.rsutils.jerasure.Encoder;
import edu.buaa.service.message.ToConsoleProducer;
import edu.buaa.web.rest.util.utils;
import io.swagger.models.auth.In;
import org.apache.kafka.common.protocol.types.Field;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.XMLWriter;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

import static edu.buaa.domain.Constants.ep;

/**
 * Service Implementation for managing {@link Task}.
 */
@Service
@Transactional
public class TaskService {

    private final Logger log = LoggerFactory.getLogger(TaskService.class);

    private final TaskRepository taskRepository;

    private final ToConsoleProducer toConsoleProducer;

    private final EdgeClient edgeClient;

    private final Edge2Client edge2Client;

    private final Edge3Client edge3Client;

    private final MaprelationService maprelationService;

    private final EsinfoService esinfoService;

    public TaskService(TaskRepository taskRepository, ToConsoleProducer toConsoleProducer,EdgeClient edgeClient,
                       Edge2Client edge2Client,Edge3Client edge3Client,MaprelationService maprelationService,
                       EsinfoService esinfoService) {
        this.taskRepository = taskRepository;
        this.toConsoleProducer = toConsoleProducer;
        this.edgeClient = edgeClient;
        this.edge2Client = edge2Client;
        this.edge3Client = edge3Client;
        this.maprelationService = maprelationService;
        this.esinfoService = esinfoService;
    }

//    public class TaskThread implements Runnable  {
//        private Task t;
//        private String type;
//        private int time;
//        private MultipartFile file;
//        private boolean waiting = false;
//
//
//        public TaskThread(Task t, String type, int time, MultipartFile file) {
//            this.t = t;
//            this.type = type;
//            this.time = time;
//            this.file = file;
//        }
//        public void run() {
//            System.out.println("执行任务线程...");
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            t.setStatus("running");
//            t.setRealtime(sdf.format(new Date()));
//            save(t);
//            System.out.println(new Timestamp(System.currentTimeMillis()).toString() + " " + t.getName() + " is running...");
//            executeTask(t, type, time, file,target,i,size);
//
//
//            if(taskresultList.size()!=0) {
////                for (int i = 0; i < 30; i++) {
////                    // 循环扫描target每一个设备
////                    try {
////                        Thread.sleep(1000);    //延时2秒
////                    } catch (Exception e) {
////
////                    }
////                    System.out.println("xunhuan"+ i);
////                    if(isPause()){
////                        t.setTskStatus(Constants.TASK_STATUS_PAUSE);
////                        commonDao.saveOrUpdate(t);
////
////                    }
////                }
//                for (int i = 0; i < taskresultList.size(); i++) {
//                    // 循环扫描target每一个设备
//                    if(isPause()){
//                        t.setTskStatus(Constants.TASK_STATUS_PAUSE);
//                        commonDao.saveOrUpdate(t);
//
//                    }
//                    String target = taskresultList.get(i).getTarget();
//                    int size = taskresultList.size();
//
//                }
//
//                // 所有设备扫描完成
//                t.setTimeEnd(sdf.format(new Date()));
//                try {
//                    Date date = sdf.parse(t.getTimeStart());
//                    Date date1 = sdf.parse(t.getTimeEnd());
//                    long  miao=(date1.getTime()-date.getTime())/1000;
//                    t.setDuration(String.valueOf(miao));
//                } catch (Exception e) {
//
//                }
//
//                t.setTskStatus(Constants.TASK_STATUS_SUCCESS);
//                t.setTskProgress("100%");
//                commonDao.saveOrUpdate(t);
//                System.out.println("任务成功");
//
//                // 基线任务后续处理
//                if(type.contains("baseline")){
//                    Document result = DocumentHelper.createDocument();
//                    Element root = result.addElement("RESULT");
//                    String fileName = t.getTskId() + ".xml";
//
//                    String ipstype = type.split("#")[1];
//
//                    List<BaselinePolicy> policyList = commonDao.getAll(BaselinePolicy.class,
//                        Restrictions.like("type", "/%/" + ipstype), null);
//
//
//
//                    List<Taskresult> taskresult = commonDao.getAll(Taskresult.class,Restrictions.eq("tskId",t.getTskId()),null);
//                    if(taskresultList.size()!=0) {
//                        for (int i = 0; i < taskresult.size(); i++) {
//                            // 循环扫描target每一个设备
//
//                            String ips = taskresult.get(i).getTarget();
//                            String role = "全部";
//                            String connectStr ;
//                            if(taskresult.get(i).getResult()==1) {
//                                connectStr = "yes";
//                            }else {
//                                connectStr = "no";
//                            }
//
//                            int sshFlag = 0;
//
//                            Element equipment = root.addElement("EQUIPMENT");
//                            equipment.addAttribute("ip", ips);
//                            equipment.addAttribute("type", ipstype);
//                            equipment.addAttribute("connect", connectStr);
//                            equipment.addAttribute("template", policyList.get(0).getName());
//                            equipment.addAttribute("role", role);
//                            // 原有脚本循环
//
//                            JSONArray datas = JSONArray.parseArray(taskresult.get(i).getDatas());
//                            //for (int i = 0; i < scriptCommandList.size(); ++i) {//遍历agent脚本
//                            for (int k = 0; k < datas.size(); k++) {
//                                JSONObject onedata = datas.getJSONObject(i);
//                                String iid = onedata.getString("script_id");
//                                Element checkItem = equipment.addElement("CHECKITEM");
//                                checkItem.addAttribute("id", iid);
//                                List<BaselineCheckItem> checkItems = commonDao.getAll(BaselineCheckItem.class,
//                                    Restrictions.eq("id", iid), null);
//                                checkItem.addAttribute("type", checkItems.get(0).getType());
//                                if (connectStr.equals("no") )
//                                    checkItem.addAttribute("collect", "no");
//                                else
//                                    checkItem.addAttribute("collect", "yes");
//                                checkItem.addText(datas.get(k).toString());
//                                //脚本循环结束
//                            }
//
//                            //设备循环结束
//
//                        }
//                    }
//
//                    try {
//                        Writer out = null;
//                        out = new PrintWriter(fileName, "utf-8");
//                        // 格式化
//                        XMLWriter writer = new XMLWriter(out);
//                        // 把document对象写到out流中。
//                        System.out.println("写入文件");
//                        writer.write(result);
//
//                        out.close();
//                        writer.close();
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//                    //将XML版本号改为1.1
//                    RandomAccessFile rf = null;
//                    try {
//                        rf = new RandomAccessFile(fileName, "rw");
//                        rf.seek(17);
//                        rf.writeBytes("1");
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }finally {
//                        try {
//                            rf.close();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
////                    generateOffReport(t.getTskId(),fileName);
//                }
//
//
//
//            } else {
//                t.setTimeEnd(sdf.format(new Date()));
//                try {
//                    Date date = sdf.parse(t.getTimeStart());
//                    Date date1 = sdf.parse(t.getTimeEnd());
//                    long  miao=(date1.getTime()-date.getTime())/1000;
//                    t.setDuration(String.valueOf(miao));
//                } catch (Exception e) {
//
//                }
//                t.setTskStatus(Constants.TASK_STATUS_FAIL);
//                commonDao.saveOrUpdate(t);
//                System.out.println("任务失败");
//            }
//
//        }
//        public void pause() {
//            if (waiting) { // 是挂起状态则直接返回
//                return;
//            }
//            synchronized (this) {
//                this.waiting = true;
//            }
//        }
//
//        public void resume() {
//            if (!waiting) { // 没有挂起则直接返回
//                return;
//            }
//            synchronized (this) {
//                this.waiting = false;
//                this.notifyAll();
//            }
//        }
//
//        boolean isPause(){
//            // 暂停任务
//            try {
//                synchronized (this){
//                    if (waiting) {
//                        this.wait();
//                        return true;
//                    }
//                    return false;
//                }
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//                return false;
//            }
//        }
//    }

    /**
     * Save a task.
     *
     * @param task the entity to save.
     * @return the persisted entity.
     */
    public Task save(Task task) {
        log.debug("Request to save Task : {}", task);
        return taskRepository.save(task);
    }

    /**
     * Get all the tasks.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Task> findAll(Pageable pageable) {
        log.debug("Request to get all Tasks");
        return taskRepository.findAll(pageable);
    }


    /**
     * Get one task by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Task> findOne(Long id) {
        log.debug("Request to get Task : {}", id);
        return taskRepository.findById(id);
    }

    /**
     * Delete the task by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Task : {}", id);
        taskRepository.deleteById(id);
    }

    public Optional<Task> findbyName(String name) {
        log.debug("Request to get Task : {}", name);
        return taskRepository.findByName(name);
    }

    public  synchronized Long executeTask(Task t, String path) {
        int k = Integer.parseInt(t.getDatanum());
        int m = Integer.parseInt(t.getChecknum());
        int w = Constants.jerasurew;
        File f  = new File(path);
        Encoder enc = new Encoder(k, m, w);
        enc.encode(f);
        toConsoleProducer.sendMsgToGatewayConsole(t.getName() + " encoding ......");
        return f.length();
    }

    public void sendEsFile(String path)  {
        File f = new File(path);
        MultipartFile mf = utils.getMulFile(f);
        edgeClient.PostFile(mf);
    }
    public void sendEsFile2(String path)  {
        File f = new File(path);
        MultipartFile mf = utils.getMulFile(f);
        edge2Client.PostFile(mf);
    }
    public void sendEsFile3(String path)  {
        File f = new File(path);
        MultipartFile mf = utils.getMulFile(f);
        edge3Client.PostFile(mf);
    }

    public int findRnode(SortedMap<Integer, String> map, int bound, HashMap<Integer, Integer> vnodecapacity) {
        int firstKey = 0;
        Iterator it = map.keySet().iterator();
        boolean flag = false;
        while (it.hasNext()) {
            //it.next()得到的是key，tm.get(key)得到obj
            firstKey = (int) it.next();
            int size = vnodecapacity.get(firstKey);
            if(size < bound) {
                log.debug("size < bound,{},,,{}",size,bound);
                vnodecapacity.put(firstKey,size+1);
                flag = true;
                break;
            }
            log.debug("size < bound,{},,,{}",size,bound);
        }
        if(!flag){
            log.debug("not found store first");
            firstKey = map.firstKey();
            vnodecapacity.put(firstKey,vnodecapacity.get(firstKey));
        }
        return firstKey;
    }


    public void getVirNode(String filename, int k, int m, String path)  {
        int time = 0;
        int MAX = 40;
        int MIN = 20;
        Random rand = new Random();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<String> res = new ArrayList<>();
        SortedMap virtualNodes = new TreeMap<Integer, String>();
        List<Maprelation> maprelationList = maprelationService.findAllbyStatus("up");
        HashMap<Integer, Integer> vnodecapacity = new HashMap<>();
        for(Maprelation maprelation:maprelationList) {
            log.debug("*****,{}",maprelation.toString());
            String virnode = maprelation.getVnode();
            Integer intvirnode = Integer.parseInt(virnode);
            String realnode = maprelation.getRnode();
            virtualNodes.putIfAbsent(intvirnode,realnode);
            vnodecapacity.put(intvirnode, 0);
        }
        double length = k+m;
        double aver = length/maprelationList.size();
        int bound = (int) (aver*(1+ep));

        for(int i=1;i<=k;i++){
            String name = generatePartName(filename, "k" ,i);
            int hashCode = getHashCode(name);          // 获取文件hashcode

            log.debug("!!!!hashcode,{}",hashCode);
            log.debug("!!!!virtualnodes,{}",virtualNodes);
            SortedMap subMap = virtualNodes.tailMap(hashCode);
            log.debug("!!!!subMap,{}",subMap);
            int firstKey;
            String rNode;

            if(subMap.size() == 0) {
                firstKey = findRnode(virtualNodes,bound,vnodecapacity);
                rNode = (String) virtualNodes.get(firstKey);

            } else {
                firstKey = findRnode(subMap,bound,vnodecapacity);
                rNode = (String) subMap.get(firstKey);
            }

            log.debug("!!!!,{}",rNode);

            String tmp = path.substring(0,path.lastIndexOf("/")+1);
            String newpath = tmp+name;
            // 发送文件
            if(rNode.equals("edge")){
                sendEsFile(newpath);

            } else {
                if(rNode.equals("edge2")){
                    sendEsFile2(newpath);
                } else {
                    if(rNode.equals("edge3")){
                        sendEsFile3(newpath);
                    }
                }
            }
            try {
                time =rand.nextInt(MAX - MIN + 1) + MIN; //
                Thread.sleep(time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String gap = String.valueOf(time);
            String msg = "生成 "+ name +" 成功，存储在 " + rNode +"  "+gap+"ms";
            toConsoleProducer.sendMsgToGatewayConsole(msg);
            for(int j=1;j<=m;j++){
                try {
                    time =rand.nextInt(MAX - MIN + 1) + MIN; //
                    Thread.sleep(time);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String stri = String.valueOf(j);
                gap = String.valueOf(time);
                String ms = rNode + " 生成中间块 " + name + "#"+stri+"  "+gap+"ms";
                toConsoleProducer.sendMsgToGatewayConsole(ms);
            }

            // 存储esinfo映射关系
            Esinfo esinfo = new Esinfo();
            esinfo.setName(name.split("\\.")[0]);
            esinfo.setVnode(String.valueOf(firstKey));
            esinfo.setPname(filename);
            esinfo.setRnode(rNode);
            esinfo.setDate(sdf.format(new Date()));
            esinfo.setType(name.split("\\.")[1]);
            esinfoService.save(esinfo);
        }
        for(int i=1;i<=m;i++){
            String name = generatePartName(filename, "m" ,i);
            int hashCode = getHashCode(name);         // 获取文件hashcode

            log.debug("!!!!hashcode,{}",hashCode);
            log.debug("!!!!virtualnodes,{}",virtualNodes);
            SortedMap subMap = virtualNodes.tailMap(hashCode);
            log.debug("!!!!subMap,{}",subMap);
            int firstKey;
            String rNode;

            if(subMap.size() == 0) {
                firstKey = findRnode(virtualNodes,bound,vnodecapacity);
                rNode = (String) virtualNodes.get(firstKey);

            } else {
                firstKey = findRnode(subMap,bound,vnodecapacity);
                rNode = (String) subMap.get(firstKey);
            }

            log.debug("!!!!,{}",rNode);

            String tmp = path.substring(0,path.lastIndexOf("/")+1);
            String newpath = tmp+name;
            // 发送文件
            if(rNode.equals("edge")){
                sendEsFile(newpath);

            } else {
                if(rNode.equals("edge2")){
                    sendEsFile2(newpath);
                } else {
                    if(rNode.equals("edge3")){
                        sendEsFile3(newpath);
                    }
                }
            }
            try {
                time  =rand.nextInt(MAX - MIN + 1) + MIN; //
                Thread.sleep(time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String gap = String.valueOf(time);
            String msg = "生成 "+ name +" 成功，存储在 " + rNode +"  "+gap+"ms";
            toConsoleProducer.sendMsgToGatewayConsole(msg);
            // 存储esinfo映射关系
            Esinfo esinfo = new Esinfo();
            esinfo.setName(name.split("\\.")[0]);
            esinfo.setVnode(String.valueOf(firstKey));
            esinfo.setPname(filename);
            esinfo.setRnode(rNode);
            esinfo.setType(name.split("\\.")[1]);
            esinfo.setDate(sdf.format(new Date()));
            esinfoService.save(esinfo);
        }
    }

    private String generatePartName(String fileName, String partSuffix, int num){
        String name = fileName;
        String ext = "txt";
        String format = ext.length() == 0 ? "%s_%s%02d%s" : "%s_%s%02d.%s";
        return String.format(format, name, partSuffix, num, ext);
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
