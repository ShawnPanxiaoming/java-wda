package com.jd.testdev.cyber.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @ Author     ：Shawn Pan
 * @ Date       ：Created in 18:33 2024/12/17
 */
@Getter
public class LongTapRequest extends TapRequest{
    private float duration = 3.0f;

    public LongTapRequest(int x, int y){
        super(x,y);
    }

}
