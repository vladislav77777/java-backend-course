package edu.java.scrapper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.DirtiesContext;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Log4j2
@Testcontainers
@DirtiesContext
public class SimpleContainerTest extends IntegrationTest {
    @Test
    public void assertThatTablesExists() {
        try (Connection connection = DriverManager.getConnection(
            POSTGRES.getJdbcUrl(),
            POSTGRES.getUsername(),
            POSTGRES.getPassword()
        );
             Statement statement = connection.createStatement()) {
            ResultSet tableTelegramChatExists = statement.executeQuery(
                "SELECT EXISTS (SELECT FROM pg_tables WHERE schemaname = 'public' AND tablename = 'telegram_chat');");
            tableTelegramChatExists.next();
            assertTrue(tableTelegramChatExists.getBoolean(1));

            ResultSet tableLinkExists = statement.executeQuery(
                "SELECT EXISTS (SELECT FROM pg_tables WHERE schemaname = 'public' AND tablename = 'link');");
            tableLinkExists.next();
            assertTrue(tableLinkExists.getBoolean(1));

            ResultSet tableAssignmentExists = statement.executeQuery(
                "SELECT EXISTS (SELECT FROM pg_tables WHERE schemaname = 'public' AND tablename = 'assignment');");
            tableAssignmentExists.next();
            assertTrue(tableAssignmentExists.getBoolean(1));
        } catch (SQLException e) {
            log.error(e);
        }
    }
}
