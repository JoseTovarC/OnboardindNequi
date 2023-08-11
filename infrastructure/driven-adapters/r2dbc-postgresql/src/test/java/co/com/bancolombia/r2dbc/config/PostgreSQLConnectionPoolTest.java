package co.com.bancolombia.r2dbc.config;

import co.com.bancolombia.r2dbc.config.PostgreSQLConnectionPool;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PostgreSQLConnectionPoolTest {



    // TODO: change four you own tests
    @Test
    void getConnectionConfig() {
        PostgreSQLConnectionPool postgreSQLConnectionPool= new PostgreSQLConnectionPool();

        PostgresqlConnectionProperties properties = new PostgresqlConnectionProperties();
        properties.setDatabase("test");
        properties.setSchema("testschema");
        properties.setUsername("usertest");
        properties.setPassword("userpass");
        properties.setHost("localhost");
        properties.setPort(1);

        Assertions.assertNotNull(postgreSQLConnectionPool.buildConnectionConfiguration(properties));
    }
}
