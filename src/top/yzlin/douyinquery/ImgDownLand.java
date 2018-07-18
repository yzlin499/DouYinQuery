package top.yzlin.douyinquery;

import java.io.*;
import java.net.URL;
import java.util.Date;

/**
 * 图片下载插件
 */
public class ImgDownLand implements DouYinFunction{
    private static final ConfigLoading configLoading=ConfigLoading.getInstance();
    private String nameFormat;

    public ImgDownLand(){
        nameFormat=configLoading.getConfigProperties("videoFileName","[name]-[title]")+".jpg";
    }

    @Override
    public void apply(DouYinInfo douYinInfo) {
        new Thread(()->{
            String name=nameFormat.replace("[name]",douYinInfo.getMemberName())
                    .replace("[title]",douYinInfo.getTitle())
                    .replace("[time]",configLoading.getDateFormat().format(new Date(douYinInfo.getCreateTime())));
            try {
                FileOutputStream fos=new FileOutputStream(name);
                InputStream is=new URL(douYinInfo.getCoverUrl()).openStream();
                int t;
                while((t=is.read())!=-1){
                    fos.write(t);
                }
                is.close();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

    }
}
