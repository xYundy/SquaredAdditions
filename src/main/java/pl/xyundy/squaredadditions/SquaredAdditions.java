package pl.xyundy.squaredadditions;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.xyundy.squaredadditions.block.ModBlocks;
import pl.xyundy.squaredadditions.item.ModItems;
import pl.xyundy.squaredadditions.slabs.Slabs;

public class SquaredAdditions implements ModInitializer {
	public static final String MOD_ID = "squaredadditions";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
  		LOGGER.info("SquaredAdditions initialization started!");
		Slabs.initialize();
		ModItems.registerModItems();
		ModBlocks.registerModBlocks();
		LOGGER.info("SquaredAdditions initialization completed!");
	}
}