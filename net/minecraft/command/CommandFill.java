/*     */ package net.minecraft.command;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.inventory.IInventory;
/*     */ import net.minecraft.nbt.JsonToNBT;
/*     */ import net.minecraft.nbt.NBTException;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.tileentity.TileEntity;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class CommandFill
/*     */   extends CommandBase
/*     */ {
/*     */   public String getCommandName() {
/*  20 */     return "fill";
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRequiredPermissionLevel() {
/*  25 */     return 2;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getCommandUsage(ICommandSender sender) {
/*  30 */     return "commands.fill.usage";
/*     */   }
/*     */ 
/*     */   
/*     */   public void processCommand(ICommandSender sender, String[] args) throws CommandException {
/*  35 */     if (args.length < 7)
/*     */     {
/*  37 */       throw new WrongUsageException("commands.fill.usage", new Object[0]);
/*     */     }
/*     */ 
/*     */     
/*  41 */     sender.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, 0);
/*  42 */     BlockPos blockpos = parseBlockPos(sender, args, 0, false);
/*  43 */     BlockPos blockpos1 = parseBlockPos(sender, args, 3, false);
/*  44 */     Block block = CommandBase.getBlockByText(sender, args[6]);
/*  45 */     int i = 0;
/*     */     
/*  47 */     if (args.length >= 8)
/*     */     {
/*  49 */       i = parseInt(args[7], 0, 15);
/*     */     }
/*     */     
/*  52 */     BlockPos blockpos2 = new BlockPos(Math.min(blockpos.getX(), blockpos1.getX()), Math.min(blockpos.getY(), blockpos1.getY()), Math.min(blockpos.getZ(), blockpos1.getZ()));
/*  53 */     BlockPos blockpos3 = new BlockPos(Math.max(blockpos.getX(), blockpos1.getX()), Math.max(blockpos.getY(), blockpos1.getY()), Math.max(blockpos.getZ(), blockpos1.getZ()));
/*  54 */     int j = (blockpos3.getX() - blockpos2.getX() + 1) * (blockpos3.getY() - blockpos2.getY() + 1) * (blockpos3.getZ() - blockpos2.getZ() + 1);
/*     */     
/*  56 */     if (j > 32768)
/*     */     {
/*  58 */       throw new CommandException("commands.fill.tooManyBlocks", new Object[] { Integer.valueOf(j), Integer.valueOf(32768) });
/*     */     }
/*  60 */     if (blockpos2.getY() >= 0 && blockpos3.getY() < 256) {
/*     */       
/*  62 */       World world = sender.getEntityWorld();
/*     */       
/*  64 */       for (int k = blockpos2.getZ(); k < blockpos3.getZ() + 16; k += 16) {
/*     */         
/*  66 */         for (int l = blockpos2.getX(); l < blockpos3.getX() + 16; l += 16) {
/*     */           
/*  68 */           if (!world.isBlockLoaded(new BlockPos(l, blockpos3.getY() - blockpos2.getY(), k)))
/*     */           {
/*  70 */             throw new CommandException("commands.fill.outOfWorld", new Object[0]);
/*     */           }
/*     */         } 
/*     */       } 
/*     */       
/*  75 */       NBTTagCompound nbttagcompound = new NBTTagCompound();
/*  76 */       boolean flag = false;
/*     */       
/*  78 */       if (args.length >= 10 && block.hasTileEntity()) {
/*     */         
/*  80 */         String s = getChatComponentFromNthArg(sender, args, 9).getUnformattedText();
/*     */ 
/*     */         
/*     */         try {
/*  84 */           nbttagcompound = JsonToNBT.getTagFromJson(s);
/*  85 */           flag = true;
/*     */         }
/*  87 */         catch (NBTException nbtexception) {
/*     */           
/*  89 */           throw new CommandException("commands.fill.tagError", new Object[] { nbtexception.getMessage() });
/*     */         } 
/*     */       } 
/*     */       
/*  93 */       List<BlockPos> list = Lists.newArrayList();
/*  94 */       j = 0;
/*     */       
/*  96 */       for (int i1 = blockpos2.getZ(); i1 <= blockpos3.getZ(); i1++) {
/*     */         
/*  98 */         for (int j1 = blockpos2.getY(); j1 <= blockpos3.getY(); j1++) {
/*     */           
/* 100 */           for (int k1 = blockpos2.getX(); k1 <= blockpos3.getX(); k1++) {
/*     */             
/* 102 */             BlockPos blockpos4 = new BlockPos(k1, j1, i1);
/*     */             
/* 104 */             if (args.length >= 9)
/*     */             {
/* 106 */               if (!args[8].equals("outline") && !args[8].equals("hollow")) {
/*     */                 
/* 108 */                 if (args[8].equals("destroy"))
/*     */                 {
/* 110 */                   world.destroyBlock(blockpos4, true);
/*     */                 }
/* 112 */                 else if (args[8].equals("keep"))
/*     */                 {
/* 114 */                   if (!world.isAirBlock(blockpos4))
/*     */                   {
/*     */                     continue;
/*     */                   }
/*     */                 }
/* 119 */                 else if (args[8].equals("replace") && !block.hasTileEntity())
/*     */                 {
/* 121 */                   if (args.length > 9) {
/*     */                     
/* 123 */                     Block block1 = CommandBase.getBlockByText(sender, args[9]);
/*     */                     
/* 125 */                     if (world.getBlockState(blockpos4).getBlock() != block1) {
/*     */                       continue;
/*     */                     }
/*     */                   } 
/*     */ 
/*     */                   
/* 131 */                   if (args.length > 10)
/*     */                   {
/* 133 */                     int l1 = CommandBase.parseInt(args[10]);
/* 134 */                     IBlockState iblockstate = world.getBlockState(blockpos4);
/*     */                     
/* 136 */                     if (iblockstate.getBlock().getMetaFromState(iblockstate) != l1) {
/*     */                       continue;
/*     */                     }
/*     */                   }
/*     */                 
/*     */                 }
/*     */               
/* 143 */               } else if (k1 != blockpos2.getX() && k1 != blockpos3.getX() && j1 != blockpos2.getY() && j1 != blockpos3.getY() && i1 != blockpos2.getZ() && i1 != blockpos3.getZ()) {
/*     */                 
/* 145 */                 if (args[8].equals("hollow")) {
/*     */                   
/* 147 */                   world.setBlockState(blockpos4, Blocks.air.getDefaultState(), 2);
/* 148 */                   list.add(blockpos4);
/*     */                 } 
/*     */                 
/*     */                 continue;
/*     */               } 
/*     */             }
/*     */             
/* 155 */             TileEntity tileentity1 = world.getTileEntity(blockpos4);
/*     */             
/* 157 */             if (tileentity1 != null) {
/*     */               
/* 159 */               if (tileentity1 instanceof IInventory)
/*     */               {
/* 161 */                 ((IInventory)tileentity1).clear();
/*     */               }
/*     */               
/* 164 */               world.setBlockState(blockpos4, Blocks.barrier.getDefaultState(), (block == Blocks.barrier) ? 2 : 4);
/*     */             } 
/*     */             
/* 167 */             IBlockState iblockstate1 = block.getStateFromMeta(i);
/*     */             
/* 169 */             if (world.setBlockState(blockpos4, iblockstate1, 2)) {
/*     */               
/* 171 */               list.add(blockpos4);
/* 172 */               j++;
/*     */               
/* 174 */               if (flag) {
/*     */                 
/* 176 */                 TileEntity tileentity = world.getTileEntity(blockpos4);
/*     */                 
/* 178 */                 if (tileentity != null) {
/*     */                   
/* 180 */                   nbttagcompound.setInteger("x", blockpos4.getX());
/* 181 */                   nbttagcompound.setInteger("y", blockpos4.getY());
/* 182 */                   nbttagcompound.setInteger("z", blockpos4.getZ());
/* 183 */                   tileentity.readFromNBT(nbttagcompound);
/*     */                 } 
/*     */               } 
/*     */             } 
/*     */             continue;
/*     */           } 
/*     */         } 
/*     */       } 
/* 191 */       for (BlockPos blockpos5 : list) {
/*     */         
/* 193 */         Block block2 = world.getBlockState(blockpos5).getBlock();
/* 194 */         world.notifyNeighborsRespectDebug(blockpos5, block2);
/*     */       } 
/*     */       
/* 197 */       if (j <= 0)
/*     */       {
/* 199 */         throw new CommandException("commands.fill.failed", new Object[0]);
/*     */       }
/*     */ 
/*     */       
/* 203 */       sender.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, j);
/* 204 */       notifyOperators(sender, this, "commands.fill.success", new Object[] { Integer.valueOf(j) });
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 209 */       throw new CommandException("commands.fill.outOfWorld", new Object[0]);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
/* 216 */     return (args.length > 0 && args.length <= 3) ? func_175771_a(args, 0, pos) : ((args.length > 3 && args.length <= 6) ? func_175771_a(args, 3, pos) : ((args.length == 7) ? getListOfStringsMatchingLastWord(args, Block.blockRegistry.getKeys()) : ((args.length == 9) ? getListOfStringsMatchingLastWord(args, new String[] { "replace", "destroy", "keep", "hollow", "outline" }) : ((args.length == 10 && "replace".equals(args[8])) ? getListOfStringsMatchingLastWord(args, Block.blockRegistry.getKeys()) : null))));
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\command\CommandFill.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */