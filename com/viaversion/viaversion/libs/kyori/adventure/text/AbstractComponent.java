/*    */ package com.viaversion.viaversion.libs.kyori.adventure.text;
/*    */ 
/*    */ import com.viaversion.viaversion.libs.kyori.adventure.text.format.Style;
/*    */ import com.viaversion.viaversion.libs.kyori.examination.ExaminableProperty;
/*    */ import com.viaversion.viaversion.libs.kyori.examination.string.StringExaminer;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import java.util.stream.Stream;
/*    */ import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Deprecated
/*    */ @ScheduledForRemoval(inVersion = "5.0.0")
/*    */ @Renderer(text = "this.debuggerString()", childrenArray = "this.children().toArray()", hasChildren = "!this.children().isEmpty()")
/*    */ public abstract class AbstractComponent
/*    */   implements Component
/*    */ {
/*    */   protected final List<Component> children;
/*    */   protected final Style style;
/*    */   
/*    */   protected AbstractComponent(@NotNull List<? extends ComponentLike> children, @NotNull Style style) {
/* 51 */     this.children = ComponentLike.asComponents(children, IS_NOT_EMPTY);
/* 52 */     this.style = style;
/*    */   }
/*    */   
/*    */   @NotNull
/*    */   public final List<Component> children() {
/* 57 */     return this.children;
/*    */   }
/*    */   
/*    */   @NotNull
/*    */   public final Style style() {
/* 62 */     return this.style;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(@Nullable Object other) {
/* 67 */     if (this == other) return true; 
/* 68 */     if (!(other instanceof AbstractComponent)) return false; 
/* 69 */     AbstractComponent that = (AbstractComponent)other;
/* 70 */     return (Objects.equals(this.children, that.children) && 
/* 71 */       Objects.equals(this.style, that.style));
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 76 */     int result = this.children.hashCode();
/* 77 */     result = 31 * result + this.style.hashCode();
/* 78 */     return result;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public abstract String toString();
/*    */ 
/*    */   
/*    */   private String debuggerString() {
/* 87 */     Stream<? extends ExaminableProperty> examinablePropertiesWithoutChildren = examinableProperties().filter(property -> !property.name().equals("children"));
/* 88 */     return (String)StringExaminer.simpleEscaping().examine(examinableName(), examinablePropertiesWithoutChildren);
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\libs\kyori\adventure\text\AbstractComponent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */