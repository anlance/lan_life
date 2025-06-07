package club.anlan.lanlife.basic.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 用户定位数据
 *
 * @author lan
 * @version 1.0
 * @date 2021/9/12 22:23
 */
@Data
@TableName("basic.bs_user_location")
public class UserLocation {

    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 用户 id
     */
    @TableField("user_id")
    private String userId;

    /**
     * 精度
     */
    @TableField("longitude")
    private Double longitude;

    /**
     * 纬度
     */
    @TableField("latitude")
    private Double latitude;

    /**
     * 精度
     */
    @TableField("accuracy")
    private float accuracy;

    /**
     * 提供者
     */
    @TableField("provider")
    private String provider;

    /**
     * 星数
     */
    @TableField("satellites")
    private int satellites;


    /**
     * 国家
     */
    @TableField("country")
    private String country;

    /**
     * 结构化地址信息
     */
    @TableField("formatted_address")
    private String formattedAddress;

    /**
     * 坐标点所在省名称
     */
    @TableField("province")
    private String province;

    /**
     * 坐标点所在城市名称
     */
    @TableField("city")
    private String city;

    /**
     * 城市编码
     */
    @TableField("city_code")
    private String cityCode;

    /**
     * 地址所在的区
     */
    @TableField("district")
    private String district;

    /**
     * 行政区编码
     */
    @TableField("ad_code")
    private String adCode;

    /**
     * 乡镇街道编码
     */
    @TableField("town_code")
    private String townCode;

    /**
     * 坐标点所在乡镇/街道
     */
    @TableField("town_ship")
    private String townShip;

    /**
     * 门牌信息列表 街道名称
     */
    @TableField("street")
    private String street;

    /**
     * 门牌信息列表 门牌号
     */
    @TableField("number")
    private String number;


    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    @DateTimeFormat
    @JsonFormat
    private LocalDateTime updateTime;


    public void setLocationDetail(Result result) {
        if (result != null && "1".equals(result.getStatus())) {
            Result.RegeoCode regeoCode = result.getRegeoCode();
            if (regeoCode != null) {
                Result.AddressComponent addressComponent = regeoCode.getAddressComponent();
                if (addressComponent != null) {
                    this.country = addressComponent.getCountry();
                    this.province = addressComponent.getProvince();
                    this.city = addressComponent.getCity();
                    this.cityCode = addressComponent.getCityCode();
                    this.district = addressComponent.getDistrict();
                    this.adCode = addressComponent.getAdCode();
                    this.townCode = addressComponent.getTownCode();
                    this.townShip = addressComponent.getTownShip();
                    Result.StreetNumber streetNumber = addressComponent.getStreetNumber();
                    if (streetNumber != null) {
                        this.street = streetNumber.getStreet();
                        this.number = streetNumber.getNumber();
                    }
                }
                this.formattedAddress = regeoCode.getFormattedAddress();
            }
        }
    }
}
