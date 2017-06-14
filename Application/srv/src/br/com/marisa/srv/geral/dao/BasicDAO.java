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
     * Obt�m uma conex�o com o banco de dados. Caso nenhuma conex�o tenha sido configurada via <code>setConn</code>, o DAO obtem
     * uma nova conex�o com o banco atrav�s do m�todo <code>getNewConn</code>., baseado no identificador do banco de dados
     * configurado via <code>setDbIdentifier</code> ou o identificador padr�o definido no arquivo parametros.properties, caso este n�o o
     * identificador n�o esteja sendo valorizado programaticamente.
     * 
     * @throws PersistenciaException
     * @return Returns Um objeto de conex�o com o banco de dados
     */
    public Connection getConn() throws PersistenciaException {
        if (conn != null) {
            return conn;
        }
        return getNewConn();
    }

    /**
     * Obt�m uma nova conex�o com o banco baseado no identificador do banco de dados configurado via <code>setDbIdentifier</code>
     * ou o identificador padr�o definido no arquivo parametros.properties, caso este n�o o identificador n�o esteja sendo valorizado
     * programaticamente. Caso j� exista uma conex�o atual ela � substitu�da pela nova conex�o obtida.
     * 
     * @throws PersistenciaException
     * @return Returns Um objeto de conex�o com o banco de dados
     */
    private Connection getNewConn() throws PersistenciaException {
        if (dbIdentifier == null) {
            dbIdentifier = getDefaultDbIdentifier();
            if (dbIdentifier == null) {
                throw new PersistenciaException(log, "N�o foi poss�vel obter uma conex�o pois n�o foi especificado um valor para o atributo dbIdentifier.");
            }
        }
        
        //log.debug("***************************************************************");
        //log.debug("In�cio do processo para obter uma conex�o com o banco de dados.");

        try {
        	String jndiContex = Parametros.getValue("database." + dbIdentifier + ".jndicontext");
            if (ObjectHelper.isEmpty(jndiContex)) {
                throw new PersistenciaException(log, "O Valor especificado para o atributo dbIdentifier (" + jndiContex + ") n�o foi encontrado no arquivo de configura��es do sistema.");
            }
            
            Context ctx = new InitialContext();
            if(ctx == null ) {
				throw new PersistenciaException(log, "Contexto n�o encontrado");
            }

            //log.debug("Realizando acesso via datasource...");

            Object obj = ctx.lookup(jndiContex);
            DataSource ds = (DataSource)obj;
                
            if (ds == null) {
                throw new PersistenciaException(log, "N�o foi poss�vel obter o datasource.");
            }
            conn = ds.getConnection();
            //log.debug("Conex�o criada com sucesso...");
            
            connectionOwner = true;
            conn.setAutoCommit(true);

        } catch (Exception e) {
            throw new PersistenciaException(log, "Erro ao obter a conexao \"" + dbIdentifier + "\". Motivo: " + e.getMessage(), e);
        }
        return conn;
    }
    
    
    /**
     * Obt�m o dbIdentifier do banco de dados cujos parametros ser�o lidos do parametros.properties
     * 
     * Este m�todo deve ser sobrescrito pelas classes que estendem BasicDAO para fornecer o dbIdentifier necess�rio para o DAO. Caso
     * este m�todo n�o seja sobrescrito, uma conex�o dever� ser passada manualmente para o DAO via <code>setConn(...)</code> ou
     * ent�o um dbIdentifier dever� ser configurado manualmente via <code>setDbIdentifier(...)</code>.
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
     * Configura manualmente uma conex�o para o DAO. Quando a conex�o � configurada desta forma, o DAO perde o <i>owner </i> da
     * mesma. Isto significa que o m�todo <code>close();</code> do DAO <B>N�O </B> fechar� esta conex�o.
     * 
     * Este m�todo � utilizado geralmente quando uma mesma conex�o vai ser compartilhada na execu��o de v�rios DAOs, visando uma
     * transa��o at�mica no banco de dados. (ex: um DAO obtem a conex�o, d� um <code>beginTrans()</code>, realiza uma opera��o,
     * outro DAO � chamado, esta mesma conex�o e transa��o s�o passados para ele, o novo DAO realiza outras opera��es, depois voc�
     * fecha o DAO atual e o DAO original.
     * 
     * @param conn Um objeto de conex�o com o banco de dados.
     */
    public void setConn(Connection conn) {
        this.conn = conn;
        this.connectionOwner = false;
    }

    /**
     * Obt�m a String de dbIdentifier. Veja mais informa��es no m�todo <code>getDefaultDbIdentifier</code>
     * 
     * @see BasicDAO#getDefaultDbIdentifier()
     * @return Returns the dbIdentifier.
     */
    public String getDbIdentifier() {
        return dbIdentifier;
    }

    /**
     * Configura a String de dbIdentifier. Veja mais informa��es no m�todo <code>getDefaultDbIdentifier</code>
     * 
     * @see BasicDAO#getDefaultDbIdentifier()
     * @param dbIdentifier The dbIdentifier to set.
     */
    public void setDbIdentifier(String dbIdentifier) {
        this.dbIdentifier = dbIdentifier;
    }

    /**
     * Indica se o DAO � owner da conex�o. Quando o DAO � owner, ele fechar� a conex�o ao ser executado o m�todo close()
     * 
     * @return Returns the connectionOwner.
     */
    public boolean isConnectionOwner() {
        return connectionOwner;
    }

    /**
     * Informa se o DAO � owner da conex�o. Quando o DAO � owner, ele fechar� a conex�o ao ser executado o m�todo close(). Por
     * default, caso uma conex�o seja atribuida manualmente ao DAO, ele ser� configurado como
     * <code>setConnectionOwner(false);</code>. Caso uma conex�o seja obtida automaticamente pelo DAO, ele ser� configurado como
     * <code>setConnectionOwner(true);</code>
     * 
     * @param connectionOwner
     */
    public void setConnectionOwner(boolean connectionOwner) {
        this.connectionOwner = connectionOwner;
    }

    /**
     * Inicia uma transa��o na conex�o do banco de dados.
     * 
     * @throws PersistenciaException
     */
    public void beginTrans() throws PersistenciaException {
        try {
            getConn().setAutoCommit(false);
        } catch (Exception e) {
            throw new PersistenciaException("Erro ao iniciar uma transa��o com o banco de dados. Motivo: " + e.getMessage(), e);
        }
    }

    /**
     * Realiza um commit na conex�o com o banco
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
            throw new PersistenciaException("Erro ao tentar realizar o commitTrans com a transa��o do banco de dados. Motivo: "
                    + e.getMessage(), e);
        }
    }

    /**
     * Realiza um rollback na conex�o com o banco
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
                    log.error("Erro ao tentar realizar o rollbackTrans com a transa��o do banco de dados. Motivo: "
                            + e.getMessage(), e);
                }
            }
        } catch (Exception e) {
            throw new PersistenciaException("Erro ao tentar realizar o rollbackTrans com a transa��o do banco de dados. Motivo: "
                    + e.getMessage(), e);
        }
    }

    /**
     * Verifica se a conex�o est� em estado de transa��o
     * 
     * @return Retorna <code>true</code> se a conex�o estiver em transa��o. Caso contr�rio, retorna <code>false</code>
     * @throws PersistenciaException
     */
    public boolean isInTransaction() throws PersistenciaException {
        if (conn == null)
            return false;

        try {
            return !conn.getAutoCommit();
        } catch (SQLException e) {
            throw new PersistenciaException("Ocorreu um erro ao tentar obter o estado da transa��o na conex�o. Motivo: "
                    + e.getMessage());
        }
    }

    /**
     * Fecha a conex�o se e somente se este DAO for owner da mesma. Caso contr�rio o m�todo n�o faz nada. � importante dar commit ou
     * rollback em qualquer transa��o antes de tentar efetuar um close.
     * 
     * @return Retorna <code>true</code> quando o DAO era owner da conex�o, fechando a mesma. Retorna <code>false</code> quando
     *         o DAO n�o era owner da conex�o, mantendo a mesma aberta.
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
                    throw new PersistenciaException("Ocorreu um erro ao tentar fechar a conex�o. Motivo: " + e.getMessage());
                }

                if (wasInTransaction) {
                    log.debug("AVISO: Executado rollback automatico na conex�o que foi fechada.");
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Fecha um Statement do JDBC caso o objeto n�o seja nulo.
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
     * Fecha um ResultSet do JDBC caso o objeto n�o seja nulo.
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
     * Fecha um ResultSet do JDBC caso o objeto n�o seja nulo.
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
     * Obt�m o sequence gerado no �ltimo INSERT realizado na conex�o
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
                throw new PersistenciaException("N�o existe last identity para a pesquisa realizada.");
            }
        } catch (SQLException e) {
            log.error("Erro na captura do last identity.", e);
            throw new PersistenciaException("Erro na captura do last identity.", e);
        }
        return identity;
    }

    /**
     * Este m�todo deve ser chamado toda vez que uma transa��o for requerida por um m�todo. Isto ocorre em m�todos que
     * conhecidamente atualizam mais de uma tabela por vez.
     * 
     * @throws PersistenciaException Lan�ada caso n�o exista uma transa��o iniciada.
     */
    public void requiresTransaction() throws PersistenciaException {
        if (!isInTransaction()) {
            throw new PersistenciaException("Este m�todo requer uma transa��o para ser executado.");
        }
    }

    /**
     * Coloca o valor de uma String nos devidos <code>PreparedStatement</code> e �ndice passados como par�metros. Caso o valor
     * seja nulo, o pr�prio valor de <code>null</code> � configurado.
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
     * Coloca o valor de um char nos devidos <code>PreparedStatement</code> e �ndice passados como par�metros.
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
     * Coloca o valor de um Character nos devidos <code>PreparedStatement</code> e �ndice passados como par�metros. Caso o valor
     * seja nulo, o pr�prio valor de <code>null</code> � configurado.
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
     * Coloca o valor de um long nos devidos <code>PreparedStatement</code> e �ndice passados como par�metros. Caso o valor seja
     * nulo, o pr�prio valor de <code>null</code> � configurado.
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
     * Coloca o valor de um boolean nos devidos <code>PreparedStatement</code> e �ndice passados como par�metros. O valor passado
     * como par�metro � uma String e n�o um boolean. O m�todo verifica se essa String representa um valor verdadeiro. Caso positivo,
     * <code>true</code> � configurado. Caso contr�rio, <code>false</code> � configurado. Caso o valor seja nulo, o pr�prio
     * valor de <code>null</code> � configurado.
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
     * Coloca o valor de um boolean nos devidos <code>PreparedStatement</code> e �ndice passados como par�metros. O valor passado
     * como par�metro � um empacotador <code>Boolean</code> e n�o um valor primitivo. Caso o valor seja nulo, o pr�prio valor de
     * <code>null</code> � configurado.
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
     * Coloca o valor de um boolean nos devidos <code>PreparedStatement</code> e �ndice passados como par�metros. Caso o valor
     * seja <code>true</code>, a String "S" � configurada. Caso contr�rio, a String "N" � configurada.
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
     * Coloca o valor de um long nos devidos <code>PreparedStatement</code> e �ndice passados como par�metros.
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
     * Coloca o valor de um int nos devidos <code>PreparedStatement</code> e �ndice passados como par�metros. O valor passado como
     * par�metro � um empacotador <code>Integer</code> e n�o um valor primitivo. Caso o valor seja nulo, o pr�prio valor de
     * <code>null</code> � configurado.
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
     * Coloca o valor de um int nos devidos <code>PreparedStatement</code> e �ndice passados como par�metros.
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
     * Coloca o valor de um double nos devidos <code>PreparedStatement</code> e �ndice passados como par�metros. Caso o valor seja
     * nulo, o pr�prio valor de <code>null</code> � configurado. O valor passado como par�metro � um empacotador
     * <code>Double</code> e n�o um valor primitivo.
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
     * Coloca o valor de um double nos devidos <code>PreparedStatement</code> e �ndice passados como par�metros.
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
     * Coloca o valor de um data nos devidos <code>PreparedStatement</code> e �ndice passados como par�metros. Primeiramente, �
     * feita a convers�o do valor passado como par�metro, que � um <code>java.util.Date</code> para um <code>java.sql.Date</code>.
     * Em seguida o valor � configurado.
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
     * Coloca o valor de um data nos devidos <code>PreparedStatement</code> e �ndice passados como par�metros. O valor de data
     * passado como par�metro � um <code>java.sql.Date</code>. Caso o valor seja nulo, o pr�prio valor de <code>null</code> �
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
     * Coloca o valor de um <code>java.sql.Timestamp</code> nos devidos <code>PreparedStatement</code> e �ndice passados como
     * par�metros. O valor de data passado como par�metro � um <code>java.util.Date</code>. Caso o valor seja nulo, o pr�prio
     * valor de <code>null</code> � configurado.
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
     * Coloca o valor de um <code>java.sql.Timestamp</code> nos devidos <code>PreparedStatement</code> e �ndice passados como
     * par�metros. O valor de data passado como par�metro � do pr�prio tipo <code>java.sql.Timestamp</code>. Caso o valor seja
     * nulo, o pr�prio valor de <code>null</code> � configurado.
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
     * Recupera um valor <code>Long</code> do <code>java.sql.ResultSet</code> e do �ndice passados como par�metros. Caso o valor
     * recuperado seja <code>null</code>, o pr�prio valor nulo � retornado.
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
     * Recupera um valor <code>Long</code> do <code>java.sql.ResultSet</code> e do nome da coluna passados como par�metros. Caso
     * o valor recuperado seja <code>null</code>, o pr�prio valor nulo � retornado.
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
     * Recupera um valor <code>Integer</code> do <code>java.sql.ResultSet</code> e do �ndice passados como par�metros. Caso o
     * valor recuperado seja <code>null</code>, o pr�prio valor nulo � retornado.
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
     * Recupera um valor <code>Integer</code> do <code>java.sql.ResultSet</code> e do nome da coluna passados como par�metros.
     * Caso o valor recuperado seja <code>null</code>, o pr�prio valor nulo � retornado.
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
     * Recupera um valor int do <code>java.sql.ResultSet</code> e do �ndice passados como par�metros. Caso o valor seja nulo, uma
     * <code>SQLException</code> � disparada.
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
     * Recupera um valor int do <code>java.sql.ResultSet</code> e do nome da coluna passados como par�metros. Caso o valor seja
     * nulo, uma <code>SQLException</code> � disparada.
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
     * Retorna um valor boolean do <code>java.sql.ResultSet</code> e do �ndice passados como par�metros. Primeiramente, �
     * recuperada uma String. Se essa String for um caractere que representa um valor verdadeiro, <code>true</code> � retornado.
     * Caso contr�rio, � retornado <code>false</code>. Se o valor recuperado for nulo ou vazio, o valor retornado tamb�m �
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
     * Retorna um valor boolean do <code>java.sql.ResultSet</code> e do nome da coluna passados como par�metros. Primeiramente, �
     * recuperada uma String. Se essa String for um caractere que representa um valor verdadeiro, <code>true</code> � retornado.
     * Caso contr�rio, � retornado <code>false</code>. Se o valor recuperado for nulo ou vazio, o valor retornado tamb�m �
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
     * Retorna um empacotador <code>Boolean</code> do <code>java.sql.ResultSet</code> e do �ndice passados como par�metros.
     * Primeiramente, � recuperada uma String. Se essa String for um caractere que representa um valor verdadeiro, <code>true</code>
     * � retornado. Caso contr�rio, � retornado <code>false</code>. Se o valor recuperado for nulo ou vazio, o valor retornado
     * tamb�m � <code>false</code>.
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
     * par�metros. Primeiramente, � recuperada uma String. Se essa String for um caractere que representa um valor verdadeiro,
     * <code>true</code> � retornado. Caso contr�rio, � retornado <code>false</code>. Se o valor recuperado for nulo ou vazio,
     * o valor retornado tamb�m � <code>false</code>.
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
     * Recupera um valor <code>Double</code> do <code>java.sql.ResultSet</code> e do �ndice passados como par�metros. Caso o
     * valor recuperado seja <code>null</code>, o pr�prio valor nulo � retornado.
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
     * Recupera um valor <code>Double</code> do <code>java.sql.ResultSet</code> e do nome da coluna passados como par�metros.
     * Caso o valor recuperado seja <code>null</code>, o pr�prio valor nulo � retornado.
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
     * Recupera um valor <code>java.util.Date</code> do <code>java.sql.ResultSet</code> e do �ndice passados como par�metros.
     * Caso o valor recuperado seja <code>null</code>, o pr�prio valor nulo � retornado.
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
     * par�metros. Caso o valor recuperado seja <code>null</code>, o pr�prio valor nulo � retornado.
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
     * Recupera um valor <code>java.util.Date</code> do <code>java.sql.ResultSet</code> e do �ndice passados como par�metros.
     * Caso o valor recuperado seja <code>null</code>, o pr�prio valor nulo � retornado.
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
     * par�metros. Caso o valor recuperado seja <code>null</code>, o pr�prio valor nulo � retornado.
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
     * Recupera um valor <code>Character</code> do <code>java.sql.ResultSet</code> e do �ndice passados como par�metros. Caso o
     * valor recuperado seja <code>null</code>, o pr�prio valor nulo � retornado.
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
     * Recupera um valor <code>char</code> do <code>java.sql.ResultSet</code> e do nome da coluna passados como par�metros. Caso
     * o valor recuperado seja <code>null</code>, � retornado um espa�o em branco
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
     * Recupera um valor <code>String</code> do <code>java.sql.ResultSet</code> e do �ndice passados como par�metros. Caso o
     * valor recuperado seja <code>null</code>, o pr�prio valor nulo � retornado.
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
     * Recupera um valor <code>String</code> do <code>java.sql.ResultSet</code> e do nome da coluna passados como par�metros.
     * Caso o valor recuperado seja <code>null</code>, o pr�prio valor nulo � retornado.
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
                    throw new SQLException("Tipo de dado inv�lido para a rotina que preenche par�metro");
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