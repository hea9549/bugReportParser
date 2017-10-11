import org.junit.Test;

import static org.junit.Assert.*;

public class EclipseBugParserTest {
    EclipseBugParser instance = new EclipseBugParser.Builder()
            .setUsingThreadNum(20)
            .setParsingStartNum(410000)
            .setParsingDocNum(10000)
            .build();

    @Test
    public void parse() throws Exception {
        instance.parse();
    }

}