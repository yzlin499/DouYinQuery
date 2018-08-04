package top.yzlin.douyinquery;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 视频下载插件
 */
public class VideoDownLand implements DouYinFunction {
    private static final ConfigLoading configLoading=ConfigLoading.getInstance();
    private String runSentence;
    private Runtime runtime=Runtime.getRuntime();
    private boolean isOpen;
    private String nameFormat;

    public VideoDownLand(){
        runSentence = configLoading.getConfigProperties("ffmpegPath", "");
        isOpen = (!"".equals(runSentence)) && new File(runSentence).exists();
        nameFormat=configLoading.getConfigProperties("videoFileName","[name]-[title]");
    }

    @Override
    public String expandName() {
        return isOpen ? "下载视频" : null;
    }
    
    @Override
    public void apply(DouYinInfo douYinInfo) {
        if(isOpen) {
            JFileChooser jFileChooser1 = new JFileChooser() {
                @Override
                public void approveSelection() {
                    String path = this.getSelectedFile().getAbsolutePath() + ".mp4";
                    new Thread(() -> {
                        try {
                            String[] code = {
                                    runSentence,
                                    "-i",
                                    douYinInfo.getVideoUrl(),
                                    "-acodec", "copy", "-vcodec", "copy", "-f", "mp4",
                                    path
                            };
                            ProcessBuilder pb = new ProcessBuilder(code);
                            pb.redirectErrorStream(true);
                            InputStream is = pb.start().getInputStream();
                            while (is.read() != -1) ;
                        } catch (IOException ex) {
                            Logger.getLogger(WatchVideo.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }).start();
                    cancelSelection();
                }
            };
            jFileChooser1.setDialogType(JFileChooser.SAVE_DIALOG);
            jFileChooser1.setApproveButtonText("保存");
            jFileChooser1.setSelectedFile(new File(nameFormat.replace("[name]", douYinInfo.getMemberName())
                    .replace("[title]", douYinInfo.getTitle())));
            jFileChooser1.showDialog(new JLabel(), "下载视频");
        }else{
            System.out.println("插件启动失败");
        }
    }
}
