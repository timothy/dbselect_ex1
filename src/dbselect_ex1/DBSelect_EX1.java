/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbselect_ex1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * Java application that performs the 5 queries on the books database:
 * @author Timothy Bradford
 */
public class DBSelect_EX1 {

    /**
     * Output formatter to show results of the query to the console.
     *
     * @param title The heading of the table
     * @param resultSet the result set of the query to be displayed
     * @throws SQLException
     */
    public static void printTable(String title, ResultSet resultSet) throws SQLException {
        System.out.println("--------------------------------------------------------------------------------------------------");
        System.out.println(title);
        System.out.println("--------------------------------------------------------------------------------------------------");
        ResultSetMetaData rsmd = resultSet.getMetaData();
        int columnsNumber = rsmd.getColumnCount();
        while (resultSet.next()) {
            for (int i = 1; i <= columnsNumber; i++) {
                if (i > 1) {
                    System.out.print(",  ");
                }
                String columnValue = resultSet.getString(i);
                System.out.print(rsmd.getColumnName(i) + ": " + columnValue);
            }
            System.out.println("");
        }
        System.out.println();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        final String DATABASE_URL = "jdbc:derby://localhost:1527/books";

        //1.Select all authors from the Authors table.
        final String SELECT_QUERY1
                = "SELECT * "
                + "FROM authors";

        //2.Select a specific author (author == 1) and list all books for that author. Include each book’s title, year, and ISBN. Order the information chronologically.
        final String SELECT_QUERY2
                = "SELECT TITLES.TITLE, TITLES.COPYRIGHT, TITLES.ISBN "
                + "FROM AUTHORS INNER JOIN AUTHORISBN ON AUTHORS.AUTHORID = AUTHORISBN.AUTHORID "
                + "INNER JOIN TITLES ON AUTHORISBN.ISBN = TITLES.ISBN "
                + "WHERE AUTHORS.AUTHORID = 1 ORDER BY TITLES.COPYRIGHT\n";

        //3.Select a specific title and list all authors for that title. Order the authors alphabetically by last name and then by first name.
        final String SELECT_QUERY3
                = "SELECT a.FIRSTNAME, a.LASTNAME "
                + "FROM AUTHORS as a "
                + "INNER JOIN AUTHORISBN ON a.AUTHORID = AUTHORISBN.AUTHORID "
                + "INNER JOIN TITLES as t ON AUTHORISBN.ISBN = t.ISBN "
                + "WHERE t.TITLE = 'Visual C++ How to Program' "
                + "ORDER BY a.LASTNAME, a.FIRSTNAME";

        //Custom Queries
        //4.1 Find full name of all authors with the first name Dan
        final String SELECT_QUERY4_1
                = "SELECT a.FIRSTNAME, a.LASTNAME \n"
                + "FROM DEITEL.AUTHORS as a \n"
                + "WHERE a.FIRSTNAME = 'Dan'";

        //4.2 Find all full names of all authors who have the last name Deitel
        final String SELECT_QUERY4_2
                = "SELECT a.FIRSTNAME, a.LASTNAME \n"
                + "FROM DEITEL.AUTHORS as a \n"
                + "WHERE a.LASTNAME = 'Deitel'";

        try (
                Connection connection = DriverManager.getConnection(
                        DATABASE_URL, "deitel", "deitel");
                //Execute Queries
                ResultSet resultSet1 = connection.createStatement().executeQuery(SELECT_QUERY1);
                ResultSet resultSet2 = connection.createStatement().executeQuery(SELECT_QUERY2);
                ResultSet resultSet3 = connection.createStatement().executeQuery(SELECT_QUERY3);
                ResultSet resultSet4_1 = connection.createStatement().executeQuery(SELECT_QUERY4_1);
                ResultSet resultSet4_2 = connection.createStatement().executeQuery(SELECT_QUERY4_2);) {

            //Print Results
            printTable("1. Select all authors from the Authors table.", resultSet1);
            printTable("2.Select a specific author(author == 1) and list all books for that author."
                    + " \nInclude each book’s title, year, and ISBN."
                    + " \nOrder the information chronologically", resultSet2);
            printTable("3.Select a specific title(Visual C++...) and list all authors for that title."
                    + " \nOrder the authors alphabetically by last name and then by first name.", resultSet3);
            printTable("4.1: Find full names of all authors with the first name Dan", resultSet4_1);
            printTable("4.2: Find full names of all authors who have the last name Deitel", resultSet4_2);
        } // AutoCloseable objects' close methods are called now  // AutoCloseable objects' close methods are called now 
        catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

}
