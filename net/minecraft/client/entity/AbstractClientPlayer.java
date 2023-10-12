/*     */ package net.minecraft.client.entity;
/*     */ 
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import java.io.File;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.network.NetworkPlayerInfo;
/*     */ import net.minecraft.client.renderer.IImageBuffer;
/*     */ import net.minecraft.client.renderer.ImageBufferDownload;
/*     */ import net.minecraft.client.renderer.ThreadDownloadImageData;
/*     */ import net.minecraft.client.renderer.texture.ITextureObject;
/*     */ import net.minecraft.client.renderer.texture.TextureManager;
/*     */ import net.minecraft.client.resources.DefaultPlayerSkin;
/*     */ import net.minecraft.entity.SharedMonsterAttributes;
/*     */ import net.minecraft.entity.ai.attributes.IAttributeInstance;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.src.Config;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraft.util.StringUtils;
/*     */ import net.minecraft.util.Vec3;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.WorldSettings;
/*     */ import net.optifine.player.CapeUtils;
/*     */ import net.optifine.player.PlayerConfigurations;
/*     */ import net.optifine.reflect.Reflector;
/*     */ 
/*     */ public abstract class AbstractClientPlayer extends EntityPlayer {
/*     */   private NetworkPlayerInfo playerInfo;
/*  29 */   private ResourceLocation locationOfCape = null;
/*  30 */   private long reloadCapeTimeMs = 0L;
/*     */   private boolean elytraOfCape = false;
/*  32 */   private String nameClear = null;
/*  33 */   private static final ResourceLocation TEXTURE_ELYTRA = new ResourceLocation("textures/entity/elytra.png");
/*     */ 
/*     */   
/*     */   public AbstractClientPlayer(World worldIn, GameProfile playerProfile) {
/*  37 */     super(worldIn, playerProfile);
/*  38 */     this.nameClear = playerProfile.getName();
/*     */     
/*  40 */     if (this.nameClear != null && !this.nameClear.isEmpty())
/*     */     {
/*  42 */       this.nameClear = StringUtils.stripControlCodes(this.nameClear);
/*     */     }
/*     */     
/*  45 */     CapeUtils.downloadCape(this);
/*  46 */     PlayerConfigurations.getPlayerConfiguration(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSpectator() {
/*  51 */     NetworkPlayerInfo networkplayerinfo = Minecraft.getMinecraft().getNetHandler().getPlayerInfo(getGameProfile().getId());
/*  52 */     return (networkplayerinfo != null && networkplayerinfo.getGameType() == WorldSettings.GameType.SPECTATOR);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasPlayerInfo() {
/*  57 */     return (getPlayerInfo() != null);
/*     */   }
/*     */ 
/*     */   
/*     */   protected NetworkPlayerInfo getPlayerInfo() {
/*  62 */     if (this.playerInfo == null)
/*     */     {
/*  64 */       this.playerInfo = Minecraft.getMinecraft().getNetHandler().getPlayerInfo(getUniqueID());
/*     */     }
/*     */     
/*  67 */     return this.playerInfo;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasSkin() {
/*  72 */     NetworkPlayerInfo networkplayerinfo = getPlayerInfo();
/*  73 */     return (networkplayerinfo != null && networkplayerinfo.hasLocationSkin());
/*     */   }
/*     */ 
/*     */   
/*     */   public ResourceLocation getLocationSkin() {
/*  78 */     NetworkPlayerInfo networkplayerinfo = getPlayerInfo();
/*  79 */     return (networkplayerinfo == null) ? DefaultPlayerSkin.getDefaultSkin(getUniqueID()) : networkplayerinfo.getLocationSkin();
/*     */   }
/*     */ 
/*     */   
/*     */   public ResourceLocation getLocationCape() {
/*  84 */     if (!Config.isShowCapes())
/*     */     {
/*  86 */       return null;
/*     */     }
/*     */ 
/*     */     
/*  90 */     if (this.reloadCapeTimeMs != 0L && System.currentTimeMillis() > this.reloadCapeTimeMs) {
/*     */       
/*  92 */       CapeUtils.reloadCape(this);
/*  93 */       this.reloadCapeTimeMs = 0L;
/*     */     } 
/*     */     
/*  96 */     if (this.locationOfCape != null)
/*     */     {
/*  98 */       return this.locationOfCape;
/*     */     }
/*     */ 
/*     */     
/* 102 */     NetworkPlayerInfo networkplayerinfo = getPlayerInfo();
/* 103 */     return (networkplayerinfo == null) ? null : networkplayerinfo.getLocationCape();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static ThreadDownloadImageData getDownloadImageSkin(ResourceLocation resourceLocationIn, String username) {
/*     */     ThreadDownloadImageData threadDownloadImageData;
/* 110 */     TextureManager texturemanager = Minecraft.getMinecraft().getTextureManager();
/* 111 */     ITextureObject itextureobject = texturemanager.getTexture(resourceLocationIn);
/*     */     
/* 113 */     if (itextureobject == null) {
/*     */       
/* 115 */       threadDownloadImageData = new ThreadDownloadImageData((File)null, String.format("http://skins.minecraft.net/MinecraftSkins/%s.png", new Object[] { StringUtils.stripControlCodes(username) }), DefaultPlayerSkin.getDefaultSkin(getOfflineUUID(username)), (IImageBuffer)new ImageBufferDownload());
/* 116 */       texturemanager.loadTexture(resourceLocationIn, (ITextureObject)threadDownloadImageData);
/*     */     } 
/*     */     
/* 119 */     return threadDownloadImageData;
/*     */   }
/*     */ 
/*     */   
/*     */   public static ResourceLocation getLocationSkin(String username) {
/* 124 */     return new ResourceLocation("skins/" + StringUtils.stripControlCodes(username));
/*     */   }
/*     */ 
/*     */   
/*     */   public String getSkinType() {
/* 129 */     NetworkPlayerInfo networkplayerinfo = getPlayerInfo();
/* 130 */     return (networkplayerinfo == null) ? DefaultPlayerSkin.getSkinType(getUniqueID()) : networkplayerinfo.getSkinType();
/*     */   }
/*     */ 
/*     */   
/*     */   public float getFovModifier() {
/* 135 */     float f = 1.0F;
/*     */     
/* 137 */     if (this.capabilities.isFlying)
/*     */     {
/* 139 */       f *= 1.1F;
/*     */     }
/*     */     
/* 142 */     IAttributeInstance iattributeinstance = getEntityAttribute(SharedMonsterAttributes.movementSpeed);
/* 143 */     f = (float)(f * (iattributeinstance.getAttributeValue() / this.capabilities.getWalkSpeed() + 1.0D) / 2.0D);
/*     */     
/* 145 */     if (this.capabilities.getWalkSpeed() == 0.0F || Float.isNaN(f) || Float.isInfinite(f))
/*     */     {
/* 147 */       f = 1.0F;
/*     */     }
/*     */     
/* 150 */     if (isUsingItem() && getItemInUse().getItem() == Items.bow) {
/*     */       
/* 152 */       int i = getItemInUseDuration();
/* 153 */       float f1 = i / 20.0F;
/*     */       
/* 155 */       if (f1 > 1.0F) {
/*     */         
/* 157 */         f1 = 1.0F;
/*     */       }
/*     */       else {
/*     */         
/* 161 */         f1 *= f1;
/*     */       } 
/*     */       
/* 164 */       f *= 1.0F - f1 * 0.15F;
/*     */     } 
/*     */     
/* 167 */     return Reflector.ForgeHooksClient_getOffsetFOV.exists() ? Reflector.callFloat(Reflector.ForgeHooksClient_getOffsetFOV, new Object[] { this, Float.valueOf(f) }) : f;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getNameClear() {
/* 172 */     return this.nameClear;
/*     */   }
/*     */ 
/*     */   
/*     */   public ResourceLocation getLocationOfCape() {
/* 177 */     return this.locationOfCape;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setLocationOfCape(ResourceLocation p_setLocationOfCape_1_) {
/* 182 */     this.locationOfCape = p_setLocationOfCape_1_;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasElytraCape() {
/* 187 */     ResourceLocation resourcelocation = getLocationCape();
/* 188 */     return (resourcelocation == null) ? false : ((resourcelocation == this.locationOfCape) ? this.elytraOfCape : true);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setElytraOfCape(boolean p_setElytraOfCape_1_) {
/* 193 */     this.elytraOfCape = p_setElytraOfCape_1_;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isElytraOfCape() {
/* 198 */     return this.elytraOfCape;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getReloadCapeTimeMs() {
/* 203 */     return this.reloadCapeTimeMs;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setReloadCapeTimeMs(long p_setReloadCapeTimeMs_1_) {
/* 208 */     this.reloadCapeTimeMs = p_setReloadCapeTimeMs_1_;
/*     */   }
/*     */ 
/*     */   
/*     */   public Vec3 getLook(float partialTicks) {
/* 213 */     return getVectorForRotation(this.rotationPitch, this.rotationYaw);
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\entity\AbstractClientPlayer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */