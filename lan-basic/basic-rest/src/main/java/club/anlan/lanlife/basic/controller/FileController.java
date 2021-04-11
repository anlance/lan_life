package club.anlan.lanlife.basic.controller;

import club.anlan.lanlife.base.result.ResultMessage;
import club.anlan.lanlife.basic.domain.File;
import club.anlan.lanlife.basic.enums.DeleteFlagEnum;
import club.anlan.lanlife.basic.query.FileQuery;
import club.anlan.lanlife.basic.service.FileService;
import club.anlan.lanlife.cache.ContentTypeCache;
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

    @PostMapping("/uploadFile")
    public ResultMessage uploadFile(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        if (multipartFile == null) {
            log.error("无文件上传");
            return ResultMessage.createFailedResult("no file");
        }
        log.info("uploadFile :{}", multipartFile.getOriginalFilename());
        System.out.println("uploadFile :{}" + multipartFile.getOriginalFilename());
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

    @DeleteMapping("/deleteFile/{id}")
    public ResultMessage deleteFile(@PathVariable("id") String id) {
        log.info("delete file id :", id);
        if (StringUtils.isNotEmpty(id)) {
            File file = fileService.getFile(id);
            file.setDeleteFlag(0);
            fileService.updateFile(file);
        }
        return ResultMessage.createSuccessResult();
    }

    @PutMapping("/recoveryFile/{id}")
    public ResultMessage recoveryFile(@PathVariable("id") String id) {
        log.info("delete file id :", id);
        if (StringUtils.isNotEmpty(id)) {
            File file = fileService.getFile(id);
            file.setDeleteFlag(1);
            fileService.updateFile(file);
        }
        return ResultMessage.createSuccessResult();
    }


    @DeleteMapping("/deleteFileReal/{id}")
    public ResultMessage deleteFileReal(@PathVariable("id") String id) {
        log.info("deleteFileReal file id :", id);
        if (StringUtils.isNotEmpty(id)) {
            fileService.deleteFile(id);
        }
        return ResultMessage.createSuccessResult();
    }

    @GetMapping("/downloadFile/{id}")
    public ResultMessage downloadFile(@PathVariable("id") String id, HttpServletResponse response) throws IOException {
        log.info("download file id :", id);
        if (StringUtils.isNotEmpty(id)) {
            File file = fileService.getFile(id);
            String fileId = imageHandler.getImageId(file.getUrl());
            InputStream inputStream = imageHandler.imageDownload(fileId);
            System.out.println("-------------");
            System.out.println(fileId);
            System.out.println("-------------");
            response.setContentType(contentTypeCache.getMap().get("." + file.getType()));
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

    @GetMapping("/getFiles")
    public ResultMessage getFile(FileQuery fileQuery) {
        log.info("getFile  fileQuery:", fileQuery);
        System.out.println("getFile  fileQuery:"+fileQuery);
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

