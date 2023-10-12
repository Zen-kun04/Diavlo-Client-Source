/*     */ package net.minecraft.entity.monster;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockSilverfish;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLiving;
/*     */ import net.minecraft.entity.EnumCreatureAttribute;
/*     */ import net.minecraft.entity.SharedMonsterAttributes;
/*     */ import net.minecraft.entity.ai.EntityAIAttackOnCollide;
/*     */ import net.minecraft.entity.ai.EntityAIBase;
/*     */ import net.minecraft.entity.ai.EntityAIHurtByTarget;
/*     */ import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
/*     */ import net.minecraft.entity.ai.EntityAISwimming;
/*     */ import net.minecraft.entity.ai.EntityAIWander;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.DamageSource;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class EntitySilverfish extends EntityMob {
/*     */   private AISummonSilverfish summonSilverfish;
/*     */   
/*     */   public EntitySilverfish(World worldIn) {
/*  30 */     super(worldIn);
/*  31 */     setSize(0.4F, 0.3F);
/*  32 */     this.tasks.addTask(1, (EntityAIBase)new EntityAISwimming((EntityLiving)this));
/*  33 */     this.tasks.addTask(3, this.summonSilverfish = new AISummonSilverfish(this));
/*  34 */     this.tasks.addTask(4, (EntityAIBase)new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.0D, false));
/*  35 */     this.tasks.addTask(5, (EntityAIBase)new AIHideInStone(this));
/*  36 */     this.targetTasks.addTask(1, (EntityAIBase)new EntityAIHurtByTarget(this, true, new Class[0]));
/*  37 */     this.targetTasks.addTask(2, (EntityAIBase)new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
/*     */   }
/*     */ 
/*     */   
/*     */   public double getYOffset() {
/*  42 */     return 0.2D;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getEyeHeight() {
/*  47 */     return 0.1F;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void applyEntityAttributes() {
/*  52 */     super.applyEntityAttributes();
/*  53 */     getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(8.0D);
/*  54 */     getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25D);
/*  55 */     getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(1.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canTriggerWalking() {
/*  60 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getLivingSound() {
/*  65 */     return "mob.silverfish.say";
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getHurtSound() {
/*  70 */     return "mob.silverfish.hit";
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getDeathSound() {
/*  75 */     return "mob.silverfish.kill";
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean attackEntityFrom(DamageSource source, float amount) {
/*  80 */     if (isEntityInvulnerable(source))
/*     */     {
/*  82 */       return false;
/*     */     }
/*     */ 
/*     */     
/*  86 */     if (source instanceof net.minecraft.util.EntityDamageSource || source == DamageSource.magic)
/*     */     {
/*  88 */       this.summonSilverfish.func_179462_f();
/*     */     }
/*     */     
/*  91 */     return super.attackEntityFrom(source, amount);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void playStepSound(BlockPos pos, Block blockIn) {
/*  97 */     playSound("mob.silverfish.step", 0.15F, 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Item getDropItem() {
/* 102 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdate() {
/* 107 */     this.renderYawOffset = this.rotationYaw;
/* 108 */     super.onUpdate();
/*     */   }
/*     */ 
/*     */   
/*     */   public float getBlockPathWeight(BlockPos pos) {
/* 113 */     return (this.worldObj.getBlockState(pos.down()).getBlock() == Blocks.stone) ? 10.0F : super.getBlockPathWeight(pos);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isValidLightLevel() {
/* 118 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getCanSpawnHere() {
/* 123 */     if (super.getCanSpawnHere()) {
/*     */       
/* 125 */       EntityPlayer entityplayer = this.worldObj.getClosestPlayerToEntity((Entity)this, 5.0D);
/* 126 */       return (entityplayer == null);
/*     */     } 
/*     */ 
/*     */     
/* 130 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public EnumCreatureAttribute getCreatureAttribute() {
/* 136 */     return EnumCreatureAttribute.ARTHROPOD;
/*     */   }
/*     */   
/*     */   static class AIHideInStone
/*     */     extends EntityAIWander
/*     */   {
/*     */     private final EntitySilverfish silverfish;
/*     */     private EnumFacing facing;
/*     */     private boolean field_179484_c;
/*     */     
/*     */     public AIHideInStone(EntitySilverfish silverfishIn) {
/* 147 */       super(silverfishIn, 1.0D, 10);
/* 148 */       this.silverfish = silverfishIn;
/* 149 */       setMutexBits(1);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean shouldExecute() {
/* 154 */       if (this.silverfish.getAttackTarget() != null)
/*     */       {
/* 156 */         return false;
/*     */       }
/* 158 */       if (!this.silverfish.getNavigator().noPath())
/*     */       {
/* 160 */         return false;
/*     */       }
/*     */ 
/*     */       
/* 164 */       Random random = this.silverfish.getRNG();
/*     */       
/* 166 */       if (random.nextInt(10) == 0) {
/*     */         
/* 168 */         this.facing = EnumFacing.random(random);
/* 169 */         BlockPos blockpos = (new BlockPos(this.silverfish.posX, this.silverfish.posY + 0.5D, this.silverfish.posZ)).offset(this.facing);
/* 170 */         IBlockState iblockstate = this.silverfish.worldObj.getBlockState(blockpos);
/*     */         
/* 172 */         if (BlockSilverfish.canContainSilverfish(iblockstate)) {
/*     */           
/* 174 */           this.field_179484_c = true;
/* 175 */           return true;
/*     */         } 
/*     */       } 
/*     */       
/* 179 */       this.field_179484_c = false;
/* 180 */       return super.shouldExecute();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean continueExecuting() {
/* 186 */       return this.field_179484_c ? false : super.continueExecuting();
/*     */     }
/*     */ 
/*     */     
/*     */     public void startExecuting() {
/* 191 */       if (!this.field_179484_c) {
/*     */         
/* 193 */         super.startExecuting();
/*     */       }
/*     */       else {
/*     */         
/* 197 */         World world = this.silverfish.worldObj;
/* 198 */         BlockPos blockpos = (new BlockPos(this.silverfish.posX, this.silverfish.posY + 0.5D, this.silverfish.posZ)).offset(this.facing);
/* 199 */         IBlockState iblockstate = world.getBlockState(blockpos);
/*     */         
/* 201 */         if (BlockSilverfish.canContainSilverfish(iblockstate)) {
/*     */           
/* 203 */           world.setBlockState(blockpos, Blocks.monster_egg.getDefaultState().withProperty((IProperty)BlockSilverfish.VARIANT, (Comparable)BlockSilverfish.EnumType.forModelBlock(iblockstate)), 3);
/* 204 */           this.silverfish.spawnExplosionParticle();
/* 205 */           this.silverfish.setDead();
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   static class AISummonSilverfish
/*     */     extends EntityAIBase
/*     */   {
/*     */     private EntitySilverfish silverfish;
/*     */     private int field_179463_b;
/*     */     
/*     */     public AISummonSilverfish(EntitySilverfish silverfishIn) {
/* 218 */       this.silverfish = silverfishIn;
/*     */     }
/*     */ 
/*     */     
/*     */     public void func_179462_f() {
/* 223 */       if (this.field_179463_b == 0)
/*     */       {
/* 225 */         this.field_179463_b = 20;
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean shouldExecute() {
/* 231 */       return (this.field_179463_b > 0);
/*     */     }
/*     */ 
/*     */     
/*     */     public void updateTask() {
/* 236 */       this.field_179463_b--;
/*     */       
/* 238 */       if (this.field_179463_b <= 0) {
/*     */         
/* 240 */         World world = this.silverfish.worldObj;
/* 241 */         Random random = this.silverfish.getRNG();
/* 242 */         BlockPos blockpos = new BlockPos((Entity)this.silverfish);
/*     */         int i;
/* 244 */         for (i = 0; i <= 5 && i >= -5; i = (i <= 0) ? (1 - i) : (0 - i)) {
/*     */           int j;
/* 246 */           for (j = 0; j <= 10 && j >= -10; j = (j <= 0) ? (1 - j) : (0 - j)) {
/*     */             int k;
/* 248 */             for (k = 0; k <= 10 && k >= -10; k = (k <= 0) ? (1 - k) : (0 - k)) {
/*     */               
/* 250 */               BlockPos blockpos1 = blockpos.add(j, i, k);
/* 251 */               IBlockState iblockstate = world.getBlockState(blockpos1);
/*     */               
/* 253 */               if (iblockstate.getBlock() == Blocks.monster_egg) {
/*     */                 
/* 255 */                 if (world.getGameRules().getBoolean("mobGriefing")) {
/*     */                   
/* 257 */                   world.destroyBlock(blockpos1, true);
/*     */                 }
/*     */                 else {
/*     */                   
/* 261 */                   world.setBlockState(blockpos1, ((BlockSilverfish.EnumType)iblockstate.getValue((IProperty)BlockSilverfish.VARIANT)).getModelBlock(), 3);
/*     */                 } 
/*     */                 
/* 264 */                 if (random.nextBoolean())
/*     */                   return; 
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\monster\EntitySilverfish.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */