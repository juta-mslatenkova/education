package com.accounts.dao;

import com.accounts.model.Account;
import com.accounts.utils.HibernateSessionFactoryUtil;
import com.accounts.utils.MakingEntriesToDbHelper;
import org.hibernate.Session;

import java.util.List;

import org.hibernate.Transaction;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Data Access Model Concrete class - responsible for getting data from a datasource
 */
public class AccountDAOImpl_PostgreSQL implements AccountDAO {

    private Account account;

    @Override
    public Account findById(int id) {
        account = HibernateSessionFactoryUtil.getSessionFactory().openSession().get(Account.class, id);
        return account;
    }

    @Override
    public void save(Account account) {
        checkArgument(MakingEntriesToDbHelper.isLoginAndPassComboUnique(account, findAll()),
                "Oooops, data was not added as the website and login combination is not unique");
        performActionsWithDatabase(Actions.SAVE, account);
    }


    @Override
    public void update(Account account) {
        checkArgument(MakingEntriesToDbHelper.isLoginAndPassComboUnique(account, findAll()),
                "Oooops, data was not added as the website and login combination is not unique");
        performActionsWithDatabase(Actions.UPDATE, account);
    }

    @Override
    public void delete(Account account) {
        performActionsWithDatabase(Actions.DELETE, account);
    }

    @Override
    public List<Account> findAll() {
        List<Account> accountList = (List<Account>) HibernateSessionFactoryUtil.getSessionFactory().openSession()
                .createQuery("FROM Account").list();
        return accountList;
    }

    enum Actions {
        DELETE, SAVE, UPDATE
    }

    private void performActionsWithDatabase(Actions actions, Account account) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        try {
            switch (actions) {
                case SAVE:
                    session.save(account);
                    break;
                case UPDATE:
                    session.update(account);
                    break;
                case DELETE:
                    session.delete(account);
                    break;
                default:
                    throw new IllegalStateException("Unknown action");
            }
            transaction.commit();

        } finally {
            session.close();
        }
    }


}

