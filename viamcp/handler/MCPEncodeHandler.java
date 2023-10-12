/*     */ package viamcp.handler;
/*     */ 
/*     */ import com.viaversion.viaversion.api.connection.UserConnection;
/*     */ import com.viaversion.viaversion.exception.CancelCodecException;
/*     */ import com.viaversion.viaversion.exception.CancelEncoderException;
/*     */ import com.viaversion.viaversion.util.PipelineUtil;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.channel.ChannelHandler;
/*     */ import io.netty.channel.ChannelHandler.Sharable;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.handler.codec.MessageToMessageEncoder;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.util.List;
/*     */ 
/*     */ @Sharable
/*     */ public class MCPEncodeHandler
/*     */   extends MessageToMessageEncoder<ByteBuf>
/*     */ {
/*     */   private final UserConnection info;
/*     */   private boolean handledCompression;
/*     */   
/*     */   public MCPEncodeHandler(UserConnection info) {
/*  23 */     this.info = info;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void encode(ChannelHandlerContext ctx, ByteBuf bytebuf, List<Object> out) throws Exception {
/*  29 */     if (!this.info.checkOutgoingPacket())
/*     */     {
/*  31 */       throw CancelEncoderException.generate(null);
/*     */     }
/*     */     
/*  34 */     if (!this.info.shouldTransformPacket()) {
/*     */       
/*  36 */       out.add(bytebuf.retain());
/*     */       
/*     */       return;
/*     */     } 
/*  40 */     ByteBuf transformedBuf = ctx.alloc().buffer().writeBytes(bytebuf);
/*     */ 
/*     */     
/*     */     try {
/*  44 */       boolean needsCompress = handleCompressionOrder(ctx, transformedBuf);
/*     */       
/*  46 */       this.info.transformOutgoing(transformedBuf, CancelEncoderException::generate);
/*     */       
/*  48 */       if (needsCompress)
/*     */       {
/*  50 */         CommonTransformer.compress(ctx, transformedBuf);
/*     */       }
/*     */       
/*  53 */       out.add(transformedBuf.retain());
/*     */     }
/*     */     finally {
/*     */       
/*  57 */       transformedBuf.release();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean handleCompressionOrder(ChannelHandlerContext ctx, ByteBuf buf) throws InvocationTargetException {
/*  63 */     if (this.handledCompression)
/*     */     {
/*  65 */       return false;
/*     */     }
/*     */     
/*  68 */     int encoderIndex = ctx.pipeline().names().indexOf("compress");
/*     */     
/*  70 */     if (encoderIndex == -1)
/*     */     {
/*  72 */       return false;
/*     */     }
/*  74 */     this.handledCompression = true;
/*     */     
/*  76 */     if (encoderIndex > ctx.pipeline().names().indexOf("via-encoder")) {
/*     */ 
/*     */       
/*  79 */       CommonTransformer.decompress(ctx, buf);
/*  80 */       ChannelHandler encoder = ctx.pipeline().get("via-encoder");
/*  81 */       ChannelHandler decoder = ctx.pipeline().get("via-decoder");
/*  82 */       ctx.pipeline().remove(encoder);
/*  83 */       ctx.pipeline().remove(decoder);
/*  84 */       ctx.pipeline().addAfter("compress", "via-encoder", encoder);
/*  85 */       ctx.pipeline().addAfter("decompress", "via-decoder", decoder);
/*  86 */       return true;
/*     */     } 
/*     */     
/*  89 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
/*  95 */     if (PipelineUtil.containsCause(cause, CancelCodecException.class)) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 100 */     super.exceptionCaught(ctx, cause);
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\viamcp\handler\MCPEncodeHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */