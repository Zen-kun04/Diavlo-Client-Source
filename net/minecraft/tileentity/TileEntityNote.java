/*    */ package net.minecraft.tileentity;
/*    */ 
/*    */ import net.minecraft.block.material.Material;
/*    */ import net.minecraft.init.Blocks;
/*    */ import net.minecraft.nbt.NBTTagCompound;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.MathHelper;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class TileEntityNote
/*    */   extends TileEntity
/*    */ {
/*    */   public byte note;
/*    */   public boolean previousRedstoneState;
/*    */   
/*    */   public void writeToNBT(NBTTagCompound compound) {
/* 17 */     super.writeToNBT(compound);
/* 18 */     compound.setByte("note", this.note);
/*    */   }
/*    */ 
/*    */   
/*    */   public void readFromNBT(NBTTagCompound compound) {
/* 23 */     super.readFromNBT(compound);
/* 24 */     this.note = compound.getByte("note");
/* 25 */     this.note = (byte)MathHelper.clamp_int(this.note, 0, 24);
/*    */   }
/*    */ 
/*    */   
/*    */   public void changePitch() {
/* 30 */     this.note = (byte)((this.note + 1) % 25);
/* 31 */     markDirty();
/*    */   }
/*    */ 
/*    */   
/*    */   public void triggerNote(World worldIn, BlockPos p_175108_2_) {
/* 36 */     if (worldIn.getBlockState(p_175108_2_.up()).getBlock().getMaterial() == Material.air) {
/*    */       
/* 38 */       Material material = worldIn.getBlockState(p_175108_2_.down()).getBlock().getMaterial();
/* 39 */       int i = 0;
/*    */       
/* 41 */       if (material == Material.rock)
/*    */       {
/* 43 */         i = 1;
/*    */       }
/*    */       
/* 46 */       if (material == Material.sand)
/*    */       {
/* 48 */         i = 2;
/*    */       }
/*    */       
/* 51 */       if (material == Material.glass)
/*    */       {
/* 53 */         i = 3;
/*    */       }
/*    */       
/* 56 */       if (material == Material.wood)
/*    */       {
/* 58 */         i = 4;
/*    */       }
/*    */       
/* 61 */       worldIn.addBlockEvent(p_175108_2_, Blocks.noteblock, i, this.note);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\tileentity\TileEntityNote.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */