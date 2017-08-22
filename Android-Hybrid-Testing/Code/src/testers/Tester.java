package testers;

import java.util.ArrayList;

import org.openqa.selenium.WebElement;

public interface Tester {
	public void test(ArrayList<WebElement> possibleTargets);
}
