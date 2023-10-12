/*    */ package net.minecraft.network.play.server;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.network.INetHandler;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.play.INetHandlerPlayClient;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ 
/*    */ public class S19PacketEntityStatus
/*    */   implements Packet<INetHandlerPlayClient>
/*    */ {
/*    */   private int entityId;
/*    */   private byte logicOpcode;
/*    */   
/*    */   public S19PacketEntityStatus() {}
/*    */   
/*    */   public S19PacketEntityStatus(Entity entityIn, byte opCodeIn) {
/* 21 */     this.entityId = entityIn.getEntityId();
/* 22 */     this.logicOpcode = opCodeIn;
/*    */   }
/*    */ 
/*    */   
/*    */   public void readPacketData(PacketBuffer buf) throws IOException {
/* 27 */     this.entityId = buf.readInt();
/* 28 */     this.logicOpcode = buf.readByte();
/*    */   }
/*    */ 
/*    */   
/*    */   public void writePacketData(PacketBuffer buf) throws IOException {
/* 33 */     buf.writeInt(this.entityId);
/* 34 */     buf.writeByte(this.logicOpcode);
/*    */   }
/*    */ 
/*    */   
/*    */   public void processPacket(INetHandlerPlayClient handler) {
/* 39 */     handler.handleEntityStatus(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public Entity getEntity(World worldIn) {
/* 44 */     return worldIn.getEntityByID(this.entityId);
/*    */   }
/*    */ 
/*    */   
/*    */   public byte getOpCode() {
/* 49 */     return this.logicOpcode;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\network\play\server\S19PacketEntityStatus.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */