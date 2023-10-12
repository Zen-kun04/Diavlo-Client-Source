/*    */ package rip.diavlo.base.utils;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import java.util.TreeSet;
/*    */ import java.util.regex.Matcher;
/*    */ import java.util.regex.Pattern;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.network.NetworkPlayerInfo;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ 
/*    */ 
/*    */ public class TabUtils
/*    */ {
/* 16 */   private static Minecraft mc = Minecraft.getMinecraft();
/*    */   
/*    */   public static boolean notNPC(String nickname) {
/* 19 */     List<String> averageNPCName = Arrays.asList(new String[] { "Shop", "Tienda", "Discord", "NameMC", "Vote", "Votos", "[NPC]", "Cosmetics", "rankup", "mines", "store", "auction", "rewards" });
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 24 */     return averageNPCName.stream().noneMatch(nickname::contains);
/*    */   }
/*    */   
/*    */   public static boolean notTabLine(String name) {
/* 28 */     String regex = "§[0-9]§[0-9]§r";
/*    */     
/* 30 */     Pattern pattern = Pattern.compile(regex);
/* 31 */     Matcher matcher = pattern.matcher(name);
/*    */     
/* 33 */     return !matcher.find();
/*    */   }
/*    */ 
/*    */   
/*    */   public static Set<String> getVIPPlayers() {
/* 38 */     Set<String> vips = new TreeSet<>();
/*    */     
/*    */     try {
/* 41 */       for (EntityPlayer p : mc.theWorld.playerEntities) {
/*    */         
/*    */         try {
/* 44 */           if (!p.getGameProfile().getName().equalsIgnoreCase(mc.thePlayer.getGameProfile().getName()) && p.getDisplayName() != null && 
/* 45 */             !p.getDisplayName().getUnformattedTextForChat().contains("§7" + p.getGameProfile().getName()) && notNPC(p.getDisplayName().getFormattedText())) {
/* 46 */             vips.add(p.getGameProfile().getName());
/*    */           }
/* 48 */         } catch (Exception exception) {}
/*    */       } 
/*    */ 
/*    */       
/* 52 */       for (NetworkPlayerInfo p : mc.ingameGUI.getTabList().getTabPlayers()) {
/*    */         try {
/* 54 */           if (!p.getGameProfile().getName().equalsIgnoreCase(mc.thePlayer.getGameProfile().getName()) && p.getDisplayName() != null && 
/* 55 */             !p.getDisplayName().getUnformattedTextForChat().contains("§7" + p.getGameProfile().getName()) && notNPC(p.getDisplayName().getFormattedText()) && p
/* 56 */             .getGameProfile().getName().length() >= 3 && notTabLine(p.getGameProfile().getName()) && !p.getGameProfile().getName().isEmpty()) {
/* 57 */             vips.add(p.getGameProfile().getName());
/*    */           }
/* 59 */         } catch (Exception exception) {}
/*    */       }
/*    */     
/* 62 */     } catch (Exception exception) {}
/* 63 */     return vips;
/*    */   }
/*    */ 
/*    */   
/*    */   public static Set<String> getPlayers() {
/* 68 */     Set<String> vips = new TreeSet<>();
/*    */     
/*    */     try {
/* 71 */       for (EntityPlayer p : mc.theWorld.playerEntities) {
/*    */         
/*    */         try {
/* 74 */           if (!p.getGameProfile().getName().equalsIgnoreCase(mc.thePlayer.getGameProfile().getName()) && p.getDisplayName() != null && 
/* 75 */             notNPC(p.getDisplayName().getFormattedText())) {
/* 76 */             vips.add(p.getGameProfile().getName());
/*    */           }
/* 78 */         } catch (Exception exception) {}
/*    */       } 
/*    */ 
/*    */       
/* 82 */       for (NetworkPlayerInfo p : mc.ingameGUI.getTabList().getTabPlayers()) {
/*    */         try {
/* 84 */           if (!p.getGameProfile().getName().equalsIgnoreCase(mc.thePlayer.getGameProfile().getName()) && p.getDisplayName() != null && 
/* 85 */             notNPC(p.getDisplayName().getFormattedText()) && p
/* 86 */             .getGameProfile().getName().length() >= 3 && notTabLine(p.getGameProfile().getName()) && !p.getGameProfile().getName().isEmpty()) {
/* 87 */             vips.add(p.getGameProfile().getName());
/*    */           }
/* 89 */         } catch (Exception exception) {}
/*    */       }
/*    */     
/* 92 */     } catch (Exception exception) {}
/* 93 */     return vips;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\bas\\utils\TabUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */