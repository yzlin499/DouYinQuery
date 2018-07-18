package top.yzlin.douyinquery;

/**
 * 实现这个接口，并且在配置文件里面的functions写入类名，就可以启动该功能
 *
 */
public interface DouYinFunction {
    void apply(DouYinInfo douYinInfo);
}
