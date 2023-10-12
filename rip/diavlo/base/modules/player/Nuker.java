/*    */ package rip.diavlo.base.modules.player;
/*    */ 
/*    */ import com.google.common.eventbus.Subscribe;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import rip.diavlo.base.api.module.Category;
/*    */ import rip.diavlo.base.api.module.Module;
/*    */ import rip.diavlo.base.events.player.UpdateEvent;
/*    */ import rip.diavlo.base.utils.BlockUtil;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Nuker
/*    */   extends Module
/*    */ {
/*    */   private boolean toggled;
/* 21 */   private Minecraft mc = Minecraft.getMinecraft();
/*    */   
/*    */   public Nuker() {
/* 24 */     super("Nuker", 0, Category.PLAYER);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Subscribe
/*    */   public void onUpdate(UpdateEvent e) {
/* 31 */     if (this.toggled) {
/*    */       
/*    */       try {
/*    */         
/* 35 */         BlockPos[] positions = BlockUtil.getBlocksAround((EntityPlayer)this.mc.thePlayer, 20, true);
/*    */         
/* 37 */         for (BlockPos p : positions) {
/* 38 */           BlockUtil.breakBlock(p);
/*    */         }
/* 40 */       } catch (Exception ea) {
/* 41 */         System.out.println(ea.getMessage());
/*    */       } 
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void onEnable() {
/* 49 */     super.onEnable();
/* 50 */     this.toggled = true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void onDisable() {
/* 55 */     super.onDisable();
/* 56 */     this.toggled = false;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\base\modules\player\Nuker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */