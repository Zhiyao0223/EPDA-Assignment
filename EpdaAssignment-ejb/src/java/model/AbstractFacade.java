/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 *
 * @author USER
 */
public abstract class AbstractFacade<T> {

    private Class<T> entityClass;

    public AbstractFacade(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected abstract EntityManager getEntityManager();

    public void create(T entity) {
        getEntityManager().persist(entity);
    }

    public void edit(T entity) {
        getEntityManager().merge(entity);
    }

    /*
    Update row by primary key
     */
    public void updateByAttribute(String tableName, Object primaryKey, String attribute, Object value) {
        String jpql = "UPDATE " + tableName + " SET " + attribute + " = :value WHERE ID = :primaryKey";
        getEntityManager().createQuery(jpql)
                .setParameter("value", value)
                .setParameter("primaryKey", primaryKey)
                .executeUpdate();
    }

    public void remove(T entity) {
        getEntityManager().remove(getEntityManager().merge(entity));
    }

    public T find(Object id) {
        return getEntityManager().find(entityClass, id);
    }

    // Search row by column
    public T findByAttribute(String attributeName, Object attributeValue) {
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery<T> cq = cb.createQuery(entityClass);
        javax.persistence.criteria.Root<T> root = cq.from(entityClass);
        cq.select(root).where(cb.equal(root.get(attributeName), attributeValue));

        try {
            return getEntityManager().createQuery(cq).getSingleResult();
        } catch (NoResultException e) {
            // Log the exception or handle it in some other way
            // For simplicity, we're just printing the exception message
            System.out.println("No result found for attribute: " + attributeName);
            return null;
        }
    }

    public List<T> findAll() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        return getEntityManager().createQuery(cq).getResultList();
    }

    public List<T> findRange(int[] range) {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    // Method to retrieve column names for a given table
    public String[] getColumnNames(String tableName) throws SQLException {
        List<String> columnNames = new ArrayList<>();

        Connection connection = null;
        ResultSet rs = null;
        try {
            connection = getEntityManager().unwrap(Connection.class);
            DatabaseMetaData metaData = connection.getMetaData();
            rs = metaData.getColumns(null, null, tableName, null);

            while (rs.next()) {
                columnNames.add(rs.getString("COLUMN_NAME"));
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (connection != null) {
                connection.close();
            }
        }

        // Convert list to array
        String[] columnNamesArray = new String[columnNames.size()];
        columnNamesArray = columnNames.toArray(columnNamesArray);

        return columnNamesArray;
    }

}
