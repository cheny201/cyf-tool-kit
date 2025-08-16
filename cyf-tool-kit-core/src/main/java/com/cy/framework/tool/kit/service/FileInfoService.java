package com.cy.framework.tool.kit.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cy.framework.tool.kit.domain.FileInfo;
import com.cy.framework.tool.kit.mapper.FileInfoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class FileInfoService extends ServiceImpl<FileInfoMapper, FileInfo> {
}
