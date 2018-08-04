package top.yzlin.douyinquery;

import java.io.*;
import java.util.Properties;

public class DataLoad {
    private static ConfigLoading configLoading = ConfigLoading.getInstance();

    private File recordFile = configLoading.configFile("record.properties");
    private Properties data = new Properties();


    public static DataLoad getInstance() {
        return instance;
    }

    private static final DataLoad instance = new DataLoad();

    private DataLoad() {
        try {
            FileReader f = new FileReader(recordFile);
            data.load(f);
            f.close();
        } catch (IOException e) {
            System.out.println("读取文档失败");
            System.out.println(e.getMessage());
        }
    }

    public void signDouYinID(String name, long id) {
        if (Long.parseLong(data.getProperty(name, "0")) < id) {
            data.setProperty(name, Long.toString(id));
        }
    }

    public long getSignID(String name) {
        return Long.parseLong(data.getProperty(name, "0"));
    }

    public void saveData() {
        try (Writer writer = new FileWriter(recordFile)) {
            data.store(writer, "");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
