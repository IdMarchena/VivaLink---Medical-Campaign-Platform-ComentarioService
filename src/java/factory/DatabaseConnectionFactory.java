/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package factory;

import java.sql.SQLException;
import dao.connection.DatabaseConnection;
import dao.connection.MongoConnection;
import dao.connection.MysqlConnection;
import dao.connection.PostgreConnection;
/**
 *
 * @author Usuario
 */
public class DatabaseConnectionFactory {
        public static DatabaseConnection connection(String tipoDb) throws SQLException{
        switch (tipoDb.toLowerCase()) {
            case "postgre":
                return (DatabaseConnection) PostgreConnection.conexion();
            case "mysql":
                return (DatabaseConnection) MysqlConnection.conexion();
            case "mongo":
                return (DatabaseConnection) MongoConnection.conexion();
               
            default:
                throw new AssertionError();
        }
    }
}
