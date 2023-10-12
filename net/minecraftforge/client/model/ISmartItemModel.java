package net.minecraftforge.client.model;

import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.ItemStack;

public interface ISmartItemModel extends IBakedModel {
  IBakedModel handleItemState(ItemStack paramItemStack);
}


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraftforge\client\model\ISmartItemModel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */