package top.yzlin.douyinquery;

import top.yzlin.Start;
import top.yzlin.tools.Tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Stream;

public class ConfigLoading {
    public static ConfigLoading getInstance() {
        return configLoading;
    }
    private static ConfigLoading configLoading=new ConfigLoading();

    private Properties configProperties;
    private Properties memberProperties;

    private SimpleDateFormat simpleDateFormat;

    private ConfigLoading(){
        configProperties=new Properties();
        memberProperties=new Properties();
        try {
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

    public String getConfigProperties(String key){
        return configProperties.getProperty(key);
    }

    public String getConfigProperties(String key,String defaultValue){
        return configProperties.getProperty(key,defaultValue);
    }

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

    public SimpleDateFormat getDateFormat(){
        return simpleDateFormat;
    }


    public String getMemberUserID(String memberName){
        return memberProperties.getProperty(memberName);
    }

    public Set<String> getMemberSet(){
        return memberProperties.stringPropertyNames();
    }
}
