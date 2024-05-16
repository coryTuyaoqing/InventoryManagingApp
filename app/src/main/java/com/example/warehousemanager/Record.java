package com.example.warehousemanager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Record {
    private int idRecord;
    private int idStaff;
    private int idOrder;
    private int idArticle;
    private int articleNr;
    private String operationTime;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US);

    public Record(int idRecord, int idStaff, int idOrder, int idArticle, int articleNr, String operationTime) {
        this.idRecord = idRecord;
        this.idStaff = idStaff;
        this.idOrder = idOrder;
        this.idArticle = idArticle;
        this.articleNr = articleNr;
        this.operationTime = operationTime;
    }

    // Getter and Setter methods
    public int getIdRecord() {
        return idRecord;
    }

    public void setIdRecord(int idRecord) {
        this.idRecord = idRecord;
    }

    public int getIdStaff() {
        return idStaff;
    }

    public void setIdStaff(int idStaff) {
        this.idStaff = idStaff;
    }

    public int getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(int idOrder) {
        this.idOrder = idOrder;
    }

    public int getIdArticle() {
        return idArticle;
    }

    public void setIdArticle(int idArticle) {
        this.idArticle = idArticle;
    }

    public int getArticleNr() {
        return articleNr;
    }

    public void setArticleNr(int articleNr) {
        this.articleNr = articleNr;
    }

    public String getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(String operationTime) {
        this.operationTime = operationTime;
    }

    public Date getOperationTimeAsDate() {
        try {
            return dateFormat.parse(operationTime);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date(0); // return epoch date if parsing fails
        }
    }

    public String getFormattedOperationTime() {
        try {
            Date date = dateFormat.parse(operationTime);
            return dateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return operationTime;
        }
    }

    @Override
    public String toString() {
        return "Record{" +
                "idRecord=" + idRecord +
                ", idStaff=" + idStaff +
                ", idOrder=" + idOrder +
                ", idArticle=" + idArticle +
                ", articleNr=" + articleNr +
                ", operationTime='" + operationTime + '\'' +
                '}';
    }
}
