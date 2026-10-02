package Pages.Components;

import Pages.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

/**
 * The burger side menu: All Items, About, Logout, Reset App State.
 */
public class SideMenu extends BasePage {

    private final By openButton = By.id("react-burger-menu-btn");
    private final By closeButton = By.id("react-burger-cross-btn");
    private final By menuWrap = By.className("bm-menu-wrap");
    private final By menuItems = By.cssSelector(".bm-item-list a.menu-item");

    public SideMenu(WebDriver driver) {
        super(driver);
    }

    public void open() {
        click(openButton);
        wait.until(ExpectedConditions.attributeToBe(menuWrap, "aria-hidden", "false"));
        waitForSlideToFinish();
    }

    public void close() {
        click(closeButton);
        wait.until(ExpectedConditions.attributeToBe(menuWrap, "aria-hidden", "true"));
        waitForSlideToFinish();
    }

    /** The menu slides in and out; clicks made while it is still moving miss or hit the overlay. */
    private void waitForSlideToFinish() {
        Rectangle[] last = {null};
        wait.until(d -> {
            Rectangle now = d.findElement(menuWrap).getRect();
            boolean settled = now.equals(last[0]);
            last[0] = now;
            return settled;
        });
    }

    public boolean isOpen() {
        return "false".equals(driver.findElement(menuWrap).getAttribute("aria-hidden"));
    }

    public List<String> getItems() {
        return driver.findElements(menuItems).stream()
                .map(e -> e.getAttribute("textContent").trim())
                .toList();
    }

    public void select(String item) {
        By link = switch (item.trim().toLowerCase()) {
            case "all items" -> By.id("inventory_sidebar_link");
            case "about" -> By.id("about_sidebar_link");
            case "logout" -> By.id("logout_sidebar_link");
            case "reset app state" -> By.id("reset_sidebar_link");
            default -> By.linkText(item);
        };
        click(link);
    }
}
