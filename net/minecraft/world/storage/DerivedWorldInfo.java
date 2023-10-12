/*     */ package net.minecraft.world.storage;
/*     */ 
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.world.EnumDifficulty;
/*     */ import net.minecraft.world.GameRules;
/*     */ import net.minecraft.world.WorldSettings;
/*     */ import net.minecraft.world.WorldType;
/*     */ 
/*     */ public class DerivedWorldInfo
/*     */   extends WorldInfo
/*     */ {
/*     */   private final WorldInfo theWorldInfo;
/*     */   
/*     */   public DerivedWorldInfo(WorldInfo p_i2145_1_) {
/*  16 */     this.theWorldInfo = p_i2145_1_;
/*     */   }
/*     */ 
/*     */   
/*     */   public NBTTagCompound getNBTTagCompound() {
/*  21 */     return this.theWorldInfo.getNBTTagCompound();
/*     */   }
/*     */ 
/*     */   
/*     */   public NBTTagCompound cloneNBTCompound(NBTTagCompound nbt) {
/*  26 */     return this.theWorldInfo.cloneNBTCompound(nbt);
/*     */   }
/*     */ 
/*     */   
/*     */   public long getSeed() {
/*  31 */     return this.theWorldInfo.getSeed();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSpawnX() {
/*  36 */     return this.theWorldInfo.getSpawnX();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSpawnY() {
/*  41 */     return this.theWorldInfo.getSpawnY();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSpawnZ() {
/*  46 */     return this.theWorldInfo.getSpawnZ();
/*     */   }
/*     */ 
/*     */   
/*     */   public long getWorldTotalTime() {
/*  51 */     return this.theWorldInfo.getWorldTotalTime();
/*     */   }
/*     */ 
/*     */   
/*     */   public long getWorldTime() {
/*  56 */     return this.theWorldInfo.getWorldTime();
/*     */   }
/*     */ 
/*     */   
/*     */   public long getSizeOnDisk() {
/*  61 */     return this.theWorldInfo.getSizeOnDisk();
/*     */   }
/*     */ 
/*     */   
/*     */   public NBTTagCompound getPlayerNBTTagCompound() {
/*  66 */     return this.theWorldInfo.getPlayerNBTTagCompound();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getWorldName() {
/*  71 */     return this.theWorldInfo.getWorldName();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSaveVersion() {
/*  76 */     return this.theWorldInfo.getSaveVersion();
/*     */   }
/*     */ 
/*     */   
/*     */   public long getLastTimePlayed() {
/*  81 */     return this.theWorldInfo.getLastTimePlayed();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isThundering() {
/*  86 */     return this.theWorldInfo.isThundering();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getThunderTime() {
/*  91 */     return this.theWorldInfo.getThunderTime();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isRaining() {
/*  96 */     return this.theWorldInfo.isRaining();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRainTime() {
/* 101 */     return this.theWorldInfo.getRainTime();
/*     */   }
/*     */ 
/*     */   
/*     */   public WorldSettings.GameType getGameType() {
/* 106 */     return this.theWorldInfo.getGameType();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSpawnX(int x) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSpawnY(int y) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSpawnZ(int z) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void setWorldTotalTime(long time) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void setWorldTime(long time) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSpawn(BlockPos spawnPoint) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void setWorldName(String worldName) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSaveVersion(int version) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void setThundering(boolean thunderingIn) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void setThunderTime(int time) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRaining(boolean isRaining) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRainTime(int time) {}
/*     */ 
/*     */   
/*     */   public boolean isMapFeaturesEnabled() {
/* 159 */     return this.theWorldInfo.isMapFeaturesEnabled();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isHardcoreModeEnabled() {
/* 164 */     return this.theWorldInfo.isHardcoreModeEnabled();
/*     */   }
/*     */ 
/*     */   
/*     */   public WorldType getTerrainType() {
/* 169 */     return this.theWorldInfo.getTerrainType();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTerrainType(WorldType type) {}
/*     */ 
/*     */   
/*     */   public boolean areCommandsAllowed() {
/* 178 */     return this.theWorldInfo.areCommandsAllowed();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAllowCommands(boolean allow) {}
/*     */ 
/*     */   
/*     */   public boolean isInitialized() {
/* 187 */     return this.theWorldInfo.isInitialized();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setServerInitialized(boolean initializedIn) {}
/*     */ 
/*     */   
/*     */   public GameRules getGameRulesInstance() {
/* 196 */     return this.theWorldInfo.getGameRulesInstance();
/*     */   }
/*     */ 
/*     */   
/*     */   public EnumDifficulty getDifficulty() {
/* 201 */     return this.theWorldInfo.getDifficulty();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDifficulty(EnumDifficulty newDifficulty) {}
/*     */ 
/*     */   
/*     */   public boolean isDifficultyLocked() {
/* 210 */     return this.theWorldInfo.isDifficultyLocked();
/*     */   }
/*     */   
/*     */   public void setDifficultyLocked(boolean locked) {}
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\world\storage\DerivedWorldInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */