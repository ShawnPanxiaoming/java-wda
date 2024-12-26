package com.shawn.cyber.model;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * Author Shawn Pan
 * Date  2024/12/9 17:58
 */
@Getter
@Setter
public class SessionBody {
    private Map capabilities;

    public SessionBody(){
        this.capabilities = new HashMap();
    }
}
