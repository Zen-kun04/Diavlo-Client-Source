/*     */ package net.minecraft.network;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.util.concurrent.ThreadFactoryBuilder;
/*     */ import io.netty.bootstrap.ServerBootstrap;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelException;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelHandler;
/*     */ import io.netty.channel.ChannelInitializer;
/*     */ import io.netty.channel.ChannelOption;
/*     */ import io.netty.channel.EventLoopGroup;
/*     */ import io.netty.channel.epoll.Epoll;
/*     */ import io.netty.channel.epoll.EpollEventLoopGroup;
/*     */ import io.netty.channel.epoll.EpollServerSocketChannel;
/*     */ import io.netty.channel.local.LocalAddress;
/*     */ import io.netty.channel.local.LocalEventLoopGroup;
/*     */ import io.netty.channel.local.LocalServerChannel;
/*     */ import io.netty.channel.nio.NioEventLoopGroup;
/*     */ import io.netty.channel.socket.nio.NioServerSocketChannel;
/*     */ import io.netty.handler.timeout.ReadTimeoutHandler;
/*     */ import io.netty.util.concurrent.Future;
/*     */ import io.netty.util.concurrent.GenericFutureListener;
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.SocketAddress;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.Callable;
/*     */ import net.minecraft.client.network.NetHandlerHandshakeMemory;
/*     */ import net.minecraft.crash.CrashReport;
/*     */ import net.minecraft.crash.CrashReportCategory;
/*     */ import net.minecraft.network.play.server.S40PacketDisconnect;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.network.NetHandlerHandshakeTCP;
/*     */ import net.minecraft.util.ChatComponentText;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import net.minecraft.util.LazyLoadBase;
/*     */ import net.minecraft.util.MessageDeserializer;
/*     */ import net.minecraft.util.MessageDeserializer2;
/*     */ import net.minecraft.util.MessageSerializer;
/*     */ import net.minecraft.util.MessageSerializer2;
/*     */ import net.minecraft.util.ReportedException;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class NetworkSystem
/*     */ {
/*  50 */   private static final Logger logger = LogManager.getLogger();
/*  51 */   public static final LazyLoadBase<NioEventLoopGroup> eventLoops = new LazyLoadBase<NioEventLoopGroup>()
/*     */     {
/*     */       protected NioEventLoopGroup load()
/*     */       {
/*  55 */         return new NioEventLoopGroup(0, (new ThreadFactoryBuilder()).setNameFormat("Netty Server IO #%d").setDaemon(true).build());
/*     */       }
/*     */     };
/*  58 */   public static final LazyLoadBase<EpollEventLoopGroup> SERVER_EPOLL_EVENTLOOP = new LazyLoadBase<EpollEventLoopGroup>()
/*     */     {
/*     */       protected EpollEventLoopGroup load()
/*     */       {
/*  62 */         return new EpollEventLoopGroup(0, (new ThreadFactoryBuilder()).setNameFormat("Netty Epoll Server IO #%d").setDaemon(true).build());
/*     */       }
/*     */     };
/*  65 */   public static final LazyLoadBase<LocalEventLoopGroup> SERVER_LOCAL_EVENTLOOP = new LazyLoadBase<LocalEventLoopGroup>()
/*     */     {
/*     */       protected LocalEventLoopGroup load()
/*     */       {
/*  69 */         return new LocalEventLoopGroup(0, (new ThreadFactoryBuilder()).setNameFormat("Netty Local Server IO #%d").setDaemon(true).build());
/*     */       }
/*     */     };
/*     */   private final MinecraftServer mcServer;
/*     */   public volatile boolean isAlive;
/*  74 */   private final List<ChannelFuture> endpoints = Collections.synchronizedList(Lists.newArrayList());
/*  75 */   private final List<NetworkManager> networkManagers = Collections.synchronizedList(Lists.newArrayList());
/*     */ 
/*     */   
/*     */   public NetworkSystem(MinecraftServer server) {
/*  79 */     this.mcServer = server;
/*  80 */     this.isAlive = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addLanEndpoint(InetAddress address, int port) throws IOException {
/*  85 */     synchronized (this.endpoints) {
/*     */       Class<NioServerSocketChannel> clazz;
/*     */       
/*     */       LazyLoadBase<NioEventLoopGroup> lazyLoadBase;
/*     */       
/*  90 */       if (Epoll.isAvailable() && this.mcServer.shouldUseNativeTransport()) {
/*     */         
/*  92 */         Class<EpollServerSocketChannel> clazz1 = EpollServerSocketChannel.class;
/*  93 */         LazyLoadBase<EpollEventLoopGroup> lazyLoadBase1 = SERVER_EPOLL_EVENTLOOP;
/*  94 */         logger.info("Using epoll channel type");
/*     */       }
/*     */       else {
/*     */         
/*  98 */         clazz = NioServerSocketChannel.class;
/*  99 */         lazyLoadBase = eventLoops;
/* 100 */         logger.info("Using default channel type");
/*     */       } 
/*     */       
/* 103 */       this.endpoints.add(((ServerBootstrap)((ServerBootstrap)(new ServerBootstrap()).channel(clazz)).childHandler((ChannelHandler)new ChannelInitializer<Channel>()
/*     */             {
/*     */               
/*     */               protected void initChannel(Channel p_initChannel_1_) throws Exception
/*     */               {
/*     */                 try {
/* 109 */                   p_initChannel_1_.config().setOption(ChannelOption.TCP_NODELAY, Boolean.valueOf(true));
/*     */                 }
/* 111 */                 catch (ChannelException channelException) {}
/*     */ 
/*     */ 
/*     */ 
/*     */                 
/* 116 */                 p_initChannel_1_.pipeline().addLast("timeout", (ChannelHandler)new ReadTimeoutHandler(30)).addLast("legacy_query", (ChannelHandler)new PingResponseHandler(NetworkSystem.this)).addLast("splitter", (ChannelHandler)new MessageDeserializer2()).addLast("decoder", (ChannelHandler)new MessageDeserializer(EnumPacketDirection.SERVERBOUND)).addLast("prepender", (ChannelHandler)new MessageSerializer2()).addLast("encoder", (ChannelHandler)new MessageSerializer(EnumPacketDirection.CLIENTBOUND));
/* 117 */                 NetworkManager networkmanager = new NetworkManager(EnumPacketDirection.SERVERBOUND);
/* 118 */                 NetworkSystem.this.networkManagers.add(networkmanager);
/* 119 */                 p_initChannel_1_.pipeline().addLast("packet_handler", (ChannelHandler)networkmanager);
/* 120 */                 networkmanager.setNetHandler((INetHandler)new NetHandlerHandshakeTCP(NetworkSystem.this.mcServer, networkmanager));
/*     */               }
/* 122 */             }).group((EventLoopGroup)lazyLoadBase.getValue()).localAddress(address, port)).bind().syncUninterruptibly());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public SocketAddress addLocalEndpoint() {
/*     */     ChannelFuture channelfuture;
/* 130 */     synchronized (this.endpoints) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 141 */       channelfuture = ((ServerBootstrap)((ServerBootstrap)(new ServerBootstrap()).channel(LocalServerChannel.class)).childHandler((ChannelHandler)new ChannelInitializer<Channel>() { protected void initChannel(Channel p_initChannel_1_) throws Exception { NetworkManager networkmanager = new NetworkManager(EnumPacketDirection.SERVERBOUND); networkmanager.setNetHandler((INetHandler)new NetHandlerHandshakeMemory(NetworkSystem.this.mcServer, networkmanager)); NetworkSystem.this.networkManagers.add(networkmanager); p_initChannel_1_.pipeline().addLast("packet_handler", (ChannelHandler)networkmanager); } }).group((EventLoopGroup)eventLoops.getValue()).localAddress((SocketAddress)LocalAddress.ANY)).bind().syncUninterruptibly();
/* 142 */       this.endpoints.add(channelfuture);
/*     */     } 
/*     */     
/* 145 */     return channelfuture.channel().localAddress();
/*     */   }
/*     */ 
/*     */   
/*     */   public void terminateEndpoints() {
/* 150 */     this.isAlive = false;
/*     */     
/* 152 */     for (ChannelFuture channelfuture : this.endpoints) {
/*     */ 
/*     */       
/*     */       try {
/* 156 */         channelfuture.channel().close().sync();
/*     */       }
/* 158 */       catch (InterruptedException var4) {
/*     */         
/* 160 */         logger.error("Interrupted whilst closing channel");
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void networkTick() {
/* 167 */     synchronized (this.networkManagers) {
/*     */       
/* 169 */       Iterator<NetworkManager> iterator = this.networkManagers.iterator();
/*     */       
/* 171 */       while (iterator.hasNext()) {
/*     */         
/* 173 */         final NetworkManager networkmanager = iterator.next();
/*     */         
/* 175 */         if (!networkmanager.hasNoChannel()) {
/*     */           
/* 177 */           if (!networkmanager.isChannelOpen()) {
/*     */             
/* 179 */             iterator.remove();
/* 180 */             networkmanager.checkDisconnected();
/*     */             
/*     */             continue;
/*     */           } 
/*     */           
/*     */           try {
/* 186 */             networkmanager.processReceivedPackets();
/*     */           }
/* 188 */           catch (Exception exception) {
/*     */             
/* 190 */             if (networkmanager.isLocalChannel()) {
/*     */               
/* 192 */               CrashReport crashreport = CrashReport.makeCrashReport(exception, "Ticking memory connection");
/* 193 */               CrashReportCategory crashreportcategory = crashreport.makeCategory("Ticking connection");
/* 194 */               crashreportcategory.addCrashSectionCallable("Connection", new Callable<String>()
/*     */                   {
/*     */                     public String call() throws Exception
/*     */                     {
/* 198 */                       return networkmanager.toString();
/*     */                     }
/*     */                   });
/* 201 */               throw new ReportedException(crashreport);
/*     */             } 
/*     */             
/* 204 */             logger.warn("Failed to handle packet for " + networkmanager.getRemoteAddress(), exception);
/* 205 */             final ChatComponentText chatcomponenttext = new ChatComponentText("Internal server error");
/* 206 */             networkmanager.sendPacket((Packet)new S40PacketDisconnect((IChatComponent)chatcomponenttext), new GenericFutureListener<Future<? super Void>>()
/*     */                 {
/*     */                   public void operationComplete(Future<? super Void> p_operationComplete_1_) throws Exception
/*     */                   {
/* 210 */                     networkmanager.closeChannel((IChatComponent)chatcomponenttext);
/*     */                   }
/*     */                 },  (GenericFutureListener<? extends Future<? super Void>>[])new GenericFutureListener[0]);
/* 213 */             networkmanager.disableAutoRead();
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public MinecraftServer getServer() {
/* 223 */     return this.mcServer;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\network\NetworkSystem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */