package pl.xyundy.squaredadditions;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.xyundy.squaredadditions.item.ModItems;

public class SquaredAdditions implements ModInitializer {
	public static final String MOD_ID = "squaredadditions";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
  LOGGER.info("SquaredAdditions initialization started!");
		ModItems.registerModItems();
		LOGGER.info("SquaredAdditions initialization completed!");
	}
}