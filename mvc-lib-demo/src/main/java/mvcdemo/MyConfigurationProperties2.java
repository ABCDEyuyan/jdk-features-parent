package mvcdemo;

import com.nf.mvc.configuration.ConfigurationProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties("s2")
public class MyConfigurationProperties2 {
    private int id;
    private String name;

}
