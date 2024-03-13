/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.Iterator;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author USER
 */
@Stateless
public class WorkingRotaFacade extends AbstractFacade<WorkingRota> {

    @PersistenceContext(unitName = "EpdaAssignment-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public WorkingRotaFacade() {
        super(WorkingRota.class);
    }

    public List<WorkingRota> getAvaiableSlot() {
        List<WorkingRota> scheduleList = findAll();

        // Remove deleted user
        Iterator<WorkingRota> iterator = scheduleList.iterator();
        while (iterator.hasNext()) {
            WorkingRota singleSchedule = iterator.next();
            if (singleSchedule.getStatus() == 1) {
                iterator.remove();
            }
        }
        return scheduleList;
    }
}
