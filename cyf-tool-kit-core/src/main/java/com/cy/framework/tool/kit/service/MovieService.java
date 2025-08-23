package com.cy.framework.tool.kit.service;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.XmlUtil;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cy.framework.common.constant.Constant;
import com.cy.framework.tool.kit.domain.FileInfo;
import com.cy.framework.tool.kit.domain.movie.*;
import com.cy.framework.tool.kit.mapper.movie.MovieMapper;
import com.cy.framework.tool.kit.mapper.movie.TagMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MovieService extends ServiceImpl<MovieMapper, Movie> {

    @Autowired
    private FileInfoService fileInfoService;
    @Autowired
    private ActorService actorService;
    @Autowired
    private MovieActorService movieActorService;
    @Autowired
    private TagService tagService;
    @Autowired
    private MovieTagService movieTagService;

    public void parse(String deviceName){
        Map<String,Actor> actorMap = actorService.list().stream().collect(Collectors.toMap(Actor::getName,actor->actor));
        Map<String, Tag> tagMap = tagService.list().stream().collect(Collectors.toMap(Tag::getName,tag->tag));
        Map<String,Movie> movieMap = this.list().stream().collect(Collectors.toMap(Movie::getNfoPath,movie->movie));
        Map<String,List<FileInfo>> map = fileInfoService.list(Wrappers.lambdaQuery(FileInfo.class).eq(FileInfo::getDeviceName,deviceName)).stream().collect(Collectors.groupingBy(FileInfo::getParentDir));
        List<Movie> saveLs = Lists.newArrayList();
        List<Movie> updateLs = Lists.newArrayList();
        map.forEach((name,ls)->{
            Movie movie = movieMap.get(name);
            if(movie == null){
                movie = new Movie();
                movie.setId(IdWorker.getIdStr());
                saveLs.add(movie);
            }else{
                updateLs.add(movie);
            }
            FileInfo nfo = null;
            for (FileInfo r : ls) {
                if(StringUtils.equals(r.getFileType(),"nfo")){
                    nfo = r;
                    break;
                }
            }
            if(nfo != null){
                movie.setNfoPath(nfo.getAbsolutePath());
                process(movie,movie.getNfoPath(),actorMap,tagMap);
            }
        });
        if(!saveLs.isEmpty()){
            this.saveBatch(saveLs);
        }
        if(!updateLs.isEmpty()){
            this.updateBatchById(updateLs);
        }
        List<Actor> actors = actorMap.values().stream().filter(v->v.getCreateTime() == null).collect(Collectors.toList());
        if(!actors.isEmpty()){
            actorService.saveBatch(actors);
        }
        List<Tag> tags = tagMap.values().stream().filter(v->v.getCreateTime() == null).collect(Collectors.toList());
        if(!tags.isEmpty()){
            tagService.saveBatch(tags);
        }
    }


    /**
     * 处理单个视频
     * @param movie
     * @param nfoPath
     * @param actorMap
     * @param tagMap
     */
    private void process(Movie movie, String nfoPath, Map<String, Actor> actorMap,Map<String, Tag> tagMap){
        String nfo = FileUtil.readString(nfoPath, Constant.DEFAULT_CHARSET);
        Document document = XmlUtil.readXML(nfo);
        Element element = document.getDocumentElement();
        /***************** 处理视频信息 *******************/
        movie.setTitle(element.getElementsByTagName("title").item(0).getTextContent());
        movie.setCompany(element.getElementsByTagName("studio").item(0).getTextContent());
        movie.setDescription(element.getElementsByTagName("plot").item(0).getTextContent());
        movie.setReleasedate(element.getElementsByTagName("releasedate").item(0).getTextContent());
        movie.setNfo(nfo);

        /***************** 处理标签信息 *******************/
        NodeList nodeList = element.getElementsByTagName("tag");
        List<MovieTag> movieTags = Lists.newArrayList();
        if(nodeList != null && nodeList.getLength() > 0){
            Tag tag = null;
            Set<String> tagNames = Sets.newHashSet();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                tagNames.add(node.getTextContent());
                tag = tagMap.get(node.getTextContent());
                if(tag == null){
                    tag = new Tag();
                    tag.setName(node.getTextContent());
                    tag.setId(IdWorker.getIdStr());
                    tagMap.put(node.getTextContent(),tag);
                }

                MovieTag movieTag = new MovieTag();
                movieTag.setTagId(tag.getId());
                movieTag.setMovieId(movie.getId());
                movieTags.add(movieTag);
            }
            movie.setTags(StringUtils.join(tagNames, Constant.SPLIT_STR));
        }
        movieTagService.remove(Wrappers.lambdaQuery(MovieTag.class).eq(MovieTag::getMovieId,movie.getId()));
        movieTagService.saveOrUpdateBatch(movieTags);

        /***************** 处理演员信息 *******************/
        nodeList = element.getElementsByTagName("actor");
        List<MovieActor> movieActors = Lists.newArrayList();
        if(nodeList != null && nodeList.getLength() > 0){
            Actor actor = null;
            Set<String> actorNames = Sets.newHashSet();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                NodeList children = node.getChildNodes();
                String name = null;
                String type = null;
                for (int j = 0; j < children.getLength(); j++) {
                    Node child = children.item(j);
                    if(StringUtils.equals("name",child.getNodeName())){
                        name = child.getTextContent();
                    }else if(StringUtils.equals("type",child.getNodeName())){
                        type = child.getTextContent();
                    }
                }
                actorNames.add(name);
                actor = actorMap.get(name);
                if(actor == null){
                    actor = new Actor();
                    actor.setName(name);
                    actor.setJob(type);
                    actor.setId(IdWorker.getIdStr());
                    actorMap.put(name,actor);
                }
                MovieActor m = new MovieActor();
                m.setActorId(actor.getId());
                m.setMovieId(movie.getId());
                movieActors.add(m);
            }
            movie.setActors(StringUtils.join(actorNames, Constant.SPLIT_STR));
        }
        movieActorService.remove(Wrappers.lambdaQuery(MovieActor.class).eq(MovieActor::getMovieId,movie.getId()));
        movieActorService.saveOrUpdateBatch(movieActors);
        super.saveOrUpdate(movie);
    }
}
