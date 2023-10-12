/*     */ package net.minecraft.entity.ai;
/*     */ 
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityCreature;
/*     */ import net.minecraft.entity.EntityLiving;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.IEntityOwnable;
/*     */ import net.minecraft.entity.SharedMonsterAttributes;
/*     */ import net.minecraft.entity.ai.attributes.IAttributeInstance;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.pathfinding.PathEntity;
/*     */ import net.minecraft.pathfinding.PathPoint;
/*     */ import net.minecraft.scoreboard.Team;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ 
/*     */ public abstract class EntityAITarget
/*     */   extends EntityAIBase {
/*     */   protected final EntityCreature taskOwner;
/*     */   protected boolean shouldCheckSight;
/*     */   private boolean nearbyOnly;
/*     */   private int targetSearchStatus;
/*     */   private int targetSearchDelay;
/*     */   private int targetUnseenTicks;
/*     */   
/*     */   public EntityAITarget(EntityCreature creature, boolean checkSight) {
/*  28 */     this(creature, checkSight, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityAITarget(EntityCreature creature, boolean checkSight, boolean onlyNearby) {
/*  33 */     this.taskOwner = creature;
/*  34 */     this.shouldCheckSight = checkSight;
/*  35 */     this.nearbyOnly = onlyNearby;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean continueExecuting() {
/*  40 */     EntityLivingBase entitylivingbase = this.taskOwner.getAttackTarget();
/*     */     
/*  42 */     if (entitylivingbase == null)
/*     */     {
/*  44 */       return false;
/*     */     }
/*  46 */     if (!entitylivingbase.isEntityAlive())
/*     */     {
/*  48 */       return false;
/*     */     }
/*     */ 
/*     */     
/*  52 */     Team team = this.taskOwner.getTeam();
/*  53 */     Team team1 = entitylivingbase.getTeam();
/*     */     
/*  55 */     if (team != null && team1 == team)
/*     */     {
/*  57 */       return false;
/*     */     }
/*     */ 
/*     */     
/*  61 */     double d0 = getTargetDistance();
/*     */     
/*  63 */     if (this.taskOwner.getDistanceSqToEntity((Entity)entitylivingbase) > d0 * d0)
/*     */     {
/*  65 */       return false;
/*     */     }
/*     */ 
/*     */     
/*  69 */     if (this.shouldCheckSight)
/*     */     {
/*  71 */       if (this.taskOwner.getEntitySenses().canSee((Entity)entitylivingbase)) {
/*     */         
/*  73 */         this.targetUnseenTicks = 0;
/*     */       }
/*  75 */       else if (++this.targetUnseenTicks > 60) {
/*     */         
/*  77 */         return false;
/*     */       } 
/*     */     }
/*     */     
/*  81 */     return (!(entitylivingbase instanceof EntityPlayer) || !((EntityPlayer)entitylivingbase).capabilities.disableDamage);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected double getTargetDistance() {
/*  89 */     IAttributeInstance iattributeinstance = this.taskOwner.getEntityAttribute(SharedMonsterAttributes.followRange);
/*  90 */     return (iattributeinstance == null) ? 16.0D : iattributeinstance.getAttributeValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public void startExecuting() {
/*  95 */     this.targetSearchStatus = 0;
/*  96 */     this.targetSearchDelay = 0;
/*  97 */     this.targetUnseenTicks = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public void resetTask() {
/* 102 */     this.taskOwner.setAttackTarget((EntityLivingBase)null);
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isSuitableTarget(EntityLiving attacker, EntityLivingBase target, boolean includeInvincibles, boolean checkSight) {
/* 107 */     if (target == null)
/*     */     {
/* 109 */       return false;
/*     */     }
/* 111 */     if (target == attacker)
/*     */     {
/* 113 */       return false;
/*     */     }
/* 115 */     if (!target.isEntityAlive())
/*     */     {
/* 117 */       return false;
/*     */     }
/* 119 */     if (!attacker.canAttackClass(target.getClass()))
/*     */     {
/* 121 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 125 */     Team team = attacker.getTeam();
/* 126 */     Team team1 = target.getTeam();
/*     */     
/* 128 */     if (team != null && team1 == team)
/*     */     {
/* 130 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 134 */     if (attacker instanceof IEntityOwnable && StringUtils.isNotEmpty(((IEntityOwnable)attacker).getOwnerId())) {
/*     */       
/* 136 */       if (target instanceof IEntityOwnable && ((IEntityOwnable)attacker).getOwnerId().equals(((IEntityOwnable)target).getOwnerId()))
/*     */       {
/* 138 */         return false;
/*     */       }
/*     */       
/* 141 */       if (target == ((IEntityOwnable)attacker).getOwner())
/*     */       {
/* 143 */         return false;
/*     */       }
/*     */     }
/* 146 */     else if (target instanceof EntityPlayer && !includeInvincibles && ((EntityPlayer)target).capabilities.disableDamage) {
/*     */       
/* 148 */       return false;
/*     */     } 
/*     */     
/* 151 */     return (!checkSight || attacker.getEntitySenses().canSee((Entity)target));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isSuitableTarget(EntityLivingBase target, boolean includeInvincibles) {
/* 158 */     if (!isSuitableTarget((EntityLiving)this.taskOwner, target, includeInvincibles, this.shouldCheckSight))
/*     */     {
/* 160 */       return false;
/*     */     }
/* 162 */     if (!this.taskOwner.isWithinHomeDistanceFromPosition(new BlockPos((Entity)target)))
/*     */     {
/* 164 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 168 */     if (this.nearbyOnly) {
/*     */       
/* 170 */       if (--this.targetSearchDelay <= 0)
/*     */       {
/* 172 */         this.targetSearchStatus = 0;
/*     */       }
/*     */       
/* 175 */       if (this.targetSearchStatus == 0)
/*     */       {
/* 177 */         this.targetSearchStatus = canEasilyReach(target) ? 1 : 2;
/*     */       }
/*     */       
/* 180 */       if (this.targetSearchStatus == 2)
/*     */       {
/* 182 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 186 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean canEasilyReach(EntityLivingBase target) {
/* 192 */     this.targetSearchDelay = 10 + this.taskOwner.getRNG().nextInt(5);
/* 193 */     PathEntity pathentity = this.taskOwner.getNavigator().getPathToEntityLiving((Entity)target);
/*     */     
/* 195 */     if (pathentity == null)
/*     */     {
/* 197 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 201 */     PathPoint pathpoint = pathentity.getFinalPathPoint();
/*     */     
/* 203 */     if (pathpoint == null)
/*     */     {
/* 205 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 209 */     int i = pathpoint.xCoord - MathHelper.floor_double(target.posX);
/* 210 */     int j = pathpoint.zCoord - MathHelper.floor_double(target.posZ);
/* 211 */     return ((i * i + j * j) <= 2.25D);
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\ai\EntityAITarget.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */