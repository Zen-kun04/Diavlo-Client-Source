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
/*    */ public class S1DPacketEntityEffect
/*    */   implements Packet<INetHandlerPlayClient>
/*    */ {
/*    */   private int entityId;
/*    */   private byte effectId;
/*    */   private byte amplifier;
/*    */   private int duration;
/*    */   private byte hideParticles;
/*    */   
/*    */   public S1DPacketEntityEffect() {}
/*    */   
/*    */   public S1DPacketEntityEffect(int entityIdIn, PotionEffect effect) {
/* 23 */     this.entityId = entityIdIn;
/* 24 */     this.effectId = (byte)(effect.getPotionID() & 0xFF);
/* 25 */     this.amplifier = (byte)(effect.getAmplifier() & 0xFF);
/*    */     
/* 27 */     if (effect.getDuration() > 32767) {
/*    */       
/* 29 */       this.duration = 32767;
/*    */     }
/*    */     else {
/*    */       
/* 33 */       this.duration = effect.getDuration();
/*    */     } 
/*    */     
/* 36 */     this.hideParticles = (byte)(effect.getIsShowParticles() ? 1 : 0);
/*    */   }
/*    */ 
/*    */   
/*    */   public void readPacketData(PacketBuffer buf) throws IOException {
/* 41 */     this.entityId = buf.readVarIntFromBuffer();
/* 42 */     this.effectId = buf.readByte();
/* 43 */     this.amplifier = buf.readByte();
/* 44 */     this.duration = buf.readVarIntFromBuffer();
/* 45 */     this.hideParticles = buf.readByte();
/*    */   }
/*    */ 
/*    */   
/*    */   public void writePacketData(PacketBuffer buf) throws IOException {
/* 50 */     buf.writeVarIntToBuffer(this.entityId);
/* 51 */     buf.writeByte(this.effectId);
/* 52 */     buf.writeByte(this.amplifier);
/* 53 */     buf.writeVarIntToBuffer(this.duration);
/* 54 */     buf.writeByte(this.hideParticles);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean func_149429_c() {
/* 59 */     return (this.duration == 32767);
/*    */   }
/*    */ 
/*    */   
/*    */   public void processPacket(INetHandlerPlayClient handler) {
/* 64 */     handler.handleEntityEffect(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getEntityId() {
/* 69 */     return this.entityId;
/*    */   }
/*    */ 
/*    */   
/*    */   public byte getEffectId() {
/* 74 */     return this.effectId;
/*    */   }
/*    */ 
/*    */   
/*    */   public byte getAmplifier() {
/* 79 */     return this.amplifier;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getDuration() {
/* 84 */     return this.duration;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean func_179707_f() {
/* 89 */     return (this.hideParticles != 0);
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\network\play\server\S1DPacketEntityEffect.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */