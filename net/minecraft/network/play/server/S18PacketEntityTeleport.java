/*     */ package net.minecraft.network.play.server;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.network.INetHandler;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.PacketBuffer;
/*     */ import net.minecraft.network.play.INetHandlerPlayClient;
/*     */ import net.minecraft.util.MathHelper;
/*     */ 
/*     */ 
/*     */ public class S18PacketEntityTeleport
/*     */   implements Packet<INetHandlerPlayClient>
/*     */ {
/*     */   private int entityId;
/*     */   private int posX;
/*     */   private int posY;
/*     */   private int posZ;
/*     */   private byte yaw;
/*     */   private byte pitch;
/*     */   private boolean onGround;
/*     */   
/*     */   public S18PacketEntityTeleport() {}
/*     */   
/*     */   public S18PacketEntityTeleport(Entity entityIn) {
/*  26 */     this.entityId = entityIn.getEntityId();
/*  27 */     this.posX = MathHelper.floor_double(entityIn.posX * 32.0D);
/*  28 */     this.posY = MathHelper.floor_double(entityIn.posY * 32.0D);
/*  29 */     this.posZ = MathHelper.floor_double(entityIn.posZ * 32.0D);
/*  30 */     this.yaw = (byte)(int)(entityIn.rotationYaw * 256.0F / 360.0F);
/*  31 */     this.pitch = (byte)(int)(entityIn.rotationPitch * 256.0F / 360.0F);
/*  32 */     this.onGround = entityIn.onGround;
/*     */   }
/*     */ 
/*     */   
/*     */   public S18PacketEntityTeleport(int entityIdIn, int posXIn, int posYIn, int posZIn, byte yawIn, byte pitchIn, boolean onGroundIn) {
/*  37 */     this.entityId = entityIdIn;
/*  38 */     this.posX = posXIn;
/*  39 */     this.posY = posYIn;
/*  40 */     this.posZ = posZIn;
/*  41 */     this.yaw = yawIn;
/*  42 */     this.pitch = pitchIn;
/*  43 */     this.onGround = onGroundIn;
/*     */   }
/*     */ 
/*     */   
/*     */   public void readPacketData(PacketBuffer buf) throws IOException {
/*  48 */     this.entityId = buf.readVarIntFromBuffer();
/*  49 */     this.posX = buf.readInt();
/*  50 */     this.posY = buf.readInt();
/*  51 */     this.posZ = buf.readInt();
/*  52 */     this.yaw = buf.readByte();
/*  53 */     this.pitch = buf.readByte();
/*  54 */     this.onGround = buf.readBoolean();
/*     */   }
/*     */ 
/*     */   
/*     */   public void writePacketData(PacketBuffer buf) throws IOException {
/*  59 */     buf.writeVarIntToBuffer(this.entityId);
/*  60 */     buf.writeInt(this.posX);
/*  61 */     buf.writeInt(this.posY);
/*  62 */     buf.writeInt(this.posZ);
/*  63 */     buf.writeByte(this.yaw);
/*  64 */     buf.writeByte(this.pitch);
/*  65 */     buf.writeBoolean(this.onGround);
/*     */   }
/*     */ 
/*     */   
/*     */   public void processPacket(INetHandlerPlayClient handler) {
/*  70 */     handler.handleEntityTeleport(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getEntityId() {
/*  75 */     return this.entityId;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getX() {
/*  80 */     return this.posX;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getY() {
/*  85 */     return this.posY;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getZ() {
/*  90 */     return this.posZ;
/*     */   }
/*     */ 
/*     */   
/*     */   public byte getYaw() {
/*  95 */     return this.yaw;
/*     */   }
/*     */ 
/*     */   
/*     */   public byte getPitch() {
/* 100 */     return this.pitch;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getOnGround() {
/* 105 */     return this.onGround;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\network\play\server\S18PacketEntityTeleport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */