package service;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.json.simple.JSONArray;

import lombok.extern.slf4j.Slf4j;
import model.domain.SeoulLocRelation;
import util.PublicCommon;

@Slf4j

public class DateLocationService {
	private static EntityManager em;
	private static ArrayList<String> locationlist;

	static {
		em = PublicCommon.getEntityManger();
		locationlist = SeoulCovidCRUDService.getAllLocations();
	}

	public static HashMap<String, HashMap<String, Object>> getDLSum() {
		em = PublicCommon.getEntityManger();
		EntityTransaction tx = em.getTransaction();
		ArrayList<String> locationlist = SeoulCovidCRUDService.getAllLocations();
		ArrayList<String> datelist = SeoulCovidCRUDService.getAllDates();
		HashMap<String, HashMap<String, Object>> bigmap = new HashMap<String, HashMap<String, Object>>();
//		tx.begin();
//		try {
//			for (String date : datelist) {
//				HashMap<String, Object> smallmap = new HashMap<String, Object>();
//
//				for (String location : locationlist) {
//					smallmap.put(location, em.createNativeQuery("select " + location + " from loctime where 확진일=?")
//							.setParameter(1, date).getSingleResult());
//				}
//				bigmap.put(date, smallmap);
//			}
//		} catch (Exception e) {
//			tx.rollback();
//			e.printStackTrace();
//		} finally {
//			em.close();
//		}
		return bigmap;
	}

	public static String getFileContent(String filename) {
		StringBuffer sbuf = new StringBuffer();
		String fileContent = null;
		InputStream is;
		try {
			is = new FileInputStream(filename);
			InputStreamReader isr = new InputStreamReader(is, "UTF-8");
			BufferedReader br = new BufferedReader(isr);
			String str;
			while ((str = br.readLine()) != null) {
				sbuf.append(str + "\r\n");
			}
			fileContent = sbuf.toString();
			is.close();
			isr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileContent;
	}

	public static HashMap<String, ArrayList<ArrayList<Long>>> mapping(JSONArray jsonarray) {
		HashMap<String, ArrayList<ArrayList<Long>>> map = new HashMap<String, ArrayList<ArrayList<Long>>>();
		for (Object feature : jsonarray.toArray()) {
			HashMap map1 = (HashMap) feature;
			String id = (String) map1.get("id");
			HashMap map2 = (HashMap) map1.get("geometry");
			ArrayList<ArrayList> map3 = (ArrayList<ArrayList>) map2.get("coordinates");
			ArrayList<ArrayList<Long>> map4 = (ArrayList<ArrayList<Long>>) map3.get(0);
			map.put(id, map4);
		}
		return map;
	}

	public static void detect(HashMap<String, ArrayList<ArrayList<Long>>> map) {
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		Long i = 1L;
		try {
			for (String location : locationlist) {
				ArrayList<String> alocations = new ArrayList<String>();
				for (ArrayList<Long> point : map.get(location)) {
					for (String alocation : locationlist) {
		 				boolean detecto = true;
		 				if (alocations.contains(alocation)) {
		 					detecto = false;
		 				}
						if (map.get(alocation).contains(point) && alocation != location && detecto) {
							alocations.add(alocation);
							SeoulLocRelation slr = SeoulLocRelation.builder().index(i).plocation(location).alocation(alocation).build();
							em.persist(slr);
							i++;
							break;
						}
					}
				}
				log.warn(location + " 체크 기록");
			}
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		}finally {
			em.close();
		}
	}
}

/*
create table loctime as select 확진일, sum(decode(지역, '종로구',1,0)) 종로구,
sum(decode(지역, '중구',1,0)) 중구, sum(decode(지역, '용산구',1,0)) 용산구, sum(decode(지역,
'성동구',1,0)) 성동구, sum(decode(지역, '광진구',1,0)) 광진구, sum(decode(지역, '동대문구',1,0))
동대문구, sum(decode(지역, '중랑구',1,0)) 중랑구, sum(decode(지역, '성북구',1,0)) 성북구,
sum(decode(지역, '강북구',1,0)) 강북구, sum(decode(지역, '도봉구',1,0)) 도봉구,
sum(decode(지역, '노원구',1,0)) 노원구, sum(decode(지역, '은평구',1,0)) 은평구,
sum(decode(지역, '서대문구',1,0)) 서대문구, sum(decode(지역, '마포구',1,0)) 마포구,
sum(decode(지역, '강서구',1,0)) 강서구, sum(decode(지역, '구로구',1,0)) 구로구,
sum(decode(지역, '양천구',1,0)) 양천구, sum(decode(지역, '금천구',1,0)) 금천구,
sum(decode(지역, '영등포구',1,0)) 영등포구, sum(decode(지역, '동작구',1,0)) 동작구,
sum(decode(지역, '관악구',1,0)) 관악구, sum(decode(지역, '서초구',1,0)) 서초구,
sum(decode(지역, '강남구',1,0)) 강남구, sum(decode(지역, '송파구',1,0)) 송파구,
sum(decode(지역, '강동구',1,0)) 강동구 from seoulcovid group by 확진일 order by 확진일 asc;
 * 
 * commit;
 */
