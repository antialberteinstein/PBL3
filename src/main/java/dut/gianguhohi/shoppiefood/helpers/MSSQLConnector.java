package dut.gianguhohi.shoppiefood.helpers;

import java.sql.*;

public class MSSQLConnector {
	
	private Statement st;
	private Connection conn;
	private boolean is_connected;
	
	public static boolean CONNECTED = true;
	public static boolean DISCONNECTED = false;
	
	private static MSSQLConnector instance = null;
	
	/*
	 * Create a connection to SQL Server, could contain an exception
	 *           of database's connecting action.
	 */
	private MSSQLConnector() throws Exception {
		is_connected = false;
		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=test_java;user=root;pwd=;encrypt=true;trustServerCertificate=true;loginTimeout=30;");
		st = conn.createStatement();
		is_connected = true;
	}
	
	/*
	 * This method do the step inserting a value into the table.
	 * The value may be like:
	 * * "id=1, name='Nguyen Van A', 'hometown='Quang Nam'"
	 * The fields looks like:
	 * * "name, hometown"
	 * * At here, the value is "'Nguyen Van A', 'Quang Nam'"
	 * * Without the fields parameter, you need to pass all the values
	 * *      for each row.
	 * Additionally, there can be an exception of database thrown.
	 * */
	public void insert(String table, String fields, String value) throws Exception {
		String sql = "INSERT INTO " + table;
		if (fields != null) {
			sql += " (" + fields + ") ";
		}
		sql += " VALUES(" + value + ");";
		st.executeUpdate(sql);
	}
	
	/*
	 * This method do the step inserting a value into the table.
	 * The value may be like:
	 * * "id=1, name='Nguyen Van A', 'hometown='Quang Nam'"
	 * The fields looks like:
	 * * "name, hometown"
	 * * At here, the value is "'Nguyen Van A', 'Quang Nam'"
	 * * Without the fields parameter, you need to pass all the values
	 * *      for each row.
	 * Additionally, there can be an exception of database thrown.
	 * */
	public void insert(String table, String value) throws Exception {
		insert(table, null, value);
	}
	
	/*
	 * This method will remove rows from the table with the condition.
	 * The condition parameter could be:
	 * * "id=123" or "name='Nguyen Van A' AND age='20'".
	 * Besides, an database's exception could be thrown.
	 * */
	public void delete(String table, String condition) throws Exception {
		String sql = "DELETE FROM " + table + " WHERE " + condition + ";";
		st.executeUpdate(sql);
	}
	
	/*
	 * This method modify rows from the table base which are chosen by the condition.
	 * Parameter's examples:
	 * * condition: "id=123" or "name='Nguyen Van A' AND age='20'".
	 * * setValue: "name='Tran Van B', major='CS'"
	 * Of course, this one would throw an exception if any database's unsuitable thing occurred.
	 */
	public void update(String table, String setValue, String condition) throws Exception {
		String sql = "UPDATE " + table + " SET " + setValue + " WHERE " + condition + ";";
		st.executeUpdate(sql);
	}
	
	/**
     * This method returns a result set that contains the rows from the SELECT command
     * in SQL.
     * Thus, there will be some necessary parameters:
     * * fields: list of fields to be selected from the table.
     * * * For example: "id, name, age".
     * * * We could also do this action:
     * * *      "max(age) as max_age, name"
     * * * Let's try it by yourself :)
     * * table: name of the table.
     * * condition: example: "id=123" or "name='Nguyen Van A' AND age='20'".
     * * groupBy: example: "id".
     * * having: example: "age > 18".
     * * orderBy: example: "age DESC". 
     */
    public ResultSet select(String fields, String table,
                            String condition, String groupBy, String having, String orderBy) throws Exception {
        String sql = "SELECT " + fields + " FROM " + table;
        if (condition != null && !condition.isEmpty()) {
            sql += " WHERE " + condition;
        }
        if (groupBy != null && !groupBy.isEmpty()) {
            sql += " GROUP BY " + groupBy;
        }
        if (having != null && !having.isEmpty()) {
            sql += " HAVING " + having;
        }
        if (orderBy != null && !orderBy.isEmpty()) {
            sql += " ORDER BY " + orderBy;
        }
        sql += ";";
        return st.executeQuery(sql);
    }

    /**
     * This method returns a result set that contains the rows from the SELECT command
     * in SQL, but without any condition, groupBy, having, or orderBy clauses.
     * Thus, there will be some necessary parameters:
     * * fields: list of fields to be selected from the table.
     * * * For example: "id, name, age".
     * * table: name of the table.
     * 
     * Example usage:
     * * select("id, name", "students");
     */
    public ResultSet select(String fields, String table) throws Exception {
        return select(fields, table, null, null, null, null);
    }

    /**
     * This method returns a result set that contains the rows from the SELECT command
     * in SQL with a WHERE clause.
     * Thus, there will be some necessary parameters:
     * * fields: list of fields to be selected from the table.
     * * * For example: "id, name, age".
     * * table: name of the table.
     * * condition: example: "id=123" or "name='Nguyen Van A' AND age='20'".
     * 
     * Example usage:
     * * select("id, name", "students", "age > 18");
     */
    public ResultSet select(String fields, String table, String condition) throws Exception {
        return select(fields, table, condition, null, null, null);
    }

    /**
     * This method returns a result set that contains the rows from the SELECT command
     * in SQL with a WHERE clause and ORDER BY clause.
     * Thus, there will be some necessary parameters:
     * * fields: list of fields to be selected from the table.
     * * * For example: "id, name, age".
     * * table: name of the table.
     * * condition: example: "id=123" or "name='Nguyen Van A' AND age='20'".
     * * orderBy: example: "age DESC".
     * 
     * Example usage:
     * * select("id, name", "students", "age > 18", "name ASC");
     */
    public ResultSet select(String fields, String table, String condition, String orderBy) throws Exception {
        return select(fields, table, condition, null, null, orderBy);
    }

    /**
     * This method returns a result set that contains the rows from the SELECT command
     * in SQL with GROUP BY, HAVING, and ORDER BY clauses.
     * Thus, there will be some necessary parameters:
     * * fields: list of fields to be selected from the table.
     * * * For example: "id, name, AVG(score) AS avg_score".
     * * table: name of the table.
     * * groupBy: example: "id".
     * * having: example: "AVG(score) > 50".
     * * orderBy: example: "avg_score DESC".
     * 
     * Example usage:
     * * select("id, AVG(score) AS avg_score", "students", "id", "AVG(score) > 50", "avg_score DESC");
     */
    public ResultSet select(String fields, String table, String groupBy, String having, String orderBy) throws Exception {
        return select(fields, table, null, groupBy, having, orderBy);
    }
	
	/*
	 * This method return the status of the connector.
	 * It will return CONNECTED (true) if the connecting procedure is successful.
	 * Otherwise, the value thrown is DISCONNECTED (false).
	 * */
	public boolean getStatus() {
		return is_connected;
	}
	
	/*
	 * This method is like a destructor that clean the connection
	 *         at the end of the program.
	 */
	private void dispose() throws Exception {
		if (is_connected) {
			st.close();
			conn.close();
		}
	}
	
	public static MSSQLConnector getConnector() throws Exception {
		if (instance == null) {
			instance = new MSSQLConnector();
		}
		
		return instance;
	}
	
	public static void disconnect() throws Exception {
		instance.dispose();
		instance = null;
	}
}
