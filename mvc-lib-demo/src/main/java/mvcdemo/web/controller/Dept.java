package mvcdemo.web.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Dept {
    private int deptId;
    private String deptName;
    private Manager manager;
    private Game youxi;
}
