package com.nf.mvc.configuration;

import org.apache.commons.beanutils.BeanUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * <pre class="code">
 *
 * </pre>
 * <h3>参考资料</h3>
 * <a href="https://juejin.cn/post/7034489501284040711">yaml解析工具类</a>
 * <a href="https://www.baeldung.com/java-snake-yaml">用snakeYml库解析yml文件 </a>
 * @author cj
 */
public class YmlParser {

    /**
     * 读取的资源名
     */
    private String fileName ="application.yml";
    /**
     * 获取的对象
     */
    private Object temp;


    /**
     * 创建一个资源获取对象,默认获取resources下的application.yml文件
     */
    public YmlParser() {
        this.load();
    }

    /**
     * 创建一个资源获取对象,默认获取resources下的fileName文件
     * @param fileName
     */
    public YmlParser(String fileName) {
        this.fileName =fileName;
        this.load();
    }

    /**
     * 加载指定的文件
     */
    private YmlParser load() {
        Yaml yaml = new Yaml();
        InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream(this.fileName);
        this.temp= yaml.load(inputStream);
        return this;
    }



    /**
     * eg "zdc.config.list"
     * eg ""
     * @param prefix
     */
    public YmlParser prefix(String prefix){

        if(prefix==null || "".equals(prefix.trim())){
            return this;
        }
        //获取层级关系
        String[] keys = prefix.trim().split("\\.");
        for (String key : keys) {
            //判断数据类型
            if(this.temp instanceof Map){
                this.temp= ((Map) this.temp).get(key);
            }
            else if(this.temp instanceof List){
                if (isNumeric(key)) {
                    this.temp= ((List) this.temp).get(Integer.parseInt(key));
                }else{
                    throw new RuntimeException(String.format("当前层级类型为List,不能使用[%s]获取子集数据",key));
                }
            }else{
                throw new RuntimeException("暂时没有解析该类型或不支持再次解析");
            }
        }
        return this;
    }

    /**
     * 返回对象类型的数据,可能是List,Map,Obj
     * @return
     */
    public Object getObj(){
       return this.temp;
    }

    /**
     * 返回Map类型的数据
     * @return
     */
    public Map getMap()  {
     if(this.temp instanceof Map){
         return (Map)this.temp;
     }
     return null;
    }

    /**
     * 返回List类型的数据
     * @return
     */
    public List getList()  {
        if(this.temp instanceof List){
            return (List)this.temp;
        }
        return null;
    }

    /**
     * 返回对象类型数据,如果集成其他类库可以直接调用其他类库的map2bean方法 该处引用的是 commons-beanutils-core ,可以根据自己本地环境替换
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T getT(Class<T> clazz) throws Exception {
        T obj = clazz.newInstance();
        Map map = this.getMap();
        BeanUtils.populate(obj,map);
        return obj;
    }

    /**
     * 返回String类型的数据
     * @return
     */
    public String getString()  {
        return this.temp==null ? "":this.temp.toString();
    }

    /**
     * 返回Integer类型的数据
     * @return
     */
    public Integer getInteger()  {
        String string = getString();
       return string!=null? Integer.parseInt(string):null;
    }

//TODO 可以自定也解析其他类型



    /**
     * 判断是否是数字
     * @param cs
     * @return
     */
    public static boolean isNumeric(final CharSequence cs) {
        if (cs == null || cs.length() == 0) {
            return false;
        }
        final int sz = cs.length();
        for (int i = 0; i < sz; i++) {
            if (!Character.isDigit(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }
    
}