/*     */ package net.minecraft.network;
/*     */ import com.google.common.util.concurrent.ThreadFactoryBuilder;
/*     */ import com.viaversion.viaversion.api.connection.UserConnection;
/*     */ import com.viaversion.viaversion.connection.UserConnectionImpl;
/*     */ import com.viaversion.viaversion.protocol.ProtocolPipelineImpl;
/*     */ import io.netty.bootstrap.Bootstrap;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelException;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelFutureListener;
/*     */ import io.netty.channel.ChannelHandler;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelInitializer;
/*     */ import io.netty.channel.ChannelOption;
/*     */ import io.netty.channel.EventLoopGroup;
/*     */ import io.netty.channel.SimpleChannelInboundHandler;
/*     */ import io.netty.channel.epoll.Epoll;
/*     */ import io.netty.channel.epoll.EpollEventLoopGroup;
/*     */ import io.netty.channel.epoll.EpollSocketChannel;
/*     */ import io.netty.channel.local.LocalChannel;
/*     */ import io.netty.channel.local.LocalEventLoopGroup;
/*     */ import io.netty.channel.nio.NioEventLoopGroup;
/*     */ import io.netty.channel.socket.nio.NioSocketChannel;
/*     */ import io.netty.handler.timeout.ReadTimeoutHandler;
/*     */ import io.netty.util.AttributeKey;
/*     */ import io.netty.util.concurrent.Future;
/*     */ import io.netty.util.concurrent.GenericFutureListener;
/*     */ import java.net.InetAddress;
/*     */ import java.net.SocketAddress;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.locks.ReentrantReadWriteLock;
/*     */ import javax.crypto.SecretKey;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.network.handshake.client.C00Handshake;
/*     */ import net.minecraft.util.ChatComponentTranslation;
/*     */ import net.minecraft.util.CryptManager;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import net.minecraft.util.LazyLoadBase;
/*     */ import net.minecraft.util.MessageDeserializer;
/*     */ import net.minecraft.util.MessageDeserializer2;
/*     */ import net.minecraft.util.MessageSerializer;
/*     */ import net.minecraft.util.MessageSerializer2;
/*     */ import org.apache.commons.lang3.ArrayUtils;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.Marker;
/*     */ import org.apache.logging.log4j.MarkerManager;
/*     */ import rip.diavlo.base.Client;
/*     */ import rip.diavlo.base.events.network.ReceivePacketEvent;
/*     */ import rip.diavlo.base.events.network.SendPacketEvent;
/*     */ import rip.diavlo.base.viaversion.vialoadingbase.ViaLoadingBase;
/*     */ import rip.diavlo.base.viaversion.vialoadingbase.netty.event.CompressionReorderEvent;
/*     */ import rip.diavlo.base.viaversion.viamcp.MCPVLBPipeline;
/*     */ import viamcp.utils.NettyUtil;
/*     */ 
/*     */ public class NetworkManager extends SimpleChannelInboundHandler<Packet> {
/*  57 */   private static final Logger logger = LogManager.getLogger();
/*     */   
/*  59 */   private static final Logger LOGGER = null;
/*     */   
/*  61 */   public static final Marker logMarkerNetwork = MarkerManager.getMarker("NETWORK");
/*  62 */   public static final Marker logMarkerPackets = MarkerManager.getMarker("NETWORK_PACKETS", logMarkerNetwork);
/*  63 */   public static final AttributeKey<EnumConnectionState> attrKeyConnectionState = AttributeKey.valueOf("protocol");
/*  64 */   public static final LazyLoadBase<NioEventLoopGroup> CLIENT_NIO_EVENTLOOP = new LazyLoadBase<NioEventLoopGroup>()
/*     */     {
/*     */       protected NioEventLoopGroup load()
/*     */       {
/*  68 */         return new NioEventLoopGroup(0, (new ThreadFactoryBuilder()).setNameFormat("Netty Client IO #%d").setDaemon(true).build());
/*     */       }
/*     */     };
/*  71 */   public static final LazyLoadBase<EpollEventLoopGroup> CLIENT_EPOLL_EVENTLOOP = new LazyLoadBase<EpollEventLoopGroup>()
/*     */     {
/*     */       protected EpollEventLoopGroup load()
/*     */       {
/*  75 */         return new EpollEventLoopGroup(0, (new ThreadFactoryBuilder()).setNameFormat("Netty Epoll Client IO #%d").setDaemon(true).build());
/*     */       }
/*     */     };
/*  78 */   public static final LazyLoadBase<LocalEventLoopGroup> CLIENT_LOCAL_EVENTLOOP = new LazyLoadBase<LocalEventLoopGroup>()
/*     */     {
/*     */       protected LocalEventLoopGroup load()
/*     */       {
/*  82 */         return new LocalEventLoopGroup(0, (new ThreadFactoryBuilder()).setNameFormat("Netty Local Client IO #%d").setDaemon(true).build());
/*     */       }
/*     */     };
/*     */   private final EnumPacketDirection direction;
/*  86 */   private final Queue<InboundHandlerTuplePacketListener> outboundPacketsQueue = Queues.newConcurrentLinkedQueue();
/*  87 */   private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
/*     */   
/*     */   public Channel channel;
/*     */   private SocketAddress socketAddress;
/*     */   private INetHandler packetListener;
/*     */   private IChatComponent terminationReason;
/*     */   private boolean isEncrypted;
/*     */   private boolean disconnected;
/*     */   
/*     */   public NetworkManager(EnumPacketDirection packetDirection) {
/*  97 */     this.direction = packetDirection;
/*     */   }
/*     */ 
/*     */   
/*     */   public void channelActive(ChannelHandlerContext p_channelActive_1_) throws Exception {
/* 102 */     super.channelActive(p_channelActive_1_);
/* 103 */     this.channel = p_channelActive_1_.channel();
/* 104 */     this.socketAddress = this.channel.remoteAddress();
/*     */ 
/*     */     
/*     */     try {
/* 108 */       setConnectionState(EnumConnectionState.HANDSHAKING);
/*     */     }
/* 110 */     catch (Throwable throwable) {
/*     */       
/* 112 */       logger.fatal(throwable);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setConnectionState(EnumConnectionState newState) {
/* 118 */     this.channel.attr(attrKeyConnectionState).set(newState);
/* 119 */     this.channel.config().setAutoRead(true);
/* 120 */     logger.debug("Enabled auto read");
/*     */   }
/*     */ 
/*     */   
/*     */   public void channelInactive(ChannelHandlerContext p_channelInactive_1_) throws Exception {
/* 125 */     closeChannel((IChatComponent)new ChatComponentTranslation("disconnect.endOfStream", new Object[0]));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void exceptionCaught(ChannelHandlerContext p_exceptionCaught_1_, Throwable p_exceptionCaught_2_) throws Exception {
/*     */     ChatComponentTranslation chatcomponenttranslation;
/* 132 */     if (p_exceptionCaught_2_ instanceof io.netty.handler.timeout.TimeoutException) {
/*     */       
/* 134 */       chatcomponenttranslation = new ChatComponentTranslation("disconnect.timeout", new Object[0]);
/*     */     }
/*     */     else {
/*     */       
/* 138 */       chatcomponenttranslation = new ChatComponentTranslation("disconnect.genericReason", new Object[] { "Internal Exception: " + p_exceptionCaught_2_ });
/*     */     } 
/*     */     
/* 141 */     closeChannel((IChatComponent)chatcomponenttranslation);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void channelRead0(ChannelHandlerContext p_channelRead0_1_, Packet p_channelRead0_2_) throws Exception {
/* 146 */     if (this.channel.isOpen()) {
/*     */       
/*     */       try {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 153 */         ReceivePacketEvent event = new ReceivePacketEvent(p_channelRead0_2_);
/* 154 */         Client.getInstance().getEventBus().post(event);
/*     */         
/* 156 */         if (event.isCancelled())
/*     */           return; 
/* 158 */         event.getPacket().processPacket(this.packetListener);
/*     */       }
/* 160 */       catch (ThreadQuickExitException threadQuickExitException) {}
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNetHandler(INetHandler handler) {
/* 169 */     Validate.notNull(handler, "packetListener", new Object[0]);
/* 170 */     logger.debug("Set listener of {} to {}", new Object[] { this, handler });
/* 171 */     this.packetListener = handler;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendPacket(Packet packetIn) {
/* 179 */     SendPacketEvent event = new SendPacketEvent(packetIn);
/* 180 */     Client.getInstance().getEventBus().post(event);
/*     */     
/* 182 */     if (event.isCancelled())
/*     */       return; 
/* 184 */     if (isChannelOpen()) {
/*     */       
/* 186 */       flushOutboundQueue();
/* 187 */       dispatchPacket(event.getPacket(), (GenericFutureListener<? extends Future<? super Void>>[])null);
/*     */     }
/*     */     else {
/*     */       
/* 191 */       this.readWriteLock.writeLock().lock();
/*     */ 
/*     */       
/*     */       try {
/* 195 */         this.outboundPacketsQueue.add(new InboundHandlerTuplePacketListener(event.getPacket(), (GenericFutureListener<? extends Future<? super Void>>[])null));
/*     */       }
/*     */       finally {
/*     */         
/* 199 */         this.readWriteLock.writeLock().unlock();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void sendPacketNoEvent(Packet packetIn) {
/* 206 */     if (isChannelOpen()) {
/*     */       
/* 208 */       flushOutboundQueue();
/* 209 */       dispatchPacket(packetIn, (GenericFutureListener<? extends Future<? super Void>>[])null);
/*     */     }
/*     */     else {
/*     */       
/* 213 */       this.readWriteLock.writeLock().lock();
/*     */ 
/*     */       
/*     */       try {
/* 217 */         this.outboundPacketsQueue.add(new InboundHandlerTuplePacketListener(packetIn, (GenericFutureListener<? extends Future<? super Void>>[])null));
/*     */       }
/*     */       finally {
/*     */         
/* 221 */         this.readWriteLock.writeLock().unlock();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void sendPacket(Packet packetIn, GenericFutureListener<? extends Future<? super Void>> listener, GenericFutureListener<? extends Future<? super Void>>... listeners) {
/* 228 */     if (isChannelOpen()) {
/*     */       
/* 230 */       flushOutboundQueue();
/* 231 */       dispatchPacket(packetIn, (GenericFutureListener<? extends Future<? super Void>>[])ArrayUtils.add((Object[])listeners, 0, listener));
/*     */     }
/*     */     else {
/*     */       
/* 235 */       this.readWriteLock.writeLock().lock();
/*     */ 
/*     */       
/*     */       try {
/* 239 */         this.outboundPacketsQueue.add(new InboundHandlerTuplePacketListener(packetIn, (GenericFutureListener<? extends Future<? super Void>>[])ArrayUtils.add((Object[])listeners, 0, listener)));
/*     */       }
/*     */       finally {
/*     */         
/* 243 */         this.readWriteLock.writeLock().unlock();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void dispatchPacket(final Packet inPacket, final GenericFutureListener<? extends Future<? super Void>>[] futureListeners) {
/* 250 */     final EnumConnectionState enumconnectionstate = EnumConnectionState.getFromPacket(inPacket);
/* 251 */     final EnumConnectionState enumconnectionstate1 = (EnumConnectionState)this.channel.attr(attrKeyConnectionState).get();
/*     */ 
/*     */     
/* 254 */     if (inPacket instanceof C00Handshake) {
/* 255 */       if (Client.getInstance().getSpoofingUtil().isBungeeHack() && !Client.getInstance().getSpoofingUtil().isPremiumSession() && !Client.getInstance().getSpoofingUtil().isPremiumUUID() && ((C00Handshake)inPacket)
/* 256 */         .getRequestedState() == EnumConnectionState.LOGIN) {
/* 257 */         ((C00Handshake)inPacket)
/* 258 */           .setIp(((C00Handshake)inPacket).getIp() + "\000" + Client.getInstance().getSpoofingUtil().getIpBungeeHack() + "\000" + 
/* 259 */             UUID.nameUUIDFromBytes(("OfflinePlayer:" + Client.getInstance().getSpoofingUtil().getFakeNick()).getBytes()).toString()
/* 260 */             .replace("-", ""));
/* 261 */         System.out.println(((C00Handshake)inPacket).getIp());
/* 262 */         System.out.println(Client.getInstance().getSpoofingUtil().getFakeNick());
/*     */       } 
/* 264 */       if (Client.getInstance().getSpoofingUtil().isBungeeHack() && !Client.getInstance().getSpoofingUtil().isPremiumSession() && Client.getInstance().getSpoofingUtil().isPremiumUUID() && ((C00Handshake)inPacket)
/* 265 */         .getRequestedState() == EnumConnectionState.LOGIN) {
/* 266 */         ((C00Handshake)inPacket).setIp(((C00Handshake)inPacket).getIp() + "\000" + 
/* 267 */             Client.getInstance().getSpoofingUtil().getIpBungeeHack() + "\000" + Client.getInstance().getSpoofingUtil().getPreUUID());
/* 268 */         System.out.println(((C00Handshake)inPacket).getIp());
/* 269 */         System.out.println(Minecraft.getMinecraft().getSession().getUsername());
/*     */       } 
/*     */       
/* 272 */       if (Client.getInstance().getSpoofingUtil().isBungeeHack() && Client.getInstance().getSpoofingUtil().isPremiumSession() && ((C00Handshake)inPacket)
/* 273 */         .getRequestedState() == EnumConnectionState.LOGIN) {
/* 274 */         ((C00Handshake)inPacket).setIp(((C00Handshake)inPacket)
/* 275 */             .getIp() + "\000" + Client.getInstance().getSpoofingUtil().getIpBungeeHack() + Client.getInstance().getSpoofingUtil().getPreUUID());
/* 276 */         System.out.println(((C00Handshake)inPacket).getIp());
/* 277 */         System.out.println(Minecraft.getMinecraft().getSession().getUsername());
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 282 */     if (enumconnectionstate1 != enumconnectionstate) {
/*     */       
/* 284 */       logger.debug("Disabled auto read");
/* 285 */       this.channel.config().setAutoRead(false);
/*     */     } 
/*     */     
/* 288 */     if (this.channel.eventLoop().inEventLoop()) {
/*     */       
/* 290 */       if (enumconnectionstate != enumconnectionstate1)
/*     */       {
/* 292 */         setConnectionState(enumconnectionstate);
/*     */       }
/*     */       
/* 295 */       ChannelFuture channelfuture = this.channel.writeAndFlush(inPacket);
/*     */       
/* 297 */       if (futureListeners != null)
/*     */       {
/* 299 */         channelfuture.addListeners((GenericFutureListener[])futureListeners);
/*     */       }
/*     */       
/* 302 */       channelfuture.addListener((GenericFutureListener)ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
/*     */     }
/*     */     else {
/*     */       
/* 306 */       this.channel.eventLoop().execute(new Runnable()
/*     */           {
/*     */             public void run()
/*     */             {
/* 310 */               if (enumconnectionstate != enumconnectionstate1)
/*     */               {
/* 312 */                 NetworkManager.this.setConnectionState(enumconnectionstate);
/*     */               }
/*     */               
/* 315 */               ChannelFuture channelfuture1 = NetworkManager.this.channel.writeAndFlush(inPacket);
/*     */               
/* 317 */               if (futureListeners != null)
/*     */               {
/* 319 */                 channelfuture1.addListeners(futureListeners);
/*     */               }
/*     */               
/* 322 */               channelfuture1.addListener((GenericFutureListener)ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
/*     */             }
/*     */           });
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void flushOutboundQueue() {
/* 330 */     if (this.channel != null && this.channel.isOpen()) {
/*     */       
/* 332 */       this.readWriteLock.readLock().lock();
/*     */ 
/*     */       
/*     */       try {
/* 336 */         while (!this.outboundPacketsQueue.isEmpty())
/*     */         {
/* 338 */           InboundHandlerTuplePacketListener networkmanager$inboundhandlertuplepacketlistener = this.outboundPacketsQueue.poll();
/* 339 */           dispatchPacket(networkmanager$inboundhandlertuplepacketlistener.packet, networkmanager$inboundhandlertuplepacketlistener.futureListeners);
/*     */         }
/*     */       
/*     */       } finally {
/*     */         
/* 344 */         this.readWriteLock.readLock().unlock();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void processReceivedPackets() {
/* 351 */     flushOutboundQueue();
/*     */     
/* 353 */     if (this.packetListener instanceof ITickable)
/*     */     {
/* 355 */       ((ITickable)this.packetListener).update();
/*     */     }
/*     */     
/* 358 */     this.channel.flush();
/*     */   }
/*     */ 
/*     */   
/*     */   public SocketAddress getRemoteAddress() {
/* 363 */     return this.socketAddress;
/*     */   }
/*     */ 
/*     */   
/*     */   public void closeChannel(IChatComponent message) {
/* 368 */     if (this.channel.isOpen()) {
/*     */       
/* 370 */       this.channel.close().awaitUninterruptibly();
/* 371 */       this.terminationReason = message;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isLocalChannel() {
/* 377 */     return (this.channel instanceof LocalChannel || this.channel instanceof io.netty.channel.local.LocalServerChannel);
/*     */   }
/*     */   public static NetworkManager createNetworkManagerAndConnect(InetAddress address, int serverPort, boolean useNativeTransport) {
/*     */     Class<NioSocketChannel> clazz;
/*     */     LazyLoadBase<NioEventLoopGroup> lazyLoadBase;
/* 382 */     final NetworkManager networkmanager = new NetworkManager(EnumPacketDirection.CLIENTBOUND);
/*     */ 
/*     */ 
/*     */     
/* 386 */     if (Epoll.isAvailable() && useNativeTransport) {
/*     */       
/* 388 */       Class<EpollSocketChannel> clazz1 = EpollSocketChannel.class;
/* 389 */       LazyLoadBase<EpollEventLoopGroup> lazyLoadBase1 = CLIENT_EPOLL_EVENTLOOP;
/*     */     }
/*     */     else {
/*     */       
/* 393 */       clazz = NioSocketChannel.class;
/* 394 */       lazyLoadBase = CLIENT_NIO_EVENTLOOP;
/*     */     } 
/*     */     
/* 397 */     ((Bootstrap)((Bootstrap)((Bootstrap)(new Bootstrap()).group((EventLoopGroup)lazyLoadBase.getValue())).handler((ChannelHandler)new ChannelInitializer<Channel>()
/*     */         {
/*     */           
/*     */           protected void initChannel(Channel p_initChannel_1_) throws Exception
/*     */           {
/*     */             try {
/* 403 */               p_initChannel_1_.config().setOption(ChannelOption.TCP_NODELAY, Boolean.valueOf(true));
/*     */             }
/* 405 */             catch (ChannelException channelException) {}
/*     */ 
/*     */ 
/*     */             
/* 409 */             p_initChannel_1_.pipeline().addLast("timeout", (ChannelHandler)new ReadTimeoutHandler(30)).addLast("splitter", (ChannelHandler)new MessageDeserializer2()).addLast("decoder", (ChannelHandler)new MessageDeserializer(EnumPacketDirection.CLIENTBOUND)).addLast("prepender", (ChannelHandler)new MessageSerializer2()).addLast("encoder", (ChannelHandler)new MessageSerializer(EnumPacketDirection.SERVERBOUND)).addLast("packet_handler", (ChannelHandler)networkmanager);
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
/* 420 */             if (p_initChannel_1_ instanceof io.netty.channel.socket.SocketChannel && ViaLoadingBase.getInstance().getTargetVersion().getVersion() != 47)
/*     */             {
/* 422 */               UserConnectionImpl userConnectionImpl = new UserConnectionImpl(p_initChannel_1_, true);
/* 423 */               new ProtocolPipelineImpl((UserConnection)userConnectionImpl);
/* 424 */               p_initChannel_1_.pipeline().addLast(new ChannelHandler[] { (ChannelHandler)new MCPVLBPipeline((UserConnection)userConnectionImpl) });
/*     */ 
/*     */             
/*     */             }
/*     */           
/*     */           }
/* 430 */         })).channel(clazz)).connect(address, serverPort).syncUninterruptibly();
/* 431 */     return networkmanager;
/*     */   }
/*     */ 
/*     */   
/*     */   public static NetworkManager provideLocalClient(SocketAddress address) {
/* 436 */     final NetworkManager networkmanager = new NetworkManager(EnumPacketDirection.CLIENTBOUND);
/* 437 */     ((Bootstrap)((Bootstrap)((Bootstrap)(new Bootstrap()).group((EventLoopGroup)CLIENT_LOCAL_EVENTLOOP.getValue())).handler((ChannelHandler)new ChannelInitializer<Channel>()
/*     */         {
/*     */           protected void initChannel(Channel p_initChannel_1_) throws Exception
/*     */           {
/* 441 */             p_initChannel_1_.pipeline().addLast("packet_handler", (ChannelHandler)networkmanager);
/*     */           }
/* 443 */         })).channel(LocalChannel.class)).connect(address).syncUninterruptibly();
/* 444 */     return networkmanager;
/*     */   }
/*     */ 
/*     */   
/*     */   public void enableEncryption(SecretKey key) {
/* 449 */     this.isEncrypted = true;
/* 450 */     this.channel.pipeline().addBefore("splitter", "decrypt", (ChannelHandler)new NettyEncryptingDecoder(CryptManager.createNetCipherInstance(2, key)));
/* 451 */     this.channel.pipeline().addBefore("prepender", "encrypt", (ChannelHandler)new NettyEncryptingEncoder(CryptManager.createNetCipherInstance(1, key)));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getIsencrypted() {
/* 456 */     return this.isEncrypted;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isChannelOpen() {
/* 461 */     return (this.channel != null && this.channel.isOpen());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasNoChannel() {
/* 466 */     return (this.channel == null);
/*     */   }
/*     */ 
/*     */   
/*     */   public INetHandler getNetHandler() {
/* 471 */     return this.packetListener;
/*     */   }
/*     */ 
/*     */   
/*     */   public IChatComponent getExitMessage() {
/* 476 */     return this.terminationReason;
/*     */   }
/*     */ 
/*     */   
/*     */   public void disableAutoRead() {
/* 481 */     this.channel.config().setAutoRead(false);
/*     */   }
/*     */   
/*     */   public void setCompressionTreshold(int treshold) {
/* 485 */     if (treshold >= 0) {
/* 486 */       if (this.channel.pipeline().get("decompress") instanceof NettyCompressionDecoder) {
/* 487 */         ((NettyCompressionDecoder)this.channel.pipeline().get("decompress")).setCompressionTreshold(treshold);
/*     */       } else {
/*     */         
/* 490 */         NettyUtil.decodeEncodePlacement(this.channel.pipeline(), "decoder", "decompress", (ChannelHandler)new NettyCompressionDecoder(treshold));
/*     */       } 
/* 492 */       if (this.channel.pipeline().get("compress") instanceof NettyCompressionEncoder) {
/* 493 */         ((NettyCompressionEncoder)this.channel.pipeline().get("decompress")).setCompressionTreshold(treshold);
/*     */       } else {
/*     */         
/* 496 */         NettyUtil.decodeEncodePlacement(this.channel.pipeline(), "encoder", "compress", (ChannelHandler)new NettyCompressionEncoder(treshold));
/*     */       } 
/*     */     } else {
/*     */       
/* 500 */       if (this.channel.pipeline().get("decompress") instanceof NettyCompressionDecoder)
/* 501 */         this.channel.pipeline().remove("decompress"); 
/* 502 */       if (this.channel.pipeline().get("compress") instanceof NettyCompressionEncoder)
/* 503 */         this.channel.pipeline().remove("compress"); 
/*     */     } 
/* 505 */     this.channel.pipeline().fireUserEventTriggered(new CompressionReorderEvent());
/*     */   }
/*     */ 
/*     */   
/*     */   public void checkDisconnected() {
/* 510 */     if (this.channel != null && !this.channel.isOpen())
/*     */     {
/* 512 */       if (!this.disconnected) {
/*     */         
/* 514 */         this.disconnected = true;
/*     */         
/* 516 */         if (getExitMessage() != null)
/*     */         {
/* 518 */           getNetHandler().onDisconnect(getExitMessage());
/*     */         }
/* 520 */         else if (getNetHandler() != null)
/*     */         {
/* 522 */           getNetHandler().onDisconnect((IChatComponent)new ChatComponentText("Disconnected"));
/*     */         }
/*     */       
/*     */       } else {
/*     */         
/* 527 */         logger.warn("handleDisconnection() called twice");
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static class InboundHandlerTuplePacketListener
/*     */   {
/*     */     private final Packet packet;
/*     */     private final GenericFutureListener<? extends Future<? super Void>>[] futureListeners;
/*     */     
/*     */     public InboundHandlerTuplePacketListener(Packet inPacket, GenericFutureListener<? extends Future<? super Void>>... inFutureListeners) {
/* 539 */       this.packet = inPacket;
/* 540 */       this.futureListeners = inFutureListeners;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\network\NetworkManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */