package com.stockmanagement.integration;

import com.stockmanagement.domain.assets.AssetType;
import com.stockmanagement.restapi.dto.AssetDTO;
import org.junit.jupiter.api.*;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author frfontoura
 * @version 1.0 11/02/2020
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AssetControllerIntegrationTest extends AbstractIntegrationTest {

    private final String BASE_URL = "/api/users/1/portfolios/1/assets";

    @Order(1)
    @Test
    @DisplayName("should create a new portfolio for the logged in user")
    public void addAssetToUserPortfolioTest() throws Exception{
        final AssetDTO assetDTO = createAsset();

        final MvcResult mvcResult = getMockMvc().perform(post(BASE_URL)
                .cookie(token)
                .contentType("application/json")
                .content(getObjectMapper().writeValueAsString(assetDTO)))
                .andExpect(status().isCreated())
                .andReturn();

        final String actual = mvcResult.getResponse().getContentAsString();
        assertEquals(getExpectationsAsString("assetIntegrationTest_addAssetToUserPortfolioTest.json"), actual);
    }

    @Order(2)
    @Test
    @DisplayName("should list all assets from user's portfolio")
    public void findByUserPortfolioTest() throws Exception{
        final MvcResult mvcResult = getMockMvc().perform(get(BASE_URL)
                .cookie(token)
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();

        final String actual = mvcResult.getResponse().getContentAsString();
        assertEquals(getExpectationsAsString("assetIntegrationTest_findByUserPortfolioTest.json"), actual);
    }

    @Order(3)
    @Test
    @DisplayName("should partially sell the assets")
    public void assetSalePartialTest() throws Exception{
        final AssetDTO assetDTO = createAsset();
        assetDTO.setAmount(15);

        final MvcResult mvcResult = getMockMvc().perform(put(BASE_URL + "/1")
                .cookie(token)
                .contentType("application/json")
                .content(getObjectMapper().writeValueAsString(assetDTO)))
                .andExpect(status().isOk())
                .andReturn();

        final String actual = mvcResult.getResponse().getContentAsString();
        assertEquals(getExpectationsAsString("assetIntegrationTest_assetSalePartialTest.json"), actual);
    }

    @Order(4)
    @Test
    @DisplayName("should sell the rest of the assets")
    public void assetSaleFullTest() throws Exception{
        final AssetDTO assetDTO = createAsset();
        assetDTO.setAmount(85);

        final MvcResult mvcResult = getMockMvc().perform(put(BASE_URL + "/1")
                .cookie(token)
                .contentType("application/json")
                .content(getObjectMapper().writeValueAsString(assetDTO)))
                .andExpect(status().isOk())
                .andReturn();

        final String actual = mvcResult.getResponse().getContentAsString();
        assertEquals("", actual);
    }

    @Override
    public String sqlFile(){
        return "db/tests/assetControllerIntegrationTest.sql";
    }

    private AssetDTO createAsset() {
        return AssetDTO.builder()
                .symbol("BCFF11")
                .amount(100)
                .lastPrice(new BigDecimal(112.56))
                .type(AssetType.REIT)
                .lastDate(LocalDate.of(2020, 02, 11))
                .build();
    }
}
