/*     */ package net.minecraft.client.multiplayer;
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.UnknownHostException;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiButton;
/*     */ import net.minecraft.client.gui.GuiDisconnected;
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ import net.minecraft.client.network.NetHandlerLoginClient;
/*     */ import net.minecraft.client.resources.I18n;
/*     */ import net.minecraft.network.EnumConnectionState;
/*     */ import net.minecraft.network.INetHandler;
/*     */ import net.minecraft.network.NetworkManager;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.handshake.client.C00Handshake;
/*     */ import net.minecraft.network.login.client.C00PacketLoginStart;
/*     */ import net.minecraft.util.ChatComponentText;
/*     */ import net.minecraft.util.ChatComponentTranslation;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import rip.diavlo.base.Client;
/*     */ import rip.diavlo.base.viaversion.viamcp.ViaMCP;
/*     */ 
/*     */ public class GuiConnecting extends GuiScreen {
/*  27 */   private static final AtomicInteger CONNECTION_ID = new AtomicInteger(0);
/*  28 */   private static final Logger logger = LogManager.getLogger();
/*     */   
/*     */   public static NetworkManager networkManager;
/*     */   private boolean cancel;
/*     */   private final GuiScreen previousGuiScreen;
/*     */   
/*     */   public GuiConnecting(GuiScreen p_i1181_1_, Minecraft mcIn, ServerData p_i1181_3_) {
/*  35 */     this.mc = mcIn;
/*  36 */     this.previousGuiScreen = p_i1181_1_;
/*  37 */     ServerAddress serveraddress = ServerAddress.fromString(p_i1181_3_.serverIP);
/*  38 */     mcIn.loadWorld((WorldClient)null);
/*  39 */     mcIn.setServerData(p_i1181_3_);
/*  40 */     connect(serveraddress.getIP(), serveraddress.getPort());
/*     */   }
/*     */ 
/*     */   
/*     */   public GuiConnecting(GuiScreen p_i1182_1_, Minecraft mcIn, String hostName, int port) {
/*  45 */     this.mc = mcIn;
/*  46 */     this.previousGuiScreen = p_i1182_1_;
/*  47 */     mcIn.loadWorld((WorldClient)null);
/*  48 */     connect(hostName, port);
/*     */   }
/*     */ 
/*     */   
/*     */   private void connect(final String ip, final int port) {
/*  53 */     ViaMCP.INSTANCE.setLastServer(ip + ":" + port);
/*  54 */     logger.info("Connecting to " + ip + ", " + port);
/*  55 */     (new Thread("Server Connector #" + CONNECTION_ID.incrementAndGet())
/*     */       {
/*     */         public void run()
/*     */         {
/*  59 */           InetAddress inetaddress = null;
/*     */ 
/*     */           
/*     */           try {
/*  63 */             if (GuiConnecting.this.cancel) {
/*     */               return;
/*     */             }
/*     */ 
/*     */             
/*  68 */             inetaddress = InetAddress.getByName(ip);
/*  69 */             GuiConnecting.networkManager = NetworkManager.createNetworkManagerAndConnect(inetaddress, port, GuiConnecting.this.mc.gameSettings.isUsingNativeTransport());
/*  70 */             GuiConnecting.networkManager.setNetHandler((INetHandler)new NetHandlerLoginClient(GuiConnecting.networkManager, GuiConnecting.this.mc, GuiConnecting.this.previousGuiScreen));
/*  71 */             GuiConnecting.networkManager.sendPacket((Packet)new C00Handshake(47, ip, port, EnumConnectionState.LOGIN));
/*  72 */             GuiConnecting.networkManager.sendPacket((Packet)new C00PacketLoginStart(GuiConnecting.this.mc.getSession().getProfile()));
/*     */           }
/*  74 */           catch (UnknownHostException unknownhostexception) {
/*     */             
/*  76 */             if (GuiConnecting.this.cancel) {
/*     */               return;
/*     */             }
/*     */ 
/*     */             
/*  81 */             GuiConnecting.logger.error("Couldn't connect to server", unknownhostexception);
/*  82 */             GuiConnecting.this.mc.displayGuiScreen((GuiScreen)new GuiDisconnected(GuiConnecting.this.previousGuiScreen, "connect.failed", (IChatComponent)new ChatComponentTranslation("disconnect.genericReason", new Object[] { "Unknown host" })));
/*     */           }
/*  84 */           catch (Exception exception) {
/*     */             
/*  86 */             if (GuiConnecting.this.cancel) {
/*     */               return;
/*     */             }
/*     */ 
/*     */             
/*  91 */             GuiConnecting.logger.error("Couldn't connect to server", exception);
/*  92 */             String s = exception.toString();
/*     */             
/*  94 */             if (inetaddress != null) {
/*     */               
/*  96 */               String s1 = inetaddress.toString() + ":" + port;
/*  97 */               s = s.replaceAll(s1, "");
/*     */             } 
/*     */             
/* 100 */             GuiConnecting.this.mc.displayGuiScreen((GuiScreen)new GuiDisconnected(GuiConnecting.this.previousGuiScreen, "connect.failed", (IChatComponent)new ChatComponentTranslation("disconnect.genericReason", new Object[] { s })));
/*     */           } 
/*     */         }
/* 103 */       }).start();
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateScreen() {
/* 108 */     this; if (networkManager != null) {
/*     */       
/* 110 */       this; if (networkManager.isChannelOpen()) {
/*     */         
/* 112 */         this; networkManager.processReceivedPackets();
/*     */       }
/*     */       else {
/*     */         
/* 116 */         this; networkManager.checkDisconnected();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void keyTyped(char typedChar, int keyCode) throws IOException {}
/*     */ 
/*     */   
/*     */   public void initGui() {
/* 127 */     this.buttonList.clear();
/* 128 */     this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 120 + 12, I18n.format("gui.cancel", new Object[0])));
/* 129 */     this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 120 + 33, "§cAuto Reconnect"));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void actionPerformed(GuiButton button) throws IOException {
/* 134 */     if (button.id == 0) {
/*     */       
/* 136 */       this.cancel = true;
/*     */       
/* 138 */       this; if (networkManager != null) {
/*     */         
/* 140 */         this; networkManager.closeChannel((IChatComponent)new ChatComponentText("Aborted"));
/*     */       } 
/*     */       
/* 143 */       this.mc.displayGuiScreen(this.previousGuiScreen);
/* 144 */     } else if (button.id == 1) {
/* 145 */       Client.getInstance().getSpoofingUtil().setAutoReconnect(!Client.getInstance().getSpoofingUtil().isAutoReconnect());
/*     */     } 
/*     */   }
/*     */   
/*     */   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
/* 150 */     drawDefaultBackground();
/*     */     
/* 152 */     this; if (networkManager == null) {
/*     */       
/* 154 */       drawCenteredString(this.fontRendererObj, I18n.format("connect.connecting", new Object[0]), this.width / 2, this.height / 2 - 50, 16777215);
/*     */     }
/*     */     else {
/*     */       
/* 158 */       drawCenteredString(this.fontRendererObj, I18n.format("connect.authorizing", new Object[0]), this.width / 2, this.height / 2 - 50, 16777215);
/*     */     } 
/*     */     
/* 161 */     super.drawScreen(mouseX, mouseY, partialTicks);
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\multiplayer\GuiConnecting.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */