package com.brsrker.emerald.jwt.auth.util.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListResultDTO<E> {

    private int pages;
    private long count;
    private List<E> list;

}
