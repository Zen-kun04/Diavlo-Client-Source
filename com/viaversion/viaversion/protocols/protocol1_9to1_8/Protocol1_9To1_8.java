/*     */ package com.viaversion.viaversion.protocols.protocol1_9to1_8;
/*     */ 
/*     */ import com.viaversion.viaversion.api.Via;
/*     */ import com.viaversion.viaversion.api.connection.StorableObject;
/*     */ import com.viaversion.viaversion.api.connection.UserConnection;
/*     */ import com.viaversion.viaversion.api.data.entity.EntityTracker;
/*     */ import com.viaversion.viaversion.api.minecraft.item.Item;
/*     */ import com.viaversion.viaversion.api.platform.providers.Provider;
/*     */ import com.viaversion.viaversion.api.platform.providers.ViaProviders;
/*     */ import com.viaversion.viaversion.api.protocol.AbstractProtocol;
/*     */ import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
/*     */ import com.viaversion.viaversion.api.protocol.packet.State;
/*     */ import com.viaversion.viaversion.api.protocol.remapper.ValueTransformer;
/*     */ import com.viaversion.viaversion.api.rewriter.EntityRewriter;
/*     */ import com.viaversion.viaversion.api.type.Type;
/*     */ import com.viaversion.viaversion.libs.gson.JsonElement;
/*     */ import com.viaversion.viaversion.libs.gson.JsonObject;
/*     */ import com.viaversion.viaversion.protocols.protocol1_8.ClientboundPackets1_8;
/*     */ import com.viaversion.viaversion.protocols.protocol1_8.ServerboundPackets1_8;
/*     */ import com.viaversion.viaversion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;
/*     */ import com.viaversion.viaversion.protocols.protocol1_9to1_8.metadata.MetadataRewriter1_9To1_8;
/*     */ import com.viaversion.viaversion.protocols.protocol1_9to1_8.packets.EntityPackets;
/*     */ import com.viaversion.viaversion.protocols.protocol1_9to1_8.packets.InventoryPackets;
/*     */ import com.viaversion.viaversion.protocols.protocol1_9to1_8.packets.PlayerPackets;
/*     */ import com.viaversion.viaversion.protocols.protocol1_9to1_8.packets.SpawnPackets;
/*     */ import com.viaversion.viaversion.protocols.protocol1_9to1_8.packets.WorldPackets;
/*     */ import com.viaversion.viaversion.protocols.protocol1_9to1_8.providers.BossBarProvider;
/*     */ import com.viaversion.viaversion.protocols.protocol1_9to1_8.providers.CommandBlockProvider;
/*     */ import com.viaversion.viaversion.protocols.protocol1_9to1_8.providers.CompressionProvider;
/*     */ import com.viaversion.viaversion.protocols.protocol1_9to1_8.providers.EntityIdProvider;
/*     */ import com.viaversion.viaversion.protocols.protocol1_9to1_8.providers.HandItemProvider;
/*     */ import com.viaversion.viaversion.protocols.protocol1_9to1_8.providers.MainHandProvider;
/*     */ import com.viaversion.viaversion.protocols.protocol1_9to1_8.providers.MovementTransmitterProvider;
/*     */ import com.viaversion.viaversion.protocols.protocol1_9to1_8.storage.ClientChunks;
/*     */ import com.viaversion.viaversion.protocols.protocol1_9to1_8.storage.CommandBlockStorage;
/*     */ import com.viaversion.viaversion.protocols.protocol1_9to1_8.storage.EntityTracker1_9;
/*     */ import com.viaversion.viaversion.protocols.protocol1_9to1_8.storage.InventoryTracker;
/*     */ import com.viaversion.viaversion.protocols.protocol1_9to1_8.storage.MovementTracker;
/*     */ import com.viaversion.viaversion.util.GsonUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Protocol1_9To1_8
/*     */   extends AbstractProtocol<ClientboundPackets1_8, ClientboundPackets1_9, ServerboundPackets1_8, ServerboundPackets1_9>
/*     */ {
/*  49 */   public static final ValueTransformer<String, JsonElement> FIX_JSON = new ValueTransformer<String, JsonElement>(Type.COMPONENT)
/*     */     {
/*     */       public JsonElement transform(PacketWrapper wrapper, String line) {
/*  52 */         return Protocol1_9To1_8.fixJson(line);
/*     */       }
/*     */     };
/*  55 */   private final MetadataRewriter1_9To1_8 metadataRewriter = new MetadataRewriter1_9To1_8(this);
/*     */   
/*     */   public Protocol1_9To1_8() {
/*  58 */     super(ClientboundPackets1_8.class, ClientboundPackets1_9.class, ServerboundPackets1_8.class, ServerboundPackets1_9.class);
/*     */   }
/*     */   
/*     */   public static JsonElement fixJson(String line) {
/*  62 */     if (line == null || line.equalsIgnoreCase("null")) {
/*  63 */       line = "{\"text\":\"\"}";
/*     */     } else {
/*  65 */       if ((!line.startsWith("\"") || !line.endsWith("\"")) && (!line.startsWith("{") || !line.endsWith("}"))) {
/*  66 */         return constructJson(line);
/*     */       }
/*  68 */       if (line.startsWith("\"") && line.endsWith("\"")) {
/*  69 */         line = "{\"text\":" + line + "}";
/*     */       }
/*     */     } 
/*     */     try {
/*  73 */       return (JsonElement)GsonUtil.getGson().fromJson(line, JsonObject.class);
/*  74 */     } catch (Exception e) {
/*  75 */       if (Via.getConfig().isForceJsonTransform()) {
/*  76 */         return constructJson(line);
/*     */       }
/*  78 */       Via.getPlatform().getLogger().warning("Invalid JSON String: \"" + line + "\" Please report this issue to the ViaVersion Github: " + e.getMessage());
/*  79 */       return (JsonElement)GsonUtil.getGson().fromJson("{\"text\":\"\"}", JsonObject.class);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static JsonElement constructJson(String text) {
/*  85 */     JsonObject jsonObject = new JsonObject();
/*  86 */     jsonObject.addProperty("text", text);
/*  87 */     return (JsonElement)jsonObject;
/*     */   }
/*     */   
/*     */   public static Item getHandItem(UserConnection info) {
/*  91 */     return ((HandItemProvider)Via.getManager().getProviders().get(HandItemProvider.class)).getHandItem(info);
/*     */   }
/*     */   
/*     */   public static boolean isSword(int id) {
/*  95 */     if (id == 267) return true; 
/*  96 */     if (id == 268) return true; 
/*  97 */     if (id == 272) return true; 
/*  98 */     if (id == 276) return true; 
/*  99 */     if (id == 283) return true;
/*     */     
/* 101 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerPackets() {
/* 106 */     this.metadataRewriter.register();
/*     */ 
/*     */     
/* 109 */     registerClientbound(State.LOGIN, 0, 0, wrapper -> {
/*     */           if (wrapper.isReadable(Type.COMPONENT, 0)) {
/*     */             return;
/*     */           }
/*     */ 
/*     */           
/*     */           wrapper.write(Type.COMPONENT, fixJson((String)wrapper.read(Type.STRING)));
/*     */         });
/*     */ 
/*     */     
/* 119 */     SpawnPackets.register(this);
/* 120 */     InventoryPackets.register(this);
/* 121 */     EntityPackets.register(this);
/* 122 */     PlayerPackets.register(this);
/* 123 */     WorldPackets.register(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public void register(ViaProviders providers) {
/* 128 */     providers.register(HandItemProvider.class, (Provider)new HandItemProvider());
/* 129 */     providers.register(CommandBlockProvider.class, (Provider)new CommandBlockProvider());
/* 130 */     providers.register(EntityIdProvider.class, (Provider)new EntityIdProvider());
/* 131 */     providers.register(BossBarProvider.class, (Provider)new BossBarProvider());
/* 132 */     providers.register(MainHandProvider.class, (Provider)new MainHandProvider());
/* 133 */     providers.register(CompressionProvider.class, (Provider)new CompressionProvider());
/* 134 */     providers.register(MovementTransmitterProvider.class, (Provider)new MovementTransmitterProvider());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void init(UserConnection userConnection) {
/* 140 */     userConnection.addEntityTracker(getClass(), (EntityTracker)new EntityTracker1_9(userConnection));
/*     */     
/* 142 */     userConnection.put((StorableObject)new ClientChunks(userConnection));
/*     */     
/* 144 */     userConnection.put((StorableObject)new MovementTracker());
/*     */     
/* 146 */     userConnection.put((StorableObject)new InventoryTracker());
/*     */     
/* 148 */     userConnection.put((StorableObject)new CommandBlockStorage());
/*     */     
/* 150 */     if (!userConnection.has(ClientWorld.class)) {
/* 151 */       userConnection.put((StorableObject)new ClientWorld(userConnection));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public MetadataRewriter1_9To1_8 getEntityRewriter() {
/* 157 */     return this.metadataRewriter;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\protocols\protocol1_9to1_8\Protocol1_9To1_8.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */