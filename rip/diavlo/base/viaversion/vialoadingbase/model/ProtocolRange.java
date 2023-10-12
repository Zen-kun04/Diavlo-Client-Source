/*    */ package rip.diavlo.base.viaversion.vialoadingbase.model;
/*    */ 
/*    */ import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
/*    */ import rip.diavlo.base.viaversion.vialoadingbase.ViaLoadingBase;
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
/*    */ public class ProtocolRange
/*    */ {
/*    */   private final ComparableProtocolVersion lowerBound;
/*    */   private final ComparableProtocolVersion upperBound;
/*    */   
/*    */   public ProtocolRange(ProtocolVersion lowerBound, ProtocolVersion upperBound) {
/* 28 */     this(ViaLoadingBase.fromProtocolVersion(lowerBound), ViaLoadingBase.fromProtocolVersion(upperBound));
/*    */   }
/*    */   
/*    */   public ProtocolRange(ComparableProtocolVersion lowerBound, ComparableProtocolVersion upperBound) {
/* 32 */     if (lowerBound == null && upperBound == null) {
/* 33 */       throw new RuntimeException("Invalid protocol range");
/*    */     }
/* 35 */     this.lowerBound = lowerBound;
/* 36 */     this.upperBound = upperBound;
/*    */   }
/*    */   
/*    */   public static ProtocolRange andNewer(ProtocolVersion version) {
/* 40 */     return new ProtocolRange(null, version);
/*    */   }
/*    */   
/*    */   public static ProtocolRange singleton(ProtocolVersion version) {
/* 44 */     return new ProtocolRange(version, version);
/*    */   }
/*    */   
/*    */   public static ProtocolRange andOlder(ProtocolVersion version) {
/* 48 */     return new ProtocolRange(version, null);
/*    */   }
/*    */   
/*    */   public boolean contains(ComparableProtocolVersion protocolVersion) {
/* 52 */     if (this.lowerBound != null && protocolVersion.getIndex() < this.lowerBound.getIndex()) return false;
/*    */     
/* 54 */     return (this.upperBound == null || protocolVersion.getIndex() <= this.upperBound.getIndex());
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 59 */     if (this.lowerBound == null) return this.upperBound.getName() + "+"; 
/* 60 */     if (this.upperBound == null) return this.lowerBound.getName() + "-"; 
/* 61 */     if (this.lowerBound == this.upperBound) return this.lowerBound.getName();
/*    */     
/* 63 */     return this.lowerBound.getName() + " - " + this.upperBound.getName();
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\base\viaversion\vialoadingbase\model\ProtocolRange.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */