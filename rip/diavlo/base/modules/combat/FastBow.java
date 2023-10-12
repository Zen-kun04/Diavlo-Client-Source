/*    */ package rip.diavlo.base.modules.combat;
/*    */ 
/*    */ import com.google.common.eventbus.Subscribe;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.item.Item;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.play.client.C03PacketPlayer;
/*    */ import net.minecraft.network.play.client.C07PacketPlayerDigging;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.EnumFacing;
/*    */ import net.minecraft.world.World;
/*    */ import rip.diavlo.base.api.module.Category;
/*    */ import rip.diavlo.base.api.module.Module;
/*    */ import rip.diavlo.base.events.player.UpdateEvent;
/*    */ 
/*    */ public class FastBow
/*    */   extends Module {
/*    */   private int arrowsShoot;
/*    */   
/*    */   public FastBow() {
/* 22 */     super("FastBow", 48, Category.COMBAT);
/*    */   }
/*    */ 
/*    */   
/*    */   @Subscribe
/*    */   public void onUpdate(UpdateEvent event) {
/* 28 */     Minecraft mc = Minecraft.getMinecraft();
/* 29 */     if (isToggled() && 
/* 30 */       mc.thePlayer.getHealth() > 0.0F && ((mc.thePlayer.onGround && mc.thePlayer.inventory
/* 31 */       .hasItem(Item.getItemById(262))) || (Minecraft.getMinecraft()).thePlayer.capabilities.isCreativeMode) && mc.thePlayer.inventory
/* 32 */       .getCurrentItem() != null && mc.thePlayer.inventory.getCurrentItem().getItem() instanceof net.minecraft.item.ItemBow && mc.gameSettings.keyBindUseItem.pressed) {
/*    */ 
/*    */       
/* 35 */       mc.playerController.sendUseItem((EntityPlayer)mc.thePlayer, (World)mc.theWorld, mc.thePlayer.inventory.getCurrentItem());
/* 36 */       this.arrowsShoot++;
/* 37 */       mc.thePlayer.inventory.getCurrentItem().getItem().onItemRightClick(mc.thePlayer.inventory.getCurrentItem(), (World)mc.theWorld, (EntityPlayer)mc.thePlayer);
/*    */       
/* 39 */       for (int i = 0; i < 20; i++) {
/* 40 */         mc.thePlayer.sendQueue.addToSendQueue((Packet)new C03PacketPlayer(false));
/*    */       }
/*    */       
/* 43 */       mc.getNetHandler().addToSendQueue((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(0, 0, 0), EnumFacing.DOWN));
/* 44 */       mc.thePlayer.inventory.getCurrentItem().getItem().onPlayerStoppedUsing(mc.thePlayer.inventory.getCurrentItem(), (World)mc.theWorld, (EntityPlayer)mc.thePlayer, 10);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void onEnable() {
/* 51 */     super.onEnable();
/* 52 */     this.arrowsShoot = 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public void onDisable() {
/* 57 */     super.onDisable();
/* 58 */     this.arrowsShoot = 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getSuffix() {
/* 63 */     return "Flechas disparadas: " + this.arrowsShoot;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\base\modules\combat\FastBow.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */