/*     */ package net.minecraft.client.gui;
/*     */ 
/*     */ import com.google.common.base.Splitter;
/*     */ import com.google.common.collect.Lists;
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.multiplayer.GuiConnecting;
/*     */ import net.minecraft.client.multiplayer.ServerData;
/*     */ import net.minecraft.client.multiplayer.ServerList;
/*     */ import net.minecraft.client.network.LanServerDetector;
/*     */ import net.minecraft.client.network.OldServerPinger;
/*     */ import net.minecraft.client.resources.I18n;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.lwjgl.input.Keyboard;
/*     */ import rip.diavlo.base.Client;
/*     */ import rip.diavlo.base.api.ui.griefing.GuiUUIDSpoof;
/*     */ import rip.diavlo.base.viaversion.viamcp.ViaMCP;
/*     */ 
/*     */ public class GuiMultiplayer
/*     */   extends GuiScreen
/*     */   implements GuiYesNoCallback {
/*  23 */   private static final Logger logger = LogManager.getLogger();
/*  24 */   private final OldServerPinger oldServerPinger = new OldServerPinger();
/*     */   
/*     */   private GuiScreen parentScreen;
/*     */   
/*     */   private ServerSelectionList serverListSelector;
/*     */   private ServerList savedServerList;
/*     */   private GuiButton btnEditServer;
/*     */   private GuiButton btnSelectServer;
/*     */   private GuiButton btnDeleteServer;
/*     */   private boolean deletingServer;
/*     */   private boolean addingServer;
/*     */   private boolean editingServer;
/*     */   private boolean directConnect;
/*     */   private String hoveringText;
/*     */   private ServerData selectedServer;
/*     */   private LanServerDetector.LanServerList lanServerList;
/*     */   private LanServerDetector.ThreadLanServerFind lanServerDetector;
/*     */   private boolean initialized;
/*     */   private GuiTextField IpBungeeBtn;
/*     */   
/*     */   public GuiMultiplayer(GuiScreen parentScreen) {
/*  45 */     this.parentScreen = parentScreen;
/*     */   }
/*     */ 
/*     */   
/*     */   public void initGui() {
/*  50 */     Keyboard.enableRepeatEvents(true);
/*  51 */     this.buttonList.clear();
/*     */     
/*  53 */     if (!this.initialized) {
/*     */       
/*  55 */       this.initialized = true;
/*  56 */       this.savedServerList = new ServerList(this.mc);
/*  57 */       this.savedServerList.loadServerList();
/*  58 */       this.lanServerList = new LanServerDetector.LanServerList();
/*     */ 
/*     */       
/*     */       try {
/*  62 */         this.lanServerDetector = new LanServerDetector.ThreadLanServerFind(this.lanServerList);
/*  63 */         this.lanServerDetector.start();
/*     */       }
/*  65 */       catch (Exception exception) {
/*     */         
/*  67 */         logger.warn("Unable to start LAN server detection: " + exception.getMessage());
/*     */       } 
/*     */       
/*  70 */       this.serverListSelector = new ServerSelectionList(this, this.mc, this.width, this.height, 32, this.height - 64, 36);
/*  71 */       this.serverListSelector.func_148195_a(this.savedServerList);
/*     */     }
/*     */     else {
/*     */       
/*  75 */       this.serverListSelector.setDimensions(this.width, this.height, 32, this.height - 64);
/*     */     } 
/*     */     
/*  78 */     createButtons();
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleMouseInput() throws IOException {
/*  83 */     super.handleMouseInput();
/*  84 */     this.serverListSelector.handleMouseInput();
/*     */   }
/*     */ 
/*     */   
/*     */   public void createButtons() {
/*  89 */     this.buttonList.add(this.btnEditServer = new GuiButton(7, this.width / 2 - 154, this.height - 28, 70, 20, I18n.format("Editar", new Object[0])));
/*  90 */     this.buttonList.add(this.btnDeleteServer = new GuiButton(2, this.width / 2 - 74, this.height - 28, 70, 20, I18n.format("Borrar", new Object[0])));
/*  91 */     this.buttonList.add(this.btnSelectServer = new GuiButton(1, this.width / 2 - 154, this.height - 52, 100, 20, I18n.format("Seleccionar", new Object[0])));
/*  92 */     this.buttonList.add(new GuiButton(4, this.width / 2 - 50, this.height - 52, 100, 20, I18n.format("Conexión Directa", new Object[0])));
/*  93 */     this.buttonList.add(new GuiButton(3, this.width / 2 + 4 + 50, this.height - 52, 100, 20, I18n.format("Añadir", new Object[0])));
/*  94 */     this.buttonList.add(new GuiButton(8, this.width / 2 + 4, this.height - 28, 70, 20, I18n.format("Refrescar", new Object[0])));
/*  95 */     this.buttonList.add(new GuiButton(0, this.width / 2 + 4 + 76, this.height - 28, 70, 20, I18n.format("Cancelar", new Object[0])));
/*  96 */     this.buttonList.add(new GuiButton(9, this.width / 2 - 260, this.height - 52, 90, 20, BungeeSpoofBtn()));
/*  97 */     this.buttonList.add(ViaMCP.INSTANCE.getAsyncVersionSlider());
/*  98 */     this.IpBungeeBtn = new GuiTextField(10, this.fontRendererObj, this.width / 2 - 258, this.height - 26, 66, 16);
/*  99 */     this.IpBungeeBtn.setEnableBackgroundDrawing(false);
/* 100 */     this.IpBungeeBtn.setTextColor(16733525);
/* 101 */     this.IpBungeeBtn.setText(Client.getInstance().getSpoofingUtil().getIpBungeeHack());
/* 102 */     this.buttonList.add(new GuiButton(11, this.width / 2 + 193, this.height - 28, 75, 20, "UUID Spoof"));
/*     */ 
/*     */     
/* 105 */     selectServer(this.serverListSelector.func_148193_k());
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateScreen() {
/* 110 */     super.updateScreen();
/*     */     
/* 112 */     if (this.lanServerList.getWasUpdated()) {
/*     */       
/* 114 */       List<LanServerDetector.LanServer> list = this.lanServerList.getLanServers();
/* 115 */       this.lanServerList.setWasNotUpdated();
/* 116 */       this.serverListSelector.func_148194_a(list);
/*     */     } 
/*     */     
/* 119 */     this.oldServerPinger.pingPendingNetworks();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onGuiClosed() {
/* 124 */     Keyboard.enableRepeatEvents(false);
/*     */     
/* 126 */     if (this.lanServerDetector != null) {
/*     */       
/* 128 */       this.lanServerDetector.interrupt();
/* 129 */       this.lanServerDetector = null;
/*     */     } 
/*     */     
/* 132 */     this.oldServerPinger.clearPendingNetworks();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void actionPerformed(GuiButton button) throws IOException {
/* 137 */     if (button.enabled) {
/*     */       
/* 139 */       GuiListExtended.IGuiListEntry guilistextended$iguilistentry = (this.serverListSelector.func_148193_k() < 0) ? null : this.serverListSelector.getListEntry(this.serverListSelector.func_148193_k());
/*     */       
/* 141 */       if (button.id == 2 && guilistextended$iguilistentry instanceof ServerListEntryNormal) {
/*     */         
/* 143 */         String s4 = (((ServerListEntryNormal)guilistextended$iguilistentry).getServerData()).serverName;
/*     */         
/* 145 */         if (s4 != null)
/*     */         {
/* 147 */           this.deletingServer = true;
/* 148 */           String s = I18n.format("selectServer.deleteQuestion", new Object[0]);
/* 149 */           String s1 = "'" + s4 + "' " + I18n.format("selectServer.deleteWarning", new Object[0]);
/* 150 */           String s2 = I18n.format("selectServer.deleteButton", new Object[0]);
/* 151 */           String s3 = I18n.format("gui.cancel", new Object[0]);
/* 152 */           GuiYesNo guiyesno = new GuiYesNo(this, s, s1, s2, s3, this.serverListSelector.func_148193_k());
/* 153 */           this.mc.displayGuiScreen(guiyesno);
/*     */         }
/*     */       
/* 156 */       } else if (button.id == 1) {
/*     */         
/* 158 */         connectToSelected();
/*     */       }
/* 160 */       else if (button.id == 4) {
/*     */         
/* 162 */         this.directConnect = true;
/* 163 */         this.mc.displayGuiScreen(new GuiScreenServerList(this, this.selectedServer = new ServerData(I18n.format("selectServer.defaultName", new Object[0]), "", false)));
/*     */       }
/* 165 */       else if (button.id == 3) {
/*     */         
/* 167 */         this.addingServer = true;
/* 168 */         this.mc.displayGuiScreen(new GuiScreenAddServer(this, this.selectedServer = new ServerData(I18n.format("selectServer.defaultName", new Object[0]), "", false)));
/*     */       }
/* 170 */       else if (button.id == 7 && guilistextended$iguilistentry instanceof ServerListEntryNormal) {
/*     */         
/* 172 */         this.editingServer = true;
/* 173 */         ServerData serverdata = ((ServerListEntryNormal)guilistextended$iguilistentry).getServerData();
/* 174 */         this.selectedServer = new ServerData(serverdata.serverName, serverdata.serverIP, false);
/* 175 */         this.selectedServer.copyFrom(serverdata);
/* 176 */         this.mc.displayGuiScreen(new GuiScreenAddServer(this, this.selectedServer));
/*     */       }
/* 178 */       else if (button.id == 0) {
/*     */         
/* 180 */         this.mc.displayGuiScreen(this.parentScreen);
/*     */       }
/* 182 */       else if (button.id == 8) {
/*     */         
/* 184 */         refreshServerList();
/*     */       }
/* 186 */       else if (button.id == 11) {
/* 187 */         this.mc.displayGuiScreen((GuiScreen)new GuiUUIDSpoof(this));
/* 188 */       } else if (button.id == 9) {
/* 189 */         Client.getInstance().getSpoofingUtil().setBungeeHack(!Client.getInstance().getSpoofingUtil().isBungeeHack());
/* 190 */         button.displayString = BungeeSpoofBtn();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void refreshServerList() {
/* 197 */     this.mc.displayGuiScreen(new GuiMultiplayer(this.parentScreen));
/*     */   }
/*     */ 
/*     */   
/*     */   public void confirmClicked(boolean result, int id) {
/* 202 */     GuiListExtended.IGuiListEntry guilistextended$iguilistentry = (this.serverListSelector.func_148193_k() < 0) ? null : this.serverListSelector.getListEntry(this.serverListSelector.func_148193_k());
/*     */     
/* 204 */     if (this.deletingServer) {
/*     */       
/* 206 */       this.deletingServer = false;
/*     */       
/* 208 */       if (result && guilistextended$iguilistentry instanceof ServerListEntryNormal) {
/*     */         
/* 210 */         this.savedServerList.removeServerData(this.serverListSelector.func_148193_k());
/* 211 */         this.savedServerList.saveServerList();
/* 212 */         this.serverListSelector.setSelectedSlotIndex(-1);
/* 213 */         this.serverListSelector.func_148195_a(this.savedServerList);
/*     */       } 
/*     */       
/* 216 */       this.mc.displayGuiScreen(this);
/*     */     }
/* 218 */     else if (this.directConnect) {
/*     */       
/* 220 */       this.directConnect = false;
/*     */       
/* 222 */       if (result)
/*     */       {
/* 224 */         connectToServer(this.selectedServer);
/*     */       }
/*     */       else
/*     */       {
/* 228 */         this.mc.displayGuiScreen(this);
/*     */       }
/*     */     
/* 231 */     } else if (this.addingServer) {
/*     */       
/* 233 */       this.addingServer = false;
/*     */       
/* 235 */       if (result) {
/*     */         
/* 237 */         this.savedServerList.addServerData(this.selectedServer);
/* 238 */         this.savedServerList.saveServerList();
/* 239 */         this.serverListSelector.setSelectedSlotIndex(-1);
/* 240 */         this.serverListSelector.func_148195_a(this.savedServerList);
/*     */       } 
/*     */       
/* 243 */       this.mc.displayGuiScreen(this);
/*     */     }
/* 245 */     else if (this.editingServer) {
/*     */       
/* 247 */       this.editingServer = false;
/*     */       
/* 249 */       if (result && guilistextended$iguilistentry instanceof ServerListEntryNormal) {
/*     */         
/* 251 */         ServerData serverdata = ((ServerListEntryNormal)guilistextended$iguilistentry).getServerData();
/* 252 */         serverdata.serverName = this.selectedServer.serverName;
/* 253 */         serverdata.serverIP = this.selectedServer.serverIP;
/* 254 */         serverdata.copyFrom(this.selectedServer);
/* 255 */         this.savedServerList.saveServerList();
/* 256 */         this.serverListSelector.func_148195_a(this.savedServerList);
/*     */       } 
/*     */       
/* 259 */       this.mc.displayGuiScreen(this);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void keyTyped(char typedChar, int keyCode) throws IOException {
/* 265 */     int i = this.serverListSelector.func_148193_k();
/* 266 */     GuiListExtended.IGuiListEntry guilistextended$iguilistentry = (i < 0) ? null : this.serverListSelector.getListEntry(i);
/*     */     
/* 268 */     if (keyCode == 15 && this.IpBungeeBtn.isFocused())
/* 269 */       this.IpBungeeBtn.setFocused(false); 
/* 270 */     if (this.IpBungeeBtn.isFocused()) {
/* 271 */       this.IpBungeeBtn.textboxKeyTyped(typedChar, keyCode);
/*     */     }
/* 273 */     if (keyCode == 63) {
/*     */       
/* 275 */       refreshServerList();
/*     */     }
/*     */     else {
/*     */       
/* 279 */       if (i >= 0) {
/*     */         
/* 281 */         if (keyCode == 200) {
/*     */           
/* 283 */           if (isShiftKeyDown()) {
/*     */             
/* 285 */             if (i > 0 && guilistextended$iguilistentry instanceof ServerListEntryNormal)
/*     */             {
/* 287 */               this.savedServerList.swapServers(i, i - 1);
/* 288 */               selectServer(this.serverListSelector.func_148193_k() - 1);
/* 289 */               this.serverListSelector.scrollBy(-this.serverListSelector.getSlotHeight());
/* 290 */               this.serverListSelector.func_148195_a(this.savedServerList);
/*     */             }
/*     */           
/* 293 */           } else if (i > 0) {
/*     */             
/* 295 */             selectServer(this.serverListSelector.func_148193_k() - 1);
/* 296 */             this.serverListSelector.scrollBy(-this.serverListSelector.getSlotHeight());
/*     */             
/* 298 */             if (this.serverListSelector.getListEntry(this.serverListSelector.func_148193_k()) instanceof ServerListEntryLanScan)
/*     */             {
/* 300 */               if (this.serverListSelector.func_148193_k() > 0)
/*     */               {
/* 302 */                 selectServer(this.serverListSelector.getSize() - 1);
/* 303 */                 this.serverListSelector.scrollBy(-this.serverListSelector.getSlotHeight());
/*     */               }
/*     */               else
/*     */               {
/* 307 */                 selectServer(-1);
/*     */               }
/*     */             
/*     */             }
/*     */           } else {
/*     */             
/* 313 */             selectServer(-1);
/*     */           }
/*     */         
/* 316 */         } else if (keyCode == 208) {
/*     */           
/* 318 */           if (isShiftKeyDown()) {
/*     */             
/* 320 */             if (i < this.savedServerList.countServers() - 1)
/*     */             {
/* 322 */               this.savedServerList.swapServers(i, i + 1);
/* 323 */               selectServer(i + 1);
/* 324 */               this.serverListSelector.scrollBy(this.serverListSelector.getSlotHeight());
/* 325 */               this.serverListSelector.func_148195_a(this.savedServerList);
/*     */             }
/*     */           
/* 328 */           } else if (i < this.serverListSelector.getSize()) {
/*     */             
/* 330 */             selectServer(this.serverListSelector.func_148193_k() + 1);
/* 331 */             this.serverListSelector.scrollBy(this.serverListSelector.getSlotHeight());
/*     */             
/* 333 */             if (this.serverListSelector.getListEntry(this.serverListSelector.func_148193_k()) instanceof ServerListEntryLanScan)
/*     */             {
/* 335 */               if (this.serverListSelector.func_148193_k() < this.serverListSelector.getSize() - 1)
/*     */               {
/* 337 */                 selectServer(this.serverListSelector.getSize() + 1);
/* 338 */                 this.serverListSelector.scrollBy(this.serverListSelector.getSlotHeight());
/*     */               }
/*     */               else
/*     */               {
/* 342 */                 selectServer(-1);
/*     */               }
/*     */             
/*     */             }
/*     */           } else {
/*     */             
/* 348 */             selectServer(-1);
/*     */           }
/*     */         
/* 351 */         } else if (keyCode != 28 && keyCode != 156) {
/*     */           
/* 353 */           super.keyTyped(typedChar, keyCode);
/*     */         }
/*     */         else {
/*     */           
/* 357 */           actionPerformed(this.buttonList.get(2));
/*     */         }
/*     */       
/*     */       } else {
/*     */         
/* 362 */         super.keyTyped(typedChar, keyCode);
/*     */       } 
/* 364 */       Client.getInstance().getSpoofingUtil().setIpBungeeHack(this.IpBungeeBtn.getText());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
/* 370 */     this.hoveringText = null;
/* 371 */     drawDefaultBackground();
/* 372 */     this.serverListSelector.drawScreen(mouseX, mouseY, partialTicks);
/* 373 */     drawCenteredString(this.fontRendererObj, I18n.format("Multijugador", new Object[0]), this.width / 2, 20, 16777215);
/* 374 */     super.drawScreen(mouseX, mouseY, partialTicks);
/*     */     
/* 376 */     if (this.hoveringText != null)
/*     */     {
/* 378 */       drawHoveringText(Lists.newArrayList(Splitter.on("\n").split(this.hoveringText)), mouseX, mouseY);
/*     */     }
/* 380 */     this.IpBungeeBtn.drawTextBox();
/*     */   }
/*     */ 
/*     */   
/*     */   public void connectToSelected() {
/* 385 */     GuiListExtended.IGuiListEntry guilistextended$iguilistentry = (this.serverListSelector.func_148193_k() < 0) ? null : this.serverListSelector.getListEntry(this.serverListSelector.func_148193_k());
/*     */     
/* 387 */     if (guilistextended$iguilistentry instanceof ServerListEntryNormal) {
/*     */       
/* 389 */       connectToServer(((ServerListEntryNormal)guilistextended$iguilistentry).getServerData());
/*     */     }
/* 391 */     else if (guilistextended$iguilistentry instanceof ServerListEntryLanDetected) {
/*     */       
/* 393 */       LanServerDetector.LanServer lanserverdetector$lanserver = ((ServerListEntryLanDetected)guilistextended$iguilistentry).getLanServer();
/* 394 */       connectToServer(new ServerData(lanserverdetector$lanserver.getServerMotd(), lanserverdetector$lanserver.getServerIpPort(), true));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void connectToServer(ServerData server) {
/* 400 */     this.mc.displayGuiScreen((GuiScreen)new GuiConnecting(this, this.mc, server));
/*     */   }
/*     */ 
/*     */   
/*     */   public void selectServer(int index) {
/* 405 */     this.serverListSelector.setSelectedSlotIndex(index);
/* 406 */     GuiListExtended.IGuiListEntry guilistextended$iguilistentry = (index < 0) ? null : this.serverListSelector.getListEntry(index);
/* 407 */     this.btnSelectServer.enabled = false;
/* 408 */     this.btnEditServer.enabled = false;
/* 409 */     this.btnDeleteServer.enabled = false;
/*     */     
/* 411 */     if (guilistextended$iguilistentry != null && !(guilistextended$iguilistentry instanceof ServerListEntryLanScan)) {
/*     */       
/* 413 */       this.btnSelectServer.enabled = true;
/*     */       
/* 415 */       if (guilistextended$iguilistentry instanceof ServerListEntryNormal) {
/*     */         
/* 417 */         this.btnEditServer.enabled = true;
/* 418 */         this.btnDeleteServer.enabled = true;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public OldServerPinger getOldServerPinger() {
/* 425 */     return this.oldServerPinger;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setHoveringText(String p_146793_1_) {
/* 430 */     this.hoveringText = p_146793_1_;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
/* 435 */     this.IpBungeeBtn.mouseClicked(mouseX, mouseY, mouseButton);
/* 436 */     super.mouseClicked(mouseX, mouseY, mouseButton);
/* 437 */     this.serverListSelector.mouseClicked(mouseX, mouseY, mouseButton);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void mouseReleased(int mouseX, int mouseY, int state) {
/* 442 */     super.mouseReleased(mouseX, mouseY, state);
/* 443 */     this.serverListSelector.mouseReleased(mouseX, mouseY, state);
/*     */   }
/*     */ 
/*     */   
/*     */   public ServerList getServerList() {
/* 448 */     return this.savedServerList;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean func_175392_a(ServerListEntryNormal p_175392_1_, int p_175392_2_) {
/* 453 */     return (p_175392_2_ > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean func_175394_b(ServerListEntryNormal p_175394_1_, int p_175394_2_) {
/* 458 */     return (p_175394_2_ < this.savedServerList.countServers() - 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public void func_175391_a(ServerListEntryNormal p_175391_1_, int p_175391_2_, boolean p_175391_3_) {
/* 463 */     int i = p_175391_3_ ? 0 : (p_175391_2_ - 1);
/* 464 */     this.savedServerList.swapServers(p_175391_2_, i);
/*     */     
/* 466 */     if (this.serverListSelector.func_148193_k() == p_175391_2_)
/*     */     {
/* 468 */       selectServer(i);
/*     */     }
/*     */     
/* 471 */     this.serverListSelector.func_148195_a(this.savedServerList);
/*     */   }
/*     */ 
/*     */   
/*     */   public void func_175393_b(ServerListEntryNormal p_175393_1_, int p_175393_2_, boolean p_175393_3_) {
/* 476 */     int i = p_175393_3_ ? (this.savedServerList.countServers() - 1) : (p_175393_2_ + 1);
/* 477 */     this.savedServerList.swapServers(p_175393_2_, i);
/*     */     
/* 479 */     if (this.serverListSelector.func_148193_k() == p_175393_2_)
/*     */     {
/* 481 */       selectServer(i);
/*     */     }
/*     */     
/* 484 */     this.serverListSelector.func_148195_a(this.savedServerList);
/*     */   }
/*     */   
/*     */   private String BungeeSpoofBtn() {
/* 488 */     return Client.getInstance().getSpoofingUtil().isBungeeHack() ? "BungeeHack: §aSi" : "BungeeHack: §cNo";
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\gui\GuiMultiplayer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */