package club.anlan.lanlife.basic.domain;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * 用户定位数据解析结果
 *
 * @author lan
 * @version 1.0
 * @date 2021/9/12 22:23
 */
@Getter
@Setter
public class Result {

    private String status;

    private String info;

    @JSONField(name = "infocode")
    private String infoCode;

    @JSONField(name = "regeocode")
    private RegeoCode regeoCode;

    @Getter
    @Setter
    public static class RegeoCode {

        @JSONField(name = "addressComponent")
        private AddressComponent addressComponent;

        @JSONField(name = "formatted_address")
        private String formattedAddress;
    }

    @Getter
    @Setter
    public static class AddressComponent {

        private String city;

        @JSONField(name = "citycode")
        private String cityCode;

        private String province;

        @JSONField(name = "adcode")
        private String adCode;


        private String district;

        @JSONField(name = "towncode")
        private String townCode;

        private String country;

        @JSONField(name = "township")
        private String townShip;

        @JSONField(name = "streetNumber")
        private StreetNumber streetNumber;

    }

    @Getter
    @Setter
    public static class StreetNumber {

        private String number;

        private String street;
    }


    public static void main(String[] args) {
        String s = "{\"status\":\"1\",\"regeocode\":{\"addressComponent\":{\"city\":\"成都市\",\"province\":\"四川省\",\"adcode\":\"510112\",\"district\":\"龙泉驿区\",\"towncode\":\"510112002000\",\"streetNumber\":{\"number\":\"666号\",\"location\":\"104.195071,30.570489\",\"direction\":\"Center\",\"distance\":\"0\",\"street\":\"龙城大道\"},\"country\":\"中国\",\"township\":\"大面街道\",\"businessAreas\":[[]],\"building\":{\"name\":[],\"type\":[]},\"neighborhood\":{\"name\":\"中国水电·云立方\",\"type\":\"商务住宅;住宅区;住宅小区\"},\"citycode\":\"028\"},\"formatted_address\":\"四川省成都市龙泉驿区大面街道中国水电·云立方凯德卓锦万黛\"},\"info\":\"OK\",\"infocode\":\"10000\"}";
        Result result;
        result = (Result) JSON.parseObject(s, Result.class);
        System.out.println(JSON.toJSONString(result));
    }
}