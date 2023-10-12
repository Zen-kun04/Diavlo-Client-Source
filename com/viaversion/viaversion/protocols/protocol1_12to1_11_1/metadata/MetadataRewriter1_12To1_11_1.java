/*    */ package com.viaversion.viaversion.protocols.protocol1_12to1_11_1.metadata;
/*    */ 
/*    */ import com.viaversion.viaversion.api.connection.UserConnection;
/*    */ import com.viaversion.viaversion.api.minecraft.entities.Entity1_12Types;
/*    */ import com.viaversion.viaversion.api.minecraft.entities.EntityType;
/*    */ import com.viaversion.viaversion.api.minecraft.item.Item;
/*    */ import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
/*    */ import com.viaversion.viaversion.api.protocol.Protocol;
/*    */ import com.viaversion.viaversion.protocols.protocol1_12to1_11_1.Protocol1_12To1_11_1;
/*    */ import com.viaversion.viaversion.protocols.protocol1_9_3to1_9_1_2.ClientboundPackets1_9_3;
/*    */ import com.viaversion.viaversion.rewriter.EntityRewriter;
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
/*    */ public class MetadataRewriter1_12To1_11_1
/*    */   extends EntityRewriter<ClientboundPackets1_9_3, Protocol1_12To1_11_1>
/*    */ {
/*    */   public MetadataRewriter1_12To1_11_1(Protocol1_12To1_11_1 protocol) {
/* 33 */     super((Protocol)protocol);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void handleMetadata(int entityId, EntityType type, Metadata metadata, List<Metadata> metadatas, UserConnection connection) {
/* 38 */     if (metadata.getValue() instanceof Item)
/*    */     {
/* 40 */       metadata.setValue(((Protocol1_12To1_11_1)this.protocol).getItemRewriter().handleItemToClient((Item)metadata.getValue()));
/*    */     }
/*    */     
/* 43 */     if (type == null)
/*    */       return; 
/* 45 */     if (type == Entity1_12Types.EntityType.EVOCATION_ILLAGER && 
/* 46 */       metadata.id() == 12) {
/* 47 */       metadata.setId(13);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public EntityType typeFromId(int type) {
/* 54 */     return (EntityType)Entity1_12Types.getTypeFromId(type, false);
/*    */   }
/*    */ 
/*    */   
/*    */   public EntityType objectTypeFromId(int type) {
/* 59 */     return (EntityType)Entity1_12Types.getTypeFromId(type, true);
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\protocols\protocol1_12to1_11_1\metadata\MetadataRewriter1_12To1_11_1.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */