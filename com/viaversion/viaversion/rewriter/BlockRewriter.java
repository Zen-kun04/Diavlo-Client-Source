/*     */ package com.viaversion.viaversion.rewriter;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.viaversion.viaversion.api.data.Mappings;
/*     */ import com.viaversion.viaversion.api.data.entity.EntityTracker;
/*     */ import com.viaversion.viaversion.api.minecraft.BlockChangeRecord;
/*     */ import com.viaversion.viaversion.api.minecraft.Position;
/*     */ import com.viaversion.viaversion.api.minecraft.blockentity.BlockEntity;
/*     */ import com.viaversion.viaversion.api.minecraft.blockentity.BlockEntityImpl;
/*     */ import com.viaversion.viaversion.api.minecraft.chunks.Chunk;
/*     */ import com.viaversion.viaversion.api.minecraft.chunks.ChunkSection;
/*     */ import com.viaversion.viaversion.api.minecraft.chunks.DataPalette;
/*     */ import com.viaversion.viaversion.api.minecraft.chunks.PaletteType;
/*     */ import com.viaversion.viaversion.api.protocol.Protocol;
/*     */ import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
/*     */ import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
/*     */ import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
/*     */ import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
/*     */ import com.viaversion.viaversion.api.type.Type;
/*     */ import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
/*     */ import com.viaversion.viaversion.util.MathUtil;
/*     */ import java.util.List;
/*     */ import java.util.function.Consumer;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BlockRewriter<C extends ClientboundPacketType>
/*     */ {
/*     */   private final Protocol<C, ?, ?, ?> protocol;
/*     */   private final Type<Position> positionType;
/*     */   private final Type<CompoundTag> nbtType;
/*     */   
/*     */   public BlockRewriter(Protocol<C, ?, ?, ?> protocol, Type<Position> positionType) {
/*  48 */     this(protocol, positionType, Type.NBT);
/*     */   }
/*     */   
/*     */   public BlockRewriter(Protocol<C, ?, ?, ?> protocol, Type<Position> positionType, Type<CompoundTag> nbtType) {
/*  52 */     this.protocol = protocol;
/*  53 */     this.positionType = positionType;
/*  54 */     this.nbtType = nbtType;
/*     */   }
/*     */   
/*     */   public void registerBlockAction(C packetType) {
/*  58 */     this.protocol.registerClientbound((ClientboundPacketType)packetType, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/*  61 */             map(BlockRewriter.this.positionType);
/*  62 */             map((Type)Type.UNSIGNED_BYTE);
/*  63 */             map((Type)Type.UNSIGNED_BYTE);
/*  64 */             map((Type)Type.VAR_INT);
/*  65 */             handler(wrapper -> {
/*     */                   if (BlockRewriter.this.protocol.getMappingData().getBlockMappings() == null) {
/*     */                     return;
/*     */                   }
/*     */                   int id = ((Integer)wrapper.get((Type)Type.VAR_INT, 0)).intValue();
/*     */                   int mappedId = BlockRewriter.this.protocol.getMappingData().getNewBlockId(id);
/*     */                   if (mappedId == -1) {
/*     */                     wrapper.cancel();
/*     */                     return;
/*     */                   } 
/*     */                   wrapper.set((Type)Type.VAR_INT, 0, Integer.valueOf(mappedId));
/*     */                 });
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void registerBlockChange(C packetType) {
/*  85 */     this.protocol.registerClientbound((ClientboundPacketType)packetType, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/*  88 */             map(BlockRewriter.this.positionType);
/*  89 */             map((Type)Type.VAR_INT);
/*  90 */             handler(wrapper -> wrapper.set((Type)Type.VAR_INT, 0, Integer.valueOf(BlockRewriter.this.protocol.getMappingData().getNewBlockStateId(((Integer)wrapper.get((Type)Type.VAR_INT, 0)).intValue()))));
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public void registerMultiBlockChange(C packetType) {
/*  96 */     this.protocol.registerClientbound((ClientboundPacketType)packetType, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/*  99 */             map((Type)Type.INT);
/* 100 */             map((Type)Type.INT);
/* 101 */             handler(wrapper -> {
/*     */                   for (BlockChangeRecord record : (BlockChangeRecord[])wrapper.passthrough(Type.BLOCK_CHANGE_RECORD_ARRAY)) {
/*     */                     record.setBlockId(BlockRewriter.this.protocol.getMappingData().getNewBlockStateId(record.getBlockId()));
/*     */                   }
/*     */                 });
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public void registerVarLongMultiBlockChange(C packetType) {
/* 111 */     this.protocol.registerClientbound((ClientboundPacketType)packetType, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/* 114 */             map((Type)Type.LONG);
/* 115 */             map((Type)Type.BOOLEAN);
/* 116 */             handler(wrapper -> {
/*     */                   for (BlockChangeRecord record : (BlockChangeRecord[])wrapper.passthrough(Type.VAR_LONG_BLOCK_CHANGE_RECORD_ARRAY)) {
/*     */                     record.setBlockId(BlockRewriter.this.protocol.getMappingData().getNewBlockStateId(record.getBlockId()));
/*     */                   }
/*     */                 });
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public void registerVarLongMultiBlockChange1_20(C packetType) {
/* 126 */     this.protocol.registerClientbound((ClientboundPacketType)packetType, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/* 129 */             map((Type)Type.LONG);
/* 130 */             handler(wrapper -> {
/*     */                   for (BlockChangeRecord record : (BlockChangeRecord[])wrapper.passthrough(Type.VAR_LONG_BLOCK_CHANGE_RECORD_ARRAY)) {
/*     */                     record.setBlockId(BlockRewriter.this.protocol.getMappingData().getNewBlockStateId(record.getBlockId()));
/*     */                   }
/*     */                 });
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public void registerAcknowledgePlayerDigging(C packetType) {
/* 141 */     registerBlockChange(packetType);
/*     */   }
/*     */   
/*     */   public void registerEffect(C packetType, final int playRecordId, final int blockBreakId) {
/* 145 */     this.protocol.registerClientbound((ClientboundPacketType)packetType, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/* 148 */             map((Type)Type.INT);
/* 149 */             map(BlockRewriter.this.positionType);
/* 150 */             map((Type)Type.INT);
/* 151 */             handler(wrapper -> {
/*     */                   int id = ((Integer)wrapper.get((Type)Type.INT, 0)).intValue();
/*     */                   int data = ((Integer)wrapper.get((Type)Type.INT, 1)).intValue();
/*     */                   if (id == playRecordId && BlockRewriter.this.protocol.getMappingData().getItemMappings() != null) {
/*     */                     wrapper.set((Type)Type.INT, 1, Integer.valueOf(BlockRewriter.this.protocol.getMappingData().getNewItemId(data)));
/*     */                   } else if (id == blockBreakId && BlockRewriter.this.protocol.getMappingData().getBlockStateMappings() != null) {
/*     */                     wrapper.set((Type)Type.INT, 1, Integer.valueOf(BlockRewriter.this.protocol.getMappingData().getNewBlockStateId(data)));
/*     */                   } 
/*     */                 });
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public void registerChunkData1_19(C packetType, ChunkTypeSupplier chunkTypeSupplier) {
/* 165 */     registerChunkData1_19(packetType, chunkTypeSupplier, null);
/*     */   }
/*     */   
/*     */   public void registerChunkData1_19(C packetType, ChunkTypeSupplier chunkTypeSupplier, Consumer<BlockEntity> blockEntityHandler) {
/* 169 */     this.protocol.registerClientbound((ClientboundPacketType)packetType, chunkDataHandler1_19(chunkTypeSupplier, blockEntityHandler));
/*     */   }
/*     */   
/*     */   public PacketHandler chunkDataHandler1_19(ChunkTypeSupplier chunkTypeSupplier, Consumer<BlockEntity> blockEntityHandler) {
/* 173 */     return wrapper -> {
/*     */         EntityTracker tracker = this.protocol.getEntityRewriter().tracker(wrapper.user());
/*     */         Preconditions.checkArgument((tracker.biomesSent() != -1), "Biome count not set");
/*     */         Preconditions.checkArgument((tracker.currentWorldSectionHeight() != -1), "Section height not set");
/*     */         Type<Chunk> chunkType = chunkTypeSupplier.supply(tracker.currentWorldSectionHeight(), MathUtil.ceilLog2(this.protocol.getMappingData().getBlockStateMappings().mappedSize()), MathUtil.ceilLog2(tracker.biomesSent()));
/*     */         Chunk chunk = (Chunk)wrapper.passthrough(chunkType);
/*     */         for (ChunkSection section : chunk.getSections()) {
/*     */           DataPalette blockPalette = section.palette(PaletteType.BLOCKS);
/*     */           for (int i = 0; i < blockPalette.size(); i++) {
/*     */             int id = blockPalette.idByIndex(i);
/*     */             blockPalette.setIdByIndex(i, this.protocol.getMappingData().getNewBlockStateId(id));
/*     */           } 
/*     */         } 
/*     */         Mappings blockEntityMappings = this.protocol.getMappingData().getBlockEntityMappings();
/*     */         if (blockEntityMappings != null || blockEntityHandler != null) {
/*     */           List<BlockEntity> blockEntities = chunk.blockEntities();
/*     */           for (int i = 0; i < blockEntities.size(); i++) {
/*     */             BlockEntity blockEntity = blockEntities.get(i);
/*     */             if (blockEntityMappings != null) {
/*     */               blockEntities.set(i, blockEntity.withTypeId(blockEntityMappings.getNewIdOrDefault(blockEntity.typeId(), blockEntity.typeId())));
/*     */             }
/*     */             if (blockEntityHandler != null && blockEntity.tag() != null) {
/*     */               blockEntityHandler.accept(blockEntity);
/*     */             }
/*     */           } 
/*     */         } 
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void registerBlockEntityData(C packetType) {
/* 207 */     registerBlockEntityData(packetType, null);
/*     */   }
/*     */   
/*     */   public void registerBlockEntityData(C packetType, Consumer<BlockEntity> blockEntityHandler) {
/* 211 */     this.protocol.registerClientbound((ClientboundPacketType)packetType, wrapper -> {
/*     */           Position position = (Position)wrapper.passthrough(this.positionType);
/*     */           int blockEntityId = ((Integer)wrapper.read((Type)Type.VAR_INT)).intValue();
/*     */           Mappings mappings = this.protocol.getMappingData().getBlockEntityMappings();
/*     */           if (mappings != null) {
/*     */             wrapper.write((Type)Type.VAR_INT, Integer.valueOf(mappings.getNewIdOrDefault(blockEntityId, blockEntityId)));
/*     */           } else {
/*     */             wrapper.write((Type)Type.VAR_INT, Integer.valueOf(blockEntityId));
/*     */           } 
/*     */           CompoundTag tag;
/*     */           if (blockEntityHandler != null && (tag = (CompoundTag)wrapper.passthrough(this.nbtType)) != null) {
/*     */             BlockEntityImpl blockEntityImpl = new BlockEntityImpl(BlockEntity.pack(position.x(), position.z()), (short)position.y(), blockEntityId, tag);
/*     */             blockEntityHandler.accept(blockEntityImpl);
/*     */           } 
/*     */         });
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface ChunkTypeSupplier {
/*     */     Type<Chunk> supply(int param1Int1, int param1Int2, int param1Int3);
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\rewriter\BlockRewriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */