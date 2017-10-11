import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.IOException;

public class EclipseBugReportTest {

    @Test
    public void oneReportTest() throws IOException {
        System.out.println((char)27 + "[34mThis text would show up red" + (char)27 + "[0m");
        Document document = Jsoup.connect("https://bugs.eclipse.org/bugs/show_bug.cgi?id=1").get();
        Elements e = document.select("div").select(".bz_alias_short_desc_container").select("a");
        if (e.size() == 0){
            System.out.println("file is null");
            return;
        }
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
        Elements re = rightElements.select(".field_label");
        if(status.size()!=0)System.out.println("status :"+status.get(0).text());
        if (product.size()!=0)System.out.println("product :"+product.get(0).text());
        if(component.size()!=0)System.out.println("component :"+component.get(0).text());
        if (hardWare.size()!=0)System.out.println("hardWare :"+hardWare.get(0).parent().text());
        if (importance.size()!=0)System.out.println("importance :"+importance.get(0).parent().parent().parent().text());
        if(assign.size()!=0)System.out.println("assign :"+assign.get(0).parent().parent().text());
        if(desc.size()!=0)System.out.println("desc :"+desc.get(0).text());
        System.out.println("reported : "+re.get(0).parent().text().split(" ")[1]);
        System.out.println("modified : "+re.get(1).parent().text().split(" ")[1]);
        System.out.println(duplicates.get(0).text());
    }

    @Test
    public void getRealAssginTest() throws IOException {
        Document document = Jsoup.connect("https://bugs.eclipse.org/bugs/show_activity.cgi?id=150847").get();
        Elements e = document.select("div").select("#bugzilla-body").select("table");
        System.out.println();
        String[] tokens;
        tokens = e.text().split(" ");
        for(int i = 0 ; i < tokens.length;i++){
            
        }

    }
}
