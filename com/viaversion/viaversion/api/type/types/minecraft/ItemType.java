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
/*    */ public class ItemType
/*    */   extends BaseItemType
/*    */ {
/*    */   public ItemType() {
/* 33 */     super("Item");
/*    */   }
/*    */ 
/*    */   
/*    */   public Item read(ByteBuf buffer) throws Exception {
/* 38 */     short id = buffer.readShort();
/* 39 */     if (id < 0) {
/* 40 */       return null;
/*    */     }
/* 42 */     DataItem dataItem = new DataItem();
/* 43 */     dataItem.setIdentifier(id);
/* 44 */     dataItem.setAmount(buffer.readByte());
/* 45 */     dataItem.setData(buffer.readShort());
/* 46 */     dataItem.setTag((CompoundTag)NBT.read(buffer));
/* 47 */     return (Item)dataItem;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void write(ByteBuf buffer, Item object) throws Exception {
/* 53 */     if (object == null) {
/* 54 */       buffer.writeShort(-1);
/*    */     } else {
/* 56 */       buffer.writeShort(object.identifier());
/* 57 */       buffer.writeByte(object.amount());
/* 58 */       buffer.writeShort(object.data());
/* 59 */       NBT.write(buffer, object.tag());
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\api\type\types\minecraft\ItemType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */