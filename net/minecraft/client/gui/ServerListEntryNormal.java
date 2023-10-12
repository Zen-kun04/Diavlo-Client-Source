/*     */ package net.minecraft.client.gui;
/*     */ 
/*     */ import com.google.common.base.Charsets;
/*     */ import com.google.common.util.concurrent.ThreadFactoryBuilder;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufInputStream;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.handler.codec.base64.Base64;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.io.InputStream;
/*     */ import java.net.UnknownHostException;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.ScheduledThreadPoolExecutor;
/*     */ import java.util.concurrent.ThreadPoolExecutor;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.multiplayer.ServerData;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.texture.DynamicTexture;
/*     */ import net.minecraft.client.renderer.texture.ITextureObject;
/*     */ import net.minecraft.client.renderer.texture.TextureUtil;
/*     */ import net.minecraft.util.EnumChatFormatting;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import org.apache.commons.lang3.Validate;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import rip.diavlo.base.Client;
/*     */ import rip.diavlo.base.api.font.CustomFontRenderer;
/*     */ 
/*     */ public class ServerListEntryNormal
/*     */   implements GuiListExtended.IGuiListEntry {
/*  31 */   private static final Logger logger = LogManager.getLogger();
/*  32 */   private static final ThreadPoolExecutor field_148302_b = new ScheduledThreadPoolExecutor(5, (new ThreadFactoryBuilder()).setNameFormat("Server Pinger #%d").setDaemon(true).build());
/*  33 */   private static final ResourceLocation UNKNOWN_SERVER = new ResourceLocation("textures/misc/unknown_server.png"); private final GuiMultiplayer owner; private final Minecraft mc;
/*  34 */   private static final ResourceLocation SERVER_SELECTION_BUTTONS = new ResourceLocation("textures/gui/server_selection.png"); private ServerData server;
/*     */   
/*     */   public void setServer(ServerData server) {
/*  37 */     this.server = server;
/*     */   }
/*     */   private final ResourceLocation serverIcon;
/*     */   private String field_148299_g;
/*     */   private DynamicTexture field_148305_h;
/*     */   private long field_148298_f;
/*     */   
/*     */   protected ServerListEntryNormal(GuiMultiplayer p_i45048_1_, ServerData serverIn) {
/*  45 */     this.owner = p_i45048_1_;
/*  46 */     this.server = serverIn;
/*  47 */     this.mc = Minecraft.getMinecraft();
/*  48 */     this.serverIcon = new ResourceLocation("servers/" + serverIn.serverIP + "/icon");
/*  49 */     this.field_148305_h = (DynamicTexture)this.mc.getTextureManager().getTexture(this.serverIcon);
/*     */   }
/*     */   
/*     */   public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected) {
/*     */     int l;
/*     */     String s1;
/*  55 */     CustomFontRenderer font = Client.getInstance().getFontManager().getDefaultFont().size(18);
/*     */     
/*  57 */     if (!this.server.field_78841_f) {
/*     */       
/*  59 */       this.server.field_78841_f = true;
/*  60 */       this.server.pingToServer = -2L;
/*  61 */       this.server.serverMOTD = "";
/*  62 */       this.server.populationInfo = "";
/*  63 */       field_148302_b.submit(new Runnable()
/*     */           {
/*     */             
/*     */             public void run()
/*     */             {
/*     */               try {
/*  69 */                 ServerListEntryNormal.this.owner.getOldServerPinger().ping(ServerListEntryNormal.this.server);
/*     */               }
/*  71 */               catch (UnknownHostException var2) {
/*     */                 
/*  73 */                 ServerListEntryNormal.this.server.pingToServer = -1L;
/*  74 */                 ServerListEntryNormal.this.server.serverMOTD = EnumChatFormatting.DARK_RED + "No se pudo encontrar ese servidor.";
/*     */               }
/*  76 */               catch (Exception var3) {
/*     */                 
/*  78 */                 ServerListEntryNormal.this.server.pingToServer = -1L;
/*  79 */                 ServerListEntryNormal.this.server.serverMOTD = EnumChatFormatting.DARK_RED + "No se puede conectar al servidor.";
/*     */               } 
/*     */             }
/*     */           });
/*     */     } 
/*     */ 
/*     */     
/*  86 */     boolean flag = (this.server.version > 47);
/*  87 */     boolean flag1 = (this.server.version < 47);
/*  88 */     boolean flag2 = (flag || flag1);
/*  89 */     this.mc.fontRendererObj.drawStringWithShadow(this.server.serverName, (x + 32 + 3), (y + 1), 16777215);
/*  90 */     List<String> list = this.mc.fontRendererObj.listFormattedStringToWidth(this.server.serverMOTD, listWidth - 32 - 2);
/*     */     
/*  92 */     if (this.server.pingToServer != -1L && this.server.pingToServer != -2L) {
/*  93 */       font.drawStringWithShadow("§fVersion§8: §7" + this.server.gameVersion.replaceAll("git:", "")
/*  94 */           .replaceAll("Bootstrap", "")
/*  95 */           .replaceAll("SNAPSHOT", "")
/*  96 */           .replaceAll("unkown", ""), (x + 312), (y + 2), -1);
/*  97 */       font.drawStringWithShadow("§fPing§8: §7" + this.server.pingToServer + "ms", (x + 312), (y + 12), -1);
/*  98 */       font.drawStringWithShadow("§fPlayers§8: §7" + this.server.populationInfo, (x + 312), (y + 22), -1);
/*     */     } 
/*     */     
/* 101 */     for (int i = 0; i < Math.min(list.size(), 2); i++)
/*     */     {
/* 103 */       this.mc.fontRendererObj.drawStringWithShadow(list.get(i), (x + 32 + 3), (y + 12 + this.mc.fontRendererObj.FONT_HEIGHT * i), 8421504);
/*     */     }
/*     */     
/* 106 */     String s2 = flag2 ? (EnumChatFormatting.DARK_RED + this.server.gameVersion) : this.server.populationInfo;
/* 107 */     int j = this.mc.fontRendererObj.getStringWidth(s2);
/* 108 */     this.mc.fontRendererObj.drawStringWithShadow(s2, (x + listWidth - j - 15 - 2), (y + 1), 8421504);
/* 109 */     int k = 0;
/* 110 */     String s = null;
/*     */ 
/*     */ 
/*     */     
/* 114 */     if (flag2) {
/*     */       
/* 116 */       l = 5;
/* 117 */       s1 = flag ? "Client out of date!" : "Server out of date!";
/* 118 */       s = this.server.playerList;
/*     */     }
/* 120 */     else if (this.server.field_78841_f && this.server.pingToServer != -2L) {
/*     */       
/* 122 */       if (this.server.pingToServer < 0L) {
/*     */         
/* 124 */         l = 5;
/*     */       }
/* 126 */       else if (this.server.pingToServer < 150L) {
/*     */         
/* 128 */         l = 0;
/*     */       }
/* 130 */       else if (this.server.pingToServer < 300L) {
/*     */         
/* 132 */         l = 1;
/*     */       }
/* 134 */       else if (this.server.pingToServer < 600L) {
/*     */         
/* 136 */         l = 2;
/*     */       }
/* 138 */       else if (this.server.pingToServer < 1000L) {
/*     */         
/* 140 */         l = 3;
/*     */       }
/*     */       else {
/*     */         
/* 144 */         l = 4;
/*     */       } 
/*     */       
/* 147 */       if (this.server.pingToServer < 0L)
/*     */       {
/* 149 */         s1 = "(no connection)";
/*     */       }
/*     */       else
/*     */       {
/* 153 */         s1 = this.server.pingToServer + "ms";
/* 154 */         s = this.server.playerList;
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 159 */       k = 1;
/* 160 */       l = (int)(Minecraft.getSystemTime() / 100L + (slotIndex * 2) & 0x7L);
/*     */       
/* 162 */       if (l > 4)
/*     */       {
/* 164 */         l = 8 - l;
/*     */       }
/*     */       
/* 167 */       s1 = "Pinging...";
/*     */     } 
/*     */     
/* 170 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 171 */     this.mc.getTextureManager().bindTexture(Gui.icons);
/* 172 */     Gui.drawModalRectWithCustomSizedTexture(x + listWidth - 15, y, (k * 10), (176 + l * 8), 10, 8, 256.0F, 256.0F);
/*     */     
/* 174 */     if (this.server.getBase64EncodedIconData() != null && !this.server.getBase64EncodedIconData().equals(this.field_148299_g)) {
/*     */       
/* 176 */       this.field_148299_g = this.server.getBase64EncodedIconData();
/* 177 */       prepareServerIcon();
/* 178 */       this.owner.getServerList().saveServerList();
/*     */     } 
/*     */     
/* 181 */     if (this.field_148305_h != null) {
/*     */       
/* 183 */       drawTextureAt(x, y, this.serverIcon);
/*     */     }
/*     */     else {
/*     */       
/* 187 */       drawTextureAt(x, y, UNKNOWN_SERVER);
/*     */     } 
/*     */     
/* 190 */     int i1 = mouseX - x;
/* 191 */     int j1 = mouseY - y;
/*     */     
/* 193 */     if (i1 >= listWidth - 15 && i1 <= listWidth - 5 && j1 >= 0 && j1 <= 8) {
/*     */       
/* 195 */       this.owner.setHoveringText(s1);
/*     */     }
/* 197 */     else if (i1 >= listWidth - j - 15 - 2 && i1 <= listWidth - 15 - 2 && j1 >= 0 && j1 <= 8) {
/*     */       
/* 199 */       this.owner.setHoveringText(s);
/*     */     } 
/*     */     
/* 202 */     if (this.mc.gameSettings.touchscreen || isSelected) {
/*     */       
/* 204 */       this.mc.getTextureManager().bindTexture(SERVER_SELECTION_BUTTONS);
/* 205 */       Gui.drawRect(x, y, (x + 32), (y + 32), -1601138544);
/* 206 */       GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 207 */       int k1 = mouseX - x;
/* 208 */       int l1 = mouseY - y;
/*     */       
/* 210 */       if (func_178013_b())
/*     */       {
/* 212 */         if (k1 < 32 && k1 > 16) {
/*     */           
/* 214 */           Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0F, 32.0F, 32, 32, 256.0F, 256.0F);
/*     */         }
/*     */         else {
/*     */           
/* 218 */           Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0F, 0.0F, 32, 32, 256.0F, 256.0F);
/*     */         } 
/*     */       }
/*     */       
/* 222 */       if (this.owner.func_175392_a(this, slotIndex))
/*     */       {
/* 224 */         if (k1 < 16 && l1 < 16) {
/*     */           
/* 226 */           Gui.drawModalRectWithCustomSizedTexture(x, y, 96.0F, 32.0F, 32, 32, 256.0F, 256.0F);
/*     */         }
/*     */         else {
/*     */           
/* 230 */           Gui.drawModalRectWithCustomSizedTexture(x, y, 96.0F, 0.0F, 32, 32, 256.0F, 256.0F);
/*     */         } 
/*     */       }
/*     */       
/* 234 */       if (this.owner.func_175394_b(this, slotIndex))
/*     */       {
/* 236 */         if (k1 < 16 && l1 > 16) {
/*     */           
/* 238 */           Gui.drawModalRectWithCustomSizedTexture(x, y, 64.0F, 32.0F, 32, 32, 256.0F, 256.0F);
/*     */         }
/*     */         else {
/*     */           
/* 242 */           Gui.drawModalRectWithCustomSizedTexture(x, y, 64.0F, 0.0F, 32, 32, 256.0F, 256.0F);
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void drawTextureAt(int p_178012_1_, int p_178012_2_, ResourceLocation p_178012_3_) {
/* 250 */     this.mc.getTextureManager().bindTexture(p_178012_3_);
/* 251 */     GlStateManager.enableBlend();
/* 252 */     Gui.drawModalRectWithCustomSizedTexture(p_178012_1_, p_178012_2_, 0.0F, 0.0F, 32, 32, 32.0F, 32.0F);
/* 253 */     GlStateManager.disableBlend();
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean func_178013_b() {
/* 258 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   private void prepareServerIcon() {
/* 263 */     if (this.server.getBase64EncodedIconData() == null) {
/*     */       
/* 265 */       this.mc.getTextureManager().deleteTexture(this.serverIcon);
/* 266 */       this.field_148305_h = null;
/*     */     } else {
/*     */       BufferedImage bufferedimage;
/*     */       
/* 270 */       ByteBuf bytebuf = Unpooled.copiedBuffer(this.server.getBase64EncodedIconData(), Charsets.UTF_8);
/* 271 */       ByteBuf bytebuf1 = Base64.decode(bytebuf);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/* 277 */         bufferedimage = TextureUtil.readBufferedImage((InputStream)new ByteBufInputStream(bytebuf1));
/* 278 */         Validate.validState((bufferedimage.getWidth() == 64), "Must be 64 pixels wide", new Object[0]);
/* 279 */         Validate.validState((bufferedimage.getHeight() == 64), "Must be 64 pixels high", new Object[0]);
/*     */       
/*     */       }
/* 282 */       catch (Throwable throwable) {
/*     */         
/* 284 */         logger.error("Invalid icon for server " + this.server.serverName + " (" + this.server.serverIP + ")", throwable);
/* 285 */         this.server.setBase64EncodedIconData((String)null);
/*     */       }
/*     */       finally {
/*     */         
/* 289 */         bytebuf.release();
/* 290 */         bytebuf1.release();
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 296 */       if (this.field_148305_h == null) {
/*     */         
/* 298 */         this.field_148305_h = new DynamicTexture(bufferedimage.getWidth(), bufferedimage.getHeight());
/* 299 */         this.mc.getTextureManager().loadTexture(this.serverIcon, (ITextureObject)this.field_148305_h);
/*     */       } 
/*     */       
/* 302 */       bufferedimage.getRGB(0, 0, bufferedimage.getWidth(), bufferedimage.getHeight(), this.field_148305_h.getTextureData(), 0, bufferedimage.getWidth());
/* 303 */       this.field_148305_h.updateDynamicTexture();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean mousePressed(int slotIndex, int p_148278_2_, int p_148278_3_, int p_148278_4_, int p_148278_5_, int p_148278_6_) {
/* 309 */     if (p_148278_5_ <= 32) {
/*     */       
/* 311 */       if (p_148278_5_ < 32 && p_148278_5_ > 16 && func_178013_b()) {
/*     */         
/* 313 */         this.owner.selectServer(slotIndex);
/* 314 */         this.owner.connectToSelected();
/* 315 */         return true;
/*     */       } 
/*     */       
/* 318 */       if (p_148278_5_ < 16 && p_148278_6_ < 16 && this.owner.func_175392_a(this, slotIndex)) {
/*     */         
/* 320 */         this.owner.func_175391_a(this, slotIndex, GuiScreen.isShiftKeyDown());
/* 321 */         return true;
/*     */       } 
/*     */       
/* 324 */       if (p_148278_5_ < 16 && p_148278_6_ > 16 && this.owner.func_175394_b(this, slotIndex)) {
/*     */         
/* 326 */         this.owner.func_175393_b(this, slotIndex, GuiScreen.isShiftKeyDown());
/* 327 */         return true;
/*     */       } 
/*     */     } 
/*     */     
/* 331 */     this.owner.selectServer(slotIndex);
/*     */     
/* 333 */     if (Minecraft.getSystemTime() - this.field_148298_f < 250L)
/*     */     {
/* 335 */       this.owner.connectToSelected();
/*     */     }
/*     */     
/* 338 */     this.field_148298_f = Minecraft.getSystemTime();
/* 339 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSelected(int p_178011_1_, int p_178011_2_, int p_178011_3_) {}
/*     */ 
/*     */   
/*     */   public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) {}
/*     */ 
/*     */   
/*     */   public void ping() {
/* 351 */     this.server.field_78841_f = true;
/* 352 */     this.server.pingToServer = -2L;
/* 353 */     this.server.serverMOTD = "";
/* 354 */     this.server.populationInfo = "";
/*     */     
/* 356 */     field_148302_b.submit(() -> {
/*     */           try {
/*     */             this.owner.getOldServerPinger().ping(this.server);
/* 359 */           } catch (UnknownHostException var2) {
/*     */             this.server.pingToServer = -1L;
/*     */             this.server.serverMOTD = EnumChatFormatting.DARK_RED + "No se pudo encontrar ese servidor.";
/* 362 */           } catch (Exception var3) {
/*     */             this.server.pingToServer = -1L;
/*     */             this.server.serverMOTD = EnumChatFormatting.DARK_RED + "No se puede conectar al servidor.";
/*     */           } 
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public ServerData getServerData() {
/* 371 */     return this.server;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\gui\ServerListEntryNormal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */