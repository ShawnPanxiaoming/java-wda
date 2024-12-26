package com.shawn.cyber.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ Author     ：Shawn Pan
 * @ Date       ：Created in 1:25 下午 2024/12/11
 */
@AllArgsConstructor
@Getter
public class IOSSwipeRequest {
    private int fromX;
    private int fromY;
    private int toX;
    private int toY;
    private int duration;
}
