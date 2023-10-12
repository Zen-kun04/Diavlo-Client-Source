/*    */ package com.viaversion.viaversion.api.type.types.minecraft;
/*    */ 
/*    */ import com.viaversion.viaversion.api.type.Type;
/*    */ import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
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
/*    */ public class NamelessNBTType
/*    */   extends Type<CompoundTag>
/*    */ {
/*    */   public NamelessNBTType() {
/* 32 */     super(CompoundTag.class);
/*    */   }
/*    */ 
/*    */   
/*    */   public CompoundTag read(ByteBuf buffer) throws Exception {
/* 37 */     return NBTType.read(buffer, false);
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(ByteBuf buffer, CompoundTag tag) throws Exception {
/* 42 */     NBTType.write(buffer, tag, null);
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\api\type\types\minecraft\NamelessNBTType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */