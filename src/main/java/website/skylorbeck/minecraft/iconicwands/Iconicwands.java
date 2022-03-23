package website.skylorbeck.minecraft.iconicwands;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.util.Identifier;
import website.skylorbeck.minecraft.iconicwands.config.Parts;
import website.skylorbeck.minecraft.iconicwands.screen.WandBenchScreenHandler;
import website.skylorbeck.minecraft.skylorlib.ConfigFileHandler;
import website.skylorbeck.minecraft.skylorlib.Registrar;

import java.io.IOException;

public class Iconicwands implements ModInitializer {
    public static Parts parts = new Parts();
    static {

        //t1 5/5 | 6/2/2 | 3/3/2/2 | 2/2/2/2/2
        //t2 10/10 | 10/5/5 | 5/5/5/5 | 4/4/4/4/4
        //t3 20/10 | 10/10/10 | 10/10/5/5 | 6/6/6/6/6

        //Stat Name || Base stat || per point spent
        //rate      10 -1
        //delay     10 -1
        //amount    5  +5
//firerate/cooldown 20 -5
        //range     10 +10

        parts.addCores(
                //t1 5/5 | 6/2/2 | 3/3/2/2 | 2/2/2/2/2
                new Parts.Core("minecraft:oak_planks",6,2,0,2,0),
                new Parts.Core("minecraft:spruce_planks",3,3,0,2, 2),
                new Parts.Core("minecraft:birch_planks",2,2,6,0,0),
                new Parts.Core("minecraft:jungle_planks",5,5,0,0,0),
                new Parts.Core("minecraft:acacia_planks",2,2,0,3,3),
                new Parts.Core("minecraft:dark_oak_planks",0,0,5,0,5),
                new Parts.Core("minecraft:bone",10,5,0,-5,0),
                new Parts.Core("minecraft:carrot",2,2,2,2,2),

                //t2 10/10 | 10/5/5 | 5/5/5/5 | 4/4/4/4/4
                new Parts.Core("minecraft:blaze_rod",5,10,0,5,0),
                new Parts.Core("minecraft:amethyst_shard",5,5,0,5,5),
                new Parts.Core("minecraft:bamboo",10,10,0,0,0),
                new Parts.Core("minecraft:crimson_planks",5,5,0,10,0),
                new Parts.Core("minecraft:warped_planks",0,0,10,0,10),
//                new Parts.Core("minecraft:copper_ingot",7,7,1,2,3),

                //t3 20/10 | 10/10/10 | 10/10/5/5 | 6/6/6/6/6
                new Parts.Core("minecraft:end_rod",0,0,5,5,10),
                new Parts.Core("minecraft:golden_carrot",6,6,6,6,6)

        );
        //Stat Name || Base stat || per point spent
        //speed      0.5f  +0.05f
        //amount     0     +5
        //cost       50    -5
        //divergence 2f    -0.1f
        //crit       0.05f +0.01f
        parts.addTips(
                //t1 5/5 | 6/2/2 | 3/3/2/2 | 2/2/2/2/2
                new Parts.Tip("minecraft:oak_planks",3,2,0,2,3),
                new Parts.Tip("minecraft:spruce_planks",3,3,0,2, 2),
                new Parts.Tip("minecraft:birch_planks",2,2,3,3,0),
                new Parts.Tip("minecraft:jungle_planks",2,3,2,0,3),
                new Parts.Tip("minecraft:acacia_planks",2,2,0,3,3),
                new Parts.Tip("minecraft:dark_oak_planks",0,2,3,2,3),
                new Parts.Tip("minecraft:glow_berries",3,3,2,2,0),
                new Parts.Tip("minecraft:lapis_lazuli",0,0,0,0,5),
                new Parts.Tip("minecraft:potato",0,0,5,0,0),
                new Parts.Tip("minecraft:gold_nugget",0,5,5,0,0),
                new Parts.Tip("minecraft:redstone",6,2,0,2,0),
                new Parts.Tip("minecraft:glass",2,0,2,0,6),

                //t2 10/10 | 10/5/5 | 5/5/5/5 | 4/4/4/4/4
                new Parts.Tip("minecraft:ghast_tear",5,0,5,5,5),
                new Parts.Tip("minecraft:quartz",5,5,10,0,0 ),
                new Parts.Tip("minecraft:emerald",0,5,5,0,10),
                new Parts.Tip("minecraft:redstone_block",10,0,0,10,0),
                new Parts.Tip("minecraft:crimson_planks",5,5,0,10,0),
                new Parts.Tip("minecraft:warped_planks",0,0,10,0,10),

                //t3 20/10 | 10/10/10 | 10/10/5/5 | 6/6/6/6/6
                new Parts.Tip("minecraft:shulker_shell",10,0,0,20,0),
                new Parts.Tip("minecraft:prismarine_crystals",0,0,0,0,0),
                new Parts.Tip("minecraft:nether_star",0,0,0,0,0)
        );
        // 5 points per level
        //Stat Name || Base stat || per point spent
        //damage   1   +1
        //cooldown 20  -5
        //cost     50  -5
        //crit     0   +0.01f
        parts.addHandles(
                //t1 3/2 | 3/1/1 | 2/1/1/1
                new Parts.Handle("minecraft:oak_planks",2,1,1,1),
                new Parts.Handle("minecraft:spruce_planks",1,2,1, 1),
                new Parts.Handle("minecraft:birch_planks",1,1,2,1),
                new Parts.Handle("minecraft:jungle_planks",1,1,1,2),
                new Parts.Handle("minecraft:acacia_planks",1,3,0,1),
                new Parts.Handle("minecraft:dark_oak_planks",1,0,3,1),
                new Parts.Handle("minecraft:kelp",0,2,3,0),
                new Parts.Handle("minecraft:glass",3,0,0,2),

                //t2 5/5 | 6/2/2 | 3/3/2/2
                new Parts.Handle("minecraft:gold_ingot",3,3,2,2),
                new Parts.Handle("minecraft:iron_ingot",2,2,3,3),
                new Parts.Handle("minecraft:leather",2,6,2,0),
                new Parts.Handle("minecraft:quartz",5,0,5,0),
                new Parts.Handle("minecraft:emerald",2,0,2,6),
                new Parts.Handle("minecraft:crimson_planks",5,5,0,0),
                new Parts.Handle("minecraft:warped_planks",0,0,5,5),

                //t3 10/5 | 6/6/3 | 6/3/3/3
                new Parts.Handle("minecraft:shulker_shell",3,6,6,0),
                new Parts.Handle("minecraft:prismarine_shard",5,0,10,0)
        );
    }
    @Override
    public void onInitialize() {
        Registrar.regBlock("timed_light_",Declarar.TIMED_LIGHT,MODID);
        Registrar.regItem("iconicwand_",Declarar.ICONIC_WAND,MODID);
        Registrar.regBlock("wand_bench_",Declarar.WAND_BENCH,MODID);
        Registrar.regItem("wand_bench_",Declarar.WAND_BENCH_ITEM,MODID);
        Registrar.regBlock("wand_pedestal_",Declarar.WAND_PEDESTAL,MODID);
        Registrar.regItem("wand_pedestal_",Declarar.WAND_PEDESTAL_ITEM,MODID);
        Registrar.regBlock("wand_display_pedestal_",Declarar.WAND_PEDESTAL_DISPLAY,MODID);
        Registrar.regItem("wand_display_pedestal_",Declarar.WAND_PEDESTAL_DISPLAY_ITEM,MODID);

        Registrar.regItem("wand_tester_",Declarar.WAND_TESTER,MODID);
        Registrar.regItem("wand_preset_tester_",Declarar.WAND_PRESET_TESTER,MODID);

//        Registrar.regItem("overworld_wand_preset_",Declarar.OVERWORLD_WAND_REI,MODID);
//        Registrar.regItem("nether_wand_preset_",Declarar.NETHER_WAND_REI,MODID);
//        Registrar.regItem("end_wand_preset_",Declarar.END_WAND_REI,MODID);
//        Registrar.regItem("food_wand_preset_",Declarar.FOOD_WAND_REI,MODID);
//        Registrar.regItem("forest_wand_preset_",Declarar.FOREST_WAND_REI,MODID);
//        Registrar.regItem("magus_wand_preset_",Declarar.MAGUS_WAND_REI,MODID);
//        Registrar.regItem("boomstick_wand_preset_",Declarar.BOOMSTICK_WAND_REI,MODID);

        Declarar.WANDING = ScreenHandlerRegistry.registerSimple(Iconicwands.getId("wand_crafting"),((syncId, inventory) -> new WandBenchScreenHandler(syncId,inventory,new SimpleInventory(4))));

        try {
            parts = ConfigFileHandler.initConfigFile("iconic_wands.json",parts);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String MODID = "iconicwands";

    public static Identifier getId(String name) {
        return new Identifier(MODID, name);
    }


}

//todo Fix REI related crash?
//todo crafting noise
//todo move model generation to reload listener
//todo make texture generation check to see if it has changed somehow?
//todo more iconics
