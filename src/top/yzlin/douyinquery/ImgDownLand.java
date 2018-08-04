package top.yzlin.douyinquery;

import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * 图片下载插件
 */
public class ImgDownLand implements DouYinFunction{
    private static final ConfigLoading configLoading=ConfigLoading.getInstance();
    private String nameFormat;

    public ImgDownLand(){
        nameFormat = configLoading.getConfigProperties("videoFileName", "[name]-[title]");
    }

    @Override
    public void apply(DouYinInfo douYinInfo) {
        JFileChooser jFileChooser1 = new JFileChooser() {
            @Override
            public void approveSelection() {
                String path = this.getSelectedFile().getAbsolutePath();
                int point = path.lastIndexOf('.');
                if (point <= -1 || !"JPG".equals(path.substring(point + 1).toUpperCase())) {
                    path += ".jpg";
                }
                File f = new File(path);
                new Thread(() -> {
                    try {
                        FileOutputStream fos = new FileOutputStream(f);
                        InputStream is = new URL(douYinInfo.getCoverUrl()).openStream();
                        int t;
                        while ((t = is.read()) != -1) {
                            fos.write(t);
                        }
                        is.close();
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();
                cancelSelection();
            }
        };
        jFileChooser1.setDialogType(JFileChooser.SAVE_DIALOG);
        jFileChooser1.setApproveButtonText("保存");
        jFileChooser1.setSelectedFile(
                new File(nameFormat.replace("[name]", douYinInfo.getMemberName())
                        .replace("[title]", douYinInfo.getTitle())));
        jFileChooser1.showDialog(new JLabel(), "保存图片");
    }

    @Override
    public String expandName() {
        return "封面另存为";
    }
}
