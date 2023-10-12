/*     */ package net.optifine.http;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.URL;
/*     */ import java.util.Map;
/*     */ import net.minecraft.client.Minecraft;
/*     */ 
/*     */ public class HttpUtils {
/*  13 */   private static String playerItemsUrl = null;
/*     */   public static final String SERVER_URL = "http://s.optifine.net";
/*     */   public static final String POST_URL = "http://optifine.net";
/*     */   
/*     */   public static byte[] get(String urlStr) throws IOException {
/*     */     byte[] abyte1;
/*  19 */     HttpURLConnection httpurlconnection = null;
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/*  24 */       URL url = new URL(urlStr);
/*  25 */       httpurlconnection = (HttpURLConnection)url.openConnection(Minecraft.getMinecraft().getProxy());
/*  26 */       httpurlconnection.setDoInput(true);
/*  27 */       httpurlconnection.setDoOutput(false);
/*  28 */       httpurlconnection.connect();
/*     */       
/*  30 */       if (httpurlconnection.getResponseCode() / 100 != 2) {
/*     */         
/*  32 */         if (httpurlconnection.getErrorStream() != null)
/*     */         {
/*  34 */           Config.readAll(httpurlconnection.getErrorStream());
/*     */         }
/*     */         
/*  37 */         throw new IOException("HTTP response: " + httpurlconnection.getResponseCode());
/*     */       } 
/*     */       
/*  40 */       InputStream inputstream = httpurlconnection.getInputStream();
/*  41 */       byte[] abyte = new byte[httpurlconnection.getContentLength()];
/*  42 */       int i = 0;
/*     */ 
/*     */       
/*     */       do {
/*  46 */         int j = inputstream.read(abyte, i, abyte.length - i);
/*     */         
/*  48 */         if (j < 0)
/*     */         {
/*  50 */           throw new IOException("Input stream closed: " + urlStr);
/*     */         }
/*     */         
/*  53 */         i += j;
/*     */       }
/*  55 */       while (i < abyte.length);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  61 */       abyte1 = abyte;
/*     */     }
/*     */     finally {
/*     */       
/*  65 */       if (httpurlconnection != null)
/*     */       {
/*  67 */         httpurlconnection.disconnect();
/*     */       }
/*     */     } 
/*     */     
/*  71 */     return abyte1;
/*     */   }
/*     */   
/*     */   public static String post(String urlStr, Map headers, byte[] content) throws IOException {
/*     */     String s3;
/*  76 */     HttpURLConnection httpurlconnection = null;
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/*  81 */       URL url = new URL(urlStr);
/*  82 */       httpurlconnection = (HttpURLConnection)url.openConnection(Minecraft.getMinecraft().getProxy());
/*  83 */       httpurlconnection.setRequestMethod("POST");
/*     */       
/*  85 */       if (headers != null)
/*     */       {
/*  87 */         for (Object s : headers.keySet()) {
/*     */           
/*  89 */           String s1 = "" + headers.get(s);
/*  90 */           httpurlconnection.setRequestProperty((String)s, s1);
/*     */         } 
/*     */       }
/*     */       
/*  94 */       httpurlconnection.setRequestProperty("Content-Type", "text/plain");
/*  95 */       httpurlconnection.setRequestProperty("Content-Length", "" + content.length);
/*  96 */       httpurlconnection.setRequestProperty("Content-Language", "en-US");
/*  97 */       httpurlconnection.setUseCaches(false);
/*  98 */       httpurlconnection.setDoInput(true);
/*  99 */       httpurlconnection.setDoOutput(true);
/* 100 */       OutputStream outputstream = httpurlconnection.getOutputStream();
/* 101 */       outputstream.write(content);
/* 102 */       outputstream.flush();
/* 103 */       outputstream.close();
/* 104 */       InputStream inputstream = httpurlconnection.getInputStream();
/* 105 */       InputStreamReader inputstreamreader = new InputStreamReader(inputstream, "ASCII");
/* 106 */       BufferedReader bufferedreader = new BufferedReader(inputstreamreader);
/* 107 */       StringBuffer stringbuffer = new StringBuffer();
/*     */       
/*     */       String s2;
/* 110 */       while ((s2 = bufferedreader.readLine()) != null) {
/*     */         
/* 112 */         stringbuffer.append(s2);
/* 113 */         stringbuffer.append('\r');
/*     */       } 
/*     */       
/* 116 */       bufferedreader.close();
/* 117 */       s3 = stringbuffer.toString();
/*     */     }
/*     */     finally {
/*     */       
/* 121 */       if (httpurlconnection != null)
/*     */       {
/* 123 */         httpurlconnection.disconnect();
/*     */       }
/*     */     } 
/*     */     
/* 127 */     return s3;
/*     */   }
/*     */ 
/*     */   
/*     */   public static synchronized String getPlayerItemsUrl() {
/* 132 */     if (playerItemsUrl == null) {
/*     */ 
/*     */       
/*     */       try {
/* 136 */         boolean flag = Config.parseBoolean(System.getProperty("player.models.local"), false);
/*     */         
/* 138 */         if (flag)
/*     */         {
/* 140 */           File file1 = (Minecraft.getMinecraft()).mcDataDir;
/* 141 */           File file2 = new File(file1, "playermodels");
/* 142 */           playerItemsUrl = file2.toURI().toURL().toExternalForm();
/*     */         }
/*     */       
/* 145 */       } catch (Exception exception) {
/*     */         
/* 147 */         Config.warn("" + exception.getClass().getName() + ": " + exception.getMessage());
/*     */       } 
/*     */       
/* 150 */       if (playerItemsUrl == null)
/*     */       {
/* 152 */         playerItemsUrl = "http://s.optifine.net";
/*     */       }
/*     */     } 
/*     */     
/* 156 */     return playerItemsUrl;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\optifine\http\HttpUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */