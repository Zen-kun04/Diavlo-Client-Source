/*    */ package com.mcping;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MinecraftPingOptions
/*    */ {
/*    */   private String hostname;
/*  8 */   private int port = 25565;
/*  9 */   private int timeout = 5000;
/* 10 */   private final String charset = "UTF-8";
/*    */   
/*    */   public MinecraftPingOptions setHostname(String hostname) {
/* 13 */     this.hostname = hostname;
/* 14 */     return this;
/*    */   }
/*    */   
/*    */   public MinecraftPingOptions setTimeout(int timeout) {
/* 18 */     this.timeout = timeout;
/* 19 */     return this;
/*    */   }
/*    */   
/*    */   public MinecraftPingOptions setPort(int port) {
/* 23 */     this.port = port;
/* 24 */     return this;
/*    */   }
/*    */   
/*    */   public String getHostname() {
/* 28 */     return this.hostname;
/*    */   }
/*    */   
/*    */   public int getPort() {
/* 32 */     return this.port;
/*    */   }
/*    */   
/*    */   public int getTimeout() {
/* 36 */     return this.timeout;
/*    */   }
/*    */   
/*    */   public String getCharset() {
/* 40 */     return "UTF-8";
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\mcping\MinecraftPingOptions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */