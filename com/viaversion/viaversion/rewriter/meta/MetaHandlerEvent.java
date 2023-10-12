/*     */ package com.viaversion.viaversion.rewriter.meta;
/*     */ 
/*     */ import com.viaversion.viaversion.api.connection.UserConnection;
/*     */ import com.viaversion.viaversion.api.data.entity.TrackedEntity;
/*     */ import com.viaversion.viaversion.api.minecraft.entities.EntityType;
/*     */ import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
/*     */ import java.util.List;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface MetaHandlerEvent
/*     */ {
/*     */   UserConnection user();
/*     */   
/*     */   int entityId();
/*     */   
/*     */   TrackedEntity trackedEntity();
/*     */   
/*     */   default EntityType entityType() {
/*  57 */     return (trackedEntity() != null) ? trackedEntity().entityType() : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default int index() {
/*  66 */     return meta().id();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default void setIndex(int index) {
/*  75 */     meta().setId(index);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Metadata meta();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void cancel();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean cancelled();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Metadata metaAtIndex(int paramInt);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   List<Metadata> metadataList();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   List<Metadata> extraMeta();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default boolean hasExtraMeta() {
/* 130 */     return (extraMeta() != null);
/*     */   }
/*     */   
/*     */   void createExtraMeta(Metadata paramMetadata);
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\rewriter\meta\MetaHandlerEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */