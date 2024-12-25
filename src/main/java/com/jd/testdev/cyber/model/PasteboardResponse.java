package com.jd.testdev.cyber.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @ Author     ：Shawn Pan
 * @ Date       ：Created in 4:33 下午 2022/5/24
 */
@AllArgsConstructor
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
public class PasteboardResponse {
    private String value;
}
