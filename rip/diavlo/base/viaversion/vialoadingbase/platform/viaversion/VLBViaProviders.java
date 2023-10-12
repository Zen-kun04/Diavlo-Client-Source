/*    */ package rip.diavlo.base.viaversion.vialoadingbase.platform.viaversion;
/*    */ 
/*    */ import com.viaversion.viaversion.api.Via;
/*    */ import com.viaversion.viaversion.api.platform.ViaPlatformLoader;
/*    */ import com.viaversion.viaversion.api.platform.providers.Provider;
/*    */ import com.viaversion.viaversion.api.platform.providers.ViaProviders;
/*    */ import com.viaversion.viaversion.api.protocol.version.VersionProvider;
/*    */ import com.viaversion.viaversion.protocols.protocol1_9to1_8.providers.MovementTransmitterProvider;
/*    */ import rip.diavlo.base.viaversion.vialoadingbase.ViaLoadingBase;
/*    */ import rip.diavlo.base.viaversion.vialoadingbase.platform.providers.VLBMovementTransmitterProvider;
/*    */ import rip.diavlo.base.viaversion.vialoadingbase.provider.VLBBaseVersionProvider;
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
/*    */ 
/*    */ public class VLBViaProviders
/*    */   implements ViaPlatformLoader
/*    */ {
/*    */   public void load() {
/* 34 */     ViaProviders providers = Via.getManager().getProviders();
/* 35 */     providers.use(VersionProvider.class, (Provider)new VLBBaseVersionProvider());
/* 36 */     providers.use(MovementTransmitterProvider.class, (Provider)new VLBMovementTransmitterProvider());
/*    */     
/* 38 */     if (ViaLoadingBase.getInstance().getProviders() != null) ViaLoadingBase.getInstance().getProviders().accept(providers); 
/*    */   }
/*    */   
/*    */   public void unload() {}
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\base\viaversion\vialoadingbase\platform\viaversion\VLBViaProviders.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */