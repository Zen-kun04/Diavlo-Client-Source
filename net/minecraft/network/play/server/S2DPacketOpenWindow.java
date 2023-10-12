/*    */ package net.minecraft.network.play.server;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.INetHandler;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.play.INetHandlerPlayClient;
/*    */ import net.minecraft.util.IChatComponent;
/*    */ 
/*    */ 
/*    */ public class S2DPacketOpenWindow
/*    */   implements Packet<INetHandlerPlayClient>
/*    */ {
/*    */   private int windowId;
/*    */   private String inventoryType;
/*    */   private IChatComponent windowTitle;
/*    */   private int slotCount;
/*    */   private int entityId;
/*    */   
/*    */   public S2DPacketOpenWindow() {}
/*    */   
/*    */   public S2DPacketOpenWindow(int incomingWindowId, String incomingWindowTitle, IChatComponent windowTitleIn) {
/* 23 */     this(incomingWindowId, incomingWindowTitle, windowTitleIn, 0);
/*    */   }
/*    */ 
/*    */   
/*    */   public S2DPacketOpenWindow(int windowIdIn, String guiId, IChatComponent windowTitleIn, int slotCountIn) {
/* 28 */     this.windowId = windowIdIn;
/* 29 */     this.inventoryType = guiId;
/* 30 */     this.windowTitle = windowTitleIn;
/* 31 */     this.slotCount = slotCountIn;
/*    */   }
/*    */ 
/*    */   
/*    */   public S2DPacketOpenWindow(int windowIdIn, String guiId, IChatComponent windowTitleIn, int slotCountIn, int incomingEntityId) {
/* 36 */     this(windowIdIn, guiId, windowTitleIn, slotCountIn);
/* 37 */     this.entityId = incomingEntityId;
/*    */   }
/*    */ 
/*    */   
/*    */   public void processPacket(INetHandlerPlayClient handler) {
/* 42 */     handler.handleOpenWindow(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public void readPacketData(PacketBuffer buf) throws IOException {
/* 47 */     this.windowId = buf.readUnsignedByte();
/* 48 */     this.inventoryType = buf.readStringFromBuffer(32);
/* 49 */     this.windowTitle = buf.readChatComponent();
/* 50 */     this.slotCount = buf.readUnsignedByte();
/*    */     
/* 52 */     if (this.inventoryType.equals("EntityHorse"))
/*    */     {
/* 54 */       this.entityId = buf.readInt();
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void writePacketData(PacketBuffer buf) throws IOException {
/* 60 */     buf.writeByte(this.windowId);
/* 61 */     buf.writeString(this.inventoryType);
/* 62 */     buf.writeChatComponent(this.windowTitle);
/* 63 */     buf.writeByte(this.slotCount);
/*    */     
/* 65 */     if (this.inventoryType.equals("EntityHorse"))
/*    */     {
/* 67 */       buf.writeInt(this.entityId);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public int getWindowId() {
/* 73 */     return this.windowId;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getGuiId() {
/* 78 */     return this.inventoryType;
/*    */   }
/*    */ 
/*    */   
/*    */   public IChatComponent getWindowTitle() {
/* 83 */     return this.windowTitle;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getSlotCount() {
/* 88 */     return this.slotCount;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getEntityId() {
/* 93 */     return this.entityId;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean hasSlots() {
/* 98 */     return (this.slotCount > 0);
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\network\play\server\S2DPacketOpenWindow.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */