/*    */ package net.optifine.http;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ import java.net.InetSocketAddress;
/*    */ import java.net.Proxy;
/*    */ import java.net.Socket;
/*    */ import java.nio.charset.Charset;
/*    */ import java.util.Map;
/*    */ 
/*    */ public class HttpPipelineSender
/*    */   extends Thread {
/* 13 */   private HttpPipelineConnection httpPipelineConnection = null;
/*    */   private static final String CRLF = "\r\n";
/* 15 */   private static Charset ASCII = Charset.forName("ASCII");
/*    */ 
/*    */   
/*    */   public HttpPipelineSender(HttpPipelineConnection httpPipelineConnection) {
/* 19 */     super("HttpPipelineSender");
/* 20 */     this.httpPipelineConnection = httpPipelineConnection;
/*    */   }
/*    */ 
/*    */   
/*    */   public void run() {
/* 25 */     HttpPipelineRequest httppipelinerequest = null;
/*    */ 
/*    */     
/*    */     try {
/* 29 */       connect();
/*    */       
/* 31 */       while (!Thread.interrupted())
/*    */       {
/* 33 */         httppipelinerequest = this.httpPipelineConnection.getNextRequestSend();
/* 34 */         HttpRequest httprequest = httppipelinerequest.getHttpRequest();
/* 35 */         OutputStream outputstream = this.httpPipelineConnection.getOutputStream();
/* 36 */         writeRequest(httprequest, outputstream);
/* 37 */         this.httpPipelineConnection.onRequestSent(httppipelinerequest);
/*    */       }
/*    */     
/* 40 */     } catch (InterruptedException var4) {
/*    */       
/*    */       return;
/*    */     }
/* 44 */     catch (Exception exception) {
/*    */       
/* 46 */       this.httpPipelineConnection.onExceptionSend(httppipelinerequest, exception);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   private void connect() throws IOException {
/* 52 */     String s = this.httpPipelineConnection.getHost();
/* 53 */     int i = this.httpPipelineConnection.getPort();
/* 54 */     Proxy proxy = this.httpPipelineConnection.getProxy();
/* 55 */     Socket socket = new Socket(proxy);
/* 56 */     socket.connect(new InetSocketAddress(s, i), 5000);
/* 57 */     this.httpPipelineConnection.setSocket(socket);
/*    */   }
/*    */ 
/*    */   
/*    */   private void writeRequest(HttpRequest req, OutputStream out) throws IOException {
/* 62 */     write(out, req.getMethod() + " " + req.getFile() + " " + req.getHttp() + "\r\n");
/* 63 */     Map<String, String> map = req.getHeaders();
/*    */     
/* 65 */     for (String s : map.keySet()) {
/*    */       
/* 67 */       String s1 = req.getHeaders().get(s);
/* 68 */       write(out, s + ": " + s1 + "\r\n");
/*    */     } 
/*    */     
/* 71 */     write(out, "\r\n");
/*    */   }
/*    */ 
/*    */   
/*    */   private void write(OutputStream out, String str) throws IOException {
/* 76 */     byte[] abyte = str.getBytes(ASCII);
/* 77 */     out.write(abyte);
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\optifine\http\HttpPipelineSender.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */