package powercrystals.minefactoryreloaded.world;

import gnu.trove.iterator.TLongObjectIterator;
import gnu.trove.map.hash.TLongObjectHashMap;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.util.ForgeDirection;

import powercrystals.minefactoryreloaded.MineFactoryReloadedCore;
import powercrystals.minefactoryreloaded.setup.MFRThings;

public class WorldGenMassiveTree extends WorldGenerator {

	/**
	 * Contains three sets of two values that provide complimentary indices for a given 'major' index - 1 and 2 for 0, 0
	 * and 2 for 1, and 0 and 1 for 2.
	 */
	private static final byte[] otherCoordPairs = new byte[] { (byte) 2, (byte) 0, (byte) 0, (byte) 1, (byte) 2, (byte) 1 };
	private static final float PI = (float) Math.PI;

	private Random rand = new org.bogdang.modifications.random.XSTR();

	/** Running variables */
	private World worldObj;
	private int[] basePos = new int[] { 0, 0, 0 };
	private int heightLimit = 0;
	private int minHeight = -1;
	private int height;
	private int leafBases;
	private int density;

	/** Setup variables */
	private float heightAttenuation = 0.45f;
	private float branchSlope = 0.45f;
	private float scaleWidth = 4.0f;
	private float branchDensity = 3.0f;
	private int trunkSize = 11;
	private boolean slopeTrunk = false;
	private boolean safeGrowth = false;

	/**
	 * Sets the limit of the random value used to initialize the height limit.
	 */
	private int heightLimitLimit = 250;

	/**
	 * Sets the distance limit for how far away the generator will populate leaves from the base leaf node.
	 */
	private int leafDistanceLimit = 4;

	private int leafNodesLength;
	/** Contains a list of a points at which to generate groups of leaves. */
	private int[][] leafNodes;

	public Block leaves = MFRThings.rubberLeavesBlock;
	public Block log = MFRThings.rubberWoodBlock;

	public WorldGenMassiveTree(boolean par1) {

		super(par1);
	}

	public WorldGenMassiveTree() {

		super();
	}

	private void setup() {

		leafBases = MathHelper.ceiling_float_int(heightLimit * heightAttenuation);
		density = Math.max(1, (int) (1.382D + Math.pow(branchDensity * heightLimit / 13.0D, 2.0D)));
		chunkMap = new TLongObjectHashMap<Chunk>((int) (scaleWidth * heightLimit));
	}

	private float layerSize(int par1) {

		if (par1 < leafBases)
			return -1.618F;
		else {
			float var2 = heightLimit * .5F;
			float var3 = heightLimit * .5F - par1;
			float var4;

			if (var3 == 0.0F) {
				var4 = var2;
			} else if (Math.abs(var3) >= var2) {
				return 0.0F;
			} else {
				var4 = (float) Math.sqrt(var2 * var2 - var3 * var3);
			}

			var4 *= 0.5F;
			return var4;
		}
	}

	/**
	 * Generates a list of leaf nodes for the tree, to be populated by generateLeaves.
	 */
	private void generateLeafNodeList() {

		int var1 = density;

		int[] basePos = this.basePos;
		int[][] var2 = new int[var1 * heightLimit][4];
		int var3 = basePos[1] + heightLimit - leafDistanceLimit;
		int var4 = 1;
		int var5 = basePos[1] + height;
		int var6 = var3 - basePos[1];
		var2[0][0] = basePos[0];
		var2[0][1] = var3;
		var2[0][2] = basePos[2];
		var2[0][3] = var5;
		--var3;

		while (var6 >= 0) {
			int var7 = 0;
			float var8 = this.layerSize(var6);

			if (var8 > 0.0F)
				for (float var9 = 0.5f; var7 < var1; ++var7) {
					float var11 = scaleWidth * var8 * (rand.nextFloat() + 0.328f);
					//float var13 = rand.nextFloat() * 2.0f * PI;
					float msin = (float)Math.sin(rand.nextFloat() * 2.0f * PI);
					int var15 = MathHelper.floor_float(var11 * msin + basePos[0] + var9);
					int var16 = MathHelper.floor_float(var11 * (float)Math.sqrt(1 - msin*msin) + basePos[2] + var9);
					int[] var17 = new int[] { var15, var3, var16 };
					int[] var18 = new int[] { var15, var3 + leafDistanceLimit, var16 };

					if (this.checkBlockLine(var17, var18) == -1) {
						int t;
						float var20 = (float)Math.sqrt((t = basePos[0] - var17[0]) * t + (t = basePos[2] - var17[2]) * t);
						int var22 = (int) (var20 * branchSlope);

						int[] var19 = new int[] { basePos[0], Math.min(var17[1] - var22, var5), basePos[2] };

						if (this.checkBlockLine(var19, var17) == -1) {
							var2[var4][0] = var15;
							var2[var4][1] = var3;
							var2[var4][2] = var16;
							var2[var4][3] = var19[1];
							++var4;
						}
					}
				}
			--var3;
			--var6;
		}

		leafNodes = var2;
		leafNodesLength = var4;
	}

	private void genLeafLayer(int x, int y, int z, final int size) {

		int t;
		final int X = x;
		final int Z = z;
		final float maxDistSq = size * size;

		for (int xMod = -size; xMod <= size; ++xMod) {
			x = X + xMod;
			final int xDistSq = xMod * xMod + (((t = xMod >> 31) ^ xMod) - t);

			for (int zMod = 0; zMod <= size;) { // negative values handled below
				final float distSq = xDistSq + zMod * zMod + zMod + 0.5f;

				if (distSq > maxDistSq) {
					break;
				} else {
					t = -1;
					do {
						z = Z + zMod * t;
						Block block = worldObj.getBlock(x, y, z);

						if (safeGrowth ? (block.isAir(worldObj, x, y, z) ||
								block.isLeaves(worldObj, x, y, z) ||
								block.canBeReplacedByLeaves(worldObj, x, y, z)) :
								block != Blocks.bedrock) {
							this.setBlockAndNotifyAdequately(worldObj, x, y, z, leaves, 0);
						}

						if (t == 1) break;
						t = 1;
					} while (true);
					++zMod;
				}
			}
		}
	}

	/**
	 * Generates the leaf portion of the tree as specified by the leafNodes list.
	 */
	private void generateLeaves() {

		int[][] leafNodes = this.leafNodes;

		for (int i = 0, e = leafNodesLength; i < e; ++i) {
			int[] n = leafNodes[i];
			int x = n[0], yO = n[1], z = n[2];
			int y = 0;

			for (int var5 = y + leafDistanceLimit; y < var5; ++y) {
				int size = (y != 0) & y != leafDistanceLimit - 1 ? 3 : 2;
				genLeafLayer(x, yO++, z, size);
			}
		}
	}

	private int[] placeScratch = new int[3];

	private void placeBlockLine(int[] par1, int[] par2, Block par3, int meta) {

		int t;
		int[] var4 = placeScratch;
		byte var6 = 0;

		for (byte i = 0; i < 3; ++i) {
			int a = par2[i] - par1[i], b = ((t = a >> 31) ^ a) - t;
			var4[i] = a;
			if (b > ((a = var4[var6]) ^ (t = a >> 31)) - t)
				var6 = i;
		}

		if (var4[var6] != 0) {
			byte var7 = otherCoordPairs[var6];
			byte var8 = otherCoordPairs[var6 + 3];
			byte var9;

			if (var4[var6] > 0) {
				var9 = 1;
			} else {
				var9 = -1;
			}

			float var10 = (float) var4[var7] / (float) var4[var6];
			float var12 = (float) var4[var8] / (float) var4[var6];
			int var16 = var4[var6] + var9;

			int[] var14 = var4;

			for (int var15 = 0; var15 != var16; var15 += var9) {
				var14[var6] = MathHelper.floor_float(par1[var6] + var15 + 0.5F);
				var14[var7] = MathHelper.floor_float(par1[var7] + var15 * var10 + 0.5F);
				var14[var8] = MathHelper.floor_float(par1[var8] + var15 * var12 + 0.5F);
				byte var17 = 0;
				int var18 = var14[0] - par1[0];
				var18 = ((t = var18 >> 31) ^ var18) - t;
				int var19 = var14[2] - par1[2];
				var19 = ((t = var19 >> 31) ^ var19) - t;
				int var20 = Math.max(var18, var19);

				if (var20 > 0) {
					if (var18 == var20) {
						var17 = 4;
					} else if (var19 == var20) {
						var17 = 8;
					}
				}

				this.setBlockAndNotifyAdequately(worldObj, var14[0], var14[1], var14[2], par3, meta | var17);
			}
		}
	}

	/**
	 * Places the trunk for the big tree that is being generated. Able to generate double-sized trunks by changing a
	 * field that is always 1 to 2.
	 */
	private void generateTrunk() {

		int var1 = basePos[0];
		int var2 = basePos[1];
		int var3 = basePos[1] + height;
		int var4 = basePos[2];

		int[] var5 = new int[] { var1, var2, var4 };
		int[] var6 = new int[] { var1, var3, var4 };

		double lim = 400f / trunkSize;

		for (int i = -trunkSize; i <= trunkSize; i++) {
			var5[0] = var1 + i;
			var6[0] = var1 + i;

			for (int j = -trunkSize; j <= trunkSize; j++) {
				if ((j * j + i * i) * 4 < trunkSize * trunkSize * 5) {
					var5[2] = var4 + j;
					var6[2] = var4 + j;

					if (slopeTrunk)
						var6[1] = var2 + sinc2(lim * i, lim * j, height) - (rand.nextInt(3) - 1);

					this.placeBlockLine(var5, var6, log, 1);
					this.setBlockAndNotifyAdequately(worldObj, var6[0], var6[1], var6[2], log, 12 | 1);
					worldObj.getBlock(var5[0], var5[1] - 1, var5[2]).
							onPlantGrow(worldObj, var5[0], var5[1] - 1, var5[2], var1, var2, var4);
				}
			}
		}
	}

	private static final int sinc2(final double x, final double z, final int y) {

		//final double pi = Math.PI, pi2 = pi / 1.5;
		double r;
		r = Math.sqrt((r = (x / Math.PI)) * r + (r = (z / Math.PI)) * r) * Math.PI / 180;
		if (r == 0) return y;
		return (int) Math.round(y * (((Math.sin(r) / r) + (Math.sin(r * Math.PI/2) / (r * Math.PI/2))) / 2));
	}

	/**
	 * Generates additional wood blocks to fill out the bases of different leaf nodes that would otherwise degrade.
	 */
	void generateLeafNodeBases() {

		int[] start = new int[] { basePos[0], basePos[1], basePos[2] };
		int[][] leafNodes = this.leafNodes;

		int heightLimit = (int) (this.heightLimit * 0.2f);
		for (int i = 0, e = leafNodesLength; i < e; ++i) {
			int[] end = leafNodes[i];
			start[1] = end[3];
			int height = start[1] - basePos[1];

			if (height >= heightLimit) {
				this.placeBlockLine(start, end, log, 12 | 1);
			}
		}
	}

	private int[] checkScratch = new int[3];

	/**
	 * Checks a line of blocks in the world from the first coordinate to triplet to the second, returning the distance
	 * (in blocks) before a non-air, non-leaf block is encountered and/or the end is encountered.
	 */
	private int checkBlockLine(int[] par1, int[] par2) {

		int t;
		int[] var3 = checkScratch;
		byte var5 = 0;

		for (byte i = 0; i < 3; ++i) {
			int a = par2[i] - par1[i], b = ((t = a >> 31) ^ a) - t;
			var3[i] = a;
			if (b > ((a = var3[var5]) ^ (t = a >> 31)) - t)
				var5 = i;
		}

		if (var3[var5] == 0)
			return -1;
		else {
			byte var6 = otherCoordPairs[var5];
			byte var7 = otherCoordPairs[var5 + 3];
			byte var8;

			if (var3[var5] > 0) {
				var8 = 1;
			} else {
				var8 = -1;
			}

			float var9 = (float) var3[var6] / (float) var3[var5];
			float var11 = (float) var3[var7] / (float) var3[var5];
			int var14 = 0;
			int var15 = var3[var5] + var8;

			int[] var13 = var3;

			for (; var14 != var15; var14 += var8) {
				var13[var5] = par1[var5] + var14;
				var13[var6] = MathHelper.floor_float(par1[var6] + var14 * var9);
				var13[var7] = MathHelper.floor_float(par1[var7] + var14 * var11);
				int x = var13[0], y = var13[1], z = var13[2];
				Block var16 = worldObj.getBlock(x, y, z);

				if (safeGrowth ? !(var16.isAir(worldObj, x, y, z) ||
						var16.isReplaceable(worldObj, x, y, z) ||
						var16.canBeReplacedByLeaves(worldObj, x, y, z) ||
						var16.isLeaves(worldObj, x, y, z) ||
						var16.isWood(worldObj, x, y, z) ||
						var16 instanceof BlockSapling) :
						var16 == Blocks.bedrock)
					break;
			}

			return var14 == var15 ? -1 : ((t = var14 >> 31) ^ var14) - t;
		}
	}

	/**
	 * Returns a boolean indicating whether or not the current location for the tree, spanning basePos to to the height
	 * limit, is valid.
	 */
	private boolean validTreeLocation() {

		int newHeight = Math.min(heightLimit + basePos[1], 255) - basePos[1];
		if (newHeight < minHeight)
			return false;
		heightLimit = newHeight;

		Block block = worldObj.getBlock(basePos[0], basePos[1] - 1, basePos[2]);

		if (!block.canSustainPlant(worldObj, basePos[0], basePos[1] - 1, basePos[2],
			ForgeDirection.UP, MFRThings.rubberSaplingBlock))
			return false;
		else {
			int[] var5 = new int[] { basePos[0], basePos[1], basePos[2] };
			int[] var6 = new int[] { basePos[0], basePos[1] + heightLimit - 1, basePos[2] };

			newHeight = this.checkBlockLine(var5, var6);

			if (newHeight == -1) newHeight = heightLimit;
			if (newHeight < minHeight)
				return false;

			heightLimit = Math.min(newHeight, heightLimitLimit);
			height = (int) (heightLimit * heightAttenuation);
			if (height >= heightLimit)
				height = heightLimit - 1;
			height += rand.nextInt(heightLimit - height);

			if (safeGrowth) {
				int var1 = basePos[0];
				int var2 = basePos[1];
				int var3 = basePos[1] + height;
				int var4 = basePos[2];

				var5 = new int[] { var1, var2, var4 };
				var6 = new int[] { var1, var3, var4 };

				double lim = 400f / trunkSize;

				for (int i = -trunkSize; i <= trunkSize; i++) {
					var5[0] = var1 + i;
					var6[0] = var1 + i;

					for (int j = -trunkSize; j <= trunkSize; j++) {
						if ((j * j + i * i) * 4 < trunkSize * trunkSize * 5) {
							var5[2] = var4 + j;
							var6[2] = var4 + j;

							if (slopeTrunk)
								var6[1] = var2 + sinc2(lim * i, lim * j, height);

							int t = checkBlockLine(var5, var6);
							if (t != -1)
								return false;
						}
					}
				}
			}

			return true;
		}
	}

	/**
	 * Rescales the generator settings, only used in WorldGenBigTree
	 */
	@Override
	public void setScale(double par1, double par3, double par5) {

		setTreeScale((float) par1, (float) par3, (float) par5);
	}

	public WorldGenMassiveTree setTreeScale(float height, float width, float leaves) {

		heightLimitLimit = (int) (height * 12.0D);
		minHeight = heightLimitLimit / 2;
		trunkSize = (int) Math.round((height / 2D));

		if (minHeight > 30)
			leafDistanceLimit = 5;
		else
			leafDistanceLimit = minHeight / 8;

		scaleWidth = width;
		branchDensity = leaves;
		return this;
	}

	public WorldGenMassiveTree setMinTrunkSize(int radius) {

		trunkSize = Math.max(radius, trunkSize);
		return this;
	}

	public WorldGenMassiveTree setLeafAttenuation(float a) {

		heightAttenuation = a;
		return this;
	}

	public WorldGenMassiveTree setSloped(boolean s) {

		slopeTrunk = s;
		return this;
	}

	public WorldGenMassiveTree setSafe(boolean s) {

		safeGrowth = s;
		return this;
	}

	@Override
	public boolean generate(World world, Random par2Random, int x, int y, int z) {

		//long time = System.nanoTime();
		worldObj = world;
		long var6 = par2Random.nextLong();
		rand.setSeed(var6);
		basePos[0] = x;
		basePos[1] = y;
		basePos[2] = z;
		if (heightLimit == 0) heightLimit = heightLimitLimit;
		if (minHeight == -1) minHeight = 80;

		if (!this.validTreeLocation())
			return false;
		else {
			this.setup();
			//time = System.nanoTime() - time;
			//logger.info("Verified spawn position of massive rubber tree in: " + time + "ns");
			//long time2 = time = System.nanoTime();
			this.generateLeafNodeList();
			//long nodes = System.nanoTime();
			this.generateLeaves();
			//long leaves = System.nanoTime();
			this.generateLeafNodeBases();
			//long bases = System.nanoTime();
			this.generateTrunk();
			//long trunk = System.nanoTime();
			//time = System.nanoTime() - time;
			//logger.info("Generated massive rubber tree in: " + time + "ns");
			//trunk -= bases; bases -= leaves; leaves -= nodes; nodes -= time2;
			//logger.info(String.format("%s for trunk, %s for leaf nodes, %s for leaves, %s for branches", trunk, nodes, leaves, bases));
			//logger.info("\tTree contains " + blocksAdded + " Blocks");
			//time = System.nanoTime();
			for (TLongObjectIterator<Chunk> iter = chunkMap.iterator(); iter.hasNext();) {
				iter.advance();
				MineFactoryReloadedCore.proxy.relightChunk(iter.value());
			}
			//time = System.nanoTime() - time;
			//logger.info("Lit massive rubber tree in: " + time + "ns");
			return true;
		}
	}

	//private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger("Tree logger");
	//private int blocksAdded = 0;
	private TLongObjectHashMap<Chunk> chunkMap;

	@Override
	public void setBlockAndNotifyAdequately(World world, int x, int y, int z, Block block, int meta) {

		if ((y < 0) | y > 255) return;
		//++blocksAdded;
		long pos = ((x & 0xFFFFFFF0L) << 32) | (z & 0xFFFFFFF0L);

		Chunk chunk = chunkMap.get(pos);
		if (chunk == null) {
			chunk = world.getChunkFromBlockCoords(x, z);
			chunkMap.put(pos, chunk);
		}

		ExtendedBlockStorage[] storage = chunk.getBlockStorageArray();
		ExtendedBlockStorage subChunk = storage[y >> 4];
		if (subChunk == null)
			storage[y >> 4] = subChunk = new ExtendedBlockStorage(y & ~15, !world.provider.hasNoSky);

		x &= 15;
		z &= 15;
		if (subChunk.getBlockByExtId(x, y & 15, z).hasTileEntity(subChunk.getExtBlockMetadata(x, y & 15, z)))
			chunk.removeTileEntity(x & 15, y, z & 15);
		y &= 15;

		subChunk.func_150818_a(x, y, z, block);
		subChunk.setExtBlockMetadata(x, y, z, meta);
		subChunk.setExtBlocklightValue(x, y, z, 0);
	}

}
