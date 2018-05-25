package top.yzlin.netInterface;
import java.net.URLConnection;
/**
 * 
 * @author yzlin
 */
@FunctionalInterface
public interface SetConnection {
	void setConnection(URLConnection conn);
}