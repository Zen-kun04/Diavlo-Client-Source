/*    */ package net.minecraft.util;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.buffer.Unpooled;
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.handler.codec.ByteToMessageDecoder;
/*    */ import io.netty.handler.codec.CorruptedFrameException;
/*    */ import java.util.List;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ 
/*    */ 
/*    */ public class MessageDeserializer2
/*    */   extends ByteToMessageDecoder
/*    */ {
/*    */   protected void decode(ChannelHandlerContext p_decode_1_, ByteBuf p_decode_2_, List<Object> p_decode_3_) throws Exception {
/* 16 */     p_decode_2_.markReaderIndex();
/* 17 */     byte[] abyte = new byte[3];
/*    */     
/* 19 */     for (int i = 0; i < abyte.length; i++) {
/*    */       
/* 21 */       if (!p_decode_2_.isReadable()) {
/*    */         
/* 23 */         p_decode_2_.resetReaderIndex();
/*    */         
/*    */         return;
/*    */       } 
/* 27 */       abyte[i] = p_decode_2_.readByte();
/*    */       
/* 29 */       if (abyte[i] >= 0) {
/*    */         
/* 31 */         PacketBuffer packetbuffer = new PacketBuffer(Unpooled.wrappedBuffer(abyte));
/*    */ 
/*    */         
/*    */         try {
/* 35 */           int j = packetbuffer.readVarIntFromBuffer();
/*    */           
/* 37 */           if (p_decode_2_.readableBytes() >= j) {
/*    */             
/* 39 */             p_decode_3_.add(p_decode_2_.readBytes(j));
/*    */             
/*    */             return;
/*    */           } 
/* 43 */           p_decode_2_.resetReaderIndex();
/*    */         }
/*    */         finally {
/*    */           
/* 47 */           packetbuffer.release();
/*    */         } 
/*    */         
/*    */         return;
/*    */       } 
/*    */     } 
/*    */     
/* 54 */     throw new CorruptedFrameException("length wider than 21-bit");
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraf\\util\MessageDeserializer2.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */