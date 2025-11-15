/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package factory;
import dao.ComentarioDao;
import dao.ComentarioDaoMysql;
import dao.ComentarioDaoMongo;
import dao.ComentarioDaoPostgres;
import java.sql.SQLException;

/**
 *
 * @author Usuario
 */
public class ComentarioDaoFactory {
    public static ComentarioDao dao(String tipoDao) throws SQLException{
                switch (tipoDao.toLowerCase()) {
            case "postgre":
                return new ComentarioDaoPostgres(tipoDao);
            case "mysql":
                return new ComentarioDaoMysql(tipoDao);
            case "mongo":
                return new ComentarioDaoMongo(tipoDao);               
            default:
                throw new AssertionError();
        }
    }
}
