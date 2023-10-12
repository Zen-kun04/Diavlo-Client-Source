/*     */ package net.minecraft.client.network;
/*     */ 
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import com.mojang.authlib.exceptions.AuthenticationException;
/*     */ import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
/*     */ import com.mojang.authlib.exceptions.InvalidCredentialsException;
/*     */ import com.mojang.authlib.minecraft.MinecraftSessionService;
/*     */ import io.netty.util.concurrent.Future;
/*     */ import io.netty.util.concurrent.GenericFutureListener;
/*     */ import java.math.BigInteger;
/*     */ import java.security.PublicKey;
/*     */ import javax.crypto.SecretKey;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiDisconnected;
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ import net.minecraft.network.EnumConnectionState;
/*     */ import net.minecraft.network.INetHandler;
/*     */ import net.minecraft.network.NetworkManager;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.login.INetHandlerLoginClient;
/*     */ import net.minecraft.network.login.client.C01PacketEncryptionResponse;
/*     */ import net.minecraft.network.login.server.S00PacketDisconnect;
/*     */ import net.minecraft.network.login.server.S01PacketEncryptionRequest;
/*     */ import net.minecraft.network.login.server.S02PacketLoginSuccess;
/*     */ import net.minecraft.network.login.server.S03PacketEnableCompression;
/*     */ import net.minecraft.util.ChatComponentTranslation;
/*     */ import net.minecraft.util.CryptManager;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class NetHandlerLoginClient
/*     */   implements INetHandlerLoginClient {
/*  34 */   private static final Logger logger = LogManager.getLogger();
/*     */   
/*     */   private final Minecraft mc;
/*     */   private final GuiScreen previousGuiScreen;
/*     */   private final NetworkManager networkManager;
/*     */   private GameProfile gameProfile;
/*     */   
/*     */   public NetHandlerLoginClient(NetworkManager networkManagerIn, Minecraft mcIn, GuiScreen p_i45059_3_) {
/*  42 */     this.networkManager = networkManagerIn;
/*  43 */     this.mc = mcIn;
/*  44 */     this.previousGuiScreen = p_i45059_3_;
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleEncryptionRequest(S01PacketEncryptionRequest packetIn) {
/*  49 */     final SecretKey secretkey = CryptManager.createNewSharedKey();
/*  50 */     String s = packetIn.getServerId();
/*  51 */     PublicKey publickey = packetIn.getPublicKey();
/*  52 */     String s1 = (new BigInteger(CryptManager.getServerIdHash(s, publickey, secretkey))).toString(16);
/*     */     
/*  54 */     if (this.mc.getCurrentServerData() != null && this.mc.getCurrentServerData().isOnLAN()) {
/*     */ 
/*     */       
/*     */       try {
/*  58 */         getSessionService().joinServer(this.mc.getSession().getProfile(), this.mc.getSession().getToken(), s1);
/*     */       }
/*  60 */       catch (AuthenticationException var10) {
/*     */         
/*  62 */         logger.warn("Couldn't connect to auth servers but will continue to join LAN");
/*     */       } 
/*     */     } else {
/*     */ 
/*     */       
/*     */       try {
/*     */         
/*  69 */         getSessionService().joinServer(this.mc.getSession().getProfile(), this.mc.getSession().getToken(), s1);
/*     */       }
/*  71 */       catch (AuthenticationUnavailableException var7) {
/*     */         
/*  73 */         this.networkManager.closeChannel((IChatComponent)new ChatComponentTranslation("disconnect.loginFailedInfo", new Object[] { new ChatComponentTranslation("disconnect.loginFailedInfo.serversUnavailable", new Object[0]) }));
/*     */         
/*     */         return;
/*  76 */       } catch (InvalidCredentialsException var8) {
/*     */         
/*  78 */         this.networkManager.closeChannel((IChatComponent)new ChatComponentTranslation("disconnect.loginFailedInfo", new Object[] { new ChatComponentTranslation("disconnect.loginFailedInfo.invalidSession", new Object[0]) }));
/*     */         
/*     */         return;
/*  81 */       } catch (AuthenticationException authenticationexception) {
/*     */         
/*  83 */         this.networkManager.closeChannel((IChatComponent)new ChatComponentTranslation("disconnect.loginFailedInfo", new Object[] { new ChatComponentTranslation("The server is Premium and you are using a Cracked account.", new Object[0]) }));
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/*  88 */     this.networkManager.sendPacket((Packet)new C01PacketEncryptionResponse(secretkey, publickey, packetIn.getVerifyToken()), new GenericFutureListener<Future<? super Void>>()
/*     */         {
/*     */           public void operationComplete(Future<? super Void> p_operationComplete_1_) throws Exception
/*     */           {
/*  92 */             NetHandlerLoginClient.this.networkManager.enableEncryption(secretkey);
/*     */           }
/*     */         },  new GenericFutureListener[0]);
/*     */   }
/*     */ 
/*     */   
/*     */   private MinecraftSessionService getSessionService() {
/*  99 */     return this.mc.getSessionService();
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleLoginSuccess(S02PacketLoginSuccess packetIn) {
/* 104 */     this.gameProfile = packetIn.getProfile();
/* 105 */     this.networkManager.setConnectionState(EnumConnectionState.PLAY);
/* 106 */     this.networkManager.setNetHandler((INetHandler)new NetHandlerPlayClient(this.mc, this.previousGuiScreen, this.networkManager, this.gameProfile));
/*     */   }
/*     */   
/*     */   public void onDisconnect(IChatComponent reason) {
/*     */     ChatComponentTranslation chatComponentTranslation;
/* 111 */     if (reason.getFormattedText().contains("java.util.UUID.toString()")) {
/* 112 */       chatComponentTranslation = new ChatComponentTranslation("The server is Premium and you are using a Cracked account.", new Object[0]);
/*     */     }
/* 114 */     this.mc.displayGuiScreen((GuiScreen)new GuiDisconnected(this.previousGuiScreen, "connect.failed", (IChatComponent)chatComponentTranslation));
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleDisconnect(S00PacketDisconnect packetIn) {
/* 119 */     this.networkManager.closeChannel(packetIn.func_149603_c());
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleEnableCompression(S03PacketEnableCompression packetIn) {
/* 124 */     if (!this.networkManager.isLocalChannel())
/*     */     {
/* 126 */       this.networkManager.setCompressionTreshold(packetIn.getCompressionTreshold());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\network\NetHandlerLoginClient.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */