/*    */ package net.minecraft.network.play.server;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.network.INetHandler;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.play.INetHandlerPlayClient;
/*    */ 
/*    */ 
/*    */ public class S1BPacketEntityAttach
/*    */   implements Packet<INetHandlerPlayClient>
/*    */ {
/*    */   private int leash;
/*    */   private int entityId;
/*    */   private int vehicleEntityId;
/*    */   
/*    */   public S1BPacketEntityAttach() {}
/*    */   
/*    */   public S1BPacketEntityAttach(int leashIn, Entity entityIn, Entity vehicle) {
/* 21 */     this.leash = leashIn;
/* 22 */     this.entityId = entityIn.getEntityId();
/* 23 */     this.vehicleEntityId = (vehicle != null) ? vehicle.getEntityId() : -1;
/*    */   }
/*    */ 
/*    */   
/*    */   public void readPacketData(PacketBuffer buf) throws IOException {
/* 28 */     this.entityId = buf.readInt();
/* 29 */     this.vehicleEntityId = buf.readInt();
/* 30 */     this.leash = buf.readUnsignedByte();
/*    */   }
/*    */ 
/*    */   
/*    */   public void writePacketData(PacketBuffer buf) throws IOException {
/* 35 */     buf.writeInt(this.entityId);
/* 36 */     buf.writeInt(this.vehicleEntityId);
/* 37 */     buf.writeByte(this.leash);
/*    */   }
/*    */ 
/*    */   
/*    */   public void processPacket(INetHandlerPlayClient handler) {
/* 42 */     handler.handleEntityAttach(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getLeash() {
/* 47 */     return this.leash;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getEntityId() {
/* 52 */     return this.entityId;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getVehicleEntityId() {
/* 57 */     return this.vehicleEntityId;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\network\play\server\S1BPacketEntityAttach.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */