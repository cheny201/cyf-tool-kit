package com.cy.framework.tool.kit.service;

import cn.hutool.core.io.FileUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.cy.framework.tool.kit.domain.FileInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.SetUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
public class FileService {

    @Autowired
    private FileInfoService fileInfoService;

    public int readDir(String deviceName,String path,String excludeDirs,boolean md5) throws Exception {
        File dir = new File(path);
        Assert.isTrue(dir.exists(),"路径不存在");
        Assert.isTrue(dir.isDirectory(),"此路径不是文件夹");

        List<File> fileLs = Lists.newArrayList();
        FileVisitor<Path> visitor = getFileVisitor(excludeDirs,fileLs);
        FileUtil.walkFiles(dir.toPath(), visitor);
        visitor.toString();

        List<FileInfo> saveLs = fileLs.stream().map(v->{
            FileInfo f = new FileInfo();
            f.setFileName(FileUtil.mainName(v));
            f.setFileType(FileUtil.getSuffix(v));
            f.setDeviceName(deviceName);
            f.setAbsolutePath(v.getAbsolutePath());
            if(md5){
                f.setMd5(DigestUtil.md5Hex(v));
            }
            f.setFileSize(v.length());
            f.setProcessName(v.getParentFile().getName());
            f.setUpdateUserId("");
            return f;
        }).collect(Collectors.toList());
        fileInfoService.saveBatch(saveLs);
        return saveLs.size();
    }

    private FileVisitor<Path> getFileVisitor(String excludeDirs,List<File> fileLs){
        Set<String> excludeDirsSet = StringUtils.isNotBlank(excludeDirs) ? Arrays.stream(excludeDirs.split(",")).map(String::toLowerCase).collect(Collectors.toSet()) : Sets.newHashSet();
        // 创建安全的文件访问器
        FileVisitor<Path> visitor = new SimpleFileVisitor<Path>() {
            int processed = 0, skipped = 0;

            @Override
            public String toString() {
                log.error("处理成功:{}个,失败:{}个",processed,skipped);
                return super.toString();
            }

            /**
             * 判定是否是系统文件
             * @param file
             * @return
             */
            private boolean isSystemProtected(File file) {
                String name = file.getName().toLowerCase();
                return name.equals("system volume information") ||
                        name.equals("$recycle.bin") ||
                        name.startsWith("$");
            }

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                // 跳过系统保护目录
                if (isSystemProtected(dir.toFile())) {
                    log.error("跳过系统目录: {}" , dir);
                    return FileVisitResult.SKIP_SUBTREE;
                }

                // 检查目录是否可读
                if (!Files.isReadable(dir)) {
                    log.error("跳过无权限目录: {}" , dir);
                    return FileVisitResult.SKIP_SUBTREE;
                }
                if(excludeDirsSet.contains(dir.toFile().getName().toLowerCase())){
                    log.error("跳过过滤的目录: {}" , dir);
                    return FileVisitResult.SKIP_SUBTREE;
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                try {
                    // 处理文件
                    fileLs.add(file.toFile());
                    processed++;
                } catch (Exception e) {
                    log.error("处理失败: {}",file,e);
                    skipped++;
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                // 专门处理文件访问失败的情况
                if (exc instanceof AccessDeniedException) {
                    log.error("访问失败(拒绝): {}" , file);
                    skipped++;
                    return FileVisitResult.CONTINUE;
                }
                return super.visitFileFailed(file, exc);
            }
        };
        return visitor;
    }

}
