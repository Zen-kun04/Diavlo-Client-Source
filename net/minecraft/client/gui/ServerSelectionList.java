/*    */ package net.minecraft.client.gui;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import java.util.List;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.multiplayer.ServerList;
/*    */ import net.minecraft.client.network.LanServerDetector;
/*    */ 
/*    */ public class ServerSelectionList
/*    */   extends GuiListExtended {
/*    */   private final GuiMultiplayer owner;
/* 12 */   private final List<ServerListEntryNormal> serverListInternet = Lists.newArrayList();
/* 13 */   private final List<ServerListEntryLanDetected> serverListLan = Lists.newArrayList();
/* 14 */   private final GuiListExtended.IGuiListEntry lanScanEntry = new ServerListEntryLanScan();
/* 15 */   private int selectedSlotIndex = -1;
/*    */ 
/*    */   
/*    */   public ServerSelectionList(GuiMultiplayer ownerIn, Minecraft mcIn, int widthIn, int heightIn, int topIn, int bottomIn, int slotHeightIn) {
/* 19 */     super(mcIn, widthIn, heightIn, topIn, bottomIn, slotHeightIn);
/* 20 */     this.owner = ownerIn;
/*    */   }
/*    */ 
/*    */   
/*    */   public GuiListExtended.IGuiListEntry getListEntry(int index) {
/* 25 */     if (index < this.serverListInternet.size())
/*    */     {
/* 27 */       return this.serverListInternet.get(index);
/*    */     }
/*    */ 
/*    */     
/* 31 */     index -= this.serverListInternet.size();
/*    */     
/* 33 */     if (index == 0)
/*    */     {
/* 35 */       return this.lanScanEntry;
/*    */     }
/*    */ 
/*    */     
/* 39 */     index--;
/* 40 */     return this.serverListLan.get(index);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected int getSize() {
/* 47 */     return this.serverListInternet.size() + 1 + this.serverListLan.size();
/*    */   }
/*    */ 
/*    */   
/*    */   public void setSelectedSlotIndex(int selectedSlotIndexIn) {
/* 52 */     this.selectedSlotIndex = selectedSlotIndexIn;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isSelected(int slotIndex) {
/* 57 */     return (slotIndex == this.selectedSlotIndex);
/*    */   }
/*    */ 
/*    */   
/*    */   public int func_148193_k() {
/* 62 */     return this.selectedSlotIndex;
/*    */   }
/*    */ 
/*    */   
/*    */   public void func_148195_a(ServerList p_148195_1_) {
/* 67 */     this.serverListInternet.clear();
/*    */     
/* 69 */     for (int i = 0; i < p_148195_1_.countServers(); i++)
/*    */     {
/* 71 */       this.serverListInternet.add(new ServerListEntryNormal(this.owner, p_148195_1_.getServerData(i)));
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void func_148194_a(List<LanServerDetector.LanServer> p_148194_1_) {
/* 77 */     this.serverListLan.clear();
/*    */     
/* 79 */     for (LanServerDetector.LanServer lanserverdetector$lanserver : p_148194_1_)
/*    */     {
/* 81 */       this.serverListLan.add(new ServerListEntryLanDetected(this.owner, lanserverdetector$lanserver));
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   protected int getScrollBarX() {
/* 87 */     return super.getScrollBarX() + 30;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getListWidth() {
/* 92 */     return super.getListWidth() + 85;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\gui\ServerSelectionList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */