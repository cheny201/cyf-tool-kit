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
@Entity(name = MovieActor.TABLE_NAME)
@Table(appliesTo = MovieActor.TABLE_NAME, comment = "电影-演员关联表")
@TableName(value = MovieActor.TABLE_NAME)
@javax.persistence.Table(name = MovieActor.TABLE_NAME,indexes = {
        @Index(name = "idx_1",columnList = "movieId"),
        @Index(name = "idx_2",columnList = "actorId"),
})
public class MovieActor extends BaseModel {
    public final static String TABLE_NAME = Constant.TABLE_PREFIX+"movie_actor";

    @Column(columnDefinition = "varchar(100) not null COMMENT '电影ID'")
    private String movieId;
    @Column(columnDefinition = "varchar(100) not null COMMENT '演员ID'")
    private String actorId;

}
