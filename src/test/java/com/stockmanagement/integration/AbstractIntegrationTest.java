package com.stockmanagement.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stockmanagement.infra.security.JwtRequest;
import com.stockmanagement.integration.utils.TestClassLoaderResourceAccessor;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.sql.DataSource;
import java.sql.Connection;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author frfontoura
 * @version 1.0 12/01/2020
 */
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public abstract class AbstractIntegrationTest {

    private final String EXPECTATIONS_PATH = "expectations/";
    private static boolean liquibaseExecuted = false;
    protected static String token;

    @Getter
    @Autowired
    private MockMvc mockMvc;

    @Getter
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    public static void beforeAll() {
        liquibaseExecuted = false;
    }

    @SneakyThrows
    @BeforeEach
    public void init(@Autowired final DataSource dataSource) {
        if(!liquibaseExecuted) {
            final Connection connection = dataSource.getConnection();
            final Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            final Liquibase liquibase = new liquibase.Liquibase("db/changelog/db.changelog-test.xml", new TestClassLoaderResourceAccessor(sqlFile()), database);
            liquibase.dropAll();
            liquibase.update("");
            liquibaseExecuted = true;

            login();
        }
    }

    /**
     * If the test needs to run an initial script, this method must be overridden. The default path for the scripts should be "db/tests/{nameOfIntegrationTestClass}.sql"
     *
     * @return
     */
    public String sqlFile(){
        return null;
    }

    @SneakyThrows
    public void login() {
        final JwtRequest jwtRequest = new JwtRequest("starlord","imgroot");
        final String content = objectMapper.writeValueAsString(jwtRequest);

        final MvcResult mvcResult = mockMvc.perform(post("/login")
                .contentType("application/json")
                .content(content))
                .andExpect(status().isOk())
                .andReturn();

        token = mvcResult.getResponse().getHeader("Authorization");
    }

    @SneakyThrows
    public String getExpectationsAsString(final String file) {
        return FileUtils.readFileToString(new ClassPathResource(EXPECTATIONS_PATH + file).getFile(), "utf8");
    }
}
