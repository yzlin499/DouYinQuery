package top.yzlin.netInterface;

import java.io.IOException;

/**
 * 愚蠢的，异常处理机制
 * 因为不想抛出异常所以使用了这种异常处理机制
 */
@FunctionalInterface
public interface IOExceptionSolution {
    /**
     * 在post的第4个参数，传入的是发生数据爆炸的位置的IOException
     * @param e 数据爆炸情况
     */
    void exceptionSolution(IOException e);
}
