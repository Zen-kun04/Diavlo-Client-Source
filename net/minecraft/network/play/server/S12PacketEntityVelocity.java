/*     */ package net.minecraft.network.play.server;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.network.INetHandler;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.PacketBuffer;
/*     */ import net.minecraft.network.play.INetHandlerPlayClient;
/*     */ 
/*     */ 
/*     */ public class S12PacketEntityVelocity
/*     */   implements Packet<INetHandlerPlayClient>
/*     */ {
/*     */   private int entityID;
/*     */   private int motionX;
/*     */   private int motionY;
/*     */   private int motionZ;
/*     */   
/*     */   public S12PacketEntityVelocity() {}
/*     */   
/*     */   public S12PacketEntityVelocity(Entity entityIn) {
/*  22 */     this(entityIn.getEntityId(), entityIn.motionX, entityIn.motionY, entityIn.motionZ);
/*     */   }
/*     */ 
/*     */   
/*     */   public S12PacketEntityVelocity(int entityIDIn, double motionXIn, double motionYIn, double motionZIn) {
/*  27 */     this.entityID = entityIDIn;
/*  28 */     double d0 = 3.9D;
/*     */     
/*  30 */     if (motionXIn < -d0)
/*     */     {
/*  32 */       motionXIn = -d0;
/*     */     }
/*     */     
/*  35 */     if (motionYIn < -d0)
/*     */     {
/*  37 */       motionYIn = -d0;
/*     */     }
/*     */     
/*  40 */     if (motionZIn < -d0)
/*     */     {
/*  42 */       motionZIn = -d0;
/*     */     }
/*     */     
/*  45 */     if (motionXIn > d0)
/*     */     {
/*  47 */       motionXIn = d0;
/*     */     }
/*     */     
/*  50 */     if (motionYIn > d0)
/*     */     {
/*  52 */       motionYIn = d0;
/*     */     }
/*     */     
/*  55 */     if (motionZIn > d0)
/*     */     {
/*  57 */       motionZIn = d0;
/*     */     }
/*     */     
/*  60 */     this.motionX = (int)(motionXIn * 8000.0D);
/*  61 */     this.motionY = (int)(motionYIn * 8000.0D);
/*  62 */     this.motionZ = (int)(motionZIn * 8000.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   public void readPacketData(PacketBuffer buf) throws IOException {
/*  67 */     this.entityID = buf.readVarIntFromBuffer();
/*  68 */     this.motionX = buf.readShort();
/*  69 */     this.motionY = buf.readShort();
/*  70 */     this.motionZ = buf.readShort();
/*     */   }
/*     */ 
/*     */   
/*     */   public void writePacketData(PacketBuffer buf) throws IOException {
/*  75 */     buf.writeVarIntToBuffer(this.entityID);
/*  76 */     buf.writeShort(this.motionX);
/*  77 */     buf.writeShort(this.motionY);
/*  78 */     buf.writeShort(this.motionZ);
/*     */   }
/*     */ 
/*     */   
/*     */   public void processPacket(INetHandlerPlayClient handler) {
/*  83 */     handler.handleEntityVelocity(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getEntityID() {
/*  88 */     return this.entityID;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMotionX() {
/*  93 */     return this.motionX;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMotionY() {
/*  98 */     return this.motionY;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMotionZ() {
/* 103 */     return this.motionZ;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\network\play\server\S12PacketEntityVelocity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */