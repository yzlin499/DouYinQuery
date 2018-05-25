package top.yzlin.douyinquery;

import java.util.Date;

public class PrintInfo implements DouYinFunction {
    private ConfigLoading configLoading=ConfigLoading.getInstance();
    @Override
    public boolean apply(DouYinInfo douYinInfo) {

        System.out.println("成员:"+douYinInfo.getMemberName()+"\t\t"+
                "标题:"+douYinInfo.getTitle()+"\t\t"+
                "时间:"+configLoading.getDateFormat().format(new Date(douYinInfo.getCreateTime())));

        return false;
    }
}
