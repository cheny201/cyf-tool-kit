package com.cy.framework.tool.kit.domain.movie;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cy.framework.data.mybatis.domain.BaseModel;
import com.cy.framework.tool.kit.domain.FileInfo;
import com.cy.framework.tool.kit.support.constant.Constant;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Table;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity(name = Movie.TABLE_NAME)
@Table(appliesTo = Movie.TABLE_NAME, comment = "电影信息表")
@TableName(value = Movie.TABLE_NAME)
@javax.persistence.Table(name = Movie.TABLE_NAME,indexes = {
        @Index(name = "idx_1",columnList = "name"),
        @Index(name = "idx_2",columnList = "title"),
})
public class Movie extends BaseModel {
    public final static String TABLE_NAME = Constant.TABLE_PREFIX+"movie";

    @Column(columnDefinition = "varchar(500) COMMENT '名称'")
    private String name;
    @Column(columnDefinition = "varchar(500) COMMENT '标题'")
    private String title;
    @Column(columnDefinition = "varchar(100) COMMENT '国家'")
    private String conutry;
    @Column(columnDefinition = "varchar(100) COMMENT '出品方'")
    private String company;
    @Column(columnDefinition = "longtext COMMENT '描述'")
    private String description;
    @Column(columnDefinition = "varchar(20) COMMENT '发行日期'")
    private String releasedate;
    @Column(columnDefinition = "varchar(100) COMMENT '演员'")
    private String actors;
    @Column(columnDefinition = "longtext COMMENT '图片'")
    private String imgs;
    @Column(columnDefinition = "varchar(500) COMMENT '标签'")
    private String tags;
    @Column(columnDefinition = "longtext COMMENT '信息文件内容'")
    private String nfo;
    @Column(columnDefinition = "varchar(500) COMMENT '信息文件路径'")
    private String nfoPath;

}