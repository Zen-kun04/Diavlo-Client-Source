/*    */ package net.minecraft.network.play.server;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.util.List;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraft.network.INetHandler;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.play.INetHandlerPlayClient;
/*    */ 
/*    */ 
/*    */ public class S30PacketWindowItems
/*    */   implements Packet<INetHandlerPlayClient>
/*    */ {
/*    */   private int windowId;
/*    */   private ItemStack[] itemStacks;
/*    */   
/*    */   public S30PacketWindowItems() {}
/*    */   
/*    */   public S30PacketWindowItems(int windowIdIn, List<ItemStack> p_i45186_2_) {
/* 21 */     this.windowId = windowIdIn;
/* 22 */     this.itemStacks = new ItemStack[p_i45186_2_.size()];
/*    */     
/* 24 */     for (int i = 0; i < this.itemStacks.length; i++) {
/*    */       
/* 26 */       ItemStack itemstack = p_i45186_2_.get(i);
/* 27 */       this.itemStacks[i] = (itemstack == null) ? null : itemstack.copy();
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void readPacketData(PacketBuffer buf) throws IOException {
/* 33 */     this.windowId = buf.readUnsignedByte();
/* 34 */     int i = buf.readShort();
/* 35 */     this.itemStacks = new ItemStack[i];
/*    */     
/* 37 */     for (int j = 0; j < i; j++)
/*    */     {
/* 39 */       this.itemStacks[j] = buf.readItemStackFromBuffer();
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void writePacketData(PacketBuffer buf) throws IOException {
/* 45 */     buf.writeByte(this.windowId);
/* 46 */     buf.writeShort(this.itemStacks.length);
/*    */     
/* 48 */     for (ItemStack itemstack : this.itemStacks)
/*    */     {
/* 50 */       buf.writeItemStackToBuffer(itemstack);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void processPacket(INetHandlerPlayClient handler) {
/* 56 */     handler.handleWindowItems(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public int func_148911_c() {
/* 61 */     return this.windowId;
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack[] getItemStacks() {
/* 66 */     return this.itemStacks;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\network\play\server\S30PacketWindowItems.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */