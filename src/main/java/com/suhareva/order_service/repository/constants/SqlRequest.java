package com.suhareva.order_service.repository.constants;

public class SqlRequest {
    public final static String INSERT_ORDER = "INSERT INTO ORDERS (ID, NUMBER, TOTAL_AMOUNT, ORDER_DATE, DELIVERY_ADDRESS," +
            " CLIENT, PAYMENT_TYPE, DELIVERY_TYPE) VALUES (?,?,?,?,?,?,?,?)";
    public final static String INSERT_PRODUCT = "INSERT INTO PRODUCTS (ID, ARTICLE, NAME, QUANTITY, AMOUNT, ORDER_ID) " +
            "VALUES (?,?,?,?,?,?)";
    public final static String SELECT_ORDERS = "SELECT ID, NUMBER, TOTAL_AMOUNT, ORDER_DATE, DELIVERY_ADDRESS," +
            " CLIENT, PAYMENT_TYPE, DELIVERY_TYPE FROM ORDERS";
    public final static String SELECT_ORDER_BY_NUMBER = "SELECT ID, NUMBER, TOTAL_AMOUNT, ORDER_DATE, DELIVERY_ADDRESS," +
            " CLIENT, PAYMENT_TYPE, DELIVERY_TYPE FROM ORDERS WHERE NUMBER = ?";
    public final static String SELECT_PRODUCT_LIST_BY_ORDER_ID = "SELECT ID, ARTICLE, NAME, QUANTITY, AMOUNT, ORDER_ID FROM PRODUCTS" +
            " WHERE ORDER_ID = ?";
    public final static String SELECT_ORDER_BY_DATE_AND_GREAT_TOTAL_AMOUNT = "SELECT ID, NUMBER, TOTAL_AMOUNT, ORDER_DATE, DELIVERY_ADDRESS," +
            " CLIENT, PAYMENT_TYPE, DELIVERY_TYPE FROM ORDERS WHERE ORDER_DATE BETWEEN ? AND ? and TOTAL_AMOUNT > ?";
    public final static String SELECT_ORDER_BY_PERIOD_DATE = "SELECT ID, NUMBER, TOTAL_AMOUNT, ORDER_DATE, DELIVERY_ADDRESS," +
            " CLIENT, PAYMENT_TYPE, DELIVERY_TYPE FROM ORDERS WHERE ORDER_DATE BETWEEN ? AND ?";
    private final static String SELECT_ALL_PRODUCT = "SELECT ID, ARTICLE, NAME, QUANTITY, AMOUNT, ORDER_ID FROM PRODUCTS";

    public static String buildSelectProductsByOrderIds(int size) {
        return SELECT_ALL_PRODUCT + " WHERE " + appendOrderIdsParams(size);
    }

    private static String appendOrderIdsParams(int size) {
        StringBuilder sql = new StringBuilder("ORDER_ID IN (");
        sql.append("?,".repeat(Math.max(0, size)));
        sql.replace(sql.length() - 1, sql.length(), ")");
        return sql.toString();
    }
}
