/*     */ package net.minecraft.entity.ai;
/*     */ 
/*     */ import com.google.common.base.Predicate;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLiving;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.SharedMonsterAttributes;
/*     */ import net.minecraft.entity.ai.attributes.IAttributeInstance;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.EntityPlayerMP;
/*     */ import net.minecraft.scoreboard.Team;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class EntityAIFindEntityNearestPlayer
/*     */   extends EntityAIBase
/*     */ {
/*  20 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */   
/*     */   private EntityLiving entityLiving;
/*     */   private final Predicate<Entity> predicate;
/*     */   private final EntityAINearestAttackableTarget.Sorter sorter;
/*     */   private EntityLivingBase entityTarget;
/*     */   
/*     */   public EntityAIFindEntityNearestPlayer(EntityLiving entityLivingIn) {
/*  28 */     this.entityLiving = entityLivingIn;
/*     */     
/*  30 */     if (entityLivingIn instanceof net.minecraft.entity.EntityCreature)
/*     */     {
/*  32 */       LOGGER.warn("Use NearestAttackableTargetGoal.class for PathfinerMob mobs!");
/*     */     }
/*     */     
/*  35 */     this.predicate = new Predicate<Entity>()
/*     */       {
/*     */         public boolean apply(Entity p_apply_1_)
/*     */         {
/*  39 */           if (!(p_apply_1_ instanceof EntityPlayer))
/*     */           {
/*  41 */             return false;
/*     */           }
/*  43 */           if (((EntityPlayer)p_apply_1_).capabilities.disableDamage)
/*     */           {
/*  45 */             return false;
/*     */           }
/*     */ 
/*     */           
/*  49 */           double d0 = EntityAIFindEntityNearestPlayer.this.maxTargetRange();
/*     */           
/*  51 */           if (p_apply_1_.isSneaking())
/*     */           {
/*  53 */             d0 *= 0.800000011920929D;
/*     */           }
/*     */           
/*  56 */           if (p_apply_1_.isInvisible()) {
/*     */             
/*  58 */             float f = ((EntityPlayer)p_apply_1_).getArmorVisibility();
/*     */             
/*  60 */             if (f < 0.1F)
/*     */             {
/*  62 */               f = 0.1F;
/*     */             }
/*     */             
/*  65 */             d0 *= (0.7F * f);
/*     */           } 
/*     */           
/*  68 */           return (p_apply_1_.getDistanceToEntity((Entity)EntityAIFindEntityNearestPlayer.this.entityLiving) > d0) ? false : EntityAITarget.isSuitableTarget(EntityAIFindEntityNearestPlayer.this.entityLiving, (EntityLivingBase)p_apply_1_, false, true);
/*     */         }
/*     */       };
/*     */     
/*  72 */     this.sorter = new EntityAINearestAttackableTarget.Sorter((Entity)entityLivingIn);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldExecute() {
/*  77 */     double d0 = maxTargetRange();
/*  78 */     List<EntityPlayer> list = this.entityLiving.worldObj.getEntitiesWithinAABB(EntityPlayer.class, this.entityLiving.getEntityBoundingBox().expand(d0, 4.0D, d0), this.predicate);
/*  79 */     Collections.sort(list, this.sorter);
/*     */     
/*  81 */     if (list.isEmpty())
/*     */     {
/*  83 */       return false;
/*     */     }
/*     */ 
/*     */     
/*  87 */     this.entityTarget = (EntityLivingBase)list.get(0);
/*  88 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean continueExecuting() {
/*  94 */     EntityLivingBase entitylivingbase = this.entityLiving.getAttackTarget();
/*     */     
/*  96 */     if (entitylivingbase == null)
/*     */     {
/*  98 */       return false;
/*     */     }
/* 100 */     if (!entitylivingbase.isEntityAlive())
/*     */     {
/* 102 */       return false;
/*     */     }
/* 104 */     if (entitylivingbase instanceof EntityPlayer && ((EntityPlayer)entitylivingbase).capabilities.disableDamage)
/*     */     {
/* 106 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 110 */     Team team = this.entityLiving.getTeam();
/* 111 */     Team team1 = entitylivingbase.getTeam();
/*     */     
/* 113 */     if (team != null && team1 == team)
/*     */     {
/* 115 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 119 */     double d0 = maxTargetRange();
/* 120 */     return (this.entityLiving.getDistanceSqToEntity((Entity)entitylivingbase) > d0 * d0) ? false : ((!(entitylivingbase instanceof EntityPlayerMP) || !((EntityPlayerMP)entitylivingbase).theItemInWorldManager.isCreative()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void startExecuting() {
/* 127 */     this.entityLiving.setAttackTarget(this.entityTarget);
/* 128 */     super.startExecuting();
/*     */   }
/*     */ 
/*     */   
/*     */   public void resetTask() {
/* 133 */     this.entityLiving.setAttackTarget((EntityLivingBase)null);
/* 134 */     super.startExecuting();
/*     */   }
/*     */ 
/*     */   
/*     */   protected double maxTargetRange() {
/* 139 */     IAttributeInstance iattributeinstance = this.entityLiving.getEntityAttribute(SharedMonsterAttributes.followRange);
/* 140 */     return (iattributeinstance == null) ? 16.0D : iattributeinstance.getAttributeValue();
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\ai\EntityAIFindEntityNearestPlayer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */