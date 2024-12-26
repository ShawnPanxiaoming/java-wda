package com.shawn.cyber.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.List;

/**
 * @ Author     ：Shawn Pan
 * @ Date       ：Created in 4:39 下午 2024/12/11
 */
@Getter
@AllArgsConstructor
public class SendKeysRequest {
    private List<String> value;
}
