/*    */ package net.minecraft.util;
/*    */ 
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ import net.minecraft.item.ItemStack;
/*    */ 
/*    */ 
/*    */ public class EntityDamageSource
/*    */   extends DamageSource
/*    */ {
/*    */   protected Entity damageSourceEntity;
/*    */   private boolean isThornsDamage = false;
/*    */   
/*    */   public EntityDamageSource(String damageTypeIn, Entity damageSourceEntityIn) {
/* 15 */     super(damageTypeIn);
/* 16 */     this.damageSourceEntity = damageSourceEntityIn;
/*    */   }
/*    */ 
/*    */   
/*    */   public EntityDamageSource setIsThornsDamage() {
/* 21 */     this.isThornsDamage = true;
/* 22 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean getIsThornsDamage() {
/* 27 */     return this.isThornsDamage;
/*    */   }
/*    */ 
/*    */   
/*    */   public Entity getEntity() {
/* 32 */     return this.damageSourceEntity;
/*    */   }
/*    */ 
/*    */   
/*    */   public IChatComponent getDeathMessage(EntityLivingBase entityLivingBaseIn) {
/* 37 */     ItemStack itemstack = (this.damageSourceEntity instanceof EntityLivingBase) ? ((EntityLivingBase)this.damageSourceEntity).getHeldItem() : null;
/* 38 */     String s = "death.attack." + this.damageType;
/* 39 */     String s1 = s + ".item";
/* 40 */     return (itemstack != null && itemstack.hasDisplayName() && StatCollector.canTranslate(s1)) ? new ChatComponentTranslation(s1, new Object[] { entityLivingBaseIn.getDisplayName(), this.damageSourceEntity.getDisplayName(), itemstack.getChatComponent() }) : new ChatComponentTranslation(s, new Object[] { entityLivingBaseIn.getDisplayName(), this.damageSourceEntity.getDisplayName() });
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isDifficultyScaled() {
/* 45 */     return (this.damageSourceEntity != null && this.damageSourceEntity instanceof EntityLivingBase && !(this.damageSourceEntity instanceof net.minecraft.entity.player.EntityPlayer));
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraf\\util\EntityDamageSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */