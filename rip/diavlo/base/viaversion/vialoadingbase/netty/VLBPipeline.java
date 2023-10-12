/*    */ package rip.diavlo.base.viaversion.vialoadingbase.netty;
/*    */ 
/*    */ import com.viaversion.viaversion.api.connection.UserConnection;
/*    */ import io.netty.channel.ChannelHandler;
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.channel.ChannelInboundHandlerAdapter;
/*    */ import rip.diavlo.base.viaversion.vialoadingbase.netty.handler.VLBViaDecodeHandler;
/*    */ import rip.diavlo.base.viaversion.vialoadingbase.netty.handler.VLBViaEncodeHandler;
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
/*    */ public abstract class VLBPipeline
/*    */   extends ChannelInboundHandlerAdapter
/*    */ {
/*    */   public static final String VIA_DECODER_HANDLER_NAME = "via-decoder";
/*    */   public static final String VIA_ENCODER_HANDLER_NAME = "via-encoder";
/*    */   private final UserConnection user;
/*    */   
/*    */   public VLBPipeline(UserConnection user) {
/* 35 */     this.user = user;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
/* 40 */     super.handlerAdded(ctx);
/*    */     
/* 42 */     ctx.pipeline().addBefore(getDecoderHandlerName(), "via-decoder", (ChannelHandler)createVLBViaDecodeHandler());
/* 43 */     ctx.pipeline().addBefore(getEncoderHandlerName(), "via-encoder", (ChannelHandler)createVLBViaEncodeHandler());
/*    */   }
/*    */ 
/*    */   
/*    */   public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
/* 48 */     super.userEventTriggered(ctx, evt);
/*    */     
/* 50 */     if (evt instanceof rip.diavlo.base.viaversion.vialoadingbase.netty.event.CompressionReorderEvent) {
/* 51 */       int decoderIndex = ctx.pipeline().names().indexOf(getDecompressionHandlerName());
/* 52 */       if (decoderIndex == -1)
/*    */         return; 
/* 54 */       if (decoderIndex > ctx.pipeline().names().indexOf("via-decoder")) {
/* 55 */         ChannelHandler decoder = ctx.pipeline().get("via-decoder");
/* 56 */         ChannelHandler encoder = ctx.pipeline().get("via-encoder");
/*    */         
/* 58 */         ctx.pipeline().remove(decoder);
/* 59 */         ctx.pipeline().remove(encoder);
/*    */         
/* 61 */         ctx.pipeline().addAfter(getDecompressionHandlerName(), "via-decoder", decoder);
/* 62 */         ctx.pipeline().addAfter(getCompressionHandlerName(), "via-encoder", encoder);
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   public VLBViaDecodeHandler createVLBViaDecodeHandler() {
/* 68 */     return new VLBViaDecodeHandler(this.user);
/*    */   }
/*    */   
/*    */   public VLBViaEncodeHandler createVLBViaEncodeHandler() {
/* 72 */     return new VLBViaEncodeHandler(this.user);
/*    */   }
/*    */   
/*    */   public abstract String getDecoderHandlerName();
/*    */   
/*    */   public abstract String getEncoderHandlerName();
/*    */   
/*    */   public abstract String getDecompressionHandlerName();
/*    */   
/*    */   public abstract String getCompressionHandlerName();
/*    */   
/*    */   public UserConnection getUser() {
/* 84 */     return this.user;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\base\viaversion\vialoadingbase\netty\VLBPipeline.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */