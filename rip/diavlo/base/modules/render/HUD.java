/*     */ package rip.diavlo.base.modules.render;
/*     */ 
/*     */ import com.google.common.eventbus.Subscribe;
/*     */ import es.diavlo.api.data.UserData;
/*     */ import java.awt.Color;
/*     */ import java.util.ArrayList;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.Gui;
/*     */ import net.minecraft.client.gui.ScaledResolution;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import rip.diavlo.base.Client;
/*     */ import rip.diavlo.base.api.font.CustomFontRenderer;
/*     */ import rip.diavlo.base.api.module.Category;
/*     */ import rip.diavlo.base.api.module.Module;
/*     */ import rip.diavlo.base.api.value.impl.ModeValue;
/*     */ import rip.diavlo.base.events.render.RenderGuiEvent;
/*     */ import rip.diavlo.base.events.render.RenderUtils;
/*     */ import rip.diavlo.base.utils.ColorUtil;
/*     */ import rip.diavlo.base.utils.render.RenderUtil;
/*     */ import rip.diavlo.base.viaversion.vialoadingbase.ViaLoadingBase;
/*     */ 
/*     */ 
/*     */ public class HUD
/*     */   extends Module
/*     */ {
/*     */   private UserData userDiavlo;
/*  28 */   public ModeValue ServerInfo = new ModeValue("Ocultar Server Info", this, new String[] { "No", "Si" });
/*     */   
/*  30 */   public ModeValue PlayerInfo = new ModeValue("Ocultar Player Info", this, new String[] { "No", "Si" });
/*     */   
/*  32 */   public ModeValue RainbowMode = new ModeValue("Rainbow Mode", this, new String[] { "No", "Si" });
/*     */   
/*     */   public HUD() {
/*  35 */     super("HUD", 0, Category.RENDER);
/*  36 */     getValues().add(this.ServerInfo);
/*  37 */     getValues().add(this.PlayerInfo);
/*  38 */     getValues().add(this.RainbowMode);
/*     */   }
/*     */ 
/*     */   
/*     */   @Subscribe
/*     */   public void onRender(RenderGuiEvent event) {
/*  44 */     this.userDiavlo = UserData.getInstance();
/*     */     
/*  46 */     int ColorUsed = (this.RainbowMode.get() == "Si") ? ColorUtil.rainbow(10) : -1;
/*     */ 
/*     */     
/*  49 */     if (mc.gameSettings.showDebugInfo || mc.thePlayer == null)
/*     */       return; 
/*  51 */     ScaledResolution sr = new ScaledResolution(mc);
/*  52 */     CustomFontRenderer font = Client.getInstance().getFontManager().getDefaultFont().size(20);
/*  53 */     CustomFontRenderer fontmini = Client.getInstance().getFontManager().getDefaultFont().size(15);
/*     */     
/*  55 */     RenderUtils.drawEntityOnScreen(sr.getScaledWidth() - 20, sr
/*  56 */         .getScaledHeight() - 20, 25, 0.0F, 0.0F, (EntityLivingBase)(Minecraft.getMinecraft()).thePlayer, false);
/*     */     
/*  58 */     if (this.PlayerInfo.get() == "No") {
/*  59 */       font.drawStringWithShadow((this.RainbowMode.get() == "Si") ? mc.thePlayer.getGameProfile().getName() : ("§c" + mc.thePlayer.getGameProfile().getName()), sr.getScaledWidth() - font.getWidth(mc.thePlayer.getGameProfile().getName()) - 10.0F, (sr.getScaledHeight() - 20), ColorUsed);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  64 */     if (this.ServerInfo.get() == "No") {
/*     */       
/*  66 */       ResourceLocation logo = new ResourceLocation("client/logotipo.png");
/*  67 */       mc.getTextureManager().bindTexture(logo);
/*     */       
/*  69 */       int imageWidth = 48;
/*  70 */       int imageHeight = 48;
/*     */       
/*  72 */       Gui.drawModalRectWithCustomSizedTexture(40, 5, 0.0F, 0.0F, imageWidth, imageHeight, imageWidth, imageHeight);
/*     */       
/*  74 */       if (this.userDiavlo != null) {
/*  75 */         font.drawStringWithShadow("§c§l" + this.userDiavlo.getNickname() + " §8[" + (this.userDiavlo.getRank().equals("VIP") ? "§a§lVIP" : (this.userDiavlo.getRank().equalsIgnoreCase("Owner") ? "§4§lOwner" : ("§b§l" + this.userDiavlo.getRank()))) + "§8]", 15.0F, 55.0F, -1);
/*     */       } else {
/*  77 */         fontmini.drawStringWithShadow((this.RainbowMode.get() == "Si") ? "No has iniciado sesión en Diavlo" : "§4§lNo has iniciado sesión en Diavlo", 5.0F, 55.0F, ColorUsed);
/*     */       } 
/*     */ 
/*     */       
/*  81 */       if (!mc.isSingleplayer()) {
/*     */         
/*  83 */         String gmver = (mc.getCurrentServerData()).gameVersion;
/*     */ 
/*     */         
/*  86 */         double ipwidth = font.getWidth("Server IP: " + (mc.getCurrentServerData()).serverIP);
/*  87 */         double Softwarewidth = font.getWidth("Software: " + ((gmver.length() >= 30) ? gmver.substring(0, 30) : gmver));
/*  88 */         double coordsWidth = font.getWidth("Coords: [ " + mc.thePlayer.getPosition().getX() + " , " + mc.thePlayer.getPosition().getY() + " , " + mc.thePlayer.getPosition().getZ() + " ]");
/*     */         
/*  90 */         double squareWidth = 140.0D;
/*     */         
/*  92 */         RenderUtil.roundedRectangle(1.0D, 75.0D, squareWidth, 65.0D, 2.0D, new Color(54, 52, 52, 98));
/*  93 */         if (this.RainbowMode.get() == "Si") {
/*  94 */           RenderUtil.roundedRectangle(squareWidth, 75.0D, 2.0D, 67.0D, 2.0D, new Color(ColorUtil.rainbow(10)));
/*  95 */           RenderUtil.roundedRectangle(1.0D, 140.0D, squareWidth + 1.0D, 2.0D, 2.0D, new Color(ColorUtil.rainbow(10)));
/*     */         } 
/*     */         
/*  98 */         font.drawStringWithShadow((this.RainbowMode.get() == "Si") ? ("Server IP: " + (mc.getCurrentServerData()).serverIP) : ("§l§fServer IP: §c§l" + (mc.getCurrentServerData()).serverIP), 2.0F, 75.0F, ColorUsed);
/*  99 */         font.drawStringWithShadow((this.RainbowMode.get() == "Si") ? ("Software: " + ((gmver.length() >= 30) ? gmver.substring(0, 30) : gmver)) : ("§l§fSoftware: §c§l" + ((gmver.length() >= 30) ? gmver.substring(0, 30) : gmver)), 2.0F, 85.0F, ColorUsed);
/* 100 */         font.drawStringWithShadow((this.RainbowMode.get() == "Si") ? ("Ping: " + (Minecraft.getMinecraft().getCurrentServerData()).pingToServer) : ("§l§fPing: §c§l" + (!mc.isSingleplayer() ? (String)Long.valueOf((Minecraft.getMinecraft().getCurrentServerData()).pingToServer) : "0") + "ms"), 2.0F, 95.0F, ColorUsed);
/* 101 */         font.drawStringWithShadow((this.RainbowMode.get() == "Si") ? ("FPS: " + Minecraft.getDebugFPS()) : ("§l§fFPS: §c§l" + Minecraft.getDebugFPS()), 2.0F, 105.0F, ColorUsed);
/* 102 */         font.drawStringWithShadow((this.RainbowMode.get() == "Si") ? ("Version: " + ViaLoadingBase.getInstance().getTargetVersion().getName()) : ("§l§fVersion: §c§l" + ViaLoadingBase.getInstance().getTargetVersion().getName()), 2.0F, 115.0F, ColorUsed);
/* 103 */         font.drawStringWithShadow((this.RainbowMode.get() == "Si") ? ("Coords: [ " + mc.thePlayer.getPosition().getX() + " , " + mc.thePlayer.getPosition().getY() + " , " + mc.thePlayer.getPosition().getZ() + " ]") : ("§l§fCoords: §f[ §c§l" + mc.thePlayer.getPosition().getX() + " §f, §c§l" + mc.thePlayer.getPosition().getY() + " §f, §c§l" + mc.thePlayer.getPosition().getZ() + " §f]"), 2.0F, 125.0F, ColorUsed);
/*     */       }
/*     */       else {
/*     */         
/* 107 */         double coordswidth = font.getWidth("Coords: [ " + mc.thePlayer.getPosition().getX() + " , " + mc.thePlayer.getPosition().getY() + " , " + mc.thePlayer.getPosition().getZ() + " ]");
/* 108 */         double squareWidth = 120.0D;
/*     */ 
/*     */         
/* 111 */         RenderUtil.roundedRectangle(1.0D, 83.0D, squareWidth, 38.0D, 2.0D, new Color(54, 52, 52, 98));
/* 112 */         if (this.RainbowMode.get() == "Si") {
/* 113 */           RenderUtil.roundedRectangle(squareWidth - 1.0D, 83.0D, 2.0D, 39.0D, 2.0D, new Color(ColorUtil.rainbow(10)));
/* 114 */           RenderUtil.roundedRectangle(1.0D, 120.0D, squareWidth, 2.0D, 2.0D, new Color(ColorUtil.rainbow(10)));
/*     */         } 
/* 116 */         font.drawStringWithShadow((this.RainbowMode.get() == "Si") ? ("FPS: " + Minecraft.getDebugFPS()) : ("§l§fFPS: §c§l" + Minecraft.getDebugFPS()), 2.0F, 85.0F, ColorUsed);
/* 117 */         font.drawStringWithShadow((this.RainbowMode.get() == "Si") ? ("Version: " + ViaLoadingBase.getInstance().getTargetVersion().getName()) : ("§l§fVersion: §c§l" + ViaLoadingBase.getInstance().getTargetVersion().getName()), 2.0F, 95.0F, ColorUsed);
/* 118 */         font.drawStringWithShadow((this.RainbowMode.get() == "Si") ? ("Coords: [ " + mc.thePlayer.getPosition().getX() + " , " + mc.thePlayer.getPosition().getY() + " , " + mc.thePlayer.getPosition().getZ() + " ]") : ("§l§fCoords: §f[ §c§l" + mc.thePlayer.getPosition().getX() + " §f, §c§l" + mc.thePlayer.getPosition().getY() + " §f, §c§l" + mc.thePlayer.getPosition().getZ() + " §f]"), 2.0F, 105.0F, ColorUsed);
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 125 */     int y = 3;
/* 126 */     ArrayList<Module> sortedModules = (ArrayList<Module>)Client.getInstance().getModuleManager().getModulesSorted(font);
/* 127 */     sortedModules.removeIf(module -> !module.isToggled());
/*     */ 
/*     */     
/* 130 */     for (Module module : sortedModules) {
/* 131 */       int color = ColorUtil.rainbow(y);
/* 132 */       String suffix = (module.getSuffix() != null) ? ((ColorUsed == -1) ? ("§c§l " + module.getSuffix()) : ("  " + module.getSuffix())) : "";
/* 133 */       String name = module.getName() + suffix;
/*     */       
/* 135 */       float xPos = sr.getScaledWidth() - font.getWidth(name) - 6.0F;
/*     */       
/* 137 */       Gui.drawRect(xPos, (y + 0.5F), (xPos + 1.5F + font.getWidth(name)), (y + 0.5F + font.getHeight(name)), -2147483648);
/* 138 */       Gui.drawRect((xPos - 1.0F), (y + 0.5F), (xPos + 1.5F), (y + 0.5F + font.getHeight(name)), (ColorUsed == -1) ? (new Color(209, 19, 38)).getRGB() : color);
/*     */       
/* 140 */       font.drawStringWithShadow(name, xPos + 1.5F + 1.0F, y + 0.5F, (ColorUsed == -1) ? -1 : color);
/* 141 */       y += 12;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\base\modules\render\HUD.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */