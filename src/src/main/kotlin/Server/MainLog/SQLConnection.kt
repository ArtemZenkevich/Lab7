package src.main.kotlin.Server.MainLog

import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.SQLException


open abstract class SQLConnection {
     fun getDBConnection(DB_USER: String?, DB_PASSWORD: String?): Connection? {
        var dbConnection: Connection? = null
        try {
            val DB_DRIVER ="org.postgresql.Driver"
            Class.forName(DB_DRIVER)
        } catch (e: ClassNotFoundException) {
            println(e.message)
        }
        try {
            dbConnection = DriverManager.getConnection("jdbc:postgresql://localhost/studs", DB_USER, DB_PASSWORD)
            return dbConnection
        } catch (e: SQLException) {
            println(e.message)
        }
        return dbConnection
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