/*     */ package net.minecraft.server.network;
/*     */ import com.google.common.base.Charsets;
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelFutureListener;
/*     */ import io.netty.util.concurrent.Future;
/*     */ import io.netty.util.concurrent.GenericFutureListener;
/*     */ import java.math.BigInteger;
/*     */ import java.security.PrivateKey;
/*     */ import java.util.Arrays;
/*     */ import java.util.Random;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import javax.crypto.SecretKey;
/*     */ import net.minecraft.entity.player.EntityPlayerMP;
/*     */ import net.minecraft.network.NetworkManager;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.login.INetHandlerLoginServer;
/*     */ import net.minecraft.network.login.client.C00PacketLoginStart;
/*     */ import net.minecraft.network.login.client.C01PacketEncryptionResponse;
/*     */ import net.minecraft.network.login.server.S00PacketDisconnect;
/*     */ import net.minecraft.network.login.server.S01PacketEncryptionRequest;
/*     */ import net.minecraft.network.login.server.S02PacketLoginSuccess;
/*     */ import net.minecraft.network.login.server.S03PacketEnableCompression;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.util.ChatComponentText;
/*     */ import net.minecraft.util.CryptManager;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import net.minecraft.util.ITickable;
/*     */ import org.apache.commons.lang3.Validate;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class NetHandlerLoginServer implements INetHandlerLoginServer, ITickable {
/*  36 */   private static final AtomicInteger AUTHENTICATOR_THREAD_ID = new AtomicInteger(0);
/*  37 */   private static final Logger logger = LogManager.getLogger();
/*  38 */   private static final Random RANDOM = new Random();
/*  39 */   private final byte[] verifyToken = new byte[4];
/*     */   private final MinecraftServer server;
/*     */   public final NetworkManager networkManager;
/*  42 */   private LoginState currentLoginState = LoginState.HELLO;
/*     */   private int connectionTimer;
/*     */   private GameProfile loginGameProfile;
/*  45 */   private String serverId = "";
/*     */   
/*     */   private SecretKey secretKey;
/*     */   private EntityPlayerMP player;
/*     */   
/*     */   public NetHandlerLoginServer(MinecraftServer serverIn, NetworkManager networkManagerIn) {
/*  51 */     this.server = serverIn;
/*  52 */     this.networkManager = networkManagerIn;
/*  53 */     RANDOM.nextBytes(this.verifyToken);
/*     */   }
/*     */ 
/*     */   
/*     */   public void update() {
/*  58 */     if (this.currentLoginState == LoginState.READY_TO_ACCEPT) {
/*     */       
/*  60 */       tryAcceptPlayer();
/*     */     }
/*  62 */     else if (this.currentLoginState == LoginState.DELAY_ACCEPT) {
/*     */       
/*  64 */       EntityPlayerMP entityplayermp = this.server.getConfigurationManager().getPlayerByUUID(this.loginGameProfile.getId());
/*     */       
/*  66 */       if (entityplayermp == null) {
/*     */         
/*  68 */         this.currentLoginState = LoginState.READY_TO_ACCEPT;
/*  69 */         this.server.getConfigurationManager().initializeConnectionToPlayer(this.networkManager, this.player);
/*  70 */         this.player = null;
/*     */       } 
/*     */     } 
/*     */     
/*  74 */     if (this.connectionTimer++ == 600)
/*     */     {
/*  76 */       closeConnection("Took too long to log in");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void closeConnection(String reason) {
/*     */     try {
/*  84 */       logger.info("Disconnecting " + getConnectionInfo() + ": " + reason);
/*  85 */       ChatComponentText chatcomponenttext = new ChatComponentText(reason);
/*  86 */       this.networkManager.sendPacket((Packet)new S00PacketDisconnect((IChatComponent)chatcomponenttext));
/*  87 */       this.networkManager.closeChannel((IChatComponent)chatcomponenttext);
/*     */     }
/*  89 */     catch (Exception exception) {
/*     */       
/*  91 */       logger.error("Error whilst disconnecting player", exception);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void tryAcceptPlayer() {
/*  97 */     if (!this.loginGameProfile.isComplete())
/*     */     {
/*  99 */       this.loginGameProfile = getOfflineProfile(this.loginGameProfile);
/*     */     }
/*     */     
/* 102 */     String s = this.server.getConfigurationManager().allowUserToConnect(this.networkManager.getRemoteAddress(), this.loginGameProfile);
/*     */     
/* 104 */     if (s != null) {
/*     */       
/* 106 */       closeConnection(s);
/*     */     }
/*     */     else {
/*     */       
/* 110 */       this.currentLoginState = LoginState.ACCEPTED;
/*     */       
/* 112 */       if (this.server.getNetworkCompressionTreshold() >= 0 && !this.networkManager.isLocalChannel())
/*     */       {
/* 114 */         this.networkManager.sendPacket((Packet)new S03PacketEnableCompression(this.server.getNetworkCompressionTreshold()), (GenericFutureListener)new ChannelFutureListener()
/*     */             {
/*     */               public void operationComplete(ChannelFuture p_operationComplete_1_) throws Exception
/*     */               {
/* 118 */                 NetHandlerLoginServer.this.networkManager.setCompressionTreshold(NetHandlerLoginServer.this.server.getNetworkCompressionTreshold());
/*     */               }
/*     */             },  new GenericFutureListener[0]);
/*     */       }
/*     */       
/* 123 */       this.networkManager.sendPacket((Packet)new S02PacketLoginSuccess(this.loginGameProfile));
/* 124 */       EntityPlayerMP entityplayermp = this.server.getConfigurationManager().getPlayerByUUID(this.loginGameProfile.getId());
/*     */       
/* 126 */       if (entityplayermp != null) {
/*     */         
/* 128 */         this.currentLoginState = LoginState.DELAY_ACCEPT;
/* 129 */         this.player = this.server.getConfigurationManager().createPlayerForUser(this.loginGameProfile);
/*     */       }
/*     */       else {
/*     */         
/* 133 */         this.server.getConfigurationManager().initializeConnectionToPlayer(this.networkManager, this.server.getConfigurationManager().createPlayerForUser(this.loginGameProfile));
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisconnect(IChatComponent reason) {
/* 140 */     logger.info(getConnectionInfo() + " lost connection: " + reason.getUnformattedText());
/*     */   }
/*     */ 
/*     */   
/*     */   public String getConnectionInfo() {
/* 145 */     return (this.loginGameProfile != null) ? (this.loginGameProfile.toString() + " (" + this.networkManager.getRemoteAddress().toString() + ")") : String.valueOf(this.networkManager.getRemoteAddress());
/*     */   }
/*     */ 
/*     */   
/*     */   public void processLoginStart(C00PacketLoginStart packetIn) {
/* 150 */     Validate.validState((this.currentLoginState == LoginState.HELLO), "Unexpected hello packet", new Object[0]);
/* 151 */     this.loginGameProfile = packetIn.getProfile();
/*     */     
/* 153 */     if (this.server.isServerInOnlineMode() && !this.networkManager.isLocalChannel()) {
/*     */       
/* 155 */       this.currentLoginState = LoginState.KEY;
/* 156 */       this.networkManager.sendPacket((Packet)new S01PacketEncryptionRequest(this.serverId, this.server.getKeyPair().getPublic(), this.verifyToken));
/*     */     }
/*     */     else {
/*     */       
/* 160 */       this.currentLoginState = LoginState.READY_TO_ACCEPT;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void processEncryptionResponse(C01PacketEncryptionResponse packetIn) {
/* 166 */     Validate.validState((this.currentLoginState == LoginState.KEY), "Unexpected key packet", new Object[0]);
/* 167 */     PrivateKey privatekey = this.server.getKeyPair().getPrivate();
/*     */     
/* 169 */     if (!Arrays.equals(this.verifyToken, packetIn.getVerifyToken(privatekey)))
/*     */     {
/* 171 */       throw new IllegalStateException("Invalid nonce!");
/*     */     }
/*     */ 
/*     */     
/* 175 */     this.secretKey = packetIn.getSecretKey(privatekey);
/* 176 */     this.currentLoginState = LoginState.AUTHENTICATING;
/* 177 */     this.networkManager.enableEncryption(this.secretKey);
/* 178 */     (new Thread("User Authenticator #" + AUTHENTICATOR_THREAD_ID.incrementAndGet())
/*     */       {
/*     */         public void run()
/*     */         {
/* 182 */           GameProfile gameprofile = NetHandlerLoginServer.this.loginGameProfile;
/*     */ 
/*     */           
/*     */           try {
/* 186 */             String s = (new BigInteger(CryptManager.getServerIdHash(NetHandlerLoginServer.this.serverId, NetHandlerLoginServer.this.server.getKeyPair().getPublic(), NetHandlerLoginServer.this.secretKey))).toString(16);
/* 187 */             NetHandlerLoginServer.this.loginGameProfile = NetHandlerLoginServer.this.server.getMinecraftSessionService().hasJoinedServer(new GameProfile((UUID)null, gameprofile.getName()), s);
/*     */             
/* 189 */             if (NetHandlerLoginServer.this.loginGameProfile != null)
/*     */             {
/* 191 */               NetHandlerLoginServer.logger.info("UUID of player " + NetHandlerLoginServer.this.loginGameProfile.getName() + " is " + NetHandlerLoginServer.this.loginGameProfile.getId());
/* 192 */               NetHandlerLoginServer.this.currentLoginState = NetHandlerLoginServer.LoginState.READY_TO_ACCEPT;
/*     */             }
/* 194 */             else if (NetHandlerLoginServer.this.server.isSinglePlayer())
/*     */             {
/* 196 */               NetHandlerLoginServer.logger.warn("Failed to verify username but will let them in anyway!");
/* 197 */               NetHandlerLoginServer.this.loginGameProfile = NetHandlerLoginServer.this.getOfflineProfile(gameprofile);
/* 198 */               NetHandlerLoginServer.this.currentLoginState = NetHandlerLoginServer.LoginState.READY_TO_ACCEPT;
/*     */             }
/*     */             else
/*     */             {
/* 202 */               NetHandlerLoginServer.this.closeConnection("Failed to verify username!");
/* 203 */               NetHandlerLoginServer.logger.error("Username '" + NetHandlerLoginServer.this.loginGameProfile.getName() + "' tried to join with an invalid session");
/*     */             }
/*     */           
/* 206 */           } catch (AuthenticationUnavailableException var3) {
/*     */             
/* 208 */             if (NetHandlerLoginServer.this.server.isSinglePlayer()) {
/*     */               
/* 210 */               NetHandlerLoginServer.logger.warn("Authentication servers are down but will let them in anyway!");
/* 211 */               NetHandlerLoginServer.this.loginGameProfile = NetHandlerLoginServer.this.getOfflineProfile(gameprofile);
/* 212 */               NetHandlerLoginServer.this.currentLoginState = NetHandlerLoginServer.LoginState.READY_TO_ACCEPT;
/*     */             }
/*     */             else {
/*     */               
/* 216 */               NetHandlerLoginServer.this.closeConnection("Authentication servers are down. Please try again later, sorry!");
/* 217 */               NetHandlerLoginServer.logger.error("Couldn't verify username because servers are unavailable");
/*     */             } 
/*     */           } 
/*     */         }
/* 221 */       }).start();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected GameProfile getOfflineProfile(GameProfile original) {
/* 227 */     UUID uuid = UUID.nameUUIDFromBytes(("OfflinePlayer:" + original.getName()).getBytes(Charsets.UTF_8));
/* 228 */     return new GameProfile(uuid, original.getName());
/*     */   }
/*     */   
/*     */   enum LoginState
/*     */   {
/* 233 */     HELLO,
/* 234 */     KEY,
/* 235 */     AUTHENTICATING,
/* 236 */     READY_TO_ACCEPT,
/* 237 */     DELAY_ACCEPT,
/* 238 */     ACCEPTED;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\server\network\NetHandlerLoginServer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */