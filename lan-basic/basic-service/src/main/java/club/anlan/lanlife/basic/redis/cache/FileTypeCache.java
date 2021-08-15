package club.anlan.lanlife.basic.redis.cache;

import club.anlan.lanlife.basic.enums.FileType;

import java.util.HashMap;
import java.util.Map;

/**
 * 文件大类缓存
 *
 * @author lan
 * @version 1.0
 * @date 2021/5/12 22:07
 */
public class FileTypeCache {
    private static Map<String, FileType> fileTypeMap = new HashMap<>();

    static {
        // pdf
        fileTypeMap.put(".pdf", FileType.PDF);

        // compress_file
        fileTypeMap.put(".tar.gz", FileType.COMPRESS_FILE);
        fileTypeMap.put(".zip", FileType.COMPRESS_FILE);
        fileTypeMap.put(".7z", FileType.COMPRESS_FILE);
        fileTypeMap.put(".rar", FileType.COMPRESS_FILE);

        // video
        fileTypeMap.put(".mp4", FileType.VIDEO);
        fileTypeMap.put(".flv", FileType.VIDEO);
        fileTypeMap.put(".rmvb", FileType.VIDEO);
        fileTypeMap.put(".avi", FileType.VIDEO);
        fileTypeMap.put(".mkv", FileType.VIDEO);

        // audio
        fileTypeMap.put(".mp3", FileType.AUDIO);

        // picture
        fileTypeMap.put(".png", FileType.PICTURE);
        fileTypeMap.put(".jpg", FileType.PICTURE);
        fileTypeMap.put(".jpeg", FileType.PICTURE);
        fileTypeMap.put(".gif", FileType.PICTURE);
        fileTypeMap.put(".ico", FileType.PICTURE);

        // doc
        fileTypeMap.put(".doc", FileType.DOC);
        fileTypeMap.put(".docx", FileType.DOC);

        // txt
        fileTypeMap.put(".txt", FileType.TXT);

        // ppt
        fileTypeMap.put(".ppt", FileType.PPT);
        fileTypeMap.put(".pptx", FileType.PPT);

        // torrent
        fileTypeMap.put(".torrent", FileType.TORRENT);

        // web
        fileTypeMap.put(".html", FileType.WEB);
        fileTypeMap.put(".htm", FileType.WEB);

        // code
        fileTypeMap.put(".js", FileType.CODE);
        fileTypeMap.put(".json", FileType.CODE);
        fileTypeMap.put(".java", FileType.CODE);
        fileTypeMap.put(".c", FileType.CODE);
        fileTypeMap.put(".cpp", FileType.CODE);
        fileTypeMap.put(".h", FileType.CODE);
        fileTypeMap.put(".py", FileType.CODE);
        fileTypeMap.put(".go", FileType.CODE);
        fileTypeMap.put(".vue", FileType.CODE);
    }
}
