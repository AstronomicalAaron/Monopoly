package cs414.a4.n.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import junit.framework.JUnit4TestAdapter;

// declare all test classes in the program
@RunWith (Suite.class)
@Suite.SuiteClasses({BankTest.class, BoardTest.class, DieTest.class, MonopolyTest.class, OwnerTest.class , PlayerTest.class, TileTest.class, 
	TokenTest.class})
 
public class TestAll 
{
	/**
	 * Runs all test and indicates failure of any
	 * @param args
	 */
	public static void main(String[] args)
	{
		junit.textui.TestRunner.run(suite());
	}
	
	/**
	 * create a TestAdapter
	 * @return
	 */
	public static junit.framework.Test suite()
	{
		return new JUnit4TestAdapter(TestAll.class);
	}

}
