package top.yzlin.douyinquery;

/**
 * 抖音信息的javaBean
 */
public class DouYinInfo {
    /**
     * 抖音ID
     */
    private long douyinID;
    /**
     * 成员姓名
     */
    private String memberName;
    /**
     * 创建时间
     */
    private long createTime;
    /**
     * 标题
     */
    private String title;
    /**
     * 视频地址
     */
    private String videoUrl;
    /**
     * 封面地址
     */
    private String coverUrl;

    public long getDouyinID() {
        return douyinID;
    }

    public void setDouyinID(long douyinID) {
        this.douyinID = douyinID;
    }

    
    
    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    @Override
    public String toString() {
        return "DouYinInfo{" + "douyinID=" + douyinID + ", memberName=" + memberName + ", title=" + title + ", videoUrl=" + videoUrl + ", coverUrl=" + coverUrl + '}';
    }


}
