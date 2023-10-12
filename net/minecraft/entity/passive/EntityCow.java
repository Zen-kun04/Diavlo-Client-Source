/*     */ package net.minecraft.entity.passive;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.entity.EntityAgeable;
/*     */ import net.minecraft.entity.EntityCreature;
/*     */ import net.minecraft.entity.EntityLiving;
/*     */ import net.minecraft.entity.SharedMonsterAttributes;
/*     */ import net.minecraft.entity.ai.EntityAIBase;
/*     */ import net.minecraft.entity.ai.EntityAIFollowParent;
/*     */ import net.minecraft.entity.ai.EntityAILookIdle;
/*     */ import net.minecraft.entity.ai.EntityAIMate;
/*     */ import net.minecraft.entity.ai.EntityAIPanic;
/*     */ import net.minecraft.entity.ai.EntityAISwimming;
/*     */ import net.minecraft.entity.ai.EntityAITempt;
/*     */ import net.minecraft.entity.ai.EntityAIWander;
/*     */ import net.minecraft.entity.ai.EntityAIWatchClosest;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.pathfinding.PathNavigateGround;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class EntityCow extends EntityAnimal {
/*     */   public EntityCow(World worldIn) {
/*  26 */     super(worldIn);
/*  27 */     setSize(0.9F, 1.3F);
/*  28 */     ((PathNavigateGround)getNavigator()).setAvoidsWater(true);
/*  29 */     this.tasks.addTask(0, (EntityAIBase)new EntityAISwimming((EntityLiving)this));
/*  30 */     this.tasks.addTask(1, (EntityAIBase)new EntityAIPanic((EntityCreature)this, 2.0D));
/*  31 */     this.tasks.addTask(2, (EntityAIBase)new EntityAIMate(this, 1.0D));
/*  32 */     this.tasks.addTask(3, (EntityAIBase)new EntityAITempt((EntityCreature)this, 1.25D, Items.wheat, false));
/*  33 */     this.tasks.addTask(4, (EntityAIBase)new EntityAIFollowParent(this, 1.25D));
/*  34 */     this.tasks.addTask(5, (EntityAIBase)new EntityAIWander((EntityCreature)this, 1.0D));
/*  35 */     this.tasks.addTask(6, (EntityAIBase)new EntityAIWatchClosest((EntityLiving)this, EntityPlayer.class, 6.0F));
/*  36 */     this.tasks.addTask(7, (EntityAIBase)new EntityAILookIdle((EntityLiving)this));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void applyEntityAttributes() {
/*  41 */     super.applyEntityAttributes();
/*  42 */     getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(10.0D);
/*  43 */     getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.20000000298023224D);
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getLivingSound() {
/*  48 */     return "mob.cow.say";
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getHurtSound() {
/*  53 */     return "mob.cow.hurt";
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getDeathSound() {
/*  58 */     return "mob.cow.hurt";
/*     */   }
/*     */ 
/*     */   
/*     */   protected void playStepSound(BlockPos pos, Block blockIn) {
/*  63 */     playSound("mob.cow.step", 0.15F, 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getSoundVolume() {
/*  68 */     return 0.4F;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Item getDropItem() {
/*  73 */     return Items.leather;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void dropFewItems(boolean wasRecentlyHit, int lootingModifier) {
/*  78 */     int i = this.rand.nextInt(3) + this.rand.nextInt(1 + lootingModifier);
/*     */     
/*  80 */     for (int j = 0; j < i; j++)
/*     */     {
/*  82 */       dropItem(Items.leather, 1);
/*     */     }
/*     */     
/*  85 */     i = this.rand.nextInt(3) + 1 + this.rand.nextInt(1 + lootingModifier);
/*     */     
/*  87 */     for (int k = 0; k < i; k++) {
/*     */       
/*  89 */       if (isBurning()) {
/*     */         
/*  91 */         dropItem(Items.cooked_beef, 1);
/*     */       }
/*     */       else {
/*     */         
/*  95 */         dropItem(Items.beef, 1);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean interact(EntityPlayer player) {
/* 102 */     ItemStack itemstack = player.inventory.getCurrentItem();
/*     */     
/* 104 */     if (itemstack != null && itemstack.getItem() == Items.bucket && !player.capabilities.isCreativeMode && !isChild()) {
/*     */       
/* 106 */       if (itemstack.stackSize-- == 1) {
/*     */         
/* 108 */         player.inventory.setInventorySlotContents(player.inventory.currentItem, new ItemStack(Items.milk_bucket));
/*     */       }
/* 110 */       else if (!player.inventory.addItemStackToInventory(new ItemStack(Items.milk_bucket))) {
/*     */         
/* 112 */         player.dropPlayerItemWithRandomChoice(new ItemStack(Items.milk_bucket, 1, 0), false);
/*     */       } 
/*     */       
/* 115 */       return true;
/*     */     } 
/*     */ 
/*     */     
/* 119 */     return super.interact(player);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public EntityCow createChild(EntityAgeable ageable) {
/* 125 */     return new EntityCow(this.worldObj);
/*     */   }
/*     */ 
/*     */   
/*     */   public float getEyeHeight() {
/* 130 */     return this.height;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\passive\EntityCow.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */