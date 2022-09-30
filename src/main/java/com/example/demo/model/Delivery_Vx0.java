package com.example.demo.model;

import com.example.demo.dataBaseConfig.Database;
import com.example.demo.dataBaseConfig.Logging;
import com.example.demo.object.DeliveryInputParams;
import com.example.demo.object.DeliveryResult;

import java.sql.*;

public class Delivery_Vx0 {

    public DeliveryResult deliveryTransaction(DeliveryInputParams input) {
        long d_id, no_o_id, c_id;
        double ol_total;
        long[] orderIDs;
        boolean newOrderRemoved;
        StringBuffer query;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/secured_service_db", "root", "@zhila920618261*");
            orderIDs = new long[10];
            Statement stmt = Database.createStatement(con);
            for (d_id = 1; d_id <= 10; d_id++) {
                do {
                    no_o_id = -1;
                    query = new StringBuffer();
                    query.append("SELECT * FROM (");
                    query.append("SELECT no_o_id");
                    query.append(" FROM neworder");
                    query.append(" WHERE no_w_id = ");
                    query.append(input.getW_id());
                    query.append("' ORDER BY no_o_id ASC");
                    query.append(") WHERE rownum = 1");
                    try {
                        ResultSet rs = stmt.executeQuery(query.toString());
                        if (rs.next()) {
                            no_o_id = rs.getLong("no_o_id");
                        }
                        orderIDs[(int) d_id - 1] = no_o_id;
                        rs.close();
                        stmt.close();
                    } catch (SQLException e) {
                        throw new Exception(query.toString() + " : " + e.getMessage());
                    }

                    newOrderRemoved = false;
                    if (no_o_id != -1) {
                        PreparedStatement psdelete = con.prepareStatement("DELETE FROM neworder  " +
                                " WHERE w_id = '" + input.getW_id() + "' ");

                        try {
                            int result = psdelete.executeUpdate();
                            if (result > 0) {
                                newOrderRemoved = true;
                            }
                        } catch (SQLException e) {
                            Logging.error(query.toString() + " : " + e.getMessage());
                            throw new Exception(psdelete.toString() + " : " + e.getMessage());
                        }
                    }
                } while (no_o_id != -1 && !newOrderRemoved);

                if (no_o_id != -1) {
                    PreparedStatement ps3 = con.prepareStatement(
                            "SELECT c_id " +
                                    "  FROM neworder" +
                                    " WHERE w_id = '" + input.getW_id() + "'");

                    try {
                        ResultSet rs = ps3.executeQuery();
                        if (!rs.next()) {
                            Logging.error(query.toString() + " not found ");
                            throw new Exception("O_ID=" + no_o_id + " O_D_ID=" + d_id + " O_W_ID='" + input.getW_id() + "' not found!");
                        }
                        c_id = rs.getLong("o_c_id");
                        rs.close();
                    } catch (SQLException e) {
                        Logging.error(query.toString() + " : " + e.getMessage());
                        throw new Exception(ps3.toString() + " : " + e.getMessage());
                    }
                    PreparedStatement ps4 = con.prepareStatement("UPDATE neworder " +
                            " SET o_carrier_id = ? " +
                            " WHERE w_id  =  '" + input.getW_id() + "' ");

                    int result = ps4.executeUpdate();
                    if (result == 0) {
                        Logging.error(query.toString() + " not found ");
                        throw new Exception("O_ID=" + no_o_id + " O_D_ID=" + d_id + " O_W_ID=" + input.getW_id() + " not found!");
                    }
                    PreparedStatement ps5 = con.prepareStatement("UPDATE neworder " +
                            "   SET ol_delivery_d =  SYSDATE " +
                            " WHERE w_id = '" + input.getW_id() + "'");
                    result = ps5.executeUpdate();
                    if (result == 0) {
                        Logging.error(query.toString() + " not found ");
                        throw new Exception("OL_O_ID=" + no_o_id + " OL_D_ID=" + d_id + " OL_W_ID=" + input.getW_id() + " not found!");
                    }
                    PreparedStatement ps6 = con.prepareStatement("SELECT SUM(ol_amount) AS ol_total" +
                            " FROM neworder " +
                            " WHERE w_id = '" + input.getW_id() + "'");

                    ResultSet rs = ps6.executeQuery();
                    if (!rs.next()) {
                        Logging.error(query.toString() + " not found");
                        throw new Exception("OL_O_ID=" + no_o_id + " OL_D_ID=" + d_id + " OL_W_ID=" + input.getW_id() + " not found!");
                    }
                    ol_total = rs.getDouble("ol_total");
                    rs.close();
                    rs = null;

                    PreparedStatement ps7 = con.prepareStatement("UPDATE newcustomer SET c_balance = c_balance + ? " +
                            ", c_delivery_cnt = c_delivery_cnt + 1" +
                            " WHERE c_id =  '" + c_id + "'" +
                            " AND c_w_id  '" + input.getW_id() + "'");
                    result = ps7.executeUpdate();
                    if (result == 0) {
                        Logging.error(query.toString() + " not found");
                        throw new Exception("C_ID=" + c_id + " C_W_ID=" + input.getW_id() + " C_D_ID=" + d_id + " not found!");
                    }
                }
            }
            con.commit();
            stmt.close();
        } catch (Exception e) {
            Logging.trace(" deliveryTransaction : " + e.getMessage());
        }
        return new DeliveryResult();
    }

    public DeliveryResult deliveryTransactionTest(DeliveryInputParams input) {
        DeliveryResult delivery = new DeliveryResult();
        delivery.setW_id(input.getW_id());
        delivery.setdDate(input.getDeliverydate());

        return delivery;
    }
}