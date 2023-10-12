/*     */ package net.minecraft.command;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.inventory.IInventory;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.tileentity.TileEntity;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.Vec3i;
/*     */ import net.minecraft.world.NextTickListEntry;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.gen.structure.StructureBoundingBox;
/*     */ 
/*     */ public class CommandClone
/*     */   extends CommandBase {
/*     */   public String getCommandName() {
/*  21 */     return "clone";
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRequiredPermissionLevel() {
/*  26 */     return 2;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getCommandUsage(ICommandSender sender) {
/*  31 */     return "commands.clone.usage";
/*     */   }
/*     */ 
/*     */   
/*     */   public void processCommand(ICommandSender sender, String[] args) throws CommandException {
/*  36 */     if (args.length < 9)
/*     */     {
/*  38 */       throw new WrongUsageException("commands.clone.usage", new Object[0]);
/*     */     }
/*     */ 
/*     */     
/*  42 */     sender.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, 0);
/*  43 */     BlockPos blockpos = parseBlockPos(sender, args, 0, false);
/*  44 */     BlockPos blockpos1 = parseBlockPos(sender, args, 3, false);
/*  45 */     BlockPos blockpos2 = parseBlockPos(sender, args, 6, false);
/*  46 */     StructureBoundingBox structureboundingbox = new StructureBoundingBox((Vec3i)blockpos, (Vec3i)blockpos1);
/*  47 */     StructureBoundingBox structureboundingbox1 = new StructureBoundingBox((Vec3i)blockpos2, (Vec3i)blockpos2.add(structureboundingbox.func_175896_b()));
/*  48 */     int i = structureboundingbox.getXSize() * structureboundingbox.getYSize() * structureboundingbox.getZSize();
/*     */     
/*  50 */     if (i > 32768)
/*     */     {
/*  52 */       throw new CommandException("commands.clone.tooManyBlocks", new Object[] { Integer.valueOf(i), Integer.valueOf(32768) });
/*     */     }
/*     */ 
/*     */     
/*  56 */     boolean flag = false;
/*  57 */     Block block = null;
/*  58 */     int j = -1;
/*     */     
/*  60 */     if ((args.length < 11 || (!args[10].equals("force") && !args[10].equals("move"))) && structureboundingbox.intersectsWith(structureboundingbox1))
/*     */     {
/*  62 */       throw new CommandException("commands.clone.noOverlap", new Object[0]);
/*     */     }
/*     */ 
/*     */     
/*  66 */     if (args.length >= 11 && args[10].equals("move"))
/*     */     {
/*  68 */       flag = true;
/*     */     }
/*     */     
/*  71 */     if (structureboundingbox.minY >= 0 && structureboundingbox.maxY < 256 && structureboundingbox1.minY >= 0 && structureboundingbox1.maxY < 256) {
/*     */       
/*  73 */       World world = sender.getEntityWorld();
/*     */       
/*  75 */       if (world.isAreaLoaded(structureboundingbox) && world.isAreaLoaded(structureboundingbox1))
/*     */       {
/*  77 */         boolean flag1 = false;
/*     */         
/*  79 */         if (args.length >= 10)
/*     */         {
/*  81 */           if (args[9].equals("masked")) {
/*     */             
/*  83 */             flag1 = true;
/*     */           }
/*  85 */           else if (args[9].equals("filtered")) {
/*     */             
/*  87 */             if (args.length < 12)
/*     */             {
/*  89 */               throw new WrongUsageException("commands.clone.usage", new Object[0]);
/*     */             }
/*     */             
/*  92 */             block = getBlockByText(sender, args[11]);
/*     */             
/*  94 */             if (args.length >= 13)
/*     */             {
/*  96 */               j = parseInt(args[12], 0, 15);
/*     */             }
/*     */           } 
/*     */         }
/*     */         
/* 101 */         List<StaticCloneData> list = Lists.newArrayList();
/* 102 */         List<StaticCloneData> list1 = Lists.newArrayList();
/* 103 */         List<StaticCloneData> list2 = Lists.newArrayList();
/* 104 */         LinkedList<BlockPos> linkedlist = Lists.newLinkedList();
/* 105 */         BlockPos blockpos3 = new BlockPos(structureboundingbox1.minX - structureboundingbox.minX, structureboundingbox1.minY - structureboundingbox.minY, structureboundingbox1.minZ - structureboundingbox.minZ);
/*     */         
/* 107 */         for (int k = structureboundingbox.minZ; k <= structureboundingbox.maxZ; k++) {
/*     */           
/* 109 */           for (int l = structureboundingbox.minY; l <= structureboundingbox.maxY; l++) {
/*     */             
/* 111 */             for (int i1 = structureboundingbox.minX; i1 <= structureboundingbox.maxX; i1++) {
/*     */               
/* 113 */               BlockPos blockpos4 = new BlockPos(i1, l, k);
/* 114 */               BlockPos blockpos5 = blockpos4.add((Vec3i)blockpos3);
/* 115 */               IBlockState iblockstate = world.getBlockState(blockpos4);
/*     */               
/* 117 */               if ((!flag1 || iblockstate.getBlock() != Blocks.air) && (block == null || (iblockstate.getBlock() == block && (j < 0 || iblockstate.getBlock().getMetaFromState(iblockstate) == j)))) {
/*     */                 
/* 119 */                 TileEntity tileentity = world.getTileEntity(blockpos4);
/*     */                 
/* 121 */                 if (tileentity != null) {
/*     */                   
/* 123 */                   NBTTagCompound nbttagcompound = new NBTTagCompound();
/* 124 */                   tileentity.writeToNBT(nbttagcompound);
/* 125 */                   list1.add(new StaticCloneData(blockpos5, iblockstate, nbttagcompound));
/* 126 */                   linkedlist.addLast(blockpos4);
/*     */                 }
/* 128 */                 else if (!iblockstate.getBlock().isFullBlock() && !iblockstate.getBlock().isFullCube()) {
/*     */                   
/* 130 */                   list2.add(new StaticCloneData(blockpos5, iblockstate, (NBTTagCompound)null));
/* 131 */                   linkedlist.addFirst(blockpos4);
/*     */                 }
/*     */                 else {
/*     */                   
/* 135 */                   list.add(new StaticCloneData(blockpos5, iblockstate, (NBTTagCompound)null));
/* 136 */                   linkedlist.addLast(blockpos4);
/*     */                 } 
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */         
/* 143 */         if (flag) {
/*     */           
/* 145 */           for (BlockPos blockpos6 : linkedlist) {
/*     */             
/* 147 */             TileEntity tileentity1 = world.getTileEntity(blockpos6);
/*     */             
/* 149 */             if (tileentity1 instanceof IInventory)
/*     */             {
/* 151 */               ((IInventory)tileentity1).clear();
/*     */             }
/*     */             
/* 154 */             world.setBlockState(blockpos6, Blocks.barrier.getDefaultState(), 2);
/*     */           } 
/*     */           
/* 157 */           for (BlockPos blockpos7 : linkedlist)
/*     */           {
/* 159 */             world.setBlockState(blockpos7, Blocks.air.getDefaultState(), 3);
/*     */           }
/*     */         } 
/*     */         
/* 163 */         List<StaticCloneData> list3 = Lists.newArrayList();
/* 164 */         list3.addAll(list);
/* 165 */         list3.addAll(list1);
/* 166 */         list3.addAll(list2);
/* 167 */         List<StaticCloneData> list4 = Lists.reverse(list3);
/*     */         
/* 169 */         for (StaticCloneData commandclone$staticclonedata : list4) {
/*     */           
/* 171 */           TileEntity tileentity2 = world.getTileEntity(commandclone$staticclonedata.pos);
/*     */           
/* 173 */           if (tileentity2 instanceof IInventory)
/*     */           {
/* 175 */             ((IInventory)tileentity2).clear();
/*     */           }
/*     */           
/* 178 */           world.setBlockState(commandclone$staticclonedata.pos, Blocks.barrier.getDefaultState(), 2);
/*     */         } 
/*     */         
/* 181 */         i = 0;
/*     */         
/* 183 */         for (StaticCloneData commandclone$staticclonedata1 : list3) {
/*     */           
/* 185 */           if (world.setBlockState(commandclone$staticclonedata1.pos, commandclone$staticclonedata1.blockState, 2))
/*     */           {
/* 187 */             i++;
/*     */           }
/*     */         } 
/*     */         
/* 191 */         for (StaticCloneData commandclone$staticclonedata2 : list1) {
/*     */           
/* 193 */           TileEntity tileentity3 = world.getTileEntity(commandclone$staticclonedata2.pos);
/*     */           
/* 195 */           if (commandclone$staticclonedata2.compound != null && tileentity3 != null) {
/*     */             
/* 197 */             commandclone$staticclonedata2.compound.setInteger("x", commandclone$staticclonedata2.pos.getX());
/* 198 */             commandclone$staticclonedata2.compound.setInteger("y", commandclone$staticclonedata2.pos.getY());
/* 199 */             commandclone$staticclonedata2.compound.setInteger("z", commandclone$staticclonedata2.pos.getZ());
/* 200 */             tileentity3.readFromNBT(commandclone$staticclonedata2.compound);
/* 201 */             tileentity3.markDirty();
/*     */           } 
/*     */           
/* 204 */           world.setBlockState(commandclone$staticclonedata2.pos, commandclone$staticclonedata2.blockState, 2);
/*     */         } 
/*     */         
/* 207 */         for (StaticCloneData commandclone$staticclonedata3 : list4)
/*     */         {
/* 209 */           world.notifyNeighborsRespectDebug(commandclone$staticclonedata3.pos, commandclone$staticclonedata3.blockState.getBlock());
/*     */         }
/*     */         
/* 212 */         List<NextTickListEntry> list5 = world.func_175712_a(structureboundingbox, false);
/*     */         
/* 214 */         if (list5 != null)
/*     */         {
/* 216 */           for (NextTickListEntry nextticklistentry : list5) {
/*     */             
/* 218 */             if (structureboundingbox.isVecInside((Vec3i)nextticklistentry.position)) {
/*     */               
/* 220 */               BlockPos blockpos8 = nextticklistentry.position.add((Vec3i)blockpos3);
/* 221 */               world.scheduleBlockUpdate(blockpos8, nextticklistentry.getBlock(), (int)(nextticklistentry.scheduledTime - world.getWorldInfo().getWorldTotalTime()), nextticklistentry.priority);
/*     */             } 
/*     */           } 
/*     */         }
/*     */         
/* 226 */         if (i <= 0)
/*     */         {
/* 228 */           throw new CommandException("commands.clone.failed", new Object[0]);
/*     */         }
/*     */ 
/*     */         
/* 232 */         sender.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, i);
/* 233 */         notifyOperators(sender, this, "commands.clone.success", new Object[] { Integer.valueOf(i) });
/*     */       
/*     */       }
/*     */       else
/*     */       {
/* 238 */         throw new CommandException("commands.clone.outOfWorld", new Object[0]);
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 243 */       throw new CommandException("commands.clone.outOfWorld", new Object[0]);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
/* 252 */     return (args.length > 0 && args.length <= 3) ? func_175771_a(args, 0, pos) : ((args.length > 3 && args.length <= 6) ? func_175771_a(args, 3, pos) : ((args.length > 6 && args.length <= 9) ? func_175771_a(args, 6, pos) : ((args.length == 10) ? getListOfStringsMatchingLastWord(args, new String[] { "replace", "masked", "filtered" }) : ((args.length == 11) ? getListOfStringsMatchingLastWord(args, new String[] { "normal", "force", "move" }) : ((args.length == 12 && "filtered".equals(args[9])) ? getListOfStringsMatchingLastWord(args, Block.blockRegistry.getKeys()) : null)))));
/*     */   }
/*     */ 
/*     */   
/*     */   static class StaticCloneData
/*     */   {
/*     */     public final BlockPos pos;
/*     */     public final IBlockState blockState;
/*     */     public final NBTTagCompound compound;
/*     */     
/*     */     public StaticCloneData(BlockPos posIn, IBlockState stateIn, NBTTagCompound compoundIn) {
/* 263 */       this.pos = posIn;
/* 264 */       this.blockState = stateIn;
/* 265 */       this.compound = compoundIn;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\command\CommandClone.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */