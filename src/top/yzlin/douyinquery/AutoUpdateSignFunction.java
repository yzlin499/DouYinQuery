package top.yzlin.douyinquery;

import java.io.*;
import java.net.URISyntaxException;

public class AutoUpdateSignFunction implements GetSignFunction {
    private File f = null;
    private Runtime rt = Runtime.getRuntime();
    private String phantomjsPath = ConfigLoading.getInstance().getConfigProperties("phantomjsPath");

    public AutoUpdateSignFunction() {
        try {
            f = new File(DouYin.class.getResource("fuckSign.js").toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getSign(String userID) {
        try {
            Process p = rt.exec(new String[]{phantomjsPath, f.getAbsolutePath(), userID});
            InputStream is = p.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuilder result = new StringBuilder();
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
            in.close();
            is.close();
            p.waitFor();
            return result.toString();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return "";
    }
}
