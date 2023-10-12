/*    */ package rip.diavlo.base.api.ui;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.concurrent.atomic.AtomicInteger;
/*    */ import net.minecraft.client.gui.GuiScreen;
/*    */ 
/*    */ 
/*    */ public class GuiCrediti
/*    */   extends GuiScreen
/*    */ {
/* 11 */   ArrayList<String> autores = new ArrayList<>();
/*    */   
/*    */   protected GuiScreen prevScreen;
/*    */   
/*    */   public GuiCrediti(GuiScreen screen) {
/* 16 */     this.prevScreen = screen;
/* 17 */     this.autores.add("SerLink04 [Owner]");
/* 18 */     this.autores.add("Bobina [Owner]");
/* 19 */     this.autores.add("Russian412 [Developer]");
/* 20 */     this.autores.add("Discord: discord.gg/programadores");
/*    */   }
/*    */   
/*    */   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
/* 24 */     drawDefaultBackground();
/* 25 */     drawCenteredString(this.fontRendererObj, "Autores del Diavlo Client", this.width / 2, this.height / 4, 16777215);
/* 26 */     AtomicInteger offset = new AtomicInteger(20);
/* 27 */     this.autores.forEach(a -> {
/*    */           drawCenteredString(this.fontRendererObj, a, this.width / 2, this.height / 4 + 10 + offset.get(), 16777215);
/*    */           offset.addAndGet(20);
/*    */         });
/* 31 */     super.drawScreen(mouseX, mouseY, partialTicks);
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\base\ap\\ui\GuiCrediti.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */