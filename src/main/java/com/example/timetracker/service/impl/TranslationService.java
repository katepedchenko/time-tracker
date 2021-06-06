package com.example.timetracker.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TranslationService {

    @Autowired
    private ModelMapper mapper;

    public <T> T translate(Object srcObj, Class<T> destClass) {
        return mapper.map(srcObj, destClass);
    }

    public void map(Object srcObj, Object destObj) {
        mapper.map(srcObj, destObj);
    }

    public <From, To> List<To> translateList(Collection<From> objects, Class<To> dtoType) {
        return objects.stream()
                .map(e -> translate(e, dtoType))
                .collect(Collectors.toList());
    }
}
