package com.taotao.service;

import com.taotao.pojo.ZtreeResult;

import java.util.List;

public interface ItemCatService {
    List<ZtreeResult> getZtreeResult(Long id);
}
