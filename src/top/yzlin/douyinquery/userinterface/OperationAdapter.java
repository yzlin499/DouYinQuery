/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package top.yzlin.douyinquery.userinterface;

import top.yzlin.douyinquery.*;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author yzlin
 */
public class OperationAdapter implements OperationInterface {
    private static final ConfigLoading configLoading = ConfigLoading.getInstance();
    private final DataLoad dataLoad = DataLoad.getInstance();

    @Override
    public void signDownloaded(Function<DataLoad, Void> function) {
        function.apply(dataLoad);
        dataLoad.saveData();
    }

    @Override
    public List<DouYinInfo> getQueryRelust(String name) {
        if ("*".endsWith(name)) {
            return configLoading.getMemberList().parallelStream()
                    .flatMap(s -> Stream.of(new DouYin(s).getData()))
                    .collect(Collectors.toList());


        } else {
            DouYin d = new DouYin(name);
            return Arrays.asList(d.getData());
        }
    }

    @Override
    public String[] getMembers() {
        List<String> memberList = configLoading.getMemberList();
        String[] data = new String[memberList.size() + 1];
        data[0] = "全体成员";
        for (int i = 1; i < data.length; i++) {
            data[i] = memberList.get(i - 1);
        }
        return data;
    }

    @Override
    public DouYinFunction[] getExpands() {
        return configLoading.getFunctions();
    }

}
