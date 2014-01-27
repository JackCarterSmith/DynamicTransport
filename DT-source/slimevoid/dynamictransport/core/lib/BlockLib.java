package slimevoid.dynamictransport.core.lib;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;

public class BlockLib {

	public static final int		BLOCK_MAX_TILES				= 3;
	public static final int		BLOCK_ELEVATOR_ID			= 0;
	public static final int		BLOCK_ELEVATOR_COMPUTER_ID	= 1;
	public static final int		BLOCK_DYNAMIC_MARK_ID		= 2;

	private static final String	PREFIX						= "dt";

	public static final String	BLOCK_TRANSPORT_BASE		= PREFIX + "base";

	private static final String	BLOCK_TRANSPORT_PREFIX		= PREFIX
																+ ".transport";
	public static final String	BLOCK_ELEVATOR				= BLOCK_TRANSPORT_PREFIX
																+ ".elevator";
	public static final String	BLOCK_ELEVATOR_COMPUTER		= BLOCK_TRANSPORT_PREFIX
																+ ".computer";
	public static final String	BLOCK_DYNAMIC_MARK			= BLOCK_TRANSPORT_PREFIX
																+ ".marker";
	public static final String	ITEM_ELEVATOR_TOOL			= PREFIX
																+ ".elevatortool";
	public static final int		BLOCK_TRANSIT_ID			= 0;

	public static Icon[][] registerIcons(IconRegister iconRegister, Icon[][] iconList) {
		iconList[BLOCK_ELEVATOR_ID][1] = iconRegister.registerIcon(CoreLib.MOD_RESOURCES
																	+ ":"
																	+ BLOCK_ELEVATOR
																	+ ".top");
		iconList[BLOCK_ELEVATOR_ID][0] = iconRegister.registerIcon(CoreLib.MOD_RESOURCES
																	+ ":"
																	+ BLOCK_ELEVATOR
																	+ ".bottom");
		iconList[BLOCK_ELEVATOR_ID][2] = iconRegister.registerIcon(CoreLib.MOD_RESOURCES
																	+ ":"
																	+ BLOCK_ELEVATOR
																	+ ".side");
		iconList[BLOCK_ELEVATOR_ID][3] = iconRegister.registerIcon(CoreLib.MOD_RESOURCES
																	+ ":"
																	+ BLOCK_ELEVATOR
																	+ ".side");
		iconList[BLOCK_ELEVATOR_ID][4] = iconRegister.registerIcon(CoreLib.MOD_RESOURCES
																	+ ":"
																	+ BLOCK_ELEVATOR
																	+ ".side");
		iconList[BLOCK_ELEVATOR_ID][5] = iconRegister.registerIcon(CoreLib.MOD_RESOURCES
																	+ ":"
																	+ BLOCK_ELEVATOR
																	+ ".side");
		iconList[BLOCK_ELEVATOR_COMPUTER_ID][1] = iconRegister.registerIcon(CoreLib.MOD_RESOURCES
																			+ ":"
																			+ BLOCK_ELEVATOR_COMPUTER
																			+ ".top");
		iconList[BLOCK_ELEVATOR_COMPUTER_ID][0] = iconRegister.registerIcon(CoreLib.MOD_RESOURCES
																			+ ":"
																			+ BLOCK_ELEVATOR_COMPUTER
																			+ ".bottom");
		iconList[BLOCK_ELEVATOR_COMPUTER_ID][2] = iconRegister.registerIcon(CoreLib.MOD_RESOURCES
																			+ ":"
																			+ BLOCK_ELEVATOR_COMPUTER
																			+ ".side");
		iconList[BLOCK_ELEVATOR_COMPUTER_ID][3] = iconRegister.registerIcon(CoreLib.MOD_RESOURCES
																			+ ":"
																			+ BLOCK_ELEVATOR_COMPUTER
																			+ ".side");
		iconList[BLOCK_ELEVATOR_COMPUTER_ID][4] = iconRegister.registerIcon(CoreLib.MOD_RESOURCES
																			+ ":"
																			+ BLOCK_ELEVATOR_COMPUTER
																			+ ".side");
		iconList[BLOCK_ELEVATOR_COMPUTER_ID][5] = iconRegister.registerIcon(CoreLib.MOD_RESOURCES
																			+ ":"
																			+ BLOCK_ELEVATOR_COMPUTER
																			+ ".side");
		iconList[BLOCK_DYNAMIC_MARK_ID][1] = iconRegister.registerIcon(CoreLib.MOD_RESOURCES
																		+ ":"
																		+ BLOCK_DYNAMIC_MARK
																		+ ".top");
		iconList[BLOCK_DYNAMIC_MARK_ID][0] = iconRegister.registerIcon(CoreLib.MOD_RESOURCES
																		+ ":"
																		+ BLOCK_DYNAMIC_MARK
																		+ ".bottom");
		iconList[BLOCK_DYNAMIC_MARK_ID][2] = iconRegister.registerIcon(CoreLib.MOD_RESOURCES
																		+ ":"
																		+ BLOCK_DYNAMIC_MARK
																		+ ".side");
		iconList[BLOCK_DYNAMIC_MARK_ID][3] = iconRegister.registerIcon(CoreLib.MOD_RESOURCES
																		+ ":"
																		+ BLOCK_DYNAMIC_MARK
																		+ ".side");
		iconList[BLOCK_DYNAMIC_MARK_ID][4] = iconRegister.registerIcon(CoreLib.MOD_RESOURCES
																		+ ":"
																		+ BLOCK_DYNAMIC_MARK
																		+ ".side");
		iconList[BLOCK_DYNAMIC_MARK_ID][5] = iconRegister.registerIcon(CoreLib.MOD_RESOURCES
																		+ ":"
																		+ BLOCK_DYNAMIC_MARK
																		+ ".side");
		return iconList;
	}
}
