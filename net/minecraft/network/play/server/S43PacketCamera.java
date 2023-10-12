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
/*    */ public class S43PacketCamera
/*    */   implements Packet<INetHandlerPlayClient>
/*    */ {
/*    */   public int entityId;
/*    */   
/*    */   public S43PacketCamera() {}
/*    */   
/*    */   public S43PacketCamera(Entity entityIn) {
/* 20 */     this.entityId = entityIn.getEntityId();
/*    */   }
/*    */ 
/*    */   
/*    */   public void readPacketData(PacketBuffer buf) throws IOException {
/* 25 */     this.entityId = buf.readVarIntFromBuffer();
/*    */   }
/*    */ 
/*    */   
/*    */   public void writePacketData(PacketBuffer buf) throws IOException {
/* 30 */     buf.writeVarIntToBuffer(this.entityId);
/*    */   }
/*    */ 
/*    */   
/*    */   public void processPacket(INetHandlerPlayClient handler) {
/* 35 */     handler.handleCamera(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public Entity getEntity(World worldIn) {
/* 40 */     return worldIn.getEntityByID(this.entityId);
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\network\play\server\S43PacketCamera.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */