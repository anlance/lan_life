package club.anlan.lanlife.storage.image;

import java.io.InputStream;

/**
 * 图片处理 handler
 *
 * @author lan
 * @version 1.0
 * @date 2021/3/28 11:55
 */
public interface ImageHandler {

    /**
     * 获取图片类型
     */
    String getImageType();

    /**
     * 图片上传
     * @param inputStream 输入流
     * @param fileName 图片名
     * @return
     */
    String imageUpload(InputStream inputStream, String fileName);

    /**
     * 字节流图片上传
     * @param bytes 字节数据
     * @param fileName 文件名
     * @return
     */
    String imageUpload(byte[] bytes, String fileName);

    /**
     * 图片删除
     * @param fileId 文件id
     * @return
     */
    boolean imageDelete(String fileId);

    /**
     * 图片访问url地址
     */
    public String getImagePrefix();

    /**
     * 下载图片
     * @param fileId 图片id
     * @return
     */
    public InputStream imageDownload(String fileId);

    /**
     * 下载图片
     * @param fileId 图片id
     * @return
     */
    public byte[] imageDownloadByte(String fileId);


    /**
     * 获取图片完整URL
     * @param fileId 图片Id
     * @return
     */
    public String getImageUrl(String fileId);
}
