package club.anlan.lanlife.storage.client;

import club.anlan.lanlife.storage.constant.ImageTypeConstant;
import org.apache.commons.lang3.StringUtils;
import org.csource.fastdfs.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.Properties;

/**
 * fastDFS
 *
 * @author lan
 * @version 1.0
 * @date 2021/3/28 11:35
 */
@Component
public class FastDFSClient {

    private static final Logger logger = LoggerFactory.getLogger(FastDFSClient.class);

    @Value("${storage.fdfs.connect-timeout:10}")
    private int connectTimeout;
    @Value("${storage.fdfs.network-timeout:30}")
    private int networkTimeout;
    @Value("${storage.fdfs.charset:UTF-8}")
    private String charset;
    @Value("${storage.fdfs.tracker-http-port:8080}")
    private int trackerHttpPort;
    @Value("${storage.fdfs.tracker-server:localhost:22122}")
    private String trackerServer;
    @Value("${storage.fdfs.http-anti-steal-token:false}")
    private boolean httpAntiStealToken;
    @Value("${storage.fdfs.http-secret-key:}")
    private String httpSecretKey;
    @Value("${storage.type:}")
    private String type;

    TrackerClient trackerClient = null;

    @PostConstruct
    public void init() {
        try {
            if (!ImageTypeConstant.IMAGE_FASTDFS.equals(type)) {
                return;
            }
            Properties properties = new Properties();
            properties.setProperty("fastdfs.charset",charset);
            properties.setProperty("fastdfs.http_anti_steal_token", httpAntiStealToken ? "true":"false");
            properties.setProperty("fastdfs.connect_timeout_in_seconds", String.valueOf(connectTimeout));
            properties.setProperty("fastdfs.http_secret_key", httpSecretKey);
            properties.setProperty("fastdfs.http_tracker_http_port", String.valueOf(trackerHttpPort));
            properties.setProperty("fastdfs.tracker_servers,", trackerServer);
            ClientGlobal.initByProperties(properties);
            trackerClient = new TrackerClient(ClientGlobal.g_tracker_group);
        } catch (Exception e) {
            logger.error("getStoreStorage exception", e);
        }
    }

    private StorageClient1 getStorageClient() throws IOException {
        TrackerServer trackerServer = trackerClient.getConnection();
        if (trackerServer == null) {
            logger.error("getConnection return null");
            return null;
        }
        StorageServer storageServer = trackerClient.getStoreStorage(trackerServer);
        if (storageServer == null) {
            logger.error("getStoreStorage return null");
            return null;
        }
        return new StorageClient1(trackerServer, storageServer);
    }

    /**
     *
     * @param file
     *            文件
     * @param fileName
     *            文件名
     * @return 返回Null则为失败
     */
    public String uploadFile(File file, String fileName) {
        FileInputStream fis = null;
        try {
            StorageClient1 storageClient = this.getStorageClient();
            if (storageClient == null) {
                return StringUtils.EMPTY;
            }
            fis = new FileInputStream(file);
            byte[] file_buff = null;
            if (fis != null) {
                file_buff = new byte[fis.available()];
                int len = fis.read(file_buff);
                while (len != -1) {
                    len = fis.read(file_buff);
                }
                return storageClient.upload_file1(file_buff, getFileExt(fileName), null);
            }
        } catch (Exception ex) {
            logger.error("上传文件失败", ex);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    logger.error("file close error", e);
                }
            }
        }
        return StringUtils.EMPTY;
    }

    public String uploadFile(InputStream inputStream, String fileName) {
        try {
            StorageClient1 storageClient = this.getStorageClient();
            if (storageClient == null) {
                return StringUtils.EMPTY;
            }
            byte[] file_buff = null;
            if (inputStream != null) {
                file_buff = new byte[inputStream.available()];
                int len = inputStream.read(file_buff);
                while (len != -1) {
                    len = inputStream.read(file_buff);
                }
                String file_ext = getFileExt(fileName);
                return storageClient.upload_file1(file_buff, file_ext, null);
            }
        } catch (Exception ex) {
            logger.error("上传文件失败", ex);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    logger.error("inputStream close error", e);
                }
            }
        }
        return StringUtils.EMPTY;
    }

    public String uploadFile(byte[] bytes, String fileName) {
        try {
            StorageClient1 storageClient = this.getStorageClient();
            if (storageClient == null) {
                return StringUtils.EMPTY;
            }
            String file_ext = getFileExt(fileName);
            String fileid = storageClient.upload_file1(bytes, file_ext, null);
            return fileid;
        } catch (Exception ex) {
            logger.error("上传文件失败", ex);
        }
        return StringUtils.EMPTY;
    }

    /**
     * 根据组名和远程文件名来删除一个文件
     *
     * @param groupName
     *            例如 "group1" 如果不指定该值，默认为group1
     * @param fileName
     *            例如"M00/00/00/wKgxgk5HbLvfP86RAAAAChd9X1Y736.jpg"
     * @return 0为成功，非0为失败，具体为错误代码
     */
    public int deleteFile(String groupName, String fileName) {
        try {
            StorageClient1 storageClient = this.getStorageClient();
            if (storageClient == null) {
                return 0;
            }
            return storageClient.delete_file(groupName == null ? "group1" : groupName, fileName);
        } catch (Exception ex) {
            logger.error("删除文件失败", ex);
            return 0;
        }
    }

    /**
     * 根据fileId来删除一个文件
     *
     * @param fileId
     *            file_id源码中的解释file_id the file id(including group name and
     *            filename);例如 group1/M00/00/00/ooYBAFM6MpmAHM91AAAEgdpiRC0012.xml
     * @return 0为成功，非0为失败，具体为错误代码
     */
    public int deleteFile(String fileId) {
        try {
            StorageClient1 storageClient = this.getStorageClient();
            if (storageClient == null) {
                return 0;
            }
            return storageClient.delete_file1(fileId);
        } catch (Exception ex) {
            logger.error("删除文件失败", ex);
            return 0;
        }
    }

    /**
     * 修改一个已经存在的文件
     *
     * @param oldFileId
     *            原来旧文件的fileId, file_id源码中的解释file_id the file id(including group
     *            name and filename);例如
     *            group1/M00/00/00/ooYBAFM6MpmAHM91AAAEgdpiRC0012.xml
     * @param file
     *            新文件
     * @param fileName
     *            新文件路径
     * @return 返回空则为失败
     */
    public String modifyFile(String oldFileId, File file, String fileName) {
        String fileid = null;
        try {
            // 先上传
            fileid = uploadFile(file, fileName);
            if (fileid == null) {
                return StringUtils.EMPTY;
            }
            // 再删除
            int delResult = deleteFile(oldFileId);
            if (delResult != 0) {
                return StringUtils.EMPTY;
            }
        } catch (Exception ex) {
            logger.error("更改文件失败", ex);
        }
        return fileid;
    }

    /**
     * 文件下载
     *
     * @param fileId
     * @return 返回一个流
     */
    public InputStream downloadFile(String fileId) {
        try {
            StorageClient1 storageClient = this.getStorageClient();
            if (storageClient == null) {
                return null;
            }
            byte[] bytes = storageClient.download_file1(fileId);
            if (bytes != null) {
                InputStream inputStream = new ByteArrayInputStream(bytes);
                return inputStream;
            }
        } catch (Exception ex) {
            logger.error("下载文件失败", ex);
        }
        return null;
    }

    /**
     * 获取文件后缀名（不带点）.
     *
     * @return 如："jpg" or "".
     */
    private static String getFileExt(String fileName) {
        if (fileName == null || ("").equals(fileName) || !fileName.contains(".")) {
            return "jpg";
        } else {
            return fileName.substring(fileName.lastIndexOf(".") + 1); // 不带最后的点
        }
    }

}
