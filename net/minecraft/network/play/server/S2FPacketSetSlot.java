/*    */ package net.minecraft.network.play.server;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraft.network.INetHandler;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.play.INetHandlerPlayClient;
/*    */ 
/*    */ 
/*    */ public class S2FPacketSetSlot
/*    */   implements Packet<INetHandlerPlayClient>
/*    */ {
/*    */   private int windowId;
/*    */   private int slot;
/*    */   private ItemStack item;
/*    */   
/*    */   public S2FPacketSetSlot() {}
/*    */   
/*    */   public S2FPacketSetSlot(int windowIdIn, int slotIn, ItemStack itemIn) {
/* 21 */     this.windowId = windowIdIn;
/* 22 */     this.slot = slotIn;
/* 23 */     this.item = (itemIn == null) ? null : itemIn.copy();
/*    */   }
/*    */ 
/*    */   
/*    */   public void processPacket(INetHandlerPlayClient handler) {
/* 28 */     handler.handleSetSlot(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public void readPacketData(PacketBuffer buf) throws IOException {
/* 33 */     this.windowId = buf.readByte();
/* 34 */     this.slot = buf.readShort();
/* 35 */     this.item = buf.readItemStackFromBuffer();
/*    */   }
/*    */ 
/*    */   
/*    */   public void writePacketData(PacketBuffer buf) throws IOException {
/* 40 */     buf.writeByte(this.windowId);
/* 41 */     buf.writeShort(this.slot);
/* 42 */     buf.writeItemStackToBuffer(this.item);
/*    */   }
/*    */ 
/*    */   
/*    */   public int func_149175_c() {
/* 47 */     return this.windowId;
/*    */   }
/*    */ 
/*    */   
/*    */   public int func_149173_d() {
/* 52 */     return this.slot;
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack func_149174_e() {
/* 57 */     return this.item;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\network\play\server\S2FPacketSetSlot.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */