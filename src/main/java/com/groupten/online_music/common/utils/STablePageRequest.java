package com.groupten.online_music.common.utils;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import java.util.Map;

@ApiModel(value = "STablePageRequest")
public class STablePageRequest {
    @ApiModelProperty(value = "页码", example = "1")
    private int p = 1;
    @ApiModelProperty(value = "最大页面记录数", example = "10")
    private int ps = 10;
    @ApiModelProperty(value = "排序字段,+升序,-降序,默认uid降序", example = "-uid")
    private String ordering = "-uid";

    public STablePageRequest(Map<String, String> pagingMap) {
        String tP = pagingMap.get("p");
        String tPs = pagingMap.get("ps");
        String tOrdering = pagingMap.get("ordering");

        if (tP != null) this.p = Integer.parseInt(tP);
        if (tPs != null) this.ps = Integer.parseInt(tPs);
        if (tOrdering != null) this.ordering = tOrdering;
    }

    public int getP() {
        return p;
    }

    public void setP(int p) {
        this.p = p;
    }

    public int getPs() {
        return ps;
    }

    public void setPs(int ps) {
        this.ps = ps;
    }

    public String getOrdering() {
        return ordering;
    }

    public void setOrdering(String ordering) {
        this.ordering = ordering;
    }

    public Pageable sTablePageable() {
        Direction direction = Direction.DESC;
        String sortOrder = this.ordering.substring(0, 1);
        String sortField = this.ordering.substring(1);

        if (!sortOrder.equals("-")) {
            direction = Direction.ASC;
        } else {
            direction = Direction.DESC;
        }

        return PageRequest.of(this.p - 1, this.ps, Sort.by(direction, sortField));
    }
}
