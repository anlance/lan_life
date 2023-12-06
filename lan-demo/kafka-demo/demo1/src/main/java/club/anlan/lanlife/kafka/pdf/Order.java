package club.anlan.lanlife.kafka.pdf;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 类
 *
 * @author lan
 * @version 1.0
 * @date 2023/8/18 23:57
 */
@Data
public class Order {

    private String stationName;
    List<Detail> detailList;

    public static Order valueOf(String name, List<Detail> list) {
        Order or = new Order();
        or.setStationName(name);
        or.setDetailList(list);
        return or;
    }

    @Data
    public static class Detail {
        private Integer num;
        private Date date;
        private String chargeNum;
        private String chargeMoney;

        public static Detail valueOf(Integer num, Date date, String chargeNum, String chargeMoney) {
            Detail detail = new Detail();
            detail.num = num;
            detail.date = date;
            detail.chargeMoney = chargeMoney;
            detail.chargeNum = chargeNum;
            return detail;
        }
    }
}
