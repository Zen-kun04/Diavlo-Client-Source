/*     */ package net.minecraft.network.play.client;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import net.minecraft.network.INetHandler;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.PacketBuffer;
/*     */ import net.minecraft.network.play.INetHandlerPlayServer;
/*     */ 
/*     */ 
/*     */ public class C03PacketPlayer
/*     */   implements Packet<INetHandlerPlayServer>
/*     */ {
/*     */   protected double x;
/*     */   public double y;
/*     */   protected double z;
/*     */   protected float yaw;
/*     */   protected float pitch;
/*     */   protected boolean onGround;
/*     */   protected boolean moving;
/*     */   protected boolean rotating;
/*     */   
/*     */   public C03PacketPlayer() {}
/*     */   
/*     */   public C03PacketPlayer(boolean isOnGround) {
/*  25 */     this.onGround = isOnGround;
/*     */   }
/*     */ 
/*     */   
/*     */   public void processPacket(INetHandlerPlayServer handler) {
/*  30 */     handler.processPlayer(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public void readPacketData(PacketBuffer buf) throws IOException {
/*  35 */     this.onGround = (buf.readUnsignedByte() != 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void writePacketData(PacketBuffer buf) throws IOException {
/*  40 */     buf.writeByte(this.onGround ? 1 : 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public double getPositionX() {
/*  45 */     return this.x;
/*     */   }
/*     */ 
/*     */   
/*     */   public double getPositionY() {
/*  50 */     return this.y;
/*     */   }
/*     */ 
/*     */   
/*     */   public double getPositionZ() {
/*  55 */     return this.z;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getYaw() {
/*  60 */     return this.yaw;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getPitch() {
/*  65 */     return this.pitch;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOnGround() {
/*  70 */     return this.onGround;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isMoving() {
/*  75 */     return this.moving;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getRotating() {
/*  80 */     return this.rotating;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setMoving(boolean isMoving) {
/*  85 */     this.moving = isMoving;
/*     */   }
/*     */   
/*     */   public static class C04PacketPlayerPosition
/*     */     extends C03PacketPlayer
/*     */   {
/*     */     public C04PacketPlayerPosition() {
/*  92 */       this.moving = true;
/*     */     }
/*     */ 
/*     */     
/*     */     public C04PacketPlayerPosition(double posX, double posY, double posZ, boolean isOnGround) {
/*  97 */       this.x = posX;
/*  98 */       this.y = posY;
/*  99 */       this.z = posZ;
/* 100 */       this.onGround = isOnGround;
/* 101 */       this.moving = true;
/*     */     }
/*     */ 
/*     */     
/*     */     public void readPacketData(PacketBuffer buf) throws IOException {
/* 106 */       this.x = buf.readDouble();
/* 107 */       this.y = buf.readDouble();
/* 108 */       this.z = buf.readDouble();
/* 109 */       super.readPacketData(buf);
/*     */     }
/*     */ 
/*     */     
/*     */     public void writePacketData(PacketBuffer buf) throws IOException {
/* 114 */       buf.writeDouble(this.x);
/* 115 */       buf.writeDouble(this.y);
/* 116 */       buf.writeDouble(this.z);
/* 117 */       super.writePacketData(buf);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class C05PacketPlayerLook
/*     */     extends C03PacketPlayer
/*     */   {
/*     */     public C05PacketPlayerLook() {
/* 125 */       this.rotating = true;
/*     */     }
/*     */ 
/*     */     
/*     */     public C05PacketPlayerLook(float playerYaw, float playerPitch, boolean isOnGround) {
/* 130 */       this.yaw = playerYaw;
/* 131 */       this.pitch = playerPitch;
/* 132 */       this.onGround = isOnGround;
/* 133 */       this.rotating = true;
/*     */     }
/*     */ 
/*     */     
/*     */     public void readPacketData(PacketBuffer buf) throws IOException {
/* 138 */       this.yaw = buf.readFloat();
/* 139 */       this.pitch = buf.readFloat();
/* 140 */       super.readPacketData(buf);
/*     */     }
/*     */ 
/*     */     
/*     */     public void writePacketData(PacketBuffer buf) throws IOException {
/* 145 */       buf.writeFloat(this.yaw);
/* 146 */       buf.writeFloat(this.pitch);
/* 147 */       super.writePacketData(buf);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class C06PacketPlayerPosLook
/*     */     extends C03PacketPlayer
/*     */   {
/*     */     public C06PacketPlayerPosLook() {
/* 155 */       this.moving = true;
/* 156 */       this.rotating = true;
/*     */     }
/*     */ 
/*     */     
/*     */     public C06PacketPlayerPosLook(double playerX, double playerY, double playerZ, float playerYaw, float playerPitch, boolean playerIsOnGround) {
/* 161 */       this.x = playerX;
/* 162 */       this.y = playerY;
/* 163 */       this.z = playerZ;
/* 164 */       this.yaw = playerYaw;
/* 165 */       this.pitch = playerPitch;
/* 166 */       this.onGround = playerIsOnGround;
/* 167 */       this.rotating = true;
/* 168 */       this.moving = true;
/*     */     }
/*     */ 
/*     */     
/*     */     public void readPacketData(PacketBuffer buf) throws IOException {
/* 173 */       this.x = buf.readDouble();
/* 174 */       this.y = buf.readDouble();
/* 175 */       this.z = buf.readDouble();
/* 176 */       this.yaw = buf.readFloat();
/* 177 */       this.pitch = buf.readFloat();
/* 178 */       super.readPacketData(buf);
/*     */     }
/*     */ 
/*     */     
/*     */     public void writePacketData(PacketBuffer buf) throws IOException {
/* 183 */       buf.writeDouble(this.x);
/* 184 */       buf.writeDouble(this.y);
/* 185 */       buf.writeDouble(this.z);
/* 186 */       buf.writeFloat(this.yaw);
/* 187 */       buf.writeFloat(this.pitch);
/* 188 */       super.writePacketData(buf);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\network\play\client\C03PacketPlayer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */