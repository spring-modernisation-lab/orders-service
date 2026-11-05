package com.springmodernisationlab.orders.persistence;

import com.springmodernisationlab.orders.domain.Money;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Objects;

/**
 * Persists {@link Money} in a single VARCHAR column as "amount currency".
 */
public class MoneyUserType implements UserType<Money> {

    @Override
    public int getSqlType() {
        return Types.VARCHAR;
    }

    @Override
    public Class<Money> returnedClass() {
        return Money.class;
    }

    @Override
    public boolean equals(Money x, Money y) {
        return Objects.equals(x, y);
    }

    @Override
    public int hashCode(Money x) {
        return Objects.hashCode(x);
    }

    @Override
    public Money nullSafeGet(ResultSet resultSet, int position, SharedSessionContractImplementor session, Object owner)
            throws SQLException {

        String value = resultSet.getString(position);
        return value == null ? null : Money.parse(value);
    }

    @Override
    public void nullSafeSet(PreparedStatement statement, Money value, int index, SharedSessionContractImplementor session)
            throws SQLException {

        if (value == null) {
            statement.setNull(index, Types.VARCHAR);
            return;
        }

        statement.setString(index, value.format());
    }

    @Override
    public Money deepCopy(Money value) {
        // Money is immutable.
        return value;
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public Serializable disassemble(Money value) {
        return (Serializable) value;
    }

    @Override
    public Money assemble(Serializable cached, Object owner) {
        return (Money) cached;
    }

    @Override
    public Money replace(Money original, Money target, Object owner) {
        return original;
    }
}
