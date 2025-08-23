package com.cy.framework.tool.kit.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cy.framework.data.mybatis.domain.BaseModel;
import com.cy.framework.tool.kit.support.constant.Constant;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Table;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity(name = FileInfo.TABLE_NAME)
@Table(appliesTo =FileInfo.TABLE_NAME, comment = "文件信息表")
@TableName(value = FileInfo.TABLE_NAME)
@javax.persistence.Table(name = FileInfo.TABLE_NAME,indexes = {
        @Index(name = "idx_1",columnList = "fileName"),
        @Index(name = "idx_2",columnList = "absolutePath"),
})
public class FileInfo extends BaseModel {
    public final static String TABLE_NAME = Constant.TABLE_PREFIX+"file_info";

    @Column(columnDefinition = "varchar(100) COMMENT '电影记录ID'")
    private String movieId;
    @Column(columnDefinition = "varchar(500) COMMENT '文件名'")
    private String fileName;
    @Column(columnDefinition = "varchar(100) COMMENT '文件类型'")
    private String fileType;
    @Column(columnDefinition = "varchar(100) COMMENT '磁盘名称'")
    private String deviceName;
    @Column(columnDefinition = "varchar(1000) COMMENT '绝对路径'")
    private String absolutePath;
    @Column(columnDefinition = "varchar(100) COMMENT 'MD5'")
    private String md5;
    @Column(columnDefinition = "bigint(11) COMMENT '文件大小'")
    private Long fileSize;
    @Column(columnDefinition = "varchar(500) COMMENT '父级目录'")
    private String parentDir;
    @Column(columnDefinition = "varchar(500) COMMENT '描述'")
    private String description;


}
