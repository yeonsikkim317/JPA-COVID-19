
package service;

import java.util.ArrayList;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import lombok.extern.slf4j.Slf4j;
import model.domain.SeoulPopulation;
import model.domain.SeoulCovid;
import util.PublicCommon;

@Slf4j

public class SeoulCovidCRUDService {

//<<<<<<< Updated upstream:step10_JPA_mp/src/service/SeoulCovidCRUDService.java
//=======
	public static void main(String[] args) {
		
		System.out.println("=========INSERT==========");
		createSeoulCovid(10000, "재웅 통해서 감염", "20/12/11", "강서구");
		System.out.println("\n");
		System.out.println("=========UPDATE==========");
		updateSeoulCovid(10000, "온라인 상에서 감염");
		System.out.println("\n");
		System.out.println("=========SELECT==========");
		findElement(10000);
		System.out.println("\n");
	    System.out.println("=========DELETE==========");
		deleteElement(10000);
	}

//>>>>>>> Stashed changes:step10_JPA_mp/src/run/test/RunEmployeeCRUD.java
	public static void createSeoulCovid(int pnumber, String history, String caughtdate, String location) {
		EntityManager em = PublicCommon.getEntityManger();
		EntityTransaction tx = em.getTransaction();
		tx.begin();

		try {
			SeoulPopulation d50 = (SeoulPopulation) em.createNamedQuery("SeoulPopulation.location").setParameter("location", location).getSingleResult();

			SeoulCovid seoulcovid = SeoulCovid.builder().patientnumber(Integer.toUnsignedLong(pnumber)).history(history).caughtdate(caughtdate).location(d50).build();

			em.persist(seoulcovid);

			tx.commit();
			log.warn("생성 기록");
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		} finally {
			em.close();
		}
	}
	

	public static void updateSeoulCovid(int index, String content) {
		EntityManager em = PublicCommon.getEntityManger();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		try {
			SeoulCovid seoulcovid = em.find(SeoulCovid.class, Integer.toUnsignedLong(index));

			System.out.println("update 전 : " + seoulcovid);
			seoulcovid.setHistory(content);
			tx.commit();
			log.warn("업데이트 기록");
			// after update
			System.out.println("update 후 : " + seoulcovid);
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		} finally {
			em.close();
		}
	}
	

	public static SeoulCovid findElement(int index) {
		EntityManager em = PublicCommon.getEntityManger();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		SeoulCovid seoulcovid = new SeoulCovid();
		try {
			seoulcovid = (SeoulCovid) em.createNamedQuery("SeoulCovid.findByPnumber").setParameter("patientnumber", Integer.toUnsignedLong(index)).getSingleResult();
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		} finally {
			em.close();
		}
		return seoulcovid;
	}
	public static void deleteElement(int index) {
		EntityManager em = PublicCommon.getEntityManger();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		try {
//<<<<<<< Updated upstream:step10_JPA_mp/src/service/SeoulCovidCRUDService.java
//			locations = (ArrayList<String>) em.createNamedQuery("SeoulPopulation.locations").getResultList();
//=======
			SeoulCovid seoulcovid = em.find(SeoulCovid.class, Integer.toUnsignedLong(index));
			em.remove(seoulcovid);
			tx.commit();
			log.warn("삭제 기록");
//>>>>>>> Stashed changes:step10_JPA_mp/src/run/test/RunEmployeeCRUD.java
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		} finally {
			em.close();
		}
	}
	
	public static ArrayList<String> getAllLocations() {
		EntityManager em = PublicCommon.getEntityManger();
		EntityTransaction tx = em.getTransaction();
		ArrayList<String> locations = new ArrayList<String>();
		tx.begin();
		try {
			locations = (ArrayList<String>) em.createNamedQuery("SeoulPopulation.locations").getResultList();
			
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		} finally {
			em.close();
		}
		return locations;
	}
	
	public static ArrayList<String> getAllDates() {
		EntityManager em = PublicCommon.getEntityManger();
		EntityTransaction tx = em.getTransaction();
		ArrayList<String> datelist = new ArrayList<String>();
		tx.begin();
		try {
			datelist = (ArrayList<String>) em.createNamedQuery("SeoulCovid.getDateList").getResultList();
			
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		} finally {
			em.close();
		}
		return datelist;
	}
//<<<<<<< Updated upstream:step10_JPA_mp/src/service/SeoulCovidCRUDService.java
}
//=======
	

//>>>>>>> Stashed changes:step10_JPA_mp/src/run/test/RunEmployeeCRUD.java
