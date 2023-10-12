/*      */ package net.minecraft.client.gui;
/*      */ import com.google.common.base.Predicate;
/*      */ import com.google.common.collect.Iterables;
/*      */ import com.google.common.collect.Lists;
/*      */ import java.util.Collection;
/*      */ import java.util.List;
/*      */ import java.util.Random;
/*      */ import net.minecraft.block.material.Material;
/*      */ import net.minecraft.client.Minecraft;
/*      */ import net.minecraft.client.renderer.GlStateManager;
/*      */ import net.minecraft.client.renderer.RenderHelper;
/*      */ import net.minecraft.client.renderer.Tessellator;
/*      */ import net.minecraft.client.renderer.WorldRenderer;
/*      */ import net.minecraft.client.renderer.entity.RenderItem;
/*      */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*      */ import net.minecraft.client.renderer.texture.TextureMap;
/*      */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*      */ import net.minecraft.client.resources.I18n;
/*      */ import net.minecraft.entity.Entity;
/*      */ import net.minecraft.entity.EntityLivingBase;
/*      */ import net.minecraft.entity.ai.attributes.IAttributeInstance;
/*      */ import net.minecraft.entity.boss.BossStatus;
/*      */ import net.minecraft.entity.player.EntityPlayer;
/*      */ import net.minecraft.init.Blocks;
/*      */ import net.minecraft.item.Item;
/*      */ import net.minecraft.item.ItemStack;
/*      */ import net.minecraft.potion.Potion;
/*      */ import net.minecraft.scoreboard.Score;
/*      */ import net.minecraft.scoreboard.ScoreObjective;
/*      */ import net.minecraft.scoreboard.ScorePlayerTeam;
/*      */ import net.minecraft.scoreboard.Scoreboard;
/*      */ import net.minecraft.scoreboard.Team;
/*      */ import net.minecraft.src.Config;
/*      */ import net.minecraft.util.BlockPos;
/*      */ import net.minecraft.util.EnumChatFormatting;
/*      */ import net.minecraft.util.FoodStats;
/*      */ import net.minecraft.util.IChatComponent;
/*      */ import net.minecraft.util.MathHelper;
/*      */ import net.minecraft.util.ResourceLocation;
/*      */ import net.minecraft.util.StringUtils;
/*      */ import net.minecraft.world.border.WorldBorder;
/*      */ import rip.diavlo.base.Client;
/*      */ 
/*      */ public class GuiIngame extends Gui {
/*   45 */   private static final ResourceLocation vignetteTexPath = new ResourceLocation("textures/misc/vignette.png");
/*   46 */   private static final ResourceLocation widgetsTexPath = new ResourceLocation("textures/gui/widgets.png");
/*   47 */   private static final ResourceLocation pumpkinBlurTexPath = new ResourceLocation("textures/misc/pumpkinblur.png");
/*   48 */   private final Random rand = new Random();
/*      */   private final Minecraft mc;
/*      */   private final RenderItem itemRenderer;
/*      */   private final GuiNewChat persistantChatGUI;
/*      */   private int updateCounter;
/*   53 */   private String recordPlaying = "";
/*      */   private int recordPlayingUpFor;
/*      */   private boolean recordIsPlaying;
/*   56 */   public float prevVignetteBrightness = 1.0F;
/*      */   private int remainingHighlightTicks;
/*      */   private ItemStack highlightingItemStack;
/*      */   private final GuiOverlayDebug overlayDebug;
/*      */   private final GuiSpectator spectatorGui;
/*      */   private final GuiPlayerTabOverlay overlayPlayerList;
/*      */   private int titlesTimer;
/*   63 */   private String displayedTitle = "";
/*   64 */   private String displayedSubTitle = "";
/*      */   private int titleFadeIn;
/*      */   private int titleDisplayTime;
/*      */   private int titleFadeOut;
/*   68 */   private int playerHealth = 0;
/*   69 */   private int lastPlayerHealth = 0;
/*   70 */   private long lastSystemTime = 0L;
/*   71 */   private long healthUpdateCounter = 0L;
/*      */ 
/*      */   
/*      */   public GuiIngame(Minecraft mcIn) {
/*   75 */     this.mc = mcIn;
/*   76 */     this.itemRenderer = mcIn.getRenderItem();
/*   77 */     this.overlayDebug = new GuiOverlayDebug(mcIn);
/*   78 */     this.spectatorGui = new GuiSpectator(mcIn);
/*   79 */     this.persistantChatGUI = new GuiNewChat(mcIn);
/*   80 */     this.overlayPlayerList = new GuiPlayerTabOverlay(mcIn, this);
/*   81 */     setDefaultTitlesTimes();
/*      */   }
/*      */ 
/*      */   
/*      */   public void setDefaultTitlesTimes() {
/*   86 */     this.titleFadeIn = 10;
/*   87 */     this.titleDisplayTime = 70;
/*   88 */     this.titleFadeOut = 20;
/*      */   }
/*      */ 
/*      */   
/*      */   public void renderGameOverlay(float partialTicks) {
/*   93 */     ScaledResolution scaledresolution = new ScaledResolution(this.mc);
/*   94 */     int i = scaledresolution.getScaledWidth();
/*   95 */     int j = scaledresolution.getScaledHeight();
/*   96 */     this.mc.entityRenderer.setupOverlayRendering();
/*   97 */     GlStateManager.enableBlend();
/*      */     
/*   99 */     if (Config.isVignetteEnabled()) {
/*      */       
/*  101 */       renderVignette(this.mc.thePlayer.getBrightness(partialTicks), scaledresolution);
/*      */     }
/*      */     else {
/*      */       
/*  105 */       GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
/*      */     } 
/*      */     
/*  108 */     ItemStack itemstack = this.mc.thePlayer.inventory.armorItemInSlot(3);
/*      */     
/*  110 */     if (this.mc.gameSettings.thirdPersonView == 0 && itemstack != null && itemstack.getItem() == Item.getItemFromBlock(Blocks.pumpkin))
/*      */     {
/*  112 */       renderPumpkinOverlay(scaledresolution);
/*      */     }
/*      */     
/*  115 */     if (!this.mc.thePlayer.isPotionActive(Potion.confusion)) {
/*      */       
/*  117 */       float f = this.mc.thePlayer.prevTimeInPortal + (this.mc.thePlayer.timeInPortal - this.mc.thePlayer.prevTimeInPortal) * partialTicks;
/*      */       
/*  119 */       if (f > 0.0F)
/*      */       {
/*  121 */         renderPortal(f, scaledresolution);
/*      */       }
/*      */     } 
/*      */     
/*  125 */     if (this.mc.playerController.isSpectator()) {
/*      */       
/*  127 */       this.spectatorGui.renderTooltip(scaledresolution, partialTicks);
/*      */     }
/*      */     else {
/*      */       
/*  131 */       renderTooltip(scaledresolution, partialTicks);
/*      */     } 
/*      */     
/*  134 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/*  135 */     this.mc.getTextureManager().bindTexture(icons);
/*  136 */     GlStateManager.enableBlend();
/*      */     
/*  138 */     if (showCrosshair()) {
/*      */       
/*  140 */       GlStateManager.tryBlendFuncSeparate(775, 769, 1, 0);
/*  141 */       GlStateManager.enableAlpha();
/*  142 */       drawTexturedModalRect(i / 2 - 7, j / 2 - 7, 0, 0, 16, 16);
/*      */     } 
/*      */     
/*  145 */     GlStateManager.enableAlpha();
/*  146 */     GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
/*  147 */     this.mc.mcProfiler.startSection("bossHealth");
/*  148 */     renderBossHealth();
/*  149 */     this.mc.mcProfiler.endSection();
/*      */     
/*  151 */     if (this.mc.playerController.shouldDrawHUD())
/*      */     {
/*  153 */       renderPlayerStats(scaledresolution);
/*      */     }
/*      */     
/*  156 */     GlStateManager.disableBlend();
/*      */     
/*  158 */     if (this.mc.thePlayer.getSleepTimer() > 0) {
/*      */       
/*  160 */       this.mc.mcProfiler.startSection("sleep");
/*  161 */       GlStateManager.disableDepth();
/*  162 */       GlStateManager.disableAlpha();
/*  163 */       int j1 = this.mc.thePlayer.getSleepTimer();
/*  164 */       float f1 = j1 / 100.0F;
/*      */       
/*  166 */       if (f1 > 1.0F)
/*      */       {
/*  168 */         f1 = 1.0F - (j1 - 100) / 10.0F;
/*      */       }
/*      */       
/*  171 */       int k = (int)(220.0F * f1) << 24 | 0x101020;
/*  172 */       drawRect(0.0D, 0.0D, i, j, k);
/*  173 */       GlStateManager.enableAlpha();
/*  174 */       GlStateManager.enableDepth();
/*  175 */       this.mc.mcProfiler.endSection();
/*      */     } 
/*      */     
/*  178 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/*  179 */     int k1 = i / 2 - 91;
/*      */     
/*  181 */     if (this.mc.thePlayer.isRidingHorse()) {
/*      */       
/*  183 */       renderHorseJumpBar(scaledresolution, k1);
/*      */     }
/*  185 */     else if (this.mc.playerController.gameIsSurvivalOrAdventure()) {
/*      */       
/*  187 */       renderExpBar(scaledresolution, k1);
/*      */     } 
/*      */     
/*  190 */     if (this.mc.gameSettings.heldItemTooltips && !this.mc.playerController.isSpectator()) {
/*      */       
/*  192 */       renderSelectedItem(scaledresolution);
/*      */     }
/*  194 */     else if (this.mc.thePlayer.isSpectator()) {
/*      */       
/*  196 */       this.spectatorGui.renderSelectedItem(scaledresolution);
/*      */     } 
/*      */     
/*  199 */     if (this.mc.isDemo())
/*      */     {
/*  201 */       renderDemo(scaledresolution);
/*      */     }
/*      */     
/*  204 */     if (this.mc.gameSettings.showDebugInfo)
/*      */     {
/*  206 */       this.overlayDebug.renderDebugInfo(scaledresolution);
/*      */     }
/*      */     
/*  209 */     if (this.recordPlayingUpFor > 0) {
/*      */       
/*  211 */       this.mc.mcProfiler.startSection("overlayMessage");
/*  212 */       float f2 = this.recordPlayingUpFor - partialTicks;
/*  213 */       int l1 = (int)(f2 * 255.0F / 20.0F);
/*      */       
/*  215 */       if (l1 > 255)
/*      */       {
/*  217 */         l1 = 255;
/*      */       }
/*      */       
/*  220 */       if (l1 > 8) {
/*      */         
/*  222 */         GlStateManager.pushMatrix();
/*  223 */         GlStateManager.translate((i / 2), (j - 68), 0.0F);
/*  224 */         GlStateManager.enableBlend();
/*  225 */         GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
/*  226 */         int l = 16777215;
/*      */         
/*  228 */         if (this.recordIsPlaying)
/*      */         {
/*  230 */           l = MathHelper.hsvToRGB(f2 / 50.0F, 0.7F, 0.6F) & 0xFFFFFF;
/*      */         }
/*      */         
/*  233 */         getFontRenderer().drawString(this.recordPlaying, (-getFontRenderer().getStringWidth(this.recordPlaying) / 2), -4.0D, l + (l1 << 24 & 0xFF000000));
/*  234 */         GlStateManager.disableBlend();
/*  235 */         GlStateManager.popMatrix();
/*      */       } 
/*      */       
/*  238 */       this.mc.mcProfiler.endSection();
/*      */     } 
/*      */     
/*  241 */     if (this.titlesTimer > 0) {
/*      */       
/*  243 */       this.mc.mcProfiler.startSection("titleAndSubtitle");
/*  244 */       float f3 = this.titlesTimer - partialTicks;
/*  245 */       int i2 = 255;
/*      */       
/*  247 */       if (this.titlesTimer > this.titleFadeOut + this.titleDisplayTime) {
/*      */         
/*  249 */         float f4 = (this.titleFadeIn + this.titleDisplayTime + this.titleFadeOut) - f3;
/*  250 */         i2 = (int)(f4 * 255.0F / this.titleFadeIn);
/*      */       } 
/*      */       
/*  253 */       if (this.titlesTimer <= this.titleFadeOut)
/*      */       {
/*  255 */         i2 = (int)(f3 * 255.0F / this.titleFadeOut);
/*      */       }
/*      */       
/*  258 */       i2 = MathHelper.clamp_int(i2, 0, 255);
/*      */       
/*  260 */       if (i2 > 8) {
/*      */         
/*  262 */         GlStateManager.pushMatrix();
/*  263 */         GlStateManager.translate((i / 2), (j / 2), 0.0F);
/*  264 */         GlStateManager.enableBlend();
/*  265 */         GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
/*  266 */         GlStateManager.pushMatrix();
/*  267 */         GlStateManager.scale(4.0F, 4.0F, 4.0F);
/*  268 */         int j2 = i2 << 24 & 0xFF000000;
/*  269 */         getFontRenderer().drawString(this.displayedTitle, (-getFontRenderer().getStringWidth(this.displayedTitle) / 2), -10.0F, 0xFFFFFF | j2, true);
/*  270 */         GlStateManager.popMatrix();
/*  271 */         GlStateManager.pushMatrix();
/*  272 */         GlStateManager.scale(2.0F, 2.0F, 2.0F);
/*  273 */         getFontRenderer().drawString(this.displayedSubTitle, (-getFontRenderer().getStringWidth(this.displayedSubTitle) / 2), 5.0F, 0xFFFFFF | j2, true);
/*  274 */         GlStateManager.popMatrix();
/*  275 */         GlStateManager.disableBlend();
/*  276 */         GlStateManager.popMatrix();
/*      */       } 
/*      */       
/*  279 */       this.mc.mcProfiler.endSection();
/*      */     } 
/*      */     
/*  282 */     Scoreboard scoreboard = this.mc.theWorld.getScoreboard();
/*  283 */     ScoreObjective scoreobjective = null;
/*  284 */     ScorePlayerTeam scoreplayerteam = scoreboard.getPlayersTeam(this.mc.thePlayer.getName());
/*      */     
/*  286 */     if (scoreplayerteam != null) {
/*      */       
/*  288 */       int i1 = scoreplayerteam.getChatFormat().getColorIndex();
/*      */       
/*  290 */       if (i1 >= 0)
/*      */       {
/*  292 */         scoreobjective = scoreboard.getObjectiveInDisplaySlot(3 + i1);
/*      */       }
/*      */     } 
/*      */     
/*  296 */     ScoreObjective scoreobjective1 = (scoreobjective != null) ? scoreobjective : scoreboard.getObjectiveInDisplaySlot(1);
/*      */     
/*  298 */     if (scoreobjective1 != null)
/*      */     {
/*  300 */       renderScoreboard(scoreobjective1, scaledresolution);
/*      */     }
/*      */     
/*  303 */     GlStateManager.enableBlend();
/*  304 */     GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
/*  305 */     GlStateManager.disableAlpha();
/*  306 */     GlStateManager.pushMatrix();
/*  307 */     GlStateManager.translate(0.0F, (j - 48), 0.0F);
/*  308 */     this.mc.mcProfiler.startSection("chat");
/*  309 */     this.persistantChatGUI.drawChat(this.updateCounter);
/*  310 */     this.mc.mcProfiler.endSection();
/*  311 */     GlStateManager.popMatrix();
/*  312 */     scoreobjective1 = scoreboard.getObjectiveInDisplaySlot(0);
/*      */     
/*  314 */     if (this.mc.gameSettings.keyBindPlayerList.isKeyDown() && (!this.mc.isIntegratedServerRunning() || this.mc.thePlayer.sendQueue.getPlayerInfoMap().size() > 1 || scoreobjective1 != null)) {
/*      */       
/*  316 */       this.overlayPlayerList.updatePlayerList(true);
/*  317 */       this.overlayPlayerList.renderPlayerlist(i, scoreboard, scoreobjective1);
/*      */     }
/*      */     else {
/*      */       
/*  321 */       this.overlayPlayerList.updatePlayerList(false);
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  327 */     Client.getInstance().getEventBus().post(new RenderGuiEvent());
/*      */     
/*  329 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/*  330 */     GlStateManager.disableLighting();
/*  331 */     GlStateManager.enableAlpha();
/*      */   }
/*      */ 
/*      */   
/*      */   protected void renderTooltip(ScaledResolution sr, float partialTicks) {
/*  336 */     if (this.mc.getRenderViewEntity() instanceof EntityPlayer) {
/*      */       
/*  338 */       GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/*  339 */       this.mc.getTextureManager().bindTexture(widgetsTexPath);
/*  340 */       EntityPlayer entityplayer = (EntityPlayer)this.mc.getRenderViewEntity();
/*  341 */       int i = sr.getScaledWidth() / 2;
/*  342 */       float f = this.zLevel;
/*  343 */       this.zLevel = -90.0F;
/*  344 */       drawTexturedModalRect(i - 91, sr.getScaledHeight() - 22, 0, 0, 182, 22);
/*  345 */       drawTexturedModalRect(i - 91 - 1 + entityplayer.inventory.currentItem * 20, sr.getScaledHeight() - 22 - 1, 0, 22, 24, 22);
/*  346 */       this.zLevel = f;
/*  347 */       GlStateManager.enableRescaleNormal();
/*  348 */       GlStateManager.enableBlend();
/*  349 */       GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
/*  350 */       RenderHelper.enableGUIStandardItemLighting();
/*      */       
/*  352 */       for (int j = 0; j < 9; j++) {
/*      */         
/*  354 */         int k = sr.getScaledWidth() / 2 - 90 + j * 20 + 2;
/*  355 */         int l = sr.getScaledHeight() - 16 - 3;
/*  356 */         renderHotbarItem(j, k, l, partialTicks, entityplayer);
/*      */       } 
/*      */       
/*  359 */       RenderHelper.disableStandardItemLighting();
/*  360 */       GlStateManager.disableRescaleNormal();
/*  361 */       GlStateManager.disableBlend();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void renderHorseJumpBar(ScaledResolution scaledRes, int x) {
/*  367 */     this.mc.mcProfiler.startSection("jumpBar");
/*  368 */     this.mc.getTextureManager().bindTexture(Gui.icons);
/*  369 */     float f = this.mc.thePlayer.getHorseJumpPower();
/*  370 */     int i = 182;
/*  371 */     int j = (int)(f * (i + 1));
/*  372 */     int k = scaledRes.getScaledHeight() - 32 + 3;
/*  373 */     drawTexturedModalRect(x, k, 0, 84, i, 5);
/*      */     
/*  375 */     if (j > 0)
/*      */     {
/*  377 */       drawTexturedModalRect(x, k, 0, 89, j, 5);
/*      */     }
/*      */     
/*  380 */     this.mc.mcProfiler.endSection();
/*      */   }
/*      */ 
/*      */   
/*      */   public void renderExpBar(ScaledResolution scaledRes, int x) {
/*  385 */     this.mc.mcProfiler.startSection("expBar");
/*  386 */     this.mc.getTextureManager().bindTexture(Gui.icons);
/*  387 */     int i = this.mc.thePlayer.xpBarCap();
/*      */     
/*  389 */     if (i > 0) {
/*      */       
/*  391 */       int j = 182;
/*  392 */       int k = (int)(this.mc.thePlayer.experience * (j + 1));
/*  393 */       int l = scaledRes.getScaledHeight() - 32 + 3;
/*  394 */       drawTexturedModalRect(x, l, 0, 64, j, 5);
/*      */       
/*  396 */       if (k > 0)
/*      */       {
/*  398 */         drawTexturedModalRect(x, l, 0, 69, k, 5);
/*      */       }
/*      */     } 
/*      */     
/*  402 */     this.mc.mcProfiler.endSection();
/*      */     
/*  404 */     if (this.mc.thePlayer.experienceLevel > 0) {
/*      */       
/*  406 */       this.mc.mcProfiler.startSection("expLevel");
/*  407 */       int k1 = 8453920;
/*      */       
/*  409 */       if (Config.isCustomColors())
/*      */       {
/*  411 */         k1 = CustomColors.getExpBarTextColor(k1);
/*      */       }
/*      */       
/*  414 */       String s = "" + this.mc.thePlayer.experienceLevel;
/*  415 */       int l1 = (scaledRes.getScaledWidth() - getFontRenderer().getStringWidth(s)) / 2;
/*  416 */       int i1 = scaledRes.getScaledHeight() - 31 - 4;
/*  417 */       int j1 = 0;
/*  418 */       getFontRenderer().drawString(s, (l1 + 1), i1, 0);
/*  419 */       getFontRenderer().drawString(s, (l1 - 1), i1, 0);
/*  420 */       getFontRenderer().drawString(s, l1, (i1 + 1), 0);
/*  421 */       getFontRenderer().drawString(s, l1, (i1 - 1), 0);
/*  422 */       getFontRenderer().drawString(s, l1, i1, k1);
/*  423 */       this.mc.mcProfiler.endSection();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void renderSelectedItem(ScaledResolution scaledRes) {
/*  429 */     this.mc.mcProfiler.startSection("selectedItemName");
/*      */     
/*  431 */     if (this.remainingHighlightTicks > 0 && this.highlightingItemStack != null) {
/*      */       
/*  433 */       String s = this.highlightingItemStack.getDisplayName();
/*      */       
/*  435 */       if (this.highlightingItemStack.hasDisplayName())
/*      */       {
/*  437 */         s = EnumChatFormatting.ITALIC + s;
/*      */       }
/*      */       
/*  440 */       int i = (scaledRes.getScaledWidth() - getFontRenderer().getStringWidth(s)) / 2;
/*  441 */       int j = scaledRes.getScaledHeight() - 59;
/*      */       
/*  443 */       if (!this.mc.playerController.shouldDrawHUD())
/*      */       {
/*  445 */         j += 14;
/*      */       }
/*      */       
/*  448 */       int k = (int)(this.remainingHighlightTicks * 256.0F / 10.0F);
/*      */       
/*  450 */       if (k > 255)
/*      */       {
/*  452 */         k = 255;
/*      */       }
/*      */       
/*  455 */       if (k > 0) {
/*      */         
/*  457 */         GlStateManager.pushMatrix();
/*  458 */         GlStateManager.enableBlend();
/*  459 */         GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
/*  460 */         getFontRenderer().drawStringWithShadow(s, i, j, 16777215 + (k << 24));
/*  461 */         GlStateManager.disableBlend();
/*  462 */         GlStateManager.popMatrix();
/*      */       } 
/*      */     } 
/*      */     
/*  466 */     this.mc.mcProfiler.endSection();
/*      */   }
/*      */ 
/*      */   
/*      */   public void renderDemo(ScaledResolution scaledRes) {
/*  471 */     this.mc.mcProfiler.startSection("demo");
/*  472 */     String s = "";
/*      */     
/*  474 */     if (this.mc.theWorld.getTotalWorldTime() >= 120500L) {
/*      */       
/*  476 */       s = I18n.format("demo.demoExpired", new Object[0]);
/*      */     }
/*      */     else {
/*      */       
/*  480 */       s = I18n.format("demo.remainingTime", new Object[] { StringUtils.ticksToElapsedTime((int)(120500L - this.mc.theWorld.getTotalWorldTime())) });
/*      */     } 
/*      */     
/*  483 */     int i = getFontRenderer().getStringWidth(s);
/*  484 */     getFontRenderer().drawStringWithShadow(s, (scaledRes.getScaledWidth() - i - 10), 5.0F, 16777215);
/*  485 */     this.mc.mcProfiler.endSection();
/*      */   }
/*      */ 
/*      */   
/*      */   protected boolean showCrosshair() {
/*  490 */     if (this.mc.gameSettings.showDebugInfo && !this.mc.thePlayer.hasReducedDebug() && !this.mc.gameSettings.reducedDebugInfo)
/*      */     {
/*  492 */       return false;
/*      */     }
/*  494 */     if (this.mc.playerController.isSpectator()) {
/*      */       
/*  496 */       if (this.mc.pointedEntity != null)
/*      */       {
/*  498 */         return true;
/*      */       }
/*      */ 
/*      */       
/*  502 */       if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
/*      */         
/*  504 */         BlockPos blockpos = this.mc.objectMouseOver.getBlockPos();
/*      */         
/*  506 */         if (this.mc.theWorld.getTileEntity(blockpos) instanceof net.minecraft.inventory.IInventory)
/*      */         {
/*  508 */           return true;
/*      */         }
/*      */       } 
/*      */       
/*  512 */       return false;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  517 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void renderScoreboard(ScoreObjective objective, ScaledResolution scaledRes) {
/*  523 */     Scoreboard scoreboard = objective.getScoreboard();
/*  524 */     Collection<Score> collection = scoreboard.getSortedScores(objective);
/*  525 */     List<Score> list = Lists.newArrayList(Iterables.filter(collection, new Predicate<Score>()
/*      */           {
/*      */             public boolean apply(Score p_apply_1_)
/*      */             {
/*  529 */               return (p_apply_1_.getPlayerName() != null && !p_apply_1_.getPlayerName().startsWith("#"));
/*      */             }
/*      */           }));
/*      */     
/*  533 */     if (list.size() > 15) {
/*      */       
/*  535 */       collection = Lists.newArrayList(Iterables.skip(list, collection.size() - 15));
/*      */     }
/*      */     else {
/*      */       
/*  539 */       collection = list;
/*      */     } 
/*      */     
/*  542 */     int i = getFontRenderer().getStringWidth(objective.getDisplayName());
/*      */     
/*  544 */     for (Score score : collection) {
/*      */       
/*  546 */       ScorePlayerTeam scoreplayerteam = scoreboard.getPlayersTeam(score.getPlayerName());
/*  547 */       String s = ScorePlayerTeam.formatPlayerName((Team)scoreplayerteam, score.getPlayerName());
/*  548 */       i = Math.max(i, getFontRenderer().getStringWidth(s));
/*      */     } 
/*      */     
/*  551 */     int i1 = collection.size() * (getFontRenderer()).FONT_HEIGHT;
/*  552 */     int j1 = scaledRes.getScaledHeight() / 2 + i1 / 3;
/*  553 */     int k1 = 3;
/*  554 */     int l1 = scaledRes.getScaledWidth() - i - k1;
/*  555 */     int j = 0;
/*      */     
/*  557 */     for (Score score1 : collection) {
/*      */       
/*  559 */       j++;
/*  560 */       ScorePlayerTeam scoreplayerteam1 = scoreboard.getPlayersTeam(score1.getPlayerName());
/*  561 */       String s1 = ScorePlayerTeam.formatPlayerName((Team)scoreplayerteam1, score1.getPlayerName());
/*  562 */       String s2 = EnumChatFormatting.RED + "" + score1.getScorePoints();
/*  563 */       int k = j1 - j * (getFontRenderer()).FONT_HEIGHT;
/*  564 */       int l = scaledRes.getScaledWidth() - k1 + 2;
/*  565 */       drawRect((l1 - 2), k, l, (k + (getFontRenderer()).FONT_HEIGHT), 1342177280);
/*  566 */       getFontRenderer().drawStringWithShadow(s1, l1, k, 553648127);
/*      */ 
/*      */       
/*  569 */       if (j == collection.size()) {
/*      */         
/*  571 */         String s3 = objective.getDisplayName();
/*  572 */         drawRect((l1 - 2), (k - (getFontRenderer()).FONT_HEIGHT - 1), l, (k - 1), 1610612736);
/*  573 */         drawRect((l1 - 2), (k - 1), l, k, 1342177280);
/*  574 */         getFontRenderer().drawStringWithShadow(s3, (l1 + i / 2 - getFontRenderer().getStringWidth(s3) / 2), (k - (getFontRenderer()).FONT_HEIGHT), 553648127);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void renderPlayerStats(ScaledResolution scaledRes) {
/*  581 */     if (this.mc.getRenderViewEntity() instanceof EntityPlayer) {
/*      */       
/*  583 */       EntityPlayer entityplayer = (EntityPlayer)this.mc.getRenderViewEntity();
/*  584 */       int i = MathHelper.ceiling_float_int(entityplayer.getHealth());
/*  585 */       boolean flag = (this.healthUpdateCounter > this.updateCounter && (this.healthUpdateCounter - this.updateCounter) / 3L % 2L == 1L);
/*      */       
/*  587 */       if (i < this.playerHealth && entityplayer.hurtResistantTime > 0) {
/*      */         
/*  589 */         this.lastSystemTime = Minecraft.getSystemTime();
/*  590 */         this.healthUpdateCounter = (this.updateCounter + 20);
/*      */       }
/*  592 */       else if (i > this.playerHealth && entityplayer.hurtResistantTime > 0) {
/*      */         
/*  594 */         this.lastSystemTime = Minecraft.getSystemTime();
/*  595 */         this.healthUpdateCounter = (this.updateCounter + 10);
/*      */       } 
/*      */       
/*  598 */       if (Minecraft.getSystemTime() - this.lastSystemTime > 1000L) {
/*      */         
/*  600 */         this.playerHealth = i;
/*  601 */         this.lastPlayerHealth = i;
/*  602 */         this.lastSystemTime = Minecraft.getSystemTime();
/*      */       } 
/*      */       
/*  605 */       this.playerHealth = i;
/*  606 */       int j = this.lastPlayerHealth;
/*  607 */       this.rand.setSeed((this.updateCounter * 312871));
/*  608 */       boolean flag1 = false;
/*  609 */       FoodStats foodstats = entityplayer.getFoodStats();
/*  610 */       int k = foodstats.getFoodLevel();
/*  611 */       int l = foodstats.getPrevFoodLevel();
/*  612 */       IAttributeInstance iattributeinstance = entityplayer.getEntityAttribute(SharedMonsterAttributes.maxHealth);
/*  613 */       int i1 = scaledRes.getScaledWidth() / 2 - 91;
/*  614 */       int j1 = scaledRes.getScaledWidth() / 2 + 91;
/*  615 */       int k1 = scaledRes.getScaledHeight() - 39;
/*  616 */       float f = (float)iattributeinstance.getAttributeValue();
/*  617 */       float f1 = entityplayer.getAbsorptionAmount();
/*  618 */       int l1 = MathHelper.ceiling_float_int((f + f1) / 2.0F / 10.0F);
/*  619 */       int i2 = Math.max(10 - l1 - 2, 3);
/*  620 */       int j2 = k1 - (l1 - 1) * i2 - 10;
/*  621 */       float f2 = f1;
/*  622 */       int k2 = entityplayer.getTotalArmorValue();
/*  623 */       int l2 = -1;
/*      */       
/*  625 */       if (entityplayer.isPotionActive(Potion.regeneration))
/*      */       {
/*  627 */         l2 = this.updateCounter % MathHelper.ceiling_float_int(f + 5.0F);
/*      */       }
/*      */       
/*  630 */       this.mc.mcProfiler.startSection("armor");
/*      */       
/*  632 */       for (int i3 = 0; i3 < 10; i3++) {
/*      */         
/*  634 */         if (k2 > 0) {
/*      */           
/*  636 */           int j3 = i1 + i3 * 8;
/*      */           
/*  638 */           if (i3 * 2 + 1 < k2)
/*      */           {
/*  640 */             drawTexturedModalRect(j3, j2, 34, 9, 9, 9);
/*      */           }
/*      */           
/*  643 */           if (i3 * 2 + 1 == k2)
/*      */           {
/*  645 */             drawTexturedModalRect(j3, j2, 25, 9, 9, 9);
/*      */           }
/*      */           
/*  648 */           if (i3 * 2 + 1 > k2)
/*      */           {
/*  650 */             drawTexturedModalRect(j3, j2, 16, 9, 9, 9);
/*      */           }
/*      */         } 
/*      */       } 
/*      */       
/*  655 */       this.mc.mcProfiler.endStartSection("health");
/*      */       
/*  657 */       for (int i6 = MathHelper.ceiling_float_int((f + f1) / 2.0F) - 1; i6 >= 0; i6--) {
/*      */         
/*  659 */         int j6 = 16;
/*      */         
/*  661 */         if (entityplayer.isPotionActive(Potion.poison)) {
/*      */           
/*  663 */           j6 += 36;
/*      */         }
/*  665 */         else if (entityplayer.isPotionActive(Potion.wither)) {
/*      */           
/*  667 */           j6 += 72;
/*      */         } 
/*      */         
/*  670 */         int k3 = 0;
/*      */         
/*  672 */         if (flag)
/*      */         {
/*  674 */           k3 = 1;
/*      */         }
/*      */         
/*  677 */         int l3 = MathHelper.ceiling_float_int((i6 + 1) / 10.0F) - 1;
/*  678 */         int i4 = i1 + i6 % 10 * 8;
/*  679 */         int j4 = k1 - l3 * i2;
/*      */         
/*  681 */         if (i <= 4)
/*      */         {
/*  683 */           j4 += this.rand.nextInt(2);
/*      */         }
/*      */         
/*  686 */         if (i6 == l2)
/*      */         {
/*  688 */           j4 -= 2;
/*      */         }
/*      */         
/*  691 */         int k4 = 0;
/*      */         
/*  693 */         if (entityplayer.worldObj.getWorldInfo().isHardcoreModeEnabled())
/*      */         {
/*  695 */           k4 = 5;
/*      */         }
/*      */         
/*  698 */         drawTexturedModalRect(i4, j4, 16 + k3 * 9, 9 * k4, 9, 9);
/*      */         
/*  700 */         if (flag) {
/*      */           
/*  702 */           if (i6 * 2 + 1 < j)
/*      */           {
/*  704 */             drawTexturedModalRect(i4, j4, j6 + 54, 9 * k4, 9, 9);
/*      */           }
/*      */           
/*  707 */           if (i6 * 2 + 1 == j)
/*      */           {
/*  709 */             drawTexturedModalRect(i4, j4, j6 + 63, 9 * k4, 9, 9);
/*      */           }
/*      */         } 
/*      */         
/*  713 */         if (f2 <= 0.0F) {
/*      */           
/*  715 */           if (i6 * 2 + 1 < i)
/*      */           {
/*  717 */             drawTexturedModalRect(i4, j4, j6 + 36, 9 * k4, 9, 9);
/*      */           }
/*      */           
/*  720 */           if (i6 * 2 + 1 == i)
/*      */           {
/*  722 */             drawTexturedModalRect(i4, j4, j6 + 45, 9 * k4, 9, 9);
/*      */           }
/*      */         }
/*      */         else {
/*      */           
/*  727 */           if (f2 == f1 && f1 % 2.0F == 1.0F) {
/*      */             
/*  729 */             drawTexturedModalRect(i4, j4, j6 + 153, 9 * k4, 9, 9);
/*      */           }
/*      */           else {
/*      */             
/*  733 */             drawTexturedModalRect(i4, j4, j6 + 144, 9 * k4, 9, 9);
/*      */           } 
/*      */           
/*  736 */           f2 -= 2.0F;
/*      */         } 
/*      */       } 
/*      */       
/*  740 */       Entity entity = entityplayer.ridingEntity;
/*      */       
/*  742 */       if (entity == null) {
/*      */         
/*  744 */         this.mc.mcProfiler.endStartSection("food");
/*      */         
/*  746 */         for (int k6 = 0; k6 < 10; k6++)
/*      */         {
/*  748 */           int j7 = k1;
/*  749 */           int l7 = 16;
/*  750 */           int k8 = 0;
/*      */           
/*  752 */           if (entityplayer.isPotionActive(Potion.hunger)) {
/*      */             
/*  754 */             l7 += 36;
/*  755 */             k8 = 13;
/*      */           } 
/*      */           
/*  758 */           if (entityplayer.getFoodStats().getSaturationLevel() <= 0.0F && this.updateCounter % (k * 3 + 1) == 0)
/*      */           {
/*  760 */             j7 = k1 + this.rand.nextInt(3) - 1;
/*      */           }
/*      */           
/*  763 */           if (flag1)
/*      */           {
/*  765 */             k8 = 1;
/*      */           }
/*      */           
/*  768 */           int j9 = j1 - k6 * 8 - 9;
/*  769 */           drawTexturedModalRect(j9, j7, 16 + k8 * 9, 27, 9, 9);
/*      */           
/*  771 */           if (flag1) {
/*      */             
/*  773 */             if (k6 * 2 + 1 < l)
/*      */             {
/*  775 */               drawTexturedModalRect(j9, j7, l7 + 54, 27, 9, 9);
/*      */             }
/*      */             
/*  778 */             if (k6 * 2 + 1 == l)
/*      */             {
/*  780 */               drawTexturedModalRect(j9, j7, l7 + 63, 27, 9, 9);
/*      */             }
/*      */           } 
/*      */           
/*  784 */           if (k6 * 2 + 1 < k)
/*      */           {
/*  786 */             drawTexturedModalRect(j9, j7, l7 + 36, 27, 9, 9);
/*      */           }
/*      */           
/*  789 */           if (k6 * 2 + 1 == k)
/*      */           {
/*  791 */             drawTexturedModalRect(j9, j7, l7 + 45, 27, 9, 9);
/*      */           }
/*      */         }
/*      */       
/*  795 */       } else if (entity instanceof EntityLivingBase) {
/*      */         
/*  797 */         this.mc.mcProfiler.endStartSection("mountHealth");
/*  798 */         EntityLivingBase entitylivingbase = (EntityLivingBase)entity;
/*  799 */         int i7 = (int)Math.ceil(entitylivingbase.getHealth());
/*  800 */         float f3 = entitylivingbase.getMaxHealth();
/*  801 */         int j8 = (int)(f3 + 0.5F) / 2;
/*      */         
/*  803 */         if (j8 > 30)
/*      */         {
/*  805 */           j8 = 30;
/*      */         }
/*      */         
/*  808 */         int i9 = k1;
/*      */         
/*  810 */         for (int k9 = 0; j8 > 0; k9 += 20) {
/*      */           
/*  812 */           int l4 = Math.min(j8, 10);
/*  813 */           j8 -= l4;
/*      */           
/*  815 */           for (int i5 = 0; i5 < l4; i5++) {
/*      */             
/*  817 */             int j5 = 52;
/*  818 */             int k5 = 0;
/*      */             
/*  820 */             if (flag1)
/*      */             {
/*  822 */               k5 = 1;
/*      */             }
/*      */             
/*  825 */             int l5 = j1 - i5 * 8 - 9;
/*  826 */             drawTexturedModalRect(l5, i9, j5 + k5 * 9, 9, 9, 9);
/*      */             
/*  828 */             if (i5 * 2 + 1 + k9 < i7)
/*      */             {
/*  830 */               drawTexturedModalRect(l5, i9, j5 + 36, 9, 9, 9);
/*      */             }
/*      */             
/*  833 */             if (i5 * 2 + 1 + k9 == i7)
/*      */             {
/*  835 */               drawTexturedModalRect(l5, i9, j5 + 45, 9, 9, 9);
/*      */             }
/*      */           } 
/*      */           
/*  839 */           i9 -= 10;
/*      */         } 
/*      */       } 
/*      */       
/*  843 */       this.mc.mcProfiler.endStartSection("air");
/*      */       
/*  845 */       if (entityplayer.isInsideOfMaterial(Material.water)) {
/*      */         
/*  847 */         int l6 = this.mc.thePlayer.getAir();
/*  848 */         int k7 = MathHelper.ceiling_double_int((l6 - 2) * 10.0D / 300.0D);
/*  849 */         int i8 = MathHelper.ceiling_double_int(l6 * 10.0D / 300.0D) - k7;
/*      */         
/*  851 */         for (int l8 = 0; l8 < k7 + i8; l8++) {
/*      */           
/*  853 */           if (l8 < k7) {
/*      */             
/*  855 */             drawTexturedModalRect(j1 - l8 * 8 - 9, j2, 16, 18, 9, 9);
/*      */           }
/*      */           else {
/*      */             
/*  859 */             drawTexturedModalRect(j1 - l8 * 8 - 9, j2, 25, 18, 9, 9);
/*      */           } 
/*      */         } 
/*      */       } 
/*      */       
/*  864 */       this.mc.mcProfiler.endSection();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void renderBossHealth() {
/*  870 */     if (BossStatus.bossName != null && BossStatus.statusBarTime > 0) {
/*      */       
/*  872 */       BossStatus.statusBarTime--;
/*  873 */       FontRenderer fontrenderer = this.mc.fontRendererObj;
/*  874 */       ScaledResolution scaledresolution = new ScaledResolution(this.mc);
/*  875 */       int i = scaledresolution.getScaledWidth();
/*  876 */       int j = 182;
/*  877 */       int k = i / 2 - j / 2;
/*  878 */       int l = (int)(BossStatus.healthScale * (j + 1));
/*  879 */       int i1 = 12;
/*  880 */       drawTexturedModalRect(k, i1, 0, 74, j, 5);
/*  881 */       drawTexturedModalRect(k, i1, 0, 74, j, 5);
/*      */       
/*  883 */       if (l > 0)
/*      */       {
/*  885 */         drawTexturedModalRect(k, i1, 0, 79, l, 5);
/*      */       }
/*      */       
/*  888 */       String s = BossStatus.bossName;
/*  889 */       getFontRenderer().drawStringWithShadow(s, (i / 2 - getFontRenderer().getStringWidth(s) / 2), (i1 - 10), 16777215);
/*  890 */       GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/*  891 */       this.mc.getTextureManager().bindTexture(icons);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void renderPumpkinOverlay(ScaledResolution scaledRes) {
/*  897 */     GlStateManager.disableDepth();
/*  898 */     GlStateManager.depthMask(false);
/*  899 */     GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
/*  900 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/*  901 */     GlStateManager.disableAlpha();
/*  902 */     this.mc.getTextureManager().bindTexture(pumpkinBlurTexPath);
/*  903 */     Tessellator tessellator = Tessellator.getInstance();
/*  904 */     WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/*  905 */     worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
/*  906 */     worldrenderer.pos(0.0D, scaledRes.getScaledHeight(), -90.0D).tex(0.0D, 1.0D).endVertex();
/*  907 */     worldrenderer.pos(scaledRes.getScaledWidth(), scaledRes.getScaledHeight(), -90.0D).tex(1.0D, 1.0D).endVertex();
/*  908 */     worldrenderer.pos(scaledRes.getScaledWidth(), 0.0D, -90.0D).tex(1.0D, 0.0D).endVertex();
/*  909 */     worldrenderer.pos(0.0D, 0.0D, -90.0D).tex(0.0D, 0.0D).endVertex();
/*  910 */     tessellator.draw();
/*  911 */     GlStateManager.depthMask(true);
/*  912 */     GlStateManager.enableDepth();
/*  913 */     GlStateManager.enableAlpha();
/*  914 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/*      */   }
/*      */ 
/*      */   
/*      */   private void renderVignette(float lightLevel, ScaledResolution scaledRes) {
/*  919 */     if (!Config.isVignetteEnabled()) {
/*      */       
/*  921 */       GlStateManager.enableDepth();
/*  922 */       GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
/*      */     }
/*      */     else {
/*      */       
/*  926 */       lightLevel = 1.0F - lightLevel;
/*  927 */       lightLevel = MathHelper.clamp_float(lightLevel, 0.0F, 1.0F);
/*  928 */       WorldBorder worldborder = this.mc.theWorld.getWorldBorder();
/*  929 */       float f = (float)worldborder.getClosestDistance((Entity)this.mc.thePlayer);
/*  930 */       double d0 = Math.min(worldborder.getResizeSpeed() * worldborder.getWarningTime() * 1000.0D, Math.abs(worldborder.getTargetSize() - worldborder.getDiameter()));
/*  931 */       double d1 = Math.max(worldborder.getWarningDistance(), d0);
/*      */       
/*  933 */       if (f < d1) {
/*      */         
/*  935 */         f = 1.0F - (float)(f / d1);
/*      */       }
/*      */       else {
/*      */         
/*  939 */         f = 0.0F;
/*      */       } 
/*      */       
/*  942 */       this.prevVignetteBrightness = (float)(this.prevVignetteBrightness + (lightLevel - this.prevVignetteBrightness) * 0.01D);
/*  943 */       GlStateManager.disableDepth();
/*  944 */       GlStateManager.depthMask(false);
/*  945 */       GlStateManager.tryBlendFuncSeparate(0, 769, 1, 0);
/*      */       
/*  947 */       if (f > 0.0F) {
/*      */         
/*  949 */         GlStateManager.color(0.0F, f, f, 1.0F);
/*      */       }
/*      */       else {
/*      */         
/*  953 */         GlStateManager.color(this.prevVignetteBrightness, this.prevVignetteBrightness, this.prevVignetteBrightness, 1.0F);
/*      */       } 
/*      */       
/*  956 */       this.mc.getTextureManager().bindTexture(vignetteTexPath);
/*  957 */       Tessellator tessellator = Tessellator.getInstance();
/*  958 */       WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/*  959 */       worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
/*  960 */       worldrenderer.pos(0.0D, scaledRes.getScaledHeight(), -90.0D).tex(0.0D, 1.0D).endVertex();
/*  961 */       worldrenderer.pos(scaledRes.getScaledWidth(), scaledRes.getScaledHeight(), -90.0D).tex(1.0D, 1.0D).endVertex();
/*  962 */       worldrenderer.pos(scaledRes.getScaledWidth(), 0.0D, -90.0D).tex(1.0D, 0.0D).endVertex();
/*  963 */       worldrenderer.pos(0.0D, 0.0D, -90.0D).tex(0.0D, 0.0D).endVertex();
/*  964 */       tessellator.draw();
/*  965 */       GlStateManager.depthMask(true);
/*  966 */       GlStateManager.enableDepth();
/*  967 */       GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/*  968 */       GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void renderPortal(float timeInPortal, ScaledResolution scaledRes) {
/*  974 */     if (timeInPortal < 1.0F) {
/*      */       
/*  976 */       timeInPortal *= timeInPortal;
/*  977 */       timeInPortal *= timeInPortal;
/*  978 */       timeInPortal = timeInPortal * 0.8F + 0.2F;
/*      */     } 
/*      */     
/*  981 */     GlStateManager.disableAlpha();
/*  982 */     GlStateManager.disableDepth();
/*  983 */     GlStateManager.depthMask(false);
/*  984 */     GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
/*  985 */     GlStateManager.color(1.0F, 1.0F, 1.0F, timeInPortal);
/*  986 */     this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
/*  987 */     TextureAtlasSprite textureatlassprite = this.mc.getBlockRendererDispatcher().getBlockModelShapes().getTexture(Blocks.portal.getDefaultState());
/*  988 */     float f = textureatlassprite.getMinU();
/*  989 */     float f1 = textureatlassprite.getMinV();
/*  990 */     float f2 = textureatlassprite.getMaxU();
/*  991 */     float f3 = textureatlassprite.getMaxV();
/*  992 */     Tessellator tessellator = Tessellator.getInstance();
/*  993 */     WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/*  994 */     worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
/*  995 */     worldrenderer.pos(0.0D, scaledRes.getScaledHeight(), -90.0D).tex(f, f3).endVertex();
/*  996 */     worldrenderer.pos(scaledRes.getScaledWidth(), scaledRes.getScaledHeight(), -90.0D).tex(f2, f3).endVertex();
/*  997 */     worldrenderer.pos(scaledRes.getScaledWidth(), 0.0D, -90.0D).tex(f2, f1).endVertex();
/*  998 */     worldrenderer.pos(0.0D, 0.0D, -90.0D).tex(f, f1).endVertex();
/*  999 */     tessellator.draw();
/* 1000 */     GlStateManager.depthMask(true);
/* 1001 */     GlStateManager.enableDepth();
/* 1002 */     GlStateManager.enableAlpha();
/* 1003 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/*      */   }
/*      */ 
/*      */   
/*      */   private void renderHotbarItem(int index, int xPos, int yPos, float partialTicks, EntityPlayer player) {
/* 1008 */     ItemStack itemstack = player.inventory.mainInventory[index];
/*      */     
/* 1010 */     if (itemstack != null) {
/*      */       
/* 1012 */       float f = itemstack.animationsToGo - partialTicks;
/*      */       
/* 1014 */       if (f > 0.0F) {
/*      */         
/* 1016 */         GlStateManager.pushMatrix();
/* 1017 */         float f1 = 1.0F + f / 5.0F;
/* 1018 */         GlStateManager.translate((xPos + 8), (yPos + 12), 0.0F);
/* 1019 */         GlStateManager.scale(1.0F / f1, (f1 + 1.0F) / 2.0F, 1.0F);
/* 1020 */         GlStateManager.translate(-(xPos + 8), -(yPos + 12), 0.0F);
/*      */       } 
/*      */       
/* 1023 */       this.itemRenderer.renderItemAndEffectIntoGUI(itemstack, xPos, yPos);
/*      */       
/* 1025 */       if (f > 0.0F)
/*      */       {
/* 1027 */         GlStateManager.popMatrix();
/*      */       }
/*      */       
/* 1030 */       this.itemRenderer.renderItemOverlays(this.mc.fontRendererObj, itemstack, xPos, yPos);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateTick() {
/* 1036 */     if (this.recordPlayingUpFor > 0)
/*      */     {
/* 1038 */       this.recordPlayingUpFor--;
/*      */     }
/*      */     
/* 1041 */     if (this.titlesTimer > 0) {
/*      */       
/* 1043 */       this.titlesTimer--;
/*      */       
/* 1045 */       if (this.titlesTimer <= 0) {
/*      */         
/* 1047 */         this.displayedTitle = "";
/* 1048 */         this.displayedSubTitle = "";
/*      */       } 
/*      */     } 
/*      */     
/* 1052 */     this.updateCounter++;
/*      */     
/* 1054 */     if (this.mc.thePlayer != null) {
/*      */       
/* 1056 */       ItemStack itemstack = this.mc.thePlayer.inventory.getCurrentItem();
/*      */       
/* 1058 */       if (itemstack == null) {
/*      */         
/* 1060 */         this.remainingHighlightTicks = 0;
/*      */       }
/* 1062 */       else if (this.highlightingItemStack != null && itemstack.getItem() == this.highlightingItemStack.getItem() && ItemStack.areItemStackTagsEqual(itemstack, this.highlightingItemStack) && (itemstack.isItemStackDamageable() || itemstack.getMetadata() == this.highlightingItemStack.getMetadata())) {
/*      */         
/* 1064 */         if (this.remainingHighlightTicks > 0)
/*      */         {
/* 1066 */           this.remainingHighlightTicks--;
/*      */         }
/*      */       }
/*      */       else {
/*      */         
/* 1071 */         this.remainingHighlightTicks = 40;
/*      */       } 
/*      */       
/* 1074 */       this.highlightingItemStack = itemstack;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void setRecordPlayingMessage(String recordName) {
/* 1080 */     setRecordPlaying(I18n.format("record.nowPlaying", new Object[] { recordName }), true);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setRecordPlaying(String message, boolean isPlaying) {
/* 1085 */     this.recordPlaying = message;
/* 1086 */     this.recordPlayingUpFor = 60;
/* 1087 */     this.recordIsPlaying = isPlaying;
/*      */   }
/*      */ 
/*      */   
/*      */   public void displayTitle(String title, String subTitle, int timeFadeIn, int displayTime, int timeFadeOut) {
/* 1092 */     if (title == null && subTitle == null && timeFadeIn < 0 && displayTime < 0 && timeFadeOut < 0) {
/*      */       
/* 1094 */       this.displayedTitle = "";
/* 1095 */       this.displayedSubTitle = "";
/* 1096 */       this.titlesTimer = 0;
/*      */     }
/* 1098 */     else if (title != null) {
/*      */       
/* 1100 */       this.displayedTitle = title;
/* 1101 */       this.titlesTimer = this.titleFadeIn + this.titleDisplayTime + this.titleFadeOut;
/*      */     }
/* 1103 */     else if (subTitle != null) {
/*      */       
/* 1105 */       this.displayedSubTitle = subTitle;
/*      */     }
/*      */     else {
/*      */       
/* 1109 */       if (timeFadeIn >= 0)
/*      */       {
/* 1111 */         this.titleFadeIn = timeFadeIn;
/*      */       }
/*      */       
/* 1114 */       if (displayTime >= 0)
/*      */       {
/* 1116 */         this.titleDisplayTime = displayTime;
/*      */       }
/*      */       
/* 1119 */       if (timeFadeOut >= 0)
/*      */       {
/* 1121 */         this.titleFadeOut = timeFadeOut;
/*      */       }
/*      */       
/* 1124 */       if (this.titlesTimer > 0)
/*      */       {
/* 1126 */         this.titlesTimer = this.titleFadeIn + this.titleDisplayTime + this.titleFadeOut;
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void setRecordPlaying(IChatComponent component, boolean isPlaying) {
/* 1133 */     setRecordPlaying(component.getUnformattedText(), isPlaying);
/*      */   }
/*      */ 
/*      */   
/*      */   public GuiNewChat getChatGUI() {
/* 1138 */     return this.persistantChatGUI;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getUpdateCounter() {
/* 1143 */     return this.updateCounter;
/*      */   }
/*      */ 
/*      */   
/*      */   public FontRenderer getFontRenderer() {
/* 1148 */     return this.mc.fontRendererObj;
/*      */   }
/*      */ 
/*      */   
/*      */   public GuiSpectator getSpectatorGui() {
/* 1153 */     return this.spectatorGui;
/*      */   }
/*      */ 
/*      */   
/*      */   public GuiPlayerTabOverlay getTabList() {
/* 1158 */     return this.overlayPlayerList;
/*      */   }
/*      */ 
/*      */   
/*      */   public void resetPlayersOverlayFooterHeader() {
/* 1163 */     this.overlayPlayerList.resetFooterHeader();
/*      */   }
/*      */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\gui\GuiIngame.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */