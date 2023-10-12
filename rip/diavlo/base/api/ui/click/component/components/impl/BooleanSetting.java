/*    */ package rip.diavlo.base.api.ui.click.component.components.impl;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.FontRenderer;
/*    */ import net.minecraft.client.gui.Gui;
/*    */ import org.lwjgl.opengl.GL11;
/*    */ import rip.diavlo.base.Client;
/*    */ import rip.diavlo.base.api.ui.click.component.Component;
/*    */ import rip.diavlo.base.api.ui.click.component.components.Button;
/*    */ import rip.diavlo.base.api.value.impl.BooleanValue;
/*    */ import rip.diavlo.base.modules.render.ClickGUI;
/*    */ import rip.diavlo.base.utils.ColorUtil;
/*    */ 
/*    */ public class BooleanSetting
/*    */   extends Component
/*    */ {
/* 17 */   private final FontRenderer fontRenderer = (Minecraft.getMinecraft()).fontRendererObj;
/*    */   
/*    */   private boolean hovered;
/*    */   private BooleanValue op;
/*    */   private Button parent;
/*    */   private int offset;
/*    */   private int x;
/*    */   private int y;
/*    */   
/*    */   public BooleanSetting(BooleanValue option, Button button, int offset) {
/* 27 */     this.op = option;
/* 28 */     this.parent = button;
/* 29 */     this.x = button.parent.getX() + button.parent.getWidth();
/* 30 */     this.y = button.parent.getY() + button.offset;
/* 31 */     this.offset = offset;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void renderComponent() {
/* 37 */     switch ((String)((ClickGUI)Client.getInstance().getModuleManager().get(ClickGUI.class)).mode.getValue()) {
/*    */       case "Normal":
/* 39 */         Gui.drawRect((this.parent.parent.getX() + 2), (this.parent.parent.getY() + this.offset), (this.parent.parent.getX() + this.parent.parent.getWidth() * 1), (this.parent.parent.getY() + this.offset + 12), this.hovered ? -14540254 : -15658735);
/* 40 */         Gui.drawRect(this.parent.parent.getX(), (this.parent.parent.getY() + this.offset), (this.parent.parent.getX() + 2), (this.parent.parent.getY() + this.offset + 12), -15658735);
/* 41 */         GL11.glPushMatrix();
/* 42 */         GL11.glScalef(0.5F, 0.5F, 0.5F);
/* 43 */         this.fontRenderer.drawStringWithShadow(this.op.getName(), ((this.parent.parent.getX() + 10 + 4) * 2 + 5), ((this.parent.parent.getY() + this.offset + 2) * 2 + 4), -1);
/* 44 */         GL11.glPopMatrix();
/* 45 */         Gui.drawRect((this.parent.parent.getX() + 3 + 4), (this.parent.parent.getY() + this.offset + 3), (this.parent.parent.getX() + 9 + 4), (this.parent.parent.getY() + this.offset + 9), -6710887);
/*    */         
/* 47 */         if (((Boolean)this.op.getValue()).booleanValue()) {
/* 48 */           Gui.drawRect((this.parent.parent.getX() + 4 + 4), (this.parent.parent.getY() + this.offset + 4), (this.parent.parent.getX() + 8 + 4), (this.parent.parent.getY() + this.offset + 8), ColorUtil.redRainbow(50));
/*    */         }
/*    */         break;
/*    */     } 
/*    */   }
/*    */   
/*    */   public void setOff(int newOff) {
/* 55 */     this.offset = newOff;
/*    */   }
/*    */ 
/*    */   
/*    */   public void updateComponent(int mouseX, int mouseY) {
/* 60 */     this.hovered = isMouseOnButton(mouseX, mouseY);
/* 61 */     this.y = this.parent.parent.getY() + this.offset;
/* 62 */     this.x = this.parent.parent.getX();
/*    */   }
/*    */ 
/*    */   
/*    */   public void mouseClicked(int mouseX, int mouseY, int button) {
/* 67 */     if (isMouseOnButton(mouseX, mouseY) && button == 0 && this.parent.open) {
/* 68 */       this.op.setValue(Boolean.valueOf(!((Boolean)this.op.getValue()).booleanValue()));
/*    */     }
/*    */   }
/*    */   
/*    */   public boolean isMouseOnButton(int x, int y) {
/* 73 */     if (x > this.x && x < this.x + 88 && y > this.y && y < this.y + 12) {
/* 74 */       return true;
/*    */     }
/* 76 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\base\ap\\ui\click\component\components\impl\BooleanSetting.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */