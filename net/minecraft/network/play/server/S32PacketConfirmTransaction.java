/*    */ package net.minecraft.network.play.server;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.INetHandler;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.play.INetHandlerPlayClient;
/*    */ 
/*    */ 
/*    */ public class S32PacketConfirmTransaction
/*    */   implements Packet<INetHandlerPlayClient>
/*    */ {
/*    */   private int windowId;
/*    */   private short actionNumber;
/*    */   private boolean field_148893_c;
/*    */   
/*    */   public S32PacketConfirmTransaction() {}
/*    */   
/*    */   public S32PacketConfirmTransaction(int windowIdIn, short actionNumberIn, boolean p_i45182_3_) {
/* 20 */     this.windowId = windowIdIn;
/* 21 */     this.actionNumber = actionNumberIn;
/* 22 */     this.field_148893_c = p_i45182_3_;
/*    */   }
/*    */ 
/*    */   
/*    */   public void processPacket(INetHandlerPlayClient handler) {
/* 27 */     handler.handleConfirmTransaction(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public void readPacketData(PacketBuffer buf) throws IOException {
/* 32 */     this.windowId = buf.readUnsignedByte();
/* 33 */     this.actionNumber = buf.readShort();
/* 34 */     this.field_148893_c = buf.readBoolean();
/*    */   }
/*    */ 
/*    */   
/*    */   public void writePacketData(PacketBuffer buf) throws IOException {
/* 39 */     buf.writeByte(this.windowId);
/* 40 */     buf.writeShort(this.actionNumber);
/* 41 */     buf.writeBoolean(this.field_148893_c);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getWindowId() {
/* 46 */     return this.windowId;
/*    */   }
/*    */ 
/*    */   
/*    */   public short getActionNumber() {
/* 51 */     return this.actionNumber;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean func_148888_e() {
/* 56 */     return this.field_148893_c;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\network\play\server\S32PacketConfirmTransaction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */