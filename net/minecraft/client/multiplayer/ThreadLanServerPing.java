/*     */ package net.minecraft.client.multiplayer;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.DatagramPacket;
/*     */ import java.net.DatagramSocket;
/*     */ import java.net.InetAddress;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class ThreadLanServerPing
/*     */   extends Thread {
/*  13 */   private static final AtomicInteger field_148658_a = new AtomicInteger(0);
/*  14 */   private static final Logger logger = LogManager.getLogger();
/*     */   
/*     */   private final String motd;
/*     */   private final DatagramSocket socket;
/*     */   private boolean isStopping = true;
/*     */   private final String address;
/*     */   
/*     */   public ThreadLanServerPing(String p_i1321_1_, String p_i1321_2_) throws IOException {
/*  22 */     super("LanServerPinger #" + field_148658_a.incrementAndGet());
/*  23 */     this.motd = p_i1321_1_;
/*  24 */     this.address = p_i1321_2_;
/*  25 */     setDaemon(true);
/*  26 */     this.socket = new DatagramSocket();
/*     */   }
/*     */ 
/*     */   
/*     */   public void run() {
/*  31 */     String s = getPingResponse(this.motd, this.address);
/*  32 */     byte[] abyte = s.getBytes();
/*     */     
/*  34 */     while (!isInterrupted() && this.isStopping) {
/*     */ 
/*     */       
/*     */       try {
/*  38 */         InetAddress inetaddress = InetAddress.getByName("224.0.2.60");
/*  39 */         DatagramPacket datagrampacket = new DatagramPacket(abyte, abyte.length, inetaddress, 4445);
/*  40 */         this.socket.send(datagrampacket);
/*     */       }
/*  42 */       catch (IOException ioexception) {
/*     */         
/*  44 */         logger.warn("LanServerPinger: " + ioexception.getMessage());
/*     */         
/*     */         break;
/*     */       } 
/*     */       
/*     */       try {
/*  50 */         sleep(1500L);
/*     */       }
/*  52 */       catch (InterruptedException interruptedException) {}
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void interrupt() {
/*  61 */     super.interrupt();
/*  62 */     this.isStopping = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public static String getPingResponse(String p_77525_0_, String p_77525_1_) {
/*  67 */     return "[MOTD]" + p_77525_0_ + "[/MOTD][AD]" + p_77525_1_ + "[/AD]";
/*     */   }
/*     */ 
/*     */   
/*     */   public static String getMotdFromPingResponse(String p_77524_0_) {
/*  72 */     int i = p_77524_0_.indexOf("[MOTD]");
/*     */     
/*  74 */     if (i < 0)
/*     */     {
/*  76 */       return "missing no";
/*     */     }
/*     */ 
/*     */     
/*  80 */     int j = p_77524_0_.indexOf("[/MOTD]", i + "[MOTD]".length());
/*  81 */     return (j < i) ? "missing no" : p_77524_0_.substring(i + "[MOTD]".length(), j);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getAdFromPingResponse(String p_77523_0_) {
/*  87 */     int i = p_77523_0_.indexOf("[/MOTD]");
/*     */     
/*  89 */     if (i < 0)
/*     */     {
/*  91 */       return null;
/*     */     }
/*     */ 
/*     */     
/*  95 */     int j = p_77523_0_.indexOf("[/MOTD]", i + "[/MOTD]".length());
/*     */     
/*  97 */     if (j >= 0)
/*     */     {
/*  99 */       return null;
/*     */     }
/*     */ 
/*     */     
/* 103 */     int k = p_77523_0_.indexOf("[AD]", i + "[/MOTD]".length());
/*     */     
/* 105 */     if (k < 0)
/*     */     {
/* 107 */       return null;
/*     */     }
/*     */ 
/*     */     
/* 111 */     int l = p_77523_0_.indexOf("[/AD]", k + "[AD]".length());
/* 112 */     return (l < k) ? null : p_77523_0_.substring(k + "[AD]".length(), l);
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\multiplayer\ThreadLanServerPing.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */