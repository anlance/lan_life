package club.anlan.lanlife.storage.file;

import java.io.InputStream;

/**
 * 文件 handler
 *
 * @author lan
 * @version 1.0
 * @date 2021/5/12 22:24
 */
public interface FileHandler {

    /**
     * 获取 handler 类型
     * @return
     */
    String getHandlerType();

    /**
     * 文件上传
     * @param inputStream 输入流
     * @param fileName 文件名
     * @return
     */
    String fileUpload(InputStream inputStream, String fileName);

    /**
     * 字节流文件上传
     * @param bytes 字节数据
     * @param fileName 文件名
     * @return
     */
    String fileUpload(byte[] bytes, String fileName);

    /**
     * 字节流大文件上传
     * @param bytes 字节数据
     * @param fileName 文件名
     * @return
     */
    String uploadAppenderFile(byte[] bytes, String fileName);

    /**
     * 大文件上传
     * @param inputStream 输入流
     * @param fileName 文件名
     * @return
     */
    String uploadAppenderFile(InputStream inputStream, String fileName);

    int modifyFile(String fileId, long historyUploadSize, byte[] bytes);


    /**
     * 文件删除
     * @param fileId 文件id
     * @return
     */
    boolean fileDelete(String fileId);

    /**
     * 文件访问url地址
     * @return
     */
    public String getFilePrefix();

    /**
     * 下载文件
     * @param fileId 文件id
     * @return
     */
    public InputStream fileDownload(String fileId);

    /**
     * 下载文件
     * @param fileId 文件id
     * @return
     */
    public byte[] fileDownloadByte(String fileId);


    /**
     * 获取文件完整URL
     * @param fileId 文件Id
     * @return
     */
    public String getFileUrl(String fileId);

    /**
     * 获取文件id
     * @param url 文件路径
     * @return
     */
    String getFileId(String url);
}
