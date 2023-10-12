/*    */ package net.minecraft.network.play.client;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.INetHandler;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.play.INetHandlerPlayServer;
/*    */ 
/*    */ 
/*    */ public class C16PacketClientStatus
/*    */   implements Packet<INetHandlerPlayServer>
/*    */ {
/*    */   private EnumState status;
/*    */   
/*    */   public C16PacketClientStatus() {}
/*    */   
/*    */   public C16PacketClientStatus(EnumState statusIn) {
/* 18 */     this.status = statusIn;
/*    */   }
/*    */ 
/*    */   
/*    */   public void readPacketData(PacketBuffer buf) throws IOException {
/* 23 */     this.status = (EnumState)buf.readEnumValue(EnumState.class);
/*    */   }
/*    */ 
/*    */   
/*    */   public void writePacketData(PacketBuffer buf) throws IOException {
/* 28 */     buf.writeEnumValue(this.status);
/*    */   }
/*    */ 
/*    */   
/*    */   public void processPacket(INetHandlerPlayServer handler) {
/* 33 */     handler.processClientStatus(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public EnumState getStatus() {
/* 38 */     return this.status;
/*    */   }
/*    */   
/*    */   public enum EnumState
/*    */   {
/* 43 */     PERFORM_RESPAWN,
/* 44 */     REQUEST_STATS,
/* 45 */     OPEN_INVENTORY_ACHIEVEMENT;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\network\play\client\C16PacketClientStatus.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */