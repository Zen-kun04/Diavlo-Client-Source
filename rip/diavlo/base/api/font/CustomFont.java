/*    */ package rip.diavlo.base.api.font;
/*    */ 
/*    */ import java.awt.Font;
/*    */ import java.io.InputStream;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ 
/*    */ public class CustomFont
/*    */ {
/*    */   private final String name;
/*    */   private final String path;
/* 13 */   private static final int[] FONT_SIZES = new int[] { 12, 14, 15, 16, 17, 18, 19, 20, 22, 24, 30, 50 }; private Map<Integer, CustomFontRenderer> fonts;
/*    */   public String getName() {
/* 15 */     return this.name; }
/* 16 */   public String getPath() { return this.path; } public Map<Integer, CustomFontRenderer> getFonts() {
/* 17 */     return this.fonts;
/*    */   }
/*    */   public CustomFont(String name, String path) {
/* 20 */     this.name = name;
/* 21 */     this.path = path;
/* 22 */     this.fonts = new HashMap<>();
/*    */   }
/*    */   
/*    */   public boolean setup() {
/* 26 */     boolean failed = false;
/* 27 */     for (int size : FONT_SIZES) {
/*    */       try {
/* 29 */         Font font = Font.createFont(0, Objects.<InputStream>requireNonNull(getClass().getClassLoader().getResourceAsStream(this.path))).deriveFont(0, size);
/* 30 */         this.fonts.put(Integer.valueOf(size), new CustomFontRenderer(font));
/* 31 */       } catch (Exception e) {
/* 32 */         e.printStackTrace();
/* 33 */         failed = true;
/*    */         break;
/*    */       } 
/*    */     } 
/* 37 */     return !failed;
/*    */   }
/*    */   
/*    */   public CustomFontRenderer size(int size) {
/* 41 */     if (this.fonts.get(Integer.valueOf(size)) == null) {
/*    */       try {
/* 43 */         Font font = Font.createFont(0, Objects.<InputStream>requireNonNull(getClass().getClassLoader().getResourceAsStream(this.path))).deriveFont(0, size);
/* 44 */         this.fonts.put(Integer.valueOf(size), new CustomFontRenderer(font));
/* 45 */       } catch (Exception e) {
/* 46 */         e.printStackTrace();
/*    */       } 
/* 48 */       System.out.println("WARNING! " + this.name + " Font does not have " + size + " font size. Temporarily added.");
/*    */     } 
/* 50 */     return this.fonts.get(Integer.valueOf(size));
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\base\api\font\CustomFont.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */