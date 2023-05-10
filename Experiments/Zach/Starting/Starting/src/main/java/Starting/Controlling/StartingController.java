package Starting.Controlling;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.DeleteMapping;
import java.sql.*;
import Starting.Users.*;

@RestController
class StartingController {
    private Connection conn;

    private Statement stmt;

    public StartingController() {
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/309test?useSSL=true", "309Tester", "309temp");
            stmt = conn.createStatement();
            conn.setAutoCommit(true);
            conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
        }
        catch(Exception e)
        {
            System.out.println("An error occurred.");
        }
    }

    @GetMapping("/getAthlete")
    public String getAthlete(@RequestParam String userName)
    {
        String getQuery = "select * from athlete where userName = " + userName;
        String retRes = "";
        try
        {
            conn.setAutoCommit(true);
            conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
            stmt = conn.createStatement();
            retRes = runQuery(stmt, getQuery);
        }
        catch(Exception e)
        {
            System.out.println("Could not fetch");
        }
        return retRes;
    }

    @PostMapping("/postAthleteBody")
    public void postAthleteBody(@RequestBody Athlete athlete)
    {
        insertAthlete(conn, athlete);
    }



    /**
     * @param stmt
     * @param sqlQuery
     * @throws SQLException
     * @Author ComS 363 Teaching Staff
     *
     *  Run the given static SQL query.
     *
     */

    private static String runQuery(Statement stmt, String sqlQuery) throws SQLException {
        // ResultSet is used to store the data returned by DBMS when issuing a static query

        ResultSet rs;

        // ResultSetMetaData is used to find meta data about the data returned
        ResultSetMetaData rsMetaData;
        String toShow;


        // Send the SQL query to the DBMS
        rs = stmt.executeQuery(sqlQuery);

        // get information about the returned result such as the number of columns
        rsMetaData = rs.getMetaData();
        System.out.println(sqlQuery);

        // toShow is to build the string to output
        toShow = "";

        // iterate through each item in the returned result
        while (rs.next()) {
            // i+1 indicates the position of the column to obtain the output
            // getString(1) means getting the value of the column 1.
            // concatenate the columns in each row
            for (int i = 0; i < rsMetaData.getColumnCount(); i++) {

                toShow += rs.getString(i + 1) + " | ";
            }
            toShow += "\n";
        }

        // release the resultSet resource
        rs.close();

        return toShow;
    }

    /**
     * Show an example of a transaction
     * @param conn Valid database connection
     * @param ath Athlete to insert
     * @Author ComS 363 Teaching Staff, with modifications made by Zach Josten
     *
     */
    private static void insertAthlete(Connection conn, Athlete ath) {

        if (conn==null || ath==null) throw new NullPointerException();
        try {
			/* we want to make sure that all SQL statements for insertion
			   of a new food are considered as one unit.
			   That is all SQL statements between the commit and previous commit
			   get stored permanently in the DBMS or  all the SQL statements
			   in the same transaction are rolled back.

			   By default, the isolation level is TRANSACTION_REPEATABLE_READ
			   By default, each SQL statement is one transaction

			   conn.setAutoCommit(false) is to
			   specify what SQL statements are in the same transaction
			   by a developer.
			   Several SQL statements can be put in one transaction.
			*/

            conn.setAutoCommit(false);
            // full protection against interference from other transaction
            // prevent dirty read
            // prevent unrepeatable reads
            // prevent phantom reads
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            // create a static statement
            Statement stmt = conn.createStatement();
            ResultSet rs;
            int id=0;

            // get the maximum id from the food table
            rs = stmt.executeQuery("select max(id) from athlete");
            while (rs.next()) {
                // 1 indicates the position of the returned result we want to get
                // since the first and only column is an integer, we need to use getInt().
                // for other data types, use the appropriate get method.
                id = rs.getInt(1);
            }
            rs.close();
            stmt.close();
            // once done, close the DBMS resources

			/* Example of a parameterized SQL query, which has ? in the query. Each ? is to be
			  replaced by a value obtained from a user
			*/
            PreparedStatement inststmt = conn.prepareStatement(
                    " insert into athlete (id,userName,fName,lName,dateJoined,workoutList) values(?,?,?,?,?,?) ");

            // Set the first ?  to have the new food id
            inststmt.setInt(1, id+1);
            // Set the second ? to have the food name
            inststmt.setString(2, ath.getUserName());

            inststmt.setString(3, ath.getFirstName());

            inststmt.setString(4, ath.getLastName());

            inststmt.setString(5, ath.getDateJoined());

            inststmt.setString(6, ath.getWorkoutList());

            // tell DBMS to insert the food into the table
            // get the number of rows affected in return. The number of row should be one
            // for a successful update.
            int rowcount = inststmt.executeUpdate();

            // show how many rows are impacted, should be one row if
            // successful
            // if not successful, SQLException occurs.
            System.out.println("Number of rows updated:" + rowcount);
            inststmt.close();

            // Tell DBMS to make sure all the changes you made from
            // the prior commit is saved to the database
            conn.commit();


        } catch (SQLException e) {
            System.out.println("Failed to insert the user " + ath.getUserName());
        }

    }



}
