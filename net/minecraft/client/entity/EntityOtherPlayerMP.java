/*     */ package net.minecraft.client.entity;
/*     */ 
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.DamageSource;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class EntityOtherPlayerMP
/*     */   extends AbstractClientPlayer
/*     */ {
/*     */   private boolean isItemInUse;
/*     */   private int otherPlayerMPPosRotationIncrements;
/*     */   private double otherPlayerMPX;
/*     */   private double otherPlayerMPY;
/*     */   private double otherPlayerMPZ;
/*     */   private double otherPlayerMPYaw;
/*     */   private double otherPlayerMPPitch;
/*     */   
/*     */   public EntityOtherPlayerMP(World worldIn, GameProfile gameProfileIn) {
/*  24 */     super(worldIn, gameProfileIn);
/*  25 */     this.stepHeight = 0.0F;
/*  26 */     this.noClip = true;
/*  27 */     this.renderOffsetY = 0.25F;
/*  28 */     this.renderDistanceWeight = 10.0D;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean attackEntityFrom(DamageSource source, float amount) {
/*  33 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPositionAndRotation2(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean p_180426_10_) {
/*  38 */     this.otherPlayerMPX = x;
/*  39 */     this.otherPlayerMPY = y;
/*  40 */     this.otherPlayerMPZ = z;
/*  41 */     this.otherPlayerMPYaw = yaw;
/*  42 */     this.otherPlayerMPPitch = pitch;
/*  43 */     this.otherPlayerMPPosRotationIncrements = posRotationIncrements;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdate() {
/*  48 */     this.renderOffsetY = 0.0F;
/*  49 */     super.onUpdate();
/*  50 */     this.prevLimbSwingAmount = this.limbSwingAmount;
/*  51 */     double d0 = this.posX - this.prevPosX;
/*  52 */     double d1 = this.posZ - this.prevPosZ;
/*  53 */     float f = MathHelper.sqrt_double(d0 * d0 + d1 * d1) * 4.0F;
/*     */     
/*  55 */     if (f > 1.0F)
/*     */     {
/*  57 */       f = 1.0F;
/*     */     }
/*     */     
/*  60 */     this.limbSwingAmount += (f - this.limbSwingAmount) * 0.4F;
/*  61 */     this.limbSwing += this.limbSwingAmount;
/*     */     
/*  63 */     if (!this.isItemInUse && isEating() && this.inventory.mainInventory[this.inventory.currentItem] != null) {
/*     */       
/*  65 */       ItemStack itemstack = this.inventory.mainInventory[this.inventory.currentItem];
/*  66 */       setItemInUse(this.inventory.mainInventory[this.inventory.currentItem], itemstack.getItem().getMaxItemUseDuration(itemstack));
/*  67 */       this.isItemInUse = true;
/*     */     }
/*  69 */     else if (this.isItemInUse && !isEating()) {
/*     */       
/*  71 */       clearItemInUse();
/*  72 */       this.isItemInUse = false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onLivingUpdate() {
/*  78 */     if (this.otherPlayerMPPosRotationIncrements > 0) {
/*     */       
/*  80 */       double d0 = this.posX + (this.otherPlayerMPX - this.posX) / this.otherPlayerMPPosRotationIncrements;
/*  81 */       double d1 = this.posY + (this.otherPlayerMPY - this.posY) / this.otherPlayerMPPosRotationIncrements;
/*  82 */       double d2 = this.posZ + (this.otherPlayerMPZ - this.posZ) / this.otherPlayerMPPosRotationIncrements;
/*     */       
/*     */       double d3;
/*  85 */       for (d3 = this.otherPlayerMPYaw - this.rotationYaw; d3 < -180.0D; d3 += 360.0D);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  90 */       while (d3 >= 180.0D)
/*     */       {
/*  92 */         d3 -= 360.0D;
/*     */       }
/*     */       
/*  95 */       this.rotationYaw = (float)(this.rotationYaw + d3 / this.otherPlayerMPPosRotationIncrements);
/*  96 */       this.rotationPitch = (float)(this.rotationPitch + (this.otherPlayerMPPitch - this.rotationPitch) / this.otherPlayerMPPosRotationIncrements);
/*  97 */       this.otherPlayerMPPosRotationIncrements--;
/*  98 */       setPosition(d0, d1, d2);
/*  99 */       setRotation(this.rotationYaw, this.rotationPitch);
/*     */     } 
/*     */     
/* 102 */     this.prevCameraYaw = this.cameraYaw;
/* 103 */     updateArmSwingProgress();
/* 104 */     float f1 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
/* 105 */     float f = (float)Math.atan(-this.motionY * 0.20000000298023224D) * 15.0F;
/*     */     
/* 107 */     if (f1 > 0.1F)
/*     */     {
/* 109 */       f1 = 0.1F;
/*     */     }
/*     */     
/* 112 */     if (!this.onGround || getHealth() <= 0.0F)
/*     */     {
/* 114 */       f1 = 0.0F;
/*     */     }
/*     */     
/* 117 */     if (this.onGround || getHealth() <= 0.0F)
/*     */     {
/* 119 */       f = 0.0F;
/*     */     }
/*     */     
/* 122 */     this.cameraYaw += (f1 - this.cameraYaw) * 0.4F;
/* 123 */     this.cameraPitch += (f - this.cameraPitch) * 0.8F;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCurrentItemOrArmor(int slotIn, ItemStack stack) {
/* 128 */     if (slotIn == 0) {
/*     */       
/* 130 */       this.inventory.mainInventory[this.inventory.currentItem] = stack;
/*     */     }
/*     */     else {
/*     */       
/* 134 */       this.inventory.armorInventory[slotIn - 1] = stack;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void addChatMessage(IChatComponent component) {
/* 140 */     (Minecraft.getMinecraft()).ingameGUI.getChatGUI().printChatMessage(component);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canCommandSenderUseCommand(int permLevel, String commandName) {
/* 145 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockPos getPosition() {
/* 150 */     return new BlockPos(this.posX + 0.5D, this.posY + 0.5D, this.posZ + 0.5D);
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\entity\EntityOtherPlayerMP.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */