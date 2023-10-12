/*    */ package com.viaversion.viaversion.protocols.protocol1_19to1_18_2.packets;
/*    */ 
/*    */ import com.google.common.base.Preconditions;
/*    */ import com.viaversion.viaversion.api.data.entity.EntityTracker;
/*    */ import com.viaversion.viaversion.api.minecraft.chunks.Chunk;
/*    */ import com.viaversion.viaversion.api.minecraft.chunks.ChunkSection;
/*    */ import com.viaversion.viaversion.api.minecraft.chunks.DataPalette;
/*    */ import com.viaversion.viaversion.api.minecraft.chunks.PaletteType;
/*    */ import com.viaversion.viaversion.api.protocol.Protocol;
/*    */ import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
/*    */ import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
/*    */ import com.viaversion.viaversion.api.protocol.packet.ServerboundPacketType;
/*    */ import com.viaversion.viaversion.api.type.Type;
/*    */ import com.viaversion.viaversion.protocols.protocol1_18to1_17_1.ClientboundPackets1_18;
/*    */ import com.viaversion.viaversion.protocols.protocol1_18to1_17_1.types.Chunk1_18Type;
/*    */ import com.viaversion.viaversion.protocols.protocol1_19to1_18_2.Protocol1_19To1_18_2;
/*    */ import com.viaversion.viaversion.protocols.protocol1_19to1_18_2.ServerboundPackets1_19;
/*    */ import com.viaversion.viaversion.rewriter.BlockRewriter;
/*    */ import com.viaversion.viaversion.util.MathUtil;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class WorldPackets
/*    */ {
/*    */   public static void register(Protocol1_19To1_18_2 protocol) {
/* 37 */     BlockRewriter<ClientboundPackets1_18> blockRewriter = new BlockRewriter((Protocol)protocol, Type.POSITION1_14);
/* 38 */     blockRewriter.registerBlockAction((ClientboundPacketType)ClientboundPackets1_18.BLOCK_ACTION);
/* 39 */     blockRewriter.registerBlockChange((ClientboundPacketType)ClientboundPackets1_18.BLOCK_CHANGE);
/* 40 */     blockRewriter.registerVarLongMultiBlockChange((ClientboundPacketType)ClientboundPackets1_18.MULTI_BLOCK_CHANGE);
/* 41 */     blockRewriter.registerEffect((ClientboundPacketType)ClientboundPackets1_18.EFFECT, 1010, 2001);
/*    */     
/* 43 */     protocol.cancelClientbound((ClientboundPacketType)ClientboundPackets1_18.ACKNOWLEDGE_PLAYER_DIGGING);
/*    */     
/* 45 */     protocol.registerClientbound((ClientboundPacketType)ClientboundPackets1_18.CHUNK_DATA, wrapper -> {
/*    */           EntityTracker tracker = protocol.getEntityRewriter().tracker(wrapper.user());
/*    */           
/*    */           Preconditions.checkArgument((tracker.biomesSent() != -1), "Biome count not set");
/*    */           
/*    */           Preconditions.checkArgument((tracker.currentWorldSectionHeight() != -1), "Section height not set");
/*    */           
/*    */           Chunk1_18Type chunkType = new Chunk1_18Type(tracker.currentWorldSectionHeight(), MathUtil.ceilLog2(protocol.getMappingData().getBlockStateMappings().mappedSize()), MathUtil.ceilLog2(tracker.biomesSent()));
/*    */           Chunk chunk = (Chunk)wrapper.passthrough((Type)chunkType);
/*    */           for (ChunkSection section : chunk.getSections()) {
/*    */             DataPalette blockPalette = section.palette(PaletteType.BLOCKS);
/*    */             for (int i = 0; i < blockPalette.size(); i++) {
/*    */               int id = blockPalette.idByIndex(i);
/*    */               blockPalette.setIdByIndex(i, protocol.getMappingData().getNewBlockStateId(id));
/*    */             } 
/*    */           } 
/*    */         });
/* 62 */     protocol.registerServerbound((ServerboundPacketType)ServerboundPackets1_19.SET_BEACON_EFFECT, wrapper -> {
/*    */           if (((Boolean)wrapper.read((Type)Type.BOOLEAN)).booleanValue()) {
/*    */             wrapper.passthrough((Type)Type.VAR_INT);
/*    */           } else {
/*    */             wrapper.write((Type)Type.VAR_INT, Integer.valueOf(-1));
/*    */           } 
/*    */           if (((Boolean)wrapper.read((Type)Type.BOOLEAN)).booleanValue()) {
/*    */             wrapper.passthrough((Type)Type.VAR_INT);
/*    */           } else {
/*    */             wrapper.write((Type)Type.VAR_INT, Integer.valueOf(-1));
/*    */           } 
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\protocols\protocol1_19to1_18_2\packets\WorldPackets.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */