/*     */ package net.minecraft.entity;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Sets;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.Callable;
/*     */ import net.minecraft.crash.CrashReport;
/*     */ import net.minecraft.crash.CrashReportCategory;
/*     */ import net.minecraft.entity.player.EntityPlayerMP;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.util.IntHashMap;
/*     */ import net.minecraft.util.ReportedException;
/*     */ import net.minecraft.world.WorldServer;
/*     */ import net.minecraft.world.chunk.Chunk;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EntityTracker
/*     */ {
/*  45 */   private static final Logger logger = LogManager.getLogger();
/*     */   private final WorldServer theWorld;
/*  47 */   private Set<EntityTrackerEntry> trackedEntities = Sets.newHashSet();
/*  48 */   private IntHashMap<EntityTrackerEntry> trackedEntityHashTable = new IntHashMap();
/*     */   
/*     */   private int maxTrackingDistanceThreshold;
/*     */   
/*     */   public EntityTracker(WorldServer theWorldIn) {
/*  53 */     this.theWorld = theWorldIn;
/*  54 */     this.maxTrackingDistanceThreshold = theWorldIn.getMinecraftServer().getConfigurationManager().getEntityViewDistance();
/*     */   }
/*     */ 
/*     */   
/*     */   public void trackEntity(Entity entityIn) {
/*  59 */     if (entityIn instanceof EntityPlayerMP) {
/*     */       
/*  61 */       trackEntity(entityIn, 512, 2);
/*  62 */       EntityPlayerMP entityplayermp = (EntityPlayerMP)entityIn;
/*     */       
/*  64 */       for (EntityTrackerEntry entitytrackerentry : this.trackedEntities)
/*     */       {
/*  66 */         if (entitytrackerentry.trackedEntity != entityplayermp)
/*     */         {
/*  68 */           entitytrackerentry.updatePlayerEntity(entityplayermp);
/*     */         }
/*     */       }
/*     */     
/*  72 */     } else if (entityIn instanceof net.minecraft.entity.projectile.EntityFishHook) {
/*     */       
/*  74 */       addEntityToTracker(entityIn, 64, 5, true);
/*     */     }
/*  76 */     else if (entityIn instanceof net.minecraft.entity.projectile.EntityArrow) {
/*     */       
/*  78 */       addEntityToTracker(entityIn, 64, 20, false);
/*     */     }
/*  80 */     else if (entityIn instanceof net.minecraft.entity.projectile.EntitySmallFireball) {
/*     */       
/*  82 */       addEntityToTracker(entityIn, 64, 10, false);
/*     */     }
/*  84 */     else if (entityIn instanceof net.minecraft.entity.projectile.EntityFireball) {
/*     */       
/*  86 */       addEntityToTracker(entityIn, 64, 10, false);
/*     */     }
/*  88 */     else if (entityIn instanceof net.minecraft.entity.projectile.EntitySnowball) {
/*     */       
/*  90 */       addEntityToTracker(entityIn, 64, 10, true);
/*     */     }
/*  92 */     else if (entityIn instanceof net.minecraft.entity.item.EntityEnderPearl) {
/*     */       
/*  94 */       addEntityToTracker(entityIn, 64, 10, true);
/*     */     }
/*  96 */     else if (entityIn instanceof net.minecraft.entity.item.EntityEnderEye) {
/*     */       
/*  98 */       addEntityToTracker(entityIn, 64, 4, true);
/*     */     }
/* 100 */     else if (entityIn instanceof net.minecraft.entity.projectile.EntityEgg) {
/*     */       
/* 102 */       addEntityToTracker(entityIn, 64, 10, true);
/*     */     }
/* 104 */     else if (entityIn instanceof net.minecraft.entity.projectile.EntityPotion) {
/*     */       
/* 106 */       addEntityToTracker(entityIn, 64, 10, true);
/*     */     }
/* 108 */     else if (entityIn instanceof net.minecraft.entity.item.EntityExpBottle) {
/*     */       
/* 110 */       addEntityToTracker(entityIn, 64, 10, true);
/*     */     }
/* 112 */     else if (entityIn instanceof net.minecraft.entity.item.EntityFireworkRocket) {
/*     */       
/* 114 */       addEntityToTracker(entityIn, 64, 10, true);
/*     */     }
/* 116 */     else if (entityIn instanceof net.minecraft.entity.item.EntityItem) {
/*     */       
/* 118 */       addEntityToTracker(entityIn, 64, 20, true);
/*     */     }
/* 120 */     else if (entityIn instanceof net.minecraft.entity.item.EntityMinecart) {
/*     */       
/* 122 */       addEntityToTracker(entityIn, 80, 3, true);
/*     */     }
/* 124 */     else if (entityIn instanceof net.minecraft.entity.item.EntityBoat) {
/*     */       
/* 126 */       addEntityToTracker(entityIn, 80, 3, true);
/*     */     }
/* 128 */     else if (entityIn instanceof net.minecraft.entity.passive.EntitySquid) {
/*     */       
/* 130 */       addEntityToTracker(entityIn, 64, 3, true);
/*     */     }
/* 132 */     else if (entityIn instanceof net.minecraft.entity.boss.EntityWither) {
/*     */       
/* 134 */       addEntityToTracker(entityIn, 80, 3, false);
/*     */     }
/* 136 */     else if (entityIn instanceof net.minecraft.entity.passive.EntityBat) {
/*     */       
/* 138 */       addEntityToTracker(entityIn, 80, 3, false);
/*     */     }
/* 140 */     else if (entityIn instanceof net.minecraft.entity.boss.EntityDragon) {
/*     */       
/* 142 */       addEntityToTracker(entityIn, 160, 3, true);
/*     */     }
/* 144 */     else if (entityIn instanceof net.minecraft.entity.passive.IAnimals) {
/*     */       
/* 146 */       addEntityToTracker(entityIn, 80, 3, true);
/*     */     }
/* 148 */     else if (entityIn instanceof net.minecraft.entity.item.EntityTNTPrimed) {
/*     */       
/* 150 */       addEntityToTracker(entityIn, 160, 10, true);
/*     */     }
/* 152 */     else if (entityIn instanceof net.minecraft.entity.item.EntityFallingBlock) {
/*     */       
/* 154 */       addEntityToTracker(entityIn, 160, 20, true);
/*     */     }
/* 156 */     else if (entityIn instanceof EntityHanging) {
/*     */       
/* 158 */       addEntityToTracker(entityIn, 160, 2147483647, false);
/*     */     }
/* 160 */     else if (entityIn instanceof net.minecraft.entity.item.EntityArmorStand) {
/*     */       
/* 162 */       addEntityToTracker(entityIn, 160, 3, true);
/*     */     }
/* 164 */     else if (entityIn instanceof net.minecraft.entity.item.EntityXPOrb) {
/*     */       
/* 166 */       addEntityToTracker(entityIn, 160, 20, true);
/*     */     }
/* 168 */     else if (entityIn instanceof net.minecraft.entity.item.EntityEnderCrystal) {
/*     */       
/* 170 */       addEntityToTracker(entityIn, 256, 2147483647, false);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void trackEntity(Entity entityIn, int trackingRange, int updateFrequency) {
/* 176 */     addEntityToTracker(entityIn, trackingRange, updateFrequency, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addEntityToTracker(Entity entityIn, int trackingRange, final int updateFrequency, boolean sendVelocityUpdates) {
/* 181 */     if (trackingRange > this.maxTrackingDistanceThreshold)
/*     */     {
/* 183 */       trackingRange = this.maxTrackingDistanceThreshold;
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/* 188 */       if (this.trackedEntityHashTable.containsItem(entityIn.getEntityId()))
/*     */       {
/* 190 */         throw new IllegalStateException("Entity is already tracked!");
/*     */       }
/*     */       
/* 193 */       EntityTrackerEntry entitytrackerentry = new EntityTrackerEntry(entityIn, trackingRange, updateFrequency, sendVelocityUpdates);
/* 194 */       this.trackedEntities.add(entitytrackerentry);
/* 195 */       this.trackedEntityHashTable.addKey(entityIn.getEntityId(), entitytrackerentry);
/* 196 */       entitytrackerentry.updatePlayerEntities(this.theWorld.playerEntities);
/*     */     }
/* 198 */     catch (Throwable throwable) {
/*     */       
/* 200 */       CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Adding entity to track");
/* 201 */       CrashReportCategory crashreportcategory = crashreport.makeCategory("Entity To Track");
/* 202 */       crashreportcategory.addCrashSection("Tracking range", trackingRange + " blocks");
/* 203 */       crashreportcategory.addCrashSectionCallable("Update interval", new Callable<String>()
/*     */           {
/*     */             public String call() throws Exception
/*     */             {
/* 207 */               String s = "Once per " + updateFrequency + " ticks";
/*     */               
/* 209 */               if (updateFrequency == Integer.MAX_VALUE)
/*     */               {
/* 211 */                 s = "Maximum (" + s + ")";
/*     */               }
/*     */               
/* 214 */               return s;
/*     */             }
/*     */           });
/* 217 */       entityIn.addEntityCrashInfo(crashreportcategory);
/* 218 */       CrashReportCategory crashreportcategory1 = crashreport.makeCategory("Entity That Is Already Tracked");
/* 219 */       ((EntityTrackerEntry)this.trackedEntityHashTable.lookup(entityIn.getEntityId())).trackedEntity.addEntityCrashInfo(crashreportcategory1);
/*     */ 
/*     */       
/*     */       try {
/* 223 */         throw new ReportedException(crashreport);
/*     */       }
/* 225 */       catch (ReportedException reportedexception) {
/*     */         
/* 227 */         logger.error("\"Silently\" catching entity tracking error.", (Throwable)reportedexception);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void untrackEntity(Entity entityIn) {
/* 234 */     if (entityIn instanceof EntityPlayerMP) {
/*     */       
/* 236 */       EntityPlayerMP entityplayermp = (EntityPlayerMP)entityIn;
/*     */       
/* 238 */       for (EntityTrackerEntry entitytrackerentry : this.trackedEntities)
/*     */       {
/* 240 */         entitytrackerentry.removeFromTrackedPlayers(entityplayermp);
/*     */       }
/*     */     } 
/*     */     
/* 244 */     EntityTrackerEntry entitytrackerentry1 = (EntityTrackerEntry)this.trackedEntityHashTable.removeObject(entityIn.getEntityId());
/*     */     
/* 246 */     if (entitytrackerentry1 != null) {
/*     */       
/* 248 */       this.trackedEntities.remove(entitytrackerentry1);
/* 249 */       entitytrackerentry1.sendDestroyEntityPacketToTrackedPlayers();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateTrackedEntities() {
/* 255 */     List<EntityPlayerMP> list = Lists.newArrayList();
/*     */     
/* 257 */     for (EntityTrackerEntry entitytrackerentry : this.trackedEntities) {
/*     */       
/* 259 */       entitytrackerentry.updatePlayerList(this.theWorld.playerEntities);
/*     */       
/* 261 */       if (entitytrackerentry.playerEntitiesUpdated && entitytrackerentry.trackedEntity instanceof EntityPlayerMP)
/*     */       {
/* 263 */         list.add((EntityPlayerMP)entitytrackerentry.trackedEntity);
/*     */       }
/*     */     } 
/*     */     
/* 267 */     for (int i = 0; i < list.size(); i++) {
/*     */       
/* 269 */       EntityPlayerMP entityplayermp = list.get(i);
/*     */       
/* 271 */       for (EntityTrackerEntry entitytrackerentry1 : this.trackedEntities) {
/*     */         
/* 273 */         if (entitytrackerentry1.trackedEntity != entityplayermp)
/*     */         {
/* 275 */           entitytrackerentry1.updatePlayerEntity(entityplayermp);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void func_180245_a(EntityPlayerMP p_180245_1_) {
/* 283 */     for (EntityTrackerEntry entitytrackerentry : this.trackedEntities) {
/*     */       
/* 285 */       if (entitytrackerentry.trackedEntity == p_180245_1_) {
/*     */         
/* 287 */         entitytrackerentry.updatePlayerEntities(this.theWorld.playerEntities);
/*     */         
/*     */         continue;
/*     */       } 
/* 291 */       entitytrackerentry.updatePlayerEntity(p_180245_1_);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendToAllTrackingEntity(Entity entityIn, Packet p_151247_2_) {
/* 298 */     EntityTrackerEntry entitytrackerentry = (EntityTrackerEntry)this.trackedEntityHashTable.lookup(entityIn.getEntityId());
/*     */     
/* 300 */     if (entitytrackerentry != null)
/*     */     {
/* 302 */       entitytrackerentry.sendPacketToTrackedPlayers(p_151247_2_);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void func_151248_b(Entity entityIn, Packet p_151248_2_) {
/* 308 */     EntityTrackerEntry entitytrackerentry = (EntityTrackerEntry)this.trackedEntityHashTable.lookup(entityIn.getEntityId());
/*     */     
/* 310 */     if (entitytrackerentry != null)
/*     */     {
/* 312 */       entitytrackerentry.func_151261_b(p_151248_2_);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void removePlayerFromTrackers(EntityPlayerMP p_72787_1_) {
/* 318 */     for (EntityTrackerEntry entitytrackerentry : this.trackedEntities)
/*     */     {
/* 320 */       entitytrackerentry.removeTrackedPlayerSymmetric(p_72787_1_);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void func_85172_a(EntityPlayerMP p_85172_1_, Chunk p_85172_2_) {
/* 326 */     for (EntityTrackerEntry entitytrackerentry : this.trackedEntities) {
/*     */       
/* 328 */       if (entitytrackerentry.trackedEntity != p_85172_1_ && entitytrackerentry.trackedEntity.chunkCoordX == p_85172_2_.xPosition && entitytrackerentry.trackedEntity.chunkCoordZ == p_85172_2_.zPosition)
/*     */       {
/* 330 */         entitytrackerentry.updatePlayerEntity(p_85172_1_);
/*     */       }
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\EntityTracker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */