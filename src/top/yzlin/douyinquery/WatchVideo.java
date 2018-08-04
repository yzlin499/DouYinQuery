/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package top.yzlin.douyinquery;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author yzlin
 */
public class WatchVideo implements DouYinFunction {
    private static final ConfigLoading configLoading = ConfigLoading.getInstance();
    private String runSentence;
    private final Runtime runtime = Runtime.getRuntime();
    private final boolean isOpen;

    public WatchVideo() {
        runSentence = configLoading.getConfigProperties("playAppPath", "");
        isOpen = (!"".equals(runSentence)) && new File(runSentence).exists();
    }


    @Override
    public String expandName() {
        return isOpen ? "视频观看" : null;
    }


    @Override
    public void apply(DouYinInfo douYinInfo) {
        new Thread(() -> {
            try {
                String[] code = {runSentence, douYinInfo.getVideoUrl()};
                ProcessBuilder pb = new ProcessBuilder(code);
                pb.redirectErrorStream(true);
                InputStream is = pb.start().getInputStream();
                while (is.read() != -1) ;
            } catch (IOException ex) {
                Logger.getLogger(WatchVideo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }).start();
    }
}
