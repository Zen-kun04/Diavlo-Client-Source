/*     */ package net.minecraft.world;
/*     */ 
/*     */ public class WorldType
/*     */ {
/*   5 */   public static final WorldType[] worldTypes = new WorldType[16];
/*   6 */   public static final WorldType DEFAULT = (new WorldType(0, "default", 1)).setVersioned();
/*   7 */   public static final WorldType FLAT = new WorldType(1, "flat");
/*   8 */   public static final WorldType LARGE_BIOMES = new WorldType(2, "largeBiomes");
/*   9 */   public static final WorldType AMPLIFIED = (new WorldType(3, "amplified")).setNotificationData();
/*  10 */   public static final WorldType CUSTOMIZED = new WorldType(4, "customized");
/*  11 */   public static final WorldType DEBUG_WORLD = new WorldType(5, "debug_all_block_states");
/*  12 */   public static final WorldType DEFAULT_1_1 = (new WorldType(8, "default_1_1", 0)).setCanBeCreated(false);
/*     */   
/*     */   private final int worldTypeId;
/*     */   private final String worldType;
/*     */   private final int generatorVersion;
/*     */   private boolean canBeCreated;
/*     */   private boolean isWorldTypeVersioned;
/*     */   private boolean hasNotificationData;
/*     */   
/*     */   private WorldType(int id, String name) {
/*  22 */     this(id, name, 0);
/*     */   }
/*     */ 
/*     */   
/*     */   private WorldType(int id, String name, int version) {
/*  27 */     this.worldType = name;
/*  28 */     this.generatorVersion = version;
/*  29 */     this.canBeCreated = true;
/*  30 */     this.worldTypeId = id;
/*  31 */     worldTypes[id] = this;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getWorldTypeName() {
/*  36 */     return this.worldType;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTranslateName() {
/*  41 */     return "generator." + this.worldType;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTranslatedInfo() {
/*  46 */     return getTranslateName() + ".info";
/*     */   }
/*     */ 
/*     */   
/*     */   public int getGeneratorVersion() {
/*  51 */     return this.generatorVersion;
/*     */   }
/*     */ 
/*     */   
/*     */   public WorldType getWorldTypeForGeneratorVersion(int version) {
/*  56 */     return (this == DEFAULT && version == 0) ? DEFAULT_1_1 : this;
/*     */   }
/*     */ 
/*     */   
/*     */   private WorldType setCanBeCreated(boolean enable) {
/*  61 */     this.canBeCreated = enable;
/*  62 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getCanBeCreated() {
/*  67 */     return this.canBeCreated;
/*     */   }
/*     */ 
/*     */   
/*     */   private WorldType setVersioned() {
/*  72 */     this.isWorldTypeVersioned = true;
/*  73 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isVersioned() {
/*  78 */     return this.isWorldTypeVersioned;
/*     */   }
/*     */ 
/*     */   
/*     */   public static WorldType parseWorldType(String type) {
/*  83 */     for (int i = 0; i < worldTypes.length; i++) {
/*     */       
/*  85 */       if (worldTypes[i] != null && (worldTypes[i]).worldType.equalsIgnoreCase(type))
/*     */       {
/*  87 */         return worldTypes[i];
/*     */       }
/*     */     } 
/*     */     
/*  91 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getWorldTypeID() {
/*  96 */     return this.worldTypeId;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean showWorldInfoNotice() {
/* 101 */     return this.hasNotificationData;
/*     */   }
/*     */ 
/*     */   
/*     */   private WorldType setNotificationData() {
/* 106 */     this.hasNotificationData = true;
/* 107 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\world\WorldType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */