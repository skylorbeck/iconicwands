package website.skylorbeck.minecraft.iconicwands;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import website.skylorbeck.minecraft.iconicwands.config.Parts;
import website.skylorbeck.minecraft.skylorlib.ConfigFileHandler;
import website.skylorbeck.minecraft.skylorlib.Registrar;

import java.io.IOException;

public class Iconicwands implements ModInitializer {
    public static Parts parts = new Parts();
    static {
        parts.addCores(
                new Parts.Core(
                        "minecraft:stick",
                        20,
                        200,
                        5,
                        10,
                        10
                ),new Parts.Core(
                        "minecraft:blaze_rod",
                        10,
                        100,
                        10,
                        20,
                        10
                ),new Parts.Core(
                        "minecraft:end_rod",
                        100,
                        100,
                        50,
                        10,
                        10
                )
        );
        parts.addTips(
                new Parts.Tip(
                        "minecraft:glow_berries",
                        1,
                        5,
                        50,
                        1
                ),
                new Parts.Tip(
                        "minecraft:ghast_tear",
                        1,
                        75,
                        25,
                        2
                ),
                new Parts.Tip(
                        "minecraft:shulker_shell",
                        2,
                        25,
                        75,
                        1.5f
                )
        );
        parts.addHandles(
                new Parts.Handle(
                        "minecraft:iron_nugget",
                        1,
                        10,
                        50
                ),
                new Parts.Handle(
                        "minecraft:gold_nugget",
                        1,
                        5,
                        25
                ),
                new Parts.Handle(
                        "minecraft:shulker_shell",
                        2,
                        10,
                        50
                )
        );
    }
    @Override
    public void onInitialize() {
        Registrar.regItem("iconicwand_",Declarar.ICONIC_WAND,MODID);

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
