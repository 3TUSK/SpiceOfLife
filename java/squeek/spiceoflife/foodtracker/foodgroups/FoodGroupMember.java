package squeek.spiceoflife.foodtracker.foodgroups;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import squeek.spiceoflife.compat.IByteIO;
import squeek.spiceoflife.interfaces.IPackable;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

public class FoodGroupMember implements IPackable
{
	String oredictName = null;
	@Nonnull ItemStack itemStack = ItemStack.EMPTY;

	public List<ItemStack> getBaseItemList()
	{
		return itemStack != ItemStack.EMPTY ? Collections.singletonList(itemStack) : OreDictionary.getOres(oredictName);
	}

	/*
	 * Constructors
	 */
	public FoodGroupMember()
	{
	}

	public FoodGroupMember(String oredictName)
	{
		this.oredictName = oredictName;
	}

	public FoodGroupMember(@Nonnull ItemStack itemStack)
	{
		this.itemStack = itemStack;
	}

	/*
	 * Packet handling
	 */
	@Override
	public void pack(IByteIO data)
	{
		data.writeItemStack(itemStack);
		data.writeUTF(oredictName != null ? oredictName : "");
	}

	@Override
	public void unpack(IByteIO data)
	{
		itemStack = data.readItemStack();
		oredictName = data.readUTF();
		oredictName = !oredictName.isEmpty() ? oredictName : null;
	}
}
