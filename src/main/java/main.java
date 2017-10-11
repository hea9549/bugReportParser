import java.io.IOException;

public class main {
    public static void main(String[] args) throws IOException {
        EclipseBugParser instance = new EclipseBugParser.Builder()
                .setUsingThreadNum(20)
                .setParsingStartNum(470000)
                .setParsingDocNum(10000)
                .build();
        instance.parse();
    }
}
