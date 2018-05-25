package top.yzlin.douyinquery;

import net.sf.json.JSONObject;
import top.yzlin.tools.Tools;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public class DouYin {
    static {
        try {
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("javascript");
            FileReader reader = new FileReader(new File(DouYin.class.getResource("fuckSign.js").toURI()));
            engine.eval(reader);
            signInvoke = (Invocable) engine;
            reader.close();
        } catch (URISyntaxException | IOException | ScriptException e) {
            e.printStackTrace();
        }
    }

    private static final ConfigLoading configLoading=ConfigLoading.getInstance();
    private static Invocable signInvoke;
    private String userID;
    private String memberName;

    public DouYin(String memberName){
        this.memberName=memberName;
        userID=configLoading.getMemberUserID(memberName);
    }



    public static void main(String[] args) {
        DouYin douYin=new DouYin("蒋芸");
        Tools.printArrays(douYin.getData(1522318665));
    }

    private DouYinInfo[] getData(long lastTime){
        String str=Tools.sendGet("https://www.douyin.com/aweme/v1/aweme/post/",
                "user_id="+userID+"&count=21&max_cursor=0&aid=1128&_signature="+getSign(userID));
        List<JSONObject> jsonObjectList=JSONObject.fromObject(str).getJSONArray("aweme_list");
        return jsonObjectList.stream()
                .map(j->{
                    DouYinInfo douYinInfo=new DouYinInfo();
                    douYinInfo.setMemberName(memberName);
                    douYinInfo.setTitle(j.getString("desc"));
                    douYinInfo.setCreateTime(j.getLong("create_time"));
                    j=j.getJSONObject("video");
                    douYinInfo.setCoverUrl(j.getJSONObject("cover").getJSONArray("url_list").getString(0));
                    douYinInfo.setVideoUrl(j.getJSONObject("play_addr").getJSONArray("url_list").getString(0));
                    return douYinInfo;
                }).filter(d->d.getCreateTime()>=lastTime)
                .toArray(DouYinInfo[]::new);
    }

    private static String getSign(String userID){
        try {
            return signInvoke.invokeFunction("generateSignature", userID).toString();
        } catch (ScriptException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return "";
    }
}
