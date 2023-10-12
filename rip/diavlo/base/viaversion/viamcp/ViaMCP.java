/*    */ package rip.diavlo.base.viaversion.viamcp;
/*    */ 
/*    */ import java.io.File;
/*    */ import rip.diavlo.base.viaversion.vialoadingbase.ViaLoadingBase;
/*    */ import rip.diavlo.base.viaversion.vialoadingbase.model.ComparableProtocolVersion;
/*    */ import rip.diavlo.base.viaversion.viamcp.gui.AsyncVersionSlider;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ViaMCP
/*    */ {
/*    */   private String lastServer;
/*    */   public static final int NATIVE_VERSION = 47;
/*    */   public static ViaMCP INSTANCE;
/*    */   private AsyncVersionSlider asyncVersionSlider;
/*    */   
/*    */   public static void create() {
/* 31 */     INSTANCE = new ViaMCP();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public ViaMCP() {
/* 37 */     ViaLoadingBase.ViaLoadingBaseBuilder.create().runDirectory(new File("ViaMCP")).nativeVersion(47).onProtocolReload(comparableProtocolVersion -> {
/*    */           if (getAsyncVersionSlider() != null) {
/*    */             getAsyncVersionSlider().setVersion(comparableProtocolVersion.getVersion());
/*    */           }
/* 41 */         }).build();
/*    */   }
/*    */   
/*    */   public void initAsyncSlider() {
/* 45 */     initAsyncSlider(5, 5, 110, 20);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void initAsyncSlider(int x, int y, int width, int height) {
/* 51 */     this.asyncVersionSlider = new AsyncVersionSlider(-1, x, y, Math.max(width, 110), height);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getLastServer() {
/* 56 */     return this.lastServer;
/*    */   }
/*    */   
/*    */   public void setLastServer(String lastServer) {
/* 60 */     this.lastServer = lastServer;
/*    */   }
/*    */   
/*    */   public AsyncVersionSlider getAsyncVersionSlider() {
/* 64 */     return this.asyncVersionSlider;
/*    */   }
/*    */   
/*    */   public static int getVersionNegger() {
/* 68 */     return 47;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\base\viaversion\viamcp\ViaMCP.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */