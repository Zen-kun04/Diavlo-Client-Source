/*    */ package com.viaversion.viabackwards.protocol.protocol1_19_4to1_20.data;
/*    */ 
/*    */ import com.viaversion.viabackwards.api.data.BackwardsMappings;
/*    */ import com.viaversion.viabackwards.api.data.VBMappingDataLoader;
/*    */ import com.viaversion.viaversion.api.minecraft.nbt.BinaryTagIO;
/*    */ import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
/*    */ import com.viaversion.viaversion.protocols.protocol1_20to1_19_4.Protocol1_20To1_19_4;
/*    */ import java.io.IOException;
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
/*    */ public class BackwardsMappings
/*    */   extends BackwardsMappings
/*    */ {
/*    */   private CompoundTag trimPatternRegistry;
/*    */   
/*    */   public BackwardsMappings() {
/* 32 */     super("1.20", "1.19.4", Protocol1_20To1_19_4.class);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void loadExtras(CompoundTag data) {
/* 37 */     super.loadExtras(data);
/*    */     
/*    */     try {
/* 40 */       this.trimPatternRegistry = BinaryTagIO.readInputStream(VBMappingDataLoader.getResource("trim_pattern-1.19.4.nbt"));
/* 41 */     } catch (IOException e) {
/* 42 */       e.printStackTrace();
/*    */     } 
/*    */   }
/*    */   
/*    */   public CompoundTag getTrimPatternRegistry() {
/* 47 */     return this.trimPatternRegistry;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viabackwards\protocol\protocol1_19_4to1_20\data\BackwardsMappings.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */