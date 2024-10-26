package dev.animals.dto;

import lombok.Data;

import java.util.List;

@Data
public class PageDataDTO<T> {
    private List<T> data;
    private Long total;
}
