/*     */ package net.minecraft.client.renderer.entity;
/*     */ 
/*     */ import net.minecraft.client.entity.AbstractClientPlayer;
/*     */ import net.minecraft.client.model.ModelBase;
/*     */ import net.minecraft.client.model.ModelPlayer;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.entity.layers.LayerArrow;
/*     */ import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
/*     */ import net.minecraft.client.renderer.entity.layers.LayerCape;
/*     */ import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
/*     */ import net.minecraft.client.renderer.entity.layers.LayerDeadmau5Head;
/*     */ import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.player.EnumPlayerModelParts;
/*     */ import net.minecraft.item.EnumAction;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.scoreboard.Score;
/*     */ import net.minecraft.scoreboard.ScoreObjective;
/*     */ import net.minecraft.scoreboard.Scoreboard;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ 
/*     */ public class RenderPlayer extends RendererLivingEntity<AbstractClientPlayer> {
/*     */   private boolean smallArms;
/*     */   
/*     */   public RenderPlayer(RenderManager renderManager) {
/*  27 */     this(renderManager, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public RenderPlayer(RenderManager renderManager, boolean useSmallArms) {
/*  32 */     super(renderManager, (ModelBase)new ModelPlayer(0.0F, useSmallArms), 0.5F);
/*  33 */     this.smallArms = useSmallArms;
/*  34 */     addLayer(new LayerBipedArmor(this));
/*  35 */     addLayer(new LayerHeldItem(this));
/*  36 */     addLayer(new LayerArrow(this));
/*  37 */     addLayer(new LayerDeadmau5Head(this));
/*  38 */     addLayer(new LayerCape(this));
/*  39 */     addLayer(new LayerCustomHead((getMainModel()).bipedHead));
/*     */   }
/*     */ 
/*     */   
/*     */   public ModelPlayer getMainModel() {
/*  44 */     return (ModelPlayer)super.getMainModel();
/*     */   }
/*     */ 
/*     */   
/*     */   public void doRender(AbstractClientPlayer entity, double x, double y, double z, float entityYaw, float partialTicks) {
/*  49 */     if (!entity.isUser() || this.renderManager.livingPlayer == entity) {
/*     */       
/*  51 */       double d0 = y;
/*     */       
/*  53 */       if (entity.isSneaking() && !(entity instanceof net.minecraft.client.entity.EntityPlayerSP))
/*     */       {
/*  55 */         d0 = y - 0.125D;
/*     */       }
/*     */       
/*  58 */       setModelVisibilities(entity);
/*  59 */       super.doRender(entity, x, d0, z, entityYaw, partialTicks);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void setModelVisibilities(AbstractClientPlayer clientPlayer) {
/*  65 */     ModelPlayer modelplayer = getMainModel();
/*     */     
/*  67 */     if (clientPlayer.isSpectator()) {
/*     */       
/*  69 */       modelplayer.setInvisible(false);
/*  70 */       modelplayer.bipedHead.showModel = true;
/*  71 */       modelplayer.bipedHeadwear.showModel = true;
/*     */     }
/*     */     else {
/*     */       
/*  75 */       ItemStack itemstack = clientPlayer.inventory.getCurrentItem();
/*  76 */       modelplayer.setInvisible(true);
/*  77 */       modelplayer.bipedHeadwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.HAT);
/*  78 */       modelplayer.bipedBodyWear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.JACKET);
/*  79 */       modelplayer.bipedLeftLegwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.LEFT_PANTS_LEG);
/*  80 */       modelplayer.bipedRightLegwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.RIGHT_PANTS_LEG);
/*  81 */       modelplayer.bipedLeftArmwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.LEFT_SLEEVE);
/*  82 */       modelplayer.bipedRightArmwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.RIGHT_SLEEVE);
/*  83 */       modelplayer.heldItemLeft = 0;
/*  84 */       modelplayer.aimedBow = false;
/*  85 */       modelplayer.isSneak = clientPlayer.isSneaking();
/*     */       
/*  87 */       if (itemstack == null) {
/*     */         
/*  89 */         modelplayer.heldItemRight = 0;
/*     */       }
/*     */       else {
/*     */         
/*  93 */         modelplayer.heldItemRight = 1;
/*     */         
/*  95 */         if (clientPlayer.getItemInUseCount() > 0) {
/*     */           
/*  97 */           EnumAction enumaction = itemstack.getItemUseAction();
/*     */           
/*  99 */           if (enumaction == EnumAction.BLOCK) {
/*     */             
/* 101 */             modelplayer.heldItemRight = 3;
/*     */           }
/* 103 */           else if (enumaction == EnumAction.BOW) {
/*     */             
/* 105 */             modelplayer.aimedBow = true;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected ResourceLocation getEntityTexture(AbstractClientPlayer entity) {
/* 114 */     return entity.getLocationSkin();
/*     */   }
/*     */ 
/*     */   
/*     */   public void transformHeldFull3DItemLayer() {
/* 119 */     GlStateManager.translate(0.0F, 0.1875F, 0.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void preRenderCallback(AbstractClientPlayer entitylivingbaseIn, float partialTickTime) {
/* 124 */     float f = 0.9375F;
/* 125 */     GlStateManager.scale(f, f, f);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void renderOffsetLivingLabel(AbstractClientPlayer entityIn, double x, double y, double z, String str, float p_177069_9_, double p_177069_10_) {
/* 130 */     if (p_177069_10_ < 100.0D) {
/*     */       
/* 132 */       Scoreboard scoreboard = entityIn.getWorldScoreboard();
/* 133 */       ScoreObjective scoreobjective = scoreboard.getObjectiveInDisplaySlot(2);
/*     */       
/* 135 */       if (scoreobjective != null) {
/*     */         
/* 137 */         Score score = scoreboard.getValueFromObjective(entityIn.getName(), scoreobjective);
/* 138 */         renderLivingLabel(entityIn, score.getScorePoints() + " " + scoreobjective.getDisplayName(), x, y, z, 64);
/* 139 */         y += ((getFontRendererFromRenderManager()).FONT_HEIGHT * 1.15F * p_177069_9_);
/*     */       } 
/*     */     } 
/*     */     
/* 143 */     super.renderOffsetLivingLabel(entityIn, x, y, z, str, p_177069_9_, p_177069_10_);
/*     */   }
/*     */ 
/*     */   
/*     */   public void renderRightArm(AbstractClientPlayer clientPlayer) {
/* 148 */     float f = 1.0F;
/* 149 */     GlStateManager.color(f, f, f);
/* 150 */     ModelPlayer modelplayer = getMainModel();
/* 151 */     setModelVisibilities(clientPlayer);
/* 152 */     modelplayer.swingProgress = 0.0F;
/* 153 */     modelplayer.isSneak = false;
/* 154 */     modelplayer.setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, (Entity)clientPlayer);
/* 155 */     modelplayer.renderRightArm();
/*     */   }
/*     */ 
/*     */   
/*     */   public void renderLeftArm(AbstractClientPlayer clientPlayer) {
/* 160 */     float f = 1.0F;
/* 161 */     GlStateManager.color(f, f, f);
/* 162 */     ModelPlayer modelplayer = getMainModel();
/* 163 */     setModelVisibilities(clientPlayer);
/* 164 */     modelplayer.isSneak = false;
/* 165 */     modelplayer.swingProgress = 0.0F;
/* 166 */     modelplayer.setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, (Entity)clientPlayer);
/* 167 */     modelplayer.renderLeftArm();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void renderLivingAt(AbstractClientPlayer entityLivingBaseIn, double x, double y, double z) {
/* 172 */     if (entityLivingBaseIn.isEntityAlive() && entityLivingBaseIn.isPlayerSleeping()) {
/*     */       
/* 174 */       super.renderLivingAt(entityLivingBaseIn, x + entityLivingBaseIn.renderOffsetX, y + entityLivingBaseIn.renderOffsetY, z + entityLivingBaseIn.renderOffsetZ);
/*     */     }
/*     */     else {
/*     */       
/* 178 */       super.renderLivingAt(entityLivingBaseIn, x, y, z);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void rotateCorpse(AbstractClientPlayer bat, float p_77043_2_, float p_77043_3_, float partialTicks) {
/* 184 */     if (bat.isEntityAlive() && bat.isPlayerSleeping()) {
/*     */       
/* 186 */       GlStateManager.rotate(bat.getBedOrientationInDegrees(), 0.0F, 1.0F, 0.0F);
/* 187 */       GlStateManager.rotate(getDeathMaxRotation(bat), 0.0F, 0.0F, 1.0F);
/* 188 */       GlStateManager.rotate(270.0F, 0.0F, 1.0F, 0.0F);
/*     */     }
/*     */     else {
/*     */       
/* 192 */       super.rotateCorpse(bat, p_77043_2_, p_77043_3_, partialTicks);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\renderer\entity\RenderPlayer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */