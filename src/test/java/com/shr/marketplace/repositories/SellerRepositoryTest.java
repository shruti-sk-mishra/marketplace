package com.shr.marketplace.repositories;

import com.shr.marketplace.config.mongo.BaseRepositoryTest;
import com.shr.marketplace.models.Seller;
import io.github.glytching.junit.extension.random.Random;
import io.github.glytching.junit.extension.random.RandomBeansExtension;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Unit test cases
 * for SellerRepository
 *
 * @author shruti.mishra
 */
@SpringBootTest
@ExtendWith(RandomBeansExtension.class)
class SellerRepositoryTest extends BaseRepositoryTest {

    @Autowired
    private SellerRepository sellerRepository;

    @Test
    void shouldCreateSeller(@Random Seller seller) {

        final var savedSeller = sellerRepository.create(seller);

        assertNotNull(savedSeller.getId());
        assertNotNull(sellerRepository.findById(savedSeller.getId()));
    }

    @Test
    void shouldGetSellerById(@Random Seller seller) {

        final var savedSeller = sellerRepository.create(seller);
        assertNotNull(savedSeller.getId());
        assertNotNull(sellerRepository.findById(savedSeller.getId()));
    }

    @Test
    void shouldGetSellerByName() {

        final var sellersName = "Seller's name";
        final var seller = new Seller(sellersName);

        final var savedSeller = sellerRepository.create(seller);
        assertNotNull(savedSeller.getId());
        assertNotNull(sellerRepository.findByName(savedSeller.getName()));
    }

    @Test
    void shouldUpdateSeller() throws IllegalAccessException {

        final var seller = new Seller("Seller's name");
        final var savedSeller = sellerRepository.create(seller);

        assertNotNull(savedSeller.getId());
        assertThat(savedSeller.getName(), is(seller.getName()));

        final var sellersNewName = "Seller's new name";

        FieldUtils.getField(Seller.class, "name", true)
                .set(savedSeller, sellersNewName);


        sellerRepository.update(savedSeller);

        final var retrievedSeller = sellerRepository.findById(savedSeller.getId()).orElseThrow();;
        assertThat(retrievedSeller.getName(), is(sellersNewName));
    }
}
