/*    */ package net.minecraft.network.play.server;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.INetHandler;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.play.INetHandlerPlayClient;
/*    */ 
/*    */ 
/*    */ public class S31PacketWindowProperty
/*    */   implements Packet<INetHandlerPlayClient>
/*    */ {
/*    */   private int windowId;
/*    */   private int varIndex;
/*    */   private int varValue;
/*    */   
/*    */   public S31PacketWindowProperty() {}
/*    */   
/*    */   public S31PacketWindowProperty(int windowIdIn, int varIndexIn, int varValueIn) {
/* 20 */     this.windowId = windowIdIn;
/* 21 */     this.varIndex = varIndexIn;
/* 22 */     this.varValue = varValueIn;
/*    */   }
/*    */ 
/*    */   
/*    */   public void processPacket(INetHandlerPlayClient handler) {
/* 27 */     handler.handleWindowProperty(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public void readPacketData(PacketBuffer buf) throws IOException {
/* 32 */     this.windowId = buf.readUnsignedByte();
/* 33 */     this.varIndex = buf.readShort();
/* 34 */     this.varValue = buf.readShort();
/*    */   }
/*    */ 
/*    */   
/*    */   public void writePacketData(PacketBuffer buf) throws IOException {
/* 39 */     buf.writeByte(this.windowId);
/* 40 */     buf.writeShort(this.varIndex);
/* 41 */     buf.writeShort(this.varValue);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getWindowId() {
/* 46 */     return this.windowId;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getVarIndex() {
/* 51 */     return this.varIndex;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getVarValue() {
/* 56 */     return this.varValue;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\network\play\server\S31PacketWindowProperty.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */