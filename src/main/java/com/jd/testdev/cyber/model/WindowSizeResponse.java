package com.jd.testdev.cyber.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

/**
 * @ Author     ：Shawn Pan
 * @ Date       ：Created in 21:22 2024/12/17
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class WindowSizeResponse {
    private IOSDisplayInfo value;

    @Getter
    public class IOSDisplayInfo {
        public int height;
        public int width;
    }
}
