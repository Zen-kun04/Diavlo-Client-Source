/*    */ package net.minecraft.block;
/*    */ 
/*    */ import net.minecraft.block.properties.IProperty;
/*    */ import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
/*    */ import net.minecraft.dispenser.IBehaviorDispenseItem;
/*    */ import net.minecraft.inventory.IInventory;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraft.tileentity.TileEntity;
/*    */ import net.minecraft.tileentity.TileEntityDispenser;
/*    */ import net.minecraft.tileentity.TileEntityDropper;
/*    */ import net.minecraft.tileentity.TileEntityHopper;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.EnumFacing;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class BlockDropper extends BlockDispenser {
/* 17 */   private final IBehaviorDispenseItem dropBehavior = (IBehaviorDispenseItem)new BehaviorDefaultDispenseItem();
/*    */ 
/*    */   
/*    */   protected IBehaviorDispenseItem getBehavior(ItemStack stack) {
/* 21 */     return this.dropBehavior;
/*    */   }
/*    */ 
/*    */   
/*    */   public TileEntity createNewTileEntity(World worldIn, int meta) {
/* 26 */     return (TileEntity)new TileEntityDropper();
/*    */   }
/*    */ 
/*    */   
/*    */   protected void dispense(World worldIn, BlockPos pos) {
/* 31 */     BlockSourceImpl blocksourceimpl = new BlockSourceImpl(worldIn, pos);
/* 32 */     TileEntityDispenser tileentitydispenser = blocksourceimpl.<TileEntityDispenser>getBlockTileEntity();
/*    */     
/* 34 */     if (tileentitydispenser != null) {
/*    */       
/* 36 */       int i = tileentitydispenser.getDispenseSlot();
/*    */       
/* 38 */       if (i < 0) {
/*    */         
/* 40 */         worldIn.playAuxSFX(1001, pos, 0);
/*    */       }
/*    */       else {
/*    */         
/* 44 */         ItemStack itemstack = tileentitydispenser.getStackInSlot(i);
/*    */         
/* 46 */         if (itemstack != null) {
/*    */           ItemStack itemstack1;
/* 48 */           EnumFacing enumfacing = (EnumFacing)worldIn.getBlockState(pos).getValue((IProperty)FACING);
/* 49 */           BlockPos blockpos = pos.offset(enumfacing);
/* 50 */           IInventory iinventory = TileEntityHopper.getInventoryAtPosition(worldIn, blockpos.getX(), blockpos.getY(), blockpos.getZ());
/*    */ 
/*    */           
/* 53 */           if (iinventory == null) {
/*    */             
/* 55 */             itemstack1 = this.dropBehavior.dispense(blocksourceimpl, itemstack);
/*    */             
/* 57 */             if (itemstack1 != null && itemstack1.stackSize <= 0)
/*    */             {
/* 59 */               itemstack1 = null;
/*    */             }
/*    */           }
/*    */           else {
/*    */             
/* 64 */             itemstack1 = TileEntityHopper.putStackInInventoryAllSlots(iinventory, itemstack.copy().splitStack(1), enumfacing.getOpposite());
/*    */             
/* 66 */             if (itemstack1 == null) {
/*    */               
/* 68 */               itemstack1 = itemstack.copy();
/*    */               
/* 70 */               if (--itemstack1.stackSize <= 0)
/*    */               {
/* 72 */                 itemstack1 = null;
/*    */               }
/*    */             }
/*    */             else {
/*    */               
/* 77 */               itemstack1 = itemstack.copy();
/*    */             } 
/*    */           } 
/*    */           
/* 81 */           tileentitydispenser.setInventorySlotContents(i, itemstack1);
/*    */         } 
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\block\BlockDropper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */