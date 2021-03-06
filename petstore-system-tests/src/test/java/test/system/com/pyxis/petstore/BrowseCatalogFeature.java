package test.system.com.pyxis.petstore;

import com.pyxis.petstore.domain.product.Item;
import com.pyxis.petstore.domain.product.Product;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import test.support.com.pyxis.petstore.builders.ProductBuilder;
import test.support.com.pyxis.petstore.web.DatabaseDriver;
import test.support.com.pyxis.petstore.web.PetStoreDriver;
import test.system.com.pyxis.petstore.page.HomePage;
import test.system.com.pyxis.petstore.page.ItemsPage;
import test.system.com.pyxis.petstore.page.ProductsPage;

import static test.support.com.pyxis.petstore.builders.ItemBuilder.anItem;
import static test.support.com.pyxis.petstore.builders.ProductBuilder.aProduct;

public class BrowseCatalogFeature {
	
	PetStoreDriver petstore = new PetStoreDriver();
	DatabaseDriver database = new DatabaseDriver();

    HomePage homePage;
    Product product;

    @Before public void
    startApplication() throws Exception {
        database.start();
        homePage = petstore.start();
        given(aProduct().withName("Iguana"));
	}

    private void given(final ProductBuilder product) throws Exception {
        this.product = product.build();
        database.given(this.product);
    }

    @Test public void
    consultsAProductCurrentlyOutOfStock() throws Exception {
        ProductsPage productsPage = homePage.searchFor("Iguana");
        ItemsPage itemsPage = productsPage.browseItemsOf("Iguana");
        itemsPage.showsNoItemAvailable();
    }

	@Test public void
    consultsAProductAvailableItems() throws Exception {
        Item greenAdult = anItem().of(product)
                .withNumber("12345678")
                .describedAs("Green Adult")
                .priced("18.50").build();
        database.given(greenAdult);
        
		ProductsPage productsPage = homePage.searchFor("Iguana");
		ItemsPage itemsPage = productsPage.browseItemsOf("Iguana");
		itemsPage.displaysItem("12345678", "Green Adult", "18.50");
	}
	
	@After public void
    stopApplication() {
		petstore.stop();
		database.stop();
	}
}
