package com.arma.inz.compcal;


import lombok.Data;

import java.util.List;

@Data
public class PageResultImpl<T> {

    private int offset;
    private int totalSize;
    private List<T> content;

    public PageResultImpl() {

    }

    public PageResultImpl(List<T> content) {
        this.content = content;
    }

    public PageResultImpl(List<T> content, int offset, int totalSize) {
        this.content = content;
        this.offset = offset;
        this.totalSize = totalSize;
    }
}