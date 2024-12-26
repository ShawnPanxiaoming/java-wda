package com.shawn.cyber.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @ Author     ：Shawn Pan
 * @ Date       ：Created in 8:02 下午 2022/9/6
 */
@Getter
@Setter
public class FindElementRequest {
    private String using;
    private String value;

    public FindElementRequest(String using, String xpath){
        this.using = using;
        this.value = xpath;
    }
}
