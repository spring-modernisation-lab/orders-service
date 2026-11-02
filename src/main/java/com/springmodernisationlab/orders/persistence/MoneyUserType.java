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
public class MoneyUserType implements UserType {

    private static final int[] SQL_TYPES = {Types.VARCHAR};

    @Override
    public int[] sqlTypes() {
        return SQL_TYPES.clone();
    }

    @Override
    public Class<?> returnedClass() {
        return Money.class;
    }

    @Override
    public boolean equals(Object x, Object y) {
        return Objects.equals(x, y);
    }

    @Override
    public int hashCode(Object x) {
        return Objects.hashCode(x);
    }

    @Override
    public Object nullSafeGet(
            ResultSet resultSet,
            String[] names,
            SharedSessionContractImplementor session,
            Object owner) throws SQLException {

        String value = resultSet.getString(names[0]);

        return value == null ? null : Money.parse(value);
    }

    @Override
    public void nullSafeSet(
            PreparedStatement statement,
            Object value,
            int index,
            SharedSessionContractImplementor session) throws SQLException {

        if (value == null) {
            statement.setNull(index, Types.VARCHAR);
            return;
        }

        statement.setString(index, ((Money) value).format());
    }

    @Override
    public Object deepCopy(Object value) {
        // Money is immutable.
        return value;
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public Serializable disassemble(Object value) {
        return (Serializable) value;
    }

    @Override
    public Object assemble(Serializable cached, Object owner) {
        return cached;
    }

    @Override
    public Object replace(Object original, Object target, Object owner) {
        return original;
    }
}
