/*     */ package com.viaversion.viabackwards.protocol.protocol1_12_2to1_13.packets;
/*     */ 
/*     */ import com.google.common.base.Joiner;
/*     */ import com.viaversion.viabackwards.ViaBackwards;
/*     */ import com.viaversion.viabackwards.protocol.protocol1_12_2to1_13.Protocol1_12_2To1_13;
/*     */ import com.viaversion.viabackwards.protocol.protocol1_12_2to1_13.data.ParticleMapping;
/*     */ import com.viaversion.viabackwards.protocol.protocol1_12_2to1_13.storage.TabCompleteStorage;
/*     */ import com.viaversion.viabackwards.utils.ChatUtil;
/*     */ import com.viaversion.viaversion.api.Via;
/*     */ import com.viaversion.viaversion.api.minecraft.Position;
/*     */ import com.viaversion.viaversion.api.minecraft.item.Item;
/*     */ import com.viaversion.viaversion.api.protocol.Protocol;
/*     */ import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
/*     */ import com.viaversion.viaversion.api.protocol.packet.PacketType;
/*     */ import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
/*     */ import com.viaversion.viaversion.api.protocol.packet.ServerboundPacketType;
/*     */ import com.viaversion.viaversion.api.protocol.packet.State;
/*     */ import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
/*     */ import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
/*     */ import com.viaversion.viaversion.api.rewriter.RewriterBase;
/*     */ import com.viaversion.viaversion.api.type.Type;
/*     */ import com.viaversion.viaversion.libs.gson.JsonElement;
/*     */ import com.viaversion.viaversion.protocols.protocol1_12_1to1_12.ClientboundPackets1_12_1;
/*     */ import com.viaversion.viaversion.protocols.protocol1_12_1to1_12.ServerboundPackets1_12_1;
/*     */ import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.ClientboundPackets1_13;
/*     */ import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.ServerboundPackets1_13;
/*     */ import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.packets.InventoryPackets;
/*     */ import com.viaversion.viaversion.rewriter.CommandRewriter;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.ThreadLocalRandom;
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
/*     */ 
/*     */ public class PlayerPacket1_13
/*     */   extends RewriterBase<Protocol1_12_2To1_13>
/*     */ {
/*  50 */   private final CommandRewriter<ClientboundPackets1_13> commandRewriter = new CommandRewriter(this.protocol);
/*     */   
/*     */   public PlayerPacket1_13(Protocol1_12_2To1_13 protocol) {
/*  53 */     super((Protocol)protocol);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void registerPackets() {
/*  59 */     ((Protocol1_12_2To1_13)this.protocol).registerClientbound(State.LOGIN, 4, -1, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/*  62 */             handler(packetWrapper -> {
/*     */                   packetWrapper.cancel();
/*     */                   packetWrapper.create(2, new PacketHandler()
/*     */                       {
/*     */                         public void handle(PacketWrapper newWrapper) throws Exception {
/*  67 */                           newWrapper.write((Type)Type.VAR_INT, packetWrapper.read((Type)Type.VAR_INT));
/*  68 */                           newWrapper.write((Type)Type.BOOLEAN, Boolean.valueOf(false));
/*     */                         }
/*     */                       }).sendToServer(Protocol1_12_2To1_13.class);
/*     */                 });
/*     */           }
/*     */         });
/*     */     
/*  75 */     ((Protocol1_12_2To1_13)this.protocol).registerClientbound((ClientboundPacketType)ClientboundPackets1_13.PLUGIN_MESSAGE, wrapper -> {
/*     */           String channel = (String)wrapper.read(Type.STRING);
/*     */           
/*     */           if (channel.equals("minecraft:trader_list")) {
/*     */             wrapper.write(Type.STRING, "MC|TrList");
/*     */             
/*     */             wrapper.passthrough((Type)Type.INT);
/*     */             
/*     */             int size = ((Short)wrapper.passthrough((Type)Type.UNSIGNED_BYTE)).shortValue();
/*     */             
/*     */             for (int i = 0; i < size; i++) {
/*     */               Item input = (Item)wrapper.read(Type.FLAT_ITEM);
/*     */               
/*     */               wrapper.write(Type.ITEM, ((Protocol1_12_2To1_13)this.protocol).getItemRewriter().handleItemToClient(input));
/*     */               
/*     */               Item output = (Item)wrapper.read(Type.FLAT_ITEM);
/*     */               
/*     */               wrapper.write(Type.ITEM, ((Protocol1_12_2To1_13)this.protocol).getItemRewriter().handleItemToClient(output));
/*     */               
/*     */               boolean secondItem = ((Boolean)wrapper.passthrough((Type)Type.BOOLEAN)).booleanValue();
/*     */               if (secondItem) {
/*     */                 Item second = (Item)wrapper.read(Type.FLAT_ITEM);
/*     */                 wrapper.write(Type.ITEM, ((Protocol1_12_2To1_13)this.protocol).getItemRewriter().handleItemToClient(second));
/*     */               } 
/*     */               wrapper.passthrough((Type)Type.BOOLEAN);
/*     */               wrapper.passthrough((Type)Type.INT);
/*     */               wrapper.passthrough((Type)Type.INT);
/*     */             } 
/*     */           } else {
/*     */             String oldChannel = InventoryPackets.getOldPluginChannelId(channel);
/*     */             if (oldChannel == null) {
/*     */               if (!Via.getConfig().isSuppressConversionWarnings() || Via.getManager().isDebug()) {
/*     */                 ViaBackwards.getPlatform().getLogger().warning("Ignoring outgoing plugin message with channel: " + channel);
/*     */               }
/*     */               wrapper.cancel();
/*     */               return;
/*     */             } 
/*     */             wrapper.write(Type.STRING, oldChannel);
/*     */             if (oldChannel.equals("REGISTER") || oldChannel.equals("UNREGISTER")) {
/*     */               String[] channels = (new String((byte[])wrapper.read(Type.REMAINING_BYTES), StandardCharsets.UTF_8)).split("\000");
/*     */               List<String> rewrittenChannels = new ArrayList<>();
/*     */               for (String s : channels) {
/*     */                 String rewritten = InventoryPackets.getOldPluginChannelId(s);
/*     */                 if (rewritten != null) {
/*     */                   rewrittenChannels.add(rewritten);
/*     */                 } else if (!Via.getConfig().isSuppressConversionWarnings() || Via.getManager().isDebug()) {
/*     */                   ViaBackwards.getPlatform().getLogger().warning("Ignoring plugin channel in outgoing REGISTER: " + s);
/*     */                 } 
/*     */               } 
/*     */               wrapper.write(Type.REMAINING_BYTES, Joiner.on(false).join(rewrittenChannels).getBytes(StandardCharsets.UTF_8));
/*     */             } 
/*     */           } 
/*     */         });
/* 128 */     ((Protocol1_12_2To1_13)this.protocol).registerClientbound((ClientboundPacketType)ClientboundPackets1_13.SPAWN_PARTICLE, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/* 131 */             map((Type)Type.INT);
/* 132 */             map((Type)Type.BOOLEAN);
/* 133 */             map((Type)Type.FLOAT);
/* 134 */             map((Type)Type.FLOAT);
/* 135 */             map((Type)Type.FLOAT);
/* 136 */             map((Type)Type.FLOAT);
/* 137 */             map((Type)Type.FLOAT);
/* 138 */             map((Type)Type.FLOAT);
/* 139 */             map((Type)Type.FLOAT);
/* 140 */             map((Type)Type.INT);
/* 141 */             handler(wrapper -> {
/*     */                   ParticleMapping.ParticleData old = ParticleMapping.getMapping(((Integer)wrapper.get((Type)Type.INT, 0)).intValue());
/*     */                   
/*     */                   wrapper.set((Type)Type.INT, 0, Integer.valueOf(old.getHistoryId()));
/*     */                   
/*     */                   int[] data = old.rewriteData((Protocol1_12_2To1_13)PlayerPacket1_13.this.protocol, wrapper);
/*     */                   
/*     */                   if (data != null) {
/*     */                     if (old.getHandler().isBlockHandler() && data[0] == 0) {
/*     */                       wrapper.cancel();
/*     */                       
/*     */                       return;
/*     */                     } 
/*     */                     for (int i : data) {
/*     */                       wrapper.write((Type)Type.VAR_INT, Integer.valueOf(i));
/*     */                     }
/*     */                   } 
/*     */                 });
/*     */           }
/*     */         });
/* 161 */     ((Protocol1_12_2To1_13)this.protocol).registerClientbound((ClientboundPacketType)ClientboundPackets1_13.PLAYER_INFO, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/* 164 */             handler(packetWrapper -> {
/*     */                   TabCompleteStorage storage = (TabCompleteStorage)packetWrapper.user().get(TabCompleteStorage.class);
/*     */                   
/*     */                   int action = ((Integer)packetWrapper.passthrough((Type)Type.VAR_INT)).intValue();
/*     */                   int nPlayers = ((Integer)packetWrapper.passthrough((Type)Type.VAR_INT)).intValue();
/*     */                   for (int i = 0; i < nPlayers; i++) {
/*     */                     UUID uuid = (UUID)packetWrapper.passthrough(Type.UUID);
/*     */                     if (action == 0) {
/*     */                       String name = (String)packetWrapper.passthrough(Type.STRING);
/*     */                       storage.usernames().put(uuid, name);
/*     */                       int nProperties = ((Integer)packetWrapper.passthrough((Type)Type.VAR_INT)).intValue();
/*     */                       for (int j = 0; j < nProperties; j++) {
/*     */                         packetWrapper.passthrough(Type.STRING);
/*     */                         packetWrapper.passthrough(Type.STRING);
/*     */                         packetWrapper.passthrough(Type.OPTIONAL_STRING);
/*     */                       } 
/*     */                       packetWrapper.passthrough((Type)Type.VAR_INT);
/*     */                       packetWrapper.passthrough((Type)Type.VAR_INT);
/*     */                       packetWrapper.passthrough(Type.OPTIONAL_COMPONENT);
/*     */                     } else if (action == 1) {
/*     */                       packetWrapper.passthrough((Type)Type.VAR_INT);
/*     */                     } else if (action == 2) {
/*     */                       packetWrapper.passthrough((Type)Type.VAR_INT);
/*     */                     } else if (action == 3) {
/*     */                       packetWrapper.passthrough(Type.OPTIONAL_COMPONENT);
/*     */                     } else if (action == 4) {
/*     */                       storage.usernames().remove(uuid);
/*     */                     } 
/*     */                   } 
/*     */                 });
/*     */           }
/*     */         });
/* 196 */     ((Protocol1_12_2To1_13)this.protocol).registerClientbound((ClientboundPacketType)ClientboundPackets1_13.SCOREBOARD_OBJECTIVE, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/* 199 */             map(Type.STRING);
/* 200 */             map((Type)Type.BYTE);
/* 201 */             handler(wrapper -> {
/*     */                   byte mode = ((Byte)wrapper.get((Type)Type.BYTE, 0)).byteValue();
/*     */                   
/*     */                   if (mode == 0 || mode == 2) {
/*     */                     JsonElement value = (JsonElement)wrapper.read(Type.COMPONENT);
/*     */                     String legacyValue = ((Protocol1_12_2To1_13)PlayerPacket1_13.this.protocol).jsonToLegacy(value);
/*     */                     wrapper.write(Type.STRING, ChatUtil.fromLegacy(legacyValue, 'f', 32));
/*     */                     int type = ((Integer)wrapper.read((Type)Type.VAR_INT)).intValue();
/*     */                     wrapper.write(Type.STRING, (type == 1) ? "hearts" : "integer");
/*     */                   } 
/*     */                 });
/*     */           }
/*     */         });
/* 214 */     ((Protocol1_12_2To1_13)this.protocol).registerClientbound((ClientboundPacketType)ClientboundPackets1_13.TEAMS, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/* 217 */             map(Type.STRING);
/* 218 */             map((Type)Type.BYTE);
/* 219 */             handler(wrapper -> {
/*     */                   byte action = ((Byte)wrapper.get((Type)Type.BYTE, 0)).byteValue();
/*     */                   
/*     */                   if (action == 0 || action == 2) {
/*     */                     JsonElement displayName = (JsonElement)wrapper.read(Type.COMPONENT);
/*     */                     
/*     */                     String legacyTextDisplayName = ((Protocol1_12_2To1_13)PlayerPacket1_13.this.protocol).jsonToLegacy(displayName);
/*     */                     
/*     */                     wrapper.write(Type.STRING, ChatUtil.fromLegacy(legacyTextDisplayName, 'f', 32));
/*     */                     
/*     */                     byte flags = ((Byte)wrapper.read((Type)Type.BYTE)).byteValue();
/*     */                     String nameTagVisibility = (String)wrapper.read(Type.STRING);
/*     */                     String collisionRule = (String)wrapper.read(Type.STRING);
/*     */                     int colour = ((Integer)wrapper.read((Type)Type.VAR_INT)).intValue();
/*     */                     if (colour == 21) {
/*     */                       colour = -1;
/*     */                     }
/*     */                     JsonElement prefixComponent = (JsonElement)wrapper.read(Type.COMPONENT);
/*     */                     JsonElement suffixComponent = (JsonElement)wrapper.read(Type.COMPONENT);
/*     */                     String prefix = ((Protocol1_12_2To1_13)PlayerPacket1_13.this.protocol).jsonToLegacy(prefixComponent);
/*     */                     if (ViaBackwards.getConfig().addTeamColorTo1_13Prefix()) {
/* 240 */                       prefix = prefix + "§" + ((colour > -1 && colour <= 15) ? Integer.toHexString(colour) : "r");
/*     */                     }
/*     */                     
/*     */                     String suffix = ((Protocol1_12_2To1_13)PlayerPacket1_13.this.protocol).jsonToLegacy(suffixComponent);
/*     */                     
/*     */                     wrapper.write(Type.STRING, ChatUtil.fromLegacyPrefix(prefix, 'f', 16));
/*     */                     
/*     */                     wrapper.write(Type.STRING, ChatUtil.fromLegacy(suffix, false, 16));
/*     */                     
/*     */                     wrapper.write((Type)Type.BYTE, Byte.valueOf(flags));
/*     */                     
/*     */                     wrapper.write(Type.STRING, nameTagVisibility);
/*     */                     wrapper.write(Type.STRING, collisionRule);
/*     */                     wrapper.write((Type)Type.BYTE, Byte.valueOf((byte)colour));
/*     */                   } 
/*     */                   if (action == 0 || action == 3 || action == 4) {
/*     */                     wrapper.passthrough(Type.STRING_ARRAY);
/*     */                   }
/*     */                 });
/*     */           }
/*     */         });
/* 261 */     ((Protocol1_12_2To1_13)this.protocol).registerClientbound((ClientboundPacketType)ClientboundPackets1_13.DECLARE_COMMANDS, null, wrapper -> {
/*     */           wrapper.cancel();
/*     */           
/*     */           TabCompleteStorage storage = (TabCompleteStorage)wrapper.user().get(TabCompleteStorage.class);
/*     */           
/*     */           if (!storage.commands().isEmpty()) {
/*     */             storage.commands().clear();
/*     */           }
/*     */           
/*     */           int size = ((Integer)wrapper.read((Type)Type.VAR_INT)).intValue();
/*     */           
/*     */           boolean initialNodes = true;
/*     */           
/*     */           for (int i = 0; i < size; i++) {
/*     */             byte flags = ((Byte)wrapper.read((Type)Type.BYTE)).byteValue();
/*     */             
/*     */             wrapper.read(Type.VAR_INT_ARRAY_PRIMITIVE);
/*     */             
/*     */             if ((flags & 0x8) != 0) {
/*     */               wrapper.read((Type)Type.VAR_INT);
/*     */             }
/*     */             
/*     */             byte nodeType = (byte)(flags & 0x3);
/*     */             if (initialNodes && nodeType == 2) {
/*     */               initialNodes = false;
/*     */             }
/*     */             if (nodeType == 1 || nodeType == 2) {
/*     */               String name = (String)wrapper.read(Type.STRING);
/*     */               if (nodeType == 1 && initialNodes) {
/*     */                 storage.commands().add('/' + name);
/*     */               }
/*     */             } 
/*     */             if (nodeType == 2) {
/*     */               this.commandRewriter.handleArgument(wrapper, (String)wrapper.read(Type.STRING));
/*     */             }
/*     */             if ((flags & 0x10) != 0) {
/*     */               wrapper.read(Type.STRING);
/*     */             }
/*     */           } 
/*     */         });
/* 301 */     ((Protocol1_12_2To1_13)this.protocol).registerClientbound((ClientboundPacketType)ClientboundPackets1_13.TAB_COMPLETE, wrapper -> {
/*     */           TabCompleteStorage storage = (TabCompleteStorage)wrapper.user().get(TabCompleteStorage.class);
/*     */           if (storage.lastRequest() == null) {
/*     */             wrapper.cancel();
/*     */             return;
/*     */           } 
/*     */           if (storage.lastId() != ((Integer)wrapper.read((Type)Type.VAR_INT)).intValue()) {
/*     */             wrapper.cancel();
/*     */           }
/*     */           int start = ((Integer)wrapper.read((Type)Type.VAR_INT)).intValue();
/*     */           int length = ((Integer)wrapper.read((Type)Type.VAR_INT)).intValue();
/*     */           int lastRequestPartIndex = storage.lastRequest().lastIndexOf(' ') + 1;
/*     */           if (lastRequestPartIndex != start)
/*     */             wrapper.cancel(); 
/*     */           if (length != storage.lastRequest().length() - lastRequestPartIndex)
/*     */             wrapper.cancel(); 
/*     */           int count = ((Integer)wrapper.passthrough((Type)Type.VAR_INT)).intValue();
/*     */           for (int i = 0; i < count; i++) {
/*     */             String match = (String)wrapper.read(Type.STRING);
/* 320 */             wrapper.write(Type.STRING, ((start == 0 && !storage.isLastAssumeCommand()) ? "/" : "") + match);
/*     */ 
/*     */             
/*     */             if (((Boolean)wrapper.read((Type)Type.BOOLEAN)).booleanValue()) {
/*     */               wrapper.read(Type.STRING);
/*     */             }
/*     */           } 
/*     */         });
/*     */     
/* 329 */     ((Protocol1_12_2To1_13)this.protocol).registerServerbound((ServerboundPacketType)ServerboundPackets1_12_1.TAB_COMPLETE, wrapper -> {
/*     */           TabCompleteStorage storage = (TabCompleteStorage)wrapper.user().get(TabCompleteStorage.class);
/*     */           
/*     */           List<String> suggestions = new ArrayList<>();
/*     */           
/*     */           String command = (String)wrapper.read(Type.STRING);
/*     */           
/*     */           boolean assumeCommand = ((Boolean)wrapper.read((Type)Type.BOOLEAN)).booleanValue();
/*     */           
/*     */           wrapper.read(Type.OPTIONAL_POSITION);
/*     */           
/*     */           if (!assumeCommand && !command.startsWith("/")) {
/*     */             String buffer = command.substring(command.lastIndexOf(' ') + 1);
/*     */             
/*     */             for (String value : storage.usernames().values()) {
/*     */               if (startsWithIgnoreCase(value, buffer)) {
/*     */                 suggestions.add(value);
/*     */               }
/*     */             } 
/*     */           } else if (!storage.commands().isEmpty() && !command.contains(" ")) {
/*     */             for (String value : storage.commands()) {
/*     */               if (startsWithIgnoreCase(value, command)) {
/*     */                 suggestions.add(value);
/*     */               }
/*     */             } 
/*     */           } 
/*     */           
/*     */           if (!suggestions.isEmpty()) {
/*     */             wrapper.cancel();
/*     */             
/*     */             PacketWrapper response = wrapper.create((PacketType)ClientboundPackets1_12_1.TAB_COMPLETE);
/*     */             
/*     */             response.write((Type)Type.VAR_INT, Integer.valueOf(suggestions.size()));
/*     */             for (String value : suggestions) {
/*     */               response.write(Type.STRING, value);
/*     */             }
/*     */             response.scheduleSend(Protocol1_12_2To1_13.class);
/*     */             storage.setLastRequest(null);
/*     */             return;
/*     */           } 
/*     */           if (!assumeCommand && command.startsWith("/")) {
/*     */             command = command.substring(1);
/*     */           }
/*     */           int id = ThreadLocalRandom.current().nextInt();
/*     */           wrapper.write((Type)Type.VAR_INT, Integer.valueOf(id));
/*     */           wrapper.write(Type.STRING, command);
/*     */           storage.setLastId(id);
/*     */           storage.setLastAssumeCommand(assumeCommand);
/*     */           storage.setLastRequest(command);
/*     */         });
/* 379 */     ((Protocol1_12_2To1_13)this.protocol).registerServerbound((ServerboundPacketType)ServerboundPackets1_12_1.PLUGIN_MESSAGE, wrapper -> {
/*     */           Item book;
/*     */           boolean signing;
/*     */           byte type;
/*     */           int x;
/*     */           int y;
/*     */           int z;
/*     */           byte flags;
/*     */           String mode;
/*     */           String str1;
/*     */           int modeId;
/*     */           int i;
/*     */           String mirror;
/*     */           int mirrorId;
/*     */           String rotation;
/*     */           int rotationId;
/*     */           byte b1;
/*     */           String channel = (String)wrapper.read(Type.STRING);
/*     */           switch (channel) {
/*     */             case "MC|BSign":
/*     */             case "MC|BEdit":
/*     */               wrapper.setPacketType((PacketType)ServerboundPackets1_13.EDIT_BOOK);
/*     */               book = (Item)wrapper.read(Type.ITEM);
/*     */               wrapper.write(Type.FLAT_ITEM, ((Protocol1_12_2To1_13)this.protocol).getItemRewriter().handleItemToServer(book));
/*     */               signing = channel.equals("MC|BSign");
/*     */               wrapper.write((Type)Type.BOOLEAN, Boolean.valueOf(signing));
/*     */               return;
/*     */             
/*     */             case "MC|ItemName":
/*     */               wrapper.setPacketType((PacketType)ServerboundPackets1_13.RENAME_ITEM);
/*     */               return;
/*     */             
/*     */             case "MC|AdvCmd":
/*     */               type = ((Byte)wrapper.read((Type)Type.BYTE)).byteValue();
/*     */               if (type == 0) {
/*     */                 wrapper.setPacketType((PacketType)ServerboundPackets1_13.UPDATE_COMMAND_BLOCK);
/*     */                 wrapper.cancel();
/*     */                 ViaBackwards.getPlatform().getLogger().warning("Client send MC|AdvCmd custom payload to update command block, weird!");
/*     */               } else if (type == 1) {
/*     */                 wrapper.setPacketType((PacketType)ServerboundPackets1_13.UPDATE_COMMAND_BLOCK_MINECART);
/*     */                 wrapper.write((Type)Type.VAR_INT, wrapper.read((Type)Type.INT));
/*     */                 wrapper.passthrough(Type.STRING);
/*     */                 wrapper.passthrough((Type)Type.BOOLEAN);
/*     */               } else {
/*     */                 wrapper.cancel();
/*     */               } 
/*     */               return;
/*     */             
/*     */             case "MC|AutoCmd":
/*     */               wrapper.setPacketType((PacketType)ServerboundPackets1_13.UPDATE_COMMAND_BLOCK);
/*     */               x = ((Integer)wrapper.read((Type)Type.INT)).intValue();
/*     */               y = ((Integer)wrapper.read((Type)Type.INT)).intValue();
/*     */               z = ((Integer)wrapper.read((Type)Type.INT)).intValue();
/*     */               wrapper.write(Type.POSITION, new Position(x, (short)y, z));
/*     */               wrapper.passthrough(Type.STRING);
/*     */               flags = 0;
/*     */               if (((Boolean)wrapper.read((Type)Type.BOOLEAN)).booleanValue()) {
/*     */                 flags = (byte)(flags | 0x1);
/*     */               }
/*     */               str1 = (String)wrapper.read(Type.STRING);
/*     */               i = str1.equals("SEQUENCE") ? 0 : (str1.equals("AUTO") ? 1 : 2);
/*     */               wrapper.write((Type)Type.VAR_INT, Integer.valueOf(i));
/*     */               if (((Boolean)wrapper.read((Type)Type.BOOLEAN)).booleanValue()) {
/*     */                 flags = (byte)(flags | 0x2);
/*     */               }
/*     */               if (((Boolean)wrapper.read((Type)Type.BOOLEAN)).booleanValue()) {
/*     */                 flags = (byte)(flags | 0x4);
/*     */               }
/*     */               wrapper.write((Type)Type.BYTE, Byte.valueOf(flags));
/*     */               return;
/*     */             case "MC|Struct":
/*     */               wrapper.setPacketType((PacketType)ServerboundPackets1_13.UPDATE_STRUCTURE_BLOCK);
/*     */               x = ((Integer)wrapper.read((Type)Type.INT)).intValue();
/*     */               y = ((Integer)wrapper.read((Type)Type.INT)).intValue();
/*     */               z = ((Integer)wrapper.read((Type)Type.INT)).intValue();
/*     */               wrapper.write(Type.POSITION, new Position(x, (short)y, z));
/*     */               wrapper.write((Type)Type.VAR_INT, Integer.valueOf(((Byte)wrapper.read((Type)Type.BYTE)).byteValue() - 1));
/*     */               mode = (String)wrapper.read(Type.STRING);
/*     */               modeId = mode.equals("SAVE") ? 0 : (mode.equals("LOAD") ? 1 : (mode.equals("CORNER") ? 2 : 3));
/*     */               wrapper.write((Type)Type.VAR_INT, Integer.valueOf(modeId));
/*     */               wrapper.passthrough(Type.STRING);
/*     */               wrapper.write((Type)Type.BYTE, Byte.valueOf(((Integer)wrapper.read((Type)Type.INT)).byteValue()));
/*     */               wrapper.write((Type)Type.BYTE, Byte.valueOf(((Integer)wrapper.read((Type)Type.INT)).byteValue()));
/*     */               wrapper.write((Type)Type.BYTE, Byte.valueOf(((Integer)wrapper.read((Type)Type.INT)).byteValue()));
/*     */               wrapper.write((Type)Type.BYTE, Byte.valueOf(((Integer)wrapper.read((Type)Type.INT)).byteValue()));
/*     */               wrapper.write((Type)Type.BYTE, Byte.valueOf(((Integer)wrapper.read((Type)Type.INT)).byteValue()));
/*     */               wrapper.write((Type)Type.BYTE, Byte.valueOf(((Integer)wrapper.read((Type)Type.INT)).byteValue()));
/*     */               mirror = (String)wrapper.read(Type.STRING);
/*     */               mirrorId = mode.equals("NONE") ? 0 : (mode.equals("LEFT_RIGHT") ? 1 : 2);
/*     */               rotation = (String)wrapper.read(Type.STRING);
/*     */               rotationId = mode.equals("NONE") ? 0 : (mode.equals("CLOCKWISE_90") ? 1 : (mode.equals("CLOCKWISE_180") ? 2 : 3));
/*     */               wrapper.passthrough(Type.STRING);
/*     */               b1 = 0;
/*     */               if (((Boolean)wrapper.read((Type)Type.BOOLEAN)).booleanValue()) {
/*     */                 b1 = (byte)(b1 | 0x1);
/*     */               }
/*     */               if (((Boolean)wrapper.read((Type)Type.BOOLEAN)).booleanValue()) {
/*     */                 b1 = (byte)(b1 | 0x2);
/*     */               }
/*     */               if (((Boolean)wrapper.read((Type)Type.BOOLEAN)).booleanValue()) {
/*     */                 b1 = (byte)(b1 | 0x4);
/*     */               }
/*     */               wrapper.passthrough((Type)Type.FLOAT);
/*     */               wrapper.passthrough((Type)Type.VAR_LONG);
/*     */               wrapper.write((Type)Type.BYTE, Byte.valueOf(b1));
/*     */               return;
/*     */             case "MC|Beacon":
/*     */               wrapper.setPacketType((PacketType)ServerboundPackets1_13.SET_BEACON_EFFECT);
/*     */               wrapper.write((Type)Type.VAR_INT, wrapper.read((Type)Type.INT));
/*     */               wrapper.write((Type)Type.VAR_INT, wrapper.read((Type)Type.INT));
/*     */               return;
/*     */             case "MC|TrSel":
/*     */               wrapper.setPacketType((PacketType)ServerboundPackets1_13.SELECT_TRADE);
/*     */               wrapper.write((Type)Type.VAR_INT, wrapper.read((Type)Type.INT));
/*     */               return;
/*     */             case "MC|PickItem":
/*     */               wrapper.setPacketType((PacketType)ServerboundPackets1_13.PICK_ITEM);
/*     */               return;
/*     */           } 
/*     */           String newChannel = InventoryPackets.getNewPluginChannelId(channel);
/*     */           if (newChannel == null) {
/*     */             if (!Via.getConfig().isSuppressConversionWarnings() || Via.getManager().isDebug()) {
/*     */               ViaBackwards.getPlatform().getLogger().warning("Ignoring incoming plugin message with channel: " + channel);
/*     */             }
/*     */             wrapper.cancel();
/*     */             return;
/*     */           } 
/*     */           wrapper.write(Type.STRING, newChannel);
/*     */           if (newChannel.equals("minecraft:register") || newChannel.equals("minecraft:unregister")) {
/*     */             String[] channels = (new String((byte[])wrapper.read(Type.REMAINING_BYTES), StandardCharsets.UTF_8)).split("\000");
/*     */             List<String> rewrittenChannels = new ArrayList<>();
/*     */             for (String s : channels) {
/*     */               String rewritten = InventoryPackets.getNewPluginChannelId(s);
/*     */               if (rewritten != null) {
/*     */                 rewrittenChannels.add(rewritten);
/*     */               } else if (!Via.getConfig().isSuppressConversionWarnings() || Via.getManager().isDebug()) {
/*     */                 ViaBackwards.getPlatform().getLogger().warning("Ignoring plugin channel in incoming REGISTER: " + s);
/*     */               } 
/*     */             } 
/*     */             if (!rewrittenChannels.isEmpty()) {
/*     */               wrapper.write(Type.REMAINING_BYTES, Joiner.on(false).join(rewrittenChannels).getBytes(StandardCharsets.UTF_8));
/*     */             } else {
/*     */               wrapper.cancel();
/*     */               return;
/*     */             } 
/*     */           } 
/*     */         });
/* 526 */     ((Protocol1_12_2To1_13)this.protocol).registerClientbound((ClientboundPacketType)ClientboundPackets1_13.STATISTICS, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/* 529 */             map((Type)Type.VAR_INT);
/* 530 */             handler(wrapper -> {
/*     */                   int size = ((Integer)wrapper.get((Type)Type.VAR_INT, 0)).intValue();
/*     */                   int newSize = size;
/*     */                   for (int i = 0; i < size; i++) {
/*     */                     int categoryId = ((Integer)wrapper.read((Type)Type.VAR_INT)).intValue();
/*     */                     int statisticId = ((Integer)wrapper.read((Type)Type.VAR_INT)).intValue();
/*     */                     String name = "";
/*     */                     switch (categoryId) {
/*     */                       case 0:
/*     */                       case 1:
/*     */                       case 2:
/*     */                       case 3:
/*     */                       case 4:
/*     */                       case 5:
/*     */                       case 6:
/*     */                       case 7:
/*     */                         wrapper.read((Type)Type.VAR_INT);
/*     */                         newSize--;
/*     */                         break;
/*     */                       case 8:
/*     */                         name = (String)((Protocol1_12_2To1_13)PlayerPacket1_13.this.protocol).getMappingData().getStatisticMappings().get(statisticId);
/*     */                         if (name == null) {
/*     */                           wrapper.read((Type)Type.VAR_INT);
/*     */                           newSize--;
/*     */                           break;
/*     */                         } 
/*     */                       default:
/*     */                         wrapper.write(Type.STRING, name);
/*     */                         wrapper.passthrough((Type)Type.VAR_INT);
/*     */                         break;
/*     */                     } 
/*     */                   } 
/*     */                   if (newSize != size) {
/*     */                     wrapper.set((Type)Type.VAR_INT, 0, Integer.valueOf(newSize));
/*     */                   }
/*     */                 });
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean startsWithIgnoreCase(String string, String prefix) {
/* 574 */     if (string.length() < prefix.length()) {
/* 575 */       return false;
/*     */     }
/* 577 */     return string.regionMatches(true, 0, prefix, 0, prefix.length());
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viabackwards\protocol\protocol1_12_2to1_13\packets\PlayerPacket1_13.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */