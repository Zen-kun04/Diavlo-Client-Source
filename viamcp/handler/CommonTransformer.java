/*    */ package viamcp.handler;
/*    */ 
/*    */ import com.viaversion.viaversion.util.PipelineUtil;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.channel.ChannelHandler;
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.handler.codec.ByteToMessageDecoder;
/*    */ import io.netty.handler.codec.MessageToByteEncoder;
/*    */ import io.netty.handler.codec.MessageToMessageDecoder;
/*    */ import java.lang.reflect.InvocationTargetException;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CommonTransformer
/*    */ {
/*    */   public static final String HANDLER_DECODER_NAME = "via-decoder";
/*    */   public static final String HANDLER_ENCODER_NAME = "via-encoder";
/*    */   
/*    */   public static void decompress(ChannelHandlerContext ctx, ByteBuf buf) throws InvocationTargetException {
/* 20 */     ChannelHandler handler = ctx.pipeline().get("decompress");
/* 21 */     ByteBuf decompressed = (handler instanceof MessageToMessageDecoder) ? PipelineUtil.callDecode((MessageToMessageDecoder)handler, ctx, buf).get(0) : PipelineUtil.callDecode((ByteToMessageDecoder)handler, ctx, buf).get(0);
/*    */ 
/*    */     
/*    */     try {
/* 25 */       buf.clear().writeBytes(decompressed);
/*    */     }
/*    */     finally {
/*    */       
/* 29 */       decompressed.release();
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public static void compress(ChannelHandlerContext ctx, ByteBuf buf) throws Exception {
/* 35 */     ByteBuf compressed = ctx.alloc().buffer();
/*    */ 
/*    */     
/*    */     try {
/* 39 */       PipelineUtil.callEncode((MessageToByteEncoder)ctx.pipeline().get("compress"), ctx, buf, compressed);
/* 40 */       buf.clear().writeBytes(compressed);
/*    */     }
/*    */     finally {
/*    */       
/* 44 */       compressed.release();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\viamcp\handler\CommonTransformer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */