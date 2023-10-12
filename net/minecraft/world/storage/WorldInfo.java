/*     */ package net.minecraft.world.storage;
/*     */ 
/*     */ import java.util.concurrent.Callable;
/*     */ import net.minecraft.crash.CrashReportCategory;
/*     */ import net.minecraft.nbt.NBTBase;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.world.EnumDifficulty;
/*     */ import net.minecraft.world.GameRules;
/*     */ import net.minecraft.world.WorldSettings;
/*     */ import net.minecraft.world.WorldType;
/*     */ 
/*     */ public class WorldInfo {
/*  15 */   public static final EnumDifficulty DEFAULT_DIFFICULTY = EnumDifficulty.NORMAL;
/*     */   private long randomSeed;
/*  17 */   private WorldType terrainType = WorldType.DEFAULT;
/*  18 */   private String generatorOptions = "";
/*     */   private int spawnX;
/*     */   private int spawnY;
/*     */   private int spawnZ;
/*     */   private long totalTime;
/*     */   private long worldTime;
/*     */   private long lastTimePlayed;
/*     */   private long sizeOnDisk;
/*     */   private NBTTagCompound playerTag;
/*     */   private int dimension;
/*     */   private String levelName;
/*     */   private int saveVersion;
/*     */   private int cleanWeatherTime;
/*     */   private boolean raining;
/*     */   private int rainTime;
/*     */   private boolean thundering;
/*     */   private int thunderTime;
/*     */   private WorldSettings.GameType theGameType;
/*     */   private boolean mapFeaturesEnabled;
/*     */   private boolean hardcore;
/*     */   private boolean allowCommands;
/*     */   private boolean initialized;
/*     */   private EnumDifficulty difficulty;
/*     */   private boolean difficultyLocked;
/*  42 */   private double borderCenterX = 0.0D;
/*  43 */   private double borderCenterZ = 0.0D;
/*  44 */   private double borderSize = 6.0E7D;
/*  45 */   private long borderSizeLerpTime = 0L;
/*  46 */   private double borderSizeLerpTarget = 0.0D;
/*  47 */   private double borderSafeZone = 5.0D;
/*  48 */   private double borderDamagePerBlock = 0.2D;
/*  49 */   private int borderWarningDistance = 5;
/*  50 */   private int borderWarningTime = 15;
/*  51 */   private GameRules theGameRules = new GameRules();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WorldInfo(NBTTagCompound nbt) {
/*  59 */     this.randomSeed = nbt.getLong("RandomSeed");
/*     */     
/*  61 */     if (nbt.hasKey("generatorName", 8)) {
/*     */       
/*  63 */       String s = nbt.getString("generatorName");
/*  64 */       this.terrainType = WorldType.parseWorldType(s);
/*     */       
/*  66 */       if (this.terrainType == null) {
/*     */         
/*  68 */         this.terrainType = WorldType.DEFAULT;
/*     */       }
/*  70 */       else if (this.terrainType.isVersioned()) {
/*     */         
/*  72 */         int i = 0;
/*     */         
/*  74 */         if (nbt.hasKey("generatorVersion", 99))
/*     */         {
/*  76 */           i = nbt.getInteger("generatorVersion");
/*     */         }
/*     */         
/*  79 */         this.terrainType = this.terrainType.getWorldTypeForGeneratorVersion(i);
/*     */       } 
/*     */       
/*  82 */       if (nbt.hasKey("generatorOptions", 8))
/*     */       {
/*  84 */         this.generatorOptions = nbt.getString("generatorOptions");
/*     */       }
/*     */     } 
/*     */     
/*  88 */     this.theGameType = WorldSettings.GameType.getByID(nbt.getInteger("GameType"));
/*     */     
/*  90 */     if (nbt.hasKey("MapFeatures", 99)) {
/*     */       
/*  92 */       this.mapFeaturesEnabled = nbt.getBoolean("MapFeatures");
/*     */     }
/*     */     else {
/*     */       
/*  96 */       this.mapFeaturesEnabled = true;
/*     */     } 
/*     */     
/*  99 */     this.spawnX = nbt.getInteger("SpawnX");
/* 100 */     this.spawnY = nbt.getInteger("SpawnY");
/* 101 */     this.spawnZ = nbt.getInteger("SpawnZ");
/* 102 */     this.totalTime = nbt.getLong("Time");
/*     */     
/* 104 */     if (nbt.hasKey("DayTime", 99)) {
/*     */       
/* 106 */       this.worldTime = nbt.getLong("DayTime");
/*     */     }
/*     */     else {
/*     */       
/* 110 */       this.worldTime = this.totalTime;
/*     */     } 
/*     */     
/* 113 */     this.lastTimePlayed = nbt.getLong("LastPlayed");
/* 114 */     this.sizeOnDisk = nbt.getLong("SizeOnDisk");
/* 115 */     this.levelName = nbt.getString("LevelName");
/* 116 */     this.saveVersion = nbt.getInteger("version");
/* 117 */     this.cleanWeatherTime = nbt.getInteger("clearWeatherTime");
/* 118 */     this.rainTime = nbt.getInteger("rainTime");
/* 119 */     this.raining = nbt.getBoolean("raining");
/* 120 */     this.thunderTime = nbt.getInteger("thunderTime");
/* 121 */     this.thundering = nbt.getBoolean("thundering");
/* 122 */     this.hardcore = nbt.getBoolean("hardcore");
/*     */     
/* 124 */     if (nbt.hasKey("initialized", 99)) {
/*     */       
/* 126 */       this.initialized = nbt.getBoolean("initialized");
/*     */     }
/*     */     else {
/*     */       
/* 130 */       this.initialized = true;
/*     */     } 
/*     */     
/* 133 */     if (nbt.hasKey("allowCommands", 99)) {
/*     */       
/* 135 */       this.allowCommands = nbt.getBoolean("allowCommands");
/*     */     }
/*     */     else {
/*     */       
/* 139 */       this.allowCommands = (this.theGameType == WorldSettings.GameType.CREATIVE);
/*     */     } 
/*     */     
/* 142 */     if (nbt.hasKey("Player", 10)) {
/*     */       
/* 144 */       this.playerTag = nbt.getCompoundTag("Player");
/* 145 */       this.dimension = this.playerTag.getInteger("Dimension");
/*     */     } 
/*     */     
/* 148 */     if (nbt.hasKey("GameRules", 10))
/*     */     {
/* 150 */       this.theGameRules.readFromNBT(nbt.getCompoundTag("GameRules"));
/*     */     }
/*     */     
/* 153 */     if (nbt.hasKey("Difficulty", 99))
/*     */     {
/* 155 */       this.difficulty = EnumDifficulty.getDifficultyEnum(nbt.getByte("Difficulty"));
/*     */     }
/*     */     
/* 158 */     if (nbt.hasKey("DifficultyLocked", 1))
/*     */     {
/* 160 */       this.difficultyLocked = nbt.getBoolean("DifficultyLocked");
/*     */     }
/*     */     
/* 163 */     if (nbt.hasKey("BorderCenterX", 99))
/*     */     {
/* 165 */       this.borderCenterX = nbt.getDouble("BorderCenterX");
/*     */     }
/*     */     
/* 168 */     if (nbt.hasKey("BorderCenterZ", 99))
/*     */     {
/* 170 */       this.borderCenterZ = nbt.getDouble("BorderCenterZ");
/*     */     }
/*     */     
/* 173 */     if (nbt.hasKey("BorderSize", 99))
/*     */     {
/* 175 */       this.borderSize = nbt.getDouble("BorderSize");
/*     */     }
/*     */     
/* 178 */     if (nbt.hasKey("BorderSizeLerpTime", 99))
/*     */     {
/* 180 */       this.borderSizeLerpTime = nbt.getLong("BorderSizeLerpTime");
/*     */     }
/*     */     
/* 183 */     if (nbt.hasKey("BorderSizeLerpTarget", 99))
/*     */     {
/* 185 */       this.borderSizeLerpTarget = nbt.getDouble("BorderSizeLerpTarget");
/*     */     }
/*     */     
/* 188 */     if (nbt.hasKey("BorderSafeZone", 99))
/*     */     {
/* 190 */       this.borderSafeZone = nbt.getDouble("BorderSafeZone");
/*     */     }
/*     */     
/* 193 */     if (nbt.hasKey("BorderDamagePerBlock", 99))
/*     */     {
/* 195 */       this.borderDamagePerBlock = nbt.getDouble("BorderDamagePerBlock");
/*     */     }
/*     */     
/* 198 */     if (nbt.hasKey("BorderWarningBlocks", 99))
/*     */     {
/* 200 */       this.borderWarningDistance = nbt.getInteger("BorderWarningBlocks");
/*     */     }
/*     */     
/* 203 */     if (nbt.hasKey("BorderWarningTime", 99))
/*     */     {
/* 205 */       this.borderWarningTime = nbt.getInteger("BorderWarningTime");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public WorldInfo(WorldSettings settings, String name) {
/* 211 */     populateFromWorldSettings(settings);
/* 212 */     this.levelName = name;
/* 213 */     this.difficulty = DEFAULT_DIFFICULTY;
/* 214 */     this.initialized = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void populateFromWorldSettings(WorldSettings settings) {
/* 219 */     this.randomSeed = settings.getSeed();
/* 220 */     this.theGameType = settings.getGameType();
/* 221 */     this.mapFeaturesEnabled = settings.isMapFeaturesEnabled();
/* 222 */     this.hardcore = settings.getHardcoreEnabled();
/* 223 */     this.terrainType = settings.getTerrainType();
/* 224 */     this.generatorOptions = settings.getWorldName();
/* 225 */     this.allowCommands = settings.areCommandsAllowed();
/*     */   }
/*     */ 
/*     */   
/*     */   public WorldInfo(WorldInfo worldInformation) {
/* 230 */     this.randomSeed = worldInformation.randomSeed;
/* 231 */     this.terrainType = worldInformation.terrainType;
/* 232 */     this.generatorOptions = worldInformation.generatorOptions;
/* 233 */     this.theGameType = worldInformation.theGameType;
/* 234 */     this.mapFeaturesEnabled = worldInformation.mapFeaturesEnabled;
/* 235 */     this.spawnX = worldInformation.spawnX;
/* 236 */     this.spawnY = worldInformation.spawnY;
/* 237 */     this.spawnZ = worldInformation.spawnZ;
/* 238 */     this.totalTime = worldInformation.totalTime;
/* 239 */     this.worldTime = worldInformation.worldTime;
/* 240 */     this.lastTimePlayed = worldInformation.lastTimePlayed;
/* 241 */     this.sizeOnDisk = worldInformation.sizeOnDisk;
/* 242 */     this.playerTag = worldInformation.playerTag;
/* 243 */     this.dimension = worldInformation.dimension;
/* 244 */     this.levelName = worldInformation.levelName;
/* 245 */     this.saveVersion = worldInformation.saveVersion;
/* 246 */     this.rainTime = worldInformation.rainTime;
/* 247 */     this.raining = worldInformation.raining;
/* 248 */     this.thunderTime = worldInformation.thunderTime;
/* 249 */     this.thundering = worldInformation.thundering;
/* 250 */     this.hardcore = worldInformation.hardcore;
/* 251 */     this.allowCommands = worldInformation.allowCommands;
/* 252 */     this.initialized = worldInformation.initialized;
/* 253 */     this.theGameRules = worldInformation.theGameRules;
/* 254 */     this.difficulty = worldInformation.difficulty;
/* 255 */     this.difficultyLocked = worldInformation.difficultyLocked;
/* 256 */     this.borderCenterX = worldInformation.borderCenterX;
/* 257 */     this.borderCenterZ = worldInformation.borderCenterZ;
/* 258 */     this.borderSize = worldInformation.borderSize;
/* 259 */     this.borderSizeLerpTime = worldInformation.borderSizeLerpTime;
/* 260 */     this.borderSizeLerpTarget = worldInformation.borderSizeLerpTarget;
/* 261 */     this.borderSafeZone = worldInformation.borderSafeZone;
/* 262 */     this.borderDamagePerBlock = worldInformation.borderDamagePerBlock;
/* 263 */     this.borderWarningTime = worldInformation.borderWarningTime;
/* 264 */     this.borderWarningDistance = worldInformation.borderWarningDistance;
/*     */   }
/*     */ 
/*     */   
/*     */   public NBTTagCompound getNBTTagCompound() {
/* 269 */     NBTTagCompound nbttagcompound = new NBTTagCompound();
/* 270 */     updateTagCompound(nbttagcompound, this.playerTag);
/* 271 */     return nbttagcompound;
/*     */   }
/*     */ 
/*     */   
/*     */   public NBTTagCompound cloneNBTCompound(NBTTagCompound nbt) {
/* 276 */     NBTTagCompound nbttagcompound = new NBTTagCompound();
/* 277 */     updateTagCompound(nbttagcompound, nbt);
/* 278 */     return nbttagcompound;
/*     */   }
/*     */ 
/*     */   
/*     */   private void updateTagCompound(NBTTagCompound nbt, NBTTagCompound playerNbt) {
/* 283 */     nbt.setLong("RandomSeed", this.randomSeed);
/* 284 */     nbt.setString("generatorName", this.terrainType.getWorldTypeName());
/* 285 */     nbt.setInteger("generatorVersion", this.terrainType.getGeneratorVersion());
/* 286 */     nbt.setString("generatorOptions", this.generatorOptions);
/* 287 */     nbt.setInteger("GameType", this.theGameType.getID());
/* 288 */     nbt.setBoolean("MapFeatures", this.mapFeaturesEnabled);
/* 289 */     nbt.setInteger("SpawnX", this.spawnX);
/* 290 */     nbt.setInteger("SpawnY", this.spawnY);
/* 291 */     nbt.setInteger("SpawnZ", this.spawnZ);
/* 292 */     nbt.setLong("Time", this.totalTime);
/* 293 */     nbt.setLong("DayTime", this.worldTime);
/* 294 */     nbt.setLong("SizeOnDisk", this.sizeOnDisk);
/* 295 */     nbt.setLong("LastPlayed", MinecraftServer.getCurrentTimeMillis());
/* 296 */     nbt.setString("LevelName", this.levelName);
/* 297 */     nbt.setInteger("version", this.saveVersion);
/* 298 */     nbt.setInteger("clearWeatherTime", this.cleanWeatherTime);
/* 299 */     nbt.setInteger("rainTime", this.rainTime);
/* 300 */     nbt.setBoolean("raining", this.raining);
/* 301 */     nbt.setInteger("thunderTime", this.thunderTime);
/* 302 */     nbt.setBoolean("thundering", this.thundering);
/* 303 */     nbt.setBoolean("hardcore", this.hardcore);
/* 304 */     nbt.setBoolean("allowCommands", this.allowCommands);
/* 305 */     nbt.setBoolean("initialized", this.initialized);
/* 306 */     nbt.setDouble("BorderCenterX", this.borderCenterX);
/* 307 */     nbt.setDouble("BorderCenterZ", this.borderCenterZ);
/* 308 */     nbt.setDouble("BorderSize", this.borderSize);
/* 309 */     nbt.setLong("BorderSizeLerpTime", this.borderSizeLerpTime);
/* 310 */     nbt.setDouble("BorderSafeZone", this.borderSafeZone);
/* 311 */     nbt.setDouble("BorderDamagePerBlock", this.borderDamagePerBlock);
/* 312 */     nbt.setDouble("BorderSizeLerpTarget", this.borderSizeLerpTarget);
/* 313 */     nbt.setDouble("BorderWarningBlocks", this.borderWarningDistance);
/* 314 */     nbt.setDouble("BorderWarningTime", this.borderWarningTime);
/*     */     
/* 316 */     if (this.difficulty != null)
/*     */     {
/* 318 */       nbt.setByte("Difficulty", (byte)this.difficulty.getDifficultyId());
/*     */     }
/*     */     
/* 321 */     nbt.setBoolean("DifficultyLocked", this.difficultyLocked);
/* 322 */     nbt.setTag("GameRules", (NBTBase)this.theGameRules.writeToNBT());
/*     */     
/* 324 */     if (playerNbt != null)
/*     */     {
/* 326 */       nbt.setTag("Player", (NBTBase)playerNbt);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public long getSeed() {
/* 332 */     return this.randomSeed;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSpawnX() {
/* 337 */     return this.spawnX;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSpawnY() {
/* 342 */     return this.spawnY;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSpawnZ() {
/* 347 */     return this.spawnZ;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getWorldTotalTime() {
/* 352 */     return this.totalTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getWorldTime() {
/* 357 */     return this.worldTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getSizeOnDisk() {
/* 362 */     return this.sizeOnDisk;
/*     */   }
/*     */ 
/*     */   
/*     */   public NBTTagCompound getPlayerNBTTagCompound() {
/* 367 */     return this.playerTag;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSpawnX(int x) {
/* 372 */     this.spawnX = x;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSpawnY(int y) {
/* 377 */     this.spawnY = y;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSpawnZ(int z) {
/* 382 */     this.spawnZ = z;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setWorldTotalTime(long time) {
/* 387 */     this.totalTime = time;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setWorldTime(long time) {
/* 392 */     this.worldTime = time;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSpawn(BlockPos spawnPoint) {
/* 397 */     this.spawnX = spawnPoint.getX();
/* 398 */     this.spawnY = spawnPoint.getY();
/* 399 */     this.spawnZ = spawnPoint.getZ();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getWorldName() {
/* 404 */     return this.levelName;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setWorldName(String worldName) {
/* 409 */     this.levelName = worldName;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSaveVersion() {
/* 414 */     return this.saveVersion;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSaveVersion(int version) {
/* 419 */     this.saveVersion = version;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getLastTimePlayed() {
/* 424 */     return this.lastTimePlayed;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getCleanWeatherTime() {
/* 429 */     return this.cleanWeatherTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCleanWeatherTime(int cleanWeatherTimeIn) {
/* 434 */     this.cleanWeatherTime = cleanWeatherTimeIn;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isThundering() {
/* 439 */     return this.thundering;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setThundering(boolean thunderingIn) {
/* 444 */     this.thundering = thunderingIn;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getThunderTime() {
/* 449 */     return this.thunderTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setThunderTime(int time) {
/* 454 */     this.thunderTime = time;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isRaining() {
/* 459 */     return this.raining;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setRaining(boolean isRaining) {
/* 464 */     this.raining = isRaining;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRainTime() {
/* 469 */     return this.rainTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setRainTime(int time) {
/* 474 */     this.rainTime = time;
/*     */   }
/*     */ 
/*     */   
/*     */   public WorldSettings.GameType getGameType() {
/* 479 */     return this.theGameType;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isMapFeaturesEnabled() {
/* 484 */     return this.mapFeaturesEnabled;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setMapFeaturesEnabled(boolean enabled) {
/* 489 */     this.mapFeaturesEnabled = enabled;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setGameType(WorldSettings.GameType type) {
/* 494 */     this.theGameType = type;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isHardcoreModeEnabled() {
/* 499 */     return this.hardcore;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setHardcore(boolean hardcoreIn) {
/* 504 */     this.hardcore = hardcoreIn;
/*     */   }
/*     */ 
/*     */   
/*     */   public WorldType getTerrainType() {
/* 509 */     return this.terrainType;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setTerrainType(WorldType type) {
/* 514 */     this.terrainType = type;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getGeneratorOptions() {
/* 519 */     return this.generatorOptions;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean areCommandsAllowed() {
/* 524 */     return this.allowCommands;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setAllowCommands(boolean allow) {
/* 529 */     this.allowCommands = allow;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isInitialized() {
/* 534 */     return this.initialized;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setServerInitialized(boolean initializedIn) {
/* 539 */     this.initialized = initializedIn;
/*     */   }
/*     */ 
/*     */   
/*     */   public GameRules getGameRulesInstance() {
/* 544 */     return this.theGameRules;
/*     */   }
/*     */ 
/*     */   
/*     */   public double getBorderCenterX() {
/* 549 */     return this.borderCenterX;
/*     */   }
/*     */ 
/*     */   
/*     */   public double getBorderCenterZ() {
/* 554 */     return this.borderCenterZ;
/*     */   }
/*     */ 
/*     */   
/*     */   public double getBorderSize() {
/* 559 */     return this.borderSize;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBorderSize(double size) {
/* 564 */     this.borderSize = size;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getBorderLerpTime() {
/* 569 */     return this.borderSizeLerpTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBorderLerpTime(long time) {
/* 574 */     this.borderSizeLerpTime = time;
/*     */   }
/*     */ 
/*     */   
/*     */   public double getBorderLerpTarget() {
/* 579 */     return this.borderSizeLerpTarget;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBorderLerpTarget(double lerpSize) {
/* 584 */     this.borderSizeLerpTarget = lerpSize;
/*     */   }
/*     */ 
/*     */   
/*     */   public void getBorderCenterZ(double posZ) {
/* 589 */     this.borderCenterZ = posZ;
/*     */   }
/*     */ 
/*     */   
/*     */   public void getBorderCenterX(double posX) {
/* 594 */     this.borderCenterX = posX;
/*     */   }
/*     */ 
/*     */   
/*     */   public double getBorderSafeZone() {
/* 599 */     return this.borderSafeZone;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBorderSafeZone(double amount) {
/* 604 */     this.borderSafeZone = amount;
/*     */   }
/*     */ 
/*     */   
/*     */   public double getBorderDamagePerBlock() {
/* 609 */     return this.borderDamagePerBlock;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBorderDamagePerBlock(double damage) {
/* 614 */     this.borderDamagePerBlock = damage;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getBorderWarningDistance() {
/* 619 */     return this.borderWarningDistance;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getBorderWarningTime() {
/* 624 */     return this.borderWarningTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBorderWarningDistance(int amountOfBlocks) {
/* 629 */     this.borderWarningDistance = amountOfBlocks;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBorderWarningTime(int ticks) {
/* 634 */     this.borderWarningTime = ticks;
/*     */   }
/*     */ 
/*     */   
/*     */   public EnumDifficulty getDifficulty() {
/* 639 */     return this.difficulty;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDifficulty(EnumDifficulty newDifficulty) {
/* 644 */     this.difficulty = newDifficulty;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDifficultyLocked() {
/* 649 */     return this.difficultyLocked;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDifficultyLocked(boolean locked) {
/* 654 */     this.difficultyLocked = locked;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addToCrashReport(CrashReportCategory category) {
/* 659 */     category.addCrashSectionCallable("Level seed", new Callable<String>()
/*     */         {
/*     */           public String call() throws Exception
/*     */           {
/* 663 */             return String.valueOf(WorldInfo.this.getSeed());
/*     */           }
/*     */         });
/* 666 */     category.addCrashSectionCallable("Level generator", new Callable<String>()
/*     */         {
/*     */           public String call() throws Exception
/*     */           {
/* 670 */             return String.format("ID %02d - %s, ver %d. Features enabled: %b", new Object[] { Integer.valueOf(WorldInfo.access$000(this.this$0).getWorldTypeID()), WorldInfo.access$000(this.this$0).getWorldTypeName(), Integer.valueOf(WorldInfo.access$000(this.this$0).getGeneratorVersion()), Boolean.valueOf(WorldInfo.access$100(this.this$0)) });
/*     */           }
/*     */         });
/* 673 */     category.addCrashSectionCallable("Level generator options", new Callable<String>()
/*     */         {
/*     */           public String call() throws Exception
/*     */           {
/* 677 */             return WorldInfo.this.generatorOptions;
/*     */           }
/*     */         });
/* 680 */     category.addCrashSectionCallable("Level spawn location", new Callable<String>()
/*     */         {
/*     */           public String call() throws Exception
/*     */           {
/* 684 */             return CrashReportCategory.getCoordinateInfo(WorldInfo.this.spawnX, WorldInfo.this.spawnY, WorldInfo.this.spawnZ);
/*     */           }
/*     */         });
/* 687 */     category.addCrashSectionCallable("Level time", new Callable<String>()
/*     */         {
/*     */           public String call() throws Exception
/*     */           {
/* 691 */             return String.format("%d game time, %d day time", new Object[] { Long.valueOf(WorldInfo.access$600(this.this$0)), Long.valueOf(WorldInfo.access$700(this.this$0)) });
/*     */           }
/*     */         });
/* 694 */     category.addCrashSectionCallable("Level dimension", new Callable<String>()
/*     */         {
/*     */           public String call() throws Exception
/*     */           {
/* 698 */             return String.valueOf(WorldInfo.this.dimension);
/*     */           }
/*     */         });
/* 701 */     category.addCrashSectionCallable("Level storage version", new Callable<String>()
/*     */         {
/*     */           public String call() throws Exception
/*     */           {
/* 705 */             String s = "Unknown?";
/*     */ 
/*     */             
/*     */             try {
/* 709 */               switch (WorldInfo.this.saveVersion) {
/*     */                 
/*     */                 case 19132:
/* 712 */                   s = "McRegion";
/*     */                   break;
/*     */                 
/*     */                 case 19133:
/* 716 */                   s = "Anvil";
/*     */                   break;
/*     */               } 
/* 719 */             } catch (Throwable throwable) {}
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 724 */             return String.format("0x%05X - %s", new Object[] { Integer.valueOf(WorldInfo.access$900(this.this$0)), s });
/*     */           }
/*     */         });
/* 727 */     category.addCrashSectionCallable("Level weather", new Callable<String>()
/*     */         {
/*     */           public String call() throws Exception
/*     */           {
/* 731 */             return String.format("Rain time: %d (now: %b), thunder time: %d (now: %b)", new Object[] { Integer.valueOf(WorldInfo.access$1000(this.this$0)), Boolean.valueOf(WorldInfo.access$1100(this.this$0)), Integer.valueOf(WorldInfo.access$1200(this.this$0)), Boolean.valueOf(WorldInfo.access$1300(this.this$0)) });
/*     */           }
/*     */         });
/* 734 */     category.addCrashSectionCallable("Level game mode", new Callable<String>()
/*     */         {
/*     */           public String call() throws Exception
/*     */           {
/* 738 */             return String.format("Game mode: %s (ID %d). Hardcore: %b. Cheats: %b", new Object[] { WorldInfo.access$1400(this.this$0).getName(), Integer.valueOf(WorldInfo.access$1400(this.this$0).getID()), Boolean.valueOf(WorldInfo.access$1500(this.this$0)), Boolean.valueOf(WorldInfo.access$1600(this.this$0)) });
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   protected WorldInfo() {}
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\world\storage\WorldInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */