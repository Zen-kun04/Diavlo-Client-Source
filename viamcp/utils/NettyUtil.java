/*    */ package viamcp.utils;
/*    */ 
/*    */ import io.netty.channel.ChannelHandler;
/*    */ import io.netty.channel.ChannelPipeline;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NettyUtil
/*    */ {
/*    */   public static ChannelPipeline decodeEncodePlacement(ChannelPipeline instance, String base, String newHandler, ChannelHandler handler) {
/* 11 */     switch (base) {
/*    */ 
/*    */       
/*    */       case "decoder":
/* 15 */         if (instance.get("via-decoder") != null)
/*    */         {
/* 17 */           base = "via-decoder";
/*    */         }
/*    */         break;
/*    */ 
/*    */ 
/*    */       
/*    */       case "encoder":
/* 24 */         if (instance.get("via-encoder") != null)
/*    */         {
/* 26 */           base = "via-encoder";
/*    */         }
/*    */         break;
/*    */     } 
/*    */ 
/*    */ 
/*    */     
/* 33 */     return instance.addBefore(base, newHandler, handler);
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\viamc\\utils\NettyUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */