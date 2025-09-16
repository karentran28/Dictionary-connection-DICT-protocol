package ca.ubc.cs317.dict.net;

import ca.ubc.cs317.dict.model.Database;
import ca.ubc.cs317.dict.model.Definition;
import ca.ubc.cs317.dict.model.MatchingStrategy;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class DictionaryConnectionTest {
    @Test
    public void testBasicConnection() throws DictConnectionException {
        DictionaryConnection conn = new DictionaryConnection("dict.org");
        assertNotNull(conn);
    }

    @Test
    public void testGetDatabaseList() throws DictConnectionException {
        DictionaryConnection conn = new DictionaryConnection("dict.org");
        Map<String, Database> dbl = conn.getDatabaseList();
        assertTrue(dbl.size() > 0);
    }

    @Test
    public void testGetDefinition() throws DictConnectionException {
        DictionaryConnection conn = new DictionaryConnection("dict.org");
        Map<String, Database> dbl = conn.getDatabaseList();
        assertTrue(dbl.size() > 0);
        Database wn = dbl.get("wn");
        assertNotNull(wn);
        Collection<Definition> defs = conn.getDefinitions("parrot", wn);
        assertTrue(defs.size() > 0);
    }

    @Test
    public void testGetMatchingStrategies() throws DictConnectionException {
        DictionaryConnection conn = new DictionaryConnection("dict.org");
        Set<MatchingStrategy> strategies = conn.getStrategyList();
        MatchingStrategy mockST = new MatchingStrategy("exact", "Match headwords exactly");
        assertTrue(!strategies.isEmpty());
        assertTrue(strategies.contains(mockST));
    }

    @Test
    public void testValidConnection() {
        assertDoesNotThrow(() -> {
            DictionaryConnection conn = new DictionaryConnection("dict.org", 2628);
            // Add assertions to verify connection state
            assertNotNull(conn);
            assertEquals("dict.org", conn.getHost());
            assertEquals(2628, conn.getPort());
        });
    }

    @Test
    public void testCloseConnection() {
        assertDoesNotThrow(() -> {
            DictionaryConnection conn = new DictionaryConnection("dict.org", 2628);
            assertNotNull(conn);
            assertEquals("dict.org", conn.getHost());
            assertEquals(2628, conn.getPort());

            conn.close();
            assertTrue(conn.getSocket().isClosed());
            assertFalse(conn.isConnected());
        });
    }
}


