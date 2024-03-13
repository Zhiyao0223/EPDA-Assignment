/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author USER
 */
@Stateless
public class MedicalReportFacade extends AbstractFacade<MedicalReport> {

    @PersistenceContext(unitName = "EpdaAssignment-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public MedicalReportFacade() {
        super(MedicalReport.class);
    }

    public void addPrognosis(String inputPrognosis, Long reportId, String tableName) {
        updateByAttribute(tableName, reportId, "prognosisDetail", inputPrognosis);
        updateByAttribute(tableName, reportId, "status", 0);
        refreshUpdatedDate(tableName, reportId);
    }

    public List<MedicalReport> getMedicalReport(Long vetId) {
        List<MedicalReport> combinedDataList = new ArrayList<>();
        try {
            String customQuery = "SELECT m.id, m.diagnosisDetail, m.prognosisDetail, m.totalFee, m.status as report_status, m.createdDate, m.updatedDate, p.name as pet_name, u.name as customer_name, t.description, wr.timeslot "
                    + "FROM MedicalReport m "
                    + "JOIN m.appointment a "
                    + "JOIN a.petID p "
                    + "JOIN p.custID u "
                    + "JOIN p.type t "
                    + "JOIN a.schedule wr "
                    + "WHERE wr.staffId.id = :staffId";

            System.out.println("Query: " + customQuery);

            List<Object[]> results = em.createQuery(customQuery)
                    .setParameter("staffId", vetId)
                    .getResultList();

            System.out.println("Query result: " + results.size());

            if (!results.isEmpty()) {
                combinedDataList = convertJsonToMedicalReport(results);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return combinedDataList;
    }

    public List<MedicalReport> convertJsonToMedicalReport(List<Object[]> tmpList) {
        List<MedicalReport> reportList = new ArrayList<>();

        for (Object[] tmp : tmpList) {
            // Provide default value in case null
            Long tmpId = (Long) tmp[0];
            String diagnosisDetail = tmp[1] != null ? (String) tmp[1] : "";
            String prognosisDetail = tmp[2] != null ? (String) tmp[2] : "";
            double totalFee = tmp[3] != null ? (double) tmp[3] : -1;
            int reportStatus = (int) tmp[4];
            Timestamp createdDate = (Timestamp) tmp[5];
            Timestamp updatedDate = (Timestamp) tmp[6];
            String petName = (String) tmp[7];
            String custName = (String) tmp[8];
            String animalType = (String) tmp[9];
            Timestamp timeslot = (Timestamp) tmp[10];

            reportList.add(new MedicalReport(tmpId, diagnosisDetail, prognosisDetail, totalFee, reportStatus, createdDate, updatedDate, petName, custName, animalType, timeslot));
        }
        return reportList;
    }

    public List<MedicalReport> sortByStatus(List<MedicalReport> reportList) {
        Collections.sort(reportList, (MedicalReport r1, MedicalReport r2) -> Integer.compare(r2.getStatus(), r1.getStatus()));
        return reportList;
    }
}
