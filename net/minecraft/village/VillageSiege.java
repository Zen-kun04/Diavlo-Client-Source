/*     */ package net.minecraft.village;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLiving;
/*     */ import net.minecraft.entity.IEntityLivingData;
/*     */ import net.minecraft.entity.monster.EntityZombie;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.Vec3;
/*     */ import net.minecraft.world.SpawnerAnimals;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class VillageSiege {
/*     */   private World worldObj;
/*     */   private boolean field_75535_b;
/*  19 */   private int field_75536_c = -1;
/*     */   
/*     */   private int field_75533_d;
/*     */   private int field_75534_e;
/*     */   private Village theVillage;
/*     */   private int field_75532_g;
/*     */   private int field_75538_h;
/*     */   private int field_75539_i;
/*     */   
/*     */   public VillageSiege(World worldIn) {
/*  29 */     this.worldObj = worldIn;
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/*  34 */     if (this.worldObj.isDaytime()) {
/*     */       
/*  36 */       this.field_75536_c = 0;
/*     */     }
/*  38 */     else if (this.field_75536_c != 2) {
/*     */       
/*  40 */       if (this.field_75536_c == 0) {
/*     */         
/*  42 */         float f = this.worldObj.getCelestialAngle(0.0F);
/*     */         
/*  44 */         if (f < 0.5D || f > 0.501D) {
/*     */           return;
/*     */         }
/*     */ 
/*     */         
/*  49 */         this.field_75536_c = (this.worldObj.rand.nextInt(10) == 0) ? 1 : 2;
/*  50 */         this.field_75535_b = false;
/*     */         
/*  52 */         if (this.field_75536_c == 2) {
/*     */           return;
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/*  58 */       if (this.field_75536_c != -1) {
/*     */         
/*  60 */         if (!this.field_75535_b) {
/*     */           
/*  62 */           if (!func_75529_b()) {
/*     */             return;
/*     */           }
/*     */ 
/*     */           
/*  67 */           this.field_75535_b = true;
/*     */         } 
/*     */         
/*  70 */         if (this.field_75534_e > 0) {
/*     */           
/*  72 */           this.field_75534_e--;
/*     */         }
/*     */         else {
/*     */           
/*  76 */           this.field_75534_e = 2;
/*     */           
/*  78 */           if (this.field_75533_d > 0) {
/*     */             
/*  80 */             spawnZombie();
/*  81 */             this.field_75533_d--;
/*     */           }
/*     */           else {
/*     */             
/*  85 */             this.field_75536_c = 2;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean func_75529_b() {
/*  94 */     List<EntityPlayer> list = this.worldObj.playerEntities;
/*  95 */     Iterator<EntityPlayer> iterator = list.iterator();
/*     */ 
/*     */     
/*     */     while (true) {
/*  99 */       if (!iterator.hasNext())
/*     */       {
/* 101 */         return false;
/*     */       }
/*     */       
/* 104 */       EntityPlayer entityplayer = iterator.next();
/*     */       
/* 106 */       if (!entityplayer.isSpectator()) {
/*     */         
/* 108 */         this.theVillage = this.worldObj.getVillageCollection().getNearestVillage(new BlockPos((Entity)entityplayer), 1);
/*     */         
/* 110 */         if (this.theVillage != null && this.theVillage.getNumVillageDoors() >= 10 && this.theVillage.getTicksSinceLastDoorAdding() >= 20 && this.theVillage.getNumVillagers() >= 20) {
/*     */           
/* 112 */           BlockPos blockpos = this.theVillage.getCenter();
/* 113 */           float f = this.theVillage.getVillageRadius();
/* 114 */           boolean flag = false;
/*     */           
/* 116 */           for (int i = 0; i < 10; i++) {
/*     */             
/* 118 */             float f1 = this.worldObj.rand.nextFloat() * 3.1415927F * 2.0F;
/* 119 */             this.field_75532_g = blockpos.getX() + (int)((MathHelper.cos(f1) * f) * 0.9D);
/* 120 */             this.field_75538_h = blockpos.getY();
/* 121 */             this.field_75539_i = blockpos.getZ() + (int)((MathHelper.sin(f1) * f) * 0.9D);
/* 122 */             flag = false;
/*     */             
/* 124 */             for (Village village : this.worldObj.getVillageCollection().getVillageList()) {
/*     */               
/* 126 */               if (village != this.theVillage && village.func_179866_a(new BlockPos(this.field_75532_g, this.field_75538_h, this.field_75539_i))) {
/*     */                 
/* 128 */                 flag = true;
/*     */                 
/*     */                 break;
/*     */               } 
/*     */             } 
/* 133 */             if (!flag) {
/*     */               break;
/*     */             }
/*     */           } 
/*     */ 
/*     */           
/* 139 */           if (flag)
/*     */           {
/* 141 */             return false;
/*     */           }
/*     */           
/* 144 */           Vec3 vec3 = func_179867_a(new BlockPos(this.field_75532_g, this.field_75538_h, this.field_75539_i));
/*     */           
/* 146 */           if (vec3 != null) {
/*     */             break;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 154 */     this.field_75534_e = 0;
/* 155 */     this.field_75533_d = 20;
/* 156 */     return true;
/*     */   }
/*     */   
/*     */   private boolean spawnZombie() {
/*     */     EntityZombie entityzombie;
/* 161 */     Vec3 vec3 = func_179867_a(new BlockPos(this.field_75532_g, this.field_75538_h, this.field_75539_i));
/*     */     
/* 163 */     if (vec3 == null)
/*     */     {
/* 165 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 173 */       entityzombie = new EntityZombie(this.worldObj);
/* 174 */       entityzombie.onInitialSpawn(this.worldObj.getDifficultyForLocation(new BlockPos((Entity)entityzombie)), (IEntityLivingData)null);
/* 175 */       entityzombie.setVillager(false);
/*     */     }
/* 177 */     catch (Exception exception) {
/*     */       
/* 179 */       exception.printStackTrace();
/* 180 */       return false;
/*     */     } 
/*     */     
/* 183 */     entityzombie.setLocationAndAngles(vec3.xCoord, vec3.yCoord, vec3.zCoord, this.worldObj.rand.nextFloat() * 360.0F, 0.0F);
/* 184 */     this.worldObj.spawnEntityInWorld((Entity)entityzombie);
/* 185 */     BlockPos blockpos = this.theVillage.getCenter();
/* 186 */     entityzombie.setHomePosAndDistance(blockpos, this.theVillage.getVillageRadius());
/* 187 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Vec3 func_179867_a(BlockPos p_179867_1_) {
/* 193 */     for (int i = 0; i < 10; i++) {
/*     */       
/* 195 */       BlockPos blockpos = p_179867_1_.add(this.worldObj.rand.nextInt(16) - 8, this.worldObj.rand.nextInt(6) - 3, this.worldObj.rand.nextInt(16) - 8);
/*     */       
/* 197 */       if (this.theVillage.func_179866_a(blockpos) && SpawnerAnimals.canCreatureTypeSpawnAtLocation(EntityLiving.SpawnPlacementType.ON_GROUND, this.worldObj, blockpos))
/*     */       {
/* 199 */         return new Vec3(blockpos.getX(), blockpos.getY(), blockpos.getZ());
/*     */       }
/*     */     } 
/*     */     
/* 203 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\village\VillageSiege.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */