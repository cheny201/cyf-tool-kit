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
@Entity(name = Actor.TABLE_NAME)
@Table(appliesTo = Actor.TABLE_NAME, comment = "电影-演员信息表")
@TableName(value = Actor.TABLE_NAME)
@javax.persistence.Table(name = Actor.TABLE_NAME,indexes = {
        @Index(name = "idx_1",columnList = "name"),
})
public class Actor extends BaseModel {
    public final static String TABLE_NAME = Constant.TABLE_PREFIX+"actor";

    @Column(columnDefinition = "varchar(100) not null COMMENT '名称'")
    private String name;
    @Column(columnDefinition = "varchar(100) COMMENT '职位'")
    private String job;

}
