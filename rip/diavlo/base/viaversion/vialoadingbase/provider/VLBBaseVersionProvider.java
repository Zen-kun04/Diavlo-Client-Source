/*    */ package rip.diavlo.base.viaversion.vialoadingbase.provider;
/*    */ 
/*    */ import com.viaversion.viaversion.api.connection.UserConnection;
/*    */ import com.viaversion.viaversion.protocols.base.BaseVersionProvider;
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
/*    */ 
/*    */ public class VLBBaseVersionProvider
/*    */   extends BaseVersionProvider
/*    */ {
/*    */   public int getClosestServerProtocol(UserConnection connection) throws Exception {
/* 28 */     if (connection.isClientSide()) {
/* 29 */       return ViaLoadingBase.getInstance().getTargetVersion().getVersion();
/*    */     }
/* 31 */     return super.getClosestServerProtocol(connection);
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\base\viaversion\vialoadingbase\provider\VLBBaseVersionProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */