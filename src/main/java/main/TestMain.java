package main;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestMain 
{
	public static void main(String[] args)
	{
		String X = ".*?";
		Pattern transform = Pattern.compile(X + "of" + X + "uses Transformation: Guardian General" + X + "in" + X);

		String line = 	"2015.12.11 23:16:44 : Noariza of Elyos uses Transformation: Guardian General I in Vorgaltem Citadel.";
		
		long start = System.currentTimeMillis();
		if(line.contains("uses Transformation: Guardian General") && line.contains("in"))
		{
			System.out.println("yep");
		}
		long end = System.currentTimeMillis() - start;
		System.out.println("total time: " + end);
		
		
		
		long start2 = System.currentTimeMillis();
		Matcher matcher = transform.matcher(line);
		if (matcher.matches()) 
		{
			System.out.println("yep");
		}	
		
		long end2 = System.currentTimeMillis() - start2;
		System.out.println("total time: " + end2);

	}
}
