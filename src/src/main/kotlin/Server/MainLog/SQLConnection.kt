package src.main.kotlin.Server.MainLog

import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.SQLException


open abstract class SQLConnection {
    val DB_USER: String? = "s333116"
    val DB_PASSWORD: String? = "gqp064"
    fun MakeNewDB(){
        try{
        insertIntoDB("CREATE TABLE FLATS(" +
                "FLAT_ID INT NOT NULL, " +
                "FLAT_NAME VARCHAR(20), " +
                "COORINATE_X INT, " +
                "COORINATE_Y INT, " +
                "CREATION_DATE VARCHAR(20), " +
                "AREA INT, " +
                "NUMBER_OF_ROOMS INT," +
                "PRICE INT, " +
                "FURNISH VARCHAR(20), " +
                "VIEW VARCHAR(20), " +
                "HOUSE VARCHAR(20), " +
                "USER_ID INT" +
                ");", getDBConnection()!!
        )
        insertIntoDB("CREATE TABLE PERSON(" +
                "USER_ID INT NOT NULL, " +
                "USERNAME VARCHAR(50), " +
                "USER_PASSWORD VARCHAR(50)" +
                ");", getDBConnection()!!
        )
        insertIntoDB("CREATE TABLE HOUSE(" +
                "HOUSE_ID INT NOT NULL, " +
                "HOUSE_NAME VARCHAR(20), " +
                "HOUSE_YEAR INT, " +
                "HOUSE_COL INT"+
                ");", getDBConnection()!!
        )}catch(e: SQLException) {
            println(e.message)
        }
    }
     fun getDBConnection(): Connection? {
        var dbConnection: Connection? = null
        try {
            val DB_DRIVER ="org.postgresql.Driver"
            Class.forName(DB_DRIVER)
        } catch (e: ClassNotFoundException) {
            println(e.message)
        }
        try {
            dbConnection = DriverManager.getConnection("jdbc:postgresql://pg:5432/studs", DB_USER, DB_PASSWORD)
            return dbConnection
        } catch (e: SQLException) {
            println(e.message)
        }
        return dbConnection
    }
    fun getMaxId(res:ResultSet):Int? {
        var id = 0
        while (res.next()) {
            id = res.getInt("USER_ID")
        }
        return id
    }
    fun insertIntoDB(info:String, connection: Connection){
        try {
            var statement = connection.createStatement()
            statement.executeUpdate(info);
        }
        catch (ex:SQLException){
            println(ex.message)
        }
    }
    fun delInDB(info:String, connection: Connection){
        try {
            var statement = connection.createStatement()
            statement.execute(info);
        }
        catch (ex:SQLException){
            println(ex.message)
        }
    }
    fun UpdateDB(info:String, connection: Connection){
        try {
            var statement = connection.createStatement()
            statement.execute(info);
        }
        catch (ex:SQLException){
            println(ex.message)
        }
    }
    fun getData(info:String, connection: Connection?): ResultSet? {
        try {
            var statement = connection?.createStatement()
            return statement?.executeQuery(info)
        } catch (e: SQLException) {
            println(e.message)
        }
        return null
    }
}