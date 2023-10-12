/*     */ package viamcp.handler;
/*     */ 
/*     */ import com.viaversion.viaversion.api.connection.UserConnection;
/*     */ import com.viaversion.viaversion.exception.CancelCodecException;
/*     */ import com.viaversion.viaversion.exception.CancelDecoderException;
/*     */ import com.viaversion.viaversion.util.PipelineUtil;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.channel.ChannelHandler;
/*     */ import io.netty.channel.ChannelHandler.Sharable;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.handler.codec.MessageToMessageDecoder;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.util.List;
/*     */ 
/*     */ @Sharable
/*     */ public class MCPDecodeHandler
/*     */   extends MessageToMessageDecoder<ByteBuf> {
/*     */   private final UserConnection info;
/*     */   private boolean handledCompression;
/*     */   private boolean skipDoubleTransform;
/*     */   
/*     */   public MCPDecodeHandler(UserConnection info) {
/*  23 */     this.info = info;
/*     */   }
/*     */   
/*     */   public UserConnection getInfo() {
/*  27 */     return this.info;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void decode(ChannelHandlerContext ctx, ByteBuf bytebuf, List<Object> out) throws Exception {
/*  34 */     if (this.skipDoubleTransform) {
/*     */       
/*  36 */       this.skipDoubleTransform = false;
/*  37 */       out.add(bytebuf.retain());
/*     */       
/*     */       return;
/*     */     } 
/*  41 */     if (!this.info.checkIncomingPacket())
/*     */     {
/*  43 */       throw CancelDecoderException.generate(null);
/*     */     }
/*     */     
/*  46 */     if (!this.info.shouldTransformPacket()) {
/*     */       
/*  48 */       out.add(bytebuf.retain());
/*     */       
/*     */       return;
/*     */     } 
/*  52 */     ByteBuf transformedBuf = ctx.alloc().buffer().writeBytes(bytebuf);
/*     */ 
/*     */     
/*     */     try {
/*  56 */       boolean needsCompress = handleCompressionOrder(ctx, transformedBuf);
/*     */       
/*  58 */       this.info.transformIncoming(transformedBuf, CancelDecoderException::generate);
/*     */       
/*  60 */       if (needsCompress) {
/*     */         
/*  62 */         CommonTransformer.compress(ctx, transformedBuf);
/*  63 */         this.skipDoubleTransform = true;
/*     */       } 
/*     */       
/*  66 */       out.add(transformedBuf.retain());
/*     */     }
/*     */     finally {
/*     */       
/*  70 */       transformedBuf.release();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean handleCompressionOrder(ChannelHandlerContext ctx, ByteBuf buf) throws InvocationTargetException {
/*  76 */     if (this.handledCompression)
/*     */     {
/*  78 */       return false;
/*     */     }
/*     */     
/*  81 */     int decoderIndex = ctx.pipeline().names().indexOf("decompress");
/*     */     
/*  83 */     if (decoderIndex == -1)
/*     */     {
/*  85 */       return false;
/*     */     }
/*     */     
/*  88 */     this.handledCompression = true;
/*     */     
/*  90 */     if (decoderIndex > ctx.pipeline().names().indexOf("via-decoder")) {
/*     */ 
/*     */       
/*  93 */       CommonTransformer.decompress(ctx, buf);
/*  94 */       ChannelHandler encoder = ctx.pipeline().get("via-encoder");
/*  95 */       ChannelHandler decoder = ctx.pipeline().get("via-decoder");
/*  96 */       ctx.pipeline().remove(encoder);
/*  97 */       ctx.pipeline().remove(decoder);
/*  98 */       ctx.pipeline().addAfter("compress", "via-encoder", encoder);
/*  99 */       ctx.pipeline().addAfter("decompress", "via-decoder", decoder);
/* 100 */       return true;
/*     */     } 
/*     */     
/* 103 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
/* 109 */     if (PipelineUtil.containsCause(cause, CancelCodecException.class)) {
/*     */       return;
/*     */     }
/*     */     
/* 113 */     super.exceptionCaught(ctx, cause);
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\viamcp\handler\MCPDecodeHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */