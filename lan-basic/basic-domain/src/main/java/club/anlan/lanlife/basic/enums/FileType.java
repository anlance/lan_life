package club.anlan.lanlife.basic.enums;

/**
 * 文件大类
 *
 * @author lan
 * @version 1.0
 * @date 2021/5/12 21:26
 */
public enum FileType {
    /**
     * 默认
     */
    DEFAULT,

    /**
     * 文件夹
     */
    FOLDER,

    /**
     * 视频
     */
    VIDEO,

    /**
     * 音频
     */
    AUDIO,

    /**
     * pdf
     */
    PDF,

    /**
     * 压缩包
     */
    COMPRESS_FILE,

    /**
     * 图片
     */
    PICTURE,

    /**
     * doc
     */
    DOC,

    /**
     * ppt
     */
    PPT,

    /**
     * txt
     */
    TXT,

    /**
     * torrent
     */
    TORRENT,

    /**
     * 网页文件
     */
    WEB,

    /**
     * 代码文件
     */
    CODE
    ;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
