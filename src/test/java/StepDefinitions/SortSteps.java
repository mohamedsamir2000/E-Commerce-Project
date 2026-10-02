package StepDefinitions;

import Context.TestContext;
import Pages.HomePage;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SortSteps {

    private final HomePage homePage;

    public SortSteps(TestContext context) {
        this.homePage = context.getHomePage();
    }

    @When("the user sorts the items by {string}")
    public void theUserSortsTheItemsBy(String sortOption) {
        homePage.sortBy(sortOption);
    }

    @Then("the items should be sorted by price from low to high")
    public void itemsSortedByPriceLowToHigh() {
        assertSorted(homePage.getItemPrices(), Comparator.naturalOrder(), "Items are not sorted correctly from Low to High");
    }

    @Then("the items should be sorted by price from high to low")
    public void itemsSortedByPriceHighToLow() {
        assertSorted(homePage.getItemPrices(), Comparator.reverseOrder(), "Items are not sorted correctly from High to Low");
    }

    @Then("the items should be sorted by name from A to Z")
    public void itemsSortedByNameAToZ() {
        assertSorted(homePage.getItemNames(), Comparator.naturalOrder(), "Sorting from A to Z failed!");
    }

    @Then("the items should be sorted by name from Z to A")
    public void itemsSortedByNameZToA() {
        assertSorted(homePage.getItemNames(), Comparator.reverseOrder(), "Sorting from Z to A failed!");
    }

    private static <T> void assertSorted(List<T> actual, Comparator<? super T> order, String message) {
        List<T> expected = new ArrayList<>(actual);
        expected.sort(order);
        Assert.assertEquals(actual, expected, message);
    }
}
