/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package top.yzlin.douyinquery;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * @author yzlin
 */
public class WatchImg implements DouYinFunction {
    private Desktop desktop = Desktop.getDesktop();

    @Override
    public String expandName() {
        return "查看封面";
    }

    @Override
    public void apply(DouYinInfo douYinInfo) {
        new Thread(() -> {
            try {
                File temp = File.createTempFile("tempImg", ".jpg");
                temp.deleteOnExit();
                FileOutputStream fos = new FileOutputStream(temp);
                InputStream is = new URL(douYinInfo.getCoverUrl()).openStream();
                int t;
                while ((t = is.read()) != -1) {
                    fos.write(t);
                }
                is.close();
                fos.close();
                desktop.open(temp);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

}
