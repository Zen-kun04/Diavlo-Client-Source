/*    */ package com.viaversion.viaversion.api.type.types.minecraft;
/*    */ 
/*    */ import com.viaversion.viaversion.api.type.Type;
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
/*    */ @Deprecated
/*    */ public class Particle1_16Type
/*    */   extends AbstractParticleType
/*    */ {
/*    */   public Particle1_16Type() {
/* 31 */     this.readers.put(3, blockHandler());
/* 32 */     this.readers.put(23, blockHandler());
/* 33 */     this.readers.put(14, dustHandler());
/* 34 */     this.readers.put(34, itemHandler(Type.FLAT_VAR_INT_ITEM));
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\api\type\types\minecraft\Particle1_16Type.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */