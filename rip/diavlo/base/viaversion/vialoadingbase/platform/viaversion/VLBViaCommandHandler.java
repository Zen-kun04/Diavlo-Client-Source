/*    */ package rip.diavlo.base.viaversion.vialoadingbase.platform.viaversion;
/*    */ 
/*    */ import com.viaversion.viaversion.api.command.ViaSubCommand;
/*    */ import com.viaversion.viaversion.commands.ViaCommandHandler;
/*    */ import rip.diavlo.base.viaversion.vialoadingbase.command.impl.LeakDetectSubCommand;
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
/*    */ public class VLBViaCommandHandler
/*    */   extends ViaCommandHandler
/*    */ {
/*    */   public VLBViaCommandHandler() {
/* 27 */     registerVLBDefaults();
/*    */   }
/*    */   
/*    */   public void registerVLBDefaults() {
/* 31 */     registerSubCommand((ViaSubCommand)new LeakDetectSubCommand());
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\base\viaversion\vialoadingbase\platform\viaversion\VLBViaCommandHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */