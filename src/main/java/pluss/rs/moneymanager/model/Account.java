package pluss.rs.moneymanager.model;

import java.math.BigDecimal;

/**
 * Model of account
 */
public class Account {


    private final long id;
    private final String name;
    private BigDecimal balance;

    public Account(long id,
                   String name,
                   BigDecimal balance) {
        this.id = id;
        this.name = name;
        this.balance = balance;
    }

    /**
     * @return identifier of account
     */

    public long getId() {
        return id;
    }


    public String getName() {
        return name;
    }

    /**
     * @return amount of money on this account
     */
    public BigDecimal getBalance() {
        return balance;
    }

    /**
     * Change balance and save to repository
     *
     * @param balance
     *         new balance
     */
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
