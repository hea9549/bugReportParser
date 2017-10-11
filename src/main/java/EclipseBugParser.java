import Model.Report;
import Util.LogUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class EclipseBugParser {
    public String TAG = "EclipseParser";

    public static final int SAVE_EXCEL = 0;
    public static final int SAVE_TOKEN_TXT = 1;

    private static String DEF_SPLIT_TOKEN = "凸";
    private static int DEF_THREAD_NUM = 5;
    private static int DEF_PARSE_NUM = 100;
    private static int DEF_PARSE_START_NUM = 1;

    private int saveMethod = SAVE_TOKEN_TXT;
    private String saveSplitToken = DEF_SPLIT_TOKEN;
    private int usingThread = DEF_THREAD_NUM;
    private int parseStartNum = DEF_PARSE_START_NUM;
    private int parseNum = DEF_PARSE_NUM;
    private List<Report> reports;
    private List<Thread> parsingThread;
    private DataManager dataManager;
    private long startTime;

    private LogUtil log;

    private EclipseBugParser() {
        parsingThread = new ArrayList<>();
        reports = new ArrayList<>();
        log = new LogUtil(TAG);
        dataManager = new DataManager();
    }

    public void parse() {
        startTime = System.currentTimeMillis();
        log.i("Start Parsing...");
        log.i("===============================Parsing Information===================================");
        log.i(String.format("%-30s : %d", "Using Thread", usingThread));
        log.i(String.format("%-30s : %d", "Parsing Doc Start Num", parseStartNum));
        log.i(String.format("%-30s : %d", "Parsing Doc Num", parseNum));
        log.i(String.format("%-30s : %s", "Parsing Doc Range", "" + parseStartNum + "~" + (parseStartNum + parseNum - 1)));
        log.i("=====================================================================================");
        int splitParseNum = parseNum/usingThread;
        int remainNum = parseNum%usingThread;
        for (int i = 0; i < usingThread; i++) {
            WorkingThread workingThread;
            if( i == usingThread-1 ){
                workingThread = new WorkingThread(i,parseStartNum+(splitParseNum*i),parseStartNum+parseNum-1);
            }else{
                workingThread = new WorkingThread(i,parseStartNum+(splitParseNum*i),parseStartNum+(splitParseNum*(i+1))-1);
            }
            parsingThread.add(workingThread);
            workingThread.start();

        }

        //waiting for all thread
        for(int i = 0 ; i < parsingThread.size(); i++){
            try {
                parsingThread.get(i).join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        log.i("===============================Parsing Finish Information=============================");
        log.i(String.format("%-30s : %s", "Parsing Doc Range", "" + parseStartNum + "~" + (parseStartNum + parseNum - 1)));
        log.i(String.format("%-30s : %s", "Parsing Doc Range", "" + parseStartNum + "~" + (parseStartNum + parseNum - 1)));
        log.i(String.format("%-30s : %s", "Parsed Doc Num", "" + dataManager.getDatas().size()));
        log.i(String.format("%-30s : %s", "Ignore Doc Num", "" + (parseNum -dataManager.getDatas().size())));
        log.i(String.format("%-30s : %s", "runing time", "" + ((System.currentTimeMillis()-startTime)/1000)+"s "+((System.currentTimeMillis()-startTime)%1000) + "ms"));
        log.i("=====================================================================================");

        log.i("start to save in txt");
        //file save
        Collections.sort(dataManager.getDatas(), new Comparator<Report>() {
            @Override
            public int compare(Report o1, Report o2) {
                return o1.getReportNum()-o2.getReportNum();
            }
        });

        File outputDirectory = new File("output");

        if (!outputDirectory.exists()){
            outputDirectory.mkdirs();
        }

        File file = new File("output/"+parseStartNum+"-"+(parseStartNum+parseNum-1)+"result.txt");
        BufferedWriter txtWriter = null;
        try{
            txtWriter = new BufferedWriter(new FileWriter(file));
            txtWriter.write(Report.getColumnName(saveSplitToken));
            txtWriter.newLine();

            for(int i = 0 ; i < dataManager.getDatas().size();i++){
                txtWriter.write(dataManager.getDatas().get(i).getFormattedData(saveSplitToken,"-"));
                txtWriter.newLine();
            }

            txtWriter.flush();
        }catch (IOException e){
            log.e("error in save files, error cause = "+e.getMessage());
            e.printStackTrace();
        }



    }




    private class WorkingThread extends Thread {
        private int id = -1;
        private int startDocNum = 1;
        private int endDocNum = 1;
        public WorkingThread(int id, int startDocNum, int endDocNum){
            this.id = id;
            this.startDocNum = startDocNum;
            this.endDocNum = endDocNum;
        }

        @Override
        public void run() {
            log.i("id = "+id+" thread start to parsing");
            docLoop : for (int i = startDocNum;i<endDocNum+1;i++){
                //init doc object and report object
                Document document = null;
                Report report = new Report();
                List<String> exceptList = new ArrayList<>();
                // try IOException to get bug html
                try {
                    document = Jsoup.connect("https://bugs.eclipse.org/bugs/show_bug.cgi?id="+i).get();
                } catch (IOException e) {
                    log.e("id = "+id+"thread throw IOException in document num :"+i+", exception ="+e.getMessage());
                }

                // e size 0 mean no document
                Elements e = document.select("div").select(".bz_alias_short_desc_container").select("a");
                if (e.size() == 0){
                    log.w("id = "+id+"thread, document num :"+i+" has not bug document");
                    continue;
                }
                //set reportNum
                report.setReportNum(Integer.valueOf((e.get(0).text().split("Bug")[1]).substring(1)));

                Elements leftElements = document.select("#bz_show_bug_column_1");
                Elements rightElements = document.select("#bz_show_bug_column_2");
                Elements status = leftElements.select("#static_bug_status");
                Elements product = leftElements.select("#field_container_product");
                Elements component = leftElements.select("#field_container_component");

                Elements hardWare = leftElements.select("#field_label_rep_platform");
                Elements importance = leftElements.select("a[href*=page.cgi?id=fields.html#importance]");
                Elements assign = leftElements.select("a[href*=page.cgi?id=fields.html#assigned_to]");
                Elements desc = document.select("#c0").select(".bz_comment_text");
                Elements duplicates = leftElements.select("#duplicates");
                Elements dayInfo = rightElements.select(".field_label");

                // parse report status
                if(status.size()!=0){
                    String[] statusList = status.get(0).text().split(" ");
                    report.setStatus(new ArrayList<>());
                    for(int j = 0 ; j < statusList.length;j++){
                        report.getStatus().add(statusList[j]);
                    }
                }else exceptList.add("status");

                //parse product label
                if (product.size()!=0){
                    report.setProduct(product.get(0).text());
                }else exceptList.add("product");

                //parse component label
                if(component.size()!=0){
                    report.setComponent(component.get(0).text());
                }else exceptList.add("component");

                //parse hardware
                if (hardWare.size()!=0){
                    report.setHardware(hardWare.get(0).parent().text().split(":")[1].trim());
                }else exceptList.add("hardware");

                //parse importance
                if (importance.size()!=0){
                    report.setImportance(importance.get(0).parent().parent().parent().text().split(":")[1].trim());
                }else exceptList.add("importance");

                //parse assign
                if(assign.size()!=0){
                    report.setAssignTo(assign.get(0).parent().parent().text().split(":")[1].trim());
                    if (report.getAssignTo().isEmpty())exceptList.add("assignTo");
                }else exceptList.add("assignTo");

                //parse desc
                if(desc.size()!=0){
                    report.setDesc(desc.get(0).text().replaceAll("[\r\n]"," "));
                }else exceptList.add("desc");

                //parse duplicates
                if(duplicates.size()!=0){
                    String[] duplicatesList = duplicates.get(0).text().split(" ");
                    report.setDuplicates(new ArrayList<>());
                    for(int j = 0 ; j < duplicatesList.length;j++){
                        report.getDuplicates().add(Integer.valueOf(duplicatesList[j]));
                    }
                }else exceptList.add("duplicates");
                //parse dayInfo
                report.setReported(dayInfo.get(0).parent().text().split(" ")[1]);
                report.setModified(dayInfo.get(1).parent().text().split(" ")[1]);

                if(exceptList.size()!=0){
                    for(int j = 0 ;j<exceptList.size();j++){
                        if (exceptList.get(j).equals("assignTo")){
                            log.e("doc num "+i+" has not assignTo. skip to parse");
                            continue docLoop;
                        }
                    }
                    log.w("except parse list : ");
                }
                log.i("id = "+id+", docNum = "+i+" get report finish");

                dataManager.addReport(report);

            }

            log.i("id = "+id+" thread working finish");
        }
    }

    public static class Builder {
        private EclipseBugParser instance;

        public Builder() {
            instance = new EclipseBugParser();
        }

        public Builder setUsingThreadNum(int num) {
            if (num > 10) LogUtil.W("EclipseParserBuilder","more 10 thread is not recommend. your thread input is "+num);
            instance.usingThread = num;
            return this;
        }

        public Builder setParsingRange(int startDocNum, int parsingNum) {
            return setParsingStartNum(startDocNum).setParsingDocNum(parsingNum);
        }

        public Builder setParsingStartNum(int startDocNum) {
            instance.parseStartNum = startDocNum;
            return this;
        }

        public Builder setParsingDocNum(int parsingNum) {
            instance.parseNum = parsingNum;
            return this;
        }

        public Builder setTag(String tag) {
            instance.TAG = tag;
            return this;
        }

        public EclipseBugParser build() {
            return instance;
        }
    }
}
