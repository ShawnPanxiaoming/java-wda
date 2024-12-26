package com.shawn.cyber.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * @ Author     ：Shawn Pan
 * @ Date       ：Created in 18:25 2034/12/17
 */
@AllArgsConstructor
@Getter
@Setter
public class IOSLaunchAppRequest {
    private String bundleId;
    private boolean shouldWaitForQuiescence = false;
    private String[] arguments =new String[0];
    private Map environment = new HashMap();

    public IOSLaunchAppRequest(String bundleId, Map<String,Object> environment){
        this.bundleId = bundleId;
        if (environment != null) this.environment = environment;
    }
}
