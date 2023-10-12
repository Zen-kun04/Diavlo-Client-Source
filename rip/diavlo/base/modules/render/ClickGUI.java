/*    */ package rip.diavlo.base.modules.render;
/*    */ 
/*    */ import net.minecraft.client.gui.GuiScreen;
/*    */ import rip.diavlo.base.api.module.Category;
/*    */ import rip.diavlo.base.api.module.Module;
/*    */ import rip.diavlo.base.api.ui.click.ClickGui;
/*    */ import rip.diavlo.base.api.value.impl.ModeValue;
/*    */ import rip.diavlo.base.utils.MinecraftUtil;
/*    */ 
/*    */ public class ClickGUI
/*    */   extends Module {
/*    */   private ClickGui clickGui;
/* 13 */   public ModeValue mode = new ModeValue("Mode", this, new String[] { "Normal" });
/*    */ 
/*    */   
/*    */   public ClickGUI() {
/* 17 */     super("ClickGUI", 54, Category.RENDER);
/* 18 */     getValues().add(this.mode);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onEnable() {
/* 23 */     if (this.clickGui == null) {
/* 24 */       this.clickGui = new ClickGui();
/*    */     }
/* 26 */     MinecraftUtil.mc.displayGuiScreen((GuiScreen)this.clickGui);
/* 27 */     toggle();
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\base\modules\render\ClickGUI.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */