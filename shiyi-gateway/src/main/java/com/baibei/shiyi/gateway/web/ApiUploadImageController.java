package com.baibei.shiyi.gateway.web;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.gateway.utils.UploadUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;

@RestController
@RequestMapping("/api/gateway")
public class ApiUploadImageController {

    @Autowired
    private UploadUtils uploadUtils;

    @Value("${oos.path.prefix}")
    private String oosPathPrefix;

    @PostMapping(value = "/uploadImage", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResult<String> uploadImage(@RequestPart("file") MultipartFile file) {
        String filePath;
        if (file.getSize() > 10 * 1024 * 1024) {
            return ApiResult.error("上传图片不能大于10MB");
        }
        try {
            if (!uploadUtils.checkImageType(file.getInputStream())) {
                return ApiResult.error("图片仅支持jpeg,png,gif格式");
            }
            filePath = uploadUtils.uploadImg3(file);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResult.error();
        }
        return ApiResult.success(oosPathPrefix+filePath);
    }

    @PostMapping(value = "/uploadImages", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResult<String> uploadImages(@RequestParam("files") MultipartFile[] files) {
        Long count = Arrays.stream(files).filter(file -> file.getSize() > 10 * 1024 * 1024).count();
        if (count > 0) {
            return ApiResult.error("上传图片不能大于10MB");
        }
        Long imagesError = Arrays.stream(files).filter(file -> {
            try {
                if (!uploadUtils.checkImageType(file.getInputStream())) {
                    return true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }).count();
        if (imagesError > 0) {
            return ApiResult.error("上传图片仅支持jpeg,png,gif格式");
        }
        StringBuilder filePathStr = new StringBuilder();
        try {
            for (MultipartFile file : files) {
                filePathStr.append(oosPathPrefix+uploadUtils.uploadImg3(file));
                filePathStr.append(",");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ApiResult.error();
        }
        String filePath = filePathStr.toString();
        if (!StringUtils.isEmpty(filePath)) {
            filePath = filePath.substring(0, filePath.length() - 1);
        }
        return ApiResult.success(filePath);
    }
}
