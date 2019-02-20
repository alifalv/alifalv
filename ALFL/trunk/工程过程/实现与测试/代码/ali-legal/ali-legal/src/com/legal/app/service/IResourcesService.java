package com.legal.app.service;

import com.legal.app.model.Resources;

import java.util.List;

public interface IResourcesService {
    void update(List<Resources> resources, Integer belongId, String type) throws Exception;

    List<Resources> list(Integer belongId, String type);
}
