package com.shawn.cyber.service;

/**
 * Author Shawn Pan
 * Date  2024/12/5 21:49
 */
public class WDARequestBuilder {
    public int port;
    public String sessionId;

    public WDARequestBuilder(int port){
        this.port = port;
    }

    public void setSessionId(String sessionId){
        this.sessionId = sessionId;
    }

    private String buildGetRequest(String url,boolean needSession) {
        String s1;
        if (needSession){
            s1 = String.join(" ","GET","/session/"+this.sessionId+url,"HTTP/1.1");
        }else{
            s1 = String.join(" ","GET",url,"HTTP/1.1");
        }
        String s2 = String.join(": ","HOST","localhost:"+port);
        String s3 = String.join(": ","Accept-Encoding", "identity");
        return String.join("\r\n", s1,s2,s3,"", "");
    }

    private String buildPostRequest(String url, String body,boolean needSession) {
        String s1;
        if (needSession){
            s1 = String.join(" ","POST","/session/"+this.sessionId+url,"HTTP/1.1");
        }else{
            s1 = String.join(" ","POST",url,"HTTP/1.1");
        }
        String s2 = String.join(": ","HOST","localhost:"+port);
        String s3 = String.join(": ","Accept-Encoding", "identity");
        String s4 = String.join(": ","Content-Type", "application/json");
        String s5 = String.join(": ", "Content-Length", String.valueOf(body.length()));
        String s6 = String.join("\r\n", s1,s2,s3,s4,s5,"", "");
        return s6 + body;
    }

    public String getRequest(String url){
        return buildGetRequest(url,false);
    }

    public String getRequestWithSession(String url){
        return buildGetRequest(url,true);
    }

    public String postRequest(String url, String body){
        return buildPostRequest(url, body, false);
    }

    public String postRequestWithSession(String url, String body){
        return buildPostRequest(url, body, true);
    }

}
