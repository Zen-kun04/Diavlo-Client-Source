/*    */ package rip.diavlo.base.modules.render;
/*    */ 
/*    */ import net.minecraft.client.gui.GuiScreen;
/*    */ import rip.diavlo.base.api.module.Category;
/*    */ import rip.diavlo.base.api.module.Module;
/*    */ import rip.diavlo.base.api.value.impl.ModeValue;
/*    */ import rip.diavlo.base.ui.NewUI;
/*    */ 
/*    */ public class ClickGUIModule extends Module {
/* 10 */   private final ModeValue mode = new ModeValue("Style", this, new String[] { "NewUI" });
/*    */   
/*    */   private NewUI clickGui;
/*    */   
/*    */   public ClickGUIModule() {
/* 15 */     super("ClickGUI", 54, Category.RENDER, "Allows you to toggle modules");
/* 16 */     getValues().add(this.mode);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onEnable() {
/* 21 */     if (this.clickGui == null) this.clickGui = new NewUI(); 
/* 22 */     this.clickGui.initGui();
/* 23 */     mc.displayGuiScreen((GuiScreen)this.clickGui);
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\base\modules\render\ClickGUIModule.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */