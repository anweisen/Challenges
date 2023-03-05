import net.anweisen.utilities.common.collection.IRandom;

public class Main
{

	public static void main(String[] args)
	{
		for (int i = 0; i < 100; i++)
		{
			System.out.println(IRandom.threadLocal().nextInt(3));
		}
	}

}
