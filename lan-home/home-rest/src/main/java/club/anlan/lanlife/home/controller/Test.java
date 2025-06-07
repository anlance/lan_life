package club.anlan.lanlife.home.controller;

import java.util.ArrayList;
import java.util.List;

/**
 * 类
 *
 * @author lan
 * @version 1.0
 * @date 2025/2/20 9:41
 */
public class Test {
    public volatile int all = 30;

    public void sell(int number) {
        while (all >= 0) {
            synchronized (Test.class){
                if(all<=0){
                    return;
                }
                if (number == 2 && all % 2 != 0) {
                    return;
                }
                System.out.println(number + "卖了票" + all);
                all -= 1;
            }
        }
    }

    public static void main(String[] args) {
        Test t = new Test();
        List<Thread> list = new ArrayList<Thread>(4);
        for (int i = 1; i <= 4; i++) {
            int finalI = i;
            list.add(new Thread(() -> t.sell(finalI)));
        }
        for (Thread thread : list) {
            thread.start();
        }
    }
}
