/*    */ package net.minecraft.network.play.server;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.INetHandler;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.play.INetHandlerPlayClient;
/*    */ 
/*    */ 
/*    */ public class S06PacketUpdateHealth
/*    */   implements Packet<INetHandlerPlayClient>
/*    */ {
/*    */   private float health;
/*    */   private int foodLevel;
/*    */   private float saturationLevel;
/*    */   
/*    */   public S06PacketUpdateHealth() {}
/*    */   
/*    */   public S06PacketUpdateHealth(float healthIn, int foodLevelIn, float saturationIn) {
/* 20 */     this.health = healthIn;
/* 21 */     this.foodLevel = foodLevelIn;
/* 22 */     this.saturationLevel = saturationIn;
/*    */   }
/*    */ 
/*    */   
/*    */   public void readPacketData(PacketBuffer buf) throws IOException {
/* 27 */     this.health = buf.readFloat();
/* 28 */     this.foodLevel = buf.readVarIntFromBuffer();
/* 29 */     this.saturationLevel = buf.readFloat();
/*    */   }
/*    */ 
/*    */   
/*    */   public void writePacketData(PacketBuffer buf) throws IOException {
/* 34 */     buf.writeFloat(this.health);
/* 35 */     buf.writeVarIntToBuffer(this.foodLevel);
/* 36 */     buf.writeFloat(this.saturationLevel);
/*    */   }
/*    */ 
/*    */   
/*    */   public void processPacket(INetHandlerPlayClient handler) {
/* 41 */     handler.handleUpdateHealth(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public float getHealth() {
/* 46 */     return this.health;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getFoodLevel() {
/* 51 */     return this.foodLevel;
/*    */   }
/*    */ 
/*    */   
/*    */   public float getSaturationLevel() {
/* 56 */     return this.saturationLevel;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\network\play\server\S06PacketUpdateHealth.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */