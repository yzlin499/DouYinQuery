package top.yzlin;

import top.yzlin.douyinquery.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.util.stream.Stream;

public class Start {
    private static final ConfigLoading configLoading=ConfigLoading.getInstance();
    private static final SimpleDateFormat dateFormat=configLoading.getDateFormat();
    public static void main(String[] args){
        String time;
        Scanner a=new Scanner(System.in);
        if (args.length>0){
            time=String.join(" ",args);
        }else{
            System.out.println("请输入要查询的时间：按照"+dateFormat.toPattern()+"的格式");
            time=a.nextLine();
        }
        long date;
        while(true) {
            try {
                date = dateFormat.parse(time).getTime();
                break;
            } catch (ParseException e) {
                System.out.println("时间格式错误，请重新输入");
                time=a.nextLine();
            }
        }
        long date2=date;//内部类的final生成的临时变量
        DouYinInfo[] douYinInfos=configLoading.getMemberSet().parallelStream()
                .flatMap(s-> Stream.of(new DouYin(s).getData(date2)))
                .toArray(DouYinInfo[]::new);
        for(DouYinFunction d:configLoading.getFunctions()){
            for(DouYinInfo i:douYinInfos){
                d.apply(i);
            }
        }
    }
}
