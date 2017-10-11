import Model.Report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataManager {
    List<Report> datas;

    public DataManager() {
        datas = new ArrayList<>();
    }

    public void addReport(Report report) {
        this.datas.add(report);
    }

    public List<Report> getDatas() {
        return datas;
    }

    public void setDatas(List<Report> datas) {
        this.datas = datas;
    }

    public void removeUnderData(int underCount) {
        HashMap<String, Integer> map = new HashMap<>();
        for (int i = 0; i < datas.size(); i++) {
            String assign = datas.get(i).getAssignTo();
            Integer intData;
            if ((intData = map.get(assign)) == null) map.put(assign, 1);
            else map.put(assign, intData + 1);
        }

        for (int i = 0; i < datas.size(); i++) {
            String assign = datas.get(i).getAssignTo();
            Integer intData;
            if ((intData = map.get(assign)) == null) {
                datas.remove(i);
                i--;
            } else if (assign.equals("EPP Error Reports")) {
                datas.remove(i);
                i--;
            } else if (assign.equals("Project Inbox")){
                datas.remove(i);
                i--;
            } else{
                if (intData < underCount) {
                    datas.remove(i);
                    i--;
                }
            }
        }


    }
}
