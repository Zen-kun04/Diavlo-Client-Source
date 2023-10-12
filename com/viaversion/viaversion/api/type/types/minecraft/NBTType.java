/*    */ package com.viaversion.viaversion.api.type.types.minecraft;
/*    */ 
/*    */ import com.viaversion.viaversion.api.type.Type;
/*    */ import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
/*    */ import com.viaversion.viaversion.libs.opennbt.tag.limiter.TagLimiter;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.buffer.ByteBufInputStream;
/*    */ import io.netty.buffer.ByteBufOutputStream;
/*    */ import java.io.DataInput;
/*    */ import java.io.DataOutput;
/*    */ import java.io.IOException;
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
/*    */ public class NBTType
/*    */   extends Type<CompoundTag>
/*    */ {
/*    */   private static final int MAX_NBT_BYTES = 2097152;
/*    */   private static final int MAX_NESTING_LEVEL = 512;
/*    */   
/*    */   public NBTType() {
/* 40 */     super(CompoundTag.class);
/*    */   }
/*    */ 
/*    */   
/*    */   public CompoundTag read(ByteBuf buffer) throws Exception {
/* 45 */     return read(buffer, true);
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(ByteBuf buffer, CompoundTag object) throws Exception {
/* 50 */     write(buffer, object, "");
/*    */   }
/*    */   
/*    */   public static CompoundTag read(ByteBuf buffer, boolean readName) throws Exception {
/* 54 */     byte id = buffer.readByte();
/* 55 */     if (id == 0) {
/* 56 */       return null;
/*    */     }
/* 58 */     if (id != 10) {
/* 59 */       throw new IOException(String.format("Expected root tag to be a CompoundTag, was %s", new Object[] { Byte.valueOf(id) }));
/*    */     }
/*    */     
/* 62 */     if (readName) {
/* 63 */       buffer.skipBytes(buffer.readUnsignedShort());
/*    */     }
/*    */     
/* 66 */     TagLimiter tagLimiter = TagLimiter.create(2097152, 512);
/* 67 */     CompoundTag tag = new CompoundTag();
/* 68 */     tag.read((DataInput)new ByteBufInputStream(buffer), tagLimiter);
/* 69 */     return tag;
/*    */   }
/*    */   
/*    */   public static void write(ByteBuf buffer, CompoundTag tag, String name) throws Exception {
/* 73 */     if (tag == null) {
/* 74 */       buffer.writeByte(0);
/*    */       
/*    */       return;
/*    */     } 
/* 78 */     ByteBufOutputStream out = new ByteBufOutputStream(buffer);
/* 79 */     out.writeByte(10);
/* 80 */     if (name != null) {
/* 81 */       out.writeUTF(name);
/*    */     }
/* 83 */     tag.write((DataOutput)out);
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\api\type\types\minecraft\NBTType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */