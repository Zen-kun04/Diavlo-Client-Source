/*    */ package net.minecraft.network.play.client;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraft.network.INetHandler;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.play.INetHandlerPlayServer;
/*    */ 
/*    */ 
/*    */ public class C0EPacketClickWindow
/*    */   implements Packet<INetHandlerPlayServer>
/*    */ {
/*    */   private int windowId;
/*    */   private int slotId;
/*    */   private int usedButton;
/*    */   private short actionNumber;
/*    */   private ItemStack clickedItem;
/*    */   private int mode;
/*    */   
/*    */   public C0EPacketClickWindow() {}
/*    */   
/*    */   public C0EPacketClickWindow(int windowId, int slotId, int usedButton, int mode, ItemStack clickedItem, short actionNumber) {
/* 24 */     this.windowId = windowId;
/* 25 */     this.slotId = slotId;
/* 26 */     this.usedButton = usedButton;
/* 27 */     this.clickedItem = (clickedItem != null) ? clickedItem.copy() : null;
/* 28 */     this.actionNumber = actionNumber;
/* 29 */     this.mode = mode;
/*    */   }
/*    */ 
/*    */   
/*    */   public void processPacket(INetHandlerPlayServer handler) {
/* 34 */     handler.processClickWindow(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public void readPacketData(PacketBuffer buf) throws IOException {
/* 39 */     this.windowId = buf.readByte();
/* 40 */     this.slotId = buf.readShort();
/* 41 */     this.usedButton = buf.readByte();
/* 42 */     this.actionNumber = buf.readShort();
/* 43 */     this.mode = buf.readByte();
/* 44 */     this.clickedItem = buf.readItemStackFromBuffer();
/*    */   }
/*    */ 
/*    */   
/*    */   public void writePacketData(PacketBuffer buf) throws IOException {
/* 49 */     buf.writeByte(this.windowId);
/* 50 */     buf.writeShort(this.slotId);
/* 51 */     buf.writeByte(this.usedButton);
/* 52 */     buf.writeShort(this.actionNumber);
/* 53 */     buf.writeByte(this.mode);
/* 54 */     buf.writeItemStackToBuffer(this.clickedItem);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getWindowId() {
/* 59 */     return this.windowId;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getSlotId() {
/* 64 */     return this.slotId;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getUsedButton() {
/* 69 */     return this.usedButton;
/*    */   }
/*    */ 
/*    */   
/*    */   public short getActionNumber() {
/* 74 */     return this.actionNumber;
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack getClickedItem() {
/* 79 */     return this.clickedItem;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getMode() {
/* 84 */     return this.mode;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\network\play\client\C0EPacketClickWindow.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */