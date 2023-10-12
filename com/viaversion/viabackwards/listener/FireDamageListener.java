/*    */ package com.viaversion.viabackwards.listener;
/*    */ 
/*    */ import com.viaversion.viabackwards.BukkitPlugin;
/*    */ import com.viaversion.viabackwards.protocol.protocol1_11_1to1_12.Protocol1_11_1To1_12;
/*    */ import com.viaversion.viaversion.bukkit.listeners.ViaBukkitListener;
/*    */ import org.bukkit.Sound;
/*    */ import org.bukkit.SoundCategory;
/*    */ import org.bukkit.entity.EntityType;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.event.EventHandler;
/*    */ import org.bukkit.event.EventPriority;
/*    */ import org.bukkit.event.entity.EntityDamageEvent;
/*    */ import org.bukkit.plugin.Plugin;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FireDamageListener
/*    */   extends ViaBukkitListener
/*    */ {
/*    */   public FireDamageListener(BukkitPlugin plugin) {
/* 34 */     super((Plugin)plugin, Protocol1_11_1To1_12.class);
/*    */   }
/*    */   
/*    */   @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
/*    */   public void onFireDamage(EntityDamageEvent event) {
/* 39 */     if (event.getEntityType() != EntityType.PLAYER)
/*    */       return; 
/* 41 */     EntityDamageEvent.DamageCause cause = event.getCause();
/* 42 */     if (cause != EntityDamageEvent.DamageCause.FIRE && cause != EntityDamageEvent.DamageCause.FIRE_TICK && cause != EntityDamageEvent.DamageCause.LAVA && cause != EntityDamageEvent.DamageCause.DROWNING) {
/*    */       return;
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 49 */     Player player = (Player)event.getEntity();
/* 50 */     if (isOnPipe(player))
/* 51 */       player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_HURT, SoundCategory.PLAYERS, 1.0F, 1.0F); 
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viabackwards\listener\FireDamageListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */