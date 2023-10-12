/*    */ package net.minecraft.network.play.server;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.INetHandler;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.play.INetHandlerPlayClient;
/*    */ import net.minecraft.potion.PotionEffect;
/*    */ 
/*    */ 
/*    */ public class S1EPacketRemoveEntityEffect
/*    */   implements Packet<INetHandlerPlayClient>
/*    */ {
/*    */   private int entityId;
/*    */   private int effectId;
/*    */   
/*    */   public S1EPacketRemoveEntityEffect() {}
/*    */   
/*    */   public S1EPacketRemoveEntityEffect(int entityIdIn, PotionEffect effect) {
/* 20 */     this.entityId = entityIdIn;
/* 21 */     this.effectId = effect.getPotionID();
/*    */   }
/*    */ 
/*    */   
/*    */   public void readPacketData(PacketBuffer buf) throws IOException {
/* 26 */     this.entityId = buf.readVarIntFromBuffer();
/* 27 */     this.effectId = buf.readUnsignedByte();
/*    */   }
/*    */ 
/*    */   
/*    */   public void writePacketData(PacketBuffer buf) throws IOException {
/* 32 */     buf.writeVarIntToBuffer(this.entityId);
/* 33 */     buf.writeByte(this.effectId);
/*    */   }
/*    */ 
/*    */   
/*    */   public void processPacket(INetHandlerPlayClient handler) {
/* 38 */     handler.handleRemoveEntityEffect(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getEntityId() {
/* 43 */     return this.entityId;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getEffectId() {
/* 48 */     return this.effectId;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\network\play\server\S1EPacketRemoveEntityEffect.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */