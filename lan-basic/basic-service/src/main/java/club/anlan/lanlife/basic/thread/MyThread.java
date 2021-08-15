package club.anlan.lanlife.basic.thread;

import lombok.Data;

/**
 * 类
 *
 * @author lan
 * @version 1.0
 * @date 2021/5/16 16:51
 */
@Data
public class MyThread implements Runnable {

    private String taskId;

    private String index;

    @Override
    public void run() {
        Thread thread = new Thread();
    }
}
