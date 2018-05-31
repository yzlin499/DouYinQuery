package top.yzlin.douyinquery;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class VideoDownLand implements DouYinFunction {
    private static final ConfigLoading configLoading=ConfigLoading.getInstance();
    private String runSentence;
    private Runtime runtime=Runtime.getRuntime();
    private boolean isOpen;
    private String nameFormat;

    public VideoDownLand(){
        String ffmpegPath=configLoading.getConfigProperties("ffmpegPath","");
        if("FFMPEG".equals(ffmpegPath.toUpperCase())){
            isOpen=true;
        }else{
            isOpen=new File(ffmpegPath).exists();
            if(!isOpen){
                return;
            }
        }
        String ffmpegSentence=configLoading.getConfigProperties("ffmpegSentence",
                "-i \"%s\"  -acodec copy -vcodec copy -f mp4 \"%s.mp4\"");
        runSentence="\""+ffmpegPath+"\" "+ffmpegSentence;
        nameFormat=configLoading.getConfigProperties("videoFileName","[name]-[title]");
    }
    @Override
    public void apply(DouYinInfo douYinInfo) {
        if(isOpen) {
            try {
                String name=nameFormat.replace("[name]",douYinInfo.getMemberName())
                        .replace("[title]",douYinInfo.getTitle())
                        .replace("[time]",configLoading.getDateFormat().format(new Date(douYinInfo.getCreateTime())));
                runtime.exec(String.format(runSentence, douYinInfo.getVideoUrl(), name));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            System.out.println("插件启动失败");
        }
    }




}
