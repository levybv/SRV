package br.com.marisa.srv.geral.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import br.com.marisa.srv.geral.excecoes.PersistenciaException;
import br.com.marisa.srv.geral.helper.ObjectHelper;
import br.com.marisa.srv.geral.parametros.Parametros;


/**
 * Classe para ser herdada por todos os DAOs do sistema
 * 
 * @author Walter Fontes
 */
public class BasicDAO {
    
    // Log4J
    private static final Logger log = Logger.getLogger(BasicDAO.class);

    private static final String CARACTERES_PARA_VERDADEIRO = "SsYy1TtVv";
    private Connection conn;
    private boolean connectionOwner = false;
    private String dbIdentifier;

    public static final int PARAMTYPE_INTEGER = 1;
    public static final int PARAMTYPE_STRING = 2;
    public static final int PARAMTYPE_DOUBLE = 3;
    public static final int PARAMTYPE_LONG = 4;
    public static final int PARAMTYPE_DATE = 5;
    public static final int PARAMTYPE_TIMESTAMP = 6;
    public static final int PARAMTYPE_BOOLEAN = 7;
    public static final int PARAMTYPE_BOOLEANSTR = 8; // Serve para passar uma String como boolean (ex: "S" ou "N")

    /**
     * Obtém uma conexão com o banco de dados. Caso nenhuma conexão tenha sido configurada via <code>setConn</code>, o DAO obtem
     * uma nova conexão com o banco através do método <code>getNewConn</code>., baseado no identificador do banco de dados
     * configurado via <code>setDbIdentifier</code> ou o identificador padrão definido no arquivo parametros.properties, caso este não o
     * identificador não esteja sendo valorizado programaticamente.
     * 
     * @throws PersistenciaException
     * @return Returns Um objeto de conexão com o banco de dados
     */
    public Connection getConn() throws PersistenciaException {
        if (conn != null) {
            return conn;
        }
        return getNewConn();
    }

    /**
     * Obtém uma nova conexão com o banco baseado no identificador do banco de dados configurado via <code>setDbIdentifier</code>
     * ou o identificador padrão definido no arquivo parametros.properties, caso este não o identificador não esteja sendo valorizado
     * programaticamente. Caso já exista uma conexão atual ela é substituída pela nova conexão obtida.
     * 
     * @throws PersistenciaException
     * @return Returns Um objeto de conexão com o banco de dados
     */
    private Connection getNewConn() throws PersistenciaException {
        if (dbIdentifier == null) {
            dbIdentifier = getDefaultDbIdentifier();
            if (dbIdentifier == null) {
                throw new PersistenciaException(log, "Não foi possível obter uma conexão pois não foi especificado um valor para o atributo dbIdentifier.");
            }
        }
        
        //log.debug("***************************************************************");
        //log.debug("Início do processo para obter uma conexão com o banco de dados.");

        try {
        	String jndiContex = Parametros.getValue("database." + dbIdentifier + ".jndicontext");
            if (ObjectHelper.isEmpty(jndiContex)) {
                throw new PersistenciaException(log, "O Valor especificado para o atributo dbIdentifier (" + jndiContex + ") não foi encontrado no arquivo de configurações do sistema.");
            }
            
            Context ctx = new InitialContext();
            if(ctx == null ) {
				throw new PersistenciaException(log, "Contexto não encontrado");
            }

            //log.debug("Realizando acesso via datasource...");

            Object obj = ctx.lookup(jndiContex);
            DataSource ds = (DataSource)obj;
                
            if (ds == null) {
                throw new PersistenciaException(log, "Não foi possível obter o datasource.");
            }
            conn = ds.getConnection();
            //log.debug("Conexão criada com sucesso...");
            
            connectionOwner = true;
            conn.setAutoCommit(true);

        } catch (Exception e) {
            throw new PersistenciaException(log, "Erro ao obter a conexao \"" + dbIdentifier + "\". Motivo: " + e.getMessage(), e);
        }
        return conn;
    }
    
    
    /**
     * Obtém o dbIdentifier do banco de dados cujos parametros serão lidos do parametros.properties
     * 
     * Este método deve ser sobrescrito pelas classes que estendem BasicDAO para fornecer o dbIdentifier necessário para o DAO. Caso
     * este método não seja sobrescrito, uma conexão deverá ser passada manualmente para o DAO via <code>setConn(...)</code> ou
     * então um dbIdentifier deverá ser configurado manualmente via <code>setDbIdentifier(...)</code>.
     * 
     * @return String contendo o dbIdentifier
     * @throws PersistenciaException
     */
    public String getDefaultDbIdentifier() throws PersistenciaException {
        String defaultDbIdentifier = null;
        try {
            defaultDbIdentifier = Parametros.getValue("database.default.identifier");
        } catch (Exception e) {
            throw new PersistenciaException(log, e.getMessage());
        }
        return defaultDbIdentifier;
    }

    /**
     * Configura manualmente uma conexão para o DAO. Quando a conexão é configurada desta forma, o DAO perde o <i>owner </i> da
     * mesma. Isto significa que o método <code>close();</code> do DAO <B>NÃO </B> fechará esta conexão.
     * 
     * Este método é utilizado geralmente quando uma mesma conexão vai ser compartilhada na execução de vários DAOs, visando uma
     * transação atômica no banco de dados. (ex: um DAO obtem a conexão, dá um <code>beginTrans()</code>, realiza uma operação,
     * outro DAO é chamado, esta mesma conexão e transação são passados para ele, o novo DAO realiza outras operações, depois você
     * fecha o DAO atual e o DAO original.
     * 
     * @param conn Um objeto de conexão com o banco de dados.
     */
    public void setConn(Connection conn) {
        this.conn = conn;
        this.connectionOwner = false;
    }

    /**
     * Obtém a String de dbIdentifier. Veja mais informações no método <code>getDefaultDbIdentifier</code>
     * 
     * @see BasicDAO#getDefaultDbIdentifier()
     * @return Returns the dbIdentifier.
     */
    public String getDbIdentifier() {
        return dbIdentifier;
    }

    /**
     * Configura a String de dbIdentifier. Veja mais informações no método <code>getDefaultDbIdentifier</code>
     * 
     * @see BasicDAO#getDefaultDbIdentifier()
     * @param dbIdentifier The dbIdentifier to set.
     */
    public void setDbIdentifier(String dbIdentifier) {
        this.dbIdentifier = dbIdentifier;
    }

    /**
     * Indica se o DAO é owner da conexão. Quando o DAO é owner, ele fechará a conexão ao ser executado o método close()
     * 
     * @return Returns the connectionOwner.
     */
    public boolean isConnectionOwner() {
        return connectionOwner;
    }

    /**
     * Informa se o DAO é owner da conexão. Quando o DAO é owner, ele fechará a conexão ao ser executado o método close(). Por
     * default, caso uma conexão seja atribuida manualmente ao DAO, ele será configurado como
     * <code>setConnectionOwner(false);</code>. Caso uma conexão seja obtida automaticamente pelo DAO, ele será configurado como
     * <code>setConnectionOwner(true);</code>
     * 
     * @param connectionOwner
     */
    public void setConnectionOwner(boolean connectionOwner) {
        this.connectionOwner = connectionOwner;
    }

    /**
     * Inicia uma transação na conexão do banco de dados.
     * 
     * @throws PersistenciaException
     */
    public void beginTrans() throws PersistenciaException {
        try {
            getConn().setAutoCommit(false);
        } catch (Exception e) {
            throw new PersistenciaException("Erro ao iniciar uma transação com o banco de dados. Motivo: " + e.getMessage(), e);
        }
    }

    /**
     * Realiza um commit na conexão com o banco
     * 
     * @throws PersistenciaException
     */
    public void commitTrans() throws PersistenciaException {
        try {
            if (!getConn().getAutoCommit()) {
                getConn().commit();
                getConn().setAutoCommit(true);
                closeConnection();
            }
        } catch (Exception e) {
            throw new PersistenciaException("Erro ao tentar realizar o commitTrans com a transação do banco de dados. Motivo: "
                    + e.getMessage(), e);
        }
    }

    /**
     * Realiza um rollback na conexão com o banco
     * 
     * @throws PersistenciaException
     */
    public void rollbackTrans() throws PersistenciaException {
        try {
            if (!getConn().getAutoCommit()) {
                try {
                    getConn().rollback();
                    getConn().setAutoCommit(true);
                    closeConnection();
                } catch (Exception e) {
                    log.error("Erro ao tentar realizar o rollbackTrans com a transação do banco de dados. Motivo: "
                            + e.getMessage(), e);
                }
            }
        } catch (Exception e) {
            throw new PersistenciaException("Erro ao tentar realizar o rollbackTrans com a transação do banco de dados. Motivo: "
                    + e.getMessage(), e);
        }
    }

    /**
     * Verifica se a conexão está em estado de transação
     * 
     * @return Retorna <code>true</code> se a conexão estiver em transação. Caso contrário, retorna <code>false</code>
     * @throws PersistenciaException
     */
    public boolean isInTransaction() throws PersistenciaException {
        if (conn == null)
            return false;

        try {
            return !conn.getAutoCommit();
        } catch (SQLException e) {
            throw new PersistenciaException("Ocorreu um erro ao tentar obter o estado da transação na conexão. Motivo: "
                    + e.getMessage());
        }
    }

    /**
     * Fecha a conexão se e somente se este DAO for owner da mesma. Caso contrário o método não faz nada. É importante dar commit ou
     * rollback em qualquer transação antes de tentar efetuar um close.
     * 
     * @return Retorna <code>true</code> quando o DAO era owner da conexão, fechando a mesma. Retorna <code>false</code> quando
     *         o DAO não era owner da conexão, mantendo a mesma aberta.
     * @throws PersistenciaException
     */
    public boolean closeConnection() throws PersistenciaException {
        boolean wasInTransaction = false;
        if (connectionOwner && (conn != null)) {
            try {
                if (isInTransaction()) {
                    wasInTransaction = true;
                    rollbackTrans();
                }
            } finally {
                try {
                    conn.close();
                    conn = null;
                    connectionOwner = false;
                } catch (SQLException e) {
                    throw new PersistenciaException("Ocorreu um erro ao tentar fechar a conexão. Motivo: " + e.getMessage());
                }

                if (wasInTransaction) {
                    log.debug("AVISO: Executado rollback automatico na conexão que foi fechada.");
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Fecha um Statement do JDBC caso o objeto não seja nulo.
     * 
     * @param stmt
     * @throws PersistenciaException
     */
    public void closeStatement(Statement stmt) throws PersistenciaException {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                log.error("Erro ao fechar o statement. Motivo: " + e.getMessage(), e);
                throw new PersistenciaException("Erro ao fechar o statement. Motivo: " + e.getMessage(), e);
            }
        }
    }

    /**
     * Fecha um ResultSet do JDBC caso o objeto não seja nulo.
     * 
     * @param rs
     * @throws PersistenciaException
     */
    public void closeResultSet(ResultSet rs) throws PersistenciaException {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                log.error("Erro ao fechar o resultset. Motivo: " + e.getMessage(), e);
                throw new PersistenciaException("Erro ao fechar o resultset. Motivo: " + e.getMessage(), e);
            }
        }
    }

    /**
     * Fecha um ResultSet do JDBC caso o objeto não seja nulo.
     * 
     * @param stmt
     * @param rs
     * @throws PersistenciaException
     */
    public void closeStatementAndResultSet(Statement stmt, ResultSet rs) throws PersistenciaException {
    	closeStatementAndResultSet(stmt,rs,null);
    }
    
    public void closeStatementAndResultSet(Statement stmt, ResultSet rs, Connection conn) throws PersistenciaException {
        closeStatement(stmt);
        closeResultSet(rs);
        if(conn != null && !isInTransaction()){
        	closeConnection();
        }
    }

    /**
     * Obtém o sequence gerado no último INSERT realizado na conexão
     * 
     * @return Um long com o sequence gerado
     * @throws PersistenciaException
     */
    public long getLastIdentity() throws PersistenciaException {

        long identity = 0;
        try {
            PreparedStatement pstm = getConn().prepareStatement("SELECT @@IDENTITY");
            // ResultSet rs = pstm.executeQuery();
            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                identity = rs.getLong(1);
            } else {
                throw new PersistenciaException("Não existe last identity para a pesquisa realizada.");
            }
        } catch (SQLException e) {
            log.error("Erro na captura do last identity.", e);
            throw new PersistenciaException("Erro na captura do last identity.", e);
        }
        return identity;
    }

    /**
     * Este método deve ser chamado toda vez que uma transação for requerida por um método. Isto ocorre em métodos que
     * conhecidamente atualizam mais de uma tabela por vez.
     * 
     * @throws PersistenciaException Lançada caso não exista uma transação iniciada.
     */
    public void requiresTransaction() throws PersistenciaException {
        if (!isInTransaction()) {
            throw new PersistenciaException("Este método requer uma transação para ser executado.");
        }
    }

    /**
     * Coloca o valor de uma String nos devidos <code>PreparedStatement</code> e índice passados como parâmetros. Caso o valor
     * seja nulo, o próprio valor de <code>null</code> é configurado.
     * 
     * @param stmt
     * @param columnIndex
     * @param valor
     * @throws SQLException
     */
    public void setString(PreparedStatement stmt, int columnIndex, String valor) throws SQLException {
        if (valor != null) {
            stmt.setString(columnIndex, valor);
        } else {
            stmt.setNull(columnIndex, Types.VARCHAR);
        }
    }

    /**
     * Coloca o valor de um char nos devidos <code>PreparedStatement</code> e índice passados como parâmetros.
     * 
     * @param stmt
     * @param columnIndex
     * @param valor
     * @throws SQLException
     */
    public void setChar(PreparedStatement stmt, int columnIndex, char valor) throws SQLException {
        setString(stmt, columnIndex, String.valueOf(valor));
    }

    /**
     * Coloca o valor de um Character nos devidos <code>PreparedStatement</code> e índice passados como parâmetros. Caso o valor
     * seja nulo, o próprio valor de <code>null</code> é configurado.
     * 
     * @param stmt
     * @param columnIndex
     * @param valor
     * @throws SQLException
     */
    public void setCharacter(PreparedStatement stmt, int columnIndex, Character valor) throws SQLException {
        if (valor != null) {
            stmt.setString(columnIndex, valor.toString());
        } else {
            stmt.setNull(columnIndex, Types.CHAR);
        }
    }

    /**
     * Coloca o valor de um long nos devidos <code>PreparedStatement</code> e índice passados como parâmetros. Caso o valor seja
     * nulo, o próprio valor de <code>null</code> é configurado.
     * 
     * @param stmt
     * @param columnIndex
     * @param valor
     * @throws SQLException
     */
    public void setLong(PreparedStatement stmt, int columnIndex, Long valor) throws SQLException {
        if (valor != null) {
            stmt.setLong(columnIndex, valor.longValue());
        } else {
            stmt.setNull(columnIndex, Types.BIGINT);
        }
    }

    /**
     * Coloca o valor de um boolean nos devidos <code>PreparedStatement</code> e índice passados como parâmetros. O valor passado
     * como parâmetro é uma String e não um boolean. O método verifica se essa String representa um valor verdadeiro. Caso positivo,
     * <code>true</code> é configurado. Caso contrário, <code>false</code> é configurado. Caso o valor seja nulo, o próprio
     * valor de <code>null</code> é configurado.
     * 
     * 
     * @param stmt
     * @param columnIndex
     * @param valor
     * @throws SQLException
     */
    public void setBoolean(PreparedStatement stmt, int columnIndex, String valor) throws SQLException {
        boolean valorBoolean;
        if (valor != null) {
            valorBoolean = (CARACTERES_PARA_VERDADEIRO.indexOf(valor.charAt(0)) != -1);
        } else {
            valorBoolean = false;
        }
        setBoolean(stmt, columnIndex, valorBoolean);
    }

    /**
     * Coloca o valor de um boolean nos devidos <code>PreparedStatement</code> e índice passados como parâmetros. O valor passado
     * como parâmetro é um empacotador <code>Boolean</code> e não um valor primitivo. Caso o valor seja nulo, o próprio valor de
     * <code>null</code> é configurado.
     * 
     * @param stmt
     * @param columnIndex
     * @param valor
     * @throws SQLException
     */
    public void setBoolean(PreparedStatement stmt, int columnIndex, Boolean valor) throws SQLException {
        if (valor != null) {
            setBoolean(stmt, columnIndex, valor.booleanValue());
        } else {
            stmt.setNull(columnIndex, Types.BIGINT);
        }
    }

    /**
     * Coloca o valor de um boolean nos devidos <code>PreparedStatement</code> e índice passados como parâmetros. Caso o valor
     * seja <code>true</code>, a String "S" é configurada. Caso contrário, a String "N" é configurada.
     * 
     * @param stmt
     * @param columnIndex
     * @param valor
     * @throws SQLException
     */
    public void setBoolean(PreparedStatement stmt, int columnIndex, boolean valor) throws SQLException {
        stmt.setString(columnIndex, valor ? "S" : "N");
    }

    /**
     * Coloca o valor de um long nos devidos <code>PreparedStatement</code> e índice passados como parâmetros.
     * 
     * @param stmt
     * @param columnIndex
     * @param valor
     * @throws SQLException
     */
    public void setLong(PreparedStatement stmt, int columnIndex, long valor) throws SQLException {
        stmt.setLong(columnIndex, valor);
    }

    /**
     * Coloca o valor de um int nos devidos <code>PreparedStatement</code> e índice passados como parâmetros. O valor passado como
     * parâmetro é um empacotador <code>Integer</code> e não um valor primitivo. Caso o valor seja nulo, o próprio valor de
     * <code>null</code> é configurado.
     * 
     * @param stmt
     * @param columnIndex
     * @param valor
     * @throws SQLException
     */
    public void setInteger(PreparedStatement stmt, int columnIndex, Integer valor) throws SQLException {
        if (valor != null) {
            stmt.setInt(columnIndex, valor.intValue());
        } else {
            stmt.setNull(columnIndex, Types.INTEGER);
        }
    }

    /**
     * Coloca o valor de um int nos devidos <code>PreparedStatement</code> e índice passados como parâmetros.
     * 
     * @param stmt
     * @param columnIndex
     * @param valor
     * @throws SQLException
     */
    public void setInteger(PreparedStatement stmt, int columnIndex, int valor) throws SQLException {
        stmt.setInt(columnIndex, valor);
    }

    /**
     * Coloca o valor de um double nos devidos <code>PreparedStatement</code> e índice passados como parâmetros. Caso o valor seja
     * nulo, o próprio valor de <code>null</code> é configurado. O valor passado como parâmetro é um empacotador
     * <code>Double</code> e não um valor primitivo.
     * 
     * @param stmt
     * @param columnIndex
     * @param valor
     * @throws SQLException
     */
    public void setDouble(PreparedStatement stmt, int columnIndex, Double valor) throws SQLException {
        if (valor != null) {
            stmt.setDouble(columnIndex, valor.doubleValue());
        } else {
            stmt.setNull(columnIndex, Types.DOUBLE);
        }
    }

    /**
     * Coloca o valor de um double nos devidos <code>PreparedStatement</code> e índice passados como parâmetros.
     * 
     * @param stmt
     * @param columnIndex
     * @param valor
     * @throws SQLException
     */
    public void setDouble(PreparedStatement stmt, int columnIndex, double valor) throws SQLException {
        stmt.setDouble(columnIndex, valor);
    }

    /**
     * Coloca o valor de um data nos devidos <code>PreparedStatement</code> e índice passados como parâmetros. Primeiramente, é
     * feita a conversão do valor passado como parâmetro, que é um <code>java.util.Date</code> para um <code>java.sql.Date</code>.
     * Em seguida o valor é configurado.
     * 
     * @param stmt
     * @param columnIndex
     * @param valor
     * @throws SQLException
     */
    public void setDate(PreparedStatement stmt, int columnIndex, Date valor) throws SQLException {
        if (valor != null) {
            java.sql.Date sqlValor = new java.sql.Date(valor.getTime());
            setDate(stmt, columnIndex, sqlValor);
        } else {
            stmt.setNull(columnIndex, Types.DATE);
        }
    }

    /**
     * Coloca o valor de um data nos devidos <code>PreparedStatement</code> e índice passados como parâmetros. O valor de data
     * passado como parâmetro é um <code>java.sql.Date</code>. Caso o valor seja nulo, o próprio valor de <code>null</code> é
     * configurado.
     * 
     * @param stmt
     * @param columnIndex
     * @param valor
     * @throws SQLException
     */
    public void setDate(PreparedStatement stmt, int columnIndex, java.sql.Date valor) throws SQLException {
        if (valor != null) {
            stmt.setDate(columnIndex, valor);
        } else {
            stmt.setNull(columnIndex, Types.DATE);
        }
    }

    /**
     * Coloca o valor de um <code>java.sql.Timestamp</code> nos devidos <code>PreparedStatement</code> e índice passados como
     * parâmetros. O valor de data passado como parâmetro é um <code>java.util.Date</code>. Caso o valor seja nulo, o próprio
     * valor de <code>null</code> é configurado.
     * 
     * @param stmt
     * @param columnIndex
     * @param valor
     * @throws SQLException
     */
    public void setTimestamp(PreparedStatement stmt, int columnIndex, Date valor) throws SQLException {
        Timestamp sqlValor = null;
        if (valor != null)
            sqlValor = new Timestamp(valor.getTime());
        setTimestamp(stmt, columnIndex, sqlValor);
    }

    /**
     * Coloca o valor de um <code>java.sql.Timestamp</code> nos devidos <code>PreparedStatement</code> e índice passados como
     * parâmetros. O valor de data passado como parâmetro é do próprio tipo <code>java.sql.Timestamp</code>. Caso o valor seja
     * nulo, o próprio valor de <code>null</code> é configurado.
     * 
     * @param stmt
     * @param columnIndex
     * @param valor
     * @throws SQLException
     */
    public void setTimestamp(PreparedStatement stmt, int columnIndex, Timestamp valor) throws SQLException {
        if (valor != null) {
            stmt.setTimestamp(columnIndex, valor);
        } else {
            stmt.setNull(columnIndex, Types.TIMESTAMP);
        }
    }

    /**
     * Recupera um valor <code>Long</code> do <code>java.sql.ResultSet</code> e do índice passados como parâmetros. Caso o valor
     * recuperado seja <code>null</code>, o próprio valor nulo é retornado.
     * 
     * @param rs
     * @param columnIndex
     * @return O valor <code>Long</code> correspondente
     * @throws SQLException
     */
    public Long getLong(ResultSet rs, int columnIndex) throws SQLException {
        long result = rs.getLong(columnIndex);
        return rs.wasNull() ? null : new Long(result);
    }

    /**
     * Recupera um valor <code>Long</code> do <code>java.sql.ResultSet</code> e do nome da coluna passados como parâmetros. Caso
     * o valor recuperado seja <code>null</code>, o próprio valor nulo é retornado.
     * 
     * @param rs
     * @param columnName
     * @return O valor <code>Long</code> correspondente
     * @throws SQLException
     */
    public Long getLong(ResultSet rs, String columnName) throws SQLException {
        long result = rs.getLong(columnName);
        return rs.wasNull() ? null : new Long(result);
    }

    /**
     * Recupera um valor <code>Integer</code> do <code>java.sql.ResultSet</code> e do índice passados como parâmetros. Caso o
     * valor recuperado seja <code>null</code>, o próprio valor nulo é retornado.
     * 
     * @param rs
     * @param columnIndex
     * @return O valor <code>Integer</code> correspondente
     * @throws SQLException
     */
    public Integer getInteger(ResultSet rs, int columnIndex) throws SQLException {
        int result = rs.getInt(columnIndex);
        return rs.wasNull() ? null : new Integer(result);
    }

    /**
     * Recupera um valor <code>Integer</code> do <code>java.sql.ResultSet</code> e do nome da coluna passados como parâmetros.
     * Caso o valor recuperado seja <code>null</code>, o próprio valor nulo é retornado.
     * 
     * @param rs
     * @param columnName
     * @return O valor <code>Integer</code> correspondente
     * @throws SQLException
     */
    public Integer getInteger(ResultSet rs, String columnName) throws SQLException {
        int result = rs.getInt(columnName);
        return rs.wasNull() ? null : new Integer(result);
    }

    /**
     * Recupera um valor int do <code>java.sql.ResultSet</code> e do índice passados como parâmetros. Caso o valor seja nulo, uma
     * <code>SQLException</code> é disparada.
     * 
     * @param rs
     * @param columnIndex
     * @return O valor <code>int</code> correspondente
     * @throws SQLException
     */
    public int getInt(ResultSet rs, int columnIndex) throws SQLException {
        int result = rs.getInt(columnIndex);
        if (rs.wasNull()) {
        	result = 0;
        }
        return result;
    }

    /**
     * Recupera um valor int do <code>java.sql.ResultSet</code> e do nome da coluna passados como parâmetros. Caso o valor seja
     * nulo, uma <code>SQLException</code> é disparada.
     * 
     * @param rs
     * @param columnName
     * @return O valor <code>int</code> correspondente
     * @throws SQLException
     */
    public int getInt(ResultSet rs, String columnName) throws SQLException {
        int result = rs.getInt(columnName);
        if (rs.wasNull()) {
        	result = 0;
        }
        return result;
    }

    /**
     * Retorna um valor boolean do <code>java.sql.ResultSet</code> e do índice passados como parâmetros. Primeiramente, é
     * recuperada uma String. Se essa String for um caractere que representa um valor verdadeiro, <code>true</code> é retornado.
     * Caso contrário, é retornado <code>false</code>. Se o valor recuperado for nulo ou vazio, o valor retornado também é
     * <code>false</code>.
     * 
     * @param rs
     * @param columnIndex
     * @return O valor <code>boolean</code> correspondente
     * @throws SQLException
     */
    public boolean getBool(ResultSet rs, int columnIndex) throws SQLException {
        String result = getString(rs, columnIndex);
        if ((result == null) || ("".equals(result))) {
            return false;
        }
        return (CARACTERES_PARA_VERDADEIRO.indexOf(result.charAt(0)) != -1);
    }

    /**
     * Retorna um valor boolean do <code>java.sql.ResultSet</code> e do nome da coluna passados como parâmetros. Primeiramente, é
     * recuperada uma String. Se essa String for um caractere que representa um valor verdadeiro, <code>true</code> é retornado.
     * Caso contrário, é retornado <code>false</code>. Se o valor recuperado for nulo ou vazio, o valor retornado também é
     * <code>false</code>.
     * 
     * @param rs
     * @param columnName
     * @return O valor <code>boolean</code> correspondente
     * @throws SQLException
     */
    public boolean getBool(ResultSet rs, String columnName) throws SQLException {
        String result = getString(rs, columnName);
        if ((result == null) || ("".equals(result))) {
            return false;
        }
        return (CARACTERES_PARA_VERDADEIRO.indexOf(result.charAt(0)) != -1);
    }

    /**
     * Retorna um empacotador <code>Boolean</code> do <code>java.sql.ResultSet</code> e do índice passados como parâmetros.
     * Primeiramente, é recuperada uma String. Se essa String for um caractere que representa um valor verdadeiro, <code>true</code>
     * é retornado. Caso contrário, é retornado <code>false</code>. Se o valor recuperado for nulo ou vazio, o valor retornado
     * também é <code>false</code>.
     * 
     * @param rs
     * @param columnIndex
     * @return O valor <code>Boolean</code> correspondente
     * @throws SQLException
     */
    public Boolean getBoolean(ResultSet rs, int columnIndex) throws SQLException {
        if (getBool(rs, columnIndex)) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    /**
     * Retorna um empacotador <code>Boolean</code> do <code>java.sql.ResultSet</code> e do nome da coluna passados como
     * parâmetros. Primeiramente, é recuperada uma String. Se essa String for um caractere que representa um valor verdadeiro,
     * <code>true</code> é retornado. Caso contrário, é retornado <code>false</code>. Se o valor recuperado for nulo ou vazio,
     * o valor retornado também é <code>false</code>.
     * 
     * @param rs
     * @param columnName
     * @return O valor <code>Boolean</code> correspondente
     * @throws SQLException
     */
    public Boolean getBoolean(ResultSet rs, String columnName) throws SQLException {
        if (getBool(rs, columnName)) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    /**
     * Recupera um valor <code>Double</code> do <code>java.sql.ResultSet</code> e do índice passados como parâmetros. Caso o
     * valor recuperado seja <code>null</code>, o próprio valor nulo é retornado.
     * 
     * @param rs
     * @param columnIndex
     * @return O valor <code>Double</code> correspondente
     * @throws SQLException
     */
    public Double getDouble(ResultSet rs, int columnIndex) throws SQLException {
        double result = rs.getDouble(columnIndex);
        return rs.wasNull() ? null : new Double(result);
    }

    /**
     * Recupera um valor <code>Double</code> do <code>java.sql.ResultSet</code> e do nome da coluna passados como parâmetros.
     * Caso o valor recuperado seja <code>null</code>, o próprio valor nulo é retornado.
     * 
     * @param rs
     * @param columnName
     * @return O valor <code>Double</code> correspondente
     * @throws SQLException
     */
    public Double getDouble(ResultSet rs, String columnName) throws SQLException {
        double result = rs.getDouble(columnName);
        return rs.wasNull() ? null : new Double(result);
    }

    /**
     * Recupera um valor <code>java.util.Date</code> do <code>java.sql.ResultSet</code> e do índice passados como parâmetros.
     * Caso o valor recuperado seja <code>null</code>, o próprio valor nulo é retornado.
     * 
     * @param rs
     * @param columnIndex
     * @return O valor <code>java.util.Date</code> correspondente
     * @throws SQLException
     */
    public Date getDate(ResultSet rs, int columnIndex) throws SQLException {
        Date result = rs.getDate(columnIndex);
        return rs.wasNull() ? null : result;
    }

    /**
     * Recupera um valor <code>java.util.Date</code> do <code>java.sql.ResultSet</code> e do nome da coluna passados como
     * parâmetros. Caso o valor recuperado seja <code>null</code>, o próprio valor nulo é retornado.
     * 
     * @param rs
     * @param columnName
     * @return O valor <code>java.util.Date</code> correspondente
     * @throws SQLException
     */
    public Date getDate(ResultSet rs, String columnName) throws SQLException {
        Date result = rs.getDate(columnName);
        return rs.wasNull() ? null : result;
    }

    /**
     * Recupera um valor <code>java.util.Date</code> do <code>java.sql.ResultSet</code> e do índice passados como parâmetros.
     * Caso o valor recuperado seja <code>null</code>, o próprio valor nulo é retornado.
     * 
     * @param rs
     * @param columnIndex
     * @return O valor <code>java.util.Date</code> correspondente
     * @throws SQLException
     */
    public Date getTimestamp(ResultSet rs, int columnIndex) throws SQLException {
        Timestamp ts = rs.getTimestamp(columnIndex);
        Date result = new Date(ts.getTime());
        return result;
    }

    /**
     * Recupera um valor <code>java.util.Date</code> do <code>java.sql.ResultSet</code> e do nome da coluna passados como
     * parâmetros. Caso o valor recuperado seja <code>null</code>, o próprio valor nulo é retornado.
     * 
     * @param rs
     * @param columnName
     * @return O valor <code>java.util.Date</code> correspondente
     * @throws SQLException
     */
    public Date getTimestamp(ResultSet rs, String columnName) throws SQLException {
        Timestamp ts = rs.getTimestamp(columnName);
        Date result = null;
        if (ts != null) {
            result = new Date(ts.getTime());
        }
        return result;
    }

    /**
     * Recupera um valor <code>Character</code> do <code>java.sql.ResultSet</code> e do índice passados como parâmetros. Caso o
     * valor recuperado seja <code>null</code>, o próprio valor nulo é retornado.
     * 
     * @param rs
     * @param columnIndex
     * @return O valor String correspondente
     * @throws SQLException
     */
    public Character getCharacter(ResultSet rs, int columnIndex) throws SQLException {
        String result = rs.getString(columnIndex);
        return rs.wasNull() ? null : new Character(result.charAt(0));
    }

    /**
     * Recupera um valor <code>char</code> do <code>java.sql.ResultSet</code> e do nome da coluna passados como parâmetros. Caso
     * o valor recuperado seja <code>null</code>, é retornado um espaço em branco
     * 
     * @param rs
     * @param columnName
     * @return O valor String correspondente
     * @throws SQLException
     */
    public char getChar(ResultSet rs, String columnName) throws SQLException {
        String result = rs.getString(columnName);
        return rs.wasNull() ? ' ' : result.charAt(0);
    }

    /**
     * Recupera um valor <code>String</code> do <code>java.sql.ResultSet</code> e do índice passados como parâmetros. Caso o
     * valor recuperado seja <code>null</code>, o próprio valor nulo é retornado.
     * 
     * @param rs
     * @param columnIndex
     * @return O valor String correspondente
     * @throws SQLException
     */
    public String getString(ResultSet rs, int columnIndex) throws SQLException {
        String result = rs.getString(columnIndex);
        return rs.wasNull() ? null : result;
    }

    /**
     * Recupera um valor <code>String</code> do <code>java.sql.ResultSet</code> e do nome da coluna passados como parâmetros.
     * Caso o valor recuperado seja <code>null</code>, o próprio valor nulo é retornado.
     * 
     * @param rs
     * @param columnName
     * @return O valor String correspondente
     * @throws SQLException
     */
    public String getString(ResultSet rs, String columnName) throws SQLException {
        String result = rs.getString(columnName);
        return rs.wasNull() ? null : result;
    }

    /**
     * Preenche parametros do statement baseado nos valores recebidos de uma lista.
     * 
     * @param stmt Objeto <code>PreparedStatement</code> a ser preenchido
     * @param listaParametros Lista de objetos <code>ParametrosSQL</code> a ser preenchido
     * @throws SQLException
     */
    public void preencheParametros(PreparedStatement stmt, List listaParametros) throws SQLException {
        Iterator it = listaParametros.iterator();
        int paramIndex = 1;
        while (it.hasNext()) {
            ParametroSQL param = (ParametroSQL) it.next();
            switch (param.getTipoParametro()) {
                case PARAMTYPE_INTEGER:
                    setInteger(stmt, paramIndex++, (Integer) param.getValor());
                    break;
                case PARAMTYPE_STRING:
                    setString(stmt, paramIndex++, (String) param.getValor());
                    break;
                case PARAMTYPE_DOUBLE:
                    setDouble(stmt, paramIndex++, (Double) param.getValor());
                    break;
                case PARAMTYPE_LONG:
                    setLong(stmt, paramIndex++, (Long) param.getValor());
                    break;
                case PARAMTYPE_DATE:
                    // setDate(stmt, paramIndex++, (Date) param.getValor());
                    setTimestamp(stmt, paramIndex++, (Date) param.getValor());
                    break;
                case PARAMTYPE_TIMESTAMP:
                    setTimestamp(stmt, paramIndex++, (Timestamp) param.getValor());
                    break;
                case PARAMTYPE_BOOLEAN:
                    setBoolean(stmt, paramIndex++, (Boolean) param.getValor());
                    break;
                case PARAMTYPE_BOOLEANSTR:
                    setBoolean(stmt, paramIndex++, (String) param.getValor());
                    break;
                default:
                    throw new SQLException("Tipo de dado inválido para a rotina que preenche parâmetro");
            }
        }
    }

    /**
     * 
     * @param sql
     * @return
     */
    protected StringBuffer getWhereAnd(StringBuffer sql) {
    	StringBuffer whereAnd = new StringBuffer();
    	if (ObjectHelper.isNotEmpty(sql)) {
    		if (sql.toString().toUpperCase().indexOf(" WHERE ") > 0) {
    			whereAnd.append(" AND ");
    		} else {
    			whereAnd.append(" WHERE ");
    		}
    	}
    	return whereAnd;
    }

}