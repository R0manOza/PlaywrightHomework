package ge.tbc.testautomation.steps;

import com.microsoft.playwright.Page;
import ge.tbc.testautomation.pages.MagentoWishListPage;
import org.testng.Assert;

public class MagentoWishlistSteps {
    private final Page page;
    private final MagentoWishListPage wishlistPage;

    public MagentoWishlistSteps(Page page) {
        this.page = page;
        this.wishlistPage = new MagentoWishListPage(page);
    }

    public MagentoWishlistSteps navigateToWishlist() {
        page.navigate("https://magento.softwaretestingboard.com/wishlist/");
        return this;
    }

    public MagentoWishlistSteps verifyItemIsPresentInWishlist(String productName) {
        wishlistPage.wishlistItem.first().waitFor();
        int itemCount = wishlistPage.wishlistItem
                .filter(new com.microsoft.playwright.Locator.FilterOptions().setHasText(productName))
                .count();
        Assert.assertTrue(itemCount > 0, "Product '" + productName + "' should be in the wishlist.");
        System.out.println("Verification: '" + productName + "' is present in the wishlist.");
        return this;
    }

    public MagentoWishlistSteps addItemFromWishlistToCart(String productName) {
        page.locator("button[data-role='all-tocart']").click();
        System.out.println("Step: Added '" + productName + "' from wishlist to cart.");
        return this;
    }
}