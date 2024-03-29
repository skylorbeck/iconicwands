package website.skylorbeck.minecraft.iconicwands;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import vazkii.patchouli.api.PatchouliAPI;
import website.skylorbeck.minecraft.iconicwands.blocks.WandBench;
import website.skylorbeck.minecraft.iconicwands.blocks.WandPedestal;
import website.skylorbeck.minecraft.iconicwands.blocks.WeakLightBlock;
import website.skylorbeck.minecraft.iconicwands.config.Parts;
import website.skylorbeck.minecraft.iconicwands.entity.MagicProjectileEntity;
import website.skylorbeck.minecraft.iconicwands.entity.WandBenchEntity;
import website.skylorbeck.minecraft.iconicwands.entity.WandPedestalEntity;
import website.skylorbeck.minecraft.iconicwands.items.IconicWand;
import website.skylorbeck.minecraft.iconicwands.items.WandPresetTester;
import website.skylorbeck.minecraft.iconicwands.items.WandTester;
import website.skylorbeck.minecraft.iconicwands.screen.WandBenchScreenHandler;

import java.util.List;

public class Declarar {
    public static final ItemGroup ICONIC_WAND_GROUP = FabricItemGroupBuilder.build(Iconicwands.getId("category"), () ->{
        ItemStack itemStack = new ItemStack(Declarar.ICONIC_WAND);
        IconicWand.saveComponents(itemStack,Iconicwands.Presets.overworld.wand);
        return itemStack;
    });
    public static Identifier MAGIC_PROJECTILE_ENTITY_ID = Iconicwands.getId("magic");

    public static <T extends Entity> EntityType<T> createMagicEntityType(EntityType.EntityFactory<T> factory) {
        return EntityType.Builder.create(factory, SpawnGroup.MISC)
                .setDimensions(0.5f, 0.5f)
                .build(MAGIC_PROJECTILE_ENTITY_ID.toString());
    }

    public static final EntityType<MagicProjectileEntity> MAGIC_PROJECTILE = Registry.register(Registry.ENTITY_TYPE, MAGIC_PROJECTILE_ENTITY_ID, createMagicEntityType(MagicProjectileEntity::new));

    public static final Item ICONIC_WAND = new IconicWand(new FabricItemSettings().rarity(Rarity.EPIC).maxCount(1).maxDamage(1024).customDamage((stack, amount, entity, breakCallback) -> {
        Parts.WandCluster wand = IconicWand.getPartComobo(stack);
        return wand.getHandle().getManaCost() + wand.getTip().getManaCost();
    }));

    public static final Block WAND_BENCH = new WandBench(FabricBlockSettings.copy(Blocks.CRAFTING_TABLE).hardness(1f).resistance(1f));

    public static final BlockEntityType<WandBenchEntity> WAND_BENCH_ENTITY = Registry.register(
            Registry.BLOCK_ENTITY_TYPE, Iconicwands.getId("wand_bench_entity"),
            FabricBlockEntityTypeBuilder.create(WandBenchEntity::new, WAND_BENCH).build(null));

    public static final BlockItem WAND_BENCH_ITEM = new BlockItem(WAND_BENCH, new FabricItemSettings().group(ICONIC_WAND_GROUP)) {
        //this was commented out during the update from 1.18.2 to 1.19, patchouli had not updated at the time.
        @Override
        public void onCraft(ItemStack stack, World world, PlayerEntity player) {
            if (FabricLoader.getInstance().isModLoaded("patchouli")) {
                ItemStack book = PatchouliAPI.get().getBookStack(Iconicwands.getId("book_1"));
                if (!player.getInventory().contains(book))
                    player.getInventory().offerOrDrop(book);
                super.onCraft(stack, world, player);
            }
        }
    };
    public static final Block WAND_PEDESTAL = new WandPedestal(FabricBlockSettings.copy(Blocks.BLACKSTONE).hardness(1f).resistance(1f).breakInstantly());
    public static final Block WAND_PEDESTAL_DISPLAY = new WandPedestal(FabricBlockSettings.copy(Blocks.BLACKSTONE).hardness(1f).resistance(1f).breakInstantly());

    public static final BlockItem WAND_PEDESTAL_ITEM = new BlockItem(WAND_PEDESTAL, new FabricItemSettings().group(ICONIC_WAND_GROUP));
    public static final BlockItem WAND_PEDESTAL_DISPLAY_ITEM = new BlockItem(WAND_PEDESTAL_DISPLAY, new FabricItemSettings().group(ICONIC_WAND_GROUP));

    public static final BlockEntityType<WandPedestalEntity> WAND_PEDESTAL_ENTITY = Registry.register(
            Registry.BLOCK_ENTITY_TYPE, Iconicwands.getId("wand_pedestal_entity"),
            FabricBlockEntityTypeBuilder.create(WandPedestalEntity::new, WAND_PEDESTAL,WAND_PEDESTAL_DISPLAY).build(null));

    public static Block TIMED_LIGHT = new WeakLightBlock(FabricBlockSettings.copy(Blocks.LIGHT));

    public static Item WAND_TESTER = new WandTester(new Item.Settings().group(ICONIC_WAND_GROUP)){
        @Override
        public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
            tooltip.add(Text.of("THIS WILL PROBABLY DESTROY YOUR WORLD"));
            tooltip.add(Text.of("THIS WILL PROBABLY DESTROY YOUR WORLD"));
            tooltip.add(Text.of("THIS WILL PROBABLY DESTROY YOUR WORLD"));
            super.appendTooltip(stack, world, tooltip, context);
        }
    };
    public static Item WAND_PRESET_TESTER = new WandPresetTester(new Item.Settings().group(ICONIC_WAND_GROUP));

    public static ScreenHandlerType<WandBenchScreenHandler> WANDING;
}