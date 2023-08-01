package com.deer.wms.pdaApi.web;

import java.util.Arrays;
import java.util.List;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deer.wms.intercept.annotation.Authority;
import com.deer.wms.intercept.annotation.User;
import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.pdaApi.model.PdaDemoEntity;
import com.deer.wms.pdaApi.service.PdaDemoService;
import com.deer.wms.project.root.configurer.ApiResultPacking;
import com.deer.wms.project.root.util.SimpleMap;
import com.deer.wms.project.root.validation.group.Post;

import lombok.RequiredArgsConstructor;

/**
 * PDA API Demo
 * 
 * @author xuesinuo
 *
 */
@ApiResultPacking // 2023-06-29 xuesinuo 返回值新玩法
@RestController
@RequestMapping("/pda/demo")
@RequiredArgsConstructor
public class PdaDemoController {
    private final PdaDemoService service;

    /**
     * 标准案例
     * 
     * @param entity
     * @param currentUser
     * @return
     */
    @Authority("pda_demo_add")
    @PostMapping("/add")
    public PdaDemoEntity add(@Validated(Post.class) @RequestBody PdaDemoEntity entity, @User CurrentUser currentUser) {
        PdaDemoEntity result = service.add(entity);
        return result;
    }

    @PostMapping("/string")
    public String string() {
        return "hello";
    }

    @PostMapping("/integer")
    public int integer() {
        return 1;
    }

    @PostMapping("/integer2")
    public Integer integer2() {
        return new Integer(1);
    }

    @PostMapping("/array")
    public String[] array() {
        return (String[]) Arrays.asList("1", "2", "a").toArray();
    }

    @PostMapping("/list")
    public List<?> list() {
        return Arrays.asList(1, 2, 3);
    }

    @PostMapping("/map")
    public Object map() {
        return SimpleMap.init("hi", "XSN");
    }

    @ApiResultPacking(false)
    @PostMapping("/string2")
    public String string2() {
        return "hello";
    }
}
