/*     */ package net.minecraft.client.gui;
/*     */ 
/*     */ import com.google.common.collect.ComparisonChain;
/*     */ import com.google.common.collect.Ordering;
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.network.NetHandlerPlayClient;
/*     */ import net.minecraft.client.network.NetworkPlayerInfo;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.EnumPlayerModelParts;
/*     */ import net.minecraft.scoreboard.IScoreObjectiveCriteria;
/*     */ import net.minecraft.scoreboard.ScoreObjective;
/*     */ import net.minecraft.scoreboard.ScorePlayerTeam;
/*     */ import net.minecraft.scoreboard.Scoreboard;
/*     */ import net.minecraft.scoreboard.Team;
/*     */ import net.minecraft.util.EnumChatFormatting;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.WorldSettings;
/*     */ import rip.diavlo.base.Client;
/*     */ import rip.diavlo.base.api.font.CustomFontRenderer;
/*     */ 
/*     */ public class GuiPlayerTabOverlay
/*     */   extends Gui {
/*  28 */   public static final Ordering<NetworkPlayerInfo> field_175252_a = Ordering.from(new PlayerComparator());
/*     */   
/*     */   private final Minecraft mc;
/*     */   private final GuiIngame guiIngame;
/*     */   private IChatComponent footer;
/*     */   private IChatComponent header;
/*     */   private long lastTimeOpened;
/*     */   private boolean isBeingRendered;
/*     */   
/*     */   public GuiPlayerTabOverlay(Minecraft mcIn, GuiIngame guiIngameIn) {
/*  38 */     this.mc = mcIn;
/*  39 */     this.guiIngame = guiIngameIn;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getPlayerName(NetworkPlayerInfo networkPlayerInfoIn) {
/*  44 */     return (networkPlayerInfoIn.getDisplayName() != null) ? networkPlayerInfoIn.getDisplayName().getFormattedText() : ScorePlayerTeam.formatPlayerName((Team)networkPlayerInfoIn.getPlayerTeam(), networkPlayerInfoIn.getGameProfile().getName());
/*     */   }
/*     */ 
/*     */   
/*     */   public void updatePlayerList(boolean willBeRendered) {
/*  49 */     if (willBeRendered && !this.isBeingRendered)
/*     */     {
/*  51 */       this.lastTimeOpened = Minecraft.getSystemTime();
/*     */     }
/*     */     
/*  54 */     this.isBeingRendered = willBeRendered;
/*     */   }
/*     */   
/*     */   public List<NetworkPlayerInfo> getTabPlayers() {
/*  58 */     NetHandlerPlayClient nethandlerplayclient = this.mc.thePlayer.sendQueue;
/*  59 */     List<NetworkPlayerInfo> lista = field_175252_a.sortedCopy(nethandlerplayclient.getPlayerInfoMap());
/*  60 */     return lista;
/*     */   }
/*     */ 
/*     */   
/*     */   public void renderPlayerlist(int width, Scoreboard scoreboardIn, ScoreObjective scoreObjectiveIn) {
/*     */     int l;
/*  66 */     NetHandlerPlayClient nethandlerplayclient = this.mc.thePlayer.sendQueue;
/*  67 */     List<NetworkPlayerInfo> list = field_175252_a.sortedCopy(nethandlerplayclient.getPlayerInfoMap());
/*  68 */     int i = 0;
/*  69 */     int j = 0;
/*  70 */     for (NetworkPlayerInfo networkplayerinfo : list) {
/*     */       
/*  72 */       int k = this.mc.fontRendererObj.getStringWidth(getPlayerName(networkplayerinfo));
/*  73 */       i = Math.max(i, k);
/*  74 */       if (scoreObjectiveIn != null && scoreObjectiveIn.getRenderType() != IScoreObjectiveCriteria.EnumRenderType.HEARTS) {
/*     */         
/*  76 */         k = this.mc.fontRendererObj.getStringWidth(" " + scoreboardIn.getValueFromObjective(networkplayerinfo.getGameProfile().getName(), scoreObjectiveIn).getScorePoints());
/*  77 */         j = Math.max(j, k);
/*     */       } 
/*     */     } 
/*  80 */     list = list.subList(0, Math.min(list.size(), 80));
/*  81 */     int l3 = list.size();
/*  82 */     int i4 = l3;
/*     */     
/*     */     int j4;
/*  85 */     for (j4 = 1; i4 > 20; i4 = (l3 + j4 - 1) / j4) {
/*  86 */       j4++;
/*     */     }
/*  88 */     boolean flag = (this.mc.isIntegratedServerRunning() || this.mc.getNetHandler().getNetworkManager().getIsencrypted());
/*     */ 
/*     */     
/*  91 */     if (scoreObjectiveIn != null) {
/*     */       
/*  93 */       if (scoreObjectiveIn.getRenderType() == IScoreObjectiveCriteria.EnumRenderType.HEARTS) {
/*  94 */         l = 90;
/*     */       } else {
/*  96 */         l = j;
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 101 */       l = 0;
/*     */     } 
/* 103 */     int i1 = Math.min(j4 * ((flag ? 9 : 0) + i + l + 13), width - 50) / j4;
/* 104 */     int j1 = width / 2 - (i1 * j4 + (j4 - 1) * 5) / 2;
/* 105 */     int k1 = 10;
/* 106 */     int l1 = i1 * j4 + (j4 - 1) * 5;
/* 107 */     List<String> list1 = null;
/* 108 */     List<String> list2 = null;
/* 109 */     if (this.header != null) {
/*     */       
/* 111 */       list1 = this.mc.fontRendererObj.listFormattedStringToWidth(this.header.getFormattedText(), width - 50);
/* 112 */       for (String s : list1) {
/* 113 */         l1 = Math.max(l1, this.mc.fontRendererObj.getStringWidth(s));
/*     */       }
/*     */     } 
/* 116 */     if (this.footer != null) {
/*     */       
/* 118 */       list2 = this.mc.fontRendererObj.listFormattedStringToWidth(this.footer.getFormattedText(), width - 50);
/* 119 */       for (String s2 : list2) {
/* 120 */         l1 = Math.max(l1, this.mc.fontRendererObj.getStringWidth(s2));
/*     */       }
/*     */     } 
/* 123 */     if (list1 != null) {
/*     */       
/* 125 */       drawRect((width / 2 - l1 / 2 - 1), (k1 - 1), (width / 2 + l1 / 2 + 1), (k1 + list1.size() * this.mc.fontRendererObj.FONT_HEIGHT), -2147483648);
/* 126 */       for (String s3 : list1) {
/*     */         
/* 128 */         int i2 = this.mc.fontRendererObj.getStringWidth(s3);
/* 129 */         this.mc.fontRendererObj.drawStringWithShadow(s3, (width / 2 - i2 / 2), k1, -1);
/* 130 */         k1 += this.mc.fontRendererObj.FONT_HEIGHT;
/*     */       } 
/* 132 */       k1++;
/*     */     } 
/* 134 */     drawRect((width / 2 - l1 / 2 - 1), (k1 - 1), (width / 2 + l1 / 2 + 1), (k1 + i4 * 9), -2147483648);
/* 135 */     for (int k4 = 0; k4 < l3; k4++) {
/*     */       
/* 137 */       int l4 = k4 / i4;
/* 138 */       int i5 = k4 % i4;
/* 139 */       int j2 = j1 + l4 * i1 + l4 * 5;
/* 140 */       int k2 = k1 + i5 * 9;
/* 141 */       drawRect(j2, k2, (j2 + i1), (k2 + 8), 553648127);
/* 142 */       GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 143 */       GlStateManager.enableAlpha();
/* 144 */       GlStateManager.enableBlend();
/* 145 */       GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
/* 146 */       if (k4 < list.size()) {
/*     */         
/* 148 */         NetworkPlayerInfo networkplayerinfo1 = list.get(k4);
/* 149 */         String s1 = getPlayerName(networkplayerinfo1);
/* 150 */         GameProfile gameprofile = networkplayerinfo1.getGameProfile();
/* 151 */         if (flag) {
/*     */           
/* 153 */           EntityPlayer entityplayer = this.mc.theWorld.getPlayerEntityByUUID(gameprofile.getId());
/* 154 */           boolean flag1 = (entityplayer != null && entityplayer.isWearing(EnumPlayerModelParts.CAPE) && (gameprofile.getName().equals("Dinnerbone") || gameprofile.getName().equals("Grumm")));
/* 155 */           this.mc.getTextureManager().bindTexture(networkplayerinfo1.getLocationSkin());
/* 156 */           int l2 = 8 + (flag1 ? 8 : 0);
/* 157 */           int i3 = 8 * (flag1 ? -1 : 1);
/* 158 */           Gui.drawScaledCustomSizeModalRect(j2, k2, 8.0F, l2, 8, i3, 8, 8, 64.0F, 64.0F);
/* 159 */           if (entityplayer != null && entityplayer.isWearing(EnumPlayerModelParts.HAT)) {
/*     */             
/* 161 */             int j3 = 8 + (flag1 ? 8 : 0);
/* 162 */             int k3 = 8 * (flag1 ? -1 : 1);
/* 163 */             Gui.drawScaledCustomSizeModalRect(j2, k2, 40.0F, j3, 8, k3, 8, 8, 64.0F, 64.0F);
/*     */           } 
/* 165 */           j2 += 9;
/*     */         } 
/* 167 */         if (networkplayerinfo1.getGameType() == WorldSettings.GameType.SPECTATOR) {
/*     */           
/* 169 */           s1 = EnumChatFormatting.ITALIC + s1;
/* 170 */           this.mc.fontRendererObj.drawStringWithShadow(s1, j2, k2, -1862270977);
/*     */ 
/*     */ 
/*     */         
/*     */         }
/* 175 */         else if (Client.getInstance().isCreativeOnTab()) {
/*     */           try {
/* 177 */             if (networkplayerinfo1.getGameProfile().getId().equals(this.mc.thePlayer.getGameProfile().getId())) {
/* 178 */               this.mc.fontRendererObj.drawStringWithShadow("§c§l" + networkplayerinfo1.getGameProfile().getName() + " §f [§c" + networkplayerinfo1.getGameType().getName().toUpperCase() + "§f]", j2, k2 - 1.0F, -1);
/*     */             } else {
/* 180 */               this.mc.fontRendererObj.drawStringWithShadow(s1 + "§f [§c" + networkplayerinfo1.getGameType().getName().toUpperCase() + "§f]", j2, k2 - 1.0F, -1);
/*     */             } 
/* 182 */           } catch (Exception e) {
/*     */             try {
/* 184 */               if (networkplayerinfo1.getGameProfile().getId().equals(this.mc.thePlayer.getGameProfile().getId())) {
/* 185 */                 this.mc.fontRendererObj.drawStringWithShadow("§c§l" + networkplayerinfo1.getGameProfile().getName(), j2, k2 - 1.0F, -1);
/*     */               } else {
/*     */                 
/* 188 */                 this.mc.fontRendererObj.drawStringWithShadow(s1, j2, k2 - 1.0F, -1);
/*     */               } 
/* 190 */             } catch (Exception e2) {
/* 191 */               this.mc.fontRendererObj.drawStringWithShadow(s1, j2, k2 - 1.0F, -1);
/*     */             } 
/*     */           } 
/*     */         } else {
/*     */           
/*     */           try {
/* 197 */             if (networkplayerinfo1.getGameProfile().getId().equals(this.mc.thePlayer.getGameProfile().getId())) {
/* 198 */               this.mc.fontRendererObj.drawStringWithShadow("§c§l" + networkplayerinfo1.getGameProfile().getName(), j2, k2 - 1.0F, -1);
/*     */             } else {
/*     */               
/* 201 */               this.mc.fontRendererObj.drawStringWithShadow(s1, j2, k2 - 1.0F, -1);
/*     */             } 
/* 203 */           } catch (Exception e2) {
/* 204 */             this.mc.fontRendererObj.drawStringWithShadow(s1, j2, k2 - 1.0F, -1);
/*     */           } 
/*     */         } 
/*     */         
/* 208 */         if (scoreObjectiveIn != null && networkplayerinfo1.getGameType() != WorldSettings.GameType.SPECTATOR) {
/*     */           
/* 210 */           int k5 = j2 + i + 1;
/* 211 */           int l5 = k5 + l;
/* 212 */           if (l5 - k5 > 5) {
/* 213 */             drawScoreboardValues(scoreObjectiveIn, k2, gameprofile.getName(), k5, l5, networkplayerinfo1);
/*     */           }
/*     */         } 
/* 216 */         drawPing(i1, j2 - (flag ? 9 : 0), k2, networkplayerinfo1);
/*     */       } 
/*     */     } 
/* 219 */     if (list2 != null) {
/*     */       
/* 221 */       k1 = k1 + i4 * 9 + 1;
/* 222 */       drawRect((width / 2 - l1 / 2 - 1), (k1 - 1), (width / 2 + l1 / 2 + 1), (k1 + list2.size() * this.mc.fontRendererObj.FONT_HEIGHT), -2147483648);
/* 223 */       for (String s4 : list2) {
/*     */         
/* 225 */         int j5 = this.mc.fontRendererObj.getStringWidth(s4);
/* 226 */         this.mc.fontRendererObj.drawStringWithShadow(s4, (width / 2 - j5 / 2), k1, -1);
/* 227 */         k1 += this.mc.fontRendererObj.FONT_HEIGHT;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void renderPlayerlist2(int width, Scoreboard scoreboardIn, ScoreObjective scoreObjectiveIn) {
/*     */     int l;
/* 234 */     CustomFontRenderer font = Client.getInstance().getFontManager().getDefaultFont().size(18);
/*     */     
/* 236 */     NetHandlerPlayClient nethandlerplayclient = this.mc.thePlayer.sendQueue;
/* 237 */     List<NetworkPlayerInfo> list = field_175252_a.sortedCopy(nethandlerplayclient.getPlayerInfoMap());
/* 238 */     int i = 0;
/* 239 */     int j = 0;
/*     */     
/* 241 */     for (NetworkPlayerInfo networkplayerinfo : list) {
/*     */       
/* 243 */       int k = this.mc.fontRendererObj.getStringWidth(getPlayerName(networkplayerinfo));
/* 244 */       i = Math.max(i, k);
/*     */       
/* 246 */       if (scoreObjectiveIn != null && scoreObjectiveIn.getRenderType() != IScoreObjectiveCriteria.EnumRenderType.HEARTS) {
/*     */         
/* 248 */         k = this.mc.fontRendererObj.getStringWidth(" " + scoreboardIn.getValueFromObjective(networkplayerinfo.getGameProfile().getName(), scoreObjectiveIn).getScorePoints());
/* 249 */         j = Math.max(j, k);
/*     */       } 
/*     */     } 
/*     */     
/* 253 */     list = list.subList(0, Math.min(list.size(), 80));
/* 254 */     int l3 = list.size();
/* 255 */     int i4 = l3;
/*     */     
/*     */     int j4;
/* 258 */     for (j4 = 1; i4 > 20; i4 = (l3 + j4 - 1) / j4)
/*     */     {
/* 260 */       j4++;
/*     */     }
/*     */     
/* 263 */     boolean flag = (this.mc.isIntegratedServerRunning() || this.mc.getNetHandler().getNetworkManager().getIsencrypted());
/*     */ 
/*     */     
/* 266 */     if (scoreObjectiveIn != null) {
/*     */       
/* 268 */       if (scoreObjectiveIn.getRenderType() == IScoreObjectiveCriteria.EnumRenderType.HEARTS)
/*     */       {
/* 270 */         l = 90;
/*     */       }
/*     */       else
/*     */       {
/* 274 */         l = j;
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 279 */       l = 0;
/*     */     } 
/*     */     
/* 282 */     int i1 = Math.min(j4 * ((flag ? 9 : 0) + i + l + 13), width - 50) / j4;
/* 283 */     int j1 = width / 2 - (i1 * j4 + (j4 - 1) * 5) / 2;
/* 284 */     int k1 = 10;
/* 285 */     int l1 = i1 * j4 + (j4 - 1) * 5;
/* 286 */     List<String> list1 = null;
/* 287 */     List<String> list2 = null;
/*     */     
/* 289 */     if (this.header != null) {
/*     */       
/* 291 */       list1 = this.mc.fontRendererObj.listFormattedStringToWidth(this.header.getFormattedText(), width - 50);
/*     */       
/* 293 */       for (String s : list1)
/*     */       {
/* 295 */         l1 = Math.max(l1, this.mc.fontRendererObj.getStringWidth(s));
/*     */       }
/*     */     } 
/*     */     
/* 299 */     if (this.footer != null) {
/*     */       
/* 301 */       list2 = this.mc.fontRendererObj.listFormattedStringToWidth(this.footer.getFormattedText(), width - 50);
/*     */       
/* 303 */       for (String s2 : list2)
/*     */       {
/* 305 */         l1 = Math.max(l1, this.mc.fontRendererObj.getStringWidth(s2));
/*     */       }
/*     */     } 
/*     */     
/* 309 */     if (list1 != null) {
/*     */       
/* 311 */       drawRect((width / 2 - l1 / 2 - 1), (k1 - 1), (width / 2 + l1 / 2 + 1), (k1 + list1.size() * this.mc.fontRendererObj.FONT_HEIGHT + 5), -2147483648);
/*     */       
/* 313 */       for (String s3 : list1) {
/*     */ 
/*     */ 
/*     */         
/* 317 */         int i2 = this.mc.fontRendererObj.getStringWidth(s3);
/* 318 */         font.drawStringWithShadow(s3, (width / 2 - i2 / 2), k1, -1);
/* 319 */         k1 += this.mc.fontRendererObj.FONT_HEIGHT + 5;
/*     */       } 
/*     */       
/* 322 */       k1++;
/*     */     } 
/*     */     
/* 325 */     drawRect((width / 2 - l1 / 2 - 1), (k1 - 1), (width / 2 + l1 / 2 + 1), (k1 + i4 * 9), -2147483648);
/*     */     
/* 327 */     for (int k4 = 0; k4 < l3; k4++) {
/*     */       
/* 329 */       int l4 = k4 / i4;
/* 330 */       int i5 = k4 % i4;
/* 331 */       int j2 = j1 + l4 * i1 + l4 * 5;
/* 332 */       int k2 = k1 + i5 * 9;
/* 333 */       drawRect(j2, k2, (j2 + i1), (k2 + 8), 553648127);
/* 334 */       GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 335 */       GlStateManager.enableAlpha();
/* 336 */       GlStateManager.enableBlend();
/* 337 */       GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
/*     */       
/* 339 */       if (k4 < list.size()) {
/*     */         
/* 341 */         NetworkPlayerInfo networkplayerinfo1 = list.get(k4);
/* 342 */         String s1 = getPlayerName(networkplayerinfo1);
/* 343 */         GameProfile gameprofile = networkplayerinfo1.getGameProfile();
/*     */         
/* 345 */         if (flag) {
/*     */           
/* 347 */           EntityPlayer entityplayer = this.mc.theWorld.getPlayerEntityByUUID(gameprofile.getId());
/* 348 */           boolean flag1 = (entityplayer != null && entityplayer.isWearing(EnumPlayerModelParts.CAPE) && (gameprofile.getName().equals("Dinnerbone") || gameprofile.getName().equals("Grumm")));
/* 349 */           this.mc.getTextureManager().bindTexture(networkplayerinfo1.getLocationSkin());
/* 350 */           int l2 = 8 + (flag1 ? 8 : 0);
/* 351 */           int i3 = 8 * (flag1 ? -1 : 1);
/* 352 */           Gui.drawScaledCustomSizeModalRect(j2, k2, 8.0F, l2, 8, i3, 8, 8, 64.0F, 64.0F);
/*     */           
/* 354 */           if (entityplayer != null && entityplayer.isWearing(EnumPlayerModelParts.HAT)) {
/*     */             
/* 356 */             int j3 = 8 + (flag1 ? 8 : 0);
/* 357 */             int k3 = 8 * (flag1 ? -1 : 1);
/* 358 */             Gui.drawScaledCustomSizeModalRect(j2, k2, 40.0F, j3, 8, k3, 8, 8, 64.0F, 64.0F);
/*     */           } 
/*     */           
/* 361 */           j2 += 9;
/*     */         } 
/*     */         
/* 364 */         if (networkplayerinfo1.getGameType() == WorldSettings.GameType.SPECTATOR) {
/*     */           
/* 366 */           s1 = EnumChatFormatting.ITALIC + s1;
/* 367 */           font.drawStringWithShadow(s1 + " [" + networkplayerinfo1.getGameType().getName() + " ]", j2, k2 - 1.0F, -1862270977);
/*     */ 
/*     */         
/*     */         }
/* 371 */         else if (Client.getInstance().isCreativeOnTab()) {
/*     */           try {
/* 373 */             if (networkplayerinfo1.getGameProfile().getId().equals(this.mc.thePlayer.getGameProfile().getId())) {
/* 374 */               font.drawStringWithShadow("§c§l" + networkplayerinfo1.getGameProfile().getName() + " §f§l [§c§l" + networkplayerinfo1.getGameType().getName().toUpperCase() + "§f§l]", j2, k2 - 1.0F, -1);
/*     */             } else {
/* 376 */               font.drawStringWithShadow(s1 + "§f§l [§c§l" + networkplayerinfo1.getGameType().getName().toUpperCase() + "§f§l]", j2, k2 - 1.0F, -1);
/*     */             } 
/* 378 */           } catch (Exception e) {
/*     */             try {
/* 380 */               if (networkplayerinfo1.getGameProfile().getId().equals(this.mc.thePlayer.getGameProfile().getId())) {
/* 381 */                 font.drawStringWithShadow("§c§l" + networkplayerinfo1.getGameProfile().getName(), j2, k2 - 1.0F, -1);
/*     */               } else {
/*     */                 
/* 384 */                 font.drawStringWithShadow(s1, j2, k2 - 1.0F, -1);
/*     */               } 
/* 386 */             } catch (Exception e2) {
/* 387 */               font.drawStringWithShadow(s1, j2, k2 - 1.0F, -1);
/*     */             } 
/*     */           } 
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 395 */         if (scoreObjectiveIn != null && networkplayerinfo1.getGameType() != WorldSettings.GameType.SPECTATOR) {
/*     */           
/* 397 */           int k5 = j2 + i + 1;
/* 398 */           int l5 = k5 + l;
/*     */           
/* 400 */           if (l5 - k5 > 5)
/*     */           {
/* 402 */             drawScoreboardValues(scoreObjectiveIn, k2, gameprofile.getName(), k5, l5, networkplayerinfo1);
/*     */           }
/*     */         } 
/*     */         
/* 406 */         drawPing(i1, j2 - (flag ? 9 : 0), k2, networkplayerinfo1);
/*     */       } 
/*     */     } 
/*     */     
/* 410 */     if (list2 != null) {
/*     */       
/* 412 */       k1 = k1 + i4 * 9 + 1;
/* 413 */       drawRect((width / 2 - l1 / 2 - 1), (k1 - 1), (width / 2 + l1 / 2 + 1), (k1 + list2.size() * this.mc.fontRendererObj.FONT_HEIGHT + 5), -2147483648);
/*     */ 
/*     */       
/* 416 */       for (String s4 : list2) {
/*     */         
/* 418 */         int j5 = this.mc.fontRendererObj.getStringWidth(s4);
/* 419 */         font.drawStringWithShadow(s4, (width / 2 - j5 / 2), k1, -1);
/* 420 */         k1 += this.mc.fontRendererObj.FONT_HEIGHT + 5;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void drawPing(int p_175245_1_, int p_175245_2_, int p_175245_3_, NetworkPlayerInfo networkPlayerInfoIn) {
/* 427 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 428 */     this.mc.getTextureManager().bindTexture(icons);
/* 429 */     int i = 0;
/* 430 */     int j = 0;
/*     */     
/* 432 */     if (networkPlayerInfoIn.getResponseTime() < 0) {
/*     */       
/* 434 */       j = 5;
/*     */     }
/* 436 */     else if (networkPlayerInfoIn.getResponseTime() < 150) {
/*     */       
/* 438 */       j = 0;
/*     */     }
/* 440 */     else if (networkPlayerInfoIn.getResponseTime() < 300) {
/*     */       
/* 442 */       j = 1;
/*     */     }
/* 444 */     else if (networkPlayerInfoIn.getResponseTime() < 600) {
/*     */       
/* 446 */       j = 2;
/*     */     }
/* 448 */     else if (networkPlayerInfoIn.getResponseTime() < 1000) {
/*     */       
/* 450 */       j = 3;
/*     */     }
/*     */     else {
/*     */       
/* 454 */       j = 4;
/*     */     } 
/*     */     
/* 457 */     this.zLevel += 100.0F;
/* 458 */     drawTexturedModalRect(p_175245_2_ + p_175245_1_ - 11, p_175245_3_, 0 + i * 10, 176 + j * 8, 10, 8);
/* 459 */     this.zLevel -= 100.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   private void drawScoreboardValues(ScoreObjective p_175247_1_, int p_175247_2_, String p_175247_3_, int p_175247_4_, int p_175247_5_, NetworkPlayerInfo p_175247_6_) {
/* 464 */     int i = p_175247_1_.getScoreboard().getValueFromObjective(p_175247_3_, p_175247_1_).getScorePoints();
/*     */     
/* 466 */     if (p_175247_1_.getRenderType() == IScoreObjectiveCriteria.EnumRenderType.HEARTS) {
/*     */       
/* 468 */       this.mc.getTextureManager().bindTexture(icons);
/*     */       
/* 470 */       if (this.lastTimeOpened == p_175247_6_.func_178855_p())
/*     */       {
/* 472 */         if (i < p_175247_6_.func_178835_l()) {
/*     */           
/* 474 */           p_175247_6_.func_178846_a(Minecraft.getSystemTime());
/* 475 */           p_175247_6_.func_178844_b((this.guiIngame.getUpdateCounter() + 20));
/*     */         }
/* 477 */         else if (i > p_175247_6_.func_178835_l()) {
/*     */           
/* 479 */           p_175247_6_.func_178846_a(Minecraft.getSystemTime());
/* 480 */           p_175247_6_.func_178844_b((this.guiIngame.getUpdateCounter() + 10));
/*     */         } 
/*     */       }
/*     */       
/* 484 */       if (Minecraft.getSystemTime() - p_175247_6_.func_178847_n() > 1000L || this.lastTimeOpened != p_175247_6_.func_178855_p()) {
/*     */         
/* 486 */         p_175247_6_.func_178836_b(i);
/* 487 */         p_175247_6_.func_178857_c(i);
/* 488 */         p_175247_6_.func_178846_a(Minecraft.getSystemTime());
/*     */       } 
/*     */       
/* 491 */       p_175247_6_.func_178843_c(this.lastTimeOpened);
/* 492 */       p_175247_6_.func_178836_b(i);
/* 493 */       int j = MathHelper.ceiling_float_int(Math.max(i, p_175247_6_.func_178860_m()) / 2.0F);
/* 494 */       int k = Math.max(MathHelper.ceiling_float_int((i / 2)), Math.max(MathHelper.ceiling_float_int((p_175247_6_.func_178860_m() / 2)), 10));
/* 495 */       boolean flag = (p_175247_6_.func_178858_o() > this.guiIngame.getUpdateCounter() && (p_175247_6_.func_178858_o() - this.guiIngame.getUpdateCounter()) / 3L % 2L == 1L);
/*     */       
/* 497 */       if (j > 0) {
/*     */         
/* 499 */         float f = Math.min((p_175247_5_ - p_175247_4_ - 4) / k, 9.0F);
/*     */         
/* 501 */         if (f > 3.0F) {
/*     */           
/* 503 */           for (int l = j; l < k; l++)
/*     */           {
/* 505 */             drawTexturedModalRect(p_175247_4_ + l * f, p_175247_2_, flag ? 25 : 16, 0, 9, 9);
/*     */           }
/*     */           
/* 508 */           for (int j1 = 0; j1 < j; j1++)
/*     */           {
/* 510 */             drawTexturedModalRect(p_175247_4_ + j1 * f, p_175247_2_, flag ? 25 : 16, 0, 9, 9);
/*     */             
/* 512 */             if (flag) {
/*     */               
/* 514 */               if (j1 * 2 + 1 < p_175247_6_.func_178860_m())
/*     */               {
/* 516 */                 drawTexturedModalRect(p_175247_4_ + j1 * f, p_175247_2_, 70, 0, 9, 9);
/*     */               }
/*     */               
/* 519 */               if (j1 * 2 + 1 == p_175247_6_.func_178860_m())
/*     */               {
/* 521 */                 drawTexturedModalRect(p_175247_4_ + j1 * f, p_175247_2_, 79, 0, 9, 9);
/*     */               }
/*     */             } 
/*     */             
/* 525 */             if (j1 * 2 + 1 < i)
/*     */             {
/* 527 */               drawTexturedModalRect(p_175247_4_ + j1 * f, p_175247_2_, (j1 >= 10) ? 160 : 52, 0, 9, 9);
/*     */             }
/*     */             
/* 530 */             if (j1 * 2 + 1 == i)
/*     */             {
/* 532 */               drawTexturedModalRect(p_175247_4_ + j1 * f, p_175247_2_, (j1 >= 10) ? 169 : 61, 0, 9, 9);
/*     */             }
/*     */           }
/*     */         
/*     */         } else {
/*     */           
/* 538 */           float f1 = MathHelper.clamp_float(i / 20.0F, 0.0F, 1.0F);
/* 539 */           int i1 = (int)((1.0F - f1) * 255.0F) << 16 | (int)(f1 * 255.0F) << 8;
/* 540 */           String s = "" + (i / 2.0F);
/*     */           
/* 542 */           if (p_175247_5_ - this.mc.fontRendererObj.getStringWidth(s + "hp") >= p_175247_4_)
/*     */           {
/* 544 */             s = s + "hp";
/*     */           }
/*     */           
/* 547 */           this.mc.fontRendererObj.drawStringWithShadow(s, ((p_175247_5_ + p_175247_4_) / 2 - this.mc.fontRendererObj.getStringWidth(s) / 2), p_175247_2_, i1);
/*     */         }
/*     */       
/*     */       } 
/*     */     } else {
/*     */       
/* 553 */       String s1 = EnumChatFormatting.YELLOW + "" + i;
/* 554 */       this.mc.fontRendererObj.drawStringWithShadow(s1, (p_175247_5_ - this.mc.fontRendererObj.getStringWidth(s1)), p_175247_2_, 16777215);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setFooter(IChatComponent footerIn) {
/* 560 */     this.footer = footerIn;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setHeader(IChatComponent headerIn) {
/* 565 */     this.header = headerIn;
/*     */   }
/*     */ 
/*     */   
/*     */   public void resetFooterHeader() {
/* 570 */     this.header = null;
/* 571 */     this.footer = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class PlayerComparator
/*     */     implements Comparator<NetworkPlayerInfo>
/*     */   {
/*     */     public int compare(NetworkPlayerInfo p_compare_1_, NetworkPlayerInfo p_compare_2_) {
/* 582 */       ScorePlayerTeam scoreplayerteam = p_compare_1_.getPlayerTeam();
/* 583 */       ScorePlayerTeam scoreplayerteam1 = p_compare_2_.getPlayerTeam();
/* 584 */       return ComparisonChain.start().compareTrueFirst((p_compare_1_.getGameType() != WorldSettings.GameType.SPECTATOR), (p_compare_2_.getGameType() != WorldSettings.GameType.SPECTATOR)).compare((scoreplayerteam != null) ? scoreplayerteam.getRegisteredName() : "", (scoreplayerteam1 != null) ? scoreplayerteam1.getRegisteredName() : "").compare(p_compare_1_.getGameProfile().getName(), p_compare_2_.getGameProfile().getName()).result();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\gui\GuiPlayerTabOverlay.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */