/*    */ package com.viaversion.viaversion.api.type.types.minecraft;
/*    */ 
/*    */ import com.viaversion.viaversion.api.minecraft.metadata.ChunkPosition;
/*    */ import com.viaversion.viaversion.api.type.Type;
/*    */ import io.netty.buffer.ByteBuf;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ChunkPositionType
/*    */   extends Type<ChunkPosition>
/*    */ {
/*    */   public ChunkPositionType() {
/* 32 */     super(ChunkPosition.class);
/*    */   }
/*    */ 
/*    */   
/*    */   public ChunkPosition read(ByteBuf buffer) throws Exception {
/* 37 */     long chunkKey = Type.LONG.readPrimitive(buffer);
/* 38 */     return new ChunkPosition(chunkKey);
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(ByteBuf buffer, ChunkPosition chunkPosition) throws Exception {
/* 43 */     Type.LONG.writePrimitive(buffer, chunkPosition.chunkKey());
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\api\type\types\minecraft\ChunkPositionType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */