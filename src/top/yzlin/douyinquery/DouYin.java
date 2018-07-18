package top.yzlin.douyinquery;

import net.sf.json.JSONObject;
import top.yzlin.tools.Tools;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.*;
import java.net.URISyntaxException;
import java.util.List;

/**
 * 该软件的使用方法
 * 由这个类来进行获取某个时间点之后的所有成员的最新抖音
 * 然后
 */
public class DouYin {
    static {
        try {
            //将算法文件给加载进来，因为我实在是看不懂这个js在干嘛
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("javascript");
            File f=new File(DouYin.class.getResource("fuckSign.js").toURI());
            InputStream is=new FileInputStream(f);
            Reader reader = new InputStreamReader(is,"UTF-8");
            engine.eval(reader);
            signInvoke = (Invocable) engine;
            reader.close();
            is.close();
        } catch (URISyntaxException | IOException | ScriptException e) {
            e.printStackTrace();
        }
    }

    private static final ConfigLoading configLoading=ConfigLoading.getInstance();
    private static Invocable signInvoke;//算法函数
    private String userID;//成员的ID
    private String memberName;//成员的姓名

    /**
     * 成员名字，按照名字来进行查找成员ID
     * @param memberName 成员名字
     */
    public DouYin(String memberName){
        this.memberName=memberName;
        userID=configLoading.getMemberUserID(memberName);
    }

    /**
     * 某个时间之后的成员抖音发布情况
     * @param lastTime 时间戳
     * @return 抖音信息实例
     */
    public DouYinInfo[] getData(long lastTime){
        String str=Tools.sendGet("https://www.douyin.com/aweme/v1/aweme/post/",
                "user_id="+userID+"&count=21&max_cursor=0&aid=1128&_signature="+getSign(userID));
        List<JSONObject> jsonObjectList=JSONObject.fromObject(str).getJSONArray("aweme_list");
        return jsonObjectList.stream()
                .map(j->{
                    DouYinInfo douYinInfo=new DouYinInfo();
                    douYinInfo.setMemberName(memberName);
                    douYinInfo.setTitle(j.getString("desc"));
                    douYinInfo.setCreateTime(j.getLong("create_time")*1000);
                    j=j.getJSONObject("video");
                    douYinInfo.setCoverUrl(j.getJSONObject("cover").getJSONArray("url_list").getString(0));
                    douYinInfo.setVideoUrl(j.getJSONObject("play_addr").getJSONArray("url_list").getString(0));
                    return douYinInfo;
                }).filter(d->d.getCreateTime()>=lastTime)
                .toArray(DouYinInfo[]::new);
    }

    /**
     * 封装一下算法
     * @param userID 成员的userID
     * @return 签名
     */
    private static String getSign(String userID){
        try {
            return signInvoke.invokeFunction("generateSignature", userID).toString();
        } catch (ScriptException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return "";
    }
}
