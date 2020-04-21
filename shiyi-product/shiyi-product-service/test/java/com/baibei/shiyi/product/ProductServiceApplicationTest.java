package com.baibei.shiyi.product;

import com.baibei.shiyi.product.feign.bean.dto.CategoryDto;
import com.baibei.shiyi.product.service.ICategoryService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Classname ProductServiceApplicationTest
 * @Description TODO
 * @Date 2019/7/31 14:04
 * @Created by Administrator
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProductServiceApplication.class)
public class ProductServiceApplicationTest {
    @Autowired
    private ICategoryService categoryService;
    @Test
    public void test1(){
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(1L);
        /*List<CategoryVo> theFirstCategorys = categoryService.getIndexCategorysByPid(1L);
        System.out.println(theFirstCategorys);*/
    }
}
