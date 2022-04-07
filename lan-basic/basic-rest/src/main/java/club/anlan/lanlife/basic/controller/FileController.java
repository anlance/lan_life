package club.anlan.lanlife.basic.controller;

import club.anlan.lanlife.component.base.annotation.Log;
import club.anlan.lanlife.component.base.result.ResultMessage;
import club.anlan.lanlife.basic.redis.RedisKey;
import club.anlan.lanlife.basic.domain.File;
import club.anlan.lanlife.basic.enums.DeleteFlagEnum;
import club.anlan.lanlife.basic.query.FileQuery;
import club.anlan.lanlife.basic.service.FileService;
import club.anlan.lanlife.basic.redis.cache.ContentTypeCache;
import club.anlan.lanlife.basic.vo.ChunkFile;
import club.anlan.lanlife.component.redis.util.RedisUtil;
import club.anlan.lanlife.component.storage.FileUtil;
import club.anlan.lanlife.component.storage.file.FileHandler;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * FileController
 *
 * @author lan
 * @version 1.0
 * @date 2021/4/4 13:53
 */
@RestController
@Slf4j
@RequestMapping("/file")
public class FileController {

    @Autowired
    private FileService fileService;

    @Autowired
    private FileHandler fileHandler;

    @Autowired
    private ContentTypeCache contentTypeCache;

    @Autowired
    private RedisUtil redisUtil;

    private final static String DEFAULT_USER_ID = "temp_user";

    @Log(description = "上传文件")
    @PostMapping("/uploadFile")
    public ResultMessage uploadFile(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        if (multipartFile == null) {
            log.error("无文件上传");
            return ResultMessage.createFailedResult("no file");
        }
        String id = fileHandler.fileUpload(multipartFile.getBytes(), multipartFile.getOriginalFilename());
        File file = new File();
        file.setId(UUID.randomUUID().toString().substring(0, 32));
        file.setName(multipartFile.getOriginalFilename());
        file.setUrl(fileHandler.getFileUrl(id));
        file.setCreateUserId(DEFAULT_USER_ID);
        file.setType(FileUtil.getExtention(multipartFile.getOriginalFilename()));
        fileService.insertFile(file);
        log.info("file: {}", file);
        return ResultMessage.createSuccessResult();
    }

    @Log(description = "上传大文件")
    @PostMapping("/uploadChunkFile")
    public ResultMessage uploadChunkFile(HttpServletRequest request) throws IOException {
        MultipartHttpServletRequest params=((MultipartHttpServletRequest) request);
        ChunkFile chunkFile = new ChunkFile();
        chunkFile.setAllIndex(Integer.valueOf(params.getParameter("allIndex")));
        chunkFile.setCurIndex(Integer.valueOf(params.getParameter("curIndex")));
        chunkFile.setFileMd5(params.getParameter("fileMd5"));
        chunkFile.setFileName(params.getParameter("fileName"));
        chunkFile.setFileSize(Long.valueOf(params.getParameter("fileSize")));
        chunkFile.setChunkSize(Long.valueOf(params.getParameter("chunkSize")));
        MultipartFile fileffff = params.getFile("files");
        chunkFile.setFiles(Arrays.asList(fileffff));
        log.info("right: {}",JSONObject.toJSONString(chunkFile));
        System.out.println(JSONObject.toJSONString(chunkFile));
        String chunkLockName = RedisKey.CHUNK_FILE_UPLOADING_LOCK + chunkFile.getFileMd5();
        String tempUser = UUID.randomUUID().toString();
        boolean curOwner = false;

        if (chunkFile.getCurIndex() == null) {
            chunkFile.setCurIndex(0);
        }
        if (chunkFile.getAllIndex() == null) {
            chunkFile.setAllIndex(1);
        }

//        boolean lock = redisUtil.lock(chunkLockName, tempUser);
//        if (!lock) {
//            return ResultMessage.createFailedResult("请求锁失败！");
//        }
        curOwner = true;

        String indexKey = RedisKey.CHUNK_FILE_UPLOADING_NEXT_INDEX+chunkFile.getFileMd5();
        String nextIndexStr = redisUtil.get(indexKey);
        Integer curIndex = chunkFile.getCurIndex();
        if (StringUtils.isEmpty(nextIndexStr)) {
            redisUtil.add(RedisKey.CHUNK_FILE_UPLOADING_NEXT_INDEX+chunkFile.getFileMd5(), "0");
            nextIndexStr = redisUtil.get(indexKey);
        }
        Integer nextIndex = Integer.valueOf(nextIndexStr);
        if (chunkFile.getCurIndex() < nextIndex) {
            return ResultMessage.createFailedResult("当前文件块已上传");
        } else if (chunkFile.getCurIndex() > nextIndex) {
            // todo 重试机制 ??
        }

        List<MultipartFile> files = chunkFile.getFiles();
        MultipartFile file = null;
        for (int i=0; i < files.size(); i++){
            file = files.get(i);
            if (!file.isEmpty()) {
                Long historyUploadSize = 0L;
                String historyUploadStr = redisUtil.get(RedisKey.CHUNK_FILE_UPLOAD_HISTORY_SIZE+chunkFile.getFileMd5());
                if (StringUtils.isNotEmpty(historyUploadStr)) {
                    historyUploadSize = Long.valueOf(historyUploadStr);
                }
                log.debug("historyUpload大小:"+historyUploadSize);
                String fileId = null;
                if (curIndex == 0) {
                    //从没上传过
                    fileId = fileHandler.uploadAppenderFile(file.getBytes(), file.getOriginalFilename());
                    log.info("fileIdssss1: {}", fileId);
                    redisUtil.add(RedisKey.CHUNK_FILE_UPLOAD_FILE_ID +chunkFile.getFileMd5(), fileId);
                    redisUtil.add(RedisKey.CHUNK_FILE_UPLOAD_HISTORY_SIZE+chunkFile.getFileMd5(), String.valueOf(historyUploadSize + file.getSize()));
                } else {
                    fileId = redisUtil.get(RedisKey.CHUNK_FILE_UPLOAD_FILE_ID +chunkFile.getFileMd5());
                    log.info("fileIdssss2: {}", fileId);
                    fileHandler.modifyFile(fileId, historyUploadSize, file.getBytes());
                    redisUtil.add(RedisKey.CHUNK_FILE_UPLOAD_HISTORY_SIZE+chunkFile.getFileMd5(), String.valueOf(historyUploadSize + file.getSize()));
                }

                if (curIndex.equals(chunkFile.getAllIndex()-1)) {
                    File insertFile = new File();
                    String fileName = chunkFile.getFileName();
                    insertFile.setId(UUID.randomUUID().toString().substring(0, 32));
                    insertFile.setName(fileName);
                    insertFile.setUrl(fileHandler.getFileUrl(fileId));
                    insertFile.setCreateUserId(DEFAULT_USER_ID);
                    log.info("fileName: {}", FileUtil.getExtention(fileName));
                    insertFile.setType(FileUtil.getExtention(fileName));
                    fileService.insertFile(insertFile);
                    log.info("file: {}", file);
                    redisUtil.delete(RedisKey.CHUNK_FILE_UPLOAD_FILE_ID +chunkFile.getFileMd5());
                    redisUtil.delete(RedisKey.CHUNK_FILE_UPLOAD_HISTORY_SIZE +chunkFile.getFileMd5());
                    redisUtil.delete(RedisKey.CHUNK_FILE_UPLOADING_NEXT_INDEX +chunkFile.getFileMd5());
                    return ResultMessage.createSuccessResult();
                }
            }
        }

        return ResultMessage.createSuccessResult();
    }

    @Log(description = "删除文件")
    @DeleteMapping("/deleteFile/{id}")
    public ResultMessage deleteFile(@PathVariable("id") String id) {
        if (StringUtils.isNotEmpty(id)) {
            File file = fileService.getFile(id);
            file.setDeleteFlag(0);
            fileService.updateFile(file);
        }
        return ResultMessage.createSuccessResult();
    }

    @Log(description = "恢复文件")
    @PutMapping("/recoveryFile/{id}")
    public ResultMessage recoveryFile(@PathVariable("id") String id) {
        if (StringUtils.isNotEmpty(id)) {
            File file = fileService.getFile(id);
            file.setDeleteFlag(1);
            fileService.updateFile(file);
        }
        return ResultMessage.createSuccessResult();
    }


    @Log(description = "彻底删除文件")
    @DeleteMapping("/deleteFileReal/{id}")
    public ResultMessage deleteFileReal(@PathVariable("id") String id) {
        if (StringUtils.isNotEmpty(id)) {
            fileService.deleteFile(id);
        }
        return ResultMessage.createSuccessResult();
    }

    @Log(description = "下载文件")
    @GetMapping("/downloadFile/{id}")
    public ResultMessage downloadFile(@PathVariable("id") String id, HttpServletResponse response) throws IOException {
        if (StringUtils.isNotEmpty(id)) {
            File file = fileService.getFile(id);
            String fileId = fileHandler.getFileId(file.getUrl());
            log.info("fileId: {}",fileId);
            InputStream inputStream = fileHandler.fileDownload(fileId);
            response.setContentType(contentTypeCache.get(file.getType()));
            log.info("type: {}",contentTypeCache.get(file.getType()));
            response.setHeader("Content-Disposition",
                    "attachment;filename=\"" + URLEncoder.encode(file.getName(), "UTF-8") + "\"");
            OutputStream outputStream = response.getOutputStream();
            IOUtils.copy(inputStream, outputStream);
            outputStream.flush();
            outputStream.close();
            inputStream.close();
        }
        return ResultMessage.createSuccessResult();
    }

    @Log(description = "查询文件")
    @GetMapping("/getFiles")
    public ResultMessage getFile(FileQuery fileQuery) {
        if (fileQuery == null) {
            fileQuery = new FileQuery();
        }
        System.out.println(fileQuery);
        if (StringUtils.isEmpty(fileQuery.getUserId())) {
            fileQuery.setUserId(DEFAULT_USER_ID);
        }
        if (fileQuery.getDeleteFlag() == null) {
            fileQuery.setDeleteFlag(DeleteFlagEnum.EXISTS.getFlag());
        }
        IPage<File> fileIPage = fileService.getFiles(fileQuery);
        System.out.println(JSONObject.toJSONString(fileIPage));
        return ResultMessage.createSuccessResult(fileIPage);
    }

    @GetMapping("/getFiles/{name}")
    public ResultMessage getFile(@PathVariable String name) {
        System.out.println(name);
        String[] str = name.split("\\|");
        System.out.println(str);
        System.out.println(str[0]);
        System.out.println(str[1]);
        return ResultMessage.createSuccessResult(name);
    }

}

