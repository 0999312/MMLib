package cn.mcmod_mmf.mmlib.block;

import java.util.Random;

import cn.mcmod_mmf.mmlib.item.info.FoodInfo;
import net.dries007.tfc.api.capability.food.FoodData;
import net.dries007.tfc.api.capability.food.FoodHandler;
import net.dries007.tfc.api.capability.food.IFoodStatsTFC;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional.Method;
import toughasnails.api.thirst.ThirstHelper;

public class BlockCakeBase extends BlockBase {
	public static final PropertyInteger BITES = PropertyInteger.create("bites", 0, 6);
	protected static final AxisAlignedBB[] CAKE_AABB = new AxisAlignedBB[] {
			new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.5D, 0.9375D),
			new AxisAlignedBB(0.1875D, 0.0D, 0.0625D, 0.9375D, 0.5D, 0.9375D),
			new AxisAlignedBB(0.3125D, 0.0D, 0.0625D, 0.9375D, 0.5D, 0.9375D),
			new AxisAlignedBB(0.4375D, 0.0D, 0.0625D, 0.9375D, 0.5D, 0.9375D),
			new AxisAlignedBB(0.5625D, 0.0D, 0.0625D, 0.9375D, 0.5D, 0.9375D),
			new AxisAlignedBB(0.6875D, 0.0D, 0.0625D, 0.9375D, 0.5D, 0.9375D),
			new AxisAlignedBB(0.8125D, 0.0D, 0.0625D, 0.9375D, 0.5D, 0.9375D) };

	public final FoodInfo info;
	public final ItemStack pieces;
	private final String modid;
	public BlockCakeBase(String modid,FoodInfo info, ItemStack piece) {
		super(Material.CAKE, false);
		this.setDefaultState(this.blockState.getBaseState().withProperty(BITES, Integer.valueOf(0)));
		this.setTickRandomly(true);
		this.setSoundType(SoundType.CLOTH);
		this.setHardness(0.5F);
		this.info = info;
		this.pieces = piece;
		this.modid = modid;
	}

	/**
	 * Checks if this block can be placed exactly at the given position.
	 */
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		return super.canPlaceBlockAt(worldIn, pos) ? this.canBlockStay(worldIn, pos) : false;
	}

	/**
	 * Called when a neighboring block was changed and marks that this state should
	 * perform any checks during a neighbor change. Cases may include when redstone
	 * power is updated, cactus blocks popping off due to a neighboring solid block,
	 * etc.
	 */
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
		if (!this.canBlockStay(worldIn, pos)) {
			worldIn.setBlockToAir(pos);
		}
	}

	private boolean canBlockStay(World worldIn, BlockPos pos) {
		return worldIn.getBlockState(pos.down()).getMaterial().isSolid();
	}

	public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos) {
		return (7 - blockState.getValue(BITES).intValue()) * 2;
	}

	public boolean hasComparatorInputOverride(IBlockState state) {
		return true;
	}

	public FoodInfo getInfo() {
		return this.info;
	}

	@Override
	public String getUnlocalizedName() {
		if (getInfo() != null) {
			StringBuilder name_builder = new StringBuilder("tile.");
			String subName = name_builder.append(modid).append('.').append(getInfo().getName()).toString();
			return subName;
		}
		return super.getUnlocalizedName();
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return CAKE_AABB[state.getValue(BITES).intValue()];
	}

	/**
	 * Called when the block is right clicked by a player.
	 */
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!worldIn.isRemote) {
			return this.eatGohan(worldIn, pos, state, playerIn);
		}
		ItemStack itemstack = playerIn.getHeldItem(hand);
		return this.eatGohan(worldIn, pos, state, playerIn) || itemstack.isEmpty();
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		if (state.getValue(BITES) > 0)
			return this.pieces.getItem();
		return super.getItemDropped(state, rand, fortune);
	}

	@Override
	public int damageDropped(IBlockState state) {
		if (state.getValue(BITES) > 0) {
			if (this.pieces.isEmpty())
				return 0;
			return this.pieces.getMetadata();
		}
		return super.damageDropped(state);
	}

	@Override
	public int quantityDropped(IBlockState state, int fortune, Random random) {
		if (state.getValue(BITES) > 0)
			return 7 - state.getValue(BITES);
		return super.quantityDropped(state, fortune, random);
	}

	private boolean eatGohan(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
		if (!player.canEat(false)) {
			return false;
		}

		int i = state.getValue(BITES).intValue();
		if (!worldIn.isRemote) {
			if (Loader.isModLoaded("tfc"))
				addTFCStats(player);
			else {
				player.getFoodStats().addStats(info.getAmount(), info.getCalories());
				if (Loader.isModLoaded("toughasnails"))
					addTANThirst(player, 2, 0.5F);
			}
			if (i < 6) {
				worldIn.setBlockState(pos, state.withProperty(BITES, Integer.valueOf(i + 1)), 3);
			} else {
				worldIn.setBlockToAir(pos);
			}
		}
		worldIn.playSound(player, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_BURP,
				SoundCategory.PLAYERS, 0.4F, worldIn.rand.nextFloat() * 0.1F + 0.8F);
		return true;
	}

	@Method(modid = "toughasnails")
	private void addTANThirst(EntityPlayer player, int i, float f) {
		ThirstHelper.getThirstData(player).addStats(i, f);
	}

	@Method(modid = "tfc")
	private void addTFCStats(EntityPlayer player) {
		if (player.getFoodStats() instanceof IFoodStatsTFC) {
			IFoodStatsTFC foodStats = (IFoodStatsTFC) player.getFoodStats();
			foodStats.addStats(new FoodHandler(null, new FoodData(info.getAmount(), info.getWater(), info.getCalories(),
					info.getNutrients(), info.getDecayModifier())));
		}

	}

	/**
	 * Convert the given metadata into a BlockState for this Block
	 */
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(BITES, (meta));
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY,
			float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return this.getDefaultState().withProperty(BITES, 0);
	}

	/**
	 * Convert the BlockState into the correct metadata value
	 */
	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(BITES).intValue();
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { BITES });
	}
}
