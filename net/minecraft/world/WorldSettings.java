/*     */ package net.minecraft.world;
/*     */ 
/*     */ import net.minecraft.entity.player.PlayerCapabilities;
/*     */ import net.minecraft.world.storage.WorldInfo;
/*     */ 
/*     */ 
/*     */ public final class WorldSettings
/*     */ {
/*     */   private final long seed;
/*     */   private final GameType theGameType;
/*     */   private final boolean mapFeaturesEnabled;
/*     */   private final boolean hardcoreEnabled;
/*     */   private final WorldType terrainType;
/*     */   private boolean commandsAllowed;
/*     */   private boolean bonusChestEnabled;
/*     */   private String worldName;
/*     */   
/*     */   public WorldSettings(long seedIn, GameType gameType, boolean enableMapFeatures, boolean hardcoreMode, WorldType worldTypeIn) {
/*  19 */     this.worldName = "";
/*  20 */     this.seed = seedIn;
/*  21 */     this.theGameType = gameType;
/*  22 */     this.mapFeaturesEnabled = enableMapFeatures;
/*  23 */     this.hardcoreEnabled = hardcoreMode;
/*  24 */     this.terrainType = worldTypeIn;
/*     */   }
/*     */ 
/*     */   
/*     */   public WorldSettings(WorldInfo info) {
/*  29 */     this(info.getSeed(), info.getGameType(), info.isMapFeaturesEnabled(), info.isHardcoreModeEnabled(), info.getTerrainType());
/*     */   }
/*     */ 
/*     */   
/*     */   public WorldSettings enableBonusChest() {
/*  34 */     this.bonusChestEnabled = true;
/*  35 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public WorldSettings enableCommands() {
/*  40 */     this.commandsAllowed = true;
/*  41 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public WorldSettings setWorldName(String name) {
/*  46 */     this.worldName = name;
/*  47 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isBonusChestEnabled() {
/*  52 */     return this.bonusChestEnabled;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getSeed() {
/*  57 */     return this.seed;
/*     */   }
/*     */ 
/*     */   
/*     */   public GameType getGameType() {
/*  62 */     return this.theGameType;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getHardcoreEnabled() {
/*  67 */     return this.hardcoreEnabled;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isMapFeaturesEnabled() {
/*  72 */     return this.mapFeaturesEnabled;
/*     */   }
/*     */ 
/*     */   
/*     */   public WorldType getTerrainType() {
/*  77 */     return this.terrainType;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean areCommandsAllowed() {
/*  82 */     return this.commandsAllowed;
/*     */   }
/*     */ 
/*     */   
/*     */   public static GameType getGameTypeById(int id) {
/*  87 */     return GameType.getByID(id);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getWorldName() {
/*  92 */     return this.worldName;
/*     */   }
/*     */   
/*     */   public enum GameType
/*     */   {
/*  97 */     NOT_SET(-1, ""),
/*  98 */     SURVIVAL(0, "survival"),
/*  99 */     CREATIVE(1, "creative"),
/* 100 */     ADVENTURE(2, "adventure"),
/* 101 */     SPECTATOR(3, "spectator");
/*     */     
/*     */     int id;
/*     */     
/*     */     String name;
/*     */     
/*     */     GameType(int typeId, String nameIn) {
/* 108 */       this.id = typeId;
/* 109 */       this.name = nameIn;
/*     */     }
/*     */ 
/*     */     
/*     */     public int getID() {
/* 114 */       return this.id;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getName() {
/* 119 */       return this.name;
/*     */     }
/*     */ 
/*     */     
/*     */     public void configurePlayerCapabilities(PlayerCapabilities capabilities) {
/* 124 */       if (this == CREATIVE) {
/*     */         
/* 126 */         capabilities.allowFlying = true;
/* 127 */         capabilities.isCreativeMode = true;
/* 128 */         capabilities.disableDamage = true;
/*     */       }
/* 130 */       else if (this == SPECTATOR) {
/*     */         
/* 132 */         capabilities.allowFlying = true;
/* 133 */         capabilities.isCreativeMode = false;
/* 134 */         capabilities.disableDamage = true;
/* 135 */         capabilities.isFlying = true;
/*     */       }
/*     */       else {
/*     */         
/* 139 */         capabilities.allowFlying = false;
/* 140 */         capabilities.isCreativeMode = false;
/* 141 */         capabilities.disableDamage = false;
/* 142 */         capabilities.isFlying = false;
/*     */       } 
/*     */       
/* 145 */       capabilities.allowEdit = !isAdventure();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isAdventure() {
/* 150 */       return (this == ADVENTURE || this == SPECTATOR);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isCreative() {
/* 155 */       return (this == CREATIVE);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isSurvivalOrAdventure() {
/* 160 */       return (this == SURVIVAL || this == ADVENTURE);
/*     */     }
/*     */ 
/*     */     
/*     */     public static GameType getByID(int idIn) {
/* 165 */       for (GameType worldsettings$gametype : values()) {
/*     */         
/* 167 */         if (worldsettings$gametype.id == idIn)
/*     */         {
/* 169 */           return worldsettings$gametype;
/*     */         }
/*     */       } 
/*     */       
/* 173 */       return SURVIVAL;
/*     */     }
/*     */ 
/*     */     
/*     */     public static GameType getByName(String gamemodeName) {
/* 178 */       for (GameType worldsettings$gametype : values()) {
/*     */         
/* 180 */         if (worldsettings$gametype.name.equals(gamemodeName))
/*     */         {
/* 182 */           return worldsettings$gametype;
/*     */         }
/*     */       } 
/*     */       
/* 186 */       return SURVIVAL;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\world\WorldSettings.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */