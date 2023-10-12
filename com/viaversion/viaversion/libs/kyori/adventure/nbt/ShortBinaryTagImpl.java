/*    */ package com.viaversion.viaversion.libs.kyori.adventure.nbt;
/*    */ 
/*    */ import com.viaversion.viaversion.libs.kyori.examination.ExaminableProperty;
/*    */ import java.util.stream.Stream;
/*    */ import org.jetbrains.annotations.Debug.Renderer;
/*    */ import org.jetbrains.annotations.NotNull;
/*    */ import org.jetbrains.annotations.Nullable;
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
/*    */ @Renderer(text = "String.valueOf(this.value) + \"s\"", hasChildren = "false")
/*    */ final class ShortBinaryTagImpl
/*    */   extends AbstractBinaryTag
/*    */   implements ShortBinaryTag
/*    */ {
/*    */   private final short value;
/*    */   
/*    */   ShortBinaryTagImpl(short value) {
/* 37 */     this.value = value;
/*    */   }
/*    */ 
/*    */   
/*    */   public short value() {
/* 42 */     return this.value;
/*    */   }
/*    */ 
/*    */   
/*    */   public byte byteValue() {
/* 47 */     return (byte)(this.value & 0xFF);
/*    */   }
/*    */ 
/*    */   
/*    */   public double doubleValue() {
/* 52 */     return this.value;
/*    */   }
/*    */ 
/*    */   
/*    */   public float floatValue() {
/* 57 */     return this.value;
/*    */   }
/*    */ 
/*    */   
/*    */   public int intValue() {
/* 62 */     return this.value;
/*    */   }
/*    */ 
/*    */   
/*    */   public long longValue() {
/* 67 */     return this.value;
/*    */   }
/*    */ 
/*    */   
/*    */   public short shortValue() {
/* 72 */     return this.value;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(@Nullable Object other) {
/* 77 */     if (this == other) return true; 
/* 78 */     if (other == null || getClass() != other.getClass()) return false; 
/* 79 */     ShortBinaryTagImpl that = (ShortBinaryTagImpl)other;
/* 80 */     return (this.value == that.value);
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 85 */     return Short.hashCode(this.value);
/*    */   }
/*    */   
/*    */   @NotNull
/*    */   public Stream<? extends ExaminableProperty> examinableProperties() {
/* 90 */     return Stream.of(ExaminableProperty.of("value", this.value));
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\libs\kyori\adventure\nbt\ShortBinaryTagImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */