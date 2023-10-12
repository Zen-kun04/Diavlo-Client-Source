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
/*    */ public class S0BPacketAnimation
/*    */   implements Packet<INetHandlerPlayClient>
/*    */ {
/*    */   private int entityId;
/*    */   private int type;
/*    */   
/*    */   public S0BPacketAnimation() {}
/*    */   
/*    */   public S0BPacketAnimation(Entity ent, int animationType) {
/* 20 */     this.entityId = ent.getEntityId();
/* 21 */     this.type = animationType;
/*    */   }
/*    */ 
/*    */   
/*    */   public void readPacketData(PacketBuffer buf) throws IOException {
/* 26 */     this.entityId = buf.readVarIntFromBuffer();
/* 27 */     this.type = buf.readUnsignedByte();
/*    */   }
/*    */ 
/*    */   
/*    */   public void writePacketData(PacketBuffer buf) throws IOException {
/* 32 */     buf.writeVarIntToBuffer(this.entityId);
/* 33 */     buf.writeByte(this.type);
/*    */   }
/*    */ 
/*    */   
/*    */   public void processPacket(INetHandlerPlayClient handler) {
/* 38 */     handler.handleAnimation(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getEntityID() {
/* 43 */     return this.entityId;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getAnimationType() {
/* 48 */     return this.type;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\network\play\server\S0BPacketAnimation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */