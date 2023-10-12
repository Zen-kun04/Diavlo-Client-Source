/*    */ package com.viaversion.viaversion.protocols.protocol1_20_2to1_20.type;
/*    */ 
/*    */ import com.google.common.base.Preconditions;
/*    */ import com.viaversion.viaversion.api.minecraft.blockentity.BlockEntity;
/*    */ import com.viaversion.viaversion.api.minecraft.chunks.Chunk;
/*    */ import com.viaversion.viaversion.api.minecraft.chunks.Chunk1_18;
/*    */ import com.viaversion.viaversion.api.minecraft.chunks.ChunkSection;
/*    */ import com.viaversion.viaversion.api.type.Type;
/*    */ import com.viaversion.viaversion.api.type.types.minecraft.BaseChunkType;
/*    */ import com.viaversion.viaversion.api.type.types.version.ChunkSectionType1_18;
/*    */ import com.viaversion.viaversion.api.type.types.version.Types1_20_2;
/*    */ import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
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
/*    */ 
/*    */ 
/*    */ public final class ChunkType1_20_2
/*    */   extends Type<Chunk>
/*    */ {
/*    */   private final ChunkSectionType1_18 sectionType;
/*    */   private final int ySectionCount;
/*    */   
/*    */   public ChunkType1_20_2(int ySectionCount, int globalPaletteBlockBits, int globalPaletteBiomeBits) {
/* 39 */     super(Chunk.class);
/* 40 */     Preconditions.checkArgument((ySectionCount > 0));
/* 41 */     this.sectionType = new ChunkSectionType1_18(globalPaletteBlockBits, globalPaletteBiomeBits);
/* 42 */     this.ySectionCount = ySectionCount;
/*    */   }
/*    */ 
/*    */   
/*    */   public Chunk read(ByteBuf buffer) throws Exception {
/* 47 */     int chunkX = buffer.readInt();
/* 48 */     int chunkZ = buffer.readInt();
/* 49 */     CompoundTag heightMap = (CompoundTag)Type.NAMELESS_NBT.read(buffer);
/*    */ 
/*    */     
/* 52 */     ByteBuf sectionsBuf = buffer.readBytes(Type.VAR_INT.readPrimitive(buffer));
/* 53 */     ChunkSection[] sections = new ChunkSection[this.ySectionCount];
/*    */     try {
/* 55 */       for (int j = 0; j < this.ySectionCount; j++) {
/* 56 */         sections[j] = this.sectionType.read(sectionsBuf);
/*    */       }
/*    */     } finally {
/* 59 */       sectionsBuf.release();
/*    */     } 
/*    */     
/* 62 */     int blockEntitiesLength = Type.VAR_INT.readPrimitive(buffer);
/* 63 */     List<BlockEntity> blockEntities = new ArrayList<>(blockEntitiesLength);
/* 64 */     for (int i = 0; i < blockEntitiesLength; i++) {
/* 65 */       blockEntities.add(Types1_20_2.BLOCK_ENTITY.read(buffer));
/*    */     }
/*    */     
/* 68 */     return (Chunk)new Chunk1_18(chunkX, chunkZ, sections, heightMap, blockEntities);
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(ByteBuf buffer, Chunk chunk) throws Exception {
/* 73 */     buffer.writeInt(chunk.getX());
/* 74 */     buffer.writeInt(chunk.getZ());
/*    */     
/* 76 */     Type.NAMELESS_NBT.write(buffer, chunk.getHeightMap());
/*    */     
/* 78 */     ByteBuf sectionBuffer = buffer.alloc().buffer();
/*    */     try {
/* 80 */       for (ChunkSection section : chunk.getSections()) {
/* 81 */         this.sectionType.write(sectionBuffer, section);
/*    */       }
/* 83 */       sectionBuffer.readerIndex(0);
/* 84 */       Type.VAR_INT.writePrimitive(buffer, sectionBuffer.readableBytes());
/* 85 */       buffer.writeBytes(sectionBuffer);
/*    */     } finally {
/* 87 */       sectionBuffer.release();
/*    */     } 
/*    */     
/* 90 */     Type.VAR_INT.writePrimitive(buffer, chunk.blockEntities().size());
/* 91 */     for (BlockEntity blockEntity : chunk.blockEntities()) {
/* 92 */       Types1_20_2.BLOCK_ENTITY.write(buffer, blockEntity);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public Class<? extends Type> getBaseClass() {
/* 98 */     return (Class)BaseChunkType.class;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\protocols\protocol1_20_2to1_20\type\ChunkType1_20_2.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */