/*    */ package rip.diavlo.base.api.module;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import java.util.List;
/*    */ import rip.diavlo.base.Client;
/*    */ import rip.diavlo.base.api.font.CustomFontRenderer;
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum Category
/*    */ {
/* 12 */   COMBAT("Combate", "a", Client.getInstance().getFontManager().getDefaultFont().size(18)),
/* 13 */   MOVEMENT("Movimiento", "b", Client.getInstance().getFontManager().getDefaultFont().size(18)),
/* 14 */   PLAYER("Jugador", "c", Client.getInstance().getFontManager().getDefaultFont().size(18)),
/*    */   
/* 16 */   RENDER("Render", "D", Client.getInstance().getFontManager().getDefaultFont().size(18)),
/* 17 */   EXPLOIT("Exploit", "d", Client.getInstance().getFontManager().getDefaultFont().size(18)); private final String label;
/*    */   
/*    */   Category(String label, String symbol, CustomFontRenderer font) {
/* 20 */     this.label = label;
/* 21 */     this.symbol = symbol;
/* 22 */     this.font = font;
/*    */   }
/*    */   private final String symbol; private final CustomFontRenderer font;
/*    */   public String getLabel() {
/* 26 */     return this.label;
/* 27 */   } public String getSymbol() { return this.symbol; } public CustomFontRenderer getFont() {
/* 28 */     return this.font;
/*    */   }
/*    */   public static List<Category> categories() {
/* 31 */     return Arrays.asList(values());
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\base\api\module\Category.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */