package top.yzlin.douyinquery;

import top.yzlin.Start;
import top.yzlin.tools.Tools;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Stream;

/**
 * 配置加载
 * 加载进来各种插件
 * 加载进来成员名单
 */
public class ConfigLoading {
    //单例
    public static ConfigLoading getInstance() {
        return configLoading;
    }
    private static ConfigLoading configLoading=new ConfigLoading();

    private Properties configProperties;//配置文件
    private Properties memberProperties;//成员名单

    private SimpleDateFormat simpleDateFormat;

    private ConfigLoading(){
        configProperties=new Properties();
        memberProperties=new Properties();
        try {
            //加载进来两个配置文件
            File configPackage=new File(Start.class.getResource("../../config/").toURI());
            File memberList= new File(configPackage,"memberList.properties");
            File config = new File(configPackage,"config.properties");
            FileReader f=new FileReader(config);
            configProperties.load(f);
            f.close();
            f=new FileReader(memberList);
            memberProperties.load(f);
            f.close();
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
        init();
    }

    private void init(){
        simpleDateFormat=new SimpleDateFormat(configProperties.getProperty("dateFormat"));
    }

    /**
     * 按照关键字来获取配置，用于插件
     * @param key 关键字
     * @return 结果
     */
    public String getConfigProperties(String key){
        return configProperties.getProperty(key);
    }

    /**
     * 按照关键字来获取配置，用于插件
     * @param key 关键字
     * @param defaultValue 默认值
     * @return 结果
     */
    public String getConfigProperties(String key,String defaultValue){
        return configProperties.getProperty(key,defaultValue);
    }

    /**
     * 获取所有的插件的实例
     * @return 插件实例
     */
    public DouYinFunction[] getFunctions(){
        return Stream.of(configProperties.getProperty("functions","").split(";"))
                .map(c->{
                    try {
                        Class aClass=Class.forName(c);
                        if(DouYinFunction.class.isAssignableFrom(aClass)){
                            return aClass;
                        }
                    } catch (ClassNotFoundException e) {
                        Tools.print("不存在类");
                    }
                    return null;
                }).filter(Objects::nonNull)
                .map(c->{
                    try {
                        return c.newInstance();
                    } catch (InstantiationException | IllegalAccessException e) {
                        Tools.print(c.getName()+"插件加载失败");
                    }
                    return null;
                }).filter(Objects::nonNull)
                .toArray(DouYinFunction[]::new);
    }

    /**
     * 获取日期的格式
     * @return
     */
    public SimpleDateFormat getDateFormat(){
        return simpleDateFormat;
    }

    /**
     * 按照姓名来获取成员的userID
     * @param memberName 姓名
     * @return userID
     */
    public String getMemberUserID(String memberName){
        return memberProperties.getProperty(memberName);
    }

    /**
     * 获得成员的姓名集合
     * @return 成员姓名集合
     */
    public Set<String> getMemberSet(){
        return memberProperties.stringPropertyNames();
    }
}
