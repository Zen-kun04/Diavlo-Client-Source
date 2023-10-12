/*     */ package com.viaversion.viaversion.api.type;
/*     */ 
/*     */ import com.viaversion.viaversion.api.minecraft.BlockChangeRecord;
/*     */ import com.viaversion.viaversion.api.minecraft.EulerAngle;
/*     */ import com.viaversion.viaversion.api.minecraft.GlobalPosition;
/*     */ import com.viaversion.viaversion.api.minecraft.PlayerMessageSignature;
/*     */ import com.viaversion.viaversion.api.minecraft.Position;
/*     */ import com.viaversion.viaversion.api.minecraft.ProfileKey;
/*     */ import com.viaversion.viaversion.api.minecraft.Quaternion;
/*     */ import com.viaversion.viaversion.api.minecraft.Vector;
/*     */ import com.viaversion.viaversion.api.minecraft.Vector3f;
/*     */ import com.viaversion.viaversion.api.minecraft.VillagerData;
/*     */ import com.viaversion.viaversion.api.minecraft.item.Item;
/*     */ import com.viaversion.viaversion.api.minecraft.metadata.ChunkPosition;
/*     */ import com.viaversion.viaversion.api.type.types.ArrayType;
/*     */ import com.viaversion.viaversion.api.type.types.BooleanType;
/*     */ import com.viaversion.viaversion.api.type.types.ByteArrayType;
/*     */ import com.viaversion.viaversion.api.type.types.ByteType;
/*     */ import com.viaversion.viaversion.api.type.types.ComponentType;
/*     */ import com.viaversion.viaversion.api.type.types.DoubleType;
/*     */ import com.viaversion.viaversion.api.type.types.FloatType;
/*     */ import com.viaversion.viaversion.api.type.types.IntType;
/*     */ import com.viaversion.viaversion.api.type.types.LongArrayType;
/*     */ import com.viaversion.viaversion.api.type.types.LongType;
/*     */ import com.viaversion.viaversion.api.type.types.RemainingBytesType;
/*     */ import com.viaversion.viaversion.api.type.types.ShortByteArrayType;
/*     */ import com.viaversion.viaversion.api.type.types.ShortType;
/*     */ import com.viaversion.viaversion.api.type.types.StringType;
/*     */ import com.viaversion.viaversion.api.type.types.UUIDIntArrayType;
/*     */ import com.viaversion.viaversion.api.type.types.UUIDType;
/*     */ import com.viaversion.viaversion.api.type.types.UnsignedByteType;
/*     */ import com.viaversion.viaversion.api.type.types.UnsignedShortType;
/*     */ import com.viaversion.viaversion.api.type.types.VarIntArrayType;
/*     */ import com.viaversion.viaversion.api.type.types.VarIntType;
/*     */ import com.viaversion.viaversion.api.type.types.VarLongType;
/*     */ import com.viaversion.viaversion.api.type.types.VoidType;
/*     */ import com.viaversion.viaversion.api.type.types.minecraft.BlockChangeRecordType;
/*     */ import com.viaversion.viaversion.api.type.types.minecraft.ChunkPositionType;
/*     */ import com.viaversion.viaversion.api.type.types.minecraft.EulerAngleType;
/*     */ import com.viaversion.viaversion.api.type.types.minecraft.FlatItemArrayType;
/*     */ import com.viaversion.viaversion.api.type.types.minecraft.FlatItemType;
/*     */ import com.viaversion.viaversion.api.type.types.minecraft.FlatVarIntItemArrayType;
/*     */ import com.viaversion.viaversion.api.type.types.minecraft.FlatVarIntItemType;
/*     */ import com.viaversion.viaversion.api.type.types.minecraft.GlobalPositionType;
/*     */ import com.viaversion.viaversion.api.type.types.minecraft.Item1_20_2Type;
/*     */ import com.viaversion.viaversion.api.type.types.minecraft.ItemArrayType;
/*     */ import com.viaversion.viaversion.api.type.types.minecraft.ItemType;
/*     */ import com.viaversion.viaversion.api.type.types.minecraft.NBTType;
/*     */ import com.viaversion.viaversion.api.type.types.minecraft.NamelessNBTType;
/*     */ import com.viaversion.viaversion.api.type.types.minecraft.OptionalVarIntType;
/*     */ import com.viaversion.viaversion.api.type.types.minecraft.PlayerMessageSignatureType;
/*     */ import com.viaversion.viaversion.api.type.types.minecraft.Position1_14Type;
/*     */ import com.viaversion.viaversion.api.type.types.minecraft.PositionType;
/*     */ import com.viaversion.viaversion.api.type.types.minecraft.ProfileKeyType;
/*     */ import com.viaversion.viaversion.api.type.types.minecraft.QuaternionType;
/*     */ import com.viaversion.viaversion.api.type.types.minecraft.VarLongBlockChangeRecordType;
/*     */ import com.viaversion.viaversion.api.type.types.minecraft.Vector3fType;
/*     */ import com.viaversion.viaversion.api.type.types.minecraft.VectorType;
/*     */ import com.viaversion.viaversion.api.type.types.minecraft.VillagerDataType;
/*     */ import com.viaversion.viaversion.libs.gson.JsonElement;
/*     */ import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
/*     */ import java.util.UUID;
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
/*     */ public abstract class Type<T>
/*     */   implements ByteBufReader<T>, ByteBufWriter<T>
/*     */ {
/*  93 */   public static final ByteType BYTE = new ByteType();
/*  94 */   public static final UnsignedByteType UNSIGNED_BYTE = new UnsignedByteType();
/*  95 */   public static final Type<byte[]> BYTE_ARRAY_PRIMITIVE = (Type<byte[]>)new ByteArrayType();
/*  96 */   public static final Type<byte[]> OPTIONAL_BYTE_ARRAY_PRIMITIVE = (Type<byte[]>)new ByteArrayType.OptionalByteArrayType();
/*  97 */   public static final Type<byte[]> SHORT_BYTE_ARRAY = (Type<byte[]>)new ShortByteArrayType();
/*  98 */   public static final Type<byte[]> REMAINING_BYTES = (Type<byte[]>)new RemainingBytesType();
/*     */   
/* 100 */   public static final ShortType SHORT = new ShortType();
/* 101 */   public static final UnsignedShortType UNSIGNED_SHORT = new UnsignedShortType();
/*     */   
/* 103 */   public static final IntType INT = new IntType();
/* 104 */   public static final FloatType FLOAT = new FloatType();
/* 105 */   public static final FloatType.OptionalFloatType OPTIONAL_FLOAT = new FloatType.OptionalFloatType();
/* 106 */   public static final DoubleType DOUBLE = new DoubleType();
/*     */   
/* 108 */   public static final LongType LONG = new LongType();
/* 109 */   public static final Type<long[]> LONG_ARRAY_PRIMITIVE = (Type<long[]>)new LongArrayType();
/*     */   
/* 111 */   public static final BooleanType BOOLEAN = new BooleanType();
/*     */ 
/*     */   
/* 114 */   public static final Type<JsonElement> COMPONENT = (Type<JsonElement>)new ComponentType();
/* 115 */   public static final Type<JsonElement> OPTIONAL_COMPONENT = (Type<JsonElement>)new ComponentType.OptionalComponentType();
/*     */   
/* 117 */   public static final Type<String> STRING = (Type<String>)new StringType();
/* 118 */   public static final Type<String> OPTIONAL_STRING = (Type<String>)new StringType.OptionalStringType();
/* 119 */   public static final Type<String[]> STRING_ARRAY = (Type<String[]>)new ArrayType(STRING);
/*     */   
/* 121 */   public static final Type<UUID> UUID = (Type<UUID>)new UUIDType();
/* 122 */   public static final Type<UUID> OPTIONAL_UUID = (Type<UUID>)new UUIDType.OptionalUUIDType();
/* 123 */   public static final Type<UUID[]> UUID_ARRAY = (Type<UUID[]>)new ArrayType(UUID);
/*     */   @Deprecated
/* 125 */   public static final Type<UUID> UUID_INT_ARRAY = (Type<UUID>)new UUIDIntArrayType();
/*     */   
/* 127 */   public static final VarIntType VAR_INT = new VarIntType();
/* 128 */   public static final OptionalVarIntType OPTIONAL_VAR_INT = new OptionalVarIntType();
/* 129 */   public static final Type<int[]> VAR_INT_ARRAY_PRIMITIVE = (Type<int[]>)new VarIntArrayType();
/* 130 */   public static final VarLongType VAR_LONG = new VarLongType();
/*     */ 
/*     */   
/*     */   @Deprecated
/* 134 */   public static final Type<Byte[]> BYTE_ARRAY = (Type<Byte[]>)new ArrayType((Type)BYTE);
/*     */   @Deprecated
/* 136 */   public static final Type<Short[]> UNSIGNED_BYTE_ARRAY = (Type<Short[]>)new ArrayType((Type)UNSIGNED_BYTE);
/*     */   @Deprecated
/* 138 */   public static final Type<Boolean[]> BOOLEAN_ARRAY = (Type<Boolean[]>)new ArrayType((Type)BOOLEAN);
/*     */   @Deprecated
/* 140 */   public static final Type<Integer[]> INT_ARRAY = (Type<Integer[]>)new ArrayType((Type)INT);
/*     */   @Deprecated
/* 142 */   public static final Type<Short[]> SHORT_ARRAY = (Type<Short[]>)new ArrayType((Type)SHORT);
/*     */   @Deprecated
/* 144 */   public static final Type<Integer[]> UNSIGNED_SHORT_ARRAY = (Type<Integer[]>)new ArrayType((Type)UNSIGNED_SHORT);
/*     */   @Deprecated
/* 146 */   public static final Type<Double[]> DOUBLE_ARRAY = (Type<Double[]>)new ArrayType((Type)DOUBLE);
/*     */   @Deprecated
/* 148 */   public static final Type<Long[]> LONG_ARRAY = (Type<Long[]>)new ArrayType((Type)LONG);
/*     */   @Deprecated
/* 150 */   public static final Type<Float[]> FLOAT_ARRAY = (Type<Float[]>)new ArrayType((Type)FLOAT);
/*     */   @Deprecated
/* 152 */   public static final Type<Integer[]> VAR_INT_ARRAY = (Type<Integer[]>)new ArrayType((Type)VAR_INT);
/*     */   @Deprecated
/* 154 */   public static final Type<Long[]> VAR_LONG_ARRAY = (Type<Long[]>)new ArrayType((Type)VAR_LONG);
/*     */ 
/*     */   
/* 157 */   public static final VoidType NOTHING = new VoidType();
/*     */ 
/*     */   
/* 160 */   public static final Type<Position> POSITION = (Type<Position>)new PositionType();
/* 161 */   public static final Type<Position> OPTIONAL_POSITION = (Type<Position>)new PositionType.OptionalPositionType();
/* 162 */   public static final Type<Position> POSITION1_14 = (Type<Position>)new Position1_14Type();
/* 163 */   public static final Type<Position> OPTIONAL_POSITION_1_14 = (Type<Position>)new Position1_14Type.OptionalPosition1_14Type();
/* 164 */   public static final Type<EulerAngle> ROTATION = (Type<EulerAngle>)new EulerAngleType();
/* 165 */   public static final Type<Vector> VECTOR = (Type<Vector>)new VectorType();
/* 166 */   public static final Type<Vector3f> VECTOR3F = (Type<Vector3f>)new Vector3fType();
/* 167 */   public static final Type<Quaternion> QUATERNION = (Type<Quaternion>)new QuaternionType();
/* 168 */   public static final Type<CompoundTag> NBT = (Type<CompoundTag>)new NBTType();
/* 169 */   public static final Type<CompoundTag> NAMELESS_NBT = (Type<CompoundTag>)new NamelessNBTType();
/* 170 */   public static final Type<CompoundTag[]> NBT_ARRAY = (Type<CompoundTag[]>)new ArrayType(NBT);
/* 171 */   public static final Type<GlobalPosition> GLOBAL_POSITION = (Type<GlobalPosition>)new GlobalPositionType();
/* 172 */   public static final Type<GlobalPosition> OPTIONAL_GLOBAL_POSITION = (Type<GlobalPosition>)new GlobalPositionType.OptionalGlobalPositionType();
/* 173 */   public static final Type<ChunkPosition> CHUNK_POSITION = (Type<ChunkPosition>)new ChunkPositionType();
/*     */   
/* 175 */   public static final Type<BlockChangeRecord> BLOCK_CHANGE_RECORD = (Type<BlockChangeRecord>)new BlockChangeRecordType();
/* 176 */   public static final Type<BlockChangeRecord[]> BLOCK_CHANGE_RECORD_ARRAY = (Type<BlockChangeRecord[]>)new ArrayType(BLOCK_CHANGE_RECORD);
/*     */   
/* 178 */   public static final Type<BlockChangeRecord> VAR_LONG_BLOCK_CHANGE_RECORD = (Type<BlockChangeRecord>)new VarLongBlockChangeRecordType();
/* 179 */   public static final Type<BlockChangeRecord[]> VAR_LONG_BLOCK_CHANGE_RECORD_ARRAY = (Type<BlockChangeRecord[]>)new ArrayType(VAR_LONG_BLOCK_CHANGE_RECORD);
/*     */   
/* 181 */   public static final Type<VillagerData> VILLAGER_DATA = (Type<VillagerData>)new VillagerDataType();
/*     */   
/* 183 */   public static final Type<Item> ITEM = (Type<Item>)new ItemType();
/* 184 */   public static final Type<Item[]> ITEM_ARRAY = (Type<Item[]>)new ItemArrayType();
/*     */   
/* 186 */   public static final Type<ProfileKey> PROFILE_KEY = (Type<ProfileKey>)new ProfileKeyType();
/* 187 */   public static final Type<ProfileKey> OPTIONAL_PROFILE_KEY = (Type<ProfileKey>)new ProfileKeyType.OptionalProfileKeyType();
/*     */   
/* 189 */   public static final Type<PlayerMessageSignature> PLAYER_MESSAGE_SIGNATURE = (Type<PlayerMessageSignature>)new PlayerMessageSignatureType();
/* 190 */   public static final Type<PlayerMessageSignature> OPTIONAL_PLAYER_MESSAGE_SIGNATURE = (Type<PlayerMessageSignature>)new PlayerMessageSignatureType.OptionalPlayerMessageSignatureType();
/* 191 */   public static final Type<PlayerMessageSignature[]> PLAYER_MESSAGE_SIGNATURE_ARRAY = (Type<PlayerMessageSignature[]>)new ArrayType(PLAYER_MESSAGE_SIGNATURE);
/*     */ 
/*     */   
/* 194 */   public static final Type<Item> FLAT_ITEM = (Type<Item>)new FlatItemType();
/* 195 */   public static final Type<Item> FLAT_VAR_INT_ITEM = (Type<Item>)new FlatVarIntItemType();
/* 196 */   public static final Type<Item[]> FLAT_ITEM_ARRAY = (Type<Item[]>)new FlatItemArrayType();
/* 197 */   public static final Type<Item[]> FLAT_VAR_INT_ITEM_ARRAY = (Type<Item[]>)new FlatVarIntItemArrayType();
/* 198 */   public static final Type<Item[]> FLAT_ITEM_ARRAY_VAR_INT = (Type<Item[]>)new ArrayType(FLAT_ITEM);
/* 199 */   public static final Type<Item[]> FLAT_VAR_INT_ITEM_ARRAY_VAR_INT = (Type<Item[]>)new ArrayType(FLAT_VAR_INT_ITEM);
/*     */   
/* 201 */   public static final Type<Item> ITEM1_20_2 = (Type<Item>)new Item1_20_2Type();
/* 202 */   public static final Type<Item[]> ITEM1_20_2_VAR_INT_ARRAY = (Type<Item[]>)new ArrayType(ITEM1_20_2);
/*     */   
/*     */   private final Class<? super T> outputClass;
/*     */   
/*     */   private final String typeName;
/*     */   
/*     */   protected Type(Class<? super T> outputClass) {
/* 209 */     this(outputClass.getSimpleName(), outputClass);
/*     */   }
/*     */   
/*     */   protected Type(String typeName, Class<? super T> outputClass) {
/* 213 */     this.outputClass = outputClass;
/* 214 */     this.typeName = typeName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<? super T> getOutputClass() {
/* 223 */     return this.outputClass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTypeName() {
/* 232 */     return this.typeName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<? extends Type> getBaseClass() {
/* 242 */     return (Class)getClass();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 247 */     return this.typeName;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\api\type\Type.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */