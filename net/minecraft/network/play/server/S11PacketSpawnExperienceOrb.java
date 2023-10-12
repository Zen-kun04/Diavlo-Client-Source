/*    */ package net.minecraft.network.play.server;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.entity.item.EntityXPOrb;
/*    */ import net.minecraft.network.INetHandler;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.play.INetHandlerPlayClient;
/*    */ import net.minecraft.util.MathHelper;
/*    */ 
/*    */ 
/*    */ public class S11PacketSpawnExperienceOrb
/*    */   implements Packet<INetHandlerPlayClient>
/*    */ {
/*    */   private int entityID;
/*    */   private int posX;
/*    */   private int posY;
/*    */   private int posZ;
/*    */   private int xpValue;
/*    */   
/*    */   public S11PacketSpawnExperienceOrb() {}
/*    */   
/*    */   public S11PacketSpawnExperienceOrb(EntityXPOrb xpOrb) {
/* 24 */     this.entityID = xpOrb.getEntityId();
/* 25 */     this.posX = MathHelper.floor_double(xpOrb.posX * 32.0D);
/* 26 */     this.posY = MathHelper.floor_double(xpOrb.posY * 32.0D);
/* 27 */     this.posZ = MathHelper.floor_double(xpOrb.posZ * 32.0D);
/* 28 */     this.xpValue = xpOrb.getXpValue();
/*    */   }
/*    */ 
/*    */   
/*    */   public void readPacketData(PacketBuffer buf) throws IOException {
/* 33 */     this.entityID = buf.readVarIntFromBuffer();
/* 34 */     this.posX = buf.readInt();
/* 35 */     this.posY = buf.readInt();
/* 36 */     this.posZ = buf.readInt();
/* 37 */     this.xpValue = buf.readShort();
/*    */   }
/*    */ 
/*    */   
/*    */   public void writePacketData(PacketBuffer buf) throws IOException {
/* 42 */     buf.writeVarIntToBuffer(this.entityID);
/* 43 */     buf.writeInt(this.posX);
/* 44 */     buf.writeInt(this.posY);
/* 45 */     buf.writeInt(this.posZ);
/* 46 */     buf.writeShort(this.xpValue);
/*    */   }
/*    */ 
/*    */   
/*    */   public void processPacket(INetHandlerPlayClient handler) {
/* 51 */     handler.handleSpawnExperienceOrb(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getEntityID() {
/* 56 */     return this.entityID;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getX() {
/* 61 */     return this.posX;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getY() {
/* 66 */     return this.posY;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getZ() {
/* 71 */     return this.posZ;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getXPValue() {
/* 76 */     return this.xpValue;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\network\play\server\S11PacketSpawnExperienceOrb.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */