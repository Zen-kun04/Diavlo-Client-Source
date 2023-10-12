/*    */ package net.minecraft.network.play.server;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.network.INetHandler;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.play.INetHandlerPlayClient;
/*    */ import net.minecraft.util.MathHelper;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class S2CPacketSpawnGlobalEntity
/*    */   implements Packet<INetHandlerPlayClient>
/*    */ {
/*    */   private int entityId;
/*    */   private int x;
/*    */   private int y;
/*    */   private int z;
/*    */   private int type;
/*    */   
/*    */   public S2CPacketSpawnGlobalEntity() {}
/*    */   
/*    */   public S2CPacketSpawnGlobalEntity(Entity entityIn) {
/* 25 */     this.entityId = entityIn.getEntityId();
/* 26 */     this.x = MathHelper.floor_double(entityIn.posX * 32.0D);
/* 27 */     this.y = MathHelper.floor_double(entityIn.posY * 32.0D);
/* 28 */     this.z = MathHelper.floor_double(entityIn.posZ * 32.0D);
/*    */     
/* 30 */     if (entityIn instanceof net.minecraft.entity.effect.EntityLightningBolt)
/*    */     {
/* 32 */       this.type = 1;
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void readPacketData(PacketBuffer buf) throws IOException {
/* 38 */     this.entityId = buf.readVarIntFromBuffer();
/* 39 */     this.type = buf.readByte();
/* 40 */     this.x = buf.readInt();
/* 41 */     this.y = buf.readInt();
/* 42 */     this.z = buf.readInt();
/*    */   }
/*    */ 
/*    */   
/*    */   public void writePacketData(PacketBuffer buf) throws IOException {
/* 47 */     buf.writeVarIntToBuffer(this.entityId);
/* 48 */     buf.writeByte(this.type);
/* 49 */     buf.writeInt(this.x);
/* 50 */     buf.writeInt(this.y);
/* 51 */     buf.writeInt(this.z);
/*    */   }
/*    */ 
/*    */   
/*    */   public void processPacket(INetHandlerPlayClient handler) {
/* 56 */     handler.handleSpawnGlobalEntity(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public int func_149052_c() {
/* 61 */     return this.entityId;
/*    */   }
/*    */ 
/*    */   
/*    */   public int func_149051_d() {
/* 66 */     return this.x;
/*    */   }
/*    */ 
/*    */   
/*    */   public int func_149050_e() {
/* 71 */     return this.y;
/*    */   }
/*    */ 
/*    */   
/*    */   public int func_149049_f() {
/* 76 */     return this.z;
/*    */   }
/*    */ 
/*    */   
/*    */   public int func_149053_g() {
/* 81 */     return this.type;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\network\play\server\S2CPacketSpawnGlobalEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */