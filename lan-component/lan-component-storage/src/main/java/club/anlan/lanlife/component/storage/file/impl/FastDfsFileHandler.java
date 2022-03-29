package club.anlan.lanlife.component.storage.file.impl;

import club.anlan.lanlife.component.storage.client.FastDFSClient;
import club.anlan.lanlife.component.storage.constant.FileHandlerTypeConstant;
import club.anlan.lanlife.component.storage.file.FileHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.InputStream;

/**
 * fastDfs file handler
 *
 * @author lan
 * @version 1.0
 * @date 2021/3/28 12:01
 */
@Component
public class FastDfsFileHandler implements FileHandler {

    @Value("${storage.down-url-prefix}")
    private String filePrefix;

    @Autowired
    private FastDFSClient fastDFSClient;

    @Override
    public String fileUpload(InputStream inputStream, String fileName) {
        return fastDFSClient.uploadFile(inputStream, fileName);
    }

    @Override
    public String fileUpload(byte[] bytes, String fileName) {
        return fastDFSClient.uploadFile(bytes, fileName);
    }

    @Override
    public String uploadAppenderFile(byte[] bytes, String fileName) {
        return fastDFSClient.uploadAppenderFile(bytes, fileName);
    }

    @Override
    public String uploadAppenderFile(InputStream inputStream, String fileName) {
        return null;
    }

    @Override
    public int modifyFile(String fileId, long historyUploadSize, byte[] bytes) {
        return fastDFSClient.modifyFile(fileId, historyUploadSize, bytes);
    }

    @Override
    public String getHandlerType() {
        return FileHandlerTypeConstant.FILE_HANDLER_FASTDFS;
    }

    @Override
    public boolean fileDelete(String fileId) {
        fastDFSClient.deleteFile(fileId);
        return true;
    }

    @Override
    public String getFilePrefix() {
        return filePrefix;
    }

    public void setFilePrefix(String filePrefix) {
        this.filePrefix = filePrefix;
    }

    @Override
    public InputStream fileDownload(String fileId) {
        return fastDFSClient.downloadFile(fileId);
    }

    @Override
    public byte[] fileDownloadByte(String fileId) {
        return null;
    }

    @Override
    public String getFileUrl(String fileId) {
        return filePrefix + "/" + fileId;
    }

    @Override
    public String getFileId(String url) {
        return url.replace(filePrefix + "/", "");
    }
}
