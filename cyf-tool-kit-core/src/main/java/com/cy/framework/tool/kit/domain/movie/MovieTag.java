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
@Entity(name = MovieTag.TABLE_NAME)
@Table(appliesTo = MovieTag.TABLE_NAME, comment = "电影-标签关联表")
@TableName(value = MovieTag.TABLE_NAME)
@javax.persistence.Table(name = MovieTag.TABLE_NAME,indexes = {
        @Index(name = "idx_1",columnList = "movieId"),
        @Index(name = "idx_2",columnList = "tagId"),
})
public class MovieTag extends BaseModel {
    public final static String TABLE_NAME = Constant.TABLE_PREFIX+"movie_tag";

    @Column(columnDefinition = "varchar(100) not null COMMENT '电影ID'")
    private String movieId;
    @Column(columnDefinition = "varchar(100) not null COMMENT '标签ID'")
    private String tagId;

}
