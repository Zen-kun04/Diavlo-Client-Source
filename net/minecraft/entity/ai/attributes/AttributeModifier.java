/*     */ package net.minecraft.entity.ai.attributes;
/*     */ 
/*     */ import io.netty.util.internal.ThreadLocalRandom;
/*     */ import java.util.Random;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import org.apache.commons.lang3.Validate;
/*     */ 
/*     */ public class AttributeModifier
/*     */ {
/*     */   private final double amount;
/*     */   private final int operation;
/*     */   private final String name;
/*     */   private final UUID id;
/*     */   private boolean isSaved;
/*     */   
/*     */   public AttributeModifier(String nameIn, double amountIn, int operationIn) {
/*  18 */     this(MathHelper.getRandomUuid((Random)ThreadLocalRandom.current()), nameIn, amountIn, operationIn);
/*     */   }
/*     */ 
/*     */   
/*     */   public AttributeModifier(UUID idIn, String nameIn, double amountIn, int operationIn) {
/*  23 */     this.isSaved = true;
/*  24 */     this.id = idIn;
/*  25 */     this.name = nameIn;
/*  26 */     this.amount = amountIn;
/*  27 */     this.operation = operationIn;
/*  28 */     Validate.notEmpty(nameIn, "Modifier name cannot be empty", new Object[0]);
/*  29 */     Validate.inclusiveBetween(0L, 2L, operationIn, "Invalid operation");
/*     */   }
/*     */ 
/*     */   
/*     */   public UUID getID() {
/*  34 */     return this.id;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getName() {
/*  39 */     return this.name;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getOperation() {
/*  44 */     return this.operation;
/*     */   }
/*     */ 
/*     */   
/*     */   public double getAmount() {
/*  49 */     return this.amount;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSaved() {
/*  54 */     return this.isSaved;
/*     */   }
/*     */ 
/*     */   
/*     */   public AttributeModifier setSaved(boolean saved) {
/*  59 */     this.isSaved = saved;
/*  60 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object p_equals_1_) {
/*  65 */     if (this == p_equals_1_)
/*     */     {
/*  67 */       return true;
/*     */     }
/*  69 */     if (p_equals_1_ != null && getClass() == p_equals_1_.getClass()) {
/*     */       
/*  71 */       AttributeModifier attributemodifier = (AttributeModifier)p_equals_1_;
/*     */       
/*  73 */       if (this.id != null) {
/*     */         
/*  75 */         if (!this.id.equals(attributemodifier.id))
/*     */         {
/*  77 */           return false;
/*     */         }
/*     */       }
/*  80 */       else if (attributemodifier.id != null) {
/*     */         
/*  82 */         return false;
/*     */       } 
/*     */       
/*  85 */       return true;
/*     */     } 
/*     */ 
/*     */     
/*  89 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  95 */     return (this.id != null) ? this.id.hashCode() : 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 100 */     return "AttributeModifier{amount=" + this.amount + ", operation=" + this.operation + ", name='" + this.name + '\'' + ", id=" + this.id + ", serialize=" + this.isSaved + '}';
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\ai\attributes\AttributeModifier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */