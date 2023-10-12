/*     */ package net.minecraft.network.play.server;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import net.minecraft.entity.player.PlayerCapabilities;
/*     */ import net.minecraft.network.INetHandler;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.PacketBuffer;
/*     */ import net.minecraft.network.play.INetHandlerPlayClient;
/*     */ 
/*     */ 
/*     */ public class S39PacketPlayerAbilities
/*     */   implements Packet<INetHandlerPlayClient>
/*     */ {
/*     */   private boolean invulnerable;
/*     */   private boolean flying;
/*     */   private boolean allowFlying;
/*     */   private boolean creativeMode;
/*     */   private float flySpeed;
/*     */   private float walkSpeed;
/*     */   
/*     */   public S39PacketPlayerAbilities() {}
/*     */   
/*     */   public S39PacketPlayerAbilities(PlayerCapabilities capabilities) {
/*  24 */     setInvulnerable(capabilities.disableDamage);
/*  25 */     setFlying(capabilities.isFlying);
/*  26 */     setAllowFlying(capabilities.allowFlying);
/*  27 */     setCreativeMode(capabilities.isCreativeMode);
/*  28 */     setFlySpeed(capabilities.getFlySpeed());
/*  29 */     setWalkSpeed(capabilities.getWalkSpeed());
/*     */   }
/*     */ 
/*     */   
/*     */   public void readPacketData(PacketBuffer buf) throws IOException {
/*  34 */     byte b0 = buf.readByte();
/*  35 */     setInvulnerable(((b0 & 0x1) > 0));
/*  36 */     setFlying(((b0 & 0x2) > 0));
/*  37 */     setAllowFlying(((b0 & 0x4) > 0));
/*  38 */     setCreativeMode(((b0 & 0x8) > 0));
/*  39 */     setFlySpeed(buf.readFloat());
/*  40 */     setWalkSpeed(buf.readFloat());
/*     */   }
/*     */ 
/*     */   
/*     */   public void writePacketData(PacketBuffer buf) throws IOException {
/*  45 */     byte b0 = 0;
/*     */     
/*  47 */     if (isInvulnerable())
/*     */     {
/*  49 */       b0 = (byte)(b0 | 0x1);
/*     */     }
/*     */     
/*  52 */     if (isFlying())
/*     */     {
/*  54 */       b0 = (byte)(b0 | 0x2);
/*     */     }
/*     */     
/*  57 */     if (isAllowFlying())
/*     */     {
/*  59 */       b0 = (byte)(b0 | 0x4);
/*     */     }
/*     */     
/*  62 */     if (isCreativeMode())
/*     */     {
/*  64 */       b0 = (byte)(b0 | 0x8);
/*     */     }
/*     */     
/*  67 */     buf.writeByte(b0);
/*  68 */     buf.writeFloat(this.flySpeed);
/*  69 */     buf.writeFloat(this.walkSpeed);
/*     */   }
/*     */ 
/*     */   
/*     */   public void processPacket(INetHandlerPlayClient handler) {
/*  74 */     handler.handlePlayerAbilities(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isInvulnerable() {
/*  79 */     return this.invulnerable;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setInvulnerable(boolean isInvulnerable) {
/*  84 */     this.invulnerable = isInvulnerable;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFlying() {
/*  89 */     return this.flying;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setFlying(boolean isFlying) {
/*  94 */     this.flying = isFlying;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAllowFlying() {
/*  99 */     return this.allowFlying;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setAllowFlying(boolean isAllowFlying) {
/* 104 */     this.allowFlying = isAllowFlying;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCreativeMode() {
/* 109 */     return this.creativeMode;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCreativeMode(boolean isCreativeMode) {
/* 114 */     this.creativeMode = isCreativeMode;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getFlySpeed() {
/* 119 */     return this.flySpeed;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setFlySpeed(float flySpeedIn) {
/* 124 */     this.flySpeed = flySpeedIn;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getWalkSpeed() {
/* 129 */     return this.walkSpeed;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setWalkSpeed(float walkSpeedIn) {
/* 134 */     this.walkSpeed = walkSpeedIn;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\network\play\server\S39PacketPlayerAbilities.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */