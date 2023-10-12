/*    */ package net.minecraft.entity.player;
/*    */ 
/*    */ import net.minecraft.nbt.NBTBase;
/*    */ import net.minecraft.nbt.NBTTagCompound;
/*    */ 
/*    */ public class PlayerCapabilities {
/*    */   public boolean disableDamage;
/*    */   public boolean isFlying;
/*    */   public boolean allowFlying;
/*    */   public boolean isCreativeMode;
/*    */   public boolean allowEdit = true;
/* 12 */   private float flySpeed = 0.05F;
/* 13 */   private float walkSpeed = 0.1F;
/*    */ 
/*    */   
/*    */   public void writeCapabilitiesToNBT(NBTTagCompound tagCompound) {
/* 17 */     NBTTagCompound nbttagcompound = new NBTTagCompound();
/* 18 */     nbttagcompound.setBoolean("invulnerable", this.disableDamage);
/* 19 */     nbttagcompound.setBoolean("flying", this.isFlying);
/* 20 */     nbttagcompound.setBoolean("mayfly", this.allowFlying);
/* 21 */     nbttagcompound.setBoolean("instabuild", this.isCreativeMode);
/* 22 */     nbttagcompound.setBoolean("mayBuild", this.allowEdit);
/* 23 */     nbttagcompound.setFloat("flySpeed", this.flySpeed);
/* 24 */     nbttagcompound.setFloat("walkSpeed", this.walkSpeed);
/* 25 */     tagCompound.setTag("abilities", (NBTBase)nbttagcompound);
/*    */   }
/*    */ 
/*    */   
/*    */   public void readCapabilitiesFromNBT(NBTTagCompound tagCompound) {
/* 30 */     if (tagCompound.hasKey("abilities", 10)) {
/*    */       
/* 32 */       NBTTagCompound nbttagcompound = tagCompound.getCompoundTag("abilities");
/* 33 */       this.disableDamage = nbttagcompound.getBoolean("invulnerable");
/* 34 */       this.isFlying = nbttagcompound.getBoolean("flying");
/* 35 */       this.allowFlying = nbttagcompound.getBoolean("mayfly");
/* 36 */       this.isCreativeMode = nbttagcompound.getBoolean("instabuild");
/*    */       
/* 38 */       if (nbttagcompound.hasKey("flySpeed", 99)) {
/*    */         
/* 40 */         this.flySpeed = nbttagcompound.getFloat("flySpeed");
/* 41 */         this.walkSpeed = nbttagcompound.getFloat("walkSpeed");
/*    */       } 
/*    */       
/* 44 */       if (nbttagcompound.hasKey("mayBuild", 1))
/*    */       {
/* 46 */         this.allowEdit = nbttagcompound.getBoolean("mayBuild");
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public float getFlySpeed() {
/* 53 */     return this.flySpeed;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setFlySpeed(float speed) {
/* 58 */     this.flySpeed = speed;
/*    */   }
/*    */ 
/*    */   
/*    */   public float getWalkSpeed() {
/* 63 */     return this.walkSpeed;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setPlayerWalkSpeed(float speed) {
/* 68 */     this.walkSpeed = speed;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\player\PlayerCapabilities.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */