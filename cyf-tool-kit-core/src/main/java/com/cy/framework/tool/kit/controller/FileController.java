package com.cy.framework.tool.kit.controller;

import com.cy.framework.common.dto.RestResultDTO;
import com.cy.framework.tool.kit.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping(value = {"file"},method = {RequestMethod.GET,RequestMethod.POST})
public class FileController {

    @Autowired
    private FileService service;


    /**
     * 读取目录
     * @param path
     * @return
     */
    @RequestMapping(value = "readDir")
    public RestResultDTO<String> readDir(@RequestParam String path,
                                         @RequestParam(required = false) String deviceName,
                                         @RequestParam(required = false) String excludeDirs) throws Exception {
        int size = service.readDir(deviceName,path,excludeDirs);
        return RestResultDTO.newSuccess("读取文件数:"+size,"操作成功");
    }

}
