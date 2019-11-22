package com.groupten.online_music.common.utils;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

@ApiModel(value = "STablePageRequest")
public class STablePageRequest {
    @ApiModelProperty(value = "页码", example = "1")
    private int pageNo = 1;
    @ApiModelProperty(value = "最大页面记录数", example = "10")
    private int pageSize = 10;
    @ApiModelProperty(value = "排序字段", example = "id")
    private String sortField = "id";
    @ApiModelProperty(value = "升序或降序", example = "desc")
    private String sortOrder = "desc";

    public STablePageRequest(int pageNo, int pageSize, String sortField, String sortOrder) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.sortField = sortField;
        this.sortOrder = sortOrder;
    }

    public int getPageNo() {
        return pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public String getSortField() {
        return sortField;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Pageable sTablePageable() {
        Direction direction = Direction.DESC;
        if (!this.sortOrder.equals("desc")) {
            direction = Direction.ASC;
        };

        return PageRequest.of(this.pageNo - 1, this.pageSize, Sort.by(direction, sortField));
    }
}
