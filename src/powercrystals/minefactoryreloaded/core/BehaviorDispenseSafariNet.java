package powercrystals.minefactoryreloaded.core;

import powercrystals.minefactoryreloaded.entity.EntitySafariNet;
import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BehaviorDispenseSafariNet extends BehaviorDefaultDispenseItem
{
	public ItemStack dispenseStack(IBlockSource dispenser, ItemStack stack)
	{
		World world = dispenser.getWorld();
		IPosition dispenserPos = BlockDispenser.func_82525_a(dispenser);
		EnumFacing dispenserFacing = EnumFacing.getFront(dispenser.func_82620_h());
		EntitySafariNet proj = new EntitySafariNet(world, dispenserPos.getX(), dispenserPos.getY(), dispenserPos.getZ(), stack);
		proj.setThrowableHeading((double)dispenserFacing.getFrontOffsetX(), 0.10000000149011612D, (double)dispenserFacing.getFrontOffsetZ(), 1.1F, 6.0F);
		world.spawnEntityInWorld((Entity)proj);
		stack.splitStack(1);
		return stack;
	}

	/**
	 * Play the dispense sound from the specified block.
	 */
	protected void playDispenseSound(IBlockSource dispenser)
	{
		dispenser.getWorld().playAuxSFX(1002, dispenser.getXInt(), dispenser.getYInt(), dispenser.getZInt(), 0);
	}
}
