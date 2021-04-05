package club.anlan.lanlife.storage.image.impl;

import club.anlan.lanlife.storage.client.FastDFSClient;
import club.anlan.lanlife.storage.constant.ImageTypeConstant;
import club.anlan.lanlife.storage.image.ImageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.InputStream;

/**
 * 类
 *
 * @author lan
 * @version 1.0
 * @date 2021/3/28 12:01
 */
@Component
public class FastdfsImageHandler implements ImageHandler {

    @Value("${storage.down-url-prefix}")
    private String imagePrefix;

    @Autowired
    private FastDFSClient fastDFSClient;

    @Override
    public String imageUpload(InputStream inputStream, String fileName) {
        return fastDFSClient.uploadFile(inputStream, fileName);
    }

    @Override
    public String imageUpload(byte[] bytes, String fileName) {
        return fastDFSClient.uploadFile(bytes, fileName);
    }


    @Override
    public String getImageType() {
        return ImageTypeConstant.IMAGE_FASTDFS;
    }

    @Override
    public boolean imageDelete(String fileId) {
        fastDFSClient.deleteFile(fileId);
        return true;
    }

    @Override
    public String getImagePrefix() {
        return imagePrefix;
    }

    public void setImagePrefix(String imagePrefix) {
        this.imagePrefix = imagePrefix;
    }

    @Override
    public InputStream imageDownload(String fileId) {
        return fastDFSClient.downloadFile(fileId);
    }

    @Override
    public byte[] imageDownloadByte(String fileId) {
        return null;
    }

    @Override
    public String getImageUrl(String fileId) {
        return imagePrefix + "/" + fileId;
    }

    @Override
    public String getImageId(String url) {
        return url.replace(imagePrefix + "/", "");
    }

    public static String getImageIds(String url) {
        return url.replace("http://anlan.club" + "/", "");
    }

    public static void main(String[] args) {
        String s = "http://anlan.club/group1/M00/00/00/rBEAEGBqiBiAIibzAAAASBejHj4182.txt";
        System.out.println(getImageIds(s));
    }
}
