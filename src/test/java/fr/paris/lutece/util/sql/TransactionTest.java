/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.paris.lutece.util.sql;

import fr.paris.lutece.test.LuteceTestCase;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Transaction Test
 */
public class TransactionTest extends LuteceTestCase
{

    private static final String SQL_CREATE_TABLE = "CREATE TABLE test_transaction ( id integer )";
    private static final String SQL_INSERT = "INSERT INTO test_transaction VALUES ( ? )";

    public void testCommit()
    {
        System.out.println("commit");
        Transaction transaction = new Transaction();
        try
        {
            PreparedStatement statement = transaction.prepareStatement(SQL_CREATE_TABLE);
            statement.executeUpdate();

            for (int i = 0; i < 3; i++)
            {
                statement = transaction.prepareStatement(SQL_INSERT);
                statement.setInt(1, i);
                statement.executeUpdate();
            }
            transaction.commit();
        }
        catch (SQLException ex)
        {
            transaction.rollback();
        }
    }

    public void testRollback()
    {
        System.out.println("rollback");
        Transaction transaction = new Transaction();
        try
        {
            PreparedStatement statement;

            for (int i = 3; i < 6; i++)
            {
                statement = transaction.prepareStatement(SQL_INSERT);
                statement.setInt(1, i);
                statement.executeUpdate();
            }
            transaction.rollback();
        }
        catch (SQLException ex)
        {
            transaction.rollback();
        }
    }
}