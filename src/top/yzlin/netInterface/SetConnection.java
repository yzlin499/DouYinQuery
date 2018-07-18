package top.yzlin.netInterface;
import java.net.URLConnection;
/**
 * 用来设置请求的header
 * @author yzlin
 */
@FunctionalInterface
public interface SetConnection {
	void setConnection(URLConnection conn);
}