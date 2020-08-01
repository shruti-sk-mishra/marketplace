package com.shr.marketplace.repositories;

import com.shr.marketplace.config.mongo.BaseRepositoryTest;
import com.shr.marketplace.models.Buyer;
import io.github.glytching.junit.extension.random.Random;
import io.github.glytching.junit.extension.random.RandomBeansExtension;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Unit test cases
 * for BuyerRepository
 *
 * @author shruti.mishra
 */
@SpringBootTest
@ExtendWith(RandomBeansExtension.class)
class BuyerRepositoryTest extends BaseRepositoryTest {

    @Autowired
    private BuyerRepository buyerRepository;

    @Test
    void shouldCreateBuyer(@Random Buyer buyer) {

        final var savedBuyer = buyerRepository.create(buyer);

        assertNotNull(savedBuyer.getId());
        assertNotNull(buyerRepository.findById(savedBuyer.getId()));
    }

    @Test
    void shouldGetBuyerById(@Random Buyer buyer) {

        final var savedBuyer = buyerRepository.create(buyer);
        assertNotNull(savedBuyer.getId());
        assertNotNull(buyerRepository.findById(savedBuyer.getId()));
    }

    @Test
    void shouldGetBuyerByName() {

        final var buyerName = "Buyer's name";
        final var buyer = new Buyer(buyerName);

        final var savedBuyer = buyerRepository.create(buyer);
        assertNotNull(savedBuyer.getId());
        assertNotNull(buyerRepository.findByName(savedBuyer.getName()));
    }

    @Test
    void shouldUpdateBuyer() throws IllegalAccessException {

        final var buyer = new Buyer("Buyer's name");
        final var savedBuyer = buyerRepository.create(buyer);

        assertNotNull(savedBuyer.getId());
        assertThat(savedBuyer.getName(), is(buyer.getName()));

        final var buyersNewName = "Buyer's new name";

        FieldUtils.getField(Buyer.class, "name", true)
                .set(savedBuyer, buyersNewName);


        buyerRepository.update(savedBuyer);

        final var retrievedBuyer = buyerRepository.findById(savedBuyer.getId()).orElseThrow();;
        assertThat(retrievedBuyer.getName(), is(buyersNewName));
    }
}
