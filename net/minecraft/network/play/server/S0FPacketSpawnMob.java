/*     */ package net.minecraft.network.play.server;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import net.minecraft.entity.DataWatcher;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityList;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.network.INetHandler;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.PacketBuffer;
/*     */ import net.minecraft.network.play.INetHandlerPlayClient;
/*     */ import net.minecraft.util.MathHelper;
/*     */ 
/*     */ public class S0FPacketSpawnMob
/*     */   implements Packet<INetHandlerPlayClient>
/*     */ {
/*     */   private int entityId;
/*     */   private int type;
/*     */   private int x;
/*     */   private int y;
/*     */   private int z;
/*     */   private int velocityX;
/*     */   private int velocityY;
/*     */   private int velocityZ;
/*     */   private byte yaw;
/*     */   private byte pitch;
/*     */   private byte headPitch;
/*     */   private DataWatcher field_149043_l;
/*     */   private List<DataWatcher.WatchableObject> watcher;
/*     */   
/*     */   public S0FPacketSpawnMob() {}
/*     */   
/*     */   public S0FPacketSpawnMob(EntityLivingBase entityIn) {
/*  35 */     this.entityId = entityIn.getEntityId();
/*  36 */     this.type = (byte)EntityList.getEntityID((Entity)entityIn);
/*  37 */     this.x = MathHelper.floor_double(entityIn.posX * 32.0D);
/*  38 */     this.y = MathHelper.floor_double(entityIn.posY * 32.0D);
/*  39 */     this.z = MathHelper.floor_double(entityIn.posZ * 32.0D);
/*  40 */     this.yaw = (byte)(int)(entityIn.rotationYaw * 256.0F / 360.0F);
/*  41 */     this.pitch = (byte)(int)(entityIn.rotationPitch * 256.0F / 360.0F);
/*  42 */     this.headPitch = (byte)(int)(entityIn.rotationYawHead * 256.0F / 360.0F);
/*  43 */     double d0 = 3.9D;
/*  44 */     double d1 = entityIn.motionX;
/*  45 */     double d2 = entityIn.motionY;
/*  46 */     double d3 = entityIn.motionZ;
/*     */     
/*  48 */     if (d1 < -d0)
/*     */     {
/*  50 */       d1 = -d0;
/*     */     }
/*     */     
/*  53 */     if (d2 < -d0)
/*     */     {
/*  55 */       d2 = -d0;
/*     */     }
/*     */     
/*  58 */     if (d3 < -d0)
/*     */     {
/*  60 */       d3 = -d0;
/*     */     }
/*     */     
/*  63 */     if (d1 > d0)
/*     */     {
/*  65 */       d1 = d0;
/*     */     }
/*     */     
/*  68 */     if (d2 > d0)
/*     */     {
/*  70 */       d2 = d0;
/*     */     }
/*     */     
/*  73 */     if (d3 > d0)
/*     */     {
/*  75 */       d3 = d0;
/*     */     }
/*     */     
/*  78 */     this.velocityX = (int)(d1 * 8000.0D);
/*  79 */     this.velocityY = (int)(d2 * 8000.0D);
/*  80 */     this.velocityZ = (int)(d3 * 8000.0D);
/*  81 */     this.field_149043_l = entityIn.getDataWatcher();
/*     */   }
/*     */ 
/*     */   
/*     */   public void readPacketData(PacketBuffer buf) throws IOException {
/*  86 */     this.entityId = buf.readVarIntFromBuffer();
/*  87 */     this.type = buf.readByte() & 0xFF;
/*  88 */     this.x = buf.readInt();
/*  89 */     this.y = buf.readInt();
/*  90 */     this.z = buf.readInt();
/*  91 */     this.yaw = buf.readByte();
/*  92 */     this.pitch = buf.readByte();
/*  93 */     this.headPitch = buf.readByte();
/*  94 */     this.velocityX = buf.readShort();
/*  95 */     this.velocityY = buf.readShort();
/*  96 */     this.velocityZ = buf.readShort();
/*  97 */     this.watcher = DataWatcher.readWatchedListFromPacketBuffer(buf);
/*     */   }
/*     */ 
/*     */   
/*     */   public void writePacketData(PacketBuffer buf) throws IOException {
/* 102 */     buf.writeVarIntToBuffer(this.entityId);
/* 103 */     buf.writeByte(this.type & 0xFF);
/* 104 */     buf.writeInt(this.x);
/* 105 */     buf.writeInt(this.y);
/* 106 */     buf.writeInt(this.z);
/* 107 */     buf.writeByte(this.yaw);
/* 108 */     buf.writeByte(this.pitch);
/* 109 */     buf.writeByte(this.headPitch);
/* 110 */     buf.writeShort(this.velocityX);
/* 111 */     buf.writeShort(this.velocityY);
/* 112 */     buf.writeShort(this.velocityZ);
/* 113 */     this.field_149043_l.writeTo(buf);
/*     */   }
/*     */ 
/*     */   
/*     */   public void processPacket(INetHandlerPlayClient handler) {
/* 118 */     handler.handleSpawnMob(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<DataWatcher.WatchableObject> func_149027_c() {
/* 123 */     if (this.watcher == null)
/*     */     {
/* 125 */       this.watcher = this.field_149043_l.getAllWatched();
/*     */     }
/*     */     
/* 128 */     return this.watcher;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getEntityID() {
/* 133 */     return this.entityId;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getEntityType() {
/* 138 */     return this.type;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getX() {
/* 143 */     return this.x;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getY() {
/* 148 */     return this.y;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getZ() {
/* 153 */     return this.z;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getVelocityX() {
/* 158 */     return this.velocityX;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getVelocityY() {
/* 163 */     return this.velocityY;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getVelocityZ() {
/* 168 */     return this.velocityZ;
/*     */   }
/*     */ 
/*     */   
/*     */   public byte getYaw() {
/* 173 */     return this.yaw;
/*     */   }
/*     */ 
/*     */   
/*     */   public byte getPitch() {
/* 178 */     return this.pitch;
/*     */   }
/*     */ 
/*     */   
/*     */   public byte getHeadPitch() {
/* 183 */     return this.headPitch;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\network\play\server\S0FPacketSpawnMob.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */