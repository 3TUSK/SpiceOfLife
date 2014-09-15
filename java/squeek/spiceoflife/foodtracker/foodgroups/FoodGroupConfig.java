package squeek.spiceoflife.foodtracker.foodgroups;

import java.io.*;
import org.apache.commons.io.FilenameUtils;
import squeek.spiceoflife.ModInfo;
import squeek.spiceoflife.ModSpiceOfLife;
import squeek.spiceoflife.helpers.FileHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class FoodGroupConfig
{
	private static final Gson gson = new GsonBuilder().enableComplexMapKeySerialization().setPrettyPrinting().create();
	private static File[] configFiles;

	public static void setup(File configDirectory)
	{
		File modConfigDirectory = new File(configDirectory, ModInfo.MODID);
		if (!modConfigDirectory.exists())
		{
			modConfigDirectory.mkdirs();
		}
		writeExampleFoodGroup(modConfigDirectory);
		configFiles = modConfigDirectory.listFiles();
	}

	public static void writeExampleFoodGroup(File configDirectory)
	{
		final String exampleFoodGroupFileName = "example-food-group.json";
		File exampleFoodGroupSource = new File(ModSpiceOfLife.instance.sourceFile, "example/" + exampleFoodGroupFileName);
		File exampleFoodGroupDest = new File(configDirectory, exampleFoodGroupFileName);

		try
		{
			FileHelper.copyFile(exampleFoodGroupSource, exampleFoodGroupDest, shouldOverwriteExampleFoodGroup(exampleFoodGroupDest));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public static boolean shouldOverwriteExampleFoodGroup(File exampleFoodGroup) throws IOException
	{
		FileInputStream exampleFoodGroupStream;
		try
		{
			exampleFoodGroupStream = new FileInputStream(exampleFoodGroup);
		}
		catch (FileNotFoundException e)
		{
			return true;
		}
		BufferedReader exampleFoodGroupReader = new BufferedReader(new InputStreamReader(exampleFoodGroupStream));
		String firstLine = exampleFoodGroupReader.readLine();
		exampleFoodGroupReader.close();

		return !firstLine.equals("// Mod Version: " + ModInfo.VERSION);
	}

	public static void load()
	{
		for (File configFile : configFiles)
		{
			try
			{
				FileReader reader = new FileReader(configFile);
				FoodGroup foodGroup = gson.fromJson(reader, FoodGroup.class);
				foodGroup.identifier = FilenameUtils.removeExtension(configFile.getName());
				foodGroup.initFromConfig();
				reader.close();
				FoodGroupRegistry.addFoodGroup(foodGroup);
			}
			catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

}