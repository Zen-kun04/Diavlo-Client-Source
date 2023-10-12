/*     */ package net.minecraft.client.renderer.vertex;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class VertexFormat
/*     */ {
/*  10 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public VertexFormat(VertexFormat vertexFormatIn) {
/*  20 */     this();
/*     */     
/*  22 */     for (int i = 0; i < vertexFormatIn.getElementCount(); i++)
/*     */     {
/*  24 */       addElement(vertexFormatIn.getElement(i));
/*     */     }
/*     */     
/*  27 */     this.nextOffset = vertexFormatIn.getNextOffset();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  32 */   private final List<VertexFormatElement> elements = Lists.newArrayList();
/*  33 */   private final List<Integer> offsets = Lists.newArrayList();
/*  34 */   private int nextOffset = 0;
/*  35 */   private int colorElementOffset = -1;
/*  36 */   private List<Integer> uvOffsetsById = Lists.newArrayList();
/*  37 */   private int normalElementOffset = -1;
/*     */   
/*     */   public VertexFormat() {}
/*     */   
/*     */   public void clear() {
/*  42 */     this.elements.clear();
/*  43 */     this.offsets.clear();
/*  44 */     this.colorElementOffset = -1;
/*  45 */     this.uvOffsetsById.clear();
/*  46 */     this.normalElementOffset = -1;
/*  47 */     this.nextOffset = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public VertexFormat addElement(VertexFormatElement element) {
/*  53 */     if (element.isPositionElement() && hasPosition()) {
/*     */       
/*  55 */       LOGGER.warn("VertexFormat error: Trying to add a position VertexFormatElement when one already exists, ignoring.");
/*  56 */       return this;
/*     */     } 
/*     */ 
/*     */     
/*  60 */     this.elements.add(element);
/*  61 */     this.offsets.add(Integer.valueOf(this.nextOffset));
/*     */     
/*  63 */     switch (element.getUsage()) {
/*     */       
/*     */       case NORMAL:
/*  66 */         this.normalElementOffset = this.nextOffset;
/*     */         break;
/*     */       
/*     */       case COLOR:
/*  70 */         this.colorElementOffset = this.nextOffset;
/*     */         break;
/*     */       
/*     */       case UV:
/*  74 */         this.uvOffsetsById.add(element.getIndex(), Integer.valueOf(this.nextOffset));
/*     */         break;
/*     */     } 
/*  77 */     this.nextOffset += element.getSize();
/*  78 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasNormal() {
/*  84 */     return (this.normalElementOffset >= 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getNormalOffset() {
/*  89 */     return this.normalElementOffset;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasColor() {
/*  94 */     return (this.colorElementOffset >= 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getColorOffset() {
/*  99 */     return this.colorElementOffset;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasUvOffset(int id) {
/* 104 */     return (this.uvOffsetsById.size() - 1 >= id);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getUvOffsetById(int id) {
/* 109 */     return ((Integer)this.uvOffsetsById.get(id)).intValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 114 */     String s = "format: " + this.elements.size() + " elements: ";
/*     */     
/* 116 */     for (int i = 0; i < this.elements.size(); i++) {
/*     */       
/* 118 */       s = s + ((VertexFormatElement)this.elements.get(i)).toString();
/*     */       
/* 120 */       if (i != this.elements.size() - 1)
/*     */       {
/* 122 */         s = s + " ";
/*     */       }
/*     */     } 
/*     */     
/* 126 */     return s;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean hasPosition() {
/* 131 */     int i = 0;
/*     */     
/* 133 */     for (int j = this.elements.size(); i < j; i++) {
/*     */       
/* 135 */       VertexFormatElement vertexformatelement = this.elements.get(i);
/*     */       
/* 137 */       if (vertexformatelement.isPositionElement())
/*     */       {
/* 139 */         return true;
/*     */       }
/*     */     } 
/*     */     
/* 143 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getIntegerSize() {
/* 148 */     return getNextOffset() / 4;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getNextOffset() {
/* 153 */     return this.nextOffset;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<VertexFormatElement> getElements() {
/* 158 */     return this.elements;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getElementCount() {
/* 163 */     return this.elements.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public VertexFormatElement getElement(int index) {
/* 168 */     return this.elements.get(index);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getOffset(int p_181720_1_) {
/* 173 */     return ((Integer)this.offsets.get(p_181720_1_)).intValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object p_equals_1_) {
/* 178 */     if (this == p_equals_1_)
/*     */     {
/* 180 */       return true;
/*     */     }
/* 182 */     if (p_equals_1_ != null && getClass() == p_equals_1_.getClass()) {
/*     */       
/* 184 */       VertexFormat vertexformat = (VertexFormat)p_equals_1_;
/* 185 */       return (this.nextOffset != vertexformat.nextOffset) ? false : (!this.elements.equals(vertexformat.elements) ? false : this.offsets.equals(vertexformat.offsets));
/*     */     } 
/*     */ 
/*     */     
/* 189 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 195 */     int i = this.elements.hashCode();
/* 196 */     i = 31 * i + this.offsets.hashCode();
/* 197 */     i = 31 * i + this.nextOffset;
/* 198 */     return i;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\renderer\vertex\VertexFormat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */