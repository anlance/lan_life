package club.anlan.lanlife.basic.vo;

import club.anlan.lanlife.component.utils.StringUtil;
import lombok.Data;

/**
 * 盐
 *
 * @author lan
 * @version 1.0
 * @date 2022/4/5 14:08
 */
@Data
public class SaltVo {

    private static final Integer RANDOM_CODE_NUM = 10;

    /**
     * 盐
     */
    private String salt;

    /**
     * 随机数
     */
    private String randomCode;

    public static SaltVo valueOf(String salt, String randomCode) {
        SaltVo saltVo = new SaltVo();
        saltVo.setSalt(salt);
        saltVo.setRandomCode(randomCode);
        return saltVo;
    }


    public static SaltVo defaultVo(){
        SaltVo saltVo = new SaltVo();
        saltVo.setSalt(StringUtil.generateRandomChars(RANDOM_CODE_NUM));
        saltVo.setRandomCode(StringUtil.generateRandomChars(RANDOM_CODE_NUM));
        return saltVo;
    }
}
