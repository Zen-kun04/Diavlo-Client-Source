/*    */ package rip.diavlo.base.viaversion.viamcp;
/*    */ 
/*    */ import com.viaversion.viaversion.api.connection.UserConnection;
/*    */ import rip.diavlo.base.viaversion.vialoadingbase.netty.VLBPipeline;
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
/*    */ public class MCPVLBPipeline
/*    */   extends VLBPipeline
/*    */ {
/*    */   public MCPVLBPipeline(UserConnection user) {
/* 26 */     super(user);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getDecoderHandlerName() {
/* 31 */     return "decoder";
/*    */   }
/*    */ 
/*    */   
/*    */   public String getEncoderHandlerName() {
/* 36 */     return "encoder";
/*    */   }
/*    */ 
/*    */   
/*    */   public String getDecompressionHandlerName() {
/* 41 */     return "decompress";
/*    */   }
/*    */ 
/*    */   
/*    */   public String getCompressionHandlerName() {
/* 46 */     return "compress";
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\base\viaversion\viamcp\MCPVLBPipeline.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */