/*     */ package com.viaversion.viaversion.protocols.protocol1_19to1_18_2.packets;
/*     */ 
/*     */ import com.viaversion.viaversion.api.Via;
/*     */ import com.viaversion.viaversion.api.data.ParticleMappings;
/*     */ import com.viaversion.viaversion.api.minecraft.item.Item;
/*     */ import com.viaversion.viaversion.api.protocol.Protocol;
/*     */ import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
/*     */ import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
/*     */ import com.viaversion.viaversion.api.protocol.packet.ServerboundPacketType;
/*     */ import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
/*     */ import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
/*     */ import com.viaversion.viaversion.api.type.Type;
/*     */ import com.viaversion.viaversion.protocols.protocol1_18to1_17_1.ClientboundPackets1_18;
/*     */ import com.viaversion.viaversion.protocols.protocol1_19to1_18_2.Protocol1_19To1_18_2;
/*     */ import com.viaversion.viaversion.protocols.protocol1_19to1_18_2.ServerboundPackets1_19;
/*     */ import com.viaversion.viaversion.protocols.protocol1_19to1_18_2.provider.AckSequenceProvider;
/*     */ import com.viaversion.viaversion.rewriter.ItemRewriter;
/*     */ import com.viaversion.viaversion.rewriter.RecipeRewriter;
/*     */ import com.viaversion.viaversion.util.Key;
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
/*     */ 
/*     */ 
/*     */ public final class InventoryPackets
/*     */   extends ItemRewriter<ClientboundPackets1_18, ServerboundPackets1_19, Protocol1_19To1_18_2>
/*     */ {
/*     */   public InventoryPackets(Protocol1_19To1_18_2 protocol) {
/*  36 */     super((Protocol)protocol);
/*     */   }
/*     */ 
/*     */   
/*     */   public void registerPackets() {
/*  41 */     registerSetCooldown((ClientboundPacketType)ClientboundPackets1_18.COOLDOWN);
/*  42 */     registerWindowItems1_17_1((ClientboundPacketType)ClientboundPackets1_18.WINDOW_ITEMS);
/*  43 */     registerSetSlot1_17_1((ClientboundPacketType)ClientboundPackets1_18.SET_SLOT);
/*  44 */     registerAdvancements((ClientboundPacketType)ClientboundPackets1_18.ADVANCEMENTS, Type.FLAT_VAR_INT_ITEM);
/*  45 */     registerEntityEquipmentArray((ClientboundPacketType)ClientboundPackets1_18.ENTITY_EQUIPMENT);
/*  46 */     ((Protocol1_19To1_18_2)this.protocol).registerClientbound((ClientboundPacketType)ClientboundPackets1_18.SPAWN_PARTICLE, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/*  49 */             map((Type)Type.INT, (Type)Type.VAR_INT);
/*  50 */             map((Type)Type.BOOLEAN);
/*  51 */             map((Type)Type.DOUBLE);
/*  52 */             map((Type)Type.DOUBLE);
/*  53 */             map((Type)Type.DOUBLE);
/*  54 */             map((Type)Type.FLOAT);
/*  55 */             map((Type)Type.FLOAT);
/*  56 */             map((Type)Type.FLOAT);
/*  57 */             map((Type)Type.FLOAT);
/*  58 */             map((Type)Type.INT);
/*  59 */             handler(wrapper -> {
/*     */                   int id = ((Integer)wrapper.get((Type)Type.VAR_INT, 0)).intValue();
/*     */                   
/*     */                   ParticleMappings particleMappings = ((Protocol1_19To1_18_2)InventoryPackets.this.protocol).getMappingData().getParticleMappings();
/*     */                   if (id == particleMappings.id("vibration")) {
/*     */                     wrapper.read(Type.POSITION1_14);
/*     */                     String resourceLocation = Key.stripMinecraftNamespace((String)wrapper.passthrough(Type.STRING));
/*     */                     if (resourceLocation.equals("entity")) {
/*     */                       wrapper.passthrough((Type)Type.VAR_INT);
/*     */                       wrapper.write((Type)Type.FLOAT, Float.valueOf(0.0F));
/*     */                     } 
/*     */                   } 
/*     */                 });
/*  72 */             handler(InventoryPackets.this.getSpawnParticleHandler((Type)Type.VAR_INT));
/*     */           }
/*     */         });
/*     */     
/*  76 */     registerClickWindow1_17_1((ServerboundPacketType)ServerboundPackets1_19.CLICK_WINDOW);
/*  77 */     registerCreativeInvAction((ServerboundPacketType)ServerboundPackets1_19.CREATIVE_INVENTORY_ACTION, Type.FLAT_VAR_INT_ITEM);
/*     */     
/*  79 */     registerWindowPropertyEnchantmentHandler((ClientboundPacketType)ClientboundPackets1_18.WINDOW_PROPERTY);
/*     */     
/*  81 */     ((Protocol1_19To1_18_2)this.protocol).registerClientbound((ClientboundPacketType)ClientboundPackets1_18.TRADE_LIST, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/*  84 */             map((Type)Type.VAR_INT);
/*  85 */             handler(wrapper -> {
/*     */                   int size = ((Short)wrapper.read((Type)Type.UNSIGNED_BYTE)).shortValue();
/*     */                   
/*     */                   wrapper.write((Type)Type.VAR_INT, Integer.valueOf(size));
/*     */                   
/*     */                   for (int i = 0; i < size; i++) {
/*     */                     InventoryPackets.this.handleItemToClient((Item)wrapper.passthrough(Type.FLAT_VAR_INT_ITEM));
/*     */                     
/*     */                     InventoryPackets.this.handleItemToClient((Item)wrapper.passthrough(Type.FLAT_VAR_INT_ITEM));
/*     */                     if (((Boolean)wrapper.read((Type)Type.BOOLEAN)).booleanValue()) {
/*     */                       InventoryPackets.this.handleItemToClient((Item)wrapper.passthrough(Type.FLAT_VAR_INT_ITEM));
/*     */                     } else {
/*     */                       wrapper.write(Type.FLAT_VAR_INT_ITEM, null);
/*     */                     } 
/*     */                     wrapper.passthrough((Type)Type.BOOLEAN);
/*     */                     wrapper.passthrough((Type)Type.INT);
/*     */                     wrapper.passthrough((Type)Type.INT);
/*     */                     wrapper.passthrough((Type)Type.INT);
/*     */                     wrapper.passthrough((Type)Type.INT);
/*     */                     wrapper.passthrough((Type)Type.FLOAT);
/*     */                     wrapper.passthrough((Type)Type.INT);
/*     */                   } 
/*     */                 });
/*     */           }
/*     */         });
/* 110 */     ((Protocol1_19To1_18_2)this.protocol).registerServerbound((ServerboundPacketType)ServerboundPackets1_19.PLAYER_DIGGING, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/* 113 */             map((Type)Type.VAR_INT);
/* 114 */             map(Type.POSITION1_14);
/* 115 */             map((Type)Type.UNSIGNED_BYTE);
/* 116 */             handler(InventoryPackets.this.sequenceHandler());
/*     */           }
/*     */         });
/* 119 */     ((Protocol1_19To1_18_2)this.protocol).registerServerbound((ServerboundPacketType)ServerboundPackets1_19.PLAYER_BLOCK_PLACEMENT, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/* 122 */             map((Type)Type.VAR_INT);
/* 123 */             map(Type.POSITION1_14);
/* 124 */             map((Type)Type.VAR_INT);
/* 125 */             map((Type)Type.FLOAT);
/* 126 */             map((Type)Type.FLOAT);
/* 127 */             map((Type)Type.FLOAT);
/* 128 */             map((Type)Type.BOOLEAN);
/* 129 */             handler(InventoryPackets.this.sequenceHandler());
/*     */           }
/*     */         });
/* 132 */     ((Protocol1_19To1_18_2)this.protocol).registerServerbound((ServerboundPacketType)ServerboundPackets1_19.USE_ITEM, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/* 135 */             map((Type)Type.VAR_INT);
/* 136 */             handler(InventoryPackets.this.sequenceHandler());
/*     */           }
/*     */         });
/*     */     
/* 140 */     (new RecipeRewriter(this.protocol)).register((ClientboundPacketType)ClientboundPackets1_18.DECLARE_RECIPES);
/*     */   }
/*     */   
/*     */   private PacketHandler sequenceHandler() {
/* 144 */     return wrapper -> {
/*     */         int sequence = ((Integer)wrapper.read((Type)Type.VAR_INT)).intValue();
/*     */         AckSequenceProvider provider = (AckSequenceProvider)Via.getManager().getProviders().get(AckSequenceProvider.class);
/*     */         provider.handleSequence(wrapper.user(), sequence);
/*     */       };
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\protocols\protocol1_19to1_18_2\packets\InventoryPackets.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */