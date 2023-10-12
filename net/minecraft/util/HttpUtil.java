/*     */ package net.minecraft.util;
/*     */ import com.google.common.util.concurrent.ListenableFuture;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStream;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.Proxy;
/*     */ import java.net.ServerSocket;
/*     */ import java.net.URL;
/*     */ import java.util.Map;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import org.apache.commons.io.FileUtils;
/*     */ import org.apache.commons.io.IOUtils;
/*     */ 
/*     */ public class HttpUtil {
/*  22 */   public static final ListeningExecutorService field_180193_a = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool((new ThreadFactoryBuilder()).setDaemon(true).setNameFormat("Downloader %d").build()));
/*  23 */   private static final AtomicInteger downloadThreadsStarted = new AtomicInteger(0);
/*  24 */   private static final Logger logger = LogManager.getLogger();
/*     */ 
/*     */   
/*     */   public static String buildPostString(Map<String, Object> data) {
/*  28 */     StringBuilder stringbuilder = new StringBuilder();
/*     */     
/*  30 */     for (Map.Entry<String, Object> entry : data.entrySet()) {
/*     */       
/*  32 */       if (stringbuilder.length() > 0)
/*     */       {
/*  34 */         stringbuilder.append('&');
/*     */       }
/*     */ 
/*     */       
/*     */       try {
/*  39 */         stringbuilder.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
/*     */       }
/*  41 */       catch (UnsupportedEncodingException unsupportedencodingexception1) {
/*     */         
/*  43 */         unsupportedencodingexception1.printStackTrace();
/*     */       } 
/*     */       
/*  46 */       if (entry.getValue() != null) {
/*     */         
/*  48 */         stringbuilder.append('=');
/*     */ 
/*     */         
/*     */         try {
/*  52 */           stringbuilder.append(URLEncoder.encode(entry.getValue().toString(), "UTF-8"));
/*     */         }
/*  54 */         catch (UnsupportedEncodingException unsupportedencodingexception) {
/*     */           
/*  56 */           unsupportedencodingexception.printStackTrace();
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/*  61 */     return stringbuilder.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public static String postMap(URL url, Map<String, Object> data, boolean skipLoggingErrors) {
/*  66 */     return post(url, buildPostString(data), skipLoggingErrors);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static String post(URL url, String content, boolean skipLoggingErrors) {
/*     */     try {
/*  73 */       Proxy proxy = (MinecraftServer.getServer() == null) ? null : MinecraftServer.getServer().getServerProxy();
/*     */       
/*  75 */       if (proxy == null)
/*     */       {
/*  77 */         proxy = Proxy.NO_PROXY;
/*     */       }
/*     */       
/*  80 */       HttpURLConnection httpurlconnection = (HttpURLConnection)url.openConnection(proxy);
/*  81 */       httpurlconnection.setRequestMethod("POST");
/*  82 */       httpurlconnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
/*  83 */       httpurlconnection.setRequestProperty("Content-Length", "" + (content.getBytes()).length);
/*  84 */       httpurlconnection.setRequestProperty("Content-Language", "en-US");
/*  85 */       httpurlconnection.setUseCaches(false);
/*  86 */       httpurlconnection.setDoInput(true);
/*  87 */       httpurlconnection.setDoOutput(true);
/*  88 */       DataOutputStream dataoutputstream = new DataOutputStream(httpurlconnection.getOutputStream());
/*  89 */       dataoutputstream.writeBytes(content);
/*  90 */       dataoutputstream.flush();
/*  91 */       dataoutputstream.close();
/*  92 */       BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(httpurlconnection.getInputStream()));
/*  93 */       StringBuffer stringbuffer = new StringBuffer();
/*     */       
/*     */       String s;
/*  96 */       while ((s = bufferedreader.readLine()) != null) {
/*     */         
/*  98 */         stringbuffer.append(s);
/*  99 */         stringbuffer.append('\r');
/*     */       } 
/*     */       
/* 102 */       bufferedreader.close();
/* 103 */       return stringbuffer.toString();
/*     */     }
/* 105 */     catch (Exception exception) {
/*     */       
/* 107 */       if (!skipLoggingErrors)
/*     */       {
/* 109 */         logger.error("Could not post to " + url, exception);
/*     */       }
/*     */       
/* 112 */       return "";
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static ListenableFuture<Object> downloadResourcePack(final File saveFile, final String packUrl, final Map<String, String> p_180192_2_, final int maxSize, final IProgressUpdate p_180192_4_, final Proxy p_180192_5_) {
/* 118 */     ListenableFuture<?> listenablefuture = field_180193_a.submit(new Runnable()
/*     */         {
/*     */           public void run()
/*     */           {
/* 122 */             HttpURLConnection httpurlconnection = null;
/* 123 */             InputStream inputstream = null;
/* 124 */             OutputStream outputstream = null;
/*     */             
/* 126 */             if (p_180192_4_ != null) {
/*     */               
/* 128 */               p_180192_4_.resetProgressAndMessage("Downloading Resource Pack");
/* 129 */               p_180192_4_.displayLoadingString("Making Request...");
/*     */             } 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*     */             try {
/* 136 */               byte[] abyte = new byte[4096];
/* 137 */               URL url = new URL(packUrl);
/* 138 */               httpurlconnection = (HttpURLConnection)url.openConnection(p_180192_5_);
/* 139 */               float f = 0.0F;
/* 140 */               float f1 = p_180192_2_.entrySet().size();
/*     */               
/* 142 */               for (Map.Entry<String, String> entry : (Iterable<Map.Entry<String, String>>)p_180192_2_.entrySet()) {
/*     */                 
/* 144 */                 httpurlconnection.setRequestProperty(entry.getKey(), entry.getValue());
/*     */                 
/* 146 */                 if (p_180192_4_ != null)
/*     */                 {
/* 148 */                   p_180192_4_.setLoadingProgress((int)(++f / f1 * 100.0F));
/*     */                 }
/*     */               } 
/*     */               
/* 152 */               inputstream = httpurlconnection.getInputStream();
/* 153 */               f1 = httpurlconnection.getContentLength();
/* 154 */               int i = httpurlconnection.getContentLength();
/*     */               
/* 156 */               if (p_180192_4_ != null)
/*     */               {
/* 158 */                 p_180192_4_.displayLoadingString(String.format("Downloading file (%.2f MB)...", new Object[] { Float.valueOf(f1 / 1000.0F / 1000.0F) }));
/*     */               }
/*     */               
/* 161 */               if (saveFile.exists()) {
/*     */                 
/* 163 */                 long j = saveFile.length();
/*     */                 
/* 165 */                 if (j == i) {
/*     */                   
/* 167 */                   if (p_180192_4_ != null)
/*     */                   {
/* 169 */                     p_180192_4_.setDoneWorking();
/*     */                   }
/*     */                   
/*     */                   return;
/*     */                 } 
/*     */                 
/* 175 */                 HttpUtil.logger.warn("Deleting " + saveFile + " as it does not match what we currently have (" + i + " vs our " + j + ").");
/* 176 */                 FileUtils.deleteQuietly(saveFile);
/*     */               }
/* 178 */               else if (saveFile.getParentFile() != null) {
/*     */                 
/* 180 */                 saveFile.getParentFile().mkdirs();
/*     */               } 
/*     */               
/* 183 */               outputstream = new DataOutputStream(new FileOutputStream(saveFile));
/*     */               
/* 185 */               if (maxSize > 0 && f1 > maxSize) {
/*     */                 
/* 187 */                 if (p_180192_4_ != null)
/*     */                 {
/* 189 */                   p_180192_4_.setDoneWorking();
/*     */                 }
/*     */                 
/* 192 */                 throw new IOException("Filesize is bigger than maximum allowed (file is " + f + ", limit is " + maxSize + ")");
/*     */               } 
/*     */               
/* 195 */               int k = 0;
/*     */               
/* 197 */               while ((k = inputstream.read(abyte)) >= 0) {
/*     */                 
/* 199 */                 f += k;
/*     */                 
/* 201 */                 if (p_180192_4_ != null)
/*     */                 {
/* 203 */                   p_180192_4_.setLoadingProgress((int)(f / f1 * 100.0F));
/*     */                 }
/*     */                 
/* 206 */                 if (maxSize > 0 && f > maxSize) {
/*     */                   
/* 208 */                   if (p_180192_4_ != null)
/*     */                   {
/* 210 */                     p_180192_4_.setDoneWorking();
/*     */                   }
/*     */                   
/* 213 */                   throw new IOException("Filesize was bigger than maximum allowed (got >= " + f + ", limit was " + maxSize + ")");
/*     */                 } 
/*     */                 
/* 216 */                 if (Thread.interrupted()) {
/*     */                   
/* 218 */                   HttpUtil.logger.error("INTERRUPTED");
/*     */                   
/* 220 */                   if (p_180192_4_ != null)
/*     */                   {
/* 222 */                     p_180192_4_.setDoneWorking();
/*     */                   }
/*     */                   
/*     */                   return;
/*     */                 } 
/*     */                 
/* 228 */                 outputstream.write(abyte, 0, k);
/*     */               } 
/*     */               
/* 231 */               if (p_180192_4_ != null) {
/*     */                 
/* 233 */                 p_180192_4_.setDoneWorking();
/*     */                 
/*     */                 return;
/*     */               } 
/* 237 */             } catch (Throwable throwable) {
/*     */               
/* 239 */               throwable.printStackTrace();
/*     */               
/* 241 */               if (httpurlconnection != null) {
/*     */                 
/* 243 */                 InputStream inputstream1 = httpurlconnection.getErrorStream();
/*     */ 
/*     */                 
/*     */                 try {
/* 247 */                   HttpUtil.logger.error(IOUtils.toString(inputstream1));
/*     */                 }
/* 249 */                 catch (IOException ioexception) {
/*     */                   
/* 251 */                   ioexception.printStackTrace();
/*     */                 } 
/*     */               } 
/*     */               
/* 255 */               if (p_180192_4_ != null) {
/*     */                 
/* 257 */                 p_180192_4_.setDoneWorking();
/*     */ 
/*     */ 
/*     */                 
/*     */                 return;
/*     */               } 
/*     */             } finally {
/* 264 */               IOUtils.closeQuietly(inputstream);
/* 265 */               IOUtils.closeQuietly(outputstream);
/*     */             } 
/*     */           }
/*     */         });
/* 269 */     return (ListenableFuture)listenablefuture;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int getSuitableLanPort() throws IOException {
/* 274 */     ServerSocket serversocket = null;
/* 275 */     int i = -1;
/*     */ 
/*     */     
/*     */     try {
/* 279 */       serversocket = new ServerSocket(0);
/* 280 */       i = serversocket.getLocalPort();
/*     */     } finally {
/*     */ 
/*     */       
/*     */       try {
/*     */         
/* 286 */         if (serversocket != null)
/*     */         {
/* 288 */           serversocket.close();
/*     */         }
/*     */       }
/* 291 */       catch (IOException iOException) {}
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 297 */     return i;
/*     */   }
/*     */ 
/*     */   
/*     */   public static String get(URL url) throws IOException {
/* 302 */     HttpURLConnection httpurlconnection = (HttpURLConnection)url.openConnection();
/* 303 */     httpurlconnection.setRequestMethod("GET");
/* 304 */     BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(httpurlconnection.getInputStream()));
/* 305 */     StringBuilder stringbuilder = new StringBuilder();
/*     */     
/*     */     String s;
/* 308 */     while ((s = bufferedreader.readLine()) != null) {
/*     */       
/* 310 */       stringbuilder.append(s);
/* 311 */       stringbuilder.append('\r');
/*     */     } 
/*     */     
/* 314 */     bufferedreader.close();
/* 315 */     return stringbuilder.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraf\\util\HttpUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */