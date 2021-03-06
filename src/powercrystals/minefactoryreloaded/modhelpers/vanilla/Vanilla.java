package powercrystals.minefactoryreloaded.modhelpers.vanilla;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.CustomProperty;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;

import net.minecraft.block.IGrowable;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import powercrystals.minefactoryreloaded.MFRRegistry;
import powercrystals.minefactoryreloaded.MineFactoryReloadedCore;
import powercrystals.minefactoryreloaded.api.FertilizerType;
import powercrystals.minefactoryreloaded.api.HarvestType;
import powercrystals.minefactoryreloaded.api.MobDrop;
import powercrystals.minefactoryreloaded.farmables.drinkhandlers.DrinkHandlerLava;
import powercrystals.minefactoryreloaded.farmables.drinkhandlers.DrinkHandlerWater;
import powercrystals.minefactoryreloaded.farmables.egghandlers.VanillaEggHandler;
import powercrystals.minefactoryreloaded.farmables.fertilizables.FertilizableCocoa;
import powercrystals.minefactoryreloaded.farmables.fertilizables.FertilizableCropPlant;
import powercrystals.minefactoryreloaded.farmables.fertilizables.FertilizableGrass;
import powercrystals.minefactoryreloaded.farmables.fertilizables.FertilizableNetherWart;
import powercrystals.minefactoryreloaded.farmables.fertilizables.FertilizableStandard;
import powercrystals.minefactoryreloaded.farmables.fertilizables.FertilizableStemPlants;
import powercrystals.minefactoryreloaded.farmables.fertilizables.FertilizerStandard;
import powercrystals.minefactoryreloaded.farmables.fruits.FruitCocoa;
import powercrystals.minefactoryreloaded.farmables.grindables.GrindableEnderman;
import powercrystals.minefactoryreloaded.farmables.grindables.GrindableSlime;
import powercrystals.minefactoryreloaded.farmables.grindables.GrindableStandard;
import powercrystals.minefactoryreloaded.farmables.grindables.GrindableZombiePigman;
import powercrystals.minefactoryreloaded.farmables.harvestables.HarvestableCocoa;
import powercrystals.minefactoryreloaded.farmables.harvestables.HarvestableCropPlant;
import powercrystals.minefactoryreloaded.farmables.harvestables.HarvestableGourd;
import powercrystals.minefactoryreloaded.farmables.harvestables.HarvestableMushroom;
import powercrystals.minefactoryreloaded.farmables.harvestables.HarvestableShrub;
import powercrystals.minefactoryreloaded.farmables.harvestables.HarvestableStandard;
import powercrystals.minefactoryreloaded.farmables.harvestables.HarvestableStemPlant;
import powercrystals.minefactoryreloaded.farmables.harvestables.HarvestableTreeLeaves;
import powercrystals.minefactoryreloaded.farmables.harvestables.HarvestableVine;
import powercrystals.minefactoryreloaded.farmables.harvestables.HarvestableWood;
import powercrystals.minefactoryreloaded.farmables.plantables.PlantableCocoa;
import powercrystals.minefactoryreloaded.farmables.plantables.PlantableCropPlant;
import powercrystals.minefactoryreloaded.farmables.plantables.PlantableNetherWart;
import powercrystals.minefactoryreloaded.farmables.plantables.PlantableSapling;
import powercrystals.minefactoryreloaded.farmables.plantables.PlantableStandard;
import powercrystals.minefactoryreloaded.farmables.ranchables.RanchableChicken;
import powercrystals.minefactoryreloaded.farmables.ranchables.RanchableCow;
import powercrystals.minefactoryreloaded.farmables.ranchables.RanchableMooshroom;
import powercrystals.minefactoryreloaded.farmables.ranchables.RanchableSheep;
import powercrystals.minefactoryreloaded.farmables.ranchables.RanchableSquid;
import powercrystals.minefactoryreloaded.farmables.spawnhandlers.SpawnableEnderman;
import powercrystals.minefactoryreloaded.farmables.spawnhandlers.SpawnableHorse;
import powercrystals.minefactoryreloaded.setup.MFRConfig;

@Mod(modid = "MineFactoryReloaded|CompatVanilla", name = "MFR Compat: Vanilla", version = MineFactoryReloadedCore.version, dependencies = "after:MineFactoryReloaded",
		customProperties = @CustomProperty(k = "cofhversion", v = "true"))
public class Vanilla {

	@EventHandler
	public void load(FMLInitializationEvent event) {

		MFRRegistry.registerPlantable(new PlantableSapling(Blocks.sapling));
		MFRRegistry.registerPlantable(new PlantableStandard(Blocks.brown_mushroom));
		MFRRegistry.registerPlantable(new PlantableStandard(Blocks.red_mushroom));
		MFRRegistry.registerPlantable(new PlantableCropPlant(Items.pumpkin_seeds, Blocks.pumpkin_stem));
		MFRRegistry.registerPlantable(new PlantableCropPlant(Items.melon_seeds, Blocks.melon_stem));
		MFRRegistry.registerPlantable(new PlantableCropPlant(Items.wheat_seeds, Blocks.wheat));
		MFRRegistry.registerPlantable(new PlantableCropPlant(Items.carrot, Blocks.carrots));
		MFRRegistry.registerPlantable(new PlantableCropPlant(Items.potato, Blocks.potatoes));
		MFRRegistry.registerPlantable(new PlantableNetherWart());
		MFRRegistry.registerPlantable(new PlantableCocoa(Items.dye, Blocks.cocoa, 3));

		MFRRegistry.registerHarvestable(new HarvestableWood(Blocks.log));
		MFRRegistry.registerHarvestable(new HarvestableWood(Blocks.log2));
		MFRRegistry.registerHarvestable(new HarvestableTreeLeaves(Blocks.leaves));
		MFRRegistry.registerHarvestable(new HarvestableTreeLeaves(Blocks.leaves2));
		MFRRegistry.registerHarvestable(new HarvestableStandard(Blocks.reeds, HarvestType.LeaveBottom));
		MFRRegistry.registerHarvestable(new HarvestableStandard(Blocks.cactus, HarvestType.LeaveBottom));
		MFRRegistry.registerHarvestable(new HarvestableStandard(Blocks.red_flower, HarvestType.Normal));
		MFRRegistry.registerHarvestable(new HarvestableStandard(Blocks.yellow_flower, HarvestType.Normal));
		MFRRegistry.registerHarvestable(new HarvestableShrub(Blocks.tallgrass));
		MFRRegistry.registerHarvestable(new HarvestableShrub(Blocks.deadbush));
		MFRRegistry.registerHarvestable(new HarvestableShrub(Blocks.double_plant));
		MFRRegistry.registerHarvestable(new HarvestableStandard(Blocks.brown_mushroom_block, HarvestType.Tree));
		MFRRegistry.registerHarvestable(new HarvestableStandard(Blocks.red_mushroom_block, HarvestType.Tree));
		MFRRegistry.registerHarvestable(new HarvestableMushroom(Blocks.brown_mushroom));
		MFRRegistry.registerHarvestable(new HarvestableMushroom(Blocks.red_mushroom));
		MFRRegistry.registerHarvestable(new HarvestableStemPlant(Blocks.pumpkin_stem, Blocks.pumpkin));
		MFRRegistry.registerHarvestable(new HarvestableStemPlant(Blocks.melon_stem, Blocks.melon_block));
		MFRRegistry.registerHarvestable(new HarvestableGourd(Blocks.pumpkin));
		MFRRegistry.registerHarvestable(new HarvestableGourd(Blocks.melon_block));
		MFRRegistry.registerHarvestable(new HarvestableCropPlant(Blocks.wheat, 7));
		MFRRegistry.registerHarvestable(new HarvestableCropPlant(Blocks.carrots, 7));
		MFRRegistry.registerHarvestable(new HarvestableCropPlant(Blocks.potatoes, 7));
		MFRRegistry.registerHarvestable(new HarvestableCropPlant(Blocks.nether_wart, 3));
		MFRRegistry.registerHarvestable(new HarvestableVine(Blocks.vine));
		MFRRegistry.registerHarvestable(new HarvestableCocoa(Blocks.cocoa));

		MFRRegistry.registerFertilizable(new FertilizableStandard((IGrowable) Blocks.sapling));
		MFRRegistry.registerFertilizable(new FertilizableCropPlant((IGrowable) Blocks.wheat, 7));
		MFRRegistry.registerFertilizable(new FertilizableCropPlant((IGrowable) Blocks.carrots, 7));
		MFRRegistry.registerFertilizable(new FertilizableCropPlant((IGrowable) Blocks.potatoes, 7));
		MFRRegistry.registerFertilizable(new FertilizableStandard((IGrowable) Blocks.brown_mushroom));
		MFRRegistry.registerFertilizable(new FertilizableStandard((IGrowable) Blocks.red_mushroom));
		MFRRegistry.registerFertilizable(new FertilizableStemPlants((IGrowable) Blocks.pumpkin_stem));
		MFRRegistry.registerFertilizable(new FertilizableStemPlants((IGrowable) Blocks.melon_stem));
		MFRRegistry.registerFertilizable(new FertilizableNetherWart());
		MFRRegistry.registerFertilizable(new FertilizableCocoa(Blocks.cocoa));
		MFRRegistry.registerFertilizable(new FertilizableGrass());

		if (MFRConfig.enableBonemealFertilizing.getBoolean(false)) {
			MFRRegistry.registerFertilizer(new FertilizerStandard(Items.dye, 15));
		} else {
			MFRRegistry.registerFertilizer(new FertilizerStandard(Items.dye, 15, FertilizerType.Grass));
		}

		MFRRegistry.registerRanchable(new RanchableCow());
		MFRRegistry.registerRanchable(new RanchableMooshroom());
		MFRRegistry.registerRanchable(new RanchableSheep());
		MFRRegistry.registerRanchable(new RanchableSquid());
		MFRRegistry.registerRanchable(new RanchableChicken());

		MFRRegistry.registerGrinderBlacklist(EntityDragon.class);
		MFRRegistry.registerGrinderBlacklist(EntityWither.class);
		MFRRegistry.registerGrinderBlacklist(EntityVillager.class);

		MFRRegistry.registerGrindable(new GrindableStandard(EntityChicken.class, new MobDrop[] {
				new MobDrop(30, null),
				new MobDrop(10, new ItemStack(Items.egg))
		}, false));
		MFRRegistry.registerGrindable(new GrindableStandard(EntityOcelot.class, new MobDrop[] {
				new MobDrop(10, new ItemStack(Items.fish)),
				new MobDrop(10, new ItemStack(Items.string))
		}));
		MFRRegistry.registerGrindable(new GrindableStandard(EntityWolf.class, new ItemStack(Items.bone)));
		MFRRegistry.registerGrindable(new GrindableZombiePigman());
		MFRRegistry.registerGrindable(new GrindableEnderman());
		MFRRegistry.registerGrindable(new GrindableSlime(EntitySlime.class, new ItemStack(Items.slime_ball), 1));
		MFRRegistry.registerGrindable(new GrindableSlime(EntityMagmaCube.class, new ItemStack(Items.magma_cream), 1) {

			@Override
			protected boolean shouldDrop(EntitySlime slime) {

				return slime.getSlimeSize() <= dropSize;
			}
		});

		MFRRegistry.registerSludgeDrop(50, new ItemStack(Blocks.sand));
		MFRRegistry.registerSludgeDrop(30, new ItemStack(Blocks.clay));
		MFRRegistry.registerSludgeDrop(30, new ItemStack(Blocks.dirt, 1, 1));
		MFRRegistry.registerSludgeDrop(10, new ItemStack(Blocks.dirt));
		MFRRegistry.registerSludgeDrop(10, new ItemStack(Blocks.gravel));
		MFRRegistry.registerSludgeDrop(5, new ItemStack(Blocks.sand, 1, 1));
		MFRRegistry.registerSludgeDrop(5, new ItemStack(Blocks.soul_sand));
		MFRRegistry.registerSludgeDrop(3, new ItemStack(Blocks.mycelium));
		MFRRegistry.registerSludgeDrop(2, new ItemStack(Blocks.dirt, 1, 2));
		MFRRegistry.registerSludgeDrop(1, new ItemStack(Blocks.netherrack));

		MFRRegistry.registerMobEggHandler(new VanillaEggHandler());

		MFRRegistry.registerRubberTreeBiome("Swampland");
		MFRRegistry.registerRubberTreeBiome("Swampland M");
		//MFRRegistry.registerRubberTreeBiome("Forest");
		//MFRRegistry.registerRubberTreeBiome("Flower Forest");
		//MFRRegistry.registerRubberTreeBiome("ForestHills");
		//MFRRegistry.registerRubberTreeBiome("ForestHills M");
		MFRRegistry.registerRubberTreeBiome("Roofed Forest");
		MFRRegistry.registerRubberTreeBiome("Roofed Forest M");
		//MFRRegistry.registerRubberTreeBiome("Taiga");
		//MFRRegistry.registerRubberTreeBiome("Taiga M");
		//MFRRegistry.registerRubberTreeBiome("TaigaHills");
		//MFRRegistry.registerRubberTreeBiome("TaigaHills M");
		//MFRRegistry.registerRubberTreeBiome("Cold Taiga");
		//MFRRegistry.registerRubberTreeBiome("Cold Taiga M");
		//MFRRegistry.registerRubberTreeBiome("Cold Taiga Hills");
		//MFRRegistry.registerRubberTreeBiome("Cold Taiga Hills M");
		MFRRegistry.registerRubberTreeBiome("Mega Taiga");
		MFRRegistry.registerRubberTreeBiome("Mega Spruce Taiga");
		MFRRegistry.registerRubberTreeBiome("Mega Taiga Hills");
		MFRRegistry.registerRubberTreeBiome("Mega Spruce Taiga Hills");
		MFRRegistry.registerRubberTreeBiome("Jungle");
		MFRRegistry.registerRubberTreeBiome("Jungle M");
		MFRRegistry.registerRubberTreeBiome("JungleHills");
		MFRRegistry.registerRubberTreeBiome("JungleHills M");
		MFRRegistry.registerRubberTreeBiome("JungleEdge");
		MFRRegistry.registerRubberTreeBiome("JungleEdge M");

		MFRRegistry.registerSafariNetBlacklist(EntityDragon.class);
		MFRRegistry.registerSafariNetBlacklist(EntityWither.class);

		MFRRegistry.registerRandomMobProvider(new VanillaMobProvider());

		MFRRegistry.registerLiquidDrinkHandler("water", new DrinkHandlerWater());
		MFRRegistry.registerLiquidDrinkHandler("lava", new DrinkHandlerLava());

		MFRRegistry.registerFruitLogBlock(Blocks.log);
		MFRRegistry.registerFruit(new FruitCocoa(Blocks.cocoa));

		MFRRegistry.registerSpawnHandler(new SpawnableHorse());
		MFRRegistry.registerSpawnHandler(new SpawnableEnderman());
	}

