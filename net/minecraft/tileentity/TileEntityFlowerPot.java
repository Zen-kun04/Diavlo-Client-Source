/*    */ package net.minecraft.tileentity;
/*    */ 
/*    */ import net.minecraft.item.Item;
/*    */ import net.minecraft.nbt.NBTTagCompound;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TileEntityFlowerPot
/*    */   extends TileEntity
/*    */ {
/*    */   private Item flowerPotItem;
/*    */   private int flowerPotData;
/*    */   
/*    */   public TileEntityFlowerPot() {}
/*    */   
/*    */   public TileEntityFlowerPot(Item potItem, int potData) {
/* 20 */     this.flowerPotItem = potItem;
/* 21 */     this.flowerPotData = potData;
/*    */   }
/*    */ 
/*    */   
/*    */   public void writeToNBT(NBTTagCompound compound) {
/* 26 */     super.writeToNBT(compound);
/* 27 */     ResourceLocation resourcelocation = (ResourceLocation)Item.itemRegistry.getNameForObject(this.flowerPotItem);
/* 28 */     compound.setString("Item", (resourcelocation == null) ? "" : resourcelocation.toString());
/* 29 */     compound.setInteger("Data", this.flowerPotData);
/*    */   }
/*    */ 
/*    */   
/*    */   public void readFromNBT(NBTTagCompound compound) {
/* 34 */     super.readFromNBT(compound);
/*    */     
/* 36 */     if (compound.hasKey("Item", 8)) {
/*    */       
/* 38 */       this.flowerPotItem = Item.getByNameOrId(compound.getString("Item"));
/*    */     }
/*    */     else {
/*    */       
/* 42 */       this.flowerPotItem = Item.getItemById(compound.getInteger("Item"));
/*    */     } 
/*    */     
/* 45 */     this.flowerPotData = compound.getInteger("Data");
/*    */   }
/*    */ 
/*    */   
/*    */   public Packet getDescriptionPacket() {
/* 50 */     NBTTagCompound nbttagcompound = new NBTTagCompound();
/* 51 */     writeToNBT(nbttagcompound);
/* 52 */     nbttagcompound.removeTag("Item");
/* 53 */     nbttagcompound.setInteger("Item", Item.getIdFromItem(this.flowerPotItem));
/* 54 */     return (Packet)new S35PacketUpdateTileEntity(this.pos, 5, nbttagcompound);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setFlowerPotData(Item potItem, int potData) {
/* 59 */     this.flowerPotItem = potItem;
/* 60 */     this.flowerPotData = potData;
/*    */   }
/*    */ 
/*    */   
/*    */   public Item getFlowerPotItem() {
/* 65 */     return this.flowerPotItem;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getFlowerPotData() {
/* 70 */     return this.flowerPotData;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\tileentity\TileEntityFlowerPot.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */