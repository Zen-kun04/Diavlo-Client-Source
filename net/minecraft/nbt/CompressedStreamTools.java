/*     */ package net.minecraft.nbt;
/*     */ import java.io.DataInput;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutput;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import net.minecraft.crash.CrashReport;
/*     */ import net.minecraft.crash.CrashReportCategory;
/*     */ 
/*     */ public class CompressedStreamTools {
/*     */   public static NBTTagCompound readCompressed(InputStream is) throws IOException {
/*     */     NBTTagCompound nbttagcompound;
/*  15 */     DataInputStream datainputstream = new DataInputStream(new BufferedInputStream(new GZIPInputStream(is)));
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/*  20 */       nbttagcompound = read(datainputstream, NBTSizeTracker.INFINITE);
/*     */     }
/*     */     finally {
/*     */       
/*  24 */       datainputstream.close();
/*     */     } 
/*     */     
/*  27 */     return nbttagcompound;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void writeCompressed(NBTTagCompound p_74799_0_, OutputStream outputStream) throws IOException {
/*  32 */     DataOutputStream dataoutputstream = new DataOutputStream(new BufferedOutputStream(new GZIPOutputStream(outputStream)));
/*     */ 
/*     */     
/*     */     try {
/*  36 */       write(p_74799_0_, dataoutputstream);
/*     */     }
/*     */     finally {
/*     */       
/*  40 */       dataoutputstream.close();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void safeWrite(NBTTagCompound p_74793_0_, File p_74793_1_) throws IOException {
/*  46 */     File file1 = new File(p_74793_1_.getAbsolutePath() + "_tmp");
/*     */     
/*  48 */     if (file1.exists())
/*     */     {
/*  50 */       file1.delete();
/*     */     }
/*     */     
/*  53 */     write(p_74793_0_, file1);
/*     */     
/*  55 */     if (p_74793_1_.exists())
/*     */     {
/*  57 */       p_74793_1_.delete();
/*     */     }
/*     */     
/*  60 */     if (p_74793_1_.exists())
/*     */     {
/*  62 */       throw new IOException("Failed to delete " + p_74793_1_);
/*     */     }
/*     */ 
/*     */     
/*  66 */     file1.renameTo(p_74793_1_);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void write(NBTTagCompound p_74795_0_, File p_74795_1_) throws IOException {
/*  72 */     DataOutputStream dataoutputstream = new DataOutputStream(new FileOutputStream(p_74795_1_));
/*     */ 
/*     */     
/*     */     try {
/*  76 */       write(p_74795_0_, dataoutputstream);
/*     */     }
/*     */     finally {
/*     */       
/*  80 */       dataoutputstream.close();
/*     */     } 
/*     */   }
/*     */   
/*     */   public static NBTTagCompound read(File p_74797_0_) throws IOException {
/*     */     NBTTagCompound nbttagcompound;
/*  86 */     if (!p_74797_0_.exists())
/*     */     {
/*  88 */       return null;
/*     */     }
/*     */ 
/*     */     
/*  92 */     DataInputStream datainputstream = new DataInputStream(new FileInputStream(p_74797_0_));
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/*  97 */       nbttagcompound = read(datainputstream, NBTSizeTracker.INFINITE);
/*     */     }
/*     */     finally {
/*     */       
/* 101 */       datainputstream.close();
/*     */     } 
/*     */     
/* 104 */     return nbttagcompound;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static NBTTagCompound read(DataInputStream inputStream) throws IOException {
/* 110 */     return read(inputStream, NBTSizeTracker.INFINITE);
/*     */   }
/*     */ 
/*     */   
/*     */   public static NBTTagCompound read(DataInput p_152456_0_, NBTSizeTracker p_152456_1_) throws IOException {
/* 115 */     NBTBase nbtbase = func_152455_a(p_152456_0_, 0, p_152456_1_);
/*     */     
/* 117 */     if (nbtbase instanceof NBTTagCompound)
/*     */     {
/* 119 */       return (NBTTagCompound)nbtbase;
/*     */     }
/*     */ 
/*     */     
/* 123 */     throw new IOException("Root tag must be a named compound tag");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void write(NBTTagCompound p_74800_0_, DataOutput p_74800_1_) throws IOException {
/* 129 */     writeTag(p_74800_0_, p_74800_1_);
/*     */   }
/*     */ 
/*     */   
/*     */   private static void writeTag(NBTBase p_150663_0_, DataOutput p_150663_1_) throws IOException {
/* 134 */     p_150663_1_.writeByte(p_150663_0_.getId());
/*     */     
/* 136 */     if (p_150663_0_.getId() != 0) {
/*     */       
/* 138 */       p_150663_1_.writeUTF("");
/* 139 */       p_150663_0_.write(p_150663_1_);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static NBTBase func_152455_a(DataInput p_152455_0_, int p_152455_1_, NBTSizeTracker p_152455_2_) throws IOException {
/* 145 */     byte b0 = p_152455_0_.readByte();
/*     */     
/* 147 */     if (b0 == 0)
/*     */     {
/* 149 */       return new NBTTagEnd();
/*     */     }
/*     */ 
/*     */     
/* 153 */     p_152455_0_.readUTF();
/* 154 */     NBTBase nbtbase = NBTBase.createNewByType(b0);
/*     */ 
/*     */     
/*     */     try {
/* 158 */       nbtbase.read(p_152455_0_, p_152455_1_, p_152455_2_);
/* 159 */       return nbtbase;
/*     */     }
/* 161 */     catch (IOException ioexception) {
/*     */       
/* 163 */       CrashReport crashreport = CrashReport.makeCrashReport(ioexception, "Loading NBT data");
/* 164 */       CrashReportCategory crashreportcategory = crashreport.makeCategory("NBT Tag");
/* 165 */       crashreportcategory.addCrashSection("Tag name", "[UNNAMED TAG]");
/* 166 */       crashreportcategory.addCrashSection("Tag type", Byte.valueOf(b0));
/* 167 */       throw new ReportedException(crashreport);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\nbt\CompressedStreamTools.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */