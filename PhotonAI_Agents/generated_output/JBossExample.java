import javax.naming.InitialContext;
import org.jboss.naming.remote.client.InitialContextFactory;

/**
 * JBoss migration example for Bank of America code converter project.
 * Converted from WebSphere to JBoss following Google Java Style Guidelines.
 */
public class JBossExample {
    
    /**
     * Looks up a resource using JBoss naming conventions.
     * Migrated from WebSphere javax.naming to JBoss-specific lookup paths.
     */
    public void lookupResource() {
        InitialContext ctx = new InitialContext();
        DataSource ds = (DataSource) ctx.lookup("java:jboss/datasources/MyDataSource");
    }
}