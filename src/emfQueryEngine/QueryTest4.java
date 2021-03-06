package emfQueryEngine;

import java.lang.ClassNotFoundException;
import java.lang.String;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class QueryTest4 {
  public static void main(String[] args) throws SQLException, ClassNotFoundException {
    String usr= "postgres";
    String pwd= "postgresxiufujiang";
    String url="jdbc:postgresql://localhost:5432/postgres";
    try {
      Class.forName("org.postgresql.Driver");
      System.out.println("Success loading Driver");
    } catch(Exception e) {
      System.out.println("Fail loading Driver");
    }
    try {
      Connection conn=DriverManager.getConnection(url, usr, pwd);
      System.out.println("Success connecting server!");
      Statement stmt = conn.createStatement();
      ResultSet rs = stmt.executeQuery("select * from sales where year=2002");
      HashMap<String,mfStructure> resultFinal=new HashMap<String,mfStructure>();
      while(rs.next()) {
        StringBuilder setKey=new StringBuilder();
        String prod=rs.getString("prod");
        setKey.append(prod);
        setKey.append("_");
        int month=rs.getInt("month");
        setKey.append(month);
        String key=setKey.toString();
        mfStructure tempTuple;
        if(resultFinal.containsKey(key)) {
          tempTuple=resultFinal.get(key);
        } else {
          tempTuple=new mfStructure();
          resultFinal.put(key,tempTuple);
          tempTuple.prod=prod;
          tempTuple.month=month;
        }
      }
      rs=stmt.executeQuery("select * from sales where year=2002");
      while(rs.next()) {
        for(mfStructure tempTuple:resultFinal.values()) {
          if(equalsHelper(rs.getString("prod"),tempTuple.prod)&&equalsHelper(rs.getInt("month"),tempTuple.month-1)) {
            tempTuple.sum_1_quant+=rs.getInt("quant");
            tempTuple.cnt_1_quant+=1;
            tempTuple.avg_1_quant=(double) tempTuple.sum_1_quant/tempTuple.cnt_1_quant;
          }
        }
      }
      rs=stmt.executeQuery("select * from sales where year=2002");
      while(rs.next()) {
        for(mfStructure tempTuple:resultFinal.values()) {
          if(equalsHelper(rs.getString("prod"),tempTuple.prod)&&equalsHelper(rs.getInt("month"),tempTuple.month+1)) {
            tempTuple.sum_2_quant+=rs.getInt("quant");
            tempTuple.cnt_2_quant+=1;
            tempTuple.avg_2_quant=(double) tempTuple.sum_2_quant/tempTuple.cnt_2_quant;
          }
        }
      }
      rs=stmt.executeQuery("select * from sales where year=2002");
      while(rs.next()) {
        for(mfStructure tempTuple:resultFinal.values()) {
          if(!equalsHelper(tempTuple.avg_1_quant,0.0)&&!equalsHelper(tempTuple.avg_2_quant,0.0)&&equalsHelper(rs.getString("prod"),tempTuple.prod)&&equalsHelper(rs.getInt("month"),tempTuple.month)&&rs.getInt("quant")>tempTuple.avg_1_quant&&rs.getInt("quant")<tempTuple.avg_2_quant) {
            tempTuple.cnt_3_quant+=1;
          }
        }
      }
      System.out.printf("%-18s ", "prod");
      System.out.printf("%-18s ", "month");
      System.out.printf("%-18s ", "cnt_3_quant");
      System.out.println();
      for(mfStructure tempTuple:resultFinal.values()) {
        int sum_1_quant=tempTuple.sum_1_quant;
        int cnt_1_quant=tempTuple.cnt_1_quant;
        double avg_1_quant=tempTuple.avg_1_quant;
        int sum_2_quant=tempTuple.sum_2_quant;
        int cnt_2_quant=tempTuple.cnt_2_quant;
        double avg_2_quant=tempTuple.avg_2_quant;
        int cnt_3_quant=tempTuple.cnt_3_quant;
        System.out.printf("%-18s ", tempTuple.prod);
        System.out.printf("%-18s ", tempTuple.month);
        if(equalsHelper((double)cnt_3_quant,0.0)) {
          System.out.printf("%-18s ", "Null");
        } else {
          System.out.printf("%-18s ", (double)cnt_3_quant);
        }
        System.out.println();
      }
    } catch(SQLException e) {
      System.out.println("Connection URL or username or password errors!");
      e.printStackTrace();
    }
  }

  public static boolean equalsHelper(int a, int b) {
    return a==b;
  }

  public static boolean equalsHelper(double a, double b) {
    return a==b;
  }

  public static boolean equalsHelper(String a, String b) {
    return a.equals(b);
  }

  public static class mfStructure {
    String prod;

    int month;

    int sum_1_quant;

    int cnt_1_quant;

    double avg_1_quant;

    int sum_2_quant;

    int cnt_2_quant;

    double avg_2_quant;

    int cnt_3_quant;
  }
}
