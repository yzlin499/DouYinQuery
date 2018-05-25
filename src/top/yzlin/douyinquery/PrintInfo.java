package top.yzlin.douyinquery;

import java.util.Date;

public class PrintInfo implements DouYinFunction {
    private ConfigLoading configLoading=ConfigLoading.getInstance();

    public PrintInfo() {
        System.out.println("----------------------------------------");
        System.out.println("----------------------------------------");
    }

    @Override
    public void apply(DouYinInfo douYinInfo) {
        System.out.println("成员:"+douYinInfo.getMemberName()+"\t\t"+
                "标题:"+douYinInfo.getTitle()+"\t\t"+
                "时间:"+configLoading.getDateFormat().format(new Date(douYinInfo.getCreateTime()))+'\n'+
                "图片地址:"+douYinInfo.getCoverUrl()+'\n'+
                "视频地址:"+douYinInfo.getVideoUrl()+"\n"+
                "----------------------------------------"
        );
    }
}
