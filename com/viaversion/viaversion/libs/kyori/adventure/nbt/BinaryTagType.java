/*     */ package com.viaversion.viaversion.libs.kyori.adventure.nbt;
/*     */ 
/*     */ import java.io.DataInput;
/*     */ import java.io.DataOutput;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.function.Predicate;
/*     */ import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval;
/*     */ import org.jetbrains.annotations.NotNull;
/*     */ import org.jetbrains.annotations.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class BinaryTagType<T extends BinaryTag>
/*     */   implements Predicate<BinaryTagType<? extends BinaryTag>>
/*     */ {
/*  43 */   private static final List<BinaryTagType<? extends BinaryTag>> TYPES = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <T extends BinaryTag> void writeUntyped(BinaryTagType<? extends BinaryTag> type, T tag, DataOutput output) throws IOException {
/*  77 */     type.write((BinaryTag)tag, output);
/*     */   }
/*     */   @NotNull
/*     */   static BinaryTagType<? extends BinaryTag> binaryTagType(byte id) {
/*  81 */     for (int i = 0; i < TYPES.size(); i++) {
/*  82 */       BinaryTagType<? extends BinaryTag> type = TYPES.get(i);
/*  83 */       if (type.id() == id) {
/*  84 */         return type;
/*     */       }
/*     */     } 
/*  87 */     throw new IllegalArgumentException(String.valueOf(id));
/*     */   }
/*     */   @Deprecated
/*     */   @ScheduledForRemoval(inVersion = "5.0.0")
/*     */   @NotNull
/*     */   static BinaryTagType<? extends BinaryTag> of(byte id) {
/*  93 */     return binaryTagType(id);
/*     */   }
/*     */   @NotNull
/*     */   static <T extends BinaryTag> BinaryTagType<T> register(Class<T> type, byte id, Reader<T> reader, @Nullable Writer<T> writer) {
/*  97 */     return register(new Impl<>(type, id, reader, writer));
/*     */   }
/*     */   @NotNull
/*     */   static <T extends NumberBinaryTag> BinaryTagType<T> registerNumeric(Class<T> type, byte id, Reader<T> reader, Writer<T> writer) {
/* 101 */     return register((BinaryTagType)new Impl.Numeric<>((Class)type, id, (Reader)reader, (Writer)writer));
/*     */   }
/*     */   
/*     */   private static <T extends BinaryTag, Y extends BinaryTagType<T>> Y register(Y type) {
/* 105 */     TYPES.add((BinaryTagType<? extends BinaryTag>)type);
/* 106 */     return type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean test(BinaryTagType<? extends BinaryTag> that) {
/* 129 */     return (this == that || (numeric() && that.numeric()));
/*     */   }
/*     */   public abstract byte id();
/*     */   abstract boolean numeric();
/*     */   @NotNull
/*     */   public abstract T read(@NotNull DataInput paramDataInput) throws IOException;
/*     */   public abstract void write(@NotNull T paramT, @NotNull DataOutput paramDataOutput) throws IOException;
/*     */   static class Impl<T extends BinaryTag> extends BinaryTagType<T> { final Class<T> type; final byte id; private final BinaryTagType.Reader<T> reader; @Nullable
/*     */     private final BinaryTagType.Writer<T> writer;
/*     */     Impl(Class<T> type, byte id, BinaryTagType.Reader<T> reader, @Nullable BinaryTagType.Writer<T> writer) {
/* 139 */       this.type = type;
/* 140 */       this.id = id;
/* 141 */       this.reader = reader;
/* 142 */       this.writer = writer;
/*     */     }
/*     */     
/*     */     @NotNull
/*     */     public final T read(@NotNull DataInput input) throws IOException {
/* 147 */       return this.reader.read(input);
/*     */     }
/*     */ 
/*     */     
/*     */     public final void write(@NotNull T tag, @NotNull DataOutput output) throws IOException {
/* 152 */       if (this.writer != null) this.writer.write(tag, output);
/*     */     
/*     */     }
/*     */     
/*     */     public final byte id() {
/* 157 */       return this.id;
/*     */     }
/*     */ 
/*     */     
/*     */     boolean numeric() {
/* 162 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 167 */       return BinaryTagType.class.getSimpleName() + '[' + this.type.getSimpleName() + " " + this.id + "]";
/*     */     }
/*     */     
/*     */     static class Numeric<T extends BinaryTag> extends Impl<T> {
/*     */       Numeric(Class<T> type, byte id, BinaryTagType.Reader<T> reader, @Nullable BinaryTagType.Writer<T> writer) {
/* 172 */         super(type, id, reader, writer);
/*     */       }
/*     */ 
/*     */       
/*     */       boolean numeric() {
/* 177 */         return true;
/*     */       }
/*     */       
/*     */       public String toString()
/*     */       {
/* 182 */         return BinaryTagType.class.getSimpleName() + '[' + this.type.getSimpleName() + " " + this.id + " (numeric)]"; } } } static interface Writer<T extends BinaryTag> { void write(@NotNull T param1T, @NotNull DataOutput param1DataOutput) throws IOException; } static interface Reader<T extends BinaryTag> { @NotNull T read(@NotNull DataInput param1DataInput) throws IOException; } static class Numeric<T extends BinaryTag> extends Impl<T> { public String toString() { return BinaryTagType.class.getSimpleName() + '[' + this.type.getSimpleName() + " " + this.id + " (numeric)]"; }
/*     */ 
/*     */     
/*     */     Numeric(Class<T> type, byte id, BinaryTagType.Reader<T> reader, @Nullable BinaryTagType.Writer<T> writer) {
/*     */       super(type, id, reader, writer);
/*     */     }
/*     */     
/*     */     boolean numeric() {
/*     */       return true;
/*     */     } }
/*     */ 
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\libs\kyori\adventure\nbt\BinaryTagType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */