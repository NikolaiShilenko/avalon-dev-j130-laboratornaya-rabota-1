/*
 * DEV-J130. Задача №2.
 */
package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Nikolai Shilenko
 */
public class DbServer implements IDbService {

    private Connection con;

    public DbServer(String url, String user, String psw) throws SQLException {
        try {
            con = DriverManager.getConnection(url, user, psw);
            System.out.println("Connection is opened...");
        } catch (SQLException ex) {
            throw new SQLException("Connection isn't opened to url: " + url + ", by user's name: " + user + "; user's password: " + psw, ex);
        }
    }

    private PreparedStatement setPreparedStatement(String sql) throws SQLException {
        try {
            PreparedStatement pst = con.prepareStatement(sql);
            System.out.println("PreparedStatement object is created...");
            return pst;
        } catch (SQLException ex) {
            throw new SQLException("PreparedStatement object isn't created with sql string: " + sql, ex);
        }
    }

//    public static void main(String[] args) {
//        try (DbServer dbServer = new DbServer("jdbc:derby://localhost:1527/test", "test", "test")) {
//
//        } catch (SQLException ex) {
//            //System.out.println(ex.getMessage() + "\n " + ex.getCause());
//            ex.printStackTrace();
//        } catch (DocumentException ex) {
//            //System.out.println(ex.getMessage() + "\n " + ex.getCause());
//            ex.printStackTrace();
//        } catch (Exception ex) {
//            //System.out.println(ex.getMessage() + "\n " + ex.getCause());
//            ex.printStackTrace();
//        }
//    }

    public boolean findAuthorById(int id) throws SQLException {
        String sql = "SELECT * FROM app.authors WHERE id = ?";
        try (PreparedStatement pst = setPreparedStatement(sql)) {
            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                return rs.next();
            } catch (SQLException ex) {
                throw new SQLException(ex);
            }
        }
    }

    public boolean findDocumentById(int id) throws SQLException {
        String sql = "SELECT * FROM app.documents WHERE id = ?";
        try (PreparedStatement pst = setPreparedStatement(sql)) {
            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                return rs.next();
            } catch (SQLException ex) {
                throw new SQLException(ex);
            }
        }
    }

    /**
     * Метод добавляет нового автора к базе данных, если все обязательные поля
     * объекта author определены. В противном случае, метод пытается обновить
     * уже существующие записи, используя заполненные поля класса для поиска
     * подходящих записей. Например, если в объекте author указан id автора,
     * поле имени автора пусто, а поле примечаний содержит текст, то у записи с
     * заданным идентификатором обновляется поле примечаний.
     *
     * @param author именные данные автора.
     * @return возвращает значение true, если создана новая запись, и значение
     * false, если обновлена существующая запись.
     * @throws DocumentException выбрасывается в случае, если поля объекта
     * author заполнены неправильно и не удаётся создать новую запись или
     * обновить уже существующую. Данное исключение также выбрасывается в случае
     * общей ошибки доступа к базе данных
     */
    @Override
    public boolean addAuthor(Authors author) throws DocumentException {
        try {
            if (!this.findAuthorById(author.getAuthor_id())) {
                String sqlInsert = "INSERT INTO app.authors (id, name, text) VALUES(?, ?, ?)";
                try (PreparedStatement pst = setPreparedStatement(sqlInsert)) {
                    pst.setInt(1, author.getAuthor_id());
                    pst.setString(2, author.getAuthor());
                    pst.setString(3, author.getNotes());
                    return pst.executeUpdate() == 1;
                } catch (SQLException ex) {
                    throw new DocumentException(ex);
                }
            } else {
                String sqlUpdate = "UPDATE app.authors SET name = ?, text = ? WHERE id = ?";
                try (PreparedStatement pst = setPreparedStatement(sqlUpdate)) {
                    pst.setString(1, author.getAuthor());
                    pst.setString(2, author.getNotes());
                    pst.setInt(3, author.getAuthor_id());
                    pst.executeUpdate();
                } catch (SQLException ex) {
                    throw new DocumentException(ex);
                }
                return false;
            }
        } catch (SQLException ex) {
            throw new DocumentException(ex);
        }
    }

    /**
     * Метод добавляет новый документ к базе данных, если все обязательные поля
     * объектов doc и author определены. В противном случае, метод пытается
     * обновить уже существующие записи, используя заполненные поля объектов для
     * поиска подходящих записей.
     *
     * @param doc добавляемый или обновляемый документ.
     * @param author ссылка на автора документа.
     * @return возвращает значение true, если создан новый документ, и значение
     * false, если обновлена уже существующая запись.
     * @throws DocumentException выбрасывается в случае, если поля объектов doc
     * и author заполнены неправильно и не удаётся создать новую запись или
     * обновить уже существующую. Данное исключение также выбрасывается в случае
     * общей ошибки доступа к базе данных
     */
    @Override
    public boolean addDocument(Documents doc, Authors author) throws DocumentException {
        try {
            if (!findDocumentById(doc.getDocument_id())) {
                String sql = "INSERT INTO app.documents (id, name, text, create_date, author) VALUES(?, ?, ?, ?, ?)";
                try (PreparedStatement pst = setPreparedStatement(sql)) {
                    pst.setInt(1, doc.getDocument_id());
                    pst.setString(2, doc.getTitle());
                    pst.setString(3, doc.getText());
                    pst.setDate(4, new java.sql.Date(doc.getDate().getTime()));
                    pst.setInt(5, author.getAuthor_id());
                    return pst.executeUpdate() == 1;
                } catch (SQLException ex) {
                    throw new DocumentException(ex);
                }
            } else {
                String sql = "UPDATE app.documents SET name = ?, text = ?, create_date = ?, author = ? WHERE id = ?";
                try (PreparedStatement pst = setPreparedStatement(sql)) {
                    pst.setString(1, doc.getTitle());
                    pst.setString(2, doc.getText());
                    pst.setDate(3, new java.sql.Date(doc.getDate().getTime()));
                    pst.setInt(4, author.getAuthor_id());
                    pst.setInt(5, doc.getDocument_id());
                    pst.executeUpdate();
                } catch (SQLException ex) {
                    throw new DocumentException(ex);
                }
                return false;
            }

        } catch (SQLException ex) {
            throw new DocumentException(ex);
        }
    }

    /**
     * Метод производит поиск документов по их автору.
     *
     * @param author автор документа. Объект может содержать неполную информацию
     * об авторе. Например, объект может содержать только именные данные автора
     * или только его идентификатор.
     * @return возвращает массив всех найденных документов. Если в базе данных
     * не найдено ни одного документа, то возвращается значение null.
     * @throws DocumentException выбрасывается в случае, если поле объекта
     * author заполнены неправильно или нелья выполнить поиск по его полям.
     * Данное исключение также выбрасывается в случае общей ошибки доступа к
     * базе данных
     */
    @Override
    public Documents[] findDocumentByAuthor(Authors author) throws DocumentException {
        String sql = "SELECT * FROM app.documents WHERE author = ?";
        try (PreparedStatement pst = setPreparedStatement(sql)) {
            pst.setInt(1, author.getAuthor_id());
            List<Documents> docList = new ArrayList<>();
            Documents doc;
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    do {
                        doc = new Documents(rs.getInt("id"), rs.getString("name"), rs.getString("text"), rs.getDate("create_date"), rs.getInt("author"));
                        docList.add(doc);
                    } while (rs.next());
                    Documents[] a = new Documents[docList.size()];
                    return docList.toArray(a);
                }
            } catch (SQLException ex) {
                throw new DocumentException(ex);
            }
        } catch (SQLException ex) {
            throw new DocumentException(ex);
        }
        return null;
    }

    /**
     * Метод производит поиск документов по их содержанию.
     *
     * @param content фрагмент текста (ключевые слова), который должен
     * содержаться в заголовке или в основном тексте документа.
     * @return возвращает массив найденных документов.Если в базе данных не
     * найдено ни одного документа, удовлетворяющего условиям поиска, то
     * возвращается значение null.
     * @throws DocumentException выбрасывается в случае, если строка content
     * равна null или является пустой. Данное исключение также выбрасывается в
     * случае общей ошибки доступа к базе данных
     */
    @Override
    public Documents[] findDocumentByContent(String content) throws DocumentException {
        String sql = "SELECT * FROM app.documents WHERE name LIKE ? OR text LIKE ?";
        try (PreparedStatement pst = setPreparedStatement(sql)) {
            pst.setString(1, "%" + content + "%");
            pst.setString(2, "%" + content + "%");
            List<Documents> docList = new ArrayList<>();
            Documents doc;
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    do {
                        doc = new Documents(rs.getInt("id"), rs.getString("name"), rs.getString("text"), rs.getDate("create_date"), rs.getInt("author"));
                        docList.add(doc);
                    } while (rs.next());
                    Documents[] a = new Documents[docList.size()];
                    return docList.toArray(a);
                }
            } catch (SQLException ex) {
                throw new DocumentException(ex);
            }
        } catch (SQLException ex) {
            throw new DocumentException(ex);
        }
        return null;
    }

    /**
     * Метод удаляет автора из базы данных. Всесте с автором удаляются и все
     * документы, которые ссылаются на удаляемого автора.
     *
     * @param author удаляемый автор. Объект может содержать неполные данные
     * автора, например, только идентификатор автора.
     * @return значение true, если запись автора успешно удалена, и значение
     * false - в противном случае.
     * @throws DocumentException выбрасывается в случае, если поля объекта
     * author заполнены неправильно или ссылка author равна null, а также случае
     * общей ошибки доступа к базе данных.
     */
    @Override
    public boolean deleteAuthor(Authors author) throws DocumentException {
        return deleteAuthor(author.getAuthor_id());
    }

    /**
     * Метод удаляет автора из базы данных по его идентификатору. Всесте с
     * автором удаляются и все документы, которые ссылаются на удаляемого
     * автора.
     *
     * @param id идентификатор удаляемого автора.
     * @return значение true, если запись автора успешно удалена, и значение
     * false - в противном случае.
     * @throws DocumentException выбрасывается в случае общей ошибки доступа к
     * базе данных.
     */
    @Override
    public boolean deleteAuthor(int id) throws DocumentException {
        String sqlDeleteDocs = "DELETE FROM app.documents WHERE author = ?";
        String sqlDeleteAuthor = "DELETE FROM app.authors WHERE id = ?";
        try (PreparedStatement pst = setPreparedStatement(sqlDeleteDocs); PreparedStatement pst2 = setPreparedStatement(sqlDeleteAuthor)) {
            pst.setInt(1, id);
            pst.executeUpdate();
            pst2.setInt(1, id);
            return pst2.executeUpdate() == 1;
        } catch (SQLException ex) {
            throw new DocumentException(ex);
        }
    }

    @Override
    public void close() throws SQLException {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
                System.out.println("Connection is closed...");
            }
        } catch (SQLException ex) {
            throw new SQLException(ex);
        }
    }

}
