package org.overture.interpreter.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

public abstract class QuickProfiler
{

	private static String PROFILE_PATH = "generated/profile.csv";
	private static StringBuilder sb = new StringBuilder();

	public static void printDuration(long start, String name)
	{
		sb.append("\"");
		sb.append(name);
		sb.append("\"");
		sb.append(", ");
		sb.append(System.currentTimeMillis() - start);
		sb.append("\n");
	}

	public static void print()
	{
		Writer writer = null;
		try
		{
			File file = new File(PROFILE_PATH);
			if (file.exists())
			{
				file.delete();
			}
			file.getParentFile().mkdirs();

			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
			writer.write(sb.toString());

		} catch (IOException e)
		{
			System.out.println(e.toString());
			e.printStackTrace();
		} finally
		{
			try
			{
				writer.close();
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}
