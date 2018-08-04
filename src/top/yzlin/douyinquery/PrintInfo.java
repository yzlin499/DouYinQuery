package top.yzlin.douyinquery;

/**
 * 输出信息插件
 */
public class PrintInfo implements DouYinFunction {
    private ConfigLoading configLoading=ConfigLoading.getInstance();

    @Override
    public String expandName() {
        return "打印信息";
    }


    @Override
    public void apply(DouYinInfo douYinInfo) {
        System.out.println("----------------------------------------");
        System.out.println("成员:"+douYinInfo.getMemberName()+"\t\t"+
                "标题:"+douYinInfo.getTitle()+"\t\t"+
                "ID:" + douYinInfo.getDouyinID() + '\n' +
                "图片地址:"+douYinInfo.getCoverUrl()+'\n'+
                "视频地址:"+douYinInfo.getVideoUrl()+"\n"+
                "----------------------------------------"
        );
    }
}
