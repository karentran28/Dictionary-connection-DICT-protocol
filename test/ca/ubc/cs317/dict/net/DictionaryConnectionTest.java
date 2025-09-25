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

    @Test
    public void testGetDatabaseInfo() {
        assertDoesNotThrow(() -> {
            DictionaryConnection conn = new DictionaryConnection("dict.org", 2628);
            assertNotNull(conn);
            Database d = new Database("gcide", "The Collaborative International Dictionary of English v.0.48");
            assertNotNull(d);
            String info = conn.getDatabaseInfo(d);
            assertNotNull(info);
            String match = "============ gcide ============\n" +
                    "00-database-info\n" +
                    "   This file was converted from the original database on:\n" +
                    "             Fri Jul 13 11:00:20 2018\n" +
                    "\n" +
                    "   The original data is available from:\n" +
                    "             ftp://ftp.gnu.org/gnu/gcide\n" +
                    "   (However, this archive does not always contain the most\n" +
                    "   recent version of the dictionary.)\n" +
                    "\n" +
                    "The original data was distributed with the notice shown below.\n" +
                    "No additional restrictions are claimed. Please redistribute this\n" +
                    "changed version under the same conditions and restriction that\n" +
                    "apply to the original version.\n" +
                    "\n" +
                    "===============================================================\n" +
                    "\n" +
                    " Begin file 1 of 26:  Letter A (Version 0.48) \n" +
                    "        \n" +
                    "           This file is part 1 of the GNU version of\n" +
                    "     The Collaborative International Dictionary of English\n" +
                    "               Also referred to as GCIDE\n" +
                    "  * * * * * * * * * * * * * * * * * * * * * * * * * * * *\n" +
                    "\n" +
                    "GCIDE is free software; you can redistribute it and/or modify it\n" +
                    "under the terms of the GNU General Public License as published\n" +
                    "by the Free Software Foundation; either version 2, or (at your\n" +
                    "option) any later version.\n" +
                    "\n" +
                    "GCIDE is distributed in the hope that it will be useful, but\n" +
                    "WITHOUT ANY WARRANTY; without even the implied warranty of\n" +
                    "MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the\n" +
                    "GNU General Public License for more details.\n" +
                    "\n" +
                    "You should have received a copy of the GNU General Public\n" +
                    "License along with this copy of GCIDE; see the file COPYING.  If\n" +
                    "not, write to the Free Software Foundation, Inc., 59 Temple\n" +
                    "Place - Suite 330, Boston, MA 02111-1307, USA.\n" +
                    "          * * * * * * * * * * * * * * * * * * * *\n" +
                    "\n" +
                    "           This dictionary was derived from the\n" +
                    "         Webster's Revised Unabridged Dictionary\n" +
                    "                 Version published 1913\n" +
                    "               by the  C. & G. Merriam Co.\n" +
                    "                   Springfield, Mass.\n" +
                    "                 Under the direction of\n" +
                    "                Noah Porter, D.D., LL.D.\n" +
                    "\n" +
                    "                        and from\n" +
                    "           WordNet, a semantic network created by\n" +
                    "              the Cognitive Science Department\n" +
                    "                 of Princeton University\n" +
                    "                  under the direction of\n" +
                    "                   Prof. George Miller\n" +
                    "\n" +
                    "             and is being updated and supplemented by\n" +
                    "         an open coalition of volunteer collaborators from\n" +
                    "                       around the world.\n" +
                    "\n" +
                    "     This electronic dictionary is the starting point for an\n" +
                    "ongoing project to develop a modern on-line comprehensive\n" +
                    "encyclopedic dictionary, by the efforts of all individuals\n" +
                    "willing to help build a large and freely available knowledge\n" +
                    "base.  Contributions of data, time, and effort are requested\n" +
                    "from any person willing to assist creation of a comprehensive\n" +
                    "and organized knowledge base for free access on the internet. \n" +
                    "Anyone willing to assist in any way in constructing such a\n" +
                    "knowledge base should contact:\n" +
                    "\n" +
                    "     Patrick Cassidy          pc@worldsoul.org\n" +
                    "     735 Belvidere Ave.       Office: (908)668-5252\n" +
                    "     Plainfield, NJ 07062\n" +
                    "     (908) 561-3416\n" +
                    "\n" +
                    "\n" +
                    "   Last edit October 6, 2002.\n" +
                    "\n" +
                    " \n" +
                    "\n" +
                    ".";
            assertEquals(match, info);
        });
    }


}


