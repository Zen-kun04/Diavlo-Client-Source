/*    */ package com.viaversion.viaversion.api.type.types.minecraft;
/*    */ 
/*    */ import com.viaversion.viaversion.api.minecraft.Position;
/*    */ import com.viaversion.viaversion.api.type.OptionalType;
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
/*    */ public class PositionType
/*    */   extends Type<Position>
/*    */ {
/*    */   public PositionType() {
/* 33 */     super(Position.class);
/*    */   }
/*    */ 
/*    */   
/*    */   public Position read(ByteBuf buffer) {
/* 38 */     long val = buffer.readLong();
/* 39 */     long x = val >> 38L;
/* 40 */     long y = val << 26L >> 52L;
/* 41 */     long z = val << 38L >> 38L;
/*    */     
/* 43 */     return new Position((int)x, (short)(int)y, (int)z);
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(ByteBuf buffer, Position object) {
/* 48 */     buffer.writeLong((object.x() & 0x3FFFFFFL) << 38L | (object.y() & 0xFFFL) << 26L | object.z() & 0x3FFFFFFL);
/*    */   }
/*    */   
/*    */   public static final class OptionalPositionType
/*    */     extends OptionalType<Position> {
/*    */     public OptionalPositionType() {
/* 54 */       super(Type.POSITION);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\api\type\types\minecraft\PositionType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */