/*    */ package rip.diavlo.base.managers;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import rip.diavlo.base.api.font.CustomFont;
/*    */ 
/*    */ 
/*    */ public final class FontManager
/*    */ {
/*    */   private CustomFont defaultFont;
/*    */   private CustomFont currentFont;
/* 12 */   private final List<CustomFont> FONTS = new ArrayList<>(); public List<CustomFont> getFONTS() { return this.FONTS; }
/*    */   
/* 14 */   public CustomFont getDefaultFont() { return this.defaultFont; } public CustomFont getCurrentFont() { return this.currentFont; }
/*    */ 
/*    */ 
/*    */   
/*    */   public void init() {
/* 19 */     add(new CustomFont[] { new CustomFont("Manrope", "assets/minecraft/client/fonts/Manrope.ttf"), new CustomFont("Icon1", "assets/minecraft/client/fonts/Icon-1.ttf"), new CustomFont("Icon2", "assets/minecraft/client/fonts/Icon-2.ttf"), new CustomFont("Icon3", "assets/minecraft/client/fonts/Icon-3.ttf") });
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
/* 30 */     this.defaultFont = this.FONTS.get(0);
/*    */     
/* 32 */     this.currentFont = this.defaultFont;
/*    */   }
/*    */   
/*    */   public void load(CustomFont font) {
/* 36 */     if (font == null)
/* 37 */       return;  if (!this.FONTS.contains(font)) add(new CustomFont[] { font }); 
/* 38 */     this.currentFont = font;
/*    */   }
/*    */   
/*    */   public void add(CustomFont... fonts) {
/* 42 */     for (CustomFont font : fonts) {
/* 43 */       if (!font.setup()) { System.out.println("An error has occurred while attempting to load \"" + font.getName() + "\""); }
/* 44 */       else { System.out.println("Successfully loaded \"" + font.getName() + "\""); }
/* 45 */        this.FONTS.add(font);
/*    */     } 
/*    */   }
/*    */   
/*    */   public CustomFont getFontBy(String name) {
/* 50 */     return this.FONTS.stream().filter(customFont -> customFont.getName().equalsIgnoreCase(name)).findFirst().orElse(this.defaultFont);
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\base\managers\FontManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */