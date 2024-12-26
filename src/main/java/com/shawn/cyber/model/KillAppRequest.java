package com.shawn.cyber.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ Author     ：Shawn Pan
 * @ Date       ：Created in 18:16 2024/12/11
 */
@Getter
@AllArgsConstructor
public class KillAppRequest {
    private String bundleId;
}
