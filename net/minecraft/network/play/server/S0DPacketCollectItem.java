/*    */ package net.minecraft.network.play.server;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.INetHandler;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.play.INetHandlerPlayClient;
/*    */ 
/*    */ 
/*    */ public class S0DPacketCollectItem
/*    */   implements Packet<INetHandlerPlayClient>
/*    */ {
/*    */   private int collectedItemEntityId;
/*    */   private int entityId;
/*    */   
/*    */   public S0DPacketCollectItem() {}
/*    */   
/*    */   public S0DPacketCollectItem(int collectedItemEntityIdIn, int entityIdIn) {
/* 19 */     this.collectedItemEntityId = collectedItemEntityIdIn;
/* 20 */     this.entityId = entityIdIn;
/*    */   }
/*    */ 
/*    */   
/*    */   public void readPacketData(PacketBuffer buf) throws IOException {
/* 25 */     this.collectedItemEntityId = buf.readVarIntFromBuffer();
/* 26 */     this.entityId = buf.readVarIntFromBuffer();
/*    */   }
/*    */ 
/*    */   
/*    */   public void writePacketData(PacketBuffer buf) throws IOException {
/* 31 */     buf.writeVarIntToBuffer(this.collectedItemEntityId);
/* 32 */     buf.writeVarIntToBuffer(this.entityId);
/*    */   }
/*    */ 
/*    */   
/*    */   public void processPacket(INetHandlerPlayClient handler) {
/* 37 */     handler.handleCollectItem(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getCollectedItemEntityID() {
/* 42 */     return this.collectedItemEntityId;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getEntityID() {
/* 47 */     return this.entityId;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\network\play\server\S0DPacketCollectItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */