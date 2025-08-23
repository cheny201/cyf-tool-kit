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
@Entity(name = Tag.TABLE_NAME)
@Table(appliesTo = Tag.TABLE_NAME, comment = "电影-标签信息表")
@TableName(value = Tag.TABLE_NAME)
@javax.persistence.Table(name = Tag.TABLE_NAME,indexes = {
        @Index(name = "idx_1",columnList = "name"),
})
public class Tag extends BaseModel {
    public final static String TABLE_NAME = Constant.TABLE_PREFIX+"tag";

    @Column(columnDefinition = "varchar(100) not null COMMENT '名称'")
    private String name;

}
