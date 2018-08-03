package top.yzlin.douyinquery;

import com.alibaba.fastjson.JSONObject;
import top.yzlin.tools.Tools;

import java.util.List;

/**
 * 该软件的使用方法
 * 由这个类来进行获取某个时间点之后的所有成员的最新抖音
 * 然后
 */
public class DouYin {
    private static final ConfigLoading configLoading=ConfigLoading.getInstance();
    private static GetSignFunction getSignFunction = new OldSignFunction();
    private String userID;//成员的ID
    private String memberName;//成员的姓名
    private String memberDytk;

    /**
     * 成员名字，按照名字来进行查找成员ID
     * @param memberName 成员名字
     */
    public DouYin(String memberName){
        this.memberName=memberName;
        userID=configLoading.getMemberUserID(memberName);
        memberDytk = configLoading.getMemberDytk(memberName);
    }

    /**
     * 某个时间之后的成员抖音发布情况
     * @param lastTime 时间戳
     * @return 抖音信息实例
     */
    public DouYinInfo[] getData(long lastTime){
        String str = Tools.sendGet("https://www.amemv.com/aweme/v1/aweme/post/",
                "user_id=" + userID + "&count=21&max_cursor=0&aid=1128&_signature=" + getSign(userID) + "&dytk=" + memberDytk,
                conn -> {
                    conn.setRequestProperty("accept", "application/json");
                    conn.setRequestProperty("accept-language", "zh-CN,zh;q=0.9");
                    conn.setRequestProperty("user-agent", "Mozilla/5.0 (Linux; U; Android 2.2; en-us; Nexus One Build/FRF91) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1");
                });
        List<Object> jsonObjectList = JSONObject.parseObject(str).getJSONArray("aweme_list");
        return jsonObjectList.stream()
                .map(data -> {
                    JSONObject j = (JSONObject) data;
                    DouYinInfo douYinInfo=new DouYinInfo();
                    douYinInfo.setMemberName(memberName);
                    douYinInfo.setTitle(j.getString("share_desc"));
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
        return getSignFunction.getSign(userID);
    }
}
