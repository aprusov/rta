import com.alex.rta.dbsetup.create;
import com.db.alex.rta.codegen.tables.Accounts;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

public class setupDb {
    private final Connection connection;
    private final SQLDialect dialect;

    public setupDb(Connection connection, SQLDialect dialect) {
        this.connection = connection;
        this.dialect = dialect;
    }

    public void execute(Map<Long, Double> initial) throws SQLException {
        new create(connection).execute();

        DSLContext db = DSL.using(connection, dialect);
        initial.forEach((id, amount) ->
                db.insertInto(Accounts.ACCOUNTS)
                        .columns(Accounts.ACCOUNTS.ID, Accounts.ACCOUNTS.BALANCE)
                        .values(id, amount)
        );
    }

}
