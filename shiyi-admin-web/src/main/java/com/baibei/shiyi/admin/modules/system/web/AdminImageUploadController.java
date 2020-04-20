package com.baibei.shiyi.admin.modules.system.web;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.content.feign.client.IUploadImageFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/admin/api")
public class AdminImageUploadController {

    @Autowired
    private IUploadImageFeign uploadImageFeign;


    @PostMapping(value = "/uploadImage")
    public ApiResult uploadImage(@RequestParam("file") MultipartFile file) {
        return uploadImageFeign.uploadImage(file);
    }

    @PostMapping(value = "/uploadImages")
    public ApiResult uploadImage(@RequestParam("files") MultipartFile[] multipartFiles) {
        return uploadImageFeign.uploadImages(multipartFiles);
    }

}
