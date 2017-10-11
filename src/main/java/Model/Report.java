package Model;

import java.util.List;

public class Report {
    public static final int COLUMN_SIZE = 11;
    private int reportNum;
    private List<String> status;
    private String product;
    private String component;
    private String reported;
    private String modified;
    private String hardware;
    private String importance;
    private List<Integer> duplicates;
    private String desc;
    private String assignTo;

    public static String getColumnName(String splitToken){
        StringBuilder formatString = new StringBuilder();
        formatString.append("reportNum").append(splitToken);
        formatString.append("status").append(splitToken);
        formatString.append("product").append(splitToken);
        formatString.append("component").append(splitToken);
        formatString.append("reported").append(splitToken);
        formatString.append("modified").append(splitToken);
        formatString.append("hardware").append(splitToken);
        formatString.append("importance").append(splitToken);
        formatString.append("duplicates").append(splitToken);
        formatString.append("desc").append(splitToken);
        formatString.append("assignTo");

        return formatString.toString();
    }

    public String getFormattedData(String splitToken,String multiSplitToken){
        StringBuilder formatString = new StringBuilder();

        if(reportNum == 0)formatString.append("noReportNum").append(splitToken);
        else formatString.append(reportNum).append(splitToken);

        if(status == null || status.size() == 0){
            formatString.append(" ").append(splitToken);
        }else {
            for(int i = 0 ; i <status.size();i++){
                if (i == status.size()-1)formatString.append(status.get(i));
                else formatString.append(status.get(i)).append(multiSplitToken);
            }
            formatString.append(splitToken);
        }

        if(product==null || product.isEmpty())formatString.append(" ").append(splitToken);
        else formatString.append(product).append(splitToken);

        if(component==null || component.isEmpty())formatString.append(" ").append(splitToken);
        else formatString.append(component).append(splitToken);

        if(reported==null || reported.isEmpty())formatString.append(" ").append(splitToken);
        else formatString.append(reported).append(splitToken);

        if(modified==null || modified.isEmpty())formatString.append(" ").append(splitToken);
        else formatString.append(modified).append(splitToken);

        if(hardware==null || hardware.isEmpty())formatString.append(" ").append(splitToken);
        else formatString.append(hardware).append(splitToken);

        if(importance==null || importance.isEmpty())formatString.append(" ").append(splitToken);
        else formatString.append(importance).append(splitToken);

        if(duplicates ==null || duplicates.size() == 0)formatString.append("noDuplicate").append(splitToken);
        else{
            for(int i = 0 ; i <duplicates.size();i++){
                if (i == duplicates.size()-1)formatString.append(duplicates.get(i));
                else formatString.append(duplicates.get(i)).append(multiSplitToken);
            }
            formatString.append(splitToken);
        }

        if(desc==null || desc.isEmpty())formatString.append(" ").append(splitToken);
        else formatString.append(desc).append(splitToken);

        if(assignTo==null || assignTo.isEmpty())formatString.append(" ");
        else formatString.append(assignTo);

        return formatString.toString();
    }

    public List<Integer> getDuplicates() {
        return duplicates;
    }

    public void setDuplicates(List<Integer> duplicates) {
        this.duplicates = duplicates;
    }

    public int getReportNum() {
        return reportNum;
    }

    public void setReportNum(int reportNum) {
        this.reportNum = reportNum;
    }

    public List<String> getStatus() {
        return status;
    }

    public void setStatus(List<String> status) {
        this.status = status;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getReported() {
        return reported;
    }

    public void setReported(String reported) {
        this.reported = reported;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public String getHardware() {
        return hardware;
    }

    public void setHardware(String hardware) {
        this.hardware = hardware;
    }

    public String getImportance() {
        return importance;
    }

    public void setImportance(String importance) {
        this.importance = importance;
    }

    public String getAssignTo() {
        return assignTo;
    }

    public void setAssignTo(String assignTo) {
        this.assignTo = assignTo;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

}
