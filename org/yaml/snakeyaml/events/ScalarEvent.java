/*     */ package org.yaml.snakeyaml.events;
/*     */ 
/*     */ import org.yaml.snakeyaml.DumperOptions;
/*     */ import org.yaml.snakeyaml.error.Mark;
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
/*     */ public final class ScalarEvent
/*     */   extends NodeEvent
/*     */ {
/*     */   private final String tag;
/*     */   private final DumperOptions.ScalarStyle style;
/*     */   private final String value;
/*     */   private final ImplicitTuple implicit;
/*     */   
/*     */   public ScalarEvent(String anchor, String tag, ImplicitTuple implicit, String value, Mark startMark, Mark endMark, DumperOptions.ScalarStyle style) {
/*  36 */     super(anchor, startMark, endMark);
/*  37 */     this.tag = tag;
/*  38 */     this.implicit = implicit;
/*  39 */     if (value == null) {
/*  40 */       throw new NullPointerException("Value must be provided.");
/*     */     }
/*  42 */     this.value = value;
/*  43 */     if (style == null) {
/*  44 */       throw new NullPointerException("Style must be provided.");
/*     */     }
/*  46 */     this.style = style;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTag() {
/*  55 */     return this.tag;
/*     */   }
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
/*     */   public DumperOptions.ScalarStyle getScalarStyle() {
/*  77 */     return this.style;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getValue() {
/*  89 */     return this.value;
/*     */   }
/*     */   
/*     */   public ImplicitTuple getImplicit() {
/*  93 */     return this.implicit;
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getArguments() {
/*  98 */     return super.getArguments() + ", tag=" + this.tag + ", " + this.implicit + ", value=" + this.value;
/*     */   }
/*     */ 
/*     */   
/*     */   public Event.ID getEventId() {
/* 103 */     return Event.ID.Scalar;
/*     */   }
/*     */   
/*     */   public boolean isPlain() {
/* 107 */     return (this.style == DumperOptions.ScalarStyle.PLAIN);
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\org\yaml\snakeyaml\events\ScalarEvent.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */