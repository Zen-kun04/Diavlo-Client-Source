/*    */ package com.viaversion.viaversion.rewriter.meta;
/*    */ 
/*    */ import com.viaversion.viaversion.api.connection.UserConnection;
/*    */ import com.viaversion.viaversion.api.data.entity.TrackedEntity;
/*    */ import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collections;
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
/*    */ public class MetaHandlerEventImpl
/*    */   implements MetaHandlerEvent
/*    */ {
/*    */   private final UserConnection connection;
/*    */   private final TrackedEntity trackedEntity;
/*    */   private final int entityId;
/*    */   private final List<Metadata> metadataList;
/*    */   private final Metadata meta;
/*    */   private List<Metadata> extraData;
/*    */   private boolean cancel;
/*    */   
/*    */   public MetaHandlerEventImpl(UserConnection connection, TrackedEntity trackedEntity, int entityId, Metadata meta, List<Metadata> metadataList) {
/* 40 */     this.connection = connection;
/* 41 */     this.trackedEntity = trackedEntity;
/* 42 */     this.entityId = entityId;
/* 43 */     this.meta = meta;
/* 44 */     this.metadataList = metadataList;
/*    */   }
/*    */ 
/*    */   
/*    */   public UserConnection user() {
/* 49 */     return this.connection;
/*    */   }
/*    */ 
/*    */   
/*    */   public int entityId() {
/* 54 */     return this.entityId;
/*    */   }
/*    */ 
/*    */   
/*    */   public TrackedEntity trackedEntity() {
/* 59 */     return this.trackedEntity;
/*    */   }
/*    */ 
/*    */   
/*    */   public Metadata meta() {
/* 64 */     return this.meta;
/*    */   }
/*    */ 
/*    */   
/*    */   public void cancel() {
/* 69 */     this.cancel = true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean cancelled() {
/* 74 */     return this.cancel;
/*    */   }
/*    */ 
/*    */   
/*    */   public Metadata metaAtIndex(int index) {
/* 79 */     for (Metadata meta : this.metadataList) {
/* 80 */       if (index == meta.id()) {
/* 81 */         return meta;
/*    */       }
/*    */     } 
/* 84 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public List<Metadata> metadataList() {
/* 89 */     return Collections.unmodifiableList(this.metadataList);
/*    */   }
/*    */ 
/*    */   
/*    */   public List<Metadata> extraMeta() {
/* 94 */     return this.extraData;
/*    */   }
/*    */ 
/*    */   
/*    */   public void createExtraMeta(Metadata metadata) {
/* 99 */     ((this.extraData != null) ? this.extraData : (this.extraData = new ArrayList<>())).add(metadata);
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\rewriter\meta\MetaHandlerEventImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */