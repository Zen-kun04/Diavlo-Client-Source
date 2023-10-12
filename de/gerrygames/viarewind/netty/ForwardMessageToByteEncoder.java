/*    */ package de.gerrygames.viarewind.netty;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.handler.codec.MessageToByteEncoder;
/*    */ 
/*    */ public class ForwardMessageToByteEncoder
/*    */   extends MessageToByteEncoder<ByteBuf> {
/*    */   protected void encode(ChannelHandlerContext ctx, ByteBuf msg, ByteBuf out) throws Exception {
/* 10 */     out.writeBytes(msg);
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\de\gerrygames\viarewind\netty\ForwardMessageToByteEncoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */