package club.anlan.lanlife.basic.redis;

/**
 * RedisKey
 *
 * @author lan
 * @version 1.0
 * @date 2021/4/24 14:54
 */
public class RedisKey {

    /**
     * @Fields RANDOM_CODE_CACHE: 文件信息缓存前缀
     */
    public static final String CONTENT_TYPE_CACHE = "content:type:.%s";

    /**
     * 大文件上传锁
     */
    public static final String CHUNK_FILE_UPLOADING_LOCK = "chunk:file:uploading:lock:";

    /**
     * 大文件上传 当前分片位置
     */
    public static final String CHUNK_FILE_UPLOADING_NEXT_INDEX = "chunk:file:uploading:next:index:";

    /**
     * 大文件上传 已经上传大小
     */
    public static final String CHUNK_FILE_UPLOAD_HISTORY_SIZE = "chunk:file:file:upload:history:size:";


    /**
     * 大文件上传 上传id
     */
    public static final String CHUNK_FILE_UPLOAD_FILE_ID = "chunk:file:file:upload:file:id:";

    /**
     * 大文件上传 上传name
     */
    public static final String CHUNK_FILE_UPLOAD_FILE_NAME = "chunk:file:file:upload:file:name:";

    /**
     * 登录时的随机数缓存
     */
    public static final String AUTH_LOGIN_USERNAME_RANDOM_CODE = "auth:login:username:random:code:";

}
