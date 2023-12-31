/*     */ package net.minecraft.client.network;
/*     */ 
/*     */ import com.google.common.base.Objects;
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import com.mojang.authlib.minecraft.MinecraftProfileTexture;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.resources.DefaultPlayerSkin;
/*     */ import net.minecraft.client.resources.SkinManager;
/*     */ import net.minecraft.network.play.server.S38PacketPlayerListItem;
/*     */ import net.minecraft.scoreboard.ScorePlayerTeam;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraft.world.WorldSettings;
/*     */ 
/*     */ 
/*     */ public class NetworkPlayerInfo
/*     */ {
/*     */   private final GameProfile gameProfile;
/*     */   private WorldSettings.GameType gameType;
/*     */   private int responseTime;
/*     */   private boolean playerTexturesLoaded = false;
/*     */   private ResourceLocation locationSkin;
/*     */   private ResourceLocation locationCape;
/*     */   private String skinType;
/*     */   private IChatComponent displayName;
/*  26 */   private int field_178873_i = 0;
/*  27 */   private int field_178870_j = 0;
/*  28 */   private long field_178871_k = 0L;
/*  29 */   private long field_178868_l = 0L;
/*  30 */   private long field_178869_m = 0L;
/*     */ 
/*     */   
/*     */   public NetworkPlayerInfo(GameProfile p_i46294_1_) {
/*  34 */     this.gameProfile = p_i46294_1_;
/*     */   }
/*     */ 
/*     */   
/*     */   public NetworkPlayerInfo(S38PacketPlayerListItem.AddPlayerData p_i46295_1_) {
/*  39 */     this.gameProfile = p_i46295_1_.getProfile();
/*  40 */     this.gameType = p_i46295_1_.getGameMode();
/*  41 */     this.responseTime = p_i46295_1_.getPing();
/*  42 */     this.displayName = p_i46295_1_.getDisplayName();
/*     */   }
/*     */ 
/*     */   
/*     */   public GameProfile getGameProfile() {
/*  47 */     return this.gameProfile;
/*     */   }
/*     */ 
/*     */   
/*     */   public WorldSettings.GameType getGameType() {
/*  52 */     return this.gameType;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getResponseTime() {
/*  57 */     return this.responseTime;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setGameType(WorldSettings.GameType p_178839_1_) {
/*  62 */     this.gameType = p_178839_1_;
/*     */   }
/*     */   
/*     */   public String getGametype() {
/*  66 */     return this.gameType.getName().toString();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setResponseTime(int p_178838_1_) {
/*  71 */     this.responseTime = p_178838_1_;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasLocationSkin() {
/*  76 */     return (this.locationSkin != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getSkinType() {
/*  81 */     return (this.skinType == null) ? DefaultPlayerSkin.getSkinType(this.gameProfile.getId()) : this.skinType;
/*     */   }
/*     */ 
/*     */   
/*     */   public ResourceLocation getLocationSkin() {
/*  86 */     if (this.locationSkin == null)
/*     */     {
/*  88 */       loadPlayerTextures();
/*     */     }
/*     */     
/*  91 */     return (ResourceLocation)Objects.firstNonNull(this.locationSkin, DefaultPlayerSkin.getDefaultSkin(this.gameProfile.getId()));
/*     */   }
/*     */ 
/*     */   
/*     */   public ResourceLocation getLocationCape() {
/*  96 */     if (this.locationCape == null)
/*     */     {
/*  98 */       loadPlayerTextures();
/*     */     }
/*     */     
/* 101 */     return this.locationCape;
/*     */   }
/*     */ 
/*     */   
/*     */   public ScorePlayerTeam getPlayerTeam() {
/* 106 */     return (Minecraft.getMinecraft()).theWorld.getScoreboard().getPlayersTeam(getGameProfile().getName());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void loadPlayerTextures() {
/* 111 */     synchronized (this) {
/*     */       
/* 113 */       if (!this.playerTexturesLoaded) {
/*     */         
/* 115 */         this.playerTexturesLoaded = true;
/* 116 */         Minecraft.getMinecraft().getSkinManager().loadProfileTextures(this.gameProfile, new SkinManager.SkinAvailableCallback()
/*     */             {
/*     */               public void skinAvailable(MinecraftProfileTexture.Type p_180521_1_, ResourceLocation location, MinecraftProfileTexture profileTexture)
/*     */               {
/* 120 */                 switch (p_180521_1_) {
/*     */                   
/*     */                   case SKIN:
/* 123 */                     NetworkPlayerInfo.this.locationSkin = location;
/* 124 */                     NetworkPlayerInfo.this.skinType = profileTexture.getMetadata("model");
/*     */                     
/* 126 */                     if (NetworkPlayerInfo.this.skinType == null)
/*     */                     {
/* 128 */                       NetworkPlayerInfo.this.skinType = "default";
/*     */                     }
/*     */                     break;
/*     */ 
/*     */                   
/*     */                   case CAPE:
/* 134 */                     NetworkPlayerInfo.this.locationCape = location;
/*     */                     break;
/*     */                 } 
/*     */               }
/*     */             },  true);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setDisplayName(IChatComponent displayNameIn) {
/* 144 */     this.displayName = displayNameIn;
/*     */   }
/*     */ 
/*     */   
/*     */   public IChatComponent getDisplayName() {
/* 149 */     return this.displayName;
/*     */   }
/*     */ 
/*     */   
/*     */   public int func_178835_l() {
/* 154 */     return this.field_178873_i;
/*     */   }
/*     */ 
/*     */   
/*     */   public void func_178836_b(int p_178836_1_) {
/* 159 */     this.field_178873_i = p_178836_1_;
/*     */   }
/*     */ 
/*     */   
/*     */   public int func_178860_m() {
/* 164 */     return this.field_178870_j;
/*     */   }
/*     */ 
/*     */   
/*     */   public void func_178857_c(int p_178857_1_) {
/* 169 */     this.field_178870_j = p_178857_1_;
/*     */   }
/*     */ 
/*     */   
/*     */   public long func_178847_n() {
/* 174 */     return this.field_178871_k;
/*     */   }
/*     */ 
/*     */   
/*     */   public void func_178846_a(long p_178846_1_) {
/* 179 */     this.field_178871_k = p_178846_1_;
/*     */   }
/*     */ 
/*     */   
/*     */   public long func_178858_o() {
/* 184 */     return this.field_178868_l;
/*     */   }
/*     */ 
/*     */   
/*     */   public void func_178844_b(long p_178844_1_) {
/* 189 */     this.field_178868_l = p_178844_1_;
/*     */   }
/*     */ 
/*     */   
/*     */   public long func_178855_p() {
/* 194 */     return this.field_178869_m;
/*     */   }
/*     */ 
/*     */   
/*     */   public void func_178843_c(long p_178843_1_) {
/* 199 */     this.field_178869_m = p_178843_1_;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\network\NetworkPlayerInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */