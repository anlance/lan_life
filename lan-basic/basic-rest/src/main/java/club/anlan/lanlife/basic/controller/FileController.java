package club.anlan.lanlife.basic.controller;

import club.anlan.lanlife.base.annotation.Log;
import club.anlan.lanlife.base.result.ResultMessage;
import club.anlan.lanlife.basic.domain.File;
import club.anlan.lanlife.basic.enums.DeleteFlagEnum;
import club.anlan.lanlife.basic.query.FileQuery;
import club.anlan.lanlife.basic.service.FileService;
import club.anlan.lanlife.redis.cache.ContentTypeCache;
import club.anlan.lanlife.storage.FileUtil;
import club.anlan.lanlife.storage.image.ImageHandler;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
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
    private ImageHandler imageHandler;

    @Autowired
    private ContentTypeCache contentTypeCache;

    private final static String DEFAULT_USER_ID = "temp_user";

    @Log(description = "上传文件")
    @PostMapping("/uploadFile")
    public ResultMessage uploadFile(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        if (multipartFile == null) {
            log.error("无文件上传");
            return ResultMessage.createFailedResult("no file");
        }
        String id = imageHandler.imageUpload(multipartFile.getBytes(), multipartFile.getOriginalFilename());
        File file = new File();
        file.setId(UUID.randomUUID().toString().substring(0, 32));
        file.setName(multipartFile.getOriginalFilename());
        file.setUrl(imageHandler.getImageUrl(id));
        file.setCreateUserId(DEFAULT_USER_ID);
        file.setType(FileUtil.getExtention(multipartFile.getOriginalFilename()));
        fileService.insertFile(file);
        log.info("file: {}", file);
        return ResultMessage.createSuccessResult();
    }

    @Log(description = "上传大文件")
    @PostMapping("/uploadChunkFile")
    public ResultMessage uploadChunkFile(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        if (multipartFile == null) {
            log.error("无文件上传");
            return ResultMessage.createFailedResult("no file");
        }
        String id = imageHandler.imageUpload(multipartFile.getBytes(), multipartFile.getOriginalFilename());
        File file = new File();
        file.setId(UUID.randomUUID().toString().substring(0, 32));
        file.setName(multipartFile.getOriginalFilename());
        file.setUrl(imageHandler.getImageUrl(id));
        file.setCreateUserId(DEFAULT_USER_ID);
        file.setType(FileUtil.getExtention(multipartFile.getOriginalFilename()));
        fileService.insertFile(file);
        log.info("file: {}", file);
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
            String fileId = imageHandler.getImageId(file.getUrl());
            InputStream inputStream = imageHandler.imageDownload(fileId);
            response.setContentType(contentTypeCache.get(contentTypeCache.getKey(file.getType())));
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

}

