/*    */ package rip.diavlo.base.api.ui.click.component.components.impl;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.FontRenderer;
/*    */ import net.minecraft.client.gui.Gui;
/*    */ import org.lwjgl.opengl.GL11;
/*    */ import rip.diavlo.base.Client;
/*    */ import rip.diavlo.base.api.module.Module;
/*    */ import rip.diavlo.base.api.ui.click.component.Component;
/*    */ import rip.diavlo.base.api.ui.click.component.components.Button;
/*    */ import rip.diavlo.base.api.value.impl.ModeValue;
/*    */ import rip.diavlo.base.modules.render.ClickGUI;
/*    */ 
/*    */ public class ModeSetting
/*    */   extends Component {
/* 16 */   private final FontRenderer fontRenderer = (Minecraft.getMinecraft()).fontRendererObj;
/*    */   
/*    */   private boolean hovered;
/*    */   
/*    */   private Button parent;
/*    */   private ModeValue set;
/*    */   private int offset;
/*    */   private int x;
/*    */   private int y;
/*    */   private Module mod;
/*    */   private int modeIndex;
/*    */   
/*    */   public ModeSetting(ModeValue set, Button button, Module mod, int offset) {
/* 29 */     this.set = set;
/* 30 */     this.parent = button;
/* 31 */     this.mod = mod;
/* 32 */     this.x = button.parent.getX() + button.parent.getWidth();
/* 33 */     this.y = button.parent.getY() + button.offset;
/* 34 */     this.offset = offset;
/* 35 */     this.modeIndex = 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setOff(int newOff) {
/* 40 */     this.offset = newOff;
/*    */   }
/*    */ 
/*    */   
/*    */   public void renderComponent() {
/* 45 */     switch ((String)((ClickGUI)Client.getInstance().getModuleManager().get(ClickGUI.class)).mode.getValue()) {
/*    */       case "Normal":
/* 47 */         Gui.drawRect((this.parent.parent.getX() + 2), (this.parent.parent.getY() + this.offset), (this.parent.parent.getX() + this.parent.parent.getWidth() * 1), (this.parent.parent.getY() + this.offset + 12), this.hovered ? -14540254 : -15658735);
/* 48 */         Gui.drawRect(this.parent.parent.getX(), (this.parent.parent.getY() + this.offset), (this.parent.parent.getX() + 2), (this.parent.parent.getY() + this.offset + 12), -15658735);
/* 49 */         GL11.glPushMatrix();
/* 50 */         GL11.glScalef(0.5F, 0.5F, 0.5F);
/* 51 */         this.fontRenderer.drawStringWithShadow("§7Mode: §f" + this.set.getValueAsString(), ((this.parent.parent.getX() + 7) * 2), ((this.parent.parent.getY() + this.offset + 2) * 2 + 5), -1);
/* 52 */         GL11.glPopMatrix();
/*    */         break;
/*    */     } 
/*    */   }
/*    */ 
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
/* 68 */       int maxIndex = this.set.getChoices().size();
/* 69 */       if (this.modeIndex + 1 > maxIndex - 1) { this.modeIndex = 0; }
/* 70 */       else { this.modeIndex++; }
/* 71 */        this.set.setValue(this.set.getChoices().get(this.modeIndex));
/*    */     } 
/*    */   }
/*    */   
/*    */   public boolean isMouseOnButton(int x, int y) {
/* 76 */     if (x > this.x && x < this.x + 88 && y > this.y && y < this.y + 12) {
/* 77 */       return true;
/*    */     }
/* 79 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\base\ap\\ui\click\component\components\impl\ModeSetting.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */