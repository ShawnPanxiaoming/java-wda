package com.shawn.cyber.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Author Shawn Pan
 * Date  2024/12/9 21:21
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
public class StatusResponse {
    private String sessionId;
    private value value;

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    @NoArgsConstructor
    public class value{
        public ios ios;
        public os os;

        @Getter
        @JsonIgnoreProperties(ignoreUnknown = true)
        @NoArgsConstructor
        public class ios{
            public String ip;
        }
        @Getter
        @JsonIgnoreProperties(ignoreUnknown = true)
        @NoArgsConstructor
        public class os{
            private String version;
        }
    }

}
