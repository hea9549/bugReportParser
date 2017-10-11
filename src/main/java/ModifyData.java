import Model.Report;
import Util.LogUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ModifyData {
    public static void main(String[] args) throws IOException {
        String filePath = "";
        String splitToken = "凸";
        String multiSplitToken = "-";
        DataManager dataManager = new DataManager();
        int beforeSize = 0;
        LogUtil log = new LogUtil("ModifyData");
        File file = new File("output/440000-449999result.txt");

        if (!file.exists()){
            log.e("err in read files");
            return;
        }

        BufferedReader br = new BufferedReader(new FileReader(file));
        String buffer = null;
        br.readLine();
        while((buffer = br.readLine()) != null){
            String[] splitted = buffer.split(splitToken);
            if (splitted.length!=Report.COLUMN_SIZE){
                log.w("not columnsize match, "+splitted[0]);
                continue;
            }
            String reportNum = splitted[0];
            String status = splitted[1];
            String product= splitted[2];
            String component= splitted[3];
            String reported= splitted[4];
            String modified= splitted[5];
            String hardware= splitted[6];
            String importance= splitted[7];
            String duplicates= splitted[8];
            String desc= splitted[9];
            String assignTo= splitted[10];

            Report report = new Report();
            report.setReportNum(Integer.valueOf(reportNum.trim()));

            List<String> statusList= new ArrayList<>();
            if (status.contains(multiSplitToken)){
                String[] multistatus = status.split(multiSplitToken);
                statusList.addAll(Arrays.asList(multistatus));
            }else{
                statusList.add(status.trim());
            }

            report.setStatus(statusList);

            report.setProduct(product);

            report.setComponent(component);

            report.setReported(reported);

            report.setModified(modified);

            report.setHardware(hardware);

            report.setImportance(importance);

            List<Integer> duplicateList = new ArrayList<>();
            try {
                if (duplicates.contains(multiSplitToken)) {
                    String[] multiDuplicates = status.split(multiSplitToken);

                    for (int i = 0; i < multiDuplicates.length; i++) {
                        duplicateList.add(Integer.valueOf(multiDuplicates[i].trim()));
                    }
                } else {
                    duplicateList.add(Integer.valueOf(duplicates.trim()));
                }
                report.setDuplicates(duplicateList);
            }catch (Exception ignored){
                duplicateList.add(-1);
            }
            report.setDuplicates(duplicateList);

            report.setDesc(desc);

            report.setAssignTo(assignTo);

            dataManager.addReport(report);
        }
        beforeSize = dataManager.getDatas().size();
        dataManager.removeUnderData(10);
        System.out.println("finish");
        File outputDirectory = new File("output");

        if (!outputDirectory.exists()){
            outputDirectory.mkdirs();
        }

        File file2 = new File("output/"+dataManager.getDatas().get(0).getReportNum()+"~"+dataManager.getDatas().get(dataManager.getDatas().size()-1).getReportNum()+"format.txt");
        BufferedWriter txtWriter = null;
        try{
            txtWriter = new BufferedWriter(new FileWriter(file2));
            txtWriter.write(Report.getColumnName(splitToken));
            txtWriter.newLine();

            for(int i = 0 ; i < dataManager.getDatas().size();i++){
                txtWriter.write(dataManager.getDatas().get(i).getFormattedData(splitToken,multiSplitToken));
                txtWriter.newLine();
            }

            txtWriter.flush();
        }catch (IOException e){
            log.e("error in save files, error cause = "+e.getMessage());
            e.printStackTrace();
        }
        System.out.println("before = "+beforeSize+", after = "+dataManager.getDatas().size());

    }
}
