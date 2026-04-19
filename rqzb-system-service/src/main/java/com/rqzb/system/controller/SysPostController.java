package com.rqzb.system.controller;

import com.rqzb.system.entity.SysPost;
import com.rqzb.system.service.SysPostService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/system/posts")
public class SysPostController extends BaseCrudController<SysPost> {

    public SysPostController(SysPostService postService) {
        super(postService);
    }
}
