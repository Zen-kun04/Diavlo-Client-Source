/*     */ package net.minecraft.command;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.tileentity.TileEntity;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.Vec3i;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.gen.structure.StructureBoundingBox;
/*     */ 
/*     */ public class CommandCompare
/*     */   extends CommandBase {
/*     */   public String getCommandName() {
/*  16 */     return "testforblocks";
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRequiredPermissionLevel() {
/*  21 */     return 2;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getCommandUsage(ICommandSender sender) {
/*  26 */     return "commands.compare.usage";
/*     */   }
/*     */ 
/*     */   
/*     */   public void processCommand(ICommandSender sender, String[] args) throws CommandException {
/*  31 */     if (args.length < 9)
/*     */     {
/*  33 */       throw new WrongUsageException("commands.compare.usage", new Object[0]);
/*     */     }
/*     */ 
/*     */     
/*  37 */     sender.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, 0);
/*  38 */     BlockPos blockpos = parseBlockPos(sender, args, 0, false);
/*  39 */     BlockPos blockpos1 = parseBlockPos(sender, args, 3, false);
/*  40 */     BlockPos blockpos2 = parseBlockPos(sender, args, 6, false);
/*  41 */     StructureBoundingBox structureboundingbox = new StructureBoundingBox((Vec3i)blockpos, (Vec3i)blockpos1);
/*  42 */     StructureBoundingBox structureboundingbox1 = new StructureBoundingBox((Vec3i)blockpos2, (Vec3i)blockpos2.add(structureboundingbox.func_175896_b()));
/*  43 */     int i = structureboundingbox.getXSize() * structureboundingbox.getYSize() * structureboundingbox.getZSize();
/*     */     
/*  45 */     if (i > 524288)
/*     */     {
/*  47 */       throw new CommandException("commands.compare.tooManyBlocks", new Object[] { Integer.valueOf(i), Integer.valueOf(524288) });
/*     */     }
/*  49 */     if (structureboundingbox.minY >= 0 && structureboundingbox.maxY < 256 && structureboundingbox1.minY >= 0 && structureboundingbox1.maxY < 256) {
/*     */       
/*  51 */       World world = sender.getEntityWorld();
/*     */       
/*  53 */       if (world.isAreaLoaded(structureboundingbox) && world.isAreaLoaded(structureboundingbox1))
/*     */       {
/*  55 */         boolean flag = false;
/*     */         
/*  57 */         if (args.length > 9 && args[9].equals("masked"))
/*     */         {
/*  59 */           flag = true;
/*     */         }
/*     */         
/*  62 */         i = 0;
/*  63 */         BlockPos blockpos3 = new BlockPos(structureboundingbox1.minX - structureboundingbox.minX, structureboundingbox1.minY - structureboundingbox.minY, structureboundingbox1.minZ - structureboundingbox.minZ);
/*  64 */         BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
/*  65 */         BlockPos.MutableBlockPos blockpos$mutableblockpos1 = new BlockPos.MutableBlockPos();
/*     */         
/*  67 */         for (int j = structureboundingbox.minZ; j <= structureboundingbox.maxZ; j++) {
/*     */           
/*  69 */           for (int k = structureboundingbox.minY; k <= structureboundingbox.maxY; k++) {
/*     */             
/*  71 */             for (int l = structureboundingbox.minX; l <= structureboundingbox.maxX; l++) {
/*     */               
/*  73 */               blockpos$mutableblockpos.set(l, k, j);
/*  74 */               blockpos$mutableblockpos1.set(l + blockpos3.getX(), k + blockpos3.getY(), j + blockpos3.getZ());
/*  75 */               boolean flag1 = false;
/*  76 */               IBlockState iblockstate = world.getBlockState((BlockPos)blockpos$mutableblockpos);
/*     */               
/*  78 */               if (!flag || iblockstate.getBlock() != Blocks.air) {
/*     */                 
/*  80 */                 if (iblockstate == world.getBlockState((BlockPos)blockpos$mutableblockpos1)) {
/*     */                   
/*  82 */                   TileEntity tileentity = world.getTileEntity((BlockPos)blockpos$mutableblockpos);
/*  83 */                   TileEntity tileentity1 = world.getTileEntity((BlockPos)blockpos$mutableblockpos1);
/*     */                   
/*  85 */                   if (tileentity != null && tileentity1 != null)
/*     */                   {
/*  87 */                     NBTTagCompound nbttagcompound = new NBTTagCompound();
/*  88 */                     tileentity.writeToNBT(nbttagcompound);
/*  89 */                     nbttagcompound.removeTag("x");
/*  90 */                     nbttagcompound.removeTag("y");
/*  91 */                     nbttagcompound.removeTag("z");
/*  92 */                     NBTTagCompound nbttagcompound1 = new NBTTagCompound();
/*  93 */                     tileentity1.writeToNBT(nbttagcompound1);
/*  94 */                     nbttagcompound1.removeTag("x");
/*  95 */                     nbttagcompound1.removeTag("y");
/*  96 */                     nbttagcompound1.removeTag("z");
/*     */                     
/*  98 */                     if (!nbttagcompound.equals(nbttagcompound1))
/*     */                     {
/* 100 */                       flag1 = true;
/*     */                     }
/*     */                   }
/* 103 */                   else if (tileentity != null)
/*     */                   {
/* 105 */                     flag1 = true;
/*     */                   }
/*     */                 
/*     */                 } else {
/*     */                   
/* 110 */                   flag1 = true;
/*     */                 } 
/*     */                 
/* 113 */                 i++;
/*     */                 
/* 115 */                 if (flag1)
/*     */                 {
/* 117 */                   throw new CommandException("commands.compare.failed", new Object[0]);
/*     */                 }
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */         
/* 124 */         sender.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, i);
/* 125 */         notifyOperators(sender, this, "commands.compare.success", new Object[] { Integer.valueOf(i) });
/*     */       }
/*     */       else
/*     */       {
/* 129 */         throw new CommandException("commands.compare.outOfWorld", new Object[0]);
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 134 */       throw new CommandException("commands.compare.outOfWorld", new Object[0]);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
/* 141 */     return (args.length > 0 && args.length <= 3) ? func_175771_a(args, 0, pos) : ((args.length > 3 && args.length <= 6) ? func_175771_a(args, 3, pos) : ((args.length > 6 && args.length <= 9) ? func_175771_a(args, 6, pos) : ((args.length == 10) ? getListOfStringsMatchingLastWord(args, new String[] { "masked", "all" }) : null)));
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\command\CommandCompare.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */