//package com.baibei.shiyi.admin.modules.system.web;
//
//import com.baibei.shiyi.admin.modules.system.bean.dto.UserDTO;
//import com.baibei.shiyi.admin.modules.system.model.User;
//import com.baibei.shiyi.admin.modules.system.service.IUserService;
//import com.baibei.shiyi.common.tool.api.ApiResult;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/admin/api")
//public class AdminUserController {
//
//    @Autowired
//    private IUserService userService;
//
//    @PostMapping(value = "/create")
//    @PreAuthorize("hasAnyRole('ADMIN','USER_ALL','USER_CREATE')")
//    public ApiResult create(@Validated @RequestBody User resources) {
//        /* checkLevel(resources);*/
//        ApiResult result;
//        try {
//            UserDTO userDTO = userService.create(resources);
//            result = ApiResult.success(userDTO);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            result = ApiResult.error(ex.getMessage());
//        }
//        return result;
//    }
//
//    @PostMapping(value = "/update")
//    @PreAuthorize("hasAnyRole('ADMIN','USER_ALL','USER_EDIT')")
//    public ApiResult update(@RequestBody User resources) {
//        if (resources.getId() == null) {
//            return ApiResult.error("Id 不能为空");
//        }
//        userService.updateUser(resources);
//        return ApiResult.success();
//    }
//}