	@EventHandler
	public void postLoad(FMLPostInitializationEvent event) {

		registerOreDictLaserOre(175, "Coal",               black, false);
		registerOreDictLaserOre(150, "Iron",               brown, false);
		registerOreDictLaserOre(100, "Redstone",             red, false);
		registerOreDictLaserOre(100, "Nikolite",       lightBlue, false);
		registerOreDictLaserOre(100, "oreSalt",            white, "oreNetherSalt");
		registerOreDictLaserOre( 90, "Copper",            orange, false);
		registerOreDictLaserOre( 85, "Tin",               silver, false);
		registerOreDictLaserOre( 85, "oreCheese",         yellow, null);
		registerOreDictLaserOre( 85, "Force",             yellow,  true);
		registerOreDictLaserOre( 80, "glowstone",         yellow, null);
		registerOreDictLaserOre( 80, "Lapis",               blue,  true);
		registerOreDictLaserOre( 70, "Gold",              yellow, false);
		registerOreDictLaserOre( 70, "oreQuartz",          white, null);
		registerOreDictLaserOre( 60, "Lead",              purple, false);
		registerOreDictLaserOre( 60, "oreZinc",            white, "oreSphalerite", orange);
		registerOreDictLaserOre( 60, "NaturalAluminum",    white, false);
		registerOreDictLaserOre( 60, "Aluminium",          white, false);
		registerOreDictLaserOre( 60, "Aluminum",           white, false);
		registerOreDictLaserOre( 60, "oreDark",            black,  true);
		registerOreDictLaserOre( 60, "oreSodalite",         blue, null);
		registerOreDictLaserOre( 55, "Mithril",             blue, false);
		registerOreDictLaserOre( 55, "Steel",               gray, false);
		registerOreDictLaserOre( 55, "oreCassiterite",     black, null);
		registerOreDictLaserOre( 55, "Diamond",        lightBlue,  true);
		registerOreDictLaserOre( 55, "oreDesh",             gray, null);
		registerOreDictLaserOre( 50, "CertusQuartz",        cyan,  true);
		registerOreDictLaserOre( 50, "Osmium",         lightBlue, false);
		registerOreDictLaserOre( 50, "oreBauxite",         brown, null);
		registerOreDictLaserOre( 45, "Rutile",             black, false);
		registerOreDictLaserOre( 45, "Titanium",           black, false);
		registerOreDictLaserOre( 45, "Tungsten",           black, false);
		registerOreDictLaserOre( 45, "oreTungstate",       black, "oreNetherTungsten");
		registerOreDictLaserOre( 45, "orePyrite",         orange, null);
		registerOreDictLaserOre( 45, "FzDarkIron",        purple, false);
		registerOreDictLaserOre( 40, "Tennantite",          lime, false);
		registerOreDictLaserOre( 40, "Nickel",            silver, false);
		registerOreDictLaserOre( 40, "Sulfur",            yellow, false);
		registerOreDictLaserOre( 40, "Saltpeter",          white, false);
		registerOreDictLaserOre( 35, "Emerald",             lime,  true);
		registerOreDictLaserOre( 35, "Ruby",                 red,  true);
		registerOreDictLaserOre( 35, "Sapphire",            blue,  true);
		registerOreDictLaserOre( 35, "GreenSapphire",      green,  true);
		registerOreDictLaserOre( 35, "Peridot",            green,  true);
		registerOreDictLaserOre( 35, "Topaz",              brown,  true);
		registerOreDictLaserOre( 35, "Tanzanite",         purple,  true);
		registerOreDictLaserOre( 35, "Malachite",           cyan,  true);
		registerOreDictLaserOre( 35, "Amber",             orange,  true);
		registerOreDictLaserOre( 30, "Adamantium",         green, false);
		registerOreDictLaserOre( 30, "Silver",              gray, false);
		registerOreDictLaserOre( 30, "Galena",            purple, false);
		registerOreDictLaserOre( 30, "Apatite",             blue,  true);
		registerOreDictLaserOre( 30, "Silicon",            black, false);
		registerOreDictLaserOre( 25, "Magnesium",         silver, false);
		registerOreDictLaserOre( 25, "Amethyst",         magenta,  true);
		registerOreDictLaserOre( 20, "Uranium",             lime, false);
		registerOreDictLaserOre( 20, "orePitchblende",     black, "oreNetherUranium", lime);
		registerOreDictLaserOre( 20, "oreFirestone",         red, null);
		registerOreDictLaserOre( 20, "MonazitOre",         green, null);
		registerOreDictLaserOre( 15, "Cinnabar",             red,  true);
		registerOreDictLaserOre( 15, "Adamantine",           red, false);
		registerOreDictLaserOre( 15, "Platinum",       lightBlue, false);
		registerOreDictLaserOre( 15, "oreCooperite",      yellow, "oreNetherPlatinum", lightBlue);
		registerOreDictLaserOre( 10, "oreArdite",         orange, null);
		registerOreDictLaserOre( 10, "oreCobalt",           blue, null);
		registerOreDictLaserOre( 10, "Yellorite",         yellow, false);
		registerOreDictLaserOre(  5, "Iridium",            white, false);

		// rarity/usefulness unknown
		registerOreDictLaserOre( 20, "oreTetrahedrite", orange, null);
		registerOreDictLaserOre( 20, "oreCadmium", lightBlue, null);
		registerOreDictLaserOre( 20, "oreIndium", silver, null);
		registerOreDictLaserOre( 20, "oreAmmonium", white, null);
		registerOreDictLaserOre( 20, "oreCalcite", orange, null);
		registerOreDictLaserOre( 20, "oreMagnetite", black, null);
		// focus also unknown
		registerOreDictLaserOre( 20, "oreManganese", pink, null);
		registerOreDictLaserOre( 20, "oreMeutoite", pink, null);
		registerOreDictLaserOre( 20, "oreEximite", pink, null);
		registerOreDictLaserOre( 20, "oreAtlarus", pink, null);
		registerOreDictLaserOre( 20, "oreOrichalcum", pink, null);
		registerOreDictLaserOre( 20, "oreRubracium", pink, null);
		registerOreDictLaserOre( 20, "oreCarmot", pink, null);
		registerOreDictLaserOre( 20, "oreAstralSilver", pink, null);
		registerOreDictLaserOre( 20, "oreOureclase", pink, null);
		registerOreDictLaserOre( 20, "oreInfuscolium", pink, null);
		registerOreDictLaserOre( 20, "oreDeepIron", pink, null);
		registerOreDictLaserOre( 20, "orePrometheum", pink, null);
		registerOreDictLaserOre( 20, "oreSanguinite", pink, null);
		registerOreDictLaserOre( 20, "oreVulcanite", pink, null);
		registerOreDictLaserOre( 20, "oreKalendrite", pink, null);
		registerOreDictLaserOre( 20, "oreAlduorite", pink, null);
		registerOreDictLaserOre( 20, "oreCeruclase", pink, null);
		registerOreDictLaserOre( 20, "oreVyroxeres", pink, null);
		registerOreDictLaserOre( 20, "oreMidasium", pink, null);
		registerOreDictLaserOre( 20, "oreLemurite", pink, null);
		registerOreDictLaserOre( 20, "oreShadowIron", pink, null);
		registerOreDictLaserOre( 20, "oreIgnatius", pink, null);
		registerOreDictLaserOre( 20, "orePotash", pink, null);
		registerOreDictLaserOre( 20, "oreBitumen", pink, null);
		registerOreDictLaserOre( 20, "orePhosphorite", pink, null);
	}

	private void registerOreDictLaserOre(int weight, String suffix, int focus, boolean isGem) {

		registerOreDictLaserOre(weight, "ore" + suffix, focus, "oreNether" + suffix, focus);
	}

	private void registerOreDictLaserOre(int weight, String name, int focus, String netherName) {

		registerOreDictLaserOre(weight, name, focus, netherName, focus);
	}

	private void registerOreDictLaserOre(int weight, String name, int focus, String netherName, int netherFocus) {

		for (ItemStack ore : OreDictionary.getOres(name))
			if (ore != null) {
				ore = ore.copy();
				ore.stackSize = 1;
				MFRRegistry.registerLaserOre(weight, ore);
				if (focus >= 0)
					MFRRegistry.addLaserPreferredOre(focus, ore);
				if (netherName != null) {
					registerOreDictLaserOre(weight / 2, netherName, netherFocus, null);
				}
				return;
			}
		if (netherName != null)
			for (ItemStack ore : OreDictionary.getOres(netherName))
				if (ore != null) {
					registerOreDictLaserOre(weight / 2, netherName, netherFocus, null);
					return;
				}
	}

	private static final int black = 15;
	private static final int red = 14;
	private static final int green = 13;
	private static final int brown = 12;
	private static final int blue = 11;
	private static final int purple = 10;
	private static final int cyan = 9;
	private static final int silver = 8;
	private static final int gray = 7;
	@SuppressWarnings("unused")
	private static final int pink = 6;
	private static final int lime = 5;
	private static final int yellow = 4;
	private static final int lightBlue = 3;
	private static final int magenta = 2;
	private static final int orange = 1;
	private static final int white = 0;
}
