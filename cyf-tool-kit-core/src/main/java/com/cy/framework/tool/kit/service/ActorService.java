package com.cy.framework.tool.kit.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cy.framework.tool.kit.domain.movie.Actor;
import com.cy.framework.tool.kit.mapper.movie.ActorMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class ActorService extends ServiceImpl<ActorMapper, Actor> {
}
