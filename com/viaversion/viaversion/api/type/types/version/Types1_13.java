/*    */ package com.viaversion.viaversion.api.type.types.version;
/*    */ 
/*    */ import com.viaversion.viaversion.api.minecraft.chunks.ChunkSection;
/*    */ import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
/*    */ import com.viaversion.viaversion.api.minecraft.metadata.types.MetaTypes;
/*    */ import com.viaversion.viaversion.api.minecraft.metadata.types.MetaTypes1_13;
/*    */ import com.viaversion.viaversion.api.type.Type;
/*    */ import com.viaversion.viaversion.api.type.types.minecraft.MetaListType;
/*    */ import com.viaversion.viaversion.api.type.types.minecraft.ParticleType;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class Types1_13
/*    */ {
/* 35 */   public static final Type<ChunkSection> CHUNK_SECTION = new ChunkSectionType1_13();
/* 36 */   public static final ParticleType PARTICLE = new ParticleType();
/* 37 */   public static final MetaTypes1_13 META_TYPES = new MetaTypes1_13(PARTICLE);
/* 38 */   public static final Type<Metadata> METADATA = (Type<Metadata>)new MetadataType((MetaTypes)META_TYPES);
/* 39 */   public static final Type<List<Metadata>> METADATA_LIST = (Type<List<Metadata>>)new MetaListType(METADATA);
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\api\type\types\version\Types1_13.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */