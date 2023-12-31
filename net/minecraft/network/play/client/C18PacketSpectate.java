/*    */ package net.minecraft.network.play.client;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.util.UUID;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.network.INetHandler;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.play.INetHandlerPlayServer;
/*    */ import net.minecraft.world.WorldServer;
/*    */ 
/*    */ 
/*    */ public class C18PacketSpectate
/*    */   implements Packet<INetHandlerPlayServer>
/*    */ {
/*    */   private UUID id;
/*    */   
/*    */   public C18PacketSpectate() {}
/*    */   
/*    */   public C18PacketSpectate(UUID id) {
/* 21 */     this.id = id;
/*    */   }
/*    */ 
/*    */   
/*    */   public void readPacketData(PacketBuffer buf) throws IOException {
/* 26 */     this.id = buf.readUuid();
/*    */   }
/*    */ 
/*    */   
/*    */   public void writePacketData(PacketBuffer buf) throws IOException {
/* 31 */     buf.writeUuid(this.id);
/*    */   }
/*    */ 
/*    */   
/*    */   public void processPacket(INetHandlerPlayServer handler) {
/* 36 */     handler.handleSpectate(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public Entity getEntity(WorldServer worldIn) {
/* 41 */     return worldIn.getEntityFromUuid(this.id);
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\network\play\client\C18PacketSpectate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */