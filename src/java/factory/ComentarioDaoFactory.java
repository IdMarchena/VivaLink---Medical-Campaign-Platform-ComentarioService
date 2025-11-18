/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package factory;
import dao.ComentarioDao;
import java.sql.SQLException;
import dao.connection.DatabaseConnection;
import dao.ComentarioDaoMongo;
import dao.ComentarioDaoMysql;
import dao.ComentarioDaoPostgres;

/**
 *
 * @author Usuario
 */
public class ComentarioDaoFactory {
public static ComentarioDao dao(String tipoDao) throws SQLException{
        DatabaseConnection conn = DatabaseConnectionFactory.connection(tipoDao);
                switch (tipoDao.toLowerCase()) {
            case "postgres" -> {
                return new ComentarioDaoPostgres(conn);
            }
            case "mysql" -> {
                return new ComentarioDaoMysql(conn);
            }
            case "mongo" -> {
                return new ComentarioDaoMongo(conn);
            }
            default -> throw new AssertionError();
        }
    }
}
