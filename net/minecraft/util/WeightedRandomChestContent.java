/*    */ package net.minecraft.util;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import java.util.Random;
/*    */ import net.minecraft.inventory.IInventory;
/*    */ import net.minecraft.item.Item;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraft.tileentity.TileEntityDispenser;
/*    */ 
/*    */ 
/*    */ public class WeightedRandomChestContent
/*    */   extends WeightedRandom.Item
/*    */ {
/*    */   private ItemStack theItemId;
/*    */   private int minStackSize;
/*    */   private int maxStackSize;
/*    */   
/*    */   public WeightedRandomChestContent(Item p_i45311_1_, int p_i45311_2_, int minimumChance, int maximumChance, int itemWeightIn) {
/* 21 */     super(itemWeightIn);
/* 22 */     this.theItemId = new ItemStack(p_i45311_1_, 1, p_i45311_2_);
/* 23 */     this.minStackSize = minimumChance;
/* 24 */     this.maxStackSize = maximumChance;
/*    */   }
/*    */ 
/*    */   
/*    */   public WeightedRandomChestContent(ItemStack stack, int minimumChance, int maximumChance, int itemWeightIn) {
/* 29 */     super(itemWeightIn);
/* 30 */     this.theItemId = stack;
/* 31 */     this.minStackSize = minimumChance;
/* 32 */     this.maxStackSize = maximumChance;
/*    */   }
/*    */ 
/*    */   
/*    */   public static void generateChestContents(Random random, List<WeightedRandomChestContent> listIn, IInventory inv, int max) {
/* 37 */     for (int i = 0; i < max; i++) {
/*    */       
/* 39 */       WeightedRandomChestContent weightedrandomchestcontent = WeightedRandom.<WeightedRandomChestContent>getRandomItem(random, listIn);
/* 40 */       int j = weightedrandomchestcontent.minStackSize + random.nextInt(weightedrandomchestcontent.maxStackSize - weightedrandomchestcontent.minStackSize + 1);
/*    */       
/* 42 */       if (weightedrandomchestcontent.theItemId.getMaxStackSize() >= j) {
/*    */         
/* 44 */         ItemStack itemstack1 = weightedrandomchestcontent.theItemId.copy();
/* 45 */         itemstack1.stackSize = j;
/* 46 */         inv.setInventorySlotContents(random.nextInt(inv.getSizeInventory()), itemstack1);
/*    */       }
/*    */       else {
/*    */         
/* 50 */         for (int k = 0; k < j; k++) {
/*    */           
/* 52 */           ItemStack itemstack = weightedrandomchestcontent.theItemId.copy();
/* 53 */           itemstack.stackSize = 1;
/* 54 */           inv.setInventorySlotContents(random.nextInt(inv.getSizeInventory()), itemstack);
/*    */         } 
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public static void generateDispenserContents(Random random, List<WeightedRandomChestContent> listIn, TileEntityDispenser dispenser, int max) {
/* 62 */     for (int i = 0; i < max; i++) {
/*    */       
/* 64 */       WeightedRandomChestContent weightedrandomchestcontent = WeightedRandom.<WeightedRandomChestContent>getRandomItem(random, listIn);
/* 65 */       int j = weightedrandomchestcontent.minStackSize + random.nextInt(weightedrandomchestcontent.maxStackSize - weightedrandomchestcontent.minStackSize + 1);
/*    */       
/* 67 */       if (weightedrandomchestcontent.theItemId.getMaxStackSize() >= j) {
/*    */         
/* 69 */         ItemStack itemstack1 = weightedrandomchestcontent.theItemId.copy();
/* 70 */         itemstack1.stackSize = j;
/* 71 */         dispenser.setInventorySlotContents(random.nextInt(dispenser.getSizeInventory()), itemstack1);
/*    */       }
/*    */       else {
/*    */         
/* 75 */         for (int k = 0; k < j; k++) {
/*    */           
/* 77 */           ItemStack itemstack = weightedrandomchestcontent.theItemId.copy();
/* 78 */           itemstack.stackSize = 1;
/* 79 */           dispenser.setInventorySlotContents(random.nextInt(dispenser.getSizeInventory()), itemstack);
/*    */         } 
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public static List<WeightedRandomChestContent> func_177629_a(List<WeightedRandomChestContent> p_177629_0_, WeightedRandomChestContent... p_177629_1_) {
/* 87 */     List<WeightedRandomChestContent> list = Lists.newArrayList(p_177629_0_);
/* 88 */     Collections.addAll(list, p_177629_1_);
/* 89 */     return list;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraf\\util\WeightedRandomChestContent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */