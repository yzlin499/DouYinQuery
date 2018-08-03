package top.yzlin.douyinquery;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.*;
import java.net.URISyntaxException;

public class OldSignFunction implements GetSignFunction {
    private Invocable signInvoke;//算法函数

    public OldSignFunction() {
        try {
            //将算法文件给加载进来，因为我实在是看不懂这个js在干嘛
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("javascript");
            File f = new File(DouYin.class.getResource("oldFuckSign.js").toURI());
            InputStream is = new FileInputStream(f);
            Reader reader = new InputStreamReader(is, "UTF-8");
            engine.eval(reader);
            signInvoke = (Invocable) engine;
            reader.close();
            is.close();
        } catch (URISyntaxException | IOException | ScriptException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getSign(String userID) {
        try {
            return signInvoke.invokeFunction("generateSignature", userID).toString();
        } catch (ScriptException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return "";
    }
}
