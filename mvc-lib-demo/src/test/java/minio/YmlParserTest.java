package minio;

import com.nf.mvc.configuration.YmlParser;
import mvcdemo.MyConfigurationProperties1;
import mvcdemo.MyConfigurationProperties2;

import java.io.InputStream;

public class YmlParserTest {
  public static void main(String[] args) throws Exception{
    testYmlParser();
  }

  private static void testYmlParser() throws Exception {
    InputStream inputStream = YmlParser.class
            .getClassLoader()
            .getResourceAsStream("application2.yml");
    if (inputStream != null) {
      YmlParser parser = YmlParser.getInstance();
      MyConfigurationProperties1 s1 = parser.parse("s1", MyConfigurationProperties1.class);
      System.out.println("s1 = " + s1);

      MyConfigurationProperties2 s2 = parser.parse("s2", MyConfigurationProperties2.class);
      System.out.println("s2 = " + s2);
    }

  }
}
