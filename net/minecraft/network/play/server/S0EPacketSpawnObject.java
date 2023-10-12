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
/*     */ public class S0EPacketSpawnObject
/*     */   implements Packet<INetHandlerPlayClient>
/*     */ {
/*     */   private int entityId;
/*     */   private int x;
/*     */   private int y;
/*     */   private int z;
/*     */   private int speedX;
/*     */   private int speedY;
/*     */   private int speedZ;
/*     */   private int pitch;
/*     */   private int yaw;
/*     */   private int type;
/*     */   private int field_149020_k;
/*     */   
/*     */   public S0EPacketSpawnObject() {}
/*     */   
/*     */   public S0EPacketSpawnObject(Entity entityIn, int typeIn) {
/*  30 */     this(entityIn, typeIn, 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public S0EPacketSpawnObject(Entity entityIn, int typeIn, int p_i45166_3_) {
/*  35 */     this.entityId = entityIn.getEntityId();
/*  36 */     this.x = MathHelper.floor_double(entityIn.posX * 32.0D);
/*  37 */     this.y = MathHelper.floor_double(entityIn.posY * 32.0D);
/*  38 */     this.z = MathHelper.floor_double(entityIn.posZ * 32.0D);
/*  39 */     this.pitch = MathHelper.floor_float(entityIn.rotationPitch * 256.0F / 360.0F);
/*  40 */     this.yaw = MathHelper.floor_float(entityIn.rotationYaw * 256.0F / 360.0F);
/*  41 */     this.type = typeIn;
/*  42 */     this.field_149020_k = p_i45166_3_;
/*     */     
/*  44 */     if (p_i45166_3_ > 0) {
/*     */       
/*  46 */       double d0 = entityIn.motionX;
/*  47 */       double d1 = entityIn.motionY;
/*  48 */       double d2 = entityIn.motionZ;
/*  49 */       double d3 = 3.9D;
/*     */       
/*  51 */       if (d0 < -d3)
/*     */       {
/*  53 */         d0 = -d3;
/*     */       }
/*     */       
/*  56 */       if (d1 < -d3)
/*     */       {
/*  58 */         d1 = -d3;
/*     */       }
/*     */       
/*  61 */       if (d2 < -d3)
/*     */       {
/*  63 */         d2 = -d3;
/*     */       }
/*     */       
/*  66 */       if (d0 > d3)
/*     */       {
/*  68 */         d0 = d3;
/*     */       }
/*     */       
/*  71 */       if (d1 > d3)
/*     */       {
/*  73 */         d1 = d3;
/*     */       }
/*     */       
/*  76 */       if (d2 > d3)
/*     */       {
/*  78 */         d2 = d3;
/*     */       }
/*     */       
/*  81 */       this.speedX = (int)(d0 * 8000.0D);
/*  82 */       this.speedY = (int)(d1 * 8000.0D);
/*  83 */       this.speedZ = (int)(d2 * 8000.0D);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void readPacketData(PacketBuffer buf) throws IOException {
/*  89 */     this.entityId = buf.readVarIntFromBuffer();
/*  90 */     this.type = buf.readByte();
/*  91 */     this.x = buf.readInt();
/*  92 */     this.y = buf.readInt();
/*  93 */     this.z = buf.readInt();
/*  94 */     this.pitch = buf.readByte();
/*  95 */     this.yaw = buf.readByte();
/*  96 */     this.field_149020_k = buf.readInt();
/*     */     
/*  98 */     if (this.field_149020_k > 0) {
/*     */       
/* 100 */       this.speedX = buf.readShort();
/* 101 */       this.speedY = buf.readShort();
/* 102 */       this.speedZ = buf.readShort();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void writePacketData(PacketBuffer buf) throws IOException {
/* 108 */     buf.writeVarIntToBuffer(this.entityId);
/* 109 */     buf.writeByte(this.type);
/* 110 */     buf.writeInt(this.x);
/* 111 */     buf.writeInt(this.y);
/* 112 */     buf.writeInt(this.z);
/* 113 */     buf.writeByte(this.pitch);
/* 114 */     buf.writeByte(this.yaw);
/* 115 */     buf.writeInt(this.field_149020_k);
/*     */     
/* 117 */     if (this.field_149020_k > 0) {
/*     */       
/* 119 */       buf.writeShort(this.speedX);
/* 120 */       buf.writeShort(this.speedY);
/* 121 */       buf.writeShort(this.speedZ);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void processPacket(INetHandlerPlayClient handler) {
/* 127 */     handler.handleSpawnObject(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getEntityID() {
/* 132 */     return this.entityId;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getX() {
/* 137 */     return this.x;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getY() {
/* 142 */     return this.y;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getZ() {
/* 147 */     return this.z;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSpeedX() {
/* 152 */     return this.speedX;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSpeedY() {
/* 157 */     return this.speedY;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSpeedZ() {
/* 162 */     return this.speedZ;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getPitch() {
/* 167 */     return this.pitch;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getYaw() {
/* 172 */     return this.yaw;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getType() {
/* 177 */     return this.type;
/*     */   }
/*     */ 
/*     */   
/*     */   public int func_149009_m() {
/* 182 */     return this.field_149020_k;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setX(int newX) {
/* 187 */     this.x = newX;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setY(int newY) {
/* 192 */     this.y = newY;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setZ(int newZ) {
/* 197 */     this.z = newZ;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSpeedX(int newSpeedX) {
/* 202 */     this.speedX = newSpeedX;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSpeedY(int newSpeedY) {
/* 207 */     this.speedY = newSpeedY;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSpeedZ(int newSpeedZ) {
/* 212 */     this.speedZ = newSpeedZ;
/*     */   }
/*     */ 
/*     */   
/*     */   public void func_149002_g(int p_149002_1_) {
/* 217 */     this.field_149020_k = p_149002_1_;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\network\play\server\S0EPacketSpawnObject.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */