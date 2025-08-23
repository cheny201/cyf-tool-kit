package com.cy.framework.tool.kit.controller;

import com.cy.framework.common.dto.RestResultDTO;
import com.cy.framework.tool.kit.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "文件管理")
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
    @Operation(summary = "读取目录中的文件")
    @RequestMapping(value = "readDir")
    public RestResultDTO<String> readDir(@Parameter(description = "目录")@RequestParam String path,
                                         @Parameter(description = "设备名称") @RequestParam(required = false) String deviceName,
                                         @Parameter(description = "排除的目录名称（使用逗号分隔）")@RequestParam(required = false) String excludeDirs,
                                         @Parameter(description = "是否需要清空记录")@RequestParam(defaultValue = "false") Boolean clean,
                                         @Parameter(description = "是否需要计算md5值")@RequestParam(defaultValue = "false") Boolean md5) throws Exception {
        int size = service.readDir(deviceName,path,excludeDirs,clean,md5);
        return RestResultDTO.newSuccess("读取文件数:"+size,"操作成功");
    }

}
