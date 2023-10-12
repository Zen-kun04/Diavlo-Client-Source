/*    */ package com.viaversion.viaversion.api.type.types.minecraft;
/*    */ 
/*    */ import com.viaversion.viaversion.api.minecraft.item.DataItem;
/*    */ import com.viaversion.viaversion.api.minecraft.item.Item;
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
/*    */ public class Item1_20_2Type
/*    */   extends BaseItemType
/*    */ {
/*    */   public Item1_20_2Type() {
/* 33 */     super("Item1_20_2Type");
/*    */   }
/*    */ 
/*    */   
/*    */   public Item read(ByteBuf buffer) throws Exception {
/* 38 */     if (!buffer.readBoolean()) {
/* 39 */       return null;
/*    */     }
/*    */     
/* 42 */     DataItem dataItem = new DataItem();
/* 43 */     dataItem.setIdentifier(VAR_INT.readPrimitive(buffer));
/* 44 */     dataItem.setAmount(buffer.readByte());
/* 45 */     dataItem.setTag((CompoundTag)NAMELESS_NBT.read(buffer));
/* 46 */     return (Item)dataItem;
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(ByteBuf buffer, Item object) throws Exception {
/* 51 */     if (object == null) {
/* 52 */       buffer.writeBoolean(false);
/*    */     } else {
/* 54 */       buffer.writeBoolean(true);
/* 55 */       VAR_INT.writePrimitive(buffer, object.identifier());
/* 56 */       buffer.writeByte(object.amount());
/* 57 */       NAMELESS_NBT.write(buffer, object.tag());
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\api\type\types\minecraft\Item1_20_2Type.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */