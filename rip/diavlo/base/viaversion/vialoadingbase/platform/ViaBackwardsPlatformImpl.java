/*    */ package rip.diavlo.base.viaversion.vialoadingbase.platform;
/*    */ 
/*    */ import com.viaversion.viabackwards.api.ViaBackwardsPlatform;
/*    */ import java.io.File;
/*    */ import java.util.logging.Logger;
/*    */ import rip.diavlo.base.viaversion.vialoadingbase.ViaLoadingBase;
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
/*    */ 
/*    */ public class ViaBackwardsPlatformImpl
/*    */   implements ViaBackwardsPlatform
/*    */ {
/*    */   private final File directory;
/*    */   
/*    */   public ViaBackwardsPlatformImpl(File directory) {
/* 30 */     init(this.directory = directory);
/*    */   }
/*    */ 
/*    */   
/*    */   public Logger getLogger() {
/* 35 */     return ViaLoadingBase.LOGGER;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isOutdated() {
/* 40 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public void disable() {}
/*    */ 
/*    */   
/*    */   public File getDataFolder() {
/* 48 */     return this.directory;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\base\viaversion\vialoadingbase\platform\ViaBackwardsPlatformImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */