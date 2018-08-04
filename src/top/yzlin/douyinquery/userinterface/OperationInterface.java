/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package top.yzlin.douyinquery.userinterface;

import top.yzlin.douyinquery.DataLoad;
import top.yzlin.douyinquery.DouYinFunction;
import top.yzlin.douyinquery.DouYinInfo;

import java.util.List;
import java.util.function.Function;

/**
 * @author yzlin
 */
public interface OperationInterface {

    void signDownloaded(Function<DataLoad, Void> function);

    List<DouYinInfo> getQueryRelust(String name);

    String[] getMembers();

    DouYinFunction[] getExpands();

}
