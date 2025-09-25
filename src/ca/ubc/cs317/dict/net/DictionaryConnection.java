package ca.ubc.cs317.dict.net;

import ca.ubc.cs317.dict.model.Database;
import ca.ubc.cs317.dict.model.Definition;
import ca.ubc.cs317.dict.model.MatchingStrategy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;

/**
 * Created by Jonatan on 2017-09-09.
 */
public class DictionaryConnection {

    private static final int DEFAULT_PORT = 2628;
    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;
    private String host;
    private int port;
    private boolean connected;

    /** Establishes a new connection with a DICT server using an explicit host and port number, and handles initial
     * welcome messages.
     *
     * @param host Name of the host where the DICT server is running
     * @param port Port number used by the DICT server
     * @throws DictConnectionException If the host does not exist, the connection can't be established, or the messages
     * don't match their expected value.
     */
    public DictionaryConnection(String host, int port) throws DictConnectionException {
        try{
            socket = new Socket(host, port);

            //writes to server
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            //listens to serve
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.input = in;
            this.output = out;

            String welcomeMessage = in.readLine();
            if (welcomeMessage == null) {
                throw new DictConnectionException("No welcome message received from server");
            }

            //220 dict.dict.org dictd 1.12.1/rf on Linux 4.19.0-10-amd64 <auth.mime> <547903076.14484.1758085096@dict.dict.org>
            if (!welcomeMessage.startsWith("220")) {
                throw new DictConnectionException("Unexpected welcome message: " + welcomeMessage);
            }

            this.host = host;
            this.port = port;
            this.connected = true;
        } catch (Exception e) {
            throw new DictConnectionException("Not implemented");
        }
    }

    /** Establishes a new connection with a DICT server using an explicit host, with the default DICT port number, and
     * handles initial welcome messages.
     *
     * @param host Name of the host where the DICT server is running
     * @throws DictConnectionException If the host does not exist, the connection can't be established, or the messages
     * don't match their expected value.
     */
    public DictionaryConnection(String host) throws DictConnectionException {
        this(host, DEFAULT_PORT);
    }

    /** Sends the final QUIT message and closes the connection with the server. This function ignores any exception that
     * may happen while sending the message, receiving its reply, or closing the connection.
     *
     */
    public synchronized void close() {
        try {
            if (output != null) {
                output.println("QUIT");
                //221 bye [d/m/c = 0/0/0; 127.000r 0.000u 0.000s]
                output.flush();
            }
            if (input != null) {
                input.readLine();
            }
        } catch (Exception e) {
            // Ignore
        } finally {
            try { if (input != null) input.close(); } catch (Exception e) { }
            try { if (output != null) output.close(); } catch (Exception e) { }
            try { if (socket != null) socket.close(); } catch (Exception e) { }
            connected = false;
        }
    }

    /** Requests and retrieves all definitions for a specific word.
     *
     * @param word The word whose definition is to be retrieved.
     * @param database The database to be used to retrieve the definition. A special database may be specified,
     *                 indicating either that all regular databases should be used (database name '*'), or that only
     *                 definitions in the first database that has a definition for the word should be used
     *                 (database '!').
     * @return A collection of Definition objects containing all definitions returned by the server.
     * @throws DictConnectionException If the connection was interrupted or the messages don't match their expected value.
     */
    public synchronized Collection<Definition> getDefinitions(String word, Database database) throws DictConnectionException {
        Collection<Definition> set = new ArrayList<>();
        try {
            if (output != null) {
                output.println("define " + database.getName() + " " + word);
                output.flush();
            }

            String response = input.readLine();
            if (response == null) {
                throw new DictConnectionException("No response received from server");
            }

            if (!response.startsWith("150")) {
                throw new DictConnectionException("Unexpected response: " + response);
            }




        } catch (Exception e) {
            throw new DictConnectionException("Not implemented");
        }
        // TODO Add your code here

        return set;
    }

    /** Requests and retrieves a list of matches for a specific word pattern.
     *
     * @param word     The word whose definition is to be retrieved.
     * @param strategy The strategy to be used to retrieve the list of matches (e.g., prefix, exact).
     * @param database The database to be used to retrieve the definition. A special database may be specified,
     *                 indicating either that all regular databases should be used (database name '*'), or that only
     *                 matches in the first database that has a match for the word should be used (database '!').
     * @return A set of word matches returned by the server.
     * @throws DictConnectionException If the connection was interrupted or the messages don't match their expected value.
     */
    public synchronized Set<String> getMatchList(String word, MatchingStrategy strategy, Database database) throws DictConnectionException {
        Set<String> set = new LinkedHashSet<>();

        // TODO Add your code here

        return set;
    }

    /** Requests and retrieves a map of database name to an equivalent database object for all valid databases used in the server.
     *
     * @return A map of Database objects supported by the server.
     * @throws DictConnectionException If the connection was interrupted or the messages don't match their expected value.
     */
    public synchronized Map<String, Database> getDatabaseList() throws DictConnectionException {
        Map<String, Database> databaseMap = new HashMap<>();
        List<String> response = new ArrayList<>();
        try {
            if (output != null) {
                output.println("SHOW DATABASES");
                output.flush();
            }

            String firstline = input.readLine();
            if (firstline == null || !firstline.startsWith("110")) {
                throw new DictConnectionException("unexpected response: " + firstline);
            }

            String line;
            while ((line = input.readLine()) != null) {
                if (line.startsWith("250")) {
                    break;
                }
                response.add(line);
            }

            for (String s : response) {
                s = s.trim();
                if (s.matches("^[^ ]+\\s+\".*\"$")) {
                    String[] split = s.split("\\s+", 2);
                    String dbName = split[0];
                    String dbDescription = split[1].replaceAll("^\"|\"$", ""); //remove "" at beginning and end
                    databaseMap.put(dbName, new Database(dbName, dbDescription));
                }
            }

        } catch (IOException e) {
            throw new DictConnectionException("Error communicating with server", e);
        }

        return databaseMap;
    }

    /** Requests and retrieves a list of all valid matching strategies supported by the server.
     *
     * @return A set of MatchingStrategy objects supported by the server.
     * @throws DictConnectionException If the connection was interrupted or the messages don't match their expected value.
     */
    public synchronized Set<MatchingStrategy> getStrategyList() throws DictConnectionException {
        Set<MatchingStrategy> set = new LinkedHashSet<>();

        // TODO Add your code here

        return set;
    }

    /** Requests and retrieves detailed information about the currently selected database.
     *
     * @return A string containing the information returned by the server in response to a "SHOW INFO <db>" command.
     * @throws DictConnectionException If the connection was interrupted or the messages don't match their expected value.
     */
    public synchronized String getDatabaseInfo(Database d) throws DictConnectionException {
	StringBuilder sb = new StringBuilder();

        try {
            if (output != null) {
                output.println("SHOW INFO " + d.getName());
                output.flush();
            }

            String firstline = input.readLine();
            if (firstline == null || !firstline.startsWith("112")) {
                throw new DictConnectionException("unexpected response: " + firstline);
            }

            String line;
            while ((line = input.readLine()) != null) {
                if (line.startsWith("250")) {
                    break;
                }
                sb.append(line).append("\n");
            }

            if (sb.length() > 0) {
                sb.setLength(sb.length() - 1);
            }

        } catch (IOException e) {
            throw new DictConnectionException("Error communicating with server", e);
        }

        return sb.toString();
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public boolean isConnected() {
        return connected;
    }

    public BufferedReader getInput() {
        return input;
    }

    public PrintWriter getOutput() {
        return output;
    }

    public Socket getSocket() {
        return socket;
    }
}
