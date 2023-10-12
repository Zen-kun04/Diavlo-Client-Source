/*     */ package net.minecraft.client.gui;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.io.File;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.util.ChatComponentText;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import rip.diavlo.base.Client;
/*     */ import rip.diavlo.base.commands.SVSDataCommand;
/*     */ import rip.diavlo.base.utils.FileManager;
/*     */ 
/*     */ 
/*     */ public class GuiNewChat
/*     */   extends Gui
/*     */ {
/*  23 */   private static final Logger logger = LogManager.getLogger();
/*     */   private final Minecraft mc;
/*  25 */   private final List<String> sentMessages = Lists.newArrayList();
/*  26 */   private final List<ChatLine> chatLines = Lists.newArrayList();
/*  27 */   private final List<ChatLine> drawnChatLines = Lists.newArrayList();
/*     */   
/*     */   private int scrollPos;
/*     */   private boolean isScrolled;
/*     */   
/*     */   public GuiNewChat(Minecraft mcIn) {
/*  33 */     this.mc = mcIn;
/*     */   }
/*     */ 
/*     */   
/*     */   public void drawChat(int updateCounter) {
/*  38 */     if (this.mc.gameSettings.chatVisibility != EntityPlayer.EnumChatVisibility.HIDDEN) {
/*     */       
/*  40 */       int i = getLineCount();
/*  41 */       boolean flag = false;
/*  42 */       int j = 0;
/*  43 */       int k = this.drawnChatLines.size();
/*  44 */       float f = this.mc.gameSettings.chatOpacity * 0.9F + 0.1F;
/*     */       
/*  46 */       if (k > 0) {
/*     */         
/*  48 */         if (getChatOpen())
/*     */         {
/*  50 */           flag = true;
/*     */         }
/*     */         
/*  53 */         float f1 = getChatScale();
/*  54 */         int l = MathHelper.ceiling_float_int(getChatWidth() / f1);
/*  55 */         GlStateManager.pushMatrix();
/*  56 */         GlStateManager.translate(2.0F, 20.0F, 0.0F);
/*  57 */         GlStateManager.scale(f1, f1, 1.0F);
/*     */         
/*  59 */         for (int i1 = 0; i1 + this.scrollPos < this.drawnChatLines.size() && i1 < i; i1++) {
/*     */           
/*  61 */           ChatLine chatline = this.drawnChatLines.get(i1 + this.scrollPos);
/*     */           
/*  63 */           if (chatline != null) {
/*     */             
/*  65 */             int j1 = updateCounter - chatline.getUpdatedCounter();
/*     */             
/*  67 */             if (j1 < 200 || flag) {
/*     */               
/*  69 */               double d0 = j1 / 200.0D;
/*  70 */               d0 = 1.0D - d0;
/*  71 */               d0 *= 10.0D;
/*  72 */               d0 = MathHelper.clamp_double(d0, 0.0D, 1.0D);
/*  73 */               d0 *= d0;
/*  74 */               int l1 = (int)(255.0D * d0);
/*     */               
/*  76 */               if (flag)
/*     */               {
/*  78 */                 l1 = 255;
/*     */               }
/*     */               
/*  81 */               l1 = (int)(l1 * f);
/*  82 */               j++;
/*     */               
/*  84 */               if (l1 > 3) {
/*     */                 
/*  86 */                 int i2 = 0;
/*  87 */                 int j2 = -i1 * 9;
/*     */                 
/*  89 */                 String s = chatline.getChatComponent().getFormattedText();
/*  90 */                 GlStateManager.enableBlend();
/*  91 */                 this.mc.fontRendererObj.drawStringWithShadow(s, i2, (j2 - 8), 16777215 + (l1 << 24));
/*  92 */                 GlStateManager.disableAlpha();
/*  93 */                 GlStateManager.disableBlend();
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */         
/*  99 */         if (flag) {
/*     */           
/* 101 */           int k2 = this.mc.fontRendererObj.FONT_HEIGHT;
/* 102 */           GlStateManager.translate(-3.0F, 0.0F, 0.0F);
/* 103 */           int l2 = k * k2 + k;
/* 104 */           int i3 = j * k2 + j;
/* 105 */           int j3 = this.scrollPos * i3 / k;
/* 106 */           int k1 = i3 * i3 / l2;
/*     */           
/* 108 */           if (l2 != i3) {
/*     */             
/* 110 */             int k3 = (j3 > 0) ? 170 : 96;
/* 111 */             int l3 = this.isScrolled ? 13382451 : 3355562;
/* 112 */             drawRect(0.0D, -j3, 2.0D, (-j3 - k1), l3 + (k3 << 24));
/* 113 */             drawRect(2.0D, -j3, 1.0D, (-j3 - k1), 13421772 + (k3 << 24));
/*     */           } 
/*     */         } 
/*     */         
/* 117 */         GlStateManager.popMatrix();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void clearChatMessages() {
/* 124 */     this.drawnChatLines.clear();
/* 125 */     this.chatLines.clear();
/* 126 */     this.sentMessages.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public void printChatMessage(IChatComponent chatComponent) {
/* 131 */     printChatMessageWithOptionalDeletion(chatComponent, 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void printChatMessageWithOptionalDeletion(IChatComponent chatComponent, int chatLineId) {
/* 136 */     if (SVSDataCommand.isGetData) {
/*     */       
/* 138 */       File txt = new File(Client.getClientDir() + "SVS getter data\\" + SVSDataCommand.fileData);
/*     */ 
/*     */       
/*     */       try {
/* 142 */         String name = chatComponent.getUnformattedText().split("Unable to execute \"")[1].split("\" as no matching commands were found.")[0];
/* 143 */         FileManager.write(txt, name);
/*     */       }
/* 145 */       catch (Exception exception) {}
/*     */     } 
/* 147 */     setChatLine(chatComponent, chatLineId, this.mc.ingameGUI.getUpdateCounter(), false);
/* 148 */     logger.info("[CHAT] " + chatComponent.getUnformattedText());
/*     */   }
/*     */ 
/*     */   
/*     */   private void setChatLine(IChatComponent chatComponent, int chatLineId, int updateCounter, boolean displayOnly) {
/* 153 */     if (chatLineId != 0)
/*     */     {
/* 155 */       deleteChatLine(chatLineId);
/*     */     }
/*     */     
/* 158 */     int i = MathHelper.floor_float(getChatWidth() / getChatScale());
/* 159 */     List<IChatComponent> list = GuiUtilRenderComponents.splitText(chatComponent, i, this.mc.fontRendererObj, false, false);
/* 160 */     boolean flag = getChatOpen();
/*     */     
/* 162 */     for (IChatComponent ichatcomponent : list) {
/*     */       
/* 164 */       if (flag && this.scrollPos > 0) {
/*     */         
/* 166 */         this.isScrolled = true;
/* 167 */         scroll(1);
/*     */       } 
/*     */       
/* 170 */       this.drawnChatLines.add(0, new ChatLine(updateCounter, ichatcomponent, chatLineId));
/*     */     } 
/*     */     
/* 173 */     while (this.drawnChatLines.size() > 100)
/*     */     {
/* 175 */       this.drawnChatLines.remove(this.drawnChatLines.size() - 1);
/*     */     }
/*     */     
/* 178 */     if (!displayOnly) {
/*     */       
/* 180 */       this.chatLines.add(0, new ChatLine(updateCounter, chatComponent, chatLineId));
/*     */       
/* 182 */       while (this.chatLines.size() > 100)
/*     */       {
/* 184 */         this.chatLines.remove(this.chatLines.size() - 1);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void refreshChat() {
/* 191 */     this.drawnChatLines.clear();
/* 192 */     resetScroll();
/*     */     
/* 194 */     for (int i = this.chatLines.size() - 1; i >= 0; i--) {
/*     */       
/* 196 */       ChatLine chatline = this.chatLines.get(i);
/* 197 */       setChatLine(chatline.getChatComponent(), chatline.getChatLineID(), chatline.getUpdatedCounter(), true);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public List<String> getSentMessages() {
/* 203 */     return this.sentMessages;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addToSentMessages(String message) {
/* 208 */     if (this.sentMessages.isEmpty() || !((String)this.sentMessages.get(this.sentMessages.size() - 1)).equals(message))
/*     */     {
/* 210 */       this.sentMessages.add(message);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void resetScroll() {
/* 216 */     this.scrollPos = 0;
/* 217 */     this.isScrolled = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void scroll(int amount) {
/* 222 */     this.scrollPos += amount;
/* 223 */     int i = this.drawnChatLines.size();
/*     */     
/* 225 */     if (this.scrollPos > i - getLineCount())
/*     */     {
/* 227 */       this.scrollPos = i - getLineCount();
/*     */     }
/*     */     
/* 230 */     if (this.scrollPos <= 0) {
/*     */       
/* 232 */       this.scrollPos = 0;
/* 233 */       this.isScrolled = false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public IChatComponent getChatComponent(int mouseX, int mouseY) {
/* 239 */     if (!getChatOpen())
/*     */     {
/* 241 */       return null;
/*     */     }
/*     */ 
/*     */     
/* 245 */     ScaledResolution scaledresolution = new ScaledResolution(this.mc);
/* 246 */     int i = scaledresolution.getScaleFactor();
/* 247 */     float f = getChatScale();
/* 248 */     int j = mouseX / i - 3;
/* 249 */     int k = mouseY / i - 27;
/* 250 */     j = MathHelper.floor_float(j / f);
/* 251 */     k = MathHelper.floor_float(k / f);
/*     */     
/* 253 */     if (j >= 0 && k >= 0) {
/*     */       
/* 255 */       int l = Math.min(getLineCount(), this.drawnChatLines.size());
/*     */       
/* 257 */       if (j <= MathHelper.floor_float(getChatWidth() / getChatScale()) && k < this.mc.fontRendererObj.FONT_HEIGHT * l + l) {
/*     */         
/* 259 */         int i1 = k / this.mc.fontRendererObj.FONT_HEIGHT + this.scrollPos;
/*     */         
/* 261 */         if (i1 >= 0 && i1 < this.drawnChatLines.size()) {
/*     */           
/* 263 */           ChatLine chatline = this.drawnChatLines.get(i1);
/* 264 */           int j1 = 0;
/*     */           
/* 266 */           for (IChatComponent ichatcomponent : chatline.getChatComponent()) {
/*     */             
/* 268 */             if (ichatcomponent instanceof ChatComponentText) {
/*     */               
/* 270 */               j1 += this.mc.fontRendererObj.getStringWidth(GuiUtilRenderComponents.func_178909_a(((ChatComponentText)ichatcomponent).getChatComponentText_TextValue(), false));
/*     */               
/* 272 */               if (j1 > j)
/*     */               {
/* 274 */                 return ichatcomponent;
/*     */               }
/*     */             } 
/*     */           } 
/*     */         } 
/*     */         
/* 280 */         return null;
/*     */       } 
/*     */ 
/*     */       
/* 284 */       return null;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 289 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getChatOpen() {
/* 296 */     return this.mc.currentScreen instanceof GuiChat;
/*     */   }
/*     */ 
/*     */   
/*     */   public void deleteChatLine(int id) {
/* 301 */     Iterator<ChatLine> iterator = this.drawnChatLines.iterator();
/*     */     
/* 303 */     while (iterator.hasNext()) {
/*     */       
/* 305 */       ChatLine chatline = iterator.next();
/*     */       
/* 307 */       if (chatline.getChatLineID() == id)
/*     */       {
/* 309 */         iterator.remove();
/*     */       }
/*     */     } 
/*     */     
/* 313 */     iterator = this.chatLines.iterator();
/*     */     
/* 315 */     while (iterator.hasNext()) {
/*     */       
/* 317 */       ChatLine chatline1 = iterator.next();
/*     */       
/* 319 */       if (chatline1.getChatLineID() == id) {
/*     */         
/* 321 */         iterator.remove();
/*     */         break;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int getChatWidth() {
/* 329 */     return calculateChatboxWidth(this.mc.gameSettings.chatWidth);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getChatHeight() {
/* 334 */     return calculateChatboxHeight(getChatOpen() ? this.mc.gameSettings.chatHeightFocused : this.mc.gameSettings.chatHeightUnfocused);
/*     */   }
/*     */ 
/*     */   
/*     */   public float getChatScale() {
/* 339 */     return this.mc.gameSettings.chatScale;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int calculateChatboxWidth(float scale) {
/* 344 */     int i = 320;
/* 345 */     int j = 40;
/* 346 */     return MathHelper.floor_float(scale * (i - j) + j);
/*     */   }
/*     */ 
/*     */   
/*     */   public static int calculateChatboxHeight(float scale) {
/* 351 */     int i = 180;
/* 352 */     int j = 20;
/* 353 */     return MathHelper.floor_float(scale * (i - j) + j);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getLineCount() {
/* 358 */     return getChatHeight() / 9;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\gui\GuiNewChat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */